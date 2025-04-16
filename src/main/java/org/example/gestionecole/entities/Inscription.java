package org.example.gestionecole.entities;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inscription {
    private int etudiantId;         // ID de l'étudiant
    private int moduleId;           // ID du module
    private String etudiantName;    // Nom complet de l'étudiant
    private String moduleName;      // Nom du module
    private LocalDate dateInscription; // Date d'inscription
}
