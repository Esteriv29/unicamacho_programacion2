package tienda.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import tienda.dao.VentaDAO; 
import tienda.modelo.FiadoDetalle;

public class PnlHistorialFiados extends JPanel {

   private JTable tablaFiados;
    private DefaultTableModel modeloTabla;
    private VentaDAO ventaDAO = new VentaDAO();
    private DecimalFormat formateador = new DecimalFormat("$ #,###");

    public PnlHistorialFiados() {
        setBackground(new Color(245, 246, 250));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Encabezado del panel
        JLabel lblTitulo = new JLabel("📝 Historial de Cuentas Fiadas (Libreta)", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(24, 38, 59));
        add(lblTitulo, BorderLayout.NORTH);

        // Configuración de la Tabla
        String[] columnas = {"N° Venta", "Fecha / Hora", "Cliente / Vecino", "Producto", "Cant.", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaFiados = new JTable(modeloTabla);
        tablaFiados.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaFiados.setRowHeight(28);
        
        // --- AQUÍ SE APLICÓ EL CAMBIO DEL ENCABEZADO FIJO ---
        tablaFiados.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaFiados.getTableHeader().setReorderingAllowed(false);
        
        tablaFiados.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(24, 38, 59)); // Azul Oscuro Fijo
                setForeground(Color.WHITE);           // Letras Blancas Fijas
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(45, 60, 80)));
                return this;
            }
        });
        // -----------------------------------------------------

        JScrollPane scroll = new JScrollPane(tablaFiados);
        add(scroll, BorderLayout.CENTER);

        // Botón para actualizar la lista manualmente
       JButton btnRefrescar = new JButton("🔄 Actualizar Lista");
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefrescar.setBackground(new Color(41, 128, 185)); // Azul Claro institucional
        btnRefrescar.setForeground(Color.WHITE);             // Letras blancas
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setBorderPainted(false);
        btnRefrescar.setOpaque(true);                        // Fuerza a Swing a pintar el fondo azul
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // El truco para evitar el parpadeo en algunos sistemas operativos
        btnRefrescar.setContentAreaFilled(true); 

        btnRefrescar.addActionListener(e -> cargarHistorial());
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(new Color(245, 246, 250));
        panelBotones.add(btnRefrescar);
        add(panelBotones, BorderLayout.SOUTH);
        // ----------------------------------------------

        // Carga inicial de datos
        cargarHistorial();
    }

    public void cargarHistorial() {
        modeloTabla.setRowCount(0);
        List<FiadoDetalle> fiados = ventaDAO.obtenerHistorialFiados();
        
        for (FiadoDetalle f : fiados) {
            Object[] fila = {
                f.getIdVenta(),
                f.getFecha(),
                f.getCliente(),
                f.getProducto(),
                f.getCantidad(),
                formateador.format(f.getSubtotal())
            };
            modeloTabla.addRow(fila);
        }
    }
}