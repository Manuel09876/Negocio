package Admission;

import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TipoProductosMateriales extends javax.swing.JInternalFrame {

    DefaultTableModel model = new DefaultTableModel();
    int idProMat;
    String nombre;
    String estado;

    public TipoProductosMateriales(int idProMat, String nombre, String estado) {
        this.idProMat = idProMat;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getIdProMat() {
        return idProMat;
    }

    public void setIdProMat(int idProMat) {
        this.idProMat = idProMat;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

   
    PreparedStatement ps;
    ResultSet rs;

    public TipoProductosMateriales() {
        initComponents();
        
        CargarDatosTabla("");
        txtIdProMat.setEnabled(false);
    }

        
    void CargarDatosTabla(String Valores) {
Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
    try {
            String[] titulosTabla = {"Código", "Descripción", "Estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[3];
            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla
            String ConsultaSQL = "SELECT * FROM tipodeproductomateriales";
            
            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);
            while (result.next()) {
                RegistroBD[0] = result.getString("idProMat");
                RegistroBD[1] = result.getString("nombre");
                RegistroBD[2] = result.getString("estado");
                model.addRow(RegistroBD);
            }
            tbProMat.setModel(model);
            tbProMat.getColumnModel().getColumn(0).setPreferredWidth(150);
            tbProMat.getColumnModel().getColumn(1).setPreferredWidth(350);
            tbProMat.getColumnModel().getColumn(2).setPreferredWidth(100);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    void Guardar() {
    Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
    // Variables
        int idProMat;
        String nombre;
        String sql = "";
        //Obtenemos la informacion de las cajas de texto
        nombre = txtProMat.getText();
        //Consulta para evitar duplicados
        String consulta = "SELECT * FROM tipodeproductomateriales WHERE nombre = ?";
        //Consulta sql para insertar los datos (nombres como en la base de datos)
        sql = "INSERT INTO tipodeproductomateriales (nombre)VALUES (?)";
        //Para almacenar los datos empleo un try cash
        try {
            //prepara la coneccion para enviar al sql (Evita ataques al sql)
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, nombre);
            //Declara otra variable para validar los registros
            int n = pst.executeUpdate();
            //si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardo exitosamente");
                //Luego Bloquera campos
            }
            CargarDatosTabla("");
            txtProMat.setText("");
            txtProMat.requestFocus();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar " + ex.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void Eliminar(JTextField id) {
Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        setIdProMat(Integer.parseInt(id.getText()));
        String consulta = "DELETE from tipodeproductomateriales where idProMat=?";
        try {
            CallableStatement cs = connection.prepareCall(consulta);
            cs.setInt(1, getIdProMat());
            cs.executeUpdate();
            JOptionPane.showMessageDialog(null, "Se Elimino");
            CargarDatosTabla("");
            txtProMat.setText("");
            txtIdProMat.setText("");
            txtProMat.requestFocus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void SeleccionarTipo(JTable Tabla, JTextField IdProMat, JTextField tipo) {
        try {
            int fila = tbProMat.getSelectedRow();
            if (fila >= 0) {
                IdProMat.setText(tbProMat.getValueAt(fila, 0).toString());
                tipo.setText(tbProMat.getValueAt(fila, 1).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Seleccion, Error: ");
        }
    }

    public void ModificarTipo(JTextField paraId, JTextField paraNombre) {
Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
    //Obtengo el valor en Cadena(String) de las cajas de Texto
        setIdProMat(Integer.parseInt(paraId.getText()));
        setNombre(paraNombre.getText());
        
        String consulta = "UPDATE tipodeproductomateriales SET tiopodeproductomateriales.nombre = ? WHERE tipodeproductomateriales.idProMat=?";
        try {
            CallableStatement cs = connection.prepareCall(consulta);
            cs.setString(1, getNombre());
            cs.setInt(2, getIdProMat());
            cs.execute();
            JOptionPane.showMessageDialog(null, "Modificacion Exitosa");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se Modifico, error " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public boolean accion(String estado, int IdProMat) {
    Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "UPDATE tipodeproductomateriales SET estado = ? WHERE idProMat = ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, IdProMat); // Usar el parámetro pasado en lugar de la variable de clase
            ps.executeUpdate(); // Cambia a executeUpdate para claridad
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtProMat = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtIdProMat = new javax.swing.JTextField();
        btnInactivar = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuia = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbProMat = new javax.swing.JTable();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Tipo de Productos y Materiales");

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Tipo");

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnInactivar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnInactivar.setText("Inactivar");
        btnInactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInactivarActionPerformed(evt);
            }
        });

        btnActivar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/verify users.png"))); // NOI18N
        btnActivar.setText("Activar");
        btnActivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exchange.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnGuia.setText("Guia");
        btnGuia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addComponent(txtProMat, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtIdProMat, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuia)
                .addGap(32, 32, 32)
                .addComponent(jButton1))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGuardar)
                .addGap(18, 18, 18)
                .addComponent(btnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnInactivar)
                .addGap(18, 18, 18)
                .addComponent(btnActivar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 14, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txtProMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtIdProMat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(btnGuia))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnGuardar)
                        .addComponent(btnModificar))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnActivar)
                        .addComponent(btnInactivar)
                        .addComponent(btnEliminar)
                        .addComponent(btnCancelar)))
                .addGap(12, 12, 12))
        );

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        tbProMat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbProMat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProMatMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbProMat);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        txtProMat.setText("");
        txtProMat.requestFocus();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        Eliminar(txtIdProMat);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnInactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivarActionPerformed
        int fila = tbProMat.getSelectedRow();
        if (fila >= 0) {
            String idText = txtIdProMat.getText().trim();
            if (!idText.isEmpty()) {
                try {
                    int id = Integer.parseInt(idText);
                    if (accion("Inactivo", id)) {
                        CargarDatosTabla("");
                        JOptionPane.showMessageDialog(null, "Inactivado");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al Inactivar");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "El ID no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione un tipo de producto o material.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnInactivarActionPerformed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        int fila = tbProMat.getSelectedRow();
        if (fila >= 0) {
            String idText = txtIdProMat.getText().trim();
            if (!idText.isEmpty()) {
                try {
                    int id = Integer.parseInt(idText);
                    if (accion("Activo", id)) {
                        CargarDatosTabla("");
                        JOptionPane.showMessageDialog(null, "Activado");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al Activar");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "El ID no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione un tipo de producto o material.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        SeleccionarTipo(tbProMat, txtIdProMat, txtProMat);
        ModificarTipo(txtIdProMat, txtProMat);
        CargarDatosTabla("");
    }//GEN-LAST:event_btnModificarActionPerformed

    private void tbProMatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProMatMouseClicked
        SeleccionarTipo(tbProMat, txtIdProMat, txtProMat);
    }//GEN-LAST:event_tbProMatMouseClicked

    private void btnGuiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiaActionPerformed
        JOptionPane.showMessageDialog(null, "TIPO DE PRODUCTOS Y MATERIALES\n"
                + "Llenar la casilla para el ingreso de tipo de Productos y Materiales\n"
                + "Botón GUARDAR para guardar los datos que se mostraran en una Tabla\n"
                + "Botón CANCELAR para limpiar las casillas\n"
                + "Botón MODIFICAR  seleccionamos una fila de la Tabla para modificar los datos y presionamos Modificar\n"
                + "Botón ELIMINAR seleccionamos la fila de la Tabla que queremos eliminar y click en Eliminar\n"
                + "Botón ACTIVAR para activar un Tipo de Productos y Materiales que habia sido desactivado\n"
                + "Botón DESACTIVAR´para desactivar un Tipo de Productos y Materiales\n"
                + "Botón NUEVO limpiara las casilla y se ubicará el puntero en Tipo\n"
                + "Esta Interfaz es para el Ingreso de nuevos Tipo de Productos y Materiales");
    }//GEN-LAST:event_btnGuiaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnActivar;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuia;
    public javax.swing.JButton btnInactivar;
    public javax.swing.JButton btnModificar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbProMat;
    private javax.swing.JTextField txtIdProMat;
    private javax.swing.JTextField txtProMat;
    // End of variables declaration//GEN-END:variables
}
