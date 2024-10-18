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

    
    PreparedStatement ps;
    ResultSet rs;

    public Unidades() {
        initComponents();
        
        CargarDatosTabla("");
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

            String ConsultaSQL = "SELECT * FROM unidades";

            Statement st = connection.createStatement();
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
            txtUnidades.setText("");
            txtUnidades.requestFocus();

        } catch (SQLException ex) {

        }finally{
         Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void Eliminar(JTextField id) {
Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        setId_unidades(Integer.parseInt(id.getText()));

        String consulta = "DELETE from unidades where id_unidades=?";

        try {
            CallableStatement cs = connection.prepareCall(consulta);
            cs.setInt(1, getId_unidades());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");
            CargarDatosTabla("");
            txtUnidades.setText("");
            txtIdUnidades.setText("");
            txtUnidades.requestFocus();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());

        }finally{
            Conectar.getInstancia().devolverConexion(connection);
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
Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        //Obtengo el valor en Cadena(String) de las cajas de Texto
        setId_unidades(Integer.parseInt(paraId.getText()));
        setNombre(paraNombre.getText());

        String sql = "UPDATE unidades SET nombre = ?  WHERE id_unidades = ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, getNombre());
            ps.setInt(2, getId_unidades());
            ps.execute();

            JOptionPane.showMessageDialog(null, "Modificacion Exitosa");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se Modifico, error " + e.toString());

        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public boolean accion(String estado, int IdUnidades) {
Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
    String sql = "UPDATE unidades SET estado = ? WHERE id_unidades = ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, IdUnidades);
            ps.execute();
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
        btnSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtUnidades = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtIdUnidades = new javax.swing.JTextField();
        btnInactivar = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuia = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUnidades = new javax.swing.JTable();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Unidades");

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
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
                        .addComponent(btnModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar)
                        .addGap(18, 18, 18)
                        .addComponent(btnInactivar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnGuia)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnActivar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSalir)
                    .addComponent(btnGuia))
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
                    .addComponent(btnModificar))
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

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

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
        // Verifica si el campo de texto txtIdUnidades no está vacío
        String idText = txtIdUnidades.getText().trim(); // Elimina espacios en blanco alrededor del texto

        if (idText.isEmpty()) {
            // Muestra un mensaje de error si el campo está vacío
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una unidad para inactivar.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                int id = Integer.parseInt(idText); // Intenta convertir el texto a un entero
                if (accion("Inactivo", id)) {
                    CargarDatosTabla(""); // Actualiza la tabla con los datos más recientes
                    JOptionPane.showMessageDialog(null, "Inactivado");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al Inactivar");
                }
            } catch (NumberFormatException e) {
                // Muestra un mensaje de error si el texto no es un número válido
                JOptionPane.showMessageDialog(null, "El ID ingresado no es un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnInactivarActionPerformed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        // Verifica si el campo de texto txtIdUnidades no está vacío
        String idText = txtIdUnidades.getText().trim(); // Elimina espacios en blanco alrededor del texto

        if (idText.isEmpty()) {
            // Muestra un mensaje de error si el campo está vacío
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una unidad para activar.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                int id = Integer.parseInt(idText); // Intenta convertir el texto a un entero
                if (accion("Activo", id)) {
                    CargarDatosTabla(""); // Actualiza la tabla con los datos más recientes
                    JOptionPane.showMessageDialog(null, "Activado");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al Activar");
                }
            } catch (NumberFormatException e) {
                // Muestra un mensaje de error si el texto no es un número válido
                JOptionPane.showMessageDialog(null, "El ID ingresado no es un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void tbUnidadesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUnidadesMouseClicked
        SeleccionarUnidades(tbUnidades, txtIdUnidades, txtUnidades);
    }//GEN-LAST:event_tbUnidadesMouseClicked

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        Modificar(txtIdUnidades, txtUnidades);
        CargarDatosTabla("");
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnGuiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiaActionPerformed
        JOptionPane.showMessageDialog(null, "UNIDADES\n"
                + "Llenar todas la casilla para el ingreso de UNIDADES de medida\n"
                + "Botón GUARDAR para guardar los datos que se mostraran en una Tabla\n"
                + "Botón CANCELAR para limpiar las casillas\n"
                + "Botón MODIFICAR  seleccionamos una fila de la Tabla para modificar los datos y presionamos Modificar\n"
                + "Botón ELIMINAR seleccionamos la fila de la Tabla que queremos eliminar y click en Eliminar\n"
                + "Botón ACTIVAR para activar una Unidad que habia sido desactivado\n"
                + "Botón DESACTIVAR´para desactivar una Unidad\n"
                + "Botón NUEVO limpiara las casilla y se ubicará el puntero en Unidades\n"
                + "Esta Interfaz es para el Ingreso de Unidades");
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
    private javax.swing.JTable tbUnidades;
    private javax.swing.JTextField txtIdUnidades;
    private javax.swing.JTextField txtUnidades;
    // End of variables declaration//GEN-END:variables
}
