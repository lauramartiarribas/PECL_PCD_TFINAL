package com.example.pecl_pcd_final;


import java.util.logging.Logger;

public class Humano extends Ser {

    private Logger logger = LoggerConFichero.getLogger();
    private int numComida;
    private boolean marcado;
    private volatile boolean enTunel = false;



    public Humano(String id, Entorno entorno) {
        this.setIdentificador(id);
        this.setEntorno(entorno);
        numComida = 0;
        marcado = false;
    }

    @Override
    public void run() {
        try {
            logger.info("Empezando " + getIdentificador());
            while(!isEstaMuerto()) {

                getEntorno().comprobarPausa();

                // Zona común tiempo entre 1 y 2
                logger.info("En la zona común " + getIdentificador());
                getEntorno().getZona_comun().meter(this);

                getEntorno().comprobarPausa();
                sleep(1000 + (int) (Math.random() * 1000));
                getEntorno().comprobarPausa();

                logger.info("Saliendo de la zona común " + getIdentificador());


                // Seleccionar túnel
                int tunelSalir = (int)(Math.random()*4);
                logger.info(getIdentificador() + " Esperando en la barrera para salir");

                getEntorno().getListaTuneles().get(tunelSalir).salirDesdeRefugio(this, tunelSalir);


                logger.info(getIdentificador() + " En la zona exterior");
                getEntorno().comprobarPausa();
                sleep(3000 + (int) Math.random() * 2000);
                getEntorno().comprobarPausa();


                getEntorno().getListaTuneles().get(tunelSalir).volverAlRefugio(this, tunelSalir);


                //Sumamos la comida recolectada y actualizamos el label
                for (int i = 0; i < this.numComida; i++) {
                    getEntorno().getComidaTotal().offer(1);
                    getEntorno().actualizarLabelComida();
                    getEntorno().comprobarPausa();
                }

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
            if (this.isEstaMuerto()){
                System.out.println("Humano muerto");
                Thread.currentThread().interrupt(); // reestablecer estado de interrupción

            }else{
                try {

                    System.out.println("ataque");
                    Thread.sleep(500);
                    Thread.interrupted();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }


        }
    }



    public void cruzarTunel() throws InterruptedException {
        logger.info(getIdentificador() + " está cruzando el túnel.");
        getEntorno().comprobarPausa();
        sleep(1000);
        getEntorno().comprobarPausa();
        logger.info(getIdentificador() + " terminó de cruzar el túnel.");
    }






    //Getter y setter

    public void setNumComida(int numComida) {
        this.numComida = numComida;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }
    public boolean estaEnTunel() {
        return enTunel;
    }
    public void setEnTunel(boolean enTunel) {
        this.enTunel = enTunel;
    }

}




