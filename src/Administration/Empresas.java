package Administration;

import com.toedter.calendar.JDateChooser;
import conectar.Conectar;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.table.DefaultTableModel;

public class Empresas extends javax.swing.JInternalFrame {

    String filtro;

    //Variable para la tabla
    DefaultTableModel model;

    int idBusiness;
    String nameBusiness;
    String addressBusiness;
    int zipCode;
    String city, state, nameOwner, cellPhoneOwner, emailOwner, nameContact, cellPhoneContact, emailContact, webpageBusiness, estado;
    Date dateStart;

    public Empresas(int idBusiness, String nameBusiness, String addressBusiness, int zipCode, String city, String state, String nameOwner, String cellPhoneOwner, String emailOwner, String nameContact, String cellPhoneContact, String emailContact, String webpageBusiness, String estado, Date dateStart) {
        this.idBusiness = idBusiness;
        this.nameBusiness = nameBusiness;
        this.addressBusiness = addressBusiness;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
        this.nameOwner = nameOwner;
        this.cellPhoneOwner = cellPhoneOwner;
        this.emailOwner = emailOwner;
        this.nameContact = nameContact;
        this.cellPhoneContact = cellPhoneContact;
        this.emailContact = emailContact;
        this.webpageBusiness = webpageBusiness;
        this.estado = estado;
        this.dateStart = dateStart;
    }

    public int getIdBusiness() {
        return idBusiness;
    }

    public void setIdBusiness(int idBusiness) {
        this.idBusiness = idBusiness;
    }

    public String getNameBusiness() {
        return nameBusiness;
    }

    public void setNameBusiness(String nameBusiness) {
        this.nameBusiness = nameBusiness;
    }

    public String getAddressBusiness() {
        return addressBusiness;
    }

    public void setAddressBusiness(String nameAddress) {
        this.addressBusiness = nameAddress;
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

    public String getNameOwner() {
        return nameOwner;
    }

    public void setNameOwner(String nameOwner) {
        this.nameOwner = nameOwner;
    }

    public String getCellPhoneOwner() {
        return cellPhoneOwner;
    }

    public void setCellPhoneOwner(String cellPhoneOwner) {
        this.cellPhoneOwner = cellPhoneOwner;
    }

    public String getEmailOwner() {
        return emailOwner;
    }

    public void setEmailOwner(String emailOwner) {
        this.emailOwner = emailOwner;
    }

    public String getNameContact() {
        return nameContact;
    }

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }

    public String getCellPhoneContact() {
        return cellPhoneContact;
    }

