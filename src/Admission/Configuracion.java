package Admission;

import conectar.Conectar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Configuracion extends javax.swing.JInternalFrame {

    Conectar con = new Conectar();
    Connection connect = con.getConexion();
    PreparedStatement ps;
    ResultSet rs;

    private int id;
    private String nombre, direccion, ciudad, estado, telefono, email, webpage, mensaje, emailPassword;
    private int zipcode;
    private byte[] logo;
    private Date fechaInicioActividades;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public Date getFechaInicioActividades() {
        return fechaInicioActividades;
    }

    public void setFechaInicioActividades(Date fechaInicioActividades) {
        this.fechaInicioActividades = fechaInicioActividades;
    }

    public Configuracion BuscarDatos() {
        String sql = "SELECT * FROM configuracion";
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                setId(rs.getInt("id"));
                setNombre(rs.getString("nombre"));
                setDireccion(rs.getString("direccion"));
                setCiudad(rs.getString("ciudad"));
                setZipcode(rs.getInt("zipcode"));
                setEstado(rs.getString("estado"));
                setTelefono(rs.getString("telefono"));
                setEmail(rs.getString("email"));
                setWebpage(rs.getString("webpage"));
                setMensaje(rs.getString("mensaje"));
                setLogo(rs.getBytes("logo")); // Obtener el logo como byte[]
                setFechaInicioActividades(rs.getDate("fecha_inicio_actividades")); // Recuperar la fecha de inicio de actividades
                //                System.out.println("Logo recuperado de la base de datos, tamaño: " + (getLogo() != null ? getLogo().length : 0) + " bytes");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return this;
    }

    public boolean GuardarConfig() {
        String sql = "INSERT INTO configuracion(nombre, direccion, ciudad, zipcode, estado, telefono, email, webpage, mensaje, logo, email_password, fecha_inicio_actividades) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, getNombre());
            ps.setString(2, getDireccion());
            ps.setString(3, getCiudad());
            ps.setInt(4, getZipcode());
            ps.setString(5, getEstado());
            ps.setString(6, getTelefono());
            ps.setString(7, getEmail());
            ps.setString(8, getWebpage());
            ps.setString(9, getMensaje());
            ps.setBytes(10, getLogo());
            ps.setString(11, getEmailPassword());//Guardar la contraseña del email
            ps.setDate(12, new java.sql.Date(getFechaInicioActividades().getTime())); // Guardar la fecha de inicio de actividades

            System.out.println("Guardando datos con logo de tamaño: " + (getLogo() != null ? getLogo().length : 0) + " bytes");

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showConfirmDialog(null, "Datos guardados correctamente.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo guardar los datos.");
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                // No cierres la conexión aquí para mantenerla abierta
            } catch (SQLException e) {
            }
        }
    }

    public boolean ModificarDatos() {
        String sql = "UPDATE configuracion SET nombre=?, direccion=?, ciudad=?, zipcode=?, estado=?, telefono=?, email=?, webpage=?, mensaje=?, logo=?, email_password=?, fecha_inicio_actividades=? WHERE id=?";
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, getNombre());
            ps.setString(2, getDireccion());
            ps.setString(3, getCiudad());
            ps.setInt(4, getZipcode());
            ps.setString(5, getEstado());
            ps.setString(6, getTelefono());
            ps.setString(7, getEmail());
            ps.setString(8, getWebpage());
            ps.setString(9, getMensaje());
            ps.setBytes(10, getLogo());
            ps.setString(11, getEmailPassword());
            // Verificar si fechaInicioActividades es null antes de usarlo
            if (getFechaInicioActividades() != null) {
                ps.setDate(12, new java.sql.Date(getFechaInicioActividades().getTime())); // Guardar la fecha de inicio de actividades
            } else {
                ps.setNull(12, java.sql.Types.DATE); // Manejar el caso donde la fecha es null
            }
            
            ps.setInt(13, getId());

            System.out.println("Actualizando datos con logo de tamaño: " + (getLogo() != null ? getLogo().length : 0) + " bytes");

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Datos actualizados correctamente.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar los datos.");
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar los datos: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connect != null) {
                    connect.close(); // Cierra la conexión aquí al finalizar la operación
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para cargar una imagen desde el sistema de archivos
    public byte[] cargarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] imageBytes = new byte[(int) file.length()];
                int bytesRead = fis.read(imageBytes);
