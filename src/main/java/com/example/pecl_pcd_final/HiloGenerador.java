package com.example.pecl_pcd_final;

import java.util.logging.Logger;

public class HiloGenerador extends Thread {

    private Entorno entorno;
    private Logger logger = LoggerConFichero.getLogger();
    private int numHumanosAGenerar;

    public HiloGenerador(Entorno entorno, int numHumanosAGenerar) {
        this.entorno = entorno;
        this.numHumanosAGenerar = numHumanosAGenerar;
    }

    @Override
    public void run() {
        //Creamos el zombie
        Zombie zombie = new Zombie("Z0000", entorno);
        logger.info("Se ha creado el zombie Z0000");
        zombie.start();

        //Creamos los humanos
        for (int i = 0; i < numHumanosAGenerar; i++) {
            try {
                entorno.comprobarPausa();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Humano humano = new Humano("H" + String.format("%04d", entorno.getNumHumanos()), entorno);
            entorno.setNumHumanos(entorno.getNumHumanos()+1);
            logger.info("Nace el humano H"+ String.format("%04d", entorno.getNumHumanos()));
            humano.start();
            int n=500+(int)Math.random()*1500;

            try {
                Thread.sleep(n); //Esperar antes del siguiente humano
            } catch (InterruptedException e) {
                System.out.println("Generador interrumpido");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}


