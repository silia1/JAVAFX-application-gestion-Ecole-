package org.example.gestionecole.services;

import org.example.gestionecole.DAO.ProfesseurDAO;
import org.example.gestionecole.entities.Professeur;

import java.util.List;
import java.util.Optional;

public class ProfesseurService {
    private final ProfesseurDAO professeurDAO;

    public ProfesseurService() {
        this.professeurDAO = new ProfesseurDAO();
    }

    public boolean addProfesseur(Professeur professeur) {
        return professeurDAO.create(professeur);
    }

    public boolean updateProfesseur(Professeur professeur) {
        return professeurDAO.update(professeur);
    }

    public boolean deleteProfesseur(Professeur professeur) {
        return professeurDAO.delete(professeur);
    }

    public List<Professeur> getAllProfesseurs() {
        return professeurDAO.getAll();
    }

    public Optional<Professeur> getProfesseurById(int id) {
        return professeurDAO.getById(id);
    }

    public String getTopProfessors() {
        return professeurDAO.getTopProfessors();
    }
}
