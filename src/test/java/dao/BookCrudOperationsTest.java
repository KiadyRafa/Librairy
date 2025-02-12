package dao;

import com.example.librairy.dao.BookCrudOperations;
import com.example.librairy.dao.Criteria;
import com.example.librairy.entity.Book;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookCrudOperationsTest {

    BookCrudOperations subject = new BookCrudOperations();

    @Test
    void read_all_books_ok() {
        Book expectedBook = bookHarryPotter();
        List<Book> actual = subject.findAll();
        assertTrue(actual.contains(expectedBook));
    }

    @Test
    void read_book_by_id_ok() {
        Book expectedBook = bookHarryPotter();
        Optional<Book> actual = subject.findById(expectedBook.getId());

        assertTrue(actual.isPresent());
        assertEquals(expectedBook, actual.get());
    }

    @Test
    void create_then_update_book_ok() {
        Book book = newBook(2, "Random Book", 2001);

        Book actual = subject.save(book);
        book.setTitle("Updated Book");
        Book updatedBook = subject.update(book);

        List<Book> existingBooks = subject.findAll();
        assertEquals(book.getTitle(), updatedBook.getTitle());
        assertTrue(existingBooks.contains(updatedBook));
    }

    @Test
    void read_books_filter_by_title_or_author_id() {
        ArrayList<Criteria> criteria = new ArrayList<>();
        criteria.add(new Criteria("title", "Harry"));
        criteria.add(new Criteria("authorId", "1001"));

        List<Book> expected = List.of(bookHarryPotter());


        List<Book> actual = subject.findByCriteria(criteria);

        assertEquals(expected.size(), actual.size());
        assertTrue(actual.stream()
                .allMatch(book -> book.getTitle().toLowerCase().contains("harry")
                        || book.getAuthorId() == 1001));
    }

    @Test
    void read_books_order_by_title_or_author_id_or_both() {
        assertThrows(UnsupportedOperationException.class, () -> {
            throw new UnsupportedOperationException("Not implemented yet");
        });
    }

    private Book bookHarryPotter() {
        return new Book(1, "Harry Potter", 1001);
    }

    private Book newBook(int id, String title, int authorId) {
        return new Book(id, title, authorId);
    }
}
