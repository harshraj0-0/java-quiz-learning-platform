<%@ page import="com.quizapp.model.User" %><% User u = (User) session.getAttribute("user"); %>
<!doctype html><html><head><title>Dashboard</title></head><body>
<h2>Dashboard</h2>
<% if (u==null) { %>
<p>Please <a href="login.jsp">login</a>.</p>
<% } else { %>
<p>Welcome, <b><%=u.getName()%></b> (<%=u.getRole()%>)</p>
<ul>
<li><a href="/quiz/list">Available Quizzes</a></li>
<% if ("CREATOR".equals(u.getRole())) { %>
<li><a href="/creator/new">Create Quiz</a></li>
<% } %>
<% if ("ADMIN".equals(u.getRole())) { %>
<li><a href="/admin/pending">Pending Quizzes</a></li>
<li><a href="/admin/users">Manage Users</a></li>
<% } %>
<li><a href="/report/history">My Performance</a></li>
</ul>
<form method="post" action="auth"><input type="hidden" name="action" value="logout"/><input type="submit" value="Logout"/></form>
<% } %>
</body></html>
