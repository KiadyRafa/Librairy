package com.example.librairy.dao;

import com.example.librairy.entity.Book;
import com.example.librairy.db.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookCrudOperations implements CrudOperations<Book> {
    private static final Logger LOGGER = Logger.getLogger(BookCrudOperations.class.getName());

    @Override
    public Optional<Book> findById(int id) {
        String sql = "SELECT id, title, author_id FROM book WHERE id = ?";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("author_id")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du livre avec l'ID " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author_id FROM book";
        try (Connection conn = DataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("author_id")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de la liste des livres", e);
        }
        return books;
    }

    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO book (title, author_id) VALUES (?, ?)";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setInt(2, book.getAuthorId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "Aucune insertion effectuée pour le livre : " + book);
            } else {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        book.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'enregistrement du livre", e);
        }
        return book;
    }

    @Override
    public Book update(Book book) {
        String sql = "UPDATE book SET title = ?, author_id = ? WHERE id = ?";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setInt(2, book.getAuthorId());
            pstmt.setInt(3, book.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "Aucune mise à jour effectuée pour le livre ID " + book.getId());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du livre", e);
        }
        return book;
    }

    @Override
    public List<Book> findByCriteria(List<Criteria> criteriaList) {
        List<Book> books = new ArrayList<>();

        if (criteriaList == null || criteriaList.isEmpty()) {
            return findAll();
        }

        StringBuilder sql = new StringBuilder("SELECT id, title, author_id FROM book WHERE 1=1");

        for (Criteria criteria : criteriaList) {
            sql.append(" AND ").append(criteria.getField()).append(" LIKE ?");
        }

        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < criteriaList.size(); i++) {
                pstmt.setString(i + 1, "%" + criteriaList.get(i).getValue() + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("author_id")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche par critères", e);
        }
        return books;
    }

    @Override
    public void delete(Book book) {
        String sql = "DELETE FROM book WHERE id = ?";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, book.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "Aucune suppression effectuée pour le livre ID " + book.getId());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du livre", e);
        }
    }
    public void deleteAll() {
        String sql = "DELETE FROM author";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
