<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<link rel="stylesheet" href="style_search_success.css" />
		<title>Result of your search</title>

		<!-- Javascript -->
		<script>
		function change_border() {
    		var x = document.getElementsByClassName('wrapper-title-line');
    		x[0].style.borderBottomStyle = "solid";
    		x[0].style.borderBottomColor = "transparent";
    		x[0].style.borderBottomWidth = "1px";
		} 
		function normal_border(){
			var x = document.getElementsByClassName('wrapper-title-line');
    		x[0].style.borderBottomColor = "#565A5C";
    		x[0].style.borderBottomStyle = "solid";
    		x[0].style.borderBottomWidth = "1px";
		} 
		</script>
	</head>
<body>
	<div class=container>
		<div class = "search_header">
			<div class="new-search">
				<form action="InitSearch" method="post">
					<input id="search-form" type="text" name="Search"></input>
					<input id="search-button" type="submit" value="New Search"></input>
				</form>
			</div>
		</div>	

		<div class="container-content">
			<div class="wrapper-title">
				<div class="wrapper-title-line"> 
					<div class="content-title">
						Search results // <c:out value="${search}" />
					</div>
				</div>
			</div>
			<div class="container-list">
				<div class="jobList">
					<ul>
						<c:forEach items="${JobsList}" var="job" varStatus="loopCounter" >
							<c:choose>
								<c:when test="${loopCounter.isFirst()}">
								    <li onmouseover="change_border()" onmouseout="normal_border()">
								</c:when>
								<c:otherwise>							
									<li>
								</c:otherwise>								
							</c:choose>
					      			<h3> <a href=<c:out value="${job.link}"/> ><c:out value="${job.title}"/></a> </h3>
						      			<p>Description: 
						      				<c:choose>
							      				<c:when test="${empty job.description}">
							      					Not available
							      				</c:when>
							      				<c:otherwise>
							      					<c:out value="${job.description}"/></p>
							      				</c:otherwise>
							      			</c:choose>						      				
						      			</p>
						      			<p>Date: 
						      				<c:choose>
							      				<c:when test="${empty job.date}">
							      					Not available
							      				</c:when>
							      				<c:otherwise>
							      					<fmt:formatDate type="date" value="${job.date}"/>
							      				</c:otherwise>
							      			</c:choose>
						      			</p>
						      			<p>Found in: <c:out value="${job.source}"/></p>
								    </li>
						</c:forEach>	    
					</ul>
				</div>
			</div>
		</div>	
		<div class="container-footer">
			<%--For displaying Previous link except for the 1st page --%>
		    <c:if test="${currentPage != 1 && maxPageRange >= 1}">
		        <td><a href="InitSearch?page=${currentPage - 1}">Previous</a></td>
		    </c:if>
		 
		    <%--For displaying Page numbers. 
		    The when condition does not display a link for the current page--%>
		    <table border="1" cellpadding="5" cellspacing="5">
		        <tr>
		            <c:forEach begin="${minPageRange}" end="${maxPageRange}" var="i">
		                <c:choose>
		                    <c:when test="${currentPage eq i}">
		                        <td>${i}</td>
		                    </c:when>
		                    <c:otherwise>
		                        <td><a href="InitSearch?page=${i}">${i}</a></td>
		                    </c:otherwise>
		                </c:choose>
		            </c:forEach>
		        </tr>
		    </table>
		     
		    <%--For displaying Next link --%>
		    <c:if test="${currentPage lt maxPageRange}">
		        <td><a href="employee.do?page=${currentPage + 1}">Next</a></td>
		    </c:if>
		</div>
	</div>
</body>
</html>