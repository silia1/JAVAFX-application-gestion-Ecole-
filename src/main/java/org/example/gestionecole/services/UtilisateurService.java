package org.example.gestionecole.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.DAO.UtilisateurDAO;
import org.example.gestionecole.config.DatabaseConfig;
import org.example.gestionecole.entities.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UtilisateurService {
    private static final Logger logger = LogManager.getLogger(UtilisateurService.class);
    private final UtilisateurDAO utilisateurDAO;

    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public boolean addUtilisateur(Utilisateur utilisateur) {
        return utilisateurDAO.addUtilisateur(utilisateur);
    }

    public Utilisateur authenticate(String username, String password) {
        return utilisateurDAO.authenticate(username, password);
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurDAO.getAllUtilisateurs();
    }

    public Utilisateur getUtilisateurByUsername(String username) {
        return utilisateurDAO.getUtilisateurByUsername(username);
    }
    public int getProfesseurIdByUserId(int userId) {
        String sql = "SELECT professeur_id FROM utilisateurs WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("professeur_id");
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving professeur_id for user ID: " + userId, e);
        }
        return -1; // Return -1 if no professor is associated
    }

}
