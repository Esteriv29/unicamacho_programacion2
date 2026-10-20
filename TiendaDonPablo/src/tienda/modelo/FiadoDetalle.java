package tienda.modelo;

public class FiadoDetalle {

private int idVenta;
    private String fecha;
    private String cliente;
    private String producto;
    private int cantidad;
    private double subtotal;

    public FiadoDetalle(int idVenta, String fecha, String cliente, String producto, int cantidad, double subtotal) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    // Getters
    public int getIdVenta() { return idVenta; }
    public String getFecha() { return fecha; }
    public String getCliente() { return cliente; }
    public String getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return subtotal; }
}