    public void setCellPhoneContact(String cellPhoneContact) {
        this.cellPhoneContact = cellPhoneContact;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getWebpageBusiness() {
        return webpageBusiness;
    }

    public void setWebpageBusiness(String webpageBusiness) {
        this.webpageBusiness = webpageBusiness;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    PreparedStatement ps;

    //Constructores de Metodos que vamos a necesitar
    //Realización de los Métodos
    public void BloquearCampos() {
        txtId.setEnabled(false);
        txtBusinessName.setEnabled(false);
        txtAddress.setEnabled(false);
        txtZipCode.setEnabled(false);
        txtCity.setEnabled(false);
        txtState.setEnabled(false);
        txtNameOwner.setEnabled(false);
        txtCellphoneOwner.setEnabled(false);
        txtEmailOwner.setEnabled(false);
        txtNameContact.setEnabled(false);
        txtCellphoneContact.setEnabled(false);
        txtEmailContact.setEnabled(false);
        txtWebsiteBusiness.setEnabled(false);
        calendar.setEnabled(false);

        btnNuevo.setEnabled(true);
        btnAgregar.setEnabled(false);

        btnCancelar.setEnabled(false);

    }

    void LimpiarCajasTexto() {

        txtBusinessName.setText("");
        txtAddress.setText("");
        txtZipCode.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtNameOwner.setText("");
        txtCellphoneOwner.setText("");
        txtEmailOwner.setText("");
        txtNameContact.setText("");
        txtCellphoneContact.setText("");
        txtEmailContact.setText("");
        txtWebsiteBusiness.setText("");
        JDateChooser calendar = new JDateChooser();
        calendar.setCalendar(null);

    }

    void DesbloquearCampos() {

        txtBusinessName.setEnabled(true);
        txtAddress.setEnabled(true);
        txtZipCode.setEnabled(true);
        txtCity.setEnabled(true);
        txtState.setEnabled(true);
        txtNameOwner.setEnabled(true);
        txtCellphoneOwner.setEnabled(true);
        txtEmailOwner.setEnabled(true);
        txtNameContact.setEnabled(true);
        txtCellphoneContact.setEnabled(true);
        txtEmailContact.setEnabled(true);
        txtWebsiteBusiness.setEnabled(true);
        calendar.setEnabled(true);

        btnNuevo.setEnabled(false);
        btnAgregar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnCancelar.setEnabled(true);

    }

    //Es lo mismo que mostrar Tabla Clientes
    void CargarDatosTable(String Valores) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida aquí
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
    
        try {

            String[] titulosTabla = {"Código", "Empresa", "Dirección", "ZipCode", "Ciudad", "Estado",
                "Propietario", "Celular", "email", "Contacto", "celular", "email", "Website", "Fecha Inicio", "Estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[16];                                   //Registros de la Basede Datos

            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla

            String ConsultaSQL = "select * from bussiness";

                Statement st = connection.createStatement();
                ResultSet result = st.executeQuery(ConsultaSQL);

                while (result.next()) {
                    RegistroBD[0] = result.getString("idBusiness");
                    RegistroBD[1] = result.getString("nameBusiness");
                    RegistroBD[2] = result.getString("addressBusiness");
                    RegistroBD[3] = result.getString("zipCode");
                    RegistroBD[4] = result.getString("city");
                    RegistroBD[5] = result.getString("state");
                    RegistroBD[6] = result.getString("nameOwner");
                    RegistroBD[7] = result.getString("cellPhoneOwner");
                    RegistroBD[8] = result.getString("emailOwner");
                    RegistroBD[9] = result.getString("nameContact");
                    RegistroBD[10] = result.getString("cellPhoneContact");
                    RegistroBD[11] = result.getString("emailContact");
                    RegistroBD[12] = result.getString("webpageBusiness");
                    RegistroBD[13] = result.getString("dateStart");
                    RegistroBD[14] = result.getString("estado");

                    model.addRow(RegistroBD);
                }

                tbEmpresa.setModel(model);
                tbEmpresa.getColumnModel().getColumn(0).setPreferredWidth(30);
                tbEmpresa.getColumnModel().getColumn(1).setPreferredWidth(100);
                tbEmpresa.getColumnModel().getColumn(2).setPreferredWidth(200);
                tbEmpresa.getColumnModel().getColumn(3).setPreferredWidth(80);
                tbEmpresa.getColumnModel().getColumn(4).setPreferredWidth(100);
                tbEmpresa.getColumnModel().getColumn(5).setPreferredWidth(100);
                tbEmpresa.getColumnModel().getColumn(6).setPreferredWidth(150);
                tbEmpresa.getColumnModel().getColumn(7).setPreferredWidth(100);
                tbEmpresa.getColumnModel().getColumn(8).setPreferredWidth(200);
                tbEmpresa.getColumnModel().getColumn(9).setPreferredWidth(150);
                tbEmpresa.getColumnModel().getColumn(10).setPreferredWidth(100);
                tbEmpresa.getColumnModel().getColumn(11).setPreferredWidth(100);
                tbEmpresa.getColumnModel().getColumn(12).setPreferredWidth(150);
                tbEmpresa.getColumnModel().getColumn(13).setPreferredWidth(150);
            
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    void Guardar() {
        Connection connection = null;
        // Variables
        int idBusiness;
        String nameBusiness;
        String addressBusiness;
        int zipCode;
        String city, state, nameOwner, cellPhoneOwner, emailOwner, nameContact, cellPhoneContact, emailContact, webpageBusiness, dateStart, estado;

        String sql = "";

        //Obtenemos la informacion de las cajas de texto
        nameBusiness = txtBusinessName.getText();
        addressBusiness = txtAddress.getText();
        zipCode = Integer.parseInt(txtZipCode.getText());
        city = txtCity.getText();
        state = txtState.getText();
        nameOwner = txtNameOwner.getText();
        cellPhoneOwner = txtCellphoneOwner.getText();
        emailOwner = txtEmailOwner.getText();
        nameContact = txtNameContact.getText();
        cellPhoneContact = txtCellphoneContact.getText();
        emailContact = txtEmailContact.getText();
        webpageBusiness = txtWebsiteBusiness.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(calendar.getDate());

        //Consulta para evitar duplicados
        String consulta = "SELECT * FROM bussiness WHERE nameBusiness = ?";

        //Consulta sql para insertar los datos (nombres como en la base de datos)
        sql = "INSERT INTO bussiness (nameBusiness, addressBusiness, zipCode, city, state, nameOwner, "
                + "cellPhoneOwner, emailOwner, nameContact, cellPhoneContact, emailContact, webpageBusiness, "
                + "dateStart)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        //Para almacenar los datos empleo un try cash
        try {
            connection = Conectar.getInstancia().obtenerConexion();

            //prepara la coneccion para enviar al sql (Evita ataques al sql)
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, nameBusiness);
            pst.setString(2, addressBusiness);
            pst.setInt(3, zipCode);
            pst.setString(4, city);
            pst.setString(5, state);
            pst.setString(6, nameOwner);
            pst.setString(7, cellPhoneOwner);
            pst.setString(8, emailOwner);
            pst.setString(9, nameContact);
            pst.setString(10, cellPhoneContact);
            pst.setString(11, emailContact);
            pst.setString(12, webpageBusiness);
            pst.setString(13, date);

            //Declara otra variable para validar los registros
            int n = pst.executeUpdate();

            //si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardo exitosamente");

                //Luego Bloquera campos
                BloquearCampos();
            } else {
                JOptionPane.showMessageDialog(null, "El registro NO se guardo exitosamente");
            }
            CargarDatosTable("");

        } catch (SQLException ex) {
            Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void Eliminar(JTextField codigo) {
        Connection connection = null;
        setIdBusiness(Integer.parseInt(codigo.getText()));

        String consulta = "DELETE from bussiness where idBusiness=?";

        try {
            connection = Conectar.getInstancia().obtenerConexion();

            CallableStatement cs = connection.prepareCall(consulta);
            cs.setInt(1, getIdBusiness());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());

        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void SeleccionarEmpresa(JTable TablaEmpresa, JTextField id, JTextField nameBusiness,
            JTextField address, JTextField zipCode, JTextField city, JTextField state, JTextField nameOwner,
            JTextField cellPhoneOwner, JTextField emailOwner, JTextField nameContact, JTextField cellphoneContact,
            JTextField emailContact, JTextField webpageBusiness, JDateChooser fecha) {

        try {

            int fila = TablaEmpresa.getSelectedRow();

            if (fila >= 0) {

                id.setText(TablaEmpresa.getValueAt(fila, 0).toString());
                nameBusiness.setText(TablaEmpresa.getValueAt(fila, 1).toString());
                address.setText(TablaEmpresa.getValueAt(fila, 2).toString());
                zipCode.setText(TablaEmpresa.getValueAt(fila, 3).toString());
                city.setText(TablaEmpresa.getValueAt(fila, 4).toString());
                state.setText(TablaEmpresa.getValueAt(fila, 5).toString());
                nameOwner.setText(TablaEmpresa.getValueAt(fila, 6).toString());
                cellPhoneOwner.setText(TablaEmpresa.getValueAt(fila, 7).toString());
                emailOwner.setText(TablaEmpresa.getValueAt(fila, 8).toString());
                nameContact.setText(TablaEmpresa.getValueAt(fila, 9).toString());
                cellphoneContact.setText(TablaEmpresa.getValueAt(fila, 10).toString());
                emailContact.setText(TablaEmpresa.getValueAt(fila, 11).toString());
                webpageBusiness.setText(TablaEmpresa.getValueAt(fila, 12).toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato de fecha de la tabla
                String fechaString = TablaEmpresa.getValueAt(fila, 13).toString(); // Obtener la fecha como String de la tabla
                Date fechaDate = sdf.parse(fechaString); // Convertir el String a un objeto Date
                fecha.setDate(fechaDate); // Establecer la fecha en el JDateChooser

            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }
        } catch (HeadlessException | ParseException e) {
            JOptionPane.showMessageDialog(null, "Error de Seleccion, Error: " + e.toString());
        }
    }

    public void ModificarEmpresa(JTextField id, JTextField empresa, JTextField direccion,
            JTextField zipCode, JTextField ciudad, JTextField estado, JTextField duenio, JTextField telefonod,
            JTextField emaild, JTextField contacto, JTextField telefonoc, JTextField emailc, JTextField web, JDateChooser fecha) {

        Connection connection = null;
        // Convertir las fechas a LocalDate
        // Por ejemplo, asumiendo que las fechas son almacenadas en formato dd-MM-yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        setIdBusiness(Integer.parseInt(id.getText()));
        setNameBusiness(empresa.getText());
        setAddressBusiness(direccion.getText());
        setZipCode(Integer.parseInt(zipCode.getText()));
        setCity(ciudad.getText());
        setState(estado.getText());
        setNameOwner(duenio.getText());
        setCellPhoneOwner(telefonod.getText());
        setEmailOwner(emaild.getText());
        setNameContact(contacto.getText());
        setCellPhoneContact(telefonoc.getText());
        setEmailContact(emailc.getText());
        setWebpageBusiness(web.getText());
        setDateStart(fecha.getDate()); //Obtenemos la fecha del JDateChooser

        String consulta = "UPDATE bussiness SET nameBusiness=?, addressBusiness=?, "
                + "zipCode=?, city=?, state=?, nameOwner=?, cellPhoneOwner=?, emailOwner=?, nameContact=?, "
                + "cellPhoneContact=?, emailContact=?, webpageBusiness=?, dateStart=? Where idBusiness=?";

        try {
            connection = Conectar.getInstancia().obtenerConexion();

            CallableStatement cs = connection.prepareCall(consulta);

            cs.setString(1, getNameBusiness());
            cs.setString(2, getAddressBusiness());
            cs.setInt(3, getZipCode());
            cs.setString(4, getCity());
            cs.setString(5, getState());
            cs.setString(6, getNameOwner());
            cs.setString(7, getCellPhoneOwner());
            cs.setString(8, getEmailOwner());
            cs.setString(9, getNameContact());
            cs.setString(10, getCellPhoneContact());
            cs.setString(11, getEmailContact());
            cs.setString(12, getWebpageBusiness());
            // Convertir la fecha de tipo java.util.Date a java.sql.Date
            Date fechaSqlDate = new java.sql.Date(getDateStart().getTime());

            cs.setDate(13, (java.sql.Date) fechaSqlDate);
            cs.setInt(14, getIdBusiness());

            cs.executeUpdate();
            JOptionPane.showMessageDialog(null, "Modificacion Exitosa");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se ejecutó la Modificacion, Error " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public Empresas BuscarEmp(java.awt.event.KeyEvent evt) {
        Connection connection = null;
        String[] titulosTabla = {"Código", "Empresa", "Dirección", "ZipCode", "Ciudad", "Estado",
            "Propietario", "Celular", "email", "Contacto", "celular", "email", "Website", "Fecha Inicio", "Estado"}; //Titulos de la Tabla
        String[] RegistroBD = new String[16];

        Empresas empresa = new Empresas();
        String sql = "SELECT * FROM bussiness WHERE nameBusiness LIKE '%" + txtBuscarEmpresa.getText() + "%'";
        model = new DefaultTableModel(null, titulosTabla);
        try {
            connection = Conectar.getInstancia().obtenerConexion();

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                RegistroBD[0] = rs.getString("idBusiness");
                RegistroBD[1] = rs.getString("nameBusiness");
                RegistroBD[2] = rs.getString("addressBusiness");
                RegistroBD[3] = rs.getString("zipCode");
                RegistroBD[4] = rs.getString("city");
                RegistroBD[5] = rs.getString("state");
                RegistroBD[6] = rs.getString("nameOwner");
                RegistroBD[7] = rs.getString("cellPhoneOwner");
                RegistroBD[8] = rs.getString("emailOwner");
                RegistroBD[9] = rs.getString("nameContact");
                RegistroBD[10] = rs.getString("cellPhoneContact");
                RegistroBD[11] = rs.getString("emailContact");
                RegistroBD[12] = rs.getString("webpageBusiness");
                RegistroBD[13] = rs.getString("dateStart");
                RegistroBD[14] = rs.getString("estado");
                model.addRow(RegistroBD);
            }
            tbEmpresa.setModel(model);
            tbEmpresa.getColumnModel().getColumn(0).setPreferredWidth(30);
            tbEmpresa.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbEmpresa.getColumnModel().getColumn(2).setPreferredWidth(200);
            tbEmpresa.getColumnModel().getColumn(3).setPreferredWidth(80);
            tbEmpresa.getColumnModel().getColumn(4).setPreferredWidth(100);
            tbEmpresa.getColumnModel().getColumn(5).setPreferredWidth(100);
            tbEmpresa.getColumnModel().getColumn(6).setPreferredWidth(150);
            tbEmpresa.getColumnModel().getColumn(7).setPreferredWidth(100);
            tbEmpresa.getColumnModel().getColumn(8).setPreferredWidth(200);
            tbEmpresa.getColumnModel().getColumn(9).setPreferredWidth(150);
            tbEmpresa.getColumnModel().getColumn(10).setPreferredWidth(100);
            tbEmpresa.getColumnModel().getColumn(11).setPreferredWidth(100);
            tbEmpresa.getColumnModel().getColumn(12).setPreferredWidth(150);
            tbEmpresa.getColumnModel().getColumn(13).setPreferredWidth(150);
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
        return empresa;
    }

    public boolean accion(String estado, int idBusiness) {
        Connection connection = null;
        String sql = "UPDATE bussiness SET estado = ? WHERE idBusiness = ?";
        try {
            connection = Conectar.getInstancia().obtenerConexion();

            ps = connection.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, idBusiness);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public Empresas() {
        initComponents();

        txtId.setEnabled(false);
        CargarDatosTable("");

        // Configurar el JDateChooser para mostrar la fecha actual al abrir la aplicación
        JDateChooser fechaChooser = new JDateChooser();
        fechaChooser.setDate(new Date()); // Establecer la fecha actual

// También puedes personalizar el formato de la fecha
        fechaChooser.setDateFormatString("dd-MM-yyyy");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbEmpresa = new javax.swing.JTable();
        btnLimpiar = new javax.swing.JButton();
        txtBuscarEmpresa = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        btnInactivar = new javax.swing.JButton();
        btnGuia = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtZipCode = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtState = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNameOwner = new javax.swing.JTextField();
        txtCellphoneOwner = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtEmailOwner = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNameContact = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCellphoneContact = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtEmailContact = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtWebsiteBusiness = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtBusinessName = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        calendar = new com.toedter.calendar.JDateChooser();
        txtId = new javax.swing.JTextField();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Empresas");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        tbEmpresa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbEmpresa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbEmpresaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbEmpresa);

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        txtBuscarEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarEmpresaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarEmpresaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarEmpresaKeyTyped(evt);
            }
        });

        jLabel17.setText("Descripción");

        jLabel16.setText("Busqueda de Empresas");

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/new product.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

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

        btnGuia.setText("Guia");
        btnGuia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel17)
                        .addGap(45, 45, 45)
                        .addComponent(txtBuscarEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(208, 208, 208)
                        .addComponent(jLabel16)))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLimpiar)
                    .addComponent(btnAgregar))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnModificar)
                    .addComponent(btnNuevo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminar)
                    .addComponent(btnCancelar))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnActivar)
                    .addComponent(btnInactivar))
                .addGap(56, 56, 56)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuia)
                    .addComponent(btnSalir))
                .addGap(34, 34, 34))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEliminar)
                            .addComponent(btnAgregar)
                            .addComponent(btnSalir)
                            .addComponent(btnModificar)
                            .addComponent(btnActivar))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnNuevo)
                                    .addComponent(btnLimpiar))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnGuia)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtBuscarEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))
                                .addGap(50, 50, 50))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnInactivar)
                                    .addComponent(btnCancelar))
                                .addGap(18, 18, 18)))))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 1130, 290));

        jPanel2.setBackground(new java.awt.Color(204, 255, 255));

        txtZipCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtZipCodeActionPerformed(evt);
            }
        });

        jLabel2.setText("Nombre Empresa");

        jLabel4.setText("Dirección");

        jLabel5.setText("Zip Code");

        jLabel6.setText("Ciudad");

        jLabel7.setText("State");

        jLabel8.setText("Name Owner");

        txtWebsiteBusiness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWebsiteBusinessActionPerformed(evt);
            }
        });

        jLabel9.setText("Cellphone Owner");

        jLabel10.setText("email Owner");

        jLabel11.setText("Nombre Contacto");

        jLabel12.setText("Celular Contacto");

        jLabel13.setText("email Contacto");

        jLabel14.setText("Website");

        jLabel15.setText("Fecha de Inicio");

        txtAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddressActionPerformed(evt);
            }
        });

        calendar.setDateFormatString("dd-MM-yyyy");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(txtZipCode, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtBusinessName, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(txtAddress, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCity, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCellphoneOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNameOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(51, 51, 51)
                        .addComponent(txtEmailOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCellphoneContact, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmailContact, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                            .addComponent(txtNameContact))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15))
                    .addComponent(txtWebsiteBusiness, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(calendar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15)
                            .addComponent(calendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txtNameOwner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txtCellphoneOwner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(txtNameContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(txtCellphoneContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(txtEmailContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtWebsiteBusiness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(txtEmailOwner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBusinessName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtZipCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(341, 341, 341))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1140, 170));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtZipCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtZipCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtZipCodeActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        Guardar();
        LimpiarCajasTexto();
        CargarDatosTable("");
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        DesbloquearCampos();
        LimpiarCajasTexto();
        txtBusinessName.requestFocus();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        ModificarEmpresa(txtId, txtBusinessName, txtAddress, txtZipCode, txtCity, txtState, txtNameOwner, txtCellphoneOwner, txtEmailOwner, txtNameContact, txtCellphoneContact, txtEmailContact, txtWebsiteBusiness, calendar);
        CargarDatosTable("");
        LimpiarCajasTexto();


    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        BloquearCampos();
        LimpiarCajasTexto();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        SeleccionarEmpresa(tbEmpresa, txtId, txtBusinessName, txtAddress, txtZipCode, txtCity, txtState, txtNameOwner, txtCellphoneOwner, txtEmailOwner, txtNameContact, txtCellphoneContact, txtEmailContact, txtWebsiteBusiness, calendar);
        Eliminar(txtId);
        CargarDatosTable("");
        LimpiarCajasTexto();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tbEmpresaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbEmpresaMouseClicked
        SeleccionarEmpresa(tbEmpresa, txtId, txtBusinessName, txtAddress, txtZipCode, txtCity, txtState, txtNameOwner, txtCellphoneOwner, txtEmailOwner, txtNameContact, txtCellphoneContact, txtEmailContact, txtWebsiteBusiness, calendar);
    }//GEN-LAST:event_tbEmpresaMouseClicked

    private void txtWebsiteBusinessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWebsiteBusinessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWebsiteBusinessActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        LimpiarCajasTexto();
        txtBusinessName.requestFocus();
        CargarDatosTable("");

    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtBuscarEmpresaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmpresaKeyTyped

    }//GEN-LAST:event_txtBuscarEmpresaKeyTyped

    private void txtBuscarEmpresaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmpresaKeyReleased

    }//GEN-LAST:event_txtBuscarEmpresaKeyReleased

    private void txtBuscarEmpresaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmpresaKeyPressed
        BuscarEmp(evt);
    }//GEN-LAST:event_txtBuscarEmpresaKeyPressed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        int fila = tbEmpresa.getSelectedRow();
        int id = Integer.parseInt(txtId.getText());
        if (accion("Activo", id)) {
            JOptionPane.showMessageDialog(null, "Activado");
            CargarDatosTable("");
        } else {
            JOptionPane.showMessageDialog(null, "Error al Activar");
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void btnInactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivarActionPerformed
        int fila = tbEmpresa.getSelectedRow();
        int id = Integer.parseInt(txtId.getText());
        if (accion("Inactivo", id)) {
            JOptionPane.showMessageDialog(null, "Inactivado");
            CargarDatosTable("");
        } else {
            JOptionPane.showMessageDialog(null, "Error al Inactivar");
        }
    }//GEN-LAST:event_btnInactivarActionPerformed

    private void btnGuiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiaActionPerformed
        JOptionPane.showMessageDialog(null, "INGRESO DE EMPRESAS\n"
                + "Botón GRABAR: debe ingresar nuevas Empresas (Todos los datos son Obligatorios puede omitir los del contacto) y presionar botón Grabar\n"
                + "Botón CANCELAR solo limpia los datos ingresados en las casillas\n"
                + "Botón ELIMINAR para eliminar una Empresa debe seleccionar en la tabla la Empresa a eliminar\n"
                + "apareceran los datos en las casillas y presionar el botón Eliminar y se eliminara la empresa designada\n"
                + "Botón MODIFICAR seleccionar la Empresa en la Tabla hacer la modificacion que desee y presionar Modificar\n"
                + "Botón ACTIVAR se activaran las Empresas Inactivados\n"
                + "Botón INACTIVAR se desactivaran las Empresas que dejaran de trabajar con usted\n"
                + "Botón Limpiar limpia las casillas y las deja vacias\n"
                + "En la casilla Busqueda podra ir escribiendo el nombre y se ira filtrando el dato a buscar\n"
                + "Esta Plataforma es para Ingresar Empresas con las que Trabajará");
    }//GEN-LAST:event_btnGuiaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnActivar;
    public javax.swing.JButton btnAgregar;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuia;
    public javax.swing.JButton btnInactivar;
    public javax.swing.JButton btnLimpiar;
    public javax.swing.JButton btnModificar;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private com.toedter.calendar.JDateChooser calendar;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbEmpresa;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBuscarEmpresa;
    private javax.swing.JTextField txtBusinessName;
    private javax.swing.JTextField txtCellphoneContact;
    private javax.swing.JTextField txtCellphoneOwner;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtEmailContact;
    private javax.swing.JTextField txtEmailOwner;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNameContact;
    private javax.swing.JTextField txtNameOwner;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtWebsiteBusiness;
    private javax.swing.JTextField txtZipCode;
    // End of variables declaration//GEN-END:variables
}
