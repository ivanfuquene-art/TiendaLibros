package co.edu.uptc.persistencia;

import co.edu.uptc.negocio.Libro;
import co.edu.uptc.negocio.Libro.Formato;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
	
    public boolean registrarLibro(Libro libro) {
        String sql = "INSERT INTO libros (isbn, titulo, autor, anio_publi, categoria, editorial, num_pag, precio, stock, formato, porcentaje_iva) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, libro.getIsbn());
            pstmt.setString(2, libro.getTitulo());
            pstmt.setString(3, libro.getAutor());
            pstmt.setInt(4, libro.getAnioPubli());
            pstmt.setString(5, libro.getCategoria());
            pstmt.setString(6, libro.getEditorial());
            pstmt.setInt(7, libro.getNumPag());
            pstmt.setDouble(8, libro.getPrecio());
            pstmt.setInt(9, libro.getStock());
            pstmt.setString(10, libro.getFormato().name());
            pstmt.setInt(11, libro.getPorcentajeIva());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar libro: " + e.getMessage());
            return false;
        }
    }

    public List<Libro> listarLibros() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement estado = conn.createStatement();
             ResultSet datosLibrosBD = estado.executeQuery(sql)) {

            while (datosLibrosBD.next()) {
                Libro libro = new Libro(
                    datosLibrosBD.getString("isbn"),
                    datosLibrosBD.getString("titulo"),
                    datosLibrosBD.getString("autor"),
                    datosLibrosBD.getInt("anio_publi"),
                    datosLibrosBD.getString("categoria"),
                    datosLibrosBD.getString("editorial"),
                    datosLibrosBD.getInt("num_pag"),
                    datosLibrosBD.getDouble("precio"),
                    datosLibrosBD.getInt("stock"),
                    Formato.valueOf(datosLibrosBD.getString("formato")),
                    datosLibrosBD.getInt("porcentaje_iva")
                );
                lista.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarLibro(Libro libro) {
        String sql = "UPDATE libros SET titulo=?, autor=?, anio_publi=?, categoria=?, editorial=?, num_pag=?, precio=?, stock=?, formato=?, porcentaje_iva=? WHERE isbn=?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement actualizarLibroBD = conn.prepareStatement(sql)) {
            
            actualizarLibroBD.setString(1, libro.getTitulo());
            actualizarLibroBD.setString(2, libro.getAutor());
            actualizarLibroBD.setInt(3, libro.getAnioPubli());
            actualizarLibroBD.setString(4, libro.getCategoria());
            actualizarLibroBD.setString(5, libro.getEditorial());
            actualizarLibroBD.setInt(6, libro.getNumPag());
            actualizarLibroBD.setDouble(7, libro.getPrecio());
            actualizarLibroBD.setInt(8, libro.getStock());
            actualizarLibroBD.setString(9, libro.getFormato().name());
            actualizarLibroBD.setInt(10, libro.getPorcentajeIva());
            actualizarLibroBD.setString(11, libro.getIsbn()); // Condición WHERE

            return actualizarLibroBD.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }


    public boolean actualizarStock(String isbn, int nuevoStock) {
        String sql = "UPDATE libros SET stock = ? WHERE isbn = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement actualizarStockBD = conn.prepareStatement(sql)) {
            
            actualizarStockBD.setInt(1, nuevoStock);
            actualizarStockBD.setString(2, isbn);
            return actualizarStockBD.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    
    public boolean eliminarLibro(String isbn) {
        String sql = "DELETE FROM libros WHERE isbn = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement borrarLibroBD = conn.prepareStatement(sql)) {
            
            borrarLibroBD.setString(1, isbn);
            return borrarLibroBD.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro (tiene ventas asociadas): " + e.getMessage());
            return false;
        }
    }
}