package co.edu.uptc.gui;

import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.ClientePremium;
import co.edu.uptc.negocio.ClienteRegular;
import co.edu.uptc.negocio.GestorTienda;
import co.edu.uptc.negocio.TipoCliente;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private GestorTienda gestor;
    private JComboBox<String> cmbRol;
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JButton btnRegistrar;

    public VentanaLogin() {
        this(new GestorTienda());
    }

    public VentanaLogin(GestorTienda gestor) {
        this.gestor = gestor;
        setTitle("Tienda Virtual de Libros - Inicio de Sesión");
        setSize(400, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Bienvenido a la Tienda de Libros", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 8, 10));
        panelCampos.add(new JLabel("Ingresar como:"));
        cmbRol = new JComboBox<>(new String[]{"Cliente", "Administrador"});
        panelCampos.add(cmbRol);

        panelCampos.add(new JLabel("Correo / Usuario:"));
        txtCorreo = new JTextField();
        panelCampos.add(txtCorreo);

        panelCampos.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        panelCampos.add(txtContrasena);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnIngresar = new JButton("Iniciar Sesión");
        btnRegistrar = new JButton("Registrarse");
        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal);

        btnIngresar.addActionListener(e -> autenticar());
        btnRegistrar.addActionListener(e -> mostrarDialogoRegistro());
    }

    private void autenticar() {
        String rol = (String) cmbRol.getSelectedItem();
        String correo = txtCorreo.getText().trim();
        String pass = new String(txtContrasena.getPassword());

        if (correo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Administrador".equals(rol)) {
            if (correo.equals("admin") && pass.equals("admin123")) {
                new VentanaAdministrador(gestor).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales de administrador incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            Cliente cliente = gestor.iniciarSesionCliente(correo, pass);
            if (cliente != null) {
                new VentanaCliente(gestor, cliente).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoRegistro() {
        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtNuevoCorreo = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtTelefono = new JTextField();
        JPasswordField txtNuevaPass = new JPasswordField();
        JComboBox<TipoCliente> cmbTipo = new JComboBox<>(TipoCliente.values());

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtApellido);
        panel.add(new JLabel("Correo:"));
        panel.add(txtNuevoCorreo);
        panel.add(new JLabel("Dirección de envío:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Tipo de Cliente:"));
        panel.add(cmbTipo);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtNuevaPass);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Registro de Nuevo Cliente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String correo = txtNuevoCorreo.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String pass = new String(txtNuevaPass.getPassword());
            TipoCliente tipo = (TipoCliente) cmbTipo.getSelectedItem();

            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente nuevo;
            if (tipo == TipoCliente.PREMIUM) {
                nuevo = new ClientePremium(0, nombre, apellido, correo, direccion, telefono, pass);
            } else {
                nuevo = new ClienteRegular(0, nombre, apellido, correo, direccion, telefono, pass);
            }

            if (gestor.registrarCliente(nuevo)) {
                JOptionPane.showMessageDialog(this, "Registro exitoso. Ya puede iniciar sesión.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar. Verifique si el correo ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new VentanaLogin().setVisible(true);
    }
}