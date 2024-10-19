package Reports;

import Admission.Configuracion;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import conectar.Conectar;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.io.File;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class DeudasPorCobrar extends javax.swing.JInternalFrame {

    DefaultTableModel modelo = new DefaultTableModel();

    public DeudasPorCobrar() {
        initComponents();
        
        AutoCompleteDecorator.decorate(cbxEmpresa);
        AutoCompleteDecorator.decorate(cbxPagarCon);
        MostrarEmpresa(cbxEmpresa);
        MostrarFormaDePago(cbxPagarCon);
        MostrarTablaDeuda();
        txtId.setVisible(false);
        txtIdEmpresa.setVisible(false);

        getNextInvoiceNumber();

    }
    
    public void addCheckBox(int column, JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }

    public boolean IsSelected(int row, int column, JTable table) {
        return table.getValueAt(row, column) != null;
    }

    public void MostrarTablaDeuda() {
Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Precio", "Status", "Estado de Cuenta", "Número Factura", "Ver PDF"};
            String[] RegistroBD = new String[17];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbDeudasPorCobrar);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer "
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, "
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, "
                    + "o.precio AS Precio, o.estado AS Status, o.eeCta AS 'Estado de Cuenta', f.numeroFactura "
                    + "FROM orderservice AS o "
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness "
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer "
                    + "LEFT JOIN facturas AS f ON o.id = f.id_ordenServicio " // Asegúrate de que tienes la relación correcta
                    + "WHERE o.estado != 'Inactivo' AND o.eeCta = 'deuda' "
                    + "ORDER BY o.fechaT ASC";
     
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[1] = rs.getString("id");
                RegistroBD[2] = rs.getString("Empresa");
                RegistroBD[3] = rs.getString("Fecha");
                RegistroBD[4] = rs.getString("Nombre");
                RegistroBD[5] = rs.getString("Tamaño");
                RegistroBD[6] = rs.getString("Direccion");
                RegistroBD[7] = rs.getString("Ciudad");
                RegistroBD[8] = rs.getString("Estado");
                RegistroBD[9] = rs.getString("Zip Code");
                RegistroBD[10] = rs.getString("Celular");
                RegistroBD[11] = rs.getString("Servicio");
                RegistroBD[12] = rs.getString("Precio");
                RegistroBD[13] = rs.getString("Status");
                RegistroBD[14] = rs.getString("Estado de Cuenta");
                RegistroBD[15] = rs.getString("numeroFactura");
                RegistroBD[16] = (rs.getString("numeroFactura") == null) ? "Sin PDF" : "Ver PDF";
                modelo.addRow(RegistroBD);
            }
            tbDeudasPorCobrar.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbDeudasPorCobrar); // Agregar checkbox a la columna 0 de cada fila
            }
            tbDeudasPorCobrar.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbDeudasPorCobrar.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbDeudasPorCobrar.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbDeudasPorCobrar.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbDeudasPorCobrar.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbDeudasPorCobrar.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbDeudasPorCobrar.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(11).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(12).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(13).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(14).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(15).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(16).setPreferredWidth(80); // Ajustar el ancho de la columna del botón
            addButtonToTable(tbDeudasPorCobrar, 16); // Agregar el botón después de configurar el modelo
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarTablaPagadas() {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Precio", "Status", "Estado de Cuenta", "Número Factura", "Ver PDF"};
            String[] RegistroBD = new String[17];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbDeudasPorCobrar);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer "
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, "
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, "
                    + "o.precio AS Precio, o.estado AS Status, o.eeCta AS 'Estado de Cuenta', f.numeroFactura "
                    + "FROM orderservice AS o "
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness "
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer "
                    + "LEFT JOIN facturas AS f ON o.id = f.id_ordenServicio " // Asegúrate de que tienes la relación correcta
                    + "WHERE o.estado != 'Inactivo' AND o.eeCta = 'Cancelada' "
                    + "ORDER BY o.fechaT ASC";
            
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[1] = rs.getString("id");
                RegistroBD[2] = rs.getString("Empresa");
                RegistroBD[3] = rs.getString("Fecha");
                RegistroBD[4] = rs.getString("Nombre");
                RegistroBD[5] = rs.getString("Tamaño");
                RegistroBD[6] = rs.getString("Direccion");
                RegistroBD[7] = rs.getString("Ciudad");
                RegistroBD[8] = rs.getString("Estado");
                RegistroBD[9] = rs.getString("Zip Code");
                RegistroBD[10] = rs.getString("Celular");
                RegistroBD[11] = rs.getString("Servicio");
                RegistroBD[12] = rs.getString("Precio");
                RegistroBD[13] = rs.getString("Status");
                RegistroBD[14] = rs.getString("Estado de Cuenta");
                RegistroBD[15] = rs.getString("numeroFactura");
                RegistroBD[16] = (rs.getString("numeroFactura") == null) ? "Sin PDF" : "Ver PDF";
                modelo.addRow(RegistroBD);
            }
            tbDeudasPorCobrar.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbDeudasPorCobrar); // Agregar checkbox a la columna 0 de cada fila
            }
            tbDeudasPorCobrar.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbDeudasPorCobrar.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbDeudasPorCobrar.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbDeudasPorCobrar.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbDeudasPorCobrar.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbDeudasPorCobrar.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbDeudasPorCobrar.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(11).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(12).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(13).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(14).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(15).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(16).setPreferredWidth(80); // Ajustar el ancho de la columna del botón
            addButtonToTable(tbDeudasPorCobrar, 16); // Agregar el botón después de configurar el modelo
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    //Codigo Para buscar dentro de la tabla por medio del txtField Busqueda
    public DefaultTableModel buscarTabla(String buscar) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        DefaultTableModel modelo = new DefaultTableModel();
        try {
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Precio", "Status", "Estado de Cuenta"};
            String[] RegistroBD = new String[15];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbDeudasPorCobrar);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer "
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, "
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, "
                    + "o.precio AS Precio, o.estado AS Status, o.eeCta AS 'Estado de Cuenta' "
                    + "FROM orderservice AS o "
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness "
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer "
                    + "WHERE o.estado != 'Inactivo' AND o.eeCta = 'deuda' AND customer.nameCustomer LIKE '%" + buscar + "%' OR customer.address LIKE '%" + buscar + "%'"
                    + "ORDER BY o.fechaT ASC";
            
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[1] = rs.getString("id");
                RegistroBD[2] = rs.getString("Empresa");
                RegistroBD[3] = rs.getString("Fecha");
                RegistroBD[4] = rs.getString("Nombre");
                RegistroBD[5] = rs.getString("Tamaño");
                RegistroBD[6] = rs.getString("Direccion");
                RegistroBD[7] = rs.getString("Ciudad");
                RegistroBD[8] = rs.getString("Estado");
                RegistroBD[9] = rs.getString("Zip Code");
                RegistroBD[10] = rs.getString("Celular");
                RegistroBD[11] = rs.getString("Servicio");
                RegistroBD[12] = rs.getString("Precio");
                RegistroBD[13] = rs.getString("Status");
                RegistroBD[14] = rs.getString("Estado de Cuenta");
                modelo.addRow(RegistroBD);
            }
            tbDeudasPorCobrar.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbDeudasPorCobrar); // Agregar checkbox a la columna 0 de cada fila
            }
            tbDeudasPorCobrar.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbDeudasPorCobrar.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbDeudasPorCobrar.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbDeudasPorCobrar.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbDeudasPorCobrar.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbDeudasPorCobrar.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbDeudasPorCobrar.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(11).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(12).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(13).setPreferredWidth(150);
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
        return modelo;
    }

    private void enviarFacturaPorEmail() {
    int selectedRow = tbDeudasPorCobrar.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione una factura para enviar por email.");
        return;
    }

    String recipientEmail = JOptionPane.showInputDialog(this, "Ingrese el email del destinatario:");
    if (recipientEmail != null && !recipientEmail.trim().isEmpty()) {
        int invoiceNumber = Integer.parseInt(tbDeudasPorCobrar.getValueAt(selectedRow, 15).toString());
        byte[] pdfData = getPDFByInvoiceNumber(invoiceNumber);

        if (pdfData != null) {
            try {
                File tempFile = File.createTempFile("factura_" + invoiceNumber, ".pdf");
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(pdfData);
                fos.close();

                EmailSender emailSender = new EmailSender();
                String subject = "Factura de Presupuesto";
                String body = "Adjunto encontrará la factura del presupuesto.";
                emailSender.sendEmail(recipientEmail, subject, body, tempFile);

                JOptionPane.showMessageDialog(this, "Email enviado con éxito a " + recipientEmail);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al preparar el archivo para enviar: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un PDF con el número de factura especificado.");
        }
    }
}


    public class InvoiceGenerator {

        public void generateInvoicePDF(String filePath, JTable table, List<Integer> selectedRows, String company, String client, String address, double subtotal, double tax, double total, String paymentMethod, double tip, int invoiceNumber, Configuracion datosEmpresa) {
            Document document = new Document(PageSize.A4);
            try {
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // Agregar logo si está disponible
                if (datosEmpresa.getLogo() != null) {
                    Image logo = Image.getInstance(datosEmpresa.getLogo());
                    logo.scaleToFit(100, 100); // Ajustar el tamaño del logo a 100x100 píxeles
                    logo.setAlignment(Element.ALIGN_LEFT);
                    document.add(logo);
                }

                // Agregar información de la empresa
                Paragraph empresa = new Paragraph();
                empresa.add(new Paragraph(datosEmpresa.getNombre(), new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD)));
                empresa.add(new Paragraph(datosEmpresa.getDireccion() + " - " + datosEmpresa.getCiudad() + ", " + datosEmpresa.getEstado() + " " + datosEmpresa.getZipcode(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
                empresa.add(new Paragraph("Teléfono: " + datosEmpresa.getTelefono(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
                empresa.add(new Paragraph("Email: " + datosEmpresa.getEmail(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
                empresa.add(new Paragraph("Web: " + datosEmpresa.getWebpage(), new Font(Font.FontFamily.TIMES_ROMAN, 12)));
                empresa.setAlignment(Element.ALIGN_CENTER);
                document.add(empresa);

                document.add(new Paragraph("\n"));

                // Agregar título
                Font fontTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
                Paragraph title = new Paragraph("Invoice N°: " + invoiceNumber, fontTitle);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                document.add(new Paragraph("\n"));
                document.add(new Paragraph("\n"));

                // Agregar información de la factura
//                document.add(new Paragraph("Empresa: " + company));
//                document.add(new Paragraph("Cliente: " + client));
//                document.add(new Paragraph("Dirección: " + address));
                document.add(new Paragraph("Fecha: " + new Date().toString()));
                document.add(new Paragraph("Método de Pago: " + paymentMethod));
                document.add(new Paragraph("\n"));

                // Crear la tabla de productos/servicios
                PdfPTable pdfTable = new PdfPTable(8); // Número de columnas
                pdfTable.setWidthPercentage(100); // Ancho total de la tabla

                // Definir los anchos relativos de las columnas
                float[] columnWidths = {1.3f, 1.5f, 1.2f, 1f, 1f, 1f, 1f, 1f}; // Ajusta estos valores según sea necesario
                pdfTable.setWidths(columnWidths);

                // Agregar encabezados de columna
                String[] headers = {"Customer", "Address", "City", "State", "Zip Code", "Cellphone", "Service", "Price"};
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    pdfTable.addCell(cell);
                }

                // Agregar filas de datos seleccionadas
                for (int rowIndex : selectedRows) {
                    pdfTable.addCell(table.getValueAt(rowIndex, 4).toString()); // Nombre
                    pdfTable.addCell(table.getValueAt(rowIndex, 6).toString()); // Dirección
                    pdfTable.addCell(table.getValueAt(rowIndex, 7).toString()); // Ciudad
                    pdfTable.addCell(table.getValueAt(rowIndex, 8).toString()); // Estado
                    pdfTable.addCell(table.getValueAt(rowIndex, 9).toString()); // Zip Code
                    pdfTable.addCell(table.getValueAt(rowIndex, 10).toString()); // Celular
                    pdfTable.addCell(table.getValueAt(rowIndex, 11).toString()); // Servicio
                    pdfTable.addCell(table.getValueAt(rowIndex, 12).toString()); // Precio
                }
                document.add(pdfTable);
                document.add(new Paragraph("\n"));

                // Agregar totales
                document.add(new Paragraph("Subtotal: $" + String.format("%.2f", subtotal)));
                document.add(new Paragraph("Impuesto: $" + String.format("%.2f", tax)));
                document.add(new Paragraph("Propina: $" + String.format("%.2f", tip)));
                document.add(new Paragraph("Total: $" + String.format("%.2f", total)));

                document.close();
                JOptionPane.showMessageDialog(null, "Factura generada correctamente en: " + filePath);

            } catch (DocumentException | IOException e) {
                JOptionPane.showMessageDialog(null, "Error al generar la factura: " + e.getMessage());
            }
        }

        public void savePDFToDatabase(String filePath, int orderId, int paymentMethodId, int invoiceNumber) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }            
            PreparedStatement pstmt = null;
            FileInputStream fis = null;

            try {
                
                String sql = "INSERT INTO facturas (pdf, id_ordenServicio, id_pagarcon, numeroFactura) VALUES (?, ?, ?, ?)";
                
                pstmt = connection.prepareCall(sql);

                File pdfFile = new File(filePath);
                fis = new FileInputStream(pdfFile);
                pstmt.setBinaryStream(1, fis, (int) pdfFile.length());
                pstmt.setInt(2, orderId);
                pstmt.setInt(3, paymentMethodId);
                pstmt.setInt(4, invoiceNumber);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "PDF guardado en la base de datos exitosamente.");

            } catch (SQLException | FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error al guardar el PDF en la base de datos: " + e.getMessage());
            } finally {
               Conectar.getInstancia().devolverConexion(connection);
            }
        }
    }

    private int getNextInvoiceNumber() {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        Statement stmt = null;
        ResultSet rs = null;
        int nextInvoiceNumber = 1;

        try {
            String sql = "SELECT MAX(numeroFactura) AS maxInvoiceNumber FROM facturas";
            
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                nextInvoiceNumber = rs.getInt("maxInvoiceNumber") + 1;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el siguiente número de factura: " + e.getMessage());
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
        }

        txtProximoNumeroFactura.setText(String.valueOf(nextInvoiceNumber));
        return nextInvoiceNumber;
    }

    private void updateOrderStatus(int orderId, String status) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        PreparedStatement pstmt = null;

        try {
            String sql = "UPDATE orderservice SET eeCta = ? WHERE id = ?";
            
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "El estado de la orden ha sido actualizado a " + status);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el estado de la orden: " + e.getMessage());
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    private int getPaymentMethodId(String paymentMethodName) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int paymentMethodId = -1;

        try {
            String sql = "SELECT id_formadepago FROM formadepago WHERE nombre = ?";
            
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, paymentMethodName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                paymentMethodId = rs.getInt("id");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del método de pago: " + e.getMessage());
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
        }

        return paymentMethodId;
    }

    private byte[] getPDFByInvoiceNumber(int invoiceNumber) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        byte[] pdfData = null;

        try {
            String sql = "SELECT pdf FROM facturas WHERE numeroFactura = ?";
            
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, invoiceNumber);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pdfData = rs.getBytes("pdf");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el PDF de la base de datos: " + e.getMessage());
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
        }

        return pdfData;
    }

    private void openPDF(byte[] pdfData) {
        try {
            File tempFile = File.createTempFile("factura", ".pdf");
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(pdfData);
            fos.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            } else {
                JOptionPane.showMessageDialog(null, "No se puede abrir el PDF en este sistema.");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al abrir el PDF: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tbDeudasPorCobrar = new javax.swing.JTable();
        btnBusquedaEmpresa = new javax.swing.JButton();
        txtIdEmpresa = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        dateInicio = new com.toedter.calendar.JDateChooser();
        dateFin = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbxEmpresa = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        cbSeleccionaTodo = new javax.swing.JCheckBox();
        btnBusquedaFechaEmpresa = new javax.swing.JButton();
        btnMostrarDeudas = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        btnMostrarCanceladas = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtEmpresa = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtST = new javax.swing.JTextField();
        txtTax = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        btnPagar = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        cbxPagarCon = new javax.swing.JComboBox<>();
        txtIdPagarCon = new javax.swing.JTextField();
        txtProximoNumeroFactura = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btnAbrirPDF = new javax.swing.JButton();
        txtNumeroFactura = new javax.swing.JTextField();
        btnEnviarEmail = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Deudas por Cobrar");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setBackground(new java.awt.Color(204, 255, 204));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbDeudasPorCobrar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Seleccion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbDeudasPorCobrar.setRowHeight(23);
        tbDeudasPorCobrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDeudasPorCobrarMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(tbDeudasPorCobrar);

        jPanel12.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 1220, 240));

        btnBusquedaEmpresa.setText("Busqueda Empresa");
        btnBusquedaEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusquedaEmpresaActionPerformed(evt);
            }
        });
        jPanel12.add(btnBusquedaEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 160, 30));
        jPanel12.add(txtIdEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 70, 30));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 10, -1, -1));
        jPanel12.add(dateInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 40, 150, -1));
        jPanel12.add(dateFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 40, 140, -1));

        jLabel1.setText("Inicio");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 20, -1, -1));

        jLabel2.setText("Final");
        jPanel12.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 20, -1, -1));

        jLabel3.setText("Empresa");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        cbxEmpresa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxEmpresaItemStateChanged(evt);
            }
        });
        jPanel12.add(cbxEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 170, -1));

        jLabel4.setText("Busqueda");
        jPanel12.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });
        jPanel12.add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 170, -1));

        cbSeleccionaTodo.setText("Selección");
        cbSeleccionaTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSeleccionaTodoActionPerformed(evt);
            }
        });
        jPanel12.add(cbSeleccionaTodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 120, -1));

        btnBusquedaFechaEmpresa.setText("Busqueda por Fecha-Empresa");
        btnBusquedaFechaEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusquedaFechaEmpresaActionPerformed(evt);
            }
        });
        jPanel12.add(btnBusquedaFechaEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 80, 250, -1));

        btnMostrarDeudas.setText("Mostrar Todo Deuda");
        btnMostrarDeudas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarDeudasActionPerformed(evt);
            }
        });
        jPanel12.add(btnMostrarDeudas, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 30, 170, -1));
        jPanel12.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, 90, 30));

        jLabel15.setText("Cliente");
        jPanel12.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        btnMostrarCanceladas.setText("Mostrar todo Canceladas");
        btnMostrarCanceladas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarCanceladasActionPerformed(evt);
            }
        });
        jPanel12.add(btnMostrarCanceladas, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, 170, -1));

        getContentPane().add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 1268, 376));

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText("Empresa");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 18, -1, -1));
        jPanel1.add(txtEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 15, 233, -1));

        jLabel6.setText("Cliente");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 57, -1, -1));
        jPanel1.add(txtCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 54, 233, -1));
        jPanel1.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 76, 233, -1));

        jLabel7.setText("Dirección");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 79, -1, -1));

        jLabel8.setText("Deuda Cliente");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 18, -1, -1));

        jLabel9.setText("Tax");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(432, 57, -1, -1));

        jLabel10.setText("Total");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 90, -1, -1));
        jPanel1.add(txtST, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 15, 175, -1));
        jPanel1.add(txtTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 54, 175, -1));
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 90, 175, -1));

        jLabel11.setText("DeudaTotal");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(692, 18, -1, -1));
        jPanel1.add(txtSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 20, 120, -1));

        jLabel12.setText("Tax");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 60, -1, -1));
        jPanel1.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 60, 120, -1));

        jLabel13.setText("Tip");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 90, -1, -1));
        jPanel1.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 90, 120, -1));

        jLabel14.setText("Total");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 120, -1, -1));

        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 120, 118, -1));

        btnPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        btnPagar.setText("PAGAR");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });
        jPanel1.add(btnPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 70, -1, -1));

        jLabel16.setText("Pagar con:");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(933, 18, -1, -1));

        cbxPagarCon.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbxPagarCon.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxPagarConItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxPagarCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1007, 15, 128, -1));
        jPanel1.add(txtIdPagarCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1147, 15, -1, -1));
        jPanel1.add(txtProximoNumeroFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(933, 121, 82, -1));

        jLabel17.setText("Factura Número");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(933, 93, -1, -1));

        btnAbrirPDF.setText("Abrir PDF");
        btnAbrirPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirPDFActionPerformed(evt);
            }
        });
        jPanel1.add(btnAbrirPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 140, -1, -1));
        jPanel1.add(txtNumeroFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(404, 140, 80, -1));

        btnEnviarEmail.setText("EnviarEmail");
        btnEnviarEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarEmailActionPerformed(evt);
            }
        });
        jPanel1.add(btnEnviarEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 130, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 394, 1270, 200));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbDeudasPorCobrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDeudasPorCobrarMouseClicked
        int column = tbDeudasPorCobrar.getColumnModel().getColumnIndexAtX(evt.getX()); // get the column of the button
        int row = evt.getY() / tbDeudasPorCobrar.getRowHeight(); // get the row of the button

        // Checking the row or column is valid or not
        if (row < tbDeudasPorCobrar.getRowCount() && row >= 0 && column < tbDeudasPorCobrar.getColumnCount() && column >= 0) {
            Object value = tbDeudasPorCobrar.getValueAt(row, column);
            if (value.equals("Ver PDF")) {
                int invoiceNumber = Integer.parseInt(tbDeudasPorCobrar.getValueAt(row, 15).toString());
                byte[] pdfData = getPDFByInvoiceNumber(invoiceNumber);

                if (pdfData != null) {
                    openPDF(pdfData);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró un PDF con el número de factura especificado.");
                }
            }
        }
    }//GEN-LAST:event_tbDeudasPorCobrarMouseClicked

    private void btnBusquedaEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusquedaEmpresaActionPerformed
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ID = txtIdEmpresa.getText();
        String ID_buscar = "";
        if (!(ID.equals(""))) {
            ID_buscar = "WHERE o.id_empresa= '" + ID + "'";
        }
        try {
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Precio", "Status", "Estado de Cuenta"};
            String[] RegistroBD = new String[15];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbDeudasPorCobrar);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer "
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, "
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, "
                    + "o.precio AS Precio, o.estado AS Status, o.eeCta AS 'Estado de Cuenta' "
                    + "FROM orderservice AS o "
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness "
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer "
                    + ID_buscar + "AND o.estado != 'Inactivo' AND o.eeCta = 'deuda' "
                    + "ORDER BY o.fechaT ASC";
            
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[1] = rs.getString("id");
                RegistroBD[2] = rs.getString("Empresa");
                RegistroBD[3] = rs.getString("Fecha");
                RegistroBD[4] = rs.getString("Nombre");
                RegistroBD[5] = rs.getString("Tamaño");
                RegistroBD[6] = rs.getString("Direccion");
                RegistroBD[7] = rs.getString("Ciudad");
                RegistroBD[8] = rs.getString("Estado");
                RegistroBD[9] = rs.getString("Zip Code");
                RegistroBD[10] = rs.getString("Celular");
                RegistroBD[11] = rs.getString("Servicio");
                RegistroBD[12] = rs.getString("Precio");
                RegistroBD[13] = rs.getString("Status");
                RegistroBD[14] = rs.getString("Estado de Cuenta");
                modelo.addRow(RegistroBD);
            }
            tbDeudasPorCobrar.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbDeudasPorCobrar); // Agregar checkbox a la columna 0 de cada fila
            }
            tbDeudasPorCobrar.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbDeudasPorCobrar.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbDeudasPorCobrar.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbDeudasPorCobrar.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbDeudasPorCobrar.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbDeudasPorCobrar.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbDeudasPorCobrar.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(11).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(12).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(13).setPreferredWidth(150);
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }//GEN-LAST:event_btnBusquedaEmpresaActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cbxEmpresaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxEmpresaItemStateChanged
        MostrarCodigoEmpresa(cbxEmpresa, txtIdEmpresa);
    }//GEN-LAST:event_cbxEmpresaItemStateChanged

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        buscarTabla(txtBusqueda.getText());
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void cbSeleccionaTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSeleccionaTodoActionPerformed
        if (cbSeleccionaTodo.isSelected()) {
            cbSeleccionaTodo.setText("Deseleccionar Todo");
            for (int i = 0; i < tbDeudasPorCobrar.getRowCount(); i++) {
                tbDeudasPorCobrar.setValueAt(true, i, 0);
            }
        } else {
            cbSeleccionaTodo.setText("Seleccionar Todo");
            for (int i = 0; i < tbDeudasPorCobrar.getRowCount(); i++) {
                tbDeudasPorCobrar.setValueAt(false, i, 0);
            }
        }
    }//GEN-LAST:event_cbSeleccionaTodoActionPerformed

    private void btnBusquedaFechaEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusquedaFechaEmpresaActionPerformed
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaInicio = dateFormat.format(dateInicio.getDate());
        String fechaFin = dateFormat.format(dateFin.getDate());
        String ID = txtIdEmpresa.getText();
        String ID_buscar = "";
        if (!(ID.equals(""))) {
            ID_buscar = "WHERE o.id_empresa= '" + ID + "'";
        }
        try {
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Precio", "Status"};
            String[] RegistroBD = new String[15];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbDeudasPorCobrar);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer "
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, "
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, "
                    + "o.precio AS Precio, o.estado AS Status "
                    + "FROM orderservice AS o "
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness "
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer "
                    + ID_buscar + "AND o.fechaT BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "' AND o.estado != 'Inactivo' "
                    + "ORDER BY o.fechaT ASC";
            
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[1] = rs.getString("id");
                RegistroBD[2] = rs.getString("Empresa");
                RegistroBD[3] = rs.getString("Fecha");
                RegistroBD[4] = rs.getString("Nombre");
                RegistroBD[5] = rs.getString("Tamaño");
                RegistroBD[6] = rs.getString("Direccion");
                RegistroBD[7] = rs.getString("Ciudad");
                RegistroBD[8] = rs.getString("Estado");
                RegistroBD[9] = rs.getString("Zip Code");
                RegistroBD[10] = rs.getString("Celular");
                RegistroBD[11] = rs.getString("Servicio");
                RegistroBD[12] = rs.getString("Precio");
                RegistroBD[13] = rs.getString("Status");
                modelo.addRow(RegistroBD);
            }
            tbDeudasPorCobrar.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbDeudasPorCobrar); // Agregar checkbox a la columna 0 de cada fila
            }
            tbDeudasPorCobrar.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbDeudasPorCobrar.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbDeudasPorCobrar.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbDeudasPorCobrar.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbDeudasPorCobrar.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbDeudasPorCobrar.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbDeudasPorCobrar.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbDeudasPorCobrar.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbDeudasPorCobrar.getColumnModel().getColumn(11).setPreferredWidth(150);
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }//GEN-LAST:event_btnBusquedaFechaEmpresaActionPerformed

    private void btnMostrarDeudasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarDeudasActionPerformed
        MostrarTablaDeuda();
    }//GEN-LAST:event_btnMostrarDeudasActionPerformed

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        try {
            // Crear una instancia de Configuracion para obtener los datos de la empresa
            Configuracion configuracion = new Configuracion();
            Configuracion datosEmpresa = configuracion.getConfiguracionActual();

            double subtotal = 0.0;
            double impuesto = 0.06; // Impuesto del 6%
            double total = 0.0;
            double tip = 0.0; // Propina inicialmente en 0
            String Reportes = "";
            JTextArea area = new JTextArea();
            int paymentMethodId = Integer.parseInt(txtIdPagarCon.getText()); // Obtener ID del método de pago seleccionado
            int nextInvoiceNumber = getNextInvoiceNumber(); // Obtener el siguiente número de factura

            List<Integer> selectedRows = new ArrayList<>();
            List<Integer> orderIds = new ArrayList<>();

            if (Seleccionados(0)) {
                for (int i = 0; i < tbDeudasPorCobrar.getRowCount(); i++) {
                    Object value = tbDeudasPorCobrar.getValueAt(i, 0);
                    if (value instanceof Boolean) {
                        boolean sel = (boolean) value;
                        if (sel) {
                            selectedRows.add(i);
                            orderIds.add(Integer.parseInt(tbDeudasPorCobrar.getValueAt(i, 1).toString())); // Obtener ID de la orden de servicio
                            double precio = Double.parseDouble(tbDeudasPorCobrar.getValueAt(i, 12).toString());
                            subtotal += precio;
                            Reportes += "Empresa : " + tbDeudasPorCobrar.getValueAt(i, 2) + " ; Cliente : " + tbDeudasPorCobrar.getValueAt(i, 4)
                                    + " precio : " + String.format("%.2f", precio) + "\n";
                        }
                    }
                }

                // Calcular impuesto
                double impuestoTotal = subtotal * impuesto;

                // Calcular total sin propina
                total = subtotal + impuestoTotal;

                // Mostrar subtotal, impuesto y total antes de propina
                area.setText("Subtotal: " + String.format("%.2f", subtotal) + "\nImpuesto (6%): " + String.format("%.2f", impuestoTotal) + "\nTotal sin propina: " + String.format("%.2f", total) + "\n\n" + Reportes);

                JOptionPane.showMessageDialog(this, area, "Información Detallada de Ventas", JOptionPane.INFORMATION_MESSAGE);

                // Agregar campo para ingresar propina
                String input = JOptionPane.showInputDialog(this, "Ingrese el monto de propina a añadir:", "Propina", JOptionPane.QUESTION_MESSAGE);
                try {
                    if (input != null && !input.isEmpty()) {
                        tip = Double.parseDouble(input);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Monto de propina inválido. Se aplicará propina de $0.", "Error", JOptionPane.ERROR_MESSAGE);
                    tip = 0.0;
                }

                total += tip;

                if ("Tarjeta de crédito/débito".equals(cbxPagarCon.getSelectedItem())) { // Tarjeta de crédito/débito
                    total *= 1.03; // Añadir recargo del 3%
                    JOptionPane.showMessageDialog(this, "El total con recargo del 3% es: $" + String.format("%.2f", total), "Total con Recargo", JOptionPane.INFORMATION_MESSAGE);
                }

                // Generar PDF de la factura
                InvoiceGenerator invoiceGenerator = new InvoiceGenerator();
                String filePath = "factura_" + System.currentTimeMillis() + ".pdf"; // Genera un nombre de archivo único
                String company = txtEmpresa.getText();
                String client = txtCliente.getText();
                String address = txtDireccion.getText();
                invoiceGenerator.generateInvoicePDF(filePath, tbDeudasPorCobrar, selectedRows, company, client, address, subtotal, impuestoTotal, total, (String) cbxPagarCon.getSelectedItem(), tip, nextInvoiceNumber, datosEmpresa);

                // Guardar el PDF en la base de datos y actualizar el estado de la orden de servicio
                for (int orderId : orderIds) {
                    invoiceGenerator.savePDFToDatabase(filePath, orderId, paymentMethodId, nextInvoiceNumber);
                    updateOrderStatus(orderId, "Cancelada");
                }

                // Actualizar el próximo número de factura
                getNextInvoiceNumber();

                //Actualizar Mostrar Tabla
                MostrarTablaDeuda();

                // Abrir el PDF generado
                File file = new File(filePath);
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Antes de obtener los datos, debe de seleccionar por lo menos un checkbox.",
                        "Mensaje", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPagarActionPerformed

    private void btnMostrarCanceladasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarCanceladasActionPerformed
        MostrarTablaPagadas();
    }//GEN-LAST:event_btnMostrarCanceladasActionPerformed

    private void cbxPagarConItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxPagarConItemStateChanged
        MostrarCodigoFormaDePago(cbxPagarCon, txtIdPagarCon);
    }//GEN-LAST:event_cbxPagarConItemStateChanged

    private void btnAbrirPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirPDFActionPerformed
        try {
            int invoiceNumber = Integer.parseInt(txtNumeroFactura.getText());
            byte[] pdfData = getPDFByInvoiceNumber(invoiceNumber);

            if (pdfData != null) {
                openPDF(pdfData);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un PDF con el número de factura especificado.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Número de factura inválido.");
        }
    }//GEN-LAST:event_btnAbrirPDFActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void btnEnviarEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarEmailActionPerformed
        enviarFacturaPorEmail();
    }//GEN-LAST:event_btnEnviarEmailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAbrirPDF;
    public javax.swing.JButton btnBusquedaEmpresa;
    public javax.swing.JButton btnBusquedaFechaEmpresa;
    public javax.swing.JButton btnEnviarEmail;
    public javax.swing.JButton btnMostrarCanceladas;
    public javax.swing.JButton btnMostrarDeudas;
    public javax.swing.JButton btnPagar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JCheckBox cbSeleccionaTodo;
    private javax.swing.JComboBox<String> cbxEmpresa;
    private javax.swing.JComboBox<String> cbxPagarCon;
    private com.toedter.calendar.JDateChooser dateFin;
    private com.toedter.calendar.JDateChooser dateInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    public javax.swing.JTable tbDeudasPorCobrar;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtEmpresa;
    private javax.swing.JTextField txtId;
    public javax.swing.JTextField txtIdEmpresa;
    private javax.swing.JTextField txtIdPagarCon;
    private javax.swing.JTextField txtNumeroFactura;
    private javax.swing.JTextField txtProximoNumeroFactura;
    private javax.swing.JTextField txtST;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTax;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    public void MostrarEmpresa(JComboBox cbxEmpresa) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "";
        sql = "select * from bussiness";
        Statement st;
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxEmpresa.removeAllItems();
            while (rs.next()) {
                cbxEmpresa.addItem(rs.getString("nameBusiness"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar en Combo " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoEmpresa(JComboBox cbxEmpresa, JTextField idBusiness) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String consuta = "select bussiness.idBusiness from bussiness where bussiness.nameBusiness=?";
        try {
            CallableStatement cs = connection.prepareCall(consuta);
            cs.setString(1, cbxEmpresa.getSelectedItem().toString());
            cs.execute();
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                idBusiness.setText(rs.getString("idBusiness"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoFormaDePago(JComboBox cbxPagarCon, JTextField idPagarCon) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String consuta = "select formadepago.id_formadepago from formadepago where formadepago.nombre=?";

        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxPagarCon.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ninguna forme de pago.");
                return;
            }
            CallableStatement cs = connection.prepareCall(consuta);

            Object selectedValue = cbxPagarCon.getSelectedItem();
            if (selectedValue != null) {
                String valorSeleccionado = selectedValue.toString();
                cs.setString(1, valorSeleccionado);
                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    idPagarCon.setText(rs.getString("id_formadepago"));

                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarFormaDePago(JComboBox cbxPagarCon) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "";
        sql = "select * from formadepago";
        Statement st;

        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxPagarCon.removeAllItems();

            while (rs.next()) {

                cbxPagarCon.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    private boolean Seleccionados(int pos) {
        int contador = 0;
        boolean bandera = true;
        for (int i = 0; i < tbDeudasPorCobrar.getRowCount(); i++) {
            Object value = tbDeudasPorCobrar.getValueAt(i, pos);
            if (value instanceof Boolean) {
                boolean seleccion = (boolean) value;
                if (seleccion) {
                    contador++;
                }

            }
        }
        if (contador == 0) {
            bandera = false;
        }
        return bandera;
    }

    public void addButtonToTable(JTable table, int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(columnIndex).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private String label;
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    if (!label.equals("Ver PDF")) {
                        JOptionPane.showMessageDialog(null, "No se encontró un PDF asociado.");
                        return;
                    }
                    int row = tbDeudasPorCobrar.getSelectedRow();
                    int invoiceNumber = Integer.parseInt(tbDeudasPorCobrar.getValueAt(row, 15).toString());
                    byte[] pdfData = getPDFByInvoiceNumber(invoiceNumber);
                    if (pdfData != null) {
                        openPDF(pdfData);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró un PDF con el número de factura especificado.");
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            return button;
        }

        public Object getCellEditorValue() {
            return label;
        }
    }

    public class EmailSender {

        private String username;
        private String password;

        public void sendEmail(String toEmail, String subject, String body, File attachment) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            if (attachment != null) {
                MimeBodyPart attachPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachPart.setDataHandler(new DataHandler(source));
                attachPart.setFileName(attachment.getName());
                multipart.addBodyPart(attachPart);
            }

            message.setContent(multipart);
            Transport.send(message);

            System.out.println("Email enviado con éxito!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

}
