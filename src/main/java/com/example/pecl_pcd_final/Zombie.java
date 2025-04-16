package com.example.pecl_pcd_final;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Zombie extends Ser {

    Logger logger= LoggerConFichero.getLogger();

    public Zombie (String id, Entorno entorno){
        this.identificador=id;
        this.entorno=entorno;
    }



    @Override
    public void run(){
        try {
            logger.info("Empezando zombie" + identificador);

            while (true){
                //Elige zona
                logger.info("En la zona de riesgo de zombie " + identificador);
                int numZonaRiesgoZombie= (int)(Math.random()*4);
                ArrayList<Ser> humanosZona = entorno.zona_riesgoHumanos.get(numZonaRiesgoZombie);
                ArrayList<Ser> zombiesZona = entorno.zona_riesgoZombie.get(numZonaRiesgoZombie);
                synchronized (zombiesZona) {
                    entorno.meter(this, entorno.ZonaRiesgoZombies.get(numZonaRiesgoZombie), zombiesZona);
                }
                //Busca humano para atacar
                synchronized (humanosZona) {
                    int numHumanos = humanosZona.size();

                    if (numHumanos > 0) {
                        Ser objetivo;
                        if (numHumanos == 1) {
                            objetivo = humanosZona.get(0);
                        } else {
                            int humanoAtacar = (int)(Math.random() * numHumanos);
                            objetivo = humanosZona.get(humanoAtacar);
                        }

                        if (objetivo != null) {
                            logger.info("Atacando a humano " + objetivo.identificador + " en zona " + numZonaRiesgoZombie);
                            atacar((Humano) objetivo, numZonaRiesgoZombie);
                        }
                    }
                }
                entorno.comprobarPausa();
                sleep(2000+ (int) Math.random()*1000);
                entorno.comprobarPausa();
                synchronized (zombiesZona) {
                    entorno.sacar(this, entorno.ZonaRiesgoZombies.get(numZonaRiesgoZombie), zombiesZona);
                }
            }


        }catch (Exception e){}

    }

    public void atacar(Humano humano, int numZona) {
        int probGanaHumano = (int)(Math.random() * 3); // 0,1,2

        if (probGanaHumano <= 1) {
            logger.info("El humano " + humano.identificador + " ha salido victorioso y queda marcado");
            humano.marcado = true;
            humano.numComida = 0;
            humano.volver(numZona, entorno.tunelesInteriorLock.get(numZona));
        } else {
            logger.info("Convirtiendo humano " + humano.identificador + " a zombie");
            Zombie zombie = new Zombie("Z" + humano.identificador.substring(1), entorno);
            zombie.start();

            synchronized (entorno.zona_riesgoHumanos.get(numZona)) {
                entorno.zona_riesgoHumanos.get(numZona).remove(humano);
            }

            synchronized (entorno.humanos) {
                entorno.humanos.remove(humano);
            }
        }

        try {
            entorno.comprobarPausa();
            Thread.sleep(500 + (int)(Math.random() * 1000));
            entorno.comprobarPausa();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // buena práctica
        }
    }

}
