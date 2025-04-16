package org.example.gestionecole.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.gestionecole.DAO.ModuleDAO;
import org.example.gestionecole.DAO.InscriptionDAO;
import org.example.gestionecole.controllers.ProfessorModulesController;
import org.example.gestionecole.entities.Etudiant;
import org.example.gestionecole.entities.Module;

import java.util.List;

import org.example.gestionecole.config.DatabaseConfig;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class ProfessorModulesService {
    private static final Logger logger = LogManager.getLogger(ProfessorModulesService.class);
    private final ModuleDAO moduleDAO = new ModuleDAO();
    private final InscriptionDAO inscriptionDAO = new InscriptionDAO();

    public List<Module> getModulesByProfessorId(int professorId) {
        return moduleDAO.getModulesForProfessor(professorId);
    }

    public List<Etudiant> getStudentsByModule(Integer moduleId) {
        logger.info("Fetching students for module ID: " + moduleId);
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
            logger.info("Number of students fetched: " + etudiants.size());
        } catch (SQLException e) {
            logger.error("Error fetching students for module ID: " + moduleId, e);
        }
        return etudiants;
    }


}
