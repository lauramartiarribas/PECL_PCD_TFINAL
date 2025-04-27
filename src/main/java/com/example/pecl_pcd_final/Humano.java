package com.example.pecl_pcd_final;

import java.util.logging.Logger;

public class Humano extends Ser {

    private Logger logger = LoggerConFichero.getLogger();
    private int numComida;
    private boolean marcado;
//    private Entorno entorno;  CAMBIAR???????



    public Humano(String id, Entorno entorno) {
        this.setIdentificador(id);
        this.setEntorno(entorno);
        numComida = 0;
        marcado = false;
    }

    @Override
    public void run() {
        try {

            while(true) {

                getEntorno().comprobarPausa();
                logger.info("Empezando " + getIdentificador());


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


                //Volver al refugio
//                if(this.isEstaMuerto()==false){
//                    getEntorno().getListaTuneles().get(tunelSalir).volverAlRefugio(this, tunelSalir);
//
//
//                }
                getEntorno().getListaTuneles().get(tunelSalir).volverAlRefugio(this, tunelSalir);

//                //Sumamos la comida recolectada y actualizamos el label
//                for (int i = 0; i < this.numComida; i++) {
//                    getEntorno().getComidaTotal().offer(1);
//                }

//                synchronized (getEntorno().getLockComida()) {
//                    getEntorno().getLockComida().notify(); // Solo notificamos a un hilo
//                }
//                getEntorno().actualizarLabelComida();
//
//                ///En la zona de descanso
//                getEntorno().getDescanso().meter(this);
//                getEntorno().comprobarPausa();
//                sleep(2000 + (int) Math.random() * 2000);
//                getEntorno().comprobarPausa();
//                getEntorno().getDescanso().sacar(this);
//
//
//                //Zona de espera en el comedor
//                comer();
//
//                //Zona de descanso si ha sido marcado
//                if (this.marcado) {
//                    getEntorno().getDescanso().meter(this);
//                    getEntorno().comprobarPausa();
//                    sleep(3000 + (int) Math.random() * 2000);
//                    getEntorno().comprobarPausa();
//                    getEntorno().getDescanso().sacar(this);
//
//                    marcado = false;
//
//                }
//
//
//                //Todos vuelven a la zona común
//


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void cruzarTunel() throws InterruptedException {
        logger.info(getIdentificador() + " está cruzando el túnel.");
        getEntorno().comprobarPausa();
        sleep(1000);
        getEntorno().comprobarPausa();
        logger.info(getIdentificador() + " terminó de cruzar el túnel.");
    }

//    public void salir(Humano humano, Lock tunel, int numTunel) {
//        tunel.lock();
//        try {
//            while (getEntorno().getHayPrioridad().get(numTunel)>0) {
//                getEntorno().getTunelesInteriorCondition().get(numTunel).await();
//            }
//            getEntorno().getListaTunelesEntrar().get(numTunel).sacar(this);
//
//            logger.info("Pasando el túnel: " + this.getIdentificador());
//            getEntorno().getListaTunelesIntermedio().get(numTunel).meter(this);
//            getEntorno().comprobarPausa();
//            sleep(1000);
//            getEntorno().comprobarPausa();
//            logger.info("Saliendo del túnel: " + this.getIdentificador());
//            getEntorno().getListaTunelesIntermedio().get(numTunel).sacar(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(getEntorno().getHayPrioridad().get(numTunel)==0){
//                getEntorno().getTunelesInteriorCondition().get(numTunel).signalAll();
//
//            }
//            tunel.unlock();
//        }
//    }
//
//    public void volver(int tunelEntrar, Lock tunelInteriorLock) {
//        getEntorno().getHayPrioridad().incrementAndGet(tunelEntrar);
//        tunelInteriorLock.lock();
//        try {
//            getEntorno().meter(this, getEntorno().TunelesEntrada.get(tunelEntrar), getEntorno().getListaTunelesEntrar().get(tunelEntrar));
//            logger.info("Esperando para entrar a la zona segura " + getIdentificador());
//            getEntorno().comprobarPausa();
//            sleep(1000);
//            getEntorno().comprobarPausa();
//            getEntorno().sacar(this, getEntorno().TunelesEntrada.get(tunelEntrar), getEntorno().getListaTunelesEntrar().get(tunelEntrar));
//
//            getEntorno().meter(this, getEntorno().TunelesIntermedio.get(tunelEntrar), getEntorno().getListaTunelesIntermedio().get(tunelEntrar));
//            getEntorno().comprobarPausa();
//            sleep(1000);
//            getEntorno().comprobarPausa();
//            getEntorno().sacar(this, getEntorno().TunelesIntermedio.get(tunelEntrar), getEntorno().getListaTunelesIntermedio().get(tunelEntrar));
//
//            logger.info("Saliendo " + getIdentificador());
//            getEntorno().getHayPrioridad().decrementAndGet(tunelEntrar);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//
//
//            tunelInteriorLock.unlock();
//
//        }
//    }
//
//    public void comer(){
//        try {
//            getEntorno().meter(this, getEntorno().ListaComedorEspera, getEntorno().getComedor_espera());
//            synchronized (getEntorno().getLockComida()) {
//                // Mientras no haya comida, espera a que se notifique
//                while (getEntorno().sumaComidaLista()==0) {
//                    getEntorno().getLockComida().wait(); // El hilo se suspende hasta que haya comida y se llame a notify()
//                }
//            }
//            getEntorno().sacar(this, getEntorno().ListaComedorEspera, getEntorno().getComedor_espera());
//
//            getEntorno().meter(this, getEntorno().ListaComedorComiendo, getEntorno().getComedor_comiendo());
//            getEntorno().getComidaTotal().poll();
//            getEntorno().actualizarLabelComida();
//            getEntorno().comprobarPausa();
//            sleep(3000 + (int) Math.random() * 2000);
//            getEntorno().comprobarPausa();
//            getEntorno().sacar(this, getEntorno().ListaComedorComiendo, getEntorno().getComedor_comiendo());
//
//
//
//        }catch (Exception e){}
//    }


    //Getter y setter

    public int getNumComida() {
        return numComida;
    }

    public void setNumComida(int numComida) {
        this.numComida = numComida;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }


}




