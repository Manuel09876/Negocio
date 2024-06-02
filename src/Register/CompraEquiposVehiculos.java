
package Register;

import Administration.Productos;
import Administration.TipoDeUsuario;
import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


public class CompraEquiposVehiculos extends javax.swing.JInternalFrame {

    DefaultTableModel model;
    int id_equipo;
    String serie;
    int id_Tipo;
    int id_marca;
    int id_proveedor;
    String condicion;
    Date anio_fabricacion;
    String Recibo;
    double subTotal;
    double taxes;
    double total;
    Date FechaPago;
    double Inicial;
    double diferencia;
    String estado;
    
    int id_pagoCred;
    Date fechaInicio;
    Date FechaTermino;
    int Frecuencia;

    public CompraEquiposVehiculos(int id_equipo, String serie, int id_Tipo, int id_marca, int id_proveedor, String condicion, Date anio_fabricacion, String Recibo, double subTotal, double taxes, double total, Date FechaPago, double Inicial, double diferencia, String estado, int id_pagoCred, Date fechaInicio, Date FechaTermino, int Frecuencia) {
        this.id_equipo = id_equipo;
        this.serie = serie;
        this.id_Tipo = id_Tipo;
        this.id_marca = id_marca;
        this.id_proveedor = id_proveedor;
        this.condicion = condicion;
        this.anio_fabricacion = anio_fabricacion;
        this.Recibo = Recibo;
        this.subTotal = subTotal;
        this.taxes = taxes;
        this.total = total;
        this.FechaPago = FechaPago;
        this.Inicial = Inicial;
        this.diferencia = diferencia;
        this.estado = estado;
        this.id_pagoCred = id_pagoCred;
        this.fechaInicio = fechaInicio;
        this.FechaTermino = FechaTermino;
        this.Frecuencia = Frecuencia;
    }

    public int getId_equipo() {
        return id_equipo;
    }

