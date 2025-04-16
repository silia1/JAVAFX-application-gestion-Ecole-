module org.example.gestionecole {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j;
    requires java.sql;
    requires static lombok;
    requires itextpdf;
    requires java.mail;

    opens org.example.gestionecole to javafx.fxml;
    exports org.example.gestionecole;
    exports org.example.gestionecole.controllers;
    opens org.example.gestionecole.controllers to javafx.fxml;
    opens org.example.gestionecole.entities to javafx.base; // Add this line
}