package co.edu.uptc.negocio;

public class ClienteRegular extends Cliente {

    public ClienteRegular(String nombreCompleto, String correo, String direccion, String telefono) {
        super(nombreCompleto, correo, direccion, telefono);
    }

    @Override
    public double calcularDescuento(double totalCompra) {
        return 0.0; 
    }
}