package Reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class HorasTrabajadas extends javax.swing.JInternalFrame {

    Conectar conexion = new Conectar();
    Connection conect = conexion.getConexion();

    

    public HorasTrabajadas() {
        initComponents();
        txtIdHoras.setEnabled(false);
        AutoCompleteDecorator.decorate(cbxTrabajador);
        MostrarTrabajador(cbxTrabajador);
    }

     private static final Logger LOGGER = Logger.getLogger(HorasTrabajadas.class.getName());

    
    // Método para registrar el inicio de sesión
     public void registrarInicioSesion(int trabajadorId) {
    String sql = "INSERT INTO horas_trabajadas (trabajador_id, inicio, fecha) VALUES (?, ?, ?)";
    try (PreparedStatement pst = conect.prepareStatement(sql)) {
        pst.setInt(1, trabajadorId);
        pst.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        pst.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
        pst.executeUpdate();
        System.out.println("Inicio de sesión registrado para trabajador ID: " + trabajadorId);
    } catch (SQLException ex) {
        Logger.getLogger(HorasTrabajadas.class.getName()).log(Level.SEVERE, null, ex);
    }
}


    // Método para registrar el fin de sesión y calcular las horas de trabajo
     public void registrarFinSesion(int trabajadorId) {
    System.out.println("Intentando registrar fin de sesión para trabajador ID: " + trabajadorId); // Mensaje de depuración

    String sql = "UPDATE horas_trabajadas SET fin = ?, horas = ?, horas_extras = ? WHERE trabajador_id = ? AND fecha = ?";
    try (PreparedStatement pst = conect.prepareStatement(sql)) {
        LocalDateTime fin = LocalDateTime.now();
        System.out.println("Hora de fin: " + fin); // Mensaje de depuración
        pst.setTimestamp(1, Timestamp.valueOf(fin));

        String sqlSelect = "SELECT inicio FROM horas_trabajadas WHERE trabajador_id = ? AND fecha = ?";
        try (PreparedStatement pstSelect = conect.prepareStatement(sqlSelect)) {
            pstSelect.setInt(1, trabajadorId);
            pstSelect.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ResultSet rs = pstSelect.executeQuery();
            if (rs.next()) {
                LocalDateTime inicio = rs.getTimestamp("inicio").toLocalDateTime();
                System.out.println("Hora de inicio: " + inicio); // Mensaje de depuración

                long minutosTrabajados = ChronoUnit.MINUTES.between(inicio, fin);
                double horasTrabajadas = minutosTrabajados / 60.0 - 0.5; // Restar 30 minutos de descanso
                double horasExtras = Math.max(0, horasTrabajadas - 8); // Suponiendo una jornada laboral de 8 horas

                pst.setDouble(2, horasTrabajadas);
                pst.setDouble(3, horasExtras);
                pst.setInt(4, trabajadorId);
                pst.setDate(5, java.sql.Date.valueOf(java.time.LocalDate.now()));

                int rowsUpdated = pst.executeUpdate();
                System.out.println("Rows updated: " + rowsUpdated); // Mensaje de depuración

                if (rowsUpdated > 0) {
                    System.out.println("Fin de sesión registrado para trabajador ID: " + trabajadorId); // Mensaje de depuración
                } else {
                    System.out.println("No se encontró el registro de inicio de sesión para el trabajador con ID: " + trabajadorId); // Mensaje de depuración
                }
            } else {
                System.out.println("No se encontró el registro de inicio de sesión para el trabajador con ID: " + trabajadorId); // Mensaje de depuración
            }
            rs.close();
        }
    } catch (SQLException ex) {
        Logger.getLogger(HorasTrabajadas.class.getName()).log(Level.SEVERE, null, ex);
    }
}







    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        TableCompras = new javax.swing.JTable();
        txtIdHoras = new javax.swing.JTextField();
        btnHistorialCompra = new javax.swing.JButton();
        txtIdTrabajador = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbxTrabajador = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Horas Trabajadas");

        jPanel12.setBackground(new java.awt.Color(0, 204, 153));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Trabajador", "Puesto de Trabajo", "Pago por Hora", "Hora extra", "Fecha Inicio", "Fecha Fin", "Pago Hora", "Horas Trabajadas", "Horas Extras", "Monto a Pagar"
            }
        ));
        TableCompras.setRowHeight(23);
        jScrollPane14.setViewportView(TableCompras);

        jPanel12.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 1130, 420));
        jPanel12.add(txtIdHoras, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 60, 30));

        btnHistorialCompra.setText("Generar Reporte");
        jPanel12.add(btnHistorialCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 90, 120, 40));
        jPanel12.add(txtIdTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 70, 30));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 100, -1, -1));
        jPanel12.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 50, 140, -1));
        jPanel12.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 50, 120, -1));

        jLabel1.setText("Inicio");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 30, -1, -1));

        jLabel2.setText("Final");
        jPanel12.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 30, -1, -1));

        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });
        cbxTrabajador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTrabajadorActionPerformed(evt);
            }
        });
        jPanel12.add(cbxTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 190, 30));

        jLabel3.setText("Trabajador");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 1190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cbxTrabajadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTrabajadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTrabajadorActionPerformed

    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable TableCompras;
    public javax.swing.JButton btnHistorialCompra;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane14;
    public javax.swing.JTextField txtIdHoras;
    public javax.swing.JTextField txtIdTrabajador;
    // End of variables declaration//GEN-END:variables

    public void MostrarTrabajador(JComboBox comboTrabajador) {

        String sql = "";
        sql = "select * from worker";
        Statement st;

        try {

            st = conect.createStatement();
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

    //CALCULOS PARA PAYROLL IMPOSIBLE DE HACER POR AHORA
    public void calcularDeducciones(int trabajadorId) {
        String sql = "SELECT horas, horas_extras FROM horas_trabajadas WHERE trabajador_id = ? AND fecha = ?";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                double horasTrabajadas = rs.getDouble("horas");
                double horasExtras = rs.getDouble("horas_extras");

                String sqlTrabajador = "SELECT pago_hora FROM trabajadores WHERE id = ?";
                try (PreparedStatement pstTrabajador = conect.prepareStatement(sqlTrabajador)) {
                    pstTrabajador.setInt(1, trabajadorId);
                    ResultSet rsTrabajador = pstTrabajador.executeQuery();
                    if (rsTrabajador.next()) {
                        double pagoHora = rsTrabajador.getDouble("pago_hora");
                        double salarioBruto = horasTrabajadas * pagoHora + horasExtras * pagoHora * 1.5;

                        double impuestoFederal = salarioBruto * obtenerTasaImpuestoFederal(trabajadorId);
                        double seguridadSocial = salarioBruto * 0.062;
                        double medicare = salarioBruto * 0.0145;
                        double impuestoEstatal = salarioBruto * obtenerTasaImpuestoEstatal(trabajadorId);
                        double impuestoLocal = salarioBruto * obtenerTasaImpuestoLocal(trabajadorId);
                        double otrasDeducciones = obtenerOtrasDeducciones(trabajadorId);
                        double salarioNeto = salarioBruto - impuestoFederal - seguridadSocial - medicare - impuestoEstatal - impuestoLocal - otrasDeducciones;

                        guardarDeducciones(trabajadorId, salarioBruto, impuestoFederal, seguridadSocial, medicare, impuestoEstatal, impuestoLocal, otrasDeducciones, salarioNeto);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(HorasTrabajadas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private double obtenerTasaImpuestoFederal(int trabajadorId) {
        // Implementar lógica para obtener la tasa de impuesto federal basada en el formulario W-4 del trabajador
        return 0.1; // Ejemplo, reemplazar con lógica real
    }

    private double obtenerTasaImpuestoEstatal(int trabajadorId) {
        // Implementar lógica para obtener la tasa de impuesto estatal
        return 0.05; // Ejemplo, reemplazar con lógica real
    }

    private double obtenerTasaImpuestoLocal(int trabajadorId) {
        // Implementar lógica para obtener la tasa de impuesto local
        return 0.01; // Ejemplo, reemplazar con lógica real
    }

    private double obtenerOtrasDeducciones(int trabajadorId) {
        // Implementar lógica para obtener otras deducciones
        return 50.0; // Ejemplo, reemplazar con lógica real
    }

    private void guardarDeducciones(int trabajadorId, double salarioBruto, double impuestoFederal, double seguridadSocial, double medicare, double impuestoEstatal, double impuestoLocal, double otrasDeducciones, double salarioNeto) {
        String sql = "INSERT INTO deducciones (trabajador_id, fecha, salario_bruto, impuesto_federal, seguridad_social, medicare, impuesto_estatal, impuesto_local, otras_deducciones, salario_neto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.setDouble(3, salarioBruto);
            pst.setDouble(4, impuestoFederal);
            pst.setDouble(5, seguridadSocial);
            pst.setDouble(6, medicare);
            pst.setDouble(7, impuestoEstatal);
            pst.setDouble(8, impuestoLocal);
            pst.setDouble(9, otrasDeducciones);
            pst.setDouble(10, salarioNeto);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(HorasTrabajadas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Implementar métodos para generar el reporte
    public void generarReporte() {
        String sql = "SELECT * FROM deducciones WHERE trabajador_id = ? AND fecha BETWEEN ? AND ?";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
//        pst.setInt(1, obtenerIdTrabajadorSeleccionado());
            pst.setDate(2, new java.sql.Date(jDateChooser1.getDate().getTime()));
            pst.setDate(3, new java.sql.Date(jDateChooser2.getDate().getTime()));
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) TableCompras.getModel();
            model.setRowCount(0); // Clear existing data

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    //                obtenerNombreTrabajador(rs.getInt("trabajador_id")),
                    rs.getDouble("salario_bruto"),
                    rs.getDouble("impuesto_federal"),
                    rs.getDouble("seguridad_social"),
                    rs.getDouble("medicare"),
                    rs.getDouble("impuesto_estatal"),
                    rs.getDouble("impuesto_local"),
                    rs.getDouble("otras_deducciones"),
                    rs.getDouble("salario_neto")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage());
        }
    }

}
