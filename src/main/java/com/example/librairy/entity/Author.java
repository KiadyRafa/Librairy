package com.example.librairy.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Author {
    private int id;
    private String name;
    private LocalDate birthDate;

    // ✅ Constructeur sans arguments (obligatoire pour JPA et tests)
    public Author() {}

    // ✅ Constructeur avec arguments
    public Author(int id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    // ✅ Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    // ✅ Redéfinition de toString()
    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }

    // ✅ Correction de equals() pour éviter NullPointerException
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id &&
                Objects.equals(name, author.name) &&
                Objects.equals(birthDate, author.birthDate);
    }

    // ✅ Utilisation de Objects.hash()
    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthDate);
    }
}
