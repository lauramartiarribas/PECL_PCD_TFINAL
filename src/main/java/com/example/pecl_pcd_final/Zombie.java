package com.example.pecl_pcd_final;

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
                entorno.comprobarPausa();
                //Elige zona
                logger.info("En la zona de riesgo de zombie " + identificador);
                int numZonaRiesgoZombie= (int)(Math.random()*4);
                entorno.meter(this, entorno.ZonaRiesgoZombies.get(numZonaRiesgoZombie), entorno.zona_riesgoZombie.get(numZonaRiesgoZombie));

                //Busca humano para atacar

                if(entorno.zona_riesgoHumanos.get(numZonaRiesgoZombie).size()==1){
                    atacar((Humano) entorno.zona_riesgoHumanos.get(numZonaRiesgoZombie).get(0),numZonaRiesgoZombie);
                }
                else if(entorno.zona_riesgoHumanos.get(numZonaRiesgoZombie).size()>1){
                    int humanoAtacar= (int)(Math.random()*entorno.zona_riesgoHumanos.size());
                    atacar((Humano) entorno.zona_riesgoHumanos.get(numZonaRiesgoZombie).get(humanoAtacar),numZonaRiesgoZombie);

                }
                entorno.comprobarPausa();
                sleep(2000+ (int) Math.random()*1000);
                entorno.comprobarPausa();
                entorno.sacar(this, entorno.ZonaRiesgoZombies.get(numZonaRiesgoZombie), entorno.zona_riesgoZombie.get(numZonaRiesgoZombie));

            }


        }catch (Exception e){}

    }

    public void atacar(Humano humano,int numTunel){
        int probGanaHumano= (int)(Math.random()*3);
        if(probGanaHumano<=1){
            logger.info("El humano "+humano.identificador+ " ha salido victorioso y queda marcado");
            humano.marcado=true;
            humano.numComida=0;
            humano.volver(numTunel,entorno.tunelesInteriorLock.get(numTunel));
        }
        else{
            logger.info("Convirtiendo humano "+humano.identificador+ " a zombie" );
            Zombie zombie = new Zombie("Z"+ humano.identificador.substring(1), entorno);
            zombie.start();
            entorno.zona_riesgoHumanos.get(numTunel).remove(humano);
            entorno.humanos.remove(humano);
        }
        try {
            entorno.comprobarPausa();
            sleep(500+(int) Math.random()*1000);
            entorno.comprobarPausa();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
