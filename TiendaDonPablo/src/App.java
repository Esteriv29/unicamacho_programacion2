import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import tienda.vista.FrmLogin;


public class App {
    public static void main(String[] args) throws Exception {

        // Opcional: Cambiar el aspecto visual (Look and Feel) al del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo establecer el Look and Feel del sistema.");
        }

        // Arrancar la interfaz gráfica del Login en un hilo seguro
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Creamos la instancia del Login y la hacemos visible
                FrmLogin login = new FrmLogin();
                login.setVisible(true);
            }
        });
    }
}