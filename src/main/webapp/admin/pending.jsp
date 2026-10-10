<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Pending Quizzes</title>
</head>
<body>
<h2>Pending Quizzes</h2>

<table border="1">
  <tr><th>ID</th><th>Title</th><th>Creator</th><th>Action</th></tr>
  <c:forEach var="q" items="${pending}">
    <tr>
      <td><c:out value="${q.id}"/></td>
      <td><c:out value="${q.title}"/></td>
      <td><c:out value="${q.creatorId}"/></td>
      <td>
        <form method="post" action="${pageContext.request.contextPath}/admin/" style="display:inline">
          <input type="hidden" name="id" value="${q.id}"/>
          <button type="submit" name="action" value="approve">Approve</button>
        </form>
        <form method="post" action="${pageContext.request.contextPath}/admin/" style="display:inline">
          <input type="hidden" name="id" value="${q.id}"/>
          <button type="submit" name="action" value="reject">Reject</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>
</body>
</html>
