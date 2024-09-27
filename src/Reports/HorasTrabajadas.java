package Reports;

import Administration.PuestoDeTrabajo;
import Bases.PeriodoPago;
import Bases.SalidaForzadaRenderer;
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
import javax.swing.Timer;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class HorasTrabajadas extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(HorasTrabajadas.class.getName());
    private Conectar conexion;
    private Connection conect;
    public PuestoDeTrabajo puestoDeTrabajo; // Instancia de PuestoDeTrabajo
    private Timer cierreAutomatico;
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

    // Método para configurar el cierre automático
    public void configurarCierreAutomatico() {
        long delay = calcularMilisegundosHastaMedianoche();
        cierreAutomatico = new Timer((int) delay, evt -> {
            cerrarSesionesPendientes();
            enviarNotificacion("Cierre automático realizado", "El sistema ha cerrado automáticamente las sesiones.");
            configurarCierreAutomatico();  // Reprogramar para el próximo día.
        });
        cierreAutomatico.setRepeats(false);
        cierreAutomatico.start();
    }

    // Método para enviar correos electrónicos
//    public void enviarCorreoElectronico(String destinatario, String asunto, String cuerpo) {
//        // Configurar propiedades para la conexión de correo
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.tuServidorCorreo.com");  // Cambia al servidor SMTP correcto
//        props.put("mail.smtp.port", "587");  // Cambia el puerto según el servidor
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//
//        // Aquí usas las variables de entorno para manejar credenciales de manera segura
//        final String username = System.getenv("EMAIL_USERNAME");  // Configura la variable de entorno EMAIL_USERNAME
//        final String password = System.getenv("EMAIL_PASSWORD");  // Configura la variable de entorno EMAIL_PASSWORD
//
//        // Autenticación
//        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);  // Usa las variables de entorno
//            }
//        });
//
//        try {
//            // Crear el mensaje de correo
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(username));  // Usa la variable de entorno para el correo
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
//            message.setSubject(asunto);
//            message.setText(cuerpo);
//
//            // Enviar el correo
//            Transport.send(message);
//            System.out.println("Correo enviado exitosamente.");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
    // Método para calcular milisegundos hasta la medianoche
    private long calcularMilisegundosHastaMedianoche() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().atStartOfDay().plusDays(1);
        return Duration.between(now, midnight).toMillis();
    }

    // Cierre de sesiones pendientes
    public void cerrarSesionesPendientes() {
        String sql = "SELECT trabajador_id FROM horas_ingreso WHERE NOT EXISTS "
                + "(SELECT 1 FROM horas_salida WHERE horas_ingreso.id_ingreso = horas_salida.id_ingreso)";
        try (Statement st = conect.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int trabajadorId = rs.getInt("trabajador_id");
                registrarFinSesionForzada(trabajadorId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public LocalDate obtenerFechaInicioActividadesDesdeBD() {
        String sql = "SELECT fecha_inicio_actividades FROM configuracion";  // Cambiar el nombre de la columna y tabla según corresponda
        try (PreparedStatement pst = conect.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getDate("fecha_inicio_actividades").toLocalDate();  // Convertir java.sql.Date a LocalDate
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener fecha de inicio de actividades: " + e.getMessage());
        }
        return LocalDate.now();  // Si hay un error, usa la fecha actual
    }

    public LocalDate[] calcularInicioYFinPeriodo(String tipoPeriodo, LocalDate fechaInicioActividades, int numeroPeriodo) {
        switch (tipoPeriodo) {
            case "Semanal":
                return new LocalDate[]{
                    fechaInicioActividades.plusWeeks(numeroPeriodo - 1),
                    fechaInicioActividades.plusWeeks(numeroPeriodo - 1).plusDays(6)
                };
            case "Quincenal":
                return new LocalDate[]{
                    fechaInicioActividades.plusDays((numeroPeriodo - 1) * 14),
                    fechaInicioActividades.plusDays((numeroPeriodo - 1) * 14).plusDays(13)
                };
            case "Mensual":
                return new LocalDate[]{
                    fechaInicioActividades.plusMonths(numeroPeriodo - 1).withDayOfMonth(1),
                    fechaInicioActividades.plusMonths(numeroPeriodo - 1).with(TemporalAdjusters.lastDayOfMonth())
                };
            default:
                throw new IllegalArgumentException("Tipo de periodo no válido: " + tipoPeriodo);
        }
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

    // Mostrar horas trabajadas
    public void Mostrar(JTable tabla, int trabajadorId) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Ingreso");
        modelo.addColumn("ID Salida");
        modelo.addColumn("Fecha Ingreso");
        modelo.addColumn("Hora Ingreso");
        modelo.addColumn("Fecha Salida");
        modelo.addColumn("Hora Salida");
        modelo.addColumn("Horas Trabajadas");
        modelo.addColumn("Forzado");

        tabla.setModel(modelo);

        String sql = "SELECT hi.id_ingreso, hs.id_salida, hi.fInicio, hs.fSalida, "
                + "TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 AS horasTrabajadas, hs.forzado "
                + "FROM horas_ingreso hi JOIN horas_salida hs ON hi.id_ingreso = hs.id_ingreso "
                + "WHERE hi.trabajador_id = ?";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Timestamp fInicio = rs.getTimestamp("fInicio");
                Timestamp fSalida = rs.getTimestamp("fSalida");

                // Extraer fecha y hora por separado
                String fechaIngreso = fInicio.toLocalDateTime().toLocalDate().toString();
                String horaIngreso = fInicio.toLocalDateTime().toLocalTime().toString();
                String fechaSalida = fSalida != null ? fSalida.toLocalDateTime().toLocalDate().toString() : "Pendiente";
                String horaSalida = fSalida != null ? fSalida.toLocalDateTime().toLocalTime().toString() : "Pendiente";

                modelo.addRow(new Object[]{
                    rs.getInt("id_ingreso"),
                    rs.getInt("id_salida"),
                    fechaIngreso,
                    horaIngreso,
                    fechaSalida,
                    horaSalida,
                    rs.getDouble("horasTrabajadas"),
                    rs.getBoolean("forzado")
                });
            }
            tabla.setModel(modelo);
            // Aplicamos el renderer personalizado en la columna "Forzado"
            tabla.getColumnModel().getColumn(modelo.getColumnCount() - 1).setCellRenderer(new SalidaForzadaRenderer());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar los registros: " + e.getMessage());
        }
    }

    public void seleccionar(JTable tabla) {
        // Obtener el índice de la fila seleccionada
        int filaSeleccionada = tabla.getSelectedRow();

        if (filaSeleccionada != -1) { // Verificar si se ha seleccionado alguna fila
            // Asegúrate de que la tabla tiene al menos las 8 columnas que se intentan acceder
            if (tabla.getColumnCount() >= 8) {
                // Obtener los valores de las columnas de la fila seleccionada
                Object valueIdIngreso = tabla.getValueAt(filaSeleccionada, 0);
                Object valueIdSalida = tabla.getValueAt(filaSeleccionada, 1);
                Object valueFechaIngreso = tabla.getValueAt(filaSeleccionada, 2);
                Object valueHoraIngreso = tabla.getValueAt(filaSeleccionada, 3);
                Object valueFechaSalida = tabla.getValueAt(filaSeleccionada, 4);
                Object valueHoraSalida = tabla.getValueAt(filaSeleccionada, 5);
                Object valueHorasTrabajadas = tabla.getValueAt(filaSeleccionada, 6);

                String idIngreso = (valueIdIngreso != null) ? valueIdIngreso.toString() : "";
                String idSalida = (valueIdSalida != null) ? valueIdSalida.toString() : "";
                String fechaIngreso = (valueFechaIngreso != null) ? valueFechaIngreso.toString() : "";
                String horaIngreso = (valueHoraIngreso != null) ? valueHoraIngreso.toString() : "";
                String fechaSalida = (valueFechaSalida != null) ? valueFechaSalida.toString() : "";
                String horaSalida = (valueHoraSalida != null) ? valueHoraSalida.toString() : "";
                String horasTrabajadas = (valueHorasTrabajadas != null) ? valueHorasTrabajadas.toString() : "";

                // Mostrar estos valores en los JTextFields
                txtIdIngreso.setText(idIngreso);
                txtIdSalida.setText(idSalida);
                txtFechaIngreso.setText(fechaIngreso);
                txtHoraIngreso.setText(horaIngreso);
                txtFechaSalida.setText(fechaSalida);
                txtHoraSalida.setText(horaSalida);
                txtTiempoTrabajado.setText(horasTrabajadas);
            } else {
                JOptionPane.showMessageDialog(null, "La tabla no contiene suficientes columnas.");
            }
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

    public void modificar(int idIngreso, int idSalida, LocalDateTime nuevaHoraIngreso, LocalDateTime nuevaHoraSalida, int trabajadorId) throws SQLException {
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

                    // Verificar si la salida era forzada y corregirla
                    String sqlSelectForzado = "SELECT forzado FROM horas_salida WHERE id_salida = ?";
                    try (PreparedStatement pstForzado = conect.prepareStatement(sqlSelectForzado)) {
                        pstForzado.setInt(1, idSalida);
                        ResultSet rs = pstForzado.executeQuery();
                        if (rs.next()) {
                            boolean esForzado = rs.getBoolean("forzado");
                            if (esForzado) {
                                // Si es forzado, corregir la salida
                                corregirSalidaForzada(idSalida, nuevaHoraSalida, trabajadorId);
                            }
                        }
                    }
                }
            }

            conect.commit();  // Confirmar la transacción
            System.out.println("Transacción realizada con éxito.");
        } catch (SQLException e) {
            conect.rollback();  // Revertir la transacción si ocurre un error
            e.printStackTrace();
            System.out.println("Error al modificar las horas: " + e.getMessage());
        } finally {
            conect.setAutoCommit(autoCommitOriginal);  // Restaurar el estado original de autocommit
        }
    }

    // Guardar horas trabajadas
    public void guardarHorasTrabajadas(int trabajadorId, double horasTrabajadas, LocalDate fecha, String tipoPeriodo) {
        String periodoPago = determinarPeriodoPago(fecha, fechaInicioActividades, tipoPeriodo);
        String sql = "INSERT INTO horas_trabajadas (trabajador_id, horas, fecha, periodo_pago) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE horas = VALUES(horas), periodo_pago = VALUES(periodo_pago)";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDouble(2, horasTrabajadas);
            pst.setDate(3, java.sql.Date.valueOf(fecha));
            pst.setString(4, periodoPago);
            pst.executeUpdate();
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

    // Registrar fin de sesión forzada
    public void registrarFinSesionForzada(int trabajadorId) throws SQLException {
        String sql = "SELECT id_ingreso FROM horas_ingreso WHERE trabajador_id = ? ORDER BY fInicio DESC LIMIT 1";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int idIngreso = rs.getInt("id_ingreso");

                String sqlCheck = "SELECT id_salida FROM horas_salida WHERE id_ingreso = ?";
                try (PreparedStatement pstCheck = conect.prepareStatement(sqlCheck)) {
                    pstCheck.setInt(1, idIngreso);
                    ResultSet rsCheck = pstCheck.executeQuery();
                    if (!rsCheck.next()) {
                        String sqlInsert = "INSERT INTO horas_salida (trabajador_id, fSalida, id_ingreso, forzado) VALUES (?, ?, ?, 1)";
                        try (PreparedStatement pstInsert = conect.prepareStatement(sqlInsert)) {
                            pstInsert.setInt(1, trabajadorId);
                            pstInsert.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                            pstInsert.setInt(3, idIngreso);
                            pstInsert.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    public void corregirSalidaForzada(int idSalida, LocalDateTime nuevaHoraSalida, int trabajadorId) throws SQLException {
        String sql = "UPDATE horas_salida SET fSalida = ?, forzado = 0 WHERE id_salida = ? AND forzado = 1";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setTimestamp(1, Timestamp.valueOf(nuevaHoraSalida));  // Nueva hora de salida
            pst.setInt(2, idSalida);
            int filasAfectadas = pst.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Salida forzada corregida con éxito.");
                // Después de corregir, refrescar la tabla
                Mostrar(Tabla, trabajadorId);  // Ahora pasamos trabajadorId correctamente
            } else {
                System.out.println("No se encontró una salida forzada con ese ID.");
            }
        }
    }

    public void calcularSueldos(int trabajadorId, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        int idPDT = 0;
        String tipoPeriodo = obtenerTipoPeriodo(idPDT);
        boolean tieneOvertime = verificarOvertime(trabajadorId);
        double pagoPorHora = obtenerPagoPorHora(trabajadorId);

        double totalHorasTrabajadas = calcularHorasTrabajadas(trabajadorId, fechaInicio, fechaFin);
        double totalHorasExtras = calcularHorasExtras(trabajadorId, fechaInicio, fechaFin, tipoPeriodo, tieneOvertime);

        // Calcular salario bruto
        double salarioBruto = (totalHorasTrabajadas * pagoPorHora) + (totalHorasExtras * pagoPorHora * 1.5);

        // Aplicar deducciones
        double salarioNeto = calcularSalarioNeto(salarioBruto, trabajadorId);

        // Guardar en la tabla de pagos
        guardarPago(trabajadorId, salarioBruto, salarioNeto);
    }

    private void guardarPago(int trabajadorId, double salarioBruto, double salarioNeto) {
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

    public String obtenerTipoPeriodo(int idPuestoDeTrabajo) throws SQLException {
        String tipoPeriodo = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Conectar con = new Conectar();
            conn = con.getConexion();

            String consulta = "SELECT p.descripcion FROM puestodetrabajo pt\n"
                    + "INNER JOIN periodo p ON pt.id_periodo=p.id\n"
                    + "WHERE idPDT = ?";
            ps = conn.prepareStatement(consulta);
            ps.setInt(1, idPuestoDeTrabajo);

            rs = ps.executeQuery();

            if (rs.next()) {
                tipoPeriodo = rs.getString("periodo");
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener tipo de periodo: " + e.getMessage());
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return tipoPeriodo;
    }

    public double calcularHorasTrabajadas(int trabajadorId, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        double totalHoras = 0.0;
        String sql = "SELECT SUM(TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida)) / 60.0 AS horasTrabajadas FROM horas_ingreso hi JOIN horas_salida hs ON hi.id_ingreso = hs.id_ingreso WHERE hi.trabajador_id = ? AND hi.fInicio BETWEEN ? AND ?";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, java.sql.Date.valueOf(fechaInicio));
            pst.setDate(3, java.sql.Date.valueOf(fechaFin));
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                totalHoras = rs.getDouble("horasTrabajadas");
            }
        }
        return totalHoras;
    }

    public double calcularDeducciones(double salarioBruto, int trabajadorId) {
        double impuestoFederal = salarioBruto * obtenerTasaImpuestoFederal(trabajadorId);
        double seguroSocial = salarioBruto * 0.062;
        double medicare = salarioBruto * 0.0145;
        double impuestoEstatal = salarioBruto * obtenerTasaImpuestoEstatal(trabajadorId);
        double otrasDeducciones = obtenerOtrasDeducciones(trabajadorId);

        return impuestoFederal + seguroSocial + medicare + impuestoEstatal + otrasDeducciones;
    }

    public void acumularVacaciones(int trabajadorId, int diasTrabajados) throws SQLException {
        String sql = "UPDATE vacaciones SET dias_acumulados = dias_acumulados + ? WHERE trabajador_id = ?";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            int diasAcumulados = diasTrabajados / 20;  // Ejemplo: Cada 20 días trabajados se acumula 1 día de vacaciones.
            pst.setInt(1, diasAcumulados);
            pst.setInt(2, trabajadorId);
            pst.executeUpdate();
        }
    }

    public void registrarAuditoria(int trabajadorId, String accion, LocalDateTime fechaHora) {
        String sql = "INSERT INTO auditoria (trabajador_id, accion, fecha_hora) VALUES (?, ?, ?)";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setString(2, accion);
            pst.setTimestamp(3, Timestamp.valueOf(fechaHora));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double calcularHorasExtrasMensuales(int trabajadorId, LocalDate fechaInicio, LocalDate fechaFin) {
        double totalHorasExtras = 0.0;

        String sql = "SELECT SUM(CASE WHEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 > 8 THEN TIMESTAMPDIFF(MINUTE, hi.fInicio, hs.fSalida) / 60.0 - 8 ELSE 0 END) AS horasExtras "
                + "FROM horas_ingreso hi "
                + "JOIN horas_salida hs ON hi.id_ingreso = hs.id_ingreso "
                + "WHERE hi.trabajador_id = ? AND hi.fInicio BETWEEN ? AND ?";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            pst.setDate(2, java.sql.Date.valueOf(fechaInicio));
            pst.setDate(3, java.sql.Date.valueOf(fechaFin));
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                totalHorasExtras = rs.getDouble("horasExtras");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return totalHorasExtras;
    }

    public boolean verificarOvertime(int trabajadorId) {
        String sql = "SELECT ot.descripcion FROM puestodetrabajo pt \n"
                + "INNER JOIN overtime ot ON pt.id_overtime = ot.id \n"
                + "WHERE pt.idTrabajador = ?";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String descripcionOvertime = rs.getString("descripcion");  // Obtén la descripción de overtime
                // Retorna true si la descripción de overtime corresponde a un tipo válido de overtime
                return descripcionOvertime != null && !descripcionOvertime.isEmpty();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar overtime: " + e.getMessage());
        }
        return false;
    }

    public double obtenerPagoPorHora(int trabajadorId) {
        String sql = "SELECT pagoPorHora FROM puestodetrabajo WHERE idTrabajador = ?";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getDouble("pagoPorHora");  // Retorna el pago por hora.
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el pago por hora: " + e.getMessage());
        }
        return 0.0;  // Retorna 0 si no se encuentra el valor.
    }

    public double calcularPagoVacaciones(int trabajadorId, int diasVacaciones) {
        double pagoVacaciones = 0.0;
        double pagoPorHora = obtenerPagoPorHora(trabajadorId);
        String tipoPago = obtenerTipoPago(trabajadorId);

        if (tipoPago.equals("Mensual")) {
            pagoVacaciones = (obtenerSueldoMensual(trabajadorId) / 30) * diasVacaciones;
        } else {
            pagoVacaciones = pagoPorHora * diasVacaciones * 8; // Vacaciones a valor de hora normal
        }

        return pagoVacaciones;
    }

    private double obtenerSueldoMensual(int trabajadorId) {
        String sql = "SELECT sueldo FROM puestodetrabajo WHERE idTrabajador = ?";

        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getDouble("sueldo");  // Retorna el pago por hora.
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el sueldo: " + e.getMessage());
        }
        return 0.0;  // Retorna 0 si no se encuentra el valor.
    }

    private String obtenerTipoPago(int trabajadorId) {
        String sql = "SELECT p.descripcion FROM puestodetrabajo pt\n"
                + "INNER JOIN periodo p ON pt.id_periodo= p.id WHERE pt.idTrabajador = ?";
        try (PreparedStatement pst = conect.prepareStatement(sql)) {
            pst.setInt(1, trabajadorId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("periodo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Semanal"; // Por defecto semanal
    }

    private double calcularHorasExtras(int trabajadorId, LocalDate fechaInicio, LocalDate fechaFin, String tipoPeriodo, boolean tieneOvertime) {
        // Verificamos que tipoPeriodo no sea null antes de llamar a equals
        if (tipoPeriodo != null && tipoPeriodo.equals("Semanal") && tieneOvertime) {
            return calcularHorasExtrasMensuales(trabajadorId, fechaInicio, fechaFin); // Reutilizamos la lógica
        }

        // Aquí puedes añadir más lógica para los casos quincenal o mensual
        if (tipoPeriodo != null && tipoPeriodo.equals("Quincenal") && tieneOvertime) {
            return calcularHorasExtrasQuincenales(trabajadorId, fechaInicio, fechaFin);
        }

        if (tipoPeriodo != null && tipoPeriodo.equals("Mensual") && tieneOvertime) {
            return calcularHorasExtrasMensuales(trabajadorId, fechaInicio, fechaFin);
        }

        // Si no hay overtime o no hay un tipo de periodo válido, devolvemos 0
        return 0.0;
    }

    private double calcularHorasExtrasQuincenales(int trabajadorId, LocalDate fechaInicio, LocalDate fechaFin) {
        // Límite de horas regulares en un periodo quincenal (14 días)
        final double HORAS_REGULARES_QUINCENAL = 80.0;

        // Obtener las horas trabajadas en el periodo quincenal para el trabajador
        double horasTrabajadas = obtenerHorasTrabajadas(trabajadorId, fechaInicio, fechaFin);

        // Si las horas trabajadas son mayores a las horas regulares, calculamos las extras
        if (horasTrabajadas > HORAS_REGULARES_QUINCENAL) {
            // Las horas extras son las que exceden las horas regulares
            return horasTrabajadas - HORAS_REGULARES_QUINCENAL;
        } else {
            // Si no ha excedido el límite, no hay horas extras
            return 0.0;
        }
    }

    private double obtenerHorasTrabajadas(int trabajadorId, LocalDate fechaInicio, LocalDate fechaFin) {
        // Aquí debes implementar la lógica para obtener las horas trabajadas del trabajador
        // Por ejemplo, a partir de una consulta a la base de datos o sumando los registros de horas.

        double horasTrabajadas = 0.0;

        // Consulta a la base de datos o cálculo de horas trabajadas en el periodo (ejemplo)
        String sql = "SELECT SUM(horas) AS total_horas FROM horas_trabajadas WHERE trabajador_id = ? AND fecha BETWEEN ? AND ?";

        try (PreparedStatement ps = conect.prepareStatement(sql)) {
            ps.setInt(1, trabajadorId);
            ps.setDate(2, java.sql.Date.valueOf(fechaInicio));
            ps.setDate(3, java.sql.Date.valueOf(fechaFin));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                horasTrabajadas = rs.getDouble("total_horas");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return horasTrabajadas;
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

        guardarHorasTrabajadas(trabajadorId, totalHoras, fecha);
    }

    public String determinarPeriodoPago(LocalDate fechaTrabajo, LocalDate fechaInicioActividades, String tipoPeriodo) {
        LocalDate primerLunesDespues = fechaInicioActividades.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));  // Primer lunes después de la fecha de inicio

        long diferenciaDias = ChronoUnit.DAYS.between(primerLunesDespues, fechaTrabajo);

        switch (tipoPeriodo) {
            case "Semanal":
                long semanas = diferenciaDias / 7;  // Calcula el número de semanas completas desde el primer lunes
                return "Semana " + (semanas + 1);   // La primera semana es desde el lunes más cercano

            case "Quincenal":
                long quincenas = diferenciaDias / 14;  // Calcula el número de quincenas completas desde el primer lunes
                return "Quincena " + (quincenas + 1);

            case "Mensual":
                long meses = ChronoUnit.MONTHS.between(primerLunesDespues.withDayOfMonth(1), fechaTrabajo.withDayOfMonth(1));  // Calcula los meses completos
                return "Mes " + (meses + 1);

            default:
                return "Desconocido";  // Si el tipo de periodo no es válido, devuelve "Desconocido"
        }
    }

    public void recalcularPeriodos(int trabajadorId, String tipoPeriodo) throws SQLException {
        LocalDate fechaInicioActividades = obtenerFechaInicioActividadesDesdeBD();  // Obtener la nueva fecha de inicio de actividades
        LocalDate fechaActual = LocalDate.now();

        // Lógica para recalcular los periodos semanales, quincenales o mensuales
        int numeroPeriodo = 1; // Puedes empezar desde el primer periodo y ajustar según corresponda

        while (!fechaInicioActividades.isAfter(fechaActual)) {
            // Calcular el inicio y fin del periodo basado en el tipo de periodo
            LocalDate[] fechasPeriodo = calcularInicioYFinPeriodo(tipoPeriodo, fechaInicioActividades, numeroPeriodo);
            LocalDate fechaInicioPeriodo = fechasPeriodo[0];
            LocalDate fechaFinPeriodo = fechasPeriodo[1];

            // Calcular las horas trabajadas en ese periodo
            double horasTrabajadas = calcularHorasTrabajadas(trabajadorId, fechaFinPeriodo, fechaActual);

            // Guardar las horas trabajadas en la base de datos
            guardarHorasTrabajadas(trabajadorId, horasTrabajadas, fechaInicioPeriodo, tipoPeriodo);

            // Incrementar el número de periodo y avanzar la fecha de inicio al siguiente periodo
            numeroPeriodo++;
            fechaInicioActividades = fechaFinPeriodo.plusDays(1);  // Mover al día siguiente del fin del periodo actual
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
            generarReporte(trabajadorId, fechaInicio, fechaFin);
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

            modificar(idIngreso, idSalida, nuevaHoraIngreso, nuevaHoraSalida, ABORT);

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

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El ID del trabajador no es válido. Por favor, verifique el valor en txtIdTrabajador.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "El ID del trabajador no puede ser nulo. Por favor, verifique el campo txtIdTrabajador.");

        }
    }//GEN-LAST:event_btnCierreActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable Tabla;
    public javax.swing.JButton btnCierre;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGenerar;
    public javax.swing.JButton btnModificar;
    public javax.swing.JButton btnMostrarDatos;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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

    private void enviarNotificacion(String cierre_automático_realizado, String el_sistema_ha_cerrado_automáticamente_las) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
