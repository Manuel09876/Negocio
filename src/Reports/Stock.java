
package Reports;

import conectar.Conectar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


public class Stock extends javax.swing.JInternalFrame {
    
    DefaultTableModel model;
    int idProduct;
    String nameProduct;
    double stock;
    String estado;

    public Stock(int idProduct, String nameProduct, double stock, String estado) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.stock = stock;
        this.estado = estado;
    }

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

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    Conectar con = new Conectar();
    Connection connect = con.getConexion();
    
    void CargarDatosTable(String Valores) {

        try {

            String[] titulosTabla = {"Id", "Nombre", "Stock", "estado"}; //Titulos de la Tabla
            String[] RegistroBD = new String[4];                                   //Registros de la Basede Datos

            model = new DefaultTableModel(null, titulosTabla); //Le pasamos los titulos a la tabla

            String ConsultaSQL = "select * from product";

            Statement st = connect.createStatement();
            ResultSet result = st.executeQuery(ConsultaSQL);

            while (result.next()) {
                RegistroBD[0] = result.getString("idProduct");
                RegistroBD[1] = result.getString("nameProduct");
                RegistroBD[2] = result.getString("stock");
                RegistroBD[3] = result.getString("estado");
                

                model.addRow(RegistroBD);
            }

            TableCompras.setModel(model);
            TableCompras.getColumnModel().getColumn(0).setPreferredWidth(50);
            TableCompras.getColumnModel().getColumn(1).setPreferredWidth(150);
            TableCompras.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableCompras.getColumnModel().getColumn(3).setPreferredWidth(150);
            

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

  
    public Stock() {
        initComponents();
        AutoCompleteDecorator.decorate(cbxProducto);
        MostrarProducto(cbxProducto);
        CargarDatosTable("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        TableCompras = new javax.swing.JTable();
        btnHistorialCompra = new javax.swing.JButton();
        txtIdProducto = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cbxProducto = new javax.swing.JComboBox<>();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Stock");

        jPanel12.setBackground(new java.awt.Color(255, 153, 153));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Producto", "Stock", "Estado"
            }
        ));
        TableCompras.setRowHeight(23);
        jScrollPane14.setViewportView(TableCompras);

        jPanel12.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 990, 420));

        btnHistorialCompra.setText("Generar Reporte");
        jPanel12.add(btnHistorialCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, 120, 50));
        jPanel12.add(txtIdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 70, 40));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 20, -1, -1));

        jLabel1.setText("Producto");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, -1, -1));

        cbxProducto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxProductoItemStateChanged(evt);
            }
        });
        cbxProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxProductoActionPerformed(evt);
            }
        });
        jPanel12.add(cbxProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 240, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1033, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 1021, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cbxProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxProductoActionPerformed

    private void cbxProductoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxProductoItemStateChanged
        MostrarCodigoProducto(cbxProducto, txtIdProducto);
    }//GEN-LAST:event_cbxProductoItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable TableCompras;
    public javax.swing.JButton btnHistorialCompra;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxProducto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane14;
    public javax.swing.JTextField txtIdProducto;
    // End of variables declaration//GEN-END:variables

    
public void MostrarProducto(JComboBox cbxProducto) {

        String sql = "";
        sql = "select * from product";
        Statement st;

        try {

            st = con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxProducto.removeAllItems();

            while (rs.next()) {

                cbxProducto.addItem(rs.getString("nameProduct"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar en Combo " + e.toString());
        }
    }

    public void MostrarCodigoProducto(JComboBox cbxEmpresa, JTextField idBusiness) {

        String consuta = "select product.idProduct from product where product.nameProduct=?";

        try {
            CallableStatement cs = con.getConexion().prepareCall(consuta);
            cs.setString(1, cbxEmpresa.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                idBusiness.setText(rs.getString("idProduct"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

}
