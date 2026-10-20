package tienda.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conectar {

// Parámetros de configuración de la base de datos
    private static final String DATABASE = "tienda_don_pablo";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE + "?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = "admin"; 

    // Método estático para obtener la conexión
    public static Connection getConexion() {
        Connection conexion = null;
        try {
            // Registrar el driver de MySQL (opcional en versiones modernas, pero buena práctica)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer la conexión
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión exitosa a la base de datos de Don Pablo!");
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos.");
            e.printStackTrace();
        }
        return conexion;
    }

    // Método de prueba rápido
    public static void main(String[] args) {
        getConexion();
    }
}