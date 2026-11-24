package co.edu.uptc.gui;

import co.edu.uptc.negocio.GestorTienda;
import co.edu.uptc.negocio.Libro;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class VentanaAdministrador extends JFrame {

    private PanelFormulario panelFormulario;
    private PanelTabla panelTabla;
    private JButton buttonAceptar;
    private JButton buttonActualizar;
    private JButton buttonEliminar;
    private JButton buttonCancelar;
    private JButton buttonCerrarSesion;

    private GestorTienda gestorTienda;

/*  Constructor vacío por si ejecutas esta ventana directamente en pruebas
    public VentanaAdministrador() {
        this(new GestorTienda());
    } */

    // Constructor que recibe el GestorTienda compartido desde VentanaLogin
    public VentanaAdministrador(GestorTienda gestorTienda) {
        this.gestorTienda = gestorTienda;

        setSize(950, 500);
        setTitle("Gestión de Tienda Virtual - Administrador");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        panelFormulario = new PanelFormulario();
        panelTabla = new PanelTabla();

        buttonAceptar = new JButton("Guardar Libro");
        buttonActualizar = new JButton("Actualizar Libro");
        buttonEliminar = new JButton("Eliminar Libro");
        buttonCancelar = new JButton("Limpiar / Cancelar");
        buttonCerrarSesion = new JButton("Cerrar Sesión");

        JPanel panelBotones = new JPanel();
        panelBotones.add(buttonAceptar);
        panelBotones.add(buttonActualizar);
        panelBotones.add(buttonEliminar);
        panelBotones.add(buttonCancelar);
        panelBotones.add(buttonCerrarSesion);

        add(panelFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarCatalogoBD();

        buttonAceptar.addActionListener(e -> guardarLibro());
        buttonActualizar.addActionListener(e -> actualizarLibro());
        buttonEliminar.addActionListener(e -> eliminarLibro());
        buttonCancelar.addActionListener(e -> panelFormulario.limpiarCampos());
        buttonCerrarSesion.addActionListener(e -> {
            new VentanaLogin(this.gestorTienda).setVisible(true);
            dispose();
        });
    }

    private void cargarCatalogoBD() {
        panelTabla.limpiarTabla();
        List<Libro> librosBD = gestorTienda.getCatalogo();
        for (Libro libro : librosBD) {
            panelTabla.agregarLibroTabla(libro);
        }
    }

    private Libro libroFormulario() throws NumberFormatException {
        int anio = Integer.parseInt(panelFormulario.getAnio());
        int numPag = Integer.parseInt(panelFormulario.getNumPag());
        double precio = Double.parseDouble(panelFormulario.getPrecio());
        int stock = Integer.parseInt(panelFormulario.getStock());

        return new Libro(
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
    }

    private void guardarLibro() {
        try {
            if (panelFormulario.getIsbn().isEmpty() || panelFormulario.getTitulo().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ISBN y el Título son campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Libro nuevoLibro = libroFormulario();

            if (gestorTienda.registrarLibro(nuevoLibro)) {
                cargarCatalogoBD();
                panelFormulario.limpiarCampos();
                JOptionPane.showMessageDialog(this, "El libro se ha registrado en MySQL.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar: Verifique que el ISBN no esté repetido.", "Error BD", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ponga valores numéricos en Año, Páginas, Precio y Stock.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarLibro() {
        try {
            if (panelFormulario.getIsbn().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el ISBN del libro que desea actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Libro libroEditado = libroFormulario();

            if (gestorTienda.actualizarLibro(libroEditado)) {
                cargarCatalogoBD();
                panelFormulario.limpiarCampos();
                JOptionPane.showMessageDialog(this, "Información del libro actualizada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró un libro con ese ISBN para actualizar.", "Error BD", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ponga valores numéricos válidos en Año, Páginas, Precio y Stock.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarLibro() {
        String isbn = panelTabla.getIsbnSeleccionado();
        if (isbn == null) {
            isbn = JOptionPane.showInputDialog(this, "Seleccione una fila o ingrese el ISBN del libro a eliminar:");
        }

        if (isbn != null && !isbn.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar el libro con ISBN " + isbn.trim() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (gestorTienda.eliminarLibro(isbn.trim())) {
                    cargarCatalogoBD();
                    JOptionPane.showMessageDialog(this, "Libro eliminado del catálogo.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar (el ISBN no existe o tiene ventas asociadas).", "Restricción de Negocio", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}