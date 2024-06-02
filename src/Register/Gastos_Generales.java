package Register;

import com.toedter.calendar.JDateChooser;
import conectar.Conectar;
import java.awt.HeadlessException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Gastos_Generales extends javax.swing.JInternalFrame {

    private JDateChooser dateChooser;
    DefaultTableModel model = new DefaultTableModel();
    int id_PagosGenerales;
    int tipodePago;
    String reciboNumero;
    double subTotal, taxes, tip, total;
    int id_proveedor;
    int id_formadepago;
    Date fecha;
    String estado;

    public Gastos_Generales(int id_PagosGenerales, int tipodePago, String reciboNumero, double subTotal, double taxes, double tip, double total, int id_proveedor, int id_formadepago, Date fecha, String estado) {
        this.id_PagosGenerales = id_PagosGenerales;
        this.tipodePago = tipodePago;
        this.reciboNumero = reciboNumero;
        this.subTotal = subTotal;
        this.taxes = taxes;
        this.tip = tip;
        this.total = total;
        this.id_proveedor = id_proveedor;
        this.id_formadepago = id_formadepago;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId_PagosGenerales() {
        return id_PagosGenerales;
    }

    public void setId_PagosGenerales(int id_PagosGenerales) {
        this.id_PagosGenerales = id_PagosGenerales;
    }

    public int getTipodePago() {
        return tipodePago;
    }

    public void setTipodePago(int tipodePago) {
        this.tipodePago = tipodePago;
    }

    public String getReciboNumero() {
        return reciboNumero;
    }

    public void setReciboNumero(String reciboNumero) {
        this.reciboNumero = reciboNumero;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public int getId_formadepago() {
        return id_formadepago;
    }

    public void setId_formadepago(int id_formadepago) {
        this.id_formadepago = id_formadepago;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    //Conexión
    Conectar con = new Conectar();
    Connection connect = con.getConexion();
    PreparedStatement ps;
    ResultSet rs;

    public Gastos_Generales() {
        initComponents();
        AutoCompleteDecorator.decorate(cbxTipodeGastGen);
        AutoCompleteDecorator.decorate(cbxPagarCon);
        AutoCompleteDecorator.decorate(cbxProveedor);
        MostrarTipoPagos(cbxTipodeGastGen);
        MostrarFormaDePago(cbxPagarCon);
        MostrarProveedor(cbxProveedor);
        MostrarDatos("");

    }

    public void MostrarDatos(String Valores) {
        try {
            String[] TitulosTabla = {"id", "Tipo", "Recibo", "SubTotal", "Taxes", "Tip", "Total", "Proveedor", "Forma de Pago", "Fecha", "Estado"};
            String[] RegistroBD = new String[11];

            model = new DefaultTableModel(null, TitulosTabla);

            String ConsultaSQL = "SELECT dpg.id_PagosGenerales AS id, tpg.nombre AS Tipo, dpg.reciboNumero AS Recibo, dpg.subTotal As SubTotal, dpg.taxes AS Taxes, dpg.tip AS Tip, dpg.total AS Total, s.nameSuplier AS Proveedor, f.nombre AS 'Forma de Pago', dpg.fecha AS Fecha, dpg.estado\n"
                    + "FROM detallepagosgenerales dpg\n"
                    + "INNER JOIN tipos_pagosgenerales tpg ON dpg.id_tipodePago=tpg.id_pagos\n"
                    + "INNER JOIN suplier s ON dpg.id_proveedor=s.idSuplier\n"
                    + "INNER Join formadepago f ON dpg.id_PagosGenerales=f.id_formadepago ORDER BY dpg.id_PagosGenerales ASC";

            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {

                RegistroBD[0] = result.getString(1);
                RegistroBD[1] = result.getString(2);
                RegistroBD[2] = result.getString(3);
                RegistroBD[3] = result.getString(4);
                RegistroBD[4] = result.getString(5);
                RegistroBD[5] = result.getString(6);
                RegistroBD[6] = result.getString(7);
                RegistroBD[7] = result.getString(8);
                RegistroBD[8] = result.getString(9);
                RegistroBD[9] = result.getString(10);
                RegistroBD[10] = result.getString(11);

                model.addRow(RegistroBD);

            }
            tbGastosGenerales.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar la Tabla " + e.toString());
        }
    }

    public void Guardar() {
        int id_PagosGenerales;
        int tipodePago;
        String reciboNumero;
        double subTotal, taxes, tip, total;
        int id_proveedor;
        int id_formadepago;
        String fecha;
        String estado;

        tipodePago = Integer.parseInt(txtIdTipodeGastGen.getText());
        reciboNumero = txtRecibo.getText();
        subTotal = Double.parseDouble(txtSubTotal.getText());
        taxes = Double.parseDouble(txtTaxes.getText());
        tip = Double.parseDouble(txtTip.getText());
        total = Double.parseDouble(txtTotal.getText());
        id_proveedor = Integer.parseInt(txtIdProveedor.getText());
        id_formadepago = Integer.parseInt(txtIdPagarCon.getText());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fecha = sdf.format(dateFecha.getDate());

//Consulta para evitar duplicados
        String consulta = "SELECT * detallepagosgenerales WHERE tipodepago=? AND fecha=?";

//Consulta para insertar los datos
        String sql = "INSERT INTO detallepagosgenerales (id_tipodePago,reciboNumero,"
                + "subTotal,taxes,tip,total,id_proveedor,id_formadepago,fecha)VALUES(?,?,?,?,?,?,?,?,?)";

//Para almacenar los datos empleo un try cash
        try {
//Preparando la conexion a sql
            PreparedStatement pst = connect.prepareStatement(sql);

            pst.setInt(1, tipodePago);
            pst.setString(2, reciboNumero);
            pst.setDouble(3, subTotal);
            pst.setDouble(4, taxes);
            pst.setDouble(5, tip);
            pst.setDouble(6, total);
            pst.setInt(7, id_proveedor);
            pst.setInt(8, id_formadepago);
            pst.setString(9, fecha);

//Declarar otra variable para validar los registros
            int n = pst.executeUpdate();

//si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se  Guardo con exito");
            }
            
            txtRecibo.setText("");
            txtSubTotal.setText("");
            txtTaxes.setText("");
            txtTip.setText("");
            txtTotal.setText("");
            txtRecibo.requestFocus();
            MostrarDatos("");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar registro " + e.toString());
        }

    }

    public void Modificar(JTextField id, JComboBox TipodePago, JTextField Recibo, JTextField SubTotal, JTextField Taxes, JTextField Tip,
            JTextField Total, JComboBox Proveedor, JComboBox FormaPago, JDateChooser fecha) {

        try {

            setId_PagosGenerales(Integer.parseInt(id.getText()));
            setTipodePago(Integer.parseInt(txtIdTipodeGastGen.getText()));
            setReciboNumero(Recibo.getText());
            setSubTotal(Double.parseDouble(SubTotal.getText()));
            setTaxes(Double.parseDouble(Taxes.getText()));
            setTip(Double.parseDouble(Tip.getText()));
            setTotal(Double.parseDouble(Total.getText()));
            setId_proveedor(Integer.parseInt(txtIdProveedor.getText()));
            setId_formadepago(Integer.parseInt(txtIdPagarCon.getText()));

            // Verificar si la fecha no es null
            Date selectedDate = fecha.getDate();
            if (selectedDate == null) {
                System.out.println("Por favor, seleccione una fecha.");
                return;
            }

            // Convertir la fecha del JDateChooser a tipo java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
            setFecha(sqlDate);

            String sql = "UPDATE detallepagosgenerales SET id_tipodePago=?, reciboNumero=?, subTotal=?, taxes=?, "
                    + "tip=?, total=?, id_proveedor=?, id_formadepago=?, fecha=? WHERE id_PagosGenerales=?";

            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setInt(1, getTipodePago());
            ps.setString(2, getReciboNumero());
            ps.setDouble(3, getSubTotal());
            ps.setDouble(4, getTaxes());
            ps.setDouble(5, getTip());
            ps.setDouble(6, getTotal());
            ps.setInt(7, getId_proveedor());
            ps.setInt(8, getId_formadepago());
            ps.setDate(9, (java.sql.Date) getFecha());
            ps.setInt(10, getId_PagosGenerales()); // Modificar el registro con el ID correspondiente
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Modificación Exitosa");
            MostrarDatos("");

        } catch (NumberFormatException e) {
            System.out.println("Error de formato de número: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al Modificar: " + e.toString());
        }
    }

    public void Borrar(JTextField id) {

        setId_PagosGenerales(Integer.parseInt(id.getText()));

        String consulta = "DELETE FROM detallepagosgenerales WHERE id_PagosGenerales=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);
            cs.setInt(1, getId_PagosGenerales());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");

            MostrarDatos("");

        } catch (HeadlessException | SQLException e) {

            JOptionPane.showMessageDialog(null, "No se Elimino registro " + e.toString());
        }

    }

    public void Seleccionar(JTable Tabla, JTextField id, JComboBox Tipo, JTextField Recibo, JTextField SubTotal, JTextField Taxes, JTextField Tip, JTextField Total, JComboBox Proveedor, JComboBox FormaPago, JDateChooser fecha) {

        try {
            int fila = tbGastosGenerales.getSelectedRow();

            if (fila >= 0) {
                id.setText(tbGastosGenerales.getValueAt(fila, 0).toString());
                Tipo.setSelectedItem(tbGastosGenerales.getValueAt(fila, 1).toString());
                Recibo.setText(tbGastosGenerales.getValueAt(fila, 2).toString());
                SubTotal.setText(tbGastosGenerales.getValueAt(fila, 3).toString());
                Taxes.setText(tbGastosGenerales.getValueAt(fila, 4).toString());
                Tip.setText(tbGastosGenerales.getValueAt(fila, 5).toString());
                Total.setText(tbGastosGenerales.getValueAt(fila, 6).toString());
                Proveedor.setSelectedItem(tbGastosGenerales.getValueAt(fila, 7));
                FormaPago.setSelectedItem(tbGastosGenerales.getValueAt(fila, 8).toString());
                fecha.setDate((Date) tbGastosGenerales.getValueAt(fila, 9));

            }

        } catch (Exception e) {
        }
    }
    
    public void Limpiar(){
        txtRecibo.setText("");
        txtRecibo.requestFocus();
        txtSubTotal.setText("");
        txtTaxes.setText("");
        txtTip.setText("");
        txtTotal.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtRecibo = new javax.swing.JTextField();
        txtTaxes = new javax.swing.JTextField();
        txtSubTotal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        dateFecha = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        cbxTipodeGastGen = new javax.swing.JComboBox<>();
        txtIdTipodeGastGen = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTip = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        cbxPagarCon = new javax.swing.JComboBox<>();
        txtIdPagarCon = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbxProveedor = new javax.swing.JComboBox<>();
        txtIdProveedor = new javax.swing.JTextField();
        txtId = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnBorrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel11 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Pagos Generales");

        jPanel3.setBackground(new java.awt.Color(153, 153, 255));

        jLabel13.setText("Recibo N°");

        jLabel14.setText("SubTotal");

        jLabel15.setText("Taxes");

        jLabel16.setText("Total");

        jLabel17.setText("Fecha");

        dateFecha.setDateFormatString("yyyy-MM-dd");

        jLabel1.setText("Tipo");

        cbxTipodeGastGen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipodeGastGenItemStateChanged(evt);
            }
        });

        jLabel2.setText("Propina");

        jLabel49.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel49.setText("Pagar con");

        cbxPagarCon.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxPagarConItemStateChanged(evt);
            }
        });
        cbxPagarCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPagarConActionPerformed(evt);
            }
        });

        jLabel3.setText("Proveedor");

        cbxProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxProveedorItemStateChanged(evt);
            }
        });

        txtIdProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTip, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRecibo)
                            .addComponent(txtTaxes)
                            .addComponent(txtSubTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))))
                .addGap(37, 37, 37)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTotal)
                            .addComponent(dateFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(cbxTipodeGastGen, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addComponent(txtIdTipodeGastGen, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxPagarCon, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdProveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdPagarCon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3)
                                            .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtIdTipodeGastGen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(20, 20, 20)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel49)
                                            .addComponent(cbxPagarCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtIdPagarCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel13)
                                            .addComponent(txtRecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1)
                                            .addComponent(cbxTipodeGastGen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(22, 22, 22)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel14)
                                            .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(37, 37, 37)
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(dateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel16)
                                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(44, 44, 44))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        btnBorrar.setBackground(new java.awt.Color(0, 102, 255));
        btnBorrar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnBorrar.setText("Eliminar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        tbGastosGenerales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbGastosGenerales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbGastosGeneralesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbGastosGenerales);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel11.setText("Descripción");

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarKeyTyped(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(0, 102, 255));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnNew.setBackground(new java.awt.Color(0, 102, 255));
        btnNew.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/new product.png"))); // NOI18N
        btnNew.setText("Nuevo");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(0, 102, 255));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(0, 102, 255));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(0, 102, 255));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnExit.setBackground(new java.awt.Color(0, 102, 255));
        btnExit.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnExit.setText("Salir");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 102, 255));
        jButton1.setText("Limpiar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBuscar)
                .addGap(163, 163, 163)
                .addComponent(btnBorrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addGap(80, 80, 80))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1005, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnNew)
                        .addGap(34, 34, 34)
                        .addComponent(btnGuardar)
                        .addGap(54, 54, 54)
                        .addComponent(btnModificar)
                        .addGap(62, 62, 62)
                        .addComponent(btnCancel)
                        .addGap(28, 28, 28)
                        .addComponent(jButton1)))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(65, 65, 65))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        Borrar(txtId);
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void tbGastosGeneralesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbGastosGeneralesMouseClicked
        Seleccionar(tbGastosGenerales, txtId, cbxTipodeGastGen, txtRecibo, txtSubTotal, txtTaxes, txtTip, txtTotal, cbxProveedor, cbxPagarCon, dateFecha);
    }//GEN-LAST:event_tbGastosGeneralesMouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        Seleccionar(tbGastosGenerales, txtId, cbxTipodeGastGen, txtRecibo, txtSubTotal, txtTaxes, txtTip, txtTotal, cbxProveedor, cbxPagarCon, dateFecha);
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyPressed

    }//GEN-LAST:event_txtBuscarKeyPressed

    private void txtBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyTyped

    }//GEN-LAST:event_txtBuscarKeyTyped

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed

    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed


    }//GEN-LAST:event_btnNewActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Guardar();
        Limpiar();
        MostrarDatos("");
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        Seleccionar(tbGastosGenerales, txtId, cbxTipodeGastGen, txtRecibo, txtSubTotal, txtTaxes, txtTip, txtTotal, cbxProveedor, cbxPagarCon, dateFecha);
        Modificar(txtId, cbxTipodeGastGen, txtRecibo, txtSubTotal, txtTaxes, txtTip, txtTotal, cbxProveedor, cbxPagarCon, dateFecha);
        MostrarDatos("");
        Limpiar();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        Limpiar();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed

        this.dispose();  //solo cierra la ventana actual
    }//GEN-LAST:event_btnExitActionPerformed

    private void cbxTipodeGastGenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipodeGastGenItemStateChanged
        MostrarCodigoTipoPagos(cbxTipodeGastGen, txtIdTipodeGastGen);
    }//GEN-LAST:event_cbxTipodeGastGenItemStateChanged

    private void cbxPagarConItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxPagarConItemStateChanged
        MostrarCodigoFormaDePago(cbxPagarCon, txtIdPagarCon);
    }//GEN-LAST:event_cbxPagarConItemStateChanged

    private void cbxPagarConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxPagarConActionPerformed
        MostrarCodigoFormaDePago(cbxPagarCon, txtIdPagarCon);
    }//GEN-LAST:event_cbxPagarConActionPerformed

    private void cbxProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxProveedorItemStateChanged
        MostrarCodigoProveedor(cbxProveedor, txtIdProveedor);
    }//GEN-LAST:event_cbxProveedorItemStateChanged

    private void txtIdProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProveedorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNew;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxPagarCon;
    private javax.swing.JComboBox<String> cbxProveedor;
    private javax.swing.JComboBox<String> cbxTipodeGastGen;
    private com.toedter.calendar.JDateChooser dateFecha;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    public static final javax.swing.JTable tbGastosGenerales = new javax.swing.JTable();
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdPagarCon;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdTipodeGastGen;
    private javax.swing.JTextField txtRecibo;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTaxes;
    private javax.swing.JTextField txtTip;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    public void MostrarTipoPagos(JComboBox cbxTipo) {

        String sql = "";
        sql = "select * from tipos_pagosgenerales";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxTipo.removeAllItems();

            while (rs.next()) {

                cbxTipo.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoTipoPagos(JComboBox cbxTipo, JTextField idTipo) {

        String consuta = "select tipos_pagosgenerales.id_pagos from tipos_pagosgenerales where tipos_pagosgenerales.nombre=?";

        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxTipo.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
                return;
            }

            CallableStatement cs = con.getConexion().prepareCall(consuta);

            Object selectedValue = cbxTipo.getSelectedItem();
            if (selectedValue != null) {
                String valorSeleccionado = selectedValue.toString();
                cs.setString(1, valorSeleccionado);

                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    idTipo.setText(rs.getString("id_pagos"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    public void MostrarCodigoFormaDePago(JComboBox cbxPagarCon, JTextField idPagarCon) {

        String consuta = "select formadepago.id_formadepago from formadepago where formadepago.nombre=?";

        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxPagarCon.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
                return;
            }

            CallableStatement cs = con.getConexion().prepareCall(consuta);

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
        }
    }

    public void MostrarFormaDePago(JComboBox cbxPagarCon) {

        String sql = "";
        sql = "select * from formadepago";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxPagarCon.removeAllItems();

            while (rs.next()) {

                cbxPagarCon.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarProveedor(JComboBox cbxProveedorNC) {

        String sql = "";
        sql = "select * from suplier";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxProveedorNC.removeAllItems();

            while (rs.next()) {

                cbxProveedorNC.addItem(rs.getString("nameSuplier"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoProveedor(JComboBox cbxProveedorNC, JTextField idSuplier) {

        String consuta = "select suplier.idSuplier from suplier where suplier.nameSuplier=?";

        try {
            CallableStatement cs = con.getConexion().prepareCall(consuta);
            cs.setString(1, cbxProveedor.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                idSuplier.setText(rs.getString("idSuplier"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }
}
