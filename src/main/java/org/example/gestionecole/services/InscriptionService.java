package org.example.gestionecole.services;

import org.example.gestionecole.DAO.InscriptionDAO;
import org.example.gestionecole.entities.Etudiant;

import java.util.List;

public class InscriptionService {
    private final InscriptionDAO inscriptionDAO;

    public InscriptionService() {
        this.inscriptionDAO = new InscriptionDAO();
    }

    /**
     * Enrolls a student in a module.
     *
     * @param studentId The ID of the student.
     * @param moduleId  The ID of the module.
     * @return true if the enrollment was successful, false otherwise.
     */
    public boolean enrollStudentInModule(int studentId, int moduleId) {
        try {
            inscriptionDAO.inscrireEtudiantModule(studentId, moduleId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves all students enrolled in a module.
     *
     * @param moduleId The ID of the module.
     * @return A list of students enrolled in the module.
     */
    public List<Etudiant> getStudentsByModule(int moduleId) {
        return inscriptionDAO.getStudentsByModule(moduleId);
    }

    /**
     * Cancels a student's enrollment in a module.
     *
     * @param studentId The ID of the student.
     * @param moduleId  The ID of the module.
     * @return true if the cancellation was successful, false otherwise.
     */
    public boolean cancelEnrollment(int studentId, int moduleId) {
        try {
            inscriptionDAO.annulerInscription(studentId, moduleId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public String getTopModules() {
        return inscriptionDAO.getTopModules();
    }
}
