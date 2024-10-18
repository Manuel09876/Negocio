package Admission;

import Bases.Empresa_Cliente;
import conectar.Conectar;
import java.awt.HeadlessException;
import java.sql.Connection;
import javax.swing.JTextField;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Clientes extends javax.swing.JInternalFrame {
    
    
    //Variable para la tabla
    DefaultTableModel model;

    //Variables
    int idCustomer;
    int Empresa;
    String name;
    String address;
    int zipCode;
    String city, state, phoneNumber, email;
    double area;
    String nota_cliente;
    String estado;

    public int getEmpresa() {
        return Empresa;
    }

    //Setters and Getters
    public void setEmpresa(int Empresa) {
        this.Empresa = Empresa;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getNota_cliente() {
        return nota_cliente;
    }

    public void setNota_cliente(String nota_cliente) {
        this.nota_cliente = nota_cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Clientes() {
        initComponents();
         
        MostrarClientes("");
        txtCodigo.setEnabled(false);
        AutoCompleteDecorator.decorate(cbxEmpresa);
        MostrarEmpresa(cbxEmpresa);

    }
    

    public void InsertarCliente() {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        
        int id_empresa;
        String name;
        String address;
        int zipCode;
        String city, state, phoneNumber, email;
        double area;
        String nota_cliente;
        String estado;

        String sql = "";
//Obtenemos la informacion de las cajas de texto
        id_empresa = Integer.parseInt(txtIdBusiness.getText());
        name = txtNameCustomer.getText();
        address = txtNameAddress.getText();
        zipCode = Integer.parseInt(txtZipCode.getText());
        city = txtCity.getText();
        state = txtState.getText();
        phoneNumber = txtPhoneNumber.getText();
        email = txtEmail.getText();
        area = Double.parseDouble(txtArea.getText());
        nota_cliente = txtNotaCliente.getText();

        String consulta = "insert into customer (id_empresa,nameCustomer,address, "
                + "zipCode,city,state,phoneNumber,email,area,nota_cliente)values(?,?,?,?,?,?,?,?,?,?)";
        try {            
//prepara la coneccion para enviar al sql (Evita ataques al sql)
            PreparedStatement pst = connection.prepareStatement(consulta);
            pst.setInt(1, id_empresa);
            pst.setString(2, name);
            pst.setString(3, address);
            pst.setInt(4, zipCode);
            pst.setString(5, city);
            pst.setString(6, state);
            pst.setString(7, phoneNumber);
            pst.setString(8, email);
            pst.setDouble(9, area);
            pst.setString(10, nota_cliente);
//Declara otra variable para validar los registros
            int n = pst.executeUpdate();
//si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardo exitosamente");
                txtCodigo.setText("");
                txtNameCustomer.setText("");
                txtNameAddress.setText("");
                txtZipCode.setText("");
                txtCity.setText("");
                txtState.setText("");
                txtPhoneNumber.setText("");
                txtEmail.setText("");
                txtArea.setText("");
                txtNotaCliente.setText("");
            }
            MostrarClientes("");

        } catch (SQLException e) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, e);

            JOptionPane.showMessageDialog(null, "No se Inserto: error " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarClientes(String Valores) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida aquí
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }

        
        try {
            String[] titulosTabla = {"Id", "Empresa", "Nombres", "Dirección", "ZipCode", "Ciudad", "Estado", "Telefono", "email", "area", "nota_cliente", "estatus"}; //Titulos de la Tabla
            String[] RegistroBD = new String[12];  //Registros de la Basede Datos
            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla
            String ConsultaSQL = """
                                 SELECT customer.idCustomer, bussiness.nameBusiness AS Empresa, customer.nameCustomer, customer.address, customer.zipCode, customer.city, customer.state, customer.phoneNumber, customer.email, customer.area, customer.nota_cliente, customer.estado
                                 FROM customer
                                 INNER JOIN bussiness on customer.id_empresa=bussiness.idBusiness""";
            
            
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(ConsultaSQL);

            while (rs.next()) {
                RegistroBD[0] = rs.getString(1);
                RegistroBD[1] = rs.getString(2);
                RegistroBD[2] = rs.getString(3);
                RegistroBD[3] = rs.getString(4);
                RegistroBD[4] = rs.getString(5);
                RegistroBD[5] = rs.getString(6);
                RegistroBD[6] = rs.getString(7);
                RegistroBD[7] = rs.getString(8);
                RegistroBD[8] = rs.getString(9);
                RegistroBD[9] = rs.getString(10);
                RegistroBD[10] = rs.getString(11);
                RegistroBD[11] = rs.getString(12);

                model.addRow(RegistroBD);
            }
            tbCustomer.setModel(model);
            tbCustomer.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbCustomer.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbCustomer.getColumnModel().getColumn(2).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(4).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(6).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(7).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(8).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(9).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(11).setPreferredWidth(250);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se puede Mostrar, Error " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void SeleccionarClientes(JTable TablaClientes, JTextField Codigo, JComboBox idE, JTextField NameCustomer, JTextField NameAddress,
            JTextField ZipCode, JTextField City, JTextField State, JTextField PhoneNumber, JTextField Email, JTextField Area, JTextArea Nota_cliente) {
        try {
            int fila = TablaClientes.getSelectedRow();
            if (fila >= 0) {

                Codigo.setText(TablaClientes.getValueAt(fila, 0).toString());
                idE.setSelectedItem(TablaClientes.getValueAt(fila, 1).toString());
                NameCustomer.setText(TablaClientes.getValueAt(fila, 2).toString());
                NameAddress.setText(TablaClientes.getValueAt(fila, 3).toString());
                ZipCode.setText(TablaClientes.getValueAt(fila, 4).toString());
                City.setText(TablaClientes.getValueAt(fila, 5).toString());
                State.setText(TablaClientes.getValueAt(fila, 6).toString());
                PhoneNumber.setText(TablaClientes.getValueAt(fila, 7).toString());
                Email.setText(TablaClientes.getValueAt(fila, 8).toString());
                Area.setText(TablaClientes.getValueAt(fila, 9).toString());
                Nota_cliente.setText(TablaClientes.getValueAt(fila, 10).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "error en la selección: " + e.toString());
        }
    }

    public void ModificarCliente(JTextField Codigo, JTextField IdE, JTextField NameCustomer, JTextField NameAddress,
            JTextField ZipCode, JTextField City, JTextField State, JTextField PhoneNumber, JTextField Email, JTextField Area,
            JTextArea nota_cliente) {
        
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }

        Empresa = Integer.parseInt(txtIdBusiness.getText());
        setIdCustomer(Integer.parseInt(Codigo.getText()));
        setEmpresa(Integer.parseInt(IdE.getText()));
        setName(NameCustomer.getText());
        setAddress(NameAddress.getText());
        setZipCode(Integer.parseInt(ZipCode.getText()));
        setCity(City.getText());
        setState(State.getText());
        setPhoneNumber(PhoneNumber.getText());
        setEmail(Email.getText());
        setArea(Double.parseDouble(Area.getText()));
        setNota_cliente(nota_cliente.getText());

        String consulta = "UPDATE customer set id_empresa=?, nameCustomer=?, address=?,zipCode=?, "
                + "city=?, state=?, phoneNumber=?, email=?, area=?, nota_cliente=? where idCustomer=?";
        try {            
            CallableStatement cs = connection.prepareCall(consulta);
            cs.setInt(1, getEmpresa());
            cs.setString(2, getName());
            cs.setString(3, getAddress());
            cs.setInt(4, getZipCode());
            cs.setString(5, getCity());
            cs.setString(6, getState());
            cs.setString(7, getPhoneNumber());
            cs.setString(8, getEmail());
            cs.setDouble(9, getArea());
            cs.setString(10, getNota_cliente());
            cs.setInt(11, getIdCustomer());
            cs.execute();
            JOptionPane.showMessageDialog(null, "Modificacion Exitosa");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se Modifico, error: " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void EliminarClientes(JTextField codigo) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        
        setIdCustomer(Integer.parseInt(codigo.getText()));
        String consulta = "DELETE from customer where idCustomer=?";
        try {            
            CallableStatement cs = connection.prepareCall(consulta);
            cs.setInt(1, getIdCustomer());
            cs.executeUpdate();
            JOptionPane.showMessageDialog(null, "Se Elimino");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public Clientes BuscarCli(java.awt.event.KeyEvent evt) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        
        String[] titulosTabla = {"Id", "Empresa", "Nombres", "Dirección", "ZipCode",
            "Ciudad", "Estado", "Telefono", "email", "area", "nota_cliente", "estatus"}; //Titulos de la Tabla
        String[] RegistroBD = new String[11];  //Registros de la Basede Datos

        Clientes cliente = new Clientes();
        String sql = """
                     SELECT customer.idCustomer, bussiness.nameBusiness AS Empresa, customer.nameCustomer, customer.address, customer.zipCode, customer.city, customer.state, customer.phoneNumber, customer.email, customer.area, customer.nota_cliente, customer.estado
                     FROM customer
                     INNER JOIN bussiness on customer.id_empresa=bussiness.idBusiness WHERE nameCustomer LIKE '%""" + txtBuscarCliente.getText() + "%'";
        model = new DefaultTableModel(null, titulosTabla);
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                RegistroBD[0] = rs.getString("idCustomer");
                RegistroBD[1] = rs.getString(2);
                RegistroBD[2] = rs.getString("nameCustomer");
                RegistroBD[3] = rs.getString("address");
                RegistroBD[4] = rs.getString("zipCode");
                RegistroBD[5] = rs.getString("city");
                RegistroBD[6] = rs.getString("state");
                RegistroBD[7] = rs.getString("phoneNumber");
                RegistroBD[8] = rs.getString("email");
                RegistroBD[9] = rs.getString("area");
                RegistroBD[10] = rs.getString("nota_cliente");
                RegistroBD[11] = rs.getString("estado");
                model.addRow(RegistroBD);
            }
            tbCustomer.setModel(model);
            tbCustomer.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbCustomer.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbCustomer.getColumnModel().getColumn(2).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(4).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(6).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(7).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(8).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(9).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(10).setPreferredWidth(150);
            tbCustomer.getColumnModel().getColumn(11).setPreferredWidth(250);

        } catch (SQLException e) {
            System.out.println(e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
        return cliente;
    }

    //Constructores de Metodos que vamos a necesitar
    //Realización de los Métodos
    public void BloquearCampos() {
        txtCodigo.setEnabled(false);
        txtNameCustomer.setEnabled(false);
        txtNameAddress.setEnabled(false);
        txtZipCode.setEnabled(false);
        txtCity.setEnabled(false);
        txtState.setEnabled(false);
        txtPhoneNumber.setEnabled(false);
        txtEmail.setEnabled(false);
        txtArea.setEnabled(false);
        txtNotaCliente.setEnabled(false);

        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    void LimpiarCajasTexto() {
        txtNameCustomer.setText("");
        txtNameAddress.setText("");
        txtZipCode.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtPhoneNumber.setText("");
        txtEmail.setText("");
        txtArea.setText("");
        txtNotaCliente.setText("");
    }

    void DesbloquearCampos() {
        txtNameCustomer.setEnabled(true);
        txtNameAddress.setEnabled(true);
        txtZipCode.setEnabled(true);
        txtCity.setEnabled(true);
        txtState.setEnabled(true);
        txtPhoneNumber.setEnabled(true);
        txtEmail.setEnabled(true);
        txtArea.setEnabled(true);
        txtNotaCliente.setEnabled(true);

        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }

    public boolean accion(String estado, int idCustomer) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        
        String sql = "UPDATE customer SET estado = ? WHERE idCustomer = ?";
        try {
            PreparedStatement ps;
            ps = connection.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, idCustomer);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtArea = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cbxEmpresa = new javax.swing.JComboBox<>();
        txtIdBusiness = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtNameCustomer = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPhoneNumber = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtZipCode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtState = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNameAddress = new javax.swing.JTextField();
        btnActivar = new javax.swing.JButton();
        btnInactivar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnLimpiar = new javax.swing.JButton();
        txtBuscarCliente = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNotaCliente = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        btnGuia = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Customers/Clientes");
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 255, 255));

        txtArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAreaActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel9.setText("Area");

        jLabel11.setText("feet");

        jLabel14.setText("Afiliar a empresa");

        cbxEmpresa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxEmpresaItemStateChanged(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/View Bills & Order Placed Details.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnActualizar.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnActualizar.setText("Modificar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnBorrar.setText("Eliminar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel1.setText("Nombre");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel10.setText("id Cliente");

        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel8.setText("email");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel7.setText("Teléfono");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel4.setText("Zip Code");

        txtZipCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtZipCodeActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel5.setText("Ciudad");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel6.setText("Estado");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel3.setText("Dirección");

        btnActivar.setText("Activar");
        btnActivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivarActionPerformed(evt);
            }
        });

        btnInactivar.setText("Inactivar");
        btnInactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInactivarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(jLabel14)
                        .addGap(79, 79, 79)
                        .addComponent(txtIdBusiness, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(cbxEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(46, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel1))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtNameCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnInactivar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnActivar))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(32, 32, 32)
                        .addComponent(txtArea, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtCity)
                                .addComponent(txtZipCode)
                                .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNameAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnActualizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(btnActivar))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNameCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnInactivar))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNameAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtZipCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(64, 64, 64)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(txtArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnNuevo)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar)
                        .addGap(18, 18, 18)
                        .addComponent(btnActualizar)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBorrar)
                        .addGap(85, 85, 85)))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtIdBusiness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbxEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        txtBuscarCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarClienteKeyPressed(evt);
            }
        });

        jLabel12.setText("Descripcion");

        tbCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCustomerMouseClicked(evt);
            }
        });
        scroll.setViewportView(tbCustomer);

        txtNotaCliente.setColumns(20);
        txtNotaCliente.setRows(5);
        jScrollPane1.setViewportView(txtNotaCliente);

        jLabel2.setText("Nota del Cliente");

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnGuia.setText("Guia");
        btnGuia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scroll))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addGap(27, 27, 27)
                .addComponent(txtBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuia)
                .addGap(27, 27, 27))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLimpiar)
                            .addComponent(jLabel12)
                            .addComponent(txtBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(btnSalir)))
                .addGap(18, 18, 18)
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnGuia)))
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed

        this.dispose();  //solo cierra la ventana actual

    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        txtBuscarCliente.setText("");
        MostrarClientes("");
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        DesbloquearCampos();
        LimpiarCajasTexto();
        txtNameCustomer.requestFocus();

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        InsertarCliente();
        MostrarClientes("");
        txtCodigo.setText("");
        txtNameCustomer.setText("");
        txtNameAddress.setText("");
        txtZipCode.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtPhoneNumber.setText("");
        txtEmail.setText("");
        txtArea.setText("");
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed

        ModificarCliente(txtCodigo, txtIdBusiness, txtNameCustomer, txtNameAddress, txtZipCode, txtCity, txtState, txtPhoneNumber, txtEmail, txtArea, txtNotaCliente);
        btnActualizar.setEnabled(true);
        MostrarClientes("");
        LimpiarCajasTexto();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        BloquearCampos();
        txtCodigo.setText("");
        txtNameCustomer.setText("");
        txtNameAddress.setText("");
        txtZipCode.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtPhoneNumber.setText("");
        txtEmail.setText("");
        txtArea.setText("");
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        SeleccionarClientes(tbCustomer, txtCodigo, cbxEmpresa, txtNameCustomer, txtNameAddress, txtZipCode, txtCity, txtState, txtPhoneNumber, txtEmail, txtArea, txtNotaCliente);
        EliminarClientes(txtCodigo);
        MostrarClientes("");
        txtCodigo.setText("");
        txtNameCustomer.setText("");
        txtNameAddress.setText("");
        txtZipCode.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtPhoneNumber.setText("");
        txtEmail.setText("");
        txtArea.setText("");
        BloquearCampos();
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void txtZipCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtZipCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtZipCodeActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

        SeleccionarClientes(tbCustomer, txtCodigo, cbxEmpresa, txtNameCustomer, txtNameAddress, txtZipCode, txtCity, txtState, txtPhoneNumber, txtEmail, txtArea, txtNotaCliente);
    }//GEN-LAST:event_formMouseClicked

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void txtAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAreaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAreaActionPerformed

    private void tbCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCustomerMouseClicked
        SeleccionarClientes(tbCustomer, txtCodigo, cbxEmpresa, txtNameCustomer, txtNameAddress, txtZipCode, txtCity, txtState, txtPhoneNumber, txtEmail, txtArea, txtNotaCliente);
    }//GEN-LAST:event_tbCustomerMouseClicked

    private void cbxEmpresaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxEmpresaItemStateChanged
        MostrarCodigoEmpresa(cbxEmpresa, txtIdBusiness);
    }//GEN-LAST:event_cbxEmpresaItemStateChanged

    private void txtBuscarClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarClienteKeyPressed
        BuscarCli(evt);

    }//GEN-LAST:event_txtBuscarClienteKeyPressed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        String idText = txtCodigo.getText().trim(); // Elimina espacios en blanco antes y después

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un cliente para activar.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                int id = Integer.parseInt(idText); // Convertir a entero después de validar que no esté vacío
                if (accion("Activo", id)) {
                    JOptionPane.showMessageDialog(null, "Cliente activado con éxito.");
                    MostrarClientes("");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al activar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El ID ingresado no es un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void btnInactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivarActionPerformed
        String idText = txtCodigo.getText().trim(); // Elimina espacios en blanco antes y después

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un cliente para inactivar.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                int id = Integer.parseInt(idText); // Convertir a entero después de validar que no esté vacío
                if (accion("Inactivo", id)) {
                    JOptionPane.showMessageDialog(null, "Cliente inactivado con éxito.");
                    MostrarClientes("");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al inactivar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El ID ingresado no es un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnInactivarActionPerformed

    private void btnGuiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiaActionPerformed
        JOptionPane.showMessageDialog(null, "CLIENTES\n"
                + "Llenar todas las casillas para el ingreso de Nuevos Clientes; si tiene varias empresas con las que trabaja\n"
                + "y le dan clientes debera asociarlos a la empresa que se las da, hay una casilla para las Notas del Cliente\n"
                + "Botón GUARDAR para guardar los datos que se mostraran en una Tabla\n"
                + "Botón CANCELAR para limpiar las casillas\n"
                + "Botón MODIFICAR  seleccionamos una fila de la Tabla para modificar los datos y presionamos Modificar\n"
                + "Botón ELIMINAR seleccionamos la fila de la Tabla que queremos eliminar y click en Eliminar\n"
                + "Botón ACTIVAR para activar un Cliente que habia sido desactivado\n"
                + "Botón DESACTIVAR´para desactivar a un Cliente\n"
                + "Botón NUEVO limpiara las casilla y se ubicará el puntero en Nombre\n"
                + "Esta Interfaz es para el Ingreso de Nuevos Clientes");
    }//GEN-LAST:event_btnGuiaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnActivar;
    public javax.swing.JButton btnActualizar;
    public javax.swing.JButton btnBorrar;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuia;
    public javax.swing.JButton btnInactivar;
    public javax.swing.JButton btnLimpiar;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxEmpresa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane scroll;
    public static final javax.swing.JTable tbCustomer = new javax.swing.JTable();
    private javax.swing.JTextField txtArea;
    private javax.swing.JTextField txtBuscarCliente;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtIdBusiness;
    private javax.swing.JTextField txtNameAddress;
    private javax.swing.JTextField txtNameCustomer;
    private javax.swing.JTextArea txtNotaCliente;
    private javax.swing.JTextField txtPhoneNumber;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtZipCode;
    // End of variables declaration//GEN-END:variables

    
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

    public void MostrarEmpresa(JComboBox cbxEmpresa) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }

        String sql = "select * from bussiness";
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

}
