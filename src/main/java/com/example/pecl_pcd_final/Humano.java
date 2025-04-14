package com.example.pecl_pcd_final;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

public class Humano extends Ser {

    Logger logger = LoggerConFichero.getLogger();
    int numComida;
    boolean marcado;



    public Humano(String id, Entorno entorno) {
        this.identificador = id;
        this.entorno = entorno;
        numComida = 0;
        marcado = false;
    }

    @Override
    public void run() {
        try {

            while(true) {
                entorno.comprobarPausa();
                logger.info("Empezando " + identificador);
                // Zona común tiempo entre 1 y 2
                logger.info("En la zona común " + identificador);
                entorno.meter(this, entorno.ListaZonaComun, entorno.zona_comun);
                entorno.comprobarPausa();
                sleep(1000 + (int) (Math.random() * 1000));
                entorno.comprobarPausa();
                logger.info("Saliendo de la zona común " + identificador);

                // Seleccionar túnel
                Random r = new Random();
                int tunelSalir = r.nextInt(0, 4);

                entorno.sacar(this, entorno.ListaZonaComun, entorno.zona_comun);

                entorno.meter(this, entorno.TunelesSalida.get(tunelSalir), entorno.listaTunelesSalir.get(tunelSalir));
                entorno.comprobarPausa();
                sleep(1000);
                entorno.comprobarPausa();
                logger.info(identificador + " Esperando en la barrera para salir");
                entorno.tunelesSalirBarreras.get(tunelSalir).await();

                entorno.sacar(this, entorno.TunelesSalida.get(tunelSalir), entorno.listaTunelesSalir.get(tunelSalir));

                Lock tunelInterior = entorno.tunelesInteriorLock.get(tunelSalir);

                //Salimos del tunel
                salir(this, tunelInterior, tunelSalir);

                this.numComida += 2;
                entorno.meter(this, entorno.ZonaRiesgoHumanos.get(tunelSalir), entorno.zona_riesgoHumanos.get(tunelSalir));

                logger.info(identificador + " En la zona exterior");
                entorno.comprobarPausa();
                sleep(3000 + (int) Math.random() * 2000);
                entorno.comprobarPausa();


                entorno.sacar(this, entorno.ZonaRiesgoHumanos.get(tunelSalir), entorno.zona_riesgoHumanos.get(tunelSalir));

                //Volvemos al refugio
                volver(tunelSalir, tunelInterior);

                //Sumamos la comida recolectada y actualizamos el label
                for (int i = 0; i < this.numComida; i++) {
                    entorno.comidaTotal.offer(1);
                }

                synchronized (entorno.lockComida) {
                    entorno.lockComida.notify(); // Solo notificamos a un hilo
                }
                entorno.actualizarLabelComida();

                ///En la zona de descanso
                entorno.meter(this, entorno.ListaDescanso, entorno.descanso);
                entorno.comprobarPausa();
                sleep(2000 + (int) Math.random() * 2000);
                entorno.comprobarPausa();
                entorno.sacar(this, entorno.ListaDescanso, entorno.descanso);

                //Zona de espera en el comedor
                comer();

                //Zona de descanso si ha sido marcado
                if (this.marcado) {
                    entorno.meter(this, entorno.ListaDescanso, entorno.descanso);
                    entorno.comprobarPausa();
                    sleep(3000 + (int) Math.random() * 2000);
                    entorno.comprobarPausa();
                    entorno.sacar(this, entorno.ListaDescanso, entorno.descanso);
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
            while (entorno.hayPrioridad.get(numTunel)) {
                entorno.tunelesInteriorCondition.get(numTunel).await();
            }

            logger.info("Pasando el túnel: " + this.identificador);
            entorno.meter(humano, entorno.TunelesIntermedio.get(numTunel), entorno.listaTunelesIntermedio.get(numTunel));
            entorno.comprobarPausa();
            sleep(1000);
            entorno.comprobarPausa();
            logger.info("Saliendo del túnel: " + this.identificador);
            entorno.sacar(humano, entorno.TunelesIntermedio.get(numTunel), entorno.listaTunelesIntermedio.get(numTunel));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tunel.unlock();
        }
    }

    public void volver(int tunelEntrar, Lock tunelInteriorLock) {
        tunelInteriorLock.lock();
        try {
            entorno.hayPrioridad.set(tunelEntrar, true);

            entorno.meter(this, entorno.TunelesEntrada.get(tunelEntrar), entorno.listaTunelesEntrar.get(tunelEntrar));
            logger.info("Esperando para entrar a la zona segura " + identificador);
            entorno.comprobarPausa();
            sleep(1000);
            entorno.comprobarPausa();
            entorno.sacar(this, entorno.TunelesEntrada.get(tunelEntrar), entorno.listaTunelesEntrar.get(tunelEntrar));

            entorno.meter(this, entorno.TunelesIntermedio.get(tunelEntrar), entorno.listaTunelesIntermedio.get(tunelEntrar));
            entorno.comprobarPausa();
            sleep(1000);
            entorno.comprobarPausa();
            entorno.sacar(this, entorno.TunelesIntermedio.get(tunelEntrar), entorno.listaTunelesIntermedio.get(tunelEntrar));

            logger.info("Saliendo " + identificador);
            entorno.hayPrioridad.set(tunelEntrar, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tunelInteriorLock.unlock();
        }
    }

    public synchronized void comer(){
        try {
            entorno.meter(this, entorno.ListaComedorEspera, entorno.comedor_espera);
            synchronized (entorno.lockComida) {
                // Mientras no haya comida, espera a que se notifique
                while (entorno.sumaComidaLista()==0) {
                    entorno.lockComida.wait(); // El hilo se suspende hasta que haya comida y se llame a notify()
                }
            }
            entorno.sacar(this, entorno.ListaComedorEspera, entorno.comedor_espera);

            entorno.meter(this, entorno.ListaComedorComiendo, entorno.comedor_comiendo);
            entorno.comidaTotal.poll();
            entorno.comidaTotal.poll();
            entorno.actualizarLabelComida();
            entorno.comprobarPausa();
            sleep(3000 + (int) Math.random() * 2000);
            entorno.comprobarPausa();
            entorno.sacar(this, entorno.ListaComedorComiendo, entorno.comedor_comiendo);


        }catch (Exception e){}
    }

}




