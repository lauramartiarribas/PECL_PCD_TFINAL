package com.example.pecl_pcd_final;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;


public class ControladorEntorno {

    private Entorno entorno;
    private Logger logger = LoggerConFichero.getLogger();

    @FXML
    public Button PlayButton;
    @FXML
    public Button PauseButton;
    @FXML
    public Button ReanudarButton;
    @FXML
    public Button BotonInformacion;


    ///////////////// REFUGIO //////////////////////
    @FXML
    public ListView ListaDescanso;
    @FXML
    public ListView ListaComedorEspera;
    @FXML
    public ListView ListaComedorComiendo;
    @FXML
    public ListView<Ser> ListaZonaComun;


    ///////////////// TUNELES //////////////////////
    @FXML
    public ListView<Ser> TunelSalir1;
    @FXML
    public ListView<Ser> TunelSalir2;
    @FXML
    public ListView<Ser> TunelSalir3;
    @FXML
    public ListView<Ser> TunelSalir4;


    @FXML
    public ListView<Ser> TunelIntermedio1;
    @FXML
    public ListView<Ser> TunelIntermedio2;
    @FXML
    public ListView<Ser> TunelIntermedio3;
    @FXML
    public ListView<Ser> TunelIntermedio4;


    @FXML
    public ListView<Ser> TunelEntrar1;
    @FXML
    public ListView<Ser> TunelEntrar2;
    @FXML
    public ListView<Ser> TunelEntrar3;
    @FXML
    public ListView<Ser> TunelEntrar4;


    ///////////////// ZONA DE RIESGO ///////////////////////////
    @FXML
    public ListView<Ser> ZonaRiesgoHumano1;
    @FXML
    public ListView<Ser> ZonaRiesgoHumano2;
    @FXML
    public ListView<Ser> ZonaRiesgoHumano3;
    @FXML
    public ListView<Ser> ZonaRiesgoHumano4;


    @FXML
    public ListView<Ser> ZonaRiesgoZombie1;
    @FXML
    public ListView<Ser> ZonaRiesgoZombie2;
    @FXML
    public ListView<Ser> ZonaRiesgoZombie3;
    @FXML
    public ListView<Ser> ZonaRiesgoZombie4;

    @FXML
    public TextField Comida;


    ///////////////// FUNCIONES DE LOS BOTONES ///////////////////////////
    @FXML
    void onPlayButtonClick(ActionEvent event) {
        PlayButton.setDisable(true);
        PauseButton.setDisable(false);
        HiloGenerador hilo = new HiloGenerador(entorno, 9999);
        hilo.start();
    }

    @FXML
    void onStopButtonClick(ActionEvent event) throws InterruptedException {
        entorno.pausar();
        PauseButton.setDisable(true);
        ReanudarButton.setDisable(false);
    }

    @FXML
    void onReanudarButtonClick(ActionEvent event) {
        entorno.reanudar();
        PauseButton.setDisable(false);
        ReanudarButton.setDisable(true);
    }


    @FXML
    void onInformacionButtonClick() {
        try {
            entorno.pausar();
            PauseButton.setDisable(true);
            ReanudarButton.setDisable(false);
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Información del Juego");
            infoAlert.setHeaderText("Estás jugando a: Apocalipsis Zombie");
            TextArea textArea = new TextArea(
                    "Es el año 2025 y se ha desencadenado un apocalipsis zombie.\n\n" +
                            "Sólo se tiene un refugio seguro para los humanos, pero deben cruzar a la zona de riesgo por los túneles para recoger Pla.\n\n" +
                            "¡Cuidado con los zombies, podrán atacar e incluso convertir a los humanos!"
            );
            textArea.setWrapText(true); // Para que el texto se ajuste
            textArea.setEditable(false); // Para que no se pueda editar
            textArea.setStyle("-fx-font-family: 'Bookman Old Style'");

            // Ajustar el tamaño del área
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);

            infoAlert.getDialogPane().setContent(textArea);
            infoAlert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void initialize() throws IOException {
        this.entorno = new Entorno(
                new ListaHilos(ListaDescanso),
                new ListaHilos(ListaComedorEspera),
                new ListaHilos(ListaComedorComiendo),
                new ListaHilos(ListaZonaComun),
                new ListaHilos(TunelSalir1),
                new ListaHilos(TunelSalir2),
                new ListaHilos(TunelSalir3),
                new ListaHilos(TunelSalir4),
                new ListaHilos(TunelIntermedio1),
                new ListaHilos(TunelIntermedio2),
                new ListaHilos(TunelIntermedio3),
                new ListaHilos(TunelIntermedio4),
                new ListaHilos(TunelEntrar1),
                new ListaHilos(TunelEntrar2),
                new ListaHilos(TunelEntrar3),
                new ListaHilos(TunelEntrar4),
                new ListaHilos(ZonaRiesgoHumano1),
                new ListaHilos(ZonaRiesgoHumano2),
                new ListaHilos(ZonaRiesgoHumano3),
                new ListaHilos(ZonaRiesgoHumano4),
                new ListaHilos(ZonaRiesgoZombie1),
                new ListaHilos(ZonaRiesgoZombie2),
                new ListaHilos(ZonaRiesgoZombie3),
                new ListaHilos(ZonaRiesgoZombie4),
                Comida
        );
        PauseButton.setDisable(true);
        ReanudarButton.setDisable(true);
        Comida.setText(String.valueOf(0));
        try {
            ImplementacionInterfaz implementacionInterfaz = new ImplementacionInterfaz(entorno);
            Registry registry = LocateRegistry.createRegistry(1099);
            InetAddress direccion = InetAddress.getLocalHost();
            String ip = direccion.getHostAddress();
            Naming.rebind("//" + ip + "/ImplementacionInterfaz", implementacionInterfaz);
            logger.info("Servidor RMI iniciado correctamente.");
        } catch (RemoteException | MalformedURLException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
