package Administration;

import conectar.Conectar;
import java.awt.HeadlessException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Productos extends javax.swing.JInternalFrame {

    String filtro;

    //Variable para la tabla
    DefaultTableModel model;
    int idProduct;
    String nameProduct;
    int categoria;
    double presentation;
    int units;
    int location;
    double stock, stock2;
    String estado;

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public double getPresentation() {
        return presentation;
    }

    public void setPresentation(double presentation) {
        this.presentation = presentation;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getStock2() {
        return stock2;
    }

    public void setStock2(double stock2) {
        this.stock2 = stock2;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Productos() {
        initComponents();

        txtIdProducto.setEnabled(false);
        AutoCompleteDecorator.decorate(cbxUnidades);
        MostrarUnidades(cbxUnidades);
    
        CargarDatosTable("");
        txtIdProducto.setEnabled(false);
        txtName.requestFocus();

    }

    //Constructores de Metodos que vamos a necesitar
    //Realización de los Métodos
    public void BloquearCampos() {
        txtName.setEnabled(false);
        txtPresentation.setEnabled(false);
        cbxUnidades.setEnabled(false);
        txtStock.setEnabled(false);
        txtOr.setEnabled(false);

        btnNew.setEnabled(true);
        btnAdd.setEnabled(false);
        btnUpdate.setEnabled(false);
        btnCancel.setEnabled(false);

    }

    void LimpiarCajasTexto() {

        txtName.setText("");
        txtPresentation.setText("");

        txtStock.setText("");
        txtOr.setText("");

    }

    void DesbloquearCampos() {

        txtName.setEnabled(true);
        txtPresentation.setEnabled(true);
        txtStock.setEnabled(true);
        txtOr.setEnabled(true);
        btnNew.setEnabled(false);
        btnAdd.setEnabled(true);
        btnUpdate.setEnabled(true);
        btnCancel.setEnabled(true);

    }

    //Es lo mismo que mostrar Tabla Clientes
    void CargarDatosTable(String Valores) {

        try {

            String[] titulosTabla = {"Id", "Nombre", "Presentacion", "Unidades", "Stock", "Stock2","estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[7];                                   //Registros de la Basede Datos

            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla

            String ConsultaSQL = "SELECT product.idProduct, product.nameProduct, product.presentation, unidades.nombre AS Unidades, product.stock, product.stock2, product.estado \n"
                    + "FROM product \n"
                    + "INNER JOIN unidades ON product.id_units=unidades.id_unidades";

            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {
                RegistroBD[0] = result.getString(1);
                RegistroBD[1] = result.getString(2);
                RegistroBD[2] = result.getString(3);
                RegistroBD[3] = result.getString(4);
                RegistroBD[4] = result.getString(5);
                RegistroBD[5] = result.getString(6);
                RegistroBD[6] = result.getString(7);
                               
                model.addRow(RegistroBD);
            }

            tbProducts.setModel(model);
            tbProducts.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbProducts.getColumnModel().getColumn(1).setPreferredWidth(150);
            tbProducts.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbProducts.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbProducts.getColumnModel().getColumn(4).setPreferredWidth(50);
            tbProducts.getColumnModel().getColumn(5).setPreferredWidth(100);
            tbProducts.getColumnModel().getColumn(6).setPreferredWidth(150);
                        
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

    void Guardar() {

        // Variables
        String nameProduct;
        double presentation;
        int id_units;
        double stock, stock2;
        String sql = "";

        //Obtenemos la informacion de las cajas de texto
        nameProduct = txtName.getText();
        presentation = Double.parseDouble(txtPresentation.getText());
        id_units = Integer.parseInt(txtIdUnidades.getText());
        stock = Double.parseDouble(txtStock.getText());
        stock2 = Double.parseDouble(txtOr.getText());

        //Consulta para evitar duplicados
        String consulta = "SELECT * FROM product WHERE nameProduct = ?";

        //Consulta sql para insertar los datos (nombres como en la base de datos)
        sql = "INSERT INTO product (nameProduct,presentation,id_units,stock,stock2)values(?,?,?,?,?)";

        //Para almacenar los datos empleo un try cash
        try {

            //prepara la coneccion para enviar al sql (Evita ataques al sql)
            PreparedStatement pst = connect.prepareStatement(sql);

            pst.setString(1, nameProduct);
            pst.setDouble(2, presentation);
            pst.setInt(3, id_units);
            pst.setDouble(4,stock);
            pst.setDouble(5, stock2);
            

            //Declara otra variable para validar los registros
            int n = pst.executeUpdate();

            //si existe un registro en la BD el registro se guardo con exito
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "El registro se guardo exitosamente");

            }
            CargarDatosTable("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar " + ex.toString());

        }

    }

    public void Eliminar(JTextField codigo) {

        setIdProduct(Integer.parseInt(codigo.getText()));

        String consulta = "DELETE from product where idProduct=?";

        try {

            CallableStatement cs = con.getConexion().prepareCall(consulta);
            cs.setInt(1, getIdProduct());
            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Elimino");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "No se Elimino, error: " + e.toString());

        }

    }

    public void SeleccionarProducto(JTable TablaProducto, JTextField IdProducto, JTextField Name, JTextField Presentacion, JComboBox Unidades, JTextField Stock, JTextField Or) {

        try {

            int fila = TablaProducto.getSelectedRow();

            if (fila >= 0) {

                IdProducto.setText(TablaProducto.getValueAt(fila, 0).toString());
                Name.setText(TablaProducto.getValueAt(fila, 1).toString());
                Presentacion.setText(TablaProducto.getValueAt(fila, 2).toString());
                Unidades.setSelectedItem(TablaProducto.getValueAt(fila, 3).toString());
                Stock.setText(TablaProducto.getValueAt(fila, 4).toString());
                Or.setText(TablaProducto.getValueAt(fila, 5).toString());

            } else {
                JOptionPane.showMessageDialog(null, "Fila seleccionada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Seleccion, Error: " + e.toString());
        }

    }

    public void ModificarProducto(JTextField IdProducto, JTextField Name, JTextField Presentation, JTextField IdUnits, 
            JTextField Stock, JTextField Or) {

        try {
            
            setIdProduct(Integer.parseInt(IdProducto.getText()));
            setNameProduct(Name.getText());
            setPresentation(Double.parseDouble(Presentation.getText()));
            setUnits(Integer.parseInt(IdUnits.getText())); // ¿Debería ser IdUnits?
            setStock(Double.parseDouble(Stock.getText()));
            setStock2(Double.parseDouble(Or.getText()));

            // Consulta de actualización
            String consulta = "UPDATE product SET nameProduct=?, presentation=?, id_units=?, stock=?, stock2=? WHERE idProduct=?";

            CallableStatement cs = con.getConexion().prepareCall(consulta);

            cs.setString(1, getNameProduct());
            cs.setDouble(2, getPresentation());
            cs.setInt(3, getUnits());
            cs.setDouble(4, getStock());
            cs.setDouble(5, getStock2());
            cs.setInt(6, getIdProduct());

            cs.executeUpdate();

            JOptionPane.showMessageDialog(null, "Modificación Exitosa");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Error al convertir un valor a número. Asegúrate de ingresar números válidos en los campos numéricos.", "Error de conversión", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al ejecutar la actualización en la base de datos: " + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error durante la modificación: " + ex.getMessage(), "Error inesperado", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Productos BuscarProductos(java.awt.event.KeyEvent evt) {
        String[] titulosTabla = {"Id", "Nombre", "Presentacion", "Unidades", "Stock", "Stock2"}; //Titulos de la Tabla
        String[] RegistroBD = new String[8];                                   //Registros de la Basede Datos

        Productos productos = new Productos();
        String ConsultaSQL = "SELECT product.idProduct, product.nameProduct, product.presentation, product.units, product.stock, product.stock2, product.estado \n"
                + "FROM product \n"
                + "WHERE nameProduct LIKE '%" + txtBuscarProductos.getText() + "%'";
        model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla
        try {
            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);
            connect = con.getConexion();
            while (result.next()) {
                RegistroBD[0] = result.getString(1);
                RegistroBD[1] = result.getString(2);
                RegistroBD[2] = result.getString(3);
                RegistroBD[3] = result.getString(4);
                RegistroBD[4] = result.getString(5);
                RegistroBD[5] = result.getString(6);
                model.addRow(RegistroBD);
            }
            tbProducts.setModel(model);
            tbProducts.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbProducts.getColumnModel().getColumn(1).setPreferredWidth(150);
            tbProducts.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbProducts.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbProducts.getColumnModel().getColumn(4).setPreferredWidth(50);
            tbProducts.getColumnModel().getColumn(5).setPreferredWidth(100);
            
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return productos;
    }
    
    public boolean accion(String estado, int idTipoDeUsuario) {
        String sql = "UPDATE product SET estado = ? WHERE idProduct = ?";
        try {
            connect = con.getConexion();
            PreparedStatement ps;
            ps = connect.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, idTipoDeUsuario);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        txtOr = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtPresentation = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtIdProducto = new javax.swing.JTextField();
        cbxUnidades = new javax.swing.JComboBox<>();
        txtIdUnidades = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel11 = new javax.swing.JLabel();
        txtBuscarProductos = new javax.swing.JTextField();
        btnSerch = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        btnInactivar = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 255, 0));
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Products");
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel6.setText("Unidades");

        jLabel8.setText("Stock");

        jLabel10.setText("Or");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel1.setText("Nombre");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel5.setText("Presentación");

        jLabel12.setText("id");

        cbxUnidades.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxUnidadesItemStateChanged(evt);
            }
        });

        btnNew.setBackground(new java.awt.Color(0, 153, 0));
        btnNew.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/new product.png"))); // NOI18N
        btnNew.setText("Nuevo");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(0, 153, 0));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnAdd.setText("Guardar");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(0, 153, 0));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnUpdate.setText("Modificar");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(0, 153, 0));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(0, 153, 0));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnDelete.setText("Eliminar");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnExit.setBackground(new java.awt.Color(0, 153, 0));
        btnExit.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnExit.setText("Salir");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel12))
                        .addGap(93, 93, 93)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(73, 73, 73)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPresentation, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtIdUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addGap(100, 100, 100)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtStock)
                            .addComponent(txtOr, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnNew)
                            .addComponent(btnCancel))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnDelete)
                                .addGap(18, 18, 18)
                                .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnAdd)
                                .addGap(18, 18, 18)
                                .addComponent(btnUpdate)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPresentation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtOr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 0, -1, -1));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        tbProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProductsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbProducts);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 59, 715, 280));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel11.setText("Descripción");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 18, -1, -1));

        txtBuscarProductos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarProductosKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarProductosKeyTyped(evt);
            }
        });
        jPanel2.add(txtBuscarProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 13, 190, -1));

        btnSerch.setBackground(new java.awt.Color(0, 102, 255));
        btnSerch.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnSerch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        btnSerch.setText("Buscar");
        btnSerch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchActionPerformed(evt);
            }
        });
        jPanel2.add(btnSerch, new org.netbeans.lib.awtextra.AbsoluteConstraints(288, 11, -1, 30));

        btnActivar.setText("Activar");
        jPanel2.add(btnActivar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 360, -1, -1));

        btnInactivar.setText("Inactivar");
        btnInactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInactivarActionPerformed(evt);
            }
        });
        jPanel2.add(btnInactivar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 360, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(417, 6, -1, 404));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed

        LimpiarCajasTexto();
        txtName.requestFocus();


    }//GEN-LAST:event_btnNewActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        ModificarProducto(txtIdProducto, txtName, txtPresentation, txtIdUnidades, txtStock, txtOr);
        CargarDatosTable("");

        txtIdProducto.setText("");
        txtName.setText("");
        txtPresentation.setText("");
        txtStock.setText("");
        txtOr.setText("");


    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed

        txtIdProducto.setText("");
        txtName.setText("");
        txtPresentation.setText("");
        txtStock.setText("");
        txtOr.setText("");


    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed

        this.dispose();  //solo cierra la ventana actual

    }//GEN-LAST:event_btnExitActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        SeleccionarProducto(tbProducts, txtIdProducto, txtName, txtPresentation, cbxUnidades, txtStock, txtOr);
        Eliminar(txtIdProducto);
        CargarDatosTable("");
        
        txtIdProducto.setText("");
        txtName.setText("");
        txtPresentation.setText("");
        txtStock.setText("");
        txtOr.setText("");

        BloquearCampos();

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSerchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchActionPerformed
        txtBuscarProductos.setText("");
        CargarDatosTable("");

    }//GEN-LAST:event_btnSerchActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        Guardar();
        CargarDatosTable("");
        LimpiarCajasTexto();
        DesbloquearCampos();

    }//GEN-LAST:event_btnAddActionPerformed

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

        SeleccionarProducto(tbProducts, txtIdProducto, txtName, txtPresentation, cbxUnidades, txtStock, txtOr);

    }//GEN-LAST:event_formMouseClicked

    private void txtBuscarProductosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductosKeyTyped

    }//GEN-LAST:event_txtBuscarProductosKeyTyped

    private void tbProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProductsMouseClicked
        SeleccionarProducto(tbProducts, txtIdProducto, txtName, txtPresentation, cbxUnidades, txtStock, txtOr);
    }//GEN-LAST:event_tbProductsMouseClicked

    private void txtBuscarProductosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductosKeyPressed
        BuscarProductos(evt);
    }//GEN-LAST:event_txtBuscarProductosKeyPressed

    private void cbxUnidadesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxUnidadesItemStateChanged
        MostrarCodigoUnidades(cbxUnidades, txtIdUnidades);
    }//GEN-LAST:event_cbxUnidadesItemStateChanged

    private void btnInactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivarActionPerformed
        int fila = tbProducts.getSelectedRow();
        int id = Integer.parseInt(txtIdProducto.getText());
        if (accion("Inactivo", id)) {
            JOptionPane.showMessageDialog(null, "Inactivado");
            CargarDatosTable("");
        }else{
            JOptionPane.showMessageDialog(null, "Error al Inactivar");
        }
    }//GEN-LAST:event_btnInactivarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActivar;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnInactivar;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSerch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbxUnidades;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public static final javax.swing.JTable tbProducts = new javax.swing.JTable();
    private javax.swing.JTextField txtBuscarProductos;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtIdUnidades;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtOr;
    private javax.swing.JTextField txtPresentation;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables

    Conectar con = new Conectar();
    Connection connect = con.getConexion();


    public void MostrarUnidades(JComboBox cbxUnidades) {

        String sql = "";
        sql = "select * from unidades";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxUnidades.removeAllItems();

            while (rs.next()) {

                cbxUnidades.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoUnidades(JComboBox cbxUnidades, JTextField idUnidades) {

        String consuta = "select unidades.id_unidades from unidades where unidades.nombre=?";

        try {
            // Validar si hay un item seleccionado en el JComboBox
            if (cbxUnidades.getSelectedIndex() == -1) {
//            JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningún proveedor.");
                return;
            }

            CallableStatement cs = con.getConexion().prepareCall(consuta);

            Object selectedValue = cbxUnidades.getSelectedItem();
            if (selectedValue != null) {
                String valorSeleccionado = selectedValue.toString();
                cs.setString(1, valorSeleccionado);

                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    idUnidades.setText(rs.getString("id_unidades"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    
}
