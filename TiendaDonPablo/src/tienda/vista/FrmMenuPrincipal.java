package tienda.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import tienda.modelo.Trabajador;

public class FrmMenuPrincipal extends JFrame {

    private Trabajador usuarioLogueado;
    private JPanel panelLateral, panelCentral;
    private JLabel lblUsuario, lblTituloSistema;
    private JButton btnVentas, btnClientes, btnProductos, btnAbonos, btnHistorial, btnCerrarSesion;

    public FrmMenuPrincipal(Trabajador trabajador) {
        this.usuarioLogueado = trabajador;

        // Configuración de la ventana principal
        setTitle("Sistema de Gestión - Tienda Don Pablo");
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Colores institucionales
        Color azulOscuro = new Color(24, 38, 59);
        Color azulClaro = new Color(41, 128, 185);
        Color grisFondo = new Color(245, 246, 250);
        Color blanco = Color.WHITE;

        // PANEL LATERAL
        panelLateral = new JPanel();
        panelLateral.setBackground(azulOscuro);
        panelLateral.setPreferredSize(new Dimension(250, 650));
        panelLateral.setLayout(null);
        add(panelLateral, BorderLayout.WEST);

        // Título del sistema
        lblTituloSistema = new JLabel("TIENDA DON PABLO", SwingConstants.CENTER);
        lblTituloSistema.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloSistema.setForeground(blanco);
        lblTituloSistema.setBounds(10, 30, 230, 30);
        panelLateral.add(lblTituloSistema);

        // Información del usuario y Rol
        String rolTexto = (usuarioLogueado != null && usuarioLogueado.getRol() != null) ? usuarioLogueado.getRol() : "Usuario";
        String nombreTexto = (usuarioLogueado != null) ? usuarioLogueado.getNombre() : "Don Pablo";
        
        lblUsuario = new JLabel("<html>" + rolTexto + ": <br><b>" + nombreTexto + "</b></html>");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setForeground(new Color(200, 214, 229));
        lblUsuario.setBounds(20, 75, 210, 45);
        panelLateral.add(lblUsuario);

        // --- DIMENSIONES DE LOS BOTONES ---
        int botonAncho = 215;
        int botonAlto = 50;  
        int xPos = 18;

        btnVentas = crearBotónEstilizado("Nueva Venta", "venta.png", xPos, 140, botonAncho, botonAlto, azulClaro, blanco);
        panelLateral.add(btnVentas);

        btnClientes = crearBotónEstilizado("Clientes (Libreta)", "clientes.png", xPos, 205, botonAncho, botonAlto, azulClaro, blanco);
        panelLateral.add(btnClientes);

        btnProductos = crearBotónEstilizado("Inventario", "productos.png", xPos, 270, botonAncho, botonAlto, azulClaro, blanco);
        panelLateral.add(btnProductos);

        btnAbonos = crearBotónEstilizado("Registrar Abono", "abonos.png", xPos, 335, botonAncho, botonAlto, azulClaro, blanco);
        panelLateral.add(btnAbonos);

        btnHistorial = crearBotónEstilizado("Historial Fiados", "abonos.png", xPos, 400, botonAncho, botonAlto, azulClaro, blanco);
        panelLateral.add(btnHistorial);

        btnCerrarSesion = crearBotónEstilizado("Cerrar Sesión", null, xPos, 520, botonAncho, botonAlto, new Color(192, 57, 43), blanco);
        panelLateral.add(btnCerrarSesion);

        // PANEL CENTRAL
        panelCentral = new JPanel();
        panelCentral.setBackground(grisFondo);
        panelCentral.setLayout(new BorderLayout());
        add(panelCentral, BorderLayout.CENTER);

        JLabel lblBienvenidaCentrada = new JLabel("Seleccione una opción del menú de la izquierda para comenzar.", SwingConstants.CENTER);
        lblBienvenidaCentrada.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblBienvenidaCentrada.setForeground(Color.GRAY);
        panelCentral.add(lblBienvenidaCentrada, BorderLayout.CENTER);

        // --- ACCIONES DE LOS BOTONES ---
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new FrmLogin().setVisible(true);
        });

        btnClientes.addActionListener(e -> mostrarPanelCentral(new PnlClientes()));
        btnProductos.addActionListener(e -> mostrarPanelCentral(new PnlProductos()));
        btnVentas.addActionListener(e -> mostrarPanelCentral(new PnlVentas()));
        btnAbonos.addActionListener(e -> mostrarPanelCentral(new PnlAbonos()));
        btnHistorial.addActionListener(e -> mostrarPanelCentral(new PnlHistorialFiados()));
    }

    // Método auxiliar para intercambiar los paneles centrales limpiando el foco
    private void mostrarPanelCentral(JPanel nuevoPanel) {
        panelCentral.removeAll();
        panelCentral.add(nuevoPanel, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private JButton crearBotónEstilizado(String texto, String nombreImagen, int x, int y, int ancho, int alto, Color fondo, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, ancho, alto);
        
        boton.setBackground(fondo);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setForeground(textoColor);
        
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15)); 
        boton.setFocusPainted(false);
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setIconTextGap(18); 
        boton.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        if (nombreImagen != null) {
            try {
                // Búsqueda con fallback de rutas relativas y absolutas dentro de los recursos
                java.net.URL rutaIcono = getClass().getResource(nombreImagen);
                if (rutaIcono == null) {
                    rutaIcono = getClass().getResource("/tienda/vista/" + nombreImagen);
                }
                
                if (rutaIcono != null) {
                    ImageIcon icon = new ImageIcon(rutaIcono);
                    Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    boton.setIcon(new ImageIcon(img));
                }
            } catch (Exception e) {
                System.out.println("No se pudo cargar la imagen del botón: " + nombreImagen);
            }
        }
        
        return boton;
    }
}