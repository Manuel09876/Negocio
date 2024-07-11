package Admission;

import conectar.Conectar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class Configuracion extends javax.swing.JInternalFrame {

    Conectar con = new Conectar();
    Connection connect = con.getConexion();
    PreparedStatement ps;
    ResultSet rs;

    private int id;
    private String nombre, direccion, ciudad;
    private int zipcode;
    private String estado, telefono, email, webpage, mensaje;

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
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connect != null) connect.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
    return this;
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
}

    public boolean ModificarDatos() {
        String sql = "UPDATE configuracion SET nombre=?, direccion=?, ciudad=?, zipcode=?, estado=?, telefono=?, email=?, webpage=? WHERE id=?";
        try {
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
            ps.setInt(10, getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                connect.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public boolean GuardarConfig() {
    String sql = "INSERT INTO configuracion(nombre, direccion, ciudad, zipcode, estado, telefono, email, webpage, mensaje) VALUES (?,?,?,?,?,?,?,?,?)";
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
        
        System.out.println("Guardando configuración: " + getNombre() + ", " + getDireccion() + ", " + getCiudad() + ", " + getZipcode() + ", " + getEstado() + ", " + getTelefono() + ", " + getEmail() + ", " + getWebpage() + ", " + getMensaje());
        
        ps.execute();
        return true;
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.toString());
        return false;
    } finally {
        try {
            if (ps != null) ps.close();
            if (connect != null) connect.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}



    public Configuracion() {
        initComponents();
        ListarConfig();
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
        jLabel10 = new javax.swing.JLabel();
        LabelLogo = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();

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
        jLabel5.setText("Estado");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 314, -1, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setText("Teléfono");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 29, -1, -1));
        jPanel1.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 52, 154, -1));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("Email");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 92, -1, -1));
        jPanel1.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 121, 154, -1));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setText("Webpage");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 161, -1, -1));
        jPanel1.add(txtWebPage, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 190, 154, -1));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setText("Mensaje");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 238, -1, -1));
        jPanel1.add(txtMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 267, 154, -1));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setText("Logo");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(501, 29, -1, -1));

        LabelLogo.setText("jLabel11");
        jPanel1.add(LabelLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 52, 124, 120));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 319, -1, -1));

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(438, 319, -1, -1));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 250, -1, -1));
        jPanel1.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 170, 80, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 6, -1, 386));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // Verificar que los campos obligatorios no estén vacíos
    if (!"".equals(txtNombre.getText()) && !"".equals(txtDireccion.getText()) && 
        !"".equals(txtCiudad.getText()) && !"".equals(txtZipCode.getText()) && 
        !"".equals(txtEstado.getText()) && !"".equals(txtTelefono.getText()) && 
        !"".equals(txtEmail.getText()) && !"".equals(txtWebPage.getText()) && 
        !"".equals(txtMensaje.getText())) {
        
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
            ModificarDatos();
            JOptionPane.showMessageDialog(null, "Datos de la empresa modificado");
            ListarConfig();
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }
    }//GEN-LAST:event_btnActualizarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelLogo;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
