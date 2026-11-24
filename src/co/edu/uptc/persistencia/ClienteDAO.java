package co.edu.uptc.persistencia;

import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.ClientePremium;
import co.edu.uptc.negocio.ClienteRegular;
import co.edu.uptc.negocio.TipoCliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {

    public boolean registrarCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (nombre, apellido, correo, direccion, telefono, tipo_cliente, contrasena) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getTipoCliente().name());
            ps.setString(7, cliente.getContrasena());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
            return false;
        }
    }

    public Cliente iniciarSesion(String correo, String contrasena) {
        String sql = "SELECT * FROM cliente WHERE correo = ? AND contrasena = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarDatosCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET nombre = ?, apellido = ?, correo = ?, "
                   + "direccion = ?, telefono = ?, tipo_cliente = ?, contrasena = ? WHERE idCliente = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getTipoCliente().name());
            ps.setString(7, cliente.getContrasena());
            ps.setInt(8, cliente.getIdCliente());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar datos del cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean existeCorreo(String correo) {
        String sql = "SELECT idCliente FROM cliente WHERE correo = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
            return false;
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        int id = rs.getInt("idCliente");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String correo = rs.getString("correo");
        String direccion = rs.getString("direccion");
        String telefono = rs.getString("telefono");
        TipoCliente tipo = TipoCliente.valueOf(rs.getString("tipo_cliente"));
        String contrasena = rs.getString("contrasena");

        if (tipo == TipoCliente.PREMIUM) {
            return new ClientePremium(id, nombre, apellido, correo, direccion, telefono, contrasena);
        } else {
            return new ClienteRegular(id, nombre, apellido, correo, direccion, telefono, contrasena);
        }
    }
}