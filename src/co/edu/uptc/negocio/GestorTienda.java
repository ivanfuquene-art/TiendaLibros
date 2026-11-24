package co.edu.uptc.negocio;

import co.edu.uptc.persistencia.LibroDAO;
import co.edu.uptc.persistencia.ClienteDAO;
import java.util.List;

public class GestorTienda {

    private LibroDAO libroDAO;
    private ClienteDAO clienteDAO;

    public GestorTienda() {
        this.libroDAO = new LibroDAO();
        this.clienteDAO = new ClienteDAO();
    }

    //GESTION DE CLIENTES
    public boolean registrarCliente(Cliente nuevoCliente) {
        if (nuevoCliente == null || clienteDAO.existeCorreo(nuevoCliente.getCorreo().trim())) {
            return false;
        }
        return clienteDAO.registrarCliente(nuevoCliente);
    }

    public Cliente iniciarSesionCliente(String correo, String contrasena) {
        if (correo == null || contrasena == null || correo.trim().isEmpty() || contrasena.isEmpty()) {
            return null;
        }
        return clienteDAO.iniciarSesion(correo.trim(), contrasena);
    }

    public boolean actualizarDatosCliente(Cliente cliente) {
        if (cliente == null || cliente.getIdCliente() <= 0) {
            return false;
        }
        return clienteDAO.actualizarDatosCliente(cliente);
    }

    //GESTION DE LIBROS
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

    //CARRITO
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

    public double calcularDescuentoCliente(List<ItemCarrito> carrito, Cliente clienteAutenticado) {
        if (clienteAutenticado == null) {
            return 0.0;
        }
        double totalBruto = calcularSubtotalCarrito(carrito) + calcularImpuestosCarrito(carrito);
        return clienteAutenticado.calcularDescuento(totalBruto);
    }

    public double calcularTotalCompra(List<ItemCarrito> carrito, Cliente clienteAutenticado) {
        double subtotal = calcularSubtotalCarrito(carrito);
        double impuestos = calcularImpuestosCarrito(carrito);
        double totalTodo = subtotal + impuestos;
        double descuento = calcularDescuentoCliente(carrito, clienteAutenticado);
        return totalTodo - descuento;
    }

    public void confirmarCompra(List<ItemCarrito> carrito) throws Exception {
        if (carrito == null || carrito.isEmpty()) {
            throw new Exception("El carrito de compras esta vacio.");
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