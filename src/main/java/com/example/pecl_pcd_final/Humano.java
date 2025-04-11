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
    public String toString() {
        return identificador;
    }

    @Override
    public void run(){
        //Zona común tiempo entre 1 y 2
        try {
            System.out.println("Empezando"+identificador);
            System.out.println("En la zona común "+identificador);
            entorno.meter(this,entorno.ListaZonaComun,entorno.zona_comun);
            sleep(1000 + (int) (Math.random() * 1000));
            System.out.println("Saliendo de la zona común "+identificador);


            //Seleccionar túnel
            Random r= new Random();
            int tunelSalir = r.nextInt(0,4);

            entorno.sacar(this,entorno.ListaZonaComun, entorno.zona_comun);

            CyclicBarrier barreraSalida= entorno.tunelesSalirBarreras.get(tunelSalir);
            //METER HUMANO EN LA LISTA DE TUNELES SALIR
            entorno.meter(this, entorno.TunelesSalida.get(tunelSalir), entorno.listaTunelesSalir.get(tunelSalir));
            System.out.println("Esperando en la barrera para salir");
            //sleep(2000);
            barreraSalida.await();

            entorno.sacar(this,entorno.TunelesSalida.get(tunelSalir), entorno.listaTunelesSalir.get(tunelSalir));

            System.out.println("saliendo");
            //QUITARLO
            Lock tunelInterior=entorno.tunelesInterior.get(tunelSalir);
            if(!entorno.hayPrioridad.get(tunelSalir)){

                try {
                    tunelInterior.lock();
                    entorno.meter(this, entorno.TunelesIntermedio.get(tunelSalir),entorno.listaTunelesIntermedio.get(tunelSalir));
                    sleep(1000);
                    entorno.sacar(this, entorno.TunelesIntermedio.get(tunelSalir),entorno.listaTunelesIntermedio.get(tunelSalir));

                    //METERLO EN ZONA EXTERIOR
                    entorno.meter(this, entorno.ZonaRiesgo.get(tunelSalir), entorno.zona_riesgoHumanos.get(tunelSalir));
                    System.out.println("En la zona exterior");
                    sleep(3000 + (int) Math.random() * 2000);
                    entorno.sacar(this, entorno.ZonaRiesgo.get(tunelSalir), entorno.zona_riesgoHumanos.get(tunelSalir));
                    this.numComida+=2;
                }catch (Exception e){}
                finally {
                    tunelInterior.unlock();
                }





            }



        }catch (Exception e){}
        finally {

        }

    }









//    @FXML
//    private ListView<String> listView;
//
//    @FXML
//    private TextField inputField;
//
//    private ObservableList<String> datos;
//
//    @FXML
//    public void initialize() {
//        datos = FXCollections.observableArrayList();
//        listView.setItems(datos);
//    }
//
//    @FXML
//    private void agregarElemento() {
//        String texto = inputField.getText();
//        if (!texto.isEmpty()) {
//            datos.add(texto);
//            inputField.clear();
//        }
//    }
//
//    @FXML
//    private void eliminarElemento() {
//        String seleccionado = listView.getSelectionModel().getSelectedItem();
//        if (seleccionado != null) {
//            datos.remove(seleccionado);
//        }
//    }

}
