package org.example.gestionecole.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.DAO.InscriptionDAO;
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.entities.Module;
import org.example.gestionecole.entities.Inscription;
import org.example.gestionecole.entities.Role;
import org.example.gestionecole.utils.EmailUtil;
import org.example.gestionecole.utils.PDFExporter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.example.gestionecole.utils.NotificationManager.showAlert;

public class InscriptionController {
    private static final Logger logger = LogManager.getLogger(InscriptionController.class);

    @FXML
    private ComboBox<Module> moduleComboBox;

    @FXML
    private ComboBox<Etudiant> etudiantComboBox;

    @FXML
    private TableView<Inscription> inscriptionTable;

    @FXML
    private TableColumn<Inscription, String> etudiantColumn;

    @FXML
    private TableColumn<Inscription, String> moduleColumn;

    @FXML
    private TableColumn<Inscription, String> dateInscriptionColumn;

    @FXML
    private Button enrollButton;

    @FXML
    private Button cancelButton;
    @FXML
    private Label moduleErrorLabel;

    @FXML
    private Label etudiantErrorLabel;

    @FXML
    private Label cancelErrorLabel;
    private final InscriptionDAO inscriptionDAO = new InscriptionDAO();


    @FXML
    public void initialize() {
        // Initialize table columns
        etudiantColumn.setCellValueFactory(new PropertyValueFactory<>("etudiantName"));
        moduleColumn.setCellValueFactory(new PropertyValueFactory<>("moduleName"));
        dateInscriptionColumn.setCellValueFactory(new PropertyValueFactory<>("dateInscription"));

        // Load data into dropdowns
        loadModules();
        loadEtudiants();
        refreshTable();

        // Cacher les étiquettes d'erreur au départ
        hideError(moduleErrorLabel);
        hideError(etudiantErrorLabel);
        hideError(cancelErrorLabel);
    }


    private void loadModules() {
        List<Module> modules = inscriptionDAO.getAllModules();
        moduleComboBox.setItems(FXCollections.observableArrayList(modules));
    }

    private void loadEtudiants() {
        List<Etudiant> etudiants = inscriptionDAO.getAllEtudiants();
        etudiantComboBox.setItems(FXCollections.observableArrayList(etudiants));
    }

    private void refreshTable() {
        List<Inscription> inscriptions = inscriptionDAO.getAllInscriptions();
        ObservableList<Inscription> observableList = FXCollections.observableArrayList(inscriptions);
        inscriptionTable.setItems(observableList);
    }



    @FXML
    private void handleEnroll(ActionEvent event) {
        Module selectedModule = moduleComboBox.getSelectionModel().getSelectedItem();
        Etudiant selectedEtudiant = etudiantComboBox.getSelectionModel().getSelectedItem();

        boolean hasError = false;

        // Vérification des erreurs
        if (selectedModule == null) {
            showError(moduleErrorLabel, "Veuillez sélectionner un module.");
            hasError = true;
        } else {
            hideError(moduleErrorLabel);
        }

        if (selectedEtudiant == null) {
            showError(etudiantErrorLabel, "Veuillez sélectionner un étudiant.");
            hasError = true;
        } else {
            hideError(etudiantErrorLabel);
        }

        // Si aucune erreur, inscrire et envoyer l'email
        if (!hasError) {
            try {
                // Inscription de l'étudiant au module
                inscriptionDAO.inscrireEtudiantModule(selectedEtudiant.getId(), selectedModule.getId());

                // Envoi d'un email à l'étudiant
                String email = selectedEtudiant.getEmail(); // Assurez-vous que l'objet Etudiant a un champ email
                if (email != null && !email.isEmpty()) {
                    String subject = "Confirmation d'inscription";
                    String body = "Bonjour " + selectedEtudiant.getNom() + ",\n\n"
                            + "Vous avez été inscrit avec succès au module : " + selectedModule.getNomModule() + ".\n\n"
                            + "Cordialement,\nGestion Ecole";
                    EmailUtil.sendEmail(email, subject, body);
                } else {
                    logger.warn("Aucune adresse email fournie pour l'étudiant : " + selectedEtudiant.getNom());
                }

                // Mise à jour de l'interface
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Inscription réussie", "L'étudiant a été inscrit et un email de confirmation a été envoyé.");
            } catch (Exception e) {
                logger.error("Erreur lors de l'inscription ou de l'envoi de l'email.", e);
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'inscription ou de l'envoi de l'email.");
            }
        }
    }

    // Méthodes utilitaires pour afficher/masquer les erreurs


    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }

    private void hideError(Label label) {
        label.setVisible(false);
    }


    @FXML
    private void handleExportPdf() {
        try {
            String resourcesDirPath = System.getProperty("user.dir") + "/src/main/resources/exports";

            File exportDir = new File(resourcesDirPath);
            if (!exportDir.exists() && !exportDir.mkdirs()) {
                throw new IOException("Échec de la création du répertoire d'exportation : " + resourcesDirPath);
            }

            String filePath = resourcesDirPath + "/Liste_Inscriptions.pdf";

            PDFExporter.exportInscriptionListToPdf(filePath, inscriptionTable.getItems());

            showAlert(Alert.AlertType.INFORMATION, "Export Réussi", "Le fichier PDF a été généré avec succès : " + filePath);
        } catch (Exception e) {
            logger.error("Erreur lors de l'export en PDF.", e);
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'exportation en PDF.");
        }
    }
    @FXML
    private void handleCancel(ActionEvent event) {
        Inscription selectedInscription = inscriptionTable.getSelectionModel().getSelectedItem();

        if (selectedInscription != null) {
            hideError(cancelErrorLabel);
            inscriptionDAO.annulerInscription(selectedInscription.getEtudiantId(), selectedInscription.getModuleId());
            refreshTable();
        } else {
            showError(cancelErrorLabel, "Veuillez sélectionner une inscription à annuler.");
        }
    }

}