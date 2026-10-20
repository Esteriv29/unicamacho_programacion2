package tienda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tienda.conexion.conectar;
import tienda.modelo.Trabajador;

public class TrabajadorDAO {

    // Método para validar el ingreso
    public Trabajador login(String cedula, String contrasena) {
        Connection con = conectar.getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Trabajador trabajador = null;

        // Consulta SQL para buscar coincidencia
        String sql = "SELECT id_trabajador, nombre, rol FROM trabajadores WHERE cedula = ? AND contrasena = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, cedula);
            ps.setString(2, contrasena);
            rs = ps.executeQuery();

            // Si encuentra un registro, las credenciales son correctas
            if (rs.next()) {
                trabajador = new Trabajador();
                trabajador.setIdTrabajador(rs.getInt("id_trabajador"));
                trabajador.setNombre(rs.getString("nombre"));
                trabajador.setRol(rs.getString("rol"));
            }
        } catch (SQLException e) {
            System.out.println("Error en el método login de TrabajadorDAO");
            e.printStackTrace();
        } finally {
            // Cerrar conexiones abiertas de forma segura
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return trabajador; // Retorna el trabajador si es correcto, o null si falló
    }
}

