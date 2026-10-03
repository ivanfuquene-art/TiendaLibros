package co.edu.uptc.negocio;

public class Libro {
	private String isbn;
	private String titulo;
	private String autor;
	private int anioPubli;
	private String categoria;
	private String editorial;
	private int numPag;
	private double precio;
	private int stock;
	private String formato;
	private int porcentajeIva;
	
	public Libro(String isbn, String titulo, String autor, int anioPubli, String categoria, String editorial,
			int numPag, double precio, int stock, String formato, int porcentajeIva) {
		super();
		this.isbn = isbn;
		this.titulo = titulo;
		this.autor = autor;
		this.anioPubli = anioPubli;
		this.categoria = categoria;
		this.editorial = editorial;
		this.numPag = numPag;
		this.precio = precio;
		this.stock = stock;
		this.formato = formato;	
		}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public int getAnioPubli() {
		return anioPubli;
	}

	public void setAnioPubli(int anioPubli) {
		this.anioPubli = anioPubli;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getEditorial() {
		return editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public int getNumPag() {
		return numPag;
	}

	public void setNumPag(int numPag) {
		this.numPag = numPag;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public int getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(int porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}
		
}
