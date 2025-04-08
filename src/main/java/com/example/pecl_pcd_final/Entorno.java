package com.example.pecl_pcd_final;

import com.example.pecl_pcd_final.Clases.Humano;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

public class Entorno {
   ArrayList<Humano> descanso;
   ArrayList<Humano> comedor;
   ArrayList<Humano> zona_comun;
   int comida;
   ArrayList<Lock> tuneles;



   @FXML
   public Label LabelDescanso;

   @FXML
   public Label LabelComedor;

   @FXML
   public Label LabelZonaComun;
}
