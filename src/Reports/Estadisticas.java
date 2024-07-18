package Reports;

import java.text.SimpleDateFormat;

public class Estadisticas extends javax.swing.JInternalFrame {

    public Estadisticas() {
        initComponents();
    }

    public void BuscarDatos() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaInicio = dateFormat.format(dateInicio.getDate());
        String fechaFin = dateFormat.format(dateFin.getDate());
        
        try {
            String todas = "SELECT SUM(precio) FROM orderservice";
            String deuda ="SELECT SUM(precio) FROM orderservice WHERE eeCta = 'Deuda'";
            String canceladas = "SELECT SUM(precio) FROM orderservice WHERE eeCta = 'Cancelada'";
            String deudasPagar = "SELECT SUM(cuota) FROM credito" + "SELECT SUM(cuota) FROM creditopg" + "SELECT SUM(cuota) FROM creditoprod";
            String todasPorEmpresa = "SELECT SUM(precio) FROM orderservice WHERE id_empresa=3";
            
            
        } catch (Exception e) {
        }
    }
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        txtEgreso = new javax.swing.JTextField();
        btnHistorialCompra = new javax.swing.JButton();
        txtIngreso = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDeudasPorCobrar = new javax.swing.JTextField();
        txtDeudasPorPagar = new javax.swing.JTextField();
        dateFin = new com.toedter.calendar.JDateChooser();
        dateInicio = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Estadisticas");

        jPanel12.setBackground(new java.awt.Color(255, 153, 0));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel12.add(txtEgreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 150, 30));

        btnHistorialCompra.setText("Generar Reporte");
        jPanel12.add(btnHistorialCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 20, 120, 50));
        jPanel12.add(txtIngreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 150, 30));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 20, -1, -1));

        jLabel1.setText("Ingreso");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        jLabel2.setText("Egreso");
        jPanel12.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jLabel3.setText("Deuda por Cobrar");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        jLabel4.setText("Deudas por Pagar");
        jPanel12.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, -1));
        jPanel12.add(txtDeudasPorCobrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 112, 150, 30));
        jPanel12.add(txtDeudasPorPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 152, 150, 30));
        jPanel12.add(dateFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 40, 170, 30));
        jPanel12.add(dateInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 170, 30));

        jLabel5.setText("Fecha Inicio");
        jPanel12.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, -1, -1));

        jLabel6.setText("Fecha Término");
        jPanel12.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1190, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 1190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 589, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnHistorialCompra;
    private javax.swing.JButton btnSalir;
    private com.toedter.calendar.JDateChooser dateFin;
    private com.toedter.calendar.JDateChooser dateInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JTextField txtDeudasPorCobrar;
    private javax.swing.JTextField txtDeudasPorPagar;
    public javax.swing.JTextField txtEgreso;
    public javax.swing.JTextField txtIngreso;
    // End of variables declaration//GEN-END:variables
}
