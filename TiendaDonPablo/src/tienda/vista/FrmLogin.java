package tienda.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import tienda.dao.TrabajadorDAO;
import tienda.modelo.Trabajador;

public class FrmLogin extends JFrame{

private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JLabel lblUsuario, lblContrasena, lblTitulo, lblLogo;
    private JPanel panelIzquierdo, panelDerecho;

    public FrmLogin() {
        // Configuración de la ventana principal
        setTitle("Acceso - Tienda Don Pablo");
        setSize(600, 350); // La hacemos un poco más ancha para el diseño de dos paneles
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // Diseño libre para acomodar los dos paneles
        setResizable(false); // Evita que deformen el diseño al maximizar

        // Definición de Colores (Paleta amigable y moderna)
        Color azulOscuro = new Color(24, 38, 59);
        Color azulClaro = new Color(41, 128, 185);
        Color blanco = Color.WHITE;
        Color grisTexto = new Color(70, 70, 70);

        // ==========================================
        // PANeL IZQUIERDO: Panel de Bienvenida (Color de fondo y Logo)
        // ==========================================
        panelIzquierdo = new JPanel();
        panelIzquierdo.setBounds(0, 0, 250, 350);
        panelIzquierdo.setBackground(azulOscuro);
        panelIzquierdo.setLayout(null);
        add(panelIzquierdo);

        // Texto de bienvenida en el panel izquierdo
        JLabel lblBienvenida = new JLabel("¡Bienvenido!", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBienvenida.setForeground(blanco);
        lblBienvenida.setBounds(10, 30, 230, 30);
        panelIzquierdo.add(lblBienvenida);

        // Intento de cargar el Logo de la tienda
        lblLogo = new JLabel("", SwingConstants.CENTER);
        lblLogo.setBounds(50, 90, 150, 150);
        
        // Buscamos la imagen en la carpeta del paquete vista
        URL rutaIcono = getClass().getResource("logo.jpg");
        if (rutaIcono != null) {
            ImageIcon miIcono = new ImageIcon(rutaIcono);
            // Redimensionar la imagen para que encaje perfecto en el JLabel de 150x150
            Image imgEscalada = miIcono.getImage().getScaledInstance(lblLogo.getWidth(), lblLogo.getHeight(), Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imgEscalada));
        } else {
            // Si no encuentra la imagen, pone un texto temporal para que no falle
            lblLogo.setText("[ LOGO ]");
            lblLogo.setForeground(blanco);
            lblLogo.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        }
        panelIzquierdo.add(lblLogo);

        // Subtítulo inferior
        JLabel lblEslogan = new JLabel("Sistema de Gestión", SwingConstants.CENTER);
        lblEslogan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEslogan.setForeground(new Color(180, 190, 200));
        lblEslogan.setBounds(10, 260, 230, 20);
        panelIzquierdo.add(lblEslogan);

        // ==========================================
        // PANEL DERECHO: Formulario de Login (Campos limpios)
        // ==========================================
        panelDerecho = new JPanel();
        panelDerecho.setBounds(250, 0, 350, 350);
        panelDerecho.setBackground(blanco);
        panelDerecho.setLayout(null);
        add(panelDerecho);

        // Título "INICIAR SESIÓN"
        lblTitulo = new JLabel("INICIAR SESIÓN");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setBounds(40, 35, 200, 30);
        panelDerecho.add(lblTitulo);

        // Label de Usuario
        lblUsuario = new JLabel("Cédula o Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(grisTexto);
        lblUsuario.setBounds(40, 95, 200, 20);
        panelDerecho.add(lblUsuario);

        // Campo de texto Usuario
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBounds(40, 120, 260, 30);
        // Pequeño truco visual: margen interno para que el texto no esté pegado al borde
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        panelDerecho.add(txtUsuario);

        // Label de Contraseña
        lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblContrasena.setForeground(grisTexto);
        lblContrasena.setBounds(40, 165, 200, 20);
        panelDerecho.add(lblContrasena);

        // Campo de contraseña
        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContrasena.setBounds(40, 190, 260, 30);
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        panelDerecho.add(txtContrasena);

        // Botón Ingresar Estilizado
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setBounds(40, 250, 260, 35);
        btnIngresar.setBackground(azulClaro);
        btnIngresar.setForeground(blanco);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIngresar.setFocusPainted(false); // Quita el molesto cuadro de enfoque interno
        btnIngresar.setBorder(null); // Quita el borde clásico aburrido de Windows
        panelDerecho.add(btnIngresar);

        // Evento del botón
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarAcceso();
            }
        });
    }

    private void validarAcceso() {
        String usuario = txtUsuario.getText();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TrabajadorDAO dao = new TrabajadorDAO();
        Trabajador t = dao.login(usuario, contrasena);

        if (t != null) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido al sistema, " + t.getNombre() + "!");
            this.dispose();
            // Abrimos el Menú Principal y le pasamos el objeto del trabajador logueado
            tienda.vista.FrmMenuPrincipal menu = new tienda.vista.FrmMenuPrincipal(t);
            menu.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}