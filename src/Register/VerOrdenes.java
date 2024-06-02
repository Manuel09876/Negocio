package Register;

import Bases.RenderTabla;
import conectar.Conectar;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class VerOrdenes extends javax.swing.JInternalFrame {

    Conectar con = new Conectar();
    Connection connect = con.getConexion();

    public VerOrdenes() {
        initComponents();
        //Inicio();
        AutoCompleteDecorator.decorate(cbxEmpresa);
        MostrarEmpresa(cbxEmpresa);
        MostrarTabla();
    }

//Mostrar datos en la Tabla
    public void MostrarTabla() {
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
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer\n"
                    + "ORDER BY o.fechaT ASC";
            Statement st = connect.createStatement();
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
            TablaRegistro.setModel(modelo);
            TablaRegistro.getColumnModel().getColumn(0).setPreferredWidth(30);
            TablaRegistro.getColumnModel().getColumn(1).setPreferredWidth(50);
            TablaRegistro.getColumnModel().getColumn(2).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(3).setPreferredWidth(200);
            TablaRegistro.getColumnModel().getColumn(4).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(5).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(6).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(7).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(8).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(9).setPreferredWidth(200);
            TablaRegistro.getColumnModel().getColumn(10).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(11).setPreferredWidth(100);
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }
    }
    
    //Codigo Para buscar dentro de la tabla por medio del txtField Busqueda
    public DefaultTableModel buscarTabla(String buscar) {
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
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer WHERE customer.nameCustomer LIKE '%"+buscar+"%' OR customer.address LIKE '%"+buscar+"%'\n"
                    + "ORDER BY o.fechaT ASC";
            Statement st = connect.createStatement();
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
            TablaRegistro.setModel(modelo);
            TablaRegistro.getColumnModel().getColumn(0).setPreferredWidth(30);
            TablaRegistro.getColumnModel().getColumn(1).setPreferredWidth(50);
            TablaRegistro.getColumnModel().getColumn(2).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(3).setPreferredWidth(200);
            TablaRegistro.getColumnModel().getColumn(4).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(5).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(6).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(7).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(8).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(9).setPreferredWidth(200);
            TablaRegistro.getColumnModel().getColumn(10).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(11).setPreferredWidth(100);
            
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }
        return modelo;
    }
   
    
//Codigo para Cambiar el Estado
    public boolean accion(String estado, int id) {
        String sql = "UPDATE orderservice SET estado = ? WHERE id = ?";
        try {
            connect = con.getConexion();
            PreparedStatement ps;
            ps = connect.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jmEliminar = new javax.swing.JMenuItem();
        jmReingresar = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dateInicio = new com.toedter.calendar.JDateChooser();
        dateFin = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        cbxEmpresa = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        txtIdBusiness = new javax.swing.JTextField();
        btnBuscarFechaEmpresa = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        btnMostrarTodo = new javax.swing.JButton();
        btnSoloEmpresa = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaRegistro = new javax.swing.JTable();

        jmEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete.png"))); // NOI18N
        jmEliminar.setText("Eliminar");
        jmEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmEliminarActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jmEliminar);

        jmReingresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exchange.png"))); // NOI18N
        jmReingresar.setText("Reingresar");
        jmReingresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmReingresarActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jmReingresar);

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Ver Ordenes");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Fecha de Inicio");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 12, -1, -1));

        jLabel2.setText("Fecha Final");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 12, -1, -1));

        dateInicio.setDateFormatString("yyyy-MM-dd");
        jPanel1.add(dateInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 6, 161, -1));

        dateFin.setDateFormatString("yyyy-MM-dd");
        jPanel1.add(dateFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(411, 6, 171, -1));

        jLabel3.setText("Empresa");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 57, -1, -1));

        cbxEmpresa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxEmpresaItemStateChanged(evt);
            }
        });
        cbxEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxEmpresaActionPerformed(evt);
            }
        });
        jPanel1.add(cbxEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 54, 287, -1));

        jLabel4.setText("Apellido y Nombre");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 85, -1, -1));

        jLabel5.setText("o Dirección");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarKeyTyped(evt);
            }
        });
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 287, -1));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1162, 64, -1, -1));
        jPanel1.add(txtIdBusiness, new org.netbeans.lib.awtextra.AbsoluteConstraints(441, 54, 83, -1));

        btnBuscarFechaEmpresa.setText("Buscar Por Fecha y Empresa");
        btnBuscarFechaEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarFechaEmpresaActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscarFechaEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, -1, -1));
        jPanel1.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(894, 80, 90, 30));

        btnMostrarTodo.setText("Mostrar Todo");
        btnMostrarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarTodoActionPerformed(evt);
            }
        });
        jPanel1.add(btnMostrarTodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 90, -1, -1));

        btnSoloEmpresa.setText("Buscar solo por Empresa");
        btnSoloEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSoloEmpresaActionPerformed(evt);
            }
        });
        jPanel1.add(btnSoloEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 50, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1310, -1));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaRegistro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TablaRegistro.setComponentPopupMenu(jPopupMenu1);
        TablaRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaRegistroMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TablaRegistro);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 32, 1300, 280));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 1310, 340));

        pack();
    }// </editor-fold>//GEN-END:initComponents
