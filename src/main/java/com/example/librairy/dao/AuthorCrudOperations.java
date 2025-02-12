package com.example.librairy.dao;

import com.example.librairy.entity.Author;
import com.example.librairy.db.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorCrudOperations implements CrudOperations<Author> {

    @Override
    public Optional<Author> findById(int id) {
        String sql = "SELECT * FROM author WHERE id = ?";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Author(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("birth_date").toLocalDate() // Récupération de birthDate
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT * FROM author";
        try (Connection conn = DataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                authors.add(new Author(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("birth_date").toLocalDate() // Récupération de birthDate
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    public Author save(Author author) {
        String sql = "INSERT INTO author (name, birth_date) VALUES (?, ?)";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, author.getName());
            pstmt.setDate(2, Date.valueOf(author.getBirthDate())); // Ajout de birthDate
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                author.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;
    }

    @Override
    public Author update(Author author) {
        String sql = "UPDATE author SET name = ?, birth_date = ? WHERE id = ?";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, author.getName());
            pstmt.setDate(2, Date.valueOf(author.getBirthDate())); // Ajout de birthDate
            pstmt.setInt(3, author.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;
    }

    @Override
    public void delete(Author author) {
        String sql = "DELETE FROM author WHERE id = ?";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, author.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    // Implémentation de findByCriteria
    public List<Author> findByCriteria(List<Criteria> criteria) {
        List<Author> authors = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM author WHERE 1=1");

        // Construction dynamique de la requête SQL
        for (Criteria criterion : criteria) {
            switch (criterion.getField()) {
                case "name":
                    sql.append(" AND name LIKE ?");
                    break;
                case "birth_date":
                    sql.append(" AND birth_date = ?");
                    break;
                default:
                    throw new IllegalArgumentException("Critère non supporté : " + criterion.getField());
            }
        }

        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int index = 1;
            for (Criteria criterion : criteria) {
                switch (criterion.getField()) {
                    case "name":
                        pstmt.setString(index++, "%" + criterion.getValue() + "%");
                        break;
                    case "birth_date":
                        pstmt.setDate(index++, Date.valueOf((LocalDate) criterion.getValue()));
                        break;
                }
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                authors.add(new Author(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("birth_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }
}