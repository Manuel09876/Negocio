package Register;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JOptionPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.toedter.calendar.JDateChooser;
import conectar.Conectar;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


public class Cotizaciones extends javax.swing.JInternalFrame {
    
    public class Configuracion {
    private String nombre;
    private String direccion;
    private String ciudad;
    private String estado;
    private String zipcode;
    private String telefono;
    private String email;
    private String webpage;
    private byte[] logo; // assuming the logo is stored as a byte array

    // Getters and Setters for each field

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getWebpage() {
            return webpage;
        }

        public void setWebpage(String webpage) {
            this.webpage = webpage;
        }

        public byte[] getLogo() {
            return logo;
        }

        public void setLogo(byte[] logo) {
            this.logo = logo;
        }
    
}


    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel tmp;

    Conectar con = new Conectar();
    Connection connect = con.getConexion();

    PreparedStatement pstmt;
    int item;
    double TotalPagar = 0.00;

    public Cotizaciones() {
        initComponents();
        MostrarListaPrecios();
        txtDias.setEnabled(false);

        TableOrdenDeServiciosCot.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Descripcion", "Cantidad de Servicios", "Precio", "Total"
                }
        ));

        tbCotizaciones.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Nombre", "Dirección", "Tamaño", "Ciudad", "Estado", "Zip Code", "Teléfono", "Fecha Inicio", "Fecha Fin", "Total", "Fecha", "PDF"
                }
        ));

        actualizarTablaCotizaciones();

