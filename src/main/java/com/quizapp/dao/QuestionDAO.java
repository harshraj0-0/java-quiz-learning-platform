package com.quizapp.dao;

import com.quizapp.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for questions. Returns full question records; be careful not to expose
 * correct answers to untrusted clients (filter at service/controller if needed).
 */
public class QuestionDAO {
    /**
     * List all questions for a quiz.
     *
     * @param quizId quiz id
     * @return list of Question objects
     * @throws SQLException on DB errors
     */
    public List<Question> listByQuiz(int quizId) throws SQLException {
        String sql = "SELECT id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks FROM questions WHERE quiz_id=?";
        List<Question> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, quizId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Question q = new Question();
                    q.setId(rs.getInt("id"));
                    q.setQuizId(rs.getInt("quiz_id"));
                    q.setQuestionText(rs.getString("question_text"));
                    q.setOptionA(rs.getString("option_a"));
                    q.setOptionB(rs.getString("option_b"));
                    q.setOptionC(rs.getString("option_c"));
                    q.setOptionD(rs.getString("option_d"));
                    q.setCorrectOption(rs.getString("correct_option"));
                    q.setMarks(rs.getInt("marks"));
                    list.add(q);
                }
            }
        }
        return list;
    }

    /**
     * Create a question and set the generated id on the provided Question object.
     *
     * @param q question to persist
     * @throws SQLException on DB errors
     */
    public void create(Question q) throws SQLException {
        String sql = "INSERT INTO questions (quiz_id,question_text,option_a,option_b,option_c,option_d,correct_option,marks) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, q.getQuizId());
            p.setString(2, q.getQuestionText());
            p.setString(3, q.getOptionA());
            p.setString(4, q.getOptionB());
            p.setString(5, q.getOptionC());
            p.setString(6, q.getOptionD());
            p.setString(7, q.getCorrectOption());
            p.setInt(8, q.getMarks());

            int rows = p.executeUpdate();
            if (rows != 1) {
                throw new SQLException("Failed to insert question, updateCount=" + rows);
            }
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) {
                    q.setId(rs.getInt(1));
                }
            }
        }
    }
}
