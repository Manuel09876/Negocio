package Register;

import Administration.Productos;
import Administration.TipoDeUsuario;
import com.toedter.calendar.JDateChooser;
import conectar.Conectar;
import java.awt.HeadlessException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class CompraEquiposVehiculos extends javax.swing.JInternalFrame {

    DefaultTableModel model;
    int id_equipos;
    String nombre;
    String serie;
    int id_Tipo;
    int id_marca;
    int id_proveedor;
    String condicion;
    String anio_fabricacion;
    String Recibo;
    double subTotal;
    double taxes;
    double total;
    Date FechaPago;
    int Forma_Pago;
    double Inicial;
    double diferencia;
    String estado;

    public CompraEquiposVehiculos(int id_equipos, String nombre, String serie, int id_Tipo, int id_marca, int id_proveedor, String condicion, String anio_fabricacion, String Recibo, double subTotal, double taxes, double total, Date FechaPago, int Forma_Pago, double Inicial, double diferencia, String estado) {
        this.id_equipos = id_equipos;
        this.nombre = nombre;
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
        this.Forma_Pago = Forma_Pago;
        this.Inicial = Inicial;
        this.diferencia = diferencia;
        this.estado = estado;
    }

    public int getId_equipos() {
        return id_equipos;
    }

    public void setId_equipos(int id_equipos) {
        this.id_equipos = id_equipos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getAnio_fabricacion() {
        return anio_fabricacion;
    }

    public void setAnio_fabricacion(String anio_fabricacion) {
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

    public int getForma_Pago() {
        return Forma_Pago;
    }

    public void setForma_Pago(int Forma_Pago) {
        this.Forma_Pago = Forma_Pago;
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
        txtIdCred.setEnabled(false);
        txtIdMarca.setEnabled(false);
        txtIdTipo.setEnabled(false);
        txtIdProveedor.setEnabled(false);
        txtIdPagarCon.setEnabled(false);
        txtEquipo.requestFocus();
        txt_Inicial.setEnabled(false);
        txt_diferencia.setEnabled(false);
        btnRegistrarCredito.setEnabled(false);
    }

    public void Limpiar() {
        txtEquipo.setText("");
        txtSerie.setText("");
        txtAnioFabricacion.setText("");
        txtCondicion.setText("");
        txtRecibo.setText("");
        txtSubtotal.setText("");
        txtTaxes.setText("");
        txtTotal.setText("");

    }
    
    

    void MostrarTabla(String Valores) {

        try {
            String[] titulosTabla = {"id", "Nombre", "serie", "Tipo", "Marca", "Proveedor", "Condicion", "Año Fabricacion",
                "Recibo", "Subtotal", "Taxes", "Total", "FechaPago", "Forma_Pago", "Estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[15];

            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla

            String ConsultaSQL = """
                                 SELECT e.id_equipos AS id, e.nombre AS Nombre, e.serie AS serie, tme.nombre AS Tipo, m.nombre AS Marca, s.nameSuplier AS Proveedor, e.condicion AS Condicion,
                                 e.anio_fabricacion AS 'Año Fabricacion', e.Recibo AS Recibo,e.SubTotal AS SubTotal, e.Taxes AS Taxes, e.Total AS Total, e.FechaPago AS FechaPago, fp.nombre AS Forma_Pago, e.estado AS Estado
                                 FROM equipos AS e
                                 INNER JOIN tipomaquinariasvehiculos AS tme ON e.id_tipo=tme.idMaqVe 
                                 INNER JOIN marca AS m ON e.id_marca=m.id_marca 
                                 INNER JOIN suplier AS s ON e.id_proveedor=s.idSuplier
                                 INNER JOIN formadepago AS fp ON e.forma_pago=fp.id_formadepago""";

            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {
                RegistroBD[0] = result.getString("id");
                RegistroBD[1] = result.getString("Nombre");
                RegistroBD[2] = result.getString("serie");
                RegistroBD[3] = result.getString("Tipo");
                RegistroBD[4] = result.getString("Marca");
                RegistroBD[5] = result.getString("Proveedor");
                RegistroBD[6] = result.getString("Condicion");
                RegistroBD[7] = result.getString("Año Fabricacion");
                RegistroBD[8] = result.getString("Recibo");
                RegistroBD[9] = result.getString("SubTotal");
                RegistroBD[10] = result.getString("Taxes");
                RegistroBD[11] = result.getString("Total");
                RegistroBD[12] = result.getString("FechaPago");
                RegistroBD[13] = result.getString("Forma_Pago");
                RegistroBD[14] = result.getString("Estado");

                model.addRow(RegistroBD);
            }

            tbEquipos.setModel(model);
            tbEquipos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbEquipos.getColumnModel().getColumn(1).setPreferredWidth(200);
            tbEquipos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbEquipos.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbEquipos.getColumnModel().getColumn(4).setPreferredWidth(150);
            tbEquipos.getColumnModel().getColumn(5).setPreferredWidth(150);
            tbEquipos.getColumnModel().getColumn(6).setPreferredWidth(100);
            tbEquipos.getColumnModel().getColumn(7).setPreferredWidth(50);
            tbEquipos.getColumnModel().getColumn(8).setPreferredWidth(100);
            tbEquipos.getColumnModel().getColumn(9).setPreferredWidth(100);
            tbEquipos.getColumnModel().getColumn(10).setPreferredWidth(100);
            tbEquipos.getColumnModel().getColumn(11).setPreferredWidth(100);
            tbEquipos.getColumnModel().getColumn(12).setPreferredWidth(100);
            tbEquipos.getColumnModel().getColumn(13).setPreferredWidth(100);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void Guardar() {

        // Variables
        int id_equipos;
        String nombre, serie;
        int id_tipo, id_marca, id_proveedor;
        String condicion, anio_fabricacion, Recibo;
        double subTotal, taxes, total;
        java.util.Date FechaPago;
        double Inicial, diferencia;
        String estado;
        int Forma_Pago;
        String sql = "";

        //Obtenemos la informacion de las cajas de texto
        nombre = txtEquipo.getText();
        serie = txtSerie.getText();
        id_tipo = Integer.parseInt(txtIdTipo.getText());
        id_marca = Integer.parseInt(txtIdMarca.getText());
        id_proveedor = Integer.parseInt(txtIdProveedor.getText());
        condicion = txtCondicion.getText();
        anio_fabricacion = txtAnioFabricacion.getText();
        Recibo = txtRecibo.getText();
        subTotal = Double.parseDouble(txtSubtotal.getText());
        taxes = Double.parseDouble(txtTaxes.getText());
        total = Double.parseDouble(txtTotal.getText());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        FechaPago = dateFechaPago.getDate();
        Forma_Pago = Integer.parseInt(txtIdPagarCon.getText());

                
        //Consulta para evitar duplicados
        String consulta = "SELECT * FROM equipos WHERE serie = ?";

        //Consulta sql para insertar los datos (nombres como en la base de datos)
        sql = "INSERT INTO equipos (nombre, serie, id_tipo, id_marca, id_proveedor, condicion, anio_fabricacion, Recibo, "
                + "SubTotal, Taxes, Total, FechaPago, forma_pago)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        //Para almacenar los datos empleo un try cash
        try {

            //prepara la coneccion para enviar al sql (Evita ataques al sql)
            PreparedStatement pst = connect.prepareStatement(sql);

            pst.setString(1, nombre);
            pst.setString(2, serie);
            pst.setInt(3, id_tipo);
            pst.setInt(4, id_marca);
            pst.setInt(5, id_proveedor);
            pst.setString(6, condicion);
            pst.setString(7, anio_fabricacion);
            pst.setString(8, Recibo);
            pst.setDouble(9, subTotal);
            pst.setDouble(10, taxes);
            pst.setDouble(11, total);
            pst.setDate(12, new java.sql.Date(FechaPago.getTime()));
            pst.setInt(13, Forma_Pago);
            
            

            //Declara otra variable para validar los registros
            int n = pst.executeUpdate();

            //si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardo exitosamente");

                //Luego Bloquera campos
            }
            MostrarTabla("");
            Limpiar();

        } catch (SQLException ex) {
            System.out.println("Error al ejecutar la consulta SQL: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public void Eliminar(JTextField id) {

        setId_equipos(Integer.parseInt(id.getText()));

        String consulta = "DELETE from equipos where id_equipos=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);
            cs.setInt(1, getId_equipos());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");
            MostrarTabla("");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());
        }
    }

    public void SeleccionarEquipo(JTable Tabla, JTextField id, JTextField nombre, JTextField serie, JComboBox id_tipo,
            JComboBox id_marca, JComboBox id_proveedor, JTextField condicion, JTextField anio_fabricacion, JTextField Recibo,
            JTextField SubTotal, JTextField Taxes, JTextField Total, JDateChooser FechaPago, JComboBox forma_pago) throws ParseException {

        try {

            int fila = tbEquipos.getSelectedRow();

            if (fila >= 0) {

                id.setText(Tabla.getValueAt(fila, 0).toString());
                nombre.setText(Tabla.getValueAt(fila, 1).toString());
                serie.setText(Tabla.getValueAt(fila, 2).toString());
                id_tipo.setSelectedItem(Tabla.getValueAt(fila, 3).toString());
                id_marca.setSelectedItem(Tabla.getValueAt(fila, 4).toString());
                id_proveedor.setSelectedItem(Tabla.getValueAt(fila, 5).toString());
                condicion.setText(Tabla.getValueAt(fila, 6).toString());
                anio_fabricacion.setText(Tabla.getValueAt(fila, 7).toString());
                Recibo.setText(Tabla.getValueAt(fila, 8).toString());
                SubTotal.setText(Tabla.getValueAt(fila, 9).toString());
                Taxes.setText(Tabla.getValueAt(fila, 10).toString());
                Total.setText(Tabla.getValueAt(fila, 11).toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato de fecha de la tabla
                String fechaString = Tabla.getValueAt(fila, 12).toString(); // Obtener la fecha como String de la tabla
                java.util.Date fechaDate = sdf.parse(fechaString); // Convertir el String a un objeto Date
                FechaPago.setDate(fechaDate); // Establecer la fecha en el JDateChooser
                forma_pago.setSelectedItem(Tabla.getValueAt(fila, 13).toString());

                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date fecha;

            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error de Seleccion, Error: ");
        }

    }

    public void modificar(JTextField id, JTextField nombre, JTextField serie, JComboBox id_tipo, JComboBox id_marca, JComboBox id_proveedor,
            JTextField condicion, JTextField anio_fabricacion, JTextField Recibo, JTextField SubTotal, JTextField Taxes, JTextField Total,
            JDateChooser FechaPago, JComboBox forma_pago) {

        String sql = "UPDATE equipos SET nombre = ?, serie = ?, id_tipo =?, id_marca =?, id_proveedor =?, condicion=?, "
                + "anio_fabricacion=?, Recibo=?, SubTotal=?, Taxes=?, Total=?, FechaPago=?, forma_pago=? WHERE id_equipo = ?";
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, getNombre());
            ps.setString(2, getSerie());
            ps.setInt(3, getId_Tipo());
            ps.setInt(4, getId_marca());
            ps.setInt(5, getId_proveedor());
            ps.setString(6, getCondicion());
            ps.setString(8, getAnio_fabricacion());
            ps.setString(8, getRecibo());
            ps.setDouble(10, getSubTotal());
            ps.setDouble(10, getTaxes());
            ps.setDouble(12, getTotal());
            ps.setDate(13, (java.sql.Date) getFechaPago());
            ps.setInt(14, getForma_Pago());
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
        txtIdTipo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCondicion = new javax.swing.JTextField();
        txtIdCred = new javax.swing.JTextField();
        txtAnioFabricacion = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbEquipos = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtRecibo = new javax.swing.JTextField();
        txtTaxes = new javax.swing.JTextField();
        txtSubtotal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        dateFechaPago = new com.toedter.calendar.JDateChooser();
        jLabel49 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        JLabelTotalCompra = new javax.swing.JLabel();
        txt_Inicial = new javax.swing.JTextField();
        txt_diferencia = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cbxPagarCon = new javax.swing.JComboBox<>();
        txtIdPagarCon = new javax.swing.JTextField();
        btnRegistrarCredito = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Equipos, Vehiculos y Maquinarias");

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Nombre");

        jLabel2.setText("Marca");

        jLabel3.setText("Año de Fabricacion");

        jLabel4.setText("Proveedor");

        cbxMarca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbxMarca.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxMarcaItemStateChanged(evt);
            }
        });

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

        jLabel9.setText("Condicion");

        txtAnioFabricacion.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                                                .addComponent(txtIdTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(42, 42, 42)
                                .addComponent(cbxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel9))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addComponent(txtCondicion, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(44, 44, 44)
                                        .addComponent(txtAnioFabricacion, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(txtIdCred))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(btnGuardar)
                        .addGap(50, 50, 50)
                        .addComponent(btnModificar)
                        .addGap(30, 30, 30)
                        .addComponent(btnBorrar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                    .addComponent(txtIdTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtAnioFabricacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
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
                                .addComponent(btnBorrar))))
                    .addComponent(txtIdCred, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        dateFechaPago.setDateFormatString("yyyy-MM-dd");

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
        cbxPagarCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPagarConActionPerformed(evt);
            }
        });

        btnRegistrarCredito.setText("Registrar Credito");
        btnRegistrarCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarCreditoActionPerformed(evt);
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
                                .addComponent(txt_Inicial, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel20))
                            .addComponent(txtRecibo)
                            .addComponent(txtTaxes)
                            .addComponent(txtSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addComponent(txtTotal))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(jLabel49))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txt_diferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(93, 93, 93)
                                .addComponent(btnRegistrarCredito)))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(dateFechaPago, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cbxPagarCon, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(txtIdPagarCon, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 103, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JLabelTotalCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel47))
                                .addGap(132, 132, 132))))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel49)
                            .addGap(18, 18, 18)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbxPagarCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtIdPagarCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                            .addComponent(txtTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btnRegistrarCredito)))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(dateFechaPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel47)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txt_Inicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(txt_diferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLabelTotalCompra))
                .addGap(20, 20, 20))
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
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxPagarConItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxPagarConItemStateChanged
        MostrarCodigoFormaDePago(cbxPagarCon, txtIdPagarCon, txt_Inicial, txt_diferencia, btnRegistrarCredito);
    }//GEN-LAST:event_cbxPagarConItemStateChanged

    private void tbEquiposMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbEquiposMouseClicked
        try {
            SeleccionarEquipo(tbEquipos, txtId, txtEquipo, txtSerie, cbxTipoMaqVe, cbxMarca, cbxProveedor, txtCondicion, txtAnioFabricacion, txtRecibo, txtSubtotal, txtTaxes, txtTotal, dateFechaPago, cbxPagarCon);
        } catch (ParseException ex) {
            Logger.getLogger(CompraEquiposVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tbEquiposMouseClicked

    private void cbxTipoMaqVeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoMaqVeItemStateChanged
        MostrarCodigoTipo(cbxTipoMaqVe, txtIdTipo);
    }//GEN-LAST:event_cbxTipoMaqVeItemStateChanged

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        Eliminar(txtId);
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        try {
            SeleccionarEquipo(tbEquipos, txtId, txtEquipo, txtSerie, cbxTipoMaqVe, cbxMarca, cbxProveedor, txtCondicion, txtAnioFabricacion, txtRecibo, txtSubtotal, txtTaxes, txtTotal, dateFechaPago, cbxPagarCon);
        } catch (ParseException ex) {
            Logger.getLogger(CompraEquiposVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
        modificar(txtId, txtEquipo, txtSerie, cbxTipoMaqVe, cbxMarca, cbxProveedor, txtCondicion, txtAnioFabricacion, txtRecibo, txtSubtotal, txtTaxes, txtTotal, dateFechaPago, cbxPagarCon);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Guardar();
        
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void cbxProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxProveedorItemStateChanged
        MostrarCodigoProveedor(cbxProveedor, txtIdProveedor);
    }//GEN-LAST:event_cbxProveedorItemStateChanged

    private void cbxMarcaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxMarcaItemStateChanged
        MostrarCodigoMarca(cbxMarca, txtIdMarca);
    }//GEN-LAST:event_cbxMarcaItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnRegistrarCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarCreditoActionPerformed
        Bases.CreditoEqVe objCredito = new Bases.CreditoEqVe();
        objCredito.EnviarDatos(txt_Inicial.getText(), txt_diferencia.getText());
        objCredito.setVisible(true);
    }//GEN-LAST:event_btnRegistrarCreditoActionPerformed

    private void cbxPagarConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxPagarConActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxPagarConActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel JLabelTotalCompra;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRegistrarCredito;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxMarca;
    private javax.swing.JComboBox<String> cbxPagarCon;
    private javax.swing.JComboBox<String> cbxProveedor;
    private javax.swing.JComboBox<String> cbxTipoMaqVe;
    private com.toedter.calendar.JDateChooser dateFechaPago;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbEquipos;
    private javax.swing.JTextField txtAnioFabricacion;
    private javax.swing.JTextField txtCondicion;
    private javax.swing.JTextField txtEquipo;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdCred;
    private javax.swing.JTextField txtIdMarca;
    private javax.swing.JTextField txtIdPagarCon;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdTipo;
    private javax.swing.JTextField txtRecibo;
    private javax.swing.JTextField txtSerie;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTaxes;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txt_Inicial;
    private javax.swing.JTextField txt_diferencia;
    // End of variables declaration//GEN-END:variables

    public void MostrarProveedor(JComboBox cbxProveedor) {

        String sql = "";
        sql = "select * from suplier";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxProveedor.removeAllItems();

            while (rs.next()) {

                cbxProveedor.addItem(rs.getString("nameSuplier"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoProveedor(JComboBox cbxProveedor, JTextField idProveedor) {

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

                if (rs.next()) {
                    idProveedor.setText(rs.getString("idSuplier"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    public void MostrarTipo(JComboBox cbxTipoMaqVe) {

        String sql = "";
        sql = "select * from tipomaquinariasvehiculos";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxTipoMaqVe.removeAllItems();

            while (rs.next()) {

                cbxTipoMaqVe.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoTipo(JComboBox cbxTipoMaqVe, JTextField idTipoMaqVe) {

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

                if (rs.next()) {
                    idTipoMaqVe.setText(rs.getString("idMaqVe"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    public void MostrarMarca(JComboBox cbxMarca) {

        String sql = "";
        sql = "select * from marca";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxMarca.removeAllItems();

            while (rs.next()) {

                cbxMarca.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoMarca(JComboBox cbxMarca, JTextField idMarca) {

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

                if (rs.next()) {
                    idMarca.setText(rs.getString("id_marca"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    public void MostrarCodigoFormaDePago(JComboBox cbxPagarCon, JTextField idPagarCon, JTextField txt_Inicial, JTextField txt_diferencia, JButton btnRegistrarCredito) {
    String consulta = "SELECT formadepago.id_formadepago FROM formadepago WHERE formadepago.nombre=?";
    
    try {
        if (cbxPagarCon.getSelectedIndex() == -1) {
            return; // Si no se ha seleccionado ningún elemento en el JComboBox, salir del método
        }

        CallableStatement cs = con.getConexion().prepareCall(consulta);
        
        Object selectedValue = cbxPagarCon.getSelectedItem();
        if (selectedValue != null) {
            String valorSeleccionado = selectedValue.toString();
            cs.setString(1, valorSeleccionado);
            cs.execute();

            ResultSet rs = cs.executeQuery();
            
            if (rs.next()) {
                idPagarCon.setText(rs.getString("id_formadepago"));
                
                // Validar el código de forma de pago y habilitar componentes
                if (Integer.parseInt(rs.getString("id_formadepago")) == 3 || Integer.parseInt(rs.getString("id_formadepago")) == 6) {
                    txt_Inicial.setEnabled(true);
                    txt_diferencia.setEnabled(true);
                    btnRegistrarCredito.setEnabled(true);
                } else {
                    txt_Inicial.setEnabled(false);
                    txt_diferencia.setEnabled(false);
                    btnRegistrarCredito.setEnabled(false);
                }
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

}
