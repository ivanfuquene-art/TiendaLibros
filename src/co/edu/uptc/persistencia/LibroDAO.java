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
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Libro libro = new Libro(
                    rs.getString("isbn"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getInt("anio_publi"),
                    rs.getString("categoria"),
                    rs.getString("editorial"),
                    rs.getInt("num_pag"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    Formato.valueOf(rs.getString("formato")),
                    rs.getInt("porcentaje_iva")
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
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setInt(3, libro.getAnioPubli());
            pstmt.setString(4, libro.getCategoria());
            pstmt.setString(5, libro.getEditorial());
            pstmt.setInt(6, libro.getNumPag());
            pstmt.setDouble(7, libro.getPrecio());
            pstmt.setInt(8, libro.getStock());
            pstmt.setString(9, libro.getFormato().name());
            pstmt.setInt(10, libro.getPorcentajeIva());
            pstmt.setString(11, libro.getIsbn()); // Condición WHERE

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }


    public boolean actualizarStock(String isbn, int nuevoStock) {
        String sql = "UPDATE libros SET stock = ? WHERE isbn = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, nuevoStock);
            pstmt.setString(2, isbn);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    
    public boolean eliminarLibro(String isbn) {
        String sql = "DELETE FROM libros WHERE isbn = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, isbn);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro (tiene ventas asociadas): " + e.getMessage());
            return false;
        }
    }
}