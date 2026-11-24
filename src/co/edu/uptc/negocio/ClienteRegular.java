package co.edu.uptc.negocio;

public class ClienteRegular extends Cliente {

    public ClienteRegular(int idCliente, String nombre, String apellido, String correo, 
                          String direccion, String telefono, String contrasena) {
        super(idCliente, nombre, apellido, correo, direccion, telefono, TipoCliente.REGULAR, contrasena);
    }

    @Override
    public double calcularDescuento(double totalCompra) {
        return 0.0;
    }
}