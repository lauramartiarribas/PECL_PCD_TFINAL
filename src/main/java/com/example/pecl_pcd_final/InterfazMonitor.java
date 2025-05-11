package com.example.pecl_pcd_final;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfazMonitor extends Remote {

    int getNumHumanosRefugio() throws RemoteException;

    int getNumHumanosTunel(int num) throws RemoteException;

    int getNumZonaInseguraHumanos(int num) throws RemoteException;

    int getNumZonaInseguraZombies(int num) throws RemoteException;

    ArrayList<String> getLetales() throws RemoteException;


}
