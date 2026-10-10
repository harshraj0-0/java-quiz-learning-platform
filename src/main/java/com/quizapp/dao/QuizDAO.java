package com.quizapp.dao;

import com.quizapp.model.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for quizzes. Provides listing, creation and approval.
 */
public class QuizDAO {
    /**
     * List approved quizzes.
     */
    public List<Quiz> listApproved() throws SQLException {
        return listByApproval(true);
    }

    /**
     * List pending quizzes (approved = false).
     */
    public List<Quiz> listPending() throws SQLException {
        return listByApproval(false);
    }

    /**
     * Shared implementation for listing by approval flag.
     */
    private List<Quiz> listByApproval(boolean approved) throws SQLException {
        String sql = "SELECT id, title, description, duration_minutes, creator_id, approved FROM quizzes WHERE approved=?";
        List<Quiz> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setBoolean(1, approved);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Quiz q = new Quiz();
                    q.setId(rs.getInt("id"));
                    q.setTitle(rs.getString("title"));
                    q.setDescription(rs.getString("description"));
                    q.setDurationMinutes(rs.getInt("duration_minutes"));
                    q.setCreatorId(rs.getInt("creator_id"));
                    q.setApproved(rs.getBoolean("approved"));
                    list.add(q);
                }
            }
        }
        return list;
    }

    /**
     * Find quiz by id.
     *
     * @param id quiz id
     * @return Quiz or null if not found
     * @throws SQLException on DB errors
     */
    public Quiz findById(int id) throws SQLException {
        String sql = "SELECT id, title, description, duration_minutes, creator_id, approved FROM quizzes WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    Quiz q = new Quiz();
                    q.setId(rs.getInt("id"));
                    q.setTitle(rs.getString("title"));
                    q.setDescription(rs.getString("description"));
                    q.setDurationMinutes(rs.getInt("duration_minutes"));
                    q.setCreatorId(rs.getInt("creator_id"));
                    q.setApproved(rs.getBoolean("approved"));
                    return q;
                }
            }
        }
        return null;
    }

    /**
     * Create a quiz, set its generated id on the provided object and return it.
     *
     * @param q quiz to create
     * @return generated id
     * @throws SQLException on DB errors
     */
    public int create(Quiz q) throws SQLException {
        String sql = "INSERT INTO quizzes (title,description,duration_minutes,creator_id,approved) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, q.getTitle());
            p.setString(2, q.getDescription());
            p.setInt(3, q.getDurationMinutes());
            p.setInt(4, q.getCreatorId());
            p.setBoolean(5, q.isApproved());
            int rows = p.executeUpdate();
            if (rows != 1) {
                throw new SQLException("Failed to insert quiz, updateCount=" + rows);
            }
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    q.setId(id);
                    return id;
                } else {
                    throw new SQLException("Failed to obtain generated id for quiz.");
                }
            }
        }
    }

    /**
     * Approve or disapprove a quiz. Returns true if a row was updated.
     *
     * @param id      quiz id
     * @param approve desired approval flag
     * @return true if update affected a row
     * @throws SQLException on DB errors
     */
    public boolean approve(int id, boolean approve) throws SQLException {
        String sql = "UPDATE quizzes SET approved=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setBoolean(1, approve);
            p.setInt(2, id);
            int updated = p.executeUpdate();
            return updated == 1;
        }
    }
}