// Agregar el botón en la columna "PDF"
        // Agregar el botón en la columna "PDF"
        tbCotizaciones.getColumnModel().getColumn(12).setCellRenderer(new ButtonRenderer());
        tbCotizaciones.getColumnModel().getColumn(12).setCellEditor(new ButtonEditor(new JCheckBox(), tbCotizaciones));
        
        

    }

    //Definición de las clases ButtonRenderer y ButtonEditor
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Ver PDF" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Ver PDF" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                abrirPDF();
            }
            isPushed = false;
            return new String(label);
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        private void abrirPDF() {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int cotizacionId = (int) table.getValueAt(selectedRow, 0);
                try {
                    String sql = "SELECT pdf FROM cotizaciones WHERE id = ?";
                    PreparedStatement pst = connect.prepareStatement(sql);
                    pst.setInt(1, cotizacionId);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        byte[] pdfData = rs.getBytes("pdf");
                        if (pdfData != null) {
                            File tempFile = File.createTempFile("cotizacion_" + cotizacionId, ".pdf");
                            FileOutputStream fos = new FileOutputStream(tempFile);
                            fos.write(pdfData);
                            fos.close();

                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().open(tempFile);
                            } else {
                                JOptionPane.showMessageDialog(null, "No se puede abrir el PDF en este sistema.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No hay PDF asociado con esta cotización.");
                        }
                    }
                } catch (SQLException | IOException e) {
                    JOptionPane.showMessageDialog(null, "Error al abrir el PDF: " + e.getMessage());
                }
            }
        }
    }

    public void MostrarListaPrecios() {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            String[] tituloTabla = {"Servicio", "Precio"};
            String[] RegistroBD = new String[2];
            modelo = new DefaultTableModel(null, tituloTabla);
            tbPreciosCot.setModel(modelo);
            String sql = "SELECT services.servicio, services.precio FROM services INNER JOIN bussiness ON services.id_empresa=bussiness.idBusiness WHERE id_empresa=3";
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[0] = rs.getString("servicio");
                RegistroBD[1] = rs.getString("precio");
                modelo.addRow(RegistroBD);
            }
            tbPreciosCot.setModel(modelo);
            tbPreciosCot.getColumnModel().getColumn(0).setPreferredWidth(150);
            tbPreciosCot.getColumnModel().getColumn(1).setPreferredWidth(50);
        } catch (SQLException e) {
            System.out.println("Error" + e.toString());
        }
    }

    // Selecciona los datos a insertar a la Tabla
    public void Seleccionar(JTable tbPrecios, JTextField TipoServCot, JTextField PrecioUnitCot) {
        try {
            int fila = tbPrecios.getSelectedRow();
            if (fila >= 0) {
                TipoServCot.setText(tbPrecios.getValueAt(fila, 0).toString());
                PrecioUnitCot.setText(tbPrecios.getValueAt(fila, 1).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la selección, error " + e.toString());
        }
    }

    //Calculo Periodo; Cantidad de servicios
    public void CalcularFecha(JDateChooser DateInicio, JDateChooser DateFinal) {
        if (DateInicio.getDate() != null && DateFinal.getDate() != null) {
            Calendar inicio = DateInicio.getCalendar();
            Calendar termino = DateFinal.getCalendar();
            int dias = -1;
            while (inicio.before(termino) || inicio.equals(termino)) {
                dias++;
                inicio.add(Calendar.DATE, 1);
            }

            // Mostrar los días calculados en txtDias
            txtDias.setText(String.valueOf(dias));

            // Verificar que el campo de texto no esté vacío
            String entreServiciosText = txtEntreServicios.getText().trim();
            if (entreServiciosText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo entre servicios no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int entreServicios = Integer.parseInt(entreServiciosText);
                int n = dias / entreServicios;
                txtCantServicios.setText("" + n);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione las fechas", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Calculo del SubTotal
    public void CalculoSubTotal(JTextField Servicios, JTextField Precio) {
        int cantidad;
        double precio;
        Double SubTotal;

        cantidad = Integer.parseInt(txtCantServicios.getText());
        precio = Double.parseDouble(txtPrecioUnitCot.getText());

        SubTotal = cantidad * precio;

//        txtSubtotal.setText(String.valueOf(SubTotal));
    }

// Calcula el Total para que se muestre en el Label
    // Calcula el Total para que se muestre en el Label
    private void TotalPagar() {
        TotalPagar = 0.00;
        int numFila = TableOrdenDeServiciosCot.getRowCount();
        for (int i = 0; i < numFila; i++) {
            Object valor = TableOrdenDeServiciosCot.getValueAt(i, 3); // Columna Total está en el índice 3
            if (valor != null && !valor.toString().isEmpty() && !valor.toString().equals("null")) {
                try {
                    TotalPagar = TotalPagar + Double.parseDouble(valor.toString());
                } catch (NumberFormatException e) {
                    // Manejar el caso en que el valor no sea un número válido
                    JOptionPane.showMessageDialog(null, "Valor inválido en la fila " + (i + 1), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        Labeltotalpagar.setText(String.format("%.2f", TotalPagar));
    }

    private void agregarTemp(int cant, String desc, double precio, int id, JTable tabla, JTextField codigo) {
        if (cant > 0) {
            tmp = (DefaultTableModel) tabla.getModel();
            ArrayList lista = new ArrayList();
            int item = 1;
            lista.add(item);
            lista.add(id);
            lista.add(desc);
            lista.add(cant);
            lista.add(precio);
            lista.add(cant * precio);
            Object[] obj = new Object[5];
            obj[0] = lista.get(1);
            obj[1] = lista.get(2);
            obj[2] = lista.get(3);
            obj[3] = lista.get(4);
            obj[4] = lista.get(5);
            tmp.addRow(obj);
            tabla.setModel(tmp);
            codigo.requestFocus();
        }

    }

    private void LimpiarOrden() {
        txtTipoServCot.setText("");
        txtCantServicios.setText("");
        txtEntreServicios.setText("");
        txtPrecioUnitCot.setText("");
    }

    private void guardarDetalle(int cotizacionId) {
        try {
            String sql = "INSERT INTO detalle_cotizacion (cotizacion_id, descripcion, cantidad, precio, total) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = connect.prepareStatement(sql);
            for (int i = 0; i < TableOrdenDeServiciosCot.getRowCount(); i++) {
                pst.setInt(1, cotizacionId);
                pst.setString(2, TableOrdenDeServiciosCot.getValueAt(i, 0).toString());
                pst.setInt(3, Integer.parseInt(TableOrdenDeServiciosCot.getValueAt(i, 1).toString()));
                pst.setDouble(4, Double.parseDouble(TableOrdenDeServiciosCot.getValueAt(i, 2).toString()));
                pst.setDouble(5, Double.parseDouble(TableOrdenDeServiciosCot.getValueAt(i, 3).toString()));
                pst.addBatch();
            }
            pst.executeBatch();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el detalle de la cotización: " + e.getMessage());
        }
    }

    private void GuardarCotizacion() {
    String nombre = txtNombreCot.getText().trim();
    String direccion = txtDireccionCot.getText().trim();
    String tamaño = txtTamañoCot.getText().trim();
    String ciudad = txtCiudadCot.getText().trim();
    String estado = txtEstadoCot.getText().trim();
    String zipCode = txtZipcodeCot.getText().trim();
    String telefono = txtTelefonoCot.getText().trim();
    Date fechaInicio = DateInicio.getDate();
    Date fechaFin = DateFinal.getDate();
    double total = Double.parseDouble(Labeltotalpagar.getText());
    Date fechaCotizacion = new Date(); // Fecha actual

    if (nombre.isEmpty() || direccion.isEmpty() || tamaño.isEmpty() || ciudad.isEmpty() || estado.isEmpty() || zipCode.isEmpty() || telefono.isEmpty() || fechaInicio == null || fechaFin == null) {
        JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        String sql = "INSERT INTO cotizaciones (nombre, direccion, tamaño, ciudad, estado, zip_code, telefono, fecha_inicio, fecha_fin, total, fecha) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, nombre);
        pst.setString(2, direccion);
        pst.setString(3, tamaño);
        pst.setString(4, ciudad);
        pst.setString(5, estado);
        pst.setString(6, zipCode);
        pst.setString(7, telefono);
        pst.setDate(8, new java.sql.Date(fechaInicio.getTime()));
        pst.setDate(9, new java.sql.Date(fechaFin.getTime()));
        pst.setDouble(10, total);
        pst.setDate(11, new java.sql.Date(fechaCotizacion.getTime()));
        pst.executeUpdate();

        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            int cotizacionId = rs.getInt(1);
            guardarDetalle(cotizacionId);

            // Obtener la configuración de la base de datos
            Configuracion datosEmpresa = obtenerConfiguracion();

            generarPDFCotizacion(cotizacionId, fechaCotizacion, datosEmpresa);
            actualizarTablaCotizaciones(); // Actualizar la tabla con la nueva cotización
            limpiarTablaOrdenDeServicios(); // Limpiar la tabla de orden de servicios
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar la cotización: " + e.getMessage());
    }
}





                   
    private void limpiarTablaOrdenDeServicios() {
        DefaultTableModel modelo = (DefaultTableModel) TableOrdenDeServiciosCot.getModel();
        modelo.setRowCount(0); // Esto borra todas las filas de la tabla
    }
    
    private Configuracion obtenerConfiguracion() {
    Configuracion config = new Configuracion();
    String sql = "SELECT nombre, direccion, ciudad, estado, zipcode, telefono, email, webpage, logo FROM configuracion WHERE id = 1";
    try {
        Statement st = connect.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            config.setNombre(rs.getString("nombre"));
            config.setDireccion(rs.getString("direccion"));
            config.setCiudad(rs.getString("ciudad"));
            config.setEstado(rs.getString("estado"));
            config.setZipcode(rs.getString("zipcode"));
            config.setTelefono(rs.getString("telefono"));
            config.setEmail(rs.getString("email"));
            config.setWebpage(rs.getString("webpage"));
            config.setLogo(rs.getBytes("logo")); // Obtener el logo como byte array
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la configuración: " + e.getMessage());
    }
    return config;
}


    //PDF
   private void generarPDFCotizacion(int cotizacionId, Date fechaCotizacion, Configuracion datosEmpresa) {
    Document document = new Document(PageSize.A4);
    try {
        String filePath = "cotizacion_" + cotizacionId + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Agregar logo y nombre de la empresa
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new int[]{1, 4});

        // Celda para el logo
        if (datosEmpresa.getLogo() != null) {
            Image logo = Image.getInstance(datosEmpresa.getLogo());
            logo.scaleToFit(100, 100); // Ajustar el tamaño del logo a 100x100 píxeles
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(PdfPCell.NO_BORDER);
            headerTable.addCell(logoCell);
        } else {
            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setBorder(PdfPCell.NO_BORDER);
            headerTable.addCell(emptyCell);
        }

        // Celda para la información de la empresa
        PdfPCell empresaCell = new PdfPCell();
        empresaCell.setBorder(PdfPCell.NO_BORDER);
        Paragraph empresaInfo = new Paragraph();
        empresaInfo.add(new Paragraph(datosEmpresa.getNombre(), new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD)));
        empresaInfo.add(new Paragraph(datosEmpresa.getDireccion() + " - " + datosEmpresa.getCiudad() + ", " + datosEmpresa.getEstado() + " " + datosEmpresa.getZipcode(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        empresaInfo.add(new Paragraph("Teléfono: " + datosEmpresa.getTelefono(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        empresaInfo.add(new Paragraph("Email: " + datosEmpresa.getEmail(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        empresaInfo.add(new Paragraph("Web: " + datosEmpresa.getWebpage(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        empresaCell.addElement(empresaInfo);
        headerTable.addCell(empresaCell);

        document.add(headerTable);

        document.add(new Paragraph("\n"));

        // Agregar contenido al PDF
        document.add(new Paragraph("Cotización #" + cotizacionId, new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD)));
        document.add(new Paragraph("Fecha: " + fechaCotizacion.toString(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        document.add(new Paragraph("Nombre: " + txtNombreCot.getText(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        document.add(new Paragraph("Dirección: " + txtDireccionCot.getText(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        document.add(new Paragraph("Ciudad: " + txtCiudadCot.getText(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        document.add(new Paragraph("Estado: " + txtEstadoCot.getText(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        document.add(new Paragraph("Zip Code: " + txtZipcodeCot.getText(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        document.add(new Paragraph("Teléfono: " + txtTelefonoCot.getText(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        document.add(new Paragraph("Fecha Inicio: " + DateInicio.getDate(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        document.add(new Paragraph("Fecha Fin: " + DateFinal.getDate(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
        document.add(new Paragraph("Total: " + Labeltotalpagar.getText(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));

        document.add(new Paragraph("\n"));

        // Agregar tabla de detalles
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{4, 1, 1, 1});
        table.addCell(new PdfPCell(new Phrase("Descripción", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD))));
        table.addCell(new PdfPCell(new Phrase("Cantidad", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD))));
        table.addCell(new PdfPCell(new Phrase("Precio", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD))));
        table.addCell(new PdfPCell(new Phrase("Total", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD))));

        for (int i = 0; i < TableOrdenDeServiciosCot.getRowCount(); i++) {
            table.addCell(TableOrdenDeServiciosCot.getValueAt(i, 0).toString());
            table.addCell(TableOrdenDeServiciosCot.getValueAt(i, 1).toString());
            table.addCell(TableOrdenDeServiciosCot.getValueAt(i, 2).toString());
            table.addCell(TableOrdenDeServiciosCot.getValueAt(i, 3).toString());
        }

        document.add(table);
        document.close();

        guardarPDFEnBaseDeDatos(cotizacionId, filePath);
    } catch (DocumentException | IOException e) {
        JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + e.getMessage());
    }
}

private void guardarPDFEnBaseDeDatos(int cotizacionId, String filePath) {
    try {
        String sql = "UPDATE cotizaciones SET pdf = ? WHERE id = ?";
        PreparedStatement pst = connect.prepareStatement(sql);

        // Leer el archivo PDF y convertirlo en un array de bytes
        File pdfFile = new File(filePath);
        FileInputStream fis = new FileInputStream(pdfFile);
        byte[] pdfData = new byte[(int) pdfFile.length()];
        fis.read(pdfData);
        fis.close();

        pst.setBytes(1, pdfData);
        pst.setInt(2, cotizacionId);
        pst.executeUpdate();

        JOptionPane.showMessageDialog(null, "PDF guardado en la base de datos correctamente.");
    } catch (SQLException | IOException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar el PDF en la base de datos: " + e.getMessage());
    }
}
   
    private void actualizarTablaCotizaciones() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tbCotizaciones.getModel();
            modelo.setRowCount(0); // Limpiar la tabla

            String sql = "SELECT id, nombre, direccion, tamaño, ciudad, estado, zip_code, telefono, fecha_inicio, fecha_fin, total, fecha FROM cotizaciones";
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Object[] row = new Object[13];
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nombre");
                row[2] = rs.getString("direccion");
                row[3] = rs.getString("tamaño");
                row[4] = rs.getString("ciudad");
                row[5] = rs.getString("estado");
                row[6] = rs.getString("zip_code");
                row[7] = rs.getString("telefono");
                row[8] = rs.getDate("fecha_inicio");
                row[9] = rs.getDate("fecha_fin");
                row[10] = rs.getDouble("total");
                row[11] = rs.getDate("fecha");
                row[12] = "Ver PDF"; // Botón para ver el PDF
                modelo.addRow(row);
            }

            tbCotizaciones.getColumnModel().getColumn(12).setCellRenderer(new ButtonRenderer());
            tbCotizaciones.getColumnModel().getColumn(12).setCellEditor(new ButtonEditor(new JCheckBox(), tbCotizaciones));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar la tabla de cotizaciones: " + e.getMessage());
        }
    }

    public void LimpiarCajas() {
        txtNombreCot.setText("");
        txtDireccionCot.setText("");
        txtTamañoCot.setText("");
        txtCiudadCot.setText("");
        txtZipcodeCot.setText("");
        txtEstadoCot.setText("");
        txtTelefonoCot.setText("");
        txtEmail.setText("");
        txtDias.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        txtNombreCot = new javax.swing.JTextField();
        txtDireccionCot = new javax.swing.JTextField();
        txtTamañoCot = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtCiudadCot = new javax.swing.JTextField();
        txtZipcodeCot = new javax.swing.JTextField();
        txtEstadoCot = new javax.swing.JTextField();
        txtId = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        DateInicio = new com.toedter.calendar.JDateChooser();
        DateFinal = new com.toedter.calendar.JDateChooser();
        txtEntreServicios = new javax.swing.JTextField();
        txtCantServicios = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTelefonoCot = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtTipoServCot = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtPrecioUnitCot = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        Labeltotalpagar = new javax.swing.JLabel();
        txtDias = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPreciosCot = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableOrdenDeServiciosCot = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbCotizaciones = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cotizaciones");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(965, 24, -1, -1));
        jPanel1.add(txtNombreCot, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 6, 127, -1));
        jPanel1.add(txtDireccionCot, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 46, 127, -1));
        jPanel1.add(txtTamañoCot, new org.netbeans.lib.awtextra.AbsoluteConstraints(81, 86, 128, -1));

        jLabel1.setText("Nombre");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 9, -1, -1));

        jLabel2.setText("Dirección");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 49, -1, -1));

        jLabel3.setText("Tamaño");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 89, -1, -1));

        jLabel4.setText("Ciudad");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 123, -1, -1));

        jLabel5.setText("Zip Code");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(238, 9, -1, -1));

        jLabel6.setText("Estado");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(238, 43, -1, -1));
        jPanel1.add(txtCiudadCot, new org.netbeans.lib.awtextra.AbsoluteConstraints(84, 120, 125, -1));
        jPanel1.add(txtZipcodeCot, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 102, -1));
        jPanel1.add(txtEstadoCot, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 102, -1));
        jPanel1.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(965, 118, 80, -1));

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo_1.png"))); // NOI18N
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(843, 118, -1, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(891, 118, -1, -1));
        jPanel1.add(DateInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 24, 140, -1));
        jPanel1.add(DateFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 64, 140, -1));

        txtEntreServicios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEntreServiciosKeyPressed(evt);
            }
        });
        jPanel1.add(txtEntreServicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 102, 71, -1));
        jPanel1.add(txtCantServicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 142, 71, -1));

        jLabel7.setText("F.Inicio");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, -1, -1));

        jLabel8.setText("F.Fin");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, -1, -1));

        jLabel9.setText("Frecuencia");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(439, 105, -1, -1));

        jLabel10.setText("Servicios");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(439, 126, -1, -1));

        jButton3.setText("Generar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(965, 58, -1, -1));
        jPanel1.add(jDateChooser3, new org.netbeans.lib.awtextra.AbsoluteConstraints(826, 47, 120, -1));

        jLabel11.setText("F.Cotizacion");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(852, 24, -1, -1));

        jLabel12.setText("Teléfono");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, -1));

        txtTelefonoCot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoCotActionPerformed(evt);
            }
        });
        jPanel1.add(txtTelefonoCot, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 99, -1));
        jPanel1.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 120, 112, -1));

        jLabel13.setText("Email");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, -1, -1));

        jLabel14.setText("Cantidad");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(439, 148, -1, -1));

        jLabel15.setText("Tipo de Servicio");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 50, -1, -1));
        jPanel1.add(txtTipoServCot, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 70, 170, -1));

        jLabel16.setText("Precio Unitario");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 100, -1, -1));
        jPanel1.add(txtPrecioUnitCot, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 100, 100, -1));

        jLabel17.setText("Total a Pagar");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 140, -1, -1));

        Labeltotalpagar.setText("--------");
        jPanel1.add(Labeltotalpagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 140, 80, -1));
        jPanel1.add(txtDias, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, 90, -1));

        jLabel18.setText("dias entre fechas");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1070, 170));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbPreciosCot.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbPreciosCot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPreciosCotMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbPreciosCot);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 0, 310, 190));

        TableOrdenDeServiciosCot.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(TableOrdenDeServiciosCot);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 680, 160));

        tbCotizaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbCotizaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCotizacionesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbCotizaciones);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 1030, 180));

        jLabel19.setText("Cotizaciones");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 180, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 1060, 390));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtTelefonoCotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoCotActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoCotActionPerformed

    private void tbPreciosCotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPreciosCotMouseClicked
        Seleccionar(tbPreciosCot, txtTipoServCot, txtPrecioUnitCot);
    }//GEN-LAST:event_tbPreciosCotMouseClicked

    private void txtEntreServiciosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntreServiciosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CalcularFecha(DateInicio, DateFinal);
        }
    }//GEN-LAST:event_txtEntreServiciosKeyPressed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        String descripcion = txtTipoServCot.getText().trim();
        String cantidadStr = txtCantServicios.getText().trim();
        String precioStr = txtPrecioUnitCot.getText().trim();

        if (descripcion.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int servicios;
        double precio;
        try {
            servicios = Integer.parseInt(cantidadStr);
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cantidad y Precio deben ser valores numéricos válidos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double total = servicios * precio;

        item = item + 1;
        modelo = (DefaultTableModel) TableOrdenDeServiciosCot.getModel();
        for (int i = 0; i < TableOrdenDeServiciosCot.getRowCount(); i++) {
            Object cellValue = TableOrdenDeServiciosCot.getValueAt(i, 0);
            if (cellValue != null && cellValue.equals(descripcion)) {
                JOptionPane.showMessageDialog(null, "El Servicio ya está ingresado");
                return;
            }
        }

        Object[] row = new Object[4]; // Asegúrate de que hay cuatro columnas
        row[0] = descripcion; // Descripción
        row[1] = servicios;   // Cantidad de Servicios
        row[2] = precio;      // Precio
        row[3] = total;       // Total

        modelo.addRow(row);
        TableOrdenDeServiciosCot.setModel(modelo);
        TotalPagar();
        LimpiarOrden();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int selectedRow = TableOrdenDeServiciosCot.getSelectedRow();
        if (selectedRow != -1) {
            modelo = (DefaultTableModel) TableOrdenDeServiciosCot.getModel();
            modelo.removeRow(selectedRow);
            TotalPagar();
            txtEntreServicios.requestFocus();
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        GuardarCotizacion();
        LimpiarCajas();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tbCotizacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCotizacionesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbCotizacionesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser DateFinal;
    private com.toedter.calendar.JDateChooser DateInicio;
    private javax.swing.JLabel Labeltotalpagar;
    private javax.swing.JTable TableOrdenDeServiciosCot;
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton jButton3;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tbCotizaciones;
    private javax.swing.JTable tbPreciosCot;
    private javax.swing.JTextField txtCantServicios;
    private javax.swing.JTextField txtCiudadCot;
    private javax.swing.JTextField txtDias;
    private javax.swing.JTextField txtDireccionCot;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEntreServicios;
    private javax.swing.JTextField txtEstadoCot;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombreCot;
    private javax.swing.JTextField txtPrecioUnitCot;
    private javax.swing.JTextField txtTamañoCot;
    private javax.swing.JTextField txtTelefonoCot;
    private javax.swing.JTextField txtTipoServCot;
    private javax.swing.JTextField txtZipcodeCot;
    // End of variables declaration//GEN-END:variables

}
