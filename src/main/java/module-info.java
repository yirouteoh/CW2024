module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;
    requires java.logging;

    exports com.example.demo.controller;
    exports com.example.demo.actors;
    exports com.example.demo.actors.plane;
    exports com.example.demo.actors.projectile;
    exports com.example.demo.levels;
    exports com.example.demo.powerups;
    exports com.example.demo.sounds;
    exports com.example.demo.utils;
    exports com.example.demo.managers;
    exports com.example.demo.screens;

    opens com.example.demo.actors to javafx.fxml;
    opens com.example.demo.actors.plane to javafx.fxml;
    opens com.example.demo.actors.projectile to javafx.fxml;
    opens com.example.demo.levels to javafx.fxml;
    opens com.example.demo.powerups to javafx.fxml;
    opens com.example.demo.sounds to javafx.fxml;
    opens com.example.demo.utils to javafx.fxml;
    opens com.example.demo.managers to javafx.fxml;
    opens com.example.demo.screens to javafx.fxml;
}
