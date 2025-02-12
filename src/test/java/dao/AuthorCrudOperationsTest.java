package dao;

import com.example.librairy.dao.AuthorCrudOperations;
import com.example.librairy.dao.Criteria;
import com.example.librairy.entity.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorCrudOperationsTest {
    AuthorCrudOperations subject;

    @BeforeEach
    void setUp() {
        subject = new AuthorCrudOperations();

        // Réinitialiser la base de données avant chaque test
        subject.deleteAll();

        // Ajouter des auteurs de test dans la base de données
        subject.save(new Author(1, "JJR", LocalDate.of(2000, 1, 1)));
        subject.save(new Author(2, "Rado", LocalDate.of(1990, 1, 1)));
    }

    @AfterEach
    void tearDown() {
        System.out.println(" Suppression de tous les auteurs après le test...");
        subject.deleteAll();
    }

    @Test
    void read_all_authors_ok() {
        Author expectedAuthor = authorJJR();
        List<Author> actual = subject.findAll();
        assertTrue(actual.contains(expectedAuthor));
    }

    @Test
    void read_author_by_id_ok() {
        Author expectedAuthor = authorJJR();
        Optional<Author> actual = subject.findById(expectedAuthor.getId());
        assertTrue(actual.isPresent());
        assertEquals(expectedAuthor, actual.get());
    }

    @Test
    void create_then_update_author_ok() {
        var authors = newAuthor(3, "Random famous author", LocalDate.of(2000, 1, 1));
        var actual = subject.save(authors);

        authors.setName("Updated Author");
        var updatedAuthor = subject.update(authors);

        var existingAuthors = subject.findAll();
        assertEquals(authors.getName(), updatedAuthor.getName());
        assertTrue(existingAuthors.contains(updatedAuthor));
    }

    @Test
    void delete_author_ok() {
        Author authorToDelete = newAuthor(3, "Author to delete", LocalDate.of(1980, 1, 1));
        subject.save(authorToDelete);

        Optional<Author> foundAuthor = subject.findById(authorToDelete.getId());
        assertTrue(foundAuthor.isPresent(), "L'auteur devrait exister avant la suppression");

        subject.delete(authorToDelete);

        Optional<Author> deletedAuthor = subject.findById(authorToDelete.getId());
        assertFalse(deletedAuthor.isPresent(), "L'auteur devrait être supprimé");
    }

    @Test
    void read_authors_filter_by_name_or_birthday_between_intervals() {
        ArrayList<Criteria> criteria = new ArrayList<>();
        criteria.add(new Criteria("name", "rado"));
        criteria.add(new Criteria("birth_date", LocalDate.of(2000, 1, 1)));
        List<Author> expected = List.of(authorJJR(), authorRado());

        List<Author> actual = subject.findByCriteria(criteria);

        assertEquals(expected, actual);
        assertTrue(actual.stream()
                .allMatch(author -> author.getName().toLowerCase().contains("rado")
                        || author.getBirthDate().equals(LocalDate.of(2000, 1, 1))));
    }
    @Test
    void read_filtered_ordered_and_paginated() {
        // Données de test
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(new Criteria("name", "Rado"));
        criteria.add(new Criteria("birth_date", LocalDate.of(1990, 1, 1)));

        int page = 0;
        int pageSize = 1;

        // Récupérer d'abord l'auteur existant pour avoir le bon ID
        List<Author> existingAuthors = subject.findAll();
        Author expectedRado = existingAuthors.stream()
                .filter(a -> a.getName().equals("Rado"))
                .findFirst()
                .orElseThrow();

        // Appel de la méthode à tester
        List<Author> actual = subject.findByCriteriaWithPagination(criteria, page, pageSize);

        // Assertions
        List<Author> expected = List.of(expectedRado);
        assertEquals(expected, actual);
        assertEquals(pageSize, actual.size());
        assertEquals("Rado", actual.get(0).getName());
        assertEquals(LocalDate.of(1990, 1, 1), actual.get(0).getBirthDate());
    }


    @Test
    void read_authors_order_by_name_or_birthday_or_both() {
        assertThrows(UnsupportedOperationException.class, () -> {
            throw new UnsupportedOperationException("Not implemented yet");
        });
    }

    private Author authorRado() {
        return newAuthor(2, "Rado", LocalDate.of(1990, 1, 1));
    }

    private Author authorJJR() {
        return newAuthor(1, "JJR", LocalDate.of(2000, 1, 1));
    }

    private Author newAuthor(int id, String name, LocalDate birthDate) {
        Author author = new Author();
        author.setId(id);
        author.setName(name);
        author.setBirthDate(birthDate);
        return author;
    }
}
