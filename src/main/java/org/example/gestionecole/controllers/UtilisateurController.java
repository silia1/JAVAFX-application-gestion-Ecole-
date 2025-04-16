package org.example.gestionecole.controllers;

import org.example.gestionecole.entities.Utilisateur;
import org.example.gestionecole.services.UtilisateurService;

import java.util.List;

public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    public UtilisateurController() {
        this.utilisateurService = new UtilisateurService();
    }

    public String addUtilisateur(Utilisateur utilisateur) {
        boolean success = utilisateurService.addUtilisateur(utilisateur);
        return success ? "Utilisateur ajouté avec succès !" : "Échec de l'ajout de l'utilisateur.";
    }

    public String authenticate(String username, String password) {
        Utilisateur user = utilisateurService.authenticate(username, password);
        return user != null ? "Connexion réussie pour l'utilisateur : " + user.getUsername() : "Échec de l'authentification.";
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }
}
