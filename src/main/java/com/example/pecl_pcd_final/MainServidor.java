package com.example.pecl_pcd_final;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

public class MainServidor extends Application {
    ControladorInicio controlador = new ControladorInicio();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ApocalipsisZombie.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 500);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Apocalipsis Zombie - Start");
        stage.show();
        stage.setOnCloseRequest(evento -> {
            evento.consume(); // Detiene el cierre automático

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar salida");
            confirmAlert.setHeaderText("¿Estás seguro/a de que quieres cerrar el juego?");
            confirmAlert.setContentText("¡Una nueva aventura te espera!");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });

        controlador = fxmlLoader.getController();

        iniciarServidor();
    }


    public void iniciarServidor(){
        try{

            ImplementacionInterfaz implementacionInterfaz= new ImplementacionInterfaz(controlador.getEntorno());
            Registry registry =LocateRegistry.createRegistry(1099);
            Naming.rebind("//172.22.74.172/ImplementacionInterfaz", implementacionInterfaz);
            System.out.println("Servidor RMI iniciado correctamente.");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}