<%@ page import="java.util.*, com.quizapp.model.Attempt" %>
<!doctype html><html><head><title>My History</title></head><body>
<h2>My Attempts</h2>
<table border="1"><tr><th>ID</th><th>Quiz ID</th><th>Score</th><th>Submitted</th></tr>
<% List<Attempt> attempts = (List<Attempt>) request.getAttribute("attempts"); if (attempts!=null) { for (Attempt a: attempts) { %>
<tr><td><%=a.getId()%></td><td><%=a.getQuizId()%></td><td><%=a.getTotalScore()%></td><td><%=a.isSubmitted()%></td></tr>
<% } } %>
</table>
</body></html>
