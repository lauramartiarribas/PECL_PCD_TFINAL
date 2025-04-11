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
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Entorno {
   ArrayList<Ser> descanso= new ArrayList<>();
   ArrayList<Ser> comedor= new ArrayList<>();
   ArrayList<Ser> zona_comun= new ArrayList<>();
   int comida;

   int numSeres;


   ArrayList<CyclicBarrier> tunelesSalir= new ArrayList<>();
   ArrayList<CyclicBarrier> tunelesEntrar= new ArrayList<>();

   ArrayList<Lock> tunelesInterior= new ArrayList<>();

   ArrayList<Boolean> hayPrioridad= new ArrayList<>();





   ArrayList<ArrayList<Ser>> zona_riesgoHumanos= new ArrayList<>();
   ArrayList<Ser> zona_riesgoHumano1=new ArrayList<>();
   ArrayList<Ser> zona_riesgoHumano2=new ArrayList<>();
   ArrayList<Ser> zona_riesgoHumano3=new ArrayList<>();
   ArrayList<Ser> zona_riesgoHumano4=new ArrayList<>();
   ArrayList<Ser> zona_riesgoZombie= new ArrayList<>();



   //Para cuando queramos pausar el juego y asi poder también alamacenar todos los hilos que estan en ese momento
   private boolean isPaused = false;
   private ArrayList<Thread> hilos = new ArrayList<>();

   public Entorno(){
      numSeres=0;
      for(int i=0; i<4;i++){
         tunelesSalir.add(new CyclicBarrier(3));
         tunelesEntrar.add(new CyclicBarrier(3));
         tunelesInterior.add(new ReentrantLock());
         hayPrioridad.add(false);
      }
      zona_riesgoHumanos.add(zona_riesgoHumano1);
      zona_riesgoHumanos.add(zona_riesgoHumano2);
      zona_riesgoHumanos.add(zona_riesgoHumano3);
      zona_riesgoHumanos.add(zona_riesgoHumano4);



   }


   @FXML
   public Button PlayButton;
   @FXML
   public Button PauseButton;

   @FXML
   public Button ReanudarButton;

   @FXML
   public ListView ListaDescanso;

   @FXML
   public ListView ListaComedor;


   @FXML
   public ListView<Ser> ListaZonaComun;


   public ObservableList<ListView<Ser>> ListaZonaRiesgo;

   @FXML
   public ListView<Ser> ListaZonaRiesgo1;
   @FXML
   public ListView<Ser> ListaZonaRiesgo2;
   @FXML
   public ListView<Ser> ListaZonaRiesgo3;
   @FXML
   public ListView<Ser> ListaZonaRiesgo4;
   @FXML
   public ListView<Ser> ListaZonaRiesgoZombie1;
   @FXML
   public ListView<Ser> ListaZonaRiesgoZombie2;
   @FXML
   public ListView<Ser> ListaZonaRiesgoZombie3;
   @FXML
   public ListView<Ser> ListaZonaRiesgoZombie4;

   @FXML
   public Label Comida;







   public synchronized void meter(Humano t,ListView vista, ArrayList<Ser> lista) {
      lista.add(t);
      Platform.runLater(() -> {
         imprimir(vista, lista); // Esto actualizará la ListView
      });
   }

   public synchronized void sacar(Ser t,ListView vista,ArrayList<Ser> lista)
   {
      lista.remove(t);
      // Nos aseguramos de que imprimir se ejecuta en el hilo de la interfaz gráfica (el hilo principal de la aplicación de JavaFX
      Platform.runLater(() -> {
         imprimir(vista, lista); // Esto actualizará la ListView
      });

   }

   public synchronized void imprimir(ListView<Ser> vista, ArrayList<Ser> lista) {
      Platform.runLater(() -> {
         ObservableList<Ser> observableList = FXCollections.observableArrayList(lista);

         vista.setItems(observableList);
      });
   }






   @FXML
   void onPlayButtonClick(ActionEvent event) {

      for(int i=0;i<5;i++){
         Humano humano= new Humano("H"+ String.format("%04d", i),this);
         hilos.add(humano);
         humano.start();
      }




   }
   @FXML
   void onStopButtonClick(ActionEvent event) {
      isPaused = true; // Cambia el estado a pausa
      for (Thread thread : hilos) {
         thread.interrupt(); // Interrumpe los hilos activos, esto lo pausará temporalmente
      }

   }

   @FXML
   void onReanudarButtonClick(ActionEvent event){
      isPaused = false; // Cambia el estado a reanudado


   }
   @FXML
   public  void  initialize() {

      System.out.println("ListView inicializado correctamente.");
      ListaZonaRiesgo = FXCollections.observableArrayList(
              ListaZonaRiesgo1,
              ListaZonaRiesgo2,
              ListaZonaRiesgo3,
              ListaZonaRiesgo4
      );




   }

}
