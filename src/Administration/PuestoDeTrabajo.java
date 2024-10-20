package Administration;

import Bases.CustomItem;
import com.toedter.calendar.JDateChooser;
import conectar.Conectar;
import java.awt.HeadlessException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class PuestoDeTrabajo extends javax.swing.JInternalFrame {

    //Variables
    int idPDT, idTrabajador, idTDT;
    Double pagoPorHora, sueldo;
    String estado, Trabajador, tipoDeTrabajo;
    int overtime, horario, periodo, vacaciones, tiempoVacaciones;
    Date fechaDePuesto;

    public int getIdPDT() {
        return idPDT;
    }

    public void setIdPDT(int idPDT) {
        this.idPDT = idPDT;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public int getIdTDT() {
        return idTDT;
    }

    public void setIdTDT(int idTDT) {
        this.idTDT = idTDT;
    }

    public Double getPagoPorHora() {
        return pagoPorHora;
    }

    public void setPagoPorHora(Double pagoPorHora) {
        this.pagoPorHora = pagoPorHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTrabajador() {
        return Trabajador;
    }

    public void setTrabajador(String Trabajador) {
        this.Trabajador = Trabajador;
    }

    public String getTipoDeTrabajo() {
        return tipoDeTrabajo;
    }

    public void setTipoDeTrabajo(String tipoDeTrabajo) {
        this.tipoDeTrabajo = tipoDeTrabajo;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public int getHorario() {
        return horario;
    }

    public void setHorario(int horario) {
        this.horario = horario;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public Double getSueldo() {
        return sueldo;
    }

    public void setSueldo(Double sueldo) {
        this.sueldo = sueldo;
    }

    public Date getFechaDePuesto() {
        return fechaDePuesto;
    }

    public void setFechaDePuesto(Date fechaDePuesto) {
        this.fechaDePuesto = fechaDePuesto;
    }

    public int getVacaciones() {
        return vacaciones;
    }

    public void setVacaciones(int vacaciones) {
        this.vacaciones = vacaciones;
    }

    public int getTiempoVacaciones() {
        return tiempoVacaciones;
    }

    public void setTiempoVacaciones(int tiempoVacaciones) {
        this.tiempoVacaciones = tiempoVacaciones;
    }

    public PuestoDeTrabajo() {
        initComponents();

        AutoCompleteDecorator.decorate(cbxTDT);
        AutoCompleteDecorator.decorate(cbxTrabajador);
        AutoCompleteDecorator.decorate(cbxOverTime);
        AutoCompleteDecorator.decorate(cbxHorario);
        AutoCompleteDecorator.decorate(cbxPeriodo);
        AutoCompleteDecorator.decorate(cbxVacaciones);

        MostrarTrabajador(cbxTrabajador);
        MostrarTipoDeTrabajo(cbxTDT);
        MostrarOverTime(cbxOverTime);
        MostrarHorario(cbxHorario);
        MostrarPeriodo(cbxPeriodo);
        MostrarVacaciones(cbxVacaciones);

        Mostrar(tbPuestosDeTrabajo);

        txtIdPDT.setEnabled(false);
        txtIdOverTime.setEnabled(false);
        txtIdPDT.setEnabled(false);
        txtIdTipoDeTrabajo.setEnabled(false);
        txtIdtrabajador.setEnabled(false);
        txtIdOverTime.setEnabled(false);
        txtIdHorario.setEnabled(false);
        txtIdPeriodo.setEnabled(false);
        txtIdVacaciones.setEnabled(false);

    }

    public void Mostrar(JTable Tabla) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        DefaultTableModel modelo = (DefaultTableModel) Tabla.getModel();

        // Asegúrate de que el modelo tenga las columnas correctas
        if (modelo.getColumnCount() == 0) {
            modelo.addColumn("idPDT");
            modelo.addColumn("Trabajador");
            modelo.addColumn("Tipo De Trabajo");
            modelo.addColumn("Pago por Hora");
            modelo.addColumn("Sueldo");
            modelo.addColumn("OverTime");
            modelo.addColumn("Horario");
            modelo.addColumn("Periodo de Pagos");
            modelo.addColumn("Vacaciones");
            modelo.addColumn("Tiempo de Vacaciones");
            modelo.addColumn("Fecha de Puesto");
            modelo.addColumn("Estado");
        }

        // Limpiar la tabla antes de agregar nuevos datos
        modelo.setRowCount(0);

        // Consulta SQL
        String sql = "SELECT pdt.idPDT, w.nombre AS Trabajador, tdt.nombre AS 'Tipo de Trabajo', pdt.pagoPorHora AS 'Pago por Hora', "
                + "pdt.sueldo AS Sueldo, o.descripcion AS Overtime, h.descripcion AS Horario, p.descripcion AS 'Periodo de Pagos', "
                + "v.descripcion AS Vacaciones, pdt.tiempoVacaciones AS 'Periodo de Vacaciones', pdt.fechaDePuesto AS 'Fecha de Puesto', "
                + "pdt.estado AS Estado "
                + "FROM puestodetrabajo pdt "
                + "INNER JOIN worker w ON pdt.idTrabajador=w.idWorker "
                + "INNER JOIN tiposdetrabajos tdt ON pdt.idTDT=tdt.id "
                + "INNER JOIN overtime o ON pdt.id_overtime=o.id "
                + "INNER JOIN horario h ON pdt.id_horario=h.id "
                + "INNER JOIN periodo p ON pdt.id_periodo=p.id "
                + "INNER JOIN vacaciones v ON pdt.id_vacaciones=v.id";

            try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {

                // Recorrer los resultados y agregar las filas al modelo de la tabla
                while (rs.next()) {
                    Object[] fila = new Object[12];
                    fila[0] = rs.getString("idPDT");
                    fila[1] = rs.getString("Trabajador");
                    fila[2] = rs.getString("Tipo de Trabajo");
                    fila[3] = rs.getString("Pago por Hora");
                    fila[4] = rs.getString("Sueldo");
                    fila[5] = rs.getString("Overtime");
                    fila[6] = rs.getString("Horario");
                    fila[7] = rs.getString("Periodo de Pagos");
                    fila[8] = rs.getString("Vacaciones");
                    fila[9] = rs.getString("Periodo de Vacaciones");
                    fila[10] = rs.getString("Fecha de Puesto");
                    fila[11] = rs.getString("Estado");

                    // Agregar la fila al modelo
                    modelo.addRow(fila);
                }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar los registros: " + e.getMessage());
        } finally {
            // Devolver la conexión al pool
            if (connection != null) {
                Conectar.getInstancia().devolverConexion(connection);
            }
        }

        // Actualizar la tabla con el nuevo modelo
        Tabla.setModel(modelo);
    }

    public void Insertar() {

        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        CallableStatement cs = null;

        try {
            // Validar y convertir los valores de IdTrabajador e IdTipoDeTrabajo
            int idTrabajador = Integer.parseInt(txtIdtrabajador.getText());
            int idTipoDeTrabajo = Integer.parseInt(txtIdTipoDeTrabajo.getText());

            // Inicializar variables para pago por hora y sueldo
            double pagoPorHora = 0.0;
            double sueldo = 0.0;

            // Validar y convertir los valores de pago por hora y sueldo
            try {
                if (!txtPagoPorHora.getText().trim().isEmpty()) {
                    pagoPorHora = Double.parseDouble(txtPagoPorHora.getText().trim());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El valor de 'Pago por Hora' no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;  // Detenemos la ejecución si el valor no es válido
            }

            try {
                if (!txtSueldo.getText().trim().isEmpty()) {
                    sueldo = Double.parseDouble(txtSueldo.getText().trim());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El valor de 'Sueldo' no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;  // Detenemos la ejecución si el valor no es válido
            }

            // Asegurarse de que uno de los valores sea cero
            if (pagoPorHora > 0) {
                sueldo = 0.0;
            } else if (sueldo > 0) {
                pagoPorHora = 0.0;
            }

            // Validar y convertir los valores de OverTime, Horario, Periodo, y Vacaciones
            int idOverTime = Integer.parseInt(txtIdOverTime.getText());
            int idHorario = Integer.parseInt(txtIdHorario.getText());
            int idPeriodo = Integer.parseInt(txtIdPeriodo.getText());
            int idVacaciones = Integer.parseInt(txtIdVacaciones.getText());
            int tiempoVacacionesValue = Integer.parseInt(txtVacaciones.getText());

            // Obtener la fecha de ingreso del JDateChooser como java.util.Date
            java.util.Date fecha = dateInicioPuesto.getDate();
            if (fecha == null) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese una fecha de ingreso válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Convertir la fecha a java.sql.Date
            java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime());

            // Preparar la consulta SQL para insertar el registro
            String consulta = "INSERT INTO puestodetrabajo (idTrabajador, idTDT, pagoPorHora, sueldo, id_overtime, id_horario, id_periodo, id_vacaciones, tiempoVacaciones, fechaDePuesto, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Activo')";

     
            cs = connection.prepareCall(consulta);
            cs.setInt(1, idTrabajador);
            cs.setInt(2, idTipoDeTrabajo);

            // Si el valor es mayor a 0 lo asignamos, si no, ponemos NULL
            if (pagoPorHora > 0) {
                cs.setDouble(3, pagoPorHora);
            } else {
                cs.setNull(3, java.sql.Types.DOUBLE);
            }

            if (sueldo > 0) {
                cs.setDouble(4, sueldo);
            } else {
                cs.setNull(4, java.sql.Types.DOUBLE);
            }

            cs.setInt(5, idOverTime);
            cs.setInt(6, idHorario);
            cs.setInt(7, idPeriodo);
            cs.setInt(8, idVacaciones);
            cs.setInt(9, tiempoVacacionesValue);
            cs.setDate(10, fechaSQL);

            // Ejecutar la consulta
            cs.execute();

            JOptionPane.showMessageDialog(null, "Registro insertado exitosamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en la conversión de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo insertar el registro. Error: " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cerrar recursos
            try {
                if (cs != null) {
                    cs.close();
                }
                // Devolver la conexión al pool
                if (connection != null) {
                    Conectar.getInstancia().devolverConexion(connection);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Metodo para seleccionar y que se muestre
    public void Eliminar(JTextField IdPDT) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        setIdPDT(Integer.parseInt(IdPDT.getText()));

        String consulta = "DELETE FROM puestodetrabajo WHERE idPDT=?";

        try {
     
            CallableStatement cs = connection.prepareCall(consulta);
            cs.setInt(1, getIdPDT());
            cs.execute();
            JOptionPane.showMessageDialog(null, "Se eliminó correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar, error " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void Seleccionar(JTable Tabla, JTextField IdPDT, JComboBox Trabajador, JComboBox TDT, JTextField PPH, JTextField sueldo, JComboBox OverTime, JComboBox Horario, JComboBox Periodo, JComboBox Vacaciones, JTextField tiempoVacaciones, JDateChooser dateInicioPuesto) {
        try {
            int fila = Tabla.getSelectedRow(); // Obtener la fila seleccionada

            if (fila >= 0) {
                // Obtener el valor de IdPDT y convertir a entero
                String valorDeIdPDT = Tabla.getValueAt(fila, 0).toString();
                try {
                    int idPDTEntero = Integer.parseInt(valorDeIdPDT); // Asegura que IdPDT es un entero válido
                    IdPDT.setText(String.valueOf(idPDTEntero));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Error en la conversión de IdPDT: " + e.getMessage());
                    return;
                }

                // Asignar otros valores a los campos de la interfaz
                Trabajador.setSelectedItem(Tabla.getValueAt(fila, 1).toString());
                TDT.setSelectedItem(Tabla.getValueAt(fila, 2).toString());
                PPH.setText(Tabla.getValueAt(fila, 3).toString());
                sueldo.setText(Tabla.getValueAt(fila, 4).toString());
                OverTime.setSelectedItem(Tabla.getValueAt(fila, 5).toString());
                Horario.setSelectedItem(Tabla.getValueAt(fila, 6).toString());
                Periodo.setSelectedItem(Tabla.getValueAt(fila, 7).toString());
                Vacaciones.setSelectedItem(Tabla.getValueAt(fila, 8).toString());
                tiempoVacaciones.setText(Tabla.getValueAt(fila, 9).toString());

                // Convertir la fecha de texto a formato Date
                String fechaTexto = Tabla.getValueAt(fila, 10).toString();
                if (fechaTexto != null && !fechaTexto.isEmpty()) {
                    java.util.Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaTexto);
                    dateInicioPuesto.setDate(fecha);
                } else {
                    dateInicioPuesto.setDate(null);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }
        } catch (HeadlessException | ParseException e) {
            JOptionPane.showMessageDialog(null, "Error de selección: " + e.toString());
        }
    }

    public void Modificar(JTextField idPDTField, JTextField idTrabajadorField, JTextField TipoDeTrabajoField, JTextField pagoPorHoraField, JTextField sueldoField, JTextField idOvertimeField, JTextField idHorarioField, JTextField idPeriodoField, JTextField idVacacionesField, JTextField tiempoVacacionesField, JDateChooser dateInicioPuesto) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        PreparedStatement ps = null;

        try {
            // Verificar que los JTextField no estén vacíos antes de hacer la conversión
            if (idPDTField.getText().trim().isEmpty() || idTrabajadorField.getText().trim().isEmpty() || TipoDeTrabajoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;  // Detener la ejecución si faltan datos
            }

            // Obtener los valores de los JTextFields y convertirlos a sus respectivos tipos
            int idPDT = Integer.parseInt(idPDTField.getText().trim());
            int idTrabajador = Integer.parseInt(idTrabajadorField.getText().trim());
            int idTDT = Integer.parseInt(TipoDeTrabajoField.getText().trim());

            // Validar si 'pagoPorHora' y 'sueldo' tienen valores válidos
            double pagoPorHora = 0.0;
            double sueldo = 0.0;

            if (!pagoPorHoraField.getText().trim().isEmpty()) {
                pagoPorHora = Double.parseDouble(pagoPorHoraField.getText().trim());
            }

            if (!sueldoField.getText().trim().isEmpty()) {
                sueldo = Double.parseDouble(sueldoField.getText().trim());
            }

            // Asegurarse de que uno de los valores sea cero
            if (pagoPorHora > 0) {
                sueldo = 0.0;
            } else if (sueldo > 0) {
                pagoPorHora = 0.0;
            }

            int idOvertime = Integer.parseInt(idOvertimeField.getText().trim());
            int idHorario = Integer.parseInt(idHorarioField.getText().trim());
            int idPeriodo = Integer.parseInt(idPeriodoField.getText().trim());
            int idVacaciones = Integer.parseInt(idVacacionesField.getText().trim());
            int tiempoVacaciones = Integer.parseInt(tiempoVacacionesField.getText().trim());

            // Obtener la fecha desde el JDateChooser
            java.util.Date ingreso = dateInicioPuesto.getDate();
            if (ingreso == null) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese una fecha de ingreso válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.sql.Date fechaSQL = new java.sql.Date(ingreso.getTime());

            // Obtener la conexión desde el pool
            
            connection.setAutoCommit(false);  // Desactivar auto-commit

            String consulta = "UPDATE puestodetrabajo SET idTrabajador = ?, idTDT = ?, pagoPorHora = ?, sueldo = ?, id_overtime = ?, id_horario = ?, id_periodo = ?, id_vacaciones = ?, tiempoVacaciones = ?, fechaDePuesto = ?, estado = 'Activo' WHERE idPDT = ?";

            ps = connection.prepareStatement(consulta);
            ps.setInt(1, idTrabajador);
            ps.setInt(2, idTDT);
            ps.setDouble(3, pagoPorHora);
            ps.setDouble(4, sueldo);
            ps.setInt(5, idOvertime);
            ps.setInt(6, idHorario);
            ps.setInt(7, idPeriodo);
            ps.setInt(8, idVacaciones);
            ps.setInt(9, tiempoVacaciones);
            ps.setDate(10, fechaSQL);
            ps.setInt(11, idPDT);

            // Ejecutar la consulta
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                connection.commit();
                JOptionPane.showMessageDialog(null, "Modificación exitosa.");
            } else {
                connection.rollback();
                JOptionPane.showMessageDialog(null, "No se encontraron registros para modificar.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en la conversión de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(null, "Error al modificar en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true);
                    Conectar.getInstancia().devolverConexion(connection);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean verificarRegistroExiste(int idPDT) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        
        String consulta = "SELECT COUNT(*) FROM puestodetrabajo WHERE idPDT = ?";

        
            try (PreparedStatement ps = connection.prepareStatement(consulta)) {
                ps.setInt(1, idPDT);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar el registro: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
        return false;
    }

    public int obtenerIdPuestoActivo(int idTrabajador) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        int idPDT = -1;
        String sql = "SELECT idPDT FROM puestodetrabajo WHERE idTrabajador = ? AND estado = 'Activo'";

            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.setInt(1, idTrabajador);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        idPDT = rs.getInt("idPDT");
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
        return idPDT;
    }

    public String obtenerPeriodoPago(int idPDT) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "SELECT p.descripcion FROM periodo p JOIN puestodetrabajo pt ON p.id = pt.id_periodo WHERE pt.idPDT = ?";

            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.setInt(1, idPDT);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("descripcion");
                    }
                }
        } catch (SQLException ex) {
            System.out.println("Error al obtener el periodo de pago: " + ex.getMessage());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }

        return "Semanal"; // Valor predeterminado
    }

    public boolean accion(String estado, int id) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "UPDATE puestodetrabajo SET estado = ? WHERE idPDT = ?";
        try {
            PreparedStatement ps;
            ps = connection.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        cbxTDT = new javax.swing.JComboBox<>();
        cbxTrabajador = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        btnNuevoTipoDeTrabajo = new javax.swing.JButton();
        txtIdtrabajador = new javax.swing.JTextField();
        txtIdTipoDeTrabajo = new javax.swing.JTextField();
        cbxOverTime = new javax.swing.JComboBox<>();
        txtIdOverTime = new javax.swing.JTextField();
        txtIdHorario = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbxHorario = new javax.swing.JComboBox<>();
        txtIdPeriodo = new javax.swing.JTextField();
        cbxPeriodo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dateInicioPuesto = new com.toedter.calendar.JDateChooser();
        cbxVacaciones = new javax.swing.JComboBox<>();
        txtIdVacaciones = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtSueldo = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        txtIdPDT = new javax.swing.JTextField();
        txtPagoPorHora = new javax.swing.JTextField();
        txtVacaciones = new javax.swing.JTextField();
        btnGuia = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        btnDesactivar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPuestosDeTrabajo = new javax.swing.JTable();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Puestos de Trabajo");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Tipo de Trabajo");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        jLabel2.setText("Pago por hora");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jLabel3.setText("Overtime");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, -1, -1));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1021, 70, 110, -1));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel1.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 160, -1, -1));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 120, 110, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1021, 210, 110, -1));

        cbxTDT.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTDTItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxTDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 210, -1));

        cbxTrabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTrabajadorItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxTrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 210, -1));

        jLabel4.setText("Trabajador");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        btnNuevoTipoDeTrabajo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-producto.png"))); // NOI18N
        btnNuevoTipoDeTrabajo.setText("Nuevo Tipo de Trabajo");
        btnNuevoTipoDeTrabajo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoTipoDeTrabajoActionPerformed(evt);
            }
        });
        jPanel1.add(btnNuevoTipoDeTrabajo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, -1, -1));
        jPanel1.add(txtIdtrabajador, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 80, -1));
        jPanel1.add(txtIdTipoDeTrabajo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 120, 80, -1));

        cbxOverTime.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxOverTimeItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxOverTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, 100, -1));

        txtIdOverTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdOverTimeActionPerformed(evt);
            }
        });
        jPanel1.add(txtIdOverTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 30, 90, -1));
        jPanel1.add(txtIdHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 60, 90, -1));

        jLabel5.setText("Horario");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 60, -1, -1));

        cbxHorario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbxHorario.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxHorarioItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 60, 100, -1));
        jPanel1.add(txtIdPeriodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 100, 90, -1));

        cbxPeriodo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxPeriodoItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxPeriodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 100, 100, -1));

        jLabel6.setText("Periodo");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 100, -1, -1));

        jLabel7.setText("Fecha Inicio Puesto");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, -1, -1));
        jPanel1.add(dateInicioPuesto, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 170, 150, -1));

        cbxVacaciones.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxVacacionesItemStateChanged(evt);
            }
        });
        jPanel1.add(cbxVacaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 130, 100, -1));
        jPanel1.add(txtIdVacaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 130, 90, -1));

        jLabel8.setText("Vacaciones");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 140, -1, -1));

        jLabel9.setText("Tiempo de Vacaciones");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 30, -1, -1));

        jLabel10.setText("Sueldo");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, -1, -1));
        jPanel1.add(txtSueldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 160, 80, -1));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 20, 110, -1));
        jPanel1.add(txtIdPDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 80, 30));
        jPanel1.add(txtPagoPorHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 110, -1));
        jPanel1.add(txtVacaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 60, 100, -1));

        btnGuia.setText("Guia");
        btnGuia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuiaActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuia, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 210, -1, -1));

        btnActivar.setText("Activar");
        btnActivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivarActionPerformed(evt);
            }
        });
        jPanel1.add(btnActivar, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, -1, -1));

        btnDesactivar.setText("Inactivar");
        btnDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarActionPerformed(evt);
            }
        });
        jPanel1.add(btnDesactivar, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 210, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1160, 250));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbPuestosDeTrabajo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbPuestosDeTrabajo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPuestosDeTrabajoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbPuestosDeTrabajo);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 5, 1120, 270));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 1140, 290));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnNuevoTipoDeTrabajoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoTipoDeTrabajoActionPerformed
        Bases.TipoDeTrabajo objTDT = new Bases.TipoDeTrabajo();
        objTDT.setVisible(true);
    }//GEN-LAST:event_btnNuevoTipoDeTrabajoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (cbxTDT.getSelectedItem() != null) {
            MostrarCodigoTrabajador(cbxTrabajador, txtIdtrabajador);
            Insertar();
            Mostrar(tbPuestosDeTrabajo);
            txtIdPDT.setText("");
            txtIdtrabajador.setText("");
            txtIdTipoDeTrabajo.setText("");
            txtPagoPorHora.setText("");
            cbxTrabajador.setSelectedItem("");
            cbxTDT.setSelectedItem("");
            cbxOverTime.setSelectedItem("");
            txtVacaciones.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un tipo de trabajo antes de guardar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        Modificar(txtIdPDT, txtIdtrabajador, txtIdTipoDeTrabajo, txtPagoPorHora, txtSueldo, txtIdOverTime, txtIdHorario, txtIdPeriodo, txtIdVacaciones, txtIdVacaciones, dateInicioPuesto);
        Mostrar(tbPuestosDeTrabajo);
        txtIdPDT.setText("");
        txtIdtrabajador.setText("");
        txtIdTipoDeTrabajo.setText("");
        txtPagoPorHora.setText("");
        cbxTrabajador.setSelectedItem("");
        cbxTDT.setSelectedItem("");
        cbxOverTime.setSelectedItem("");
        txtVacaciones.setText("");
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        txtIdPDT.setText("");
        txtIdtrabajador.setText("");
        txtIdTipoDeTrabajo.setText("");
        txtPagoPorHora.setText("");
        cbxTrabajador.setSelectedItem("");
        cbxTDT.setSelectedItem("");
        cbxOverTime.setSelectedItem("");
        txtVacaciones.setText("");
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        try {
            Seleccionar(tbPuestosDeTrabajo, txtIdPDT, cbxTrabajador, cbxTDT, txtPagoPorHora, txtSueldo, cbxOverTime, cbxHorario, cbxPeriodo, cbxVacaciones, txtVacaciones, dateInicioPuesto);
        } catch (Exception ex) {
            Logger.getLogger(PuestoDeTrabajo.class.getName()).log(Level.SEVERE, null, ex);
        }
        Eliminar(txtIdPDT);
        Mostrar(tbPuestosDeTrabajo);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void cbxTrabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTrabajadorItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            MostrarCodigoTrabajador(cbxTrabajador, txtIdtrabajador);
        }
    }//GEN-LAST:event_cbxTrabajadorItemStateChanged

    private void cbxTDTItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTDTItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            MostrarCodigoTipodeTrabajo(cbxTDT, txtIdTipoDeTrabajo);
        }
    }//GEN-LAST:event_cbxTDTItemStateChanged

    private void txtIdOverTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdOverTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdOverTimeActionPerformed

    private void cbxOverTimeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxOverTimeItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            MostrarCodigoPorOverTime(cbxOverTime, txtIdOverTime);
        }
    }//GEN-LAST:event_cbxOverTimeItemStateChanged

    private void tbPuestosDeTrabajoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPuestosDeTrabajoMouseClicked
        try {
            Seleccionar(tbPuestosDeTrabajo, txtIdPDT, cbxTrabajador, cbxTDT, txtPagoPorHora, txtSueldo, cbxOverTime, cbxHorario, cbxPeriodo, cbxVacaciones, txtVacaciones, dateInicioPuesto);
        } catch (Exception ex) {
            Logger.getLogger(PuestoDeTrabajo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tbPuestosDeTrabajoMouseClicked

    private void cbxHorarioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxHorarioItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && cbxHorario.getSelectedItem() != null) {
            MostrarCodigoHorario(cbxHorario, txtIdHorario);
        }
    }//GEN-LAST:event_cbxHorarioItemStateChanged

    private void cbxPeriodoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxPeriodoItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && cbxPeriodo.getSelectedItem() != null) {
            MostrarCodigoPeriodo(cbxPeriodo, txtIdPeriodo);
        }
    }//GEN-LAST:event_cbxPeriodoItemStateChanged

    private void cbxVacacionesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxVacacionesItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && cbxVacaciones.getSelectedItem() != null) {
            MostrarCodigoVacaciones(cbxVacaciones, txtIdVacaciones);
        }
    }//GEN-LAST:event_cbxVacacionesItemStateChanged

    private void btnGuiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuiaActionPerformed
        JOptionPane.showMessageDialog(null, "PUESTOS DE TRABAJO\n"
                + "Tenemos una una casilla de Trabajadores, una casilla para elección de Puestos de Trabajo\n"
                + "otras casillas para determinar las condiciones de trabajo y la fecha de ingreso\n"
                + "Botón GUARDAR para guardar los datos que se mostraran en una Tabla\n"
                + "Botón CANCELAR para limpiar las casillas\n"
                + "Botón MODIFICAR  seleccionamos una fila de la Tabla para modificar los datos y presionamos Modificar\n"
                + "Botón ELIMINAR seleccionamos la fila de la Tabla que queremos eliminar y click en Eliminar\n"
                + "Botón ACTIVAR para activar un trabajador que habia sido desactivado\n"
                + "Botón DESACTIVAR´para desactivar a un Trabajador\n"
                + "Botón NUEVO TIPO DE TRABAJO cuando no tengamos el tipo buscado podemos crearlo, aparecera otra interfaz\n"
                + "al presionar este botón");
    }//GEN-LAST:event_btnGuiaActionPerformed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        int fila = tbPuestosDeTrabajo.getSelectedRow();
        int id = Integer.parseInt(txtIdPDT.getText());
        if (accion("Activo", id)) {
            JOptionPane.showMessageDialog(null, "Activado");
            Mostrar(tbPuestosDeTrabajo);
        } else {
            JOptionPane.showMessageDialog(null, "Error al Activar");
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        int fila = tbPuestosDeTrabajo.getSelectedRow();
        int id = Integer.parseInt(txtIdPDT.getText());
        if (accion("Inactivo", id)) {
            JOptionPane.showMessageDialog(null, "Inactivado");
            Mostrar(tbPuestosDeTrabajo);
        } else {
            JOptionPane.showMessageDialog(null, "Error al Inactivar");
        }
    }//GEN-LAST:event_btnDesactivarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnActivar;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnDesactivar;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuia;
    public javax.swing.JButton btnModificar;
    public javax.swing.JButton btnNuevoTipoDeTrabajo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxHorario;
    private javax.swing.JComboBox<String> cbxOverTime;
    private javax.swing.JComboBox<String> cbxPeriodo;
    private javax.swing.JComboBox<String> cbxTDT;
    private javax.swing.JComboBox<String> cbxTrabajador;
    private javax.swing.JComboBox<String> cbxVacaciones;
    private com.toedter.calendar.JDateChooser dateInicioPuesto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbPuestosDeTrabajo;
    private javax.swing.JTextField txtIdHorario;
    private javax.swing.JTextField txtIdOverTime;
    private javax.swing.JTextField txtIdPDT;
    private javax.swing.JTextField txtIdPeriodo;
    private javax.swing.JTextField txtIdTipoDeTrabajo;
    private javax.swing.JTextField txtIdVacaciones;
    private javax.swing.JTextField txtIdtrabajador;
    private javax.swing.JTextField txtPagoPorHora;
    private javax.swing.JTextField txtSueldo;
    private javax.swing.JTextField txtVacaciones;
    // End of variables declaration//GEN-END:variables

    public void MostrarTrabajador(JComboBox comboTrabajador) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "SELECT * FROM worker";

            try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                comboTrabajador.removeAllItems();
                while (rs.next()) {
                    comboTrabajador.addItem(rs.getString("nombre"));
                }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Trabajadores: " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoTrabajador(JComboBox trabajador, JTextField IdTrabajador) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        if (trabajador.getSelectedItem() != null) {
            String consulta = "SELECT idWorker FROM worker WHERE nombre=?";

            try {
                CallableStatement cs = connection.prepareCall(consulta);
                cs.setString(1, trabajador.getSelectedItem().toString());
                cs.execute();

                ResultSet rs = cs.executeQuery();
                if (rs.next()) {
                    IdTrabajador.setText(rs.getString("idWorker"));
                } else {
                    IdTrabajador.setText("");
                }
                rs.close(); // Asegurarse de cerrar el ResultSet

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al mostrar: " + e.toString());
            } finally {
                // Devolver la conexión al pool
                Conectar.getInstancia().devolverConexion(connection);
            }
        } else {
            IdTrabajador.setText("");
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un trabajador válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void MostrarTipoDeTrabajo(JComboBox comboTipoDeTrabajo) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "SELECT * FROM tiposdetrabajos";

            try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                comboTipoDeTrabajo.removeAllItems();
                while (rs.next()) {
                    comboTipoDeTrabajo.addItem(rs.getString("nombre"));
                }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tipos de Trabajo: " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoTipodeTrabajo(JComboBox tipoDeTrabajo, JTextField IdTipoDeTrabajo) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String consulta = "select tiposdetrabajos.id from tiposdetrabajos where tiposdetrabajos.nombre=?";

        if (tipoDeTrabajo.getSelectedItem() != null) {
            try {
                CallableStatement cs = connection.prepareCall(consulta);
                cs.setString(1, tipoDeTrabajo.getSelectedItem().toString());
                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    IdTipoDeTrabajo.setText(rs.getString("id"));
                } else {
                    IdTipoDeTrabajo.setText("");
                }
                rs.close(); // Asegurarse de cerrar el ResultSet

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
            } finally {
                // Devolver la conexión al pool
                Conectar.getInstancia().devolverConexion(connection);
            }
        } else {
            IdTipoDeTrabajo.setText("");
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un tipo de trabajo válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void MostrarOverTime(JComboBox<String> comboOverTime) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "SELECT * FROM overtime";
     
try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                comboOverTime.removeAllItems();
                while (rs.next()) {
                    comboOverTime.addItem(rs.getString("descripcion"));
                }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Overtime: " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoPorOverTime(JComboBox<String> overtimeCombo, JTextField idOvertime) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        if (overtimeCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione un valor de OverTime.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String consulta = "SELECT id FROM overtime WHERE descripcion = ?";
            try (PreparedStatement ps = connection.prepareStatement(consulta)) {
                ps.setString(1, overtimeCombo.getSelectedItem().toString());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idOvertime.setText(rs.getString("id"));
                    }
                }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar: " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarHorario(JComboBox comboHorario) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "select * from horario";
            try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                comboHorario.removeAllItems();
                while (rs.next()) {
                    comboHorario.addItem(rs.getString("descripcion"));
                }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Horario: " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoHorario(JComboBox Horario, JTextField IdHorario) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String consulta = "select id from horario where descripcion=?";

        if (Horario.getSelectedItem() != null) {
            try {
                CallableStatement cs = connection.prepareCall(consulta);
                cs.setString(1, Horario.getSelectedItem().toString());
                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    IdHorario.setText(rs.getString("id"));
                } else {
                    IdHorario.setText("");
                }
                rs.close(); // Asegurarse de cerrar el ResultSet

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
            } finally {
                // Devolver la conexión al pool
                Conectar.getInstancia().devolverConexion(connection);
            }
        } else {
            IdHorario.setText("");
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un tipo de horario válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void MostrarPeriodo(JComboBox comboPeriodo) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "select * from periodo";
            try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                comboPeriodo.removeAllItems();
                while (rs.next()) {
                    comboPeriodo.addItem(rs.getString("descripcion"));
                }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Periodo: " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoPeriodo(JComboBox Periodo, JTextField IdPeriodo) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String consulta = "select id from periodo where descripcion=?";

        if (Periodo.getSelectedItem() != null) {
            try {
                CallableStatement cs = connection.prepareCall(consulta);
                cs.setString(1, Periodo.getSelectedItem().toString());
                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    IdPeriodo.setText(rs.getString("id"));
                } else {
                    IdPeriodo.setText("");
                }
                rs.close(); // Asegurarse de cerrar el ResultSet

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
            } finally {
                // Devolver la conexión al pool
                Conectar.getInstancia().devolverConexion(connection);
            }
        } else {
            IdPeriodo.setText("");
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un tipo de horario periodo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void MostrarVacaciones(JComboBox comboVacaciones) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String sql = "select * from vacaciones";
     
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                comboVacaciones.removeAllItems();
                while (rs.next()) {
                    comboVacaciones.addItem(rs.getString("descripcion"));
                }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Periodo: " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoVacaciones(JComboBox Vacaciones, JTextField IdVacaciones) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida
    if (connection == null) {
        throw new RuntimeException("Error: La conexión a la base de datos es nula.");
    }
        String consulta = "select id from vacaciones where descripcion=?";

        if (Vacaciones.getSelectedItem() != null) {
            try {
                CallableStatement cs = connection.prepareCall(consulta);
                cs.setString(1, Vacaciones.getSelectedItem().toString());
                cs.execute();

                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    IdVacaciones.setText(rs.getString("id"));
                } else {
                    IdVacaciones.setText("");
                }
                rs.close(); // Asegurarse de cerrar el ResultSet

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
            } finally {
                // Devolver la conexión al pool
                Conectar.getInstancia().devolverConexion(connection);
            }
        } else {
            IdVacaciones.setText("");
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un tipo de horario periodo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}
