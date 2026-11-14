package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ModeloTablaLibro extends AbstractTableModel {

    private List<Libro> listaLibros;
    private final String[] columnas = {
        "ISBN", "Título", "Autor", "Año", "Categoría", 
        "Editorial", "Págs", "Precio", "Stock", "Formato", "IVA (%)"
    };

    public ModeloTablaLibro() {
        this.listaLibros = new ArrayList<>();
    }

    public void agregarLibro(Libro libro) {
        this.listaLibros.add(libro);
        fireTableDataChanged();
    }

    public void limpiar() {
        this.listaLibros.clear();
        fireTableDataChanged();
    }

    public Libro getLibroEnFila(int fila) {
        if (fila >= 0 && fila < listaLibros.size()) {
            return listaLibros.get(fila);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return listaLibros.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Libro libro = listaLibros.get(rowIndex);
        switch (columnIndex) {
            case 0: return libro.getIsbn();
            case 1: return libro.getTitulo();
            case 2: return libro.getAutor();
            case 3: return libro.getAnioPubli();
            case 4: return libro.getCategoria();
            case 5: return libro.getEditorial();
            case 6: return libro.getNumPag();
            case 7: return libro.getPrecio();
            case 8: return libro.getStock();
            case 9: return libro.getFormato();
            case 10: return libro.getPorcentajeIva();
            default: return null;
        }
    }
}