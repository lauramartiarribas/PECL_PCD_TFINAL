package com.example.pecl_pcd_final;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.logging.Logger;

public class Zombie extends Ser {

    private Logger logger= LoggerConFichero.getLogger();
    private int numMuertes;

    public Zombie (String id, Entorno entorno){
        this.setIdentificador(id);
        this.setEntorno(entorno);
        this.numMuertes=0;
    }
    @Override
    public void run(){
        try {
            logger.info("Empezando zombie" + getIdentificador());

            while (true){
                //Elige zona
                logger.info("En la zona de riesgo de zombie " + getIdentificador());
                int numZonaRiesgoZombie= (int)(Math.random()*4);
                ListaHilos humanosZona = getEntorno().getZona_riesgoHumanos().get(numZonaRiesgoZombie);
                ListaHilos zombiesZona = getEntorno().getZona_riesgoZombie().get(numZonaRiesgoZombie);

                getEntorno().getZona_riesgoZombie().get(numZonaRiesgoZombie).meter(this);

                //Busca humano para atacar
                synchronized (humanosZona) {
                    int numHumanos = humanosZona.getLista().size();

                    if (numHumanos > 0) {
                        Ser objetivo;
                        if (numHumanos == 1) {

                            objetivo = humanosZona.getLista().get(0);
                        } else {
                            int humanoAtacar = (int)(Math.random() * numHumanos);
                            objetivo = humanosZona.getLista().get(humanoAtacar);
                        }

                        if (objetivo != null) {
                            logger.info("Atacando a humano " + objetivo.getIdentificador() + " en zona " + numZonaRiesgoZombie);
                            atacar((Humano) objetivo, numZonaRiesgoZombie);
                        }
                    }
                }
                getEntorno().comprobarPausa();
                sleep(2000+ (int) Math.random()*1000);
                getEntorno().comprobarPausa();
                synchronized (zombiesZona) {
                    logger.info("Zombie "+this.getIdentificador()+ " saliendo de la zona de riesgo "+numZonaRiesgoZombie );

                    getEntorno().getZona_riesgoZombie().get(numZonaRiesgoZombie).sacar(this);
                }
            }


        }catch (Exception e){}

    }
    public void atacar(Humano humano, int numZona) throws InterruptedException {

        int tiempoAtaque=500+ (int) Math.random()*1000;
        System.out.println("interrumpido en el ataque");

        getEntorno().comprobarPausa();
        humano.sleep(tiempoAtaque);
        getEntorno().comprobarPausa();

        getEntorno().comprobarPausa();
        this.sleep(tiempoAtaque);
        getEntorno().comprobarPausa();

        int probGanaHumano = (int)(Math.random() * 3); // 0,1,2

        if (probGanaHumano <= 1) {
            logger.info("El humano " + humano.getIdentificador() + " ha salido victorioso y queda marcado");
            humano.setMarcado(true);
            humano.setNumComida(0);
            humano.getEntorno().getListaTuneles().get(numZona).volverAlRefugio(humano,numZona);
        } else {
            logger.info("Convirtiendo humano " + humano.getIdentificador() + " a zombie");
            humano.setEstaMuerto(true);
            this.numMuertes++;
            logger.info("El zombie "+ this.getIdentificador()+" ha convertido a "+ this.numMuertes);
            Zombie zombie = new Zombie("Z" + humano.getIdentificador().substring(1), getEntorno());
            humano.getEntorno().getZona_riesgoHumanos().get(numZona).sacar(humano);
            humano.interrupt();
            zombie.start();







        }

        try {
            getEntorno().comprobarPausa();
            Thread.sleep(500 + (int)(Math.random() * 1000));
            getEntorno().comprobarPausa();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

}









