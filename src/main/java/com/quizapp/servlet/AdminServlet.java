package com.quizapp.servlet;

import com.quizapp.dao.AttemptDAO;
import com.quizapp.dao.QuizDAO;
import com.quizapp.dao.UserDAO;
import com.quizapp.model.Quiz;
import com.quizapp.model.User;
import com.quizapp.util.PasswordUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/*")
public class AdminServlet extends javax.servlet.http.HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        try {
            QuizDAO qd = new QuizDAO();
            UserDAO ud = new UserDAO();
            AttemptDAO ad = new AttemptDAO();

            if (path.equals("/pending") || path.equals("/")) {
                req.setAttribute("pending", qd.listPending());
                req.getRequestDispatcher("/WEB-INF/jsp/admin/pending.jsp").forward(req, resp);
                return;
            } else if (path.equals("/users")) {
                req.setAttribute("users", ud.findAll());
                req.getRequestDispatcher("/WEB-INF/jsp/admin/users.jsp").forward(req, resp);
                return;
            } else if (path.equals("/dashboard")) {
                List<Quiz> quizzes = qd.listApproved();
                List<String> labels = new ArrayList<>();
                List<Integer> averages = new ArrayList<>();
                for (Quiz q : quizzes) {
                    labels.add(q.getTitle());
                    List<com.quizapp.model.Attempt> attempts = ad.listByQuiz(q.getId());
                    int sum = 0, count = 0;
                    for (com.quizapp.model.Attempt a : attempts) { sum += a.getTotalScore(); count++; }
                    averages.add((count == 0) ? 0 : (sum / count));
                }
                Gson gson = new Gson();
                req.setAttribute("chartLabelsJson", gson.toJson(labels));
                req.setAttribute("chartDataJson", gson.toJson(averages));
                req.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(req, resp);
                return;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            UserDAO ud = new UserDAO();
            QuizDAO qd = new QuizDAO();

            if ("approve".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                qd.approve(id, true);
                resp.sendRedirect(req.getContextPath() + "/admin/pending");
                return;
            } else if ("reject".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                qd.approve(id, false);
                resp.sendRedirect(req.getContextPath() + "/admin/pending");
                return;
            } else if ("createUser".equals(action)) {
                User u = new User();
                u.setName(req.getParameter("name"));
                u.setEmail(req.getParameter("email"));
                String raw = req.getParameter("password");
                u.setPasswordHash(raw == null ? null : PasswordUtil.hash(raw)); // HASH HERE
                u.setRole(req.getParameter("role"));
                ud.create(u);
                resp.sendRedirect(req.getContextPath() + "/admin/users");
                return;
            } else if ("updateUser".equals(action)) {
                User u = new User();
                u.setId(Integer.parseInt(req.getParameter("id")));
                u.setName(req.getParameter("name"));
                u.setEmail(req.getParameter("email"));
                u.setRole(req.getParameter("role"));
                String newPass = req.getParameter("password");
                if (newPass != null && !newPass.isBlank()) {
                    u.setPasswordHash(PasswordUtil.hash(newPass));
                } else {
                    u.setPasswordHash(null); // indicates leave unchanged
                }
                ud.update(u);
                resp.sendRedirect(req.getContextPath() + "/admin/users");
                return;
            } else if ("deleteUser".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                ud.delete(id);
                resp.sendRedirect(req.getContextPath() + "/admin/users");
                return;
            }

            resp.sendRedirect(req.getContextPath() + "/admin/pending");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
