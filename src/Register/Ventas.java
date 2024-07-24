package Register;

import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Ventas extends javax.swing.JInternalFrame {

    Conectar con = new Conectar();
    Connection connect = con.getConexion();

    public Ventas() {
        initComponents();
        AutoCompleteDecorator.decorate(cbxClientes);
        MostrarCiente(cbxClientes);
        MostrarTabla();
    }

    public void addCheckBox(int column, JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }

    public boolean IsSelected(int row, int column, JTable table) {
        return table.getValueAt(row, column) != null;
    }

    
    public void MostrarTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {

            String[] tituloTabla = {"Seleccion", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Nota Cliente", "Nota Empresa", "Comentario", "Status", "Trabajador", "id Trab", "idST"};
            String[] RegistroBD = new String[19];
            modelo = new DefaultTableModel(null, tituloTabla);

            String sql = "SELECT \n"
                    + "    o.id, \n"
                    + "    bussiness.nameBusiness AS Empresa, \n"
                    + "    o.fechaT AS Fecha, \n"
                    + "    customer.nameCustomer AS Nombre, \n"
                    + "    customer.area AS Tamaño, \n"
                    + "    customer.address AS Direccion, \n"
                    + "    customer.city AS Ciudad, \n"
                    + "    customer.state AS Estado, \n"
                    + "    customer.zipCode AS 'Zip Code', \n"
                    + "    customer.phoneNumber AS Celular, \n"
                    + "    o.servicio AS Servicio, \n"
                    + "    customer.nota_cliente AS 'Nota Cliente', \n"
                    + "    o.notaEmpresa AS 'Nota Empresa', \n"
                    + "    servicio_trabajador.comentario AS comentario,\n"
                    + "    o.estado AS Status, \n"
                    + "    worker.nombre AS Trabajador, servicio_trabajador.id_Trabajador AS 'id Trab', servicio_trabajador.id_ST AS idST \n"
                    + "FROM \n"
                    + "    orderservice AS o\n"
                    + "INNER JOIN \n"
                    + "    bussiness ON o.id_empresa = bussiness.idBusiness\n"
                    + "INNER JOIN \n"
                    + "    customer ON o.id_cliente = customer.idCustomer\n"
                    + "INNER JOIN \n"
                    + "    servicio_trabajador ON o.id = servicio_trabajador.id_OS\n"
                    + "INNER JOIN \n"
                    + "    worker ON servicio_trabajador.id_Trabajador = worker.idWorker\n"
                    + "WHERE o.estado = 'Programado' \n"
                    + "ORDER BY \n"
                    + "    o.fechaT ASC";
            Statement st = connect.createStatement();
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
                RegistroBD[12] = rs.getString("Nota Cliente");
                RegistroBD[13] = rs.getString("Nota Empresa");
                RegistroBD[14] = rs.getString("Comentario");
                RegistroBD[15] = rs.getString("Status");
                RegistroBD[16] = rs.getString("Trabajador");
                RegistroBD[17] = rs.getString("id Trab");
                RegistroBD[18] = rs.getString("idST");
                modelo.addRow(RegistroBD);
            }
            tbVentas.setModel(modelo);

            tbVentas.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbVentas.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbVentas.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbVentas.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbVentas.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbVentas.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbVentas.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(11).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(12).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(13).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(14).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(15).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(16).setPreferredWidth(150);

            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbVentas); // Agregar checkbox a la columna 0 de cada fila
            }

        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }
    }
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbVentas = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtCodNV = new javax.swing.JTextField();
        txtProductoNV = new javax.swing.JTextField();
        txtCantNV = new javax.swing.JTextField();
        txtPrecioNV = new javax.swing.JTextField();
        txtTotalNV = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        btnGenerarVenta = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        txtPagarConNV = new javax.swing.JTextField();
        txtVueltoNV = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        JLabelTotalPagarNV = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtIdNV = new javax.swing.JTextField();
        cbxClientes = new javax.swing.JComboBox<>();
        btnCajas = new javax.swing.JButton();
        txtIdCustomer = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Ventas");

        jPanel10.setBackground(new java.awt.Color(204, 255, 204));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane8.setViewportView(tbVentas);

        jPanel10.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 147, 1120, 260));

        jLabel30.setText("Código");
        jPanel10.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, -1, -1));

        jLabel31.setText("Servicio");
        jPanel10.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, -1, -1));

        jLabel32.setText("Cant");
        jPanel10.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, -1, -1));

        jLabel33.setText("Precio");
        jPanel10.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 50, -1, -1));

        jLabel34.setText("Total");
        jPanel10.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 50, -1, -1));

        jLabel35.setText("Stock");
        jPanel10.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 50, -1, 20));

        txtCodNV.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jPanel10.add(txtCodNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 90, 30));
        jPanel10.add(txtProductoNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 170, 30));
        jPanel10.add(txtCantNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, 64, 30));
        jPanel10.add(txtPrecioNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 64, 30));
        jPanel10.add(txtTotalNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 80, 64, 30));
        jPanel10.add(txtStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 80, 64, 30));

        btnGenerarVenta.setBackground(new java.awt.Color(204, 204, 204));
        btnGenerarVenta.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnGenerarVenta.setText("Generar Venta");
        jPanel10.add(btnGenerarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 80, 130, 30));

        jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel36.setText("Cliente");
        jPanel10.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, -1, -1));
        jPanel10.add(txtPagarConNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 450, 100, -1));
        jPanel10.add(txtVueltoNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 450, 90, -1));

        jLabel37.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel37.setText("Total Pagar");
        jPanel10.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 450, -1, -1));

        JLabelTotalPagarNV.setText("-----------");
        jPanel10.add(JLabelTotalPagarNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 450, 130, -1));

        jLabel38.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel38.setText("vuelto");
        jPanel10.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 450, -1, -1));

        jLabel39.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel39.setText("Pagar con");
        jPanel10.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 450, -1, -1));

        txtIdNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdNVActionPerformed(evt);
            }
        });
        jPanel10.add(txtIdNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 20, 64, 30));

        cbxClientes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxClientesItemStateChanged(evt);
            }
        });
        jPanel10.add(cbxClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 450, 230, -1));

        btnCajas.setText("Cajas");
        jPanel10.add(btnCajas, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 30, -1, -1));
        jPanel10.add(txtIdCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 90, -1));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel10.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 70, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1190, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 1190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 589, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdNVActionPerformed

    private void cbxClientesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxClientesItemStateChanged
        MostrarCodigoCliente(cbxClientes, txtIdCustomer);
    }//GEN-LAST:event_cbxClientesItemStateChanged

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel JLabelTotalPagarNV;
    public javax.swing.JButton btnCajas;
    public javax.swing.JButton btnGenerarVenta;
    private javax.swing.JButton btnSalir;
    public javax.swing.JComboBox<Object> cbxClientes;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JScrollPane jScrollPane8;
    public javax.swing.JTable tbVentas;
    public javax.swing.JTextField txtCantNV;
    public javax.swing.JTextField txtCodNV;
    private javax.swing.JTextField txtIdCustomer;
    public javax.swing.JTextField txtIdNV;
    public javax.swing.JTextField txtPagarConNV;
    public javax.swing.JTextField txtPrecioNV;
    public javax.swing.JTextField txtProductoNV;
    public javax.swing.JTextField txtStock;
    public javax.swing.JTextField txtTotalNV;
    public javax.swing.JTextField txtVueltoNV;
    // End of variables declaration//GEN-END:variables

    public void MostrarCiente(JComboBox cbxCliente) {

        String sql = "";
        sql = "select * from customer";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxCliente.removeAllItems();

            while (rs.next()) {

                cbxCliente.addItem(rs.getString("nameCustomer"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Combo " + e.toString());
        }
    }

    public void MostrarCodigoCliente(JComboBox cbxCliente, JTextField idCustomer) {

        String consuta = "select customer.idCustomer from customer where customer.nameCustomer=?";

        try {
            CallableStatement cs = con.getConexion().prepareCall(consuta);
            cs.setString(1, cbxCliente.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                idCustomer.setText(rs.getString("idCustomer"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }
}
