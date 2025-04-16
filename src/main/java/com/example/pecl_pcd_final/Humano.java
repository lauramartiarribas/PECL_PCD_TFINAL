package com.example.pecl_pcd_final;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

public class Humano extends Ser {

    private Logger logger = LoggerConFichero.getLogger();
    private int numComida;
    private boolean marcado;



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
                getEntorno().meter(this, getEntorno().ListaZonaComun, getEntorno().getZona_comun());
                getEntorno().comprobarPausa();
                sleep(1000 + (int) (Math.random() * 1000));
                getEntorno().comprobarPausa();
                logger.info("Saliendo de la zona común " + getIdentificador());

                // Seleccionar túnel
                Random r = new Random();
                int tunelSalir = r.nextInt(0, 4);

                getEntorno().sacar(this, getEntorno().ListaZonaComun, getEntorno().getZona_comun());

                getEntorno().meter(this, getEntorno().TunelesSalida.get(tunelSalir), getEntorno().getListaTunelesSalir().get(tunelSalir));
                getEntorno().comprobarPausa();
                sleep(1000);
                getEntorno().comprobarPausa();
                logger.info(getIdentificador() + " Esperando en la barrera para salir");
                getEntorno().getTunelesSalirBarreras().get(tunelSalir).await();

                getEntorno().sacar(this, getEntorno().TunelesSalida.get(tunelSalir), getEntorno().getListaTunelesSalir().get(tunelSalir));

                Lock tunelInterior = getEntorno().getTunelesInteriorLock().get(tunelSalir);

                //Salimos del tunel
                salir(this, tunelInterior, tunelSalir);

                this.numComida += 2;
                getEntorno().meter(this, getEntorno().ZonaRiesgoHumanos.get(tunelSalir), getEntorno().getZona_riesgoHumanos().get(tunelSalir));

                logger.info(getIdentificador() + " En la zona exterior");
                getEntorno().comprobarPausa();
                sleep(3000 + (int) Math.random() * 2000);
                getEntorno().comprobarPausa();


                getEntorno().sacar(this, getEntorno().ZonaRiesgoHumanos.get(tunelSalir), getEntorno().getZona_riesgoHumanos().get(tunelSalir));

                //Volvemos al refugio
                volver(tunelSalir, tunelInterior);

                //Sumamos la comida recolectada y actualizamos el label
                for (int i = 0; i < this.numComida; i++) {
                    getEntorno().getComidaTotal().offer(1);
                }

                synchronized (getEntorno().getLockComida()) {
                    getEntorno().getLockComida().notify(); // Solo notificamos a un hilo
                }
                getEntorno().actualizarLabelComida();

                ///En la zona de descanso
                getEntorno().meter(this, getEntorno().ListaDescanso, getEntorno().getDescanso());
                getEntorno().comprobarPausa();
                sleep(2000 + (int) Math.random() * 2000);
                getEntorno().comprobarPausa();
                getEntorno().sacar(this, getEntorno().ListaDescanso, getEntorno().getDescanso());

                //Zona de espera en el comedor
                comer();

                //Zona de descanso si ha sido marcado
                if (this.marcado) {
                    getEntorno().meter(this, getEntorno().ListaDescanso, getEntorno().getDescanso());
                    getEntorno().comprobarPausa();
                    sleep(3000 + (int) Math.random() * 2000);
                    getEntorno().comprobarPausa();
                    getEntorno().sacar(this, getEntorno().ListaDescanso, getEntorno().getDescanso());
                }

                //Todos vuelven a la zona común



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void salir(Humano humano, Lock tunel, int numTunel) {
        tunel.lock();
        try {
            while (getEntorno().getHayPrioridad().get(numTunel)) {
                getEntorno().getTunelesInteriorCondition().get(numTunel).await();
            }

            logger.info("Pasando el túnel: " + this.getIdentificador());
            getEntorno().meter(humano, getEntorno().TunelesIntermedio.get(numTunel), getEntorno().getListaTunelesIntermedio().get(numTunel));
            getEntorno().comprobarPausa();
            sleep(1000);
            getEntorno().comprobarPausa();
            logger.info("Saliendo del túnel: " + this.getIdentificador());
            getEntorno().sacar(humano, getEntorno().TunelesIntermedio.get(numTunel), getEntorno().getListaTunelesIntermedio().get(numTunel));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tunel.unlock();
        }
    }

    public void volver(int tunelEntrar, Lock tunelInteriorLock) {
        tunelInteriorLock.lock();
        try {
            getEntorno().getHayPrioridad().set(tunelEntrar, true);

            getEntorno().meter(this, getEntorno().TunelesEntrada.get(tunelEntrar), getEntorno().getListaTunelesEntrar().get(tunelEntrar));
            logger.info("Esperando para entrar a la zona segura " + getIdentificador());
            getEntorno().comprobarPausa();
            sleep(1000);
            getEntorno().comprobarPausa();
            getEntorno().sacar(this, getEntorno().TunelesEntrada.get(tunelEntrar), getEntorno().getListaTunelesEntrar().get(tunelEntrar));

            getEntorno().meter(this, getEntorno().TunelesIntermedio.get(tunelEntrar), getEntorno().getListaTunelesIntermedio().get(tunelEntrar));
            getEntorno().comprobarPausa();
            sleep(1000);
            getEntorno().comprobarPausa();
            getEntorno().sacar(this, getEntorno().TunelesIntermedio.get(tunelEntrar), getEntorno().getListaTunelesIntermedio().get(tunelEntrar));

            logger.info("Saliendo " + getIdentificador());
            getEntorno().getHayPrioridad().set(tunelEntrar, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tunelInteriorLock.unlock();
        }
    }

    public synchronized void comer(){
        try {
            getEntorno().meter(this, getEntorno().ListaComedorEspera, getEntorno().getComedor_espera());
            synchronized (getEntorno().getLockComida()) {
                // Mientras no haya comida, espera a que se notifique
                while (getEntorno().sumaComidaLista()==0) {
                    getEntorno().getLockComida().wait(); // El hilo se suspende hasta que haya comida y se llame a notify()
                }
            }
            getEntorno().sacar(this, getEntorno().ListaComedorEspera, getEntorno().getComedor_espera());

            getEntorno().meter(this, getEntorno().ListaComedorComiendo, getEntorno().getComedor_comiendo());
            getEntorno().getComidaTotal().poll();
            getEntorno().actualizarLabelComida();
            getEntorno().comprobarPausa();
            sleep(3000 + (int) Math.random() * 2000);
            getEntorno().comprobarPausa();
            getEntorno().sacar(this, getEntorno().ListaComedorComiendo, getEntorno().getComedor_comiendo());


        }catch (Exception e){}
    }


    //Getter y setter

    public void setNumComida(int numComida) {
        this.numComida = numComida;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }
}




