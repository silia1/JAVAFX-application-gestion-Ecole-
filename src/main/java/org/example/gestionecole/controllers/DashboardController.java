package org.example.gestionecole.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.entities.Role;
import org.example.gestionecole.services.EtudiantService;
import org.example.gestionecole.services.ModuleService;
import org.example.gestionecole.services.ProfesseurService;
import org.example.gestionecole.services.InscriptionService;

/**
 * Controller for the Dashboard view.
 */
public class DashboardController {
    private static final Logger logger = LogManager.getLogger(DashboardController.class);

    @FXML
    private Label totalEtudiantsLabel;

    @FXML
    private Label totalProfesseursLabel;

    @FXML
    private Label totalModulesLabel;

   // @FXML
   // private Label topModulesLabel;

   // @FXML
    //private Label topProfesseursLabel;

    @FXML
    private PieChart statsPieChart;

    private final EtudiantService etudiantService = new EtudiantService();
    private final ProfesseurService professeurService = new ProfesseurService();
    private final ModuleService moduleService = new ModuleService();
    private final InscriptionService inscriptionService = new InscriptionService();

    /**
     * Initializes the dashboard with statistics.
     */
    @FXML
    private void initialize() {
        updateStatistics();
    }

    /**
     * Updates the dashboard statistics.
     */
    private void updateStatistics() {
        try {
            int totalEtudiants = etudiantService.getAllEtudiants().size();
            int totalProfesseurs = professeurService.getAllProfesseurs().size();
            int totalModules = moduleService.getAllModules().size();

            totalEtudiantsLabel.setText(String.valueOf(totalEtudiants));
            totalProfesseursLabel.setText(String.valueOf(totalProfesseurs));
            totalModulesLabel.setText(String.valueOf(totalModules));

            // Fetch top modules and top professors (mocked for now, replace with actual DB queries)
            String topModules = inscriptionService.getTopModules(); // Replace with actual logic
            String topProfessors = professeurService.getTopProfessors(); // Replace with actual logic

           // topModulesLabel.setText(topModules);
           // topProfesseursLabel.setText(topProfessors);

            // Populate the pie chart
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Étudiants", totalEtudiants),
                    new PieChart.Data("Professeurs", totalProfesseurs),
                    new PieChart.Data("Modules", totalModules)
            );

            statsPieChart.setData(pieChartData);

            logger.info("Dashboard statistics updated.");
        } catch (Exception e) {
            logger.error("Error updating dashboard statistics", e);
            showError("Une erreur s'est produite lors de la récupération des statistiques.");
        }
    }

    public void setUserRole(Role role) {
        // Implement role-based access control if necessary
        // For example, hide certain statistics for non-admin roles
        logger.info("Setting Dashboard for role: " + role);
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
