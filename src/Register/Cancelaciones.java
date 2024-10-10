package Register;

import conectar.Conectar;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Cancelaciones extends javax.swing.JInternalFrame {

    public Cancelaciones() {
        initComponents();
        
        MostrarTabla();
    }
    
   
    public void MostrarTabla() {
        Connection connection = null;
        DefaultTableModel modelo = new DefaultTableModel();
        try {
            String[] tituloTabla = {"id", "Empresa", "Fecha", "Nombre", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Email", "Servicio", "Precio", "Status"};
            String[] RegistroBD = new String[13];
            modelo = new DefaultTableModel(null, tituloTabla);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer AS Nombre, "
                    + "customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, "
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, customer.email AS Email, "
                    + "o.servicio AS Servicio, o.precio AS Precio, o.estado AS Status\n"
                    + "FROM orderservice AS o\n"
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness\n"
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer WHERE o.estado = 'Inactivo'\n"
                    + "ORDER BY o.fechaT ASC";

            Conectar.getInstancia().obtenerConexion();
            
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[0] = rs.getString("id");
                RegistroBD[1] = rs.getString("Empresa");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                RegistroBD[2] = dateFormat.format(rs.getDate("Fecha"));
                RegistroBD[3] = rs.getString("Nombre");
                RegistroBD[4] = rs.getString("Direccion");
                RegistroBD[5] = rs.getString("Ciudad");
                RegistroBD[6] = rs.getString("Estado");
                RegistroBD[7] = rs.getString("Zip Code");
                RegistroBD[8] = rs.getString("Celular");
                RegistroBD[9] = rs.getString("Email");
                RegistroBD[10] = rs.getString("Servicio");
                RegistroBD[11] = rs.getString("Precio");
                RegistroBD[12] = rs.getString("Status");
                modelo.addRow(RegistroBD);
            }
            tbCancelaciones.setModel(modelo);
            tbCancelaciones.getColumnModel().getColumn(0).setPreferredWidth(30);
            tbCancelaciones.getColumnModel().getColumn(1).setPreferredWidth(50);
            tbCancelaciones.getColumnModel().getColumn(2).setPreferredWidth(150);
            tbCancelaciones.getColumnModel().getColumn(3).setPreferredWidth(200);
            tbCancelaciones.getColumnModel().getColumn(4).setPreferredWidth(150);
            tbCancelaciones.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbCancelaciones.getColumnModel().getColumn(6).setPreferredWidth(150);
            tbCancelaciones.getColumnModel().getColumn(7).setPreferredWidth(150);
            tbCancelaciones.getColumnModel().getColumn(8).setPreferredWidth(150);
            tbCancelaciones.getColumnModel().getColumn(9).setPreferredWidth(200);
            tbCancelaciones.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbCancelaciones.getColumnModel().getColumn(11).setPreferredWidth(100);
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSalir = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCancelaciones = new javax.swing.JTable();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cancelaciones");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(992, 6, -1, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbCancelaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbCancelaciones);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 15, 1130, 377));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 47, 1160, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbCancelaciones;
    // End of variables declaration//GEN-END:variables
}