    public void setId_equipo(int id_equipo) {
        this.id_equipo = id_equipo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getId_Tipo() {
        return id_Tipo;
    }

    public void setId_Tipo(int id_Tipo) {
        this.id_Tipo = id_Tipo;
    }

    public int getId_marca() {
        return id_marca;
    }

    public void setId_marca(int id_marca) {
        this.id_marca = id_marca;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public Date getAnio_fabricacion() {
        return anio_fabricacion;
    }

    public void setAnio_fabricacion(Date anio_fabricacion) {
        this.anio_fabricacion = anio_fabricacion;
    }

    public String getRecibo() {
        return Recibo;
    }

    public void setRecibo(String Recibo) {
        this.Recibo = Recibo;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getFechaPago() {
        return FechaPago;
    }

    public void setFechaPago(Date FechaPago) {
        this.FechaPago = FechaPago;
    }

    public double getInicial() {
        return Inicial;
    }

    public void setInicial(double Inicial) {
        this.Inicial = Inicial;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_pagoCred() {
        return id_pagoCred;
    }

    public void setId_pagoCred(int id_pagoCred) {
        this.id_pagoCred = id_pagoCred;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaTermino() {
        return FechaTermino;
    }

    public void setFechaTermino(Date FechaTermino) {
        this.FechaTermino = FechaTermino;
    }

    public int getFrecuencia() {
        return Frecuencia;
    }

    public void setFrecuencia(int Frecuencia) {
        this.Frecuencia = Frecuencia;
    }

    

    

    
    
    //Conexión
    Conectar con = new Conectar();
    Connection connect = con.getConexion();
    PreparedStatement ps;
    ResultSet rs;
    
    public CompraEquiposVehiculos() {
        initComponents();
        AutoCompleteDecorator.decorate(cbxProveedor);
        AutoCompleteDecorator.decorate(cbxMarca);
        AutoCompleteDecorator.decorate(cbxTipoMaqVe);
        AutoCompleteDecorator.decorate(cbxPagarCon);
        MostrarTipo(cbxTipoMaqVe);
        MostrarMarca(cbxMarca);
        MostrarProveedor(cbxProveedor);
        MostrarFormaDePago(cbxPagarCon);
        MostrarTabla("");
        txtId.setEnabled(false);
        txtEquipo.requestFocus();
    }


    void MostrarTabla(String Valores){

        try {
            String[] titulosTabla = {"Código", "Descripción", "serie", "Tipo", "Marca", "Proveedor", "Condicion", "Recibo" , "Subtotal", "Taxes", "Total", "Fecha", "F.Pago"}; //Titulos de la Tabla
            String[] RegistroBD = new String[13];
            
            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla
            
            String ConsultaSQL = "SELECT * FROM equipos";
            
            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {
                RegistroBD[0] = result.getString("id_equipos");
                RegistroBD[1] = result.getString("Descripcion");
                RegistroBD[2] = result.getString("serie");
                RegistroBD[3] = result.getString("Marca");
                RegistroBD[4] = result.getString("Proveedor");
                RegistroBD[5] = result.getString("Condicion");
                RegistroBD[6] = result.getString("Recibo");
                RegistroBD[7] = result.getString("Subtotal");
                RegistroBD[8] = result.getString("Taxes");
                RegistroBD[9] = result.getString("Total");
                RegistroBD[10] = result.getString("Fecha");
                RegistroBD[11] = result.getString("F.Pago");

                model.addRow(RegistroBD);
            }
            
            tbEquipos.setModel(model);
            tbEquipos.getColumnModel().getColumn(0).setPreferredWidth(100);
            tbEquipos.getColumnModel().getColumn(1).setPreferredWidth(300);
            tbEquipos.getColumnModel().getColumn(2).setPreferredWidth(200);
            tbEquipos.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbEquipos.getColumnModel().getColumn(4).setPreferredWidth(150);
            tbEquipos.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbEquipos.getColumnModel().getColumn(6).setPreferredWidth(150);
            tbEquipos.getColumnModel().getColumn(7).setPreferredWidth(100);
            tbEquipos.getColumnModel().getColumn(8).setPreferredWidth(150);
            tbEquipos.getColumnModel().getColumn(9).setPreferredWidth(150);
            tbEquipos.getColumnModel().getColumn(10).setPreferredWidth(150);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void Guardar() {

        // Variables
        int id_equipos;
        String Recibo;
        String nombre;
        String serie;
        int id_categoria;
        int id_marca;
        int id_proveedor;
        String Condicion = null;
        Date anio_fabricacion;
        double precio;
        String sql = "";

        //Obtenemos la informacion de las cajas de texto
        Recibo = txtRecibo.getText();
        nombre = txtEquipo.getText();
        serie = txtSerie.getText();
        id_categoria = Integer.parseInt(txtIdMatVe.getText());
        id_marca = Integer.parseInt(txtIdMarca.getText());
        id_proveedor = Integer.parseInt(txtIdProveedor.getText());
        condicion = txtCondicion.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        anio_fabricacion = dateAnioFabricacion.getDate();
        
        //Consulta para evitar duplicados
        String consulta = "SELECT * FROM equipos WHERE serie = ?";
        
        //Consulta sql para insertar los datos (nombres como en la base de datos)
        sql = "INSERT INTO vehiculos (Recibo, nombre, serie, id_categoria, id_marca, id_proveedor, anio_fabricacion, precio)VALUES (?,?,?,?,?,?,?,?)";

        //Para almacenar los datos empleo un try cash
        try {
            
            //prepara la coneccion para enviar al sql (Evita ataques al sql)
            PreparedStatement pst = connect.prepareStatement(sql);
   
            pst.setString(1, Recibo);
            pst.setString(2, nombre);
            pst.setString(3, serie);
            pst.setInt(4, id_categoria);
            pst.setInt(5, id_marca);
            pst.setInt(6, id_proveedor);
            pst.setString(7, Condicion);
            pst.setDate(8, (java.sql.Date) anio_fabricacion);
            
            
            
            //Declara otra variable para validar los registros
            int n = pst.executeUpdate();

            //si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardo exitosamente");

                //Luego Bloquera campos
                
            }
            MostrarTabla("");

        } catch (SQLException ex) {
            Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);

        }

    }
    
    public void Eliminar(JTextField id) {

        setId_equipo(Integer.parseInt(id.getText()));

        String consulta = "DELETE from equipos where id_equipos=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);
            cs.setInt(1, getId_equipo());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");
            
            MostrarTabla("");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());

        }

    }

    public void SeleccionarEquipo(JTable Tabla, JTextField id, JTextField Recibo, JTextField equipo, JTextField id_categoria, JTextField id_marca, JTextField id_proveedor, JTextField Condicion) {

        try {

            int fila = tbEquipos.getSelectedRow();

            if (fila >= 0) {

                id.setText(tbEquipos.getValueAt(fila, 0).toString());
                equipo.setText(tbEquipos.getValueAt(fila, 1).toString());
                

            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Seleccion, Error: ");
        }

    }


    public void modificar(JTextField id, JTextField Recibo, JTextField equipo, JTextField id_categoria, JTextField id_marca, JTextField id_proveedor, JTextField Condicion) {
        String sql = "UPDATE equipos SET Recibo=?, nombre = ?, serie = ?, id_categoria =?, id_marca =?, id_proveedor =?, Condicion=?, anio_fabricacion=? WHERE id_equipo = ?";
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, getRecibo());
//            ps.setString(2, getNombre());
//            ps.setString(3, getSerie());
//            ps.setInt(4, getId_categoria());
//            ps.setInt(5, getId_marca());
//            ps.setInt(6, getId_proveedor());
//            ps.setString(7, getCondicion());
//            ps.setDate(8, (java.sql.Date) anio_fabricacion);
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Registro Modificado exitosamente");
            
            MostrarTabla("");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEquipo = new javax.swing.JTextField();
        cbxMarca = new javax.swing.JComboBox<>();
        dateAnioFabricacion = new com.toedter.calendar.JDateChooser();
        cbxProveedor = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        txtIdMarca = new javax.swing.JTextField();
        txtIdProveedor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtSerie = new javax.swing.JTextField();
        txtId = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbxTipoMaqVe = new javax.swing.JComboBox<>();
        txtIdMatVe = new javax.swing.JTextField();
        dateInicio = new com.toedter.calendar.JDateChooser();
        dateTermino = new com.toedter.calendar.JDateChooser();
        txtFrecuencia = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCondicion = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnFechasDePago = new javax.swing.JButton();
        txtCuota = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtInteres = new javax.swing.JTextField();
        txtIdCred = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbEquipos = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtRecibo = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel49 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        JLabelTotalCompra = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cbxPagarCon = new javax.swing.JComboBox<>();
        txtPagarCon = new javax.swing.JTextField();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Equipos o Vehiculos");

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Equipo");

        jLabel2.setText("Marca");

        jLabel3.setText("Año de Fabricacion");

        jLabel4.setText("Proveedor");

        cbxMarca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbxMarca.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxMarcaItemStateChanged(evt);
            }
        });

        dateAnioFabricacion.setDateFormatString("yyyy-MM-dd");

        cbxProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbxProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxProveedorItemStateChanged(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exchange.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnBorrar.setText("Eliminar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        jLabel5.setText("Serie");

        jLabel6.setText("Tipo");

        cbxTipoMaqVe.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoMaqVeItemStateChanged(evt);
            }
        });

