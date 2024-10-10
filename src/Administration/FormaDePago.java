package Administration;

import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FormaDePago extends javax.swing.JInternalFrame {

    DefaultTableModel model = new DefaultTableModel();
    int id_formasdepago;
    String nombre;
    String estado;

    public FormaDePago(int id_formasdepago, String nombre, String estado) {
        this.id_formasdepago = id_formasdepago;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getId_formasdepago() {
        return id_formasdepago;
    }

    public void setId_formasdepago(int id_formasdepago) {
        this.id_formasdepago = id_formasdepago;
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

    //Conexión
    PreparedStatement ps;
    ResultSet rs;

    public FormaDePago() {
        initComponents();

        CargarDatosTabla("");
        txtIdFormasDePago.setEnabled(false);
    }

    void CargarDatosTabla(String Valores) {
        Connection connection = null;

        try {
            String[] titulosTabla = {"Código", "Descripción", "Estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[3];

            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla

            String ConsultaSQL = "SELECT * FROM formadepago";

            // Obtener la conexión del pool
            connection = Conectar.getInstancia().obtenerConexion();

            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {
                RegistroBD[0] = result.getString("id_formadepago");
                RegistroBD[1] = result.getString("nombre");
                RegistroBD[2] = result.getString("estado");

                model.addRow(RegistroBD);
            }

            tbFormasDePago.setModel(model);
            tbFormasDePago.getColumnModel().getColumn(0).setPreferredWidth(150);
            tbFormasDePago.getColumnModel().getColumn(1).setPreferredWidth(350);
            tbFormasDePago.getColumnModel().getColumn(2).setPreferredWidth(100);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    void Guardar() {
        Connection connection = null;
        // Variables
        int id_formadepago;
        String nombre;
        String sql = "";

        //Obtenemos la informacion de las cajas de texto
        nombre = txtFormasDePago.getText();

        //Consulta para evitar duplicados
        String consulta = "SELECT * FROM formadepago WHERE nombre = ?";

        //Consulta sql para insertar los datos (nombres como en la base de datos)
        sql = "INSERT INTO formadepago (nombre)VALUES (?)";

        //Para almacenar los datos empleo un try cash
        try {
            // Obtener la conexión del pool
            connection = Conectar.getInstancia().obtenerConexion();

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
            txtFormasDePago.setText("");
            txtFormasDePago.requestFocus();

        } catch (SQLException ex) {
            Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void Eliminar(JTextField id) {
        Connection connection = null;
        setId_formasdepago(Integer.parseInt(id.getText()));

        String consulta = "DELETE from formadepago where id_formadepago=?";

        try {
            // Obtener la conexión del pool
            connection = Conectar.getInstancia().obtenerConexion();

            CallableStatement cs = connection.prepareCall(consulta);
            cs.setInt(1, getId_formasdepago());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");
            CargarDatosTabla("");
            txtFormasDePago.setText("");
            txtIdFormasDePago.setText("");
            txtFormasDePago.requestFocus();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());

        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void SeleccionarCategoria(JTable Tabla, JTextField IdFormaDePago, JTextField FormaDePago) {

        try {

            int fila = tbFormasDePago.getSelectedRow();

            if (fila >= 0) {

                IdFormaDePago.setText(tbFormasDePago.getValueAt(fila, 0).toString());
                FormaDePago.setText(tbFormasDePago.getValueAt(fila, 1).toString());

            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Seleccion, Error: ");
        }

    }

    public boolean modificar(FormaDePago fp) {
        Connection connection = null;
        String sql = "UPDATE formadepago SET nombre = ?  WHERE id_formadepago = ?";
        try {
            connection = Conectar.getInstancia().obtenerConexion();

            ps = connection.prepareStatement(sql);
            ps.setString(1, fp.getNombre());
            ps.setInt(2, fp.getId_formasdepago());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public boolean accion(String estado, int IdFormaDePago) {
        Connection connection = null;
        String sql = "UPDATE formadepago SET estado = ? WHERE id_formadepago = ?";
        try {
            connection = Conectar.getInstancia().obtenerConexion();

            ps = connection.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, IdFormaDePago);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }finally {
        // Devolver la conexión al pool
        Conectar.getInstancia().devolverConexion(connection);
    }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtFormasDePago = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtIdFormasDePago = new javax.swing.JTextField();
        btnInactivar = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        btnGuia = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbFormasDePago = new javax.swing.JTable();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Formas de Pago");

        txtFormasDePago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFormasDePagoActionPerformed(evt);
            }
        });

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
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(btnGuardar))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnInactivar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnActivar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                        .addComponent(btnGuia)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(txtFormasDePago, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(txtIdFormasDePago, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))))
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
                                    .addComponent(txtFormasDePago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtIdFormasDePago)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar)
                    .addComponent(btnEliminar)
                    .addComponent(btnInactivar)
                    .addComponent(btnActivar)
                    .addComponent(btnGuia))
                .addGap(15, 15, 15))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 703, -1));

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        tbFormasDePago.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbFormasDePago.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbFormasDePagoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbFormasDePago);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 116, 703, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        txtFormasDePago.setText("");
        txtFormasDePago.requestFocus();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        Eliminar(txtIdFormasDePago);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnInactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivarActionPerformed
        int fila = tbFormasDePago.getSelectedRow();
        int id = Integer.parseInt(txtIdFormasDePago.getText());
        if (accion("Inactivo", id)) {
            CargarDatosTabla("");
            JOptionPane.showMessageDialog(null, "Inactivado");
        } else {
            JOptionPane.showMessageDialog(null, "Error al Inactivar");
        }
    }//GEN-LAST:event_btnInactivarActionPerformed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        int fila = tbFormasDePago.getSelectedRow();
        int id = Integer.parseInt(txtIdFormasDePago.getText());
        if (accion("Activo", id)) {
            CargarDatosTabla("");
            JOptionPane.showMessageDialog(null, "Activar");
        } else {
            JOptionPane.showMessageDialog(null, "Error al Activar");
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void tbFormasDePagoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbFormasDePagoMouseClicked
        SeleccionarCategoria(tbFormasDePago, txtIdFormasDePago, txtFormasDePago);
    }//GEN-LAST:event_tbFormasDePagoMouseClicked

    private void txtFormasDePagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFormasDePagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFormasDePagoActionPerformed

    private void btnGuiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiaActionPerformed
        JOptionPane.showMessageDialog(null, "FORMAS DE PAGO\n"
                + "En la casilla Forma de Pago ingresar la nueva Forma de Trabajo\n"
                + "Botón GUARDAR para guardar el dato que se mostrará en la Tabla\n"
                + "Botón CANCELAR para limpiar la casilla\n"
                + "Botón ELIMINAR seleccionamos la fila de la Tabla que queremos eliminar y click en Eliminar\n"
                + "Botón ACTIVAR para activar una Forma de Pago que habia sido desactivado\n"
                + "Botón INACTIVAR´para desactivar a una Forma de Pago\n"
                + "Esta Interfaz es para determinar las diferentes Formas de Pago");
    }//GEN-LAST:event_btnGuiaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnActivar;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuia;
    public javax.swing.JButton btnInactivar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbFormasDePago;
    private javax.swing.JTextField txtFormasDePago;
    private javax.swing.JTextField txtIdFormasDePago;
    // End of variables declaration//GEN-END:variables
}
