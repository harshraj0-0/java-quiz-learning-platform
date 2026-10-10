package com.quizapp.servlet;
import com.quizapp.dao.QuizDAO; import com.quizapp.dao.AttemptDAO; import com.quizapp.model.Attempt; import com.quizapp.model.Quiz; import com.quizapp.model.User;
import javax.servlet.*; import javax.servlet.http.*; import javax.servlet.annotation.*; import java.io.IOException; import java.sql.SQLException; import java.util.*;
@WebServlet("/report/*") public class ReportServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo()==null?"":req.getPathInfo();
        try {
            if (path.equals("/leaderboard")) {
                // Simple leaderboard: average score per user (demo query omitted - show all attempts)
                AttemptDAO ad = new AttemptDAO(); // We'll show recent attempts
                req.setAttribute("attempts", ad.listByQuiz(1)); // demo for quiz 1
                req.getRequestDispatcher("/report/leaderboard.jsp").forward(req, resp); return;
            } else if (path.equals("/history")) {
                User u = (User) req.getSession().getAttribute("user"); if (u==null) { resp.sendRedirect(req.getContextPath()+"/login.jsp"); return; }
                AttemptDAO ad = new AttemptDAO();
                req.setAttribute("attempts", ad.listByUser(u.getId()));
                req.getRequestDispatcher("/report/history.jsp").forward(req, resp); return;
            }
        } catch (SQLException e) { throw new ServletException(e); }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
