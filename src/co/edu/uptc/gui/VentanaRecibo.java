package co.edu.uptc.gui;

import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.ClientePremium;
import co.edu.uptc.negocio.ItemCarrito;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VentanaRecibo extends JDialog {

    public VentanaRecibo(JFrame padre, Cliente cliente, List<ItemCarrito> items,
                         double subtotal, double iva, double descuento, double total) {
        super(padre, "Comprobante de Compra", true);
        setSize(450, 520);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout(10, 10));

        JTextArea areaRecibo = new JTextArea();
        areaRecibo.setEditable(false);
        areaRecibo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaRecibo.setMargin(new Insets(10, 10, 10, 10));

        areaRecibo.setText(generarTextoRecibo(cliente, items, subtotal, iva, descuento, total));

        add(new JScrollPane(areaRecibo), BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Aceptar y Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelSur = new JPanel();
        panelSur.add(btnCerrar);
        add(panelSur, BorderLayout.SOUTH);
    }

    private String generarTextoRecibo(Cliente cliente, List<ItemCarrito> items,
                                      double subtotal, double iva, double descuento, double total) {
        StringBuilder factura = new StringBuilder();
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        factura.append("        TIENDA VIRTUAL DE LIBROS UPTC        \n");
        factura.append("=============================================\n");
        factura.append("Fecha:      ").append(fecha).append("\n");
        factura.append("Cliente:    ").append(cliente.getNombreCompleto()).append("\n");
        factura.append("Correo:     ").append(cliente.getCorreo()).append("\n");
        factura.append("Teléfono:   ").append(cliente.getTelefono()).append("\n");
        factura.append("Envío a:    ").append(cliente.getDireccion()).append("\n");
        factura.append("Categoría:  ").append(cliente.getTipoCliente()).append("\n");
        factura.append("---------------------------------------------\n");
        factura.append(String.format("%-20s %5s %14s\n", "LIBRO", "CANT", "SUBTOTAL"));
        factura.append("---------------------------------------------\n");

        for (ItemCarrito item : items) {
            String titulo = item.getLibro().getTitulo();
            if (titulo.length() > 19) {
                titulo = titulo.substring(0, 19);
            }
            factura.append(String.format("%-20s %5d %14.2f\n",
                    titulo, item.getCantidad(), item.calcularSubtotal()));
        }

        factura.append("---------------------------------------------\n");
        factura.append(String.format("Subtotal:                     $%12.2f\n", subtotal));
        factura.append(String.format("IVA Total:                    $%12.2f\n", iva));
        factura.append(String.format("Descuento Cliente:           -$%12.2f\n", descuento));
        factura.append("=============================================\n");
        factura.append(String.format("TOTAL PAGADO:                 $%12.2f\n", total));
        factura.append("=============================================\n");

        if (cliente instanceof ClientePremium) {
            factura.append("\n* Beneficio Premium aplicado:\n");
            factura.append("  ").append(((ClientePremium) cliente).obtenerBeneficios()).append("\n");
        }

        factura.append("¡Gracias por su compra!\n");

        return factura.toString();
    }
}