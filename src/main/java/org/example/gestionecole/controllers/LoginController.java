package org.example.gestionecole.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.entities.Role;
import org.example.gestionecole.entities.Utilisateur;
import org.example.gestionecole.services.UtilisateurService;
import org.example.gestionecole.utils.NotificationManager;
import org.example.gestionecole.utils.SessionManager;

import java.io.IOException;

public class LoginController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    private final UtilisateurService utilisateurService = new UtilisateurService();

    @FXML
    private Label errorLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private ImageView loginImage;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        loginImage.setImage(new Image(getClass().getResourceAsStream("/images/cropped-ensa.png")));
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            Utilisateur utilisateur = utilisateurService.authenticate(username, password);

            if (utilisateur != null) {
                logger.info("User authenticated successfully: " + utilisateur.getUsername());

                // Set session
                SessionManager.setCurrentUser(utilisateur);

                // If the user is a professor, retrieve their associated professor ID
                if (utilisateur.getRole() == Role.professeur) {
                    int professeurId = utilisateurService.getProfesseurIdByUserId(utilisateur.getId());
                    SessionManager.setCurrentProfessorId(professeurId);
                }

                openDashboard(utilisateur.getRole());
            } else {
                logger.warn("Invalid login attempt for username: " + username);
                errorLabel.setText("Invalid username or password.");
                errorLabel.setVisible(true);
            }
        } catch (Exception e) {
            logger.error("Error during login process.", e);
            errorLabel.setText("An error occurred. Please try again.");
            errorLabel.setVisible(true);
        }
    }

    private void openDashboard(Role role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gestionecole/DashboardView.fxml"));
            Parent root = loader.load();

            // Ensure controller is properly set up
            DashboardController dashboardController = loader.getController();
            if (dashboardController != null) {
                dashboardController.setUserRole(role);
            } else {
                logger.error("DashboardController not found in FXML loader.");
            }

            // Set the scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1024, 768));
            stage.centerOnScreen();
            stage.show();

            logger.info("Navigated to dashboard for role: " + role);
        } catch (IOException e) {
            logger.error("Error loading DashboardView.fxml. Ensure the file path is correct and the FXML is valid.", e);
            NotificationManager.showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the dashboard.");
        } catch (Exception e) {
            logger.error("Unexpected error during dashboard loading.", e);
            NotificationManager.showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
        }
    }

}
