package org.example.gestionecole.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.gestionecole.entities.Professeur;
import org.example.gestionecole.services.ModuleService;

import java.util.List;
import java.util.Optional;

public class ProfessorSelectionController {
    @FXML
    private TableView<Professeur> professorTable;
    @FXML
    private TableColumn<Professeur, String> prenomColumn;
    @FXML
    private TableColumn<Professeur, String> nomColumn;

    private ModuleService moduleService; // This will be set before loading data
    private Professeur selectedProfessor;

    @FXML
    private void initialize() {
        // Initialize table columns
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
    }

    // Method to load professors after setting the service

    public void setModuleService(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @FXML
    private void handleSelect() {
        selectedProfessor = professorTable.getSelectionModel().getSelectedItem();
        closeDialog();
    }

    @FXML
    private void handleCancel() {
        selectedProfessor = null;
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) professorTable.getScene().getWindow();
        stage.close();
    }

    public Optional<Professeur> getSelectedProfessor() {
        return Optional.ofNullable(selectedProfessor);
    }

    public void loadProfessors() {
        if (moduleService != null) {
            List<Professeur> professors = moduleService.getAllProfessors();
            if (professors.isEmpty()) {
                System.out.println("No professors found in the database.");
            } else {
                System.out.println("Professors retrieved: " + professors.size());
            }
            ObservableList<Professeur> professorList = FXCollections.observableArrayList(professors);
            professorTable.setItems(professorList);
        } else {
            System.out.println("ModuleService is null. Cannot load professors.");
        }
    }


}
