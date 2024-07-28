
package Reports;


public class HorasTrabajadas extends javax.swing.JInternalFrame {


    public HorasTrabajadas() {
        initComponents();
        txtIdHoras.setEnabled(false);
        
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

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Horas Trabajadas");

        jPanel12.setBackground(new java.awt.Color(0, 204, 153));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Trabajador", "Pago Hora", "Horas", "Horas Extras", "Fecha"
            }
        ));
        TableCompras.setRowHeight(23);
        jScrollPane14.setViewportView(TableCompras);

        jPanel12.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 1060, 420));
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

        jPanel12.add(cbxTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 190, 30));

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable TableCompras;
    public javax.swing.JButton btnHistorialCompra;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane14;
    public javax.swing.JTextField txtIdHoras;
    public javax.swing.JTextField txtIdTrabajador;
    // End of variables declaration//GEN-END:variables
}
