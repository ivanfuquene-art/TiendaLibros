package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;
import java.awt.*;
import javax.swing.*;

public class PanelTabla extends JPanel {

    private JTable tablaLibros;
    private ModeloTablaLibro modeloTabla;

    public PanelTabla() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Catalogo registrado"));

        modeloTabla = new ModeloTablaLibro();
        tablaLibros = new JTable(modeloTabla);

        add(new JScrollPane(tablaLibros), BorderLayout.CENTER);
    }

    public void agregarLibroTabla(Libro libro) {
        modeloTabla.agregarLibro(libro);
    }

    public String getIsbnSeleccionado() {
        int fila = tablaLibros.getSelectedRow();
        if (fila != -1) {
            return (String) modeloTabla.getValueAt(fila, 0);
        }
        return null;
    }

    public void limpiarTabla() {
        modeloTabla.limpiar();
    }
}