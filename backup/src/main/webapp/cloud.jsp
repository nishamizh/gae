
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>File Upload Example</title>
</head>
<body>
<form action="/upload" method="POST" enctype="multipart/form-data">
    <p>What's your name?</p>
    <input type="text" name="name" value="Joey">
    <p>What file do you want to upload?</p>
    <input type="file" name="fileToUpload">
    <br/><br/>
    <input type="submit" value="Submit">
</form>
</body>
</html>
