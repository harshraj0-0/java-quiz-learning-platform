<%@ page import="java.util.*, com.quizapp.model.Quiz" %>
<!doctype html><html><head><title>Quizzes</title></head><body>
<h2>Available Quizzes</h2>
<ul>
<% List<Quiz> qs = (List<Quiz>) request.getAttribute("quizzes"); if (qs!=null) { for (Quiz q: qs) { %>
  <li><b><%=q.getTitle()%></b> - <%=q.getDescription()%> (Duration: <%=q.getDurationMinutes()%> mins) 
  <a href="<%=request.getContextPath()%>/quiz/take?id=<%=q.getId()%>">View & Start</a></li>
<% } } %>
</ul>
</body></html>
