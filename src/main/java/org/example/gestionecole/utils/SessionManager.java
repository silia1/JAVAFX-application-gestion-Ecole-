package org.example.gestionecole.utils;

import org.example.gestionecole.DAO.ProfesseurDAO;
import org.example.gestionecole.entities.Professeur;
import org.example.gestionecole.entities.Utilisateur;

public class SessionManager {
    private static Utilisateur currentUser;
    private static Professeur currentProfessor;

    public static void setCurrentUser(Utilisateur user) {
        currentUser = user;

        // If the user is a professor, fetch their details
        if ("professeur".equalsIgnoreCase(user.getRole().name())) {
            ProfesseurDAO professeurDAO = new ProfesseurDAO();
            currentProfessor = professeurDAO.getProfessorByUserId(user.getId()); // Ensure this method exists and works
        } else {
            currentProfessor = null;
        }
    }

    public static Utilisateur getCurrentUser() {
        return currentUser;
    }

    public static Professeur getCurrentProfessor() {
        return currentProfessor;
    }

    public static void clearSession() {
        currentUser = null;
        currentProfessor = null;
    }
    //new
    private static int currentProfessorId = -1; // Default to -1 if not a professor

    public static void setCurrentProfessorId(int professorId) {
        currentProfessorId = professorId;

    }

    public static int getCurrentProfessorId() {
        return currentProfessorId;
    }


}
