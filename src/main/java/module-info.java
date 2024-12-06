module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    exports com.example.demo.controller;
    opens com.example.demo.actors to javafx.fxml;
    opens com.example.demo.actors.plane to javafx.fxml;
    opens com.example.demo.actors.projectile to javafx.fxml;
    opens com.example.demo.levels to javafx.fxml;
    opens com.example.demo.powerups to javafx.fxml;
    opens com.example.demo.sounds to javafx.fxml;
    opens com.example.demo.utils to javafx.fxml;
    opens com.example.demo.screens to javafx.fxml;
    opens com.example.demo.managers to javafx.fxml;
}