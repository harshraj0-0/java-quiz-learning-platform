package com.quizapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO for persisting individual answer records.
 *
 * <p>Provides both a convenience method (which opens a Connection) and a
 * transaction-aware variant that accepts an existing Connection.</p>
 */
public class AnswerDAO {

    /**
     * Save an answer using a fresh connection.
     *
     * @param attemptId    attempt identifier
     * @param questionId   question identifier
     * @param selectedOption option selected by the user (may be null)
     * @param isCorrect    whether the answer was correct
     * @param marksAwarded marks awarded for this answer
     * @throws SQLException on DB errors
     */
    public void saveAnswer(int attemptId, int questionId, String selectedOption, boolean isCorrect, int marksAwarded) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            saveAnswer(conn, attemptId, questionId, selectedOption, isCorrect, marksAwarded);
        }
    }

    /**
     * Transaction-aware save: uses the provided Connection and does not manage
     * commit/rollback (caller is responsible).
     *
     * @param conn          existing JDBC connection (must be open)
     * @param attemptId     attempt id
     * @param questionId    question id
     * @param selectedOption selected option
     * @param isCorrect     correctness flag
     * @param marksAwarded  marks awarded
     * @throws SQLException on DB errors
     */
    public void saveAnswer(Connection conn, int attemptId, int questionId, String selectedOption, boolean isCorrect, int marksAwarded) throws SQLException {
        String sql = "INSERT INTO answers (attempt_id, question_id, selected_option, is_correct, marks_awarded) VALUES (?,?,?,?,?)";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, attemptId);
            p.setInt(2, questionId);
            p.setString(3, selectedOption);
            p.setBoolean(4, isCorrect);
            p.setInt(5, marksAwarded);
            p.executeUpdate();
        }
    }
}
