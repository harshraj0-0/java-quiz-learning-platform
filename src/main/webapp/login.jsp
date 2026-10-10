<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html><html><head><title>Login</title></head><body>
<h2>Login</h2>
<form method="post" action="auth">
<input type="hidden" name="action" value="login"/>
Email: <input name="email"/><br/>
Password: <input type="password" name="password"/><br/>
<input type="submit" value="Login"/>
</form>
</body></html>
