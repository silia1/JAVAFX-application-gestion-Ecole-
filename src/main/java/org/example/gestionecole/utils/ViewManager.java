package org.example.gestionecole.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewManager {
    private static final Logger logger = LogManager.getLogger(ViewManager.class);
    private static Stage primaryStage;
    private static FXMLLoader currentLoader; // Store the current FXMLLoader
    private static final Map<String, Object> controllersCache = new HashMap<>();

    /**
     * Set the primary stage for the application.
     *
     * @param stage The primary stage.
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Switches the view by loading the specified FXML file and replacing the current scene.
     *
     * @param fxmlPath The path to the FXML file.
     */
    public static void switchView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            currentLoader = loader; // Store the current loader for controller access
            controllersCache.put(fxmlPath, loader.getController()); // Cache the controller

            // Adjust the scene to handle resizing
            Scene scene = new Scene(root);
            bindSceneToStage(scene);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            logger.error("Error switching to view: " + fxmlPath, e);
            throw new RuntimeException("Failed to load FXML file: " + fxmlPath, e);
        }
    }

    /**
     * Returns the controller of the specified FXML file if previously loaded.
     *
     * @param fxmlPath The FXML file path.
     * @return The controller instance, or null if not loaded.
     */
    public static Object getController(String fxmlPath) {
        return controllersCache.get(fxmlPath);
    }

    /**
     * Binds the scene to the primary stage for resizing behavior.
     *
     * @param scene The scene to bind.
     */
    private static void bindSceneToStage(Scene scene) {
        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) ->
                scene.getRoot().setScaleX(newWidth.doubleValue() / primaryStage.getWidth())
        );

        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) ->
                scene.getRoot().setScaleY(newHeight.doubleValue() / primaryStage.getHeight())
        );
    }
}
