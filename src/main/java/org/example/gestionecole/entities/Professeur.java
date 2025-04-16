package org.example.gestionecole.entities;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Professeur {
    private int id;
    private String nom;
    private String prenom;
    private String specialite;
}
