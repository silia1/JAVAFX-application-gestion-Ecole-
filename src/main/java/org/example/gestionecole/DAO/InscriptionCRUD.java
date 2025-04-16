package org.example.gestionecole.DAO;

import java.util.List;

public interface InscriptionCRUD<T, PK> {
    /**
     * Enroll a student in a module.
     *
     * @param studentId The ID of the student.
     * @param moduleId The ID of the module.
     */
    void inscrireEtudiantModule(PK studentId, PK moduleId);

    /**
     * Retrieve all students enrolled in a module.
     *
     * @param moduleId The ID of the module.
     * @return List of enrolled students.
     */
    List<T> getStudentsByModule(PK moduleId);

    /**
     * Cancel a student's enrollment in a module.
     *
     * @param studentId The ID of the student.
     * @param moduleId The ID of the module.
     */
    void annulerInscription(PK studentId, PK moduleId);
}

