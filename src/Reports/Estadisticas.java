package Reports;

import conectar.Conectar;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Estadisticas extends javax.swing.JInternalFrame {

    Conectar con = new Conectar();

    public Estadisticas() {
        initComponents();
    }

    public void BuscarDatos() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaInicio = dateFormat.format(dateInicio.getDate());
        String fechaFin = dateFormat.format(dateFin.getDate());

        try {
            String todas = "SELECT SUM(precio) FROM orderservice";
            String deuda = "SELECT SUM(precio) FROM orderservice WHERE eeCta = 'Deuda'";
            String canceladas = "SELECT SUM(precio) FROM orderservice WHERE eeCta = 'Cancelada'";
            String deudasPagar = "SELECT SUM(cuota) FROM credito" + "SELECT SUM(cuota) FROM creditopg" + "SELECT SUM(cuota) FROM creditoprod";
            String todasPorEmpresa = "SELECT SUM(precio) FROM orderservice WHERE id_empresa=3";

        } catch (Exception e) {
        }
    }

    private DefaultCategoryDataset crearDataset(String query, String label) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            Connection conn = con.getConexion();
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String fecha = rs.getString("fechaT"); // Reemplaza "fecha_column_name" con el nombre correcto de la columna
                double ingreso = rs.getDouble("totalIngreso");
                dataset.addValue(ingreso, label, fecha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    private TimeSeriesCollection crearData(String query, String label) {
        TimeSeries series = new TimeSeries(label);
        try {
            Connection conn = con.getConexion();
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Date fecha = rs.getDate("fechaT"); // Asegúrate de que 'fecha' es el nombre correcto de la columna en tu tabla
                double valor = rs.getDouble("totalEngreso"); // O "totalEgreso" según corresponda
                series.add(new Day(fecha), valor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private void generarGraficas() {
        String fechaInicio = new SimpleDateFormat("yyyy-MM-dd").format(dateInicio.getDate());
        String fechaFin = new SimpleDateFormat("yyyy-MM-dd").format(dateFin.getDate());

        String queryIngresos = "SELECT fechaT, SUM(precio) as totalIngreso FROM orderservice WHERE fechaT BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "' GROUP BY fechaT";
        String queryEgresos = "SELECT fechaT, SUM(precio) as totalEgreso FROM orderservice WHERE eeCta = 'Deuda' AND fechaT BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "' GROUP BY fechaT";

        TimeSeriesCollection datasetIngresos = crearData(queryIngresos, "Ingresos");
        TimeSeriesCollection datasetEgresos = crearData(queryEgresos, "Egresos");

        JFreeChart chartIngresos = ChartFactory.createTimeSeriesChart("Ingresos por Día", "Fecha", "Ingreso", datasetIngresos, true, true, false);
        JFreeChart chartEgresos = ChartFactory.createTimeSeriesChart("Egresos por Día", "Fecha", "Egreso", datasetEgresos, true, true, false);

        // Personalizar el eje X para mostrar solo el día
        XYPlot plotIngresos = (XYPlot) chartIngresos.getPlot();
        DateAxis axisIngresos = (DateAxis) plotIngresos.getDomainAxis();
        axisIngresos.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy"));

        XYPlot plotEgresos = (XYPlot) chartEgresos.getPlot();
        DateAxis axisEgresos = (DateAxis) plotEgresos.getDomainAxis();
        axisEgresos.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy"));

        ChartPanel chartPanelIngresos = new ChartPanel(chartIngresos);
        ChartPanel chartPanelEgresos = new ChartPanel(chartEgresos);

        JPanel panelGraficas = new JPanel();
        panelGraficas.setLayout(new BoxLayout(panelGraficas, BoxLayout.Y_AXIS));
        panelGraficas.add(chartPanelIngresos);
        panelGraficas.add(chartPanelEgresos);

        JFrame frame = new JFrame("Graficas de Ingresos y Egresos");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(panelGraficas);
        frame.pack();
        frame.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        txtEgreso = new javax.swing.JTextField();
        btnHistorialCompra = new javax.swing.JButton();
        txtIngreso = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDeudasPorCobrar = new javax.swing.JTextField();
        txtDeudasPorPagar = new javax.swing.JTextField();
        dateFin = new com.toedter.calendar.JDateChooser();
        dateInicio = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Estadisticas");

        jPanel12.setBackground(new java.awt.Color(255, 153, 0));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel12.add(txtEgreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 150, 30));

        btnHistorialCompra.setText("Generar Reporte");
        btnHistorialCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialCompraActionPerformed(evt);
            }
        });
        jPanel12.add(btnHistorialCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 20, 120, 50));
        jPanel12.add(txtIngreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 150, 30));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 20, -1, -1));

        jLabel1.setText("Ingreso");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        jLabel2.setText("Egreso");
        jPanel12.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jLabel3.setText("Deuda por Cobrar");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        jLabel4.setText("Deudas por Pagar");
        jPanel12.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, -1));
        jPanel12.add(txtDeudasPorCobrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 112, 150, 30));
        jPanel12.add(txtDeudasPorPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 152, 150, 30));
        jPanel12.add(dateFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 40, 170, 30));
        jPanel12.add(dateInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 170, 30));

        jLabel5.setText("Fecha Inicio");
        jPanel12.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, -1, -1));

        jLabel6.setText("Fecha Término");
        jPanel12.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, -1, -1));

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

    private void btnHistorialCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialCompraActionPerformed
        generarGraficas();
    }//GEN-LAST:event_btnHistorialCompraActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnHistorialCompra;
    private javax.swing.JButton btnSalir;
    private com.toedter.calendar.JDateChooser dateFin;
    private com.toedter.calendar.JDateChooser dateInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JTextField txtDeudasPorCobrar;
    private javax.swing.JTextField txtDeudasPorPagar;
    public javax.swing.JTextField txtEgreso;
    public javax.swing.JTextField txtIngreso;
    // End of variables declaration//GEN-END:variables
}
