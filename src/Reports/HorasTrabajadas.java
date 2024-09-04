package Reports;

import Administration.PuestoDeTrabajo;
import Bases.PeriodoPago;
import Bases.Servidor;
import com.toedter.calendar.JDateChooser;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.time.format.DateTimeParseException;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HorasTrabajadas extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(HorasTrabajadas.class.getName());
    private Conectar conexion;
    private Connection conect;
    public PuestoDeTrabajo puestoDeTrabajo; // Instancia de PuestoDeTrabajo
//    private static LocalDate fechaInicioCobros;
    private LocalDate fechaInicioActividades;

    public HorasTrabajadas(PuestoDeTrabajo puestoDeTrabajo) {
        this.puestoDeTrabajo = puestoDeTrabajo;
        this.conexion = new Conectar();
        this.conect = conexion.getConexion();
        this.fechaInicioActividades = obtenerFechaInicioActividadesDesdeBD(); // Asegúrate de que `conect` esté inicializado antes de esta llamada.
    }

    public HorasTrabajadas() {
        initComponents();
        this.puestoDeTrabajo = new PuestoDeTrabajo();
        this.conexion = new Conectar();
        this.conect = conexion.getConexion(); // Asegurando la inicialización de la conexión antes de cualquier uso

        txtIdHoras.setEnabled(false);
        txtIdTrabajador.setVisible(false);

        // Supongamos que recuperas la fecha desde la base de datos
        this.fechaInicioActividades = obtenerFechaInicioActividadesDesdeBD(); // Aquí también asegura que `conect` esté inicializado.

//        // Inicializa dateSemanlQuincenal y dateMensual si no se hace en initComponents()
//        if (dateSemanlQuincenal == null) {
//            dateSemanlQuincenal = new JDateChooser(); // Asegura la inicialización
//        }
//        if (dateMensual == null) {
//            dateMensual = new JDateChooser(); // Asegura la inicialización
//        }
        AutoCompleteDecorator.decorate(cbxTrabajador);
        MostrarTrabajador(cbxTrabajador);
        Mostrar(Tabla, 0); // Mostrar inicialmente con ID 0 o ajusta según sea necesario

        cbxTrabajador.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED && evt.getItem() != null) {
                    MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);
                }
            }
        });
    }

