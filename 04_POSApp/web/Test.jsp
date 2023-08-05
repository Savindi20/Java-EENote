<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: Savindi
  Date: 8/5/2023
  Time: 10:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%--Scriplet - (java multiple lines liyann)--%>
<%
    System.out.println("Hello There");
    String name = "IJSE";
    int age = 21;
    ArrayList colors = new ArrayList();
%>

<h1>Hello There</h1>

<%--expresion - (variable ekaka value ekk print karagnn)--%>
<h1><%=name%></h1>

<%--declaration - (eka line ekakin variable ekk declare krl thiyagnn)--%>
<%!String address = "Matara";%>
</body>
</html>