//                System.out.println("Bytes leídos: " + bytesRead);
                return imageBytes;
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al leer la imagen: " + e.getMessage());
            }
        }
        return null;
    }

// Método para obtener la configuración actual
    public Configuracion getConfiguracionActual() {
        Configuracion config = new Configuracion();
        return config.BuscarDatos();
    }

    public void ListarConfig() {
        BuscarDatos();
        txtId.setText("" + getId());
        txtNombre.setText(getNombre());
        txtDireccion.setText(getDireccion());
        txtCiudad.setText(getCiudad());
        txtZipCode.setText("" + getZipcode());
        txtEstado.setText(getEstado());
        txtTelefono.setText(getTelefono());
        txtEmail.setText(getEmail());
        txtWebPage.setText(getWebpage());
        txtMensaje.setText(getMensaje());

        // Mostrar la imagen en el JLabel LabelLogo
        if (getLogo() != null) {
            ImageIcon imageIcon = new ImageIcon(getLogo());
            LabelLogo.setIcon(imageIcon);
        } else {
            LabelLogo.setIcon(null); // Si no hay imagen, vaciar el JLabel
        }
    }

    public Configuracion() {
        initComponents();
        ListarConfig();
        txtId.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtNombre = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCiudad = new javax.swing.JTextField();
        txtZipCode = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtWebPage = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtMensaje = new javax.swing.JTextField();
        LabelLogo = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        btnCargarLogo = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        btnGuia = new javax.swing.JButton();
        dateFechaInicio = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        txtContrasenia = new javax.swing.JTextField();

        setTitle("Configuración");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 52, 154, -1));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setText("Nombre de la Empresa");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 23, -1, -1));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("Dirección Comercial");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 92, -1, -1));
        jPanel1.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 121, 154, -1));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Ciudad");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 161, -1, -1));
        jPanel1.add(txtCiudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 190, 154, -1));
        jPanel1.add(txtZipCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 267, 154, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Zip Code");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 238, -1, -1));
        jPanel1.add(txtEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 343, 154, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Fecha de Inicio Actividades");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 310, -1, 20));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setText("Teléfono");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 29, -1, -1));
        jPanel1.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, 154, 30));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("Email");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 92, -1, -1));
        jPanel1.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, 154, -1));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setText("Webpage");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, -1, -1));
        jPanel1.add(txtWebPage, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 230, 200, -1));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setText("Contraseña");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 150, -1, -1));
        jPanel1.add(txtMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, 210, -1));

        LabelLogo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel1.add(LabelLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 50, 260, 210));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 290, -1, -1));

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 290, -1, -1));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 340, -1, -1));
        jPanel1.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 20, -1, -1));

        btnCargarLogo.setText("Cargar Imagen");
        btnCargarLogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarLogoActionPerformed(evt);
            }
        });
        jPanel1.add(btnCargarLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 340, -1, -1));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setText("Mensaje");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 260, -1, -1));

        btnGuia.setText("Guia");
        btnGuia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiaActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuia, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, -1, -1));
        jPanel1.add(dateFechaInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, 190, -1));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setText("Estado");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 314, -1, -1));
        jPanel1.add(txtContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, 150, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 730, 386));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // Verificar que los campos obligatorios no estén vacíos
        if (!"".equals(txtNombre.getText()) && !"".equals(txtDireccion.getText())
                && !"".equals(txtCiudad.getText()) && !"".equals(txtZipCode.getText())
                && !"".equals(txtEstado.getText()) && !"".equals(txtTelefono.getText())
                && !"".equals(txtEmail.getText()) && !"".equals(txtWebPage.getText())
                && !"".equals(txtMensaje.getText()) && !"".equals(new String(txtContrasenia.getText()))
                && dateFechaInicio.getDate() != null) { // Asegúrate de que la fecha de inicio no esté vacía

            // Establecer los valores a la instancia actual de Configuracion
            setNombre(txtNombre.getText());
            setDireccion(txtDireccion.getText());
            setCiudad(txtCiudad.getText());
            setZipcode(Integer.parseInt(txtZipCode.getText()));
            setEstado(txtEstado.getText());
            setTelefono(txtTelefono.getText());
            setEmail(txtEmail.getText());
            setWebpage(txtWebPage.getText());
            setMensaje(txtMensaje.getText());
            setEmailPassword(new String(txtContrasenia.getText()));
            setFechaInicioActividades(dateFechaInicio.getDate()); // Establece la fecha de inicio de actividades

            // Guardar la configuración
            if (GuardarConfig()) {
                JOptionPane.showMessageDialog(null, "Datos guardados exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar datos");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        if (!"".equals(txtNombre.getText()) || !"".equals(txtDireccion.getText()) || !"".equals(txtCiudad.getText()) || !"".equals(txtZipCode.getText()) || !"".equals(txtEstado.getText()) || !"".equals(txtTelefono.getText()) || !"".equals(txtEmail.getText()) || !"".equals(txtWebPage.getText()) || !"".equals(txtMensaje.getText())) {
            setNombre(txtNombre.getText());
            setDireccion(txtDireccion.getText());
            setCiudad(txtCiudad.getText());
            setZipcode(Integer.parseInt(txtZipCode.getText()));
            setEstado(txtEstado.getText());
            setTelefono(txtTelefono.getText());
            setEmail(txtEmail.getText());
            setWebpage(txtWebPage.getText());
            setMensaje(txtMensaje.getText());
            setEmailPassword(new String(txtContrasenia.getText()));
            ModificarDatos();
            JOptionPane.showMessageDialog(null, "Datos de la empresa modificado");
            ListarConfig();
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnCargarLogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarLogoActionPerformed
        byte[] logo = cargarImagen();
        if (logo != null) {
            setLogo(logo);
            ImageIcon imageIcon = new ImageIcon(logo);
            LabelLogo.setIcon(imageIcon);
            JOptionPane.showMessageDialog(null, "Logo cargado exitosamente");
        } else {
            JOptionPane.showMessageDialog(null, "Error al cargar el logo.");
        }
    }//GEN-LAST:event_btnCargarLogoActionPerformed

    private void btnGuiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiaActionPerformed
        JOptionPane.showMessageDialog(null, "CONFIGURACIÓN\n"
                + "Llenar las casillas con los datos de tu Empresa\n"
                + "Botón GUARDAR para guardar los datos que se mostraran en las casilla\n"
                + "La Imagen del Logo deberá tener aprox 250 pixeles SI TIENE PROBLEMAS CONTACTARNOS"
                + "Botón ACTUALIZAR  seleccionamos una fila de la Tabla para modificar los datos y presionamos Modificar\n"
                + "Botón CARGAR IMAGEN para cargar tu logo deberas ubicar tu imagen y cumpla con el tamaño\n"
                + "Esta Interfaz es para guardar los Datos de tu empresa lo cual aparecerá en las facturas y cotizaciones");
    }//GEN-LAST:event_btnGuiaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelLogo;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCargarLogo;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuia;
    private javax.swing.JButton btnSalir;
    private com.toedter.calendar.JDateChooser dateFechaInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCiudad;
    private javax.swing.JTextField txtContrasenia;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtMensaje;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtWebPage;
    private javax.swing.JTextField txtZipCode;
    // End of variables declaration//GEN-END:variables
}
