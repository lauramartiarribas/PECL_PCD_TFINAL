package com.example.pecl_pcd_final;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
            int tunelSalir = (int) (Math.random() * 4);
            try {
                // Seleccionar túnel

                if(!marcado) {
                    getEntorno().comprobarPausa();

                    // Zona común tiempo entre 1 y 2
                    logger.info("En la zona común " + getIdentificador());
                    getEntorno().getZona_comun().meter(this);

                    getEntorno().comprobarPausa();
                    sleep(1000 + (int) (Math.random() * 1000));
                    getEntorno().comprobarPausa();




                    logger.info(getIdentificador() + " Esperando en la barrera para salir");


                    getEntorno().getListaTuneles().get(tunelSalir).salirDesdeRefugio(this, tunelSalir);


                    logger.info(getIdentificador() + " En la zona exterior");
                    getEntorno().comprobarPausa();
                    sleep(3000 + (int) Math.random() * 2000);
                    getEntorno().comprobarPausa();
                }
                esperarAtaque();
                if(isEstaMuerto()){
                    getEntorno().getZonaRiesgoH(tunelSalir).getHumanos().sacar(this);
                    return;
                }


                getEntorno().getListaTuneles().get(tunelSalir).volverAlRefugio(this, tunelSalir);


                getEntorno().actualizarComida(numComida);
                getEntorno().actualizarLabelComida();

                getEntorno().comprobarPausa();
                sleep(3000 + (int) Math.random() * 2000);
                getEntorno().comprobarPausa();
                getEntorno().getDescanso().sacar(this);


                ///En la zona de descanso
                getEntorno().getDescanso().meter(this);
                getEntorno().comprobarPausa();
                sleep(2000 + (int) Math.random() * 2000);
                getEntorno().comprobarPausa();
                getEntorno().getDescanso().sacar(this);


                //Zona de comedor
                getEntorno().comer(this);

                //Zona de descanso si ha sido marcado
                if (this.marcado) {
                    getEntorno().getDescanso().meter(this);
                    getEntorno().comprobarPausa();
                    sleep(3000 + (int) Math.random() * 2000);
                    getEntorno().comprobarPausa();
                    getEntorno().getDescanso().sacar(this);

                    marcado = false;
                }


            }
            catch (InterruptedException e) {


            }
        }

    }



    public void cruzarTunel() throws InterruptedException {
        logger.info(getIdentificador() + " está cruzando el túnel.");
        getEntorno().comprobarPausa();
        sleep(1000);
        getEntorno().comprobarPausa();

    }

    public void matar(int numZona){

        logger.info(getIdentificador() + " se muere");
        getEntorno().getDescanso().sacar(this);
        getEntorno().getZona_comun().sacar(this);
        getEntorno().getComedor_comiendo().sacar(this);
        getEntorno().getComedor_espera().sacar(this);

        getEntorno().getZonaRiesgoH(numZona).getHumanos().sacar(this);
        getEntorno().getListaTunelesIntermedio().get(numZona).sacar(this);
        getEntorno().getListaTunelesSalir().get(numZona).sacar(this);
        getEntorno().getListaTunelesEntrar().get(numZona).sacar(this);
        getEntorno().getTunelIntermedio(numZona).sacar(this);
        getEntorno().getTunelSalir(numZona).sacar(this);
        getEntorno().getTunelEntrar(numZona).sacar(this);


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

    public boolean getDefendiendose() {
        return defendiendose;
    }
}




