package com.example.pecl_pcd_final;


import javafx.application.Platform;

import java.awt.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;
import java.util.logging.Logger;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;


public class Entorno {

    ///El logger///
    private Logger logger = LoggerConFichero.getLogger();

    ///Elementos del juego ///
    //Para cuando queramos pausar el juego y así poder también almacenar todos los hilos que se tienen en ese momento
    private boolean enPausa = false;

    private int numHumanos;


    /// Refugio ///
    private ListaHilos descanso;
    private ListaHilos comedor_espera;
    private ListaHilos comedor_comiendo;
    private ListaHilos zona_comun;


    private Lock cerrojoComida;
    private Condition hayComida;
    private ConcurrentLinkedQueue<Integer> comidaTotal;
    private ConcurrentLinkedQueue<Humano> colaComedor;

    private javafx.scene.control.TextField labelComida;


    /// Túneles ///
    private ArrayList<Tunel> listaTuneles = new ArrayList<>();


    private ArrayList<ListaHilos> listaTunelesSalir = new ArrayList<>();

    private ArrayList<ListaHilos> listaTunelesIntermedio = new ArrayList<>();

    private ArrayList<ListaHilos> listaTunelesEntrar = new ArrayList<>();


    /// Zona de riesgo ///
    private ArrayList<ListaHilos> zona_riesgoHumanos = new ArrayList<>();

    private ArrayList<ListaHilos> zona_riesgoZombie = new ArrayList<>();


    public Entorno(ListaHilos descanso, ListaHilos comedor_espera, ListaHilos comedor_comiendo, ListaHilos zona_comun,
                   ListaHilos tunelSalir1, ListaHilos tunelSalir2, ListaHilos tunelSalir3, ListaHilos tunelSalir4,
                   ListaHilos tunelIntermedio1, ListaHilos tunelIntermedio2, ListaHilos tunelIntermedio3, ListaHilos tunelIntermedio4,
                   ListaHilos tunelEntrar1, ListaHilos tunelEntrar2, ListaHilos tunelEntrar3, ListaHilos tunelEntrar4,
                   ListaHilos zona_riesgoHumano1, ListaHilos zona_riesgoHumano2, ListaHilos zona_riesgoHumano3, ListaHilos zona_riesgoHumano4,
                   ListaHilos zona_riesgoZombie1, ListaHilos zona_riesgoZombie2, ListaHilos zona_riesgoZombie3, ListaHilos zona_riesgoZombie4,

                   javafx.scene.control.TextField labelComida) throws IOException {

        this.numHumanos = 1;
        this.descanso = descanso;
        this.comedor_espera = comedor_espera;
        this.comedor_comiendo = comedor_comiendo;
        this.zona_comun = zona_comun;


        for (int i = 0; i < 4; i++) {
            this.listaTuneles.add(new Tunel(this));
        }


        this.labelComida = labelComida;
        this.cerrojoComida = new ReentrantLock();
        this.hayComida = cerrojoComida.newCondition();
        this.comidaTotal = new ConcurrentLinkedQueue<>();
        this.colaComedor = new ConcurrentLinkedQueue<>();


        listaTunelesSalir.add(tunelSalir1);
        listaTunelesSalir.add(tunelSalir2);
        listaTunelesSalir.add(tunelSalir3);
        listaTunelesSalir.add(tunelSalir4);

        listaTunelesIntermedio.add(tunelIntermedio1);
        listaTunelesIntermedio.add(tunelIntermedio2);
        listaTunelesIntermedio.add(tunelIntermedio3);
        listaTunelesIntermedio.add(tunelIntermedio4);

        listaTunelesEntrar.add(tunelEntrar1);
        listaTunelesEntrar.add(tunelEntrar2);
        listaTunelesEntrar.add(tunelEntrar3);
        listaTunelesEntrar.add(tunelEntrar4);

        zona_riesgoHumanos.add(zona_riesgoHumano1);
        zona_riesgoHumanos.add(zona_riesgoHumano2);
        zona_riesgoHumanos.add(zona_riesgoHumano3);
        zona_riesgoHumanos.add(zona_riesgoHumano4);

        zona_riesgoZombie.add(zona_riesgoZombie1);
        zona_riesgoZombie.add(zona_riesgoZombie2);
        zona_riesgoZombie.add(zona_riesgoZombie3);
        zona_riesgoZombie.add(zona_riesgoZombie4);


    }


    public synchronized void pausar() {
        enPausa = true;
    }

    public synchronized void comprobarPausa() throws InterruptedException {
        while (enPausa) {
            wait();
        }
    }

    public synchronized void reanudar() {
        enPausa = false; // Cambia el estado a reanudado
        notifyAll();
    }


