package org.example.gestionecole.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.config.DatabaseConfig;
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.entities.Module;
import org.example.gestionecole.entities.Professeur;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModuleDAO {
    private static final Logger logger = LogManager.getLogger(ModuleDAO.class);

    //creer un nouveau module
    public boolean create(Module module) {
        String sql = "INSERT INTO modules (nom_module, code_module, professeur_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, module.getNomModule());
            pstmt.setString(2, module.getCodeModule());
            if (module.getProfesseur() != null) {
                pstmt.setInt(3, module.getProfesseur().getId());
            } else {
                pstmt.setNull(3, Types.INTEGER); // Handle null professor
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        module.setId(generatedKeys.getInt(1)); // Set the generated ID
                    }
                }
                logger.info("Module successfully created: " + module);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating module: " + module, e);
        }
        return false;
    }

    public boolean update(Module module) {
        String sql = "UPDATE modules SET nom_module = ?, code_module = ?, professeur_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, module.getNomModule());
            pstmt.setString(2, module.getCodeModule());
            if (module.getProfesseur() != null) {
                pstmt.setInt(3, module.getProfesseur().getId());
            } else {
                pstmt.setNull(3, Types.INTEGER); // Handle null professor
            }
            pstmt.setInt(4, module.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Module successfully updated: " + module);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error updating module: " + module, e);
        }
        return false;
    }

    public boolean delete(Module module) {
        String sql = "DELETE FROM modules WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, module.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Module successfully deleted: " + module);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error deleting module: " + module, e);
        }
        return false;
    }

    public List<Module> getAll() {
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT m.*, p.prenom, p.nom, p.specialite FROM modules m " +
                "LEFT JOIN professeurs p ON m.professeur_id = p.id";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Module module = extractModuleFromResultSet(rs);
                modules.add(module);
            }
            logger.info("All modules retrieved from the database.");
        } catch (SQLException e) {
            logger.error("Error retrieving all modules.", e);
        }
        return modules;
    }

    public Optional<Professeur> findProfessorByName(String prenom, String nom) {
        String sql = "SELECT * FROM professeurs WHERE prenom = ? AND nom = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, prenom);
            pstmt.setString(2, nom);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Professeur prof = new Professeur();
                    prof.setId(rs.getInt("id"));
                    prof.setPrenom(rs.getString("prenom"));
                    prof.setNom(rs.getString("nom"));
                    prof.setSpecialite(rs.getString("specialite"));
                    return Optional.of(prof);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding professor by name: " + prenom + " " + nom, e);
        }
        return Optional.empty();
    }

    private Module extractModuleFromResultSet(ResultSet rs) throws SQLException {
        Module module = new Module();
        module.setId(rs.getInt("id"));
        module.setNomModule(rs.getString("nom_module"));
        module.setCodeModule(rs.getString("code_module"));

        int professorId = rs.getInt("professeur_id");
        if (!rs.wasNull()) {
            Professeur prof = new Professeur();
            prof.setId(professorId);
            prof.setPrenom(rs.getString("prenom"));
            prof.setNom(rs.getString("nom"));
            prof.setSpecialite(rs.getString("specialite"));
            module.setProfesseur(prof);
        }

        return module;
    }

    //NEW WISSAL
    public List<Module> getModulesForProfessor(int professorId) {
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT m.*, p.prenom, p.nom, p.specialite FROM modules m " +
                "LEFT JOIN professeurs p ON m.professeur_id = p.id " +
                "WHERE m.professeur_id = ?";
//new
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, professorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Module module = extractModuleFromResultSet(rs);
                    modules.add(module);
                }
            }
            logger.info("Modules pour le professeur ID " + professorId + " récupérés avec succès.");
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des modules pour le professeur ID " + professorId, e);
        }

        return modules;
    }

    public boolean assignProfessorToModule(int moduleId, int professorId) {
        String sql = "UPDATE modules SET professeur_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, professorId);
            pstmt.setInt(2, moduleId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Professor ID " + professorId + " assigned to Module ID " + moduleId);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error assigning professor to module ID: " + moduleId, e);
        }
        return false;
    }

    public List<Professeur> getAllProfessors() {
        List<Professeur> professors = new ArrayList<>();
        String sql = "SELECT * FROM professeurs";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Professeur professor = new Professeur();
                professor.setId(rs.getInt("id"));
                professor.setPrenom(rs.getString("prenom"));
                professor.setNom(rs.getString("nom"));
                professor.setSpecialite(rs.getString("specialite"));
                professors.add(professor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professors;
    }

    public List<Etudiant> getUnassignedStudents() {
        List<Etudiant> students = new ArrayList<>();
        String sql = "SELECT e.id, e.nom, e.prenom, e.email " +
                "FROM etudiants e " +
                "LEFT JOIN inscriptions i ON e.id = i.etudiant_id " +
                "WHERE i.module_id IS NULL";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Etudiant student = new Etudiant();
                student.setId(rs.getInt("id"));           // Student ID
                student.setNom(rs.getString("nom"));     // Student Last Name
                student.setPrenom(rs.getString("prenom"));// Student First Name
                student.setEmail(rs.getString("email")); // Student Email
                students.add(student); // Add to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean addStudentToModule(int moduleId, int studentId, LocalDate dateInscription) {
        String sql = "INSERT INTO inscriptions (etudiant_id, module_id, date_inscription) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, moduleId);
            pstmt.setDate(3, java.sql.Date.valueOf(dateInscription));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
