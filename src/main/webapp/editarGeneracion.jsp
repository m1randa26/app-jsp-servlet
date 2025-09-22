<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Generación</title>
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
            max-width: 600px;
            margin: 0 auto;
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        input[type="text"], input[type="date"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type="checkbox"] {
            margin-right: 8px;
        }
        .button-group {
            text-align: center;
            margin-top: 20px;
        }
        .btn {
            padding: 10px 20px;
            margin: 0 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .btn-primary {
            background-color: #4CAF50;
            color: white;
        }
        .btn-primary:hover {
            background-color: #45a049;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Editar Generación</h1>

        <form action="generaciones" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="${generacion.id}">

            <div class="form-group">
                <label for="nombre">Nombre:</label>
                <input type="text" id="nombre" name="nombre" value="${generacion.nombre}" required>
            </div>

            <div class="form-group">
                <label for="fechaInicio">Fecha de Inicio:</label>
                <input type="date" id="fechaInicio" name="fechaInicio"
                       value="<fmt:formatDate value='${generacion.fechaInicio}' pattern='yyyy-MM-dd'/>" required>
            </div>

            <div class="form-group">
                <label for="fechaFin">Fecha de Fin:</label>
                <input type="date" id="fechaFin" name="fechaFin"
                       value="<fmt:formatDate value='${generacion.fechaFin}' pattern='yyyy-MM-dd'/>" required>
            </div>

            <div class="form-group">
                <label>
                    <input type="checkbox" name="activa" ${generacion.activa ? 'checked' : ''}>
                    Generación Activa
                </label>
            </div>

            <div class="button-group">
                <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                <a href="generaciones" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>

