package tienda.modelo;

public class Cliente {

private int idCliente;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private double limiteCredito;
    private double deuda;

    // Constructores
    public Cliente() {}

    public Cliente(int idCliente, String nombre, String apellido, String telefono, String direccion, double limiteCredito, double deuda) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.limiteCredito = limiteCredito;
        this.deuda = deuda;
    }   

    // Getters y Setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public double getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(double limiteCredito) { this.limiteCredito = limiteCredito; }
    
    public double getDeuda() { return deuda; }
    public void setDeuda(double deuda) { this.deuda = deuda; }
}
