package Reports;

import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Sueldos extends javax.swing.JInternalFrame {

    private Conectar conexion = new Conectar();
    private Connection conect = conexion.getConexion();

    public Sueldos() {
        initComponents();
        AutoCompleteDecorator.decorate(cbxTrabajador);
        MostrarTrabajador(cbxTrabajador);
    }

    public Sueldos(Connection connection) {
        this.conexion = new Conectar();
        this.conect = conexion.getConexion();
    }

    public void mostrarSueldos(int trabajadorId, java.util.Date fechaInicio, java.util.Date fechaFin) {
    DefaultTableModel modelo = new DefaultTableModel();
    
    // Definir las columnas de la tabla
    modelo.addColumn("ID Trabajador");
    modelo.addColumn("Nombre");
    modelo.addColumn("Fecha");
    modelo.addColumn("Horas Trabajadas");
    modelo.addColumn("Horas Extras");
    modelo.addColumn("Sueldo Bruto");
    modelo.addColumn("Sueldo Neto");
    
    TableSueldos.setModel(modelo);

    // SQL para calcular y obtener los datos de sueldos
    String sql = "SELECT t.idWorker, t.nombre, ht.fecha, ht.horas, ht.horas_extras, "
               + "(ht.horas * p.pagoPorHora + ht.horas_extras * p.pagoPorHora * 1.5) AS sueldo_bruto, "
               + "(ht.horas * p.pagoPorHora + ht.horas_extras * p.pagoPorHora * 1.5) - "
               + "(ht.horas * p.pagoPorHora + ht.horas_extras * p.pagoPorHora * 1.5) * 0.3 AS sueldo_neto " // Supongamos que el 30% son deducciones
               + "FROM horas_trabajadas ht "
               + "JOIN worker t ON ht.trabajador_id = t.idWorker "
               + "JOIN puestodetrabajo p ON t.idWorker = p.idTrabajador "
               + "WHERE ht.trabajador_id = ? AND ht.fecha BETWEEN ? AND ? "
               + "ORDER BY ht.fecha ASC";

    try (PreparedStatement pst = conect.prepareStatement(sql)) {
        pst.setInt(1, trabajadorId);
        pst.setDate(2, new java.sql.Date(fechaInicio.getTime()));
        pst.setDate(3, new java.sql.Date(fechaFin.getTime()));

        ResultSet rs = pst.executeQuery();
        
        // Llenar la tabla con los datos obtenidos
        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("idWorker"),
                rs.getString("nombre"),
                rs.getDate("fecha"),
                rs.getDouble("horas"),
                rs.getDouble("horas_extras"),
                rs.getDouble("sueldo_bruto"),
                rs.getDouble("sueldo_neto")
            });
            System.out.println("Datos añadidos a la tabla: " + rs.getString("nombre"));
        }
        
        TableSueldos.setModel(modelo);  // Asegurarse de que el modelo se asigna a la tabla

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al mostrar sueldos: " + e.getMessage());
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        TableSueldos = new javax.swing.JTable();
        btnHistorial = new javax.swing.JButton();
        txtIdSueldos = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbxTrabajador = new javax.swing.JComboBox<>();
        txtIdTrabajador = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Sueldos");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setBackground(new java.awt.Color(153, 153, 255));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableSueldos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TableSueldos.setRowHeight(23);
        jScrollPane14.setViewportView(TableSueldos);

        jPanel12.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1160, 420));

        btnHistorial.setText("Reportes");
        btnHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialActionPerformed(evt);
            }
        });
        jPanel12.add(btnHistorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 120, 50));
        jPanel12.add(txtIdSueldos, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 30, 70, 40));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, -1, -1));
        jPanel12.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 30, 130, -1));
        jPanel12.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 30, 120, -1));

        jLabel1.setText("Inicio");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 10, -1, -1));

        jLabel2.setText("Final");
        jPanel12.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 10, -1, -1));

        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });
        jPanel12.add(cbxTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 180, -1));
        jPanel12.add(txtIdTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, -1, -1));

        jLabel3.setText("Trabajador");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        getContentPane().add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1200, 589));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        if (txtIdTrabajador.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor, seleccione un trabajador válido.");
        return;
    }

    try {
        int trabajadorId = Integer.parseInt(txtIdTrabajador.getText().trim());
        java.util.Date fechaInicio = jDateChooser1.getDate();
        java.util.Date fechaFin = jDateChooser2.getDate();
        
        if (fechaInicio == null || fechaFin == null) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione las fechas de inicio y fin.");
            return;
        }

        mostrarSueldos(trabajadorId, fechaInicio, fechaFin);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "El ID de trabajador debe ser un número válido.");
    }
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable TableSueldos;
    public javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane14;
    public javax.swing.JTextField txtIdSueldos;
    private javax.swing.JTextField txtIdTrabajador;
    // End of variables declaration//GEN-END:variables
public void MostrarTrabajador(JComboBox comboTrabajador) {
        String sql = "SELECT * FROM worker";
        try (Statement st = conect.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            comboTrabajador.removeAllItems();
            while (rs.next()) {
                comboTrabajador.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Trabajadores: " + e.toString());
        }
    }

    public void MostrarCodigoTrabajador(JComboBox trabajador, JTextField IdTrabajador) {

        String consuta = "select worker.idWorker from worker where worker.nombre=?";

        try {
            CallableStatement cs = conect.prepareCall(consuta);
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
