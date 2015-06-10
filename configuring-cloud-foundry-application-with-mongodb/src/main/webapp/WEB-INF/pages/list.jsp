<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>List of people</title>
</head>
<body>
<h1>List of people</h1>
<table border="1">
    <c:forEach items="${personList}" var="person">
        <tr>
            <td><c:out value="${person.firstName}"/></td>
            <td><c:out value="${person.lastName}"/></td>
            <c:url value="/delete" var="deleteUrl">
                <c:param name="id" value="${person.id}"/>
            </c:url>
            <td><a href="${deleteUrl}">delete</a></td>
        </tr>
    </c:forEach>
</table>
<a href="<c:url value="/add"/>">Add new person</a>

</body>
</html>