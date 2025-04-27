package com.example.pecl_pcd_final;


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

   private int numHumanos=1;
   private ListaHilos listaHumanos;

   /// Refugio ///
   private ListaHilos descanso;
   private ListaHilos comedor_espera;
   private ListaHilos comedor_comiendo;
   private ListaHilos zona_comun;

   private Lock lockComida= new ReentrantLock();

   private ConcurrentLinkedQueue<Integer> comidaTotal= new ConcurrentLinkedQueue<>();



   /// Túneles ///


//   private ArrayList<CyclicBarrier> tunelesSalirBarreras= new ArrayList<>();
//   private ArrayList<Semaphore> tunelesSalirSemaforo= new ArrayList<>();
//
//   private ArrayList<Lock> tunelesInteriorLock= new ArrayList<>();
//   private ArrayList<Condition> tunelesInteriorCondition= new ArrayList<>(4);
//   private AtomicIntegerArray hayPrioridad= new AtomicIntegerArray(4);
//
//   private int esperandoIda=0;
//   private int esperandoVuelta=0;
//   private ArrayList<Object> control ;





   private ArrayList<Tunel> listaTuneles= new ArrayList<>();



   private ArrayList<ListaHilos> listaTunelesSalir= new ArrayList<>();

   private ArrayList<ListaHilos> listaTunelesIntermedio = new ArrayList<>();

   private ArrayList<ListaHilos> listaTunelesEntrar= new ArrayList<>();




   /// Zona de riesgo ///
   private ArrayList<ListaHilos> zona_riesgoHumanos= new ArrayList<>();

   private ArrayList<ListaHilos> zona_riesgoZombie= new ArrayList<>();




   public Entorno(ListaHilos descanso, ListaHilos comedor_espera, ListaHilos comedor_comiendo, ListaHilos zona_comun,
                  ListaHilos tunelSalir1, ListaHilos tunelSalir2, ListaHilos tunelSalir3, ListaHilos tunelSalir4,
                  ListaHilos tunelIntermedio1, ListaHilos tunelIntermedio2, ListaHilos tunelIntermedio3, ListaHilos tunelIntermedio4 ,
                  ListaHilos tunelEntrar1, ListaHilos tunelEntrar2, ListaHilos tunelEntrar3, ListaHilos tunelEntrar4,
                  ListaHilos zona_riesgoHumano1,ListaHilos zona_riesgoHumano2,ListaHilos zona_riesgoHumano3,ListaHilos zona_riesgoHumano4,
                  ListaHilos zona_riesgoZombie1, ListaHilos zona_riesgoZombie2, ListaHilos zona_riesgoZombie3, ListaHilos zona_riesgoZombie4) throws IOException {

      this.descanso=descanso;
      this.comedor_espera=comedor_espera;
      this.comedor_comiendo=comedor_comiendo;
      this.zona_comun=zona_comun;


      for(int i=0; i<4; i++){
         this.listaTuneles.add(new Tunel());
      }



//      for(int i=0; i<4;i++){
//         tunelesSalirBarreras.add(new CyclicBarrier(3));
//         tunelesSalirSemaforo.add(new Semaphore(1));
//         tunelesInteriorLock.add(new ReentrantLock());
//         //tunelesInteriorCondition.add(tunelesInteriorLock.get(i).newCondition());
//
//      }

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

      zona_riesgoHumanos.add(zona_riesgoHumano1);
      zona_riesgoHumanos.add(zona_riesgoHumano2);
      zona_riesgoHumanos.add(zona_riesgoHumano3);
      zona_riesgoHumanos.add(zona_riesgoHumano4);

      zona_riesgoZombie.add(zona_riesgoZombie1);
      zona_riesgoZombie.add(zona_riesgoZombie2);
      zona_riesgoZombie.add(zona_riesgoZombie3);
      zona_riesgoZombie.add(zona_riesgoZombie4);


   }






//   //Para sacar los hilos por pantalla
//   public synchronized void meter(Ser t, ListView vista, ArrayList<Ser> lista) {
//      lista.remove(t); // evita duplicados
//      lista.add(t);
//      Platform.runLater(() -> imprimir(vista, lista));
//   }
//
//
//   public synchronized void sacar(Ser t,ListView vista,ArrayList<Ser> lista) {
//      lista.remove(t);
//      Platform.runLater(() -> {
//         imprimir(vista, lista);
//      });
//
//   }
//
//   public synchronized void imprimir(ListView<Ser> vista, ArrayList<Ser> lista) {
//      Platform.runLater(() -> {
//         ObservableList<Ser> observableList = FXCollections.observableArrayList(new ArrayList<>(lista));
//
//         vista.setItems(observableList);
//      });
//   }






//   public int sumaComidaLista(){
//      int suma = 0;
//      for (Integer j : comidaTotal) {
//         suma += j;
//      }
//      return suma;
//   }
//
//   public void actualizarLabelComida(){
//      Platform.runLater(() -> {
//         Comida.setText(String.valueOf(sumaComidaLista()));
//      });
//   }





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


            //humanos.add(humano);
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






   //Getters y setters
   public ListaHilos getDescanso() {
      return descanso;
   }

   public ListaHilos getComedor_espera() {
      return comedor_espera;
   }

   public ListaHilos getComedor_comiendo() {
      return comedor_comiendo;
   }

   public ListaHilos getZona_comun() {
      return zona_comun;
   }

   public Lock getLockComida() {
      return lockComida;
   }

   public ConcurrentLinkedQueue<Integer> getComidaTotal() {
      return comidaTotal;
   }



//   public ArrayList<CyclicBarrier> getTunelesSalirBarreras() {
//      return tunelesSalirBarreras;
//   }
//
//   public ArrayList<Lock> getTunelesInteriorLock() {
//      return tunelesInteriorLock;
//   }
//
//   public ArrayList<Condition> getTunelesInteriorCondition() {
//      return tunelesInteriorCondition;
//   }
//
//   public AtomicIntegerArray getHayPrioridad() {
//      return hayPrioridad;
//   }
//

   public ArrayList<ListaHilos> getZona_riesgoHumanos() {
      return zona_riesgoHumanos;
   }

   public ArrayList<ListaHilos> getListaTunelesSalir() {
      return listaTunelesSalir;
   }

   public ArrayList<ListaHilos> getListaTunelesIntermedio() {
      return listaTunelesIntermedio;
   }

   public ArrayList<ListaHilos> getListaTunelesEntrar() {
      return listaTunelesEntrar;
   }

   public ArrayList<ListaHilos> getZona_riesgoZombie() {
      return zona_riesgoZombie;
   }


//   public ArrayList<Semaphore> getTunelesSalirSemaforo(){return tunelesSalirSemaforo; }


   public ArrayList<Tunel> getListaTuneles() {
      return listaTuneles;
   }

   public void setListaTuneles(ArrayList<Tunel> listaTuneles) {
      this.listaTuneles = listaTuneles;
   }
}
