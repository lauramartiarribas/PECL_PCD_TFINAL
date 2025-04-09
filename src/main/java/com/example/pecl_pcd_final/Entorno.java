package com.example.pecl_pcd_final;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Entorno {
   ArrayList<Humano> descanso;
   ArrayList<Humano> comedor;
   ArrayList<Humano> zona_comun;
   int comida;

   int numSeres;


   ArrayList<CyclicBarrier> tunelesSalir= new ArrayList<>();
   ArrayList<CyclicBarrier> tunelesEntrar= new ArrayList<>();

   ArrayList<Lock> tunelesInterior= new ArrayList<>();

   ArrayList<Boolean> hayPrioridad= new ArrayList<>();



   ArrayList<Humano> humanos= new ArrayList<>();
   ArrayList<Zombie> zombies= new ArrayList<>();

   ArrayList<Object> zona_riesgo= new ArrayList<>();

   public Entorno(){
      numSeres=0;
      zona_riesgo.add(humanos);
      zona_riesgo.add(zombies);
      for(int i=0; i<4;i++){
         tunelesSalir.add(new CyclicBarrier(3));
         tunelesEntrar.add(new CyclicBarrier(3));
         tunelesInterior.add(new ReentrantLock());
         hayPrioridad.add(false);
      }
   }


   @FXML
   public Button PlayButton;
   @FXML
   public Button PauseButton;

   @FXML
   public ListView ListaDescanso;

   @FXML
   public ListView ListaComedor;


   @FXML
   public ListView ListaZonaComun;

   @FXML
   public Label Comida;






   public synchronized void meter(Humano t,ListView vista, ArrayList<Humano> lista)
   {
      lista.add(t);
      imprimir(vista,lista);
   }

   public synchronized void sacar(Ser t,ListView vista,ArrayList<Humano> lista)
   {
      lista.remove(t);
      imprimir(vista,lista);
   }

   public void imprimir(ListView vista,ArrayList<Humano> lista)
   {
      vista.setItems((ObservableList) lista);
   }







   @FXML
   void onPlayButtonClick(ActionEvent event) {
      for(int i=0;i<4;i++){
         Humano humano= new Humano("H"+ String.format("%04d", i),this);
         humano.start();
      }
   }
   @FXML
   void onStopButtonClick(ActionEvent event) {

   }

}
