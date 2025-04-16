package org.example.gestionecole.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.entities.Role;
import org.example.gestionecole.utils.SessionManager;
import org.example.gestionecole.utils.ViewManager;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SidebarController {

    private static final Logger logger = LogManager.getLogger(SidebarController.class);

    @FXML
    private HBox dashboardRow;

    @FXML
    private HBox etudiantsRow;

    @FXML
    private HBox professeursRow;

    @FXML
    private HBox modulesRow; // Only visible for admin and others except professor

    @FXML
    private HBox professorModulesRow; // New row for professors

    @FXML
    private HBox inscriptionsRow;

    @FXML
    private HBox logoutRow;

    @FXML
    private Label dashboardButton;

    @FXML
    private Label etudiantsButton;

    @FXML
    private Label professeursButton;

    @FXML
    private Label modulesButton;

    @FXML
    private Label professorModulesButton; // New label for professors

    @FXML
    private Label inscriptionsButton;

    @FXML
    private Label logoutButton;

    private ResourceBundle resourceBundle;

    public void initialize() {
        Role role = SessionManager.getCurrentUser().getRole(); // Get current user's role
        setRolePermissions(role);
        loadResourceBundle(Locale.getDefault());
        setLocalizedText();
    }

    @FXML
    public void navigateToDashboard(MouseEvent event) {
        ViewManager.switchView("/org/example/gestionecole/DashboardView.fxml");
    }

    @FXML
    public void navigateToEtudiants(MouseEvent event) {
        ViewManager.switchView("/org/example/gestionecole/EtudiantView.fxml");
    }

    @FXML
    public void navigateToProfesseurs(MouseEvent event) {
        ViewManager.switchView("/org/example/gestionecole/ProfesseurView.fxml");
    }

    @FXML
    public void navigateToModules(MouseEvent event) {
        ViewManager.switchView("/org/example/gestionecole/ModuleView.fxml");
    }

    @FXML
    public void navigateToProfessorModules(MouseEvent event) { // New navigation for professors
        ViewManager.switchView("/org/example/gestionecole/ProfessorModulesView.fxml");
    }

    @FXML
    public void navigateToInscriptions(MouseEvent event) {
        ViewManager.switchView("/org/example/gestionecole/InscriptionView.fxml");
    }

    @FXML
    public void handleLogout(MouseEvent event) {
        SessionManager.clearSession();
        ViewManager.switchView("/org/example/gestionecole/LoginView.fxml");
    }

    private void setRolePermissions(Role role) {
        logger.info("Setting role permissions for role: " + role);

        // Dashboard row is always visible and enabled
        dashboardRow.setVisible(true);
        dashboardRow.setManaged(true);
        dashboardRow.setDisable(false);

        // Hide, disable, and unmanage other rows by default
        etudiantsRow.setVisible(false);
        etudiantsRow.setManaged(false);
        etudiantsRow.setDisable(true);

        professeursRow.setVisible(false);
        professeursRow.setManaged(false);
        professeursRow.setDisable(true);

        modulesRow.setVisible(false);
        modulesRow.setManaged(false);
        modulesRow.setDisable(true);

        professorModulesRow.setVisible(false);
        professorModulesRow.setManaged(false);
        professorModulesRow.setDisable(true);

        inscriptionsRow.setVisible(false);
        inscriptionsRow.setManaged(false);
        inscriptionsRow.setDisable(true);

        // Adjust visibility, management, and interactivity based on role
        switch (role) {
            case admin:
                etudiantsRow.setVisible(true);
                etudiantsRow.setManaged(true);
                etudiantsRow.setDisable(false);

                professeursRow.setVisible(true);
                professeursRow.setManaged(true);
                professeursRow.setDisable(false);

                modulesRow.setVisible(true);
                modulesRow.setManaged(true);
                modulesRow.setDisable(false);

                inscriptionsRow.setVisible(true);
                inscriptionsRow.setManaged(true);
                inscriptionsRow.setDisable(false);
                break;

            case secretaire:
                etudiantsRow.setVisible(true);
                etudiantsRow.setManaged(true);
                etudiantsRow.setDisable(false);

                inscriptionsRow.setVisible(true);
                inscriptionsRow.setManaged(true);
                inscriptionsRow.setDisable(false);
                break;

            case professeur:
                //etudiantsRow.setVisible(true);
                //etudiantsRow.setManaged(true);
                //etudiantsRow.setDisable(false);

                professorModulesRow.setVisible(true); // Only this for professors
                professorModulesRow.setManaged(true);
                professorModulesRow.setDisable(false);
                break;

            default:
                logger.warn("Unknown role: " + role);
                break;
        }
    }

    private void loadResourceBundle(Locale locale) {
        try {
            resourceBundle = ResourceBundle.getBundle("org.example.gestionecole.i18n.messages", locale);
            logger.info("Resource bundle loaded for locale: " + locale);
        } catch (MissingResourceException e) {
            logger.error("Resource bundle not found for locale: " + locale, e);
        }
    }

    private void setLocalizedText() {
        try {
            dashboardButton.setText(resourceBundle.getString("dashboard.title"));
            etudiantsButton.setText(resourceBundle.getString("etudiants.title"));
            professeursButton.setText(resourceBundle.getString("professeurs.title"));
            modulesButton.setText(resourceBundle.getString("modules.title"));
            professorModulesButton.setText(resourceBundle.getString("professor.modules.title")); // Add to i18n
            inscriptionsButton.setText(resourceBundle.getString("inscriptions.title"));
            logoutButton.setText(resourceBundle.getString("logout.title"));
            logger.info("Localized text applied successfully.");
        } catch (Exception e) {
            logger.error("Error setting localized text. Ensure the resource keys are correct.", e);
        }
    }

    public void switchLanguage(Locale locale) {
        loadResourceBundle(locale);
        setLocalizedText();
        logger.info("Language switched to: " + locale.getLanguage());
    }

    @FXML
    public void switchToEnglish(MouseEvent event) {
        switchLanguage(Locale.ENGLISH);
    }

    @FXML
    public void switchToFrench(MouseEvent event) {
        switchLanguage(Locale.FRENCH);
    }
}
