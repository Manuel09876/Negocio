package Administration;

import Bases.RenderTabla;
import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class AsignacionTrabajos extends javax.swing.JInternalFrame {

    Conectar con = new Conectar();
    Connection connect = con.getConexion();

    public AsignacionTrabajos() {
        initComponents();
        AutoCompleteDecorator.decorate(cbxTrabajador);
        AutoCompleteDecorator.decorate(cbxEmpresa);
        MostrarTrabajador(cbxTrabajador);
        MostrarEmpresa(cbxEmpresa);
        MostrarTabla();
        addCheckBox(0, tbAsignacionTrabajos);
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
//Muestra la tabla con los datos completos

    public void MostrarTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Nota Cliente", "Nota Empresa", "Status"};
            String[] RegistroBD = new String[15];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbAsignacionTrabajos);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer "
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, "
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, "
                    + "customer.nota_cliente AS 'Nota Cliente', o.notaEmpresa AS 'Nota Empresa', o.estado AS Status "
                    + "FROM orderservice AS o "
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness "
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer "
                    + "WHERE o.estado = 'Activo' "
                    + "ORDER BY o.fechaT ASC";
            Statement st = connect.createStatement();
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
                RegistroBD[12] = rs.getString("Nota Cliente");
                RegistroBD[13] = rs.getString("Nota Empresa");
                RegistroBD[14] = rs.getString("Status");
                modelo.addRow(RegistroBD);
            }
            tbAsignacionTrabajos.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbAsignacionTrabajos); // Agregar checkbox a la columna 0 de cada fila
            }
            tbAsignacionTrabajos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbAsignacionTrabajos.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbAsignacionTrabajos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbAsignacionTrabajos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbAsignacionTrabajos.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbAsignacionTrabajos.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbAsignacionTrabajos.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbAsignacionTrabajos.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbAsignacionTrabajos.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbAsignacionTrabajos.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(11).setPreferredWidth(150);
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }
    }

