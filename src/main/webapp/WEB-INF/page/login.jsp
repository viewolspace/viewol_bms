<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title></title>
</head>
<body>

<h1>user login</h1>

<form action="login/submitLogin.do" method="post">
    username:<input type="text" name="username">

    <p>
        password:<input type="password" name="password">

    <p>
        rememberMe:<input type="checkbox" checked="checked" name="rememberMe" id="rememberMe"
                          style="width: 10px; height: 10px;">

    <p>
        <input type="submit" value="submit">
</form>
</body>
</html>
