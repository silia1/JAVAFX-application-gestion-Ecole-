package org.example.gestionecole.entities;

public enum Role {
    admin,secretaire,professeur,user;

    /**
     * Converts a string to a Role enum.
     *
     * @param roleString The role string from the database.
     * @return The corresponding Role enum, or null if the string is invalid.
     */
    public static Role fromString(String roleString) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(roleString)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + roleString);
    }

}

