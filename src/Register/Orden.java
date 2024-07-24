package Register;

import Bases.Detalle;
import Bases.OrdenServicio;
import Bases.Venta;
import com.toedter.calendar.JDateChooser;
import java.sql.ResultSetMetaData;
import conectar.Conectar;
import java.awt.HeadlessException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Orden extends javax.swing.JInternalFrame {

    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel tmp;

    Conectar con = new Conectar();
    Connection connect = con.getConexion();
    Venta v = new Venta();
    Detalle Dv = new Detalle();
    int item;
    double TotalPagar = 0.00;

    // Variables temporales para almacenar los datos
    private String tempFrecuenciaStr;
    private String tempServicioStr;
    private String tempPrecioStr;
    private String tempNotaEmpresa;

    public Orden() {

        initComponents();
        AutoCompleteDecorator.decorate(cbxBusiness);
        AutoCompleteDecorator.decorate(cbxCustomer);
        AutoCompleteDecorator.decorate(cbxTrabajador);
        MostrarEmpresa(cbxBusiness);
        MostrarCiente(cbxCustomer);
        MostrarTrabajador(cbxTrabajador);
        txtIdBusiness.setVisible(false);
        txtIdCustomer.setVisible(false);
        txtIdTrabajador.setVisible(false);
        txtIdNC.setVisible(false);

    }

// Selecciona los datos a insertar a la Tabla
    public void Seleccionar(JTable tbPrecios, JComboBox Business, JTextField Servicio, JTextField PrecioPorServicio) {
        try {
            int fila = tbPrecios.getSelectedRow();
            if (fila >= 0) {
                Business.setSelectedItem(tbPrecios.getValueAt(fila, 0).toString());
                Servicio.setText(tbPrecios.getValueAt(fila, 1).toString());
                PrecioPorServicio.setText(tbPrecios.getValueAt(fila, 2).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error en la selección, error " + e.toString());
        }
    }

//Calculo Periodo; Cantidad de servicios
    public void CalcularFecha(JDateChooser DateInicio, JDateChooser DateFinal) {
        if (DateInicio.getDate() != null && DateFinal.getDate() != null) {
            Calendar inicio = DateInicio.getCalendar();
            Calendar termino = DateFinal.getCalendar();
            int dias = 0;  // Cambiado de -1 a 0 para incluir el primer día en el conteo
            while (!inicio.after(termino)) {
                dias++;
                inicio.add(Calendar.DATE, 1);
            }
            int n = dias / Integer.parseInt(txtEntreServicios.getText());
            if (dias % Integer.parseInt(txtEntreServicios.getText()) != 0) {
                n++;  // Incrementar si hay un resto, para incluir el último servicio
            }
            txtCantServicios.setText("" + n);
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione la fechas", "", JOptionPane.ERROR_MESSAGE);
        }
    }

//Calculo del SubTotal
    public void CalculoSubTotal(JTextField Servicios, JTextField Precio) {
        int cantidad;
        double precio;
        Double SubTotal;

        cantidad = Integer.parseInt(txtCantServicios.getText());
        precio = Double.parseDouble(txtPrecioPorServicio.getText());

        SubTotal = cantidad * precio;

        txtSubtotal.setText(String.valueOf(SubTotal));

    }

//Calcula el Total para que se muestre en el Label
    private void calcularTotal(JTable tabla, JLabel totalPagar) {      //NUEVA VENTA CALCULOS
        double total = 0.00;
        int numFila = tabla.getRowCount();
        for (int i = 0; i < numFila; i++) {
            total = total + Double.parseDouble(String.valueOf(tabla.getValueAt(i, 4)));
        }
        totalPagar.setText("" + total);
    }

    //Agregar Servicios para la venta
    private void agregarTemp(int cant, String desc, double precio, int id, JTable tabla, JTextField codigo) {
        if (cant > 0) {
            tmp = (DefaultTableModel) tabla.getModel();
            ArrayList lista = new ArrayList();
            int item = 1;
            lista.add(item);
            lista.add(id);
            lista.add(desc);
            lista.add(cant);
            lista.add(precio);
            lista.add(cant * precio);
            Object[] obj = new Object[5];
            obj[0] = lista.get(1);
            obj[1] = lista.get(2);
            obj[2] = lista.get(3);
            obj[3] = lista.get(4);
            obj[4] = lista.get(5);
            tmp.addRow(obj);
            tabla.setModel(tmp);
            codigo.requestFocus();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        TableOrdenDeServicio = new javax.swing.JTable();
        jLabel43 = new javax.swing.JLabel();
        txtCantServicios = new javax.swing.JTextField();
        txtPrecioPorServicio = new javax.swing.JTextField();
        txtSubtotal = new javax.swing.JTextField();
        btnGenerarCompra = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        cbxCustomer = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();
        JLabelTotal = new javax.swing.JLabel();
        txtIdNC = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbxBusiness = new javax.swing.JComboBox<>();
        txtIdBusiness = new javax.swing.JTextField();
        txtIdCustomer = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNotaE = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEntreServicios = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        DateInicio = new com.toedter.calendar.JDateChooser();
        DateFinal = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtArea = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbPrecios = new javax.swing.JTable();
        txtServicios = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnLista = new javax.swing.JButton();
        btnServicios = new javax.swing.JButton();
        btnMTP = new javax.swing.JButton();
        btnTotal = new javax.swing.JButton();
        btnAdicionar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cbxTrabajador = new javax.swing.JComboBox<>();
        txtIdTrabajador = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtComisión = new javax.swing.JTextField();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Orden de Servicio");
        setPreferredSize(new java.awt.Dimension(1500, 596));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        jButton2.setText("Salir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, -1));

        jPanel16.setBackground(new java.awt.Color(153, 153, 255));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableOrdenDeServicio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descripción", "Cantidad de Servicios", "Precio", "Total", "Frecuencia"
            }
        ));
        TableOrdenDeServicio.setRowHeight(23);
        jScrollPane12.setViewportView(TableOrdenDeServicio);

        jPanel16.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 760, 230));

        jLabel43.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel43.setText("Precio por Servicio");
        jPanel16.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 10, -1, -1));
        jPanel16.add(txtCantServicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 40, 70, 30));
        jPanel16.add(txtPrecioPorServicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 40, 70, 30));
        jPanel16.add(txtSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 40, 70, 30));

        btnGenerarCompra.setBackground(new java.awt.Color(204, 204, 204));
        btnGenerarCompra.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnGenerarCompra.setText("Generar");
        btnGenerarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarCompraActionPerformed(evt);
            }
        });
        jPanel16.add(btnGenerarCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 410, 90, 30));

        jLabel46.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel46.setText("Cliente");
        jPanel16.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        cbxCustomer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCustomerItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 200, -1));

        jLabel47.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel47.setText("Total Pagar");
        jPanel16.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 350, -1, -1));

        JLabelTotal.setText("-----------");
        jPanel16.add(JLabelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 360, 130, -1));
        jPanel16.add(txtIdNC, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 10, 70, 30));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setText("Empresa");
        jPanel16.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        cbxBusiness.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxBusinessItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxBusiness, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 200, -1));
        jPanel16.add(txtIdBusiness, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 80, -1));
        jPanel16.add(txtIdCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, 80, -1));

        txtNotaE.setColumns(20);
        txtNotaE.setRows(5);
        jScrollPane1.setViewportView(txtNotaE);

        jPanel16.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 420, 180, 60));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Nota Empresa");
        jPanel16.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 400, -1, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Tiempo entre servicios");
        jPanel16.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, -1, -1));
        jPanel16.add(txtEntreServicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 420, 110, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("dias");
        jPanel16.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 420, -1, -1));

        DateInicio.setDateFormatString("yyyy/MM/dd");
        jPanel16.add(DateInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 380, 130, -1));

        DateFinal.setDateFormatString("yyyy/MM/dd");
        jPanel16.add(DateFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 380, 140, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setText("F.Inicio");
        jPanel16.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, -1, -1));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("F.Termino");
        jPanel16.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 380, -1, -1));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setText("id Orden de Servicio");
        jPanel16.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 20, -1, -1));
        jPanel16.add(txtArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 460, 110, -1));

        tbPrecios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Empresa", "Servicio", "Precio"
            }
        ));
        tbPrecios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPreciosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbPrecios);
        if (tbPrecios.getColumnModel().getColumnCount() > 0) {
            tbPrecios.getColumnModel().getColumn(0).setPreferredWidth(30);
            tbPrecios.getColumnModel().getColumn(1).setPreferredWidth(200);
            tbPrecios.getColumnModel().getColumn(2).setPreferredWidth(30);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 90, 300, 370));
        jPanel16.add(txtServicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 150, 30));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setText("Servicio");
        jPanel16.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, -1, -1));

        btnLista.setBackground(new java.awt.Color(204, 204, 204));
        btnLista.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        btnLista.setText("Lista");
        btnLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListaActionPerformed(evt);
            }
        });
        jPanel16.add(btnLista, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 50, -1, -1));

        btnServicios.setBackground(new java.awt.Color(204, 204, 204));
        btnServicios.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnServicios.setText("Servicios");
        btnServicios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiciosActionPerformed(evt);
            }
        });
        jPanel16.add(btnServicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, -1, -1));

        btnMTP.setBackground(new java.awt.Color(204, 204, 204));
        btnMTP.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnMTP.setText("Mostrar Tamaño Propiedad");
        btnMTP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMTPActionPerformed(evt);
            }
        });
        jPanel16.add(btnMTP, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, -1, -1));

        btnTotal.setBackground(new java.awt.Color(204, 204, 204));
        btnTotal.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnTotal.setText("Total");
        btnTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTotalActionPerformed(evt);
            }
        });
        jPanel16.add(btnTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, -1, -1));

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo_1.png"))); // NOI18N
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });
        jPanel16.add(btnAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 50, -1, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel16.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 50, -1, -1));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("Trabajador");
        jPanel16.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, -1, -1));

        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 330, 200, -1));
        jPanel16.add(txtIdTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 330, 80, -1));

        jLabel8.setText("Comisión");
        jPanel16.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 330, -1, -1));
        jPanel16.add(txtComisión, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 330, 90, -1));

        getContentPane().add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 1090, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cbxBusinessItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxBusinessItemStateChanged
        MostrarCodigoEmpresa(cbxBusiness, txtIdBusiness);
    }//GEN-LAST:event_cbxBusinessItemStateChanged

    private void cbxCustomerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCustomerItemStateChanged
        MostrarCodigoCliente(cbxCustomer, txtIdCustomer);
    }//GEN-LAST:event_cbxCustomerItemStateChanged
