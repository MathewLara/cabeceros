<%--
  Created by IntelliJ IDEA.
  User: Usuario
  Date: 10/11/2025
  Time: 8:21
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inicio de sesion</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

<div class="login-container">
    <form action ="/manejodecookies/login" method="post">
        <div>
            <label for ="user">Ingrese el usuario</label>
            <input type ="text" id ="user" name="user">
        </div>

        <div>
            <label for ="password">Ingrese el password</label>
            <input type ="password" id ="password" name="password">
        </div>
        <div>
            <input type="submit" value="Entrar">
        </div>
    </form>
</div>

</body>
</html>