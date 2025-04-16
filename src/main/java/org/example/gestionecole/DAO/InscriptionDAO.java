package org.example.gestionecole.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.config.DatabaseConfig;
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.entities.Inscription;
import org.example.gestionecole.entities.Module;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InscriptionDAO {
    private static final Logger logger = LogManager.getLogger(InscriptionDAO.class);

    /**
     * Inscrire un étudiant à un module.
     *
     * @param studentId ID de l'étudiant.
     * @param moduleId  ID du module.
     */
    public void inscrireEtudiantModule(Integer studentId, Integer moduleId) {
        String sql = "INSERT INTO inscriptions (etudiant_id, module_id, date_inscription) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, moduleId);
            pstmt.setDate(3, Date.valueOf(LocalDate.now())); // Ajoute la date actuelle
            pstmt.executeUpdate();

            logger.info("Étudiant ID " + studentId + " inscrit au module ID " + moduleId);
        } catch (SQLException e) {
            logger.error("Erreur lors de l'inscription de l'étudiant ID " + studentId + " au module ID " + moduleId, e);
        }
    }

    /**
     * Obtenir les étudiants inscrits dans un module donné.
     *
     * @param moduleId ID du module.
     * @return Liste des étudiants inscrits.
     */
    public List<Etudiant> getStudentsByModule(Integer moduleId) {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT e.* FROM etudiants e " +
                "INNER JOIN inscriptions i ON e.id = i.etudiant_id " +
                "WHERE i.module_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, moduleId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Etudiant etudiant = new Etudiant();
                etudiant.setId(rs.getInt("id"));
                etudiant.setMatricule(rs.getString("matricule"));
                etudiant.setNom(rs.getString("nom"));
                etudiant.setPrenom(rs.getString("prenom"));
                etudiant.setDateNaissance(rs.getDate("date_naissance"));
                etudiant.setEmail(rs.getString("email"));
                etudiant.setPromotion(rs.getString("promotion"));
                etudiants.add(etudiant);
            }
            logger.info("Étudiants inscrits au module ID " + moduleId + " récupérés.");
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des étudiants inscrits au module ID " + moduleId, e);
        }
        return etudiants;
    }

    /**
     * Annuler l'inscription d'un étudiant à un module.
     *
     * @param studentId ID de l'étudiant.
     * @param moduleId  ID du module.
     */
    public void annulerInscription(Integer studentId, Integer moduleId) {
        String sql = "DELETE FROM inscriptions WHERE etudiant_id = ? AND module_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, moduleId);
            pstmt.executeUpdate();

            logger.info("Inscription de l'étudiant ID " + studentId + " au module ID " + moduleId + " annulée.");
        } catch (SQLException e) {
            logger.error("Erreur lors de l'annulation de l'inscription de l'étudiant ID " + studentId + " au module ID " + moduleId, e);
        }
    }

    /**
     * Obtenir toutes les inscriptions.
     *
     * @return Liste des inscriptions.
     */
    public List<Inscription> getAllInscriptions() {
        List<Inscription> inscriptions = new ArrayList<>();
        String sql = "SELECT i.*, e.nom AS etudiant_nom, e.prenom AS etudiant_prenom, " +
                "m.nom_module AS module_nom " +
                "FROM inscriptions i " +
                "JOIN etudiants e ON i.etudiant_id = e.id " +
                "JOIN modules m ON i.module_id = m.id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Inscription inscription = new Inscription();
                inscription.setEtudiantId(rs.getInt("etudiant_id"));
                inscription.setModuleId(rs.getInt("module_id"));
                inscription.setEtudiantName(rs.getString("etudiant_nom") + " " + rs.getString("etudiant_prenom"));
                inscription.setModuleName(rs.getString("module_nom"));
                inscription.setDateInscription(rs.getDate("date_inscription").toLocalDate());
                inscriptions.add(inscription);
            }
            logger.info("Toutes les inscriptions récupérées avec succès.");
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des inscriptions", e);
        }
        return inscriptions;
    }

    /**
     * Obtenir tous les modules disponibles.
     *
     * @return Liste des modules.
     */
    public List<Module> getAllModules() {
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT * FROM modules";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Module module = new Module();
                module.setId(rs.getInt("id"));
                module.setNom(rs.getString("nom_module"));
                modules.add(module);
            }
            logger.info("Modules récupérés avec succès.");
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des modules", e);
        }
        return modules;
    }

    /**
     * Obtenir tous les étudiants disponibles.
     *
     * @return Liste des étudiants.
     */
    public List<Etudiant> getAllEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM etudiants";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Etudiant etudiant = new Etudiant();
                etudiant.setId(rs.getInt("id"));
                etudiant.setMatricule(rs.getString("matricule"));
                etudiant.setNom(rs.getString("nom"));
                etudiant.setPrenom(rs.getString("prenom"));
                etudiant.setDateNaissance(rs.getDate("date_naissance"));
                etudiant.setEmail(rs.getString("email"));
                etudiant.setPromotion(rs.getString("promotion"));
                etudiants.add(etudiant);
            }
            logger.info("Étudiants récupérés avec succès.");
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des étudiants", e);
        }
        return etudiants;
    }

    public String getTopModules() {
        String sql = "SELECT module_name, COUNT(*) as count FROM inscriptions " +
                "JOIN modules ON inscriptions.module_id = modules.id " +
                "GROUP BY nom_module " +
                "ORDER BY count DESC LIMIT 3";
        List<String> topModules = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String moduleName = rs.getString("module_name");
                topModules.add(moduleName);
            }
        } catch (Exception e) {
            logger.error("Error fetching top modules", e);
        }
        return String.join(", ", topModules);
    }

}