//Mostrar Lista de Precios por Empresa
    private void btnListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListaActionPerformed
        String ID = txtIdBusiness.getText();
        String ID_buscar = "";

        if (!(ID.equals(""))) {
            ID_buscar = "WHERE services.id_empresa= '" + ID + "'";
        }
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            String[] tituloTabla = {"Empresa", "Servicio", "Precio"};
            String[] RegistroBD = new String[3];
            modelo = new DefaultTableModel(null, tituloTabla);
            tbPrecios.setModel(modelo);
            String sql = "SELECT bussiness.nameBusiness AS Empresa, services.servicio, services.precio FROM services INNER JOIN bussiness ON services.id_empresa=bussiness.idBusiness " + ID_buscar;
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[0] = rs.getString("Empresa");
                RegistroBD[1] = rs.getString("servicio");
                RegistroBD[2] = rs.getString("precio");
                modelo.addRow(RegistroBD);
            }
            tbPrecios.setModel(modelo);
            tbPrecios.getColumnModel().getColumn(0).setPreferredWidth(100);
            tbPrecios.getColumnModel().getColumn(1).setPreferredWidth(150);
            tbPrecios.getColumnModel().getColumn(2).setPreferredWidth(50);
        } catch (SQLException e) {
            System.out.println("Error" + e.toString());
        }
    }//GEN-LAST:event_btnListaActionPerformed
