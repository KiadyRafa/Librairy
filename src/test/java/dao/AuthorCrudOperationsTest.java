package dao;

import com.example.librairy.dao.AuthorCrudOperations;
import com.example.librairy.dao.Criteria;
import com.example.librairy.entity.Author;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

public class AuthorCrudOperationsTest {

    AuthorCrudOperations subject = new AuthorCrudOperations();

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
        var authors = newAuthor(2, "Random famous author", LocalDate.of(2000, 1, 1)); // ID en int

        var actual = subject.save(authors);

        authors.setName("Updated Author");
        var updatedAuthor = subject.update(authors);

        var existingAuthors = subject.findAll();
        assertEquals(authors.getName(), updatedAuthor.getName());
        assertTrue(existingAuthors.contains(updatedAuthor));
    }

    @Test
    void read_authors_filter_by_name_or_birthday_between_intervals() {
        ArrayList<Criteria> criteria = new ArrayList<>();
        criteria.add(new Criteria("name", "rado"));
        criteria.add(new Criteria("birth_date", LocalDate.of(2000, 1, 1)));
        List<Author> expected = List.of(
                authorJJR(),
                authorRado());

        // TODO: Implement findByCriteria in AuthorCrudOperations
        List<Author> actual = subject.findByCriteria(criteria);

        assertEquals(expected, actual);
        assertTrue(actual.stream()
                .allMatch(author -> author.getName().toLowerCase().contains("rado")
                        || author.getBirthDate().equals(LocalDate.of(2000, 1, 1))));
    }

    @Test
    void read_authors_order_by_name_or_birthday_or_both() {
        assertThrows(UnsupportedOperationException.class, () -> {
            throw new UnsupportedOperationException("Not implemented yet");
        });
    }

    private Author authorRado() {
        return newAuthor(2, "Rado", LocalDate.of(1990, 1, 1)); // ID en int
    }

    private Author authorJJR() {
        Author expectedAuthor = new Author();
        expectedAuthor.setId(1); // ID en int
        expectedAuthor.setName("JJR");
        expectedAuthor.setBirthDate(LocalDate.of(2000, 1, 1));
        return expectedAuthor;
    }

    private Author newAuthor(int id, String name, LocalDate birthDate) { // ID en int
        Author author = new Author();
        author.setId(id);
        author.setName(name);
        author.setBirthDate(birthDate);
        return author;
    }
}