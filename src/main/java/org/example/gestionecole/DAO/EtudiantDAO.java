package org.example.gestionecole.DAO;

import org.example.gestionecole.config.DatabaseConfig;
import org.example.gestionecole.entities.Etudiant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EtudiantDAO {
    private static final Logger logger = LogManager.getLogger(EtudiantDAO.class);

    public boolean create(Etudiant etudiant) {
        if (etudiant.getDateNaissance() == null) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être null.");
        }

        String sql = "INSERT INTO etudiants (matricule, nom, prenom, date_naissance, email, promotion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, etudiant.getMatricule());
            pstmt.setString(2, etudiant.getNom());
            pstmt.setString(3, etudiant.getPrenom());
            pstmt.setDate(4, etudiant.getDateNaissance());
            pstmt.setString(5, etudiant.getEmail());
            pstmt.setString(6, etudiant.getPromotion());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erreur lors de la création de l'étudiant : ", e);
            return false;
        }
    }



    public boolean update(Etudiant etudiant) {
        String sql = "UPDATE etudiants SET nom = ?, prenom = ?, date_naissance = ?, email = ?, promotion = ? WHERE matricule = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setDate(3, etudiant.getDateNaissance());
            pstmt.setString(4, etudiant.getEmail());
            pstmt.setString(5, etudiant.getPromotion());
            pstmt.setString(6, etudiant.getMatricule());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erreur lors de la mise à jour de l'étudiant : ", e);
            return false;
        }
    }


    public List<Etudiant> getAll() {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM etudiants";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Etudiant etudiant = extractEtudiantFromResultSet(rs);
                etudiants.add(etudiant);
            }
            logger.info("Étudiants récupérés avec succès.");
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des étudiants.", e);
        }
        return etudiants;
    }

    private Etudiant extractEtudiantFromResultSet(ResultSet rs) throws SQLException {
        return new Etudiant(
                rs.getInt("id"),
                rs.getString("matricule"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getDate("date_naissance"),
                rs.getString("email"),
                rs.getString("promotion")
        );
    }
}
