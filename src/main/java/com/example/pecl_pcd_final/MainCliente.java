package com.example.pecl_pcd_final;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
        InetAddress direccion = InetAddress.getLocalHost();
        String ip = direccion.getHostAddress();
        InterfazMonitor interfazMonitor = (InterfazMonitor) Naming.lookup("//"+ip+"/ImplementacionInterfaz");
//        while (!controladorDatos.getImplementacionInterfaz().getEntorno().isEnPausa()){
//            //controladorDatos.actualizarDatos(interfazMonitor);
//
//
//            int humanosRefugioNum = interfazMonitor.getNumHumanosRefugio();
//            controladorDatos.textoHumanosRefugio.setText(String.valueOf(humanosRefugioNum));
//            int humanosTunel0= interfazMonitor.getNumHumanosTunel(0);
//            controladorDatos.textoHumanostuneles0.setText(String.valueOf(humanosTunel0));
//            int humanosTunel1 = interfazMonitor.getNumHumanosTunel(1);
//            controladorDatos.textoHumanostuneles1.setText(String.valueOf(humanosTunel1));
//            int humanosTunel2 = interfazMonitor.getNumHumanosTunel(2);
//            controladorDatos.textoHumanostuneles2.setText(String.valueOf(humanosTunel2));
//            int humanosTunel3 = interfazMonitor.getNumHumanosTunel(3);
//            controladorDatos.textoHumanostuneles3.setText(String.valueOf(humanosTunel3));
//
//            int humanosRiesgo0 = interfazMonitor.getNumZonaInseguraHumanos(0);
//            controladorDatos.textoHumanosRiesgo0.setText(String.valueOf(humanosRiesgo0));
//            int humanosRiesgo1 = interfazMonitor.getNumZonaInseguraHumanos(1);
//            controladorDatos.textoHumanosRiesgo1.setText(String.valueOf(humanosRiesgo1));
//            int humanosRiesgo2 = interfazMonitor.getNumZonaInseguraHumanos(2);
//            controladorDatos.textoHumanosRiesgo2.setText(String.valueOf(humanosRiesgo2));
//            int humanosRiesgo3 = interfazMonitor.getNumZonaInseguraHumanos(3);
//            controladorDatos.textoHumanosRiesgo3.setText(String.valueOf(humanosRiesgo3));
//
//            int zombiesRiesgo0 = interfazMonitor.getNumZonaInseguraZombies(0);
//            controladorDatos.textoZombiesRiesgo0.setText(String.valueOf(zombiesRiesgo0));
//            int zombiesRiesgo1 = interfazMonitor.getNumZonaInseguraZombies(1);
//            controladorDatos.textoZombiesRiesgo1.setText(String.valueOf(zombiesRiesgo1));
//            int zombiesRiesgo2 = interfazMonitor.getNumZonaInseguraZombies(2);
//            controladorDatos.textoZombiesRiesgo2.setText(String.valueOf(zombiesRiesgo2));
//            int zombiesRiesgo3 = interfazMonitor.getNumZonaInseguraZombies(3);
//            controladorDatos.textoZombiesRiesgo3.setText(String.valueOf(zombiesRiesgo3));
//
//            ArrayList<String> zombisLetales= interfazMonitor.getLetales();
//            String texto = String.join("\n", zombisLetales);
//            controladorDatos.textoZombiesLetales.setText(texto);
//
//        }
        Thread actualizador = new Thread(() -> {
            while (true) {
                try {
                    int humanosRefugioNum = interfazMonitor.getNumHumanosRefugio();
                    int humanosTunel0 = interfazMonitor.getNumHumanosTunel(0);
                    int humanosTunel1 = interfazMonitor.getNumHumanosTunel(1);
                    int humanosTunel2 = interfazMonitor.getNumHumanosTunel(2);
                    int humanosTunel3 = interfazMonitor.getNumHumanosTunel(3);

                    int humanosRiesgo0 = interfazMonitor.getNumZonaInseguraHumanos(0);
                    int humanosRiesgo1 = interfazMonitor.getNumZonaInseguraHumanos(1);
                    int humanosRiesgo2 = interfazMonitor.getNumZonaInseguraHumanos(2);
                    int humanosRiesgo3 = interfazMonitor.getNumZonaInseguraHumanos(3);

                    int zombiesRiesgo0 = interfazMonitor.getNumZonaInseguraZombies(0);
                    int zombiesRiesgo1 = interfazMonitor.getNumZonaInseguraZombies(1);
                    int zombiesRiesgo2 = interfazMonitor.getNumZonaInseguraZombies(2);
                    int zombiesRiesgo3 = interfazMonitor.getNumZonaInseguraZombies(3);

                    ArrayList<String> zombisLetales = interfazMonitor.getLetales();

                    // ACTUALIZAR LA INTERFAZ
                    Platform.runLater(() -> {
                        controladorDatos.textoHumanosRefugio.setText(String.valueOf(humanosRefugioNum));
                        controladorDatos.textoHumanostuneles0.setText(String.valueOf(humanosTunel0));
                        controladorDatos.textoHumanostuneles1.setText(String.valueOf(humanosTunel1));
                        controladorDatos.textoHumanostuneles2.setText(String.valueOf(humanosTunel2));
                        controladorDatos.textoHumanostuneles3.setText(String.valueOf(humanosTunel3));

                        controladorDatos.textoHumanosRiesgo0.setText(String.valueOf(humanosRiesgo0));
                        controladorDatos.textoHumanosRiesgo1.setText(String.valueOf(humanosRiesgo1));
                        controladorDatos.textoHumanosRiesgo2.setText(String.valueOf(humanosRiesgo2));
                        controladorDatos.textoHumanosRiesgo3.setText(String.valueOf(humanosRiesgo3));

                        controladorDatos.textoZombiesRiesgo0.setText(String.valueOf(zombiesRiesgo0));
                        controladorDatos.textoZombiesRiesgo1.setText(String.valueOf(zombiesRiesgo1));
                        controladorDatos.textoZombiesRiesgo2.setText(String.valueOf(zombiesRiesgo2));
                        controladorDatos.textoZombiesRiesgo3.setText(String.valueOf(zombiesRiesgo3));

                        controladorDatos.textoZombiesLetales.setText(String.join("\n", zombisLetales));
                    });

                    Thread.sleep(100); // Pequeña pausa para no saturar la CPU
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        actualizador.setDaemon(true); // El hilo se cierra al cerrar la app
        actualizador.start();

    }



    public static void main(String[] args) {
        launch();
    }
}
