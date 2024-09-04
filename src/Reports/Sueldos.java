package Reports;

import Bases.PeriodoPago;
import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
        AutoCompleteDecorator.decorate(cbxPeriodoPago);
        MostrarTrabajador(cbxTrabajador);
        llenarComboPeriodoPago(cbxPeriodoPago); // Llenar el JComboBox de períodos de pago
    }

    // Método para llenar el JComboBox de períodos de pago
    private void llenarComboPeriodoPago(JComboBox<String> comboPeriodoPago) {
        comboPeriodoPago.removeAllItems(); // Limpiar cualquier item existente
        comboPeriodoPago.addItem("Semanal");
        comboPeriodoPago.addItem("Quincenal");
        comboPeriodoPago.addItem("Mensual");
    }

//    public Sueldos(Connection connection) {
//        initComponents(); // Asegúrate de inicializar los componentes en este constructor también
//        this.conect = connection; // Asignar la conexión a la variable adecuada
//        AutoCompleteDecorator.decorate(cbxTrabajador);
//        MostrarTrabajador(cbxTrabajador);
//    }
    // Método para mostrar sueldos basado en el rango de fechas y tipo de período
    public void mostrarSueldos(int trabajadorId, java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        // Definir el modelo de tabla
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Trabajador");
        modelo.addColumn("Nombre");
        modelo.addColumn("Fecha");
        modelo.addColumn("Horas Trabajadas");
        modelo.addColumn("Horas Extras");
        modelo.addColumn("Sueldo Bruto");
        modelo.addColumn("Sueldo Neto");
        TableSueldos.setModel(modelo);

        // SQL para obtener los datos de sueldos
        String sql = "SELECT t.idWorker, t.nombre, ht.fecha, ht.horas, COALESCE(ht.horas_extras, 0) AS horas_extras, "
                + "(ht.horas * p.pagoPorHora + COALESCE(ht.horas_extras, 0) * p.pagoPorHora * 1.5) AS sueldo_bruto, "
                + "(ht.horas * p.pagoPorHora + COALESCE(ht.horas_extras, 0) * p.pagoPorHora * 1.5) * 0.7 AS sueldo_neto "
                + "FROM horas_trabajadas ht "
                + "JOIN worker t ON ht.trabajador_id = t.idWorker "
                + "JOIN puestodetrabajo p ON t.idWorker = p.idTrabajador "
                + "WHERE ht.trabajador_id = ? AND ht.fecha BETWEEN ? AND ? "
                + "ORDER BY ht.fecha ASC";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);  // Parámetro 1
            pst.setDate(2, fechaInicio);  // Parámetro 2
            pst.setDate(3, fechaFin);  // Parámetro 3

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
            }

            TableSueldos.setModel(modelo);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al mostrar sueldos: " + e.getMessage());
        }
    }

    private java.util.Date[] obtenerFechasPeriodoPago(int trabajadorId, String periodoPago) {
        java.util.Date[] periodo = new java.util.Date[2];
        LocalDate fechaInicioActividades = obtenerFechaInicioActividades(trabajadorId); // Método para obtener la fecha de inicio de actividades
        LocalDate fechaInicio = null;
        LocalDate fechaFin = null;

        switch (periodoPago) {
            case "Semanal":
                fechaInicio = fechaInicioActividades.with(DayOfWeek.MONDAY); // Inicia el lunes
                fechaFin = fechaInicio.plusDays(6); // Termina el domingo
                break;
            case "Quincenal":
                fechaInicio = fechaInicioActividades; // Inicia en el primer día del periodo quincenal
                fechaFin = fechaInicio.plusDays(13); // Termina después de 14 días
                break;
            case "Mensual":
                fechaInicio = fechaInicioActividades.withDayOfMonth(1); // Inicia en el primer día del mes
                fechaFin = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth()); // Termina en el último día del mes
                break;
        }

        // Convertir LocalDate a java.util.Date
        periodo[0] = java.sql.Date.valueOf(fechaInicio);
        periodo[1] = java.sql.Date.valueOf(fechaFin);
        return periodo;
    }

    private LocalDate obtenerFechaInicioActividades(int trabajadorId) {
        String sql = "SELECT fechaDePuesto FROM puestodetrabajo WHERE idTrabajador = ? ORDER BY fechaDePuesto ASC LIMIT 1";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getDate("fechaDePuesto").toLocalDate();
            } else {
                throw new SQLException("No se encontró la fecha de inicio de actividades para el trabajador con ID: " + trabajadorId);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener la fecha de inicio de actividades: " + e.getMessage());
            return LocalDate.now();
        }
    }

//    // Método para obtener el período de pago del trabajador
//    public java.util.Date[] obtenerPeriodoPago(int trabajadorId) {
//        java.util.Date[] periodo = new java.util.Date[2];
//
//        String sql = "SELECT p.tipo_periodo, p.fecha_inicio, p.fecha_fin "
//                + "FROM puestodetrabajo pt "
//                + "JOIN periodos_pago p ON pt.id_periodo = p.id "
//                + "WHERE pt.idTrabajador = ?";
//
//        try (PreparedStatement pst = conect.prepareStatement(sql)) {
//            pst.setInt(1, trabajadorId);
//            ResultSet rs = pst.executeQuery();
//
//            if (rs.next()) {
//                periodo[0] = rs.getDate("fecha_inicio");
//                periodo[1] = rs.getDate("fecha_fin");
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al obtener período de pago: " + e.getMessage());
//        }
//
//        return periodo;
//    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        TableSueldos = new javax.swing.JTable();
        btnHistorial = new javax.swing.JButton();
        txtIdSueldos = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        cbxTrabajador = new javax.swing.JComboBox<>();
        txtIdTrabajador = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbxPeriodoPago = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jDateChooserInicio = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateChooserFin = new com.toedter.calendar.JDateChooser();

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
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 20, -1, -1));

        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });
        jPanel12.add(cbxTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 180, -1));
        jPanel12.add(txtIdTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, -1, -1));

        jLabel3.setText("Trabajador");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        jPanel12.add(cbxPeriodoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, 180, -1));

        jLabel1.setText("Priodo");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, -1, -1));
        jPanel12.add(jDateChooserInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 60, 120, -1));

        jLabel2.setText("Inicio");
        jPanel12.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 60, -1, -1));

        jLabel4.setText("Fin");
        jPanel12.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 60, -1, -1));
        jPanel12.add(jDateChooserFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 60, 110, -1));

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
            // Supongamos que obtienes las fechas de inicio y fin de algún componente de la interfaz
            java.sql.Date fechaInicio = new java.sql.Date(jDateChooserInicio.getDate().getTime());
            java.sql.Date fechaFin = new java.sql.Date(jDateChooserFin.getDate().getTime());

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
    private javax.swing.JComboBox<String> cbxPeriodoPago;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private com.toedter.calendar.JDateChooser jDateChooserFin;
    private com.toedter.calendar.JDateChooser jDateChooserInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
        String consulta = "SELECT worker.idWorker FROM worker WHERE worker.nombre=?";

        try (PreparedStatement ps = conect.prepareStatement(consulta)) {
            ps.setString(1, trabajador.getSelectedItem().toString()); // Asegúrate de que estás proporcionando el parámetro
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                IdTrabajador.setText(rs.getString("idWorker"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

}
