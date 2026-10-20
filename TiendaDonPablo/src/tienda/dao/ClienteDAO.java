package tienda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tienda.conexion.conectar;
import tienda.modelo.Cliente;

public class ClienteDAO {

    // Método para listar todos los clientes o buscarlos por nombre/apellido
    public List<Cliente> buscarClientes(String textoBusqueda) {
        List<Cliente> lista = new ArrayList<>();
        Connection con = conectar.getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;

        // SQL que busca coincidencia por nombre o apellido
        String sql = "SELECT * FROM clientes WHERE nombre LIKE ? OR apellido LIKE ?";

        try {
            ps = con.prepareStatement(sql);
            // El '%' sirve para que busque cualquier texto que contenga la palabra
            ps.setString(1, "%" + textoBusqueda + "%");
            ps.setString(2, "%" + textoBusqueda + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setTelefono(rs.getString("telefono"));
                c.setDireccion(rs.getString("direccion"));
                c.setLimiteCredito(rs.getDouble("limite_credito"));
                c.setDeuda(rs.getDouble("deuda")); // <--- ¡LÍNEA AGREGADA COMPLETADA AQUÍ!
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar clientes.");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    // Método para registrar un nuevo cliente en la base de datos
    public boolean registrarCliente(Cliente c) {
        Connection con = conectar.getConexion();
        PreparedStatement ps = null;
        String sql = "INSERT INTO clientes (nombre, apellido, telefono, direccion, limite_credito, deuda) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getDireccion());
            ps.setDouble(5, c.getLimiteCredito());
            ps.setDouble(6, c.getDeuda()); // <--- Asegura que los nuevos clientes inicien con su deuda asignada

            int resultado = ps.executeUpdate();
            return resultado > 0; // Retorna true si se insertó con éxito
        } catch (SQLException e) {
            System.out.println("Error al registrar cliente.");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para acumular el valor del fiado en la deuda del cliente
    public boolean acumularSaldoCliente(int idCliente, double monto) {
        Connection con = conectar.getConexion();
        PreparedStatement ps = null;
        // Sumamos el cobro directamente a la columna 'deuda'
        String sql = "UPDATE clientes SET deuda = deuda + ? WHERE id_cliente = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setDouble(1, monto);
            ps.setInt(2, idCliente);

            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar la deuda del cliente.");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para restar un abono de la deuda actual del cliente
    public boolean registrarAbonoCliente(int idCliente, double montoAbono) {
        Connection con = conectar.getConexion();
        PreparedStatement ps = null;
        // Restamos el abono directamente a la columna 'deuda'
        String sql = "UPDATE clientes SET deuda = deuda - ? WHERE id_cliente = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setDouble(1, montoAbono);
            ps.setInt(2, idCliente);

            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar el abono del cliente.");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean eliminarCliente(int idCliente) {
    String sql = "DELETE FROM clientes WHERE id_cliente = ?";
    try (Connection con = conectar.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, idCliente);
        int filasAfectadas = ps.executeUpdate();
        return filasAfectadas > 0;
        
    } catch (SQLException e) {
        System.out.println("Error al eliminar cliente: " + e.getMessage());
        return false;
    }
}
}