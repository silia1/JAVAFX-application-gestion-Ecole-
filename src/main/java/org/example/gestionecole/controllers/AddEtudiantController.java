package org.example.gestionecole.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.services.EtudiantService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddEtudiantController {
    private static final Logger logger = LogManager.getLogger(AddEtudiantController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private TextField matriculeField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField dateNaissanceField; // TextField for date input

    @FXML
    private TextField emailField;

    @FXML
    private TextField promotionField;

    @FXML
    private Label errorMessage;

    private final EtudiantService etudiantService = new EtudiantService();
    @Setter
    private ObservableList<Etudiant> etudiantList; // Reference to the ObservableList

    @FXML
    private void handleSave() {
        try {
            // Vérification des champs obligatoires
            if (matriculeField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Le matricule doit être rempli.");
            }
            if (nomField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Le nom doit être rempli.");
            }
            if (prenomField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Le prénom doit être rempli.");
            }
            if (dateNaissanceField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("La date de naissance doit être remplie.");
            }
            if (emailField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("L'email doit être rempli.");
            }
            if (promotionField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("La promotion doit être remplie.");
            }

            // Vérification de la validité de la date de naissance
            String dateInput = dateNaissanceField.getText().trim();
            LocalDate dateNaissance = null;
            try {
                dateNaissance = LocalDate.parse(dateInput, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("La date de naissance doit être au format yyyy-MM-dd.");
            }
            Etudiant newEtudiant = new Etudiant();
            newEtudiant.setMatricule(matriculeField.getText().trim());
            newEtudiant.setNom(nomField.getText().trim());
            newEtudiant.setPrenom(prenomField.getText().trim());
            newEtudiant.setDateNaissance(Date.valueOf(dateNaissance)); // Automatically converts to java.sql.Date
            newEtudiant.setEmail(emailField.getText().trim());
            newEtudiant.setPromotion(promotionField.getText().trim());

            boolean success = etudiantService.addEtudiant(newEtudiant);

            if (success) {
                if (etudiantList != null) {
                    etudiantList.add(newEtudiant); // Add directly to the ObservableList
                }
                closeWindow();
            } else {
                throw new RuntimeException("Erreur lors de l'ajout de l'étudiant.");
            }
        } catch (DateTimeParseException e) {
            showError("Erreur de validation: La date de naissance doit être au format yyyy-MM-dd.");
        } catch (IllegalArgumentException e) {
            showError("Erreur de validation: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur inattendue : ", e);
            showError("Erreur: Une erreur inattendue s'est produite.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) matriculeField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
    }
}