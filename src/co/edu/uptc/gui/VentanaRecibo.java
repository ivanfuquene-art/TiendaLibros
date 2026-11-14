package co.edu.uptc.gui;

import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.ClientePremium;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRecibo extends JDialog {

    private DefaultTableModel modeloCarrito;
    private DefaultTableModel modeloCatalogo;
    private Cliente clienteActual;
    private double totalNeto;

    public VentanaRecibo(JFrame parent, DefaultTableModel carrito, DefaultTableModel catalogo, Cliente cliente) {
        super(parent, "Finalizar Compra - Recibo", true);
        this.modeloCarrito = carrito;
        this.modeloCatalogo = catalogo;
        this.clienteActual = cliente;

        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JTextArea txtResumen = new JTextArea();
        txtResumen.setEditable(false);
        txtResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResumen.setText(generarTextoResumen());
        add(new JScrollPane(txtResumen), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new GridLayout(2, 1, 5, 5));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelPago = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPago.add(new JLabel("Método de Pago:"));
        String[] metodos = {"Tarjeta de Crédito", "PSE / Transferencia", "Efectivo"};
        JComboBox<String> cbMetodoPago = new JComboBox<>(metodos);
        panelPago.add(cbMetodoPago);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnConfirmar = new JButton("Confirmar y Pagar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnConfirmar.setBackground(new Color(34, 139, 34));
        btnConfirmar.setForeground(Color.WHITE);

        panelBotones.add(btnCancelar);
        panelBotones.add(btnConfirmar);

        panelInferior.add(panelPago);
        panelInferior.add(panelBotones);
        add(panelInferior, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dispose());

        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarCompra(cbMetodoPago.getSelectedItem().toString());
            }
        });
    }

    private String generarTextoResumen() {
        StringBuilder sb = new StringBuilder();
        String tipo = (clienteActual instanceof ClientePremium) ? "PREMIUM" : "REGULAR";
        
        sb.append("========== RESUMEN DE COMPRA ==========\n");
        sb.append("Cliente: ").append(clienteActual.getNombreCompleto()).append(" (").append(tipo).append(")\n\n");
        sb.append(String.format("%-10s %-20s %-5s %s\n", "ISBN", "LIBRO", "CANT", "SUBTOTAL"));
        sb.append("---------------------------------------\n");

        double subtotalAcumulado = 0;
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            String isbn = modeloCarrito.getValueAt(i, 0).toString();
            String titulo = modeloCarrito.getValueAt(i, 1).toString();
            if(titulo.length() > 18) titulo = titulo.substring(0, 15) + "...";
            
            String cant = modeloCarrito.getValueAt(i, 2).toString();
            double sub = (double) modeloCarrito.getValueAt(i, 3);
            subtotalAcumulado += sub;

            sb.append(String.format("%-10s %-20s %-5s $%.2f\n", isbn, titulo, cant, sub));
        }
        
        double descuento = clienteActual.calcularDescuento(subtotalAcumulado);
        this.totalNeto = subtotalAcumulado - descuento;

        sb.append("---------------------------------------\n");
        sb.append(String.format("SUBTOTAL:           $%.2f\n", subtotalAcumulado));
        sb.append(String.format("DESCUENTO (%s): -$%.2f\n", tipo, descuento));
        sb.append(String.format("TOTAL A PAGAR:      $%.2f\n", totalNeto));
        sb.append("=======================================\n");
        return sb.toString();
    }

    private void procesarCompra(String metodoPago) {
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            String isbnCarrito = modeloCarrito.getValueAt(i, 0).toString();
            int cantComprada = (int) modeloCarrito.getValueAt(i, 2);

            for (int j = 0; j < modeloCatalogo.getRowCount(); j++) {
                if (modeloCatalogo.getValueAt(j, 0).equals(isbnCarrito)) {
                    int stockActual = (int) modeloCatalogo.getValueAt(j, 4);
                    modeloCatalogo.setValueAt(stockActual - cantComprada, j, 4);
                    break;
                }
            }
        }

        String mensajeFinal = "¡COMPRA EXITOSA!\n\n" +
                              "Método de Pago: " + metodoPago + "\n" +
                              "Total Pagado: $" + totalNeto + "\n\n" +
                              "El inventario ha sido actualizado.\n" +
                              "Gracias por su compra.";

        JOptionPane.showMessageDialog(this, mensajeFinal, "Recibo de Compra", JOptionPane.INFORMATION_MESSAGE);
        
        modeloCarrito.setRowCount(0); 
        dispose(); 
    }
}