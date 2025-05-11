package com.example.pecl_pcd_final;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import static java.lang.Thread.currentThread;

public class ZonaRiesgoHumano {

    private Logger logger = LoggerConFichero.getLogger();
    private ListaHilos humanos;
    private ArrayList<Humano> humanosDisponibles;
    private Entorno entorno;
    private Lock cerrojoAtaque;


    public ZonaRiesgoHumano(ListaHilos humanos, Entorno entorno) {
        this.humanos = humanos;
        this.entorno = entorno;
        cerrojoAtaque = new ReentrantLock();
        humanosDisponibles= new ArrayList<>();
    }

//    public void meterHumano(Humano h) {
//        humanosDisponibles.add(h);
//        humanos.meter(h);
//    }
//
//    public void sacarHumano(Humano h) {
//        humanosDisponibles.remove(h);
//        humanos.sacar(h);
//    }


    public Humano elegirObjetivoAleatorio() {
        cerrojoAtaque.lock();
        Humano h = null;
        try {
            if (humanosDisponibles.isEmpty()) {
                return h;
            } else {
                int n = (int) (Math.random() * humanosDisponibles.size());
                h = humanosDisponibles.get(n);
                humanosDisponibles.remove(h);
                h.setDefendiendose(true);
                h.setMarcado(true);
            }
        } catch (Exception e) {
        } finally {
            cerrojoAtaque.unlock();
            return h;
        }
    }


    public void atacar(int numZona, Zombie zombie) {
        try {
            int tiempoAtaque = 500 + (int) (Math.random() * 1000);
            Humano humano = elegirObjetivoAleatorio();
            if(humano!=null) {

                humano.interrupt();
                zombie.dormir(tiempoAtaque);

                int probGanaHumano = (int) (Math.random() * 3);
                if (probGanaHumano <= 1) {
                    logger.info("El humano " + humano.getIdentificador() + " ha salido victorioso.");
                    humano.setNumComida(0);


                } else {
                    logger.info("El zombie " + zombie.getIdentificador() + " ha convertido al humano " + humano.getIdentificador());
                    humano.matar(numZona);
                    humano.setEstaMuerto(true);



                    entorno.comprobarPausa();

                    Zombie nuevo = new Zombie("Z" + humano.getIdentificador().substring(1), entorno);
                    nuevo.start();

                    zombie.setNumMuertes(zombie.getNumMuertes() + 1);
                }
                entorno.getZonaRiesgoH(numZona).getHumanos().sacar(humano);
                humano.setDefendiendose(false);
                synchronized ((humano)) {
                    humano.notify();
                }
            }
        } catch (Exception e) {}
    }

    public ListaHilos getHumanos() {
        return humanos;
    }

    public ArrayList<Humano> getHumanosDisponibles() {
        return humanosDisponibles;
    }
}
