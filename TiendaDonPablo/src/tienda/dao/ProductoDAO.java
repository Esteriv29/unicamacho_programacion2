package tienda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tienda.conexion.conectar;
import tienda.modelo.Producto;

public class ProductoDAO {

// Buscar productos por nombre (Corregido según tu base de datos)
    public List<Producto> buscarProductos(String textoBusqueda) {
        List<Producto> lista = new ArrayList<>();
        Connection con = conectar.getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;

        // CORRECCIÓN 1: Cambiamos 'descripcion' por 'nombre' que es tu columna real
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
            
                p.setDescripcion(rs.getString("nombre")); 
                
                p.setPrecio(rs.getDouble("precio_venta"));
                
                p.setStock(rs.getInt("stock"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar productos.");
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
    // Método para registrar un nuevo producto en la base de datos
public boolean registrarProducto(Producto p) {
    Connection con = conectar.getConexion();
    PreparedStatement ps = null;
    // Usamos los nombres exactos de tu MySQL: codigo_barras (opcional por ahora), nombre, precio_venta, stock
    String sql = "INSERT INTO productos (nombre, precio_venta, stock) VALUES (?, ?, ?)";

    try {
        ps = con.prepareStatement(sql);
        ps.setString(1, p.getDescripcion()); // Recuerda que en el modelo 'descripcion' mapea a 'nombre'
        ps.setDouble(2, p.getPrecio());      // Mapea a 'precio_venta'
        ps.setInt(3, p.getStock());          // Mapea a 'stock'

        int resultado = ps.executeUpdate();
        return resultado > 0; // Retorna true si se guardó con éxito
    } catch (SQLException e) {
        System.out.println("Error al registrar producto.");
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
// Metodo para eliminar producto
public boolean eliminarProducto(int idProducto) {
    String sql = "DELETE FROM productos WHERE id_producto = ?";
    try (Connection con = conectar.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, idProducto);
        int filasAfectadas = ps.executeUpdate();
        return filasAfectadas > 0;
        
    } catch (SQLException e) {
        System.out.println("Error al eliminar producto: " + e.getMessage());
        return false;
    }
}
}