//Mostrar Numero de servicios por periodo
    private void btnServiciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiciosActionPerformed
        CalcularFecha(DateInicio, DateFinal);
    }//GEN-LAST:event_btnServiciosActionPerformed
//Mostrar Tamaño de la Propiedad
    private void btnMTPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMTPActionPerformed
        String ID = txtIdCustomer.getText();
        String ID_buscar = "";

        if (!(ID.equals(""))) {
            ID_buscar = "WHERE idCustomer= '" + ID + "'";
        }
        try {
            String sql = "SELECT * FROM customer " + ID_buscar;
            Statement st;
            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                txtArea.setText(rs.getString("area"));
            }
        } catch (SQLException e) {
            System.out.println("Error al Ingresar Tamaño" + e.toString());
        }
    }//GEN-LAST:event_btnMTPActionPerformed

    private void tbPreciosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPreciosMouseClicked
        Seleccionar(tbPrecios, cbxBusiness, txtServicios, txtPrecioPorServicio);
    }//GEN-LAST:event_tbPreciosMouseClicked
//Calculo del SubTotal
    private void btnTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTotalActionPerformed
        CalculoSubTotal(txtCantServicios, txtPrecioPorServicio);
    }//GEN-LAST:event_btnTotalActionPerformed
//Adicionar item a la tabla para su venta
    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        try {
            String cantidadStr = txtCantServicios.getText().trim();
            String servicioStr = txtServicios.getText().trim();
            String precioStr = txtPrecioPorServicio.getText().trim();
            String entreServiciosStr = txtEntreServicios.getText().trim();

            // Verificar que las entradas no estén vacías
            if (cantidadStr.isEmpty() || servicioStr.isEmpty() || precioStr.isEmpty() || entreServiciosStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos antes de adicionar.", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int Servicios = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);
            double total = Servicios * precio;

            item = item + 1;
            modelo = (DefaultTableModel) TableOrdenDeServicio.getModel();
            for (int i = 0; i < TableOrdenDeServicio.getRowCount(); i++) {
                if (TableOrdenDeServicio.getValueAt(i, 0).equals(servicioStr)) {
                    JOptionPane.showMessageDialog(null, "El Servicio ya está ingresado");
                    return;
                }
            }
            ArrayList<Object> lista = new ArrayList<>();
            lista.add(item);
            lista.add(servicioStr);
            lista.add(Servicios);
            lista.add(precio);
            lista.add(total);
            lista.add(entreServiciosStr);  // Añadir frecuencia
            Object[] O = new Object[5];
            O[0] = lista.get(1);
            O[1] = lista.get(2);
            O[2] = lista.get(3);
            O[3] = lista.get(4);
            O[4] = lista.get(5);  // Añadir frecuencia
            modelo.addRow(O);
            TableOrdenDeServicio.setModel(modelo);
            TotalPagar();

            // Guardar datos en variables temporales antes de limpiar los campos
            tempFrecuenciaStr = entreServiciosStr;
            tempServicioStr = servicioStr;
            tempPrecioStr = precioStr;
            tempNotaEmpresa = txtNotaE.getText().trim();

            // Limpiar los campos después de adicionar
            txtServicios.setText("");
            txtPrecioPorServicio.setText("");
            txtEntreServicios.setText("");
            txtCantServicios.setText("");
            txtSubtotal.setText("");
            txtNotaE.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en el formato de los datos: " + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al adicionar el servicio: " + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAdicionarActionPerformed
//Eliminar item de la tabla venta
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        modelo = (DefaultTableModel) TableOrdenDeServicio.getModel();
        modelo.removeRow(TableOrdenDeServicio.getSelectedRow());
        TotalPagar();
        txtEntreServicios.requestFocus();

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged
//Registra venta a la base de datos
    private void btnGenerarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarCompraActionPerformed
        if (verificarCamposCompletos()) {
            System.out.println("Todos los campos están completos. Procediendo a registrar la orden.");
            RegistrarVenta();
            registrarOrden();

            // Limpiar la tabla después de generar la compra
            DefaultTableModel model = (DefaultTableModel) TableOrdenDeServicio.getModel();
            model.setRowCount(0);

            // Limpiar los campos
            txtServicios.setText("");
            txtPrecioPorServicio.setText("");
            txtEntreServicios.setText("");
            txtCantServicios.setText("");
            txtSubtotal.setText("");
            txtNotaE.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos antes de registrar la orden.", "", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGenerarCompraActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser DateFinal;
    private com.toedter.calendar.JDateChooser DateInicio;
    public javax.swing.JLabel JLabelTotal;
    public javax.swing.JTable TableOrdenDeServicio;
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGenerarCompra;
    private javax.swing.JButton btnLista;
    private javax.swing.JButton btnMTP;
    private javax.swing.JButton btnServicios;
    private javax.swing.JButton btnTotal;
    private javax.swing.JComboBox<String> cbxBusiness;
    public javax.swing.JComboBox<Object> cbxCustomer;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tbPrecios;
    private javax.swing.JTextField txtArea;
    public javax.swing.JTextField txtCantServicios;
    private javax.swing.JTextField txtComisión;
    private javax.swing.JTextField txtEntreServicios;
    private javax.swing.JTextField txtIdBusiness;
    private javax.swing.JTextField txtIdCustomer;
    public javax.swing.JTextField txtIdNC;
    private javax.swing.JTextField txtIdTrabajador;
    private javax.swing.JTextArea txtNotaE;
    public javax.swing.JTextField txtPrecioPorServicio;
    private javax.swing.JTextField txtServicios;
    public javax.swing.JTextField txtSubtotal;
    // End of variables declaration//GEN-END:variables

//LLenar el ComboBox de la Empresa
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

//LLana el ComboBox Cliente
    public void MostrarCiente(JComboBox cbxCliente) {

        String sql = "";
        sql = "SELECT nameCustomer AS nombre FROM customer;";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxCliente.removeAllItems();

            while (rs.next()) {

                cbxCliente.addItem(rs.getString("nombre"));

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Combo " + e.toString());
        }
    }

    public void MostrarCodigoCliente(JComboBox cbxCliente, JTextField idCustomer) {

        String consuta = "select customer.idCustomer from customer where customer.nameCustomer=?";

        try {
            CallableStatement cs = con.getConexion().prepareCall(consuta);
            cs.setString(1, cbxCliente.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                idCustomer.setText(rs.getString("idCustomer"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

//LLena el ComboBox Trabajador como vendedor para posterior comision
    public void MostrarTrabajador(JComboBox comboTrabajador) {

        String sql = "";
        sql = "select * from worker";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            comboTrabajador.removeAllItems();

            while (rs.next()) {

                comboTrabajador.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoTrabajador(JComboBox trabajador, JTextField IdTrabajador) {

        String consuta = "select worker.idWorker from worker where worker.nombre=?";

        try {
            CallableStatement cs = con.getConexion().prepareCall(consuta);
            cs.setString(1, trabajador.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                IdTrabajador.setText(rs.getString("idWorker"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

//Calculo del Total y que se muestre en el Label Total
    private void TotalPagar() {
        TotalPagar = 0.00;
        int numFila = TableOrdenDeServicio.getRowCount();
        for (int i = 0; i < numFila; i++) {
            double cal = Double.parseDouble(String.valueOf(TableOrdenDeServicio.getModel().getValueAt(i, 3)));
            TotalPagar = TotalPagar + cal;
        }
        JLabelTotal.setText(String.format("%.2f", TotalPagar));
    }

    private void LimpiarOrden() {
        txtServicios.setText("");
        txtCantServicios.setText("");
        txtEntreServicios.setText("");
        txtPrecioPorServicio.setText("");
        txtSubtotal.setText("");
        txtNotaE.setText("");
    }

    //Registra a la venta a BD
    private void RegistrarVenta() {
        String empresa = cbxBusiness.getSelectedItem().toString();
        String cliente = cbxCustomer.getSelectedItem().toString();
        String trabajador = cbxTrabajador.getSelectedItem().toString();
        double monto = TotalPagar;
        v.setEmpresa(empresa);
        v.setCliente(cliente);
        v.setVendedor(trabajador);
        v.setTotal(monto);
        v.RegistrarVenta(v);
    }

    private void RegistrarDetalle() {
        for (int i = 0; i < TableOrdenDeServicio.getRowCount(); i++) {
            String Servicio = TableOrdenDeServicio.getValueAt(i, 0).toString();
            String fecha = TableOrdenDeServicio.getValueAt(i, 1).toString();
            Double precio = Double.valueOf(TableOrdenDeServicio.getValueAt(i, 2).toString());
            int id = 1;
            Dv.setServicio(Servicio);
            Dv.setFecha(fecha);
            Dv.setPrecio(precio);
            Dv.setId_detVenta(id);
        }
    }

    private boolean verificarCamposCompletos() {
        boolean completos = !(txtIdBusiness.getText().trim().isEmpty()
                || txtIdCustomer.getText().trim().isEmpty()
                || tempFrecuenciaStr == null || tempFrecuenciaStr.isEmpty()
                || tempServicioStr == null || tempServicioStr.isEmpty()
                || tempPrecioStr == null || tempPrecioStr.isEmpty());

        if (!completos) {
            System.out.println("Campos faltantes:");
            if (txtIdBusiness.getText().trim().isEmpty()) {
                System.out.println("Falta ID de la empresa");
            }
            if (txtIdCustomer.getText().trim().isEmpty()) {
                System.out.println("Falta ID del cliente");
            }
            if (tempFrecuenciaStr == null || tempFrecuenciaStr.isEmpty()) {
                System.out.println("Falta tiempo entre servicios");
            }
            if (tempServicioStr == null || tempServicioStr.isEmpty()) {
                System.out.println("Falta servicio");
            }
            if (tempPrecioStr == null || tempPrecioStr.isEmpty()) {
                System.out.println("Falta precio por servicio");
            }
        }

        return completos;
    }

    private void registrarOrden() {
    Conectar con = new Conectar();
    Connection connect = null;
    PreparedStatement stmt = null;
    try {
        connect = con.getConexion();

        // Obtenemos los datos de las cajas de texto
        String empresaStr = txtIdBusiness.getText().trim();
        String clienteStr = txtIdCustomer.getText().trim();

        // Convertir los valores a los tipos adecuados
        int empresa = Integer.parseInt(empresaStr);
        int cliente = Integer.parseInt(clienteStr);

        // Obtener la fecha de inicio y fin
        Date fechaInicio = DateInicio.getDate();
        Date fechaFin = DateFinal.getDate();

        if (fechaInicio == null || fechaFin == null) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione las fechas de inicio y fin.", "", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < TableOrdenDeServicio.getRowCount(); i++) {
            String servicioStr = TableOrdenDeServicio.getValueAt(i, 0).toString();
            int frecuencia = Integer.parseInt(TableOrdenDeServicio.getValueAt(i, 4).toString());
            double precio = Double.parseDouble(TableOrdenDeServicio.getValueAt(i, 2).toString());
            String notaEmpresa = txtNotaE.getText().trim();

            System.out.println("Datos a registrar:");
            System.out.println("Empresa: " + empresa);
            System.out.println("Cliente: " + cliente);
            System.out.println("Frecuencia: " + frecuencia);
            System.out.println("Servicio: " + servicioStr);
            System.out.println("Precio: " + precio);
            System.out.println("Nota Empresa: " + notaEmpresa);
            System.out.println("Fecha Inicio: " + fechaInicio);
            System.out.println("Fecha Fin: " + fechaFin);

            // Calcular las fechas intermedias basadas en la frecuencia
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaInicio);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            while (!calendar.getTime().after(fechaFin)) {
                // Insertar la orden de servicio para cada fecha intermedia
                String sql = "INSERT INTO orderservice (fechaT, id_empresa, id_cliente, servicio, precio, notaEmpresa, frecuencia, inicio, fin) VALUES (?,?,?,?,?,?,?,?,?)";

                stmt = connect.prepareStatement(sql);
                stmt.setString(1, dateFormat.format(calendar.getTime()));
                stmt.setInt(2, empresa);
                stmt.setInt(3, cliente);
                stmt.setString(4, servicioStr);
                stmt.setDouble(5, precio);
                stmt.setString(6, notaEmpresa);
                stmt.setInt(7, frecuencia);
                stmt.setString(8, dateFormat.format(fechaInicio));
                stmt.setString(9, dateFormat.format(fechaFin));

                System.out.println("Insertando orden de servicio para la fecha: " + dateFormat.format(calendar.getTime()));

                // Ejecutar la consulta
                stmt.executeUpdate();

                // Incrementar la fecha según la frecuencia
                calendar.add(Calendar.DAY_OF_MONTH, frecuencia);
            }
        }
        JOptionPane.showMessageDialog(null, "Ordenes de servicio registradas correctamente");

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Error en los datos ingresados: " + ex.getMessage());
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


}
