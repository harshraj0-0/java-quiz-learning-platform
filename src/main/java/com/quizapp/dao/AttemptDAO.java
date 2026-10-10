package com.quizapp.dao;

import com.quizapp.model.Attempt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for quiz attempts. Handles creation, finalization and queries.
 *
 * <p>Note: methods use try-with-resources to ensure Connections/Statements/ResultSets
 * are properly closed.</p>
 */
public class AttemptDAO {

    /**
     * Create a new attempt row and return the generated ID.
     *
     * @param quizId    the quiz id
     * @param userId    the user id
     * @param startTime attempt start timestamp
     * @return generated attempt id
     * @throws SQLException on DB errors
     */
    public int createAttempt(int quizId, int userId, Timestamp startTime) throws SQLException {
        String sql = "INSERT INTO attempts (quiz_id,user_id,start_time,submitted) VALUES (?,?,?,FALSE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, quizId);
            p.setInt(2, userId);
            p.setTimestamp(3, startTime);
            int rows = p.executeUpdate();
            if (rows != 1) {
                throw new SQLException("Failed to insert attempt, update count = " + rows);
            }
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to obtain generated id for attempt.");
                }
            }
        }
    }

    /**
     * Finalize an attempt using the provided Connection. The update is conditional:
     * it only sets submitted=TRUE if submitted was previously FALSE. Returns true if
     * the finalize succeeded (row was updated).
     *
     * @param conn      existing connection (transaction-managed by caller)
     * @param attemptId attempt id
     * @param score     total score
     * @param endTime   end timestamp
     * @return true if the attempt was finalized by this call
     * @throws SQLException on DB errors
     */
    public boolean finalizeAttempt(Connection conn, int attemptId, int score, Timestamp endTime) throws SQLException {
        String sql = "UPDATE attempts SET total_score=?, end_time=?, submitted=TRUE WHERE id=? AND submitted=FALSE";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, score);
            p.setTimestamp(2, endTime);
            p.setInt(3, attemptId);
            int updated = p.executeUpdate();
            return updated == 1;
        }
    }

    /**
     * Convenience finalize (manages its own Connection).
     *
     * @param attemptId attempt id
     * @param score     total score
     * @param endTime   end timestamp
     * @return true if successfully finalized
     * @throws SQLException on DB errors
     */
    public boolean finalizeAttempt(int attemptId, int score, Timestamp endTime) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            return finalizeAttempt(conn, attemptId, score, endTime);
        }
    }

    /**
     * Find an attempt by id.
     *
     * @param attemptId id to lookup
     * @return populated Attempt or null if not found
     * @throws SQLException on DB errors
     */
    public Attempt findById(int attemptId) throws SQLException {
        String sql = "SELECT id, quiz_id, user_id, total_score, submitted, start_time, end_time FROM attempts WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, attemptId);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    Attempt a = new Attempt();
                    a.setId(rs.getInt("id"));
                    a.setQuizId(rs.getInt("quiz_id"));
                    a.setUserId(rs.getInt("user_id"));
                    a.setTotalScore(rs.getInt("total_score"));
                    a.setSubmitted(rs.getBoolean("submitted"));
                    Timestamp st = rs.getTimestamp("start_time");
                    if (st != null) a.setStartTime(new java.util.Date(st.getTime()));
                    Timestamp et = rs.getTimestamp("end_time");
                    if (et != null) a.setEndTime(new java.util.Date(et.getTime()));
                    return a;
                }
            }
        }
        return null;
    }

    /**
     * List attempts for a specific user.
     *
     * @param userId user id
     * @return list of attempts (possibly empty)
     * @throws SQLException on DB errors
     */
    public List<Attempt> listByUser(int userId) throws SQLException {
        String sql = "SELECT id, quiz_id, user_id, total_score, submitted, start_time, end_time FROM attempts WHERE user_id=?";
        List<Attempt> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, userId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Attempt a = new Attempt();
                    a.setId(rs.getInt("id"));
                    a.setQuizId(rs.getInt("quiz_id"));
                    a.setUserId(rs.getInt("user_id"));
                    a.setTotalScore(rs.getInt("total_score"));
                    a.setSubmitted(rs.getBoolean("submitted"));
                    Timestamp st = rs.getTimestamp("start_time");
                    if (st != null) a.setStartTime(new java.util.Date(st.getTime()));
                    Timestamp et = rs.getTimestamp("end_time");
                    if (et != null) a.setEndTime(new java.util.Date(et.getTime()));
                    list.add(a);
                }
            }
        }
        return list;
    }

    /**
     * List attempts for a quiz.
     *
     * @param quizId quiz id
     * @return list of attempts
     * @throws SQLException on DB errors
     */
    public List<Attempt> listByQuiz(int quizId) throws SQLException {
        String sql = "SELECT id, quiz_id, user_id, total_score, submitted, start_time, end_time FROM attempts WHERE quiz_id=?";
        List<Attempt> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, quizId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Attempt a = new Attempt();
                    a.setId(rs.getInt("id"));
                    a.setQuizId(rs.getInt("quiz_id"));
                    a.setUserId(rs.getInt("user_id"));
                    a.setTotalScore(rs.getInt("total_score"));
                    a.setSubmitted(rs.getBoolean("submitted"));
                    Timestamp st = rs.getTimestamp("start_time");
                    if (st != null) a.setStartTime(new java.util.Date(st.getTime()));
                    Timestamp et = rs.getTimestamp("end_time");
                    if (et != null) a.setEndTime(new java.util.Date(et.getTime()));
                    list.add(a);
                }
            }
        }
        return list;
    }
}
