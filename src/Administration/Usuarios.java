package Administration;

import Bases.Combo;
import conectar.Conectar;
import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Usuarios extends javax.swing.JInternalFrame {

    String filtro;

    // Variables
    DefaultTableModel model;
    int idUsuarios;
    String nombre, usuario, password;
    int id_rol;
    String estado;

    public Usuarios(int idUsuarios, String nombre, String usuario, String password, int tipUsu, String estado) {
        this.idUsuarios = idUsuarios;
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
        this.id_rol = tipUsu;
        this.estado = estado;
    }

    public int getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(int idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuarios() {
        initComponents();

        txtIdUsuario.setEnabled(false);
        AutoCompleteDecorator.decorate(cbxTipoDeUsuario);
        AutoCompleteDecorator.decorate(cbxTrabajador);
        MostrarCTipoDeUsuario(cbxTipoDeUsuario);
        MostrarTrabajador(cbxTrabajador);
        txtIdTPU.setVisible(false);
        rdRelacionar = new javax.swing.JRadioButton();
        rdRelacionar.setText("Trabajador");
        CargarDatosTable("");
    }

    // Conexión
    Conectar con = new Conectar();
    Connection connect = con.getConexion();
    PreparedStatement ps;
    ResultSet rs;

    // Constructores de Metodos que vamos a necesitar
    // Realización de los Métodos
    public void BloquearCampos() {
        txtIdUsuario.setEnabled(false);
        txtNombre.setEnabled(false);
        cbxTipoDeUsuario.setEnabled(false);
        txtUsuario.setEnabled(false);
        txtPassword.setEnabled(false);

        btnNuevo.setEnabled(true);
        btnGrabar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnCancelar.setEnabled(false);

    }

    public void LimpiarCajasTexto() {

        txtIdUsuario.setText("");
        txtNombre.setText("");
        cbxTipoDeUsuario.getSelectedItem();
        txtUsuario.setText("");
        txtPassword.setText("");

    }

    public void DesbloquearCampos() {

        txtIdUsuario.setEnabled(true);
        txtNombre.setEnabled(true);
        txtUsuario.setEnabled(true);
        txtPassword.setEnabled(true);
        cbxTipoDeUsuario.setEnabled(true);

        btnNuevo.setEnabled(false);
        btnGrabar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnCancelar.setEnabled(true);

    }

    // Es lo mismo que mostrar Tabla Clientes
    public void CargarDatosTable(String Valores) {
        try {
            String[] titulosTabla = {"id", "Nick", "Usuario", "Password", "Rol", "Estado"}; // Titulos de la Tabla
            String[] RegistroBD = new String[6]; // Registros de la Base de Datos
            model = new DefaultTableModel(null, titulosTabla); // Le pasamos los titulos a la tabla
            String ConsultaSQL = "SELECT u.idUsuarios AS id, u.nombre AS Nick, u.usuario AS Usuario, "
                    + "u.password AS Contrasenia, r.nombre AS Rol, u.estado AS Estado FROM usuarios u\n"
                    + "INNER JOIN roles r ON u.rol=r.id";
            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);
            while (result.next()) {
                RegistroBD[0] = result.getString("id");
                RegistroBD[1] = result.getString("Nick");
                RegistroBD[2] = result.getString("Usuario");
                RegistroBD[3] = result.getString("Contrasenia");
                RegistroBD[4] = result.getString("Rol");
                RegistroBD[5] = result.getString("Estado");
                model.addRow(RegistroBD);
            }
            tbUsuarios.setModel(model);
            tbUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbUsuarios.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbUsuarios.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbUsuarios.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbUsuarios.getColumnModel().getColumn(2).setPreferredWidth(100);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void CargarDatosTablaUsuariosTrabajadores(JTable table) {
        try {
            String[] titulosTabla = {"Trabajador", "Rol"}; // Titulos de la Tabla
            String[] RegistroBD = new String[2]; // Registros de la Base de Datos
            DefaultTableModel model = new DefaultTableModel(null, titulosTabla); // Le pasamos los titulos a la tabla
            String ConsultaSQL = "SELECT w.nombre AS NombreTrabajador, r.nombre AS Rol "
                    + "FROM usuario_trabajador ut "
                    + "INNER JOIN worker w ON ut.id_trabajador = w.idWorker "
                    + "INNER JOIN usuarios u ON ut.id_usuario = u.idUsuarios "
                    + "INNER JOIN roles r ON u.rol = r.id";
            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);
            while (result.next()) {
                RegistroBD[0] = result.getString("NombreTrabajador");
                RegistroBD[1] = result.getString("Rol");
                model.addRow(RegistroBD);
            }
            tbUsuTrab.setModel(model);
            tbUsuTrab.getColumnModel().getColumn(0).setPreferredWidth(150);
            tbUsuTrab.getColumnModel().getColumn(1).setPreferredWidth(200);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void Guardar() {
        // Variables
        String nombre = txtNombre.getText();
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();
        int idRol = Integer.parseInt(txtIdTPU.getText());

        // Consulta para evitar duplicados
        String consultaDuplicado = "SELECT * FROM usuarios WHERE nombre = ?";

        // Consulta SQL para insertar los datos (nombres como en la base de datos)
        String sqlInsertUsuario = "INSERT INTO usuarios (nombre, usuario, password, rol) VALUES (?, ?, ?, ?)";

        try {
            // Verificar duplicados
            PreparedStatement pstDuplicado = connect.prepareStatement(consultaDuplicado);
            pstDuplicado.setString(1, nombre);
            ResultSet rsDuplicado = pstDuplicado.executeQuery();
            if (rsDuplicado.next()) {
                JOptionPane.showMessageDialog(null, "El usuario ya existe.");
                return;
            }

            // Insertar usuario
            PreparedStatement pstInsertUsuario = connect.prepareStatement(sqlInsertUsuario, Statement.RETURN_GENERATED_KEYS);
            pstInsertUsuario.setString(1, nombre);
            pstInsertUsuario.setString(2, usuario);
            pstInsertUsuario.setString(3, password);
            pstInsertUsuario.setInt(4, idRol);
            int n = pstInsertUsuario.executeUpdate();

            // Obtener el ID del usuario recién insertado
            ResultSet generatedKeys = pstInsertUsuario.getGeneratedKeys();
            int idUsuario = 0;
            if (generatedKeys.next()) {
                idUsuario = generatedKeys.getInt(1);
            }

            // Si se selecciona el radio botón `rdRelacionar`, insertar la relación con el trabajador
            if (rdRelacionar.isSelected()) {
                int idTrabajador = Integer.parseInt(txtIdTrabajador.getText());
                insertarUsuarioTrabajador(idUsuario, idTrabajador);
            }

            // Si el registro se guardó con éxito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardó exitosamente.");
                // Luego bloquear campos
                BloquearCampos();
                CargarDatosTable("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Usuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Eliminar(JTextField codigo) {

        setIdUsuarios(Integer.parseInt(codigo.getText()));

        String consulta = "DELETE from usuarios where idUsuarios=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);
            cs.setInt(1, getIdUsuarios());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());

        }

    }

    public void SeleccionarUsuario(JTable TablaUsuario, JTextField codigo, JTextField nombre, JTextField usuario, JTextField password) {
        try {
            int fila = TablaUsuario.getSelectedRow();
            if (fila >= 0) {
                codigo.setText(TablaUsuario.getValueAt(fila, 0).toString());
                nombre.setText(TablaUsuario.getValueAt(fila, 1).toString());
                usuario.setText(TablaUsuario.getValueAt(fila, 2).toString());
                password.setText(TablaUsuario.getValueAt(fila, 3).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Seleccion, Error: ");
        }
    }

    public void ModificarUsuario(JTextField codigo, JTextField nombre, JTextField usuario, JTextField password) {
        setIdUsuarios(Integer.parseInt(codigo.getText()));
        setNombre(nombre.getText());
        setUsuario(usuario.getText());
        setPassword(password.getText());

        String consulta = "UPDATE usuarios SET nombre=?, usuario=?, password=?, rol=? WHERE idUsuarios=?";
        try {
            PreparedStatement ps = con.getConexion().prepareStatement(consulta);
            ps.setString(1, getNombre());
            ps.setString(2, getUsuario());
            ps.setString(3, getPassword());
            ps.setInt(4, Integer.parseInt(txtIdTPU.getText()));
            ps.setInt(5, getIdUsuarios());
            ps.executeUpdate();

            if (rdRelacionar.isSelected()) {
                int idTrabajador = Integer.parseInt(txtIdTrabajador.getText());
                insertarOActualizarUsuarioTrabajador(getIdUsuarios(), idTrabajador);
            }

            JOptionPane.showMessageDialog(null, "Modificación Exitosa");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No se Modificó, error: " + e.toString());
        }
    }

    public void insertarOActualizarUsuarioTrabajador(int idUsuario, int idTrabajador) {
        // Comprobar si ya existe una relación
        String consultaSelect = "SELECT COUNT(*) FROM usuario_trabajador WHERE id_usuario = ?";
        try (Connection connect = con.getConexion(); 
             PreparedStatement psSelect = connect.prepareStatement(consultaSelect)) {
            psSelect.setInt(1, idUsuario);
            ResultSet rs = psSelect.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Si existe, actualizar
                String consultaUpdate = "UPDATE usuario_trabajador SET id_trabajador = ? WHERE id_usuario = ?";
                try (PreparedStatement psUpdate = connect.prepareStatement(consultaUpdate)) {
                    psUpdate.setInt(1, idTrabajador);
                    psUpdate.setInt(2, idUsuario);
                    psUpdate.executeUpdate();
                }
            } else {
                // Si no existe, insertar
                String consultaInsert = "INSERT INTO usuario_trabajador (id_usuario, id_trabajador) VALUES (?, ?)";
                try (PreparedStatement psInsert = connect.prepareStatement(consultaInsert)) {
                    psInsert.setInt(1, idUsuario);
                    psInsert.setInt(2, idTrabajador);
                    psInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existeRelacionUsuarioTrabajador(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM usuario_trabajador WHERE id_usuario=?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void actualizarUsuarioTrabajador(int idUsuario, int idTrabajador) {
        String consulta = "UPDATE usuario_trabajador SET id_trabajador=? WHERE id_usuario=?";
        try (PreparedStatement ps = connect.prepareStatement(consulta)) {
            ps.setInt(1, idTrabajador);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertarUsuarioTrabajador(int idUsuario, int idTrabajador) {
        String sql = "INSERT INTO usuario_trabajador (id_usuario, id_trabajador) VALUES (?, ?)";
        try (Connection connect = con.getConexion();
             PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idTrabajador);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar en usuario_trabajador: " + e.getMessage());
        }
    }

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtIdUsuario = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtUsuario = new javax.swing.JTextField();
        cbxTipoDeUsuario = new javax.swing.JComboBox<>();
        btnSalir = new javax.swing.JButton();
        txtIdTPU = new javax.swing.JTextField();
        cbxTrabajador = new javax.swing.JComboBox<>();
        txtIdTrabajador = new javax.swing.JTextField();
        rdRelacionar = new javax.swing.JRadioButton();
        txtPassword = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnGrabar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUsuarios = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbUsuTrab = new javax.swing.JTable();
        btnNuevo = new javax.swing.JButton();

        setTitle("Usuarios");

        jPanel1.setBackground(new java.awt.Color(0, 0, 204));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nuevo Usuario");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));

        jLabel2.setText("id");

        jLabel3.setText("Nombre");

        jLabel4.setText("Tipo de Usuario");

        jLabel5.setText("Usuario");

        jLabel6.setText("Contraseña");

        cbxTipoDeUsuario.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoDeUsuarioItemStateChanged(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });

        rdRelacionar.setText("Trabajador");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4))
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cbxTipoDeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(txtIdTPU, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdUsuario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdRelacionar)
                                .addGap(92, 92, 92)
                                .addComponent(btnSalir))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(116, 116, 116)
                                .addComponent(cbxTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIdTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 45, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnSalir)
                    .addComponent(rdRelacionar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cbxTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxTipoDeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdTPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(153, 255, 153));

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGrabar.setText("Grabar");
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(btnGrabar)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar)
                .addGap(29, 29, 29)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                .addComponent(btnModificar)
                .addGap(15, 15, 15))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGrabar)
                    .addComponent(btnCancelar)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel7.setText("Busqueda de Usuario");

        jLabel8.setText("Nombres o Apellidos");

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(jLabel7))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38)
                .addComponent(btnBuscar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnBuscar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        tbUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Tipo de Usuario"
            }
        ));
        tbUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbUsuarios);
        if (tbUsuarios.getColumnModel().getColumnCount() > 0) {
            tbUsuarios.getColumnModel().getColumn(0).setHeaderValue("Código");
            tbUsuarios.getColumnModel().getColumn(1).setHeaderValue("Nombre");
            tbUsuarios.getColumnModel().getColumn(2).setHeaderValue("Tipo de Usuario");
        }

        tbUsuTrab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbUsuTrab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbUsuTrabMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbUsuTrab);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(229, 229, 229))
        );

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/new product.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNuevo))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 233, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        Guardar();
        CargarDatosTable("");

    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        BloquearCampos();
        txtIdUsuario.setText("");
        txtNombre.setText("");
        cbxTipoDeUsuario.setSelectedIndex(0);
        txtUsuario.setText("");
        txtPassword.setText("");
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        SeleccionarUsuario(tbUsuarios, txtNombre, txtNombre, txtUsuario, txtPassword);
        Eliminar(txtNombre);
        CargarDatosTable("");
        txtIdUsuario.setText("");
        txtNombre.setText("");
        cbxTipoDeUsuario.setSelectedIndex(0);
        txtUsuario.setText("");
        txtPassword.setText("");
        BloquearCampos();

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        DesbloquearCampos();
        LimpiarCajasTexto();
        txtNombre.requestFocus();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        ModificarUsuario(txtIdUsuario, txtNombre, txtUsuario, txtPassword);
        CargarDatosTable("");
        txtIdUsuario.setText("");
        txtNombre.setText("");
        cbxTipoDeUsuario.setSelectedIndex(0);
        txtUsuario.setText("");
        txtPassword.setText("");

    }//GEN-LAST:event_btnModificarActionPerformed

    private void tbUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUsuariosMouseClicked
        SeleccionarUsuario(tbUsuarios, txtIdUsuario, txtNombre, txtUsuario, txtPassword);

    }//GEN-LAST:event_tbUsuariosMouseClicked

    private void cbxTipoDeUsuarioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoDeUsuarioItemStateChanged
        MostrarCodigoTipoDeUsuario(cbxTipoDeUsuario, txtIdTPU);
    }//GEN-LAST:event_cbxTipoDeUsuarioItemStateChanged

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged

    private void tbUsuTrabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUsuTrabMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbUsuTrabMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<Object> cbxTipoDeUsuario;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rdRelacionar;
    private javax.swing.JTable tbUsuTrab;
    private javax.swing.JTable tbUsuarios;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtIdTPU;
    private javax.swing.JTextField txtIdTrabajador;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

    public void MostrarCTipoDeUsuario(JComboBox cbxTipoDeUsuario) {
        String sql = "";
        sql = "SELECT * FROM roles;";
        Statement st;
        try {
            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxTipoDeUsuario.removeAllItems();
            while (rs.next()) {
                cbxTipoDeUsuario.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Combo " + e.toString());
        }
    }

    public void MostrarCodigoTipoDeUsuario(JComboBox cbxTipoDeUsuario, JTextField idTPU) {
        String consuta = "select id from roles where nombre=?";
        try {
            CallableStatement cs = con.getConexion().prepareCall(consuta);
            cs.setString(1, cbxTipoDeUsuario.getSelectedItem().toString());
            cs.execute();
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                idTPU.setText(rs.getString("id"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    public void MostrarTrabajador(JComboBox comboTrabajador) {
        String sql = "select * from worker";
        try (Statement st = connect.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            comboTrabajador.removeAllItems();
            while (rs.next()) {
                comboTrabajador.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Trabajadores: " + e.toString());
        }
    }

    public void MostrarCodigoTrabajador(JComboBox trabajador, JTextField IdTrabajador) {
        if (trabajador.getSelectedItem() != null) {
            String consulta = "SELECT idWorker FROM worker WHERE nombre=?";
            try {
                CallableStatement cs = connect.prepareCall(consulta);
                cs.setString(1, trabajador.getSelectedItem().toString());
                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    IdTrabajador.setText(rs.getString("idWorker"));
                } else {
                    IdTrabajador.setText("");
                }
                rs.close(); // Asegurarse de cerrar el ResultSet

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
            }
        } else {
            IdTrabajador.setText("");
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un trabajador válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

}
