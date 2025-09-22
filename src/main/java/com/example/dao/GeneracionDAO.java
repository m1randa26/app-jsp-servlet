package com.example.dao;

import com.example.model.Generacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GeneracionDAO {

    public List<Generacion> getAllGeneraciones() throws SQLException {
        List<Generacion> generaciones = new ArrayList<>();
        String sql = "SELECT id, nombre, fecha_inicio, fecha_fin, activa FROM generaciones ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Generacion generacion = new Generacion();
                generacion.setId(rs.getInt("id"));
                generacion.setNombre(rs.getString("nombre"));
                generacion.setFechaInicio(rs.getDate("fecha_inicio"));
                generacion.setFechaFin(rs.getDate("fecha_fin"));
                generacion.setActiva(rs.getBoolean("activa"));
                generaciones.add(generacion);
            }
        }

        return generaciones;
    }

    public Generacion getGeneracionById(int id) throws SQLException {
        String sql = "SELECT id, nombre, fecha_inicio, fecha_fin, activa FROM generaciones WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Generacion generacion = new Generacion();
                    generacion.setId(rs.getInt("id"));
                    generacion.setNombre(rs.getString("nombre"));
                    generacion.setFechaInicio(rs.getDate("fecha_inicio"));
                    generacion.setFechaFin(rs.getDate("fecha_fin"));
                    generacion.setActiva(rs.getBoolean("activa"));
                    return generacion;
                }
            }
        }

        return null;
    }

    public boolean updateGeneracion(Generacion generacion) throws SQLException {
        String sql = "UPDATE generaciones SET nombre = ?, fecha_inicio = ?, fecha_fin = ?, activa = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, generacion.getNombre());
            stmt.setDate(2, generacion.getFechaInicio());
            stmt.setDate(3, generacion.getFechaFin());
            stmt.setBoolean(4, generacion.isActiva());
            stmt.setInt(5, generacion.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}
