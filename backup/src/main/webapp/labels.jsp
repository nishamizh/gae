<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.data.VisionParameters"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>


<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    <title>Mock</title>
    <style>
        h4 {text-align: center;}
        p {text-align: center;}
        div {text-align: center;}
    </style>
</head>


List of the top "${numberofentities}" images matching the criteria checked above
<br>
<%-- <img src="<%=request.getAttribute("imageSource")%>" alt="Girl in a jacket" width="100" height="120">
<img src="happy2.jpeg" alt="Girl in a jacket" width="100" height="120">
<img src="happy3.jpg" alt="Girl in a jacket" width="100" height="120"> --%>
<%
			List<VisionParameters> imageLabels = (List<VisionParameters>) request.getAttribute("list");
		%>
				<table class="table table-dark">
				<thead>
    
					<tr>
						<th scope="col">Label</th>
						<th scope="col">Score</th>

					</tr>
</thead>
 <tbody>
					<c:forEach items="${list}" var="label">
						<tr>
						
							<td><img src="<c:url value="${label.imageSource}"/>" width="100" height="120"/></td>
							
						</tr>
					</c:forEach>
</tbody>
				</table>



<br><br>

To specifically request for images within the time frame choose the from and to timeframe.

<button type="button">From</button>
<button type="button">To</button>
<br>
<a href='index.html'>Home Page</a>
</html>