package com.quizapp.servlet;
import com.quizapp.dao.QuizDAO; import com.quizapp.dao.QuestionDAO; import com.quizapp.model.Quiz; import com.quizapp.model.Question; import com.quizapp.model.User;
import javax.servlet.*; import javax.servlet.http.*; import javax.servlet.annotation.*; import java.io.IOException; import java.sql.SQLException;
@WebServlet("/creator/*") public class CreatorServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo()==null?"":req.getPathInfo();
        try {
            if (path.equals("/new")) {
                req.getRequestDispatcher("/creator/new_quiz.jsp").forward(req, resp); return;
            }
        } catch (Exception e) { throw new ServletException(e); }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action"); try {
            QuizDAO qd = new QuizDAO(); QuestionDAO qud = new QuestionDAO();
            User u = (User) req.getSession().getAttribute("user"); if (u==null) { resp.sendRedirect(req.getContextPath()+"/login.jsp"); return; }
            if ("createQuiz".equals(action)) {
                Quiz q = new Quiz(); q.setTitle(req.getParameter("title")); q.setDescription(req.getParameter("description")); q.setDurationMinutes(Integer.parseInt(req.getParameter("duration"))); q.setCreatorId(u.getId()); q.setApproved(false);
                int quizId = qd.create(q);
                resp.sendRedirect(req.getContextPath()+"/creator/addQuestions?quizId="+quizId); return;
            } else if ("addQuestion".equals(action)) {
                Question q = new Question(); q.setQuizId(Integer.parseInt(req.getParameter("quizId"))); q.setQuestionText(req.getParameter("question_text")); q.setOptionA(req.getParameter("option_a")); q.setOptionB(req.getParameter("option_b")); q.setOptionC(req.getParameter("option_c")); q.setOptionD(req.getParameter("option_d")); q.setCorrectOption(req.getParameter("correct_option")); q.setMarks(Integer.parseInt(req.getParameter("marks")));
                qud.create(q); resp.sendRedirect(req.getContextPath()+"/creator/addQuestions?quizId="+req.getParameter("quizId")); return;
            }
        } catch (SQLException e) { throw new ServletException(e); }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
