package Administration;

import Bases.Tables;
import conectar.Conectar;
import java.awt.HeadlessException;
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

public class Roles extends javax.swing.JInternalFrame {

    DefaultTableModel model;
    private int id_rol;
    private String rol, estado;

    public Roles(int idTipoDeUsuaio, String tipoDeUsuario, String estado) {
        this.id_rol = idTipoDeUsuaio;
        this.rol = tipoDeUsuario;
        this.estado = estado;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Roles() {
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
    public void CargarDatosTabla(String Valores) {

        try {
            String[] titulosTabla = {"Código", "Descripción", "Estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[3];

            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla

            String ConsultaSQL = "SELECT * FROM roles";

            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {
                RegistroBD[0] = result.getString("id");
                RegistroBD[1] = result.getString("nombre");
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

    public void Guardar() {

        // Variables
        int idTipoDeUsuario;
        String tipoDeUsuario;
        String sql = "";

        //Obtenemos la informacion de las cajas de texto
        tipoDeUsuario = txtNuevoTU.getText();

        //Consulta para evitar duplicados
        String consulta = "SELECT * FROM roles WHERE nombre = ?";

        //Consulta sql para insertar los datos (nombres como en la base de datos)
        sql = "INSERT INTO roles (nombre)VALUES (?)";

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

        setId_rol(Integer.parseInt(codigo.getText()));

        String consulta = "DELETE from roles where id=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);
            cs.setInt(1, getId_rol());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");

        } catch (HeadlessException | SQLException e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());

        }

    }

    public void SeleccionarTipoDeUsuario(JTable Tabla, JTextField codigo, JTextField tipoDeUsuario) {
        try {
            int fila = tbTipoUsuario.getSelectedRow();

            if (fila >= 0) {
                // Solo asigna el ID y nombre si son necesarios para la operación actual
                String idSeleccionado = tbTipoUsuario.getValueAt(fila, 0).toString();
                String nombreSeleccionado = tbTipoUsuario.getValueAt(fila, 1).toString();

                // Verificar si es necesario actualizar los campos de texto
                if (!idSeleccionado.equals(codigo.getText())) {
                    codigo.setText(idSeleccionado);
                }
                if (!nombreSeleccionado.equals(tipoDeUsuario.getText())) {
                    tipoDeUsuario.setText(nombreSeleccionado);
                }

                // Debug: Imprimir después de la selección
                System.out.println("ID seleccionado: " + codigo.getText());
                System.out.println("Nombre seleccionado después de la selección: " + tipoDeUsuario.getText());
            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error de Selección, Error: " + e.toString());
        }
    }

    public void modificar(JTextField id, JTextField nombre) {
        try {
            // Obtener y verificar el nuevo nombre
            String idText = id.getText().trim();
            String nuevoNombre = nombre.getText().trim();

            // Debug: Verificar el nuevo nombre antes de la actualización
            System.out.println("Nuevo nombre antes de la actualización: " + nuevoNombre);

            // Validaciones y configuración de valores en el objeto
            // Establecer la conexión y preparar la consulta
            connect = con.getConexion();
            String sql = "UPDATE roles SET nombre = ? WHERE id = ?";
            ps = connect.prepareStatement(sql);

            // Configurar los valores en el objeto
            ps.setString(1, nuevoNombre);
            ps.setInt(2, Integer.parseInt(idText));

            // Debug: Verificar antes de ejecutar el SQL
            System.out.println("Actualizando en la base de datos con ID: " + idText + " y Nombre: " + nuevoNombre);

            // Ejecución de la consulta de actualización en la base de datos
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Confirmación de actualización
                System.out.println("Actualización exitosa en la base de datos");
                JOptionPane.showMessageDialog(null, "Modificación exitosa");

                // Verificar la actualización en la base de datos
                verificarActualizacion(Integer.parseInt(idText), nuevoNombre);
            } else {
                System.out.println("La actualización en la base de datos no tuvo éxito.");
                JOptionPane.showMessageDialog(null, "Error al modificar el rol");
            }

            // ...
        } catch (SQLException e) {
            System.out.println("Error al Modificar: " + e.toString());
            JOptionPane.showMessageDialog(null, "Error al Modificar: " + e.toString());
        }
    }

    public void verificarActualizacion(int idRol, String nuevoNombre) {
        String sqlVerificacion = "SELECT nombre FROM roles WHERE id = ?";
        try {
            PreparedStatement psVerificacion = connect.prepareStatement(sqlVerificacion);
            psVerificacion.setInt(1, idRol);
            ResultSet rs = psVerificacion.executeQuery();

            if (rs.next()) {
                String nombreEnBD = rs.getString("nombre");
                System.out.println("Nombre en la base de datos después de la actualización: " + nombreEnBD);

                if (nombreEnBD.equals(nuevoNombre)) {
                    System.out.println("Verificación exitosa: Los cambios se realizaron correctamente.");
                } else {
                    System.out.println("Verificación fallida: El nombre no coincide con el valor esperado.");
                }
            } else {
                System.out.println("No se encontró el rol con ID: " + idRol);
            }

            rs.close();
            psVerificacion.close();
        } catch (SQLException e) {
            System.out.println("Error al verificar la actualización: " + e.getMessage());
        }
    }

    public boolean accion(String estado, int idTipoDeUsuario) {
        String sql = "UPDATE roles SET estado = ? WHERE id = ?";
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

    public void Limpiar() {
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
        btnGuia = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTipoUsuario = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        btnInactivar = new javax.swing.JButton();

        setTitle("Roles");
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

        btnGuia.setText("Guia");
        btnGuia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiaActionPerformed(evt);
            }
        });
        jPanel2.add(btnGuia, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, -1, -1));

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
        // Debug: Verificar el valor de txtNuevoTU antes de cualquier operación
        System.out.println("Contenido de txtNuevoTU antes de modificar: " + txtNuevoTU.getText());

        // Asegúrate de capturar el nuevo nombre de usuario antes de cualquier cambio
        String nuevoNombre = txtNuevoTU.getText().trim();
        String idActual = txtId.getText().trim();

        // Verifica si se seleccionó un ID válido y el nuevo nombre no está vacío
        if (idActual.isEmpty() || nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un usuario y proporcionar un nuevo nombre.");
            return;
        }

        // Realiza la modificación con el nuevo nombre
        modificar(txtId, txtNuevoTU);

        // Cargar datos de la tabla y limpiar campos después de la actualización
        CargarDatosTabla("");
        Limpiar();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void tbTipoUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbTipoUsuarioMouseClicked
        // Llamar a SeleccionarTipoDeUsuario para llenar los JTextFields con los valores seleccionados
        SeleccionarTipoDeUsuario(tbTipoUsuario, txtId, txtNuevoTU);
    }//GEN-LAST:event_tbTipoUsuarioMouseClicked

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        Limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        int fila = tbTipoUsuario.getSelectedRow();
        int id = Integer.parseInt(txtId.getText());
        if (accion("Activo", id)) {
            JOptionPane.showMessageDialog(null, "Activado");
            CargarDatosTabla("");
        } else {
            JOptionPane.showMessageDialog(null, "Error al Activar");
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void btnInactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivarActionPerformed
        int fila = tbTipoUsuario.getSelectedRow();
        int id = Integer.parseInt(txtId.getText());
        if (accion("Inactivo", id)) {
            JOptionPane.showMessageDialog(null, "Inactivado");
            CargarDatosTabla("");
        } else {
            JOptionPane.showMessageDialog(null, "Error al Inactivar");
        }
    }//GEN-LAST:event_btnInactivarActionPerformed

    private void btnGuiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiaActionPerformed
        JOptionPane.showMessageDialog(null, "Esta Interfaz es para Crear NUEVOS TIPOS DE USUARIOS o ROLES\n"
                + "USUARIO SOLO VER: Podra solo ver los datos de la Tablas\n"
                + "Botón GRABAR: debe ingresar el nuevo tipo de usuario y presionar botón Grabar\n"
                + "Botón CANCELAR solo borra el dato ingresado en las casilla , Descripción\n"
                + "Botón ELIMINAR para eliminar un Rol o Tipo de Usuario debe seleccionar en la tabla \n"
                + "aparecera el dato en el recuadro presionar el botón Eliminar y se eliminara el dato designado\n"
                + "Botón MODIFICAR seleccionar el Rol en la Tabla hacer la modificacion que desee y presionar Modificar\n"
                + "Botón ACTIVAR se activaran los Roles Inactivados\n"
                + "Botón INACTIVAR se desactivaran los Roles que no deban ingresar al sistema\n"
                + "Esta Plataforma es para determinar los Roles de quien puede INGRESAR A TU SISTEMA");
    }//GEN-LAST:event_btnGuiaActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {
        SeleccionarTipoDeUsuario(tbTipoUsuario, txtNuevoTU, txtId);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnActivar;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGrabarTU;
    public javax.swing.JButton btnGuia;
    public javax.swing.JButton btnInactivar;
    public javax.swing.JButton btnLimpiar;
    public javax.swing.JButton btnModificar;
    public javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable tbTipoUsuario;
    private javax.swing.JTextField txtId;
    public javax.swing.JTextField txtNuevoTU;
    // End of variables declaration//GEN-END:variables
}
