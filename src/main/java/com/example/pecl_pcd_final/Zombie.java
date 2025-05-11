package com.example.pecl_pcd_final;

import java.util.logging.Logger;

public class Zombie extends Ser {

    private Logger logger= LoggerConFichero.getLogger();
    private int numMuertes;

    public Zombie (String id, Entorno entorno){
        this.setIdentificador(id);
        this.setEntorno(entorno);
        this.numMuertes=0;
        this.getEntorno().getZombiesTotales().add(this);
    }

    @Override
    public void run(){
        while(true) {
            try {
                //Elige zona
                logger.info("En la zona de riesgo de zombie " + getIdentificador());
                int numZonaRiesgoZombie = (int) (Math.random() * 4);

                getEntorno().getZona_riesgoZombie().get(numZonaRiesgoZombie).meter(this);

                getEntorno().getZonaRiesgoH(numZonaRiesgoZombie).atacar(numZonaRiesgoZombie, this);
                dormir(2000 + (int) Math.random() * 1000);

                getEntorno().getZona_riesgoZombie().get(numZonaRiesgoZombie).sacar(this);
            } catch (Exception e) {
                logger.warning("Se ha producido un error en el zombie: "+getIdentificador());
            }
        }
    }

    public int getNumMuertes() {
        return numMuertes;
    }

    public void setNumMuertes(int numMuertes) {
        this.numMuertes = numMuertes;
    }
}









