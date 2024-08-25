package Administration;

//import Bases.Tables;
import conectar.Conectar;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Proveedor extends javax.swing.JInternalFrame {

    DefaultTableModel model;

    int idSuplier;
    String nameSuplier, address;
    int zipCode;
    String city, state, phoneNumber, website, email, estado;

    public Proveedor(int idSuplier, String nameSuplier, String address, int zipCode, String city, String state, String phoneNumber, String website, String email, String estado) {
        this.idSuplier = idSuplier;
        this.nameSuplier = nameSuplier;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.email = email;
        this.estado = estado;
    }

    public int getIdSuplier() {
        return idSuplier;
    }

    public void setIdSuplier(int idSuplier) {
        this.idSuplier = idSuplier;
    }

    public String getNameSuplier() {
        return nameSuplier;
    }

    public void setNameSuplier(String nameSuplier) {
        this.nameSuplier = nameSuplier;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Proveedor() {
        initComponents();

        txtIdSuplier.setEnabled(false);
        MostrarProveedor("");

    }

    Conectar objconexion = new Conectar();
    Connection connect = objconexion.getConexion();
    private static final Logger LOGGER = Logger.getLogger(Proveedor.class.getName());
    ResultSet rs;

    DefaultTableModel modelo = new DefaultTableModel();

    public void MostrarProveedor(String Valores) {
        try {
            String[] tituloTabla = {"idSuplier", "Proveedor", "Dirección", "ZipCode", "Ciudad", "Estado", "Telefono", "Wensite", "email", "Estatus"};
            String[] RegistroBD = new String[10];
            model = new DefaultTableModel(null, tituloTabla);
            String sql = "SELECT * FROM suplier";
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[0] = rs.getString("idSuplier");
                RegistroBD[1] = rs.getString("nameSuplier");
                RegistroBD[2] = rs.getString("address");
                RegistroBD[3] = rs.getString("zipCode");
                RegistroBD[4] = rs.getString("city");
                RegistroBD[5] = rs.getString("state");
                RegistroBD[6] = rs.getString("phoneNumber");
                RegistroBD[7] = rs.getString("website");
                RegistroBD[8] = rs.getString("email");
                RegistroBD[9] = rs.getString("estado");
                model.addRow(RegistroBD);
            }
            tbSuplier.setModel(model);
            tbSuplier.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbSuplier.getColumnModel().getColumn(1).setPreferredWidth(200);
            tbSuplier.getColumnModel().getColumn(2).setPreferredWidth(150);
            tbSuplier.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbSuplier.getColumnModel().getColumn(4).setPreferredWidth(200);
            tbSuplier.getColumnModel().getColumn(5).setPreferredWidth(50);
            tbSuplier.getColumnModel().getColumn(6).setPreferredWidth(200);
            tbSuplier.getColumnModel().getColumn(7).setPreferredWidth(150);
            tbSuplier.getColumnModel().getColumn(8).setPreferredWidth(150);
        } catch (SQLException e) {
        }
    }

    public void Insertar() {
        //Variables
        String nameSuplier, address;
        int zipCode;
        String city, state, phoneNumber, website, email, estado;

        //Obtenemos la Información de la Caja de Texto
        nameSuplier = txtNameSuplier.getText();
        address = txtAddress.getText();
        zipCode = Integer.parseInt(txtZipCode.getText());
        city = txtCity.getText();
        state = txtState.getText();
        phoneNumber = txtPhonenNumber.getText();
        website = txtWebsite.getText();
        email = txtEmail.getText();

        //Consulta sql para insertar los datos (nombres como en la base de datos)
        String sql = "INSERT INTO suplier (nameSuplier, address, zipCode, city, state, phoneNumber, website,email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        //Para almacenar los datos empleo un try cash
        try {

            //prepara la coneccion para enviar al sql (Evita ataques al sql)
            PreparedStatement pst = connect.prepareStatement(sql);

            pst.setString(1, nameSuplier);
            pst.setString(2, address);
            pst.setInt(3, zipCode);
            pst.setString(4, city);
            pst.setString(5, state);
            pst.setString(6, phoneNumber);
            pst.setString(7, website);
            pst.setString(8, email);

            //Declara otra variable para validar los registros
            int n = pst.executeUpdate();

            //si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardo exitosamente");

                txtNameSuplier.setText("");
                txtAddress.setText("");
                txtZipCode.setText("");
                txtCity.setText("");
                txtState.setText("");
                txtPhonenNumber.setText("");
                txtWebsite.setText("");
                txtEmail.setText("");
            }
            MostrarProveedor("");

        } catch (SQLException e) {
            Logger.getLogger(Tarifario.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "El registro NO se guardo exitosamente, Error " + e.toString());
        }

    }

    public void Seleccionar(JTable tbProveedor, JTextField IdSuplier, JTextField NameSuplier, JTextField Addres, JTextField ZipCode, JTextField City, JTextField State, JTextField PhoneNumber, JTextField Website, JTextField Email) {

        try {
            int fila = tbProveedor.getSelectedRow();

            if (fila >= 0) {
                IdSuplier.setText(tbProveedor.getValueAt(fila, 0).toString());
                NameSuplier.setText(tbProveedor.getValueAt(fila, 1).toString());
                Addres.setText(tbProveedor.getValueAt(fila, 2).toString());
                ZipCode.setText(tbProveedor.getValueAt(fila, 3).toString());
                City.setText(tbProveedor.getValueAt(fila, 4).toString());
                State.setText(tbProveedor.getValueAt(fila, 5).toString());
                PhoneNumber.setText(tbProveedor.getValueAt(fila, 6).toString());
                Website.setText(tbProveedor.getValueAt(fila, 7).toString());
                Email.setText(tbProveedor.getValueAt(fila, 8).toString());

            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error en la selección, error " + e.toString());
        }
    }

    public void Modificar(JTextField IdSuplier, JTextField NameSuplier, JTextField Addres, JTextField ZipCode, JTextField City, JTextField State, JTextField PhoneNumber, JTextField Website, JTextField Email) {

        setIdSuplier(Integer.parseInt(IdSuplier.getText()));
        setNameSuplier(NameSuplier.getText());
        setAddress(Addres.getText());
        setZipCode(Integer.parseInt(ZipCode.getText()));
        setCity(City.getText());
        setState(State.getText());
        setPhoneNumber(PhoneNumber.getText());
        setWebsite(Website.getText());
        setEmail(Email.getText());

        String consulta = "UPDATE suplier SET nameSuplier=?, address=?, zipCode=?, city=?, state=?, phoneNumber=?, website=?,email=? WHERE idSuplier=?";

        try {
            CallableStatement cs = objconexion.getConexion().prepareCall(consulta);

            cs.setString(1, getNameSuplier());
            cs.setString(2, getAddress());
            cs.setInt(3, getZipCode());
            cs.setString(4, getCity());
            cs.setString(5, getState());
            cs.setString(6, getPhoneNumber());
            cs.setString(7, getWebsite());
            cs.setString(8, getEmail());
            cs.setInt(9, getIdSuplier());  //No olvidar el Id
            cs.execute();

            JOptionPane.showMessageDialog(null, "Modificación exitosa");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al modificar, error: " + e.toString());
        }

    }

    public void Eliminar(JTextField codigo) {

        setIdSuplier(Integer.parseInt(codigo.getText()));

        String consulta = "DELETE FROM suplier WHERE idSuplier=?";

        try {

            CallableStatement cs = objconexion.getConexion().prepareCall(consulta);
            cs.setInt(1, getIdSuplier());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Se Elimino");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());
        }

    }

    public Proveedor BuscarProveedor(java.awt.event.KeyEvent evt) {
        String[] tituloTabla = {"idSuplier", "Proveedor", "Dirección", "ZipCode", "Ciudad", "Estado", "Telefono", "Wensite", "email", "Estatus"};
        String[] RegistroBD = new String[10];
        
        Proveedor proveedor = new Proveedor();
        String sql = "SELECT * FROM suplier WHERE nameSuplier LIKE '%" + txtBuscar.getText() + "%'";
        model = new DefaultTableModel(null, tituloTabla); //Le pasamos los titulos a la tabla
        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                RegistroBD[0] = rs.getString("idSuplier");
                RegistroBD[1] = rs.getString("nameSuplier");
                RegistroBD[2] = rs.getString("address");
                RegistroBD[3] = rs.getString("zipCode");
                RegistroBD[4] = rs.getString("city");
                RegistroBD[5] = rs.getString("state");
                RegistroBD[6] = rs.getString("phoneNumber");
                RegistroBD[7] = rs.getString("website");
                RegistroBD[8] = rs.getString("email");
                RegistroBD[9] = rs.getString("estado");
                model.addRow(RegistroBD);
            }
            tbSuplier.setModel(model);
            tbSuplier.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbSuplier.getColumnModel().getColumn(1).setPreferredWidth(200);
            tbSuplier.getColumnModel().getColumn(2).setPreferredWidth(150);
            tbSuplier.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbSuplier.getColumnModel().getColumn(4).setPreferredWidth(200);
            tbSuplier.getColumnModel().getColumn(5).setPreferredWidth(50);
            tbSuplier.getColumnModel().getColumn(6).setPreferredWidth(200);
            tbSuplier.getColumnModel().getColumn(7).setPreferredWidth(150);
            tbSuplier.getColumnModel().getColumn(8).setPreferredWidth(150);            
        }catch (SQLException e) {
            System.out.println(e.toString());
        }        
        return proveedor;
    }

    // Posiblemente para usarlos despues para Busqueda
    public boolean accion(String estado, int idSuplier) {
        String sql = "UPDATE suplier SET estado = ? WHERE idSuplier = ?";
        try {
            connect = objconexion.getConexion();
            PreparedStatement pst = connect.prepareStatement(sql);
            pst = connect.prepareStatement(sql);
            pst.setString(1, estado);
            pst.setInt(2, idSuplier);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }
    }

    public Proveedor buscarProv(int id) {
        String sql = "SELECT * FROM suplier WHERE idSuplier = ?";
        Proveedor prov = new Proveedor();
        try {
            connect = objconexion.getConexion();
            PreparedStatement pst = connect.prepareStatement(sql);
            pst = connect.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                prov.setNameSuplier(rs.getString("nameSuplier"));
                prov.setAddress(rs.getString("address"));
                prov.setZipCode(rs.getInt("zipCode"));
                prov.setCity(rs.getString("city"));
                prov.setState(rs.getString("state"));
                prov.setPhoneNumber(rs.getString("phoneNumber"));
                prov.setWebsite(rs.getString("website"));
                prov.setEmail(rs.getString("email"));

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return prov;
    }

    public Proveedor buscarCodigo(String codigo) {
        String sql = "SELECT * FROM suplier WHERE nameSuplier = ? AND estado = 'Activo'";
        Proveedor prov = new Proveedor();
        try {
            connect = objconexion.getConexion();
            PreparedStatement pst = connect.prepareStatement(sql);
            pst.setString(1, codigo);
            rs = pst.executeQuery();
            if (rs.next()) {
                prov.setIdSuplier(rs.getInt("idSuplier"));//Segun id recuperamos los valores de la BD
                prov.setNameSuplier(rs.getString("nameSuplier"));
                prov.setAddress(rs.getString("address"));
                prov.setZipCode(rs.getInt("zipCode"));
                prov.setCity(rs.getString("city"));
                prov.setState(rs.getString("state"));
                prov.setPhoneNumber(rs.getString("phoneNumber"));
                prov.setWebsite(rs.getString("website"));
                prov.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return prov;
    }

    private void limpiar() {
        txtIdSuplier.setText("");
        txtNameSuplier.setText("");
        txtAddress.setText("");
        txtZipCode.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtPhonenNumber.setText("");
        txtWebsite.setText("");
        txtEmail.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnMostrar = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNameSuplier = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtZipCode = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtState = new javax.swing.JTextField();
        txtPhonenNumber = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtWebsite = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        txtIdSuplier = new javax.swing.JTextField();
        btnModificar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbSuplier = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnReingresar = new javax.swing.JButton();
        btnInactivar = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnGuia = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Proveedores");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));

        btnMostrar.setText("Mostrar");
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
        });

        jLabel9.setText("Description");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(txtBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                .addGap(26, 26, 26)
                .addComponent(btnMostrar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(btnMostrar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 630, 56));

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Dirección");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 76, -1, -1));

        jLabel3.setText("Zip Code");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 116, -1, -1));

        jLabel4.setText("Ciudad");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 150, -1, -1));

        jLabel5.setText("State");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 194, -1, -1));

        jLabel6.setText("Teléfono");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 228, -1, -1));

        jLabel7.setText("Website");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 268, -1, -1));

        txtNameSuplier.setMinimumSize(new java.awt.Dimension(150, 28));
        jPanel2.add(txtNameSuplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 33, 241, -1));

        txtAddress.setMinimumSize(new java.awt.Dimension(150, 28));
        jPanel2.add(txtAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 73, 241, -1));

        txtZipCode.setMinimumSize(new java.awt.Dimension(150, 28));
        jPanel2.add(txtZipCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 113, 241, -1));

        txtCity.setMinimumSize(new java.awt.Dimension(150, 28));
        jPanel2.add(txtCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 147, 241, -1));

        txtState.setMinimumSize(new java.awt.Dimension(150, 28));
        jPanel2.add(txtState, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 191, 241, -1));

        txtPhonenNumber.setMinimumSize(new java.awt.Dimension(150, 28));
        jPanel2.add(txtPhonenNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 225, 241, -1));

        jLabel8.setText("email");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 302, -1, -1));

        txtWebsite.setMinimumSize(new java.awt.Dimension(150, 28));
        jPanel2.add(txtWebsite, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 265, 241, -1));

        txtEmail.setMinimumSize(new java.awt.Dimension(150, 28));
        jPanel2.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 299, 241, -1));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        btnNew.setText("Nuevo");
        jPanel2.add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 339, -1, -1));

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGrabar.setText("Grabar");
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });
        jPanel2.add(btnGrabar, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, -1, -1));

        jLabel1.setText("Proveedor");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 36, -1, -1));

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel2.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, -1, -1));
        jPanel2.add(txtIdSuplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, -1));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel2.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 400, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 370, 460));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbSuplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbSuplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbSuplierMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbSuplier);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 720, 320));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(378, 113, 740, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, -1, -1));

        btnReingresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/login.png"))); // NOI18N
        btnReingresar.setText("Activar");
        btnReingresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReingresarActionPerformed(evt);
            }
        });
        getContentPane().add(btnReingresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, -1, -1));

        btnInactivar.setText("Inactivar");
        btnInactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInactivarActionPerformed(evt);
            }
        });
        getContentPane().add(btnInactivar, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, -1, -1));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnExit.setText("Salir");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 10, -1, -1));

        btnGuia.setText("Guia");
        btnGuia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiaActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuia, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 10, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        Modificar(txtIdSuplier, txtNameSuplier, txtAddress, txtZipCode, txtCity, txtState, txtPhonenNumber, txtWebsite, txtEmail);
        MostrarProveedor("");
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        Insertar();
        MostrarProveedor("");
        txtIdSuplier.setText("");
        txtNameSuplier.setText("");
        txtAddress.setText("");
        txtZipCode.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtPhonenNumber.setText("");
        txtWebsite.setText("");
        txtEmail.setText("");
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        Seleccionar(tbSuplier, txtIdSuplier, txtNameSuplier, txtAddress, txtZipCode, txtCity, txtState, txtPhonenNumber, txtWebsite, txtEmail);
        Eliminar(txtIdSuplier);
        MostrarProveedor("");
        txtIdSuplier.setText("");
        txtNameSuplier.setText("");
        txtAddress.setText("");
        txtZipCode.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtPhonenNumber.setText("");
        txtWebsite.setText("");
        txtEmail.setText("");
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnReingresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReingresarActionPerformed
        int fila = tbSuplier.getSelectedRow();
        int id = Integer.parseInt(txtIdSuplier.getText());
        if (accion("Activo", id)) {
            JOptionPane.showMessageDialog(null, "Activado");
            MostrarProveedor("");
        } else {
            JOptionPane.showMessageDialog(null, "Error al Activar");
        }
    }//GEN-LAST:event_btnReingresarActionPerformed

    private void tbSuplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbSuplierMouseClicked
        Seleccionar(tbSuplier, txtIdSuplier, txtNameSuplier, txtAddress, txtZipCode, txtCity, txtState, txtPhonenNumber, txtWebsite, txtEmail);
    }//GEN-LAST:event_tbSuplierMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        txtNameSuplier.setText("");
        txtAddress.setText("");
        txtZipCode.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtPhonenNumber.setText("");
        txtWebsite.setText("");
        txtEmail.setText("");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnInactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivarActionPerformed
        int fila = tbSuplier.getSelectedRow();
        int id = Integer.parseInt(txtIdSuplier.getText());
        if (accion("Inactivo", id)) {
            JOptionPane.showMessageDialog(null, "Inactivado");
            MostrarProveedor("");
        } else {
            JOptionPane.showMessageDialog(null, "Error al Inactivar");
        }
    }//GEN-LAST:event_btnInactivarActionPerformed

    private void btnGuiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiaActionPerformed
        JOptionPane.showMessageDialog(null, "INGRESO DE PROVEEDORES\n"
                + "Botón NUEVO limpia las casillas y enfoca el cursor en nombre\n"
                + "Botón GRABAR: debe ingresar nuevos Proveedores (Todos los datos son Obligatorios) presionar botón Grabar\n"
                + "Ingresar el nombre del Proveedor, y sus datos\n"
                + "Botón CANCELAR solo limpia los datos ingresados en las casillas\n"
                + "Botón ELIMINAR para eliminar un Proveedor debe seleccionar en la tabla de Proveedores a eliminar\n"
                + "apareceran los datos en las casillas y presionar el botón Eliminar y se eliminara el Proveedor designado\n"
                + "Botón MODIFICAR seleccionar el Proveedor en la Tabla hacer la modificacion que desee y presionar Modificar\n"
                + "Botón ACTIVAR se activara el Proveedor Inactivado\n"
                + "Botón INACTIVAR se desactivara el Proveedor que dejara de trabajar con usted\n"
                + "Botón LIMPIAR limpia las casillas y las deja vacias\n"
                + "En la casilla Busqueda podra ir escribiendo el nombre y se ira filtrando el dato a buscar\n"
                + "Esta Plataforma es para Ingresar los PROVEEDORES con los que Trabajará");
    }//GEN-LAST:event_btnGuiaActionPerformed

    private void btnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarActionPerformed
        txtBuscar.setText("");
        MostrarProveedor("");
    }//GEN-LAST:event_btnMostrarActionPerformed

    private void txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyPressed
        BuscarProveedor(evt);
    }//GEN-LAST:event_txtBuscarKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnGuia;
    private javax.swing.JButton btnInactivar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnMostrar;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnReingresar;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbSuplier;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtIdSuplier;
    private javax.swing.JTextField txtNameSuplier;
    private javax.swing.JTextField txtPhonenNumber;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtWebsite;
    private javax.swing.JTextField txtZipCode;
    // End of variables declaration//GEN-END:variables
}
