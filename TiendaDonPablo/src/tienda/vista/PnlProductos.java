package tienda.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import tienda.dao.ProductoDAO;
import tienda.modelo.Producto;

public class PnlProductos extends JPanel {

    private JTextField txtBuscar;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private ProductoDAO productoDAO;

    public PnlProductos() {
        productoDAO = new ProductoDAO();

        // Configuración estética del panel base
        setBackground(new Color(245, 246, 250));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- BARRA SUPERIOR ---
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(new Color(245, 246, 250));

        JLabel lblTitulo = new JLabel("Control de Inventario - Productos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(24, 38, 59));
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);

        // Subpanel Layout modificado para dar espacio al nuevo botón
        JPanel panelBuscador = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelBuscador.setBackground(new Color(245, 246, 250));

        JLabel lblBuscar = new JLabel("🔍 Buscar Artículo: ");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtBuscar = new JTextField(20); // Ajustamos tamaño
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // --- BOTÓN NUEVO PRODUCTO CON PROTECCIÓN VISUAL EN WINDOWS ---
        JButton btnNuevoProducto = new JButton("➕ Registrar Producto");
        btnNuevoProducto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNuevoProducto.setBackground(new Color(41, 128, 185)); // Mismo azul institucional
        btnNuevoProducto.setForeground(Color.WHITE);
        btnNuevoProducto.setOpaque(true);
        btnNuevoProducto.setBorderPainted(false);
        btnNuevoProducto.setFocusPainted(false);
        btnNuevoProducto.setPreferredSize(new Dimension(160, 30));
        btnNuevoProducto.setBorder(BorderFactory.createEmptyBorder());
        btnNuevoProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
       // Boton para eliminar producto
       JButton btnEliminarProducto = new JButton("🗑️ Eliminar Producto");
       btnEliminarProducto.setFont(new Font("Segoe UI", Font.BOLD, 13));
       btnEliminarProducto.setBackground(new Color(192, 57, 43)); // Rojo
       btnEliminarProducto.setForeground(Color.WHITE);
       btnEliminarProducto.setOpaque(true);
       btnEliminarProducto.setBorderPainted(false);
       btnEliminarProducto.setFocusPainted(false);
       btnEliminarProducto.setPreferredSize(new Dimension(160, 32));
       btnEliminarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
       
        panelBuscador.add(lblBuscar);
        panelBuscador.add(txtBuscar);
        panelBuscador.add(btnNuevoProducto); // Lo sumamos a la barra superior
        
       // Agregar el botón al panel superior/buscador de productos
        panelBuscador.add(btnEliminarProducto);

        panelSuperior.add(panelBuscador, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // --- TABLA DE PRODUCTOS ---
        String[] columnas = {"Código / ID", "Descripción del Producto", "Precio Unitario", "Stock Disponible"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaProductos.setRowHeight(25);

        JTableHeader encabezado = tablaProductos.getTableHeader();
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        encabezado.setBackground(new Color(24, 38, 59));
        encabezado.setForeground(Color.WHITE);

        encabezado.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(24, 38, 59));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(45, 60, 80)));
                return this;
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        add(scrollTabla, BorderLayout.CENTER);

        // --- BUSCADOR EN TIEMPO REAL ---
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cargarTabla(txtBuscar.getText());
            }
        });

        // --- EVENTO PARA ABRIR EL DIÁLOGO FLOTANTE ---
        btnNuevoProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame ventanaPadre = (Frame) SwingUtilities.getWindowAncestor(PnlProductos.this);
                DlgFormProducto formulario = new DlgFormProducto(ventanaPadre);
                formulario.setVisible(true);

                // Si guardó con éxito, refresca la lista inmediatamente
                if (formulario.isAccionExitosa()) {
                    cargarTabla("");
                }
            }
        });
        
        // 3. Programar el evento del botón
        btnEliminarProducto.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, 
                "Por favor, seleccione un producto de la tabla para eliminar.", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProducto = Integer.parseInt(tablaProductos.getValueAt(filaSeleccionada, 0).toString());
        String nombreProducto = tablaProductos.getValueAt(filaSeleccionada, 1).toString();

        int confirmacion = JOptionPane.showConfirmDialog(null, 
            "¿Desea eliminar el producto '" + nombreProducto + "' del inventario?", 
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            ProductoDAO productoDAO = new ProductoDAO();
            boolean eliminado = productoDAO.eliminarProducto(idProducto);

            if (eliminado) {
                JOptionPane.showMessageDialog(null, "Producto eliminado correctamente del inventario.");
                cargarTabla(""); // Refresca la tabla
            } else {
                JOptionPane.showMessageDialog(null, 
                    "No se pudo eliminar el producto.\nNota: Si el producto ya fue vendido en alguna ocasión, MySQL protege el registro para conservar el historial de ventas.", 
                    "Error al Eliminar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
});

        cargarTabla("");
    }

    public void cargarTabla(String texto) {
     modeloTabla.setRowCount(0);
    List<Producto> lista = productoDAO.buscarProductos(texto);

    // Formateador para dinero en pesos (ej: $ 3.200)
    java.text.DecimalFormat formateador = new java.text.DecimalFormat("$ #,###");

    for (Producto p : lista) {
        // Convertimos el precio a texto formateado
        String precioFormateado = formateador.format(p.getPrecio());

        // Manejo del stock para mostrar "AGOTADO" si llega a 0
        String stockTexto;
        if (p.getStock() <= 0) {
            stockTexto = "⚠️ AGOTADO (0)";
        } else {
            stockTexto = p.getStock() + " unidades";
        }

        // Creamos la fila con los datos limpios y formateados
        Object[] fila = {
            p.getIdProducto(),
            p.getDescripcion(),
            precioFormateado, 
            stockTexto
        };

        modeloTabla.addRow(fila);
    }
}
}