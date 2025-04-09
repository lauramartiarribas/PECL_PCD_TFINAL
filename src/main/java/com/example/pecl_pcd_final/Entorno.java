package com.example.pecl_pcd_final;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Entorno {
   ArrayList<Humano> descanso;
   ArrayList<Humano> comedor;
   ArrayList<Humano> zona_comun;
   int comida;



   ArrayList<CyclicBarrier> tunelesSalir= new ArrayList<>();
   ArrayList<CyclicBarrier> tunelesEntrar= new ArrayList<>();

   ArrayList<Lock> tunelesInterior= new ArrayList<>();

   ArrayList<Boolean> hayPrioridad= new ArrayList<>();



   ArrayList<Humano> humanos= new ArrayList<>();
   ArrayList<Zombie> zombies= new ArrayList<>();

   ArrayList<Object> zona_riesgo= new ArrayList<>();

   public Entorno(){
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
   public ListView ListaDescanso;

   @FXML
   public ListView ListaComedor;


   @FXML
   public ListView ListaZonaComun;

   @FXML
   public Label Comida;
}
