package com.example.pecl_pcd_final;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class ControladorDatosDistribuidos {


    private ImplementacionInterfaz implementacionInterfaz;

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


    public ImplementacionInterfaz getImplementacionInterfaz() {
        return implementacionInterfaz;
    }
}
