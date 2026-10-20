package tienda.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import tienda.dao.ClienteDAO;
import tienda.modelo.Cliente;

public class DlgFormCliente extends JDialog {

private JTextField txtNombre, txtApellido, txtTelefono, txtDireccion, txtLimite;
    private JButton btnGuardar, btnCancelar;
    private boolean accionExitosa = false;

    // Le pasamos el JFrame padre para que se centre correctamente encima de él
    public DlgFormCliente(Frame padre) {
        super(padre, "Registrar Nuevo Cliente", true); // true hace que sea Modal (bloquea el fondo)
        setSize(400, 450);
        setLocationRelativeTo(padre);
        setLayout(null);
        setResizable(false);

        // Colores combinados con tu sistema
        Color azulOscuro = new Color(24, 38, 59);
        Color azulClaro = new Color(41, 128, 185);
        Color grisTexto = new Color(70, 70, 70);

        // Título de la ventana interna
        JLabel lblTitulo = new JLabel("NUEVO CLIENTE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setBounds(10, 20, 364, 30);
        add(lblTitulo);

        // Crear los campos con un método auxiliar para no repetir código visual
        int yInicial = 80;
        int espacioY = 55;

        add(crearLabel("Nombre:", 40, yInicial));
        txtNombre = crearTxt(40, yInicial + 20);
        add(txtNombre);

        add(crearLabel("Apellido:", 40, yInicial + espacioY));
        txtApellido = crearTxt(40, yInicial + espacioY + 20);
        add(txtApellido);

        add(crearLabel("Teléfono:", 40, yInicial + (espacioY * 2)));
        txtTelefono = crearTxt(40, yInicial + (espacioY * 2) + 20);
        add(txtTelefono);

        add(crearLabel("Dirección:", 40, yInicial + (espacioY * 3)));
        txtDireccion = crearTxt(40, yInicial + (espacioY * 3) + 20);
        add(txtDireccion);

        add(crearLabel("Límite de Crédito ($):", 40, yInicial + (espacioY * 4)));
        txtLimite = crearTxt(40, yInicial + (espacioY * 4) + 20);
        txtLimite.setText("50000"); // Un valor inicial por defecto razonable
        add(txtLimite);

        // BOTÓN GUARDAR
        btnGuardar = new JButton("GUARDAR");
        btnGuardar.setBounds(40, 360, 140, 35);
        btnGuardar.setBackground(azulClaro);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(null);
        add(btnGuardar);

        // BOTÓN CANCELAR
        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBounds(200, 360, 140, 35);
        btnCancelar.setBackground(new Color(192, 57, 43));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(null);
        add(btnCancelar);

        // --- EVENTOS ---
        btnCancelar.addActionListener(e -> dispose());

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDatos();
            }
        });
    }

    private void guardarDatos() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String limiteStr = txtLimite.getText().trim();

        // Validar campos vacíos obligatorios
        if (nombre.isEmpty() || apellido.isEmpty() || limiteStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre, Apellido y Límite son campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double limite = Double.parseDouble(limiteStr);

            // Armar el objeto modelo
            Cliente nuevoCliente = new Cliente(0, nombre, apellido, telefono, direccion, limite, 0.0);
            ClienteDAO dao = new ClienteDAO();

            if (dao.registrarCliente(nuevoCliente)) {
                JOptionPane.showMessageDialog(this, "¡Cliente guardado correctamente!");
                accionExitosa = true;
                dispose(); // Cierra el formulario flotante
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El límite de crédito debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isAccionExitosa() {
        return accionExitosa;
    }

    // Métodos ayudantes para mantener el diseño limpio
    private JLabel crearLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(70, 70, 70));
        lbl.setBounds(x, y, 200, 20);
        return lbl;
    }

    private JTextField crearTxt(int x, int y) {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setBounds(x, y, 300, 25);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(0, 5, 0, 5)
        ));
        return txt;
    }
}
