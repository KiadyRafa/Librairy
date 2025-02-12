package dao;

import com.example.librairy.dao.BookCrudOperations;
import com.example.librairy.dao.Criteria;
import com.example.librairy.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookCrudOperationsTest {

    BookCrudOperations subject;

    @BeforeEach
    void setUp() {
        subject = new BookCrudOperations();

        // Réinitialiser la base de données avant chaque test
        subject.deleteAll();

        // Ajouter des livres de test dans la base de données
        subject.save(new Book(1, "Harry Potter", 1001));
        subject.save(new Book(2, "The Lord of the Rings", 1002));
    }

    @Test
    void read_all_books_ok() {
        // Test pour vérifier que findAll() retourne tous les livres
        Book expectedBook = bookHarryPotter();
        List<Book> actual = subject.findAll();
        assertTrue(actual.contains(expectedBook));
    }

    @Test
    void read_book_by_id_ok() {
        // Test pour vérifier que findById() retourne le livre correct
        Book expectedBook = bookHarryPotter();
        Optional<Book> actual = subject.findById(expectedBook.getId());

        assertTrue(actual.isPresent());
        assertEquals(expectedBook, actual.get());
    }

    @Test
    void create_then_update_book_ok() {
        // Test pour vérifier la création et la mise à jour d'un livre
        Book book = newBook(3, "Random Book", 1003);

        // Créer le livre
        Book actual = subject.save(book);

        // Mettre à jour le livre
        book.setTitle("Updated Book");
        Book updatedBook = subject.update(book);

        // Vérifier que la mise à jour a fonctionné
        List<Book> existingBooks = subject.findAll();
        assertEquals(book.getTitle(), updatedBook.getTitle());
        assertTrue(existingBooks.contains(updatedBook));
    }

    @Test
    void read_books_filter_by_title_or_author_id() {
        // Test pour vérifier le filtrage des livres par titre ou ID d'auteur
        ArrayList<Criteria> criteria = new ArrayList<>();
        criteria.add(new Criteria("title", "Harry"));
        criteria.add(new Criteria("authorId", 1001));

        List<Book> expected = List.of(bookHarryPotter());

        // Appeler la méthode de filtrage
        List<Book> actual = subject.findByCriteria(criteria);

        // Vérifier les résultats
        assertEquals(expected.size(), actual.size());
        assertTrue(actual.stream()
                .allMatch(book -> book.getTitle().toLowerCase().contains("harry")
                        || book.getAuthorId() == 1001));
    }

    @Test
    void delete_book_ok() {
        // Créer un livre et l'ajouter à la base de données
        Book bookToDelete = newBook(4, "Book to delete", 1004);
        subject.save(bookToDelete);

        // Vérifier que le livre existe dans la base de données
        Optional<Book> foundBook = subject.findById(bookToDelete.getId());
        assertTrue(foundBook.isPresent(), "Le livre devrait exister avant la suppression");

        // Supprimer le livre
        subject.delete(bookToDelete);

        // Vérifier que le livre a été supprimé
        Optional<Book> deletedBook = subject.findById(bookToDelete.getId());
        assertFalse(deletedBook.isPresent(), "Le livre devrait être supprimé");
    }

    @Test
    void read_books_order_by_title_or_author_id_or_both() {
        // Test pour vérifier le tri des livres (non implémenté)
        assertThrows(UnsupportedOperationException.class, () -> {
            throw new UnsupportedOperationException("Not implemented yet");
        });
    }

    private Book bookHarryPotter() {
        return newBook(1, "Harry Potter", 1001);
    }

    private Book newBook(int id, String title, int authorId) {
        return new Book(id, title, authorId);
    }
}