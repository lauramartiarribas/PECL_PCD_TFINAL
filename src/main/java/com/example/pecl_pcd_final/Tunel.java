package com.example.pecl_pcd_final;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tunel {
    private CyclicBarrier barreraTunel;
    private Lock cerrojoTunel;
    private Condition puedeAtravesar;
    private Entorno entorno;




    private boolean tunelOcupado= false;


    private final ConcurrentLinkedQueue<Humano> colaParaVolver = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Humano> colaParaSalir = new ConcurrentLinkedQueue<>();

    public Tunel(Entorno entorno){
        this.barreraTunel= new CyclicBarrier(3);
        this.cerrojoTunel= new ReentrantLock();
        this.puedeAtravesar= cerrojoTunel.newCondition();
        this.entorno=entorno;

    }



    public void salirDesdeRefugio(Humano humano, int tunelSalir) throws InterruptedException {
        try {


            // Esperar a formar grupo de 3
            barreraTunel.await();

            // Ya hay tres que van a cruzar
            entorno.getZona_comun().sacar(humano);
            entorno.getTunelSalir(tunelSalir).meter(humano);

            // Ahora esperar su turno para cruzar de uno en uno
            cerrojoTunel.lock();
            try {
                // Se agrega a la cola de salida
                colaParaSalir.offer(humano);

                //Mientras haya alguien cruzando, o alguien queriendo volver de la ZR o haya alguien cruzando antes
                while (tunelOcupado || !colaParaVolver.isEmpty() ) {
                    puedeAtravesar.await();
                }
                tunelOcupado = true;
                colaParaSalir.poll();

                // Cruza el túnel
                entorno.getTunelSalir(tunelSalir).sacar(humano);
                entorno.getTunelIntermedio(tunelSalir).meter(humano);



            } finally {
                cerrojoTunel.unlock();
            }

            humano.cruzarTunel();



            cerrojoTunel.lock();
            try {

                entorno.getTunelIntermedio(tunelSalir).sacar(humano);
                entorno.getZonaRiesgoH(tunelSalir).meter(humano);

                tunelOcupado = false;
                puedeAtravesar.signalAll();
            } finally {
                cerrojoTunel.unlock();
            }

            humano.setNumComida(2);

        } catch (BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void volverAlRefugio(Humano humano, int tunelEntrar) throws InterruptedException {
        if(humano.isEstaMuerto()){
            return;
        }
        entorno.getZonaRiesgoH(tunelEntrar).sacar(humano);
        entorno.getTunelEntrar(tunelEntrar).meter(humano);


        cerrojoTunel.lock();
        try {
            colaParaVolver.offer(humano);

            // Prioridad para volver: si hay alguien en cola para volver, pasa primero
            while (tunelOcupado ) {
                puedeAtravesar.await();
            }

            tunelOcupado = true;
            colaParaVolver.poll();

            // Cruza el túnel
            entorno.getTunelEntrar(tunelEntrar).sacar(humano);
            entorno.getTunelIntermedio(tunelEntrar).meter(humano);


        } finally {
            cerrojoTunel.unlock();
        }

        humano.cruzarTunel();





        cerrojoTunel.lock();
        try {
            entorno.getTunelIntermedio(tunelEntrar).sacar(humano);
            entorno.getDescanso().meter(humano);

            tunelOcupado = false;
            puedeAtravesar.signalAll();
        } finally {
            cerrojoTunel.unlock();
        }
    }


}
