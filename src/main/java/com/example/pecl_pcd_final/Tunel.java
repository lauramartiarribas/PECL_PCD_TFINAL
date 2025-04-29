package com.example.pecl_pcd_final;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tunel {
    private CyclicBarrier barreraTunel;
    private Lock cerrojoTunel;
    private Condition puedeAtravesar;


    private boolean tunelOcupado= false;


    private final ArrayList<Humano> colaParaVolver = new ArrayList<>();
    private final ArrayList<Humano> colaParaSalir = new ArrayList<>();

    public Tunel(){
        this.barreraTunel= new CyclicBarrier(3);
        this.cerrojoTunel= new ReentrantLock();
        this.puedeAtravesar= cerrojoTunel.newCondition();

    }



    public void salirDesdeRefugio(Humano humano, int tunelSalir) throws InterruptedException {
        try {
            // Se agrega a la cola de salida
            cerrojoTunel.lock();
            colaParaSalir.add(humano);
            cerrojoTunel.unlock();

            // Esperar a formar grupo de 3
            barreraTunel.await();

            // Ya hay tres que van a cruzar
            humano.getEntorno().getZona_comun().sacar(humano);
            humano.getEntorno().getListaTunelesSalir().get(tunelSalir).meter(humano);

            // Ahora esperar su turno para cruzar de uno en uno
            cerrojoTunel.lock();
            try {
                while (tunelOcupado || !colaParaVolver.isEmpty() || colaParaSalir.get(0) != humano) {
                    puedeAtravesar.await();
                }
                tunelOcupado = true;
                colaParaSalir.remove(0);

            } finally {
                cerrojoTunel.unlock();
            }

            // Cruza el túnel
            humano.getEntorno().getListaTunelesSalir().get(tunelSalir).sacar(humano);
            humano.getEntorno().getListaTunelesIntermedio().get(tunelSalir).meter(humano);

            humano.cruzarTunel();

            humano.getEntorno().getListaTunelesIntermedio().get(tunelSalir).sacar(humano);
            humano.getEntorno().getZona_riesgoHumanos().get(tunelSalir).meter(humano);

            cerrojoTunel.lock();
            try {
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

        humano.getEntorno().getZona_riesgoHumanos().get(tunelEntrar).sacar(humano);
        humano.getEntorno().getListaTunelesEntrar().get(tunelEntrar).meter(humano);

        cerrojoTunel.lock();
        try {
            colaParaVolver.add(humano);

            // Prioridad para volver: si hay alguien en cola para volver, pasa primero
            while (tunelOcupado || colaParaVolver.get(0) != humano) {
                puedeAtravesar.await();
            }

            tunelOcupado = true;
            colaParaVolver.remove(0);

        } finally {
            cerrojoTunel.unlock();
        }

        // Cruza el túnel
        humano.getEntorno().getListaTunelesEntrar().get(tunelEntrar).sacar(humano);
        humano.getEntorno().getListaTunelesIntermedio().get(tunelEntrar).meter(humano);

        humano.cruzarTunel();

        humano.getEntorno().getListaTunelesIntermedio().get(tunelEntrar).sacar(humano);
        humano.getEntorno().getDescanso().meter(humano);

        cerrojoTunel.lock();
        try {
            tunelOcupado = false;
            puedeAtravesar.signalAll();
        } finally {
            cerrojoTunel.unlock();
        }
    }
}
