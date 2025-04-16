package org.example.gestionecole.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.entities.Module;
import org.example.gestionecole.entities.Professeur;
import org.example.gestionecole.services.ProfessorModulesService;
import org.example.gestionecole.utils.SessionManager;

import java.util.List;

public class ProfessorModulesController {

    private static final Logger logger = LogManager.getLogger(ProfessorModulesController.class);
    private final ProfessorModulesService service = new ProfessorModulesService();

    @FXML
    private TableView<Module> modulesTable;

    @FXML
    private TableColumn<Module, String> moduleNameColumn;

    @FXML
    private TableColumn<Module, String> moduleCodeColumn;

    @FXML
    private TableView<Etudiant> studentsTable;

    @FXML
    private TableColumn<Etudiant, String> studentNameColumn;

    @FXML
    private TableColumn<Etudiant, String> studentMatriculeColumn;

    @FXML
    private TableColumn<Etudiant, String> studentEmailColumn;

    private ObservableList<Module> moduleList;
    private ObservableList<Etudiant> studentList;

    @FXML
    public void initialize() {
        try {
            setupTables();
            loadModulesForCurrentProfessor();
        } catch (Exception e) {
            logger.error("Error initializing ProfessorModulesController: ", e);
        }
    }

    private void setupTables() {
        moduleNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomModule"));
        moduleCodeColumn.setCellValueFactory(new PropertyValueFactory<>("codeModule"));

        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        studentMatriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        studentEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    @FXML
    public void loadModulesForCurrentProfessor() {
        int professorId = SessionManager.getCurrentProfessorId(); // Fix here
        List<Module> modules = service.getModulesByProfessorId(professorId);

        modulesTable.getItems().clear();
        modulesTable.getItems().addAll(modules);
    }


    @FXML
    public void onModuleSelected() {
        Module selectedModule = modulesTable.getSelectionModel().getSelectedItem();
        if (selectedModule != null) {
            logger.info("Selected module ID: " + selectedModule.getId());
            List<Etudiant> students = service.getStudentsByModule(selectedModule.getId());
            studentsTable.getItems().clear();
            studentsTable.getItems().addAll(students);
        } else {
            logger.warn("No module selected.");
        }

    }
}
