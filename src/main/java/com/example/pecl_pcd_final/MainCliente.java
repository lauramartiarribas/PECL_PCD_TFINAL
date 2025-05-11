package com.example.pecl_pcd_final;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Optional;

public class MainCliente extends Application {
    ControladorDatosDistribuidos controladorDatos = new ControladorDatosDistribuidos();

    @Override
    public void start(Stage stage) throws IOException, NotBoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DatosDistribuidos.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Apocalipsis Zombie - Remoto");
        stage.show();
        stage.setOnCloseRequest(evento -> {
            evento.consume(); // Detiene el cierre automático

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar salida");
            confirmAlert.setHeaderText("¿Estás seguro/a de que quieres cerrar los datos en remoto?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });
        controladorDatos = fxmlLoader.getController();

        InterfazMonitor interfazMonitor = (InterfazMonitor) Naming.lookup("//172.22.74.172/ImplementacionInterfaz");
        while (true){
            //controladorDatos.actualizarDatos(interfazMonitor);

            int humanosRefugioNum = interfazMonitor.getNumHumanosRefugio();
            controladorDatos.textoHumanosRefugio.setText(String.valueOf(humanosRefugioNum));
            int humanosTunel0= interfazMonitor.getNumHumanosTunel(0);
            controladorDatos.textoHumanostuneles0.setText(String.valueOf(humanosTunel0));
            int humanosTunel1 = interfazMonitor.getNumHumanosTunel(1);
            controladorDatos.textoHumanostuneles1.setText(String.valueOf(humanosTunel1));
            int humanosTunel2 = interfazMonitor.getNumHumanosTunel(2);
            controladorDatos.textoHumanostuneles2.setText(String.valueOf(humanosTunel2));
            int humanosTunel3 = interfazMonitor.getNumHumanosTunel(3);
            controladorDatos.textoHumanostuneles3.setText(String.valueOf(humanosTunel3));

            int humanosRiesgo0 = interfazMonitor.getNumZonaInseguraHumanos(0);
            controladorDatos.textoHumanosRiesgo0.setText(String.valueOf(humanosRiesgo0));
            int humanosRiesgo1 = interfazMonitor.getNumZonaInseguraHumanos(1);
            controladorDatos.textoHumanosRiesgo1.setText(String.valueOf(humanosRiesgo1));
            int humanosRiesgo2 = interfazMonitor.getNumZonaInseguraHumanos(2);
            controladorDatos.textoHumanosRiesgo2.setText(String.valueOf(humanosRiesgo2));
            int humanosRiesgo3 = interfazMonitor.getNumZonaInseguraHumanos(3);
            controladorDatos.textoHumanosRiesgo3.setText(String.valueOf(humanosRiesgo3));

            int zombiesRiesgo0 = interfazMonitor.getNumZonaInseguraZombies(0);
            controladorDatos.textoZombiesRiesgo0.setText(String.valueOf(zombiesRiesgo0));
            int zombiesRiesgo1 = interfazMonitor.getNumZonaInseguraZombies(1);
            controladorDatos.textoZombiesRiesgo1.setText(String.valueOf(zombiesRiesgo1));
            int zombiesRiesgo2 = interfazMonitor.getNumZonaInseguraZombies(2);
            controladorDatos.textoZombiesRiesgo2.setText(String.valueOf(zombiesRiesgo2));
            int zombiesRiesgo3 = interfazMonitor.getNumZonaInseguraZombies(3);
            controladorDatos.textoZombiesRiesgo3.setText(String.valueOf(zombiesRiesgo3));

            ArrayList<String> zombisLetales= interfazMonitor.getLetales();
            String texto = String.join("\n", zombisLetales);
            controladorDatos.textoZombiesLetales.setText(texto);

        }
    }



    public static void main(String[] args) {
        launch();
    }
}
