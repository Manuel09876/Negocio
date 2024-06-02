package Administration;

import com.toedter.calendar.JDateChooser;
import conectar.Conectar;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.GregorianCalendar;

public class Trabajadores extends javax.swing.JInternalFrame {
//Variables

    DefaultTableModel model;

    int id;
    String tipoDocumento;
    String numDocumento;
    String Nombres, sexo, fechaNacimiento;
    int edad;
    String direccion;
    int zipCode;
    String ciudad, state, telefono, email, estado;

    public Trabajadores(int id, String tipoDocumento, String numDocumento, String Nombres, String sexo, String fechaNacimiento, int edad, String direccion, int zipCode, String ciudad, String state, String telefono, String email, String estado) {
        this.id = id;
        this.tipoDocumento = tipoDocumento;
        this.numDocumento = numDocumento;
        this.Nombres = Nombres;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.direccion = direccion;
        this.zipCode = zipCode;
        this.ciudad = ciudad;
        this.state = state;
        this.telefono = telefono;
        this.email = email;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String Nombres) {
        this.Nombres = Nombres;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public void BloquearCampos() {
        txtTipoDocumento.setEnabled(false);
        txtNumeroDocumento.setEnabled(false);
        txtNombres.setEnabled(false);
        txtEdad.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtZipCode.setEnabled(false);
        txtCiudad.setEnabled(false);
        txtState.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtEmail.setEnabled(false);

        btnGrabar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNuevo.setEnabled(true);
    }

    private void limpiarCajas() {

        txtTipoDocumento.setText("");
        txtNumeroDocumento.setText("");
        txtNombres.setText("");
        txtEdad.setText("");
        txtDireccion.setText("");
        txtZipCode.setText("");
        txtCiudad.setText("");
        txtState.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
    }

    void DesbloquearCampos() {
        txtTipoDocumento.setEnabled(true);
        txtNumeroDocumento.setEnabled(true);
        txtNombres.setEnabled(true);
        txtEdad.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtZipCode.setEnabled(true);
        txtCiudad.setEnabled(true);
        txtState.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtEmail.setEnabled(true);
        btnGrabar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnNuevo.setEnabled(false);
    }

    void CargarDatosTable(String Valores) {

        try {

            String[] titulosTabla = {"Id", "Documento", "NumeroDoc", "Nombres", "Sexo", "FechaNac", "Edad", "Direccion", 
                "ZipCode", "Ciudad", "Estado", "Telefono", "email", "Estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[14];                                   //Registros de la Basede Datos

            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla

            String ConsultaSQL = "select * from worker";

            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {
                RegistroBD[0] = result.getString("idWorker");
                RegistroBD[1] = result.getString("documentType");
                RegistroBD[2] = result.getString("documentNumber");
                RegistroBD[3] = result.getString("nombre");
                RegistroBD[4] = result.getString("sex");
                RegistroBD[5] = result.getString("bornDate");
                RegistroBD[6] = result.getString("age");
                RegistroBD[7] = result.getString("address");
                RegistroBD[8] = result.getString("zipCode");
                RegistroBD[9] = result.getString("city");
                RegistroBD[10] = result.getString("state");
                RegistroBD[11] = result.getString("cellphone");
                RegistroBD[12] = result.getString("email");
                RegistroBD[13] = result.getString("status");

                model.addRow(RegistroBD);
            }

            tbTrabajadores.setModel(model);
            tbTrabajadores.getColumnModel().getColumn(0).setPreferredWidth(20);
            tbTrabajadores.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(2).setPreferredWidth(80);
            tbTrabajadores.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbTrabajadores.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbTrabajadores.getColumnModel().getColumn(5).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(6).setPreferredWidth(50);
            tbTrabajadores.getColumnModel().getColumn(7).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(8).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(9).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(10).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(11).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(12).setPreferredWidth(100);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }

    public void Guardar() {
        //Variables
        String tipoDocumento;
        String numDocumento;
        String Nombres, sexo, fechaNacimiento;
        int edad;
        String direccion;
        int zipCode;
        String ciudad, state, telefono, email, estado;

        //Obtenemos la Información de la Caja de Texto
        tipoDocumento = txtTipoDocumento.getText();
        numDocumento = txtNumeroDocumento.getText();
        Nombres = txtNombres.getText();
        sexo = cbxSexo.getSelectedItem().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaNac = sdf.format(JDateFechaNac.getDate());
      //  fechaNacimiento = JDateFechaNac.getCalendar().toString();
        edad = Integer.parseInt(txtEdad.getText());
        direccion = txtDireccion.getText();
        zipCode = Integer.parseInt(txtZipCode.getText());
        ciudad = txtCiudad.getText();
        state = txtState.getText();
        telefono = txtTelefono.getText();
        email = txtEmail.getText();
        

        //Consulta sql para insertar los datos (nombres como en la base de datos)
        String sql = "INSERT INTO worker (documentType, documentNumber, nombre, sex, "
                + "bornDate, age, address, zipCode, city, state, cellphone, "
                + "email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //Para almacenar los datos empleo un try cash
        try {

            //prepara la coneccion para enviar al sql (Evita ataques al sql)
            PreparedStatement ps = connect.prepareStatement(sql);

            ps.setString(1, tipoDocumento);
            ps.setString(2, numDocumento);
            ps.setString(3, Nombres);
            ps.setString(4, sexo);
            ps.setString(5, fechaNac);
            ps.setInt(6, edad);
            ps.setString(7, direccion);
            ps.setInt(8, zipCode);
            ps.setString(9, ciudad);
            ps.setString(10, state);
            ps.setString(11, telefono);
            ps.setString(12, email);

            //Declara otra variable para validar los registros
            int n = ps.executeUpdate();

            //si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardo exitosamente");

                txtTipoDocumento.setText("");
                txtNumeroDocumento.setText("");
                txtNombres.setText("");
                cbxSexo.setSelectedItem("");
               // JDateFechaNac.setDate();
                txtDireccion.setText("");
                txtZipCode.setText("");
                txtCiudad.setText("");
                txtState.setText("");
                txtTelefono.setText("");
                txtEmail.setText("");
            }
            CargarDatosTable("");

        } catch (SQLException e) {
            Logger.getLogger(Tarifario.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "El registro NO se guardo exitosamente, Error " + e.toString());
        }

    }

    public void Eliminar(JTextField codigo) {

        setId(Integer.parseInt(codigo.getText()));

        String consulta = "DELETE from worker where idWorker=?";

        try {

            CallableStatement cs = objconexion.getConexion().prepareCall(consulta);
            cs.setInt(1, getId());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());

        }

    }

    public void SeleccionarTrabajador(JTable TablaTrabajador, JTextField Id, JTextField tipodeDocumento, JTextField numeroDocumento,
            JTextField Nombres, JComboBox Sexo, JDateChooser FechaNacimiento,
            JTextField Edad, JTextField Direccion, JTextField ZipCode, JTextField Ciudad,
            JTextField State, JTextField Telefono, JTextField Email) {

        int fila = TablaTrabajador.getSelectedRow();
        if (fila >= 0) {

            Id.setText(TablaTrabajador.getValueAt(fila, 0).toString());
            tipodeDocumento.setText(TablaTrabajador.getValueAt(fila, 1).toString());
            numeroDocumento.setText(TablaTrabajador.getValueAt(fila, 2).toString());
            Nombres.setText(TablaTrabajador.getValueAt(fila, 3).toString());
            Sexo.setSelectedItem(TablaTrabajador.getValueAt(fila, 4).toString());
            FechaNacimiento.setDateFormatString(TablaTrabajador.getValueAt(fila, 5).toString());
            Edad.setText(TablaTrabajador.getValueAt(fila, 6).toString());
            Direccion.setText(TablaTrabajador.getValueAt(fila, 7).toString());
            ZipCode.setText(TablaTrabajador.getValueAt(fila, 8).toString());
            Ciudad.setText(TablaTrabajador.getValueAt(fila, 9).toString());
            State.setText(TablaTrabajador.getValueAt(fila, 10).toString());
            Telefono.setText(TablaTrabajador.getValueAt(fila, 11).toString());
            Email.setText(TablaTrabajador.getValueAt(fila, 12).toString());

        } else {
            JOptionPane.showMessageDialog(null, "Fila No seleccionada");
        }
    }

    public void ModificarTrabajador(JTextField Id, JTextField tipodeDocumento, JTextField numeroDocumento,
            JTextField Nombres, JComboBox Sexo, JDateChooser FechaNacimiento,
            JTextField Edad, JTextField Direccion, JTextField ZipCode, JTextField Ciudad,
            JTextField State, JTextField Telefono, JTextField Email) {

        setId(Integer.parseInt(Id.getText()));
        setTipoDocumento(tipodeDocumento.getText());
        setNumDocumento(numeroDocumento.getText());
        setNombres(Nombres.getText());
        setSexo(Sexo.getSelectedItem().toString());
        setFechaNacimiento(FechaNacimiento.getDateFormatString());
        setEdad(Integer.parseInt(Edad.getText()));
        setDireccion(Direccion.getText());
        setZipCode(Integer.parseInt(ZipCode.getText()));
        setCiudad(Ciudad.getText());
        setState(State.getText());
        setTelefono(Telefono.getText());
        setEmail(Email.getText());

        String consulta = "UPDATE worker set documentType=?, documentNumber=?, nombre=?, sex=?, "
                + "bornDate=?, age=?, address=?, zipCode=?, city=?, state=?, cellphone=?, "
                + "email=? where idWorker=?";
        try {
            CallableStatement cs = objconexion.getConexion().prepareCall(consulta);
            cs.setString(1, getTipoDocumento());
            cs.setString(2, getNumDocumento());
            cs.setString(3, getNombres());
            cs.setString(4, getSexo());
            cs.setString(5, getFechaNacimiento());
            cs.setInt(6, getEdad());
            cs.setString(7, getDireccion());
            cs.setInt(8, getZipCode());
            cs.setString(9, getCiudad());
            cs.setString(10, getState());
            cs.setString(11, getTelefono());
            cs.setString(12, getEmail());
            cs.setInt(13, getId());
            cs.executeUpdate();
            JOptionPane.showMessageDialog(null, "Modificacion Exitosa");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se Modifico, error: " + e.toString());
        }
    }
    
    public void BuscarTrabajadores(java.awt.event.KeyEvent evt) {

        try {

            String[] titulosTabla = {"Id", "Documento", "NumeroDoc", "Nombres",
                "Sexo", "FechaNac", "Edad", "Direccion", "ZipCode",
                "Ciudad", "Estado", "Telefono", "email", "Estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[14];                                   //Registros de la Basede Datos

            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla

            String ConsultaSQL = "select * from worker WHERE nombre LIKE '%"+txtBuscarTrabajador.getText()+"%'";

            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {
                RegistroBD[0] = result.getString("idWorker");
                RegistroBD[1] = result.getString("documentType");
                RegistroBD[2] = result.getString("documentNumber");
                RegistroBD[3] = result.getString("nombre");
                RegistroBD[4] = result.getString("sex");
                RegistroBD[5] = result.getString("bornDate");
                RegistroBD[6] = result.getString("age");
                RegistroBD[7] = result.getString("address");
                RegistroBD[8] = result.getString("zipCode");
                RegistroBD[9] = result.getString("city");
                RegistroBD[10] = result.getString("state");
                RegistroBD[11] = result.getString("cellphone");
                RegistroBD[12] = result.getString("email");
                RegistroBD[13] = result.getString("status");

                model.addRow(RegistroBD);
            }
           
            tbTrabajadores.setModel(model);
            tbTrabajadores.getColumnModel().getColumn(0).setPreferredWidth(20);
            tbTrabajadores.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(2).setPreferredWidth(80);
            tbTrabajadores.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbTrabajadores.getColumnModel().getColumn(4).setPreferredWidth(80);
            tbTrabajadores.getColumnModel().getColumn(5).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(6).setPreferredWidth(50);
            tbTrabajadores.getColumnModel().getColumn(7).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(8).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(9).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(10).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(11).setPreferredWidth(100);
            tbTrabajadores.getColumnModel().getColumn(12).setPreferredWidth(100);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }
    
    

    public Trabajadores() {
        initComponents();

        CargarDatosTable("");
        txtId.setEnabled(false);
        txtTipoDocumento.requestFocus();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        tbTrabajador = new javax.swing.JScrollPane();
        tbTrabajadores = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        txtBuscarTrabajador = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        txtEdad = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNumeroDocumento = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtZipCode = new javax.swing.JTextField();
        txtCiudad = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtTipoDocumento = new javax.swing.JTextField();
        txtId = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbxSexo = new javax.swing.JComboBox<>();
        JDateFechaNac = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        txtState = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Worker");
        setPreferredSize(new java.awt.Dimension(800, 500));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbTrabajador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbTrabajadorMouseClicked(evt);
            }
        });

        tbTrabajadores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbTrabajadores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbTrabajadoresMouseClicked(evt);
            }
        });
        tbTrabajador.setViewportView(tbTrabajadores);

        jPanel1.add(tbTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 980, 290));

        jLabel18.setText("Descripción");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        txtBuscarTrabajador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarTrabajadorKeyPressed(evt);
            }
        });
        jPanel1.add(txtBuscarTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 266, -1));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        btnBuscar.setText("Limpiar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 1020, 400));

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Trabajadores"));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Tipo de Documento");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 50, -1, -1));
        jPanel2.add(txtNombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 103, 160, -1));
        jPanel2.add(txtEdad, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 200, 113, -1));

        jLabel3.setText("Numero de Documento");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 78, -1, -1));

        jLabel6.setText("Apellidos y Nombre");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        jLabel7.setText("Sexo");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jLabel8.setText("Fecha de Nacimiento");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel9.setText("Edad");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));
        jPanel2.add(txtNumeroDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 75, 110, -1));
        jPanel2.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 240, 149, -1));

        jLabel11.setText("Dirección");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));
        jPanel2.add(txtZipCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 270, 122, -1));
        jPanel2.add(txtCiudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, 122, -1));

        jLabel12.setText("Zip Code");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        jLabel13.setText("Ciudad");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, -1, -1));
        jPanel2.add(txtTipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 47, 113, -1));
        jPanel2.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 18, 113, -1));

        jLabel1.setText("id Trabajador");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 21, -1, -1));

        cbxSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Femenino" }));
        cbxSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSexoActionPerformed(evt);
            }
        });
        jPanel2.add(cbxSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 110, -1));

        JDateFechaNac.setDateFormatString("yyyy-MM-dd");
        jPanel2.add(JDateFechaNac, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 120, -1));

        jLabel14.setText("Estado");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, -1, -1));
        jPanel2.add(txtState, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 330, 130, -1));

        jLabel15.setText("Teléfono");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, -1));
        jPanel2.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 360, 116, -1));

        jLabel16.setText("email");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 54, -1));

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        jPanel2.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 400, 116, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 360, 440));

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/new product.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 420, -1, -1));

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGrabar.setText("Grabar");
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGrabar, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 420, -1, -1));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 420, -1, -1));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 420, -1, -1));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1230, 420, 100, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 420, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed

        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        BloquearCampos();
        limpiarCajas();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed

        ModificarTrabajador(txtId, txtTipoDocumento, txtNumeroDocumento, txtNombres, cbxSexo, JDateFechaNac, txtEdad, txtDireccion, txtZipCode, txtCiudad, txtState, txtTelefono, txtEmail);
        CargarDatosTable("");
        limpiarCajas();

    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        DesbloquearCampos();
        limpiarCajas();
        txtTipoDocumento.requestFocus();

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed

        Guardar();
        limpiarCajas();
        CargarDatosTable("");
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        txtBuscarTrabajador.setText("");
        CargarDatosTable("");
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void tbTrabajadorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbTrabajadorMouseClicked
        SeleccionarTrabajador(tbTrabajadores, txtId, txtTipoDocumento, txtNumeroDocumento, txtNombres, cbxSexo, JDateFechaNac, txtEdad, txtDireccion, txtZipCode, txtCiudad, txtState, txtTelefono, txtEmail);
    }//GEN-LAST:event_tbTrabajadorMouseClicked

    private void tbTrabajadoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbTrabajadoresMouseClicked

    }//GEN-LAST:event_tbTrabajadoresMouseClicked

    private void cbxSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxSexoActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        SeleccionarTrabajador(tbTrabajadores, txtId, txtTipoDocumento, txtNumeroDocumento, txtNombres, cbxSexo, JDateFechaNac, txtEdad, txtDireccion, txtZipCode, txtCiudad, txtState, txtTelefono, txtEmail);
        Eliminar(txtId);
        CargarDatosTable("");
        limpiarCajas();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtBuscarTrabajadorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarTrabajadorKeyPressed
        BuscarTrabajadores(evt);
    }//GEN-LAST:event_txtBuscarTrabajadorKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser JDateFechaNac;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxSexo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane tbTrabajador;
    private javax.swing.JTable tbTrabajadores;
    private javax.swing.JTextField txtBuscarTrabajador;
    private javax.swing.JTextField txtCiudad;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtEdad;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtNumeroDocumento;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTipoDocumento;
    private javax.swing.JTextField txtZipCode;
    // End of variables declaration//GEN-END:variables

    Conectar objconexion = new Conectar();
    Connection connect = objconexion.getConexion();
}
