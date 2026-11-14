package co.edu.uptc.negocio;

import co.edu.uptc.persistencia.LibroDAO;
import java.util.ArrayList;
import java.util.List;

public class GestorTienda {

    private LibroDAO libroDAO;
    private List<Cliente> clientes;

    public GestorTienda() {
        this.libroDAO = new LibroDAO();
        this.clientes = new ArrayList<>();
        cargarUsuariosPorDefecto();
    }


    private void cargarUsuariosPorDefecto() {
        clientes.add(new ClienteRegular("Juan Pérez", "regular@tienda.com", "Calle 10 # 15-20", "3001234567"));
        clientes.add(new ClientePremium("Ana Gómez", "premium@tienda.com", "Carrera 12 # 20-30", "3109876543", 0.10));
    }

    public boolean registrarCliente(Cliente nuevoCliente) {
        if (autenticarCliente(nuevoCliente.getCorreo()) != null) {
            return false;
        }
        clientes.add(nuevoCliente);
        return true;
    }

    public Cliente autenticarCliente(String correo) {
        for (Cliente c : clientes) {
            if (c.getCorreo().equalsIgnoreCase(correo.trim())) {
                return c;
            }
        }
        return null;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }


    public boolean registrarLibro(Libro libro) {
        return libroDAO.registrarLibro(libro);
    }

    public List<Libro> getCatalogo() {
        return libroDAO.listarLibros();
    }

    public Libro buscarLibroPorIsbn(String isbn) {
        for (Libro l : libroDAO.listarLibros()) {
            if (l.getIsbn().equalsIgnoreCase(isbn.trim())) {
                return l;
            }
        }
        return null;
    }

    public boolean actualizarLibro(Libro libro) {
        return libroDAO.actualizarLibro(libro);
    }

    public boolean eliminarLibro(String isbn) {
        return libroDAO.eliminarLibro(isbn);
    }


    public boolean validarStock(Libro libro, int cantidadSolicitada) {
        return libro != null && cantidadSolicitada > 0 && libro.getStock() >= cantidadSolicitada;
    }

    public double calcularSubtotalCarrito(List<ItemCarrito> carrito) {
        double subtotal = 0.0;
        for (ItemCarrito item : carrito) {
            subtotal += item.calcularSubtotal();
        }
        return subtotal;
    }

    public double calcularImpuestosCarrito(List<ItemCarrito> carrito) {
        double impuestos = 0.0;
        for (ItemCarrito item : carrito) {
            impuestos += item.calcularImpuesto();
        }
        return impuestos;
    }

    public double calcularTotalCompra(List<ItemCarrito> carrito, Cliente clienteAutenticado) {
        double subtotal = calcularSubtotalCarrito(carrito);
        double impuestos = calcularImpuestosCarrito(carrito);
        double totalBruto = subtotal + impuestos;
        double descuento = (clienteAutenticado != null) ? clienteAutenticado.calcularDescuento(totalBruto) : 0.0;
        return totalBruto - descuento;
    }

    public void confirmarCompra(List<ItemCarrito> carrito) throws Exception {
        if (carrito == null || carrito.isEmpty()) {
            throw new Exception("El carrito de compras está vacío.");
        }
        for (ItemCarrito item : carrito) {
            Libro libroBD = buscarLibroPorIsbn(item.getLibro().getIsbn());
            if (!validarStock(libroBD, item.getCantidad())) {
                throw new Exception("Stock insuficiente para el libro: " + item.getLibro().getTitulo());
            }
        }
        for (ItemCarrito item : carrito) {
            Libro libroBD = buscarLibroPorIsbn(item.getLibro().getIsbn());
            int nuevoStock = libroBD.getStock() - item.getCantidad();
            
            libroBD.setStock(nuevoStock);
            boolean actualizado = libroDAO.actualizarStock(libroBD.getIsbn(), nuevoStock);
            
            if (!actualizado) {
                throw new Exception("Error al actualizar el inventario en la base de datos para ISBN: " + libroBD.getIsbn());
            }
        }
    }

    public boolean actualizarStockDirecto(String isbn, int nuevoStock) {
        if (nuevoStock < 0) {
            return false;
        }
        return libroDAO.actualizarStock(isbn, nuevoStock);
    }
}