    public void nacerHumanos() {
        new Thread(() -> {

            for (int i = 0; i < 9998; i++) { //9999

                Humano humano = new Humano("H" + String.format("%04d", numHumanos), this);
                numHumanos++;

                humano.start();
                try {
                    this.comprobarPausa();
                    sleep(500 + (int) Math.random() * 1500);
                    this.comprobarPausa();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();


    }


    public void comer(Humano humano) {

        try {
            comedor_espera.meter(humano);
            colaComedor.offer(humano);
            // Mientras no haya comida, espera a que se notifique
            cerrojoComida.lock();
            try {
                while (getComidaTotal().size() == 0) {
                    hayComida.await(); // El hilo se suspende hasta que haya comida y se llame a notify()
                }
            } finally {
                cerrojoComida.unlock();
            }
            comedor_espera.sacar(humano);

            comedor_comiendo.meter(humano);
            colaComedor.poll();

            comidaTotal.poll();
            actualizarLabelComida();
            comprobarPausa();
            sleep(3000 + (int) Math.random() * 2000);
            comprobarPausa();

            comedor_comiendo.sacar(humano);


            cerrojoComida.lock();
            try {
                if (!comidaTotal.isEmpty()) {
                    hayComida.signal(); // signalAll?????
                }
            } finally {
                cerrojoComida.unlock();
            }


        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }

    }


    public synchronized void atacar(Humano humano, int numZona, Zombie zombie) {
        try {
            int tiempoAtaque = 500 + (int) (Math.random() * 1000);

            humano.getCerrojoDefendiendose().lock();
            try {

                humano.setDefendiendose(true);
                humano.sleep(tiempoAtaque);
                zombie.sleep(tiempoAtaque);

                int probGanaHumano = (int) (Math.random() * 3);
                if (probGanaHumano <= 1) {
                    logger.info("El humano " + humano.getIdentificador() + " ha salido victorioso.");
                    humano.setMarcado(true);
                    humano.setNumComida(0);
                } else {
                    logger.info("El zombie " + zombie.getIdentificador() + " ha convertido al humano " + humano.getIdentificador());

                    humano.setEstaMuerto(true);
                    humano.interrupt();
                    getZonaRiesgoH(numZona).sacar(humano);
                    comprobarPausa();

                    Zombie nuevo = new Zombie("Z" + humano.getIdentificador().substring(1), this);
                    nuevo.start();

                    zombie.setNumMuertes(zombie.getNumMuertes() + 1);
                }

            } catch (InterruptedException e) {
                logger.warning("Ataque interrumpido");
                Thread.currentThread().interrupt();
            } finally {
                humano.setDefendiendose(false);
                humano.getDefendiendoseCondicion().signalAll();
                humano.getCerrojoDefendiendose().unlock();
            }
        } catch (Exception e) {
            logger.warning("Excepción durante ataque: " + e.getMessage());
        }
    }


    public synchronized Humano elegirHumano(int numZona) {
        int numHumanos = zona_riesgoHumanos.get(numZona).getLista().size();
        Humano objetivo = null;
        if (numHumanos > 0) {

            if (numHumanos == 1) {

                objetivo = (Humano) zona_riesgoHumanos.get(numZona).getLista().get(0);
            } else {
                int humanoAtacar = (int) (Math.random() * numHumanos);
                objetivo = (Humano) zona_riesgoHumanos.get(numZona).getLista().get(humanoAtacar);
            }

        }
        return objetivo;
    }


    //Getters y setters
    public ListaHilos getDescanso() {
        return descanso;
    }

    public ListaHilos getComedor_espera() {
        return comedor_espera;
    }

    public ListaHilos getComedor_comiendo() {
        return comedor_comiendo;
    }

    public ListaHilos getZona_comun() {
        return zona_comun;
    }


    public ConcurrentLinkedQueue<Integer> getComidaTotal() {
        return comidaTotal;
    }


    public void actualizarLabelComida() {
        Platform.runLater(() -> {
            labelComida.setText(String.valueOf(this.getComidaTotal().size()));
        });
    }


    public ArrayList<ListaHilos> getZona_riesgoHumanos() {
        return zona_riesgoHumanos;
    }

    public ArrayList<ListaHilos> getListaTunelesSalir() {
        return listaTunelesSalir;
    }

    public ArrayList<ListaHilos> getListaTunelesIntermedio() {
        return listaTunelesIntermedio;
    }

    public ArrayList<ListaHilos> getListaTunelesEntrar() {
        return listaTunelesEntrar;
    }

    public ArrayList<ListaHilos> getZona_riesgoZombie() {
        return zona_riesgoZombie;
    }


    public ArrayList<Tunel> getListaTuneles() {
        return listaTuneles;
    }


    public ListaHilos getTunelSalir(int numTunel) {
        return listaTunelesSalir.get(numTunel);
    }

    public ListaHilos getTunelIntermedio(int numTunel) {
        return listaTunelesIntermedio.get(numTunel);
    }

    public ListaHilos getTunelEntrar(int numTunel) {
        return listaTunelesEntrar.get(numTunel);
    }


    public ListaHilos getZonaRiesgoH(int num) {
        return zona_riesgoHumanos.get(num);
    }


}
