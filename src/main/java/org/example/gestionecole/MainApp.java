package org.example.gestionecole;

import org.example.gestionecole.config.DatabaseConfig;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.utils.ViewManager;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The main application class for the School Management System.
 */
public class MainApp extends Application {
    private static final Logger logger = LogManager.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            // Set application title
            primaryStage.setTitle("Gestion École");

            // Initialize the ViewManager with the primary stage
            ViewManager.setPrimaryStage(primaryStage);

            Locale locale = Locale.FRENCH;
            ResourceBundle resourceBundle = ResourceBundle.getBundle("org.example.gestionecole.i18n.messages", locale);

            // Load the login view as the initial view
            ViewManager.switchView("/org/example/gestionecole/LoginView.fxml");

            logger.info("Application started successfully.");
        } catch (Exception e) {
            logger.error("Error starting the application.", e);
        }
    }

    @Override
    public void stop() {
        try {
            // Close the database connection gracefully
            DatabaseConfig.closeConnection();
            logger.info("Application stopped gracefully.");
        } catch (Exception e) {
            logger.error("Error stopping the application.", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
