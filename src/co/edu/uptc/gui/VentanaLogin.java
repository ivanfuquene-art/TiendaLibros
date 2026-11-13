package co.edu.uptc.gui;

import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.ClientePremium;
import co.edu.uptc.negocio.ClienteRegular;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaLogin extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;

    public VentanaLogin() {
        setTitle("Autenticación - Tienda de Libros");
        setSize(350, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 15));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelFormulario.add(new JLabel("Correo Electrónico:"));
        txtCorreo = new JTextField();
        panelFormulario.add(txtCorreo);

        panelFormulario.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        panelFormulario.add(txtContrasena);

        JPanel panelBotones = new JPanel();
        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnRegistrarse = new JButton("Registrarse");

        panelBotones.add(btnIniciarSesion);
        panelBotones.add(btnRegistrarse);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarCredenciales();
            }
        });
    }

    private void validarCredenciales() {
        String correo = txtCorreo.getText();
        String contrasena = new String(txtContrasena.getPassword());

        if (correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese correo y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        } if (correo.equals("admin@tienda.com") && contrasena.equals("admin123")) {
            VentanaAdministrador adminView = new VentanaAdministrador();
            adminView.setVisible(true);
            this.dispose();             
        } else if (correo.equals("regular@tienda.com") && contrasena.equals("1234")) {
            Cliente cliente = new ClienteRegular("Ivan Fuquene", correo, "Calle 10", "3008293901");
            new VentanaCliente(cliente).setVisible(true);
            this.dispose();            
        } else if (correo.equals("premium@tienda.com") && contrasena.equals("9876")) {
            Cliente cliente = new ClientePremium("Miguel Prieto", correo, "Cra 15", "3119203991", 0.10);
            new VentanaCliente(cliente).setVisible(true);
            this.dispose();            
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaLogin login = new VentanaLogin();
            login.setVisible(true);
        });
    }
}