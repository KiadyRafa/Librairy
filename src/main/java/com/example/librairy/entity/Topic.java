package com.example.librairy.entity;

public class Topic {
    private int id;
    private String name;

    // 🔹 Constructeur par défaut (nécessaire pour Hibernate et autres frameworks)
    public Topic() {}

    // 🔹 Constructeur avec un entier pour l'ID
    public Topic(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // 🔹 Constructeur avec un ID sous forme de String
    public Topic(String id, String name) {
        try {
            this.id = Integer.parseInt(id); // Convertit le String en int
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("L'ID doit être un nombre entier valide.");
        }
        this.name = name;
    }

    // 🔹 Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(String id) {
        try {
            this.id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("L'ID doit être un nombre entier valide.");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
