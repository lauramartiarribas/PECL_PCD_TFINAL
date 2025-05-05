package com.example.pecl_pcd_final;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Zombie extends Ser {

    private Logger logger= LoggerConFichero.getLogger();
    private int numMuertes;
    private Lock cerrojoAtaque= new ReentrantLock();

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
                ListaHilos zombiesZona = getEntorno().getZona_riesgoZombie().get(numZonaRiesgoZombie);

                getEntorno().getZona_riesgoZombie().get(numZonaRiesgoZombie).meter(this);

                //Busca humano para atacar
                Humano objetivo= getEntorno().elegirHumano(numZonaRiesgoZombie);
                if(objetivo!=null){
                    getEntorno().atacar(objetivo,numZonaRiesgoZombie,this);
                }


                //
                getEntorno().comprobarPausa();
                sleep(2000+ (int) Math.random()*1000);
                getEntorno().comprobarPausa();

                logger.info("Zombie "+this.getIdentificador()+ " saliendo de la zona de riesgo "+numZonaRiesgoZombie );
                getEntorno().getZona_riesgoZombie().get(numZonaRiesgoZombie).sacar(this);

            }


        }catch (Exception e){}

    }

//    public void atacar2(Humano humano, int numZona) throws InterruptedException {
//        cerrojoAtaque.lock();
//        try {
//            // Verificar si el humano está en una zona no atacable
//            if (humano.estaEnTunel() || humano.isEstaMuerto()) {
//                logger.info("El zombie " + this.getIdentificador() + " no puede atacar al humano " + humano.getIdentificador() + " porque está en un túnel o ya está muerto.");
//                return;
//            }
//
//            int tiempoAtaque = 500 + (int)(Math.random() * 1000);
//            logger.info("Zombie " + this.getIdentificador() + " atacando al humano " + humano.getIdentificador());
//
//            getEntorno().comprobarPausa();
//            humano.sleep(tiempoAtaque);
//            getEntorno().comprobarPausa();
//
//            this.sleep(tiempoAtaque);
//            getEntorno().comprobarPausa();
//
//            int probGanaHumano = (int)(Math.random() * 3); // 0,1,2
//
//            if (probGanaHumano <= 1) {
//                logger.info("El humano " + humano.getIdentificador() + " ha salido victorioso y queda marcado");
//                humano.setMarcado(true);
//                humano.setNumComida(0);
//            } else {
//                logger.info("Convirtiendo humano " + humano.getIdentificador() + " a zombie");
//                humano.setEstaMuerto(true); // marcarlo como muerto
//                this.numMuertes++;
//                logger.info("El zombie "+ this.getIdentificador() + " ha convertido a " + humano.getIdentificador());
//
//                synchronized (getEntorno().getZonaRiesgoH(numZona)) {
//                    getEntorno().getZonaRiesgoH(numZona).sacar(humano);
//                }
//
//                getEntorno().comprobarPausa();
//                humano.interrupt(); // esto debería detener el hilo humano
//                getEntorno().comprobarPausa();
//
//                // Crear el nuevo zombie solo si el humano fue correctamente marcado como muerto
//                Zombie zombie = new Zombie("Z" + humano.getIdentificador().substring(1), getEntorno());
//                zombie.start();
//            }
//
//        } catch (Exception e) {
//            logger.warning("Error en ataque: " + e.getMessage());
//            Thread.currentThread().interrupt();
//        } finally {
//            cerrojoAtaque.unlock();
//        }
//    }

//    public synchronized Humano elegirHumano(ListaHilos humanosZona, int numZonaRiesgoZombie ){
//        synchronized (humanosZona) {
//            int numHumanos = humanosZona.getLista().size();
//
//            if (numHumanos > 0) {
//                Ser objetivo;
//                if (numHumanos == 1) {
//
//                    objetivo = humanosZona.getLista().get(0);
//                } else {
//                    int humanoAtacar = (int)(Math.random() * numHumanos);
//                    objetivo = humanosZona.getLista().get(humanoAtacar);
//                }
//
//                if (objetivo != null) {
//                    logger.info("Atacando a humano " + objetivo.getIdentificador() + " en zona " + numZonaRiesgoZombie);
//                    atacar2((Humano) objetivo, numZonaRiesgoZombie);
//                }
//            }
//        }
//    }


    public int getNumMuertes() {
        return numMuertes;
    }

    public void setNumMuertes(int numMuertes) {
        this.numMuertes = numMuertes;
    }
}









