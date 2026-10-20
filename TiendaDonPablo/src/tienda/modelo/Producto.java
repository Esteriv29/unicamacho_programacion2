package tienda.modelo;

public class Producto {

    private int idProducto;
    private String descripcion;
    private double precio;
    private int stock;

    // Constructores
    public Producto() {}

    public Producto(int idProducto, String descripcion, double precio, int stock) {
        this.idProducto = idProducto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    // Este método le dice a Java cómo mostrar el producto en listas desplegables
    @Override
    public String toString() {
        return this.descripcion + " ($ " + new java.text.DecimalFormat("#,###").format(this.precio) + ")";
    }
}