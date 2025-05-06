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

    public int getNumMuertes() {
        return numMuertes;
    }

    public void setNumMuertes(int numMuertes) {
        this.numMuertes = numMuertes;
    }
}









