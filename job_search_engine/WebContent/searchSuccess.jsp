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
				<form action="InitSearch" method="get">
					<input id="search-form" type="text" name="Search"></input>
					<input id="search-button" type="submit" value="New Search"></input>
				</form>
			</div>
			<div class = "menu_options">
				<p><a href="mainPage.html">Home</a></p>
				<p><a href="about.html">About</a></p>
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
							<a href=<c:out value="${job.link}"/> >
							<c:choose>
								<c:when test="${loopCounter.isFirst()}">
								    <li onmouseover="change_border()" onmouseout="normal_border()">
								</c:when>
								<c:otherwise>							
									<li>
								</c:otherwise>								
							</c:choose>
									<table>
									<tr>
										<td>
											<img src="<c:out value="${job.thumbnail}"/>">
										</td>
										<td>
						      				<h3> <c:out value="${job.title}"/> </h3>
						      				<p>Company: <c:out value="${job.company}"/></p>
							      			<p>Description: 
							      				<c:choose>
								      				<c:when test="${empty job.description}">
								      					Not available
								      				</c:when>
								      				<c:otherwise>
								      					<c:out value="${job.description}"/>
								      				</c:otherwise>
								      			</c:choose>	
											</p>					      				
							      			<table style="width:100%">
							      				<tr>
													<td>
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
									      			</td>
									      			<td><p>Found in: <c:out value="${job.source}"/></p></td>
									      		</tr>
								      		</table> 
									    </td>
								    	</tr>
								  	</table>	  
								</li>
							</a>
						</c:forEach>	    
					</ul>
				</div>
			</div>
			<div class="paginator">
				<%--For displaying Previous link except for the 1st page --%>
				<ul>
			    <c:if test="${currentPage != 1 && maxPageRange >= 1}">
			        <li class="extrem"><a href="InitSearch?page=${currentPage - 1}">&#10094;&#10094;Previous</a></li>
			    </c:if>
			 
			    <%--For displaying Page numbers. 
			    The when condition does not display a link for the current page--%>
	            <c:forEach begin="${minPageRange}" end="${maxPageRange}" var="i">
	                <c:choose>
	                    <c:when test="${currentPage eq i}">
	                        <li>${i}</li>
	                    </c:when>
	                    <c:otherwise>
	                        <li><a href="InitSearch?page=${i}">${i}</a></li>
	                    </c:otherwise>
	                </c:choose>
	            </c:forEach>
			    <%--For displaying Next link --%>
			    <c:if test="${currentPage lt maxPageRange}">
			        <li class="extrem"><a href="InitSearch?page=${currentPage + 1}">Next&#10095;&#10095;</a></li>
			    </c:if>
			    </ul>
			</div>
		</div>
	</div>
</body>
</html>