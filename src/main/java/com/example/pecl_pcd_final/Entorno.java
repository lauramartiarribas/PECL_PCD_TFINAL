package com.example.pecl_pcd_final;

import com.almasb.fxgl.app.MainWindow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Entorno {
   ArrayList<Humano> descanso= new ArrayList<>();
   ArrayList<Humano> comedor= new ArrayList<>();
   ArrayList<Humano> zona_comun= new ArrayList<>();
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
   public ListView<Humano> ListaZonaComun;

   @FXML
   public Label Comida;







   public synchronized void meter(Humano t,ListView vista, ArrayList<Humano> lista) {
      lista.add(t);
      imprimir(vista,lista);
   }

   public synchronized void sacar(Ser t,ListView vista,ArrayList<Humano> lista)
   {
      lista.remove(t);
      // Nos aseguramos de que imprimir se ejecuta en el hilo de la interfaz gráfica
      Platform.runLater(() -> {
         imprimir(vista, lista); // Esto actualizará la ListView
      });

   }

   public void imprimir(ListView vista,ArrayList<Humano> lista)
   {
      ObservableList<Humano> datosObservableList = FXCollections.observableArrayList(lista);

      // Asignar la ObservableList al ListView
      ListaZonaComun.setItems(datosObservableList);
      vista.setItems(datosObservableList);
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
   @FXML
   public void initialize() {

      System.out.println("ListView inicializado correctamente.");
   }

}
