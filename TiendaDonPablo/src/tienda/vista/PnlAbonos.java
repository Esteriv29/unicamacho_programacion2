package tienda.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.List;
import tienda.dao.ClienteDAO;
import tienda.modelo.Cliente;

public class PnlAbonos extends JPanel {

private JComboBox<String> cbClientes;
    private JLabel lblDeudaActual;
    private JTextField txtMontoAbono;
    private JButton btnGuardarAbono;
    
    private ClienteDAO clienteDAO = new ClienteDAO();
    private DecimalFormat formateador = new DecimalFormat("$ #,##0");
    private int idClienteSeleccionado = -1;
    private double deudaActualCliente = 0.0;

    public PnlAbonos() {
        setBackground(new Color(245, 246, 250));
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        Color azulOscuro = new Color(24, 38, 59);
        Color verdeEsmeralda = new Color(46, 204, 113);

        // Panel contenedor central con estilo de tarjeta blanca
        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230), 1));
        tarjeta.setLayout(null);
        tarjeta.setPreferredSize(new Dimension(450, 400));

        // Título del módulo
        JLabel lblTitulo = new JLabel("Registrar Abono a Libreta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setBounds(20, 20, 410, 30);
        tarjeta.add(lblTitulo);

        // Etiqueta Buscar Cliente
        JLabel lblBuscar = new JLabel("Buscar Cliente:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBuscar.setBounds(40, 75, 200, 20);
        tarjeta.add(lblBuscar);

        // ComboBox Buscador de Clientes
        cbClientes = new JComboBox<>();
        cbClientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbClientes.setBounds(40, 100, 370, 35);
        tarjeta.add(cbClientes);

        // Contenedor visual de la deuda
        JLabel lblTextoDeuda = new JLabel("DEUDA ACTUAL:", SwingConstants.CENTER);
        lblTextoDeuda.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTextoDeuda.setForeground(Color.GRAY);
        lblTextoDeuda.setBounds(40, 155, 370, 20);
        tarjeta.add(lblTextoDeuda);

        lblDeudaActual = new JLabel("$ 0", SwingConstants.CENTER);
        lblDeudaActual.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblDeudaActual.setForeground(new Color(192, 57, 43)); // Rojo para las deudas
        lblDeudaActual.setBounds(40, 175, 370, 45);
        tarjeta.add(lblDeudaActual);

        // Etiqueta Monto a Abonar
        JLabel lblMonto = new JLabel("Monto que va a pagar ($):");
        lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMonto.setBounds(40, 240, 200, 20);
        tarjeta.add(lblMonto);

        // Campo de Texto para el Abono
        txtMontoAbono = new JTextField();
        txtMontoAbono.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtMontoAbono.setHorizontalAlignment(JTextField.CENTER);
        txtMontoAbono.setBounds(40, 265, 370, 35);
        tarjeta.add(txtMontoAbono);

        // Botón Guardar Abono
        btnGuardarAbono = new JButton("💾 REGISTRAR ABONO");
        btnGuardarAbono.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardarAbono.setBackground(verdeEsmeralda);
        btnGuardarAbono.setForeground(Color.WHITE);
        btnGuardarAbono.setOpaque(true);
        btnGuardarAbono.setBorderPainted(false);
        btnGuardarAbono.setBounds(40, 325, 370, 45);
        btnGuardarAbono.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tarjeta.add(btnGuardarAbono);

        // Añadir tarjeta al panel principal centrado
        add(tarjeta);

        // Inicializar la lógica del buscador interactivo
        configurarBuscadorClientes();

        // Evento del botón registrar abono
        btnGuardarAbono.addActionListener(e -> ejecutarRegistroAbono());
    }

    private void configurarBuscadorClientes() {
        cbClientes.removeAllItems();
        cbClientes.addItem("-- Seleccione un vecino --");
        cbClientes.setEditable(true);

        // Carga inicial de todos los clientes
        cargarItemsCombo("");

        JTextField txtEditor = (JTextField) cbClientes.getEditor().getEditorComponent();

        // 1. PLACEHOLDER AUTOMÁTICO
        txtEditor.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtEditor.getText().equals("-- Seleccione un vecino --")) {
                    txtEditor.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtEditor.getText().trim().isEmpty()) {
                    txtEditor.setText("-- Seleccione un vecino --");
                }
            }
        });

        // 2. BUSCADOR EN TIEMPO REAL
        txtEditor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) return;

                String textoEscrito = txtEditor.getText();

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cbClientes.setPopupVisible(false);
                    cargarDeudaClienteSeleccionado();
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    cargarItemsCombo(textoEscrito.trim());
                    txtEditor.setText(textoEscrito);

                    if (cbClientes.getItemCount() > 1 && !textoEscrito.trim().isEmpty()) {
                        cbClientes.setPopupVisible(true);
                    }
                });
            }
        });

        // 3. CAPTURA SELECCIÓN CON EL RATÓN O ENTER
        cbClientes.addActionListener(e -> cargarDeudaClienteSeleccionado());
    }

    private void cargarItemsCombo(String filtro) {
        cbClientes.removeAllItems();
        cbClientes.addItem("-- Seleccione un vecino --");
        List<Cliente> lista = clienteDAO.buscarClientes(filtro);
        for (Cliente c : lista) {
            cbClientes.addItem(c.getIdCliente() + " - " + c.getNombre() + " " + c.getApellido());
        }
    }

    private void cargarDeudaClienteSeleccionado() {
        Object item = cbClientes.getSelectedItem();
        if (item == null) return;

        String seleccion = item.toString().trim();

        if (seleccion.contains("-- Seleccione") || !seleccion.contains(" - ")) {
            idClienteSeleccionado = -1;
            deudaActualCliente = 0.0;
            lblDeudaActual.setText("$ 0");
            return;
        }

        try {
            String[] partes = seleccion.split(" - ");
            int id = Integer.parseInt(partes[0].trim());

            List<Cliente> resultado = clienteDAO.buscarClientes("");
            for (Cliente c : resultado) {
                if (c.getIdCliente() == id) {
                    idClienteSeleccionado = id;
                    deudaActualCliente = c.getDeuda();
                    lblDeudaActual.setText(formateador.format(deudaActualCliente));
                    return;
                }
            }
        } catch (Exception ex) {
            idClienteSeleccionado = -1;
        }
    }

    private void ejecutarRegistroAbono() {
        // Forzar actualización por si el usuario no presionó Enter
        cargarDeudaClienteSeleccionado();

        if (idClienteSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente válido de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String abonoStr = txtMontoAbono.getText().trim();
        if (abonoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escriba el monto que va a abonar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Limpieza segura: quita el símbolo de moneda y espacios
            abonoStr = abonoStr.replace("$", "").replace(" ", "").trim();

            double montoAbono = Double.parseDouble(abonoStr);

            if (montoAbono <= 0) {
                JOptionPane.showMessageDialog(this, "El abono debe ser un monto mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (montoAbono > deudaActualCliente) {
                JOptionPane.showMessageDialog(this, "El abono no puede ser mayor que la deuda actual (" + formateador.format(deudaActualCliente) + ").", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Confirmar abono de " + formateador.format(montoAbono) + " para este cliente?",
                    "Confirmar Operación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                if (clienteDAO.registrarAbonoCliente(idClienteSeleccionado, montoAbono)) {
                    JOptionPane.showMessageDialog(this, "¡Abono registrado con éxito en la libreta!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Limpieza e higiene de la interfaz tras registrar
                    txtMontoAbono.setText("");
                    configurarBuscadorClientes();
                    lblDeudaActual.setText("$ 0");
                    idClienteSeleccionado = -1;
                    deudaActualCliente = 0.0;
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar el pago en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número entero o decimal válido (ej: 10000 o 15000.50).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}