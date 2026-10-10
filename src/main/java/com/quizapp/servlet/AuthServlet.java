package com.quizapp.servlet;
import com.quizapp.dao.UserDAO; import com.quizapp.model.User; import com.quizapp.util.PasswordUtil;
import javax.servlet.*; import javax.servlet.http.*; import javax.servlet.annotation.*; import java.io.IOException; import java.sql.SQLException;
@WebServlet("/auth") public class AuthServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action")==null?"login":req.getParameter("action");
        if (action.equals("login")) {
            String email = req.getParameter("email"); String password = req.getParameter("password");
            UserDAO dao = new UserDAO();
            try {
                User u = dao.findByEmail(email);
                if (u!=null && PasswordUtil.verify(password, u.getPasswordHash())) {
                    req.getSession().setAttribute("user", u);
                    resp.sendRedirect(req.getContextPath()+"/dashboard.jsp"); return;
                } else { req.setAttribute("error","Invalid credentials"); }
            } catch (SQLException e) { throw new ServletException(e); }
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } else if (action.equals("logout")) {
            req.getSession().invalidate(); resp.sendRedirect(req.getContextPath()+"/index.jsp");
        }
    }
}
