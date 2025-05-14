package com.example.pecl_pcd_final;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ControladorDatosDistribuidos {


    private InterfazMonitor interfazRemota;


    @FXML
    public TextField textoHumanosRefugio;

    @FXML
    public TextField textoHumanostuneles0;

    @FXML
    public TextField textoHumanostuneles1;
    @FXML
    public TextField textoHumanostuneles2;
    @FXML
    public TextField textoHumanostuneles3;

    @FXML
    public TextField textoHumanosRiesgo0;
    @FXML
    public TextField textoHumanosRiesgo1;
    @FXML
    public TextField textoHumanosRiesgo2;
    @FXML
    public TextField textoHumanosRiesgo3;

    @FXML
    public TextField textoZombiesRiesgo0;
    @FXML
    public TextField textoZombiesRiesgo1;
    @FXML
    public TextField textoZombiesRiesgo2;
    @FXML
    public TextField textoZombiesRiesgo3;

    @FXML
    public TextArea textoZombiesLetales;

    @FXML
    public Button botonStopReanudar;



    public void setInterfaz(InterfazMonitor interfazMonitor) {
        this.interfazRemota = interfazMonitor;
    }

    @FXML
    void onStopReanudarButtonClick(ActionEvent event) {
        try {
            interfazRemota.pausarReanudar();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }




}