        jLabel8.setText("Cuota");

        jLabel9.setText("Condicion");

        jLabel10.setText("Inicio");

        jLabel11.setText("Termino");

        btnFechasDePago.setText("Establecer Fechas de Pago");

        jLabel12.setText("Frecuencia");

        jLabel7.setText("Tasa de Interes");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel9))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dateAnioFabricacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCondicion, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtIdCred, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel1))
                                        .addGap(36, 36, 36)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(txtEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(24, 24, 24)
                                                .addComponent(jButton1))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(244, 244, 244)
                                                .addComponent(txtIdMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel2))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(42, 42, 42)
                                                .addComponent(cbxMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGap(44, 44, 44)
                                                .addComponent(cbxTipoMaqVe, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtIdMatVe, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(42, 42, 42)
                                .addComponent(cbxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(btnGuardar)
                        .addGap(50, 50, 50)
                        .addComponent(btnModificar)
                        .addGap(30, 30, 30)
                        .addComponent(btnBorrar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(dateInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dateTermino, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(76, 76, 76))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(61, 61, 61)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtInteres, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(btnFechasDePago)
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jLabel12)
                                .addGap(83, 83, 83)
                                .addComponent(jLabel8))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(txtFrecuencia, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(txtCuota, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cbxTipoMaqVe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdMatVe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbxMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtIdMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dateAnioFabricacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txtCondicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnGuardar)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnModificar)
                                        .addComponent(btnBorrar)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel12))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtFrecuencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCuota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(btnFechasDePago))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtInteres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(txtIdCred, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addGap(2, 2, 2)
                        .addComponent(dateTermino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)))
                .addGap(60, 60, 60))
        );

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        tbEquipos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbEquipos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbEquiposMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbEquipos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel13.setText("Recibo N°");

        jLabel14.setText("SubTotal");

        jLabel15.setText("Taxes");

        jLabel16.setText("Total");

        jLabel17.setText("Fecha");

        jLabel49.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel49.setText("Pagar con");

        jLabel47.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel47.setText("Total Pagar");

        JLabelTotalCompra.setText("-----------");

        jLabel19.setText("Inicial");

        jLabel20.setText("Diferencia");

        cbxPagarCon.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxPagarConItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel16)
                                .addComponent(jLabel15))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(3, 3, 3))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel19)
                                .addComponent(jLabel14)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel20))
                            .addComponent(txtRecibo)
                            .addComponent(jTextField2)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addComponent(jTextField4))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(jLabel49))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(JLabelTotalCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cbxPagarCon, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(txtPagarCon, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 103, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel47)
                                .addGap(192, 192, 192))))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel49)
                            .addGap(18, 18, 18)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbxPagarCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtPagarCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13)
                                .addComponent(txtRecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(25, 25, 25)
                            .addComponent(jLabel14))))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel16))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel47)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(JLabelTotalCompra)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 543, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbxProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxProveedorItemStateChanged
        MostrarCodigoProveedor(cbxProveedor, txtIdProveedor);
    }//GEN-LAST:event_cbxProveedorItemStateChanged

    private void cbxMarcaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxMarcaItemStateChanged
        MostrarCodigoMarca(cbxMarca, txtIdMarca);
    }//GEN-LAST:event_cbxMarcaItemStateChanged

    private void cbxTipoMaqVeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoMaqVeItemStateChanged
        MostrarCodigoTipo(cbxTipoMaqVe, txtIdMatVe);
    }//GEN-LAST:event_cbxTipoMaqVeItemStateChanged

    private void cbxPagarConItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxPagarConItemStateChanged
        MostrarCodigoFormaDePago(cbxPagarCon, txtPagarCon);
    }//GEN-LAST:event_cbxPagarConItemStateChanged

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tbEquiposMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbEquiposMouseClicked
        SeleccionarEquipo(tbEquipos, txtId, txtRecibo, txtEquipo, txtIdMatVe, txtIdMarca, txtIdProveedor, txtCondicion);
    }//GEN-LAST:event_tbEquiposMouseClicked

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        SeleccionarEquipo(tbEquipos, txtId, txtRecibo, txtEquipo, txtIdMatVe, txtIdMarca, txtIdProveedor, txtCondicion);
        modificar(txtId, txtRecibo, txtEquipo, txtIdMatVe, txtIdMarca, txtIdProveedor, txtCondicion);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        Eliminar(txtId);
    }//GEN-LAST:event_btnBorrarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel JLabelTotalCompra;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnFechasDePago;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxMarca;
    private javax.swing.JComboBox<String> cbxPagarCon;
    private javax.swing.JComboBox<String> cbxProveedor;
    private javax.swing.JComboBox<String> cbxTipoMaqVe;
    private com.toedter.calendar.JDateChooser dateAnioFabricacion;
    private com.toedter.calendar.JDateChooser dateInicio;
    private com.toedter.calendar.JDateChooser dateTermino;
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTable tbEquipos;
    private javax.swing.JTextField txtCondicion;
    private javax.swing.JTextField txtCuota;
    private javax.swing.JTextField txtEquipo;
    private javax.swing.JTextField txtFrecuencia;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdCred;
    private javax.swing.JTextField txtIdMarca;
    private javax.swing.JTextField txtIdMatVe;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtInteres;
    private javax.swing.JTextField txtPagarCon;
    private javax.swing.JTextField txtRecibo;
    private javax.swing.JTextField txtSerie;
    // End of variables declaration//GEN-END:variables

    public void MostrarProveedor(JComboBox cbxProveedor){
        
        String sql="";
        sql="select * from suplier";
        Statement st;
        
        try {
            
            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxProveedor.removeAllItems();
            
            while (rs.next()) {                
                
                cbxProveedor.addItem(rs.getString("nameSuplier"));
            }
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " +e.toString());
        }
    }
    
    public void MostrarCodigoProveedor(JComboBox cbxProveedor, JTextField idProveedor){
        
    String consuta = "select suplier.idSuplier from suplier where suplier.nameSuplier=?";
    
    try {
        // Validar si hay un item seleccionado en el JComboBox
        if (cbxProveedor.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
            return;
        }

        CallableStatement cs = con.getConexion().prepareCall(consuta);

        Object selectedValue = cbxProveedor.getSelectedItem();
        if (selectedValue != null) {
            String valorSeleccionado = selectedValue.toString();
            cs.setString(1, valorSeleccionado);
            
            cs.execute();
            
            ResultSet rs = cs.executeQuery();
            
            if(rs.next()){
                idProveedor.setText(rs.getString("idSuplier"));
            }
        } 
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al mostrar " +e.toString());
    }
}
    
    public void MostrarTipo(JComboBox cbxTipoMaqVe){
        
        String sql="";
        sql="select * from tipomaquinariasvehiculos";
        Statement st;
        
        try {
            
            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxTipoMaqVe.removeAllItems();
            
            while (rs.next()) {                
                
                cbxTipoMaqVe.addItem(rs.getString("nombre"));
            }
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " +e.toString());
        }
    }
    
    public void MostrarCodigoTipo(JComboBox cbxTipoMaqVe, JTextField idTipoMaqVe){
        
    String consuta = "select tipomaquinariasvehiculos.idMaqVe from tipomaquinariasvehiculos where tipomaquinariasvehiculos.nombre=?";
    
    try {
        // Validar si hay un item seleccionado en el JComboBox
        if (cbxTipoMaqVe.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
            return;
        }

        CallableStatement cs = con.getConexion().prepareCall(consuta);

        Object selectedValue = cbxTipoMaqVe.getSelectedItem();
        if (selectedValue != null) {
            String valorSeleccionado = selectedValue.toString();
            cs.setString(1, valorSeleccionado);
            
            cs.execute();
            
            ResultSet rs = cs.executeQuery();
            
            if(rs.next()){
                idTipoMaqVe.setText(rs.getString("idMaqVe"));
            }
        } 
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al mostrar " +e.toString());
    }
}
    
    public void MostrarMarca(JComboBox cbxMarca){
        
        String sql="";
        sql="select * from marca";
        Statement st;
        
        try {
            
            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxMarca.removeAllItems();
            
            while (rs.next()) {                
                
                cbxMarca.addItem(rs.getString("nombre"));
            }
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " +e.toString());
        }
    }
    
    public void MostrarCodigoMarca(JComboBox cbxMarca, JTextField idMarca){
        
    String consuta = "select marca.id_marca from marca where marca.nombre=?";
    
    try {
        // Validar si hay un item seleccionado en el JComboBox
        if (cbxMarca.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
            return;
        }

        CallableStatement cs = con.getConexion().prepareCall(consuta);

        Object selectedValue = cbxMarca.getSelectedItem();
        if (selectedValue != null) {
            String valorSeleccionado = selectedValue.toString();
            cs.setString(1, valorSeleccionado);
            
            cs.execute();
            
            ResultSet rs = cs.executeQuery();
            
            if(rs.next()){
                idMarca.setText(rs.getString("id_marca"));
            }
        } 
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al mostrar " +e.toString());
    }
}
    
    public void MostrarCodigoFormaDePago(JComboBox cbxPagarCon, JTextField idPagarCon) {

        String consuta = "select formadepago.id_formadepago from formadepago where formadepago.nombre=?";

        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxPagarCon.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
                return;
            }

            CallableStatement cs = con.getConexion().prepareCall(consuta);

            Object selectedValue = cbxPagarCon.getSelectedItem();
            if (selectedValue != null) {
                String valorSeleccionado = selectedValue.toString();
                cs.setString(1, valorSeleccionado);

                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    idPagarCon.setText(rs.getString("id_formadepago"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }
    
    public void MostrarFormaDePago(JComboBox cbxPagarCon) {

        String sql = "";
        sql = "select * from formadepago";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxPagarCon.removeAllItems();

            while (rs.next()) {

                cbxPagarCon.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }
    
    private void registrarPagosPendientes() {

        Conectar con = new Conectar();
        Connection connect = con.getConexion();
        PreparedStatement stmt = null;
        try {
            // Obtenemos los datos de las cajas de texto
//            int empresa = Integer.parseInt(txtIdBusiness.getText().trim());
//            int cliente = Integer.parseInt(txtIdCustomer.getText().trim());

            //Validacion para la Frecuencia
            int frecuencia = 0;
            frecuencia = Integer.parseInt(txtFrecuencia.getText());

            String servicio = txtEquipo.getText();
            double precio = Double.parseDouble(txtCuota.getText());
//            String notaEmpresa = txtNotaE.getText();
            //obtener la fecha de inicio y fin
            Date fechaInicio = dateInicio.getDate();
            Date fechafin = dateTermino.getDate();

            //Calcular las fechas intermedias basadas en la frecuencia
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaInicio);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            while (calendar.getTime().before(fechafin)) {
                //Insertar la orden de servicio para cada fecha intermedia
                String sql = "INSERT INTO detalle_compra (fechaT, id_empresa, id_cliente, servicio, precio, notaEmpresa, frecuencia, inicio, fin) VALUES (?,?,?, ?, ?, ?, ?, ?, ?)";

                stmt = connect.prepareStatement(sql);
                stmt.setString(1, dateFormat.format(calendar.getTime()));
//                stmt.setInt(2, empresa);
//                stmt.setInt(3, cliente);
                stmt.setString(4, servicio);
                stmt.setDouble(5, precio);
//                stmt.setString(6, notaEmpresa);
                stmt.setInt(7, frecuencia);
                stmt.setString(8, dateFormat.format(dateInicio.getDate()));
                stmt.setString(9, dateFormat.format(dateTermino.getDate()));

                //Ejecutar la consulta
                stmt.executeUpdate();

                //Incrementar la fecha segun la frecuencia
                calendar.add(Calendar.DAY_OF_MONTH, frecuencia);
            }
            JOptionPane.showInternalMessageDialog(null, "Ordenes de servicio registradas correctamente");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
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
