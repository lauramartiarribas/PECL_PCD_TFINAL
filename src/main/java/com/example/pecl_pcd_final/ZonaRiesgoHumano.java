package com.example.pecl_pcd_final;

import java.util.ArrayList;
import java.util.logging.Logger;

import static java.lang.Thread.currentThread;

public class ZonaRiesgoHumano {

    private Logger logger = LoggerConFichero.getLogger();
    private ListaHilos humanos;
    private Entorno entorno;
    public ZonaRiesgoHumano(ListaHilos humanos, Entorno entorno) {
        this.humanos = humanos;
        this.entorno=entorno;
    }
    public ListaHilos getHumanos() {
        return humanos;
    }
    public void meterHumano(Humano h) {
        humanos.meter(h);
    }

    public void sacarHumano(Humano h) {
        humanos.sacar(h);
    }
    public Humano elegirObjetivoAleatorio() {
        ArrayList<Ser> lista = humanos.getLista();
        if (lista.isEmpty()) return null;

        int index = (lista.size() == 1) ? 0 : (int) (Math.random() * lista.size());
        return (Humano) lista.get(index);
    }
    public int cantidadHumanos() {
        return humanos.getLista().size();
    }
    public synchronized void atacar(Humano humano, int numZona, Zombie zombie) {
        try {
            int tiempoAtaque = 500 + (int) (Math.random() * 1000);

            humano.getCerrojoDefendiendose().lock();
            try {

                humano.setDefendiendose(true);
                zombie.sleep(tiempoAtaque);

                int probGanaHumano = (int) (Math.random() * 3);
                if (probGanaHumano <= 1) {
                    logger.info("El humano " + humano.getIdentificador() + " ha salido victorioso.");
                    humano.setMarcado(true);
                    humano.setNumComida(0);
                    entorno.getListaTuneles().get(numZona).volverAlRefugio(humano, numZona);


                } else {
                    logger.info("El zombie " + zombie.getIdentificador() + " ha convertido al humano " + humano.getIdentificador());

                    humano.setEstaMuerto(true);
                    humano.interrupt();
                    entorno.getZonaRiesgoH(numZona).sacarHumano(humano);
                    entorno.comprobarPausa();

                    Zombie nuevo = new Zombie("Z" + humano.getIdentificador().substring(1), entorno);
                    nuevo.start();

                    zombie.setNumMuertes(zombie.getNumMuertes() + 1);
                }

            } catch (InterruptedException e) {
                currentThread().interrupt();
            } finally {
                humano.setDefendiendose(false);
                humano.getDefendiendoseCondicion().signalAll();
                humano.getCerrojoDefendiendose().unlock();
            }
        } catch (Exception e) {

        }
    }

}
