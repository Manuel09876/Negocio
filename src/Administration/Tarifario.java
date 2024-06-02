package Administration;

import java.sql.CallableStatement;
import conectar.Conectar;
import java.awt.HeadlessException;
import java.sql.Connection;
import javax.swing.table.DefaultTableModel;
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
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Tarifario extends javax.swing.JInternalFrame {

    DefaultTableModel model;

    int id;
    int id_empresa;
    String service;
    double precio;
    String estado;

    public Tarifario(int id, int id_empresa, String service, double precio, String estado) {
        this.id = id;
        this.id_empresa = id_empresa;
        this.service = service;
        this.precio = precio;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Tarifario() {
        initComponents();

        txtId.setEnabled(false);
        txtIdEmpresa.setEditable(false);
        txtServicio.requestFocus();
        MostrarServicios("");
        AutoCompleteDecorator.decorate(cbxEmpresas);
        MostrarEmpresa(cbxEmpresas);
    }

    public void Insertar() {
        //Variables
        String service;
        int id_empresa;
        double precio;
        String estado;

        //Obtenemos la Información de la Caja de Texto
        service = txtServicio.getText();
        id_empresa = Integer.parseInt(txtIdEmpresa.getText());
        precio = Double.parseDouble(txtPrecio.getText());

        //Consulta sql para insertar los datos (nombres como en la base de datos)
        String sql = "INSERT INTO services (id_empresa, servicio, precio)VALUES(?,?,?)";

        //Para almacenar los datos empleo un try cash
        try {
            //prepara la coneccion para enviar al sql (Evita ataques al sql)
            PreparedStatement pst = connect.prepareStatement(sql);

            pst.setInt(1, id_empresa);
            pst.setString(2, service);
            pst.setDouble(3, precio);

            //Declara otra variable para validar los registros
            int n = pst.executeUpdate();

            //si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardo exitosamente");

                txtServicio.setText("");
                txtPrecio.setText("");
            }
            MostrarServicios("");

        } catch (SQLException e) {
            Logger.getLogger(Tarifario.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "El registro NO se guardo exitosamente, Error " + e.toString());
        }

    }

    public void Seleccionar(JTable TablaServicios, JTextField Id, JComboBox Empresa, JTextField Servicio, JTextField precio) {

        try {
            int fila = TablaServicios.getSelectedRow();

            if (fila >= 0) {
                Id.setText(TablaServicios.getValueAt(fila, 0).toString());
                Empresa.setSelectedItem(TablaServicios.getValueAt(fila, 1).toString());
                Servicio.setText(TablaServicios.getValueAt(fila, 2).toString());
                precio.setText(TablaServicios.getValueAt(fila, 3).toString());

            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error en la selección, error " + e.toString());
        }
    }

    public void Modificar(JTextField Id, JTextField Servicio, JTextField precio) {

        setId(Integer.parseInt(Id.getText()));

        setService(Servicio.getText());
        setPrecio(Double.parseDouble(precio.getText()));

        String consulta = "UPDATE services SET servicio=?, precio=? WHERE id=?";

        try {
            CallableStatement cs = objconexion.getConexion().prepareCall(consulta);

            cs.setString(1, getService());
            cs.setDouble(2, getPrecio());
            cs.setInt(3, getId());      //No olvidar el Id
            cs.execute();

            JOptionPane.showMessageDialog(null, "Modificación exitosa");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al modificar, error: " + e.toString());
        }

    }

    public void Eliminar(JTextField codigo) {

        setId(Integer.parseInt(codigo.getText()));

        String consulta = "DELETE FROM services WHERE id=?";

        try {

            CallableStatement cs = objconexion.getConexion().prepareCall(consulta);
            cs.setInt(1, getId());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Se Elimino");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());
        }

    }

    public void MostrarServicios(String buscar) {

        try {

            String[] tituloTabla = {"id", "Empresa", "Servicio", "Precio", "Estado"};
            String[] RegistroBD = new String[5];

            model = new DefaultTableModel(null, tituloTabla);

            String sql = "SELECT services.id, bussiness.nameBusiness as Empresa, services.servicio, services.precio, services.estado "
                    + "FROM services "
                    + "INNER JOIN bussiness "
                    + "WHERE services.id_empresa=bussiness.idBusiness";

            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                RegistroBD[0] = rs.getString("id");
                RegistroBD[1] = rs.getString("Empresa");
                RegistroBD[2] = rs.getString("servicio");
                RegistroBD[3] = rs.getString("precio");
                RegistroBD[4] = rs.getString("estado");

                model.addRow(RegistroBD);
            }

            tbServicios.setModel(model);
            tbServicios.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbServicios.getColumnModel().getColumn(1).setPreferredWidth(150);
            tbServicios.getColumnModel().getColumn(2).setPreferredWidth(200);
            tbServicios.getColumnModel().getColumn(3).setPreferredWidth(150);

        } catch (SQLException e) {
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtServicio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        btnImprimir = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cbxEmpresas = new javax.swing.JComboBox<>();
        txtIdEmpresa = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbServicios = new javax.swing.JTable();

        setBackground(new java.awt.Color(0, 0, 153));
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Tarifario");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Nombre del Servicio");

        jLabel3.setText("Precio");

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf.png"))); // NOI18N
        btnImprimir.setText("Imprimir");

        jLabel5.setText("Id");

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        jLabel6.setText("Empresas");

        cbxEmpresas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxEmpresasItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtServicio)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbxEmpresas, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnImprimir)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar))
                .addGap(27, 27, 27))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImprimir)
                    .addComponent(jLabel3)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(txtIdEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cbxEmpresas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 567, 160));

        jPanel2.setBackground(new java.awt.Color(0, 51, 153));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(btnModificar)
                .addGap(43, 43, 43)
                .addComponent(btnCancelar)
                .addGap(53, 53, 53)
                .addComponent(btnSalir)
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnModificar)
                    .addComponent(btnSalir)
                    .addComponent(btnCancelar))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 570, -1));

        jLabel4.setText("Taifario");

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addGap(78, 78, 78)
                .addComponent(btnBuscar)
                .addContainerGap(300, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(btnBuscar))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, -1, -1));

        tbServicios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbServicios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbServiciosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbServicios);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 570, 220));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Insertar();
        MostrarServicios("");
        txtId.setText("");
        txtServicio.setText("");
        txtPrecio.setText("");
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        Modificar(txtId, txtServicio, txtPrecio);
        MostrarServicios("");
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        txtId.setText("");
        txtServicio.setText("");
        txtPrecio.setText("");
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        Seleccionar(tbServicios, txtId, cbxEmpresas, txtServicio, txtPrecio);
        Eliminar(txtId);
        MostrarServicios("");
        txtId.setText("");
        txtServicio.setText("");
        txtPrecio.setText("");

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tbServiciosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbServiciosMouseClicked
        Seleccionar(tbServicios, txtId, cbxEmpresas, txtServicio, txtPrecio);
    }//GEN-LAST:event_tbServiciosMouseClicked

    private void cbxEmpresasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxEmpresasItemStateChanged
        MostrarCodigoEmpresa(cbxEmpresas, txtIdEmpresa);
    }//GEN-LAST:event_cbxEmpresasItemStateChanged

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String ID = txtIdEmpresa.getText();
        String ID_buscar = "";
        if (!(ID.equals(""))) {
            ID_buscar = "WHERE services.id_empresa= '" + ID + "'";
        }
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            String[] tituloTabla = {"id", "Empresa", "Servicio", "Precio", "Estado"};
            String[] RegistroBD = new String[5];
            model = new DefaultTableModel(null, tituloTabla); //Le pasamos los titulos a la tabla
            tbServicios.setModel(model);
            String sql = "SELECT services.id, bussiness.nameBusiness AS Empresa, services.servicio, services.precio, services.estado FROM services INNER JOIN bussiness ON services.id_empresa=bussiness.idBusiness " + ID_buscar;
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[0] = rs.getString("id");
                RegistroBD[1] = rs.getString("Empresa");
                RegistroBD[2] = rs.getString("Servicio");
                RegistroBD[3] = rs.getString("Precio");
                RegistroBD[4] = rs.getString("Estado");
                model.addRow(RegistroBD);
            }
            tbServicios.setModel(model);
            tbServicios.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbServicios.getColumnModel().getColumn(1).setPreferredWidth(150);
            tbServicios.getColumnModel().getColumn(2).setPreferredWidth(200);
            tbServicios.getColumnModel().getColumn(3).setPreferredWidth(150);
        } catch (SQLException e) {
            System.out.println("Error: " + e.toString());
        }

    }//GEN-LAST:event_btnBuscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxEmpresas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbServicios;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdEmpresa;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtServicio;
    // End of variables declaration//GEN-END:variables

    Conectar objconexion = new Conectar();
    Connection connect = objconexion.getConexion();

    public void MostrarEmpresa(JComboBox cbxEmpresa) {

        String sql = "";
        sql = "select * from bussiness";
        Statement st;

        try {

            st = objconexion.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxEmpresa.removeAllItems();

            while (rs.next()) {

                cbxEmpresa.addItem(rs.getString("nameBusiness"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar en Combo " + e.toString());
        }
    }

    public void MostrarCodigoEmpresa(JComboBox cbxEmpresa, JTextField idBusiness) {

        String consuta = "select bussiness.idBusiness from bussiness where bussiness.nameBusiness=?";

        try {
            CallableStatement cs = objconexion.getConexion().prepareCall(consuta);
            cs.setString(1, cbxEmpresa.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                idBusiness.setText(rs.getString("idBusiness"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }
}
