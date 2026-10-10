<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Admin - Users</title>
</head>
<body>
<h2>Users</h2>

<h3>Create New User</h3>
<form method="post" action="${pageContext.request.contextPath}/admin/">
  <input type="hidden" name="action" value="createUser"/>
  Name: <input name="name" required/>
  Email: <input name="email" required/>
  Password: <input name="password" required/>
  Role:
  <select name="role">
    <option>ADMIN</option>
    <option>CREATOR</option>
    <option>PARTICIPANT</option>
  </select>
  <input type="submit" value="Create"/>
</form>

<h3>Existing Users</h3>
<table border="1">
  <tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Actions</th></tr>
  <c:forEach var="u" items="${users}">
    <tr>
      <td><c:out value="${u.id}"/></td>
      <td><c:out value="${u.name}"/></td>
      <td><c:out value="${u.email}"/></td>
      <td><c:out value="${u.role}"/></td>
      <td>
        <form method="post" action="${pageContext.request.contextPath}/admin/" style="display:inline">
          <input type="hidden" name="action" value="updateUser"/>
          <input type="hidden" name="id" value="${u.id}"/>
          <input name="name" value="${u.name}" required/>
          <input name="email" value="${u.email}" required/>
          <select name="role">
            <option ${u.role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
            <option ${u.role == 'CREATOR' ? 'selected' : ''}>CREATOR</option>
            <option ${u.role == 'PARTICIPANT' ? 'selected' : ''}>PARTICIPANT</option>
          </select>
          <!-- Leave password blank to keep existing; fill to reset -->
          <input name="password" placeholder="(leave blank to keep)"/>
          <input type="submit" value="Update"/>
        </form>

        <form method="post" action="${pageContext.request.contextPath}/admin/" style="display:inline" onsubmit="return confirm('Delete user?');">
          <input type="hidden" name="action" value="deleteUser"/>
          <input type="hidden" name="id" value="${u.id}"/>
          <input type="submit" value="Delete"/>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>

<p><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a> | <a href="${pageContext.request.contextPath}/admin/pending">Pending Quizzes</a></p>
</body>
</html>