//Muestra los datos despues de estar filtrados
    public void FiltrarTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (dateInicio.getDate() == null || dateFin.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Ingrese Fecha Periodo");
            return;
        }
        String fechaInicio = dateFormat.format(dateInicio.getDate());
        String fechaFin = dateFormat.format(dateFin.getDate());
        String ID = txtIdBusiness.getText();
        String ID_buscar = "";
        try {
            if (!(ID.equals(""))) {
                ID_buscar = "WHERE o.id_empresa= '" + ID + "'";
            }
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Nota Cliente", "Nota Empresa", "Status"};
            String[] RegistroBD = new String[15];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbAsignacionTrabajos);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer \n"
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, \n"
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, \n"
                    + "customer.nota_cliente AS 'Nota Cliente', o.notaEmpresa AS 'Nota Empresa', o.estado AS Status \n"
                    + "FROM orderservice AS o \n"
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness \n"
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer " + ID_buscar + " AND o.fechaT BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "' AND o.estado='Activo' \n"
                    + "ORDER BY o.fechaT ASC";
            Statement st = connect.createStatement();
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
                RegistroBD[12] = rs.getString("Nota Cliente");
                RegistroBD[13] = rs.getString("Nota Empresa");
                RegistroBD[14] = rs.getString("Status");
                modelo.addRow(RegistroBD);
            }
            tbAsignacionTrabajos.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbAsignacionTrabajos); // Agregar checkbox a la columna 0 de cada fila
            }
            tbAsignacionTrabajos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbAsignacionTrabajos.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbAsignacionTrabajos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbAsignacionTrabajos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbAsignacionTrabajos.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbAsignacionTrabajos.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbAsignacionTrabajos.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbAsignacionTrabajos.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbAsignacionTrabajos.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbAsignacionTrabajos.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(11).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(12).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(13).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(14).setPreferredWidth(150);
        } catch (SQLException e) {
            //System.out.println("Error al mostrar la Tabla " + e.toString());
        }
    }

    public void FiltrarSoloEmpresa() {
        DefaultTableModel modelo = new DefaultTableModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ID = txtIdBusiness.getText();
        String ID_buscar = "";
        try {
            if (!(ID.equals(""))) {
                ID_buscar = "WHERE o.id_empresa= '" + ID + "'";
            }
            String[] tituloTabla = {"Selección", "id", "Empresa", "Fecha", "Nombre", "Tamaño", "Direccion", "Ciudad", "Estado", "Zip Code", "Celular", "Servicio", "Nota Cliente", "Nota Empresa", "Status"};
            String[] RegistroBD = new String[15];
            modelo = new DefaultTableModel(null, tituloTabla);
            addCheckBox(0, tbAsignacionTrabajos);
            String sql = "SELECT o.id, bussiness.nameBusiness AS Empresa, o.fechaT AS Fecha, customer.nameCustomer \n"
                    + "AS Nombre, customer.area AS Tamaño, customer.address AS Direccion, customer.city AS Ciudad, customer.state AS Estado, \n"
                    + "customer.zipCode AS 'Zip Code', customer.phoneNumber AS Celular, o.servicio AS Servicio, \n"
                    + "customer.nota_cliente AS 'Nota Cliente', o.notaEmpresa AS 'Nota Empresa', o.estado AS Status \n"
                    + "FROM orderservice AS o \n"
                    + "INNER JOIN bussiness ON o.id_empresa = bussiness.idBusiness \n"
                    + "INNER JOIN customer ON o.id_cliente = customer.idCustomer " + ID_buscar + " AND o.estado='Activo' \n"
                    + "ORDER BY o.fechaT ASC";
            Statement st = connect.createStatement();
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
                RegistroBD[12] = rs.getString("Nota Cliente");
                RegistroBD[13] = rs.getString("Nota Empresa");
                RegistroBD[14] = rs.getString("Status");
                modelo.addRow(RegistroBD);
            }
            tbAsignacionTrabajos.setModel(modelo);
            // Añadir checkbox a cada fila
            for (int i = 0; i < modelo.getRowCount(); i++) {
                addCheckBox(0, tbAsignacionTrabajos); // Agregar checkbox a la columna 0 de cada fila
            }
            tbAsignacionTrabajos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbAsignacionTrabajos.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbAsignacionTrabajos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbAsignacionTrabajos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbAsignacionTrabajos.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbAsignacionTrabajos.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(6).setPreferredWidth(80);
            tbAsignacionTrabajos.getColumnModel().getColumn(7).setPreferredWidth(60);
            tbAsignacionTrabajos.getColumnModel().getColumn(8).setPreferredWidth(60);
            tbAsignacionTrabajos.getColumnModel().getColumn(9).setPreferredWidth(80);
            tbAsignacionTrabajos.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(11).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(12).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(13).setPreferredWidth(150);
            tbAsignacionTrabajos.getColumnModel().getColumn(14).setPreferredWidth(150);
        } catch (SQLException e) {
            System.out.println("Error al mostrar la Tabla " + e.toString());
        }
    }

    //Guardar selección y asignar trabajo a trabajador, eliminando fila
    public void guardarSeleccion() {
        try {
// Conexión a la base de datos
            Conectar con = new Conectar();
            Connection connect = con.getConexion();

// Obtener el modelo de la tabla
            DefaultTableModel modelo = (DefaultTableModel) tbAsignacionTrabajos.getModel();

// Crear un ArrayList para almacenar las filas seleccionadas
            ArrayList<Integer> filasSeleccionadas = new ArrayList<>();

// Recorrer las filas de la tabla para guardar los registros
            for (int i = 0; i < modelo.getRowCount(); i++) {
                Boolean seleccionado = (Boolean) modelo.getValueAt(i, 0); // Suponiendo que la columna 0 es donde se encuentra el CheckBox
                if (seleccionado != null && seleccionado) {
                    filasSeleccionadas.add(i);
                    int id = Integer.parseInt(modelo.getValueAt(i, 1).toString()); // Obtener el ID de la tabla
                    int id_Trabajador = Integer.parseInt(txtIdTrabajador.getText()); // Obtener el nombre del trabajador seleccionado en el ComboBox

// Insertar el registro en la base de datos
                    String sql = "INSERT INTO servicio_trabajador (id_OS, id_Trabajador, comentario) VALUES (?, ?, 'sin comentario')";
                    PreparedStatement pstmt = connect.prepareStatement(sql);
                    pstmt.setInt(1, id);
                    pstmt.setInt(2, id_Trabajador);
                    pstmt.executeUpdate();

// Actualizar el estado del registro a "asignado"
                    String sqlUpdate = "UPDATE orderservice SET estado = 'Programado' WHERE id = ?";
                    PreparedStatement pstmtUpdate = connect.prepareStatement(sqlUpdate);
                    pstmtUpdate.setInt(1, id);
                    pstmtUpdate.executeUpdate();

                    //Eliminar la fila del modelo de la tabla
                    modelo.removeRow(i);
                    i--; //Decrmentar el indice ya que se eliminó una fila
                }

            }
            // Cerrar la conexión
            connect.close();
            // Mensaje de éxito
            JOptionPane.showMessageDialog(null, "Registros guardados correctamente");
        } catch (SQLException e) {
            System.out.println("Error al guardar en la base de datos: " + e.getMessage());
        }
    }

    private boolean Seleccionados(int pos) {
        int contador = 0;
        boolean bandera = true;
        for (int i = 0; i < tbAsignacionTrabajos.getRowCount(); i++) {
            boolean seleccion = (boolean) tbAsignacionTrabajos.getValueAt(i, pos);
            if (seleccion) {
                contador++;
            }
        }
        if (contador == 0) {
            bandera = false;
        }
        return bandera;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbxTrabajador = new javax.swing.JComboBox<>();
        dateInicio = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        txtIdTrabajador = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        dateFin = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        cbxEmpresa = new javax.swing.JComboBox<>();
        txtIdBusiness = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        btnProgramar = new javax.swing.JButton();
        cbSeleccionaTodo = new javax.swing.JCheckBox();
        btnSoloEmpresa = new javax.swing.JButton();
        btnMostrarTodo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbAsignacionTrabajos = new javax.swing.JTable();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Asignación de Trabajos");

        jPanel1.setBackground(new java.awt.Color(102, 255, 102));

        jLabel1.setText("Trabajador");

        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });

        dateInicio.setDateFormatString("yyyy-MM-dd");

        jLabel2.setText("F.Inicio");

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jLabel3.setText("F.Final");

        dateFin.setDateFormatString("yyyy-MM-dd");

        jLabel4.setText("Empresa");

        cbxEmpresa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxEmpresaItemStateChanged(evt);
            }
        });

        btnFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/configuraciones.png"))); // NOI18N
        btnFiltrar.setText("Filtrar Fecha Empresa");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });

        btnProgramar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-producto.png"))); // NOI18N
        btnProgramar.setText("Programar");
        btnProgramar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProgramarActionPerformed(evt);
            }
        });

        cbSeleccionaTodo.setText("Selecciona Todo");
        cbSeleccionaTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSeleccionaTodoActionPerformed(evt);
            }
        });

        btnSoloEmpresa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/configuraciones.png"))); // NOI18N
        btnSoloEmpresa.setText("Filtrar solo por Empresa");
        btnSoloEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSoloEmpresaActionPerformed(evt);
            }
        });

        btnMostrarTodo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/box.png"))); // NOI18N
        btnMostrarTodo.setText("Mostrar Todo");
        btnMostrarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarTodoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxTrabajador, 0, 262, Short.MAX_VALUE)
                            .addComponent(cbxEmpresa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(41, 41, 41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(cbSeleccionaTodo)
                        .addGap(37, 37, 37)
                        .addComponent(btnSoloEmpresa)
                        .addGap(57, 57, 57)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(btnMostrarTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnProgramar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdTrabajador, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                            .addComponent(txtIdBusiness))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(dateFin, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(btnSalir)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(176, 176, 176))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbSeleccionaTodo)
                    .addComponent(btnProgramar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrarTodo))
                .addGap(9, 9, 9))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(dateFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(dateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(cbxTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(txtIdTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel3))))
                            .addGap(1, 1, 1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(btnSalir)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbxEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtIdBusiness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(btnSoloEmpresa))
                    .addComponent(btnFiltrar))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        tbAsignacionTrabajos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbAsignacionTrabajos);
        if (tbAsignacionTrabajos.getColumnModel().getColumnCount() > 0) {
            tbAsignacionTrabajos.getColumnModel().getColumn(0).setPreferredWidth(5);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
//Boton Salir
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed
//Evento para ComboBox Trabajador
    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged
//Evento para ComboBox Empresa
    private void cbxEmpresaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxEmpresaItemStateChanged
        MostrarCodigoEmpresa(cbxEmpresa, txtIdBusiness);
    }//GEN-LAST:event_cbxEmpresaItemStateChanged
