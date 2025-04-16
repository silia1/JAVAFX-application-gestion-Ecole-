package org.example.gestionecole.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.DAO.EtudiantDAO;
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.entities.Role;
import org.example.gestionecole.utils.PDFExporter;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EtudiantController {
    private static final Logger logger = LogManager.getLogger(EtudiantController.class);

    @FXML
    private TableView<Etudiant> etudiantTable;

    @FXML
    private TableColumn<Etudiant, String> matriculeColumn;
    @FXML
    private TableColumn<Etudiant, String> nomColumn;
    @FXML
    private TableColumn<Etudiant, String> prenomColumn;
    @FXML
    private TableColumn<Etudiant, String> emailColumn;
    @FXML
    private TableColumn<Etudiant, String> promotionColumn;
    @FXML
    private TableColumn<Etudiant, String> dateNaissanceColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label errorMessage;

    private final EtudiantDAO etudiantDAO = new EtudiantDAO();
    private ObservableList<Etudiant> etudiantList;


    @FXML
    private void initialize() {
        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateNaissanceColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        promotionColumn.setCellValueFactory(new PropertyValueFactory<>("promotion"));

        loadEtudiantData();
    }

    private void loadEtudiantData() {
        List<Etudiant> etudiants = etudiantDAO.getAll();
        etudiantList = FXCollections.observableArrayList(etudiants);
        etudiantTable.setItems(etudiantList);
    }


    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gestionecole/AddEtudiantView.fxml"));
            Parent root = loader.load();

            AddEtudiantController controller = loader.getController();
            controller.setEtudiantList(etudiantList);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Étudiant");
            stage.showAndWait();
        } catch (IOException e) {
            logger.error("Erreur lors de l'ouverture de la fenêtre d'ajout.", e);
        }
    }

    @FXML
    private void handleEdit() {
        Etudiant selectedEtudiant = etudiantTable.getSelectionModel().getSelectedItem();
        if (selectedEtudiant == null) {
            showError("Veuillez sélectionner un étudiant à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gestionecole/EditEtudiantView.fxml"));
            Parent root = loader.load();

            EditEtudiantController controller = loader.getController();
            controller.setEtudiant(selectedEtudiant);
            controller.setEtudiantList(etudiantList);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un Étudiant");
            stage.showAndWait();
        } catch (IOException e) {
            logger.error("Erreur lors de l'ouverture de la fenêtre de modification.", e);
            showError("Erreur lors de l'ouverture de la fenêtre de modification.");
        }
    }

    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
    }


    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleExportPdf() {
        try {
            String resourcesDirPath = System.getProperty("user.dir") + "/src/main/resources/exports";

            File exportDir = new File(resourcesDirPath);
            if (!exportDir.exists() && !exportDir.mkdirs()) {
                throw new IOException("Failed to create export directory: " + resourcesDirPath);
            }

            String filePath = resourcesDirPath + "/Liste_Etudiants.pdf";

            PDFExporter.exportEtudiantListToPdf(filePath, etudiantList);

            showSuccess("Le fichier PDF a été généré avec succès : " + filePath);
        } catch (Exception e) {
            logger.error("Erreur lors de l'export en PDF.", e);
            showError("Une erreur s'est produite lors de l'exportation en PDF.");
        }
    }

    public void showSuccess(String message) {
        // Affiche une alerte d'information avec le message passé
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}