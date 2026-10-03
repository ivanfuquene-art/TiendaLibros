package co.edu.uptc.gui;

import java.awt.*;
import javax.swing.*;

public class PanelFormulario extends JPanel {

    private JTextField txtIsbn;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtAnio;
    private JTextField txtCategoria;
    private JTextField txtEditorial;
    private JTextField txtNumPag;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JComboBox<String> cbFormato;
    private JComboBox<Integer> cbIva;

    public PanelFormulario() {
        setLayout(new GridLayout(11, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Datos del Libro"));
        setPreferredSize(new Dimension(320, 0));

        txtIsbn = new JTextField();
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtAnio = new JTextField();
        txtCategoria = new JTextField();
        txtEditorial = new JTextField();
        txtNumPag = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();
        
        cbFormato = new JComboBox<>(new String[]{"Físico", "Digital"});
        cbIva = new JComboBox<>(new Integer[]{19, 5});

        add(new JLabel("ISBN:"));
        add(txtIsbn);
        add(new JLabel("Título:"));
        add(txtTitulo);
        add(new JLabel("Autor:"));
        add(txtAutor);
        add(new JLabel("Año Publicación:"));
        add(txtAnio);
        add(new JLabel("Categoría:"));
        add(txtCategoria);
        add(new JLabel("Editorial:"));
        add(txtEditorial);
        add(new JLabel("N° Páginas:"));
        add(txtNumPag);
        add(new JLabel("Precio Base:"));
        add(txtPrecio);
        add(new JLabel("Stock:"));
        add(txtStock);
        add(new JLabel("Formato:"));
        add(cbFormato);
        add(new JLabel("IVA (%):"));
        add(cbIva);
    }

    // Getters para capturar la información ingresada
    public String getIsbn() { return txtIsbn.getText().trim(); }
    public String getTitulo() { return txtTitulo.getText().trim(); }
    public String getAutor() { return txtAutor.getText().trim(); }
    public String getAnio() { return txtAnio.getText().trim(); }
    public String getCategoria() { return txtCategoria.getText().trim(); }
    public String getEditorial() { return txtEditorial.getText().trim(); }
    public String getNumPag() { return txtNumPag.getText().trim(); }
    public String getPrecio() { return txtPrecio.getText().trim(); }
    public String getStock() { return txtStock.getText().trim(); }
    public String getFormato() { return (String) cbFormato.getSelectedItem(); }
    public int getPorcentajeIva() { return (Integer) cbIva.getSelectedItem(); }
    
    public void limpiarCampos() {
        txtIsbn.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnio.setText("");
        txtCategoria.setText("");
        txtEditorial.setText("");
        txtNumPag.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
    }

}