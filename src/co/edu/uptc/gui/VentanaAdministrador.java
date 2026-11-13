package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class VentanaAdministrador extends JFrame {

    private PanelFormulario panelFormulario;
    private PanelTabla panelTabla;
    private JButton buttonAceptar;
    private JButton buttonCancelar;
    private ArrayList<Libro> listaLibros;

    public VentanaAdministrador() {
        setSize(900, 480);
        setTitle("Gestión de Tienda Virtual - Libros");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        listaLibros = new ArrayList<>();

        panelFormulario = new PanelFormulario();
        panelTabla = new PanelTabla();

        buttonAceptar = new JButton("Guardar Libro");
        buttonCancelar = new JButton("Limpiar / Cancelar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(buttonAceptar);
        panelBotones.add(buttonCancelar);

        add(panelFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        buttonAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarLibro();
            }
        });

        buttonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelFormulario.limpiarCampos();
            }
        });
    }

    private void guardarLibro() {
        try {
            if (panelFormulario.getIsbn().isEmpty() || panelFormulario.getTitulo().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ISBN y el Título son campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int anio = Integer.parseInt(panelFormulario.getAnio());
            int numPag = Integer.parseInt(panelFormulario.getNumPag());
            double precio = Double.parseDouble(panelFormulario.getPrecio());
            int stock = Integer.parseInt(panelFormulario.getStock());

            Libro nuevoLibro = new Libro(
                panelFormulario.getIsbn(),
                panelFormulario.getTitulo(),
                panelFormulario.getAutor(),
                anio,
                panelFormulario.getCategoria(),
                panelFormulario.getEditorial(),
                numPag,
                precio,
                stock,
                panelFormulario.getFormato(),
                panelFormulario.getPorcentajeIva()
            );

            listaLibros.add(nuevoLibro);
            panelTabla.agregarLibroTabla(nuevoLibro);

            JOptionPane.showMessageDialog(this, "El libro se ha registrado.");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ponga valores numericos en Año, Paginas, Precio y Stock.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

}