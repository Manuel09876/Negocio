package Presentation;

import Administration.AsignacionPermisos;
import Reports.HorasTrabajadas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import conectar.Conectar;
import java.awt.ComponentOrientation;
import Bases.PermisoManager;

public class Loggin extends javax.swing.JFrame {

    private static final Logger LOGGER = Logger.getLogger(HorasTrabajadas.class.getName());
    private int trabajadorId; // Variable de instancia para almacenar el ID del trabajador
    private HorasTrabajadas ht; // Asegúrate de que esta instancia esté correctamente inicializada
    private AsignacionPermisos ap;
    private PermisoManager pm;

    public Loggin() {
        initComponents();  // Asegúrate de que los componentes estén inicializados antes

        ht = new HorasTrabajadas();
        LOGGER.info("Componentes inicializados y listos para la autenticación del usuario.");
        txtUser.requestFocus();  // Ahora puedes llamar a requestFocus() aquí sin problemas
        btnTest.setVisible(false);
    }

    public void IngresaSistema(String usuario, String contrasena) {
        LOGGER.info("Intentando acceder con el usuario: " + usuario);
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión
        LOGGER.fine("Conexión obtenida: " + connection);
        if (connection == null) {
            LOGGER.severe("Error: La conexión a la base de datos es nula.");
            throw new RuntimeException("Error: La conexión a la base de datos es nula.");
        }

        String sql = "SELECT idUsuarios, rol, password FROM usuarios WHERE usuario = ? AND estado = 'Activo'";
        LOGGER.fine("Ejecutando consulta: " + sql);

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, usuario);
            System.out.println("Ejecutando consulta: " + sql);

            try (ResultSet result = pst.executeQuery()) {
                if (result.next()) {
                    String passwordBD = result.getString("password");
                    if (passwordBD.equals(contrasena)) {  // Asegúrate de que la comparación sea segura si usas hash
                        int rolId = result.getInt("rol");
                        System.out.println("Rol ID capturado: " + rolId);  // Agregar esta línea para depurar el rolId
                        int idUsuarios = result.getInt("idUsuarios");

                        // Obtener el idTrabajador relacionado con el idUsuarios
                        int idTrabajador = obtenerTrabajadorId(usuario);
                        if (idTrabajador != 0) {
                            ht.registrarInicioSesion(idTrabajador); // Registrar inicio de sesión del trabajador
                            // Lógica para abrir la ventana principal

                            // Abrir la ventana principal pasando rolId y usuario
                            abrirVentanaPrincipal(rolId, usuario);

                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Usuario no encontrado o inactivo");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al ingresar al sistema", ex);
            JOptionPane.showMessageDialog(this, "Error al ingresar al sistema. Por favor, intente de nuevo.");
        } finally {
            Conectar.getInstancia().devolverConexion(connection); // Devolver la conexión al pool
        }
    }

    private void abrirVentanaPrincipal(int rolId, String usuario) throws SQLException {
        VentanaPrincipal objVP = new VentanaPrincipal(rolId, usuario);
        objVP.setVisible(true);

        // Llamar al nuevo método para cargar permisos de login
        cargarPermisosDesdeLogin(rolId, objVP);

        System.out.println(rolId);

        this.dispose();
    }

    //Metodos para cargar la interfaz
    public void cargarPermisosDesdeLogin(int rolId, VentanaPrincipal objVP) {
        if (rolId != -1) {
            String usuario = null;

//        rolId = obtenerRolDeUsuario(usuario); // Forzar la actualización del rolId.
            if (rolId == -1) {
                System.out.println("Error: No se pudo obtener un rol válido.");
                return;
            }

            // Cargar permisos de menús principales
            cargarPermisosMenuPrincipal(rolId, objVP);

            // Cargar permisos de submenús
            cargarPermisosSubmenus(rolId, objVP);
        } else {
            JOptionPane.showMessageDialog(null, "Rol ID no válido.");
        }
    }

    public void cargarPermisosMenuPrincipal(int rolId, VentanaPrincipal objVP) {
        Connection connection = null;
        try {
            connection = Conectar.getInstancia().obtenerConexion();
            if (connection == null) {
                throw new RuntimeException("Error: La conexión a la base de datos es nula.");
            }

            String sql = """
            SELECT m.nombre_menu, rm.visualizar
            FROM roles_menus rm
            JOIN menus m ON rm.id_menu = m.id_menu
            WHERE rm.id_rol = ? AND rm.activo = true;
        """;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, rolId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String nombreMenu = rs.getString("nombre_menu");
                    boolean visualizar = rs.getBoolean("visualizar");

                    // Agrega depuración
                    System.out.println("Nombre del menú recuperado: " + nombreMenu + ", Visualizar: " + visualizar);

                    // Aquí estamos actualizando la visibilidad del menú en VentanaPrincipal
                    objVP.actualizarMenuPrincipal(nombreMenu, visualizar);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void cargarPermisosSubmenus(int rolId, VentanaPrincipal objVP) {
        Connection connection = null;
        try {
            connection = Conectar.getInstancia().obtenerConexion();
            if (connection == null) {
                throw new RuntimeException("Error: La conexión a la base de datos es nula.");
            }

            String sql = """
                SELECT rs.id_menu, rs.id_submenu, rs.visualizar, rs.agregar, rs.editar, rs.eliminar, sm.nombre_submenu
                FROM roles_menus_submenus rs
                JOIN submenus sm ON rs.id_submenu = sm.id_submenu
                WHERE rs.id_rol = ? AND rs.activo = true;
            """;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, rolId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int menuId = rs.getInt("id_menu");
                    String nombreSubmenu = rs.getString("nombre_submenu");
                    boolean visualizar = rs.getBoolean("visualizar");
                    boolean agregar = rs.getBoolean("agregar");
                    boolean editar = rs.getBoolean("editar");
                    boolean eliminar = rs.getBoolean("eliminar");

                    // Llamar un método para actualizar la interfaz con los permisos
                    objVP.aplicarPermisos(menuId, nombreSubmenu, visualizar, agregar, editar, eliminar);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
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
        System.out.println("Usuario: " + usu + ", Contraseña: " + pas); // Depuración
        IngresaSistema(usu, pas);

        // Código para obtener tipUsu y usuario
        String usuario = txtUser.getText();
        int tipUsu = obtenerRolDeUsuario(usuario); // Implementa este método para obtener el rol del usuario
//        try {
//            abrirVentanaPrincipal(tipUsu, usuario);
//        } catch (SQLException ex) {
//            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_btnEnterActionPerformed

    private void btnTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestActionPerformed

        JOptionPane.showMessageDialog(null, "Conexion establecida");

    }//GEN-LAST:event_btnTestActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        if (JOptionPane.showConfirmDialog(null, "¡Desea salir del Sistema?", "Acceso", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
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

    private int obtenerTrabajadorId(String usuario) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida aquí
        if (connection == null) {
            throw new RuntimeException("Error: La conexión a la base de datos es nula.");
        }

        String sql = "SELECT ut.id_trabajador FROM usuario_trabajador ut INNER JOIN usuarios u ON ut.id_usuario = u.idUsuarios WHERE u.usuario = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, usuario);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int idTrabajador = rs.getInt("id_trabajador");
                    System.out.println("ID del trabajador obtenido: " + idTrabajador);
                    return idTrabajador;
                } else {
                    System.out.println("No se encontró el trabajador para el usuario: " + usuario);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conectar.getInstancia().devolverConexion(connection); // Devolver la conexión al pool
        }
        return 0;
    }

    private int obtenerRolDeUsuario(String usuario) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida aquí
        if (connection == null) {
            throw new RuntimeException("Error: La conexión a la base de datos es nula.");
        }

        String sql = "SELECT rol FROM usuarios WHERE usuario = ?";
        int rolId = 0;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rolId = rs.getInt("rol"); // Obtener el ID del rol
                } else {
                    System.out.println("Usuario no encontrado: " + usuario);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conectar.getInstancia().devolverConexion(connection); // Devolver la conexión
        }

        return rolId;
    }

