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


   ArrayList<CyclicBarrier> tunelesSalirBarreras= new ArrayList<>();
   ArrayList<CyclicBarrier> tunelesEntrarBarreras= new ArrayList<>();

   ArrayList<Lock> tunelesInterior= new ArrayList<>();

   ArrayList<Boolean> hayPrioridad= new ArrayList<>();





   ArrayList<ArrayList<Ser>> zona_riesgoHumanos= new ArrayList<>();
   ArrayList<Ser> zona_riesgoHumano1=new ArrayList<>();
   ArrayList<Ser> zona_riesgoHumano2=new ArrayList<>();
   ArrayList<Ser> zona_riesgoHumano3=new ArrayList<>();
   ArrayList<Ser> zona_riesgoHumano4=new ArrayList<>();


   ArrayList<ArrayList<Ser>> listaTunelesSalir= new ArrayList<>();
   ArrayList<Ser> tunelSalir1=new ArrayList<>();
   ArrayList<Ser> tunelSalir2=new ArrayList<>();
   ArrayList<Ser> tunelSalir3=new ArrayList<>();
   ArrayList<Ser> tunelSalir4=new ArrayList<>();


   ArrayList<ArrayList<Ser>> listaTunelesEntrar= new ArrayList<>();
   ArrayList<Ser> tunelEntrar1=new ArrayList<>();
   ArrayList<Ser> tunelEntrar2=new ArrayList<>();
   ArrayList<Ser> tunelEntrar3=new ArrayList<>();
   ArrayList<Ser> tunelEntrar4=new ArrayList<>();


   ArrayList<Ser> zona_riesgoZombie= new ArrayList<>();



   //Para cuando queramos pausar el juego y asi poder también alamacenar todos los hilos que estan en ese momento
   private boolean isPaused = false;
   private ArrayList<Thread> hilos = new ArrayList<>();

   public Entorno(){
      numSeres=0;
      for(int i=0; i<4;i++){
         tunelesSalirBarreras.add(new CyclicBarrier(3));
         tunelesEntrarBarreras.add(new CyclicBarrier(3));
         tunelesInterior.add(new ReentrantLock());
         hayPrioridad.add(false);
      }
      zona_riesgoHumanos.add(zona_riesgoHumano1);
      zona_riesgoHumanos.add(zona_riesgoHumano2);
      zona_riesgoHumanos.add(zona_riesgoHumano3);
      zona_riesgoHumanos.add(zona_riesgoHumano4);

      listaTunelesSalir.add(tunelSalir1);
      listaTunelesSalir.add(tunelSalir2);
      listaTunelesSalir.add(tunelSalir3);
      listaTunelesSalir.add(tunelSalir4);

      listaTunelesEntrar.add(tunelEntrar1);
      listaTunelesEntrar.add(tunelEntrar2);
      listaTunelesEntrar.add(tunelEntrar3);
      listaTunelesEntrar.add(tunelEntrar4);



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

   ///////////////// TUNELES //////////////////////


   public ObservableList<ListView<Ser>> TunelesSalida;

   @FXML
   public ListView<Ser> TunelSalir1;
   @FXML
   public ListView<Ser> TunelSalir2;
   @FXML
   public ListView<Ser> TunelSalir3;
   @FXML
   public ListView<Ser> TunelSalir4;


   public ObservableList<ListView<Ser>> TunelesIntermedio;

   @FXML
   public ListView<Ser> TunelIntermedio1;
   @FXML
   public ListView<Ser> TunelIntermedio2;
   @FXML
   public ListView<Ser> TunelIntermedio3;
   @FXML
   public ListView<Ser> TunelIntermedio4;


   public ObservableList<ListView<Ser>> TunelesEntrada;

   @FXML
   public ListView<Ser> TunelEntrar1;
   @FXML
   public ListView<Ser> TunelEntrar2;
   @FXML
   public ListView<Ser> TunelEntrar3;
   @FXML
   public ListView<Ser> TunelEntrar4;





   ///////////////// ZONA DE RIESGO ///////////////////////////
   public ObservableList<ListView<Ser>> ZonaRiesgo;

   @FXML
   public ListView<Ser> ZonaRiesgo1;
   @FXML
   public ListView<Ser> ZonaRiesgo2;
   @FXML
   public ListView<Ser> ZonaRiesgo3;
   @FXML
   public ListView<Ser> ZonaRiesgo4;
   @FXML
   public ListView<Ser> ZonaRiesgoZombie1;
   @FXML
   public ListView<Ser> ZonaRiesgoZombie2;
   @FXML
   public ListView<Ser> ZonaRiesgoZombie3;
   @FXML
   public ListView<Ser> ZonaRiesgoZombie4;

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
      ZonaRiesgo = FXCollections.observableArrayList(
              ZonaRiesgo1,
              ZonaRiesgo2,
              ZonaRiesgo3,
              ZonaRiesgo4
      );
      TunelesSalida = FXCollections.observableArrayList(
              TunelSalir1,
              TunelSalir2,
              TunelSalir3,
              TunelSalir4
      );

      TunelesEntrada = FXCollections.observableArrayList(
              TunelEntrar1,
              TunelEntrar2,
              TunelEntrar3,
              TunelEntrar4
      );


      TunelesIntermedio = FXCollections.observableArrayList(
              TunelIntermedio1,
              TunelIntermedio2,
              TunelIntermedio3,
              TunelIntermedio4
      );



   }

}
