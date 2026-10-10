<!doctype html><html><head><title>Create Quiz</title></head><body>
<h2>Create New Quiz</h2>
<form method="post" action="/creator/">
<input type="hidden" name="action" value="createQuiz"/>
Title: <input name="title"/><br/>
Description:<br/><textarea name="description"></textarea><br/>
Duration (minutes): <input name="duration" value="20"/><br/>
<input type="submit" value="Create and Add Questions"/>
</form>
</body></html>
