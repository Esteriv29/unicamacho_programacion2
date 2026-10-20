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
import tienda.dao.ClienteDAO;
import tienda.modelo.Cliente;

public class PnlClientes extends JPanel {

   private JTextField txtBuscar;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private ClienteDAO clienteDAO;
    private JButton btnEliminar;

    public PnlClientes() {
        clienteDAO = new ClienteDAO();
        
        // Configuración del panel base
        setBackground(new Color(245, 246, 250));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- BARRA SUPERIOR: Buscador y Título ---
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(new Color(245, 246, 250));

        JLabel lblTitulo = new JLabel("La Libreta Digital - Gestión de Clientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(24, 38, 59));
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);

        // Subpanel para el buscador y botones
        JPanel panelBuscador = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelBuscador.setBackground(new Color(245, 246, 250));
        
        JLabel lblBuscar = new JLabel("🔍 Buscar Cliente: ");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        txtBuscar = new JTextField(18);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // --- BOTÓN NUEVO CLIENTE ---
        JButton btnNuevoCliente = new JButton("➕ Registrar Cliente");
        btnNuevoCliente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNuevoCliente.setBackground(new Color(41, 128, 185)); // Azul claro
        btnNuevoCliente.setForeground(Color.WHITE);
        btnNuevoCliente.setOpaque(true);
        btnNuevoCliente.setBorderPainted(false);
        btnNuevoCliente.setFocusPainted(false);
        btnNuevoCliente.setPreferredSize(new Dimension(160, 32));
        btnNuevoCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // --- BOTÓN ELIMINAR CLIENTE ---
        btnEliminar = new JButton("🗑️ Eliminar Cliente");
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEliminar.setBackground(new Color(192, 57, 43)); // Rojo
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setOpaque(true);
        btnEliminar.setBorderPainted(false);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setPreferredSize(new Dimension(160, 32));
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // AGREGAR ELEMENTOS AL PANEL SUPERIOR
        panelBuscador.add(lblBuscar);
        panelBuscador.add(txtBuscar);
        panelBuscador.add(btnNuevoCliente);
        panelBuscador.add(btnEliminar); // 👈 ¡AQUÍ ESTÁ LA LÍNEA QUE FALTABA!
        
        panelSuperior.add(panelBuscador, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // --- TABLA CENTRAL: Lista de Clientes ---
        String[] columnas = {"ID", "Nombre", "Apellido", "Teléfono", "Dirección", "Límite de Crédito"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaClientes.setRowHeight(25);
        
        // Configuración del Encabezado
        JTableHeader encabezado = tablaClientes.getTableHeader();
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
        
        JScrollPane scrollTabla = new JScrollPane(tablaClientes);
        add(scrollTabla, BorderLayout.CENTER);

        // --- EVENTO DE BÚSQUEDA EN TIEMPO REAL ---
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cargarTabla(txtBuscar.getText());
            }
        });

        // --- EVENTO ELIMINAR CLIENTE ---
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaClientes.getSelectedRow();
                
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(null, 
                        "Por favor, seleccione un cliente de la tabla para eliminar.", 
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idCliente = Integer.parseInt(tablaClientes.getValueAt(filaSeleccionada, 0).toString());
                String nombreCliente = tablaClientes.getValueAt(filaSeleccionada, 1).toString();

                int confirmacion = JOptionPane.showConfirmDialog(null, 
                    "¿Está seguro de que desea eliminar al cliente: " + nombreCliente + "?\nEsta acción no se puede deshacer.", 
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    boolean eliminado = clienteDAO.eliminarCliente(idCliente);

                    if (eliminado) {
                        JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente.");
                        cargarTabla(txtBuscar.getText()); // 👈 Refresca con la búsqueda actual
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "No se pudo eliminar el cliente.\n" +
                            "Nota: Si el cliente tiene ventas o historial en la libreta, no se puede borrar para conservar el registro contable.", 
                            "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // --- EVENTO REGISTRAR CLIENTE ---
        btnNuevoCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame ventanaPadre = (Frame) SwingUtilities.getWindowAncestor(PnlClientes.this);
                DlgFormCliente formulario = new DlgFormCliente(ventanaPadre);
                formulario.setVisible(true);

                if (formulario.isAccionExitosa()) {
                    cargarTabla("");
                }
            }
        });

        // Carga inicial
        cargarTabla("");
    } 

    // Método para rellenar la tabla llamando al DAO
    public void cargarTabla(String texto) {
        modeloTabla.setRowCount(0);
        List<Cliente> lista = clienteDAO.buscarClientes(texto);

        for (Cliente c : lista) {
            Object[] fila = {
                c.getIdCliente(),
                c.getNombre(),
                c.getApellido(),
                c.getTelefono(),
                c.getDireccion(),
                c.getLimiteCredito()
            };
            modeloTabla.addRow(fila);
        }
    }
}