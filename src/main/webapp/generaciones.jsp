<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sistema de Generaciones</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #4CAF50;
            color: white;
            font-weight: bold;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        tr:hover {
            background-color: #e8f5e8;
        }
        .btn-edit {
            background-color: #008CBA;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .btn-edit:hover {
            background-color: #007B9A;
        }
        .no-data {
            text-align: center;
            color: #666;
            font-style: italic;
            padding: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Sistema de Generaciones</h1>

        <c:if test="${empty generaciones}">
            <div class="no-data">
                No hay generaciones registradas en la base de datos.
            </div>
        </c:if>

        <c:if test="${not empty generaciones}">
            <table>
                <thead>
                    <tr>
                        <th>Número de Generación</th>
                        <th>Fecha Inicio</th>
                        <th>Fecha Fin</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="generacion" items="${generaciones}" varStatus="status">
                        <tr>
                            <td>${status.count}</td>
                            <td>
                                <fmt:formatDate value="${generacion.fechaInicio}" pattern="dd/MM/yyyy"/>
                            </td>
                            <td>
                                <fmt:formatDate value="${generacion.fechaFin}" pattern="dd/MM/yyyy"/>
                            </td>
                            <td>
                                <a href="carrerasEspecialidades?generacionId=${generacion.id}" class="btn-edit">
                                    Editar
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</body>
</html>
