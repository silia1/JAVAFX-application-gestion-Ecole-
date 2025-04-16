package org.example.gestionecole.services;

import org.example.gestionecole.DAO.ModuleDAO;
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.entities.Module;
import org.example.gestionecole.entities.Professeur;

import java.time.LocalDate;
import java.util.List;

public class ModuleService {
    private final ModuleDAO moduleDAO;

    public ModuleService() {
        this.moduleDAO = new ModuleDAO();
    }

    public boolean addOrUpdateModule(Module module) {
        if (module.getId() > 0) {
            return moduleDAO.update(module);
        } else {
            return moduleDAO.create(module);
        }
    }

    public boolean deleteModule(Module module) {
        return moduleDAO.delete(module);
    }

    public List<Module> getAllModules() {
        return moduleDAO.getAll();
    }


    public boolean assignProfessorToModule(int moduleId, int professorId) {
        return moduleDAO.assignProfessorToModule(moduleId, professorId);
    }

    public List<Professeur> getAllProfessors() {
        return moduleDAO.getAllProfessors();
    }

    public List<Etudiant> getUnassignedStudents() {
        return moduleDAO.getUnassignedStudents();
    }


    public boolean addStudentToModule(int moduleId, int studentId, LocalDate dateInscription) {
        return moduleDAO.addStudentToModule(moduleId, studentId, dateInscription);
    }

}
