package com.example.dao;

import com.example.model.Carrera;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarreraDAO {

    // Obtener las carreras de una generación específica
    public List<Carrera> getCarrerasByGeneracion(int generacionId) throws SQLException {
        List<Carrera> carreras = new ArrayList<>();
        String sql = "SELECT c.id, c.nombre, c.descripcion, c.activa " +
                    "FROM carreras c " +
                    "INNER JOIN generacion_carrera gc ON c.id = gc.carrera_id " +
                    "WHERE gc.generacion_id = ? AND c.activa = true " +
                    "ORDER BY c.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, generacionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Carrera carrera = new Carrera();
                    carrera.setId(rs.getInt("id"));
                    carrera.setNombre(rs.getString("nombre"));
                    carrera.setDescripcion(rs.getString("descripcion"));
                    carrera.setActiva(rs.getBoolean("activa"));
                    carreras.add(carrera);
                }
            }
        }

        return carreras;
    }

    // Obtener todas las carreras
    public List<Carrera> getAllCarreras() throws SQLException {
        List<Carrera> carreras = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, activa FROM carreras WHERE activa = true ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Carrera carrera = new Carrera();
                carrera.setId(rs.getInt("id"));
                carrera.setNombre(rs.getString("nombre"));
                carrera.setDescripcion(rs.getString("descripcion"));
                carrera.setActiva(rs.getBoolean("activa"));
                carreras.add(carrera);
            }
        }

        return carreras;
    }

    // Obtener carreras que NO están asignadas a una generación específica
    public List<Carrera> getCarrerasNoAsignadasAGeneracion(int generacionId) throws SQLException {
        List<Carrera> carreras = new ArrayList<>();
        String sql = "SELECT c.id, c.nombre, c.descripcion, c.activa " +
                    "FROM carreras c " +
                    "WHERE c.activa = true " +
                    "AND c.id NOT IN (SELECT carrera_id FROM generacion_carrera WHERE generacion_id = ?) " +
                    "ORDER BY c.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, generacionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Carrera carrera = new Carrera();
                    carrera.setId(rs.getInt("id"));
                    carrera.setNombre(rs.getString("nombre"));
                    carrera.setDescripcion(rs.getString("descripcion"));
                    carrera.setActiva(rs.getBoolean("activa"));
                    carreras.add(carrera);
                }
            }
        }

        return carreras;
    }

    // Asignar una carrera a una generación
    public boolean asignarCarreraAGeneracion(int generacionId, int carreraId) throws SQLException {
        String sql = "INSERT INTO generacion_carrera (generacion_id, carrera_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, generacionId);
            stmt.setInt(2, carreraId);

            return stmt.executeUpdate() > 0;
        }
    }
}
