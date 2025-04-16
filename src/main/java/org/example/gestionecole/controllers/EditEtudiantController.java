package org.example.gestionecole.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.gestionecole.DAO.EtudiantDAO;
import org.example.gestionecole.entities.Etudiant;

import java.sql.Date;
import java.util.List;

public class EditEtudiantController {

    @FXML
    private TextField matriculeField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField dateNaissanceField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField promotionField;

    private Etudiant etudiant;
    private List<Etudiant> etudiantList; // Reference to the list in TableView

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;

        // Populate fields with current data
        matriculeField.setText(etudiant.getMatricule());
        nomField.setText(etudiant.getNom());
        prenomField.setText(etudiant.getPrenom());
        dateNaissanceField.setText(String.valueOf(etudiant.getDateNaissance()));
        emailField.setText(etudiant.getEmail());
        promotionField.setText(etudiant.getPromotion());
    }

    public void setEtudiantList(List<Etudiant> etudiantList) {
        this.etudiantList = etudiantList;
    }

    @FXML
    private void handleSave() {
        try {
            // Validate and apply changes
            etudiant.setNom(nomField.getText());
            etudiant.setPrenom(prenomField.getText());
            etudiant.setDateNaissance(Date.valueOf(dateNaissanceField.getText())); // Convert to java.sql.Date
            etudiant.setEmail(emailField.getText());
            etudiant.setPromotion(promotionField.getText());

            // Update the database
            EtudiantDAO etudiantDAO = new EtudiantDAO();
            etudiantDAO.update(etudiant);

            // Refresh the list to reflect changes in the TableView
            int index = etudiantList.indexOf(etudiant);
            if (index != -1) {
                etudiantList.set(index, etudiant);
            }

            // Close the window
            Stage stage = (Stage) matriculeField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            // Handle errors (e.g., invalid date format)
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        // Close the window without saving
        Stage stage = (Stage) matriculeField.getScene().getWindow();
        stage.close();
    }
}
