package co.edu.uptc.gui;

import co.edu.uptc.gui.ModeloTablaLibro;
import co.edu.uptc.negocio.Libro;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class PanelTabla extends JPanel {

	private JTable tablaLibros;
    private ModeloTablaLibro modeloTabla;

    public PanelTabla() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Catálogo Registrado"));

        String[] columnas = {"ISBN", "Título", "Autor", "Categoría", "Formato", "Precio Base", "IVA", "Stock"};
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