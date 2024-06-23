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
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class ComprasProductosMateriales extends javax.swing.JInternalFrame {

    Conectar con = new Conectar();
    Connection connect = con.getConexion();

    DefaultTableModel model = new DefaultTableModel();
    int id_CompraProMat;
    String numeroRecibo;
    int id_Tipo;
    int id_Producto;
    int cantidad;
    double precioUnit;
    int id_proveedor;
    int id_Marca;
    Date fecha;
    String estado;

    public ComprasProductosMateriales(int id_CompraProMat, String numeroRecibo, int id_Tipo, int id_Producto, int cantidad, double precioUnit, int id_proveedor, int id_Marca, Date fecha, String estado) {
        this.id_CompraProMat = id_CompraProMat;
        this.numeroRecibo = numeroRecibo;
        this.id_Tipo = id_Tipo;
        this.id_Producto = id_Producto;
        this.cantidad = cantidad;
        this.precioUnit = precioUnit;
        this.id_proveedor = id_proveedor;
        this.id_Marca = id_Marca;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId_CompraProMat() {
        return id_CompraProMat;
    }

    public void setId_CompraProMat(int id_CompraProMat) {
        this.id_CompraProMat = id_CompraProMat;
    }

    public String getNumeroRecibo() {
        return numeroRecibo;
    }

    public void setNumeroRecibo(String numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    public int getId_Tipo() {
        return id_Tipo;
    }

    public void setId_Tipo(int id_Tipo) {
        this.id_Tipo = id_Tipo;
    }

    public int getId_Producto() {
        return id_Producto;
    }

    public void setId_Producto(int id_Producto) {
        this.id_Producto = id_Producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(double precioUnit) {
        this.precioUnit = precioUnit;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public int getId_Marca() {
        return id_Marca;
    }

    public void setId_Marca(int id_Marca) {
        this.id_Marca = id_Marca;
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
    PreparedStatement ps;
    ResultSet rs;

    public ComprasProductosMateriales() {
        initComponents();
        AutoCompleteDecorator.decorate(cbxProveedor);
        AutoCompleteDecorator.decorate(cbxMarca);
        AutoCompleteDecorator.decorate(cbxProducto);
        AutoCompleteDecorator.decorate(cbxPagarCon);
        AutoCompleteDecorator.decorate(cbxTipoProducto);
        
        MostrarProveedor(cbxProveedor);
        MostrarMarca(cbxMarca);
        MostrarProducto(cbxProducto);
        MostrarFormaDePago(cbxPagarCon);
        MostrarTipo(cbxTipoProducto);
                
        txtIdMarca.setEnabled(false);
        txtIdProducto.setEnabled(false);
        txtIdProveedor.setEnabled(false);
        txtIdTipoPro.setEnabled(false);
        txtId_CompraProMat.setEnabled(false);
        txtIdPagarCon.setEnabled(false);
        txtId_CompraProMat.setEnabled(false);

        MostrarDatos("");
        
    }

    public void MostrarDatos(String Valores) {
        try {
            String[] TitulosTabla = {"id", "Recibo", "Tipo", "Descripcion", "Cantidad", "Proveedor", "Marca", "PrecioUnit"};
            String[] RegistroBD = new String[8];

            model = new DefaultTableModel(null, TitulosTabla);

            String ConsultaSQL = "SELECT * FROM detalle_compraproductosmateriales";

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

                model.addRow(RegistroBD);

            }

        } catch (SQLException e) {
            System.out.println("Error "+e);
        }
    }

    public void Guardar() {
        int id_CompraProMat;
        String numeroRecibo;
        int id_Tipo;
        int id_Producto;
        int cantidad;
        double precioUnit;
        int id_proveedor;
        int id_Marca;
        Date fecha;
        String estado;

        id_CompraProMat = Integer.parseInt(txtId_CompraProMat.getText());
        numeroRecibo = txtNumeroRecibo.getText();
        id_Tipo = Integer.parseInt(txtIdTipoPro.getText());
        int id_producto = Integer.parseInt(txtIdProducto.getText());
        cantidad = Integer.parseInt(txtCant.getText());
        precioUnit = Double.parseDouble(txtSubTotal.getText());
        id_proveedor = Integer.parseInt(txtIdProveedor.getText());
        id_Marca = Integer.parseInt(txtIdMarca.getText());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        fecha = dateFecha.getDate();

        //Consulta para evitar duplicados
        String consulta = "SELECT * detalle_compraproductosmateriales WHERE id_producto=? AND fecha=?";

        //Consulta para insertar los datos
        String sql = "INSERT INTO detalle_compraproductosmateriales (id_producto,numeroRecibo,cantidad, "
                + "precioUnit,id_proveedor,id_Marca, fecha)VALUES(?,?,?,?,?,?,?)";

        //Para almacenar los datos empleo un try cash
        try {
            //Preparando la conexion a sql
            PreparedStatement pst = connect.prepareStatement(sql);

            pst.setInt(1, id_producto);
            pst.setString(2, numeroRecibo);
            pst.setInt(3, cantidad);
            pst.setDouble(4, precioUnit);
            pst.setInt(5, id_proveedor);
            pst.setInt(6, id_Marca);
            pst.setDate(7, (java.sql.Date) fecha);
            
            //Declarar otra variable para validar los registros
            int n = pst.executeUpdate();

            //si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se  Guardo con exito");

                MostrarDatos("");
            }
            MostrarDatos("");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al gusrdar registro " + e.toString());
        }

    }

    public void Modificar(JTextField idProducto, JTextField Recibo, JTextField cantidad, JTextField precioUnit, JComboBox Proveedor, JComboBox Marca, JDateChooser fecha) {
        String sql = "UPDATE detalle_compraproductosmateriales SET id_producto=?, numeroRecibo=?, cantidad=?, precioUnit=?, id_proveedor=?, id_Marca=?, fecha=?";

        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setInt(1, getId_Producto());
            ps.setString(2, getNumeroRecibo());
            ps.setInt(3, getCantidad());
            ps.setDouble(4, getPrecioUnit());
            ps.setInt(5, getId_proveedor());
            ps.setInt(6, getId_Marca());
            ps.setDate(7, (java.sql.Date) getFecha());
            ps.execute();

            JOptionPane.showMessageDialog(null, "Registro se odifico con exito");
            MostrarDatos(sql);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());

        }
    }

    public void Borrar(JTextField id) {

        setId_CompraProMat(Integer.parseInt(id.getText()));

        String consulta = "DELETE FROM detalle_compraproductosmateriales WHERE id_CompraProMat=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);
            cs.setInt(1, getId_CompraProMat());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");

            MostrarDatos("");

        } catch (HeadlessException | SQLException e) {

            JOptionPane.showMessageDialog(null, "No se Elimino registro " + e.toString());
        }

    }

    public void Seleccionar(JTable Tabla, JTextField id, JTextField Recibo, JComboBox Producto, JTextField cantidad, JTextField precioUnit, JComboBox Proveedor, JComboBox Marca, JDateChooser fecha) {

        try {
            int fila = tbCompraProducto.getSelectedRow();

            if (fila >= 0) {
                id.setText(tbCompraProducto.getValueAt(fila, 0).toString());
                Producto.setSelectedItem(tbCompraProducto.getValueAt(fila, 1).toString());
                Recibo.setText(tbCompraProducto.getValueAt(fila, 2).toString());
                cantidad.setText(tbCompraProducto.getValueAt(fila, 3).toString());
                precioUnit.setText(tbCompraProducto.getValueAt(fila, 4).toString());
                Proveedor.setSelectedItem(tbCompraProducto.getValueAt(fila, 5).toString());
                Marca.setSelectedItem(tbCompraProducto.getValueAt(fila, 6));
                fecha.setDateFormatString(tbCompraProducto.getValueAt(fila, 7).toString());

            }

        } catch (Exception e) {
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tbCompraProducto = new javax.swing.JTable();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        txtNumeroRecibo = new javax.swing.JTextField();
        txtCant = new javax.swing.JTextField();
        txtSubTotal = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        btnGenerarCompra = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        cbxProveedor = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();
        JLabelTotalCompra = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txtTaxes = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtIdProveedor = new javax.swing.JTextField();
        cbxProducto = new javax.swing.JComboBox<>();
        txtIdMarca = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        cbxMarca = new javax.swing.JComboBox<>();
        txtIdProducto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbxPagarCon = new javax.swing.JComboBox<>();
        txtIdPagarCon = new javax.swing.JTextField();
        dateFecha = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtId_CompraProMat = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbxTipoProducto = new javax.swing.JComboBox<>();
        txtIdTipoPro = new javax.swing.JTextField();
        btnRefresh = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 51, 153));
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Compras Productos y Materiales");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel16.setBackground(new java.awt.Color(204, 204, 255));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbCompraProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbCompraProducto.setRowHeight(23);
        tbCompraProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCompraProductoMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tbCompraProducto);

        jPanel16.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 147, 1050, 220));

        jLabel40.setText("Producto");
        jPanel16.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        jLabel41.setText("Recibo N°");
        jPanel16.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

        jLabel42.setText("Cant");
        jPanel16.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, -1, -1));

        jLabel43.setText("Precio Unitario");
        jPanel16.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, -1, -1));

        jLabel44.setText("Total");
        jPanel16.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, -1, -1));
        jPanel16.add(txtNumeroRecibo, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 170, 30));
        jPanel16.add(txtCant, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, 70, 30));
        jPanel16.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 50, 70, 30));
        jPanel16.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 50, 70, 30));

        btnGenerarCompra.setBackground(new java.awt.Color(204, 204, 204));
        btnGenerarCompra.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnGenerarCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-producto.png"))); // NOI18N
        jPanel16.add(btnGenerarCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 100, 60, 30));

        jLabel46.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel46.setText("Proveedor");
        jPanel16.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, -1, -1));

        cbxProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxProveedorItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 420, 220, -1));

        jLabel47.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel47.setText("Total Pagar");
        jPanel16.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 90, -1, -1));

        JLabelTotalCompra.setText("-----------");
        jPanel16.add(JLabelTotalCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 120, 130, -1));

        jLabel49.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel49.setText("Pagar con");
        jPanel16.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 390, -1, -1));
        jPanel16.add(txtTaxes, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 70, 30));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel16.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 10, -1, -1));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/clear.png"))); // NOI18N
        jButton2.setText("Limpiar");
        jPanel16.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 50, -1, -1));
        jPanel16.add(txtIdProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 420, 80, -1));

        cbxProducto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxProductoItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 150, -1));
        jPanel16.add(txtIdMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 460, 80, -1));

        jLabel51.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel51.setText("Marca");
        jPanel16.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 460, -1, -1));

        cbxMarca.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxMarcaItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 460, 220, -1));
        jPanel16.add(txtIdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 80, -1));

        jLabel1.setText("Impuestos");
        jPanel16.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, -1, -1));

        cbxPagarCon.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxPagarConItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxPagarCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 390, 190, -1));
        jPanel16.add(txtIdPagarCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 390, 90, -1));

        dateFecha.setDateFormatString("yyyy-MM-dd");
        jPanel16.add(dateFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 380, 220, -1));

        jLabel2.setText("Fecha de Compra");
        jPanel16.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, -1, -1));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel16.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 380, 120, -1));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exchange.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel16.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 420, 120, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel16.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 470, 120, -1));
        jPanel16.add(txtId_CompraProMat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, -1));

        jLabel3.setText("Otros");
        jPanel16.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 90, -1, -1));
        jPanel16.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, 80, 30));

        jLabel4.setText("Tipo");
        jPanel16.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        cbxTipoProducto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoProductoItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxTipoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 150, -1));
        jPanel16.add(txtIdTipoPro, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 80, -1));

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jPanel16.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 10, -1, -1));

        getContentPane().add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1080, 510));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxProveedorItemStateChanged
        MostrarCodigoProveedor(cbxProveedor, txtIdProveedor);
    }//GEN-LAST:event_cbxProveedorItemStateChanged

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cbxMarcaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxMarcaItemStateChanged
        MostrarCodigoMarca(cbxMarca, txtIdMarca);
    }//GEN-LAST:event_cbxMarcaItemStateChanged

    private void cbxProductoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxProductoItemStateChanged
        MostrarCodigoProducto(cbxProducto, txtIdProducto);
    }//GEN-LAST:event_cbxProductoItemStateChanged

    private void cbxPagarConItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxPagarConItemStateChanged
        MostrarCodigoFormaDePago(cbxPagarCon, txtIdPagarCon);
    }//GEN-LAST:event_cbxPagarConItemStateChanged

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        Modificar(txtIdProducto, txtNumeroRecibo, txtCant, txtSubTotal, cbxProveedor, cbxMarca, dateFecha);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        Seleccionar(tbCompraProducto, txtCant, txtCant, cbxProducto, txtTotal, txtCant, cbxProveedor, cbxMarca, dateFecha);
        Borrar(txtId_CompraProMat);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tbCompraProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCompraProductoMouseClicked
        Seleccionar(tbCompraProducto, txtCant, txtCant, cbxProducto, txtTotal, txtCant, cbxProveedor, cbxMarca, dateFecha);
    }//GEN-LAST:event_tbCompraProductoMouseClicked

    private void cbxTipoProductoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoProductoItemStateChanged
        MostrarCodigoTipo(cbxTipoProducto, txtIdTipoPro);
    }//GEN-LAST:event_cbxTipoProductoItemStateChanged

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
         repaint(); // Actualizar el JInternalFrame al hacer clic en el botón "Refresh"
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel JLabelTotalCompra;
    private javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGenerarCompra;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JComboBox<Object> cbxMarca;
    private javax.swing.JComboBox<String> cbxPagarCon;
    private javax.swing.JComboBox<String> cbxProducto;
    public javax.swing.JComboBox<Object> cbxProveedor;
    private javax.swing.JComboBox<String> cbxTipoProducto;
    private com.toedter.calendar.JDateChooser dateFecha;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel51;
    public javax.swing.JPanel jPanel16;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JTextField jTextField1;
    public javax.swing.JTable tbCompraProducto;
    public javax.swing.JTextField txtCant;
    private javax.swing.JTextField txtIdMarca;
    private javax.swing.JTextField txtIdPagarCon;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdTipoPro;
    private javax.swing.JTextField txtId_CompraProMat;
    public javax.swing.JTextField txtNumeroRecibo;
    public javax.swing.JTextField txtSubTotal;
    public javax.swing.JTextField txtTaxes;
    public javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

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

    public void MostrarCodigoMarca(JComboBox cbxMarca, JTextField idMarca) {

        String consuta = "select marca.id_marca from marca where marca.nombre=?";

        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxMarca.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
                return;
            }

            CallableStatement cs = con.getConexion().prepareCall(consuta);

            Object selectedValue = cbxMarca.getSelectedItem();
            if (selectedValue != null) {
                String valorSeleccionado = selectedValue.toString();
                cs.setString(1, valorSeleccionado);

                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    idMarca.setText(rs.getString("id_marca"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    public void MostrarMarca(JComboBox cbxMarca) {

        String sql = "";
        sql = "select * from marca";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxMarca.removeAllItems();

            while (rs.next()) {

                cbxMarca.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarProducto(JComboBox cbxProducto) {

        String sql = "";
        sql = "select * from product";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxProducto.removeAllItems();

            while (rs.next()) {

                cbxProducto.addItem(rs.getString("nameProduct"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoProducto(JComboBox cbxProducto, JTextField idProducto) {

        String consuta = "select product.idProduct from product where product.nameProduct=?";

        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxProducto.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
                return;
            }

            CallableStatement cs = con.getConexion().prepareCall(consuta);

            Object selectedValue = cbxProducto.getSelectedItem();
            if (selectedValue != null) {
                String valorSeleccionado = selectedValue.toString();
                cs.setString(1, valorSeleccionado);

                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    idProducto.setText(rs.getString("idProduct"));
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

    public void MostrarTipo(JComboBox cbxTipoProducto) {

        String sql = "";
        sql = "select * from tipodeproductomateriales";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxTipoProducto.removeAllItems();

            while (rs.next()) {

                cbxTipoProducto.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoTipo(JComboBox cbxTipoProducto, JTextField idTipoProducto) {

        String consuta = "select tipodeproductomateriales.idProMat from tipodeproductomateriales where tipodeproductomateriales.nombre=?";

        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxTipoProducto.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
                return;
            }

            CallableStatement cs = con.getConexion().prepareCall(consuta);

            Object selectedValue = cbxTipoProducto.getSelectedItem();
            if (selectedValue != null) {
                String valorSeleccionado = selectedValue.toString();
                cs.setString(1, valorSeleccionado);

                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    idTipoProducto.setText(rs.getString("idProMat"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    //Implementacion Posterior
    public void MostrarLocalizacion(JComboBox cbxLocalizacion) {

        String sql = "";
        sql = "select * from localizacion";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxLocalizacion.removeAllItems();

            while (rs.next()) {

                cbxLocalizacion.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoLocalizacion(JComboBox cbxLocalizacion, JTextField idLocalizacion) {

        String consuta = "select localizacion.id_localizacion from localizacion where localizacion.nombre=?";

        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxLocalizacion.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
                return;
            }

            CallableStatement cs = con.getConexion().prepareCall(consuta);

            Object selectedValue = cbxLocalizacion.getSelectedItem();
            if (selectedValue != null) {
                String valorSeleccionado = selectedValue.toString();
                cs.setString(1, valorSeleccionado);

                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    idLocalizacion.setText(rs.getString("id_localizacion"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

}
