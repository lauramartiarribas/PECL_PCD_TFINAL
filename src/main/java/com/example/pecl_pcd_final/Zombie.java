package com.example.pecl_pcd_final;

import java.util.ArrayList;
import java.util.logging.Logger;

//public class Zombie extends Ser {
//
//    private Logger logger= LoggerConFichero.getLogger();
//    private int numMuertes;
//
//    public Zombie (String id, Entorno entorno){
//        this.setIdentificador(id);
//        this.setEntorno(entorno);
//        this.numMuertes=0;
//    }
//
//
//
//    @Override
//    public void run(){
//        try {
//            logger.info("Empezando zombie" + getIdentificador());
//
//            while (true){
//                //Elige zona
//                logger.info("En la zona de riesgo de zombie " + getIdentificador());
//                int numZonaRiesgoZombie= (int)(Math.random()*4);
//                ArrayList<Ser> humanosZona = getEntorno().getZona_riesgoHumanos().get(numZonaRiesgoZombie);
//                ArrayList<Ser> zombiesZona = getEntorno().getZona_riesgoZombie().get(numZonaRiesgoZombie);
//                synchronized (zombiesZona) {
//                    getEntorno().meter(this, getEntorno().ZonaRiesgoZombies.get(numZonaRiesgoZombie), zombiesZona);
//                }
//                //Busca humano para atacar
//                synchronized (humanosZona) {
//                    int numHumanos = humanosZona.size();
//
//                    if (numHumanos > 0) {
//                        Ser objetivo;
//                        if (numHumanos == 1) {
//                            objetivo = humanosZona.get(0);
//                        } else {
//                            int humanoAtacar = (int)(Math.random() * numHumanos);
//                            objetivo = humanosZona.get(humanoAtacar);
//                        }
//
//                        if (objetivo != null) {
//                            logger.info("Atacando a humano " + objetivo.getIdentificador() + " en zona " + numZonaRiesgoZombie);
//                            atacar((Humano) objetivo, numZonaRiesgoZombie);
//                        }
//                    }
//                }
//                getEntorno().comprobarPausa();
//                sleep(2000+ (int) Math.random()*1000);
//                getEntorno().comprobarPausa();
//                synchronized (zombiesZona) {
//                    getEntorno().sacar(this, getEntorno().ZonaRiesgoZombies.get(numZonaRiesgoZombie), zombiesZona);
//                }
//            }
//
//
//        }catch (Exception e){}
//
//    }
//
//    public void atacar(Humano humano, int numZona) {
//        int probGanaHumano = (int)(Math.random() * 3); // 0,1,2
//
//        if (probGanaHumano <= 1) {
//            logger.info("El humano " + humano.getIdentificador() + " ha salido victorioso y queda marcado");
//            humano.setMarcado(true);
//            humano.setNumComida(0);
//            humano.volver(numZona, getEntorno().getTunelesInteriorLock().get(numZona));
//        } else {
//            logger.info("Convirtiendo humano " + humano.getIdentificador() + " a zombie");
//            this.numMuertes++;
//            logger.info("El zombie "+ this.getIdentificador()+" ha convertido a "+ this.numMuertes);
//            Zombie zombie = new Zombie("Z" + humano.getIdentificador().substring(1), getEntorno());
//            zombie.start();
//
//            synchronized (getEntorno().getZona_riesgoHumanos().get(numZona)) {
//                getEntorno().getZona_riesgoHumanos().get(numZona).remove(humano);
//            }
//
//            synchronized (getEntorno().getHumanos()) {
//                getEntorno().getHumanos().remove(humano);
//            }
//        }
//
//        try {
//            getEntorno().comprobarPausa();
//            Thread.sleep(500 + (int)(Math.random() * 1000));
//            getEntorno().comprobarPausa();
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt(); // buena práctica
//        }
//    }
//
//}
