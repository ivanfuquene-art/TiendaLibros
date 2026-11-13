package co.edu.uptc.negocio;

public class ClientePremium extends Cliente {
    
    private double porcentajeDescuento;

    public ClientePremium(String nombreCompleto, String correo, String direccion, String telefono, double porcentajeDescuento) {
        super(nombreCompleto, correo, direccion, telefono);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public double calcularDescuento(double totalCompra) {
        return totalCompra * porcentajeDescuento;
    }
}