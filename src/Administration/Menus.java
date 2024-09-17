package Administration;

import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Menus extends javax.swing.JInternalFrame {

    Conectar conexion = new Conectar();
    Connection conect = conexion.getConexion();
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel modelo = new DefaultTableModel();

    public Menus() {
        initComponents();

        CargarDatosTableMenu("");
        CargarDatosTableSubMenu("");

    }

    void CargarDatosTableMenu(String Valores) {
        try {
            String[] titulosTabla = {"Id", "Menu"}; //Titulos de la Tabla
            String[] RegistroBD = new String[2];                                   //Registros de la Basede Datos
            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla
            String ConsultaSQL = "select * from menus";
            Statement st = conect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);
            while (result.next()) {
                RegistroBD[0] = result.getString("id_menu");
                RegistroBD[1] = result.getString("nombre_menu");

                model.addRow(RegistroBD);
            }
            tbMenus.setModel(model);
            tbMenus.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbMenus.getColumnModel().getColumn(1).setPreferredWidth(200);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void CargarDatosTableSubMenu(String Valores) {
        try {
            String[] titulosTabla = {"Id", "Menu", "Submenu"}; //Titulos de la Tabla
            String[] RegistroBD = new String[3];                                   //Registros de la Basede Datos
            modelo = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla
            String ConsultaSQL = """
                                 SELECT s.id_submenu AS Id, m.nombre_menu AS Menu, s.nombre_submenu AS Submenu 
                                 FROM submenus AS s
                                 INNER JOIN menus AS m ON s.menu_id = m.id_menu""";
            Statement st = conect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);
            while (result.next()) {
                RegistroBD[0] = result.getString(1);
                RegistroBD[1] = result.getString(2);
                RegistroBD[2] = result.getString(3);

                modelo.addRow(RegistroBD);

            }
            tbSubmenus.setModel(modelo);
            tbSubmenus.getColumnModel().getColumn(0).setPreferredWidth(20);
            tbSubmenus.getColumnModel().getColumn(1).setPreferredWidth(60);
            tbSubmenus.getColumnModel().getColumn(2).setPreferredWidth(150);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertarMenu(String nombreMenu) {
        nombreMenu = txtMenu.getText();
        String sql = "INSERT INTO menus (nombre_menu) VALUES (?)";
        try (PreparedStatement ps = conect.prepareStatement(sql)) {
            ps.setString(1, nombreMenu);
            ps.executeUpdate();
            txtMenu.setText("");
            CargarDatosTableMenu("");
            JOptionPane.showMessageDialog(null, "Menú agregado exitosamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar el menú.");
        }
    }

    private void insertarSubmenu(String nombreSubmenu, int menuId) {
        nombreSubmenu = txtSubmenu.getText();

        String sql = "INSERT INTO submenus (nombre_submenu, menu_id) VALUES (?, ?)";
        try (PreparedStatement ps = conect.prepareStatement(sql)) {
            ps.setString(1, nombreSubmenu);
            ps.setInt(2, menuId);
            ps.executeUpdate();
            txtSubmenu.setText("");
            CargarDatosTableSubMenu("");
            JOptionPane.showMessageDialog(null, "Submenú agregado exitosamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar el submenú.");
        }
    }

    private int obtenerMenuId(String menuName) {
        String sql = "SELECT id_menu FROM menus WHERE nombre_menu = ?";
        try (PreparedStatement ps = conect.prepareStatement(sql)) {
            ps.setString(1, menuName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_menu");
            }
            System.out.println(menuName);
        } catch (SQLException e) {
        }
        return -1;
    }

    private void modificarSubmenu(int submenuId, String nuevoNombre) {
        String sql = "UPDATE submenus SET nombre_submenu = ? WHERE id_submenu = ?";
        try (PreparedStatement ps = conect.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);  // Asignar el nuevo nombre del submenú
            ps.setInt(2, submenuId);  // Especificar el ID del submenú a modificar
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Submenú modificado exitosamente.");
                CargarDatosTableSubMenu("");  // Recargar la tabla de submenús
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el submenú a modificar.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar el submenú: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnAddMenu = new javax.swing.JButton();
        txtMenu = new javax.swing.JTextField();
        btnAddSubmenu = new javax.swing.JButton();
        txtSubmenu = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbMenus = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbSubmenus = new javax.swing.JTable();

        setTitle("Menus - Submenus");

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        btnAddMenu.setText("Add Menu");
        btnAddMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMenuActionPerformed(evt);
            }
        });

        btnAddSubmenu.setText("Add Submenu");
        btnAddSubmenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSubmenuActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddMenu)
                    .addComponent(btnAddSubmenu))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtSubmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 171, Short.MAX_VALUE)
                        .addComponent(btnModificar)
                        .addGap(57, 57, 57)))
                .addComponent(btnSalir)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddMenu)
                            .addComponent(txtMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSalir)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(btnModificar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddSubmenu)
                    .addComponent(txtSubmenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        tbMenus.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Id", "Menu"
            }
        ));
        jScrollPane1.setViewportView(tbMenus);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        tbSubmenus.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Menu", "Submenu"
            }
        ));
        jScrollPane2.setViewportView(tbSubmenus);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddMenuActionPerformed
        String menuName = txtMenu.getText();
        insertarMenu(menuName);
    }//GEN-LAST:event_btnAddMenuActionPerformed

    private void btnAddSubmenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSubmenuActionPerformed
        String submenuName = txtSubmenu.getText();
        String menuName = JOptionPane.showInputDialog("Ingrese el nombre del menú al que pertenece el submenú:");
        int menuId = obtenerMenuId(menuName);
        if (menuId != -1) {
            insertarSubmenu(submenuName, menuId);
        } else {
            JOptionPane.showMessageDialog(null, "Menú no encontrado.");
        }
    }//GEN-LAST:event_btnAddSubmenuActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        int fila = tbSubmenus.getSelectedRow();  // Obtener la fila seleccionada
        if (fila >= 0) {
            // Obtener el ID del submenú seleccionado y el nuevo nombre desde la tabla
            int submenuId = Integer.parseInt(tbSubmenus.getValueAt(fila, 0).toString());  // Columna 0 es el ID
            String nombreActual = tbSubmenus.getValueAt(fila, 2).toString();  // Columna 2 es el nombre del submenú

            // Solicitar el nuevo nombre al usuario
            String nuevoNombre = JOptionPane.showInputDialog(null, "Ingrese el nuevo nombre para el submenú:", nombreActual);

            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                modificarSubmenu(submenuId, nuevoNombre);  // Llamar al método para modificar
            } else {
                JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un submenú para modificar.");
        }
    }//GEN-LAST:event_btnModificarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAddMenu;
    public javax.swing.JButton btnAddSubmenu;
    public javax.swing.JButton btnModificar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tbMenus;
    private javax.swing.JTable tbSubmenus;
    private javax.swing.JTextField txtMenu;
    private javax.swing.JTextField txtSubmenu;
    // End of variables declaration//GEN-END:variables

}
