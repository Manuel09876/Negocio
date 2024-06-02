package Administration;

import Bases.Tables;
import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TipoDeUsuario extends javax.swing.JInternalFrame {

    DefaultTableModel model;
    private int idTipoDeUsuaio;
    private String tipoDeUsuario, estado;

    public TipoDeUsuario(int idTipoDeUsuaio, String tipoDeUsuario, String estado) {
        this.idTipoDeUsuaio = idTipoDeUsuaio;
        this.tipoDeUsuario = tipoDeUsuario;
        this.estado = estado;
    }

    public int getIdTipoDeUsuaio() {
        return idTipoDeUsuaio;
    }

    public void setIdTipoDeUsuaio(int idTipoDeUsuaio) {
        this.idTipoDeUsuaio = idTipoDeUsuaio;
    }

    public String getTipoDeUsuario() {
        return tipoDeUsuario;
    }

    public void setTipoDeUsuario(String tipoDeUsuario) {
        this.tipoDeUsuario = tipoDeUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TipoDeUsuario() {
        initComponents();
        
        txtId.setEnabled(false);
        CargarDatosTabla("");
    }

     //Conexión
    Conectar con = new Conectar();
    Connection connect = con.getConexion();
    PreparedStatement ps;
    ResultSet rs;

    //Metodos
   
    void CargarDatosTabla(String Valores){

        try {
            String[] titulosTabla = {"Código", "Descripción","Estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[3];
            
            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla
            
            String ConsultaSQL = "SELECT * FROM tipodeusuario";
            
            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {
                RegistroBD[0] = result.getString("idTipoDeUsuario");
                RegistroBD[1] = result.getString("tipoDeUsuario");
                RegistroBD[2] = result.getString("estado");

                model.addRow(RegistroBD);
            }
            
            tbTipoUsuario.setModel(model);
            tbTipoUsuario.getColumnModel().getColumn(0).setPreferredWidth(150);
            tbTipoUsuario.getColumnModel().getColumn(1).setPreferredWidth(350);
            tbTipoUsuario.getColumnModel().getColumn(2).setPreferredWidth(100);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void Guardar() {

        // Variables
        int idTipoDeUsuario;
        String tipoDeUsuario;
        String sql = "";

        //Obtenemos la informacion de las cajas de texto
        tipoDeUsuario = txtNuevoTU.getText();
        
        //Consulta para evitar duplicados
        String consulta = "SELECT * FROM tipodeusuario WHERE tipoDeUsuario = ?";
        
        //Consulta sql para insertar los datos (nombres como en la base de datos)
        sql = "INSERT INTO tipodeusuario (tipoDeUsuario)VALUES (?)";

        //Para almacenar los datos empleo un try cash
        try {
            
            //prepara la coneccion para enviar al sql (Evita ataques al sql)
            PreparedStatement pst = connect.prepareStatement(sql);
   
            pst.setString(1, tipoDeUsuario);
           
            
            //Declara otra variable para validar los registros
            int n = pst.executeUpdate();

            //si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardo exitosamente");

                //Luego Bloquera campos
                
            }
            CargarDatosTabla("");

        } catch (SQLException ex) {
            Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);

        }

    }
    
    public void Eliminar(JTextField codigo) {

        setIdTipoDeUsuaio(Integer.parseInt(codigo.getText()));

        String consulta = "DELETE from tipodeusuario where idTipoDeUsuario=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);
            cs.setInt(1, getIdTipoDeUsuaio());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());

        }

    }

    public void SeleccionarTipoDeUsuario(JTable Tabla, JTextField codigo, JTextField tipoDeUsuario) {

        try {

            int fila = tbTipoUsuario.getSelectedRow();

            if (fila >= 0) {

                codigo.setText(tbTipoUsuario.getValueAt(fila, 0).toString());
                tipoDeUsuario.setText(tbTipoUsuario.getValueAt(fila, 1).toString());
                

            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Seleccion, Error: ");
        }

    }


    public void modificar(JTextField id, JTextField nombre) {
        
        try {
        
            setIdTipoDeUsuaio(Integer.parseInt(id.getText()));
            setName(nombre.getText());
            
        String sql = "UPDATE tipodeusuario SET tipoDeUsuario = ?  WHERE idTipoDeUsuario = ?";
        
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, getTipoDeUsuario());
            ps.setInt(2, getIdTipoDeUsuaio());
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Modificacion exitosa");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Modificar "+e.toString());
            
        }
    }

    public boolean accion(String estado, int idTipoDeUsuario) {
        String sql = "UPDATE tipodeusuario SET estado = ? WHERE idTipoDeUsuario = ?";
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, idTipoDeUsuario);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }
    }

     public void Limpiar(){
        txtNuevoTU.setText("");
        txtNuevoTU.requestFocus();
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNuevoTU = new javax.swing.JTextField();
        btnGrabarTU = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnModificar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtBuscarTU = new javax.swing.JTextField();
        btnBuscarTU = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTipoUsuario = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        btnInactivar = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Descripción");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));
        jPanel1.add(txtNuevoTU, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 240, -1));

        btnGrabarTU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGrabarTU.setText("Grabar");
        btnGrabarTU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarTUActionPerformed(evt);
            }
        });
        jPanel1.add(btnGrabarTU, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Nuevo Tipo de Usuario");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 0, 428, -1));
        jPanel1.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 80, -1));

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel1.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, -1));

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, 90));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Busqueda de Tipo de Usuario");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 0, -1, -1));

        jLabel4.setText("Descripción");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 25, -1, -1));
        jPanel2.add(txtBuscarTU, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 22, 212, -1));

        btnBuscarTU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        btnBuscarTU.setText("Buscar");
        btnBuscarTU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarTUActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscarTU, new org.netbeans.lib.awtextra.AbsoluteConstraints(316, 14, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 440, 60));

        tbTipoUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Codigo", "Descripción", "Estado"
            }
        ));
        tbTipoUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbTipoUsuarioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbTipoUsuario);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 440, 280));

        jPanel4.setBackground(new java.awt.Color(0, 51, 153));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel4.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(334, 29, -1, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel4.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        btnActivar.setText("Activar");
        btnActivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivarActionPerformed(evt);
            }
        });
        jPanel4.add(btnActivar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, -1, -1));

        btnInactivar.setText("Inactivar");
        btnInactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInactivarActionPerformed(evt);
            }
        });
        jPanel4.add(btnInactivar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, -1, -1));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 440, 80));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGrabarTUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarTUActionPerformed
        Guardar();
        CargarDatosTabla("");
        txtNuevoTU.setText("");
        txtNuevoTU.requestFocus();

    }//GEN-LAST:event_btnGrabarTUActionPerformed

    private void btnBuscarTUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarTUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarTUActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        txtNuevoTU.setText("");
        txtNuevoTU.requestFocus();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        SeleccionarTipoDeUsuario(tbTipoUsuario, txtId, txtNuevoTU);
        Eliminar(txtId);
        CargarDatosTabla("");
        
        txtNuevoTU.setText("");
        txtNuevoTU.requestFocus();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        modificar(txtId, txtNuevoTU);
        CargarDatosTabla("");
        Limpiar();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void tbTipoUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbTipoUsuarioMouseClicked
        SeleccionarTipoDeUsuario(tbTipoUsuario, txtId, txtNuevoTU);
    }//GEN-LAST:event_tbTipoUsuarioMouseClicked

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        Limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        int fila = tbTipoUsuario.getSelectedRow();
        int id = Integer.parseInt(txtId.getText());
        if (accion("Activo", id)) {
            CargarDatosTabla("");
            JOptionPane.showMessageDialog(null, "Activado");
        }else{
            JOptionPane.showMessageDialog(null, "Error al Activar");
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void btnInactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivarActionPerformed
        int fila = tbTipoUsuario.getSelectedRow();
        int id = Integer.parseInt(txtId.getText());
        if (accion("Inactivo", id)) {
            CargarDatosTabla("");
            JOptionPane.showMessageDialog(null, "Inactivado");
        }else{
            JOptionPane.showMessageDialog(null, "Error al Inactivar");
        }
    }//GEN-LAST:event_btnInactivarActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) { 
        SeleccionarTipoDeUsuario(tbTipoUsuario, txtNuevoTU, txtId);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActivar;
    private javax.swing.JButton btnBuscarTU;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGrabarTU;
    private javax.swing.JButton btnInactivar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbTipoUsuario;
    private javax.swing.JTextField txtBuscarTU;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNuevoTU;
    // End of variables declaration//GEN-END:variables
}
