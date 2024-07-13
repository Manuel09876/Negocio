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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
        txtId.setEnabled(false);
        MostrarDatos("");
    }

    public void MostrarDatos(String Valores) {
        try {
            String[] TitulosTabla = {"id", "Tipo", "Recibo", "SubTotal", "Taxes", "Tip", "Total", "Proveedor", "Forma de Pago", "Fecha", "Estado"};
            String[] RegistroBD = new String[11];

            model = new DefaultTableModel(null, TitulosTabla);

            String ConsultaSQL = "SELECT dpg.id_PagosGenerales AS id, tpg.nombre AS Tipo, dpg.reciboNumero AS Recibo, dpg.subTotal AS SubTotal, dpg.taxes AS Taxes, dpg.tip AS Tip, dpg.total AS Total, s.nameSuplier AS Proveedor, fp.nombre AS 'Forma de Pago', dpg.fecha AS Fecha, dpg.estado AS Estado\n"
                    + "FROM detallepagosgenerales dpg\n"
                    + "INNER JOIN tipos_pagosgenerales tpg ON dpg.id_tipodePago=tpg.id_pagos\n"
                    + "INNER JOIN suplier s ON dpg.id_proveedor=s.idSuplier\n"
                    + "INNER JOIN formadepago fp ON dpg.id_formadepago=fp.id_formadepago\n"
                    + "ORDER BY dpg.id_PagosGenerales ASC";

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

    public void GuardarCredito() {
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
                + "subTotal,taxes,tip,total,id_proveedor,id_formadepago,fecha, estado)VALUES(?,?,?,?,?,?,?,?,?,'Pendiente')";

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

                Object selectedItem = cbxProveedor.getSelectedItem();
                if (selectedItem != null) {
                    // Hacer algo con el elemento seleccionado
                    String selectedItemAsString = selectedItem.toString();
                } else {
                    // Manejo de caso en el que el elemento seleccionado es null
                }

            }

        } catch (Exception e) {
        }
    }

    public void Limpiar() {
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
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        txtFrecuencia = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        btnRegistrarCredito = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        txt_Inicial = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_diferencia = new javax.swing.JTextField();
        dateFechaPagoCred = new com.toedter.calendar.JDateChooser();
        txtTotal1 = new javax.swing.JTextField();
        txtInteres = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtNumeroCuotas = new javax.swing.JTextField();
        txtValorCuota = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Pagos Generales");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(153, 153, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setText("Recibo N°");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 22, -1, -1));

        jLabel14.setText("SubTotal");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 66, -1, -1));

        jLabel15.setText("Taxes");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 106, -1, -1));
        jPanel3.add(txtRecibo, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 19, 178, -1));
        jPanel3.add(txtTaxes, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 103, 178, -1));
        jPanel3.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 63, 178, -1));

        jLabel16.setText("Total");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, -1, -1));
        jPanel3.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, 142, -1));

        jLabel17.setText("Fecha");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 60, -1, -1));

        dateFecha.setDateFormatString("yyyy-MM-dd");
        jPanel3.add(dateFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 142, -1));

        jLabel1.setText("Tipo");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 110, -1, -1));

        cbxTipodeGastGen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipodeGastGenItemStateChanged(evt);
            }
        });
        jPanel3.add(cbxTipodeGastGen, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 110, 148, -1));
        jPanel3.add(txtIdTipodeGastGen, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 110, 77, -1));

        jLabel2.setText("Propina");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 138, -1, -1));
        jPanel3.add(txtTip, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 135, 181, -1));

        jLabel49.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel49.setText("Pagar con");
        jPanel3.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(643, 66, -1, -1));

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
        jPanel3.add(cbxPagarCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 63, 162, -1));
        jPanel3.add(txtIdPagarCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 63, 89, -1));

        jLabel3.setText("Proveedor");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(643, 24, -1, -1));

        cbxProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxProveedorItemStateChanged(evt);
            }
        });
        jPanel3.add(cbxProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 21, 162, -1));

        txtIdProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdProveedorActionPerformed(evt);
            }
        });
        jPanel3.add(txtIdProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 21, 89, -1));
        jPanel3.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 110, 80, 32));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 1040, -1));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBorrar.setBackground(new java.awt.Color(0, 102, 255));
        btnBorrar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnBorrar.setText("Eliminar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        jPanel2.add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 10, -1, 30));

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

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 53, 1005, 160));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel11.setText("Descripción");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 18, -1, -1));

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarKeyTyped(evt);
            }
        });
        jPanel2.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 13, 190, -1));

        btnBuscar.setBackground(new java.awt.Color(0, 102, 255));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(288, 11, -1, 30));

        btnNuevo.setBackground(new java.awt.Color(0, 102, 255));
        btnNuevo.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/new product.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jPanel2.add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, -1, 30));

        btnGuardar.setBackground(new java.awt.Color(0, 102, 255));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel2.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, -1, 30));

        btnModificar.setBackground(new java.awt.Color(0, 102, 255));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel2.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 10, -1, 30));

        btnCancel.setBackground(new java.awt.Color(0, 102, 255));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel2.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 10, -1, 30));

        btnExit.setBackground(new java.awt.Color(0, 102, 255));
        btnExit.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnExit.setText("Salir");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jPanel2.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 230, -1, 30));

        jButton1.setBackground(new java.awt.Color(0, 102, 255));
        jButton1.setText("Limpiar");
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 10, -1, -1));
        jPanel2.add(txtFrecuencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 250, 100, -1));

        jLabel19.setText("Inicial");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, -1, -1));

        btnRegistrarCredito.setText("Registrar Credito");
        btnRegistrarCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarCreditoActionPerformed(evt);
            }
        });
        jPanel2.add(btnRegistrarCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 330, -1, -1));

        jLabel20.setText("Diferencia");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, -1, -1));
        jPanel2.add(txt_Inicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 230, 88, -1));

        jLabel8.setText("Tasa de Interes");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 280, -1, -1));

        txt_diferencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_diferenciaKeyReleased(evt);
            }
        });
        jPanel2.add(txt_diferencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, 92, -1));

        dateFechaPagoCred.setDateFormatString("yyyy-MM-dd");
        jPanel2.add(dateFechaPagoCred, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 260, 150, -1));
        jPanel2.add(txtTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 220, 100, -1));
        jPanel2.add(txtInteres, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 280, 100, -1));

        jLabel7.setText("Total");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 220, -1, -1));

        jLabel21.setText("Proxima Fecha de Pago");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 230, -1, -1));

        jLabel12.setText("Frecuencia de Pago");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 250, -1, -1));

        jLabel10.setText("%");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 280, -1, -1));

        jLabel22.setText("Valor de cuota");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 340, -1, -1));
        jPanel2.add(txtNumeroCuotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 310, 100, -1));
        jPanel2.add(txtValorCuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 340, 100, -1));

        jLabel18.setText("Numero de cuotas");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 310, -1, 30));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 181, 1040, 380));

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

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        txtRecibo.setText("");
        txtSubTotal.setText("");
        txtTaxes.setText("");
        txtTip.setText("");
        txtTotal.setText("");
        txtRecibo.requestFocus();

    }//GEN-LAST:event_btnNuevoActionPerformed

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

    private void txt_diferenciaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_diferenciaKeyReleased

    }//GEN-LAST:event_txt_diferenciaKeyReleased

    private void btnRegistrarCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarCreditoActionPerformed

        registrarPagosPendientes();
        GuardarCredito();
        LimpiartxtCred();
        MostrarDatos("");
        txt_Inicial.setEnabled(false);
        txt_diferencia.setEnabled(false);
        btnRegistrarCredito.setEnabled(false);
        btnGuardar.setEnabled(true);
        txtTotal1.setEnabled(false);
        txtFrecuencia.setEnabled(false);
        txtInteres.setEnabled(false);
        txtNumeroCuotas.setEnabled(false);
        txtValorCuota.setEnabled(false);
    }//GEN-LAST:event_btnRegistrarCreditoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnRegistrarCredito;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxPagarCon;
    private javax.swing.JComboBox<String> cbxProveedor;
    private javax.swing.JComboBox<String> cbxTipodeGastGen;
    private com.toedter.calendar.JDateChooser dateFecha;
    private com.toedter.calendar.JDateChooser dateFechaPagoCred;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    public static final javax.swing.JTable tbGastosGenerales = new javax.swing.JTable();
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtFrecuencia;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdPagarCon;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdTipodeGastGen;
    private javax.swing.JTextField txtInteres;
    private javax.swing.JTextField txtNumeroCuotas;
    private javax.swing.JTextField txtRecibo;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTaxes;
    private javax.swing.JTextField txtTip;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotal1;
    private javax.swing.JTextField txtValorCuota;
    private javax.swing.JTextField txt_Inicial;
    private javax.swing.JTextField txt_diferencia;
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
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ninguna forme de pago.");
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

                    // Validar el código de forma de pago y habilitar componentes
                    if (Integer.parseInt(rs.getString("id_formadepago")) == 3 || Integer.parseInt(rs.getString("id_formadepago")) == 6) {
                        txt_Inicial.setEnabled(true);
                        txt_diferencia.setEnabled(true);
                        btnRegistrarCredito.setEnabled(true);
                        btnGuardar.setEnabled(false);
                        txtTotal1.setEnabled(true);
                        txtFrecuencia.setEnabled(true);
                        txtInteres.setEnabled(true);
                        txtNumeroCuotas.setEnabled(true);
                        txtValorCuota.setEnabled(true);
                    } else {
                        txt_Inicial.setEnabled(false);
                        txt_diferencia.setEnabled(false);
                        btnRegistrarCredito.setEnabled(false);
                        btnGuardar.setEnabled(true);
                        txtTotal1.setEnabled(false);
                        txtFrecuencia.setEnabled(false);
                        txtInteres.setEnabled(false);
                        txtNumeroCuotas.setEnabled(false);
                        txtValorCuota.setEnabled(false);
                    }
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

    // Método para hallar el id_venta que se está ejecutando en ese momento
    public int IdCompra() {
        int id = 0;
        String sql = "SELECT MAX(id_pagosgenerales) FROM detallepagosgenerales";
        Conectar con = new Conectar();
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
            }
        }
        return id;
    }

    public void registrarPagosPendientes() {
        Conectar con = new Conectar();
        Connection connect = null;
        PreparedStatement stmt = null;
        try {
            connect = con.getConexion();

            // Obtener los datos de las cajas de texto
            int id = IdCompra();
            int frecuencia = Integer.parseInt(txtFrecuencia.getText());
            double interes = Double.parseDouble(txtInteres.getText());
            int numeroCuotas = Integer.parseInt(txtNumeroCuotas.getText());
            double valorCuota = Double.parseDouble(txtValorCuota.getText());
            double total = Double.parseDouble(txtTotal.getText());
            double diferencia = total;

            // Obtener la fecha de inicio
            Date fechaInicio = dateFechaPagoCred.getDate();
            if (fechaInicio == null) {
                JOptionPane.showMessageDialog(null, "La fecha de inicio es nula. Por favor selecciona una fecha válida.");
                return; // Añadido return para evitar continuar si la fecha es nula
            }

            // Preparar la inserción de pagos en la base de datos
            String sql = "INSERT INTO creditopg (id_compra, frecuencia, fechaPago, interes, NumeroCuotas, cuota, Diferencia, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, 'Pendiente')";
            stmt = connect.prepareStatement(sql);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaInicio);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 1; i <= numeroCuotas; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, frecuencia);

                Date fechaPago = calendar.getTime();
                double cuotaActual = valorCuota;

                // Ajustar la última cuota si la diferencia es menor que el valor de la cuota
                if (i == numeroCuotas && diferencia < valorCuota) {
                    cuotaActual = diferencia;
                }

                diferencia -= cuotaActual;

                stmt.setInt(1, id);
                stmt.setInt(2, frecuencia);
                stmt.setString(3, dateFormat.format(fechaPago));
                stmt.setDouble(4, interes);
                stmt.setInt(5, i); // Número de la cuota actual
                stmt.setDouble(6, cuotaActual);
                stmt.setDouble(7, diferencia); // Diferencia actualizada

                stmt.executeUpdate();


            }

            JOptionPane.showMessageDialog(null, "Crédito registrado correctamente con todas las fechas de pago.");

        } catch (NumberFormatException ex) {
            System.out.println("Error " + ex);
            JOptionPane.showMessageDialog(null, "Error al parsear un valor numérico: " + ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }

    private void LimpiartxtCred() {
        txtTotal1.setText("");
        txtFrecuencia.setText("");
        txtNumeroCuotas.setText("");
        txtValorCuota.setText("");
        txtInteres.setText("");
        txt_Inicial.setText("");
        txt_diferencia.setText("");
        cbxPagarCon.setSelectedItem("");
        cbxProveedor.setSelectedItem("");

    }

    private void LimpiarCampos() {
        txt_diferencia.setText("");
        txtFrecuencia.setText("");
        txt_Inicial.setText("");
        txtInteres.setText("");
        txtNumeroCuotas.setText("");
        txtValorCuota.setText("");
        dateFechaPagoCred.setDateFormatString("");
        txtTotal.setText("");
    }

    private void initListeners() {
        txt_Inicial.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularDiferencia();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calcularDiferencia();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calcularDiferencia();
            }
        });
    }

    private void calcularDiferencia() {
        try {
            double total = Double.parseDouble(txtTotal.getText());
            double inicial = Double.parseDouble(txt_Inicial.getText());
            double diferencia = total - inicial;
            txt_diferencia.setText(String.valueOf(diferencia));
        } catch (NumberFormatException ex) {
            // Manejo de excepción en caso de que los textos no sean números válidos
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos.");
        }

    }
}
