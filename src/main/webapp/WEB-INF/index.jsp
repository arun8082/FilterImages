<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function getfolder(e) {
		var files = e.target.files;
		var path = files[0].webkitRelativePath;
		alert(path);
		var Folder = path.split("/");
		document.getElementById("baseFolderPath").value = path;
	}
</script>
</head>
<body>
	<span><ul><c:forEach items="${success}" var="list"><li><a href="results/${list}" download>results/${list}</a></li></c:forEach></ul></span>
	<span><label style="color: red">${error}</label></span>
	<form action="details" method="post">
		<span><label>User Name:</label><input type="text"
			name="userName" /></span> <span><label>Base Folder Path:</label><input
			type="text" name="baseFolderPath" id="baseFolderPath" style="width: 50%"/></span> <span><input
			type="submit" value="View Photos" /></span>
	</form>
</body>
</html>