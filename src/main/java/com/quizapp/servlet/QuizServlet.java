package com.quizapp.servlet;

import com.quizapp.dao.AnswerDAO;
import com.quizapp.dao.AttemptDAO;
import com.quizapp.dao.DBConnection;
import com.quizapp.dao.QuestionDAO;
import com.quizapp.dao.QuizDAO;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/quiz/*")
public class QuizServlet extends javax.servlet.http.HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        try {
            QuizDAO qd = new QuizDAO();
            QuestionDAO qdao = new QuestionDAO();
            if (path.equals("/list") || path.equals("/") || path.equals("")) {
                req.setAttribute("quizzes", qd.listApproved());
                req.getRequestDispatcher("/quizzes.jsp").forward(req, resp);
                return;
            } else if (path.equals("/take")) {
                int id = Integer.parseInt(req.getParameter("id"));
                Quiz quiz = qd.findById(id);
                List<Question> qs = qdao.listByQuiz(id);
                req.setAttribute("quiz", quiz);
                req.setAttribute("questions", qs);
                req.getRequestDispatcher("/take_quiz_timed.jsp").forward(req, resp);
                return;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        try {
            if (path.equals("/start")) {
                User u = (User) req.getSession().getAttribute("user");
                if (u == null) {
                    resp.sendRedirect(req.getContextPath() + "/login.jsp");
                    return;
                }
                int quizId = Integer.parseInt(req.getParameter("quizId"));
                AttemptDAO ad = new AttemptDAO();
                int attemptId = ad.createAttempt(quizId, u.getId(), new Timestamp(System.currentTimeMillis()));
                resp.sendRedirect(req.getContextPath() + "/quiz/take?id=" + quizId + "&attemptId=" + attemptId);
                return;
            } else if (path.equals("/submit")) {
                User u = (User) req.getSession().getAttribute("user");
                if (u == null) {
                    resp.sendRedirect(req.getContextPath() + "/login.jsp");
                    return;
                }
                int quizId = Integer.parseInt(req.getParameter("quizId"));
                int attemptId = Integer.parseInt(req.getParameter("attemptId"));

                QuestionDAO qdao = new QuestionDAO();
                AttemptDAO ad = new AttemptDAO();
                AnswerDAO ansd = new AnswerDAO();
                QuizDAO qzd = new QuizDAO();

                List<Question> qs = qdao.listByQuiz(quizId);

                // Server-side enforcement of time
                com.quizapp.model.Attempt attempt = ad.findById(attemptId);
                if (attempt == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid attempt");
                    return;
                }
                Quiz quiz = qzd.findById(quizId);
                if (quiz == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quiz");
                    return;
                }

                long startMillis = attempt.getStartTime() == null ? 0L : attempt.getStartTime().getTime();
                long allowedMillis = quiz.getDurationMinutes() * 60L * 1000L;
                long now = System.currentTimeMillis();
                boolean isLate = (startMillis > 0) && (now > (startMillis + allowedMillis));
                if (isLate) {
                    req.setAttribute("error", "Quiz time expired. Submission rejected.");
                    req.getRequestDispatcher("/quiz_result.jsp").forward(req, resp);
                    return;
                }

                int total = 0;

                // Transaction: save answers and finalize attempt atomically
                try (java.sql.Connection conn = DBConnection.getConnection()) {
                    try {
                        conn.setAutoCommit(false);

                        for (Question q : qs) {
                            String ans = req.getParameter("q_" + q.getId());
                            boolean correct = (ans != null && ans.equalsIgnoreCase(q.getCorrectOption()));
                            int marks = correct ? q.getMarks() : 0;
                            total += marks;
                            ansd.saveAnswer(conn, attemptId, q.getId(), ans, correct, marks);
                        }

                        ad.finalizeAttempt(conn, attemptId, total, new Timestamp(System.currentTimeMillis()));

                        conn.commit();
                    } catch (Exception e) {
                        conn.rollback();
                        throw e;
                    } finally {
                        conn.setAutoCommit(true);
                    }
                }

                req.setAttribute("score", total);
                int maxMarks = qs.stream().mapToInt(Question::getMarks).sum();
                req.setAttribute("max", maxMarks);
                req.getRequestDispatcher("/quiz_result.jsp").forward(req, resp);
                return;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