    //    // Este nuevo método cargará los permisos al inicio de sesión usando el rolId del usuario
//    public void cargarPermisosLogin(int rolId, VentanaPrincipal objVP) {
//        Connection connection = Conectar.getInstancia().obtenerConexion();
//        if (connection == null) {
//            throw new RuntimeException("Error: La conexión a la base de datos es nula.");
//        }
//
//        String sql = "SELECT nombre_menu AS menu, nombre_submenu AS submenu FROM roles_menus_submenus rms "
//                   + "JOIN menus m ON rms.id_menu = m.id_menu "
//                   + "LEFT JOIN submenus s ON rms.id_submenu = s.id_submenu "
//                   + "WHERE rms.id_rol = ?";
//
//        try (PreparedStatement pst = connection.prepareStatement(sql)) {
//            pst.setInt(1, rolId);
//            ResultSet rs = pst.executeQuery();
//            while (rs.next()) {
//                String menuName = rs.getString("menu");
//                String submenuName = rs.getString("submenu");
//                objVP.setMenuVisibility(menuName, true);
//                if (submenuName != null) {
//                    objVP.setMenuVisibility(submenuName, true);
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Error al cargar permisos del usuario: " + e.getMessage());
//        } finally {
//            Conectar.getInstancia().devolverConexion(connection);
//        }
//    }
    //    private int obtenerTrabajadorId(int idUsuario) {
//        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida aquí
//        if (connection == null) {
//            throw new RuntimeException("Error: La conexión a la base de datos es nula.");
//        }
//
//        String sql = "SELECT id_trabajador FROM usuario_trabajador WHERE id_usuario = ?";
//        try {
//            PreparedStatement pst = connection.prepareStatement(sql);
//            pst.setInt(1, idUsuario);
//            ResultSet rs = pst.executeQuery();
//            if (rs.next()) {
//                int idTrabajador = rs.getInt("id_trabajador");
//                System.out.println("ID del trabajador obtenido: " + idTrabajador); // Mensaje de depuración
//                return idTrabajador;
//            } else {
//                System.out.println("No se encontró el trabajador para el usuario con ID: " + idUsuario); // Mensaje de depuración
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            Conectar.getInstancia().devolverConexion(connection);
//        }
//        return 0;
//    }
}
