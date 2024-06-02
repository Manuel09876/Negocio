package Administration;

import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class PuestoDeTrabajo extends javax.swing.JInternalFrame {

    //Variables
    int idPDT;
    int idTrabajador;
    int idTDT;
    Double precioPorHora;
    String overtime;
    String estado;
    String Trabajador;
    String tipoDeTrabajo;

    public PuestoDeTrabajo(int idPDT, int idTrabajador, int idTDT, Double precioPorHora, String overtime, String estado, String Trabajador, String tipoDeTrabajo) {
        this.idPDT = idPDT;
        this.idTrabajador = idTrabajador;
        this.idTDT = idTDT;
        this.precioPorHora = precioPorHora;
        this.overtime = overtime;
        this.estado = estado;
        this.Trabajador = Trabajador;
        this.tipoDeTrabajo = tipoDeTrabajo;
    }

    public int getIdPDT() {
        return idPDT;
    }

    public void setIdPDT(int idPDT) {
        this.idPDT = idPDT;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public int getIdTDT() {
        return idTDT;
    }

    public void setIdTDT(int idTDT) {
        this.idTDT = idTDT;
    }

    public Double getPrecioPorHora() {
        return precioPorHora;
    }

    public void setPrecioPorHora(Double precioPorHora) {
        this.precioPorHora = precioPorHora;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTrabajador() {
        return Trabajador;
    }

    public void setTrabajador(String Trabajador) {
        this.Trabajador = Trabajador;
    }

    public String getTipoDeTrabajo() {
        return tipoDeTrabajo;
    }

    public void setTipoDeTrabajo(String tipoDeTrabajo) {
        this.tipoDeTrabajo = tipoDeTrabajo;
    }

    Conectar objConect = new Conectar();

    public void Mostrar(JTable Tabla) {

        DefaultTableModel modelo = new DefaultTableModel();

        //Para ordenar la Tabla 
        String sql = "";

        //Darle titulos a la Tabla
        modelo.addColumn("idPDT");
        modelo.addColumn("Trabajador");
        modelo.addColumn("Tipo De Trabajo");
        modelo.addColumn("Precio por Hora");
        modelo.addColumn("OverTime");

        Tabla.setModel(modelo);

        //Consulto la BD
        sql = "select idPDT, w.nombre as trabajador, tdt.nombre as TipoTrabajo, precioPorHora, o.descripcion as OverTime\n"
                + "from puestodetrabajo pdt\n"
                + "inner join worker w on pdt.idTrabajador=w.idWorker\n"
                + "inner join tiposdetrabajos tdt on pdt.idTDT=tdt.id\n"
                + "inner join overtime o on o.id=pdt.idOverTime";

        /*select usuarios.id, usuarios.nombres, usuarios.apellidos, sexo.sexoDescripcion as sexo\n"
                + "from usuarios\n"
                + "inner join sexo on usuarios.fksexo=sexo.id*/
        String[] datos = new String[5];
        Statement st;

        try {

            st = objConect.getConexion().createStatement();

            ResultSet rs = st.executeQuery(sql);

            //Recorre la Tabla con un while
            while (rs.next()) {
                //LLenamos la Tabla
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);

                //Lo agregue a las filas 
                modelo.addRow(datos);

                //la agregue a la Tabla
                Tabla.setModel(modelo);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "No se pudo mostrar los registros, error: " + e.toString());
        }

    }

    public void Insertar(JTextField IdTrabajador, JTextField IdTipoDeTrabajo, JTextField pagoPorHora, JTextField IdOverTime) {

        Conectar con = new Conectar();
        Connection connect = con.getConexion();

        //Hago la Consulta
        String consulta = "Insert into puestodetrabajo (idTrabajador, idTDT, precioPorHora, idOverTime)values (?, ?, ?, ?);";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);

            //Los datos a enviar a la BD
            cs.setString(1, IdTrabajador.getText());
            cs.setString(2, IdTipoDeTrabajo.getText());
            cs.setString(3, pagoPorHora.getText());
            cs.setString(4, IdOverTime.getText());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Se Inserto");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "No se Inserto. Error: " + e.toString());
        }
    }

    //Metodo para seleccionar y que se muestre
    public void Seleccionar(JTable Tabla, JTextField Id, JComboBox Trabajador, JComboBox TDT, JTextField PPH, JComboBox OverTime) {

        try {
            //Creamos un contador de filas que va a recorer la fila
            int fila = Tabla.getSelectedRow();
            //Comparo la fila con la posición que tiene un valor, me sercioro que se ha seleccionado la fila
            if (fila >= 0) {
                //
                Id.setText(Tabla.getValueAt(fila, 0).toString());
                Trabajador.setSelectedItem(Tabla.getValueAt(fila, 1).toString());
                TDT.setSelectedItem(Tabla.getValueAt(fila, 2).toString());
                PPH.setText(Tabla.getValueAt(fila, 3).toString());
                OverTime.setSelectedItem(Tabla.getValueAt(fila, 4).toString());

            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error de seleccion, error: " + e.toString());
        }

    }

    public void Modificar(JTextField idTrabajador, JTextField TipoDeTrabajo, JTextField pagoPorHora, JComboBox OverTime) {

        //Obtengo el valor en Cadena(String) de las cajas de Texto
        setIdTrabajador(Integer.parseInt(idTrabajador.getText()));
        setIdTDT(Integer.parseInt(TipoDeTrabajo.getText()));
        setPrecioPorHora(Double.valueOf(pagoPorHora.getText()));
        setOvertime(OverTime.getSelectedItem().toString());

        Conectar con = new Conectar();

        String consulta = "UPDATE puestodetrabajo SET idTrabajador = ?, idTDT = ?, precioPorHora = ?, overTime =? WHERE idPDT=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);

            //
            cs.setInt(1, getIdTrabajador());
            cs.setInt(2, getIdTDT());
            cs.setDouble(3, getPrecioPorHora());
            cs.setString(4, getOvertime());

            cs.execute();

            JOptionPane.showMessageDialog(null, "Modificacion Exitosa");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se Modifico, error " + e.toString());
        }

    }

    public void Eliminar(JTextField IdPDT) {

        setIdPDT(Integer.parseInt(IdPDT.getText()));

        Conectar con = new Conectar();

        String consulta = "DELETE FROM puestodetrabajo WHERE idPDT=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);

            cs.setInt(1, getIdPDT());

            cs.execute();

            JOptionPane.showMessageDialog(null, "Se Elimino correctamente ");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "No se pudo eliminar, error " + e.toString());
        }
    }

    public PuestoDeTrabajo() {
        initComponents();
        AutoCompleteDecorator.decorate(cbxTDT);
        AutoCompleteDecorator.decorate(cbxTrabajador);
        MostrarTrabajador(cbxTrabajador);
        MostrarTipoDeTrabajo(cbxTDT);
        MostrarOverTime(cbxOverTime);
        Mostrar(tbPuestosDeTrabajo);
        txtIdPDT.setDragEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPPH = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtIdPDT = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        cbxTDT = new javax.swing.JComboBox<>();
        cbxTrabajador = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        btnNuevoTipoDeTrabajo = new javax.swing.JButton();
        txtIdtrabajador = new javax.swing.JTextField();
        txtIdTipoDeTrabajo = new javax.swing.JTextField();
        cbxOverTime = new javax.swing.JComboBox<>();
        txtIOverTime = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPuestosDeTrabajo = new javax.swing.JTable();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Puestos de Trabajo");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Tipo de Trabajo");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));
        jPanel1.add(txtPPH, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 100, -1));

        jLabel2.setText("Pago por hora");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jLabel3.setText("Overtime");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, -1, -1));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel1.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, -1, -1));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 290, -1, -1));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 250, -1, -1));
        jPanel1.add(txtIdPDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 80, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        cbxTDT.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTDTItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxTDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, 210, -1));

        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, 210, -1));

        jLabel4.setText("Trabajador");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        btnNuevoTipoDeTrabajo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-producto.png"))); // NOI18N
        btnNuevoTipoDeTrabajo.setText("Nuevo Tipo de Trabajo");
        btnNuevoTipoDeTrabajo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoTipoDeTrabajoActionPerformed(evt);
            }
        });
        jPanel1.add(btnNuevoTipoDeTrabajo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, -1, -1));
        jPanel1.add(txtIdtrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 80, -1));
        jPanel1.add(txtIdTipoDeTrabajo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 80, -1));

        cbxOverTime.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxOverTimeItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxOverTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, 100, -1));

        txtIOverTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIOverTimeActionPerformed(evt);
            }
        });
        jPanel1.add(txtIOverTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 200, 90, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 450, 340));

        tbPuestosDeTrabajo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbPuestosDeTrabajo);

        jPanel2.add(jScrollPane1);

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, 570, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnNuevoTipoDeTrabajoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoTipoDeTrabajoActionPerformed
        Bases.TipoDeTrabajo objTDT = new Bases.TipoDeTrabajo();
        objTDT.setVisible(true);
    }//GEN-LAST:event_btnNuevoTipoDeTrabajoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Insertar(txtIdtrabajador, txtIdTipoDeTrabajo, txtPPH, txtIOverTime);
        Mostrar(tbPuestosDeTrabajo);
        txtIdPDT.setText("");
        txtIdtrabajador.setText("");
        txtIdTipoDeTrabajo.setText("");
        txtPPH.setText("");
        cbxTrabajador.setSelectedItem("");
        cbxTDT.setSelectedItem("");
        cbxOverTime.setSelectedItem("");

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        Seleccionar(tbPuestosDeTrabajo, txtIdPDT, cbxTrabajador, cbxTDT, txtPPH, cbxOverTime);
        Modificar(txtIdtrabajador, txtIdTipoDeTrabajo, txtPPH, cbxOverTime);
        Mostrar(tbPuestosDeTrabajo);
        txtIdPDT.setText("");
        txtIdtrabajador.setText("");
        txtIdTipoDeTrabajo.setText("");
        txtPPH.setText("");
        cbxTrabajador.setSelectedItem("");
        cbxTDT.setSelectedItem("");
        cbxOverTime.setSelectedItem("");
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        txtIdPDT.setText("");
        txtIdtrabajador.setText("");
        txtIdTipoDeTrabajo.setText("");
        txtPPH.setText("");
        cbxTrabajador.setSelectedItem("");
        cbxTDT.setSelectedItem("");
        cbxOverTime.setSelectedItem("");
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        Seleccionar(tbPuestosDeTrabajo, txtIdPDT, cbxTrabajador, cbxTDT, txtPPH, cbxOverTime);
        Eliminar(txtIdPDT);
        Mostrar(tbPuestosDeTrabajo);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        MostrarCodigoTrabajador(cbxTrabajador, txtIdtrabajador);
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged

    private void cbxTDTItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTDTItemStateChanged
        MostrarCodigoTipodeTrabajo(cbxTDT, txtIdTipoDeTrabajo);
    }//GEN-LAST:event_cbxTDTItemStateChanged

    private void txtIOverTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIOverTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIOverTimeActionPerformed

    private void cbxOverTimeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxOverTimeItemStateChanged
        MostrarCodigoPorOverTime(cbxOverTime, txtIOverTime);
    }//GEN-LAST:event_cbxOverTimeItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevoTipoDeTrabajo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxOverTime;
    private javax.swing.JComboBox<String> cbxTDT;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbPuestosDeTrabajo;
    private javax.swing.JTextField txtIOverTime;
    private javax.swing.JTextField txtIdPDT;
    private javax.swing.JTextField txtIdTipoDeTrabajo;
    private javax.swing.JTextField txtIdtrabajador;
    private javax.swing.JTextField txtPPH;
    // End of variables declaration//GEN-END:variables

    public void MostrarTrabajador(JComboBox comboTrabajador) {

        String sql = "";
        sql = "select * from worker";
        Statement st;

        try {

            st = objConect.getConexion().createStatement();
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
            CallableStatement cs = objConect.getConexion().prepareCall(consuta);
            cs.setString(1, cbxTrabajador.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                IdTrabajador.setText(rs.getString("idWorker"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    public void MostrarTipoDeTrabajo(JComboBox comboTipoDeTrabajo) {

        String sql = "";
        sql = "select * from tiposdetrabajos";
        Statement st;

        try {

            st = objConect.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            comboTipoDeTrabajo.removeAllItems();

            while (rs.next()) {

                comboTipoDeTrabajo.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoTipodeTrabajo(JComboBox comboTipoDeTrabajo, JTextField idTipoDeTrabajo) {

        String consuta = "select tiposdetrabajos.id from tiposdetrabajos where tiposdetrabajos.nombre=?";

        try {
            CallableStatement cs = objConect.getConexion().prepareCall(consuta);
            cs.setString(1, comboTipoDeTrabajo.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                idTipoDeTrabajo.setText(rs.getString("id"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    public void MostrarOverTime(JComboBox comboOverTime) {

        String sql = "";
        sql = "select * from overtime";
        Statement st;

        try {

            st = objConect.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            comboOverTime.removeAllItems();

            while (rs.next()) {

                comboOverTime.addItem(rs.getString("descripcion"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoPorOverTime(JComboBox overtimeCombo, JTextField idOvertime) {

        String consuta = "select overtime.id from overtime where overtime.descripcion=?";

        try {
            CallableStatement cs = objConect.getConexion().prepareCall(consuta);
            cs.setString(1, overtimeCombo.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                idOvertime.setText(rs.getString("id"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }
}