//Salir
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed
//Mostrar Empresas en el comboBox
    private void cbxEmpresaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxEmpresaItemStateChanged
        MostrarCodigoEmpresa(cbxEmpresa, txtIdBusiness);
    }//GEN-LAST:event_cbxEmpresaItemStateChanged

    private void cbxEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxEmpresaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxEmpresaActionPerformed
//Filtramos Fecha y Empresa con Boton Buscar
    private void btnBuscarFechaEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarFechaEmpresaActionPerformed
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaInicio = dateFormat.format(dateInicio.getDate());
        String fechaFin = dateFormat.format(dateFin.getDate());
        String ID = txtIdBusiness.getText();
        String ID_buscar = "";
        if (!(ID.equals(""))) {
            ID_buscar = "WHERE o.id_empresa= '" + ID + "'";
        }
        try {
            String[] tituloTabla = {"id", "Empresa", "Fecha", "Nombre", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Email", "Servicio", "Precio", "Status"};
            String[] RegistroBD = new String[13];
            modelo = new DefaultTableModel(null, tituloTabla);
            String query = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, \n"
                    + "customer.nameCustomer AS Nombre, customer.address AS Direccion, \n"
                    + "customer.city AS Ciudad, customer.state AS Estado, customer.zipCode AS 'Zip Code', \n"
                    + "customer.phoneNumber AS Celular, customer.email AS Email, o.servicio AS Servicio, o.precio AS Precio, "
                    + "o.estado AS Status \n"
                    + "FROM orderservice AS o \n"
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness \n"
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer \n"
                    + ID_buscar + "AND o.fechaT BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "' \n"
                    + "ORDER BY o.fechaT ASC;";
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                RegistroBD[0] = rs.getString("id");
                RegistroBD[1] = rs.getString("Empresa");
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
            TablaRegistro.setModel(modelo);
            TablaRegistro.getColumnModel().getColumn(0).setPreferredWidth(30);
            TablaRegistro.getColumnModel().getColumn(1).setPreferredWidth(50);
            TablaRegistro.getColumnModel().getColumn(2).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(3).setPreferredWidth(200);
            TablaRegistro.getColumnModel().getColumn(4).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(5).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(6).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(7).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(8).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(9).setPreferredWidth(200);
            TablaRegistro.getColumnModel().getColumn(10).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(11).setPreferredWidth(100);
        } catch (SQLException e) {
            System.out.println("Error " + e.toString());
        }
        
    }//GEN-LAST:event_btnBuscarFechaEmpresaActionPerformed

    private void txtBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyTyped
       
    }//GEN-LAST:event_txtBuscarKeyTyped

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        buscarTabla(txtBuscar.getText());
    }//GEN-LAST:event_txtBuscarKeyReleased
//Poner en Inactivo un trabajo sin borrarlo de la base de datos
    private void jmEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmEliminarActionPerformed
        int fila = TablaRegistro.getSelectedRow();
        int id = Integer.parseInt(txtId.getText());
        if (accion("Inactivo", id)) {
            MostrarTabla();
            JOptionPane.showMessageDialog(null, "Eliminado");
        }else{
            JOptionPane.showMessageDialog(null, "Error al eliminar");
        }
    }//GEN-LAST:event_jmEliminarActionPerformed

