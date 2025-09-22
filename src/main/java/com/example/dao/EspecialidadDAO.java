package com.example.dao;

import com.example.model.Especialidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadDAO {

    // Obtener las especialidades de una carrera específica
    public List<Especialidad> getEspecialidadesByCarrera(int carreraId) throws SQLException {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT e.id, e.nombre, e.descripcion, e.activa " +
                    "FROM especialidades e " +
                    "INNER JOIN carrera_especialidad ce ON e.id = ce.especialidad_id " +
                    "WHERE ce.carrera_id = ? AND e.activa = true " +
                    "ORDER BY e.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carreraId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Especialidad especialidad = new Especialidad();
                    especialidad.setId(rs.getInt("id"));
                    especialidad.setNombre(rs.getString("nombre"));
                    especialidad.setDescripcion(rs.getString("descripcion"));
                    especialidad.setActiva(rs.getBoolean("activa"));
                    especialidades.add(especialidad);
                }
            }
        }

        return especialidades;
    }

    // Obtener todas las especialidades
    public List<Especialidad> getAllEspecialidades() throws SQLException {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, activa FROM especialidades WHERE activa = true ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Especialidad especialidad = new Especialidad();
                especialidad.setId(rs.getInt("id"));
                especialidad.setNombre(rs.getString("nombre"));
                especialidad.setDescripcion(rs.getString("descripcion"));
                especialidad.setActiva(rs.getBoolean("activa"));
                especialidades.add(especialidad);
            }
        }

        return especialidades;
    }

    // Obtener especialidades que NO están asignadas a una carrera específica
    public List<Especialidad> getEspecialidadesNoAsignadasACarrera(int carreraId) throws SQLException {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT e.id, e.nombre, e.descripcion, e.activa " +
                    "FROM especialidades e " +
                    "WHERE e.activa = true " +
                    "AND e.id NOT IN (SELECT especialidad_id FROM carrera_especialidad WHERE carrera_id = ?) " +
                    "ORDER BY e.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carreraId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Especialidad especialidad = new Especialidad();
                    especialidad.setId(rs.getInt("id"));
                    especialidad.setNombre(rs.getString("nombre"));
                    especialidad.setDescripcion(rs.getString("descripcion"));
                    especialidad.setActiva(rs.getBoolean("activa"));
                    especialidades.add(especialidad);
                }
            }
        }

        return especialidades;
    }

    // Asignar una especialidad a una carrera
    public boolean asignarEspecialidadACarrera(int carreraId, int especialidadId) throws SQLException {
        String sql = "INSERT INTO carrera_especialidad (carrera_id, especialidad_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carreraId);
            stmt.setInt(2, especialidadId);

            return stmt.executeUpdate() > 0;
        }
    }
}
