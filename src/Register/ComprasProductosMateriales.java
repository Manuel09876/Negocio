package Register;

import Administration.Productos;
import com.toedter.calendar.JDateChooser;
import conectar.Conectar;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ComprasProductosMateriales extends javax.swing.JInternalFrame {

    Conectar con = new Conectar();
    Connection connect = con.getConexion();
    DefaultTableModel model = new DefaultTableModel();
    private int item = 0;
    double Totalpagar = 0.00;
    Productos pro = new Productos();

    // Variables para la compra
    int id;
    int id_CompraProMat;
    String Recibo;
    int id_Tipo;
    int id_Producto;
    int cantidad;
    double precioUnit;
    int id_proveedor;
    int id_Marca;
    Date fecha;
    String estado;
    double SubTotal;
    double Taxes;
    double total;
    double Total;
    int id_FormaPago;

    public int getId_CompraProMat() {
        return id_CompraProMat;
    }

    public double getTotalpagar() {
        return Totalpagar;
    }

    public void setTotalpagar(double Totalpagar) {
        this.Totalpagar = Totalpagar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(double SubTotal) {
        this.SubTotal = SubTotal;
    }

    public double getTaxes() {
        return Taxes;
    }

    public void setTaxes(double Taxes) {
        this.Taxes = Taxes;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getId_FormaPago() {
        return id_FormaPago;
    }

    public void setId_FormaPago(int id_FormaPago) {
        this.id_FormaPago = id_FormaPago;
    }

    public void setId_CompraProMat(int id_CompraProMat) {
        this.id_CompraProMat = id_CompraProMat;
    }

    public String getRecibo() {
        return Recibo;
    }

    public void setRecibo(String Recibo) {
        this.Recibo = Recibo;
    }

    public int getId_Tipo() {
        return id_Tipo;
    }

    public void setId_Tipo(int id_Tipo) {
        this.id_Tipo = id_Tipo;
    }

    public int getId_Producto() {
        return id_Producto;
    }

    public void setId_Producto(int id_Producto) {
        this.id_Producto = id_Producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(double precioUnit) {
        this.precioUnit = precioUnit;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public int getId_Marca() {
        return id_Marca;
    }

    public void setId_Marca(int id_Marca) {
        this.id_Marca = id_Marca;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Conexión
    PreparedStatement ps;
    ResultSet rs;

    public ComprasProductosMateriales() {
        initComponents();
        AutoCompleteDecorator.decorate(cbxProveedor);
        AutoCompleteDecorator.decorate(cbxMarca);
        AutoCompleteDecorator.decorate(cbxProducto);
        AutoCompleteDecorator.decorate(cbxPagarCon);
        AutoCompleteDecorator.decorate(cbxTipoProducto);

        MostrarProveedor(cbxProveedor);
        MostrarMarca(cbxMarca);
        MostrarProducto(cbxProducto);
        MostrarFormaDePago(cbxPagarCon);
        MostrarTipo(cbxTipoProducto);

        txtIdMarca.setEnabled(false);
        txtIdProducto.setEnabled(false);
        txtIdProveedor.setEnabled(false);
        txtIdTipoPro.setEnabled(false);
        txtId.setEnabled(false);
        txtIdPagarCon.setEnabled(false);
        txtId.setEnabled(false);
        txtTotalxP.setEnabled(false);

        initListeners();

        // Inicialización del JTable y su modelo
        model.setColumnIdentifiers(new Object[]{"Descripción", "Tipo", "Marca", "Cantidad", "Precio Unitario", "Total", "Item"});
        tbCompraProducto.setModel(model); // Asignar el modelo a la tabla

        // Permitir la selección de filas en la tabla
        tbCompraProducto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbCompraProducto.setColumnSelectionAllowed(false);
        tbCompraProducto.setRowSelectionAllowed(true);

        // Añadir ListSelectionListener para manejar la selección de filas
        tbCompraProducto.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = tbCompraProducto.getSelectedRow();
                    if (selectedRow != -1) {
                        // Obtener los datos de la fila seleccionada
                        Object descripcion = tbCompraProducto.getValueAt(selectedRow, 0);
                        Object Tipo = tbCompraProducto.getValueAt(selectedRow, 1);
                        Object Marca = tbCompraProducto.getValueAt(selectedRow, 2);
                        Object cantidad = tbCompraProducto.getValueAt(selectedRow, 3);
                        Object subTotal = tbCompraProducto.getValueAt(selectedRow, 4);
                        Object total = tbCompraProducto.getValueAt(selectedRow, 5);

                        // Mostrar los datos de la fila seleccionada en la consola
//                        System.out.println("Fila seleccionada: " + selectedRow);
//                        System.out.println("Descripción: " + descripcion);
//                        System.out.println("Cantidad: " + cantidad);
//                        System.out.println("Subtotal: " + subTotal);
//                        System.out.println("Total: " + total);
                        // Aquí puedes actualizar cualquier campo de texto o componente con los datos de la fila seleccionada
                        // Ejemplo: actualizar componentes con los valores seleccionados
                        cbxProducto.setSelectedItem(descripcion.toString());
                        txtCant.setText(cantidad.toString());
                        txtPrecioUnitario.setText(subTotal.toString());
                        txtTotalxP.setText(total.toString());
                    }
                }
            }
        });

        // Configurar el botón de eliminación
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        cbxProducto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                cbxProductoKeyPressed(evt);
            }
        });

        // Prueba de la conexión y consulta SQL
        mostrarStock("NombreDelProducto");

        // Debugging: Verificar que el modelo tiene las columnas configuradas
