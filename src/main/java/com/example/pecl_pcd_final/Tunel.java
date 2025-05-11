package com.example.pecl_pcd_final;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Tunel {

    private Logger logger = LoggerConFichero.getLogger();

    private CyclicBarrier barreraTunel;
    private Lock cerrojoTunel;
    private Condition puedeAtravesar;
    private Entorno entorno;
    private boolean tunelOcupado;



    private  ConcurrentLinkedQueue<Humano> colaParaVolver = new ConcurrentLinkedQueue<>();
    private  ConcurrentLinkedQueue<Humano> colaParaSalir = new ConcurrentLinkedQueue<>();

    public Tunel(Entorno entorno){
        this.barreraTunel= new CyclicBarrier(3);
        this.cerrojoTunel= new ReentrantLock();
        this.puedeAtravesar= cerrojoTunel.newCondition();
        this.entorno=entorno;
        this.tunelOcupado= false;

    }



    public void salirDesdeRefugio(Humano humano, int tunelSalir) throws InterruptedException {
        try {
            // Esperar a formar grupo de 3
            logger.info(humano.getIdentificador() + " Esperando en la barrera para salir");
            barreraTunel.await();

            //Ya hay tres que van a cruzar
            entorno.getZona_comun().sacar(humano);
            entorno.getTunelSalir(tunelSalir).meter(humano);

            //Ahora esperar su turno para cruzar de uno en uno
            cerrojoTunel.lock();
            try {
                //Se mete en la cola de salida
                colaParaSalir.offer(humano);

                //Mientras haya alguien cruzando, o alguien queriendo volver de la ZR
                while (tunelOcupado || !colaParaVolver.isEmpty()) {
                    puedeAtravesar.await();
                }
                //Cuando ya le toca
                tunelOcupado = true;
                colaParaSalir.poll();

                //Cruza el túnel y está dentro
                entorno.getTunelSalir(tunelSalir).sacar(humano);
                entorno.getTunelIntermedio(tunelSalir).meter(humano);

                logger.info(humano.getIdentificador() + " está cruzando el túnel.");
                humano.dormir(1000);


                //Sale del tunel y va a la ZR
                entorno.getTunelIntermedio(tunelSalir).sacar(humano);
                entorno.getZonaRiesgoH(tunelSalir).getHumanos().meter(humano);
                entorno.getZonaRiesgoH(tunelSalir).getHumanosDisponibles().add(humano);
                humano.setNumComida(2);

                tunelOcupado = false;
                puedeAtravesar.signalAll();
            } finally {
                cerrojoTunel.unlock();
            }



        } catch (BrokenBarrierException e) {

        }
    }

    public void volverAlRefugio(Humano humano, int tunelEntrar) throws InterruptedException {
        entorno.getZonaRiesgoH(tunelEntrar).getHumanos().sacar(humano);
        entorno.getZonaRiesgoH(tunelEntrar).getHumanosDisponibles().remove(humano);
        entorno.getTunelEntrar(tunelEntrar).meter(humano);
        colaParaVolver.offer(humano);
        cerrojoTunel.lock();
        try {



            //Prioridad para volver: si hay alguien en cola para volver, pasa primero
            while (tunelOcupado) {
                puedeAtravesar.await();
            }

            //Cuando ya puede volver
            tunelOcupado = true;
            colaParaVolver.poll();

            //Cruza el túnel
            entorno.getTunelEntrar(tunelEntrar).sacar(humano);
            entorno.getTunelIntermedio(tunelEntrar).meter(humano);
            logger.info(humano.getIdentificador() + " está cruzando el túnel.");
            humano.dormir(1000);


            entorno.getTunelIntermedio(tunelEntrar).sacar(humano);
            entorno.getDescanso().meter(humano);

            //tunelOcupado = false;
            tunelOcupado = false;
            puedeAtravesar.signalAll();
        } finally {


//                entorno.getTunelIntermedio(tunelEntrar).sacar(humano);
            cerrojoTunel.unlock();
        }

    }
}
