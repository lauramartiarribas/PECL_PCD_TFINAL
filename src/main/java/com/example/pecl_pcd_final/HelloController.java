package com.example.pecl_pcd_final;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController {

    @FXML
    private Button startButton;

    @FXML
    void onStartButtonClick(ActionEvent event) {
        // Cerrar la ventana actual
        Stage stageBorrar = (Stage) startButton.getScene().getWindow();
        stageBorrar.close();

        // Cargar la nueva escena
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProgramaP.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setTitle("Programa");
            stage.setScene(scene);
            stage.setResizable(false);

            stage.setHeight(750);
            stage.setWidth(1400);
            stage.show();
            stage.setOnCloseRequest(evento ->{
                Platform.exit();
                System.exit(0);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
