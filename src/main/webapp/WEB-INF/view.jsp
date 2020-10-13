<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<span>
		<b>UserName:</b><label>${userName}</label><br/>
		<b>BaseFolderPath:</b><label>${baseFolderPath}</label>
	</span>

	<form method="post" action="submit">
		<table border="1px">

			<c:forEach items="${list}" var="map" varStatus="loop">
				<tr>
					<td>${loop.count}</td>
					<td>${map.key}</td>
					<c:forEach items="${map.value}" var="path">
						<td><img src="images/${path}" height="200px" /></td>
					</c:forEach>
					<td><input type="checkbox" name="suspense" value="${map.key}"/><label>Suspense</label><br />
						<input type="checkbox" name="different_images" value="${map.key}"/><label>Different Image</label><br />
						<input type="checkbox" name="detection_failed" value="${map.key}"/><label>Detection Failed</label></td>
				</tr>
			</c:forEach>
		</table>
		<input type="submit" value="Submit">
	</form>

</body>
</html>