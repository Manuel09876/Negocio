package Reports;

import Bases.CustomTableCellRenderer;
import conectar.Conectar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.awt.event.ItemEvent;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Stock extends javax.swing.JInternalFrame {
    
    DefaultTableModel model;
    int idProduct;
    String nameProduct;
    double stock;
    String estado;

    public Stock(int idProduct, String nameProduct, double stock, String estado) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.stock = stock;
        this.estado = estado;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

   

    public Stock() {
        initComponents();
        
        AutoCompleteDecorator.decorate(cbxProducto);
        AutoCompleteDecorator.decorate(cbxTrabajador);
        MostrarProducto(cbxProducto);
        MostrarTrabajador(cbxTrabajador);
        CargarDatosTable("");

        txtIdProducto.setEnabled(false);
        txtIdTrabajador.setEnabled(false);
    }

    // Método para aplicar el renderizado personalizado a las columnas necesarias
    private void aplicarRenderPersonalizado() {
        CustomTableCellRenderer cellRenderer = new CustomTableCellRenderer();
        for (int i = 0; i < tbStock.getColumnCount(); i++) {
            tbStock.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    void CargarDatosTable(String Valores) {
Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        try {
            String[] titulosTabla = {"Id", "Nombre", "Stock", "estado", "Stock Minimo"}; // Titulos de la Tabla
            String[] RegistroBD = new String[5]; // Registros de la Base de Datos

            model = new DefaultTableModel(null, titulosTabla); // Le pasamos los titulos a la tabla

            String ConsultaSQL = "SELECT idProduct, nameProduct, stock, estado, stock_minimo FROM product";

            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {
                RegistroBD[0] = result.getString("idProduct");
                RegistroBD[1] = result.getString("nameProduct");
                RegistroBD[2] = result.getString("stock");
                RegistroBD[3] = result.getString("estado");
                RegistroBD[4] = result.getString("stock_minimo");

                model.addRow(RegistroBD);
            }

            tbStock.setModel(model);
            tbStock.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbStock.getColumnModel().getColumn(1).setPreferredWidth(150);
            tbStock.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbStock.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbStock.getColumnModel().getColumn(4).setPreferredWidth(100);

            // Aplicar el renderer personalizado a las columnas necesarias
            aplicarRenderPersonalizado();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public DefaultTableModel buscarTabla(String buscar) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }   
        DefaultTableModel modelo = new DefaultTableModel();

        try {
            String[] titulosTabla = {"Id", "Nombre", "Stock", "estado", "Stock Minimo"}; // Titulos de la Tabla
            String[] RegistroBD = new String[5]; // Registros de la Base de Datos

            modelo = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla

            // Ajustar la consulta SQL para buscar por nombre exacto
            String ConsultaSQL = "SELECT * FROM product WHERE nameProduct LIKE ?";

            PreparedStatement pst = connection.prepareStatement(ConsultaSQL);
            pst.setString(1, "%" + buscar + "%"); // Usar el valor del comboBox para la búsqueda
            ResultSet result = pst.executeQuery();

            while (result.next()) {
                RegistroBD[0] = result.getString("idProduct");
                RegistroBD[1] = result.getString("nameProduct");
                RegistroBD[2] = result.getString("stock");
                RegistroBD[3] = result.getString("estado");
                RegistroBD[4] = result.getString("stock_minimo");
                modelo.addRow(RegistroBD);
            }

            tbStock.setModel(modelo);
            tbStock.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbStock.getColumnModel().getColumn(1).setPreferredWidth(150);
            tbStock.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbStock.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbStock.getColumnModel().getColumn(4).setPreferredWidth(100);

            // Aplicar el renderer personalizado a las columnas necesarias
            aplicarRenderPersonalizado();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
        return modelo;
    }

    public boolean ActualizarStock() {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        String idProductoStr = txtIdProducto.getText();
        String cantidadStr = txtCantidad.getText();

        if (idProductoStr.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return false;
        }

        int idProduct;
        int cantidad;

        try {
            idProduct = Integer.parseInt(idProductoStr);
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese valores numéricos válidos.");
            return false;
        }

        String consultaStock = "SELECT stock FROM product WHERE idProduct = ?";
        String sqlActualizar = "UPDATE product SET stock = stock - ? WHERE idProduct = ?";

        try {
            PreparedStatement pstConsulta = connection.prepareStatement(consultaStock);
            pstConsulta.setInt(1, idProduct);
            ResultSet rs = pstConsulta.executeQuery();

            if (rs.next()) {
                int stockActual = rs.getInt("stock");

                if (cantidad > stockActual) {
                    JOptionPane.showMessageDialog(null, "Error: la cantidad a retirar excede el stock actual.");
                    return false;
                } else {
                    Conectar.getInstancia().obtenerConexion();
                    
                    PreparedStatement pstActualizar = connection.prepareStatement(sqlActualizar);
                    pstActualizar.setInt(1, cantidad);
                    pstActualizar.setInt(2, idProduct);
                    pstActualizar.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Stock actualizado correctamente.");
                    CargarDatosTable("");
                    return true;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error: producto no encontrado.");
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el stock: " + e.getMessage());
            return false;
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void GuardarRetiro() {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        String idTrabajadorStr = txtIdTrabajador.getText();
        String idProductoStr = txtIdProducto.getText();
        String cantidadStr = txtCantidad.getText();
        java.util.Date date = dateRetiro.getDate();

        if (idTrabajadorStr.isEmpty() || idProductoStr.isEmpty() || cantidadStr.isEmpty() || date == null) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }

        int idTrabajador;
        int idProduct;
        int cantidad;

        try {
            idTrabajador = Integer.parseInt(idTrabajadorStr);
            idProduct = Integer.parseInt(idProductoStr);
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese valores numéricos válidos.");
            return;
        }

        String sql = "INSERT INTO retiro (id_trabajador, id_producto, cantidad, fecha) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idTrabajador);
            pst.setInt(2, idProduct);
            pst.setInt(3, cantidad);
            pst.setDate(4, new java.sql.Date(date.getTime()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Retiro guardado correctamente.");
            ActualizarStock();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el retiro: " + e.getMessage());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tbStock = new javax.swing.JTable();
        txtIdProducto = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cbxProducto = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        cbxTrabajador = new javax.swing.JComboBox<>();
        txtIdTrabajador = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        dateRetiro = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        btnActualizarStock = new javax.swing.JButton();
        btnInstruccion = new javax.swing.JButton();
        btnSolicitantes = new javax.swing.JButton();
        btnMostrarTodo = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Stock");

        jPanel12.setBackground(new java.awt.Color(102, 102, 255));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbStock.setRowHeight(23);
        jScrollPane14.setViewportView(tbStock);

        jPanel12.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 990, 250));
        jPanel12.add(txtIdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 70, 40));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 20, -1, -1));

        jLabel1.setText("Producto");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, -1, -1));

        cbxProducto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxProductoItemStateChanged(evt);
            }
        });
        cbxProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxProductoActionPerformed(evt);
            }
        });
        cbxProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbxProductoKeyReleased(evt);
            }
        });
        jPanel12.add(cbxProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 240, -1));

        cbxTrabajador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });

        jLabel2.setText("Trabajador");

        jLabel3.setText("Cantidad");

        jLabel4.setText("Retiro de Materiales");

        btnActualizarStock.setText("Actualizar Stock");
        btnActualizarStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarStockActionPerformed(evt);
            }
        });

        btnInstruccion.setText("Guia");
        btnInstruccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstruccionActionPerformed(evt);
            }
        });

        btnSolicitantes.setText("Solicitudes");
        btnSolicitantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSolicitantesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(69, 69, 69)
                                .addComponent(dateRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(102, 102, 102)
                                .addComponent(jLabel4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnInstruccion)
                            .addComponent(btnSolicitantes))
                        .addGap(36, 36, 36))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbxTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(txtIdTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(109, 109, 109)
                        .addComponent(btnActualizarStock)
                        .addContainerGap(350, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnSolicitantes)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(dateRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(46, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInstruccion)
                        .addGap(24, 24, 24))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(btnActualizarStock)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 1000, 150));

        btnMostrarTodo.setText("Mostrar Todo");
        btnMostrarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarTodoActionPerformed(evt);
            }
        });
        jPanel12.add(btnMostrarTodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1033, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 1021, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 556, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cbxProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxProductoActionPerformed

    private void cbxProductoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxProductoItemStateChanged

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (cbxProducto.getSelectedItem() != null) {
                MostrarCodigoProducto(cbxProducto, txtIdProducto);
                buscarTabla(cbxProducto.getSelectedItem().toString());
            }
        }
    }//GEN-LAST:event_cbxProductoItemStateChanged

    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged

    private void btnInstruccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstruccionActionPerformed
        JOptionPane.showMessageDialog(null, "1° Click en producto y elegir el que necesite\n"
                + "2° Elegir Trabajador \n"
                + "3° Verificar Stock, escribir cantidad a retirar y la fecha \n "
                + "4° Precionar boton Asignar para que se actualice el stock");
    }//GEN-LAST:event_btnInstruccionActionPerformed

    private void btnActualizarStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarStockActionPerformed
        boolean stockActualizado = ActualizarStock();
        if (stockActualizado) {
            GuardarRetiro();
            CargarDatosTable("");
            txtCantidad.setText("");
        }
    }//GEN-LAST:event_btnActualizarStockActionPerformed

    private void cbxProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxProductoKeyReleased

    }//GEN-LAST:event_cbxProductoKeyReleased

    private void btnMostrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodoActionPerformed
        CargarDatosTable("");
    }//GEN-LAST:event_btnMostrarTodoActionPerformed

    private void btnSolicitantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSolicitantesActionPerformed
        Bases.UsuarioStock objPedidos = new Bases.UsuarioStock();
        objPedidos.setVisible(true);
    }//GEN-LAST:event_btnSolicitantesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnActualizarStock;
    public javax.swing.JButton btnInstruccion;
    public javax.swing.JButton btnMostrarTodo;
    private javax.swing.JButton btnSalir;
    public javax.swing.JButton btnSolicitantes;
    private javax.swing.JComboBox<String> cbxProducto;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private com.toedter.calendar.JDateChooser dateRetiro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane14;
    public javax.swing.JTable tbStock;
    private javax.swing.JTextField txtCantidad;
    public javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtIdTrabajador;
    // End of variables declaration//GEN-END:variables

    public void MostrarProducto(JComboBox cbxProducto) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "select * from product";
        Statement st;

        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxProducto.removeAllItems();

            while (rs.next()) {

                cbxProducto.addItem(rs.getString("nameProduct"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar en Combo " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoProducto(JComboBox cbxProducto, JTextField idProducto) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        String consuta = "select product.idProduct from product where product.nameProduct=?";
        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxProducto.getSelectedIndex() == -1) {

                return;
            }
            CallableStatement cs = connection.prepareCall(consuta);
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
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarTrabajador(JComboBox comboTrabajador) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "";
        sql = "select * from worker";
        Statement st;

        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            comboTrabajador.removeAllItems();

            while (rs.next()) {

                comboTrabajador.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoTrabajador(JComboBox trabajador, JTextField IdTrabajador) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        String consulta = "select worker.idWorker from worker where worker.nombre=?";

        try {
            // Verificar si el elemento seleccionado es null
            if (trabajador.getSelectedItem() != null) {
                
                CallableStatement cs = connection.prepareCall(consulta);
                cs.setString(1, trabajador.getSelectedItem().toString());
                cs.execute();

                // Ejecutar la consulta y procesar el resultado
                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    IdTrabajador.setText(rs.getString("idWorker"));
                }
            } else {
//                JOptionPane.showMessageDialog(null, "No hay un trabajador seleccionado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

}
