package com.example.pecl_pcd_final;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController  {
    @FXML
    private Button startButton;

    @FXML
    void onStartButtonClick(ActionEvent event) {{
        Stage stageBorrar= (Stage) startButton.getScene().getWindow();
        stageBorrar.close();
        Stage stage = new Stage();
        stage.show();
        FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("com/example/pecl_pcd_final/ProgramaP.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Programa");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) { e.printStackTrace();
        }

    }


}}