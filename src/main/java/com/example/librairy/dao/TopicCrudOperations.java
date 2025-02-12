package com.example.librairy.dao;

import com.example.librairy.entity.Topic;
import com.example.librairy.db.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TopicCrudOperations implements CrudOperations<Topic> {

    @Override
    public Optional<Topic> findById(int id) {
        String sql = "SELECT * FROM topic WHERE id = ?";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Topic(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Topic> findAll() {
        List<Topic> topics = new ArrayList<>();
        String sql = "SELECT * FROM topic";
        try (Connection conn = DataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                topics.add(new Topic(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topics;
    }

    @Override
    public Topic save(Topic topic) {
        String sql = "INSERT INTO topic (name) VALUES (?)";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, topic.getName());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insertion échouée, aucune ligne affectée.");
            }

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                topic.setId(rs.getInt(1));
            } else {
                throw new SQLException("Échec de la récupération de l'ID généré.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topic;
    }


    @Override
    public Topic update(Topic topic) {
        String sql = "UPDATE topic SET name = ? WHERE id = ?";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, topic.getName());
            pstmt.setInt(2, topic.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topic;
    }

    @Override
    public void delete(Topic topic) {
        String sql = "DELETE FROM topic WHERE id = ?";
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, topic.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // implementation findByCriteria
    public List<Topic> findByCriteria(List<Criteria> criteriaList) {
        List<Topic> topics = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM topic WHERE 1=1");

        for (Criteria criteria : criteriaList) {
            sql.append(" AND ").append(criteria.getField()).append(" LIKE ?");
        }

        System.out.println("Requête SQL exécutée : " + sql); // Debug

        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < criteriaList.size(); i++) {
                pstmt.setString(i + 1, "%" + criteriaList.get(i).getValue() + "%");
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                topics.add(new Topic(rs.getInt("id"), rs.getString("name")));
            }

            System.out.println("Résultats trouvés : " + topics); // Debug

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topics;
    }

}
