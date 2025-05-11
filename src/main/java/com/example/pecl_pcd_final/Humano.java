package com.example.pecl_pcd_final;

import java.util.logging.Logger;

public class Humano extends Ser {

    private Logger logger = LoggerConFichero.getLogger();
    private int numComida;
    private boolean marcado;
    private boolean defendiendose;


    public Humano(String id, Entorno entorno) {
        this.setIdentificador(id);
        this.setEntorno(entorno);
        numComida = 0;
        marcado = false;
        defendiendose=false;
    }

    @Override
    public void run() {
        logger.info("Empezando " + getIdentificador());
        while(!isEstaMuerto()) {
            // Seleccionar túnel
            int tunelSalir = (int) (Math.random() * 4);
            try {
                if(!marcado) {
                    getEntorno().comprobarPausa();
                    // Zona común tiempo entre 1 y 2
                    logger.info("En la zona común " + getIdentificador());
                    getEntorno().getZona_comun().meter(this);
                    dormir(1000 + (int) (Math.random() * 1000));

                    // Salir por el túnel elegido
                    getEntorno().getListaTuneles().get(tunelSalir).salirDesdeRefugio(this, tunelSalir);

                    logger.info(getIdentificador() + " En la zona exterior");
                    dormir(3000 + (int) Math.random() * 2000);
                }
                esperarAtaque();
                if(isEstaMuerto()){
                    getEntorno().getZonaRiesgoH(tunelSalir).getHumanos().sacar(this);
                    return;
                }

                getEntorno().getListaTuneles().get(tunelSalir).volverAlRefugio(this, tunelSalir);

                getEntorno().actualizarComida(numComida);
                getEntorno().actualizarLabelComida();


                //En la zona de descanso
                dormir(2000 + (int) Math.random() * 2000);
                getEntorno().getDescanso().sacar(this);


                //Zona de comedor
                getEntorno().comer(this);

                //Zona de descanso si ha sido marcado
                if (this.marcado) {
                    getEntorno().getDescanso().meter(this);
                    dormir(3000 + (int) Math.random() * 2000);
                    getEntorno().getDescanso().sacar(this);
                    marcado = false;
                }
            }
            catch (InterruptedException e) {
                logger.warning("Se ha producido un error en el humano "+getIdentificador());
            }
        }

    }

    public synchronized void esperarAtaque() throws InterruptedException {
        logger.info("Esperando ataque");
        while (defendiendose) {
            this.wait();
        }
    }





    //Getter y setter
    public void setNumComida(int numComida) {
        this.numComida = numComida;
    }
    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }
    public void setDefendiendose(boolean defendiendose) {
        this.defendiendose = defendiendose;
    }
}




