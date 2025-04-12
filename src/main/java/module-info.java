module com.example.pecl_pcd_final {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.logging;

    opens com.example.pecl_pcd_final to javafx.fxml;
    exports com.example.pecl_pcd_final;
}