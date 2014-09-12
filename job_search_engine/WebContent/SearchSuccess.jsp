<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="list_modifier.css">
<title>Result of the search</title>
</head>
<body>
<div class="content-title">
	Search results // <c:out value="${search}" />
</div>
<div class="new-search">
	<form action="InitSearch" method="post">
		New Search: <input type="text" name="Search">
	<input type="submit" value="Search">
</div>
<div class="jobList">
	<ul>
		<c:forEach items="${BerlinStartupJobs}" var="job">
		    <li>
      			<h3> <a href=<c:out value="${job.link}"/> style="color: #000000" > <c:out value="${job.title}"/> </a> </h3>
      			<p><c:out value="${job.description}"/></p>
		    </li>	    
		</c:forEach>
	</ul>
</div>
</body>
</html>