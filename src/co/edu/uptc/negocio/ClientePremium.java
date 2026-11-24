package co.edu.uptc.negocio;

public class ClientePremium extends Cliente {

    private double porcentajeDescuento;

    public ClientePremium(int idCliente, String nombre, String apellido, String correo, 
                          String direccion, String telefono, String contrasena, double porcentajeDescuento) {
        super(idCliente, nombre, apellido, correo, direccion, telefono, TipoCliente.PREMIUM, contrasena);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public ClientePremium(int idCliente, String nombre, String apellido, String correo, 
                          String direccion, String telefono, String contrasena) {
        this(idCliente, nombre, apellido, correo, direccion, telefono, contrasena, 0.15);
    }

    @Override
    public double calcularDescuento(double totalCompra) {
        return totalCompra * porcentajeDescuento;
    }

    public String obtenerBeneficios() {
        return "15% de descuento + envio sin costo adicional";
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
}