//    // Constructor que inicializa la conexión a la base de datos
//    public HorasTrabajadas(Connection connection) {
//        this.conect = connection;
//    }
    private LocalDate obtenerFechaInicioActividadesDesdeBD() {
        if (conect == null) {
            JOptionPane.showMessageDialog(null, "Conexión a la base de datos no establecida.");
            return LocalDate.now(); // Retornar la fecha actual como fallback
        }
        String sql = "SELECT fecha_inicio_actividades FROM configuracion"; // Ajusta el nombre de la tabla y la columna según tu base de datos

        try (PreparedStatement pst = conect.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getDate("fecha_inicio_actividades").toLocalDate(); // Convierte java.sql.Date a LocalDate
            } else {
                throw new SQLException("No se encontró la fecha de inicio de actividades en la tabla de configuración.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener la fecha de inicio de actividades: " + e.getMessage());
            return LocalDate.now(); // Retorna la fecha actual como fallback en caso de error
        }
    }

    public LocalDate calcularInicioPeriodo(int numeroPeriodo, String tipoPeriodo) {
        switch (tipoPeriodo) {
            case "Semanal":
                return fechaInicioActividades.plusWeeks(numeroPeriodo - 1);
            case "Quincenal":
                return fechaInicioActividades.plusDays((numeroPeriodo - 1) * 14);
            case "Mensual":
                return fechaInicioActividades.plusMonths(numeroPeriodo - 1).withDayOfMonth(1);
            default:
                throw new IllegalArgumentException("Tipo de periodo de pago desconocido: " + tipoPeriodo);
        }
    }

    public double calcularHorasTrabajadasPorPeriodo(String periodoPago, int trabajadorId, int numeroPeriodo) {
        LocalDate fechaInicioPeriodo = calcularInicioPeriodo(numeroPeriodo, periodoPago);
        LocalDate fechaFinPeriodo = obtenerFechaFinPeriodo(fechaInicioPeriodo, periodoPago);
        return calcularHorasTrabajadas(trabajadorId, fechaInicioPeriodo, fechaFinPeriodo);
    }

    private LocalDate obtenerFechaFinPeriodo(LocalDate fechaInicio, String tipoPeriodo) {
        switch (tipoPeriodo) {
            case "Semanal":
                return fechaInicio.plusDays(6); // Fin de la semana
            case "Quincenal":
                return fechaInicio.plusDays(13); // Fin de la quincena
            case "Mensual":
                return fechaInicio.with(TemporalAdjusters.lastDayOfMonth()); // Fin del mes
            default:
                throw new IllegalArgumentException("Tipo de período desconocido: " + tipoPeriodo);
        }
    }

    private double calcularHorasTrabajadas(int trabajadorId, LocalDate fechaInicio, LocalDate fechaFin) {
        double totalHorasTrabajadas = 0.0;
        String sql = "SELECT SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida)) / 60.0 AS horasTrabajadas "
                + "FROM horas_ingreso hi "
                + "JOIN horas_salida hs ON hi.trabajador_id = hs.trabajador_id "
                + "WHERE hi.trabajador_id = ? AND hi.fInicio BETWEEN ? AND ?";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, java.sql.Date.valueOf(fechaInicio));
            pst.setDate(3, java.sql.Date.valueOf(fechaFin));
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                totalHorasTrabajadas = rs.getDouble("horasTrabajadas");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Formatear a dos decimales
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.valueOf(df.format(totalHorasTrabajadas));
    }

    //Muestra los Datos en la Tabla
    public void Mostrar(JTable Tabla, int trabajadorId) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Ingreso");
        modelo.addColumn("ID Salida");
        modelo.addColumn("Fecha Ingreso");
        modelo.addColumn("Fecha Salida");
        modelo.addColumn("Hora de Ingreso");
        modelo.addColumn("Hora de Salida");
        modelo.addColumn("Horas Trabajadas");

        Tabla.setModel(modelo);

        String sql = "SELECT hi.id_ingreso AS idIngreso, hs.id_salida AS idSalida, hi.fInicio, hs.fSalida, "
                + "TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 AS horasTrabajadas "
                + "FROM horas_ingreso hi "
                + "JOIN horas_salida hs ON hi.trabajador_id = hs.trabajador_id AND hs.fSalida > hi.fInicio "
                + "AND hs.fSalida = (SELECT MIN(hs2.fSalida) FROM horas_salida hs2 WHERE hs2.trabajador_id = hi.trabajador_id AND hs2.fSalida > hi.fInicio) "
                + "WHERE hi.trabajador_id = ? ORDER BY hi.fInicio ASC;";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);  // Ahora este parámetro es válido
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String idIngreso = rs.getString("idIngreso");
                String idSalida = rs.getString("idSalida");
                String fechaIngreso = rs.getTimestamp("fInicio").toLocalDateTime().toLocalDate().toString();
                String fechaSalida = (rs.getTimestamp("fSalida") != null) ? rs.getTimestamp("fSalida").toLocalDateTime().toLocalDate().toString() : "Pendiente";
                String horaIngreso = rs.getTimestamp("fInicio").toLocalDateTime().toLocalTime().toString();
                String horaSalida = (rs.getTimestamp("fSalida") != null) ? rs.getTimestamp("fSalida").toLocalDateTime().toLocalTime().toString() : "Pendiente";
                String horasTrabajadas = (rs.getString("horasTrabajadas") != null) ? rs.getString("horasTrabajadas") : "0";

                modelo.addRow(new Object[]{idIngreso, idSalida, fechaIngreso, fechaSalida, horaIngreso, horaSalida, horasTrabajadas});
            }

            Tabla.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar los registros, error: " + e.toString());
        }
    }

    public void seleccionar(JTable tabla) {
        // Obtener el índice de la fila seleccionada
        int filaSeleccionada = tabla.getSelectedRow();

        if (filaSeleccionada != -1) { // Verificar si se ha seleccionado alguna fila
            // Obtener los valores de las columnas de la fila seleccionada
            Object valueIdIngreso = tabla.getValueAt(filaSeleccionada, 0);
            Object valueIdSalida = tabla.getValueAt(filaSeleccionada, 1);
            Object valueFechaIngreso = tabla.getValueAt(filaSeleccionada, 2);
            Object valueFechaSalida = tabla.getValueAt(filaSeleccionada, 3);
            Object valueHoraIngreso = tabla.getValueAt(filaSeleccionada, 4);
            Object valueHoraSalida = tabla.getValueAt(filaSeleccionada, 5);
            Object valueHorasTrabajadas = tabla.getValueAt(filaSeleccionada, 6);

            String idIngreso = (valueIdIngreso != null) ? valueIdIngreso.toString() : "";
            String idSalida = (valueIdSalida != null) ? valueIdSalida.toString() : "";
            String fechaIngreso = (valueFechaIngreso != null) ? valueFechaIngreso.toString() : "";
            String fechaSalida = (valueFechaSalida != null) ? valueFechaSalida.toString() : "";
            String horaIngreso = (valueHoraIngreso != null) ? valueHoraIngreso.toString() : "";
            String horaSalida = (valueHoraSalida != null) ? valueHoraSalida.toString() : "";
            String horasTrabajadas = (valueHorasTrabajadas != null) ? valueHorasTrabajadas.toString() : "";

            // Aquí puedes mostrar estos valores en campos de texto (JTextFields) o usarlos para otros propósitos
            // Ejemplo:
            txtIdIngreso.setText(idIngreso);
            txtIdSalida.setText(idSalida);
            txtFechaIngreso.setText(fechaIngreso);
            txtFechaSalida.setText(fechaSalida);
            txtHoraIngreso.setText(horaIngreso);
            txtHoraSalida.setText(horaSalida);
            txtTiempoTrabajado.setText(horasTrabajadas);
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila.");
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

    public void guardarHorasTrabajadas(int trabajadorId, double horasTrabajadas, LocalDate fecha, String periodoPago) {
        // Asegúrate de que la fecha sea válida
        if (fecha == null || fecha.equals(LocalDate.EPOCH)) {
            fecha = LocalDate.now(); // Usa la fecha actual si la fecha es nula o no válida
        }

        String sql = "INSERT INTO horas_trabajadas (trabajador_id, horas, fecha, periodo_pago) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE horas = VALUES(horas), periodo_pago = VALUES(periodo_pago)";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDouble(2, horasTrabajadas);
            pst.setDate(3, java.sql.Date.valueOf(fecha));
            pst.setString(4, periodoPago); // Asegúrate de añadir el período de pago aquí
            pst.executeUpdate();
            System.out.println("Horas trabajadas registradas para trabajador ID: " + trabajadorId + " en la fecha: " + fecha);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al guardar horas trabajadas: ", ex);
        }
    }

    // Método para registrar el inicio de sesión
    public void registrarInicioSesion(int trabajadorId) {
        //        revisarEstadoSesion(trabajadorId); // Revisa el estado de la sesión en intervalos
        // Registrar el inicio de sesión en horas_ingreso
        String sql = "INSERT INTO horas_ingreso (trabajador_id, fInicio) VALUES (?, ?)";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pst.executeUpdate();
//            System.out.println("Inicio de sesión registrado para trabajador ID: " + trabajadorId);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar inicio de sesión: ", ex);
        }
    }

    // Método para registrar el fin de sesión y calcular las horas de trabajo
    public void registrarFinSesion(int trabajadorId) {
        //        System.out.println("Registrando fin de sesión para trabajador ID: " + trabajadorId);
        String obtenerIdIngresoSQL = "SELECT id_ingreso FROM horas_ingreso WHERE trabajador_id = ? ORDER BY fInicio DESC LIMIT 1";
        try (PreparedStatement pstIngreso = conect.prepareStatement(obtenerIdIngresoSQL)) {
            pstIngreso.setInt(1, trabajadorId);
            ResultSet rs = pstIngreso.executeQuery();

            if (rs.next()) {
                int idIngreso = rs.getInt("id_ingreso");

                String sql = "INSERT INTO horas_salida (trabajador_id, fSalida, id_ingreso) VALUES (?, ?, ?)";
                try (PreparedStatement pstSalida = conect.prepareStatement(sql)) {
                    pstSalida.setInt(1, trabajadorId);
                    pstSalida.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    pstSalida.setInt(3, idIngreso);

                    int rowsAffected = pstSalida.executeUpdate();
                    System.out.println("Filas afectadas: " + rowsAffected);

//                    if (rowsAffected > 0) {
//                        System.out.println("Fin de sesión registrado para trabajador ID: " + trabajadorId);
//                        double horasTrabajadas = calcularHorasTrabajadasPorDia(trabajadorId);
//                        guardarHorasTrabajadas(trabajadorId, horasTrabajadas, LocalDate.EPOCH);
//                        actualizarCalculosPago(trabajadorId); // Este método puede ser problemático si hay errores o bloqueos
//                    } else {
//                        System.out.println("No se pudo registrar el fin de sesión para trabajador ID: " + trabajadorId);
//                    }
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error al registrar fin de sesión: ", ex);
                }
            } else {
                System.out.println("No se encontró una sesión de inicio para el trabajador ID: " + trabajadorId);
                JOptionPane.showMessageDialog(null, "No se encontró una sesión activa para el trabajador con ID: " + trabajadorId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener id_ingreso: ", ex);
        }
    }
    
    public void calcularPagos(int trabajadorId) {
        int idPDT = puestoDeTrabajo.obtenerIdPuestoActivo(trabajadorId);
        String periodoPago = puestoDeTrabajo.obtenerPeriodoPago(idPDT);
        boolean tieneOvertime = puestoDeTrabajo.verificarOvertime(idPDT);
        //DECLARAR Y OBTENER EL VALOR DE pagoPorHora AQUÍ
        double pagoPorHora = puestoDeTrabajo.obtenerPagoPorHora(idPDT);  // Asegúrate de que este método exista y funcione correctamente
        double sueldo = puestoDeTrabajo.obtenerSueldo(idPDT);
        double totalHorasTrabajadas = calcularHorasTrabajadasPorPeriodo(periodoPago, trabajadorId, PROPERTIES);
        double totalHorasExtras = 0.0;

        if (tieneOvertime && totalHorasTrabajadas > 40) {
            totalHorasExtras = totalHorasTrabajadas - 40;
            totalHorasTrabajadas = 40;
        }

        double salarioBruto = pagoPorHora > 0 ? (totalHorasTrabajadas * pagoPorHora) + (totalHorasExtras * pagoPorHora * 1.5) : sueldo;
        double salarioNeto = calcularSalarioNeto(salarioBruto, trabajadorId);
        guardarPagos(trabajadorId, salarioBruto, salarioNeto);
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
    
    

    private void consolidarHorasTrabajadas(int trabajadorId, LocalDate fecha) {
        double totalHoras = 0.0;
        String sql = "SELECT SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida)) / 60.0 AS horasTrabajadas "
                + "FROM horas_ingreso hi "
                + "JOIN horas_salida hs ON hi.id_ingreso = hs.id_ingreso "
                + "WHERE hi.trabajador_id = ? AND DATE(hi.fInicio) = ?";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, java.sql.Date.valueOf(fecha));
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                totalHoras = rs.getDouble("horasTrabajadas");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        guardarHorasTrabajadas(trabajadorId, totalHoras, fecha, determinarPeriodoPago(fecha));
    }

    public String determinarPeriodoPago(LocalDate fechaTrabajo) {
        if (esSemanal(fechaTrabajo)) {
            return "Semanal";
        } else if (esQuincenal(fechaTrabajo)) {
            return "Quincenal";
        } else if (esMensual(fechaTrabajo)) {
            return "Mensual";
        } else {
            return "Otro"; // Ajusta a un valor corto si el valor desconocido es demasiado largo
        }
    }

    private boolean esSemanal(LocalDate fecha) {
        // Lógica para determinar si la fecha cae en un período semanal
        // Implementar según las reglas de tu empresa
        return false;
    }

    private boolean esQuincenal(LocalDate fecha) {
        // Lógica para determinar si la fecha cae en un período quincenal
        // Implementar según las reglas de tu empresa
        return false;
    }

    private boolean esMensual(LocalDate fecha) {
        // Lógica para determinar si la fecha cae en un período mensual
        // Implementar según las reglas de tu empresa
        return false;
    }

    // Método para calcular las horas trabajadas después de registrar la salida
    public double calcularHorasTrabajadasPorDia(int trabajadorId) {
        String sql = "SELECT DATE(hi.fInicio) as fecha, SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida)) / 60.0 AS horasTrabajadas "
                + "FROM horas_ingreso hi "
                + "JOIN horas_salida hs ON hi.id_ingreso = hs.id_ingreso "
                + "WHERE hi.trabajador_id = ? AND DATE(hi.fInicio) = CURDATE() "
                + "GROUP BY DATE(hi.fInicio)";
        double totalHorasTrabajadas = 0.0;

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                totalHorasTrabajadas = rs.getDouble("horasTrabajadas");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al calcular horas trabajadas por día: ", ex);
        }
        return totalHorasTrabajadas;
    }

    public void generarReporte(int trabajadorId, java.util.Date fechaInicio, java.util.Date fechaFin) {
        String sql = "SELECT * FROM deducciones WHERE trabajador_id = ? AND fecha BETWEEN ? AND ?";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            // Asegúrate de establecer todos los parámetros necesarios en la consulta SQL
            pst.setInt(1, trabajadorId); // Establece el parámetro para trabajador_id
            pst.setDate(2, new java.sql.Date(fechaInicio.getTime())); // Establece el parámetro para fecha de inicio
            pst.setDate(3, new java.sql.Date(fechaFin.getTime())); // Establece el parámetro para fecha final

            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) Tabla.getModel();
            model.setRowCount(0); // Limpiar datos existentes

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
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

    // Implementar métodos para generar el reporte
    public void generarReporte() {
        String sql = "SELECT * FROM deducciones WHERE trabajador_id = ? AND fecha BETWEEN ? AND ?";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            // Verificar si los JDateChoosers están inicializados
            if (jDateChooser1 == null || jDateChooser2 == null) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione un rango de fechas válido.");
                return;  // Salir del método si están nulos
            }

            if (jDateChooser1.getDate() == null || jDateChooser2.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione fechas válidas en ambos campos.");
                return; // Salir del método si las fechas son nulas
            }

            pst.setDate(2, new java.sql.Date(jDateChooser1.getDate().getTime()));
            pst.setDate(3, new java.sql.Date(jDateChooser2.getDate().getTime()));
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) Tabla.getModel();
            model.setRowCount(0); // Limpiar datos existentes

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
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

    

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        txtIdHoras = new javax.swing.JTextField();
        btnGenerar = new javax.swing.JButton();
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
        txtFechaIngreso = new javax.swing.JTextField();
        txtTiempoTrabajado = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtHoraIngreso = new javax.swing.JTextField();
        txtHoraSalida = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        txtIdSalida = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtFechaSalida = new javax.swing.JTextField();
        btnCierre = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        dateMensual = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        dateSemanlQuincenal = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();

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

        jPanel12.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 1130, 360));
        jPanel12.add(txtIdHoras, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 60, 30));

        btnGenerar.setText("Generar Reporte");
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 80, 120, 40));
        jPanel12.add(txtIdTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 70, 30));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel12.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 20, -1, -1));
        jPanel12.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 10, 120, -1));
        jPanel12.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 50, 120, -1));

        jLabel1.setText("Inicio");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 10, -1, -1));

        jLabel2.setText("Final");
        jPanel12.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 50, -1, -1));

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
        jPanel12.add(btnMostrarDatos, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));
        jPanel12.add(txtIdIngreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 70, -1));

        jLabel4.setText("id Ingreso");
        jPanel12.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, -1));
        jPanel12.add(txtFechaIngreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 20, 100, -1));
        jPanel12.add(txtTiempoTrabajado, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 100, 100, -1));

        jLabel5.setText("Fecha Ingreso");
        jPanel12.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, -1, -1));

        jLabel6.setText("Horas Trabajadas");
        jPanel12.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 100, -1, -1));

        jLabel7.setText("Hora de Ingreso");
        jPanel12.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, -1, -1));

        jLabel8.setText("Hora de Salida");
        jPanel12.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 60, -1, -1));
        jPanel12.add(txtHoraIngreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 20, 90, -1));
        jPanel12.add(txtHoraSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 60, 90, -1));

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel12.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 90, -1, -1));

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel12.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 90, -1, -1));
        jPanel12.add(txtIdSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 70, -1));

        jLabel9.setText("id Salida");
        jPanel12.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, -1, -1));

        jLabel10.setText("Fecha Salida");
        jPanel12.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 60, -1, -1));
        jPanel12.add(txtFechaSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 50, 100, -1));

        btnCierre.setText("Cierre");
        btnCierre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCierreActionPerformed(evt);
            }
        });
        jPanel12.add(btnCierre, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, -1, -1));

        jLabel11.setText("Mes");
        jPanel12.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 550, -1, -1));
        jPanel12.add(dateMensual, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 550, 190, -1));

        jLabel12.setText("Semanal - Quincenal");
        jPanel12.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 550, -1, -1));
        jPanel12.add(dateSemanlQuincenal, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 550, 160, -1));

        jLabel13.setText("Establecer el inicio de las fechas de Pagos");
        jPanel12.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 510, -1, -1));

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
        // Verificar que el estado sea seleccionado y que el ítem no sea null
        if (evt.getStateChange() == ItemEvent.SELECTED && evt.getItem() != null) {
            MostrarCodigoTrabajador(cbxTrabajador, txtIdTrabajador);
        }
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed
        try {
            int trabajadorId = Integer.parseInt(txtIdTrabajador.getText()); // Asegúrate de que txtIdTrabajador contenga un valor numérico válido
            java.util.Date fechaInicio = jDateChooser1.getDate();
            java.util.Date fechaFin = jDateChooser2.getDate();

            // Verificar que se hayan seleccionado fechas válidas
            if (fechaInicio == null || fechaFin == null) {
                JOptionPane.showMessageDialog(null, "Seleccione un rango de fechas válido.");
                return;
            }

            // Llamada a generarReporte con los parámetros correctos
//            generarReporte(trabajadorId, fechaInicio, fechaFin);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de trabajador no válido.");
        }
    }//GEN-LAST:event_btnGenerarActionPerformed

    private void btnMostrarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarDatosActionPerformed
        // Verifica si txtIdTrabajador tiene un valor
        if (txtIdTrabajador.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de trabajador.");
            return;
        }

        try {
            // Obtener el ID del trabajador desde el JTextField txtIdTrabajador antes de llamar al método Mostrar
            int trabajadorId = Integer.parseInt(txtIdTrabajador.getText());
            Mostrar(Tabla, trabajadorId);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de trabajador no válido. Asegúrese de que sea un número.");
        }
    }//GEN-LAST:event_btnMostrarDatosActionPerformed

    private void TablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaMouseClicked
        seleccionar(Tabla);
    }//GEN-LAST:event_TablaMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        seleccionar(Tabla);
        int idIngreso = Integer.parseInt(txtIdIngreso.getText());
        int idSalida = Integer.parseInt(txtIdSalida.getText());
        eliminar(idIngreso, idSalida);
        txtFechaIngreso.setText("");
        txtHoraSalida.setText("");
        txtHoraIngreso.setText("");
        txtIdIngreso.setText("");
        txtIdSalida.setText("");
        txtTiempoTrabajado.setText("");

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        try {
            int idIngreso = Integer.parseInt(txtIdIngreso.getText());
            int idSalida = Integer.parseInt(txtIdSalida.getText());

            // Obtener la fecha del JTextField Fecha si es requerido en formato LocalDate
            LocalDate fecha = LocalDate.parse(txtFechaIngreso.getText());

            // Obtener la hora de ingreso y de salida del JTextField y convertirlos a enteros
            int horaIngreso = Integer.parseInt(txtHoraIngreso.getText().split(":")[0]);
            int minutoIngreso = Integer.parseInt(txtHoraIngreso.getText().split(":")[1]);

            int horaSalida = Integer.parseInt(txtHoraSalida.getText().split(":")[0]);
            int minutoSalida = Integer.parseInt(txtHoraSalida.getText().split(":")[1]);

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
        txtFechaIngreso.setText("");
        txtHoraSalida.setText("");
        txtHoraIngreso.setText("");
        txtIdIngreso.setText("");
        txtIdSalida.setText("");
        txtTiempoTrabajado.setText("");
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCierreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCierreActionPerformed
        // Verificar si el JTextField txtIdTrabajador no está vacío
        if (!txtIdTrabajador.getText().isEmpty()) {
            try {
                int trabajadorId = Integer.parseInt(txtIdTrabajador.getText()); // Obtén el ID del trabajador desde el JTextField

//                // Verificar si hay una sesión activa
//                if (verificarSesionActiva(trabajadorId)) {
//                    // Cierra la sesión activa del trabajador
//                    cerrarSesionTrabajador(trabajadorId);
//
//                    // Actualiza la tabla para reflejar los cambios
//                    Mostrar(Tabla, trabajadorId);
//                } else {
//                    JOptionPane.showMessageDialog(null, "No se encontró una sesión activa para el trabajador con ID: " + trabajadorId);
//                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El ID del trabajador no es válido. Por favor, verifique el valor en txtIdTrabajador.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "El ID del trabajador no puede ser nulo. Por favor, verifique el campo txtIdTrabajador.");

        }
    }//GEN-LAST:event_btnCierreActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable Tabla;
    private javax.swing.JButton btnCierre;
    private javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnMostrarDatos;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private com.toedter.calendar.JDateChooser dateMensual;
    private com.toedter.calendar.JDateChooser dateSemanlQuincenal;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JTextField txtFechaIngreso;
    private javax.swing.JTextField txtFechaSalida;
    private javax.swing.JTextField txtHoraIngreso;
    private javax.swing.JTextField txtHoraSalida;
    public javax.swing.JTextField txtIdHoras;
    private javax.swing.JTextField txtIdIngreso;
    private javax.swing.JTextField txtIdSalida;
    public javax.swing.JTextField txtIdTrabajador;
    private javax.swing.JTextField txtTiempoTrabajado;
    // End of variables declaration//GEN-END:variables

    public void MostrarTrabajador(JComboBox<String> comboTrabajador) {
        System.out.println("Método MostrarTrabajador llamado en: " + new java.util.Date());
        String sql = "SELECT * FROM worker";
        try (Statement st = conect.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            comboTrabajador.removeAllItems(); // Limpiar los elementos existentes en el comboBox

            while (rs.next()) {
                comboTrabajador.addItem(rs.getString("nombre")); // Agregar cada trabajador al comboBox
                System.out.println("Añadiendo trabajador al ComboBox: " + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Trabajadores: " + e.toString());
        }
    }

    public void MostrarCodigoTrabajador(JComboBox<String> trabajador, JTextField IdTrabajador) {
        if (trabajador.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Seleccione un trabajador válido.");
            return;
        }

        String consulta = "SELECT worker.idWorker FROM worker WHERE worker.nombre = ?";

        try {
            CallableStatement cs = conexion.getConexion().prepareCall(consulta);
            cs.setString(1, trabajador.getSelectedItem().toString());
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                IdTrabajador.setText(rs.getString("idWorker"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar código del trabajador: " + e.toString());
        }
    }

    private void reiniciarHorasYPagos(int trabajadorId) {
        String sqlHoras = "DELETE FROM horas_trabajadas WHERE trabajador_id = ?";
        String sqlPagos = "DELETE FROM pagos_acumulados WHERE trabajador_id = ?";

        try (PreparedStatement pstHoras = conect.prepareStatement(sqlHoras); PreparedStatement pstPagos = conect.prepareStatement(sqlPagos)) {
            pstHoras.setInt(1, trabajadorId);
            pstHoras.executeUpdate();

            pstPagos.setInt(1, trabajadorId);
            pstPagos.executeUpdate();

            System.out.println("Registros de horas y pagos acumulados reiniciados para trabajador ID: " + trabajadorId);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al reiniciar registros: ", ex);
        }
    }

    // Método para guardar las horas trabajadas en la base de datos
    private void guardarHorasTrabajadas(int trabajadorId, double horasTrabajadas, LocalDate fecha) {
        // Asegúrate de que la fecha sea válida
        if (fecha == null || fecha.equals(LocalDate.EPOCH)) {
            fecha = LocalDate.now(); // Usa la fecha actual si la fecha es nula o no válida
        }

        String sql = "INSERT INTO horas_trabajadas (trabajador_id, horas, fecha) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE horas = VALUES(horas)";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDouble(2, horasTrabajadas);
            pst.setDate(3, java.sql.Date.valueOf(fecha));
            pst.executeUpdate();
            System.out.println("Horas trabajadas registradas para trabajador ID: " + trabajadorId + " en la fecha: " + fecha);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al guardar horas trabajadas: ", ex);
        }
    }

}
