package com.example;

import com.example.librairy.dao.AuthorCrudOperations;
import com.example.librairy.entity.Author;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        AuthorCrudOperations authorDAO = new AuthorCrudOperations();

        // Ajouter un nouvel auteur
        Author newAuthor = new Author(0, "Victor Hugo", LocalDate.of(1802, 2, 26));
        Author savedAuthor = authorDAO.save(newAuthor);
        System.out.println("Auteur ajouté : " + savedAuthor);

        //  Récupérer un auteur par son ID
        Optional<Author> foundAuthor = authorDAO.findById(savedAuthor.getId());
        foundAuthor.ifPresent(author ->
                System.out.println("Auteur trouvé : " + author)
        );

        //  Afficher tous les auteurs
        List<Author> authors = authorDAO.findAll();
        System.out.println(" Liste des auteurs :");
        for (Author author : authors) {
            System.out.println(author);
        }

        //  Modifier un auteur
        if (foundAuthor.isPresent()) {
            Author authorToUpdate = foundAuthor.get();
            authorToUpdate.setName("Victor-Marie Hugo"); // Nouveau nom
            authorDAO.update(authorToUpdate);
            System.out.println("Auteur mis à jour : " + authorDAO.findById(authorToUpdate.getId()).get());
        }

        //  Supprimer un auteur
        if (foundAuthor.isPresent()) {
            authorDAO.delete(foundAuthor.get());
            System.out.println("Auteur supprimé avec succès !");
        }

        // Vérification après suppression
        List<Author> updatedAuthors = authorDAO.findAll();
        System.out.println(" Liste des auteurs après suppression :");
        for (Author author : updatedAuthors) {
            System.out.println(author);
        }
    }
}
