
package Admission;

import Administration.Productos;
import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class Marcas extends javax.swing.JInternalFrame {

    DefaultTableModel model;
    int id_marca;
    String Nombre;
    String estado;

    public Marcas(int id_marca, String Nombre, String estado) {
        this.id_marca = id_marca;
        this.Nombre = Nombre;
        this.estado = estado;
    }

    public int getId_marca() {
        return id_marca;
    }

    public void setId_marca(int id_marca) {
        this.id_marca = id_marca;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
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

    public Marcas() {
        initComponents();
        CargarDatosTabla("");
        txtIdMarcas.setVisible(false);
    }
    
    public void CargarDatosTabla(String Valores) {
        try {
            String[] titulosTabla = {"Código", "Descripción", "Estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[3];
            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla
            String ConsultaSQL = "SELECT * FROM marca";
            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);
            while (result.next()) {
                RegistroBD[0] = result.getString("id_marca");
                RegistroBD[1] = result.getString("nombre");
                RegistroBD[2] = result.getString("estado");
                model.addRow(RegistroBD);
            }
            tbMarcas.setModel(model);
            tbMarcas.getColumnModel().getColumn(0).setPreferredWidth(150);
            tbMarcas.getColumnModel().getColumn(1).setPreferredWidth(350);
            tbMarcas.getColumnModel().getColumn(2).setPreferredWidth(100);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void Guardar() {
        // Variables
        int id_marca;
        String nombre;
        String sql = "";
        //Obtenemos la informacion de las cajas de texto
        nombre = txtMarcas.getText();
        //Consulta para evitar duplicados
        String consulta = "SELECT * FROM marca WHERE nombre = ?";
        //Consulta sql para insertar los datos (nombres como en la base de datos)
        sql = "INSERT INTO marca (nombre)VALUES (?)";
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
            txtMarcas.setText("");
            txtMarcas.requestFocus();
        } catch (SQLException ex) {
            Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Eliminar(JTextField id) {
        setId_marca(Integer.parseInt(id.getText()));
        String consulta = "DELETE from marca where id_marca=?";
        try {
            CallableStatement cs = con.getConexion().prepareCall(consulta);
            cs.setInt(1, getId_marca());
            cs.executeUpdate();
            JOptionPane.showMessageDialog(null, "Se Elimino");
            CargarDatosTabla("");
            txtMarcas.setText("");
            txtIdMarcas.setText("");
            txtMarcas.requestFocus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());
        }
    }

    public void SeleccionarCategoria(JTable Tabla, JTextField IdMarca, JTextField Marca) {
        try {
            int fila = tbMarcas.getSelectedRow();
            if (fila >= 0) {
                IdMarca.setText(tbMarcas.getValueAt(fila, 0).toString());
                Marca.setText(tbMarcas.getValueAt(fila, 1).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Seleccion, Error: ");
        }
    }

    public void Modificar(JTextField paraId, JTextField paranombre) {        
         //Obtengo el valor en Cadena(String) de las cajas de Texto
        setId_marca(Integer.parseInt(paraId.getText()));
        setNombre(paranombre.getText());        
        Conectar con = new Conectar();        
        String sql = "UPDATE marca SET nombre = ?  WHERE id_marca = ?";
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, getNombre());
            ps.setInt(2, getId_marca());
            ps.execute();            
            JOptionPane.showMessageDialog(null, "Modificacion Exitosa");            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se Modifico, error "+e.toString());            
        }
    }
    
    public boolean accion(String estado, int IdMarca) {
        String sql = "UPDATE marca SET estado = ? WHERE id_marca = ?";
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, IdMarca);
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
        jLabel1 = new javax.swing.JLabel();
        txtMarcas = new javax.swing.JTextField();
        txtIdMarcas = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        btnInactivar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuia = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbMarcas = new javax.swing.JTable();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Marcas");
        setPreferredSize(new java.awt.Dimension(575, 358));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Marca");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 23, -1, -1));
        jPanel1.add(txtMarcas, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 20, 206, -1));
        jPanel1.add(txtIdMarcas, new org.netbeans.lib.awtextra.AbsoluteConstraints(339, 20, 78, 26));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(661, 6, -1, -1));

        btnActivar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/verify users.png"))); // NOI18N
        btnActivar.setText("Activar");
        btnActivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivarActionPerformed(evt);
            }
        });
        jPanel1.add(btnActivar, new org.netbeans.lib.awtextra.AbsoluteConstraints(634, 52, -1, -1));

        btnInactivar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnInactivar.setText("Inactivar");
        btnInactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInactivarActionPerformed(evt);
            }
        });
        jPanel1.add(btnInactivar, new org.netbeans.lib.awtextra.AbsoluteConstraints(513, 57, -1, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(398, 57, -1, -1));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 57, -1, -1));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 55, -1, -1));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exchange.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel1.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 52, -1, -1));

        btnGuia.setText("Guia");
        btnGuia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiaActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuia, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 18, -1, -1));

        tbMarcas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbMarcas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbMarcasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbMarcas);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 738, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 128, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        int fila = tbMarcas.getSelectedRow();
        int id = Integer.parseInt(txtIdMarcas.getText());
        if (accion("Activo", id)) {
            CargarDatosTabla("");
            JOptionPane.showMessageDialog(null, "Activado");
        }else{
            JOptionPane.showMessageDialog(null, "Error al reingresar");
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void btnInactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivarActionPerformed
        int fila = tbMarcas.getSelectedRow();
        int id = Integer.parseInt(txtIdMarcas.getText());
        if (accion("Inactivo", id)) {
            CargarDatosTabla("");
            JOptionPane.showMessageDialog(null, "Inactivado");
        }else{
            JOptionPane.showMessageDialog(null, "Error al eliminar");
        }
    }//GEN-LAST:event_btnInactivarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        Eliminar(txtIdMarcas);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        txtMarcas.setText("");
        txtMarcas.requestFocus();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tbMarcasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbMarcasMouseClicked
        SeleccionarCategoria(tbMarcas, txtIdMarcas, txtMarcas);
    }//GEN-LAST:event_tbMarcasMouseClicked

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        Modificar(txtIdMarcas, txtMarcas);
        CargarDatosTabla("");
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnGuiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiaActionPerformed
        JOptionPane.showMessageDialog(null, "MARCAS\n"
                + "Llenar todas la casilla para el ingreso de Nuevas Marcas\n"
                + "Botón GUARDAR para guardar los datos que se mostraran en una Tabla\n"
                + "Botón CANCELAR para limpiar las casillas\n"
                + "Botón MODIFICAR  seleccionamos una fila de la Tabla para modificar los datos y presionamos Modificar\n"
                + "Botón ELIMINAR seleccionamos la fila de la Tabla que queremos eliminar y click en Eliminar\n"
                + "Botón ACTIVAR para activar una Marca que habia sido desactivado\n"
                + "Botón DESACTIVAR´para desactivar una Marca\n"
                + "Botón NUEVO limpiara las casilla y se ubicará el puntero en Marca\n"
                + "Esta Interfaz es para el Ingreso de Nuevas Marcas");
    }//GEN-LAST:event_btnGuiaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnActivar;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuia;
    public javax.swing.JButton btnInactivar;
    public javax.swing.JButton btnModificar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbMarcas;
    private javax.swing.JTextField txtIdMarcas;
    private javax.swing.JTextField txtMarcas;
    // End of variables declaration//GEN-END:variables
}
