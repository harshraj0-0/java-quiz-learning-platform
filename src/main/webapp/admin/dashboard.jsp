<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Admin Dashboard</title>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<h2>Admin Dashboard - Performance Overview</h2>
<canvas id="perfChart" width="600" height="300"></canvas>

<script>
  // chartLabelsJson and chartDataJson are safe JSON literals produced by the servlet (Gson)
  var labels = <%= request.getAttribute("chartLabelsJson") == null ? "[]" : request.getAttribute("chartLabelsJson") %>;
  var data = <%= request.getAttribute("chartDataJson") == null ? "[]" : request.getAttribute("chartDataJson") %>;

  var ctx = document.getElementById('perfChart').getContext('2d');
  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: 'Average Score',
        data: data
      }]
    },
    options: {}
  });
</script>

<p><a href="<%=request.getContextPath()%>/admin/pending">Pending Quizzes</a> |
   <a href="<%=request.getContextPath()%>/admin/users">Manage Users</a></p>
</body>
</html>
