package org.example.gestionecole.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.entities.Professeur;
import org.example.gestionecole.services.ProfesseurService;
import org.example.gestionecole.utils.PDFExporter;

import java.io.File;
import java.io.IOException;

public class ProfesseurController {

    private static final Logger logger = LogManager.getLogger(ProfesseurController.class);

    @FXML
    private TableView<Professeur> professeurTable;
    @FXML
    private TableColumn<Professeur, String> nomColumn;
    @FXML
    private TableColumn<Professeur, String> prenomColumn;
    @FXML
    private TableColumn<Professeur, String> specColumn;

    @FXML
    private TextField txtNom, txtPrenom, txtSpec;
    @FXML
    private Label errorLabel; // Label pour afficher les erreurs
    @FXML
    private Label totalProfessorsLabel, statusMessageLabel;

    private final ProfesseurService professeurService = new ProfesseurService();

    @FXML
    public void initialize() {
        // Initialize TableView columns
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        prenomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        specColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpecialite()));

        loadProfesseurs();
        updateTotalProfessors();

        // Set listener for row selection
        professeurTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> populateForm(newValue));
    }

    private void loadProfesseurs() {
        professeurTable.setItems(FXCollections.observableArrayList(professeurService.getAllProfesseurs()));
    }

    private void populateForm(Professeur professeur) {
        if (professeur != null) {
            txtNom.setText(professeur.getNom());
            txtPrenom.setText(professeur.getPrenom());
            txtSpec.setText(professeur.getSpecialite());
        } else {
            clearForm();
        }
    }

    @FXML
    private void onAddProf() {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String spec = txtSpec.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || spec.isEmpty()) {
            setStatusMessage("Veuillez remplir tous les champs.", false);
            return;
        }

        Professeur p = new Professeur();
        p.setNom(nom);
        p.setPrenom(prenom);
        p.setSpecialite(spec);

        if (professeurService.addProfesseur(p)) {
            setStatusMessage("Professeur ajouté avec succès.", true);
            loadProfesseurs();
            updateTotalProfessors();
            clearForm();
        } else {
            setStatusMessage("Échec de l'ajout du professeur.", false);
        }
    }

    @FXML
    private void onUpdateProf() {
        Professeur selected = professeurTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatusMessage("Veuillez sélectionner un professeur à modifier.", false);
            return;
        }

        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String spec = txtSpec.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || spec.isEmpty()) {
            setStatusMessage("Veuillez remplir tous les champs.", false);
            return;
        }

        selected.setNom(nom);
        selected.setPrenom(prenom);
        selected.setSpecialite(spec);

        if (professeurService.updateProfesseur(selected)) {
            setStatusMessage("Professeur modifié avec succès.", true);
            loadProfesseurs();
            updateTotalProfessors();
            clearForm();
        } else {
            setStatusMessage("Échec de la modification du professeur.", false);
        }
    }

    @FXML
    private void onDeleteProf() {
        Professeur selected = professeurTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatusMessage("Veuillez sélectionner un professeur à supprimer.", false);
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce professeur ?");
        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (professeurService.deleteProfesseur(selected)) {
                setStatusMessage("Professeur supprimé avec succès.", true);
                loadProfesseurs();
                updateTotalProfessors();
                clearForm();
            } else {
                setStatusMessage("Échec de la suppression du professeur.", false);
            }
        }
    }

    @FXML
    private void clearForm() {
        txtNom.clear();
        txtPrenom.clear();
        txtSpec.clear();
        professeurTable.getSelectionModel().clearSelection();
    }

    private void updateTotalProfessors() {
        int total = professeurTable.getItems().size();
        totalProfessorsLabel.setText("Total Professeurs: " + total);
    }

    private void setStatusMessage(String message, boolean isSuccess) {
        statusMessageLabel.setText(message);
        statusMessageLabel.setTextFill(isSuccess ? Color.GREEN : Color.RED);
    }
    @FXML
    private void handleExportProfesseurPdf() { // Exporte la liste des professeurs en PDF
        try {
            String resourcesDirPath = System.getProperty("user.dir") + "/src/main/resources/exports";

            File exportDir = new File(resourcesDirPath);
            if (!exportDir.exists() && !exportDir.mkdirs()) {
                throw new IOException("Failed to create export directory: " + resourcesDirPath);
            }

            String filePath = resourcesDirPath + "/Liste_Professeurs.pdf";

            PDFExporter.exportProfesseurListToPdf(filePath, professeurTable.getItems());

            showAlert("Export Réussi", "Le fichier PDF a été généré avec succès : " + filePath, Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            logger.error("Erreur lors de l'export en PDF.", e);
            showError("Une erreur s'est produite lors de l'exportation en PDF.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
