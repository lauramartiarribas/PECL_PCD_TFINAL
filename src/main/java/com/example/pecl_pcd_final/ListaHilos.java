package com.example.pecl_pcd_final;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.util.ArrayList;
import java.util.Iterator;


public class ListaHilos {

    /* La clase ListaThreads permite gestionar las listas de threads en los monitores,
con métodos para meter y sacar threads en ella. Cada vez que una lista se modifica,
se imprime su nuevo contenido en el ListView que toma como parámetro el constructor. */

    private ArrayList<Ser> lista;
    private ListView listView;
    private ObservableList<String> observableList;

    public ListaHilos(ListView listView) {
        this.lista = new ArrayList<>();
        this.listView = listView;
        this.observableList = FXCollections.observableArrayList();
        this.listView.setItems(observableList);
    }

    public synchronized void meter(Ser t) {
        lista.add(t);
        imprimir();
    }

    public synchronized void sacar(Ser t) {
        lista.remove(t);
        imprimir();
    }

    private void imprimir() {
        Platform.runLater(() -> {
            observableList.clear();
            synchronized (this) {
                for (Ser ser : lista) {
                    //if(ser.isEstaMuerto()){return;}
                    observableList.add(ser.toString());
                }
            }
        });
    }


    public ArrayList<Ser> getLista() {
        return lista;
    }
}