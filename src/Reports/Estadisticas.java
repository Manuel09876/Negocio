package Reports;
// LOS TOTALES DE DEUDA NO DEBEN SER POR FECHA SINO TOTALES

import conectar.Conectar;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
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
import org.jfree.data.xy.XYDataset;

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
            String Total = "SELECT SUM(precio) FROM orderservice";
            String Egresos = "SELECT (SELECT SUM(Total) FROM compraproductosmateriales) + (SELECT SUM(total) FROM detallepagosgenerales) +"
                    + "(SELECT SUM(Total) FROM detalle_compraproductosmateriales) AS TotalCombinado FROM dual";
            String deudaCobrar = "SELECT SUM(precio) FROM orderservice WHERE eeCta = 'Deuda'";
            String Ingresos = "SELECT SUM(precio) FROM orderservice WHERE eeCta = 'Cancelada'";
            String deudasPagar = "SELECT SUM(Deuda) AS TotalDeuda\n"
                    + "FROM (\n"
                    + "  SELECT cp.id, \n"
                    + "         COALESCE(p.nameProduct, 'Sin Descripción') AS Descripcion, \n"
                    + "         cp.fechaPago AS 'Fecha de Pago', \n"
                    + "         cp.NumeroCuotas AS 'Numero de Cuota', \n"
                    + "         cp.cuota AS Cuota, \n"
                    + "         cp.Diferencia AS Deuda, \n"
                    + "         cp.estado AS Estado \n"
                    + "  FROM creditoprod AS cp\n"
                    + "  INNER JOIN detalle_compraproductosmateriales AS dcpm ON cp.id_compra = dcpm.id_CompProMat\n"
                    + "  INNER JOIN product AS p ON dcpm.id_producto = p.idProduct\n"
                    + "  WHERE cp.estado = 'Pendiente'\n"
                    + "  UNION ALL\n"
                    + "  SELECT cpg.id, \n"
                    + "         (SELECT nombre FROM tipos_pagosgenerales tpg WHERE tpg.id_pagos = (SELECT id_tipodePago FROM detallepagosgenerales dpg WHERE dpg.id_PagosGenerales = cpg.id_compra)) AS Descripcion, \n"
                    + "         cpg.fechaPago AS 'Fecha de Pago', \n"
                    + "         cpg.NumeroCuotas AS 'Cuota Numero', \n"
                    + "         cpg.cuota AS 'Monto a Pagar', \n"
                    + "         cpg.Diferencia AS Deuda, \n"
                    + "         cpg.estado AS Estado \n"
                    + "  FROM creditopg cpg \n"
                    + "  WHERE cpg.estado = 'Pendiente'\n"
                    + "  UNION ALL\n"
                    + "  SELECT c.id, \n"
                    + "         e.nombre AS Descripcion, \n"
                    + "         c.fechaPago AS 'Fecha de Pago', \n"
                    + "         c.NumeroCuotas AS 'Numero de Cuota', \n"
                    + "         c.cuota AS Cuota, \n"
                    + "         c.Diferencia AS Deuda, \n"
                    + "         c.estado AS Estado \n"
                    + "  FROM credito AS c \n"
                    + "  INNER JOIN equipos AS e ON c.id_compra = e.id_equipos \n"
                    + "  WHERE c.estado = 'Pendiente'\n"
                    + ") AS total_deuda\n"
                    + "ORDER BY `Fecha de Pago` ASC;";

            String todasPorEmpresa = "SELECT SUM(precio) FROM orderservice WHERE id_empresa=3";

        } catch (Exception e) {
        }
    }

    private TimeSeriesCollection crearDataset(String query, String label, String dateColumn, String valueColumn) {
        TimeSeries series = new TimeSeries(label);
        try {
            Connection conn = con.getConexion();
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Date fecha = rs.getDate(dateColumn); // Asegúrate de que 'dateColumn' es el nombre correcto de la columna de fecha
                double valor = rs.getDouble(valueColumn); // Asegúrate de que 'valueColumn' es el nombre correcto de la columna de valor
//            System.out.println("Fecha: " + fecha + ", Valor: " + valor); // Depuración
                series.add(new Day(fecha), valor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String categoria = "Totales";

        // Agregar los datos al dataset
        dataset.addValue(Double.parseDouble(txtTotalIngresos.getText()), "Ingresos", categoria);
        dataset.addValue(Double.parseDouble(txtTotalEgresos.getText()), "Egresos", categoria);
        dataset.addValue(Double.parseDouble(txtTotalDeudasCobrar.getText()), "Deudas por Cobrar", categoria);
        dataset.addValue(Double.parseDouble(txtTotalDeudasPagar.getText()), "Deudas por Pagar", categoria);

        return dataset;
    }

    private JFreeChart createBarChart(DefaultCategoryDataset dataset) {
        JFreeChart barChart = ChartFactory.createBarChart(
                "Totales de Ingresos, Egresos y Deudas",
                "Categoría",
                "Total",
                dataset
        );
        return barChart;
    }

    private void exportarGraficaComoImagen(JFreeChart chart, String filename, int width, int height) {
        try {
            BufferedImage chartImage = chart.createBufferedImage(width, height);
            File outputfile = new File(filename);
            ImageIO.write(chartImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void imprimirImagen(String filename) {
        try {
            BufferedImage image = ImageIO.read(new File(filename));
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setPrintable(new Printable() {
                public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
                    if (pageIndex != 0) {
                        return NO_SUCH_PAGE;
                    }
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                    g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                    return PAGE_EXISTS;
                }
            });

            if (printJob.printDialog()) {
                printJob.print();
            }
        } catch (IOException | PrinterException e) {
            e.printStackTrace();
        }
    }

    private void generarGraficas() {
        if (dateInicio.getDate() == null || dateFin.getDate() == null) {
            System.out.println("Las fechas de inicio y fin no pueden ser nulas");
            return;
        }

        String fechaInicio = new SimpleDateFormat("yyyy-MM-dd").format(dateInicio.getDate());
        String fechaFin = new SimpleDateFormat("yyyy-MM-dd").format(dateFin.getDate());

        String queryIngresos = "SELECT fechaT, SUM(precio) as total FROM orderservice WHERE eeCta = 'Cancelada' AND fechaT BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "' GROUP BY fechaT";
        String queryEgresos = "SELECT fecha, SUM(Total) as total FROM (SELECT fecha, Total FROM compraproductosmateriales UNION ALL SELECT fecha, total FROM detallepagosgenerales UNION ALL SELECT fecha, Total FROM detalle_compraproductosmateriales) AS egresos WHERE fecha BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "' GROUP BY fecha";
        String queryDeudasPorCobrar = "SELECT fechaT, SUM(precio) as total FROM orderservice WHERE eeCta = 'Deuda' AND fechaT BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "' GROUP BY fechaT";
        String queryDeudasPorPagar = "SELECT fechaPago AS fecha, SUM(Deuda) as total FROM (SELECT cp.fechaPago AS fechaPago, cp.Diferencia AS Deuda FROM creditoprod AS cp INNER JOIN detalle_compraproductosmateriales AS dcpm ON cp.id_compra = dcpm.id_CompProMat INNER JOIN product AS p ON dcpm.id_producto = p.idProduct WHERE cp.estado = 'Pendiente' UNION ALL SELECT cpg.fechaPago AS fechaPago, cpg.Diferencia AS Deuda FROM creditopg cpg WHERE cpg.estado = 'Pendiente' UNION ALL SELECT c.fechaPago AS fechaPago, c.Diferencia AS Deuda FROM credito AS c INNER JOIN equipos AS e ON c.id_compra = e.id_equipos WHERE c.estado = 'Pendiente') AS total_deuda WHERE fechaPago BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "' GROUP BY fechaPago";

        // Calcula y actualiza los totales
        double totalIngresos = obtenerTotal(queryIngresos, "total");
        double totalEgresos = obtenerTotal(queryEgresos, "total");
        double totalDeudasCobrar = obtenerTotal(queryDeudasPorCobrar, "total");
        double totalDeudasPagar = obtenerTotal(queryDeudasPorPagar, "total");

        txtTotalIngresos.setText(String.valueOf(totalIngresos));
        txtTotalEgresos.setText(String.valueOf(totalEgresos));
        txtTotalDeudasCobrar.setText(String.valueOf(totalDeudasCobrar));
        txtTotalDeudasPagar.setText(String.valueOf(totalDeudasPagar));

        TimeSeriesCollection datasetIngresos = crearDataset(queryIngresos, "Ingresos", "fechaT", "total");
        TimeSeriesCollection datasetEgresos = crearDataset(queryEgresos, "Egresos", "fecha", "total");
        TimeSeriesCollection datasetDeudasPorCobrar = crearDataset(queryDeudasPorCobrar, "Deudas por Cobrar", "fechaT", "total");
        TimeSeriesCollection datasetDeudasPorPagar = crearDataset(queryDeudasPorPagar, "Deudas por Pagar", "fecha", "total");

        JFreeChart chartIngresos = ChartFactory.createTimeSeriesChart("Ingresos por Día", "Fecha", "Ingreso", datasetIngresos, true, true, false);
        JFreeChart chartEgresos = ChartFactory.createTimeSeriesChart("Egresos por Día", "Fecha", "Egreso", datasetEgresos, true, true, false);
        JFreeChart chartDeudasPorCobrar = ChartFactory.createTimeSeriesChart("Deudas por Cobrar por Día", "Fecha", "Deuda por Cobrar", datasetDeudasPorCobrar, true, true, false);
        JFreeChart chartDeudasPorPagar = ChartFactory.createTimeSeriesChart("Deudas por Pagar por Día", "Fecha", "Deuda por Pagar", datasetDeudasPorPagar, true, true, false);

        // Personalizar el eje X para mostrar solo el día
        configurarEjeFecha(chartIngresos);
        configurarEjeFecha(chartEgresos);
        configurarEjeFecha(chartDeudasPorCobrar);
        configurarEjeFecha(chartDeudasPorPagar);

        ChartPanel chartPanelIngresos = new ChartPanel(chartIngresos);
        ChartPanel chartPanelEgresos = new ChartPanel(chartEgresos);
        ChartPanel chartPanelDeudasPorCobrar = new ChartPanel(chartDeudasPorCobrar);
        ChartPanel chartPanelDeudasPorPagar = new ChartPanel(chartDeudasPorPagar);

        // Crear dataset y gráfico de barras
        DefaultCategoryDataset barDataset = createDataset();
        JFreeChart barChart = createBarChart(barDataset);
        ChartPanel barChartPanel = new ChartPanel(barChart);

        JPanel panelGraficas = new JPanel();
        panelGraficas.setLayout(new GridLayout(3, 2)); // Establecer el diseño de cuadrícula para 3 filas y 2 columnas
        panelGraficas.add(chartPanelIngresos);
        panelGraficas.add(chartPanelEgresos);
        panelGraficas.add(chartPanelDeudasPorCobrar);
        panelGraficas.add(chartPanelDeudasPorPagar);
        panelGraficas.add(barChartPanel);  // Añadir el gráfico de barras

        JFrame frame = new JFrame("Graficas de Ingresos y Egresos");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(panelGraficas);
        frame.pack();
        frame.setVisible(true);
    }

    private double obtenerTotal(String query, String columnLabel) {
        double total = 0.0;
        try (Connection conn = con.getConexion(); PreparedStatement pst = conn.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                total = rs.getDouble(columnLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    private void configurarEjeFecha(JFreeChart chart) {
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        txtTotalEgresos = new javax.swing.JTextField();
        btnGenerarReporte = new javax.swing.JButton();
        txtTotalIngresos = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTotalDeudasCobrar = new javax.swing.JTextField();
        txtTotalDeudasPagar = new javax.swing.JTextField();
        dateFin = new com.toedter.calendar.JDateChooser();
        dateInicio = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnImprimirGrafica = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Estadisticas");

        jPanel12.setBackground(new java.awt.Color(255, 153, 0));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel12.add(txtTotalEgresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 150, 30));

        btnGenerarReporte.setText("Generar Reporte");
        btnGenerarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerarReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 20, 120, 50));
        jPanel12.add(txtTotalIngresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 150, 30));

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
        jPanel12.add(txtTotalDeudasCobrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 112, 150, 30));
        jPanel12.add(txtTotalDeudasPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 152, 150, 30));
        jPanel12.add(dateFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 40, 170, 30));
        jPanel12.add(dateInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 170, 30));

        jLabel5.setText("Fecha Inicio");
        jPanel12.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, -1, -1));

        jLabel6.setText("Fecha Término");
        jPanel12.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, -1, -1));

        btnImprimirGrafica.setText("Imprimir Grafica");
        btnImprimirGrafica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirGraficaActionPerformed(evt);
            }
        });
        jPanel12.add(btnImprimirGrafica, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 120, -1, -1));

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

    private void btnGenerarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteActionPerformed
        generarGraficas();
    }//GEN-LAST:event_btnGenerarReporteActionPerformed

    private void btnImprimirGraficaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirGraficaActionPerformed
        DefaultCategoryDataset barDataset = createDataset();
        JFreeChart barChart = createBarChart(barDataset);
        String filename = "grafica.png";
        exportarGraficaComoImagen(barChart, filename, 800, 600); // Ajustar tamaño de la imagen
        imprimirImagen(filename);
    }//GEN-LAST:event_btnImprimirGraficaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnGenerarReporte;
    public javax.swing.JButton btnImprimirGrafica;
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
    private javax.swing.JTextField txtTotalDeudasCobrar;
    private javax.swing.JTextField txtTotalDeudasPagar;
    public javax.swing.JTextField txtTotalEgresos;
    public javax.swing.JTextField txtTotalIngresos;
    // End of variables declaration//GEN-END:variables
}
