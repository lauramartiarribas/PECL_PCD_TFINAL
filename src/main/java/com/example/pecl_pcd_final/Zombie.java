package com.example.pecl_pcd_final;

import com.example.pecl_pcd_final.Entorno;
import com.example.pecl_pcd_final.Ser;

public class Zombie extends Ser {

    public Zombie (String id, Entorno entorno){
        this.identificador=id;
        this.entorno=entorno;
    }



    @Override
    public void run(){
        try {
            System.out.println("Empezando zombie" + identificador);

            while (true){
                //Elige zona
                System.out.println("En la zona de riesgo de zombie " + identificador);
                int numZonaRiesgoZombie= (int)(Math.random()*3);
                entorno.meter(this, entorno.ZonaRiesgoZombies.get(numZonaRiesgoZombie), entorno.zona_riesgoZombie.get(numZonaRiesgoZombie));

                //Busca humano para atacar

                if(entorno.zona_riesgoHumanos.get(numZonaRiesgoZombie).size()==1){
                    atacar((Humano) entorno.zona_riesgoHumanos.get(numZonaRiesgoZombie).get(0),numZonaRiesgoZombie);
                }
                else{
                    int humanoAtacar= (int)(Math.random()*entorno.zona_riesgoHumanos.size());
                    atacar((Humano) entorno.zona_riesgoHumanos.get(numZonaRiesgoZombie).get(humanoAtacar),numZonaRiesgoZombie);

                }
                sleep(2000+ (int) Math.random()*1000);

            }


        }catch (Exception e){}

    }

    public void atacar(Humano humano,int numTunel){
        int probGanaHumano= (int)(Math.random()*2);
        if(probGanaHumano<=1){
            humano.marcado=true;
            humano.numComida=0;
            humano.volverAtaque(numTunel);
        }
        else{
            Zombie zombie = new Zombie("Z"+ humano.identificador.substring(1), entorno);
            zombie.start();
        }
        try {
            sleep(500+(int) Math.random()*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
