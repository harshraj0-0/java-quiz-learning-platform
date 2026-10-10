<%@ page import="java.util.*" %>
<% String quizId = request.getParameter("quizId"); %>
<!doctype html><html><head><title>Add Questions</title></head><body>
<h2>Add Question to Quiz ID: <%=quizId%></h2>
<form method="post" action="/creator/">
<input type="hidden" name="action" value="addQuestion"/>
<input type="hidden" name="quizId" value="<%=quizId%>"/>
Question: <input name="question_text"/><br/>
A: <input name="option_a"/> B: <input name="option_b"/> C: <input name="option_c"/> D: <input name="option_d"/><br/>
Correct option (A/B/C/D): <input name="correct_option"/><br/>
Marks: <input name="marks" value="1"/><br/>
<input type="submit" value="Add Question"/>
</form>
<p>When done, wait for admin approval.</p>
</body></html>
