package org.example.gestionecole.DAO;

import org.example.gestionecole.entities.Etudiant;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface CRUD <T,PK>{
    //T : Représente le type de l'entité sur laquelle vous allez travailler (par exemple, Article, User, etc.).
    //PK : Représente le type de la clé primaire de l'entité (par exemple, Integer ou String).
    boolean create(T t);

    boolean delete(T t);
    Optional<T> getById(PK id);

    Optional<Etudiant> getBymatricule(String matricule);

    Optional<Etudiant> getBynom(String nom);

    boolean update(T t);
    List<T> getAll();

    ResultSet Load();
}
