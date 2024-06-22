package Reports;

import conectar.Conectar;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class DeudasPorPagar extends javax.swing.JInternalFrame {

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class; // Columna de selección
            }
            return String.class; // Otras columnas
        }
    };
    
    
    //Variable
    int id, id_compra, frecuencia;
    Date fechaPago;
    double interes;
    int NumeroCuotas;
    double cuota;
    double Diferencia;
    String estado;

    public DeudasPorPagar(int id, int id_compra, int frecuencia, Date fechaPago, double interes, int NumeroCuotas, double cuota, double Diferencia, String estado) {
        this.id = id;
        this.id_compra = id_compra;
        this.frecuencia = frecuencia;
        this.fechaPago = fechaPago;
        this.interes = interes;
        this.NumeroCuotas = NumeroCuotas;
        this.cuota = cuota;
        this.Diferencia = Diferencia;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public int getNumeroCuotas() {
        return NumeroCuotas;
    }

    public void setNumeroCuotas(int NumeroCuotas) {
        this.NumeroCuotas = NumeroCuotas;
    }

    public double getCuota() {
        return cuota;
    }

    public void setCuota(double cuota) {
        this.cuota = cuota;
    }

    public double getDiferencia() {
        return Diferencia;
    }

    public void setDiferencia(double Diferencia) {
        this.Diferencia = Diferencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public DeudasPorPagar() {
        initComponents();
        MostrarTabla("");
        

    }
    
    // Método que agrega el CheckBox
    public void addCheckBox(int column, JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    
    public boolean IsSelected(int row, int column, JTable table) {
        return table.getValueAt(row, column) != null;
    }

    private void MostrarTabla(String Valores) {
    try {
        // Configurar las columnas del modelo de la tabla
        String[] titulosTabla = {"Seleccion", "id", "Descripcion", "Fecha de Pago", "Cuota Numero", "Monto a Pagar", "Deuda", "Estado"};
        modelo.setColumnIdentifiers(titulosTabla);

        // Configurar la tabla con el modelo
        tbDeudas.setModel(modelo);

        // Consulta SQL para obtener los datos
        String sql = """
                     SELECT c.id, e.nombre AS Descripcion, c.fechaPago AS 'Fecha de Pago', c.NumeroCuotas AS 'Cuota Numero', c.cuota AS 'Monto a Pagar', c.Diferencia AS Deuda, c.estado AS Estado 
                     FROM credito AS c
                     INNER JOIN equipos AS e ON c.id_compra=e.id_equipos""";

        // Conexión a la base de datos
        Conectar con = new Conectar();
        Connection connect = con.getConexion();
        Statement st = connect.createStatement();
        ResultSet result = st.executeQuery(sql);

        // Añadir filas al modelo de la tabla
        while (result.next()) {
            Object[] RegistroBD = new Object[8];
            RegistroBD[0] = Boolean.FALSE; // Inicializa la celda de selección con false
            RegistroBD[1] = result.getString("id");
            RegistroBD[2] = result.getString("Descripcion");
            RegistroBD[3] = result.getString("Fecha de Pago");
            RegistroBD[4] = result.getString("Cuota Numero");
            RegistroBD[5] = result.getString("Monto a Pagar");
            RegistroBD[6] = result.getString("Deuda");
            RegistroBD[7] = result.getString("Estado");
            modelo.addRow(RegistroBD);
        }

        // Añadir CheckBox a la primera columna
        addCheckBox(0, tbDeudas);

        // Ocultar la columna "id"
        TableColumn ci = tbDeudas.getColumn("id");
        ci.setMaxWidth(0);
        ci.setMinWidth(0);
        ci.setPreferredWidth(0);
        tbDeudas.doLayout();

    } catch (SQLException e) {
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tbDeudas = new javax.swing.JTable();
        txtBuscarCompra = new javax.swing.JTextField();
        btnHistorialCompra = new javax.swing.JButton();
        txtIdCompra = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        cbSeleccionaTodo = new javax.swing.JCheckBox();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Deudas por Pagar");

        jPanel12.setBackground(new java.awt.Color(204, 204, 204));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbDeudas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbDeudas.setRowHeight(23);
        jScrollPane14.setViewportView(tbDeudas);

        jPanel12.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1060, 420));
        jPanel12.add(txtBuscarCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 240, 30));

        btnHistorialCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        btnHistorialCompra.setText("Buscar");
        jPanel12.add(btnHistorialCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 100, 40));
        jPanel12.add(txtIdCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 70, 40));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, -1, -1));

        cbSeleccionaTodo.setText("Selecciona Todo");
        cbSeleccionaTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSeleccionaTodoActionPerformed(evt);
            }
        });
        jPanel12.add(cbSeleccionaTodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 120, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1190, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 1190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 589, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cbSeleccionaTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSeleccionaTodoActionPerformed
                                                       
        if (cbSeleccionaTodo.isSelected()) {
            cbSeleccionaTodo.setText("Deseleccionar Todo");
            for (int i = 0; i < tbDeudas.getRowCount(); i++) {
                tbDeudas.setValueAt(true, i, 0);
            }
        } else {
            cbSeleccionaTodo.setText("Seleccionar Todo");
            for (int i = 0; i < tbDeudas.getRowCount(); i++) {
                tbDeudas.setValueAt(false, i, 0);
            }
        }
    }//GEN-LAST:event_cbSeleccionaTodoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnHistorialCompra;
    private javax.swing.JButton btnSalir;
    private javax.swing.JCheckBox cbSeleccionaTodo;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane14;
    public javax.swing.JTable tbDeudas;
    public javax.swing.JTextField txtBuscarCompra;
    public javax.swing.JTextField txtIdCompra;
    // End of variables declaration//GEN-END:variables

}
