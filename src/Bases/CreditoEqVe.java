package Bases;

import Register.CompraEquiposVehiculos;
import com.toedter.calendar.JDateChooser;
import conectar.Conectar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class CreditoEqVe extends javax.swing.JFrame {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    DefaultTableModel model = new DefaultTableModel();
    int id;
    int id_Compra;
    Double Diferencia;
    int Frecuencia;
    Date fechaInicio;
    double interes;
    int NumeroCuotas;
    double ValorCuota;
    Date fechasPago;
    String estado;
    private int tipoPago; // 6 para préstamo, 3 para tarjeta de crédito

    public CreditoEqVe(int id, int id_Compra, Double Diferencia, int Frecuencia, Date fechaInicio, double interes, int NumeroCuotas, Double ValorCuota, Date fechasPago, String estado, int tipoPago) {
        this.id = id;
        this.id_Compra = id_Compra;
        this.Diferencia = Diferencia;
        this.Frecuencia = Frecuencia;
        this.fechaInicio = fechaInicio;
        this.interes = interes;
        this.NumeroCuotas = NumeroCuotas;
        this.ValorCuota = ValorCuota;
        this.fechasPago = fechasPago;
        this.estado = estado;
        this.tipoPago = tipoPago;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_Compra() {
        return id_Compra;
    }

    public void setId_Compra(int id_Compra) {
        this.id_Compra = id_Compra;
    }

    public Double getDiferencia() {
        return Diferencia;
    }

    public void setDiferencia(Double Diferencia) {
        this.Diferencia = Diferencia;
    }

    public int getFrecuencia() {
        return Frecuencia;
    }

    public void setFrecuencia(int Frecuencia) {
        this.Frecuencia = Frecuencia;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public int getNumeroCuotas() {
        return NumeroCuotas;
    }

    public void setNumeroCuotas(int NumeroCuotas) {
        this.NumeroCuotas = NumeroCuotas;
    }

    public double getValorCuota() {
        return ValorCuota;
    }

    public void setValorCuota(double ValorCuota) {
        this.ValorCuota = ValorCuota;
    }

    public Date getFechasPago() {
        return fechasPago;
    }

    public void setFechasPago(Date fechasPago) {
        this.fechasPago = fechasPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public JDateChooser getDateInicio() {
        return dateInicio;
    }

    public void setDateInicio(JDateChooser dateInicio) {
        this.dateInicio = dateInicio;
    }

    public CreditoEqVe() {
        initComponents();
        txtIdCompra.setEnabled(false);
        txtIdCredito.setEnabled(false);
//        initListeners(); //Inicializar los listener

    }

    private String inicial, diferencia;

    public void EnviarDatos(String inicial, String diferencia) {
        this.inicial = inicial;
        this.diferencia = diferencia;
        txtInicial.setText(inicial);
        txtDiferencia.setText(diferencia);
    }

    // Método para hallar el id_venta que se está ejecutando en ese momento
//    public int IdCompra() {
//        int id = 0;
//        String sql = "SELECT MAX(id_equipos) FROM equipos";
//        Conectar con = new Conectar();
//        Connection connect = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            connect = con.getConexion();
//            ps = connect.prepareStatement(sql);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                id = rs.getInt(1);
//            }
//        } catch (SQLException e) {
//            System.out.println(e.toString());
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (ps != null) {
//                    ps.close();
//                }
//                if (connect != null) {
//                    connect.close();
//                }
//            } catch (SQLException e) {
//            }
//        }
//        return id;
//    }
//
//    public void registrarPagosPendientes() {
//        Conectar con = new Conectar();
//        Connection connect = null;
//        PreparedStatement stmt = null;
//        try {
//            connect = con.getConexion();
//
//            // Obtener los datos de las cajas de texto
//            int id = IdCompra();
//            int frecuencia = Integer.parseInt(txtFrecuencia.getText());
//            double interes = Double.parseDouble(txtInteres.getText());
//            int numeroCuotas = Integer.parseInt(txtNumeroCuotas.getText());
//            double valorCuota = Double.parseDouble(txtValorCuota.getText());
//            double total = Double.parseDouble(txtTotal.getText());
//            double diferencia = total;
//
//            // Obtener la fecha de inicio
//            Date fechaInicio = dateInicio.getDate();
//            if (fechaInicio == null) {
//                JOptionPane.showMessageDialog(null, "La fecha de inicio es nula. Por favor selecciona una fecha válida.");
////                return;
//            }
//
//            // Preparar la inserción de pagos en la base de datos
//            String sql = "INSERT INTO credito (id_compra, frecuencia, fechaPago, interes, NumeroCuotas, cuota, Diferencia, estado) "
//                    + "VALUES (?, ?, ?, ?, ?, ?, ?, 'Pendiente')";
//            stmt = connect.prepareStatement(sql);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(fechaInicio);
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//            for (int i = 1; i <= numeroCuotas; i++) {
//                calendar.add(Calendar.DAY_OF_MONTH, frecuencia);
//
//                Date fechaPago = calendar.getTime();
//                diferencia -= valorCuota;
//
//                stmt.setInt(1, id);
//                stmt.setInt(2, frecuencia);
//                stmt.setString(3, dateFormat.format(fechaPago));
//                stmt.setDouble(4, interes);
//                stmt.setInt(5, i); // Número de la cuota actual
//                stmt.setDouble(6, valorCuota);
//                stmt.setDouble(7, diferencia); // Diferencia actualizada
//                stmt.setString(8, "Pendiente");
//
//                stmt.executeUpdate();
//
////                // Mostrar aviso dos días antes de la fecha de pago
////                Calendar avisoCalendar = Calendar.getInstance();
////                avisoCalendar.setTime(fechaPago);
////                avisoCalendar.add(Calendar.DAY_OF_MONTH, -2);
////                Date fechaAviso = avisoCalendar.getTime();
////                DateFormat avisoFormat = new SimpleDateFormat("dd/MM/yyyy");
////                JOptionPane.showMessageDialog(null, "¡Atención! Quedan 2 días para la fecha de pago de la cuota " + i + ": " + avisoFormat.format(fechaAviso));
//            }
//
//            JOptionPane.showMessageDialog(null, "Crédito registrado correctamente con todas las fechas de pago.");
//
//        } catch (NumberFormatException ex) {
//            System.out.println("Error "+ex);
//            JOptionPane.showMessageDialog(null, "Error al parsear un valor numérico: " + ex.getMessage());
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
//        } finally {
//            try {
//                if (stmt != null) {
//                    stmt.close();
//                }
//                if (connect != null) {
//                    connect.close();
//                }
//            } catch (SQLException ex) {
//                JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + ex.getMessage());
//            }
//        }
//    }
//
//    public void MostrarTabla(String Valores) {
//        try {
//            String[] titulosTabla = {"id", "idCompra", "Descripcion", "Fecha de Pago", "Cuota Numero", "Monto a Pagar"}; //Titulos de la Tabla
//            String[] RegistroBD = new String[6];
//
//            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla
//
//            String sql = "";
//
//            Conectar con = new Conectar();
//            Connection connect = con.getConexion();
//            Statement st = connect.createStatement();
//            ResultSet result = st.executeQuery(sql);
//            
//            while (result.next()) {
//                RegistroBD[0] = result.getString("id");
//                RegistroBD[1] = result.getString("idCompra");
//                RegistroBD[2] = result.getString("descripcion");
//                RegistroBD[3] = result.getString("fechaPago");
//                RegistroBD[4] = result.getString("NumeroCuotas");
//                RegistroBD[5] = result.getString("cuota");
//                
//
//                model.addRow(RegistroBD);
//            }
//
//            tbCredito.setModel(model); //Le asignamos a nuestra tabla la modelación de la Base de Datos
//            TableColumn ci = tbCredito.getColumn("idCredito");
//            ci.setMaxWidth(0);
//            ci.setMinWidth(0);
//            ci.setPreferredWidth(0);
//            tbCredito.doLayout();
//            
//        } catch (SQLException e) {
//        }
//    }
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        dateInicio = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        txtFrecuencia = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtNumeroCuotas = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtInteres = new javax.swing.JTextField();
        btnFechasDePago = new javax.swing.JButton();
        txtIdCredito = new javax.swing.JTextField();
        txtInicial = new javax.swing.JTextField();
        txtDiferencia = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        txtValorCuota = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtIdCompra = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCredito = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 102, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, -1, -1));

        jLabel10.setText("Proxima Fecha de Pago");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, -1, -1));

        dateInicio.setDateFormatString("yyyy-MM-dd");
        jPanel1.add(dateInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 150, -1));

        jLabel12.setText("Frecuencia de Pago");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, -1, -1));
        jPanel1.add(txtFrecuencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, 100, -1));

        jLabel8.setText("Numero de cuotas");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, -1, 30));
        jPanel1.add(txtNumeroCuotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, 100, -1));

        jLabel7.setText("Tasa de Interes");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, -1, -1));
        jPanel1.add(txtInteres, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, 100, -1));

        btnFechasDePago.setText("Establecer Fechas de Pago");
        btnFechasDePago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFechasDePagoActionPerformed(evt);
            }
        });
        jPanel1.add(btnFechasDePago, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, -1, -1));
        jPanel1.add(txtIdCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 14, 98, -1));
        jPanel1.add(txtInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 130, -1));
        jPanel1.add(txtDiferencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 120, -1));

        jLabel1.setText("Inicial");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

        jLabel2.setText("Diferencia");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, -1, -1));

        jButton2.setText("Modificar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 70, -1, -1));

        jButton3.setText("Eliminar");
        jButton3.setToolTipText("");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, -1, -1));

        jButton4.setText("Pagada");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 170, -1, -1));
        jPanel1.add(txtValorCuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, 100, -1));

        jLabel3.setText("Valor de cuota");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 170, -1, -1));
        jPanel1.add(txtIdCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel4.setText("%");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, -1, -1));

        jLabel5.setText("Total");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, -1, -1));
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 100, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 710, 208));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbCredito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbCredito);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 14, 710, 255));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 220, 720, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnFechasDePagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechasDePagoActionPerformed
//        registrarPagosPendientes();
//        LimpiarCampos();
    }//GEN-LAST:event_btnFechasDePagoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CreditoEqVe.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreditoEqVe.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreditoEqVe.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreditoEqVe.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreditoEqVe().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFechasDePago;
    private com.toedter.calendar.JDateChooser dateInicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbCredito;
    private javax.swing.JTextField txtDiferencia;
    private javax.swing.JTextField txtFrecuencia;
    private javax.swing.JTextField txtIdCompra;
    private javax.swing.JTextField txtIdCredito;
    private javax.swing.JTextField txtInicial;
    private javax.swing.JTextField txtInteres;
    private javax.swing.JTextField txtNumeroCuotas;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtValorCuota;
    // End of variables declaration//GEN-END:variables

//    private void LimpiarCampos() {
//        txtDiferencia.setText("");
//        txtFrecuencia.setText("");
//        txtInicial.setText("");
//        txtInteres.setText("");
//        txtNumeroCuotas.setText("");
//        txtValorCuota.setText("");
//        dateInicio.setDateFormatString("");
//        txtTotal.setText("");
//    }
    
//     private void initListeners() {
//        txtInicial.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                calcularDiferencia();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                calcularDiferencia();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                calcularDiferencia();
//            }
//        });
//    }
//
//    private void calcularDiferencia() {
//        try {
//            double total = Double.parseDouble(txtTotal.getText());
//            double inicial = Double.parseDouble(txtInicial.getText());
//            double diferencia = total - inicial;
//            txtDiferencia.setText(String.valueOf(diferencia));
//        } catch (NumberFormatException ex) {
//            // Manejo de excepción en caso de que los textos no sean números válidos
////            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos.");
//        }
//        
//    }

}
