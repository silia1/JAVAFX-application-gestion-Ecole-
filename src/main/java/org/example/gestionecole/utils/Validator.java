package org.example.gestionecole.utils;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");
    private static final Pattern MATRICULE_PATTERN = Pattern.compile("^[A-Z0-9]{5,20}$");

    public static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Adresse email invalide.");
        }
    }

    public static void validateMatricule(String matricule) {
        if (matricule == null || !MATRICULE_PATTERN.matcher(matricule).matches()) {
            throw new IllegalArgumentException("Matricule invalide. Doit contenir entre 5 et 20 caractères alphanumériques.");
        }
    }

    public static void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " ne peut pas être vide.");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException(fieldName + " ne peut pas dépasser 50 caractères.");
        }
    }

    public static void validateDateNaissance(LocalDate date) {
        if (date == null || date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date de naissance invalide.");
        }
    }

    public static void validatePromotion(String promotion) {
        if (promotion == null || promotion.trim().isEmpty()) {
            throw new IllegalArgumentException("Promotion ne peut pas être vide.");
        }
        if (promotion.length() > 50) {
            throw new IllegalArgumentException("Promotion ne peut pas dépasser 50 caractères.");
        }
    }
}
