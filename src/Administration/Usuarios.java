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
        txtIdTrabajador.setVisible(false);

        // Inicializar y configurar rdRelacionar
        rdRelacionar = new javax.swing.JRadioButton();
        rdRelacionar.setText("Trabajador");
        rdRelacionar.setSelected(true);  // Aseguramos que esté seleccionado inicialmente

        // Añadir el JRadioButton al panel jPanel2
        jPanel2.add(rdRelacionar);

        CargarDatosTable("");
        CargarDatosTablaUsuariosTrabajadores(tbUsuTrab);

        // Listener para detectar cambios en la selección
        rdRelacionar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (rdRelacionar.isSelected()) {
                    System.out.println("El radio botón rdRelacionar está seleccionado.");
                } else {
                    System.out.println("El radio botón rdRelacionar NO está seleccionado.");
                }
            }
        });

    }

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
        Connection connection = null; // Inicializamos la conexión en null
        try {
            // Obtener una conexión del pool antes de ejecutar la consulta
            connection = Conectar.getInstancia().obtenerConexion();

            String[] titulosTabla = {"id", "Nick", "Usuario", "Password", "Rol", "Estado"}; // Titulos de la Tabla
            String[] RegistroBD = new String[6]; // Registros de la Base de Datos
            model = new DefaultTableModel(null, titulosTabla); // Le pasamos los titulos a la tabla
            String ConsultaSQL = "SELECT u.idUsuarios AS id, u.nombre AS Nick, u.usuario AS Usuario, "
                    + "u.password AS Contrasenia, r.nombre AS Rol, u.estado AS Estado FROM usuarios u\n"
                    + "INNER JOIN roles r ON u.rol=r.id_rol";
            Statement st = connection.createStatement();
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
        } finally {
            // Devolver la conexión al pool después de completar la operación
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void CargarDatosTablaUsuariosTrabajadores(JTable table) {
        Connection connection = null;
        try {
// Obtener una conexión del pool
            connection = Conectar.getInstancia().obtenerConexion();
            String[] titulosTabla = {"Trabajador", "Rol"}; // Titulos de la Tabla
            String[] RegistroBD = new String[2]; // Registros de la Base de Datos
            DefaultTableModel model = new DefaultTableModel(null, titulosTabla); // Le pasamos los titulos a la tabla
            String ConsultaSQL = "SELECT w.nombre AS NombreTrabajador, r.nombre AS Rol "
                    + "FROM usuario_trabajador ut "
                    + "INNER JOIN worker w ON ut.id_trabajador = w.idWorker "
                    + "INNER JOIN usuarios u ON ut.id_usuario = u.idUsuarios "
                    + "INNER JOIN roles r ON u.rol = r.id_rol";
            Statement st = connection.createStatement();
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
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void Guardar() {
        Connection connection = null;
        try {
            // Obtener una conexión del pool
            connection = Conectar.getInstancia().obtenerConexion();

            String nombre = txtNombre.getText();
            String usuario = txtUsuario.getText();
            String password = txtPassword.getText();
            int idRol = Integer.parseInt(txtIdTPU.getText());

            // Consulta para evitar duplicados
            String consultaDuplicado = "SELECT * FROM usuarios WHERE nombre = ?";

            // Consulta SQL para insertar los datos
            String sqlInsertUsuario = "INSERT INTO usuarios (nombre, usuario, password, rol) VALUES (?, ?, ?, ?)";

            // Verificar duplicados
            PreparedStatement pstDuplicado = connection.prepareStatement(consultaDuplicado);
            pstDuplicado.setString(1, nombre);
            ResultSet rsDuplicado = pstDuplicado.executeQuery();

            if (rsDuplicado.next()) {
                JOptionPane.showMessageDialog(null, "El usuario ya existe.");
                return;
            }

            // Insertar usuario
            PreparedStatement pstInsertUsuario = connection.prepareStatement(sqlInsertUsuario, Statement.RETURN_GENERATED_KEYS);
            pstInsertUsuario.setString(1, nombre);
            pstInsertUsuario.setString(2, usuario);
            pstInsertUsuario.setString(3, password);
            pstInsertUsuario.setInt(4, idRol);
            int n = pstInsertUsuario.executeUpdate();

            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardó exitosamente.");
                BloquearCampos();
                CargarDatosTable("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Usuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void Eliminar(JTextField codigo) {
        Connection connection = null;
        try {
            // Obtener una conexión del pool
            connection = Conectar.getInstancia().obtenerConexion();

            setIdUsuarios(Integer.parseInt(codigo.getText()));
            String consulta = "DELETE from usuarios where idUsuarios=?";
            PreparedStatement ps = connection.prepareCall(consulta);
            ps.setInt(1, getIdUsuarios());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Se eliminó correctamente");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar, error: " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void SeleccionarUsuario(JTable TablaUsuario, JTextField codigo, JTextField nombre, JTextField usuario, JTextField password) {
        try {
            int fila = TablaUsuario.getSelectedRow(); // Obtén la fila seleccionada
            if (fila >= 0) {
                // Asegúrate de obtener el valor correcto del modelo de la tabla
                codigo.setText(TablaUsuario.getValueAt(fila, 0).toString()); // ID del rol
                nombre.setText(TablaUsuario.getValueAt(fila, 1).toString());
                usuario.setText(TablaUsuario.getValueAt(fila, 2).toString());
                password.setText(TablaUsuario.getValueAt(fila, 3).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Selección, Error: " + e.toString());
        }
    }

    public void ModificarUsuario(JTextField codigo, JTextField nombre, JTextField usuario, JTextField password) {
        Connection connection = null;
        try {
            // Obtener una conexión del pool
            connection = Conectar.getInstancia().obtenerConexion();

            setIdUsuarios(Integer.parseInt(codigo.getText()));
            int idRol = Integer.parseInt(txtIdTPU.getText());

            setNombre(nombre.getText());
            setUsuario(usuario.getText());
            setPassword(password.getText());

            String consulta = "UPDATE usuarios SET nombre=?, usuario=?, password=?, rol=? WHERE idUsuarios=?";
            PreparedStatement ps = connection.prepareStatement(consulta);
            ps.setString(1, getNombre());
            ps.setString(2, getUsuario());
            ps.setString(3, getPassword());
            ps.setInt(4, idRol);
            ps.setInt(5, getIdUsuarios());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Modificación Exitosa");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se Modificó, error: " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void BuscarUsuario(java.awt.event.KeyEvent evt) {
        Connection connection = null;
        try {
            // Obtener una conexión del pool 
            connection = Conectar.getInstancia().obtenerConexion();

            String[] titulosTabla = {"id", "Nick", "Usuario", "Password", "Rol", "Estado"};
            String[] RegistroBD = new String[6];
            model = new DefaultTableModel(null, titulosTabla);

            String ConsultaSQL = "SELECT u.idUsuarios AS id, u.nombre AS Nick, u.usuario AS Usuario, "
                    + "u.password AS Contrasenia, r.nombre AS Rol, u.estado AS Estado FROM usuarios u "
                    + "INNER JOIN roles r ON u.rol = r.id_rol WHERE u.nombre LIKE ? OR u.usuario LIKE ?";

            PreparedStatement ps = connection.prepareStatement(ConsultaSQL);
            String searchParam = "%" + txtBuscar.getText() + "%";
            ps.setString(1, searchParam);
            ps.setString(2, searchParam);

            ResultSet result = ps.executeQuery();
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
            tbUsuarios.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbUsuarios.getColumnModel().getColumn(4).setPreferredWidth(100);
            tbUsuarios.getColumnModel().getColumn(5).setPreferredWidth(100);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Devolver la conexión al pool 
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void insertarOActualizarUsuarioTrabajador(int idUsuario, int idTrabajador) {
        Connection connection = null; // Inicializamos la conexión en null
        try {
            // Obtener una conexión del pool
            connection = Conectar.getInstancia().obtenerConexion();

            // Comprobar si ya existe una relación
            String consultaSelect = "SELECT COUNT(*) FROM usuario_trabajador WHERE id_usuario = ?";
            try (PreparedStatement psSelect = connection.prepareStatement(consultaSelect)) {
                psSelect.setInt(1, idUsuario);
                ResultSet rs = psSelect.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    // Si existe, actualizar
                    String consultaUpdate = "UPDATE usuario_trabajador SET id_trabajador = ? WHERE id_usuario = ?";
                    try (PreparedStatement psUpdate = connection.prepareStatement(consultaUpdate)) {
                        psUpdate.setInt(1, idTrabajador);
                        psUpdate.setInt(2, idUsuario);
                        psUpdate.executeUpdate();
                    }
                } else {
                    // Si no existe, insertar
                    String consultaInsert = "INSERT INTO usuario_trabajador (id_usuario, id_trabajador) VALUES (?, ?)";
                    try (PreparedStatement psInsert = connection.prepareStatement(consultaInsert)) {
                        psInsert.setInt(1, idUsuario);
                        psInsert.setInt(2, idTrabajador);
                        psInsert.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Devolver la conexión al pool después de completar la operación
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public boolean existeRelacionUsuarioTrabajador(int idUsuario) {
        Connection connection = null;
        String sql = "SELECT COUNT(*) FROM usuario_trabajador WHERE id_usuario=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            connection = Conectar.getInstancia().obtenerConexion();
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
        }
        return false;
    }

    public void actualizarUsuarioTrabajador(int idUsuario, int idTrabajador) {
        Connection connection = null;
        String consulta = "UPDATE usuario_trabajador SET id_trabajador=? WHERE id_usuario=?";
        try {
            connection = Conectar.getInstancia().obtenerConexion();
            try (PreparedStatement ps = connection.prepareStatement(consulta)) {
                ps.setInt(1, idTrabajador);
                ps.setInt(2, idUsuario);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void insertarUsuarioTrabajador(int idUsuario, int idTrabajador) {
        Connection connection = null;
        String sql = "INSERT INTO usuario_trabajador (id_usuario, id_trabajador) VALUES (?, ?)";
        try {
            connection = Conectar.getInstancia().obtenerConexion();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, idTrabajador);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar en usuario_trabajador: " + e.getMessage());
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public boolean accion(String estado, int idUsuario) {
        Connection connection = null;
        String sql = "UPDATE usuarios SET estado = ? WHERE idUsuarios = ?";
        try {
            connection = Conectar.getInstancia().obtenerConexion();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, estado);
                ps.setInt(2, idUsuario);
                ps.execute();
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
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
        btnActivar = new javax.swing.JButton();
        btnInactivar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnGrabar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnMostrarDatos = new javax.swing.JButton();
        btnGuia = new javax.swing.JButton();
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

        rdRelacionar.setSelected(true);
        rdRelacionar.setText("Trabajador");

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
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtIdUsuario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cbxTipoDeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(txtIdTPU, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
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
                                .addGap(0, 19, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(btnActivar)
                        .addGap(36, 36, 36)
                        .addComponent(btnInactivar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnActivar)
                            .addComponent(btnInactivar))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
        });

        btnMostrarDatos.setText("Mostrar Datos");
        btnMostrarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarDatosActionPerformed(evt);
            }
        });

        btnGuia.setText("Guia");
        btnGuia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiaActionPerformed(evt);
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
                .addComponent(btnMostrarDatos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuia)
                .addGap(28, 28, 28))
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnMostrarDatos)
                        .addComponent(btnGuia)))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        Eliminar(txtIdUsuario);
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
        CargarDatosTablaUsuariosTrabajadores(tbUsuTrab);
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

    private void btnMostrarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarDatosActionPerformed
        txtBuscar.setText("");
        CargarDatosTable("");
    }//GEN-LAST:event_btnMostrarDatosActionPerformed

    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged

    private void tbUsuTrabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUsuTrabMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbUsuTrabMouseClicked

    private void txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyPressed
        BuscarUsuario(evt);
    }//GEN-LAST:event_txtBuscarKeyPressed

    private void btnGuiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiaActionPerformed
        JOptionPane.showMessageDialog(null, "FORMA DE TRABAJO, LLENADO DE NUEVOS USUARIOS\n"
                + "USUARIO SOLO VER: Podra solo ver los usuarios y demas datos de las Tablas\n"
                + "Botón GRABAR: debe ingresar los datos del nuevo usuarios seleccionar el tipo de usuario, \n"
                + "buscar al Trabajador y presionar botón Grabar\n"
                + "Botón CANCELAR solo borra los datos ingresados en las casillas ,Nombre, Usuario y Contraseña\n"
                + "Botón ELIMINAR para eliminar un usuario seleccionar en la tabla el usuario a eliminar \n"
                + "apareceran sus datos en los recuadros presionar eliminar y se eliminara el usuario designado\n"
                + "Botón MODIFICAR seleccionar el usuario de la Tabla hacer las modificaciones que desee y presionar Modificar\n"
                + "Botón NUEVO para ingresar nuevos datos se limpiaran las casillas si estuvieran ocupadas y el puntero se \n"
                + "ubicara en la casilla Nombre\n"
                + "Botón BUSCAR en el recuadro de busqueda vaya ingresando los datos requeridos segun Nick o usuario y \n"
                + "seleccionara lo requerido\n"
                + "Habran casos en que son usuarios y no Trabajadores tendrá que deseleccionar el botón que esta encima de trabajador\n"
                + "Quienes son Usuarios no trabajadores; Los Clientes y los que ustedes consideren quien no recibe un salario\n"
                + "Botón ACTIVAR se activaran los Usuarios Inactivados\n"
                + "Botón INACTIVAR se desactivaran los usuarios que no deben ingresar al sistema\n"
                + "Esta Plataforma es para controlar quien puede INGRESAR A TU SISTEMA");
    }//GEN-LAST:event_btnGuiaActionPerformed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        int fila = tbUsuarios.getSelectedRow();
        int id = Integer.parseInt(txtIdUsuario.getText());
        if (accion("Activo", id)) {
            JOptionPane.showMessageDialog(null, "Activado");
            CargarDatosTable("");
        } else {
            JOptionPane.showMessageDialog(null, "Error al Activar");
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void btnInactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivarActionPerformed
        int fila = tbUsuarios.getSelectedRow();
        int id = Integer.parseInt(txtIdUsuario.getText());
        if (accion("Inactivo", id)) {
            JOptionPane.showMessageDialog(null, "Inactivado");
            CargarDatosTable("");
        } else {
            JOptionPane.showMessageDialog(null, "Error al Inactivar");
        }
    }//GEN-LAST:event_btnInactivarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnActivar;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnGuia;
    public javax.swing.JButton btnInactivar;
    public javax.swing.JButton btnModificar;
    public javax.swing.JButton btnMostrarDatos;
    public javax.swing.JButton btnNuevo;
    public javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<Object> cbxTipoDeUsuario;
    public javax.swing.JComboBox<String> cbxTrabajador;
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
    public javax.swing.JRadioButton rdRelacionar;
    public javax.swing.JTable tbUsuTrab;
    public javax.swing.JTable tbUsuarios;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtIdTPU;
    private javax.swing.JTextField txtIdTrabajador;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

    public void MostrarCTipoDeUsuario(JComboBox cbxTipoDeUsuario) {
        Connection connection = null;
        String sql = "";
        sql = "SELECT * FROM roles;";
        Statement st;
        try {
            // Obtener una conexión del pool 
            connection = Conectar.getInstancia().obtenerConexion();

            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxTipoDeUsuario.removeAllItems();
            while (rs.next()) {
                cbxTipoDeUsuario.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Combo " + e.toString());
        } finally {
// Devolver la conexión al pool 
            Conectar.getInstancia().devolverConexion(connection);
        }

    }

    public void MostrarCodigoTipoDeUsuario(JComboBox cbxTipoDeUsuario, JTextField idTPU) {
        Connection connection = null;
        String consuta = "select id_rol from roles where nombre=?";
        try {
            // Obtener una conexión del pool 
            connection = Conectar.getInstancia().obtenerConexion();

            CallableStatement cs = connection.prepareCall(consuta);
            cs.setString(1, cbxTipoDeUsuario.getSelectedItem().toString());
            cs.execute();
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                idTPU.setText(rs.getString("id_rol"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        } finally {
// Devolver la conexión al pool 
            Conectar.getInstancia().devolverConexion(connection);
        }

    }

    public void MostrarTrabajador(JComboBox comboTrabajador) {
        Connection connection = null;
        String sql = "select * from worker";

        try {
            // Obtener la conexión
            connection = Conectar.getInstancia().obtenerConexion();

            // Ejecutar la consulta
            try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                comboTrabajador.removeAllItems();
                while (rs.next()) {
                    comboTrabajador.addItem(rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Trabajadores: " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoTrabajador(JComboBox trabajador, JTextField IdTrabajador) {
        Connection connection = null;
        if (trabajador.getSelectedItem() != null) {
            String consulta = "SELECT idWorker FROM worker WHERE nombre=?";
            try {
                // Obtener una conexión del pool 
                connection = Conectar.getInstancia().obtenerConexion();

                try (CallableStatement cs = connection.prepareCall(consulta)) {
                    cs.setString(1, trabajador.getSelectedItem().toString());
                    ResultSet rs = cs.executeQuery();

                    if (rs.next()) {
                        IdTrabajador.setText(rs.getString("idWorker"));
                    } else {
                        IdTrabajador.setText("");
                    }
                    rs.close(); // Asegurarse de cerrar el ResultSet
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
            } finally {
                // Devolver la conexión al pool 
                Conectar.getInstancia().devolverConexion(connection);
            }

        } else {
            IdTrabajador.setText("");
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un trabajador válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

}
