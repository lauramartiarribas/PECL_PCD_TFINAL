package com.example.pecl_pcd_final;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;
import java.util.logging.Logger;


public class Entorno {

   ///El logger///
   private Logger logger = LoggerConFichero.getLogger();

   ///Elementos del juego ///
   //Para cuando queramos pausar el juego y así poder también almacenar todos los hilos que se tienen en ese momento
   private boolean enPausa = false;

   private int numHumanos;
   private ArrayList<Thread> humanos = new ArrayList<>();

   /// Refugio ///
   private ArrayList<Ser> descanso= new ArrayList<>();
   private ArrayList<Ser> comedor_espera= new ArrayList<>();
   private ArrayList<Ser> comedor_comiendo= new ArrayList<>();
   private ArrayList<Ser> zona_comun= new ArrayList<>();

   private Lock lockComida= new ReentrantLock();

   private ConcurrentLinkedQueue<Integer> comidaTotal= new ConcurrentLinkedQueue<>();



   /// Túneles ///
   private ArrayList<CyclicBarrier> tunelesSalirBarreras= new ArrayList<>();
   private ArrayList<Semaphore> tunelesSalirSemaforo= new ArrayList<>();

   private ArrayList<Lock> tunelesInteriorLock= new ArrayList<>();
   private ArrayList<Condition> tunelesInteriorCondition= new ArrayList<>(4);
   private AtomicIntegerArray hayPrioridad= new AtomicIntegerArray(4);


   private ArrayList<ArrayList<Ser>> listaTunelesSalir= new ArrayList<>();
   private ArrayList<Ser> tunelSalir1=new ArrayList<>();
   private ArrayList<Ser> tunelSalir2=new ArrayList<>();
   private ArrayList<Ser> tunelSalir3=new ArrayList<>();
   private ArrayList<Ser> tunelSalir4=new ArrayList<>();

   private ArrayList<ArrayList<Ser>> listaTunelesIntermedio = new ArrayList<>();
   private ArrayList<Ser> tunelIntermedio1 = new ArrayList<>();
   private ArrayList<Ser> tunelIntermedio2 = new ArrayList<>();
   private ArrayList<Ser> tunelIntermedio3 = new ArrayList<>();
   private ArrayList<Ser> tunelIntermedio4 = new ArrayList<>();

   private ArrayList<ArrayList<Ser>> listaTunelesEntrar= new ArrayList<>();
   private ArrayList<Ser> tunelEntrar1=new ArrayList<>();
   private ArrayList<Ser> tunelEntrar2=new ArrayList<>();
   private ArrayList<Ser> tunelEntrar3=new ArrayList<>();
   private ArrayList<Ser> tunelEntrar4=new ArrayList<>();



   /// Zona de riesgo ///
   private ArrayList<ArrayList<Ser>> zona_riesgoHumanos= new ArrayList<>();
   private ArrayList<Ser> zona_riesgoHumano1=new ArrayList<>();
   private ArrayList<Ser> zona_riesgoHumano2=new ArrayList<>();
   private ArrayList<Ser> zona_riesgoHumano3=new ArrayList<>();
   private ArrayList<Ser> zona_riesgoHumano4=new ArrayList<>();

   private ArrayList<ArrayList<Ser>> zona_riesgoZombie= new ArrayList<>();
   private ArrayList<Ser> zona_riesgoZombie1=new ArrayList<>();
   private ArrayList<Ser> zona_riesgoZombie2=new ArrayList<>();
   private ArrayList<Ser> zona_riesgoZombie3=new ArrayList<>();
   private ArrayList<Ser> zona_riesgoZombie4=new ArrayList<>();




