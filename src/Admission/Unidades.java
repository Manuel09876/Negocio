
package Admission;

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

public class Unidades extends javax.swing.JInternalFrame {
    
    DefaultTableModel model;
    int id_unidades;
    String nombre;
    String estado;

    public Unidades(int id_unidades, String nombre, String estado) {
        this.id_unidades = id_unidades;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getId_unidades() {
        return id_unidades;
    }

    public void setId_unidades(int id_unidades) {
        this.id_unidades = id_unidades;
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
    Conectar con = new Conectar();
    Connection connect = con.getConexion();
    PreparedStatement ps;
    ResultSet rs;

    public Unidades() {
        initComponents();
        CargarDatosTabla("");
    }
    
    void CargarDatosTabla(String Valores) {

        try {
            String[] titulosTabla = {"Código", "Descripción", "Estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[3];

            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla

            String ConsultaSQL = "SELECT * FROM unidades";

            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {
                RegistroBD[0] = result.getString("id_unidades");
                RegistroBD[1] = result.getString("nombre");
                RegistroBD[2] = result.getString("estado");

                model.addRow(RegistroBD);
            }

            tbUnidades.setModel(model);
            tbUnidades.getColumnModel().getColumn(0).setPreferredWidth(150);
            tbUnidades.getColumnModel().getColumn(1).setPreferredWidth(350);
            tbUnidades.getColumnModel().getColumn(2).setPreferredWidth(100);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void Guardar() {

        // Variables
        int id_unidades;
        String nombre;
        String sql = "";

        //Obtenemos la informacion de las cajas de texto
        nombre = txtUnidades.getText();

        //Consulta para evitar duplicados
        String consulta = "SELECT * FROM unidades WHERE nombre = ?";

        //Consulta sql para insertar los datos (nombres como en la base de datos)
        sql = "INSERT INTO unidades (nombre)VALUES (?)";

        //Para almacenar los datos empleo un try cash
        try {

            //prepara la coneccion para enviar al sql (Evita ataques al sql)
            PreparedStatement pst = connect.prepareStatement(sql);

            pst.setString(1, nombre);

            //Declara otra variable para validar los registros
            int n = pst.executeUpdate();

            //si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardo exitosamente");

                //Luego Bloquera campos
            }
            CargarDatosTabla("");
            txtUnidades.setText("");
            txtUnidades.requestFocus();

        } catch (SQLException ex) {
            

        }

    }

    public void Eliminar(JTextField id) {

        setId_unidades(Integer.parseInt(id.getText()));

        String consulta = "DELETE from unidades where id_unidades=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);
            cs.setInt(1, getId_unidades());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");
            CargarDatosTabla("");
            txtUnidades.setText("");
            txtIdUnidades.setText("");
            txtUnidades.requestFocus();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());

        }

    }

    public void SeleccionarUnidades(JTable Tabla, JTextField IdUnidades, JTextField Unidades) {

        try {

            int fila = tbUnidades.getSelectedRow();

            if (fila >= 0) {

                IdUnidades.setText(tbUnidades.getValueAt(fila, 0).toString());
                Unidades.setText(tbUnidades.getValueAt(fila, 1).toString());

            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Seleccion, Error: ");
        }

    }

    public void Modificar(JTextField paraId, JTextField paraNombre) {
        
        //Obtengo el valor en Cadena(String) de las cajas de Texto
        setId_unidades(Integer.parseInt(paraId.getText()));
        setNombre(paraNombre.getText());
        
        Conectar con = new Conectar();
        
        String sql = "UPDATE unidades SET nombre = ?  WHERE id_unidades = ?";
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, getNombre());
            ps.setInt(2, getId_unidades());
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Modificacion Exitosa");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se Modifico, error "+e.toString());
            
        }
    }
    
    public boolean accion(String estado, int IdUnidades) {
        String sql = "UPDATE unidades SET estado = ? WHERE id_unidades = ?";
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, IdUnidades);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtUnidades = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtIdUnidades = new javax.swing.JTextField();
        btnInactivar = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUnidades = new javax.swing.JTable();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Unidades");

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

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exchange.png"))); // NOI18N
        jButton2.setText("Modificar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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
                .addComponent(txtUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtIdUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar)
                        .addGap(18, 18, 18)
                        .addComponent(btnInactivar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnActivar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtIdUnidades)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar)
                    .addComponent(btnEliminar)
                    .addComponent(btnInactivar)
                    .addComponent(btnActivar)
                    .addComponent(jButton2))
                .addGap(15, 15, 15))
        );

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        tbUnidades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbUnidades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbUnidadesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbUnidades);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
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
        txtUnidades.setText("");
        txtUnidades.requestFocus();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        Eliminar(txtIdUnidades);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnInactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivarActionPerformed
        int fila = tbUnidades.getSelectedRow();
        int id = Integer.parseInt(txtIdUnidades.getText());
        if (accion("Inactivo", id)) {
            CargarDatosTabla("");
            JOptionPane.showMessageDialog(null, "Inactivado");
        }else{
            JOptionPane.showMessageDialog(null, "Error al Inactivar");
        }
    }//GEN-LAST:event_btnInactivarActionPerformed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        int fila = tbUnidades.getSelectedRow();
        int id = Integer.parseInt(txtIdUnidades.getText());
        if (accion("Activo", id)) {
            CargarDatosTabla("");
            JOptionPane.showMessageDialog(null, "Activar");
        }else{
            JOptionPane.showMessageDialog(null, "Error al Activar");
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void tbUnidadesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUnidadesMouseClicked
        SeleccionarUnidades(tbUnidades, txtIdUnidades, txtUnidades);
    }//GEN-LAST:event_tbUnidadesMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Modificar(txtIdUnidades, txtUnidades);
        CargarDatosTabla("");
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActivar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnInactivar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbUnidades;
    private javax.swing.JTextField txtIdUnidades;
    private javax.swing.JTextField txtUnidades;
    // End of variables declaration//GEN-END:variables
}
