package com.example.servlet;

import com.example.dao.CarreraDAO;
import com.example.dao.EspecialidadDAO;
import com.example.dao.GeneracionDAO;
import com.example.model.Carrera;
import com.example.model.Especialidad;
import com.example.model.Generacion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/carrerasEspecialidades")
public class CarreraEspecialidadServlet extends HttpServlet {
    private CarreraDAO carreraDAO;
    private EspecialidadDAO especialidadDAO;
    private GeneracionDAO generacionDAO;

    @Override
    public void init() throws ServletException {
        carreraDAO = new CarreraDAO();
        especialidadDAO = new EspecialidadDAO();
        generacionDAO = new GeneracionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("getEspecialidades".equals(action)) {
                getEspecialidadesByCarrera(request, response);
            } else {
                showCarrerasEspecialidades(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Error de base de datos", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("agregarCarrera".equals(action)) {
                agregarCarreraAGeneracion(request, response);
            } else if ("agregarEspecialidad".equals(action)) {
                agregarEspecialidadACarrera(request, response);
            } else if ("getCarrerasDisponibles".equals(action)) {
                getCarrerasDisponibles(request, response);
            } else if ("getEspecialidadesDisponibles".equals(action)) {
                getEspecialidadesDisponibles(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Error de base de datos", e);
        }
    }

    private void showCarrerasEspecialidades(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String generacionIdStr = request.getParameter("generacionId");
        if (generacionIdStr == null || generacionIdStr.isEmpty()) {
            response.sendRedirect("generaciones");
            return;
        }

        int generacionId = Integer.parseInt(generacionIdStr);

        // Obtener información de la generación
        Generacion generacion = generacionDAO.getGeneracionById(generacionId);

        // Obtener carreras de la generación
        List<Carrera> carreras = carreraDAO.getCarrerasByGeneracion(generacionId);

        // Obtener carreras disponibles para agregar (no asignadas)
        List<Carrera> carrerasDisponibles = carreraDAO.getCarrerasNoAsignadasAGeneracion(generacionId);

        request.setAttribute("generacion", generacion);
        request.setAttribute("carreras", carreras);
        request.setAttribute("carrerasDisponibles", carrerasDisponibles);
        request.getRequestDispatcher("/carrerasEspecialidades.jsp").forward(request, response);
    }

    private void getEspecialidadesByCarrera(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String carreraIdStr = request.getParameter("carreraId");
        if (carreraIdStr == null || carreraIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int carreraId = Integer.parseInt(carreraIdStr);
        List<Especialidad> especialidades = especialidadDAO.getEspecialidadesByCarrera(carreraId);

        // Devolver las especialidades en formato JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < especialidades.size(); i++) {
            Especialidad esp = especialidades.get(i);
            json.append("{");
            json.append("\"id\":").append(esp.getId()).append(",");
            json.append("\"nombre\":\"").append(esp.getNombre().replace("\"", "\\\"")).append("\",");
            json.append("\"descripcion\":\"").append(esp.getDescripcion() != null ? esp.getDescripcion().replace("\"", "\\\"") : "").append("\"");
            json.append("}");

            if (i < especialidades.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        response.getWriter().write(json.toString());
    }

    private void agregarCarreraAGeneracion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        String generacionIdStr = request.getParameter("generacionId");
        String carreraIdStr = request.getParameter("carreraId");

        if (generacionIdStr != null && carreraIdStr != null &&
            !generacionIdStr.isEmpty() && !carreraIdStr.isEmpty()) {

            int generacionId = Integer.parseInt(generacionIdStr);
            int carreraId = Integer.parseInt(carreraIdStr);

            boolean success = carreraDAO.asignarCarreraAGeneracion(generacionId, carreraId);

            if (success) {
                response.sendRedirect("carrerasEspecialidades?generacionId=" + generacionId + "&success=carrera");
            } else {
                response.sendRedirect("carrerasEspecialidades?generacionId=" + generacionId + "&error=carrera");
            }
        } else {
            response.sendRedirect("generaciones");
        }
    }

    private void agregarEspecialidadACarrera(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        String generacionIdStr = request.getParameter("generacionId");
        String carreraIdStr = request.getParameter("carreraId");
        String especialidadIdStr = request.getParameter("especialidadId");

        if (carreraIdStr != null && especialidadIdStr != null &&
            !carreraIdStr.isEmpty() && !especialidadIdStr.isEmpty()) {

            int carreraId = Integer.parseInt(carreraIdStr);
            int especialidadId = Integer.parseInt(especialidadIdStr);

            boolean success = especialidadDAO.asignarEspecialidadACarrera(carreraId, especialidadId);

            if (success && generacionIdStr != null && !generacionIdStr.isEmpty()) {
                response.sendRedirect("carrerasEspecialidades?generacionId=" + generacionIdStr + "&success=especialidad");
            } else if (generacionIdStr != null && !generacionIdStr.isEmpty()) {
                response.sendRedirect("carrerasEspecialidades?generacionId=" + generacionIdStr + "&error=especialidad");
            } else {
                response.sendRedirect("generaciones");
            }
        } else {
            response.sendRedirect("generaciones");
        }
    }

    private void getEspecialidadesDisponibles(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String carreraIdStr = request.getParameter("carreraId");
        if (carreraIdStr == null || carreraIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int carreraId = Integer.parseInt(carreraIdStr);
        List<Especialidad> especialidades = especialidadDAO.getEspecialidadesNoAsignadasACarrera(carreraId);

        // Devolver las especialidades disponibles en formato JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < especialidades.size(); i++) {
            Especialidad esp = especialidades.get(i);
            json.append("{");
            json.append("\"id\":").append(esp.getId()).append(",");
            json.append("\"nombre\":\"").append(esp.getNombre().replace("\"", "\\\"")).append("\"");
            json.append("}");

            if (i < especialidades.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        response.getWriter().write(json.toString());
    }

    private void getCarrerasDisponibles(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String generacionIdStr = request.getParameter("generacionId");
        if (generacionIdStr == null || generacionIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int generacionId = Integer.parseInt(generacionIdStr);
        List<Carrera> carreras = carreraDAO.getCarrerasNoAsignadasAGeneracion(generacionId);

        // Devolver las carreras disponibles en formato JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < carreras.size(); i++) {
            Carrera carrera = carreras.get(i);
            json.append("{");
            json.append("\"id\":").append(carrera.getId()).append(",");
            json.append("\"nombre\":\"").append(carrera.getNombre().replace("\"", "\\\"")).append("\"");
            json.append("}");

            if (i < carreras.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        response.getWriter().write(json.toString());
    }
}
