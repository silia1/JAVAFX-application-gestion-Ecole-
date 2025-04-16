package org.example.gestionecole.entities;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Etudiant {
    private int id;
    private String matricule;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String email;
    private String promotion;

    @Override
    public String toString() {
        return nom + " " + prenom; // Affiche le nom complet
    }
}