   public Entorno() throws IOException {
      numHumanos =1;
      for(int i=0; i<4;i++){
         tunelesSalirBarreras.add(new CyclicBarrier(3));
         tunelesSalirSemaforo.add(new Semaphore(3));
         tunelesInteriorLock.add(new ReentrantLock());
         tunelesInteriorCondition.add(tunelesInteriorLock.get(i).newCondition());

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
   public Button BotonInformacion;

   ///////////////// REFUGIO //////////////////////
   @FXML
   public ListView ListaDescanso;

   @FXML
   public ListView ListaComedorEspera;

   @FXML
   public ListView ListaComedorComiendo;

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






   //Para sacar los hilos por pantalla
   public synchronized void meter(Ser t, ListView vista, ArrayList<Ser> lista) {
      lista.remove(t); // evita duplicados
      lista.add(t);
      Platform.runLater(() -> imprimir(vista, lista));
   }


   public synchronized void sacar(Ser t,ListView vista,ArrayList<Ser> lista) {
      lista.remove(t);
      Platform.runLater(() -> {
         imprimir(vista, lista);
      });

   }

   public synchronized void imprimir(ListView<Ser> vista, ArrayList<Ser> lista) {
      Platform.runLater(() -> {
         ObservableList<Ser> observableList = FXCollections.observableArrayList(new ArrayList<>(lista));

         vista.setItems(observableList);
      });
   }

   public int sumaComidaLista(){
      int suma = 0;
      for (Integer j : comidaTotal) {
         suma += j;
      }
      return suma;
   }

   public void actualizarLabelComida(){
      Platform.runLater(() -> {
         Comida.setText(String.valueOf(sumaComidaLista()));
      });
   }




   //Botones

   @FXML
   void onPlayButtonClick(ActionEvent event) {
      PlayButton.setDisable(true);
      PauseButton.setDisable(false);
      enPausa=false;

      nacerHumanos();

      //Creamos el zombie
      Zombie zombie= new Zombie("Z0000", this);
      zombie.start();

   }

   @FXML
   void onStopButtonClick(ActionEvent event) throws InterruptedException {
      pausar();
      PauseButton.setDisable(true);
      ReanudarButton.setDisable(false);
   }

   @FXML
   void onReanudarButtonClick(ActionEvent event){
      reanudar();
      PauseButton.setDisable(false);
      ReanudarButton.setDisable(true);
   }

   public synchronized void pausar(){
      enPausa=true;
   }

   public synchronized void comprobarPausa() throws InterruptedException{
      while(enPausa){
         wait();
      }
   }

   public synchronized void reanudar(){
      enPausa = false; // Cambia el estado a reanudado
      notifyAll();
   }

   public void nacerHumanos(){
      new Thread(() ->{
         ///while (true)
         for(int i=0;i<14;i++){

            Humano humano= new Humano("H"+ String.format("%04d", numHumanos),this);
            numHumanos++;



            humanos.add(humano);
            humano.start();
            try {
               this.comprobarPausa();
               humano.sleep(500+(int)Math.random()*1500);
               this.comprobarPausa();
            } catch (InterruptedException e) {
               throw new RuntimeException(e);
            }

         }
      }).start();


   }



   @FXML
   void onInformacionButtonClick(){
      try {
         pausar();
         PauseButton.setDisable(true);
         ReanudarButton.setDisable(false);
         Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
         infoAlert.setTitle("Información del Juego");
         infoAlert.setHeaderText("Estás jugando a: Apocalipsis Zombie");
         TextArea textArea = new TextArea(
                 "Es el año 2025 y se ha desencadenado un apocalipsis zombie.\n\n" +
                         "Sólo se tiene un refugio seguro para los humanos, pero deben cruzar a la zona de riesgo por los túneles para recoger comida.\n\n" +
                         "¡Cuidado con los zombies, podrán atacar e incluso convertir a los humanos!"
         );
         textArea.setWrapText(true); // Para que el texto se ajuste
         textArea.setEditable(false); // Para que no se pueda editar
         textArea.setStyle("-fx-font-family: 'Bookman Old Style'");

         // Ajustar el tamaño del área
         textArea.setMaxWidth(Double.MAX_VALUE);
         textArea.setMaxHeight(Double.MAX_VALUE);

         infoAlert.getDialogPane().setContent(textArea);infoAlert.showAndWait();

      } catch (Exception e) {
         e.printStackTrace();
      }
   }



   @FXML
   public  void  initialize() {
      PauseButton.setDisable(true);
      ReanudarButton.setDisable(true);
      Comida.setText(String.valueOf(0));
      logger.info("Inicializado correctamente.");
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



   //Getters y setters
   public ArrayList<Ser> getDescanso() {
      return descanso;
   }

   public ArrayList<Ser> getComedor_espera() {
      return comedor_espera;
   }

   public ArrayList<Ser> getComedor_comiendo() {
      return comedor_comiendo;
   }

   public ArrayList<Ser> getZona_comun() {
      return zona_comun;
   }

   public Lock getLockComida() {
      return lockComida;
   }

   public ConcurrentLinkedQueue<Integer> getComidaTotal() {
      return comidaTotal;
   }

   public ArrayList<Thread> getHumanos() {
      return humanos;
   }

   public ArrayList<CyclicBarrier> getTunelesSalirBarreras() {
      return tunelesSalirBarreras;
   }

   public ArrayList<Lock> getTunelesInteriorLock() {
      return tunelesInteriorLock;
   }

   public ArrayList<Condition> getTunelesInteriorCondition() {
      return tunelesInteriorCondition;
   }

   public AtomicIntegerArray getHayPrioridad() {
      return hayPrioridad;
   }


   public ArrayList<ArrayList<Ser>> getZona_riesgoHumanos() {
      return zona_riesgoHumanos;
   }

   public ArrayList<ArrayList<Ser>> getListaTunelesSalir() {
      return listaTunelesSalir;
   }

   public ArrayList<ArrayList<Ser>> getListaTunelesIntermedio() {
      return listaTunelesIntermedio;
   }

   public ArrayList<ArrayList<Ser>> getListaTunelesEntrar() {
      return listaTunelesEntrar;
   }

   public ArrayList<ArrayList<Ser>> getZona_riesgoZombie() {
      return zona_riesgoZombie;
   }
   public ArrayList<Semaphore> getTunelesSalirSemaforo(){return tunelesSalirSemaforo; }
}
