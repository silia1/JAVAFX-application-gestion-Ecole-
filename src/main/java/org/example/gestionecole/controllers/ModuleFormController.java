package org.example.gestionecole.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.gestionecole.entities.Module;

public class ModuleFormController {
    @FXML
    private TextField nomModuleField;
    @FXML
    private TextField codeModuleField;
    @FXML
    private TextField professeurField;

    private Module module; // The module being edited or added
    private boolean saveClicked = false;

    @FXML
    private void handleSave() {
        module.setNomModule(nomModuleField.getText());
        module.setCodeModule(codeModuleField.getText());
        module.setProfesseurName(professeurField.getText());
        saveClicked = true;
        closeForm();
    }

    @FXML
    private void handleCancel() {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) nomModuleField.getScene().getWindow();
        stage.close();
    }

    public void setModule(Module module) {
        this.module = module;

        if (module != null) {
            nomModuleField.setText(module.getNomModule());
            codeModuleField.setText(module.getCodeModule());
            professeurField.setText(module.getProfesseurName());
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }
}
