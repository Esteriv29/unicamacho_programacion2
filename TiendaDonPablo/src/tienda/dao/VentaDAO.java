package tienda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import tienda.conexion.conectar; 
import tienda.modelo.FiadoDetalle;

public class VentaDAO {

   private conectar conectar = new conectar(); 

    // MÉTODO 1: Para cargar la tabla en la pestaña "Historial de Fiados"
    public List<FiadoDetalle> obtenerHistorialFiados() {
        List<FiadoDetalle> lista = new ArrayList<>();
        Connection con = conectar.getConexion(); 
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT v.id_venta, v.fecha, c.nombre, c.apellido, " +
                     "IFNULL(p.nombre, 'Sin especificar') AS prod_nombre, " +
                     "IFNULL(dv.cantidad, 0) AS cantidad, " +
                     "v.total AS subtotal " + 
                     "FROM ventas v " +
                     "JOIN clientes c ON v.id_cliente = c.id_cliente " +
                     "LEFT JOIN detalle_ventas dv ON v.id_venta = dv.id_venta " +
                     "LEFT JOIN productos p ON dv.id_producto = p.id_producto " +
                     "WHERE UPPER(v.tipo_pago) = 'CRÉDITO' " + 
                     "ORDER BY v.fecha DESC";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String clienteCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                
                FiadoDetalle detalle = new FiadoDetalle(
                    rs.getInt("id_venta"),
                    rs.getString("fecha"),
                    clienteCompleto,
                    rs.getString("prod_nombre"), 
                    rs.getInt("cantidad"),
                    rs.getDouble("subtotal")
                );
                lista.add(detalle);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener historial en VentaDAO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    // MÉTODO 2: El proceso transaccional que inyecta los datos reales en MySQL al cobrar
    public boolean guardarVentaCompleta(int idCliente, int idTrabajador, double total, String tipoPago, DefaultTableModel modeloCarrito) {
       Connection con = conectar.getConexion();
    PreparedStatement psVenta = null;
    PreparedStatement psDetalle = null;
    PreparedStatement psStock = null; // 👈 1. PREPAREDSTATEMENT PARA STOCK
    ResultSet rs = null;
    
    String sqlVenta = "INSERT INTO ventas (id_cliente, id_trabajador, fecha, total, tipo_pago) VALUES (?, ?, NOW(), ?, ?)";
    String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
    
    // 👈 2. CONSULTA SQL PARA DESCONTAR EL STOCK
    String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";
    
    try {
        con.setAutoCommit(false); // Transacción segura
        
        // --- A. INSERTAR VENTA (CABECERA) ---
        psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
        psVenta.setInt(1, idCliente);
        psVenta.setInt(2, idTrabajador);
        psVenta.setDouble(3, total);
        psVenta.setString(4, tipoPago);
        psVenta.executeUpdate();
        
        rs = psVenta.getGeneratedKeys();
        int idVentaGenerado = 0;
        if (rs.next()) {
            idVentaGenerado = rs.getInt(1);
        }
        
        // --- B. PREPARAR CONSULTAS PARA DETALLE Y STOCK ---
        psDetalle = con.prepareStatement(sqlDetalle);
        psStock = con.prepareStatement(sqlStock); // 👈 Preparar actualización de stock
        
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            int idProducto = Integer.parseInt(modeloCarrito.getValueAt(i, 0).toString());
            int cantidad = Integer.parseInt(modeloCarrito.getValueAt(i, 3).toString());
            
            String precioStr = modeloCarrito.getValueAt(i, 2).toString().replace("$", "").replace(".", "").trim();
            double precioUnitario = Double.parseDouble(precioStr);
            
            String subtotalStr = modeloCarrito.getValueAt(i, 4).toString().replace("$", "").replace(".", "").trim();
            double subtotal = Double.parseDouble(subtotalStr);
            
            // Insertar Detalle
            psDetalle.setInt(1, idVentaGenerado);
            psDetalle.setInt(2, idProducto);
            psDetalle.setInt(3, cantidad);
            psDetalle.setDouble(4, precioUnitario);
            psDetalle.setDouble(5, subtotal);
            psDetalle.addBatch();
            
            // 👈 3. PARÁMETROS PARA RESTAR STOCK (stock = stock - cantidad)
            psStock.setInt(1, cantidad);    // Restar esta cantidad
            psStock.setInt(2, idProducto);  // Al producto correspondiente
            psStock.addBatch();
        }
        
        // Ejecutar los lotes
        psDetalle.executeBatch();
        psStock.executeBatch(); // 👈 Ejecutar actualización de stock en lote
        
        con.commit(); // Guardado global exitoso
        return true;
        
    } catch (SQLException e) {
        try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        System.out.println("Error al guardar la venta y actualizar stock: " + e.getMessage());
        e.printStackTrace();
        return false;
    } finally {
        try {
            if (rs != null) rs.close();
            if (psVenta != null) psVenta.close();
            if (psDetalle != null) psDetalle.close();
            if (psStock != null) psStock.close(); // 👈 Cerrar recurso
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
}