package co.edu.uptc.gui;

import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.ClientePremium;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaCliente extends JFrame {

    private Cliente clienteActual;
    private JTable tablaCatalogo;
    private DefaultTableModel modeloCatalogo;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JLabel lblDescuento;
    private JLabel lblTotalPagar;

    public VentanaCliente(Cliente cliente) {
        this.clienteActual = cliente;
        
        String tipoCliente = (cliente instanceof ClientePremium) ? "PREMIUM" : "REGULAR";
        setTitle("Tienda Virtual - Bienvenido " + cliente.getNombreCompleto() + " [" + tipoCliente + "]");
        
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelCatalogo = new JPanel(new BorderLayout());
        panelCatalogo.setBorder(BorderFactory.createTitledBorder("Catálogo de Libros Disponibles"));
        

        String[] columnasCatalogo = {"ISBN", "Título", "Autor", "Precio", "Stock"};
        modeloCatalogo = new DefaultTableModel(columnasCatalogo, 0);
        tablaCatalogo = new JTable(modeloCatalogo);
        
        modeloCatalogo.addRow(new Object[]{"001", "Cien Años de Soledad", "G. García Márquez", 50000.0, 10});
        modeloCatalogo.addRow(new Object[]{"002", "Clean Code", "Robert C. Martin", 85000.0, 5});
        
        panelCatalogo.add(new JScrollPane(tablaCatalogo), BorderLayout.CENTER);
        
        JButton btnAgregarCarrito = new JButton("Agregar al Carrito");
        JPanel panelBtnCatalogo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtnCatalogo.add(btnAgregarCarrito);
        panelCatalogo.add(panelBtnCatalogo, BorderLayout.SOUTH);


        JPanel panelCarrito = new JPanel(new BorderLayout());
        panelCarrito.setBorder(BorderFactory.createTitledBorder("Tu Carrito de Compras"));
        
        String[] columnasCarrito = {"ISBN", "Título", "Cantidad", "Subtotal"};
        modeloCarrito = new DefaultTableModel(columnasCarrito, 0);
        tablaCarrito = new JTable(modeloCarrito);
        
        panelCarrito.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        
        JPanel panelCheckout = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        
        lblDescuento = new JLabel("Descuento: $0.0");
        lblDescuento.setForeground(new Color(0, 128, 0));
        
        lblTotalPagar = new JLabel("Total a Pagar: $0.0");
        lblTotalPagar.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton btnFinalizarCompra = new JButton("Finalizar Compra");

        panelCheckout.add(lblDescuento);
        panelCheckout.add(lblTotalPagar);
        panelCheckout.add(btnFinalizarCompra);
        panelCarrito.add(panelCheckout, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelCatalogo, panelCarrito);
        splitPane.setDividerLocation(250);
        add(splitPane, BorderLayout.CENTER);

        btnAgregarCarrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarAlCarrito();
            }
        });
        
        btnFinalizarCompra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modeloCarrito.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(VentanaCliente.this, "El carrito esta vacio.", "Atencion", JOptionPane.WARNING_MESSAGE);
                } else {
                    VentanaRecibo ventanaRecibo = new VentanaRecibo(VentanaCliente.this, modeloCarrito, modeloCatalogo, clienteActual);
                    ventanaRecibo.setVisible(true);
                    actualizarTotal();
                }
            }
        });
    }

    private void agregarAlCarrito() {
        int filaSeleccionada = tablaCatalogo.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro del catalogo primero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String isbn = (String) modeloCatalogo.getValueAt(filaSeleccionada, 0);
        String titulo = (String) modeloCatalogo.getValueAt(filaSeleccionada, 1);
        double precio = (double) modeloCatalogo.getValueAt(filaSeleccionada, 3);
        int stockDisponible = (int) modeloCatalogo.getValueAt(filaSeleccionada, 4);

        String cantStr = JOptionPane.showInputDialog(this, "¿Cuántas unidades desea comprar de '" + titulo + "'? \nStock disponible: " + stockDisponible);
        
        if (cantStr != null && !cantStr.trim().isEmpty()) {
            try {
                int cantidadPedida = Integer.parseInt(cantStr);

                if (cantidadPedida <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } if (cantidadPedida > stockDisponible) {
                    JOptionPane.showMessageDialog(this, "Error: Stock insuficiente. Solo hay " + stockDisponible + " unidades disponibles.", "Stock Agotado", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double subtotal = precio * cantidadPedida;
                modeloCarrito.addRow(new Object[]{isbn, titulo, cantidadPedida, subtotal});
                actualizarTotal();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void actualizarTotal() {
        double subtotal = 0;
        
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            subtotal += (double) modeloCarrito.getValueAt(i, 3);
        }
        
        double descuento = clienteActual.calcularDescuento(subtotal);
        double totalFinal = subtotal - descuento;

        lblDescuento.setText("Descuento: -$" + descuento);
        lblTotalPagar.setText("Total a Pagar: $" + totalFinal);
    }
}