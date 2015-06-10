<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add new person</title>
</head>
<body>
<form:form commandName="person">
    <table>
        <tr>
            <td><form:label path="firstName">First name:</form:label></td>
            <td><form:input path="firstName"/></td>
            <td style="color:red;"><form:errors path="firstName"/></td>
        </tr>
        <tr>
            <td><form:label path="lastName">Last name:</form:label></td>
            <td><form:input path="lastName"/></td>
            <td style="color:red;"><form:errors path="lastName"/></td>
        </tr>
        <tr>
            <td colspan="3">
                <input type="submit"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>