package com.example.pecl_pcd_final;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class ControladorInicio {

    @FXML
    private Button startButton;

    @FXML
    void onStartButtonClick(ActionEvent event) {
        // Cerrar la ventana actual
        Stage stageBorrar = (Stage) startButton.getScene().getWindow();
        stageBorrar.close();

        // Cargar la nueva escena
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ApocalipsisZombie.class.getResource("Entorno.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setTitle("Apocalipsis Zombie");
            stage.setScene(scene);
            stage.setResizable(false);

            stage.setHeight(750);
            stage.setWidth(1400);
            stage.show();
            stage.setOnCloseRequest(evento -> {
                evento.consume(); // Detiene el cierre automático

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmar salida");
                confirmAlert.setHeaderText("¿Estás seguro/a de que quieres cerrar?");
                confirmAlert.setContentText("Se perderá la partida actual.");

                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Platform.exit();
                    System.exit(0);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private Button BotonInformacion;

    @FXML
    void onInformacionButtonClick(){
        try {
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Información del Juego");
            infoAlert.setHeaderText("¡Bienvenido al Juego de Apocalipsis Zombie!");
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
}
