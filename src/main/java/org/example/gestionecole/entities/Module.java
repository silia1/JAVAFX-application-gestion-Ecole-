package org.example.gestionecole.entities;

import lombok.*;
import org.example.gestionecole.DAO.ModuleDAO;

import java.util.Optional;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Module {
    private int id;
    private String nomModule;
    private String codeModule;
    private Professeur professeur;

    //getter for professeur name
    public String getProfesseurName() {
        if (professeur != null) {
            return professeur.getPrenom() + " " + professeur.getNom();
        }
        return "No Professor Assigned";
    }
    // Setter for professor name
    public void setProfesseurName(String fullName) {
        if (fullName != null && !fullName.isEmpty()) {
            String[] nameParts = fullName.split(" ");
            if (nameParts.length >= 2) {
                String prenom = nameParts[0];
                String nom = nameParts[1];

                // Perform database lookup for the professor
                ModuleDAO moduleDAO = new ModuleDAO();
                Optional<Professeur> profOptional = moduleDAO.findProfessorByName(prenom, nom);

                if (profOptional.isPresent()) {
                    this.professeur = profOptional.get();
                } else {
                    this.professeur = null; // No professor found
                }
            } else {
                this.professeur = null; // Invalid name format
            }
        } else {
            this.professeur = null; // No professor assigned
        }
    }

    public void setNom(String nomModule) {
        this.nomModule = nomModule;
    }
    @Override
    public String toString() {
        return this.nomModule; // Remplacez "nomModule" par le nom de l'attribut contenant le nom du module
    }

}
