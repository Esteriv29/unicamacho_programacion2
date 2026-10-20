package tienda.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import tienda.dao.ProductoDAO;
import tienda.modelo.Producto;

public class DlgFormProducto extends JDialog {

 private JTextField txtNombre, txtPrecio, txtStock;
    private JButton btnGuardar, btnCancelar;
    private boolean accionExitosa = false;

    public DlgFormProducto(Frame padre) {
        super(padre, "Registrar Nuevo Producto", true);
        setSize(400, 350);
        setLocationRelativeTo(padre);
        setLayout(null);
        setResizable(false);

        Color azulOscuro = new Color(24, 38, 59);
        Color azulClaro = new Color(41, 128, 185);

        // Título de la ventana
        JLabel lblTitulo = new JLabel("NUEVO PRODUCTO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setBounds(10, 20, 364, 30);
        add(lblTitulo);

        // Campos del Formulario
        int yInicial = 70;
        int espacioY = 55;

        add(crearLabel("Nombre / Descripción del Producto:", 40, yInicial));
        txtNombre = crearTxt(40, yInicial + 20);
        add(txtNombre);

        add(crearLabel("Precio de Venta ($):", 40, yInicial + espacioY));
        txtPrecio = crearTxt(40, yInicial + espacioY + 20);
        add(txtPrecio);

        add(crearLabel("Stock / Cantidad Inicial:", 40, yInicial + (espacioY * 2)));
        txtStock = crearTxt(40, yInicial + (espacioY * 2) + 20);
        add(txtStock);

        // BOTÓN GUARDAR
        btnGuardar = new JButton("GUARDAR");
        btnGuardar.setBounds(40, 250, 140, 35);
        btnGuardar.setBackground(azulClaro);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(null);
        add(btnGuardar);

        // BOTÓN CANCELAR
        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBounds(200, 250, 140, 35);
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
        String precioStr = txtPrecio.getText().trim();
        String stockStr = txtStock.getText().trim();

        if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // CORRECCIÓN CLAVE: Removemos los puntos de miles que confunden a Java
            precioStr = precioStr.replace(".", "").trim();

            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            // Creamos el objeto modelo
            Producto nuevoProducto = new Producto(0, nombre, precio, stock);
            ProductoDAO dao = new ProductoDAO();

            if (dao.registrarProducto(nuevoProducto)) {
                JOptionPane.showMessageDialog(this, "¡Producto registrado correctamente!");
                accionExitosa = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio y el stock deben ser valores numéricos válidos (Ej: 3500 o 3.500).", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isAccionExitosa() {
        return accionExitosa;
    }

    private JLabel crearLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(70, 70, 70));
        lbl.setBounds(x, y, 250, 20);
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
