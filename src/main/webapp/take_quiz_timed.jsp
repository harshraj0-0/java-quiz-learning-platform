<%@ page import="java.util.*, com.quizapp.model.Question, com.quizapp.model.Quiz" %>
<% Quiz quiz = (Quiz) request.getAttribute("quiz"); List<Question> qs = (List<Question>) request.getAttribute("questions"); %>
<!doctype html><html><head><title>Take Quiz</title></head><body>
<h2>Quiz: <%=quiz.getTitle()%></h2>
<p><%=quiz.getDescription()%></p>
<p>Duration: <%=quiz.getDurationMinutes()%> minutes</p>

<!-- Start creates attempt and redirects to same page with attemptId -->
<form method="post" action="<%=request.getContextPath()%>/quiz/start">
<input type="hidden" name="quizId" value="<%=quiz.getId()%>"/>
<input type="submit" value="Start Quiz"/>
</form>

<% String attemptId = request.getParameter("attemptId"); if (attemptId!=null) { %>
  <div id="timer">Time left: <span id="time"></span></div>
  <form method="post" action="<%=request.getContextPath()%>/quiz/submit" id="quizForm">
  <input type="hidden" name="quizId" value="<%=quiz.getId()%>"/>
  <input type="hidden" name="attemptId" value="<%=attemptId%>"/>
  <% for (Question q: qs) { %>
    <div style="border:1px solid #ccc;padding:8px;margin:8px">
      <p><b>Q<%=q.getId()%>:</b> <%=q.getQuestionText()%></p>
      <label><input type="radio" name="q_<%=q.getId()%>" value="A"/> <%=q.getOptionA()%></label><br/>
      <label><input type="radio" name="q_<%=q.getId()%>" value="B"/> <%=q.getOptionB()%></label><br/>
      <label><input type="radio" name="q_<%=q.getId()%>" value="C"/> <%=q.getOptionC()%></label><br/>
      <label><input type="radio" name="q_<%=q.getId()%>" value="D"/> <%=q.getOptionD()%></label><br/>
    </div>
  <% } %>
  <input type="submit" value="Submit"/>
  </form>

<script>
// simple client-side countdown from quiz.duration_minutes
var minutes = <%=quiz.getDurationMinutes()%>;
var seconds = minutes * 60;
var display = document.getElementById('time');
var timer = setInterval(function(){
  if(seconds<=0){ clearInterval(timer); document.getElementById('quizForm').submit(); }
  var m = Math.floor(seconds/60); var s = seconds%60; display.innerText = m+":"+(s<10?"0":"") + s; seconds--; },1000);
</script>

<% } %>
</body></html>
