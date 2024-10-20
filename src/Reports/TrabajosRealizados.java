package Reports;

import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class TrabajosRealizados extends javax.swing.JInternalFrame {

    public TrabajosRealizados() {
        initComponents();
        
        MostrarEmpresa(cbxEmpresa);
        AutoCompleteDecorator.decorate(cbxEmpresa);
        MostrarTabla();
        txtSubtotal.setText(Double.toString(subtotal()));
    }

    public double subtotal() {
        double contar = tbVentas.getRowCount();
        double subtotal = 0.00;
        for (int i = 0; i < contar; i++) {

            subtotal = subtotal + Double.parseDouble(tbVentas.getValueAt(i, 12).toString());

        }
        return subtotal;
    }

    public void addCheckBox(int column, JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }

    public boolean IsSelected(int row, int column, JTable table) {
        return table.getValueAt(row, column) != null;
    }

    public void MostrarTabla() {
Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Precio", "Status"};
            String[] RegistroBD = new String[15];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbVentas);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer "
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, "
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, "
                    + "o.precio AS Precio, o.estado AS Status "
                    + "FROM orderservice AS o "
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness "
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer "
                    + "WHERE o.estado = 'Realizado' "
                    + "ORDER BY o.fechaT ASC";
    
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[1] = rs.getString("id");
                RegistroBD[2] = rs.getString("Empresa");
                RegistroBD[3] = rs.getString("Fecha");
                RegistroBD[4] = rs.getString("Nombre");
                RegistroBD[5] = rs.getString("Tamaño");
                RegistroBD[6] = rs.getString("Direccion");
                RegistroBD[7] = rs.getString("Ciudad");
                RegistroBD[8] = rs.getString("Estado");
                RegistroBD[9] = rs.getString("Zip Code");
                RegistroBD[10] = rs.getString("Celular");
                RegistroBD[11] = rs.getString("Servicio");
                RegistroBD[12] = rs.getString("Precio");
                RegistroBD[13] = rs.getString("Status");
                modelo.addRow(RegistroBD);
            }
            tbVentas.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbVentas); // Agregar checkbox a la columna 0 de cada fila
            }
            tbVentas.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbVentas.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbVentas.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbVentas.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbVentas.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbVentas.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbVentas.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(11).setPreferredWidth(150);
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    //Codigo Para buscar dentro de la tabla por medio del txtField Busqueda
    public DefaultTableModel buscarTabla(String buscar) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        DefaultTableModel modelo = new DefaultTableModel();
        try {
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Precio", "Status"};
            String[] RegistroBD = new String[15];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbVentas);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer "
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, "
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, "
                    + "o.precio AS Precio, o.estado AS Status "
                    + "FROM orderservice AS o "
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness "
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer "
                    + "WHERE o.estado = 'Realizado' AND customer.nameCustomer LIKE '%" + buscar + "%' OR customer.address LIKE '%" + buscar + "%'"
                    + "ORDER BY o.fechaT ASC";
            
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[1] = rs.getString("id");
                RegistroBD[2] = rs.getString("Empresa");
                RegistroBD[3] = rs.getString("Fecha");
                RegistroBD[4] = rs.getString("Nombre");
                RegistroBD[5] = rs.getString("Tamaño");
                RegistroBD[6] = rs.getString("Direccion");
                RegistroBD[7] = rs.getString("Ciudad");
                RegistroBD[8] = rs.getString("Estado");
                RegistroBD[9] = rs.getString("Zip Code");
                RegistroBD[10] = rs.getString("Celular");
                RegistroBD[11] = rs.getString("Servicio");
                RegistroBD[12] = rs.getString("Precio");
                RegistroBD[13] = rs.getString("Status");
                modelo.addRow(RegistroBD);
            }
            tbVentas.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbVentas); // Agregar checkbox a la columna 0 de cada fila
            }
            tbVentas.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbVentas.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbVentas.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbVentas.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbVentas.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbVentas.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbVentas.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(11).setPreferredWidth(150);
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
        return modelo;
    }

    private void btnImprimirPDFActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Crear el documento
            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream("documento.pdf"));

            // Abrir el documento para escribir
            documento.open();

            // Agregar contenido al documento
            documento.add(new Paragraph("Contenido del PDF"));

            // Cerrar el documento
            documento.close();

            JOptionPane.showMessageDialog(null, "PDF generado con éxito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar PDF: " + e.getMessage());
        }
    }

    public void SeleccionarVenta(JTable TablaVenta, JTextField empresa, JTextField cliente, JTextField direccion, JTextField subtotal, JTextField tax, JTextField total) {
        try {
            int fila = TablaVenta.getSelectedRow();
            if (fila >= 0) {
                empresa.setText(TablaVenta.getValueAt(fila, 2).toString());
                cliente.setText(TablaVenta.getValueAt(fila, 4).toString());
                direccion.setText(TablaVenta.getValueAt(fila, 6).toString());
                subtotal.setText(TablaVenta.getValueAt(fila, 12).toString());
//                txtTax= Double.parseDouble(txtSubtotal.getText()*0.06);
                // password.setText(TablaUsuario.getValueAt(fila, 3).toString());   
            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Seleccion, Error: ");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tbVentas = new javax.swing.JTable();
        btnBusquedaEmpresa = new javax.swing.JButton();
        txtIdEmpresa = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        dateInicio = new com.toedter.calendar.JDateChooser();
        dateFin = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbxEmpresa = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        cbSeleccionaTodo = new javax.swing.JCheckBox();
        btnBusquedaFechaEmpresa = new javax.swing.JButton();
        btnMostrarTodo = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtEmpresa = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCustomer = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtST = new javax.swing.JTextField();
        txtTax = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        btnPagar = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Trabajos Realizados");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setBackground(new java.awt.Color(153, 153, 255));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Seleccion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbVentas.setRowHeight(23);
        tbVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbVentasMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(tbVentas);

        jPanel12.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 1170, 240));

        btnBusquedaEmpresa.setText("Busqueda Empresa");
        btnBusquedaEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusquedaEmpresaActionPerformed(evt);
            }
        });
        jPanel12.add(btnBusquedaEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 160, 30));
        jPanel12.add(txtIdEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 70, 30));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 80, -1, -1));
        jPanel12.add(dateInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 40, 150, -1));
        jPanel12.add(dateFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 40, 140, -1));

        jLabel1.setText("Inicio");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 20, -1, -1));

        jLabel2.setText("Final");
        jPanel12.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 20, -1, -1));

        jLabel3.setText("Empresa");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        cbxEmpresa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxEmpresaItemStateChanged(evt);
            }
        });
        jPanel12.add(cbxEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 170, -1));

        jLabel4.setText("Busqueda");
        jPanel12.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });
        jPanel12.add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 170, -1));

        cbSeleccionaTodo.setText("Selección");
        cbSeleccionaTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSeleccionaTodoActionPerformed(evt);
            }
        });
        jPanel12.add(cbSeleccionaTodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 120, -1));

        btnBusquedaFechaEmpresa.setText("Busqueda por Fecha-Empresa");
        btnBusquedaFechaEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusquedaFechaEmpresaActionPerformed(evt);
            }
        });
        jPanel12.add(btnBusquedaFechaEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 80, 250, -1));

        btnMostrarTodo.setText("Mostrar Todo");
        btnMostrarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarTodoActionPerformed(evt);
            }
        });
        jPanel12.add(btnMostrarTodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, 170, -1));
        jPanel12.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, 90, 30));

        jLabel15.setText("Cliente");
        jPanel12.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        getContentPane().add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 1190, 372));

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));

        jLabel5.setText("Empresa");

        jLabel6.setText("Cliente");

        jLabel7.setText("Dirección");

        jLabel8.setText("Deuda Cliente");

        jLabel9.setText("Tax");

        jLabel10.setText("Total");

        jLabel11.setText("DeudaTotal");

        jLabel12.setText("Tax");

        jLabel13.setText("Tip");

        jLabel14.setText("Total");

        btnPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        btnPagar.setText("PAGAR");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                            .addComponent(txtEmpresa)
                            .addComponent(txtCustomer))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addGap(21, 21, 21))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtST)
                            .addComponent(txtTax)
                            .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                        .addGap(95, 95, 95)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13)
                                .addComponent(jLabel12)))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSubtotal)
                            .addComponent(jTextField8)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(84, 84, 84))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPagar))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPagar)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(txtST, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(txtTax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 390, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnBusquedaEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusquedaEmpresaActionPerformed
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ID = txtIdEmpresa.getText();
        String ID_buscar = "";
        if (!(ID.equals(""))) {
            ID_buscar = "WHERE o.id_empresa= '" + ID + "'";
        }
        try {
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Precio", "Status"};
            String[] RegistroBD = new String[15];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbVentas);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer "
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, "
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, "
                    + "o.precio AS Precio, o.estado AS Status "
                    + "FROM orderservice AS o "
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness "
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer "
                    + ID_buscar + "AND o.estado = 'Realizado' "
                    + "ORDER BY o.fechaT ASC";
            
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[1] = rs.getString("id");
                RegistroBD[2] = rs.getString("Empresa");
                RegistroBD[3] = rs.getString("Fecha");
                RegistroBD[4] = rs.getString("Nombre");
                RegistroBD[5] = rs.getString("Tamaño");
                RegistroBD[6] = rs.getString("Direccion");
                RegistroBD[7] = rs.getString("Ciudad");
                RegistroBD[8] = rs.getString("Estado");
                RegistroBD[9] = rs.getString("Zip Code");
                RegistroBD[10] = rs.getString("Celular");
                RegistroBD[11] = rs.getString("Servicio");
                RegistroBD[12] = rs.getString("Precio");
                RegistroBD[13] = rs.getString("Status");
                modelo.addRow(RegistroBD);
            }
            tbVentas.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbVentas); // Agregar checkbox a la columna 0 de cada fila
            }
            tbVentas.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbVentas.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbVentas.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbVentas.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbVentas.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbVentas.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbVentas.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(11).setPreferredWidth(150);
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }//GEN-LAST:event_btnBusquedaEmpresaActionPerformed

    private void cbxEmpresaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxEmpresaItemStateChanged
        MostrarCodigoEmpresa(cbxEmpresa, txtIdEmpresa);
    }//GEN-LAST:event_cbxEmpresaItemStateChanged

    private void cbSeleccionaTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSeleccionaTodoActionPerformed
        if (cbSeleccionaTodo.isSelected()) {
            cbSeleccionaTodo.setText("Deseleccionar Todo");
            for (int i = 0; i < tbVentas.getRowCount(); i++) {
                tbVentas.setValueAt(true, i, 0);
            }
        } else {
            cbSeleccionaTodo.setText("Seleccionar Todo");
            for (int i = 0; i < tbVentas.getRowCount(); i++) {
                tbVentas.setValueAt(false, i, 0);
            }
        }

    }//GEN-LAST:event_cbSeleccionaTodoActionPerformed

    private void btnBusquedaFechaEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusquedaFechaEmpresaActionPerformed
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaInicio = dateFormat.format(dateInicio.getDate());
        String fechaFin = dateFormat.format(dateFin.getDate());
        String ID = txtIdEmpresa.getText();
        String ID_buscar = "";
        if (!(ID.equals(""))) {
            ID_buscar = "WHERE o.id_empresa= '" + ID + "'";
        }
        try {
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Precio", "Status"};
            String[] RegistroBD = new String[15];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbVentas);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer "
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, "
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, "
                    + "o.precio AS Precio, o.estado AS Status "
                    + "FROM orderservice AS o "
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness "
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer "
                    + ID_buscar + "AND o.fechaT BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "' AND o.estado = 'Realizado' "
                    + "ORDER BY o.fechaT ASC";
            
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[1] = rs.getString("id");
                RegistroBD[2] = rs.getString("Empresa");
                RegistroBD[3] = rs.getString("Fecha");
                RegistroBD[4] = rs.getString("Nombre");
                RegistroBD[5] = rs.getString("Tamaño");
                RegistroBD[6] = rs.getString("Direccion");
                RegistroBD[7] = rs.getString("Ciudad");
                RegistroBD[8] = rs.getString("Estado");
                RegistroBD[9] = rs.getString("Zip Code");
                RegistroBD[10] = rs.getString("Celular");
                RegistroBD[11] = rs.getString("Servicio");
                RegistroBD[12] = rs.getString("Precio");
                RegistroBD[13] = rs.getString("Status");
                modelo.addRow(RegistroBD);
            }
            tbVentas.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbVentas); // Agregar checkbox a la columna 0 de cada fila
            }
            tbVentas.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbVentas.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbVentas.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbVentas.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbVentas.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbVentas.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbVentas.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbVentas.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbVentas.getColumnModel().getColumn(11).setPreferredWidth(150);
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }finally{
          Conectar.getInstancia().devolverConexion(connection);
    }
    }//GEN-LAST:event_btnBusquedaFechaEmpresaActionPerformed

    private void btnMostrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodoActionPerformed
        MostrarTabla();
    }//GEN-LAST:event_btnMostrarTodoActionPerformed

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        buscarTabla(txtBusqueda.getText());
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void tbVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbVentasMouseClicked
        int seleccionar = tbVentas.rowAtPoint(evt.getPoint());
        txtId.setText(String.valueOf(tbVentas.getValueAt(seleccionar, 1)));
        txtEmpresa.setText(tbVentas.getValueAt(seleccionar, 2).toString());
        txtCustomer.setText(tbVentas.getValueAt(seleccionar, 4).toString());
        txtDireccion.setText(tbVentas.getValueAt(seleccionar, 6).toString());
        txtST.setText(tbVentas.getValueAt(seleccionar, 12).toString());
    }//GEN-LAST:event_tbVentasMouseClicked

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
    double subtotal = 0.0;
    double impuesto = 0.06; // Impuesto del 6%
    double total = 0.0;
    double tip = 0.0; // Propina inicialmente en 0
    String Reportes = "";
    JTextArea area = new JTextArea();
    
    if (Seleccionados(0)) {
        for (int i = 0; i < tbVentas.getRowCount(); i++) {
            Object value = tbVentas.getValueAt(i, 0);
            if (value instanceof Boolean) {
                boolean sel = (boolean) value;
                if (sel) {
                    double precio = Double.parseDouble(tbVentas.getValueAt(i, 12).toString());
                    subtotal += precio;
                    Reportes += "Empresa : " + tbVentas.getValueAt(i, 2) + " ; Cliente : " + tbVentas.getValueAt(i, 4)
                            + " precio : " + String.format("%.2f", precio) + "\n";
                }
            }
        }
        
        // Calcular impuesto
        double impuestoTotal = subtotal * impuesto;
        
        // Calcular total sin propina
        total = subtotal + impuestoTotal;
        
        // Mostrar subtotal, impuesto y total antes de propina
        area.setText("Subtotal: " + String.format("%.2f", subtotal) + "\nImpuesto (6%): " + String.format("%.2f", impuestoTotal) + "\nTotal sin propina: " + String.format("%.2f", total) + "\n\n" + Reportes);
        
        JOptionPane.showMessageDialog(this, area, "Información Detallada de Ventas", JOptionPane.INFORMATION_MESSAGE);
        
        // Agregar campo para ingresar propina
        String input = JOptionPane.showInputDialog(this, "Ingrese el monto de propina a añadir:", "Propina", JOptionPane.QUESTION_MESSAGE);
        try {
            tip = Double.parseDouble(input);
            total += tip;
            // Mostrar total final con propina
            JOptionPane.showMessageDialog(this, "Subtotal: " + String.format("%.2f", subtotal) + "\nImpuesto (6%): " + String.format("%.2f", impuestoTotal) + "\nTotal + Propina: " + String.format("%.2f", total), "Total Detallado con Propina", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            // Manejar error de ingreso inválido
            JOptionPane.showMessageDialog(this, "Error al ingresar la propina. Se aplicará propina de $0.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    } else {
        JOptionPane.showMessageDialog(null, "Antes de obtener los datos, debe de seleccionar por lo menos un checkbox.",
                "Mensaje", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_btnPagarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnBusquedaEmpresa;
    public javax.swing.JButton btnBusquedaFechaEmpresa;
    public javax.swing.JButton btnMostrarTodo;
    public javax.swing.JButton btnPagar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JCheckBox cbSeleccionaTodo;
    private javax.swing.JComboBox<String> cbxEmpresa;
    private com.toedter.calendar.JDateChooser dateFin;
    private com.toedter.calendar.JDateChooser dateInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    public javax.swing.JTable tbVentas;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtEmpresa;
    private javax.swing.JTextField txtId;
    public javax.swing.JTextField txtIdEmpresa;
    private javax.swing.JTextField txtST;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTax;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    public void MostrarEmpresa(JComboBox cbxEmpresa) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        String sql = "";
        sql = "select * from bussiness";
        Statement st;
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxEmpresa.removeAllItems();
            while (rs.next()) {
                cbxEmpresa.addItem(rs.getString("nameBusiness"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar en Combo " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoEmpresa(JComboBox cbxEmpresa, JTextField idBusiness) {
     Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }        
        String consuta = "select bussiness.idBusiness from bussiness where bussiness.nameBusiness=?";
        try {
            CallableStatement cs = connection.prepareCall(consuta);
            cs.setString(1, cbxEmpresa.getSelectedItem().toString());
            cs.execute();
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                idBusiness.setText(rs.getString("idBusiness"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    private boolean Seleccionados(int pos) {
        int contador = 0;
        boolean bandera = true;
        for (int i = 0; i < tbVentas.getRowCount(); i++) {
            Object value = tbVentas.getValueAt(i, pos);
            if (value instanceof Boolean) {
                boolean seleccion = (boolean) value;
                if (seleccion) {
                    contador++;
                }
            }
        }
        if (contador == 0) {
            bandera = false;
        }
        return bandera;
    }
}
