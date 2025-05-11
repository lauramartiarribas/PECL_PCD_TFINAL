package com.example.pecl_pcd_final;


import javafx.application.Platform;

import java.awt.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;
import java.util.logging.Logger;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;


public class Entorno {

    //El logger
    private Logger logger = LoggerConFichero.getLogger();

    //Elementos del juego
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


    // Túneles
    private ArrayList<Tunel> listaTuneles = new ArrayList<>();

    private ArrayList<ListaHilos> listaTunelesSalir = new ArrayList<>();
    private ArrayList<ListaHilos> listaTunelesIntermedio = new ArrayList<>();
    private ArrayList<ListaHilos> listaTunelesEntrar = new ArrayList<>();


    // Zona de riesgo
    private ArrayList<ZonaRiesgoHumano> zona_riesgoHumanos = new ArrayList<>();
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

        zona_riesgoHumanos.add(new ZonaRiesgoHumano(zona_riesgoHumano1,this));
        zona_riesgoHumanos.add(new ZonaRiesgoHumano(zona_riesgoHumano2,this));
        zona_riesgoHumanos.add(new ZonaRiesgoHumano(zona_riesgoHumano3,this));
        zona_riesgoHumanos.add(new ZonaRiesgoHumano(zona_riesgoHumano4,this));


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


    public void comer(Humano humano) {

        try {
            comedor_espera.meter(humano);
            colaComedor.offer(humano);
            // Mientras no haya comida, espera a que se notifique
            cerrojoComida.lock();
            try {
                while (comidaTotal.isEmpty()) {
                    try {
                        hayComida.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
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

        } catch (Exception e) {
            currentThread().interrupt();
        }

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


    public void actualizarLabelComida() {
        Platform.runLater(() -> labelComida.setText(String.valueOf(comidaTotal.size())));
    }
    public void actualizarComida(int numComida){
        for (int i = 0; i < numComida; i++) {
            comidaTotal.offer(1);
        }
        cerrojoComida.lock();
        try {
            hayComida.signalAll(); // Notifica a todos los que esperan por comida
        } finally {
            cerrojoComida.unlock();
        }
        actualizarLabelComida();
    }



    public ArrayList<ZonaRiesgoHumano> getZona_riesgoHumanos() {
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


    public ZonaRiesgoHumano getZonaRiesgoH(int num) {
        return zona_riesgoHumanos.get(num);
    }

    public int getNumHumanos() {
        return numHumanos;
    }

    public void setNumHumanos(int numHumanos) {
        this.numHumanos = numHumanos;
    }
}
