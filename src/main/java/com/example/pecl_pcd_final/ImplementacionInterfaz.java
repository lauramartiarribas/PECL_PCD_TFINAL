package com.example.pecl_pcd_final;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ImplementacionInterfaz extends UnicastRemoteObject implements InterfazMonitor {

    private Entorno entorno;

    public ImplementacionInterfaz() throws RemoteException {
    }


    public ImplementacionInterfaz(Entorno entorno) throws RemoteException {
        this.entorno = entorno;
    }


    @Override
    public int getNumHumanosRefugio() throws RemoteException {
        return entorno.getNumRefugio();
    }

    @Override
    public int getNumHumanosTunel(int num) throws RemoteException {
        return entorno.getNumTunel(num);
    }

    @Override
    public int getNumZonaInseguraHumanos(int num) throws RemoteException {
        return entorno.getNumZonaInseguraHumanos(num);
    }

    @Override
    public int getNumZonaInseguraZombies(int num) throws RemoteException {
        return entorno.getNumZonaInseguraZombie(num);
    }

    @Override
    public ArrayList<String> getLetales() throws RemoteException {
        return entorno.getZombiesLetales();
    }

    public Entorno getEntorno() {
        return entorno;
    }
}
