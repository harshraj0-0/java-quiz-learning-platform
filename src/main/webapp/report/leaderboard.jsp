<%@ page import="java.util.*, com.quizapp.model.Attempt" %>
<!doctype html><html><head><title>Leaderboard</title></head><body>
<h2>Leaderboard (Recent Attempts for Demo)</h2>
<table border="1"><tr><th>Attempt ID</th><th>Quiz ID</th><th>User ID</th><th>Score</th></tr>
<% List<Attempt> attempts = (List<Attempt>) request.getAttribute("attempts"); if (attempts!=null) { for (Attempt a: attempts) { %>
<tr><td><%=a.getId()%></td><td><%=a.getQuizId()%></td><td><%=a.getUserId()%></td><td><%=a.getTotalScore()%></td></tr>
<% } } %>
</table>
</body></html>
