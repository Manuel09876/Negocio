package Reports;

import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Trabajos extends javax.swing.JInternalFrame {

    Conectar con = new Conectar();
    Connection connect = con.getConexion();

    public Trabajos() {
        initComponents();
        AutoCompleteDecorator.decorate(cbxTrabajador);
        MostrarTabla();
        MostrarTrabajador(cbxTrabajador);
        addCheckBox(0, tbTrabajos);
    }

    //Metodo que agrega el checkBox
    public void addCheckBox(int column, JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }

    public boolean IsSelected(int row, int column, JTable table) {
        return table.getValueAt(row, column) != null;
    }

//Muestra la tabla con los datos completos
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
            tbTrabajos.setModel(modelo);
            tbTrabajos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbTrabajos.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbTrabajos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbTrabajos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbTrabajos.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbTrabajos.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbTrabajos.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbTrabajos.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbTrabajos.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbTrabajos.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(11).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(12).setPreferredWidth(80);
            tbTrabajos.getColumnModel().getColumn(13).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(14).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(15).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(16).setPreferredWidth(150);

            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbTrabajos); // Agregar checkbox a la columna 0 de cada fila
            }

        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }
    }

    //Filtrar por Trabajador
    public void FiltrarTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ID = txtIdTrabajador.getText();
        String ID_buscar = "";

        try {
            if (!(ID.equals(""))) {
                ID_buscar = "WHERE worker.idWorker = '" + ID + "'";
            }
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
                    + ID_buscar + " AND o.estado = 'Programado' \n"
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
            tbTrabajos.setModel(modelo);

            tbTrabajos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbTrabajos.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbTrabajos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbTrabajos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbTrabajos.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbTrabajos.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbTrabajos.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbTrabajos.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbTrabajos.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbTrabajos.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(11).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(12).setPreferredWidth(80);
            tbTrabajos.getColumnModel().getColumn(13).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(14).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(15).setPreferredWidth(150);
            tbTrabajos.getColumnModel().getColumn(16).setPreferredWidth(150);

            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbTrabajos); // Agregar checkbox a la columna 0 de cada fila
            }

        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }
    }

    //Asignar Trabajo a otro Trabajador
    public static void modificarIdST(int nuevoId, int idST) {
        DefaultTableModel modelo = new DefaultTableModel();
        Conectar con = new Conectar();
        Connection connect = con.getConexion();

        String sql = "UPDATE servicio_trabajador SET id_Trabajador = ? WHERE id_ST= ?";
        try (PreparedStatement declaracion = connect.prepareStatement(sql)) {
            declaracion.setInt(1, nuevoId);
            declaracion.setInt(2, idST); // Aquí asumo que tienes una columna id_fila que identifica cada fila en tu tabla
            System.out.println("Nuevo id Trabajador " + nuevoId + " el idST " + idST);
            int filasActualizadas = declaracion.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("El ID del trabajador se ha actualizado correctamente en la base de datos.");
            } else {
                System.out.println("No se ha actualizado ningún registro en la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public void cambiarIdTrabajador(JTable tabla, JTextField txtIdTrabajador) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();

        // Buscar la fila seleccionada por el CheckBox
        int idST = -1;

        if (model.getRowCount() > 0) {
            for (int i = 0; i < model.getRowCount(); i++) {
                Boolean checkBoxValue = (Boolean) model.getValueAt(i, 0); // Obtiene el valor del checkbox (true/false)
                if (checkBoxValue != null && checkBoxValue.booleanValue()) { // Verifica si el valor del checkbox es true
                    idST = i;

                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay fila seleccionada");
        }

        if (idST != -1) {
            // Obtener el nuevo ID del JTextField
            int nuevoId = Integer.parseInt(txtIdTrabajador.getText());

            // Actualizar el ID del trabajador en la columna 17 de la fila seleccionada
            model.setValueAt(nuevoId, idST, 17); // Columna 17 para el ID del trabajador
            JOptionPane.showMessageDialog(null, "El ID del trabajador se ha actualizado correctamente en la tabla.");
                        
        } else {
            JOptionPane.showMessageDialog(null, "Ninguna fila ha sido seleccionada.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        cbxTrabajador = new javax.swing.JComboBox<>();
        txtIdTrabajador = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        btnMostrarTodo = new javax.swing.JButton();
        btnReprogramar = new javax.swing.JButton();
        btnReasignar = new javax.swing.JButton();
        cbSeleccion = new javax.swing.JCheckBox();
        btnTrabajoRealizado = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTrabajos = new javax.swing.JTable();

        setMaximizable(true);
        setTitle("Trabajos");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 102, 255));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });

        btnFiltrar.setText("Firltrar por Trabajador");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });

        btnMostrarTodo.setText("Mostrar Todo");
        btnMostrarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarTodoActionPerformed(evt);
            }
        });

        btnReprogramar.setText("Reprogramar");
        btnReprogramar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReprogramarActionPerformed(evt);
            }
        });

        btnReasignar.setText("Reasignar");
        btnReasignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReasignarActionPerformed(evt);
            }
        });

        cbSeleccion.setText("Seleccionar");
        cbSeleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSeleccionActionPerformed(evt);
            }
        });

        btnTrabajoRealizado.setText("Trabajo Realizado");
        btnTrabajoRealizado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrabajoRealizadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnFiltrar)
                            .addComponent(cbSeleccion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnReprogramar))
                    .addComponent(cbxTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(257, 257, 257)
                        .addComponent(btnMostrarTodo))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtIdTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 256, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnReasignar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTrabajoRealizado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(157, 157, 157)
                .addComponent(btnSalir)
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtIdTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSalir)
                        .addComponent(btnTrabajoRealizado)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(btnFiltrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbSeleccion)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReprogramar)
                            .addComponent(btnReasignar)
                            .addComponent(btnMostrarTodo))
                        .addGap(21, 21, 21))))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 1320, -1));

        tbTrabajos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbTrabajos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 141, 1320, 430));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        FiltrarTabla();
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnMostrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodoActionPerformed
        MostrarTabla();
    }//GEN-LAST:event_btnMostrarTodoActionPerformed

    private void btnReprogramarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprogramarActionPerformed
        try {
            // Recorremos todas las filas de la tabla
            for (int i = 0; i < tbTrabajos.getRowCount(); i++) {
                // Verificamos si el checkbox de la fila actual está seleccionado
                Object value = tbTrabajos.getValueAt(i, 0);
                if (value != null && value instanceof Boolean) {
                    boolean isChecked = (boolean) value;
                    // Obtenemos el ID de la fila actual
                    int id = Integer.parseInt(tbTrabajos.getValueAt(i, 1).toString());
                    System.out.println(id);
                    // Actualizar el estado del registro a "Activo" en la tabla orderservice
                    String sqlUpdate = "UPDATE orderservice SET estado = 'Activo' WHERE id = ?";
                    try (PreparedStatement pstmtUpdate = connect.prepareStatement(sqlUpdate)) {
                        pstmtUpdate.setInt(1, id);
                        pstmtUpdate.executeUpdate();
                    }

                    int idST = Integer.parseInt(tbTrabajos.getValueAt(i, 18).toString());
                    System.out.println(idST);
                    // Borrar el registro correspondiente de la tabla servicio_trabajador
                    String deleteQuery = "DELETE FROM servicio_trabajador WHERE id_ST = ?";
                    try (PreparedStatement pstmtDelete = connect.prepareStatement(deleteQuery)) {
                        pstmtDelete.setInt(1, idST);
                        pstmtDelete.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Se eliminó el registro de servicio_trabajador");
                    }

                    MostrarTabla(); // Actualizar la tabla después de realizar los cambios
                }
            }
        } catch (SQLException e) { // Captura otras excepciones diferentes de SQLException
            System.err.println("Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnReprogramarActionPerformed

    private void btnReasignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReasignarActionPerformed

        try {
            cambiarIdTrabajador(tbTrabajos, txtIdTrabajador);
            // Recorremos todas las filas de la tabla
            for (int i = 0; i < tbTrabajos.getRowCount(); i++) {
                // Verificamos si el checkbox de la fila actual está seleccionado

                Object value = tbTrabajos.getValueAt(i, 0);
                if (value != null && value instanceof Boolean) {
                    boolean isChecked = (boolean) value;

                    // Obtenemos el ID de la fila actual
                    int idST = Integer.parseInt((String) tbTrabajos.getValueAt(i, 18));
                    int id_Trabajador = Integer.parseInt(txtIdTrabajador.getText());
//                    System.out.println("idST "+idST+ " id_Trabajador "+id_Trabajador);
// Actualizar el estado del registro a "Activo"                    
                    String sqlUpdate = "UPDATE servicio_trabajador SET id_Trabajador = ? WHERE id_ST = ?";
                    PreparedStatement pstmtUpdate = connect.prepareStatement(sqlUpdate);
                    pstmtUpdate.setInt(1, id_Trabajador);
                    pstmtUpdate.setInt(2, idST);
                    pstmtUpdate.executeUpdate();

                    MostrarTabla();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar actualizar el estado: " + e.getMessage());
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnReasignarActionPerformed

    private void cbSeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSeleccionActionPerformed
        if (cbSeleccion.isSelected()) {
            cbSeleccion.setText("Deseleccionar Todo");
            for (int i = 0; i < tbTrabajos.getRowCount(); i++) {
                tbTrabajos.setValueAt(true, i, 0);
            }
        } else {
            cbSeleccion.setText("Seleccionar Todo");
            for (int i = 0; i < tbTrabajos.getRowCount(); i++) {
                tbTrabajos.setValueAt(false, i, 0);
            }
        }
    }//GEN-LAST:event_cbSeleccionActionPerformed

    private void btnTrabajoRealizadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrabajoRealizadoActionPerformed
        try {
            // Recorremos todas las filas de la tabla
            for (int i = 0; i < tbTrabajos.getRowCount(); i++) {
                // Verificamos si el checkbox de la fila actual está seleccionado
                Object value = tbTrabajos.getValueAt(i, 0);
                if (value != null && value instanceof Boolean) {
                    boolean isChecked = (boolean) value;
                    // Obtenemos el ID de la fila actual
                    int id = Integer.parseInt(tbTrabajos.getValueAt(i, 1).toString());
                    System.out.println(id);
                    // Actualizar el estado del registro a "Realizado" en la tabla orderservice
                    String sqlUpdate = "UPDATE orderservice SET estado = 'Realizado' WHERE id = ?";
                    try (PreparedStatement pstmtUpdate = connect.prepareStatement(sqlUpdate)) {
                        pstmtUpdate.setInt(1, id);
                        pstmtUpdate.executeUpdate();
                    }

                    int idST = Integer.parseInt(tbTrabajos.getValueAt(i, 18).toString());
                    System.out.println(idST);
                    // Borrar el registro correspondiente de la tabla servicio_trabajador
                    String deleteQuery = "DELETE FROM servicio_trabajador WHERE id_ST = ?";
                    try (PreparedStatement pstmtDelete = connect.prepareStatement(deleteQuery)) {
                        pstmtDelete.setInt(1, idST);
                        pstmtDelete.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Se eliminó el registro de servicio_trabajador");
                    }

                    MostrarTabla(); // Actualizar la tabla después de realizar los cambios
                }
            }
        } catch (SQLException e) { // Captura otras excepciones diferentes de SQLException
            System.err.println("Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTrabajoRealizadoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnFiltrar;
    public javax.swing.JButton btnMostrarTodo;
    public javax.swing.JButton btnReasignar;
    public javax.swing.JButton btnReprogramar;
    private javax.swing.JButton btnSalir;
    public javax.swing.JButton btnTrabajoRealizado;
    private javax.swing.JCheckBox cbSeleccion;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable tbTrabajos;
    private javax.swing.JTextField txtIdTrabajador;
    // End of variables declaration//GEN-END:variables
//Muestra los Trabajadores en el ComboBox
    public void MostrarTrabajador(JComboBox comboTrabajador) {

        String sql = "";
        sql = "select * from worker";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            comboTrabajador.removeAllItems();

            while (rs.next()) {

                comboTrabajador.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoTrabajador(JComboBox trabajador, JTextField IdTrabajador) {

        String consuta = "select worker.idWorker from worker where worker.nombre=?";

        try {
            CallableStatement cs = con.getConexion().prepareCall(consuta);
            cs.setString(1, trabajador.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                IdTrabajador.setText(rs.getString("idWorker"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

}
