package org.example.gestionecole.services;

import org.example.gestionecole.DAO.EtudiantDAO;
import org.example.gestionecole.entities.Etudiant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class EtudiantService {
    private static final Logger logger = LogManager.getLogger(EtudiantService.class);

    private final EtudiantDAO etudiantDAO;

    public EtudiantService() {
        this.etudiantDAO = new EtudiantDAO();
    }

    /**
     * Adds a new student.
     *
     * @param etudiant The Etudiant to add.
     * @return true if added successfully, false otherwise.
     */
    public boolean addEtudiant(Etudiant etudiant) {
        return etudiantDAO.create(etudiant);
    }


    /**
     * Updates an existing student.
     *
     * @param etudiant The Etudiant with updated data.
     * @return true if updated successfully, false otherwise.
     */
    public boolean updateEtudiant(Etudiant etudiant) {
        return etudiantDAO.update(etudiant);
    }

    /**
     * Deletes a student.
     *
     * @param etudiant The Etudiant to delete.
     * @return true if deleted successfully, false otherwise.
     */
    /**
     * Retrieves a student by ID.
     *
     * @param id The ID of the student.
     * @return An Optional of Etudiant if found.
     */

    /**
     * Retrieves all students.
     *
     * @return A list of all Etudiants.
     */
    public List<Etudiant> getAllEtudiants() {
        return etudiantDAO.getAll();
    }
}