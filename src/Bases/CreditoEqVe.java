
package Bases;

import com.toedter.calendar.JDateChooser;
import conectar.Conectar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;


public class CreditoEqVe extends javax.swing.JFrame {
    
    int id;
    int id_Compra;
    int Frecuencia;
    Date fechaInicio;
    Date fechaTermino;
    double interes;
    double cuota;
    Date fechasPago;
    String estado;

    public CreditoEqVe(int id, int id_Compra, int Frecuencia, Date fechaInicio, Date fechaTermino, double interes, double cuota, Date fechasPago, String estado) {
        this.id = id;
        this.id_Compra = id_Compra;
        this.Frecuencia = Frecuencia;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.interes = interes;
        this.cuota = cuota;
        this.fechasPago = fechasPago;
        this.estado = estado;
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

    public Date getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(Date fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public double getCuota() {
        return cuota;
    }

    public void setCuota(double cuota) {
        this.cuota = cuota;
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

    public JDateChooser getDateTermino() {
        return dateTermino;
    }

    public void setDateTermino(JDateChooser dateTermino) {
        this.dateTermino = dateTermino;
    }
    
    
    public CreditoEqVe() {
        initComponents();
        txtIdCompra.setEnabled(false);
        txtIdCredito.setEnabled(false);
        
    }
    
    private String inicial, diferencia;
     
    public void EnviarDatos(String inicial, String diferencia){
        this.inicial = inicial;
        this.diferencia = diferencia;
        txtInicial.setText(inicial);
        txtDiferencia.setText(diferencia);
//        addCheckBox(0, tbCredito);
    }
    
    //Metodo que agrega el checkBox
    public void addCheckBox(int column, JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }

    public boolean IsSelected(int row, int column, JTable table) {
        return table.getValueAt(row, column) != null;
    }
    
    private void registrarPagosPendientes() {

        Conectar con = new Conectar();
        Connection connect = con.getConexion();
        PreparedStatement stmt = null;
        try {
            // Obtenemos los datos de las cajas de texto
            int id_Compra = Integer.parseInt(txtIdCompra.getText().trim());
            

            //Validacion para la Frecuencia
            int frecuencia = 0;
            frecuencia = Integer.parseInt(txtFrecuencia.getText());


            double precio = Double.parseDouble(txtCuota.getText());

            //obtener la fecha de inicio y fin
            Date fechaInicio = dateInicio.getDate();
            Date fechafin = dateTermino.getDate();

            //Calcular las fechas intermedias basadas en la frecuencia
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaInicio);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            while (calendar.getTime().before(fechaTermino)) {
                //Insertar la orden de servicio para cada fecha intermedia
                String sql = "INSERT INTO credito (id_compra, frecuencia, fechaInicio, fechaTermino, interes, cuota, fechaPago) VALUES (?,?,?, ?, ?, ?, ?)";

                stmt = connect.prepareStatement(sql);
                stmt.setString(1, dateFormat.format(calendar.getTime()));
                stmt.setInt(2, getId_Compra());
                stmt.setInt(3, frecuencia);
                stmt.setString(4, dateFormat.format(dateInicio.getDate()));
                stmt.setString(5, dateFormat.format(dateTermino.getDate()));
                stmt.setDouble(6, getInteres());
                stmt.setDouble(7, getCuota());
                
                //Ejecutar la consulta
                stmt.executeUpdate();

                //Incrementar la fecha segun la frecuencia
                calendar.add(Calendar.DAY_OF_MONTH, frecuencia);
            }
            JOptionPane.showInternalMessageDialog(null, "Ordenes de servicio registradas correctamente");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    
    

   

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
        txtCuota = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        dateTermino = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        txtInteres = new javax.swing.JTextField();
        btnFechasDePago = new javax.swing.JButton();
        txtIdCompra = new javax.swing.JTextField();
        txtIdCredito = new javax.swing.JTextField();
        txtInicial = new javax.swing.JTextField();
        txtDiferencia = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
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

        jLabel10.setText("Inicio");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, -1, -1));
        jPanel1.add(dateInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 150, -1));

        jLabel12.setText("Frecuencia");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, -1, -1));
        jPanel1.add(txtFrecuencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 70, 100, -1));

        jLabel8.setText("Cuota");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, -1, -1));
        jPanel1.add(txtCuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 130, 100, -1));

        jLabel11.setText("Termino");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, -1, -1));
        jPanel1.add(dateTermino, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 150, -1));

        jLabel7.setText("Tasa de Interes");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, -1, -1));
        jPanel1.add(txtInteres, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 100, 100, -1));

        btnFechasDePago.setText("Establecer Fechas de Pago");
        btnFechasDePago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFechasDePagoActionPerformed(evt);
            }
        });
        jPanel1.add(btnFechasDePago, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 170, -1, -1));
        jPanel1.add(txtIdCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 152, 91, -1));
        jPanel1.add(txtIdCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 14, 98, -1));
        jPanel1.add(txtInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 130, -1));
        jPanel1.add(txtDiferencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 120, -1));

        jLabel1.setText("Inicial");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

        jLabel2.setText("Diferencia");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, -1, -1));

        jButton2.setText("Modificar");
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 70, -1, -1));

        jButton3.setText("Eliminar");
        jButton3.setToolTipText("");
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, -1, -1));

        jButton4.setText("Pagada");
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 170, -1, -1));

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
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFechasDePagoActionPerformed

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
            java.util.logging.Logger.getLogger(CreditoEqVe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreditoEqVe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreditoEqVe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreditoEqVe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private com.toedter.calendar.JDateChooser dateTermino;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbCredito;
    private javax.swing.JTextField txtCuota;
    private javax.swing.JTextField txtDiferencia;
    private javax.swing.JTextField txtFrecuencia;
    private javax.swing.JTextField txtIdCompra;
    private javax.swing.JTextField txtIdCredito;
    private javax.swing.JTextField txtInicial;
    private javax.swing.JTextField txtInteres;
    // End of variables declaration//GEN-END:variables

    

}
