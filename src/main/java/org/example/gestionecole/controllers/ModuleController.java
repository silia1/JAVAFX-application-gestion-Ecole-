package org.example.gestionecole.controllers;

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
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.entities.Module;
import org.example.gestionecole.entities.Professeur;
import org.example.gestionecole.services.ModuleService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.example.gestionecole.utils.NotificationManager.showAlert;

public class ModuleController {

    private static final Logger logger = LogManager.getLogger(ModuleController.class);

    @FXML
    private TableView<Module> moduleTable;

    @FXML
    private TableColumn<Module, String> nomModuleColumn;

    @FXML
    private TableColumn<Module, String> codeModuleColumn;

    @FXML
    private TableColumn<Module, String> professeurColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button assignProfessorButton;

    @FXML
    private Label errorMessage;

    @FXML
    private Label messageLabel;

    private final ModuleService moduleService;


    public ModuleController() {
        this.moduleService = new ModuleService();
    }

    @FXML
    private void initialize() {
        nomModuleColumn.setCellValueFactory(new PropertyValueFactory<>("nomModule"));
        codeModuleColumn.setCellValueFactory(new PropertyValueFactory<>("codeModule"));
        professeurColumn.setCellValueFactory(new PropertyValueFactory<>("professeurName"));

        loadModules();
    }

    private void loadModules() {
        List<Module> modules = moduleService.getAllModules();
        moduleTable.getItems().setAll(modules);
    }

    @FXML
    private void handleAdd() {
        Module newModule = new Module();
        boolean saveClicked = showModuleForm(newModule);

        if (saveClicked) {
            boolean success = moduleService.addOrUpdateModule(newModule);
            if (success) {
                moduleTable.getItems().add(newModule);
                showMessage("Module ajouté avec succès ");
            } else {
                showError("Échec de l'ajout du module.");
            }
        }
    }

    @FXML
    private void handleEdit() {
        Module selectedModule = moduleTable.getSelectionModel().getSelectedItem();
        if (selectedModule != null) {
            boolean saveClicked = showModuleForm(selectedModule);

            if (saveClicked) {
                boolean success = moduleService.addOrUpdateModule(selectedModule);
                if (success) {
                    moduleTable.refresh();
                    showMessage("Module modifié avec succès ");
                } else {
                    showError("Échec de modification du module.");
                }
            }
        } else {
            showError("No module selected.");
        }
    }

    @FXML
    private void handleDelete() {
        Module selectedModule = moduleTable.getSelectionModel().getSelectedItem();
        if (selectedModule != null) {
            boolean success = moduleService.deleteModule(selectedModule);
            if (success) {
                moduleTable.getItems().remove(selectedModule);
                showMessage("Module supprimé avec succès ");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the module.");
            }
        } else {
            showError("Échec de suppression du module.");
        }
    }

    private boolean showModuleForm(Module module) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gestionecole/ModuleForm.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Module Form");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(moduleTable.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            ModuleFormController controller = loader.getController();
            controller.setModule(module);

            dialogStage.showAndWait();
            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleAssignProfessor() {
        Module selectedModule = moduleTable.getSelectionModel().getSelectedItem();
        if (selectedModule == null) {
            showError("Aucun module sélectionné. Veuillez sélectionner un module pour affecter un professeur.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gestionecole/ProfessorSelectionDialog.fxml"));
            Parent dialogRoot = loader.load();

            ProfessorSelectionController controller = loader.getController();
            controller.setModuleService(moduleService);
            controller.loadProfessors();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Assign Professor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(moduleTable.getScene().getWindow());
            dialogStage.setScene(new Scene(dialogRoot));

            dialogStage.showAndWait();

            Optional<Professeur> selectedProfessor = controller.getSelectedProfessor();
            if (selectedProfessor.isPresent()) {
                boolean success = moduleService.assignProfessorToModule(selectedModule.getId(), selectedProfessor.get().getId());
                if (success) {
                    selectedModule.setProfesseur(selectedProfessor.get());
                    moduleTable.refresh();
                    showMessage("Professeur affecté avec succès.");
                } else {
                    showError("Échec de l'affectation du professeur.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the professor selection dialog.");
        }
    }

    @FXML
    private void handleEnrollUnassignedStudent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gestionecole/StudentSelectionDialog.fxml"));
            Parent dialogRoot = loader.load();

            StudentSelectionController controller = loader.getController();
            controller.setModuleService(moduleService);
            controller.loadUnassignedStudents();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Select a Student to Enroll");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(moduleTable.getScene().getWindow());
            dialogStage.setScene(new Scene(dialogRoot));

            dialogStage.showAndWait();

            Optional<Etudiant> selectedStudent = controller.getSelectedStudent();
            if (selectedStudent.isPresent()) {
                Module selectedModule = moduleTable.getSelectionModel().getSelectedItem();
                if (selectedModule == null) {
                    showError("Aucun module sélectionné. Veuillez sélectionner un module pour affecter l'étudiant.");
                    return;
                }

                boolean success = moduleService.addStudentToModule(
                        selectedModule.getId(),
                        selectedStudent.get().getId(),
                        LocalDate.now()
                );

                if (success) {
                    showMessage("Étudiant inscrit avec succès.");
                } else {
                    showError("Échec de l'inscription de l'étudiant");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the student selection dialog.");
        }
    }

    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
    }

    private void showMessage(String message) {
        messageLabel.setText(message);  // Affichage du message dans le Label
        messageLabel.setStyle("-fx-text-fill: green;");  // Couleur verte pour un message de succès
    }

}