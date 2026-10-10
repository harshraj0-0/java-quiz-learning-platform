# Java Online Quiz Platform 🎯

A full-featured *Java Web-Based Quiz Platform* built with *Servlets, JSP, JDBC (MySQL)* and clean *MVC architecture*.  
It supports *Admin, Creator, and Participant* roles and manages the *entire quiz lifecycle* – from creation and approval to attempting and reporting.

---

## 🚀 Highlights

- 🔐 Secure authentication with *BCrypt-hashed passwords*
- 👥 Role-based access: *ADMIN / CREATOR / PARTICIPANT*
- 🧩 Quiz builder with multiple questions & options
- ✅ Admin quiz approval workflow
- ⏱ Timed quiz attempts (client + server enforced)
- 📊 Attempt history, simple leaderboard & *Chart.js* powered admin dashboard
- 🧱 Clean *MVC + DAO*-based architecture
- 🛠 Built with *Maven, deployable as a **WAR* on any servlet container

---

### 🛠 Technologies Used

Backend

Java 8+ (or 23 since your pom specifies 23)

Servlet API 4.0.1

JSP 2.3

BCrypt (jbcrypt) for password hashing

JDBC + PreparedStatement

MySQL

HikariCP (Connection Pooling, recommended)

Frontend

HTML, CSS, JSP

JSTL

Chart.js (Admin dashboard chart)

Build Tool

Maven

WAR Packaging


## ✨ Features

### 👤 User Management

- Secure login using *BCrypt-hashed passwords*
- Supported roles:
  - ADMIN
  - CREATOR
  - PARTICIPANT
- Admin capabilities:
  - Create, update, delete users
  - Assign / change roles
- *Session-based authentication*
- /admin/* protected via *Servlet Filter*
- Optional role-based filters (e.g., RoleFilter) can be enabled for fine-grained control

---

### 🧱 Quiz Creation (CREATOR)

- Create *quiz metadata*:
  - Title
  - Description
  - Duration (in minutes)
- Add *unlimited questions* per quiz with:
  - Question text
  - Options: *A / B / C / D*
  - Correct option
  - Marks per question
- Created quizzes are saved as *PENDING* and require *Admin approval* before they become visible to participants.

---

### ✔ Quiz Approval (ADMIN)

- View all *pending quizzes*
- *Approve / Reject* quizzes
  - Approved quizzes become visible to participants
  - Rejected quizzes can be edited or recreated
- Manage users and roles
- *Admin Dashboard*:
  - Visual stats using *Chart.js* (e.g. number of quizzes, attempts, users, etc.)

---

### 🎮 Quiz Taking (Participant)

- View all *approved quizzes*
- Start quiz → attempt is *recorded in DB*
- *Timed quiz*:
  - Client-side countdown (JavaScript timer)
  - Server-side enforcement of duration for security
- On submit:
  - Answers are stored securely in DB
  - Score is *calculated automatically*
- Show:
  - Quiz result
  - Attempt summary

---

### 📊 Reports

- *Attempt history* per participant
- *Simple leaderboard* (demonstration-level; can be extended)
- Admin view:
  - Global statistics
  - Performance overview via *Chart.js*

---
### ⚙ How to Run the Project

⿡ Clone the repository: https://github.com/CodeConstructors1/Java-Online-Quiz-Platform.git

⿢ Configure database: Update DBConnection.java or connection pool (if improved version is used):

jdbc:mysql://localhost:3306/quiz_platform

user=root

password=

⿣ Build project: mvn clean package

⿤ Deploy WAR file:

Deploy quiz-platform-full.war to:

Apache Tomcat 9/10

Jetty

Any Java EE servlet container

⿥ Access application: http://localhost:8080/quiz-platform-full/

## 🧱 Project Architecture

```bash
quiz-platform/
├─ pom.xml
├─ src/
│  ├─ main/
│  │  ├─ java/com/quizapp/
│  │  │  ├─ dao/
│  │  │  │  ├─ DBConnection.java
│  │  │  │  ├─ UserDAO.java
│  │  │  │  ├─ QuizDAO.java
│  │  │  │  ├─ QuestionDAO.java
│  │  │  │  ├─ AttemptDAO.java
│  │  │  │  └─ AnswerDAO.java
│  │  │  ├─ model/
│  │  │  │  ├─ User.java
│  │  │  │  ├─ Quiz.java
│  │  │  │  ├─ Question.java
│  │  │  │  ├─ Attempt.java
│  │  │  │  └─ Answer.java
│  │  │  ├─ servlet/
│  │  │  │  ├─ AuthServlet.java
│  │  │  │  ├─ AdminServlet.java
│  │  │  │  ├─ QuizServlet.java
│  │  │  │  ├─ CreatorServlet.java
│  │  │  │  └─ ReportServlet.java
│  │  │  ├─ filter/
│  │  │  │  ├─ AuthFilter.java
│  │  │  │  └─ (RoleFilter.java / CsrfFilter.java - optional)
│  │  │  └─ util/
│  │  │     ├─ PasswordUtil.java
│  │  │     └─ other helpers...
│  │  ├─ webapp/
│  │  │  ├─ WEB-INF/
│  │  │  │  ├─ web.xml
│  │  │  │  └─ jsp/
│  │  │  │     ├─ admin/dashboard.jsp
│  │  │  │     ├─ admin/pending.jsp
│  │  │  │     └─ admin/users.jsp
│  │  │  ├─ index.jsp
│  │  │  ├─ login.jsp
│  │  │  ├─ quizzes.jsp
│  │  │  ├─ take_quiz_timed.jsp
│  │  │  └─ quiz_result.jsp
│  └─ test/
├─ README.md



