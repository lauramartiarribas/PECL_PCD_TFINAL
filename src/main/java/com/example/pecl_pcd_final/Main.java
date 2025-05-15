package com.example.pecl_pcd_final;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Main extends Application {



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




    }


    public static void main(String[] args) {
        launch();
    }
}