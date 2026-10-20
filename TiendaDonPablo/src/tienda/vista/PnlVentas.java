package tienda.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.List;
import tienda.dao.ClienteDAO;
import tienda.dao.ProductoDAO;
import tienda.dao.VentaDAO;
import tienda.modelo.Cliente;
import tienda.modelo.Producto;

public class PnlVentas extends JPanel {

  private JTextField txtBuscarProducto, txtCantidad;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JLabel lblTotalGigante;
    private JButton btnAgregar, btnCobrarEfectivo, btnFiarLibreta, btnEliminarItem;
    private JComboBox<String> cbClientes; 
    
    private double totalVenta = 0.0;
    private DecimalFormat formateador = new DecimalFormat("$ #,###");
    
    // Instancias de los DAOs para conectar con la base de datos
    private ProductoDAO productoDAO = new ProductoDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private VentaDAO ventaDAO = new VentaDAO();

    public PnlVentas() {
        setBackground(new Color(245, 246, 250));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Color azulOscuro = new Color(24, 38, 59);
        Color azulClaro = new Color(41, 128, 185);
        Color verdeEsmeralda = new Color(46, 204, 113);

        // =========================================================================
        // 1. PANEL CENTRAL: BUSCADOR Y CARRITO DE COMPRAS
        // =========================================================================
        JPanel panelCentro = new JPanel(new BorderLayout(15, 15));
        panelCentro.setBackground(new Color(245, 246, 250));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBusqueda.setBackground(new Color(245, 246, 250));

        JLabel lblBuscar = new JLabel("🔍 Producto:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtBuscarProducto = new JTextField(15);
        txtBuscarProducto.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblCant = new JLabel("Cant:");
        lblCant.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtCantidad = new JTextField("1", 4);
        txtCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCantidad.setHorizontalAlignment(JTextField.CENTER);

        btnAgregar = new JButton("Añadir");
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAgregar.setBackground(azulClaro);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setOpaque(true);
        btnAgregar.setBorderPainted(false);
        btnAgregar.setPreferredSize(new Dimension(90, 28));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscarProducto);
        panelBusqueda.add(lblCant);
        panelBusqueda.add(txtCantidad);
        panelBusqueda.add(btnAgregar);
        panelCentro.add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = {"Código", "Producto", "Precio Unit.", "Cant.", "Subtotal"};
        modeloCarrito = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCarrito.setRowHeight(25);

        JTableHeader encabezado = tablaCarrito.getTableHeader();
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        encabezado.setBackground(azulOscuro);
        encabezado.setForeground(Color.WHITE);
        encabezado.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(azulOscuro);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(45, 60, 80)));
                return this;
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaCarrito);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        btnEliminarItem = new JButton("❌ Quitar Artículo Seleccionado");
        btnEliminarItem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEliminarItem.setForeground(new Color(192, 57, 43));
        btnEliminarItem.setContentAreaFilled(false);
        btnEliminarItem.setBorderPainted(false);
        btnEliminarItem.setHorizontalAlignment(SwingConstants.LEFT);
        btnEliminarItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelCentro.add(btnEliminarItem, BorderLayout.SOUTH);
        
        add(panelCentro, BorderLayout.CENTER);

        // =========================================================================
        // 2. PANEL LATERAL DERECHO: FORMULARIO DE COBRO Y TOTALIZADOR
        // =========================================================================
        JPanel panelFacturacion = new JPanel();
        panelFacturacion.setBackground(Color.WHITE);
        panelFacturacion.setPreferredSize(new Dimension(300, 600));
        panelFacturacion.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(220, 224, 230)));
        panelFacturacion.setLayout(null);

        JLabel lblTituloTotal = new JLabel("TOTAL A COBRAR:", SwingConstants.CENTER);
        lblTituloTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloTotal.setForeground(Color.GRAY);
        lblTituloTotal.setBounds(20, 20, 260, 20);
        panelFacturacion.add(lblTituloTotal);

        lblTotalGigante = new JLabel("$ 0", SwingConstants.CENTER);
        lblTotalGigante.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTotalGigante.setForeground(azulOscuro);
        lblTotalGigante.setBounds(20, 45, 260, 50);
        panelFacturacion.add(lblTotalGigante);

        JSeparator separador = new JSeparator();
        separador.setBounds(20, 110, 260, 2);
        panelFacturacion.add(separador);

        JLabel lblCliente = new JLabel("Asignar Cliente (Opcional):");
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCliente.setForeground(azulOscuro);
        lblCliente.setBounds(25, 130, 250, 20);
        panelFacturacion.add(lblCliente);

        cbClientes = new JComboBox<>();
        cbClientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbClientes.setBounds(25, 155, 250, 30);
        panelFacturacion.add(cbClientes);

        btnCobrarEfectivo = new JButton("💵 COBRAR EN EFECTIVO");
        btnCobrarEfectivo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCobrarEfectivo.setBackground(verdeEsmeralda);
        btnCobrarEfectivo.setForeground(Color.WHITE);
        btnCobrarEfectivo.setOpaque(true);
        btnCobrarEfectivo.setBorderPainted(false);
        btnCobrarEfectivo.setFocusPainted(false);
        btnCobrarEfectivo.setBounds(25, 250, 250, 45);
        btnCobrarEfectivo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelFacturacion.add(btnCobrarEfectivo);

        btnFiarLibreta = new JButton("📝 FIAR A LA LIBRETA");
        btnFiarLibreta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnFiarLibreta.setBackground(azulOscuro);
        btnFiarLibreta.setForeground(Color.WHITE);
        btnFiarLibreta.setOpaque(true);
        btnFiarLibreta.setBorderPainted(false);
        btnFiarLibreta.setFocusPainted(false);
        btnFiarLibreta.setBounds(25, 310, 250, 45);
        btnFiarLibreta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelFacturacion.add(btnFiarLibreta);

        add(panelFacturacion, BorderLayout.EAST);

        // =========================================================================
        // ACCIONES Y LOGICA DE EVENTOS
        // =========================================================================
        cargarClientesCombo();

        ActionListener accionBuscar = e -> ejecutarBusquedaProducto();
        btnAgregar.addActionListener(accionBuscar);
        txtBuscarProducto.addActionListener(accionBuscar);

        btnEliminarItem.addActionListener(e -> {
            int filaSeleccionada = tablaCarrito.getSelectedRow();
            if (filaSeleccionada >= 0) {
                modeloCarrito.removeRow(filaSeleccionada);
                calcularTotal();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un artículo de la tabla para quitarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

      // 1. ACCIÓN: COBRAR EN EFECTIVO
        btnCobrarEfectivo.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
           if (modeloCarrito.getRowCount() == 0) {
            JOptionPane.showMessageDialog(PnlVentas.this, "El carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
         return;
        }

        // CORRECCIÓN: "Efectivo" limpio, sin comillas simples internas
        boolean guardado = ventaDAO.guardarVentaCompleta(1, 1, totalVenta, "Efectivo", modeloCarrito);

        if (guardado) {
            JOptionPane.showMessageDialog(PnlVentas.this, "¡Venta en efectivo registrada con éxito!\nTotal cobrado: " + formateador.format(totalVenta), "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);
            limpiarVenta();
        } else {
            JOptionPane.showMessageDialog(PnlVentas.this, "Error crítico al registrar la venta en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});

      // 2. ACCIÓN: FIAR A LA LIBRETA
        btnFiarLibreta.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
          if (modeloCarrito.getRowCount() == 0) {
            JOptionPane.showMessageDialog(PnlVentas.this, "El carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String clienteSeleccionado = cbClientes.getSelectedItem().toString();

        if (clienteSeleccionado.contains("-- Público General")) {
            JOptionPane.showMessageDialog(PnlVentas.this, "Para fiar una compra, debe buscar y seleccionar un cliente de la lista.", "Cliente requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String[] partes = clienteSeleccionado.split(" - ");
            int idCliente = Integer.parseInt(partes[0]);
            String nombreCliente = partes[1];

            int respuesta = JOptionPane.showConfirmDialog(PnlVentas.this, 
                "¿Está seguro de cargar " + formateador.format(totalVenta) + " a la cuenta de " + nombreCliente + "?",
                "Confirmar Fiado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                
                // CORRECCIÓN: "Crédito" limpio con tilde, sin comillas simples internas
                boolean ventaGuardada = ventaDAO.guardarVentaCompleta(idCliente, 1, totalVenta, "Crédito", modeloCarrito);
                
                if (ventaGuardada) {
                    if (clienteDAO.acumularSaldoCliente(idCliente, totalVenta)) {
                        JOptionPane.showMessageDialog(PnlVentas.this, "¡Cuenta guardada en la libreta digital de " + nombreCliente + "!", "Fiado Registrado", JOptionPane.INFORMATION_MESSAGE);
                        limpiarVenta();
                    } else {
                        JOptionPane.showMessageDialog(PnlVentas.this, "Se registró la venta, pero no se pudo actualizar el saldo acumulado.", "Error de Saldo", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(PnlVentas.this, "Hubo un error al guardar los registros de la venta.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(PnlVentas.this, "Por favor, seleccione un cliente válido de la lista desplegable.", "Error de selección", JOptionPane.ERROR_MESSAGE);
        }
       }
     });
    }

    private void ejecutarBusquedaProducto() {
        String texto = txtBuscarProducto.getText().trim();
        if (texto.isEmpty()) return;

        List<Producto> encontrados = productoDAO.buscarProductos(texto);

        if (encontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún producto con ese nombre.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        } else if (encontrados.size() == 1) {
            agregarProductoAlCarrito(encontrados.get(0));
        } else {
            Frame ventanaPadre = (Frame) SwingUtilities.getWindowAncestor(this);
            Producto seleccionado = (Producto) JOptionPane.showInputDialog(
                    this,
                    "Se encontraron múltiples artículos. Seleccione uno:",
                    "Selector de Productos",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    encontrados.toArray(),
                    encontrados.get(0)
            );
            if (seleccionado != null) {
                agregarProductoAlCarrito(seleccionado);
            }
        }
    }

    private void agregarProductoAlCarrito(Producto p) {
        try {
        int cantidad = Integer.parseInt(txtCantidad.getText().trim());
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- VALIDACIÓN DE STOCK (EVITA NÚMEROS NEGATIVOS) ---
        if (p.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, 
                "El producto '" + p.getDescripcion() + "' está AGOTADO.\nDon Pablo debe ingresar más inventario antes de venderlo.", 
                "Sin Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cantidad > p.getStock()) {
            JOptionPane.showMessageDialog(this, 
                "No hay suficiente inventario disponible de '" + p.getDescripcion() + "'.\nSolo quedan: " + p.getStock() + " unidades.", 
                "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // ------------------------------------------------------

        double subtotal = p.getPrecio() * cantidad;

        Object[] fila = {
            p.getIdProducto(),
            p.getDescripcion(),
            formateador.format(p.getPrecio()),
            cantidad,
            formateador.format(subtotal)
        };
        
        modeloCarrito.addRow(fila);
        calcularTotal();
        
        txtBuscarProducto.setText("");
        txtCantidad.setText("1");
        txtBuscarProducto.requestFocus();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Escriba una cantidad numérica válida.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

    private void calcularTotal() {
        totalVenta = 0.0;
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            String subtotalStr = modeloCarrito.getValueAt(i, 4).toString();
            subtotalStr = subtotalStr.replace("$", "").replace(".", "").trim();
            totalVenta += Double.parseDouble(subtotalStr);
        }
        lblTotalGigante.setText(formateador.format(totalVenta));
    }

    private void cargarClientesCombo() {
       cbClientes.removeAllItems();
        cbClientes.addItem("-- Público General (Efectivo) --");
        cbClientes.setEditable(true);
        
        // Carga inicial de los vecinos de la base de datos
        List<Cliente> todosLosClientes = clienteDAO.buscarClientes("");
        for (Cliente c : todosLosClientes) {
            cbClientes.addItem(c.getIdCliente() + " - " + c.getNombre() + " " + c.getApellido());
        }

        // CORRECCIÓN: Usamos 'cbClientes' que es el nombre real de tu variable
        JTextField txtEditorVentas = (JTextField) cbClientes.getEditor().getEditorComponent();

        // --- EFECTO FANTASMA / PLACEHOLDER AUTOMÁTICO ---
        txtEditorVentas.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtEditorVentas.getText().equals("-- Público General (Efectivo) --")) {
                    txtEditorVentas.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtEditorVentas.getText().trim().isEmpty()) {
                    txtEditorVentas.setText("-- Público General (Efectivo) --");
                }
            }
        });

        // --- BUSCADOR DINÁMICO MIENTRAS DON PABLO ESCRIBE ---
        txtEditorVentas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) return;

                String textoEscrito = txtEditorVentas.getText().trim();

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cbClientes.setPopupVisible(false);
                    return;
                }

                Runnable filtrar = () -> {
                    List<Cliente> filtrados = clienteDAO.buscarClientes(textoEscrito);
                    cbClientes.removeAllItems();
                    cbClientes.addItem("-- Público General (Efectivo) --");

                    for (Cliente c : filtrados) {
                        cbClientes.addItem(c.getIdCliente() + " - " + c.getNombre() + " " + c.getApellido());
                    }
                    txtEditorVentas.setText(textoEscrito);

                    if (!filtrados.isEmpty() && textoEscrito.length() > 0) {
                        cbClientes.setPopupVisible(true);
                    }
                };
                SwingUtilities.invokeLater(filtrar);
            }
        });
    }
    // =========================================================================
    // MÉTODO AGREGADO: Restablece los campos de la interfaz tras cobrar
    // =========================================================================
    private void limpiarVenta() {
        modeloCarrito.setRowCount(0);
        totalVenta = 0.0;
        lblTotalGigante.setText("$ 0");
        txtBuscarProducto.setText("");
        txtCantidad.setText("1");
        cargarClientesCombo(); 
        txtBuscarProducto.requestFocus();
    }
}