//        System.out.println("Columnas en el modelo: " + model.getColumnCount()); // Debe ser 5
//        System.out.println("Filas en el modelo (inicial): " + model.getRowCount()); // Debe ser 0
    }

    public void RegistrarCompra() {
        String recibo = txtNumeroRecibo.getText().trim();
        if (recibo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: El recibo no puede estar vacío.");
            return;
        }

        try {
            id_proveedor = Integer.parseInt(txtIdProveedor.getText());
            SubTotal = Double.parseDouble(txtSubTotal.getText().trim());
            Taxes = Double.parseDouble(txtTaxes.getText().trim());
            Total = Double.parseDouble(JLabelTotalCompra.getText().trim());
            id_FormaPago = Integer.parseInt(txtIdPagarCon.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Por favor verifica los valores numéricos.");
            return;
        }

        fecha = dateFecha.getDate();
        if (fecha == null) {
            JOptionPane.showMessageDialog(null, "Error: La fecha no puede estar vacía.");
            return;
        }
        java.sql.Date sqlDate = new java.sql.Date(fecha.getTime());

        // Consulta para insertar los datos
        String sql = "INSERT INTO compraproductosmateriales (Recibo, id_proveedor, SubTotal, Taxes, Total, id_FormaPago, fecha) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, recibo);
            pst.setInt(2, id_proveedor);
            pst.setDouble(3, SubTotal);
            pst.setDouble(4, Taxes);
            pst.setDouble(5, Total);
            pst.setInt(6, id_FormaPago);
            pst.setDate(7, sqlDate);

            int n = pst.executeUpdate();

            if (n > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    id_CompraProMat = rs.getInt(1); // Obtener el ID generado
                    txtId.setText(String.valueOf(id_CompraProMat)); // Actualizar el campo de texto
                    JOptionPane.showMessageDialog(null, "El registro se guardó con éxito");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar registro: " + e.toString());
        }
    }

    public void RegistrarDetalleCompra() {
        if (id_CompraProMat <= 0) {
            JOptionPane.showMessageDialog(null, "Error: El ID de la compra no es válido.");
            return;
        }

        fecha = dateFecha.getDate();
        if (fecha == null) {
            JOptionPane.showMessageDialog(null, "Error: La fecha no puede estar vacía.");
            return;
        }
        java.sql.Date sqlDate = new java.sql.Date(fecha.getTime());

        for (int i = 0; i < tbCompraProducto.getRowCount(); i++) {
            // Validación de los valores en la tabla
            String id_productoStr = txtIdProducto.getText().trim();
            String id_TipoStr = txtIdTipoPro.getText().trim();
            String id_MarcaStr = txtIdMarca.getText().trim();
            String CantidadStr = tbCompraProducto.getValueAt(i, 3).toString().trim(); // Cantidad
            String PrecioStr = tbCompraProducto.getValueAt(i, 4).toString().trim(); // Precio Unitario
            String TotalStr = tbCompraProducto.getValueAt(i, 5).toString().trim(); // Total

            // Verificación de entradas vacías
            if (id_productoStr.isEmpty() || id_TipoStr.isEmpty() || id_MarcaStr.isEmpty()
                    || CantidadStr.isEmpty() || PrecioStr.isEmpty() || TotalStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Error: Hay campos vacíos en la fila " + (i + 1));
                continue;
            }

            try {
                int id_producto = Integer.parseInt(id_productoStr);
                int id_Tipo = Integer.parseInt(id_TipoStr);
                int id_Marca = Integer.parseInt(id_MarcaStr);
                int Cantidad = Integer.parseInt(CantidadStr);
                double Precio = Double.parseDouble(PrecioStr);
                double Total = Double.parseDouble(TotalStr);

                // Consulta para insertar los datos
                String sql = "INSERT INTO detalle_compraproductosmateriales (id_CompProMat, id_producto, id_Tipo, id_Marca, cantidad, precio_Unitario, Total, fecha) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                // Para almacenar los datos empleo un try-catch
                try {
                    PreparedStatement pst = connect.prepareStatement(sql);

                    pst.setInt(1, id_CompraProMat);
                    pst.setInt(2, id_producto);
                    pst.setInt(3, id_Tipo);
                    pst.setInt(4, id_Marca);
                    pst.setInt(5, Cantidad);
                    pst.setDouble(6, Precio);
                    pst.setDouble(7, Total);
                    pst.setDate(8, sqlDate);

                    int n = pst.executeUpdate();

                    if (n > 0) {
                        JOptionPane.showMessageDialog(null, "El detalle se guardó con éxito");
                    }

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al guardar detalle: " + e.toString());
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error en la fila " + (i + 1) + ": " + e.getMessage());
            }
        }
    }

    // Método para eliminar una fila de la tabla
    private void eliminarFila() {
        int selectedRow = tbCompraProducto.getSelectedRow();

        if (selectedRow != -1) {
            System.out.println("Fila seleccionada: " + selectedRow);
            DefaultTableModel model = (DefaultTableModel) tbCompraProducto.getModel();
            // Obtener los datos de la fila seleccionada
            Object descripcion = tbCompraProducto.getValueAt(selectedRow, 0);
            Object cantidad = tbCompraProducto.getValueAt(selectedRow, 1);
            Object subTotal = tbCompraProducto.getValueAt(selectedRow, 2);
            Object total = tbCompraProducto.getValueAt(selectedRow, 3);

            // Mostrar los datos de la fila seleccionada en la consola
            System.out.println("Fila seleccionada: " + selectedRow);
            System.out.println("Descripción: " + descripcion);
            System.out.println("Cantidad: " + cantidad);
            System.out.println("Subtotal: " + subTotal);
            System.out.println("Total: " + total);

            // Aquí puedes actualizar cualquier campo de texto o componente con los datos de la fila seleccionada
            cbxProducto.setSelectedItem(descripcion.toString());
            txtCant.setText(cantidad.toString());
            txtPrecioUnitario.setText(subTotal.toString());
            txtTotalxP.setText(total.toString());

            // Eliminar la fila seleccionada
            model.removeRow(selectedRow);
            TotalPagar(); // Actualiza el total a pagar
            LimpiarCompra();
            cbxProducto.requestFocus();
        } else {
//        JOptionPane.showMessageDialog(null, "No se ha seleccionado ninguna fila. Por favor, seleccione una fila para eliminar.");
        }
    }

    private void LimpiarTablaCompra() {
        model = (DefaultTableModel) tbCompraProducto.getModel();
        int fila = tbCompraProducto.getRowCount();
        for (int i = 0; i < fila; i++) {
            model.removeRow(0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tbCompraProducto = new javax.swing.JTable();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtNumeroRecibo = new javax.swing.JTextField();
        txtCant = new javax.swing.JTextField();
        txtPrecioUnitario = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        cbxProveedor = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();
        JLabelTotalCompra = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txtTaxes = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        txtIdProveedor = new javax.swing.JTextField();
        cbxProducto = new javax.swing.JComboBox<>();
        txtIdMarca = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        cbxMarca = new javax.swing.JComboBox<>();
        txtIdProducto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbxPagarCon = new javax.swing.JComboBox<>();
        txtIdPagarCon = new javax.swing.JTextField();
        dateFecha = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbxTipoProducto = new javax.swing.JComboBox<>();
        txtIdTipoPro = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txt_Inicial = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txt_diferencia = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTotal1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtFrecuencia = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtInteres = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        dateFechaPagoCred = new com.toedter.calendar.JDateChooser();
        btnRegistrarCredito = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtNumeroCuotas = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtValorCuota = new javax.swing.JTextField();
        txtTotalxP = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnMostrarStock = new javax.swing.JButton();
        txtSubTotal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 51, 153));
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Compras Productos y Materiales");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel16.setBackground(new java.awt.Color(204, 204, 255));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbCompraProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbCompraProducto.setRowHeight(23);
        tbCompraProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCompraProductoMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tbCompraProducto);

        jPanel16.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 1050, 210));

        jLabel40.setText("Producto");
        jPanel16.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel41.setText("Recibo N°");
        jPanel16.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, -1, -1));

        jLabel42.setText("Cant");
        jPanel16.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 100, -1, -1));

        jLabel43.setText("Precio Unitario");
        jPanel16.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, -1, -1));
        jPanel16.add(txtNumeroRecibo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 2, 110, 30));

        txtCant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantKeyPressed(evt);
            }
        });
        jPanel16.add(txtCant, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 90, 80, 30));

        txtPrecioUnitario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrecioUnitarioKeyPressed(evt);
            }
        });
        jPanel16.add(txtPrecioUnitario, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, 80, 30));

        jLabel46.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel46.setText("Proveedor");
        jPanel16.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        cbxProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxProveedorItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 150, -1));

        jLabel47.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel47.setText("Total Pagar");
        jPanel16.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 470, -1, -1));

        JLabelTotalCompra.setText("-----------");
        jPanel16.add(JLabelTotalCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 470, 90, 20));

        jLabel49.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel49.setText("Pagar con");
        jPanel16.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 70, -1, -1));

        txtTaxes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTaxesKeyPressed(evt);
            }
        });
        jPanel16.add(txtTaxes, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 420, 70, 30));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel16.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 10, -1, -1));
        jPanel16.add(txtIdProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 80, -1));

        cbxProducto.setEditable(true);
        cbxProducto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxProductoItemStateChanged(evt);
            }
        });
        cbxProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxProductoMouseClicked(evt);
            }
        });
        cbxProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxProductoKeyPressed(evt);
            }
        });
        jPanel16.add(cbxProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 150, -1));
        jPanel16.add(txtIdMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 100, 80, -1));

        jLabel51.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel51.setText("Marca");
        jPanel16.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, 20));

        cbxMarca.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxMarcaItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 150, -1));
        jPanel16.add(txtIdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 40, 80, -1));

        jLabel1.setText("tax");
        jPanel16.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 430, -1, -1));

        cbxPagarCon.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxPagarConItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxPagarCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 70, 140, -1));
        jPanel16.add(txtIdPagarCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 70, 80, -1));

        dateFecha.setDateFormatString("yyyy-MM-dd");
        jPanel16.add(dateFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, 130, -1));

        jLabel2.setText("Fecha de Compra");
        jPanel16.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, -1, -1));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel16.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 110, 110, -1));
        jPanel16.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, -1));
        jPanel16.add(txtStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 80, 30));

        jLabel4.setText("Tipo");
        jPanel16.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        cbxTipoProducto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoProductoItemStateChanged(evt);
            }
        });
        jPanel16.add(cbxTipoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 150, -1));
        jPanel16.add(txtIdTipoPro, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 80, -1));

        jLabel19.setText("Inicial");
        jPanel16.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, -1, -1));

        txt_Inicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_InicialKeyPressed(evt);
            }
        });
        jPanel16.add(txt_Inicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 420, 88, -1));

        jLabel20.setText("Diferencia");
        jPanel16.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, -1, -1));

        txt_diferencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_diferenciaKeyReleased(evt);
            }
        });
        jPanel16.add(txt_diferencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 450, 92, -1));

        jLabel7.setText("Total");
        jPanel16.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 390, -1, -1));
        jPanel16.add(txtTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 390, 100, -1));

        jLabel12.setText("Frecuencia de Pago");
        jPanel16.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 430, -1, -1));
        jPanel16.add(txtFrecuencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 430, 100, -1));

        jLabel8.setText("Tasa de Interes");
        jPanel16.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 470, -1, -1));
        jPanel16.add(txtInteres, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 470, 100, -1));

        jLabel10.setText("%");
        jPanel16.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 470, -1, -1));

        jLabel21.setText("Proxima Fecha de Pago");
        jPanel16.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 420, -1, -1));

        dateFechaPagoCred.setDateFormatString("yyyy-MM-dd");
        jPanel16.add(dateFechaPagoCred, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 440, 150, -1));

        btnRegistrarCredito.setText("Registrar Credito");
        btnRegistrarCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarCreditoActionPerformed(evt);
            }
        });
        jPanel16.add(btnRegistrarCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 470, -1, -1));

        jLabel11.setText("Numero de cuotas");
        jPanel16.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 390, -1, 30));
        jPanel16.add(txtNumeroCuotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 390, 100, -1));

        jLabel18.setText("Valor de cuota");
        jPanel16.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 420, -1, -1));
        jPanel16.add(txtValorCuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 420, 100, -1));

        txtTotalxP.setToolTipText("");
        jPanel16.add(txtTotalxP, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 130, 80, 30));

        jLabel5.setText("Total");
        jPanel16.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 140, -1, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel16.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 120, -1, -1));

        btnMostrarStock.setFont(new java.awt.Font("Segoe UI", 1, 8)); // NOI18N
        btnMostrarStock.setText("Mostrar Stock");
        btnMostrarStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarStockActionPerformed(evt);
            }
        });
        jPanel16.add(btnMostrarStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, -1));
        jPanel16.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 390, 70, -1));

        jLabel3.setText("SubTotal");
        jPanel16.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 390, -1, -1));

        getContentPane().add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1080, 510));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxProveedorItemStateChanged
        MostrarCodigoProveedor(cbxProveedor, txtIdProveedor);
    }//GEN-LAST:event_cbxProveedorItemStateChanged

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cbxMarcaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxMarcaItemStateChanged
        MostrarCodigoMarca(cbxMarca, txtIdMarca);
    }//GEN-LAST:event_cbxMarcaItemStateChanged

    private void cbxProductoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxProductoItemStateChanged
        MostrarCodigoProducto(cbxProducto, txtIdProducto);
    }//GEN-LAST:event_cbxProductoItemStateChanged

    private void cbxPagarConItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxPagarConItemStateChanged
        MostrarCodigoFormaDePago(cbxPagarCon, txtIdPagarCon);
    }//GEN-LAST:event_cbxPagarConItemStateChanged

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (tbCompraProducto.getRowCount() > 0) {
            if (!txtTaxes.getText().trim().isEmpty() && !JLabelTotalCompra.getText().trim().isEmpty()) {
                RegistrarCompra(); // Registrar la compra y obtener el ID generado
                RegistrarDetalleCompra(); // Registrar los detalles usando el ID generado
                ActualizarStock();
                LimpiarTablaCompra(); // Para limpiar los campos de la Tabla
                txtSubTotal.setText("");
                txtTaxes.setText("");
                JLabelTotalCompra.setText("");
                txtNumeroRecibo.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Debes llenar casilla Taxes y dar Enter");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay Datos en la Compra");
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tbCompraProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCompraProductoMouseClicked
        // Obtener el índice de la fila seleccionada
        int selectedRow = tbCompraProducto.getSelectedRow();
        if (selectedRow != -1) {
            // Obtener los datos de la fila seleccionada
            Object descripcion = tbCompraProducto.getValueAt(selectedRow, 0);
            Object cantidad = tbCompraProducto.getValueAt(selectedRow, 1);
            Object subTotal = tbCompraProducto.getValueAt(selectedRow, 2);
            Object total = tbCompraProducto.getValueAt(selectedRow, 3);

            // Mostrar los datos de la fila seleccionada en la consola
            System.out.println("Fila seleccionada: " + selectedRow);
            System.out.println("Descripción: " + descripcion);
            System.out.println("Cantidad: " + cantidad);
            System.out.println("Subtotal: " + subTotal);
            System.out.println("Total: " + total);

            // Aquí puedes actualizar cualquier campo de texto o componente con los datos de la fila seleccionada
            cbxProducto.setSelectedItem(descripcion.toString());
            txtCant.setText(cantidad.toString());
            txtPrecioUnitario.setText(subTotal.toString());
            txtTotalxP.setText(total.toString());
        } else {
            System.out.println("No se ha seleccionado ninguna fila.");
        }

    }//GEN-LAST:event_tbCompraProductoMouseClicked

    private void cbxTipoProductoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoProductoItemStateChanged
        MostrarCodigoTipo(cbxTipoProducto, txtIdTipoPro);
    }//GEN-LAST:event_cbxTipoProductoItemStateChanged

    private void txt_diferenciaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_diferenciaKeyReleased

    }//GEN-LAST:event_txt_diferenciaKeyReleased

    private void btnRegistrarCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarCreditoActionPerformed
        if (tbCompraProducto.getRowCount() > 0) {
            if (!txtTaxes.getText().trim().isEmpty() && !JLabelTotalCompra.getText().trim().isEmpty()) {
                RegistrarCompra(); // Registrar la compra y obtener el ID generado
                RegistrarDetalleCompra(); // Registrar los detalles usando el ID generado
                registrarPagosPendientes();
                ActualizarStock();
                LimpiarTablaCompra(); // Para limpiar los campos de la Tabla
                LimpiartxtCred();
                txtSubTotal.setText("");
                txtTaxes.setText("");
                JLabelTotalCompra.setText("");
                txtNumeroRecibo.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Debes llenar casilla Taxes y dar Enter");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay Datos en la Compra");
        }

    }//GEN-LAST:event_btnRegistrarCreditoActionPerformed

    private void txtPrecioUnitarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioUnitarioKeyPressed


    }//GEN-LAST:event_txtPrecioUnitarioKeyPressed

    private void txtCantKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!txtCant.getText().isEmpty()) {
                if (!txtStock.getText().isEmpty() && !txtPrecioUnitario.getText().isEmpty()) {
                    try {
                        int cant = Integer.parseInt(txtCant.getText());
                        double precioUnit = Double.parseDouble(txtPrecioUnitario.getText());
                        double total = cant * precioUnit;
                        String Tipo = (String) cbxTipoProducto.getSelectedItem();
                        String Marca = (String) cbxMarca.getSelectedItem();

                        txtTotalxP.setText(String.valueOf(total));

                        item = item + 1;
                        DefaultTableModel model = (DefaultTableModel) tbCompraProducto.getModel();

                        // Verifica si el producto ya está en la tabla
                        boolean productoExistente = false;
                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (model.getValueAt(i, 0).equals(cbxProducto.getSelectedItem().toString())) {
                                JOptionPane.showMessageDialog(null, "El producto ya está registrado");
                                productoExistente = true;
                                break;
                            }
                        }

                        if (!productoExistente) {
                            Object selectedItem = cbxProducto.getSelectedItem();
                            String descripcion = selectedItem.toString();

                            // Añadir los valores a una nueva fila
                            Object[] fila = new Object[7];
                            fila[0] = descripcion; // Descripción del producto
                            fila[1] = Tipo;
                            fila[2] = Marca;
                            fila[3] = cant; // Cantidad
                            fila[4] = precioUnit; // Precio Unitario
                            fila[5] = total; // Total
                            fila[6] = item; // Item

                            model.addRow(fila);
                            model.fireTableDataChanged(); // Asegurarse de que la tabla se actualiza

                            TotalPagar();
                            LimpiarCompra();
                            cbxProducto.requestFocus();
                            txtStock.setText("0");

//                            System.out.println("Fila añadida a la tabla"); // Debug line
//                            System.out.println("Filas en el modelo (después de añadir): " + model.getRowCount()); // Debug line
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error en el formato de los datos: " + e.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Stock o SubTotal vacío");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad");
            }
        }
    }//GEN-LAST:event_txtCantKeyPressed

    //eliminar Producto ingresado por error
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminarFila();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void cbxProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxProductoKeyPressed

    }//GEN-LAST:event_cbxProductoKeyPressed

    private void cbxProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxProductoMouseClicked

    }//GEN-LAST:event_cbxProductoMouseClicked

    private void btnMostrarStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarStockActionPerformed
        String selectedProduct = (String) cbxProducto.getSelectedItem();
        if (selectedProduct != null) {
            mostrarStock(selectedProduct);
//                System.out.println("Producto: " + selectedProduct);
        } else {
            System.out.println("error"
            );
        }
    }//GEN-LAST:event_btnMostrarStockActionPerformed

    private void txtTaxesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxesKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!txtTaxes.getText().isEmpty()) {
                try {
                    double tax = Double.parseDouble(txtTaxes.getText());
                    // Formatear el valor tax a dos decimales
                    DecimalFormat df = new DecimalFormat("#.00");
                    String formattedTax = df.format(tax);

                    // Supongo que Totalpagar es una variable que ya tienes definida
                    double totalPagarConTax = Totalpagar + tax;

                    // Formatear el total a dos decimales
                    String formattedTotal = df.format(totalPagarConTax);

                    // Establecer el texto en el JLabel
                    JLabelTotalCompra.setText(formattedTotal);

                    // Para depuración
//                System.out.println("Tax: " + formattedTax);
//                System.out.println("Total a pagar con Tax: " + formattedTotal);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese los taxes");
            }
        }
    }//GEN-LAST:event_txtTaxesKeyPressed

    private void txt_InicialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_InicialKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calcularDiferencia();
        }
    }//GEN-LAST:event_txt_InicialKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel JLabelTotalCompra;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnMostrarStock;
    private javax.swing.JButton btnRegistrarCredito;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JComboBox<Object> cbxMarca;
    private javax.swing.JComboBox<String> cbxPagarCon;
    private javax.swing.JComboBox<String> cbxProducto;
    public javax.swing.JComboBox<Object> cbxProveedor;
    private javax.swing.JComboBox<String> cbxTipoProducto;
    private com.toedter.calendar.JDateChooser dateFecha;
    private com.toedter.calendar.JDateChooser dateFechaPagoCred;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    public javax.swing.JPanel jPanel16;
    private javax.swing.JScrollPane jScrollPane12;
    public javax.swing.JTable tbCompraProducto;
    public javax.swing.JTextField txtCant;
    private javax.swing.JTextField txtFrecuencia;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdMarca;
    private javax.swing.JTextField txtIdPagarCon;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdTipoPro;
    private javax.swing.JTextField txtInteres;
    private javax.swing.JTextField txtNumeroCuotas;
    public javax.swing.JTextField txtNumeroRecibo;
    public javax.swing.JTextField txtPrecioUnitario;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtSubTotal;
    public javax.swing.JTextField txtTaxes;
    private javax.swing.JTextField txtTotal1;
    private javax.swing.JTextField txtTotalxP;
    private javax.swing.JTextField txtValorCuota;
    private javax.swing.JTextField txt_Inicial;
    private javax.swing.JTextField txt_diferencia;
    // End of variables declaration//GEN-END:variables

    // Para llenar el ComboBox de los Proveedores
    public void MostrarProveedor(JComboBox cbxProveedorNC) {

        String sql = "";
        sql = "select * from suplier";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxProveedorNC.removeAllItems();

            while (rs.next()) {

                cbxProveedorNC.addItem(rs.getString("nameSuplier"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoProveedor(JComboBox cbxProveedorNC, JTextField idSuplier) {

        String consuta = "select suplier.idSuplier from suplier where suplier.nameSuplier=?";

        try {
            CallableStatement cs = con.getConexion().prepareCall(consuta);
            cs.setString(1, cbxProveedor.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                idSuplier.setText(rs.getString("idSuplier"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    // Para llenar el comboBox de las Marcas
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

    // Para llenar el ComboBox de los Productos
    public void MostrarProducto(JComboBox cbxProducto) {

        String sql = "";
        sql = "select nameProduct from product";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxProducto.removeAllItems();

            while (rs.next()) {

                cbxProducto.addItem(rs.getString("nameProduct"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Producto " + e.toString());
        }
    }

    public void MostrarCodigoProducto(JComboBox cbxProducto, JTextField idProducto) {
        String consuta = "select product.idProduct from product where product.nameProduct=?";
        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxProducto.getSelectedIndex() == -1) {

                return;
            }
            CallableStatement cs = con.getConexion().prepareCall(consuta);
            Object selectedValue = cbxProducto.getSelectedItem();
            if (selectedValue != null) {
                String valorSeleccionado = selectedValue.toString();
                cs.setString(1, valorSeleccionado);
                cs.execute();
                ResultSet rs = cs.executeQuery();
                if (rs.next()) {
                    idProducto.setText(rs.getString("idProduct"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    //Para llenar el ComboBox Formas de Pago
    public void MostrarCodigoFormaDePago(JComboBox cbxPagarCon, JTextField idPagarCon) {

        String consuta = "select formadepago.id_formadepago from formadepago where formadepago.nombre=?";

        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxPagarCon.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ninguna forme de pago.");
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

    //Para llenar el ComboBox Tipos de Productos
    public void MostrarTipo(JComboBox cbxTipoProducto) {

        String sql = "";
        sql = "select * from tipodeproductomateriales";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxTipoProducto.removeAllItems();

            while (rs.next()) {

                cbxTipoProducto.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoTipo(JComboBox cbxTipoProducto, JTextField idTipoProducto) {

        String consuta = "select tipodeproductomateriales.idProMat from tipodeproductomateriales where tipodeproductomateriales.nombre=?";

        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxTipoProducto.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
                return;
            }

            CallableStatement cs = con.getConexion().prepareCall(consuta);

            Object selectedValue = cbxTipoProducto.getSelectedItem();
            if (selectedValue != null) {
                String valorSeleccionado = selectedValue.toString();
                cs.setString(1, valorSeleccionado);

                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    idTipoProducto.setText(rs.getString("idProMat"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    // Método para hallar el id_Compra que se está ejecutando en ese momento
    public int IdCompra() {
        int id = 0;
        String sql = "SELECT MAX(id_CompraProMat) FROM compraproductosmateriales";
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

//Registrar los Pagos pendientes Por Credito o Prestamos
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
            double Inicial = Double.parseDouble(txt_Inicial.getText());
            int numeroCuotas = Integer.parseInt(txtNumeroCuotas.getText());
            double valorCuota = Double.parseDouble(txtValorCuota.getText());
            double total = Double.parseDouble(JLabelTotalCompra.getText());
            double diferencia = total;

            // Obtener la fecha de inicio
            Date fechaInicio = dateFechaPagoCred.getDate();
            if (fechaInicio == null) {
                JOptionPane.showMessageDialog(null, "La fecha de inicio es nula. Por favor selecciona una fecha válida.");
                return; // Añadido return para evitar continuar si la fecha es nula
            }

            // Preparar la inserción de pagos en la base de datos
            String sql = "INSERT INTO creditoprod (id_compra, frecuencia, fechaPago, interes, Inicial, NumeroCuotas, cuota, Diferencia, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Pendiente')";
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
                stmt.setDouble(5, Inicial);
                stmt.setInt(6, i); // Número de la cuota actual
                stmt.setDouble(7, cuotaActual);
                stmt.setDouble(8, diferencia); // Diferencia actualizada

                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Crédito registrado correctamente con todas las fechas de pago.");

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

//Limpiar las cajas de texto
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
        cbxTipoProducto.setSelectedItem("");

    }

    private void LimpiarCampos() {
        txt_diferencia.setText("");
        txtFrecuencia.setText("");
        txt_Inicial.setText("");
        txtInteres.setText("");
        txtNumeroCuotas.setText("");
        txtValorCuota.setText("");
        dateFechaPagoCred.setDateFormatString("");
        txtTotalxP.setText("");
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
            double total = Double.parseDouble(JLabelTotalCompra.getText().trim());
            double inicial = Double.parseDouble(txt_Inicial.getText().trim());
            double diferencia = total - inicial;
            txt_diferencia.setText(String.valueOf(diferencia));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos.");
        }
    }

    // Método para calcular el total a pagar
    private void TotalPagar() {
        Totalpagar = 0.00;

        int numFila = tbCompraProducto.getRowCount();
        for (int i = 0; i < numFila; i++) {
            double cal = Double.parseDouble(String.valueOf(tbCompraProducto.getModel().getValueAt(i, 5)));

            Totalpagar = Totalpagar + cal;
        }
        txtSubTotal.setText(String.format("%.2f", Totalpagar));
    }

    // Método para limpiar los campos después de añadir un producto
    private void LimpiarCompra() {
        txtCant.setText("");
        txtStock.setText("");
        txtPrecioUnitario.setText("");
        txtTotalxP.setText("");
        txtInteres.setText("");
    }

    // Método para refrescar la JTable
    private void refreshTable(JTable table) {
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
    }

    // Método para mostrar el stock de un producto
    private void mostrarStock(String producto) {
        Conectar con = new Conectar();
        Connection connect = con.getConexion();
        if (connect != null) {
            String sql = "SELECT stock FROM product WHERE nameProduct=?";
            try {
                PreparedStatement ps = connect.prepareStatement(sql);
                ps.setString(1, producto);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int stock = rs.getInt("stock");
                    txtStock.setText(String.valueOf(stock));
                } else {
                    txtStock.setText("0");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al mostrar stock: " + e.toString());
            } finally {
                try {
                    connect.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Error: Conexión a la base de datos fallida.");
        }
    }

// Método para buscar un producto por nombre
    public Productos BuscarPro(String nombre) {
        Productos producto = new Productos();
        String sql = "SELECT * FROM product WHERE nameProduct = ?";
        try (Connection connect = con.getConexion(); PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto.setIdProduct(rs.getInt("idProduct"));  // Ajusta el nombre de la columna según tu base de datos
                    producto.setNameProduct(rs.getString("nameProduct"));  // Ajusta el nombre de la columna según tu base de datos
                    producto.setStock(rs.getInt("stock"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.toString());
        }
        return producto;
    }

// Método para actualizar el stock de un producto
    public boolean ActualizarStock(int cant, String nombre) {
        String sql = "UPDATE product SET stock = ? WHERE nameProduct = ?";  // Ajusta el nombre de la tabla y columna según tu base de datos
        try (Connection connect = con.getConexion(); PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setInt(1, cant);
            ps.setString(2, nombre);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el stock: " + e.toString());
            return false;
        }
    }

// Método para actualizar el stock de todos los productos en la tabla
    private void ActualizarStock() {
        for (int i = 0; i < tbCompraProducto.getRowCount(); i++) {
            try {
                String nombre = tbCompraProducto.getValueAt(i, 0).toString();
                int cant = Integer.parseInt(tbCompraProducto.getValueAt(i, 3).toString());
                Productos pro = BuscarPro(nombre);

                if (pro != null) {
                    int StockActual = pro.getStock() + cant;  // Sumamos el stock actual con la cantidad ingresada
                    if (ActualizarStock(StockActual, nombre)) {
                        System.out.println("Stock actualizado para " + nombre);
                    } else {
                        System.out.println("Error al actualizar el stock para " + nombre);
                    }
                } else {
                    System.out.println("Producto no encontrado: " + nombre);
                }
            } catch (NumberFormatException e) {
                System.out.println("Cantidad inválida en la fila " + (i + 1) + ": " + e.toString());
            } catch (Exception e) {
                System.out.println("Error al actualizar el stock para la fila " + (i + 1) + ": " + e.getMessage());
            }
        }
    }
}
