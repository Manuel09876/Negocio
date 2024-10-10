package Reports;

import conectar.Conectar;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Sueldos extends javax.swing.JInternalFrame {

    public Sueldos() {
        initComponents();
        
        AutoCompleteDecorator.decorate(cbxTrabajador);
        AutoCompleteDecorator.decorate(cbxPeriodoPago);
        MostrarTrabajador(cbxTrabajador);
        txtIdSueldos.setVisible(false);
        inicializarPeriodoPago();  // Llenar el JComboBox de períodos de pago con valores predeterminados

//        llenarComboPeriodoPago(cbxPeriodoPago); // Llenar el JComboBox de períodos de pago
        cbxPeriodoPago.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (!txtIdTrabajador.getText().trim().isEmpty()) {
                    int trabajadorId = Integer.parseInt(txtIdTrabajador.getText().trim());
                    llenarComboPeriodoPago(cbxPeriodoPago); // Llenar el JComboBox con los periodos
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un trabajador válido.");
                }
            }
        });

        cbxNumeroPeriodo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxNumeroPeriodoItemStateChanged(evt);
            }
        });

    }
    
    private void cbxPeriodoPagoItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String tipoPeriodo = cbxPeriodoPago.getSelectedItem().toString();
            LocalDate fechaInicioActividades = obtenerFechaInicioActividadesGlobal();  // Usa la fecha de inicio global
            actualizarNumeroPeriodoComboBox(tipoPeriodo, fechaInicioActividades);
        }
    }

    private void cargarPeriodoTrabajador(int trabajadorId) {
        Connection connection = null;
        String sql = """
                     SELECT p.descripcion FROM puestodetrabajo pt 
                     INNER JOIN periodo p ON pt.id_periodo=p.id
                     WHERE  pt.idTrabajador = ?""";

        try{
        Conectar.getInstancia().obtenerConexion();
            
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String tipoPeriodo = rs.getString("id_periodo");
                cbxPeriodoPago.setSelectedItem(tipoPeriodo); // Fijar el valor correcto
                cbxPeriodoPago.setEnabled(false); // Bloquear el ComboBox para evitar cambios
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el periodo de pago del trabajador.");
            }
        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el periodo de pago del trabajador: " + e.getMessage());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    private void cargarNumeroPeriodo(int trabajadorId) {
        // Obtener la fecha de inicio global de actividades
        LocalDate fechaInicioActividades = obtenerFechaInicioActividadesGlobal();

        // Obtener el tipo de periodo (semanal, quincenal, mensual) desde el ComboBox (que ya está bloqueado)
        String tipoPeriodo = cbxPeriodoPago.getSelectedItem().toString();

        // Calcular el número de periodos
        actualizarNumeroPeriodoComboBox(tipoPeriodo, fechaInicioActividades);
    }

    private void inicializarPeriodoPago() {
        cbxPeriodoPago.addItem("Semanal");
        cbxPeriodoPago.addItem("Quincenal");
        cbxPeriodoPago.addItem("Mensual");
    }

    // Método para mostrar sueldos basado en el rango de fechas y tipo de período
    public void mostrarSueldos(int trabajadorId, java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        Connection connection = null;
        // Definir el modelo de tabla
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Trabajador");
        modelo.addColumn("Nombre");
        modelo.addColumn("Fecha");
        modelo.addColumn("Horas Trabajadas");
        modelo.addColumn("Horas Extras");
        modelo.addColumn("Sueldo Bruto");
        modelo.addColumn("Sueldo Neto");
        TableSueldos.setModel(modelo);

        // SQL para obtener los datos de sueldos con parámetros
        String sql = "SELECT t.idWorker, t.nombre, DATE(hi.fInicio) AS fecha, \n"
                + "SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) AS horasTrabajadas, \n"
                + "SUM(CASE WHEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 > 8 \n"
                + "THEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 - 8 ELSE 0 END) AS horasExtras,\n"
                + "(SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) * p.pagoPorHora) AS sueldoBruto,\n"
                + "(SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) * p.pagoPorHora) * 0.7 AS sueldoNeto\n"
                + "FROM horas_ingreso hi \n"
                + "JOIN horas_salida hs ON hi.id_ingreso = hs.id_ingreso\n"
                + "JOIN worker t ON hi.trabajador_id = t.idWorker\n"
                + "JOIN puestodetrabajo p ON t.idWorker = p.idTrabajador\n"
                + "WHERE hi.trabajador_id = ?\n" // Parámetro 1: trabajadorId
                + "AND hi.fInicio BETWEEN ? AND ?\n" // Parámetro 2 y 3: fechaInicio, fechaFin
                + "GROUP BY t.idWorker, t.nombre, DATE(hi.fInicio)\n"
                + "ORDER BY DATE(hi.fInicio) ASC";

        Conectar.getInstancia().obtenerConexion();
        
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            // Establecer los parámetros en el PreparedStatement
            pst.setInt(1, trabajadorId);  // Parámetro 1
            pst.setDate(2, fechaInicio);  // Parámetro 2
            pst.setDate(3, fechaFin);     // Parámetro 3

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("idWorker"),
                    rs.getString("nombre"),
                    rs.getDate("fecha"),
                    rs.getDouble("horasTrabajadas"),
                    rs.getDouble("horasExtras"),
                    rs.getDouble("sueldoBruto"),
                    rs.getDouble("sueldoNeto")
                });
            }

            TableSueldos.setModel(modelo);
            System.out.println("ID Trabajador: " + trabajadorId);
            System.out.println("Fecha Inicio: " + fechaInicio);
            System.out.println("Fecha Fin: " + fechaFin);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al mostrar sueldos: " + e.getMessage());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    private java.util.Date[] obtenerFechasPeriodoPago(int trabajadorId, String periodoPago) {
        java.util.Date[] periodo = new java.util.Date[2];
        LocalDate fechaInicioActividades = obtenerFechaInicioActividades(trabajadorId); // Método para obtener la fecha de inicio de actividades
        LocalDate fechaInicio = null;
        LocalDate fechaFin = null;

        switch (periodoPago) {
            case "Semanal":
                fechaInicio = fechaInicioActividades.with(DayOfWeek.MONDAY); // Inicia el lunes
                fechaFin = fechaInicio.plusDays(6); // Termina el domingo
                break;
            case "Quincenal":
                fechaInicio = fechaInicioActividades; // Inicia en el primer día del periodo quincenal
                fechaFin = fechaInicio.plusDays(13); // Termina después de 14 días
                break;
            case "Mensual":
                fechaInicio = fechaInicioActividades.withDayOfMonth(1); // Inicia en el primer día del mes
                fechaFin = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth()); // Termina en el último día del mes
                break;
        }

        // Convertir LocalDate a java.util.Date
        periodo[0] = java.sql.Date.valueOf(fechaInicio);
        periodo[1] = java.sql.Date.valueOf(fechaFin);
        return periodo;
    }

    public LocalDate[] obtenerRangoPorPeriodo(String tipoPeriodo, LocalDate fechaInicioActividades, int numeroPeriodo) {
        LocalDate fechaInicioPeriodo;
        LocalDate fechaFinPeriodo;

        switch (tipoPeriodo) {
            case "Semanal":
                fechaInicioPeriodo = fechaInicioActividades.plusWeeks(numeroPeriodo - 1); // Semana n
                fechaFinPeriodo = fechaInicioPeriodo.plusDays(6); // El domingo de esa semana
                break;

            case "Quincenal":
                fechaInicioPeriodo = fechaInicioActividades.plusDays((numeroPeriodo - 1) * 14); // Quincena n
                fechaFinPeriodo = fechaInicioPeriodo.plusDays(13); // Fin de la quincena
                break;

            case "Mensual":
                fechaInicioPeriodo = fechaInicioActividades.plusMonths(numeroPeriodo - 1).withDayOfMonth(1); // Mes n
                fechaFinPeriodo = fechaInicioPeriodo.withDayOfMonth(fechaInicioPeriodo.lengthOfMonth()); // Último día del mes
                break;

            default:
                throw new IllegalArgumentException("Tipo de periodo no válido: " + tipoPeriodo);
        }

        return new LocalDate[]{fechaInicioPeriodo, fechaFinPeriodo}; // Retorna las fechas de inicio y fin del periodo
    }

    private LocalDate obtenerFechaInicioActividadesGlobal() {
        Connection connection = null;
        String sql = "SELECT fecha_inicio_actividades FROM configuracion"; // Tabla de configuración

        try{
        Conectar.getInstancia().obtenerConexion();
            
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDate("fecha_inicio_actividades").toLocalDate();
            } else {
                throw new SQLException("No se encontró la fecha de inicio de actividades.");
            }
        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener la fecha de inicio de actividades: " + e.getMessage());
            return LocalDate.now(); // Por defecto, devolver la fecha actual si falla
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    private LocalDate obtenerFechaInicioActividades(int trabajadorId) {
        Connection connection = null;
        String sql = "SELECT fechaDePuesto FROM puestodetrabajo WHERE idTrabajador = ? ORDER BY fechaDePuesto ASC LIMIT 1";

        try{
        Conectar.getInstancia().obtenerConexion();
            
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getDate("fechaDePuesto").toLocalDate();
            } else {
                throw new SQLException("No se encontró la fecha de inicio de actividades para el trabajador con ID: " + trabajadorId);
            }
        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener la fecha de inicio de actividades: " + e.getMessage());
            return LocalDate.now();
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    private void llenarComboPeriodoPago(JComboBox<String> comboPeriodoPago) {
        if (comboPeriodoPago.getSelectedItem() != null) {
            String tipoPeriodo = comboPeriodoPago.getSelectedItem().toString();
            LocalDate fechaInicioActividades = obtenerFechaInicioActividadesGlobal();  // Usa la fecha de inicio global
            actualizarNumeroPeriodoComboBox(tipoPeriodo, fechaInicioActividades);  // Pasa la fecha de inicio global
        } else {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado ningún periodo de pago.");
        }
    }

    private int calcularNumeroPeriodos(LocalDate fechaInicioActividades, LocalDate fechaActual, String tipoPeriodo) {
        switch (tipoPeriodo) {
            case "Semanal":
                return (int) ChronoUnit.WEEKS.between(fechaInicioActividades, fechaActual) + 1; // Incluye la semana actual
            case "Quincenal":
                return (int) ChronoUnit.DAYS.between(fechaInicioActividades, fechaActual) / 14 + 1; // Periodos de 14 días
            case "Mensual":
                return (int) ChronoUnit.MONTHS.between(fechaInicioActividades, fechaActual) + 1; // Incluye el mes actual
            default:
                throw new IllegalArgumentException("Tipo de periodo no válido: " + tipoPeriodo);
        }
    }

    private void cbxNumeroPeriodoItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            try {
                // Obtener el tipo de período seleccionado
                String tipoPeriodo = cbxPeriodoPago.getSelectedItem().toString();

                // Obtener el número de período seleccionado
                int numeroPeriodo = Integer.parseInt(cbxNumeroPeriodo.getSelectedItem().toString());

                // Obtener la fecha de inicio de actividades (de la empresa, no del trabajador)
                LocalDate fechaInicioActividades = obtenerFechaInicioActividadesGlobal();

                // Obtener el rango de fechas para el período seleccionado
                LocalDate[] rangoFechas = obtenerRangoPorPeriodo(tipoPeriodo, fechaInicioActividades, numeroPeriodo);

                // Mostrar el rango de fechas en el JTextField txtFechaPeriodo
                String rangoFechasStr = "Desde: " + rangoFechas[0] + " Hasta: " + rangoFechas[1];
                txtFechaPeriodo.setText(rangoFechasStr);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error al seleccionar el número de período: " + e.getMessage());
            }
        }
    }

