package com.example.pecl_pcd_final;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Humano extends Ser {

    private Logger logger = LoggerConFichero.getLogger();
    private int numComida;
    private boolean marcado;

    private Lock cerrojoDefendiendose;

    private Condition defendiendoseCondicion;

    private Boolean defendiendose;




    public Humano(String id, Entorno entorno) {
        this.setIdentificador(id);
        this.setEntorno(entorno);
        numComida = 0;
        marcado = false;
        cerrojoDefendiendose= new ReentrantLock();
        defendiendoseCondicion= cerrojoDefendiendose.newCondition();
        defendiendose=false;
    }

    @Override
    public void run() {
        try {
            logger.info("Empezando " + getIdentificador());
            while(!isEstaMuerto()) {
                cerrojoDefendiendose.lock();
                try{
                    while (defendiendose) {
                        defendiendoseCondicion.await(); // El humano espera hasta que termine el ataque
                    }
                }finally {
                    cerrojoDefendiendose.unlock();
                }


                getEntorno().comprobarPausa();

                // Zona común tiempo entre 1 y 2
                logger.info("En la zona común " + getIdentificador());
                getEntorno().getZona_comun().meter(this);

                getEntorno().comprobarPausa();
                sleep(1000 + (int) (Math.random() * 1000));
                getEntorno().comprobarPausa();




                // Seleccionar túnel
                int tunelSalir = (int)(Math.random()*4);
                logger.info(getIdentificador() + " Esperando en la barrera para salir");

                getEntorno().getListaTuneles().get(tunelSalir).salirDesdeRefugio(this, tunelSalir);


                logger.info(getIdentificador() + " En la zona exterior");
                getEntorno().comprobarPausa();
                sleep(3000 + (int) Math.random() * 2000);
                getEntorno().comprobarPausa();


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


               //Todos vuelven a la zona común



            }
        } catch (InterruptedException e) {
                logger.warning("Humano " + getIdentificador() + " interrumpido por otra causa.");
                Thread.currentThread().interrupt(); // Mantiene interrupción
        }
    }



    public void cruzarTunel() throws InterruptedException {
        logger.info(getIdentificador() + " está cruzando el túnel.");
        getEntorno().comprobarPausa();
        sleep(1000);
        getEntorno().comprobarPausa();

    }






    //Getter y setter

    public void setNumComida(int numComida) {
        this.numComida = numComida;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    public Lock getCerrojoDefendiendose() {
        return cerrojoDefendiendose;
    }

    public Condition getDefendiendoseCondicion() {
        return defendiendoseCondicion;
    }

    public void setDefendiendose(Boolean defendiendose) {
        this.defendiendose = defendiendose;
    }

    public Boolean getDefendiendose() {
        return defendiendose;
    }
}