//Filtrar Empresa y Fecha
    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        FiltrarTabla();
    }//GEN-LAST:event_btnFiltrarActionPerformed
//Seleecionar todos los checkbox
    private void cbSeleccionaTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSeleccionaTodoActionPerformed
        if (cbSeleccionaTodo.isSelected()) {
            cbSeleccionaTodo.setText("Deseleccionar Todo");
            for (int i = 0; i < tbAsignacionTrabajos.getRowCount(); i++) {
                tbAsignacionTrabajos.setValueAt(true, i, 0);
            }
        } else {
            cbSeleccionaTodo.setText("Seleccionar Todo");
            for (int i = 0; i < tbAsignacionTrabajos.getRowCount(); i++) {
                tbAsignacionTrabajos.setValueAt(false, i, 0);
            }
        }
    }//GEN-LAST:event_cbSeleccionaTodoActionPerformed

    private void btnSoloEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSoloEmpresaActionPerformed
        FiltrarSoloEmpresa();
    }//GEN-LAST:event_btnSoloEmpresaActionPerformed

    private void btnMostrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodoActionPerformed
        MostrarTabla();
    }//GEN-LAST:event_btnMostrarTodoActionPerformed

    private void btnProgramarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProgramarActionPerformed
        guardarSeleccion();
    }//GEN-LAST:event_btnProgramarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnMostrarTodo;
    private javax.swing.JButton btnProgramar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSoloEmpresa;
    private javax.swing.JCheckBox cbSeleccionaTodo;
    private javax.swing.JComboBox<String> cbxEmpresa;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private com.toedter.calendar.JDateChooser dateFin;
    private com.toedter.calendar.JDateChooser dateInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbAsignacionTrabajos;
    private javax.swing.JTextField txtIdBusiness;
    private javax.swing.JTextField txtIdTrabajador;
    // End of variables declaration//GEN-END:variables

//Muestra los Trabajadores en el ComboBox
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

//Muestra las Empresas en el ComboBox
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
