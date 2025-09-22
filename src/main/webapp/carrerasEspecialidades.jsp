<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Carreras y Especialidades - ${generacion.nombre}</title>
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
            max-width: 1200px;
            margin: 0 auto;
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        .columns-container {
            display: flex;
            gap: 30px;
            margin-top: 30px;
        }
        .column {
            flex: 1;
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            border: 2px solid #ddd;
        }
        .column h2 {
            color: #4CAF50;
            margin-top: 0;
            margin-bottom: 20px;
            text-align: center;
        }
        select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            background-color: white;
        }
        .carrera-info, .especialidad-info {
            margin-top: 15px;
            padding: 15px;
            background-color: #e8f5e8;
            border-radius: 4px;
            border-left: 4px solid #4CAF50;
        }
        .info-label {
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
        }
        .info-content {
            color: #666;
            font-style: italic;
        }
        .no-selection {
            color: #999;
            text-align: center;
            padding: 20px;
            font-style: italic;
        }
        .back-button {
            background-color: #6c757d;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin-bottom: 20px;
        }
        .back-button:hover {
            background-color: #5a6268;
        }
        .loading {
            color: #007bff;
            text-align: center;
            padding: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <a href="generaciones" class="back-button">← Volver a Generaciones</a>

        <h1>Carreras y Especialidades</h1>
        <p style="text-align: center; color: #666; margin-bottom: 30px;">
            <strong>Generación:</strong> ${generacion.nombre}
            (<fmt:formatDate value="${generacion.fechaInicio}" pattern="dd/MM/yyyy"/> -
             <fmt:formatDate value="${generacion.fechaFin}" pattern="dd/MM/yyyy"/>)
        </p>

        <div class="columns-container">
            <!-- Columna 1: Carreras -->
            <div class="column">
                <h2>Carreras de la Generación</h2>

                <c:if test="${empty carreras}">
                    <div class="no-selection">
                        No hay carreras asignadas a esta generación.
                    </div>
                </c:if>

                <c:if test="${not empty carreras}">
                    <select id="carreraSelect" onchange="cargarEspecialidades()">
                        <option value="">-- Seleccione una carrera --</option>
                        <c:forEach var="carrera" items="${carreras}">
                            <option value="${carrera.id}" data-descripcion="${carrera.descripcion}">
                                ${carrera.nombre}
                            </option>
                        </c:forEach>
                    </select>

                    <div id="carreraInfo" style="display: none;" class="carrera-info">
                        <div class="info-label">Descripción:</div>
                        <div id="carreraDescripcion" class="info-content"></div>
                    </div>
                </c:if>
            </div>

            <!-- Columna 2: Especialidades -->
            <div class="column">
                <h2>Especialidades de la Carrera</h2>

                <div id="especialidadesContainer">
                    <div class="no-selection">
                        Seleccione una carrera para ver sus especialidades.
                    </div>
                </div>
            </div>
        </div>

        <!-- Sección para agregar nuevas carreras y especialidades -->
        <div style="margin-top: 40px; border-top: 2px solid #4CAF50; padding-top: 30px;">
            <h2 style="text-align: center; color: #4CAF50; margin-bottom: 30px;">Agregar Nuevas Asignaciones</h2>

            <!-- Mensajes de éxito/error -->
            <c:if test="${param.success == 'carrera'}">
                <div style="background-color: #d4edda; color: #155724; padding: 10px; border-radius: 4px; margin-bottom: 20px; text-align: center;">
                    ✓ Carrera agregada exitosamente a la generación
                </div>
            </c:if>
            <c:if test="${param.success == 'especialidad'}">
                <div style="background-color: #d4edda; color: #155724; padding: 10px; border-radius: 4px; margin-bottom: 20px; text-align: center;">
                    ✓ Especialidad agregada exitosamente a la carrera
                </div>
            </c:if>
            <c:if test="${param.error == 'carrera'}">
                <div style="background-color: #f8d7da; color: #721c24; padding: 10px; border-radius: 4px; margin-bottom: 20px; text-align: center;">
                    ✗ Error al agregar la carrera
                </div>
            </c:if>
            <c:if test="${param.error == 'especialidad'}">
                <div style="background-color: #f8d7da; color: #721c24; padding: 10px; border-radius: 4px; margin-bottom: 20px; text-align: center;">
                    ✗ Error al agregar la especialidad
                </div>
            </c:if>

            <div class="columns-container">
                <!-- Agregar nueva carrera a la generación -->
                <div class="column">
                    <h3 style="color: #007bff; text-align: center; margin-bottom: 20px;">Agregar Carrera a la Generación</h3>

                    <c:if test="${empty carrerasDisponibles}">
                        <div class="no-selection">
                            No hay más carreras disponibles para agregar a esta generación.
                        </div>
                    </c:if>

                    <c:if test="${not empty carrerasDisponibles}">
                        <form method="post" action="carrerasEspecialidades">
                            <input type="hidden" name="action" value="agregarCarrera">
                            <input type="hidden" name="generacionId" value="${generacion.id}">

                            <select name="carreraId" required style="margin-bottom: 15px;">
                                <option value="">-- Seleccione una carrera --</option>
                                <c:forEach var="carrera" items="${carrerasDisponibles}">
                                    <option value="${carrera.id}">${carrera.nombre}</option>
                                </c:forEach>
                            </select>

                            <button type="submit" style="width: 100%; background-color: #007bff; color: white; padding: 10px; border: none; border-radius: 4px; cursor: pointer; font-size: 14px;">
                                Agregar Carrera
                            </button>
                        </form>
                    </c:if>
                </div>

                <!-- Agregar nueva especialidad a una carrera -->
                <div class="column">
                    <h3 style="color: #28a745; text-align: center; margin-bottom: 20px;">Agregar Especialidad a una Carrera</h3>

                    <c:if test="${empty carreras}">
                        <div class="no-selection">
                            Debe haber al menos una carrera asignada para poder agregar especialidades.
                        </div>
                    </c:if>

                    <c:if test="${not empty carreras}">
                        <form method="post" action="carrerasEspecialidades" id="formEspecialidad">
                            <input type="hidden" name="action" value="agregarEspecialidad">
                            <input type="hidden" name="generacionId" value="${generacion.id}">

                            <select name="carreraId" id="carreraParaEspecialidad" onchange="cargarEspecialidadesDisponibles()" required style="margin-bottom: 15px;">
                                <option value="">-- Seleccione una carrera --</option>
                                <c:forEach var="carrera" items="${carreras}">
                                    <option value="${carrera.id}">${carrera.nombre}</option>
                                </c:forEach>
                            </select>

                            <div id="especialidadSelectContainer">
                                <select name="especialidadId" disabled style="margin-bottom: 15px;">
                                    <option value="">-- Primero seleccione una carrera --</option>
                                </select>
                            </div>

                            <button type="submit" id="btnAgregarEspecialidad" disabled style="width: 100%; background-color: #28a745; color: white; padding: 10px; border: none; border-radius: 4px; cursor: pointer; font-size: 14px;">
                                Agregar Especialidad
                            </button>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <script>
        function cargarEspecialidades() {
            const carreraSelect = document.getElementById('carreraSelect');
            const especialidadesContainer = document.getElementById('especialidadesContainer');
            const carreraInfo = document.getElementById('carreraInfo');
            const carreraDescripcion = document.getElementById('carreraDescripcion');

            const carreraId = carreraSelect.value;
            const selectedOption = carreraSelect.options[carreraSelect.selectedIndex];

            if (carreraId === '') {
                especialidadesContainer.innerHTML = '<div class="no-selection">Seleccione una carrera para ver sus especialidades.</div>';
                carreraInfo.style.display = 'none';
                return;
            }

            // Mostrar información de la carrera seleccionada
            const descripcion = selectedOption.getAttribute('data-descripcion') || 'Sin descripción disponible';
            carreraDescripcion.textContent = descripcion;
            carreraInfo.style.display = 'block';

            // Mostrar indicador de carga
            especialidadesContainer.innerHTML = '<div class="loading">Cargando especialidades...</div>';

            // Realizar petición AJAX para obtener las especialidades
            const xhr = new XMLHttpRequest();
            xhr.open('GET', 'carrerasEspecialidades?action=getEspecialidades&carreraId=' + carreraId, true);

            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        try {
                            const especialidades = JSON.parse(xhr.responseText);
                            mostrarEspecialidades(especialidades);
                        } catch (e) {
                            especialidadesContainer.innerHTML = '<div class="no-selection" style="color: red;">Error al cargar las especialidades.</div>';
                        }
                    } else {
                        especialidadesContainer.innerHTML = '<div class="no-selection" style="color: red;">Error al cargar las especialidades.</div>';
                    }
                }
            };

            xhr.send();
        }

        function mostrarEspecialidades(especialidades) {
            const container = document.getElementById('especialidadesContainer');

            if (especialidades.length === 0) {
                container.innerHTML = '<div class="no-selection">Esta carrera no tiene especialidades asignadas.</div>';
                return;
            }

            let html = '<select id="especialidadSelect" onchange="mostrarInfoEspecialidad()">';
            html += '<option value="">-- Seleccione una especialidad --</option>';

            especialidades.forEach(function(especialidad) {
                html += '<option value="' + especialidad.id + '" data-descripcion="' +
                        (especialidad.descripcion || '') + '">' + especialidad.nombre + '</option>';
            });

            html += '</select>';
            html += '<div id="especialidadInfo" style="display: none;" class="especialidad-info">';
            html += '<div class="info-label">Descripción:</div>';
            html += '<div id="especialidadDescripcion" class="info-content"></div>';
            html += '</div>';

            container.innerHTML = html;
        }

        function mostrarInfoEspecialidad() {
            const especialidadSelect = document.getElementById('especialidadSelect');
            const especialidadInfo = document.getElementById('especialidadInfo');
            const especialidadDescripcion = document.getElementById('especialidadDescripcion');

            const selectedOption = especialidadSelect.options[especialidadSelect.selectedIndex];

            if (especialidadSelect.value === '') {
                especialidadInfo.style.display = 'none';
                return;
            }

            const descripcion = selectedOption.getAttribute('data-descripcion') || 'Sin descripción disponible';
            especialidadDescripcion.textContent = descripcion;
            especialidadInfo.style.display = 'block';
        }

        function cargarEspecialidadesDisponibles() {
            const carreraSelect = document.getElementById('carreraParaEspecialidad');
            const especialidadContainer = document.getElementById('especialidadSelectContainer');
            const btnAgregar = document.getElementById('btnAgregarEspecialidad');

            const carreraId = carreraSelect.value;

            if (carreraId === '') {
                especialidadContainer.innerHTML = '<select name="especialidadId" disabled style="margin-bottom: 15px;"><option value="">-- Primero seleccione una carrera --</option></select>';
                btnAgregar.disabled = true;
                return;
            }

            // Mostrar indicador de carga
            especialidadContainer.innerHTML = '<select name="especialidadId" disabled style="margin-bottom: 15px;"><option value="">Cargando especialidades disponibles...</option></select>';

            // Realizar petición AJAX para obtener las especialidades disponibles
            const xhr = new XMLHttpRequest();
            xhr.open('POST', 'carrerasEspecialidades', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        try {
                            const especialidades = JSON.parse(xhr.responseText);
                            mostrarEspecialidadesDisponibles(especialidades);
                        } catch (e) {
                            especialidadContainer.innerHTML = '<select name="especialidadId" disabled style="margin-bottom: 15px;"><option value="">Error al cargar especialidades</option></select>';
                            btnAgregar.disabled = true;
                        }
                    } else {
                        especialidadContainer.innerHTML = '<select name="especialidadId" disabled style="margin-bottom: 15px;"><option value="">Error al cargar especialidades</option></select>';
                        btnAgregar.disabled = true;
                    }
                }
            };

            xhr.send('action=getEspecialidadesDisponibles&carreraId=' + carreraId);
        }

        function mostrarEspecialidadesDisponibles(especialidades) {
            const container = document.getElementById('especialidadSelectContainer');
            const btnAgregar = document.getElementById('btnAgregarEspecialidad');

            if (especialidades.length === 0) {
                container.innerHTML = '<select name="especialidadId" disabled style="margin-bottom: 15px;"><option value="">No hay especialidades disponibles</option></select>';
                btnAgregar.disabled = true;
                return;
            }

            let html = '<select name="especialidadId" required style="margin-bottom: 15px;">';
            html += '<option value="">-- Seleccione una especialidad --</option>';

            especialidades.forEach(function(especialidad) {
                html += '<option value="' + especialidad.id + '">' + especialidad.nombre + '</option>';
            });

            html += '</select>';
            container.innerHTML = html;
            btnAgregar.disabled = false;
        }
    </script>
</body>
</html>
