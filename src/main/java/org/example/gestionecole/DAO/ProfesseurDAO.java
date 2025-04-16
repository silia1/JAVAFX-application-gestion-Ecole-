package org.example.gestionecole.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.config.DatabaseConfig;
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.entities.Professeur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfesseurDAO implements CRUD<Professeur, Integer> {
    private static final Logger logger = LogManager.getLogger(ProfesseurDAO.class);

    @Override
    public boolean create(Professeur professeur) {
        String sql = "INSERT INTO professeurs (nom, prenom, specialite) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, professeur.getNom());
            pstmt.setString(2, professeur.getPrenom());
            pstmt.setString(3, professeur.getSpecialite());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        professeur.setId(generatedKeys.getInt(1));
                    }
                }
                logger.info("Professeur créé : " + professeur);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la création du professeur : " + professeur, e);
        }
        return false;
    }

    @Override
    public boolean delete(Professeur professeur) {
        String sql = "DELETE FROM professeurs WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, professeur.getId());
            int rowsAffected = pstmt.executeUpdate();
            logger.info("Professeur supprimé : " + professeur);
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression du professeur : " + professeur, e);
        }
        return false;
    }

    @Override
    public Optional<Professeur> getById(Integer id) {
        return Optional.empty();
    }


    @Override
    public boolean update(Professeur professeur) {
        String sql = "UPDATE professeurs SET nom = ?, prenom = ?, specialite = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, professeur.getNom());
            pstmt.setString(2, professeur.getPrenom());
            pstmt.setString(3, professeur.getSpecialite());
            pstmt.setInt(4, professeur.getId());

            int rowsAffected = pstmt.executeUpdate();
            logger.info("Professeur mis à jour : " + professeur);
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Erreur lors de la mise à jour du professeur : " + professeur, e);
        }
        return false;
    }

    @Override
    public List<Professeur> getAll() {
        List<Professeur> professeurs = new ArrayList<>();
        String sql = "SELECT * FROM professeurs";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Professeur professeur = extractProfesseurFromResultSet(rs);
                professeurs.add(professeur);
            }
            logger.info("Tous les professeurs ont été récupérés.");
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération de tous les professeurs.", e);
        }
        return professeurs;
    }

    @Override
    public ResultSet Load() {
        String sql = "SELECT * FROM professeurs";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            logger.info("Chargement des professeurs en cours.");
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            logger.error("Erreur lors du chargement des professeurs.", e);
            return null;
        }
    }

    /**
     * Extracts a Professeur object from a ResultSet.
     *
     * @param rs The ResultSet containing the professeur data.
     * @return A Professeur object.
     * @throws SQLException if a database access error occurs.
     */
    private Professeur extractProfesseurFromResultSet(ResultSet rs) throws SQLException {
        Professeur professeur = new Professeur();
        professeur.setId(rs.getInt("id"));
        professeur.setNom(rs.getString("nom"));
        professeur.setPrenom(rs.getString("prenom"));
        professeur.setSpecialite(rs.getString("specialite"));
        // Note: Handling the `modulesEnseignes` is outside the scope of this method, as it may require additional queries.
        return professeur;
    }

    @Override
    public Optional<Etudiant> getBymatricule(String matricule) {
        return Optional.empty();
    }

    @Override
    public Optional<Etudiant> getBynom(String nom) {
        return Optional.empty();
    }
    public Professeur getProfessorByUserId(int userId) {
        String sql = "SELECT * FROM professeurs WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Professeur professor = new Professeur();
                    professor.setId(rs.getInt("id"));
                    professor.setNom(rs.getString("nom"));
                    professor.setPrenom(rs.getString("prenom"));
                    professor.setSpecialite(rs.getString("specialite"));
                    return professor;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTopProfessors() {
        String sql = "SELECT professeur_name, COUNT(*) as count FROM modules " +
                "JOIN professeurs ON modules.professeur_id = professeurs.id " +
                "GROUP BY professeur_name " +
                "ORDER BY count DESC LIMIT 3";
        List<String> topProfessors = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String professorName = rs.getString("professeur_name");
                topProfessors.add(professorName);
            }
        } catch (Exception e) {
            logger.error("Error fetching top professors", e);
        }
        return String.join(", ", topProfessors);
    }


}
