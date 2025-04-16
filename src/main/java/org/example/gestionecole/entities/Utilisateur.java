package org.example.gestionecole.entities;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur {
    private int id;
    private String username;
    private String password;
    private Role role;
    private Integer professeurId; // Linked to a specific professor (nullable for other roles)

}
