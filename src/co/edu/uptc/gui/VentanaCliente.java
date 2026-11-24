package co.edu.uptc.gui;

import co.edu.uptc.negocio.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaCliente extends JFrame {

    private GestorTienda gestor;
    private Cliente clienteActual;
    private List<ItemCarrito> carrito;

    private JLabel lblInfoCliente;
    private PanelTabla panelTablaCatalogo;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JLabel lblResumenTotales;

    public VentanaCliente(GestorTienda gestor, Cliente clienteActual) {
        this.gestor = gestor;
        this.clienteActual = clienteActual;
        this.carrito = new ArrayList<>();

        setTitle("Tienda Virtual - Catálogo y Compras");
        setSize(950, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
        cargarCatalogo();
        actualizarTablaCarrito();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Panel Superior en 2 filas para que el texto largo no se monte sobre los botones
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1, 5, 5));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        lblInfoCliente = new JLabel();
        actualizarEtiquetaCliente();
        panelSuperior.add(lblInfoCliente);

        JPanel panelBotonesCuenta = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnActualizarDatos = new JButton("Actualizar Mis Datos");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        panelBotonesCuenta.add(btnActualizarDatos);
        panelBotonesCuenta.add(btnCerrarSesion);
        panelSuperior.add(panelBotonesCuenta);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel Central: Catálogo con tu PanelTabla (Izquierda) y Carrito (Derecha)
        JSplitPane splitCentral = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitCentral.setResizeWeight(0.58);

        // Catálogo reutilizando PanelTabla
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelTablaCatalogo = new PanelTabla();
        panelIzquierdo.add(panelTablaCatalogo, BorderLayout.CENTER);

        JButton btnAgregarCarrito = new JButton("Agregar al Carrito");
        panelIzquierdo.add(btnAgregarCarrito, BorderLayout.SOUTH);

        // Carrito
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Carrito de Compras"));
        modeloCarrito = new DefaultTableModel(new String[]{"ISBN", "Título", "Cant.", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCarrito = new JTable(modeloCarrito);
        panelDerecho.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel panelSurCarrito = new JPanel(new BorderLayout(5, 5));
        lblResumenTotales = new JLabel("Total: $0.0");
        lblResumenTotales.setFont(new Font("Arial", Font.BOLD, 12));
        panelSurCarrito.add(lblResumenTotales, BorderLayout.NORTH);

        JPanel panelBotonesCarrito = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnQuitar = new JButton("Quitar Item");
        JButton btnComprar = new JButton("Confirmar Compra");
        panelBotonesCarrito.add(btnQuitar);
        panelBotonesCarrito.add(btnComprar);
        panelSurCarrito.add(panelBotonesCarrito, BorderLayout.SOUTH);

        panelDerecho.add(panelSurCarrito, BorderLayout.SOUTH);

        splitCentral.setLeftComponent(panelIzquierdo);
        splitCentral.setRightComponent(panelDerecho);
        add(splitCentral, BorderLayout.CENTER);

        // Eventos
        btnAgregarCarrito.addActionListener(e -> agregarLibroAlCarrito());
        btnQuitar.addActionListener(e -> quitarDelCarrito());
        btnComprar.addActionListener(e -> procesarCompra());
        btnActualizarDatos.addActionListener(e -> mostrarDialogoActualizarDatos());
        btnCerrarSesion.addActionListener(e -> {
            new VentanaLogin(gestor).setVisible(true);
            dispose();
        });
    }

    private void actualizarEtiquetaCliente() {
        String texto = "Cliente: " + clienteActual.getNombreCompleto() + " | Tipo: " + clienteActual.getTipoCliente();
        if (clienteActual instanceof ClientePremium) {
            texto += " (15% Descuento Exclusivo + Envío Gratis)";
        }
        lblInfoCliente.setText(texto);
    }

    private void cargarCatalogo() {
        panelTablaCatalogo.limpiarTabla();
        for (Libro l : gestor.getCatalogo()) {
            panelTablaCatalogo.agregarLibroTabla(l);
        }
    }

    private void agregarLibroAlCarrito() {
        String isbn = panelTablaCatalogo.getIsbnSeleccionado();
        if (isbn == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro del catálogo.");
            return;
        }

        Libro libro = gestor.buscarLibroPorIsbn(isbn);

        String cantStr = JOptionPane.showInputDialog(this, "Cantidad a llevar:", "1");
        if (cantStr == null) return;

        try {
            int cantidad = Integer.parseInt(cantStr.trim());
            if (!gestor.validarStock(libro, cantidad)) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida o sin stock suficiente.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (ItemCarrito item : carrito) {
                if (item.getLibro().getIsbn().equals(isbn)) {
                    int nuevaCant = item.getCantidad() + cantidad;
                    if (!gestor.validarStock(libro, nuevaCant)) {
                        JOptionPane.showMessageDialog(this, "Supera el stock disponible.", "Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    item.setCantidad(nuevaCant);
                    actualizarTablaCarrito();
                    return;
                }
            }

            carrito.add(new ItemCarrito(libro, cantidad));
            actualizarTablaCarrito();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
        }
    }

    private void quitarDelCarrito() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila != -1) {
            carrito.remove(fila);
            actualizarTablaCarrito();
        }
    }

    private void actualizarTablaCarrito() {
        modeloCarrito.setRowCount(0);
        for (ItemCarrito item : carrito) {
            modeloCarrito.addRow(new Object[]{
                item.getLibro().getIsbn(),
                item.getLibro().getTitulo(),
                item.getCantidad(),
                String.format("$%.2f", item.calcularSubtotal())
            });
        }

        double subtotal = gestor.calcularSubtotalCarrito(carrito);
        double iva = gestor.calcularImpuestosCarrito(carrito);
        double descuento = gestor.calcularDescuentoCliente(carrito, clienteActual);
        double total = gestor.calcularTotalCompra(carrito, clienteActual);

        lblResumenTotales.setText(String.format(
            "Subtotal: $%.2f | IVA: $%.2f | Descuento: -$%.2f | TOTAL: $%.2f",
            subtotal, iva, descuento, total
        ));
    }

    private void procesarCompra() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.");
            return;
        }

        try {
            double subtotal = gestor.calcularSubtotalCarrito(carrito);
            double iva = gestor.calcularImpuestosCarrito(carrito);
            double descuento = gestor.calcularDescuentoCliente(carrito, clienteActual);
            double total = gestor.calcularTotalCompra(carrito, clienteActual);

            List<ItemCarrito> itemsComprados = new ArrayList<>(carrito);

            gestor.confirmarCompra(carrito);

            new VentanaRecibo(this, clienteActual, itemsComprados, subtotal, iva, descuento, total).setVisible(true);

            carrito.clear();
            actualizarTablaCarrito();
            cargarCatalogo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en la compra", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoActualizarDatos() {
        JTextField txtNombre = new JTextField(clienteActual.getNombre());
        JTextField txtApellido = new JTextField(clienteActual.getApellido());
        JTextField txtCorreo = new JTextField(clienteActual.getCorreo());
        JTextField txtDireccion = new JTextField(clienteActual.getDireccion());
        JTextField txtTelefono = new JTextField(clienteActual.getTelefono());
        JPasswordField txtPass = new JPasswordField(clienteActual.getContrasena());

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtApellido);
        panel.add(new JLabel("Correo:"));
        panel.add(txtCorreo);
        panel.add(new JLabel("Dirección de envío:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPass);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Actualizar Datos Personales",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            clienteActual.setNombre(txtNombre.getText().trim());
            clienteActual.setApellido(txtApellido.getText().trim());
            clienteActual.setCorreo(txtCorreo.getText().trim());
            clienteActual.setDireccion(txtDireccion.getText().trim());
            clienteActual.setTelefono(txtTelefono.getText().trim());
            clienteActual.setContrasena(new String(txtPass.getPassword()));

            if (gestor.actualizarDatosCliente(clienteActual)) {
                actualizarEtiquetaCliente();
                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente en la base de datos.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}