//Seleccion de la casilla seleccion o checkBox
    private void TablaRegistroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaRegistroMouseClicked
        int seleccionar = TablaRegistro.rowAtPoint(evt.getPoint());
        txtId.setText(String.valueOf(TablaRegistro.getValueAt(seleccionar, 0)));
    }//GEN-LAST:event_TablaRegistroMouseClicked
//Reingresar o poner en Activo un trabajo que este Inactivo en la Basede Datos
    private void jmReingresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmReingresarActionPerformed
        int fila = TablaRegistro.getSelectedRow();
        int id = Integer.parseInt(txtId.getText());
        if (accion("Activo", id)) {
            MostrarTabla();
            JOptionPane.showMessageDialog(null, "Reingresado");
        }else{
            JOptionPane.showMessageDialog(null, "Error al reingresar");
        }
    }//GEN-LAST:event_jmReingresarActionPerformed
//Boton para volver a Mostrar la Tabla Actualizada
    private void btnMostrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodoActionPerformed
        MostrarTabla();
    }//GEN-LAST:event_btnMostrarTodoActionPerformed
//Boton para la busqueda solo por Empresa
    private void btnSoloEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSoloEmpresaActionPerformed
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ID = txtIdBusiness.getText();
        String ID_buscar = "";
        if (!(ID.equals(""))) {
            ID_buscar = "WHERE o.id_empresa= '" + ID + "'";
        }
        try {
            String[] tituloTabla = {"id", "Empresa", "Fecha", "Nombre", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Email", "Servicio", "Precio", "Status"};
            String[] RegistroBD = new String[13];
            modelo = new DefaultTableModel(null, tituloTabla);
            String query = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, \n"
                    + "customer.nameCustomer AS Nombre, customer.address AS Direccion, \n"
                    + "customer.city AS Ciudad, customer.state AS Estado, customer.zipCode AS 'Zip Code', \n"
                    + "customer.phoneNumber AS Celular, customer.email AS Email, o.servicio AS Servicio, o.precio AS Precio, "
                    + "o.estado AS Status \n"
                    + "FROM orderservice AS o \n"
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness \n"
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer " + ID_buscar + " ORDER BY o.fechaT ASC;";
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                RegistroBD[0] = rs.getString("id");
                RegistroBD[1] = rs.getString("Empresa"); 
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
            TablaRegistro.setModel(modelo);
            TablaRegistro.getColumnModel().getColumn(0).setPreferredWidth(30);
            TablaRegistro.getColumnModel().getColumn(1).setPreferredWidth(50);
            TablaRegistro.getColumnModel().getColumn(2).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(3).setPreferredWidth(200);
            TablaRegistro.getColumnModel().getColumn(4).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(5).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(6).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(7).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(8).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(9).setPreferredWidth(200);
            TablaRegistro.getColumnModel().getColumn(10).setPreferredWidth(150);
            TablaRegistro.getColumnModel().getColumn(11).setPreferredWidth(100);
        } catch (SQLException e) {
            System.out.println("Error " + e.toString());
        }
    }//GEN-LAST:event_btnSoloEmpresaActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaRegistro;
    private javax.swing.JButton btnBuscarFechaEmpresa;
    private javax.swing.JButton btnMostrarTodo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSoloEmpresa;
    private javax.swing.JComboBox<String> cbxEmpresa;
    private com.toedter.calendar.JDateChooser dateFin;
    private com.toedter.calendar.JDateChooser dateInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem jmEliminar;
    private javax.swing.JMenuItem jmReingresar;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdBusiness;
    // End of variables declaration//GEN-END:variables

    public void MostrarEmpresa(JComboBox cbxEmpresa) {
        String sql = "";
        sql = "select * from bussiness";
        Statement st;
        try {
            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxEmpresa.removeAllItems();
            while (rs.next()) {
                cbxEmpresa.addItem(rs.getString("nameBusiness"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar en Combo " + e.toString());
        }
    }

    public void MostrarCodigoEmpresa(JComboBox cbxEmpresa, JTextField idBusiness) {
        String consuta = "select bussiness.idBusiness from bussiness where bussiness.nameBusiness=?";
        try {
            CallableStatement cs = con.getConexion().prepareCall(consuta);
            cs.setString(1, cbxEmpresa.getSelectedItem().toString());
            cs.execute();
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                idBusiness.setText(rs.getString("idBusiness"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }
}
