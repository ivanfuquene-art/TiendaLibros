package co.edu.uptc.negocio;

public enum TipoCliente {
    REGULAR(0.0),
    PREMIUM(0.15);

    private final double porcentajeDescuento;

    TipoCliente(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
}