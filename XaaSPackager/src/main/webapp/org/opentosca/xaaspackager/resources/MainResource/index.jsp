<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>XaaS Packager</title>
</head>
<body>
	<h1>XaaS Packager</h1>

	<h4>Configuration:</h4>
	<p>
	<form action="./configuration" method="post"
		enctype="multipart/form-data">
		Winery Address: <input type="text" value="${it.wineryAddress}"
			name="wineryAddress" /> <input type="submit" value="Update" /><br />
	</form>
	</p>
	<p>
	<form action="./configuration" method="post"
		enctype="multipart/form-data">
		Container Address: <input type="text" value="${it.containerAddress}"
			name="containerAddress" /> <input type="submit" value="Update" /><br />
	</form>
	</p>

	<h4>Deployment Artifact Upload:</h4>
	<form action="./package" method="post" enctype="multipart/form-data"
		id="daUploadForm">

		<p>
			<select name="artifactType" form="daUploadForm">
				<c:forEach var="artifactType" items="${it.artifactTypes}">
					<option value="${artifactType}" >${artifactType}</option>
				</c:forEach>
			</select> 
		</p>
		<p> 
			Select a file : <input type="file" name="file" size="45" />
			<input type="submit" value="Upload" />
		</p>

	</form>
</body>
</html>
