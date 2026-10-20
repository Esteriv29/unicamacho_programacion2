package tienda.modelo;

public class Trabajador {

    private int idTrabajador;
    private String cedula;
    private String nombre;
    private String telefono;
    private String rol;
    private String contrasena;

    // Constructor vacío
    public Trabajador() {}

    // Constructor con datos principales
    public Trabajador(int idTrabajador, String nombre, String rol) {
        this.idTrabajador = idTrabajador;
        this.nombre = nombre;
        this.rol = rol;
    }

    // Getters y Setters
    public int getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(int idTrabajador) { this.idTrabajador = idTrabajador; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}

