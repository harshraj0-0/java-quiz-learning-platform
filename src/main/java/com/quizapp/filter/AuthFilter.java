package com.quizapp.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Simple authentication filter that protects /admin/* endpoints.
 *
 * <p>It only checks that a "user" attribute exists in the session and does not
 * create a session for anonymous requests (uses getSession(false)).</p>
 */
@WebFilter("/admin/*")
public class AuthFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Avoid creating a session for anonymous users
        HttpSession session = req.getSession(false);
        Object u = (session == null) ? null : session.getAttribute("user");

        if (u == null) {
            // Redirect to login page if not authenticated
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        chain.doFilter(request, response);
    }
}
