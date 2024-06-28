package Register;

import Administration.Productos;
import Administration.TipoDeUsuario;
import Bases.CreditoEqVe;
import com.toedter.calendar.JDateChooser;
import conectar.Conectar;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class CompraEquiposVehiculos extends javax.swing.JInternalFrame {

    DefaultTableModel model;
    int id;
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
        this.id = id_equipos;
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
        txtId_Compra.setEnabled(false);
        txtIdCred.setEnabled(false);
        txtIdMarca.setEnabled(false);
        txtIdTipo.setEnabled(false);
        txtIdProveedor.setEnabled(false);
        txtIdPagarCon.setEnabled(false);
        txtEquipo.requestFocus();
        txt_Inicial.setEnabled(false);
        txt_diferencia.setEnabled(false);
        btnRegistrarCredito.setEnabled(false);
        initListeners();

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
        cbxPagarCon.setSelectedItem("");
        cbxMarca.setSelectedItem("");
        cbxProveedor.setSelectedItem("");
        cbxTipoMaqVe.setSelectedItem("");

    }

    public void MostrarTabla(String Valores) {

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
                                 INNER JOIN formadepago AS fp ON e.forma_pago=fp.id_formadepago ORDER BY e.id_equipos DESC""";

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

    public void Guardar() {

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

    public void GuardarCredito() {

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
                + "SubTotal, Taxes, Total, FechaPago, forma_pago,estado)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,'Pendiente')";

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

        setId(Integer.parseInt(id.getText()));

        String consulta = "DELETE from equipos where id_equipos=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);
            cs.setInt(1, getId());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");
            MostrarTabla("");
            Limpiar();
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
                + "anio_fabricacion=?, Recibo=?, SubTotal=?, Taxes=?, Total=?, FechaPago=?, forma_pago=? WHERE id_equipos = ?";
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            ps.setString(1, getNombre());
            ps.setString(2, getSerie());
            ps.setInt(3, getId_Tipo());
            ps.setInt(4, getId_marca());
            ps.setInt(5, getId_proveedor());
            ps.setString(6, getCondicion());
            ps.setString(7, getAnio_fabricacion());
            ps.setString(8, getRecibo());
            ps.setDouble(9, getSubTotal());
            ps.setDouble(10, getTaxes());
            ps.setDouble(11, getTotal());
            ps.setDate(12, (java.sql.Date) getFechaPago());
            ps.setInt(13, getForma_Pago());
            ps.setInt(14, getId());
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
        btnSalir = new javax.swing.JButton();
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
        txtId_Compra = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbxTipoMaqVe = new javax.swing.JComboBox<>();
        txtIdTipo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCondicion = new javax.swing.JTextField();
        txtIdCred = new javax.swing.JTextField();
        txtAnioFabricacion = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
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
        txt_Inicial = new javax.swing.JTextField();
        txt_diferencia = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cbxPagarCon = new javax.swing.JComboBox<>();
        txtIdPagarCon = new javax.swing.JTextField();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtTotal1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtFrecuencia = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtInteres = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtNumeroCuotas = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtValorCuota = new javax.swing.JTextField();
        btnModifCred = new javax.swing.JButton();
        btnEliminarCred = new javax.swing.JButton();
        dateFechaPagoCred = new com.toedter.calendar.JDateChooser();
        jLabel21 = new javax.swing.JLabel();
        btnRegistrarCredito = new javax.swing.JButton();
        txtIdCompra = new javax.swing.JTextField();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Equipos, Vehiculos y Maquinarias");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(418, 28, -1, -1));

        jLabel1.setText("Nombre");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(88, 35, -1, -1));

        jLabel2.setText("Marca");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 156, -1, -1));

        jLabel3.setText("Año de Fabricacion");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 236, -1, -1));

        jLabel4.setText("Proveedor");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 190, -1, -1));
        jPanel1.add(txtEquipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 32, 226, -1));

        cbxMarca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbxMarca.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxMarcaItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 156, 226, -1));

        cbxProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbxProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxProveedorItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(159, 190, 226, -1));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 345, -1, -1));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exchange.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel1.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(182, 345, -1, -1));

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnBorrar.setText("Eliminar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(327, 350, -1, -1));
        jPanel1.add(txtIdMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 156, 86, -1));
        jPanel1.add(txtIdProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 190, 86, -1));

        jLabel5.setText("Serie");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 73, -1, -1));
        jPanel1.add(txtSerie, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 70, 226, -1));
        jPanel1.add(txtId_Compra, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 23, 59, 41));

        jLabel6.setText("Tipo");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 119, -1, -1));

        cbxTipoMaqVe.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoMaqVeItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxTipoMaqVe, new org.netbeans.lib.awtextra.AbsoluteConstraints(163, 116, 224, -1));
        jPanel1.add(txtIdTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 116, 86, -1));

        jLabel9.setText("Condicion de Compra");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 295, -1, -1));
        jPanel1.add(txtCondicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(159, 292, 226, -1));
        jPanel1.add(txtIdCred, new org.netbeans.lib.awtextra.AbsoluteConstraints(403, 292, 95, 35));

        txtAnioFabricacion.setToolTipText("");
        jPanel1.add(txtAnioFabricacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 233, 224, -1));

        jButton2.setText("Nuevo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 400, -1, -1));

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(182, 400, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 510, 612));

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 706, 219));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(522, 355, 727, -1));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setText("Recibo N°");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 22, -1, -1));

        jLabel14.setText("SubTotal");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 56, -1, -1));

        jLabel15.setText("Taxes");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 85, -1, -1));
        jPanel3.add(txtRecibo, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 19, 120, -1));

        txtTaxes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTaxesKeyPressed(evt);
            }
        });
        jPanel3.add(txtTaxes, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 82, 120, -1));
        jPanel3.add(txtSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 53, 120, -1));

        jLabel16.setText("Total");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 119, -1, -1));

        txtTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTotalKeyPressed(evt);
            }
        });
        jPanel3.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 116, 120, -1));

        jLabel17.setText("Fecha");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 157, -1, -1));

        dateFechaPago.setDateFormatString("yyyy-MM-dd");
        jPanel3.add(dateFechaPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 151, 120, -1));

        jLabel49.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel49.setText("Pagar con");
        jPanel3.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 14, -1, -1));

        txt_Inicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_InicialKeyPressed(evt);
            }
        });
        jPanel3.add(txt_Inicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 192, 88, -1));

        txt_diferencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_diferenciaKeyReleased(evt);
            }
        });
        jPanel3.add(txt_diferencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 232, 92, -1));

        jLabel19.setText("Inicial");
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 195, -1, -1));

        jLabel20.setText("Diferencia");
        jPanel3.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 235, -1, -1));

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
        jPanel3.add(cbxPagarCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(414, 11, 176, -1));
        jPanel3.add(txtIdPagarCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(612, 11, 91, -1));
        jPanel3.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, 170, -1));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        jPanel3.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 270, -1, -1));

        jLabel7.setText("Total");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(359, 36, -1, -1));
        jPanel3.add(txtTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(449, 33, 100, -1));

        jLabel12.setText("Frecuencia de Pago");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(323, 66, -1, -1));
        jPanel3.add(txtFrecuencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(449, 63, 100, -1));

        jLabel8.setText("Tasa de Interes");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(349, 96, -1, -1));
        jPanel3.add(txtInteres, new org.netbeans.lib.awtextra.AbsoluteConstraints(449, 93, 100, -1));

        jLabel10.setText("%");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(567, 96, -1, -1));

        jLabel11.setText("Numero de cuotas");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(333, 119, -1, 30));
        jPanel3.add(txtNumeroCuotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(449, 123, 100, -1));

        jLabel18.setText("Valor de cuota");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(339, 156, -1, -1));
        jPanel3.add(txtValorCuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(449, 153, 100, -1));

        btnModifCred.setText("Modificar");
        jPanel3.add(btnModifCred, new org.netbeans.lib.awtextra.AbsoluteConstraints(609, 53, -1, -1));

        btnEliminarCred.setText("Eliminar");
        btnEliminarCred.setToolTipText("");
        jPanel3.add(btnEliminarCred, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 103, -1, -1));

        dateFechaPagoCred.setDateFormatString("yyyy-MM-dd");
        jPanel3.add(dateFechaPagoCred, new org.netbeans.lib.awtextra.AbsoluteConstraints(449, 193, 150, -1));

        jLabel21.setText("Proxima Fecha de Pago");
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 195, -1, -1));

        btnRegistrarCredito.setText("Registrar Credito");
        btnRegistrarCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarCreditoActionPerformed(evt);
            }
        });
        jPanel3.add(btnRegistrarCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 240, -1, -1));
        jPanel3.add(txtIdCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 290, 80, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(522, 6, 710, 330));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxPagarConItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxPagarConItemStateChanged
        MostrarCodigoFormaDePago(cbxPagarCon, txtIdPagarCon, txt_Inicial, txt_diferencia, btnRegistrarCredito);
    }//GEN-LAST:event_cbxPagarConItemStateChanged

    private void tbEquiposMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbEquiposMouseClicked
        try {
            SeleccionarEquipo(tbEquipos, txtId_Compra, txtEquipo, txtSerie, cbxTipoMaqVe, cbxMarca, cbxProveedor, txtCondicion, txtAnioFabricacion, txtRecibo, txtSubtotal, txtTaxes, txtTotal, dateFechaPago, cbxPagarCon);
        } catch (ParseException ex) {
            Logger.getLogger(CompraEquiposVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tbEquiposMouseClicked

    private void cbxTipoMaqVeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoMaqVeItemStateChanged
        MostrarCodigoTipo(cbxTipoMaqVe, txtIdTipo);
    }//GEN-LAST:event_cbxTipoMaqVeItemStateChanged

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        Eliminar(txtId_Compra);
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        try {
            SeleccionarEquipo(tbEquipos, txtId_Compra, txtEquipo, txtSerie, cbxTipoMaqVe, cbxMarca, cbxProveedor, txtCondicion, txtAnioFabricacion, txtRecibo, txtSubtotal, txtTaxes, txtTotal, dateFechaPago, cbxPagarCon);
        } catch (ParseException ex) {
            Logger.getLogger(CompraEquiposVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
        modificar(txtId_Compra, txtEquipo, txtSerie, cbxTipoMaqVe, cbxMarca, cbxProveedor, txtCondicion, txtAnioFabricacion, txtRecibo, txtSubtotal, txtTaxes, txtTotal, dateFechaPago, cbxPagarCon);
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

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnRegistrarCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarCreditoActionPerformed

        registrarPagosPendientes();
        GuardarCredito();
        LimpiartxtCred();
    }//GEN-LAST:event_btnRegistrarCreditoActionPerformed

    private void cbxPagarConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxPagarConActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxPagarConActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Limpiar();
        txtEquipo.requestFocus();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void txt_diferenciaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_diferenciaKeyReleased

    }//GEN-LAST:event_txt_diferenciaKeyReleased

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        Limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txt_InicialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_InicialKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            double total = Double.parseDouble(txtTotal.getText());
            double inicial = Double.parseDouble(txt_Inicial.getText());
            double diferencia = total - inicial;
        }
    }//GEN-LAST:event_txt_InicialKeyPressed

    private void txtTaxesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxesKeyPressed
       
    }//GEN-LAST:event_txtTaxesKeyPressed

    private void txtTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalKeyPressed

    }//GEN-LAST:event_txtTotalKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminarCred;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModifCred;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRegistrarCredito;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxMarca;
    private javax.swing.JComboBox<String> cbxPagarCon;
    private javax.swing.JComboBox<String> cbxProveedor;
    private javax.swing.JComboBox<String> cbxTipoMaqVe;
    private com.toedter.calendar.JDateChooser dateFechaPago;
    private com.toedter.calendar.JDateChooser dateFechaPagoCred;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JTable tbEquipos;
    private javax.swing.JTextField txtAnioFabricacion;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCondicion;
    private javax.swing.JTextField txtEquipo;
    private javax.swing.JTextField txtFrecuencia;
    private javax.swing.JTextField txtIdCompra;
    private javax.swing.JTextField txtIdCred;
    private javax.swing.JTextField txtIdMarca;
    private javax.swing.JTextField txtIdPagarCon;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdTipo;
    private javax.swing.JTextField txtId_Compra;
    private javax.swing.JTextField txtInteres;
    private javax.swing.JTextField txtNumeroCuotas;
    private javax.swing.JTextField txtRecibo;
    private javax.swing.JTextField txtSerie;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTaxes;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotal1;
    private javax.swing.JTextField txtValorCuota;
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
                        btnGuardar.setEnabled(false);
                        txtTotal1.setEnabled(true);
                        txtFrecuencia.setEnabled(true);
                        txtInteres.setEnabled(true);
                        txtNumeroCuotas.setEnabled(true);
                        txtValorCuota.setEnabled(true);
                    } else {
                        txt_Inicial.setEnabled(false);
                        txt_diferencia.setEnabled(false);
                        btnRegistrarCredito.setEnabled(false);
                        btnGuardar.setEnabled(true);
                        txtTotal1.setEnabled(false);
                        txtFrecuencia.setEnabled(false);
                        txtInteres.setEnabled(false);
                        txtNumeroCuotas.setEnabled(false);
                        txtValorCuota.setEnabled(false);
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

    // Método para hallar el id_Compra que se está ejecutando en ese momento
    public int IdCompra() {
        int id = 0;
        String sql = "SELECT MAX(id_equipos) FROM equipos";
        Conectar con = new Conectar();
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connect = con.getConexion();
            ps = connect.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
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
            }
        }
        return id;
    }

    public void registrarPagosPendientes() {
        Conectar con = new Conectar();
        Connection connect = null;
        PreparedStatement stmt = null;
        try {
            connect = con.getConexion();

            // Obtener los datos de las cajas de texto
            int id = IdCompra();
            int frecuencia = Integer.parseInt(txtFrecuencia.getText());
            double interes = Double.parseDouble(txtInteres.getText());
            int numeroCuotas = Integer.parseInt(txtNumeroCuotas.getText());
            double valorCuota = Double.parseDouble(txtValorCuota.getText());
            double total = Double.parseDouble(txtTotal.getText());
            double diferencia = total;

            // Obtener la fecha de inicio
            Date fechaInicio = dateFechaPagoCred.getDate();
            if (fechaInicio == null) {
                JOptionPane.showMessageDialog(null, "La fecha de inicio es nula. Por favor selecciona una fecha válida.");
                return; // Añadido return para evitar continuar si la fecha es nula
            }

            // Preparar la inserción de pagos en la base de datos
            String sql = "INSERT INTO credito (id_compra, frecuencia, fechaPago, interes, NumeroCuotas, cuota, Diferencia, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, 'Pendiente')";
            stmt = connect.prepareStatement(sql);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaInicio);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 1; i <= numeroCuotas; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, frecuencia);

                Date fechaPago = calendar.getTime();
                double cuotaActual = valorCuota;

                // Ajustar la última cuota si la diferencia es menor que el valor de la cuota
                if (i == numeroCuotas && diferencia < valorCuota) {
                    cuotaActual = diferencia;
                }

                diferencia -= cuotaActual;

                stmt.setInt(1, id);
                stmt.setInt(2, frecuencia);
                stmt.setString(3, dateFormat.format(fechaPago));
                stmt.setDouble(4, interes);
                stmt.setInt(5, i); // Número de la cuota actual
                stmt.setDouble(6, cuotaActual);
                stmt.setDouble(7, diferencia); // Diferencia actualizada

                stmt.executeUpdate();

//            // Mostrar aviso dos días antes de la fecha de pago
//            Calendar avisoCalendar = Calendar.getInstance();
//            avisoCalendar.setTime(fechaPago);
//            avisoCalendar.add(Calendar.DAY_OF_MONTH, -2);
//            Date fechaAviso = avisoCalendar.getTime();
//            DateFormat avisoFormat = new SimpleDateFormat("dd/MM/yyyy");
//            JOptionPane.showMessageDialog(null, "¡Atención! Quedan 2 días para la fecha de pago de la cuota " + i + ": " + avisoFormat.format(fechaAviso));
            }

            JOptionPane.showMessageDialog(null, "Crédito registrado correctamente con todas las fechas de pago.");

        } catch (NumberFormatException ex) {
            System.out.println("Error " + ex);
            JOptionPane.showMessageDialog(null, "Error al parsear un valor numérico: " + ex.getMessage());
        } catch (SQLException ex) {
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
                JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }

    private void LimpiartxtCred() {
        txtTotal1.setText("");
        txtFrecuencia.setText("");
        txtNumeroCuotas.setText("");
        txtValorCuota.setText("");
        txtInteres.setText("");
        txt_Inicial.setText("");
        txt_diferencia.setText("");
        cbxPagarCon.setSelectedItem("");
        cbxMarca.setSelectedItem("");
        cbxProveedor.setSelectedItem("");
        cbxTipoMaqVe.setSelectedItem("");

    }

    private void LimpiarCampos() {
        txt_diferencia.setText("");
        txtFrecuencia.setText("");
        txt_Inicial.setText("");
        txtInteres.setText("");
        txtNumeroCuotas.setText("");
        txtValorCuota.setText("");
        dateFechaPagoCred.setDateFormatString("");
        txtTotal.setText("");
    }

    private void initListeners() {
        txt_Inicial.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularDiferencia();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calcularDiferencia();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calcularDiferencia();
            }
        });
    }

    private void calcularDiferencia() {
        try {
            double total = Double.parseDouble(txtTotal.getText());
            double inicial = Double.parseDouble(txt_Inicial.getText());
            double diferencia = total - inicial;
            txt_diferencia.setText(String.valueOf(diferencia));
        } catch (NumberFormatException ex) {
            // Manejo de excepción en caso de que los textos no sean números válidos
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos.");
        }

    }

}
