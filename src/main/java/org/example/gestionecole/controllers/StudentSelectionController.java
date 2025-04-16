package org.example.gestionecole.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.services.ModuleService;

import java.util.List;
import java.util.Optional;

public class StudentSelectionController {
    @FXML
    private TableView<Etudiant> studentTable;
    @FXML
    private TableColumn<Etudiant, String> prenomColumn;
    @FXML
    private TableColumn<Etudiant, String> nomColumn;
    @FXML
    private TableColumn<Etudiant, String> emailColumn;

    private ModuleService moduleService;
    private Etudiant selectedStudent;

    /**
     * Initialize the TableView with column mappings.
     */
    @FXML
    private void initialize() {
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    /**
     * Sets the ModuleService to enable database interactions.
     *
     * @param moduleService The ModuleService instance.
     */
    public void setModuleService(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * Load unassigned students into the TableView.
     */
    public void loadUnassignedStudents() {
        if (moduleService != null) {
            List<Etudiant> students = moduleService.getUnassignedStudents();

            // Debug: Print out unassigned students
            if (students.isEmpty()) {
                System.out.println("No unassigned students found.");
            } else {
                System.out.println("Unassigned students retrieved: " + students.size());
                for (Etudiant student : students) {
                    System.out.println(student.getPrenom() + " " + student.getNom() + " (" + student.getEmail() + ")");
                }
            }

            // Load the students into the TableView
            ObservableList<Etudiant> studentList = FXCollections.observableArrayList(students);
            studentTable.setItems(studentList);
        } else {
            System.out.println("ModuleService is null. Cannot load unassigned students.");
        }
    }

    /**
     * Handles the selection of a student and closes the dialog.
     */
    @FXML
    private void handleSelect() {
        selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        closeDialog();
    }

    /**
     * Handles the cancel action and closes the dialog.
     */
    @FXML
    private void handleCancel() {
        selectedStudent = null;
        closeDialog();
    }

    /**
     * Closes the dialog.
     */
    private void closeDialog() {
        Stage stage = (Stage) studentTable.getScene().getWindow();
        stage.close();
    }

    /**
     * Returns the selected student.
     *
     * @return An Optional containing the selected student, or empty if none is selected.
     */
    public Optional<Etudiant> getSelectedStudent() {
        return Optional.ofNullable(selectedStudent);
    }
}
