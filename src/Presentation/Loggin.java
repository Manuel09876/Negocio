package Presentation;

import Reports.HorasTrabajadas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import conectar.Conectar;

public class Loggin extends javax.swing.JFrame {

    private static final Logger LOGGER = Logger.getLogger(HorasTrabajadas.class.getName());
    private int trabajadorId; // Variable de instancia para almacenar el ID del trabajador

    HorasTrabajadas ht;

    public Loggin() {
        initComponents();
        ht = new HorasTrabajadas();
        txtUser.requestFocus();
        btnTest.setVisible(false);

    }

    public void IngresaSistema(String Usuario, String Contrasena) {
        String sql = "SELECT idUsuarios, rol_id FROM usuarios WHERE usuario = ? AND password = ?";
        try {
            PreparedStatement pst = conect.prepareStatement(sql);
            pst.setString(1, Usuario);
            pst.setString(2, Contrasena);
            try (ResultSet result = pst.executeQuery()) {
                if (result.next()) {
                    int rolId = result.getInt("rol_id");
                    int trabajadorId = result.getInt("idUsuarios");
                    System.out.println("ID del trabajador al ingresar: " + trabajadorId); // Mensaje de depuración
                    this.setVisible(false);
                    VentanaPrincipal objVP = new VentanaPrincipal(rolId);
                    objVP.setVisible(true);
                    objVP.pack();
                    VentanaPrincipal.lbUsuario.setText(Usuario);
                    ht.registrarInicioSesion(trabajadorId); // Registrar inicio de sesión
                } else {
                    JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblUser = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnTest = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lblSystemAccess = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnEnter = new javax.swing.JButton();
        btnClean = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setBackground(new java.awt.Color(0, 0, 204));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblUser.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblUser.setText("User");
        getContentPane().add(lblUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 196, -1, -1));

        lblPassword.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPassword.setText("Password");
        getContentPane().add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 242, -1, -1));

        txtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });
        getContentPane().add(txtUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 200, 155, -1));

        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });
        getContentPane().add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 246, 155, -1));

        btnTest.setText("Test");
        btnTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestActionPerformed(evt);
            }
        });
        getContentPane().add(btnTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 150, -1, -1));

        jPanel1.setBackground(new java.awt.Color(0, 51, 204));

        jScrollPane1.setBackground(new java.awt.Color(0, 102, 153));

        lblSystemAccess.setBackground(new java.awt.Color(255, 255, 255));
        lblSystemAccess.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblSystemAccess.setText("Acceso al Sistema");
        jScrollPane1.setViewportView(lblSystemAccess);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(126, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 494, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/password.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 242, 48, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/users.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 191, 48, 31));

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        btnEnter.setBackground(new java.awt.Color(0, 153, 0));
        btnEnter.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnEnter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exit small.png"))); // NOI18N
        btnEnter.setText("Enter/Ingresar");
        btnEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnterActionPerformed(evt);
            }
        });

        btnClean.setBackground(new java.awt.Color(0, 153, 0));
        btnClean.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnClean.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/clear.png"))); // NOI18N
        btnClean.setText("Clean/Limpiar");
        btnClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanActionPerformed(evt);
            }
        });

        btnExit.setBackground(new java.awt.Color(0, 153, 0));
        btnExit.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/salir.png"))); // NOI18N
        btnExit.setText("Exit/Salir");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(btnEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnClean, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClean, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 490, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnterActionPerformed

        String usu = txtUser.getText();
        String pas = new String(txtPassword.getPassword());

        IngresaSistema(usu, pas);

    }//GEN-LAST:event_btnEnterActionPerformed

    private void btnTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestActionPerformed
        Conectar conecta = new Conectar();
        Connection con = conecta.getConexion();
        JOptionPane.showMessageDialog(null, "Conexion establecida");

        // TODO add your handling code here:
    }//GEN-LAST:event_btnTestActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        if (JOptionPane.showConfirmDialog(null, "¡Desea salir del Sistema?", "Acceso", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
        int trabajadorId = obtenerTrabajadorId(txtUser.getText());
        if (trabajadorId != 0) {
            System.out.println("ID del trabajador al salir: " + trabajadorId); // Mensaje de depuración
            ht.registrarFinSesion(trabajadorId);
        } else {
            System.out.println("No se encontró el ID del trabajador."); // Mensaje de depuración
        }
        System.exit(0);
    }
    }//GEN-LAST:event_btnExitActionPerformed


    private void btnCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanActionPerformed

        txtUser.setText("");
        txtPassword.setText("");

    }//GEN-LAST:event_btnCleanActionPerformed

    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserActionPerformed

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Loggin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Loggin().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClean;
    private javax.swing.JButton btnEnter;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnTest;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSystemAccess;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

    Conectar conexion = new Conectar();
    Connection conect = conexion.getConexion();

    private int obtenerTrabajadorId(String usuario) {
    String sql = "SELECT idUsuarios FROM usuarios WHERE usuario = ?";
    try {
        PreparedStatement pst = conect.prepareStatement(sql);
        pst.setString(1, usuario);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            int trabajadorId = rs.getInt("idUsuarios");
            System.out.println("ID del trabajador obtenido: " + trabajadorId); // Mensaje de depuración
            return trabajadorId;
        } else {
            System.out.println("No se encontró el trabajador con usuario: " + usuario); // Mensaje de depuración
        }
    } catch (SQLException ex) {
        Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
    }
    return 0;
}



}
