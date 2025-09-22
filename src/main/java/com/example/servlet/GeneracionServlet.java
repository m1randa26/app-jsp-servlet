package com.example.servlet;

import com.example.dao.GeneracionDAO;
import com.example.model.Generacion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/generaciones")
public class GeneracionServlet extends HttpServlet {
    private GeneracionDAO generacionDAO;

    @Override
    public void init() throws ServletException {
        generacionDAO = new GeneracionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("edit".equals(action)) {
                showEditForm(request, response);
            } else {
                showGeneracionList(request, response);
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
            if ("update".equals(action)) {
                updateGeneracion(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Error de base de datos", e);
        }
    }

    private void showGeneracionList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Generacion> generaciones = generacionDAO.getAllGeneraciones();
        request.setAttribute("generaciones", generaciones);
        request.getRequestDispatcher("/generaciones.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Generacion generacion = generacionDAO.getGeneracionById(id);

        if (generacion != null) {
            request.setAttribute("generacion", generacion);
            request.getRequestDispatcher("/editarGeneracion.jsp").forward(request, response);
        } else {
            response.sendRedirect("generaciones");
        }
    }

    private void updateGeneracion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        boolean activa = request.getParameter("activa") != null;

        Generacion generacion = new Generacion();
        generacion.setId(id);
        generacion.setNombre(nombre);
        generacion.setFechaInicio(java.sql.Date.valueOf(fechaInicio));
        generacion.setFechaFin(java.sql.Date.valueOf(fechaFin));
        generacion.setActiva(activa);

        generacionDAO.updateGeneracion(generacion);
        response.sendRedirect("generaciones");
    }
}
