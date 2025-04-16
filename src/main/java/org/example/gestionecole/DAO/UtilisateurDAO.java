package org.example.gestionecole.DAO;

import org.example.gestionecole.config.DatabaseConfig;
import org.example.gestionecole.entities.Role;
import org.example.gestionecole.entities.Utilisateur;
import org.example.gestionecole.utils.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Utilisateur entity.
 */
public class UtilisateurDAO {
    private static final Logger logger = LogManager.getLogger(UtilisateurDAO.class);

    /**
     * Adds a new user to the database.
     *
     * @param utilisateur The Utilisateur object to add.
     * @return true if added successfully, false otherwise.
     */
    public boolean addUtilisateur(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, utilisateur.getUsername());
            pstmt.setString(2, PasswordUtil.hashPassword(utilisateur.getPassword())); // Hash the password
            pstmt.setString(3, utilisateur.getRole().name()); // Use Role enum's name method

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    utilisateur.setId(keys.getInt(1));
                }
                logger.info("Added Utilisateur: " + utilisateur);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error adding Utilisateur: " + utilisateur, e);
        }
        return false;
    }

    /**
     * Updates an existing user in the database.
     *
     * @param utilisateur The updated Utilisateur object.
     * @return true if updated successfully, false otherwise.
     */
    public boolean updateUtilisateur(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateurs SET username=?, password=?, role=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getUsername());
            pstmt.setString(2, PasswordUtil.hashPassword(utilisateur.getPassword())); // Hash the password before updating
            pstmt.setString(3, utilisateur.getRole().name());
            pstmt.setInt(4, utilisateur.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Updated Utilisateur: " + utilisateur);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error updating Utilisateur: " + utilisateur, e);
        }
        return false;
    }

    /**
     * Deletes a user from the database.
     *
     * @param utilisateurId The ID of the user to delete.
     * @return true if deleted successfully, false otherwise.
     */
    public boolean deleteUtilisateur(int utilisateurId) {
        String sql = "DELETE FROM utilisateurs WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, utilisateurId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Deleted Utilisateur with ID: " + utilisateurId);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error deleting Utilisateur with ID: " + utilisateurId, e);
        }
        return false;
    }

    /**
     * Retrieves a user by username.
     *
     * @param username The username of the user.
     * @return Utilisateur object if found, null otherwise.
     */
    public Utilisateur getUtilisateurByUsername(String username) {
        String sql = "SELECT * FROM utilisateurs WHERE username=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Utilisateur utilisateur = extractUtilisateurFromResultSet(rs);
                logger.info("Retrieved Utilisateur: " + utilisateur);
                return utilisateur;
            }
        } catch (SQLException e) {
            logger.error("Error retrieving Utilisateur with username: " + username, e);
        }
        return null;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return List of Utilisateur objects.
     */
    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                utilisateurs.add(extractUtilisateurFromResultSet(rs));
            }
            logger.info("Retrieved all Utilisateurs.");
        } catch (SQLException e) {
            logger.error("Error retrieving all Utilisateurs.", e);
        }
        return utilisateurs;
    }

    /**
     * Authenticates a user by username and password.
     *
     * @param username The username of the user.
     * @param password The plaintext password entered by the user.
     * @return The authenticated Utilisateur object if credentials match, null otherwise.
     */
    public Utilisateur authenticate(String username, String password) {
        String sql = "SELECT * FROM utilisateurs WHERE username=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Utilisateur utilisateur = extractUtilisateurFromResultSet(rs);

                // Verify the hashed password
                if (PasswordUtil.verifyPassword(password, utilisateur.getPassword())) {
                    logger.info("User authenticated successfully: " + username);
                    return utilisateur;
                } else {
                    logger.warn("Password mismatch for user: " + username);
                }
            }
        } catch (SQLException e) {
            logger.error("Error during authentication for username: " + username, e);
        }
        return null;
    }

    /**
     * Extracts a Utilisateur object from a ResultSet.
     *
     * @param rs The ResultSet.
     * @return Utilisateur object.
     * @throws SQLException if a database access error occurs.
     */
    private Utilisateur extractUtilisateurFromResultSet(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getInt("id"));
        utilisateur.setUsername(rs.getString("username"));
        utilisateur.setPassword(rs.getString("password")); // Store hashed password
        utilisateur.setRole(Role.fromString(rs.getString("role"))); // Use Role.fromString method
        return utilisateur;
    }
}
