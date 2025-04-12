package com.example.pecl_pcd_final;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
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
            logger.info("Empezando " + identificador);
            // Zona común tiempo entre 1 y 2
            logger.info("En la zona común " + identificador);
            entorno.meter(this, entorno.ListaZonaComun, entorno.zona_comun);
            sleep(1000 + (int) (Math.random() * 1000));
            logger.info("Saliendo de la zona común " + identificador);

            // Seleccionar túnel
            Random r = new Random();
            int tunelSalir = r.nextInt(0, 4);

            entorno.sacar(this, entorno.ListaZonaComun, entorno.zona_comun);

            entorno.meter(this, entorno.TunelesSalida.get(tunelSalir), entorno.listaTunelesSalir.get(tunelSalir));
            sleep(1000);
            logger.info(identificador + " Esperando en la barrera para salir");
            entorno.tunelesSalirBarreras.get(tunelSalir).await();

            entorno.sacar(this, entorno.TunelesSalida.get(tunelSalir), entorno.listaTunelesSalir.get(tunelSalir));

            Lock tunelInterior = entorno.tunelesInteriorLock.get(tunelSalir);
            salir(this, tunelInterior, tunelSalir);

            entorno.meter(this, entorno.ZonaRiesgoHumanos.get(tunelSalir), entorno.zona_riesgoHumanos.get(tunelSalir));
            logger.info(identificador + " En la zona exterior");
            sleep(3000 + (int) Math.random() * 2000);
            this.numComida += 2;
            entorno.sacar(this, entorno.ZonaRiesgoHumanos.get(tunelSalir), entorno.zona_riesgoHumanos.get(tunelSalir));

            volver(tunelSalir, tunelInterior);
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
            sleep(1000);
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

            entorno.sacar(this, entorno.ZonaRiesgoHumanos.get(tunelEntrar), entorno.zona_riesgoHumanos.get(tunelEntrar));

            entorno.meter(this, entorno.TunelesEntrada.get(tunelEntrar), entorno.listaTunelesEntrar.get(tunelEntrar));
            logger.info("Esperando para entrar a la zona segura " + identificador);
            sleep(1000);
            entorno.sacar(this, entorno.TunelesEntrada.get(tunelEntrar), entorno.listaTunelesEntrar.get(tunelEntrar));

            entorno.meter(this, entorno.TunelesIntermedio.get(tunelEntrar), entorno.listaTunelesIntermedio.get(tunelEntrar));
            sleep(1000);
            entorno.sacar(this, entorno.TunelesIntermedio.get(tunelEntrar), entorno.listaTunelesIntermedio.get(tunelEntrar));

            logger.info("Saliendo " + identificador);
            entorno.hayPrioridad.set(tunelEntrar, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tunelInteriorLock.unlock();
        }
    }
}




