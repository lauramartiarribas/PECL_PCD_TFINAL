package com.example.pecl_pcd_final;

public class HiloGenerador extends Thread {

    private Entorno entorno;
    private int numHumanosAGenerar;

    public HiloGenerador(Entorno entorno, int numHumanosAGenerar) {
        this.entorno = entorno;
        this.numHumanosAGenerar = numHumanosAGenerar;
    }

    @Override
    public void run() {
        //Creamos el zombie
        Zombie zombie = new Zombie("Z0000", entorno);
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


