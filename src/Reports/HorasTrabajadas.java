package Reports;

import Administration.PuestoDeTrabajo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import conectar.Conectar;
import java.awt.HeadlessException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class HorasTrabajadas extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(HorasTrabajadas.class.getName());
    private Conectar conexion = new Conectar();
    private Connection conect = conexion.getConexion();
    private PuestoDeTrabajo puestoDeTrabajo; // Instancia de PuestoDeTrabajo

    public HorasTrabajadas(PuestoDeTrabajo puestoDeTrabajo) {
        this.puestoDeTrabajo = puestoDeTrabajo;
        this.conexion = new Conectar();
        this.conect = conexion.getConexion();
    }

    public HorasTrabajadas() {
        initComponents();
        txtIdHoras.setEnabled(false);
        txtIdTrabajador.setVisible(false);
        AutoCompleteDecorator.decorate(cbxTrabajador);
        MostrarTrabajador(cbxTrabajador);
        int trabajadorId = 0;

        this.conect = conexion.getConexion();  // Asegurando la inicialización de la conexión
    }

    // Constructor que inicializa la conexión a la base de datos
    public HorasTrabajadas(Connection connection) {
        this.conect = connection;
    }

    public void Mostrar(JTable Tabla, int trabajadorId) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Ingreso");
        modelo.addColumn("ID Salida");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora de Ingreso");
        modelo.addColumn("Hora de Salida");
        modelo.addColumn("Horas Trabajadas");

        Tabla.setModel(modelo);

        String sql = "SELECT hi.id_ingreso AS idIngreso, "
                + "(SELECT hs.id_salida FROM horas_salida hs "
                + " WHERE hs.trabajador_id = hi.trabajador_id "
                + " AND hs.fSalida > hi.fInicio "
                + " ORDER BY hs.fSalida ASC LIMIT 1) AS idSalida, "
                + "hi.fInicio, "
                + "(SELECT hs.fSalida FROM horas_salida hs "
                + " WHERE hs.trabajador_id = hi.trabajador_id "
                + " AND hs.fSalida > hi.fInicio "
                + " ORDER BY hs.fSalida ASC LIMIT 1) AS fSalida, "
                + "TIMESTAMPDIFF(MINUTE, hi.fInicio, "
                + "(SELECT hs.fSalida FROM horas_salida hs "
                + " WHERE hs.trabajador_id = hi.trabajador_id "
                + " AND hs.fSalida > hi.fInicio "
                + " ORDER BY hs.fSalida ASC LIMIT 1)) / 60.0 AS horasTrabajadas "
                + "FROM horas_ingreso hi "
                + "WHERE hi.trabajador_id = ? "
                + "ORDER BY hi.fInicio ASC";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String idIngreso = rs.getString("idIngreso");
                String idSalida = rs.getString("idSalida");
                String fecha = rs.getTimestamp("fInicio").toLocalDateTime().toLocalDate().toString();
                String horaIngreso = rs.getTimestamp("fInicio").toLocalDateTime().toLocalTime().toString();
                String horaSalida = (rs.getTimestamp("fSalida") != null) ? rs.getTimestamp("fSalida").toLocalDateTime().toLocalTime().toString() : "Pendiente";
                String horasTrabajadas = (rs.getString("horasTrabajadas") != null) ? rs.getString("horasTrabajadas") : "0";

                modelo.addRow(new Object[]{idIngreso, idSalida, fecha, horaIngreso, horaSalida, horasTrabajadas});
            }

            Tabla.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar los registros, error: " + e.toString());
        }
    }

    public void Seleccionar(JTable Tabla, JTextField codigoIngreso, JTextField codigoSalida, JTextField Fecha, JTextField HoraIngreso, JTextField HoraSalida, JTextField HorasTrabajadas) {
        try {
            int fila = Tabla.getSelectedRow();
            if (fila >= 0) {
                codigoIngreso.setText(Tabla.getValueAt(fila, 0).toString());
                codigoSalida.setText(Tabla.getValueAt(fila, 1).toString());
                Fecha.setText(Tabla.getValueAt(fila, 2).toString());
                HoraIngreso.setText(Tabla.getValueAt(fila, 3).toString());
                HoraSalida.setText(Tabla.getValueAt(fila, 4).toString());
                HorasTrabajadas.setText(Tabla.getValueAt(fila, 5).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error de Selección, Error: " + e.toString());
        }
    }

    public void eliminar(int idIngreso, int idSalida) {
        // Eliminar registro de horas_ingreso
        String sqlDeleteHorasIngreso = "DELETE FROM horas_ingreso WHERE id_ingreso = ?";
        try (PreparedStatement pst = conect.prepareStatement(sqlDeleteHorasIngreso)) {
            pst.setInt(1, idIngreso);
            pst.executeUpdate();
            System.out.println("Registro de horas_ingreso eliminado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar el registro de horas_ingreso: " + e.getMessage());
        }

        // Eliminar registro de horas_salida
        String sqlDeleteHorasSalida = "DELETE FROM horas_salida WHERE id_salida = ?";
        try (PreparedStatement pst = conect.prepareStatement(sqlDeleteHorasSalida)) {
            pst.setInt(1, idSalida);
            pst.executeUpdate();
            System.out.println("Registro de horas_salida eliminado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar el registro de horas_salida: " + e.getMessage());
        }
    }

    public void modificar(int idIngreso, int idSalida, LocalDateTime nuevaHoraIngreso, LocalDateTime nuevaHoraSalida) throws SQLException {
    boolean autoCommitOriginal = conect.getAutoCommit();

    try {
        conect.setAutoCommit(false);  // Desactivar autocommit

        // Modificar registro de horas_ingreso
        String sqlUpdateHorasIngreso = "UPDATE horas_ingreso SET fInicio = ? WHERE id_ingreso = ?";
        try (PreparedStatement pst = conect.prepareStatement(sqlUpdateHorasIngreso)) {
            pst.setTimestamp(1, Timestamp.valueOf(nuevaHoraIngreso));
            pst.setInt(2, idIngreso);
            int affectedRowsIngreso = pst.executeUpdate();
            System.out.println("Hora de ingreso actualizada con éxito en horas_ingreso. Filas afectadas: " + affectedRowsIngreso);
        }

        // Modificar registro de horas_salida
        if (idSalida > 0) {
            String sqlUpdateHorasSalida = "UPDATE horas_salida SET fSalida = ? WHERE id_salida = ?";
            try (PreparedStatement pst = conect.prepareStatement(sqlUpdateHorasSalida)) {
                pst.setTimestamp(1, Timestamp.valueOf(nuevaHoraSalida));
                pst.setInt(2, idSalida);
                int affectedRowsSalida = pst.executeUpdate();
                System.out.println("Hora de salida actualizada con éxito en horas_salida. Filas afectadas: " + affectedRowsSalida);
            }
        }

        conect.commit();  // Confirmar la transacción
        System.out.println("Transacción realizada con éxito.");
    } catch (SQLException e) {
        conect.rollback();  // Revertir la transacción si ocurre un error
        LOGGER.log(Level.SEVERE, "Error al realizar la transacción: ", e);
        e.printStackTrace();
        System.out.println("Error al modificar las horas: " + e.getMessage());
    } finally {
        conect.setAutoCommit(autoCommitOriginal);  // Restaurar el estado original de autocommit
    }
}


    // Método para registrar el inicio de sesión
    public void registrarInicioSesion(int trabajadorId) {
        String sql = "INSERT INTO horas_ingreso (trabajador_id, fInicio) VALUES (?, ?)";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pst.executeUpdate();
            System.out.println("Inicio de sesión registrado para trabajador ID: " + trabajadorId);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar inicio de sesión: ", ex);
            ex.printStackTrace();
        }
    }

    // Método para registrar el fin de sesión y calcular las horas de trabajo
    public void registrarFinSesion(int trabajadorId) {
        System.out.println("Registrando fin de sesión para trabajador ID: " + trabajadorId); // Mensaje de depuración
        String sql = "INSERT INTO horas_salida (trabajador_id, fSalida) VALUES (?, ?)";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            int rowsAffected = pst.executeUpdate();
            System.out.println("Filas afectadas: " + rowsAffected); // Mensaje de depuración
            if (rowsAffected > 0) {
                System.out.println("Fin de sesión registrado para trabajador ID: " + trabajadorId);
            } else {
                System.out.println("No se pudo registrar el fin de sesión para trabajador ID: " + trabajadorId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar fin de sesión: ", ex);
        }
    }

    // Método para calcular las horas trabajadas después de registrar la salida
    private double calcularHorasTrabajadasPorDia(int trabajadorId) {
        String sql = "SELECT TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) AS minutosTrabajados "
                + "FROM horas_ingreso hi "
                + "JOIN horas_salida hs ON hi.trabajador_id = hs.trabajador_id "
                + "WHERE hi.trabajador_id = ? AND DATE(hi.fInicio) = CURDATE()";
        double horasTrabajadas = 0.0;

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int minutosTrabajados = rs.getInt("minutosTrabajados");
                horasTrabajadas = minutosTrabajados / 60.0;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al calcular horas trabajadas por día: ", ex);
        }
        return horasTrabajadas;
    }

// Método para guardar las horas trabajadas en la base de datos
    private void guardarHorasTrabajadas(int trabajadorId, double horasTrabajadas) {
        String sql = "INSERT INTO horas_trabajadas (trabajador_id, horas, fecha) VALUES (?, ?, ?)";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDouble(2, horasTrabajadas);
            pst.setDate(3, java.sql.Date.valueOf(LocalDate.now())); // Guardar con la fecha actual
            pst.executeUpdate();
            System.out.println("Horas trabajadas registradas para trabajador ID: " + trabajadorId);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al guardar horas trabajadas: ", ex);
        }
    }

    public void calcularPagos(int trabajadorId) {
        int idPDT = puestoDeTrabajo.obtenerIdPuestoActivo(trabajadorId);
        String periodoPago = puestoDeTrabajo.obtenerPeriodoPago(idPDT);
        boolean tieneOvertime = puestoDeTrabajo.verificarOvertime(idPDT);
        double pagoPorHora = puestoDeTrabajo.obtenerPagoPorHora(idPDT);
        double sueldo = puestoDeTrabajo.obtenerSueldo(idPDT);
        double totalHorasTrabajadas = calcularHorasTrabajadasPorPeriodo(periodoPago, trabajadorId);
        double totalHorasExtras = 0;

        if (tieneOvertime && totalHorasTrabajadas > 40) {
            totalHorasExtras = totalHorasTrabajadas - 40;
            totalHorasTrabajadas = 40;
        }

        double salarioBruto;
        if (pagoPorHora > 0) {
            salarioBruto = (totalHorasTrabajadas * pagoPorHora) + (totalHorasExtras * pagoPorHora * 1.5);
        } else {
            salarioBruto = sueldo + (totalHorasExtras * pagoPorHora * 1.5);
        }

        double salarioNeto = calcularSalarioNeto(salarioBruto, trabajadorId);
        guardarPagos(trabajadorId, salarioBruto, salarioNeto);
    }

    private double calcularHorasTrabajadasPorPeriodo(String periodo, int trabajadorId) {
        String sql = "";

        switch (periodo) {
            case "Semanal":
                sql = "SELECT SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida)) / 60.0 AS totalHoras "
                        + "FROM horas_ingreso hi "
                        + "JOIN horas_salida hs ON hi.trabajador_id = hs.trabajador_id "
                        + "WHERE hi.trabajador_id = ? AND WEEK(hi.fInicio) = WEEK(CURDATE())";
                break;
            case "Quincenal":
                sql = "SELECT SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida)) / 60.0 AS totalHoras "
                        + "FROM horas_ingreso hi "
                        + "JOIN horas_salida hs ON hi.trabajador_id = hs.trabajador_id "
                        + "WHERE hi.trabajador_id = ? AND hi.fInicio >= CURDATE() - INTERVAL 14 DAY";
                break;
            case "Mensual":
                sql = "SELECT SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida)) / 60.0 AS totalHoras "
                        + "FROM horas_ingreso hi "
                        + "JOIN horas_salida hs ON hi.trabajador_id = hs.trabajador_id "
                        + "WHERE hi.trabajador_id = ? AND MONTH(hi.fInicio) = MONTH(CURDATE())";
                break;
        }

        double totalHoras = 0.0;
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                totalHoras = rs.getDouble("totalHoras");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al calcular horas trabajadas por periodo: ", ex);
        }
        return totalHoras;
    }

    private double calcularHorasTrabajadas(String sql, int trabajadorId) {
        double totalHoras = 0;
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                totalHoras = rs.getDouble("totalHoras");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al calcular horas trabajadas: ", ex);
        }
        return totalHoras;
    }

    private void guardarPagos(int trabajadorId, double salarioBruto, double salarioNeto) {
        String sql = "INSERT INTO pagos (trabajador_id, salario_bruto, salario_neto, fecha_pago) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDouble(2, salarioBruto);
            pst.setDouble(3, salarioNeto);
            pst.setDate(4, java.sql.Date.valueOf(LocalDate.now())); // Fecha del pago
            pst.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al guardar pagos: ", ex);
        }
    }

    private double calcularSalarioBruto(double totalHorasTrabajadas, double totalHorasExtras, double pagoPorHora) {
        return (totalHorasTrabajadas * pagoPorHora) + (totalHorasExtras * pagoPorHora * 1.5);
    }

    private double calcularSalarioNeto(double salarioBruto, int trabajadorId) {
        // Aquí puedes añadir deducciones como impuestos, seguridad social, etc.
        double impuestoFederal = salarioBruto * obtenerTasaImpuestoFederal(trabajadorId);
        double seguridadSocial = salarioBruto * 0.062;
        double medicare = salarioBruto * 0.0145;
        double impuestoEstatal = salarioBruto * obtenerTasaImpuestoEstatal(trabajadorId);
        double impuestoLocal = salarioBruto * obtenerTasaImpuestoLocal(trabajadorId);
        double otrasDeducciones = obtenerOtrasDeducciones(trabajadorId);

        return salarioBruto - impuestoFederal - seguridadSocial - medicare - impuestoEstatal - impuestoLocal - otrasDeducciones;
    }

    public void generarReporte(int trabajadorId, java.util.Date fechaInicio, java.util.Date fechaFin) {
        String sql = "SELECT p.fecha_pago, p.salario_bruto, p.salario_neto, h.horas, h.horas_extras, v.descripcion AS vacaciones "
                + "FROM pagos p "
                + "JOIN horas_trabajadas h ON p.trabajador_id = h.trabajador_id "
                + "JOIN puestodetrabajo pt ON p.trabajador_id = pt.idTrabajador "
                + "LEFT JOIN vacaciones v ON pt.id_vacaciones = v.id "
                + "WHERE p.trabajador_id = ? AND p.fecha_pago BETWEEN ? AND ?";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, new java.sql.Date(fechaInicio.getTime()));
            pst.setDate(3, new java.sql.Date(fechaFin.getTime()));
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) Tabla.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getDate("fecha_pago"),
                    rs.getDouble("salario_bruto"),
                    rs.getDouble("salario_neto"),
                    rs.getDouble("horas"),
                    rs.getDouble("horas_extras"),
                    rs.getString("vacaciones")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage());
        }
    }

    //REVISAR
    private double obtenerPagoPorHora(int trabajadorId) {
        String sql = "SELECT pagoPorHora FROM puestodetrabajo WHERE idTrabajador = ?";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("precioPorHora");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener pago por hora: ", ex);
        }
        return 0.0;
    }

    private String obtenerNombreTrabajador(int trabajadorId) {
        String sql = "SELECT nombre FROM worker WHERE idWorker = ?";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener nombre del trabajador: ", ex);
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        txtIdHoras = new javax.swing.JTextField();
        btnHistorialCompra = new javax.swing.JButton();
        txtIdTrabajador = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbxTrabajador = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        btnMostrarDatos = new javax.swing.JButton();
        txtIdIngreso = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        txtTiempoTrabajado = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtHingreso = new javax.swing.JTextField();
        txtHSalida = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        txtIdSalida = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Horas Trabajadas");

        jPanel12.setBackground(new java.awt.Color(0, 204, 153));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        Tabla.setRowHeight(23);
        Tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(Tabla);

        jPanel12.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 1130, 420));
        jPanel12.add(txtIdHoras, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 60, 30));

        btnHistorialCompra.setText("Generar Reporte");
        btnHistorialCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialCompraActionPerformed(evt);
            }
        });
        jPanel12.add(btnHistorialCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 90, 120, 40));
        jPanel12.add(txtIdTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 70, 30));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 20, -1, -1));
        jPanel12.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 50, 140, -1));
        jPanel12.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 50, 120, -1));

        jLabel1.setText("Fecha de Inicio de Pagos");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 30, -1, -1));

        jLabel2.setText("Final");
        jPanel12.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 30, -1, -1));

        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });
        cbxTrabajador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTrabajadorActionPerformed(evt);
            }
        });
        jPanel12.add(cbxTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 190, 30));

        jLabel3.setText("Trabajador");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, -1, -1));

        btnMostrarDatos.setText("Mostrar Datos / Refresh");
        btnMostrarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarDatosActionPerformed(evt);
            }
        });
        jPanel12.add(btnMostrarDatos, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, -1, -1));
        jPanel12.add(txtIdIngreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 100, -1));

        jLabel4.setText("id Ingreso");
        jPanel12.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, -1));
        jPanel12.add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 100, 100, -1));
        jPanel12.add(txtTiempoTrabajado, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 100, 100, -1));

        jLabel5.setText("Fecha");
        jPanel12.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, -1, -1));

        jLabel6.setText("Horas Trabajadas");
        jPanel12.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 100, -1, -1));

        jLabel7.setText("Hora de Ingreso");
        jPanel12.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, -1, -1));

        jLabel8.setText("Hora de Salida");
        jPanel12.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 60, -1, -1));
        jPanel12.add(txtHingreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, 90, -1));
        jPanel12.add(txtHSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 60, 90, -1));

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel12.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 100, -1, -1));

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel12.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 100, -1, -1));
        jPanel12.add(txtIdSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 100, -1));

        jLabel9.setText("id Salida");
        jPanel12.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 1190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void cbxTrabajadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTrabajadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTrabajadorActionPerformed

    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged

    private void btnHistorialCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialCompraActionPerformed
        int trabajadorId = Integer.parseInt(txtIdTrabajador.getText());
        java.util.Date fechaInicio = jDateChooser1.getDate();
        java.util.Date fechaFin = jDateChooser2.getDate();
        generarReporte(trabajadorId, fechaInicio, fechaFin);
    }//GEN-LAST:event_btnHistorialCompraActionPerformed

    private void btnMostrarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarDatosActionPerformed
        // Obtener el ID del trabajador desde el JTextField txtIdTrabajador antes de llamar al método Mostrar
        int trabajadorId = Integer.parseInt(txtIdTrabajador.getText());
        Mostrar(Tabla, trabajadorId);
    }//GEN-LAST:event_btnMostrarDatosActionPerformed

    private void TablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaMouseClicked
        Seleccionar(Tabla, txtIdIngreso, txtIdSalida, txtFecha, txtHingreso, txtHSalida, txtTiempoTrabajado);
    }//GEN-LAST:event_TablaMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        Seleccionar(Tabla, txtIdIngreso, txtIdSalida, txtFecha, txtHingreso, txtHSalida, txtTiempoTrabajado);
        int idIngreso = Integer.parseInt(txtIdIngreso.getText());
        int idSalida = Integer.parseInt(txtIdSalida.getText());
        eliminar(idIngreso, idSalida);
        txtFecha.setText("");
        txtHSalida.setText("");
        txtHingreso.setText("");
        txtIdIngreso.setText("");
        txtIdSalida.setText("");
        txtTiempoTrabajado.setText("");

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        try {
            int idIngreso = Integer.parseInt(txtIdIngreso.getText());
            int idSalida = Integer.parseInt(txtIdSalida.getText());

            // Obtener la fecha del JTextField Fecha si es requerido en formato LocalDate
            LocalDate fecha = LocalDate.parse(txtFecha.getText());

            // Obtener la hora de ingreso y de salida del JTextField y convertirlos a enteros
            int horaIngreso = Integer.parseInt(txtHingreso.getText().split(":")[0]);
            int minutoIngreso = Integer.parseInt(txtHingreso.getText().split(":")[1]);

            int horaSalida = Integer.parseInt(txtHSalida.getText().split(":")[0]);
            int minutoSalida = Integer.parseInt(txtHSalida.getText().split(":")[1]);

            // Construir objetos LocalDateTime con los valores obtenidos
            LocalDateTime nuevaHoraIngreso = LocalDateTime.of(fecha, LocalTime.of(horaIngreso, minutoIngreso));
            LocalDateTime nuevaHoraSalida = LocalDateTime.of(fecha, LocalTime.of(horaSalida, minutoSalida));

            modificar(idIngreso, idSalida, nuevaHoraIngreso, nuevaHoraSalida);

            System.out.println("ID Ingreso: " + idIngreso);
            System.out.println("ID Salida: " + idSalida);
            System.out.println("Fecha: " + fecha);
            System.out.println("Hora de Ingreso: " + nuevaHoraIngreso);
            System.out.println("Hora de Salida: " + nuevaHoraSalida);
        } catch (NumberFormatException | DateTimeParseException e) {
            System.out.println("Error al convertir texto a enteros o Fecha/Hora incorrecta: " + e.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(HorasTrabajadas.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Limpiar campos de texto después de la operación
        txtFecha.setText("");
        txtHSalida.setText("");
        txtHingreso.setText("");
        txtIdIngreso.setText("");
        txtIdSalida.setText("");
        txtTiempoTrabajado.setText("");
    }//GEN-LAST:event_btnModificarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable Tabla;
    private javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnHistorialCompra;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnMostrarDatos;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtHSalida;
    private javax.swing.JTextField txtHingreso;
    public javax.swing.JTextField txtIdHoras;
    private javax.swing.JTextField txtIdIngreso;
    private javax.swing.JTextField txtIdSalida;
    public javax.swing.JTextField txtIdTrabajador;
    private javax.swing.JTextField txtTiempoTrabajado;
    // End of variables declaration//GEN-END:variables

    public void MostrarTrabajador(JComboBox comboTrabajador) {
        String sql = "SELECT * FROM worker";
        try (Statement st = conect.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            comboTrabajador.removeAllItems();
            while (rs.next()) {
                comboTrabajador.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Trabajadores: " + e.toString());
        }
    }

    public void MostrarCodigoTrabajador(JComboBox trabajador, JTextField IdTrabajador) {

        String consuta = "select worker.idWorker from worker where worker.nombre=?";

        try {
            CallableStatement cs = conect.prepareCall(consuta);
            cs.setString(1, trabajador.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                IdTrabajador.setText(rs.getString("idWorker"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

    //CALCULOS PARA PAYROLL IMPOSIBLE DE HACER POR AHORA
    public void calcularDeducciones(int trabajadorId) {
        String sql = "SELECT horas, horas_extras FROM horas_trabajadas WHERE trabajador_id = ? AND fecha = ?";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                double horasTrabajadas = rs.getDouble("horas");
                double horasExtras = rs.getDouble("horas_extras");

                String sqlTrabajador = "SELECT pagoPorHora FROM puestodetrabajo WHERE idTrabajador = ?";
                try (PreparedStatement pstTrabajador = conect.prepareStatement(sqlTrabajador)) {
                    pstTrabajador.setInt(1, trabajadorId);
                    ResultSet rsTrabajador = pstTrabajador.executeQuery();
                    if (rsTrabajador.next()) {
                        double pagoHora = rsTrabajador.getDouble("pagoPorHora");
                        double salarioBruto = horasTrabajadas * pagoHora + horasExtras * pagoHora * 1.5;

                        double impuestoFederal = salarioBruto * obtenerTasaImpuestoFederal(trabajadorId);
                        double seguridadSocial = salarioBruto * 0.062;
                        double medicare = salarioBruto * 0.0145;
                        double impuestoEstatal = salarioBruto * obtenerTasaImpuestoEstatal(trabajadorId);
                        double impuestoLocal = salarioBruto * obtenerTasaImpuestoLocal(trabajadorId);
                        double otrasDeducciones = obtenerOtrasDeducciones(trabajadorId);
                        double salarioNeto = salarioBruto - impuestoFederal - seguridadSocial - medicare - impuestoEstatal - impuestoLocal - otrasDeducciones;

                        guardarDeducciones(trabajadorId, salarioBruto, impuestoFederal, seguridadSocial, medicare, impuestoEstatal, impuestoLocal, otrasDeducciones, salarioNeto);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(HorasTrabajadas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private double obtenerTasaImpuestoFederal(int trabajadorId) {
        // Implementar lógica para obtener la tasa de impuesto federal basada en el formulario W-4 del trabajador
        return 0.1; // Ejemplo, reemplazar con lógica real
    }

    private double obtenerTasaImpuestoEstatal(int trabajadorId) {
        // Implementar lógica para obtener la tasa de impuesto estatal
        return 0.05; // Ejemplo, reemplazar con lógica real
    }

    private double obtenerTasaImpuestoLocal(int trabajadorId) {
        // Implementar lógica para obtener la tasa de impuesto local
        return 0.01; // Ejemplo, reemplazar con lógica real
    }

    private double obtenerOtrasDeducciones(int trabajadorId) {
        // Implementar lógica para obtener otras deducciones
        return 50.0; // Ejemplo, reemplazar con lógica real
    }

    private void guardarDeducciones(int trabajadorId, double salarioBruto, double impuestoFederal, double seguridadSocial, double medicare, double impuestoEstatal, double impuestoLocal, double otrasDeducciones, double salarioNeto) {
        String sql = "INSERT INTO deducciones (trabajador_id, fecha, salario_bruto, impuesto_federal, seguridad_social, medicare, impuesto_estatal, impuesto_local, otras_deducciones, salario_neto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.setDouble(3, salarioBruto);
            pst.setDouble(4, impuestoFederal);
            pst.setDouble(5, seguridadSocial);
            pst.setDouble(6, medicare);
            pst.setDouble(7, impuestoEstatal);
            pst.setDouble(8, impuestoLocal);
            pst.setDouble(9, otrasDeducciones);
            pst.setDouble(10, salarioNeto);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(HorasTrabajadas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Implementar métodos para generar el reporte
    public void generarReporte() {
        String sql = "SELECT * FROM deducciones WHERE trabajador_id = ? AND fecha BETWEEN ? AND ?";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
//        pst.setInt(1, obtenerIdTrabajadorSeleccionado());
            pst.setDate(2, new java.sql.Date(jDateChooser1.getDate().getTime()));
            pst.setDate(3, new java.sql.Date(jDateChooser2.getDate().getTime()));
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) Tabla.getModel();
            model.setRowCount(0); // Clear existing data

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    //                obtenerNombreTrabajador(rs.getInt("trabajador_id")),
                    rs.getDouble("salario_bruto"),
                    rs.getDouble("impuesto_federal"),
                    rs.getDouble("seguridad_social"),
                    rs.getDouble("medicare"),
                    rs.getDouble("impuesto_estatal"),
                    rs.getDouble("impuesto_local"),
                    rs.getDouble("otras_deducciones"),
                    rs.getDouble("salario_neto")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage());
        }
    }

}
