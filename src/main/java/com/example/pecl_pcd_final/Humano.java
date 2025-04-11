package com.example.pecl_pcd_final;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

public class Humano extends Ser {

    int numComida;
    boolean marcado;



    public Humano(String id, Entorno entorno){
        this.identificador= id;
        this.entorno=entorno;
        numComida=0;
        marcado=false;
    }


    @Override
    public void run(){

        try {
            System.out.println("Empezando"+identificador);
            //Zona común tiempo entre 1 y 2
            System.out.println("En la zona común "+identificador);
            entorno.meter(this,entorno.ListaZonaComun,entorno.zona_comun);
            sleep(1000 + (int) (Math.random() * 1000));
            System.out.println("Saliendo de la zona común "+identificador);


            //Seleccionar túnel
            Random r= new Random();
            int tunelSalir = r.nextInt(0,4);

            entorno.sacar(this,entorno.ListaZonaComun, entorno.zona_comun);

            CyclicBarrier barreraSalida= entorno.tunelesSalirBarreras.get(tunelSalir);
            entorno.meter(this, entorno.TunelesSalida.get(tunelSalir), entorno.listaTunelesSalir.get(tunelSalir));
            System.out.println(identificador+ " Esperando en la barrera para salir");
            //sleep(2000);
            barreraSalida.await();

            entorno.sacar(this,entorno.TunelesSalida.get(tunelSalir), entorno.listaTunelesSalir.get(tunelSalir));

            System.out.println(identificador+ " Saliendo");

            Lock tunelInterior=entorno.tunelesInteriorLock.get(tunelSalir);

            if(!entorno.hayPrioridad.get(tunelSalir)){

                pasarTunel( this, tunelInterior, tunelSalir);

                entorno.meter(this, entorno.ZonaRiesgoHumanos.get(tunelSalir), entorno.zona_riesgoHumanos.get(tunelSalir));
                System.out.println(identificador +" En la zona exterior");
                sleep(3000 + (int) Math.random() * 2000);
                this.numComida+=2;
                entorno.sacar(this, entorno.ZonaRiesgoHumanos.get(tunelSalir), entorno.zona_riesgoHumanos.get(tunelSalir));

            }

            volver(tunelSalir);




        }catch (Exception e){}

    }

    public void pasarTunel(Humano humano, Lock tunel, int numTunel){
        tunel.lock();
        try {
            while (entorno.hayPrioridad.get(numTunel)){
                entorno.tunelesInteriorCondition.get(numTunel).await();
            }
            entorno.meter(humano, entorno.TunelesIntermedio.get(numTunel),entorno.listaTunelesIntermedio.get(numTunel));
            sleep(1000);
            entorno.sacar(humano, entorno.TunelesIntermedio.get(numTunel),entorno.listaTunelesIntermedio.get(numTunel));

        }catch (Exception e){}
        finally {
            tunel.unlock();
        }
    }


    public void volver(int tunelEntrar){
        entorno.hayPrioridad.set(tunelEntrar, true);

        entorno.sacar(this,entorno.ZonaRiesgoHumanos.get(tunelEntrar), entorno.zona_riesgoHumanos.get(tunelEntrar));


        entorno.meter(this, entorno.TunelesEntrada.get(tunelEntrar), entorno.listaTunelesEntrar.get(tunelEntrar));
        System.out.println(identificador+ " Esperando para entrar a la zona segura");
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        entorno.sacar(this,entorno.TunelesEntrada.get(tunelEntrar), entorno.listaTunelesEntrar.get(tunelEntrar));


        entorno.meter(this, entorno.TunelesIntermedio.get(tunelEntrar),entorno.listaTunelesIntermedio.get(tunelEntrar));
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        entorno.sacar(this, entorno.TunelesIntermedio.get(tunelEntrar),entorno.listaTunelesIntermedio.get(tunelEntrar));


        System.out.println(identificador+ " Saliendo");
        entorno.hayPrioridad.set(tunelEntrar, false);
    }


}
