package co.edu.uptc.negocio;

public class ItemCarrito {
    private Libro libro;
    private int cantidad;

    public ItemCarrito(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
    }

       
    public Libro getLibro() {
		return libro;
	}



	public void setLibro(Libro libro) {
		this.libro = libro;
	}



	public int getCantidad() {
		return cantidad;
	}



	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}



	public void aumentarCantidad() {
        this.cantidad++;
    }

    public void disminuirCantidad() {
        if (this.cantidad > 1) {
            this.cantidad--;
        }
    }

    public double calcularSubtotal() {
        return libro.getPrecio() * cantidad;
    }

    public double calcularImpuesto() {
        return calcularSubtotal() * (libro.getPorcentajeIva() / 100.0);
    }
    

}