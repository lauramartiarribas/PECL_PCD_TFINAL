package com.example.pecl_pcd_final;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import java.util.logging.SimpleFormatter;



public class Entorno {

   ///El logger///

   ArrayList<Ser> descanso= new ArrayList<>();
   ArrayList<Ser> comedor= new ArrayList<>();
   ArrayList<Ser> zona_comun= new ArrayList<>();

   int comidaTotal;

   int numSeres;
   //Para cuando queramos pausar el juego y así poder también almacenar todos los hilos que están en ese momento
   private boolean isPaused = false;
   public ArrayList<Thread> humanos = new ArrayList<>();



   ArrayList<CyclicBarrier> tunelesSalirBarreras= new ArrayList<>();
   ArrayList<CyclicBarrier> tunelesEntrarBarreras= new ArrayList<>();

   ArrayList<Lock> tunelesInteriorLock= new ArrayList<>();
   ArrayList<Condition> tunelesInteriorCondition= new ArrayList<>(4);
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

   ArrayList<ArrayList<Ser>> listaTunelesIntermedio = new ArrayList<>();
   ArrayList<Ser> tunelIntermedio1 = new ArrayList<>();
   ArrayList<Ser> tunelIntermedio2 = new ArrayList<>();
   ArrayList<Ser> tunelIntermedio3 = new ArrayList<>();
   ArrayList<Ser> tunelIntermedio4 = new ArrayList<>();


   ArrayList<ArrayList<Ser>> listaTunelesEntrar= new ArrayList<>();
   ArrayList<Ser> tunelEntrar1=new ArrayList<>();
   ArrayList<Ser> tunelEntrar2=new ArrayList<>();
   ArrayList<Ser> tunelEntrar3=new ArrayList<>();
   ArrayList<Ser> tunelEntrar4=new ArrayList<>();


   ArrayList<ArrayList<Ser>> zona_riesgoZombie= new ArrayList<>();
   ArrayList<Ser> zona_riesgoZombie1=new ArrayList<>();
   ArrayList<Ser> zona_riesgoZombie2=new ArrayList<>();
   ArrayList<Ser> zona_riesgoZombie3=new ArrayList<>();
   ArrayList<Ser> zona_riesgoZombie4=new ArrayList<>();




   public Entorno() throws IOException {
      numSeres=0;
      for(int i=0; i<4;i++){
         tunelesSalirBarreras.add(new CyclicBarrier(3));
         tunelesEntrarBarreras.add(new CyclicBarrier(3));
         tunelesInteriorLock.add(new ReentrantLock());
         tunelesInteriorCondition.add(tunelesInteriorLock.get(i).newCondition());
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

      listaTunelesIntermedio.add(tunelIntermedio1);
      listaTunelesIntermedio.add(tunelIntermedio2);
      listaTunelesIntermedio.add(tunelIntermedio3);
      listaTunelesIntermedio.add(tunelIntermedio4);

      listaTunelesEntrar.add(tunelEntrar1);
      listaTunelesEntrar.add(tunelEntrar2);
      listaTunelesEntrar.add(tunelEntrar3);
      listaTunelesEntrar.add(tunelEntrar4);

      zona_riesgoZombie.add(zona_riesgoZombie1);
      zona_riesgoZombie.add(zona_riesgoZombie2);
      zona_riesgoZombie.add(zona_riesgoZombie3);
      zona_riesgoZombie.add(zona_riesgoZombie4);


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
   public ObservableList<ListView<Ser>> ZonaRiesgoHumanos;

   @FXML
   public ListView<Ser> ZonaRiesgoHumano1;
   @FXML
   public ListView<Ser> ZonaRiesgoHumano2;
   @FXML
   public ListView<Ser> ZonaRiesgoHumano3;
   @FXML
   public ListView<Ser> ZonaRiesgoHumano4;


   public ObservableList<ListView<Ser>> ZonaRiesgoZombies;

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







   public synchronized void meter(Ser t,ListView vista, ArrayList<Ser> lista) {
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
         ObservableList<Ser> observableList = FXCollections.observableArrayList(new ArrayList<>(lista));

         vista.setItems(observableList);
      });
   }






   @FXML
   void onPlayButtonClick(ActionEvent event) {

      for(int i=0;i<10;i++){
         //COMPROBAR QUE SE DUERMEN 0,5/2 SEGUNDOS

         Humano humano= new Humano("H"+ String.format("%04d", i),this);
         humanos.add(humano);
         humano.start();
      }

      Zombie zombie= new Zombie("Z0000", this);
      zombie.start();

//      while(true){
//
//         Humano humano= new Humano("H"+ String.format("%04d", numSeres),this);
//         hilos.add(humano);
//         humano.start();
//         numSeres++;
//      }




   }
   @FXML
   void onStopButtonClick(ActionEvent event) {
      isPaused = true; // Cambia el estado a pausa
      for (Thread thread : humanos) {
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
      ZonaRiesgoHumanos = FXCollections.observableArrayList(
              ZonaRiesgoHumano1,
              ZonaRiesgoHumano2,
              ZonaRiesgoHumano3,
              ZonaRiesgoHumano4
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

      ZonaRiesgoZombies = FXCollections.observableArrayList(
              ZonaRiesgoZombie1,
              ZonaRiesgoZombie2,
              ZonaRiesgoZombie3,
              ZonaRiesgoZombie4
      );


   }

}