// Método para actualizar el ComboBox de Número de Periodo según el tipo de periodo
    private void actualizarNumeroPeriodoComboBox(String tipoPeriodo, LocalDate fechaInicioActividades) {
        cbxNumeroPeriodo.removeAllItems(); // Limpiar los ítems existentes

        LocalDate fechaActual = LocalDate.now();
        int maxPeriodos = 0;

        switch (tipoPeriodo) {
            case "Semanal":
                maxPeriodos = (int) ChronoUnit.WEEKS.between(fechaInicioActividades, fechaActual) + 1;
                break;
            case "Quincenal":
                maxPeriodos = (int) ChronoUnit.DAYS.between(fechaInicioActividades, fechaActual) / 14 + 1;
                break;
            case "Mensual":
                maxPeriodos = (int) ChronoUnit.MONTHS.between(fechaInicioActividades, fechaActual) + 1;
                break;
        }

        for (int i = 1; i <= maxPeriodos; i++) {
            cbxNumeroPeriodo.addItem(String.valueOf(i));
        }
    }

    private int obtenerNumeroMaximoDeSemanas(LocalDate fechaInicioActividades) {
        LocalDate fechaActual = LocalDate.now(); // Fecha actual
        return (int) ChronoUnit.WEEKS.between(fechaInicioActividades.with(DayOfWeek.MONDAY), fechaActual.with(DayOfWeek.SUNDAY)); // Total de semanas completas
    }

    private int obtenerNumeroMaximoDeQuincenas(LocalDate fechaInicioActividades) {
        LocalDate fechaActual = LocalDate.now(); // Fecha actual
        return (int) ChronoUnit.DAYS.between(fechaInicioActividades, fechaActual) / 14 + 1; // Cada quincena es de 14 días
    }

    private int obtenerNumeroMaximoDeMeses(LocalDate fechaInicioActividades) {
        LocalDate fechaActual = LocalDate.now(); // Fecha actual
        return (int) ChronoUnit.MONTHS.between(fechaInicioActividades.withDayOfMonth(1), fechaActual.withDayOfMonth(1)) + 1; // Total de meses completos
    }

    private String obtenerTipoPeriodoAsignado(int trabajadorId) {
        Connection connection = null;
        
        String sql = "SELECT p.descripcion FROM puestodetrabajo pt\n"
                + "INNER JOIN periodo p ON pt.id_periodo=p.id\n"
                + "INNER JOIN worker w ON pt.idTrabajador=w.idWorker\n"
                + "WHERE idTrabajador=? LIMIT 1";
        
        try{
        Conectar.getInstancia().obtenerConexion();
            
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("descripcion"); // Retorna "Semanal", "Quincenal" o "Mensual"
            }
        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el periodo del trabajador: " + e.getMessage());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
        return null; // Retorna null si no se encuentra el periodo
    }

    public void limpiarTabla(javax.swing.JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        int rowCount = modelo.getRowCount();

        // Eliminar todas las filas una por una
        for (int i = rowCount - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        TableSueldos = new javax.swing.JTable();
        btnMostrar = new javax.swing.JButton();
        txtIdSueldos = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        cbxTrabajador = new javax.swing.JComboBox<>();
        txtIdTrabajador = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbxPeriodoPago = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        cbxNumeroPeriodo = new javax.swing.JComboBox<>();
        txtFechaPeriodo = new javax.swing.JTextField();
        btnCargarPeriodos = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTrabajadores = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaPlanillaMensual = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaResumen = new javax.swing.JTable();
        btnReporte = new javax.swing.JButton();
        panelsemanal = new javax.swing.JPanel();
        panelquincenal = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaPlanillaSemanal = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaPlanillaQuincenal = new javax.swing.JTable();

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Sueldos");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setBackground(new java.awt.Color(153, 153, 255));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TableSueldos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TableSueldos.setRowHeight(23);
        jScrollPane14.setViewportView(TableSueldos);

        jPanel12.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 710, 110));

        btnMostrar.setText("Mostrar");
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });
        jPanel12.add(btnMostrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 10, 80, 30));
        jPanel12.add(txtIdSueldos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 20));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 20, -1, -1));

        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });
        jPanel12.add(cbxTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 180, -1));
        jPanel12.add(txtIdTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, -1, -1));

        jLabel3.setText("Trabajador");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        jPanel12.add(cbxPeriodoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 180, -1));

        jLabel1.setText("Priodo");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, -1, -1));

        jPanel12.add(cbxNumeroPeriodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, 110, -1));
        jPanel12.add(txtFechaPeriodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 270, -1));

        btnCargarPeriodos.setText("Numero Periodo");
        btnCargarPeriodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarPeriodosActionPerformed(evt);
            }
        });
        jPanel12.add(btnCargarPeriodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, -1, -1));

        btnLimpiar.setText("Limpiar Tabla");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        jPanel12.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 50, -1, -1));

        tablaTrabajadores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaTrabajadores);

        jPanel12.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 110, 670, 110));

        tablaPlanillaMensual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tablaPlanillaMensual);

        jPanel12.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, 710, 130));

        tablaResumen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(tablaResumen);

        jPanel12.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 490, 670, 130));

        btnReporte.setText("Reportes");
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });
        jPanel12.add(btnReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 40, -1, -1));
        jPanel12.add(panelsemanal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 700, 130));
        jPanel12.add(panelquincenal, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 290, 670, 130));

        tablaPlanillaSemanal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(tablaPlanillaSemanal);

        jPanel12.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 680, 110));

        tablaPlanillaQuincenal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(tablaPlanillaQuincenal);

        jPanel12.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 300, 650, 110));

        getContentPane().add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1430, 650));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);

        // Obtener el tipo de periodo asignado al trabajador desde la base de datos
        String tipoPeriodoAsignado = obtenerTipoPeriodoAsignado(Integer.parseInt(txtIdTrabajador.getText()));

        // Establecer el tipo de periodo en el ComboBox
        cbxPeriodoPago.setSelectedItem(tipoPeriodoAsignado);

        // Obtener la fecha de inicio de actividades de la empresa
        LocalDate fechaInicioActividades = obtenerFechaInicioActividadesGlobal();

        // Llenar el ComboBox de números de periodo
        actualizarNumeroPeriodoComboBox(tipoPeriodoAsignado, fechaInicioActividades);
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarActionPerformed
        if (txtIdTrabajador.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un trabajador válido.");
            return;
        }

        if (cbxPeriodoPago.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un periodo de pago.");
            return;
        }

        if (cbxNumeroPeriodo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un número de periodo.");
            return;
        }

        try {
            int trabajadorId = Integer.parseInt(txtIdTrabajador.getText().trim());
            LocalDate fechaInicioActividades = obtenerFechaInicioActividadesGlobal(); // Fecha de inicio de la empresa
            String tipoPeriodo = cbxPeriodoPago.getSelectedItem().toString(); // Semanal, Quincenal, Mensual
            int numeroPeriodo = Integer.parseInt(cbxNumeroPeriodo.getSelectedItem().toString()); // El periodo seleccionado

            // Obtener el rango de fechas basado en el periodo y la fecha de inicio de actividades
            LocalDate[] fechasPeriodo = obtenerRangoPorPeriodo(tipoPeriodo, fechaInicioActividades, numeroPeriodo);

            // Convertir las fechas LocalDate a java.sql.Date para usar en la consulta SQL
            java.sql.Date fechaInicio = java.sql.Date.valueOf(fechasPeriodo[0]);
            java.sql.Date fechaFin = java.sql.Date.valueOf(fechasPeriodo[1]);

            // Mostrar las fechas en el campo de texto de la interfaz (opcional)
            txtFechaPeriodo.setText(fechasPeriodo[0] + " - " + fechasPeriodo[1]);

            // Mostrar los sueldos para el trabajador en el periodo seleccionado
            mostrarSueldos(trabajadorId, fechaInicio, fechaFin);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID de trabajador debe ser un número válido.");
        }
    }//GEN-LAST:event_btnMostrarActionPerformed

    private void btnCargarPeriodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarPeriodosActionPerformed
        String tipoPeriodo = cbxPeriodoPago.getSelectedItem().toString();
        LocalDate fechaInicioActividades = obtenerFechaInicioActividadesGlobal();  // Usa la fecha de inicio global
        actualizarNumeroPeriodoComboBox(tipoPeriodo, fechaInicioActividades);
    }//GEN-LAST:event_btnCargarPeriodosActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarTabla(TableSueldos);
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        try {
            int trabajadorId = Integer.parseInt(txtIdTrabajador.getText().trim());
            LocalDate fechaInicioActividades = obtenerFechaInicioActividadesGlobal();
            String tipoPeriodo = cbxPeriodoPago.getSelectedItem().toString();
            int numeroPeriodo = Integer.parseInt(cbxNumeroPeriodo.getSelectedItem().toString());

            LocalDate[] fechasPeriodo = obtenerRangoPorPeriodo(tipoPeriodo, fechaInicioActividades, numeroPeriodo);
            java.sql.Date fechaInicio = java.sql.Date.valueOf(fechasPeriodo[0]);
            java.sql.Date fechaFin = java.sql.Date.valueOf(fechasPeriodo[1]);

            txtFechaPeriodo.setText(fechasPeriodo[0] + " - " + fechasPeriodo[1]);

            // Mostrar planilla dependiendo del tipo de periodo
            if (tipoPeriodo.equals("Semanal")) {
                mostrarPlanillaSemanal(trabajadorId, fechaInicio, fechaFin);
            } else if (tipoPeriodo.equals("Quincenal")) {
                mostrarPlanillaQuincenal(trabajadorId, fechaInicio, fechaFin);
            } else if (tipoPeriodo.equals("Mensual")) {
                mostrarPlanillaMensual(trabajadorId, fechaInicio, fechaFin);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, verifique el ID del trabajador o el número de periodo.");
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado: " + e.getMessage());
        }
    }//GEN-LAST:event_btnReporteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable TableSueldos;
    public javax.swing.JButton btnCargarPeriodos;
    public javax.swing.JButton btnLimpiar;
    public javax.swing.JButton btnMostrar;
    public javax.swing.JButton btnReporte;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxNumeroPeriodo;
    private javax.swing.JComboBox<String> cbxPeriodoPago;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable3;
    private javax.swing.JPanel panelquincenal;
    private javax.swing.JPanel panelsemanal;
    private javax.swing.JTable tablaPlanillaMensual;
    private javax.swing.JTable tablaPlanillaQuincenal;
    private javax.swing.JTable tablaPlanillaSemanal;
    private javax.swing.JTable tablaResumen;
    private javax.swing.JTable tablaTrabajadores;
    private javax.swing.JTextField txtFechaPeriodo;
    public javax.swing.JTextField txtIdSueldos;
    private javax.swing.JTextField txtIdTrabajador;
    // End of variables declaration//GEN-END:variables

    public void MostrarTrabajador(JComboBox comboTrabajador) {
        Connection connection = null;
        
        String sql = "SELECT * FROM worker";
        
        try{
        Conectar.getInstancia().obtenerConexion();
            
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            comboTrabajador.removeAllItems();
            while (rs.next()) {
                comboTrabajador.addItem(rs.getString("nombre"));
            }
        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Trabajadores: " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoTrabajador(JComboBox trabajador, JTextField IdTrabajador) {
        Connection connection = null;
        
        String consulta = "SELECT worker.idWorker FROM worker WHERE worker.nombre=?";

        try{
        Conectar.getInstancia().obtenerConexion();
        
        
        try (PreparedStatement ps = connection.prepareStatement(consulta)) {
            ps.setString(1, trabajador.getSelectedItem().toString()); // Asegúrate de que estás proporcionando el parámetro
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                IdTrabajador.setText(rs.getString("idWorker"));
            }
        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void mostrarPlanillaYResumen(int trabajadorId, java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        Connection connection = null;
        
        // Tabla 1: Relación de Trabajadores
        DefaultTableModel modeloTrabajadores = new DefaultTableModel();
        modeloTrabajadores.addColumn("ID Trabajador");
        modeloTrabajadores.addColumn("Nombre");
        modeloTrabajadores.addColumn("Puesto");

//        // Tabla 2: Planilla de Pagos
//        DefaultTableModel modeloPlanilla = new DefaultTableModel();
//        modeloPlanilla.addColumn("ID Trabajador");
//        modeloPlanilla.addColumn("Fecha");
//        modeloPlanilla.addColumn("Horas Trabajadas");
//        modeloPlanilla.addColumn("Horas Extras");
//        modeloPlanilla.addColumn("Sueldo Bruto");
//        modeloPlanilla.addColumn("Sueldo Neto");
        // Tabla 3: Resumen de Monto Total
        DefaultTableModel modeloResumen = new DefaultTableModel();
        modeloResumen.addColumn("ID Trabajador");
        modeloResumen.addColumn("Nombre");
        modeloResumen.addColumn("Total a Pagar");

        // SQL para obtener la información básica de los trabajadores
        String sqlTrabajadores = "SELECT w.idWorker, w.nombre, tt.nombre AS puesto "
                + "FROM worker w "
                + "JOIN puestodetrabajo pt ON w.idWorker = pt.idTrabajador "
                + "JOIN tiposdetrabajos tt ON pt.idTDT = tt.id";

//        // SQL para la planilla de pagos
//        String sqlPlanilla = "SELECT t.idWorker, DATE(hi.fInicio) AS fecha, "
//                + "SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) AS horasTrabajadas, "
//                + "SUM(CASE WHEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 > 8 "
//                + "THEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 - 8 ELSE 0 END) AS horasExtras, "
//                + "(SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) * p.pagoPorHora) AS sueldoBruto, "
//                + "(SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) * p.pagoPorHora) * 0.7 AS sueldoNeto "
//                + "FROM horas_ingreso hi "
//                + "JOIN horas_salida hs ON hi.id_ingreso = hs.id_ingreso "
//                + "JOIN worker t ON hi.trabajador_id = t.idWorker "
//                + "JOIN puestodetrabajo p ON t.idWorker = p.idTrabajador "
//                + "WHERE hi.trabajador_id = ? "
//                + // Primer parámetro: trabajadorId
//                "AND hi.fInicio BETWEEN ? AND ? "
//                + // Segundo y tercer parámetro: fechaInicio, fechaFin
//                "GROUP BY t.idWorker, DATE(hi.fInicio) "
//                + "ORDER BY DATE(hi.fInicio) ASC";
        // SQL para obtener el monto total a pagar a cada trabajador
        String sqlResumen = "SELECT t.idWorker, t.nombre, "
                + "SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 * p.pagoPorHora) AS totalPagar "
                + "FROM horas_ingreso hi "
                + "JOIN horas_salida hs ON hi.id_ingreso = hs.id_ingreso "
                + "JOIN worker t ON hi.trabajador_id = t.idWorker "
                + "JOIN puestodetrabajo p ON t.idWorker = p.idTrabajador "
                + "WHERE hi.fInicio BETWEEN ? AND ? "
                + // Parámetros para fechaInicio y fechaFin
                "GROUP BY t.idWorker "
                + "ORDER BY t.nombre ASC";

        
        try{
        Conectar.getInstancia().obtenerConexion();
            
        try (Statement st = connection.createStatement(); PreparedStatement pstResumen = connection.prepareStatement(sqlResumen)) {

            // Obtener los trabajadores
            ResultSet rsTrabajadores = st.executeQuery(sqlTrabajadores);
            while (rsTrabajadores.next()) {
                modeloTrabajadores.addRow(new Object[]{
                    rsTrabajadores.getInt("idWorker"),
                    rsTrabajadores.getString("nombre"),
                    rsTrabajadores.getString("puesto")
                });
            }

//            // Obtener los datos de la planilla de pagos
//            pstPlanilla.setInt(1, trabajadorId);  // Establecer trabajadorId como primer parámetro
//            pstPlanilla.setDate(2, fechaInicio);  // Establecer fechaInicio como segundo parámetro
//            pstPlanilla.setDate(3, fechaFin);     // Establecer fechaFin como tercer parámetro
//            ResultSet rsPlanilla = pstPlanilla.executeQuery();
//            while (rsPlanilla.next()) {
//                modeloPlanilla.addRow(new Object[]{
//                    rsPlanilla.getInt("idWorker"),
//                    rsPlanilla.getDate("fecha"),
//                    rsPlanilla.getDouble("horasTrabajadas"),
//                    rsPlanilla.getDouble("horasExtras"),
//                    rsPlanilla.getDouble("sueldoBruto"),
//                    rsPlanilla.getDouble("sueldoNeto")
//                });
//            }
            // Obtener el resumen del monto total
            pstResumen.setDate(1, fechaInicio);  // Establecer fechaInicio
            pstResumen.setDate(2, fechaFin);     // Establecer fechaFin
            ResultSet rsResumen = pstResumen.executeQuery();
            while (rsResumen.next()) {
                modeloResumen.addRow(new Object[]{
                    rsResumen.getInt("idWorker"),
                    rsResumen.getString("nombre"),
                    rsResumen.getDouble("totalPagar")
                });
            }
        }
            // Asignar los modelos a las tablas
            tablaTrabajadores.setModel(modeloTrabajadores);
//            tablaPlanillaMensual.setModel(modeloPlanilla);
            tablaResumen.setModel(modeloResumen);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al mostrar la planilla: " + e.getMessage());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void mostrarPlanillaSemanal(int trabajadorId, java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        Connection connection = null;
        
        DefaultTableModel modeloSemanal = new DefaultTableModel();
        modeloSemanal.addColumn("ID Trabajador");
        modeloSemanal.addColumn("Fecha");
        modeloSemanal.addColumn("Horas Trabajadas");
        modeloSemanal.addColumn("Horas Extras");
        modeloSemanal.addColumn("Sueldo Bruto");
        modeloSemanal.addColumn("Sueldo Neto");

        String sqlSemanal = "SELECT t.idWorker, DATE(hi.fInicio) AS fecha, "
                + "SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) AS horasTrabajadas, "
                + "SUM(CASE WHEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 > 8 "
                + "THEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 - 8 ELSE 0 END) AS horasExtras, "
                + "(SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) * p.pagoPorHora) AS sueldoBruto, "
                + "(SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) * p.pagoPorHora) * 0.7 AS sueldoNeto "
                + "FROM horas_ingreso hi "
                + "JOIN horas_salida hs ON hi.id_ingreso = hs.id_ingreso "
                + "JOIN worker t ON hi.trabajador_id = t.idWorker "
                + "JOIN puestodetrabajo p ON t.idWorker = p.idTrabajador "
                + "WHERE hi.trabajador_id = ? "
                + "AND hi.fInicio BETWEEN ? AND ? "
                + "GROUP BY t.idWorker, DATE(hi.fInicio) "
                + "ORDER BY DATE(hi.fInicio) ASC";

        try{
        Conectar.getInstancia().obtenerConexion();
            
        try (PreparedStatement pst = connection.prepareStatement(sqlSemanal)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, fechaInicio);
            pst.setDate(3, fechaFin);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                modeloSemanal.addRow(new Object[]{
                    rs.getInt("idWorker"),
                    rs.getDate("fecha"),
                    rs.getDouble("horasTrabajadas"),
                    rs.getDouble("horasExtras"),
                    rs.getDouble("sueldoBruto"),
                    rs.getDouble("sueldoNeto")
                });
            }
        }
            tablaPlanillaSemanal.setModel(modeloSemanal);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar la planilla semanal: " + e.getMessage());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void mostrarPlanillaQuincenal(int trabajadorId, java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        Connection connection = null;
        
        DefaultTableModel modeloQuincenal = new DefaultTableModel();
        modeloQuincenal.addColumn("ID Trabajador");
        modeloQuincenal.addColumn("Fecha");
        modeloQuincenal.addColumn("Horas Trabajadas");
        modeloQuincenal.addColumn("Horas Extras");
        modeloQuincenal.addColumn("Sueldo Bruto");
        modeloQuincenal.addColumn("Sueldo Neto");

        String sqlQuincenal = "SELECT t.idWorker, DATE(hi.fInicio) AS fecha, "
                + "SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) AS horasTrabajadas, "
                + "SUM(CASE WHEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 > 8 "
                + "THEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 - 8 ELSE 0 END) AS horasExtras, "
                + "(SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) * p.pagoPorHora) AS sueldoBruto, "
                + "(SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) * p.pagoPorHora) * 0.7 AS sueldoNeto "
                + "FROM horas_ingreso hi "
                + "JOIN horas_salida hs ON hi.id_ingreso = hs.id_ingreso "
                + "JOIN worker t ON hi.trabajador_id = t.idWorker "
                + "JOIN puestodetrabajo p ON t.idWorker = p.idTrabajador "
                + "WHERE hi.trabajador_id = ? "
                + "AND hi.fInicio BETWEEN ? AND ? "
                + "GROUP BY t.idWorker, DATE(hi.fInicio) "
                + "ORDER BY DATE(hi.fInicio) ASC";

        try{
        Conectar.getInstancia().obtenerConexion();
            
        try (PreparedStatement pst = connection.prepareStatement(sqlQuincenal)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, fechaInicio);
            pst.setDate(3, fechaFin);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                modeloQuincenal.addRow(new Object[]{
                    rs.getInt("idWorker"),
                    rs.getDate("fecha"),
                    rs.getDouble("horasTrabajadas"),
                    rs.getDouble("horasExtras"),
                    rs.getDouble("sueldoBruto"),
                    rs.getDouble("sueldoNeto")
                });
            }
        }
            tablaPlanillaQuincenal.setModel(modeloQuincenal);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar la planilla quincenal: " + e.getMessage());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
                
    }

    public void mostrarPlanillaMensual(int trabajadorId, java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        Connection connection = null;
        
        DefaultTableModel modeloMensual = new DefaultTableModel();
        modeloMensual.addColumn("ID Trabajador");
        modeloMensual.addColumn("Fecha");
        modeloMensual.addColumn("Horas Trabajadas");
        modeloMensual.addColumn("Horas Extras");
        modeloMensual.addColumn("Sueldo Bruto");
        modeloMensual.addColumn("Sueldo Neto");

        String sqlMensual = "SELECT t.idWorker, DATE(hi.fInicio) AS fecha, "
                + "SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) AS horasTrabajadas, "
                + "SUM(CASE WHEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 > 8 "
                + "THEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 - 8 ELSE 0 END) AS horasExtras, "
                + "(SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) * p.pagoPorHora) AS sueldoBruto, "
                + "(SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0) * p.pagoPorHora) * 0.7 AS sueldoNeto "
                + "FROM horas_ingreso hi "
                + "JOIN horas_salida hs ON hi.id_ingreso = hs.id_ingreso "
                + "JOIN worker t ON hi.trabajador_id = t.idWorker "
                + "JOIN puestodetrabajo p ON t.idWorker = p.idTrabajador "
                + "WHERE hi.trabajador_id = ? "
                + "AND hi.fInicio BETWEEN ? AND ? "
                + "GROUP BY t.idWorker, DATE(hi.fInicio) "
                + "ORDER BY DATE(hi.fInicio) ASC";

        try{
        Conectar.getInstancia().obtenerConexion();        
        
        try (PreparedStatement pst = connection.prepareStatement(sqlMensual)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, fechaInicio);
            pst.setDate(3, fechaFin);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                modeloMensual.addRow(new Object[]{
                    rs.getInt("idWorker"),
                    rs.getDate("fecha"),
                    rs.getDouble("horasTrabajadas"),
                    rs.getDouble("horasExtras"),
                    rs.getDouble("sueldoBruto"),
                    rs.getDouble("sueldoNeto")
                });
            }
        }
            tablaPlanillaMensual.setModel(modeloMensual);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar la planilla mensual: " + e.getMessage());
        }finally{
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

}
