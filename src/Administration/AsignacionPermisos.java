package Administration;

import Admission.*;
import Register.*;
import Reports.*;
import Bases.Permiso;
import Bases.PermisoManager;
import Presentation.VentanaPrincipal;
import java.sql.Statement;
import conectar.Conectar;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class AsignacionPermisos extends javax.swing.JInternalFrame {

    private int rolId;
    private boolean isUpdatingTable = false; // Declaración de la bandera a nivel de clase

    private VentanaPrincipal vp;
    private PermisoManager permisoManager;
    private List<Permiso> permisos;

    private Usuarios u;
    private Roles r;
    private AsignacionPermisos ap;
    private Empresas em;
    private Trabajadores tb;
    private Tarifario tf;
    private Productos p;
    private Formularios f;
    private Proveedor pr;
    private Convenios cv;
    private BusquedaDeConvenios bc;
    private AsignacionTrabajos at;
    private PuestoDeTrabajo pt;
    private FormaDePago fp;
    private Menus ms;
    private Clientes c;
    private Marcas mc;
    private Unidades un;
    private tipos_pagosgenerales tp;
    private TipoProductosMateriales tpm;
    private TipoMaquinariasYVehiculos tm;
    private Localizacion l;
    private Configuracion cf;
    private Orden os;
    private VerOrdenes vo;
    private Ventas vt;
    private ComprasProductosMateriales cpm;
    private CompraEquiposVehiculos cev;
    private Gastos_Generales gg;
    private Kardex k;
    private Cotizaciones ct;
    private Cancelaciones cc;
    private RTrabajosRealizados tr;
    private Estadisticas es;
    private DeudasPorPagar dpp;
    private DeudasPorCobrar dpc;
    private HorasTrabajadas ht;
    private Sueldos sd;
    private Stock st;
    private Trabajos tbs;
    private BudgetManager b;

    // Tablas de permisos
    JTable tbUsuTrab = null;

    private void setupTableModelListener(JTable table, String tableName) {
        TableModelListener listener = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();

                    // Validar que la fila y la columna son válidas
                    if (row < 0 || column < 0 || row >= table.getRowCount() || column >= table.getColumnCount()) {
                        return;
                    }

                    // Validar que el valor obtenido no sea nulo
                    Object value = table.getValueAt(row, column);
                    if (value == null) {
                        return; // Si el valor es nulo, no hacemos nada para evitar el NullPointerException
                    }

                    // Verificar si el valor puede ser casteado a Boolean antes de hacer el cast
                    if (!(value instanceof Boolean)) {
                        System.out.println("Error: El valor en la celda no es un Boolean en la fila " + row + ", columna " + column);
                        return;
                    }

                    // Remover el oyente para evitar ciclos infinitos
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.removeTableModelListener(this);

                    try {
                        // Manejar la columna "Todo" para seleccionar/desmarcar todas las opciones
                        if (column == 6) { // Suponiendo que la columna "Todo" está en el índice 6
                            Boolean isSelected = (Boolean) value;

                            // Actualizar las columnas de "Visualizar", "Agregar", "Editar", "Eliminar"
                            for (int i = 2; i <= 5; i++) {
                                table.setValueAt(isSelected, row, i);
                            }
                        }

                        // Verificar si la columna es una de las de permisos individuales (Visualizar, Agregar, Editar, Eliminar)
                        if (column >= 2 && column <= 5) {
                            Boolean selected = (Boolean) value;

                            // Verificar si todos los permisos están seleccionados
                            boolean allSelected = true;
                            for (int i = 2; i <= 5; i++) {
                                Object cellValue = table.getValueAt(row, i);
                                if (cellValue == null || !(cellValue instanceof Boolean) || !(Boolean) cellValue) {
                                    allSelected = false;
                                    break;
                                }
                            }
                            table.setValueAt(allSelected, row, 6); // Actualizar la columna "Todo"

                            // Guardar permisos en la base de datos
                            String menuName = getMenuNameByRow(tableName, row);
                            String submenuName = getSubmenuNameByRow(tableName, row);

                            // Validar que los nombres no estén vacíos
                            if (!menuName.isEmpty() && !submenuName.isEmpty()) {
                                // Llamar a los métodos específicos basados en el nombre del submenú y la acción
                                handlePermission(menuName, submenuName, column, selected);
                            } else {
                                System.out.println("Error: Nombres de menú o submenú no válidos.");
                            }
                        }
                    } finally {
                        // Volver a agregar el oyente después de los cambios
                        model.addTableModelListener(this);
                    }
                }
            }
        };

        table.getModel().addTableModelListener(listener);
    }

    // Método para inicializar las tablas con valores booleanos predeterminados
    private void inicializarTablas() {
        limpiarTabla(tbAdministracion);
        limpiarTabla(tbAdmision);
        limpiarTabla(tbRegistros);
        limpiarTabla(tbReportes);

        for (int i = 0; i < tbAdministracion.getRowCount(); i++) {
            for (int j = 2; j <= 6; j++) {
                tbAdministracion.setValueAt(false, i, j);
            }
        }

        for (int i = 0; i < tbAdmision.getRowCount(); i++) {
            for (int j = 2; j <= 6; j++) {
                tbAdmision.setValueAt(false, i, j);
            }
        }

        for (int i = 0; i < tbRegistros.getRowCount(); i++) {
            for (int j = 2; j <= 6; j++) {
                tbRegistros.setValueAt(false, i, j);
            }
        }

        for (int i = 0; i < tbReportes.getRowCount(); i++) {
            for (int j = 2; j <= 6; j++) {
                tbReportes.setValueAt(false, i, j);
            }
        }
    }

    // Llama a este método después de inicializar los componentes
    public AsignacionPermisos(VentanaPrincipal vp) throws SQLException {
        Connection connection = null;

        connection = Conectar.getInstancia().obtenerConexion();

        if (connection == null || connection.isClosed()) {
            throw new SQLException("No se pudo obtener la conexión a la base de datos.");
        }
        this.vp = vp;
        this.permisoManager = new PermisoManager(connection); // Instancia para manejar permisos

        initComponents();

        // Inicializa la variable u aquí
        this.u = new Usuarios(); // Asegúrate de usar la clase correspondiente.
        this.r = new Roles();
        this.ap = this;
        this.em = new Empresas();
        this.tb = new Trabajadores();
        this.tf = new Tarifario();
        this.p = new Productos();
        this.f = new Formularios();
        this.pr = new Proveedor();
        this.cv = new Convenios();
        this.bc = new BusquedaDeConvenios();
        this.at = new AsignacionTrabajos();
        this.pt = new PuestoDeTrabajo();
        this.fp = new FormaDePago();
        this.ms = new Menus();

        this.c = new Clientes();
        this.mc = new Marcas();
        this.un = new Unidades();
        this.tp = new tipos_pagosgenerales();
        this.tpm = new TipoProductosMateriales();
        this.tm = new TipoMaquinariasYVehiculos();
        this.l = new Localizacion();
        this.cf = new Configuracion();

        this.os = new Orden();
        this.vo = new VerOrdenes();
        this.vt = new Ventas();
        this.cpm = new ComprasProductosMateriales();
        this.cev = new CompraEquiposVehiculos();
        this.gg = new Gastos_Generales();
        this.k = new Kardex();
        this.ct = new Cotizaciones();
        this.cc = new Cancelaciones();
        this.tr = new RTrabajosRealizados();
        this.es = new Estadisticas();
        this.dpp = new DeudasPorPagar();
        this.dpc = new DeudasPorCobrar();
        this.ht = new HorasTrabajadas();
        this.sd = new Sueldos();
        this.st = new Stock();
        this.tbs = new Trabajos();
        this.b = new BudgetManager();

        // Configuración adicional de componentes
        AutoCompleteDecorator.decorate(cbxTPU);
        MostrarTipodeUsuarioCombo(cbxTPU);

        // Configurar checkboxes en las tablas
        setupTableCheckBoxes(tbAdministracion);
        setupTableCheckBoxes(tbAdmision);
        setupTableCheckBoxes(tbRegistros);
        setupTableCheckBoxes(tbReportes);

        // Configurar listeners para los checkboxes principales
        setupMenuCheckboxListeners();

        // Configurar oyentes del modelo de tabla para cada tabla
        setupTableModelListener(tbAdministracion, "Administracion");
        setupTableModelListener(tbAdmision, "Admision");
        setupTableModelListener(tbRegistros, "Registros");
        setupTableModelListener(tbReportes, "Reportes");

        // Inicialmente deshabilitar los botones de cada menú
        btnAdministracion.setEnabled(false);
        btnAdmision.setEnabled(false);
        btnRegistros.setEnabled(false);
        btnReportes.setEnabled(false);

        // Forzamos que los botones permanezcan habilitados
        btnGrabar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(true);

// También aseguramos su visibilidad si en algún momento se ocultan
        btnGrabar.setVisible(true);
        btnModificar.setVisible(true);
        btnCancelar.setVisible(true);
        btnSalir.setVisible(true);
        
        cbxTPU.addItemListener(new ItemListener() {
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            int rolId = obtenerRolIdActual(); // Método que devuelve el ID del rol seleccionado
            cargarPermisosSubmenus(rolId);  // Cargar los permisos del rol seleccionado
        }
    }
});


//        DefaultTableModel model = (DefaultTableModel) table.getModel();
//        model.addTableModelListener(new TableModelListener() {
//            @Override
//            public void tableChanged(TableModelEvent e) {
//                // Aquí puedes colocar tu implementación del listener
//            }
//        });
    }

    private void setupMenuCheckboxListeners() {
        // Listeners simplificados para habilitar/deshabilitar tablas de permisos
        chAdministracion.addItemListener(e -> handleCheckboxChange(e, jScrollPane1, btnAdministracion, "Administracion", tbAdministracion));
        chAdmision.addItemListener(e -> handleCheckboxChange(e, jScrollPane2, btnAdmision, "Admision", tbAdmision));
        chRegistros.addItemListener(e -> handleCheckboxChange(e, jScrollPane3, btnRegistros, "Registros", tbRegistros));
        chReportes.addItemListener(e -> handleCheckboxChange(e, jScrollPane5, btnReportes, "Reportes", tbReportes));
    }

    private void handleCheckboxChange(ItemEvent e, JScrollPane scrollPane, JButton button, String menuName, JTable table) {
        boolean isSelected = e.getStateChange() == ItemEvent.SELECTED; // Verificar si el checkbox fue seleccionado o no
        button.setEnabled(isSelected); // Habilitar o deshabilitar el botón asociado al menú
        scrollPane.setVisible(isSelected); // Mostrar u ocultar la tabla correspondiente

        if (isSelected) {
            // Llenar la tabla con los datos del submenú correspondiente al menú seleccionado
            int menuId = obtenerMenuId(menuName); // Obtener el ID del menú basado en el nombre
            if (menuId != -1) {
                llenarTablaConSubmenu(table, menuId); // Llenar la tabla con los submenús
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo encontrar el menú especificado.");
            }
        } else {
            // Limpiar la tabla si se deselecciona el checkbox
            limpiarTabla(table); // Método para vaciar la tabla
        }
        updateLayout(); // Método para refrescar la interfaz
    }

    private void limpiarCheckboxesDeTabla(JTable table) {
        // Iterar sobre todas las filas y columnas que tienen checkboxes (asumimos que están en las columnas 2 a 5)
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 2; j <= 5; j++) {
                table.setValueAt(false, i, j); // Desmarcar todas las casillas de permisos
            }
        }
    }

    private void limpiarTabla(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Esto eliminará todas las filas de la tabla
    }

    // Método para limpiar las tablas antes de cargar nuevos datos
    private void limpiarTablas() {
        limpiarTabla(tbAdministracion);
        limpiarTabla(tbAdmision);
        limpiarTabla(tbRegistros);
        limpiarTabla(tbReportes);
    }

    private void setupTableCheckBoxes(JTable table) {
        for (int i = 2; i <= 6; i++) {  // Columnas que contienen booleanos
            TableColumn tc = table.getColumnModel().getColumn(i);
            tc.setCellEditor(table.getDefaultEditor(Boolean.class));   // Editor de celdas para booleans
            tc.setCellRenderer(table.getDefaultRenderer(Boolean.class)); // Renderizador de celdas para booleans
        }
    }

    private void prepararPantallaParaNuevoRol() {
        // Limpiar todas las tablas
        limpiarTablas();

        // Limpiar los checkboxes principales (si los tienes como chAdministracion, etc.)
        chAdministracion.setSelected(false);
        chAdmision.setSelected(false);
        chRegistros.setSelected(false);
        chReportes.setSelected(false);

    }

    // Método adicional para limpiar los checkboxes en las tablas
    private void limpiarCheckboxesEnTablas() {
        // Limpiar checkboxes en la tabla de Administración
        for (int i = 0; i < tbAdministracion.getRowCount(); i++) {
            for (int j = 2; j <= 6; j++) { // Columnas de Visualizar, Agregar, Editar, Eliminar, Todo
                tbAdministracion.setValueAt(false, i, j); // Asignar false a todas las celdas
            }
        }

        // Limpiar checkboxes en la tabla de Admisión
        for (int i = 0; i < tbAdmision.getRowCount(); i++) {
            for (int j = 2; j <= 6; j++) { // Columnas de Visualizar, Agregar, Editar, Eliminar, Todo
                tbAdmision.setValueAt(false, i, j);
            }
        }

        // Limpiar checkboxes en la tabla de Registros
        for (int i = 0; i < tbRegistros.getRowCount(); i++) {
            for (int j = 2; j <= 6; j++) { // Columnas de Visualizar, Agregar, Editar, Eliminar, Todo
                tbRegistros.setValueAt(false, i, j);
            }
        }

        // Limpiar checkboxes en la tabla de Reportes
        for (int i = 0; i < tbReportes.getRowCount(); i++) {
            for (int j = 2; j <= 6; j++) { // Columnas de Visualizar, Agregar, Editar, Eliminar, Todo
                tbReportes.setValueAt(false, i, j);
            }
        }

        // Notificar a las tablas que los datos han cambiado, forzar la actualización
        ((DefaultTableModel) tbAdministracion.getModel()).fireTableDataChanged();
        ((DefaultTableModel) tbAdmision.getModel()).fireTableDataChanged();
        ((DefaultTableModel) tbRegistros.getModel()).fireTableDataChanged();
        ((DefaultTableModel) tbReportes.getModel()).fireTableDataChanged();
    }

// Método para limpiar los checkboxes principales
    public void limpiarCheckboxesPrincipales() {
        chAdministracion.setSelected(false);
        chAdmision.setSelected(false);
        chRegistros.setSelected(false);
        chReportes.setSelected(false);

    }

    //Llenar las Tablas
//    private void llenarTablaConSubmenu(JTable table, int menuId) {
//        Connection connection = null;
//        try {
//            connection = Conectar.getInstancia().obtenerConexion();
//            if (connection == null) {
//                throw new RuntimeException("Error: La conexión a la base de datos es nula.");
//            }
//            // Consulta para obtener los submenús asociados al menú
//            String sql = "SELECT rms.id_submenu, s.nombre_submenu, rms.visualizar, rms.agregar, rms.editar, rms.eliminar \n"
//                    + "FROM roles_menus_submenus rms \n"
//                    + "JOIN submenus s ON rms.id_submenu = s.id_submenu \n"
//                    + "WHERE rms.id_menu = ? AND rms.id_rol = ? ";
//
//            try (PreparedStatement ps = connection.prepareStatement(sql)) {
//                ps.setInt(1, menuId);
//                ResultSet rs = ps.executeQuery();
//
//                DefaultTableModel model = (DefaultTableModel) table.getModel();
//                model.setRowCount(0); // Limpiar la tabla antes de llenarla
//                int rowNumber = 1;
//                while (rs.next()) {
//                    int idSubmenu = rs.getInt("id_submenu");
//                    String nombreSubmenu = rs.getString("nombre_submenu");
//
//                    // Agregar los submenús a la tabla
//                    model.addRow(new Object[]{rowNumber++, nombreSubmenu, false, false, false, false, false, idSubmenu});
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // Devolver la conexión al pool
//            Conectar.getInstancia().devolverConexion(connection);
//        }
//    }
    private void llenarTablaConSubmenu(JTable table, int menuId) {
        // Limpiar la tabla antes de llenarla con nuevos datos
        limpiarTabla(table);

        Connection connection = null;
        try {
            connection = Conectar.getInstancia().obtenerConexion();
            if (connection == null) {
                throw new RuntimeException("Error: La conexión a la base de datos es nula.");
            }

            String sql = """
            SELECT rms.id_submenu, s.nombre_submenu, rms.visualizar, rms.agregar, rms.editar, rms.eliminar 
            FROM roles_menus_submenus rms 
            JOIN submenus s ON rms.id_submenu = s.id_submenu 
            WHERE rms.id_menu = ? AND rms.id_rol = ?
        """;

            System.out.println("Ejecutando consulta para menuId: " + menuId + " y rolId: " + rolId);

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, menuId);
                ps.setInt(2, rolId);  // Usamos rolId en lugar de dejarlo fijo en 1
                ResultSet rs = ps.executeQuery();

                DefaultTableModel model = (DefaultTableModel) table.getModel();

                int rowNumber = 1;
                while (rs.next()) {
                    int idSubmenu = rs.getInt("id_submenu");
                    String nombreSubmenu = rs.getString("nombre_submenu");

                    // Obtener los valores de permisos como enteros
                    int visualizar = rs.getInt("visualizar");
                    int agregar = rs.getInt("agregar");
                    int editar = rs.getInt("editar");
                    int eliminar = rs.getInt("eliminar");

                    // Convertir a Boolean para que la tabla maneje correctamente los checkboxes
                    Boolean visualizarBoolean = visualizar == 1; // true si es 1, false si es 0
                    Boolean agregarBoolean = agregar == 1;
                    Boolean editarBoolean = editar == 1;
                    Boolean eliminarBoolean = eliminar == 1;

                    System.out.println("Visualizar: " + visualizarBoolean + ", Agregar: " + agregarBoolean
                            + ", Editar: " + editarBoolean + ", Eliminar: " + eliminarBoolean);

                    // Agregar la fila a la tabla
                    model.addRow(new Object[]{rowNumber++, nombreSubmenu, visualizarBoolean, agregarBoolean, editarBoolean, eliminarBoolean, false, idSubmenu});
                }

                // Notificar que los datos en la tabla han cambiado
                model.fireTableDataChanged();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Devolver la conexión al pool
            if (connection != null) {
                Conectar.getInstancia().devolverConexion(connection);
            }
        }
    }

    private void handlePermission(String menuName, String submenuName, int column, Boolean selected) {
        // Aquí iría el código existente para manejar cada submenú
        if (menuName.equals("Administracion")) {
            switch (submenuName) {
                case "Usuarios":
                    handleUsuarios(column, selected);
                    break;
                case "Roles":
                    handleRoles(column, selected);
                    break;
                case "Asignación de Permisos":
                    handleAsignacionPermisos(column, selected);
                    break;
                case "Empresas":
                    handleEmpresas(column, selected);
                    break;
                case "Trabajadores":
                    handleTrabajadores(column, selected);
                    break;
                case "Tarifario":
                    handleTarifario(column, selected);
                    break;
                case "Productos":
                    handleProductos(column, selected);
                    break;
                case "Formularios":
                    handleFormularios(column, selected);
                    break;
                case "Proveedor":
                    handleProveedor(column, selected);
                    break;
                case "Convenios":
                    handleConvenios(column, selected);
                    break;
                case "Búsqueda de Convenios":
                    handleBusquedaConvenios(column, selected);
                    break;
                case "Asignación de Trabajos":
                    handleAsignacionTrabajos(column, selected);
                    break;
                case "Puesto de Trabajos":
                    handlePuestosDeTrabajo(column, selected);
                    break;
                case "Formas de Pago":
                    handleFormasDePago(column, selected);
                    break;
                case "Menus - Submenus":
                    handleMenus(column, selected);
                    break;
                // Añadir más casos según sea necesario...
            }
        } else if (menuName.equals("Admision")) {
            switch (submenuName) {
                case "Clientes":
                    handleClientes(column, selected);
                    break;
                case "Marcas":
                    handleMarcas(column, selected);
                    break;
                case "Unidades":
                    handleUnidades(column, selected);
                    break;
                case "Tipo de Pagos Generales":
                    handleTipoDePagosGenerales(column, selected);
                    break;
                case "Tipo de Productos y Materiales":
                    handleTipoDeProductosMateriales(column, selected);
                    break;
                case "Tipo de Maquinarias y Vehiculos":
                    handleTipoDeMaquinariasVehiculos(column, selected);
                    break;
                case "Localización":
                    handleLocalizacion(column, selected);
                    break;
                case "Configuración":
                    handleconfiguracion(column, selected);
                    break;
                // Añadir más casos según sea necesario...
            }
        } else if (menuName.equals("Registros")) {
            switch (submenuName) {
                case "Orden de Servicio":
                    handleOrdenDeServicio(column, selected);
                    break;
                case "Ver Ordenes":
                    handleVerOrdenes(column, selected);
                    break;
                case "Ventas":
                    handleVentas(column, selected);
                    break;
                case "Compras de Productos y Materiales":
                    handleCompraProductosMateriales(column, selected);
                    break;
                case "Compra Equipos y Vehiculos":
                    handleCompraEquiposVehiculos(column, selected);
                    break;
                case "Gastos Generales":
                    handleGastosGenerales(column, selected);
                    break;
                case "Kardex":
                    handleKardex(column, selected);
                    break;
                case "Cotizaciones":
                    handleCotizaciones(column, selected);
                    break;
                case "Cancelaciones":
                    handleCancelaciones(column, selected);
                    break;
                // Añadir más casos según sea necesario...
            }
        } else if (menuName.equals("Reportes")) {
            switch (submenuName) {
                case "Trabajos Realizados":
                    handleTrabajosRealizados(column, selected);
                    break;
                case "Estadísticas":
                    handleEstadisticas(column, selected);
                    break;
                case "Deudas Por Pagar":
                    handleDeudaPorPagar(column, selected);
                    break;
                case "Deudas Por Cobrar":
                    handleDeudasPorCobrar(column, selected);
                    break;
                case "Horas Trabajadas":
                    handleHorasTrabajadas(column, selected);
                    break;
                case "Sueldos":
                    handleSueldos(column, selected);
                    break;
                case "Stock":
                    handleStock(column, selected);
                    break;
                case "Trabajos":
                    handleTrabajos(column, selected);
                    break;
                case "Presupuesto":
                    handlePresupuestos(column, selected);
                    break;
            }
        }
    }

    /// Método para obtener el nombre del menú basado en la fila
    private String getMenuNameByRow(String tableName, int row) {
        // Asocia los nombres de los menús a las tablas correspondientes
        switch (tableName) {
            case "Administracion":
                return "Administracion";
            case "Admision":
                return "Admision";
            case "Registros":
                return "Registros";
            case "Reportes":
                return "Reportes";
            default:
                return "";
        }
    }

    public int obtenerRolIdActual() {
        try {
            String textoRolId = txtIdTPU.getText();
            if (textoRolId == null || textoRolId.trim().isEmpty()) {
                System.out.println("Error: El texto del rol ID es nulo o vacío.");
                return -1; // Maneja el error devolviendo un valor incorrecto si no hay rol ID válido.
            }
            return Integer.parseInt(textoRolId.trim());
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir txtIdTPU a número: " + e.getMessage());
            return -1; // Devuelve -1 si hay un error al convertir.
        }
    }

    private void desactivarPermisoEnBaseDeDatos(int rolId, int menuId, int submenuId) {
        Connection connection = null;

        String sql = """
        UPDATE roles_menus_submenus
        SET activo = 0
        WHERE id_rol = ? AND id_menu = ? AND id_submenu = ?;
    """;

        try {
            // Obtener la conexión del pool
            connection = Conectar.getInstancia().obtenerConexion();

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, rolId);
                ps.setInt(2, menuId);
                ps.setInt(3, submenuId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    // Método para obtener el ID del submenú
    private int obtenerSubmenuId(String submenuName) {
        Connection connection = null;
        String sql = "SELECT id_submenu FROM submenus WHERE nombre_submenu = ?";

        try {
            // Obtener la conexión del pool
            connection = Conectar.getInstancia().obtenerConexion();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, submenuName);  // Asegúrate de que el nombre de columna sea correcto
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Submenu ID encontrado: " + rs.getInt("id_submenu"));
                    return rs.getInt("id_submenu");  // Verifica que el nombre de la columna es 'id_submenu'
                } else {
                    System.out.println("No se encontró submenu con el nombre: " + submenuName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
        return -1;  // Devuelve -1 si no se encuentra un resultado válido
    }

    private String getSubmenuNameByRow(String tableName, int row) {
        String[] submenuNames;
        switch (tableName) {
            case "Administracion":
                submenuNames = new String[]{"Usuarios", "Roles", "Asignación de Permisos", "Empresas", "Trabajadores", "Tarifario", "Productos", "Formularios", "Proveedor", "Convenios", "Búsqueda de Convenios", "Asignación de Trabajos", "Puesto de Trabajos", "Formas de Pago"};
                break;
            case "Admision":
                submenuNames = new String[]{"Clientes", "Marcas", "Unidades", "Tipo de Pagos", "Tipos de Productos y Materiales", "Tipo de Maquinarias y Vehículos", "Localización", "Configuración"};
                break;
            case "Registros":
                submenuNames = new String[]{"Orden de Servicio", "Ver Ordenes", "Ventas", "Compras de Productos y Materiales", "Compra Equipos y Vehículos", "Gastos Generales", "Kardex", "Cotizaciones", "Cancelaciones"};
                break;
            case "Reportes":
                submenuNames = new String[]{"Trabajos Realizados", "Estadísticas", "Deudas por Pagar", "Deudas por Cobrar", "Horas Trabajadas", "Sueldos", "Stock", "Trabajos", "Presupuestos"};
                break;
            default:
                return "";
        }
        return submenuNames.length > row ? submenuNames[row] : "";
    }

    private void handleUsuarios(int column, Boolean selected) {
        if (this.u != null) {
            // Limpieza de botones antes de aplicar la lógica
            u.btnGrabar.setEnabled(false);
            u.btnEliminar.setEnabled(false);
            u.btnModificar.setEnabled(false);
            u.btnActivar.setVisible(false);
            u.btnInactivar.setVisible(false);
            u.btnCancelar.setEnabled(true);
            u.btnGuia.setVisible(true);
            u.btnSalir.setVisible(true);
            u.btnNuevo.setVisible(true);
            u.btnMostrarDatos.setVisible(true);
            u.cbxTrabajador.setEnabled(true);
            u.rdRelacionar.setVisible(true);
            u.CargarDatosTable("");
            u.CargarDatosTablaUsuariosTrabajadores(tbUsuTrab);
        } else {
            JOptionPane.showMessageDialog(null, "El objeto 'u' no está inicializado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        switch (column) {

            case 2: // Visualizar
                if (selected) {

                }
                break;
            case 3: // Agregar
                if (selected) {
                    u.btnGrabar.setEnabled(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    u.btnModificar.setVisible(true);
                    u.btnActivar.setVisible(true);
                    u.btnInactivar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    u.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleRoles(int column, Boolean selected) {
        r.CargarDatosTabla("");
        r.btnActivar.setVisible(false);
        r.btnInactivar.setVisible(false);
        r.btnCancelar.setVisible(true);
        r.btnGrabarTU.setVisible(false);
        r.btnEliminar.setVisible(false);
        r.btnModificar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    r.CargarDatosTabla("");
                }
                break;
            case 3: // Agregar
                if (selected) {
                    r.btnGrabarTU.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    r.btnModificar.setVisible(true);
                    r.btnActivar.setVisible(true);
                    r.btnInactivar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    r.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleAsignacionPermisos(int column, Boolean selected) {
        btnGrabar.setVisible(false);
        btnModificar.setVisible(false);
        btnCancelar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

                }
                break;
            case 3: // Agregar
                if (selected) {
                    btnGrabar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    btnModificar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    btnCancelar.setVisible(true);
                }
                break;
        }
    }

    private void handleEmpresas(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    em.btnActivar.setVisible(false);
                    em.btnAgregar.setVisible(false);
                    em.btnCancelar.setVisible(false);
                    em.btnEliminar.setVisible(false);
                    em.btnInactivar.setVisible(false);
                    em.btnLimpiar.setVisible(false);
                    em.btnModificar.setVisible(false);
                    em.btnNuevo.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    em.btnAgregar.setVisible(true);
                    em.btnEliminar.setVisible(true);
                    em.btnNuevo.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    em.btnModificar.setVisible(true);
                    em.btnActivar.setVisible(true);
                    em.btnInactivar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    em.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleTrabajadores(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    tb.btnActivar.setVisible(false);
                    tb.btnCancelar.setVisible(false);
                    tb.btnEliminar.setVisible(false);
                    tb.btnGrabar.setVisible(false);
                    tb.btnInactivar.setVisible(false);
                    tb.btnModificar.setVisible(false);
                    tb.btnNuevo.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    tb.btnGrabar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    tb.btnModificar.setVisible(true);
                    tb.btnNuevo.setVisible(true);
                    tb.btnActivar.setVisible(true);
                    tb.btnInactivar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    tb.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleTarifario(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    tf.btnCancelar.setVisible(false);
                    tf.btnEliminar.setVisible(false);
                    tf.btnGuardar.setVisible(false);
                    tf.btnModificar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    tf.btnGuardar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    tf.btnModificar.setVisible(true);
                    tf.btnCancelar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    tf.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleProductos(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    p.btnActivar.setVisible(false);
                    p.btnAdd.setVisible(false);
                    p.btnCancel.setVisible(false);
                    p.btnDelete.setVisible(false);
                    p.btnInactivar.setVisible(false);
                    p.btnNew.setVisible(false);
                    p.btnUpdate.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    p.btnNew.setVisible(true);
                    p.btnAdd.setVisible(true);
                    p.btnCancel.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    p.btnUpdate.setVisible(true);
                    p.btnActivar.setVisible(true);
                    p.btnInactivar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    p.btnDelete.setVisible(true);
                }
                break;
        }
    }

    private void handleFormularios(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleProveedor(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    pr.btnCancel.setVisible(false);
                    pr.btnEliminar.setVisible(false);
                    pr.btnGrabar.setVisible(false);
                    pr.btnInactivar.setVisible(false);
                    pr.btnModificar.setVisible(false);
                    pr.btnNew.setVisible(false);
                    pr.btnReingresar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    pr.btnGrabar.setVisible(true);
                    pr.btnNew.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    pr.btnModificar.setVisible(true);
                    pr.btnInactivar.setVisible(true);
                    pr.btnReingresar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    pr.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleConvenios(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleBusquedaConvenios(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleAsignacionTrabajos(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    at.btnFiltrar.setVisible(true);
                    at.btnMostrarTodo.setVisible(true);
                    at.btnSoloEmpresa.setVisible(true);
                    at.btnProgramar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    at.btnProgramar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handlePuestosDeTrabajo(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    pt.btnActivar.setVisible(false);
                    pt.btnCancelar.setVisible(false);
                    pt.btnDesactivar.setVisible(false);
                    pt.btnEliminar.setVisible(false);
                    pt.btnGuardar.setVisible(false);
                    pt.btnModificar.setVisible(false);
                    pt.btnNuevoTipoDeTrabajo.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    pt.btnGuardar.setVisible(true);
                    pt.btnCancelar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    pt.btnModificar.setVisible(true);
                    pt.btnActivar.setVisible(true);
                    pt.btnDesactivar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    pt.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleFormasDePago(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    fp.btnActivar.setVisible(false);
                    fp.btnCancelar.setVisible(false);
                    fp.btnEliminar.setVisible(false);
                    fp.btnGuardar.setVisible(false);
                    fp.btnInactivar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    fp.btnGuardar.setVisible(true);
                    fp.btnCancelar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    fp.btnInactivar.setVisible(true);
                    fp.btnActivar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    fp.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleMenus(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    ms.btnAddMenu.setVisible(false);
                    ms.btnAddSubmenu.setVisible(false);
                    ms.btnModificar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    ms.btnAddMenu.setVisible(true);
                    ms.btnAddSubmenu.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    ms.btnModificar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleClientes(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    c.btnActivar.setVisible(false);
                    c.btnActualizar.setVisible(false);
                    c.btnBorrar.setVisible(false);
                    c.btnCancelar.setVisible(false);
                    c.btnGuardar.setVisible(false);
                    c.btnInactivar.setVisible(false);
                    c.btnLimpiar.setVisible(false);
                    c.btnNuevo.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    c.btnGuardar.setVisible(true);
                    c.btnCancelar.setVisible(true);
                    c.btnLimpiar.setVisible(true);
                    c.btnNuevo.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    c.btnActivar.setVisible(true);
                    c.btnActualizar.setVisible(true);
                    c.btnInactivar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    c.btnBorrar.setVisible(true);
                }
                break;
        }
    }

    private void handleMarcas(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    mc.btnActivar.setVisible(false);
                    mc.btnCancelar.setVisible(false);
                    mc.btnEliminar.setVisible(false);
                    mc.btnGuardar.setVisible(false);
                    mc.btnInactivar.setVisible(false);
                    mc.btnModificar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    mc.btnGuardar.setVisible(true);
                    mc.btnCancelar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    mc.btnModificar.setVisible(true);
                    mc.btnActivar.setVisible(true);
                    mc.btnInactivar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    mc.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleUnidades(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    un.btnActivar.setVisible(false);
                    un.btnCancelar.setVisible(false);
                    un.btnEliminar.setVisible(false);
                    un.btnGuardar.setVisible(false);
                    un.btnInactivar.setVisible(false);
                    un.btnModificar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    un.btnGuardar.setVisible(true);
                    un.btnCancelar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    un.btnActivar.setVisible(true);
                    un.btnInactivar.setVisible(true);
                    un.btnModificar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    un.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleTipoDePagosGenerales(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    tp.btnActivar.setVisible(false);
                    tp.btnCancelar.setVisible(false);
                    tp.btnEliminar.setVisible(false);
                    tp.btnGuardar.setVisible(false);
                    tp.btnInactivar.setVisible(false);
                    tp.btnModificar.setVisible(false);
                    tp.btnLimpiar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    tp.btnCancelar.setVisible(true);
                    tp.btnGuardar.setVisible(true);
                    tp.btnLimpiar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    tp.btnActivar.setVisible(true);
                    tp.btnInactivar.setVisible(true);
                    tp.btnModificar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    tp.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleTipoDeProductosMateriales(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    tpm.btnActivar.setVisible(false);
                    tpm.btnCancelar.setVisible(false);
                    tpm.btnEliminar.setVisible(false);
                    tpm.btnGuardar.setVisible(false);
                    tpm.btnInactivar.setVisible(false);
                    tpm.btnModificar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    tpm.btnGuardar.setVisible(true);
                    tpm.btnCancelar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    tpm.btnModificar.setVisible(true);
                    tpm.btnActivar.setVisible(true);
                    tpm.btnInactivar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    tpm.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleTipoDeMaquinariasVehiculos(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    tm.btnActivar.setVisible(false);
                    tm.btnCancelar.setVisible(false);
                    tm.btnEliminar.setVisible(false);
                    tm.btnGuardar.setVisible(false);
                    tm.btnInactivar.setVisible(false);
                    tm.btnModificar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    tm.btnCancelar.setVisible(true);
                    tm.btnGuardar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    tm.btnActivar.setVisible(true);
                    tm.btnInactivar.setVisible(true);
                    tm.btnModificar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    tm.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleLocalizacion(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    l.btnActivar.setVisible(false);
                    l.btnCancelar.setVisible(false);
                    l.btnEliminar.setVisible(false);
                    l.btnGuardar.setVisible(false);
                    l.btnInactivar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    l.btnCancelar.setVisible(true);
                    l.btnGuardar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    l.btnActivar.setVisible(true);
                    l.btnInactivar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    l.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleconfiguracion(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    cf.btnActualizar.setVisible(false);
                    cf.btnCargarLogo.setVisible(false);
                    cf.btnGuardar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    cf.btnGuardar.setVisible(true);
                    cf.btnCargarLogo.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    cf.btnActualizar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleOrdenDeServicio(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    os.btnAdicionar.setVisible(false);
                    os.btnEliminar.setVisible(false);
                    os.btnGenerarCompra.setVisible(false);
                    os.btnLista.setVisible(false);
                    os.btnMTP.setVisible(false);
                    os.btnServicios.setVisible(false);
                    os.btnTotal.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    os.btnAdicionar.setVisible(true);
                    os.btnEliminar.setVisible(true);
                    os.btnGenerarCompra.setVisible(true);
                    os.btnLista.setVisible(true);
                    os.btnMTP.setVisible(true);
                    os.btnServicios.setVisible(true);
                    os.btnTotal.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleVerOrdenes(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    vo.btnBuscarFechaEmpresa.setVisible(false);
                    vo.btnMostrarTodo.setVisible(false);
                    vo.btnSoloEmpresa.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    vo.btnBuscarFechaEmpresa.setVisible(true);
                    vo.btnMostrarTodo.setVisible(true);
                    vo.btnSoloEmpresa.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleVentas(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleCompraProductosMateriales(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    cpm.btnEliminar.setVisible(false);
                    cpm.btnGuardar.setVisible(false);
                    cpm.btnMostrarStock.setVisible(false);
                    cpm.btnRegistrarCredito.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    cpm.btnEliminar.setVisible(true);
                    cpm.btnGuardar.setVisible(true);
                    cpm.btnMostrarStock.setVisible(true);
                    cpm.btnRegistrarCredito.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleCompraEquiposVehiculos(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    cev.btnBorrar.setVisible(false);
                    cev.btnGuardar.setVisible(false);
                    cev.btnLimpiar.setVisible(false);
                    cev.btnModificar.setVisible(false);
                    cev.btnNuevo.setVisible(false);
                    cev.btnRegistrarCredito.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    cev.btnBorrar.setVisible(true);
                    cev.btnGuardar.setVisible(true);
                    cev.btnLimpiar.setVisible(true);
                    cev.btnModificar.setVisible(true);
                    cev.btnNuevo.setVisible(true);
                    cev.btnRegistrarCredito.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleGastosGenerales(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    gg.btnBorrar.setVisible(false);
                    gg.btnCancel.setVisible(false);
                    gg.btnGuardar.setVisible(false);
                    gg.btnLimpiar.setVisible(false);
                    gg.btnModificar.setVisible(false);
                    gg.btnNuevo.setVisible(false);
                    gg.btnRegistrarCredito.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    gg.btnBorrar.setVisible(true);
                    gg.btnCancel.setVisible(true);
                    gg.btnGuardar.setVisible(true);
                    gg.btnLimpiar.setVisible(true);
                    gg.btnModificar.setVisible(true);
                    gg.btnNuevo.setVisible(true);
                    gg.btnRegistrarCredito.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleKardex(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleCotizaciones(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    ct.btnAdicionar.setVisible(false);
                    ct.btnEliminar.setVisible(false);
                    ct.btnGenerar.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    ct.btnAdicionar.setVisible(true);
                    ct.btnEliminar.setVisible(true);
                    ct.btnGenerar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleCancelaciones(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleTrabajosRealizados(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    tr.MostrarTabla();
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleEstadisticas(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleDeudaPorPagar(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleDeudasPorCobrar(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleHorasTrabajadas(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleSueldos(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleStock(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handleTrabajos(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    private void handlePresupuestos(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    // Implementar lógica de visualizar
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    // Implementar lógica de editar
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    // Implementar lógica de eliminar
                }
                break;
        }
    }

    // Método para obtener el ID del menú
    private int obtenerMenuId(String nombremenu) {
        Connection connection = null;
        try {
            connection = Conectar.getInstancia().obtenerConexion();
            String sql = "SELECT id_menu FROM menus WHERE LOWER(nombre_menu) = LOWER(?)";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, nombremenu);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id_menu");
                    System.out.println("ID del menú encontrado para " + nombremenu + ": " + id);
                    return id;
                } else {
                    System.out.println("Error: El nombre del menú '" + nombremenu + "' no existe en la tabla menus.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del menú: " + e.getMessage());
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
        }
        return -1;  // Devuelve -1 si no se encuentra un resultado válido
    }

    // Método para obtener el ID del permiso basado en la columna
    private int obtenerPermisoIdPorColumna(int column) {
        switch (column) {
            case 2:
                return 1; // Visualizar
            case 3:
                return 2; // Agregar
            case 4:
                return 3; // Editar
            case 5:
                return 4; // Eliminar
            default:
                return -1;
        }
    }

    // Método para guardar permisos desde una tabla específica
    // Modificación del método en la clase AsignacionPermisos para que coincida
    private void guardarPermisosDeTabla(JTable tabla, int rolId) {
        // Itera sobre todas las filas de la tabla
        for (int i = 0; i < tabla.getRowCount(); i++) {
            // Obtener el ID del submenú desde la columna 7, asegurando que no sea nulo
            Object submenuIdObj = tabla.getValueAt(i, 7);
            if (submenuIdObj == null) {
                System.out.println("Error: ID del submenú es nulo en la fila " + i);
                continue; // Saltar esta fila si el ID del submenú es nulo
            }
            int submenuId = (int) submenuIdObj;

            int menuId = obtenerMenuIdPorSubmenu(submenuId); // Obtener el ID del menú basado en el submenú

            // Obtener permisos, validando que no sean nulos antes de procesarlos
            Object visualizarObj = tabla.getValueAt(i, 2);
            Object agregarObj = tabla.getValueAt(i, 3);
            Object editarObj = tabla.getValueAt(i, 4);
            Object eliminarObj = tabla.getValueAt(i, 5);

            // Validar que todos los permisos no sean nulos
            if (visualizarObj == null || agregarObj == null || editarObj == null || eliminarObj == null) {
                System.out.println("Error: Valores de permisos nulos en la fila " + i);
                continue; // Saltar esta fila si alguno de los permisos es nulo
            }

            boolean visualizar = (boolean) visualizarObj;
            boolean agregar = (boolean) agregarObj;
            boolean editar = (boolean) editarObj;
            boolean eliminar = (boolean) eliminarObj;

            // Guardar permisos en la base de datos
            System.out.println("Guardando permisos para menú ID: " + menuId + ", submenú ID: " + submenuId);
            permisoManager.guardarPermiso(rolId, menuId, submenuId, visualizar, agregar, editar, eliminar, true);
        }
    }

//    // Método para limpiar los checkboxes en una tabla específica
    private void limpiarTablaPermisos(JTable tabla) {
        for (int i = 0; i < tabla.getRowCount(); i++) {
            tabla.setValueAt(false, i, 2); // Limpiar columna "Visualizar"
            tabla.setValueAt(false, i, 3); // Limpiar columna "Agregar"
            tabla.setValueAt(false, i, 4); // Limpiar columna "Editar"
            tabla.setValueAt(false, i, 5); // Limpiar columna "Eliminar"
        }
    }

    private void actualizarTablaConPermisos(int menuId, int submenuId, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
        JTable tabla = obtenerTablaPorMenuId(menuId);
        if (tabla != null) {
            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                int idSubmenu = (int) model.getValueAt(i, 7); // Asume que la columna 7 contiene el ID del submenú
                if (idSubmenu == submenuId) {
                    model.setValueAt(visualizar, i, 2);
                    model.setValueAt(agregar, i, 3);
                    model.setValueAt(editar, i, 4);
                    model.setValueAt(eliminar, i, 5);
                    break;
                }
            }
        }
    }

    private JTable obtenerTablaPorMenuId(int menuId) {
        // Asocia cada menú con una tabla
        switch (menuId) {
            case 1:
                return tbAdministracion;
            case 2:
                return tbAdmision;
            case 3:
                return tbRegistros;
            case 4:
                return tbReportes;
            default:
                return null;
        }
    }

    private void updateLayout() {
        revalidate(); // Vuelve a validar el diseño
        repaint(); // Repinta la interfaz para mostrar los cambios
    }

    public void cargarPermisosMenuPrincipal(int rolId) {
        Connection connection = null;
        try {
            connection = Conectar.getInstancia().obtenerConexion();
            if (connection == null) {
                throw new RuntimeException("Error: La conexión a la base de datos es nula.");
            }
            String sql = """
    SELECT m.nombre_menu, rm.visualizar
    FROM roles_menus rm
    JOIN menus m ON rm.id_menu = m.id_menu
    WHERE rm.id_rol = ? AND rm.activo = true;
    """;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, rolId);
                ResultSet rs = ps.executeQuery();

                // Limpiar tablas antes de cargar datos
                limpiarTabla(tbAdministracion);
                limpiarTabla(tbAdmision);
                limpiarTabla(tbRegistros);
                limpiarTabla(tbReportes);

                while (rs.next()) {
                    String nombreMenu = rs.getString("nombre_menu");
                    boolean visualizar = rs.getBoolean("visualizar");

                    // Llamar al método handleCheckboxChange para reflejar el estado del checkbox en la interfaz
                    actualizarCheckboxMenu(nombreMenu, visualizar);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Método para actualizar los checkboxes de los menús principales usando handleCheckboxChange()
    private void actualizarCheckboxMenu(String nombreMenu, boolean visualizar) {
        switch (nombreMenu.toLowerCase()) {
            case "administracion":
                chAdministracion.setSelected(visualizar);
                handleCheckboxChange(new ItemEvent(chAdministracion, ItemEvent.ITEM_STATE_CHANGED, chAdministracion, visualizar ? ItemEvent.SELECTED : ItemEvent.DESELECTED),
                        jScrollPane1, btnAdministracion, "Administracion", tbAdministracion);
                break;
            case "admision":
                chAdmision.setSelected(visualizar);
                handleCheckboxChange(new ItemEvent(chAdmision, ItemEvent.ITEM_STATE_CHANGED, chAdmision, visualizar ? ItemEvent.SELECTED : ItemEvent.DESELECTED),
                        jScrollPane2, btnAdmision, "Admision", tbAdmision);
                break;
            case "registros":
                chRegistros.setSelected(visualizar);
                handleCheckboxChange(new ItemEvent(chRegistros, ItemEvent.ITEM_STATE_CHANGED, chRegistros, visualizar ? ItemEvent.SELECTED : ItemEvent.DESELECTED),
                        jScrollPane3, btnRegistros, "Registros", tbRegistros);
                break;
            case "reportes":
                chReportes.setSelected(visualizar);
                handleCheckboxChange(new ItemEvent(chReportes, ItemEvent.ITEM_STATE_CHANGED, chReportes, visualizar ? ItemEvent.SELECTED : ItemEvent.DESELECTED),
                        jScrollPane5, btnReportes, "Reportes", tbReportes);
                break;
            // Añadir más menús si es necesario
        }
    }

    public void cargarPermisosSubmenus(int rolId) {
        Connection connection = null;
        try {
            connection = Conectar.getInstancia().obtenerConexion();
            if (connection == null) {
                throw new RuntimeException("Error: La conexión a la base de datos es nula.");
            }

            String sql = """
        SELECT rs.id_menu, rs.id_submenu, rs.visualizar, rs.agregar, rs.editar, rs.eliminar, sm.nombre_submenu
        FROM roles_menus_submenus rs
        JOIN submenus sm ON rs.id_submenu = sm.id_submenu
        WHERE rs.id_rol = ? AND rs.activo = true;
        """;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, rolId);
                ResultSet rs = ps.executeQuery();

                // Limpiar las tablas antes de actualizar los permisos
                limpiarTabla(tbAdministracion);
                limpiarTabla(tbAdmision);
                limpiarTabla(tbRegistros);
                limpiarTabla(tbReportes);

                while (rs.next()) {
                    int menuId = rs.getInt("id_menu");
                    int submenuId = rs.getInt("id_submenu");
                    boolean visualizar = rs.getBoolean("visualizar");
                    boolean agregar = rs.getBoolean("agregar");
                    boolean editar = rs.getBoolean("editar");
                    boolean eliminar = rs.getBoolean("eliminar");
                    String nombreSubmenu = rs.getString("nombre_submenu");

                    // Dependiendo del menú al que pertenezca el submenú, llenar la tabla correspondiente
                    switch (menuId) {
                        case 1: // Administración
                            llenarFilaSubmenu(tbAdministracion, submenuId, nombreSubmenu, visualizar, agregar, editar, eliminar);
                            break;
                        case 2: // Admisión
                            llenarFilaSubmenu(tbAdmision, submenuId, nombreSubmenu, visualizar, agregar, editar, eliminar);
                            break;
                        case 3: // Registros
                            llenarFilaSubmenu(tbRegistros, submenuId, nombreSubmenu, visualizar, agregar, editar, eliminar);
                            break;
                        case 4: // Reportes
                            llenarFilaSubmenu(tbReportes, submenuId, nombreSubmenu, visualizar, agregar, editar, eliminar);
                            break;
                        // Agregar más casos según sea necesario
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    private void llenarFilaSubmenu(JTable tabla, int submenuId, String nombreSubmenu, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();

        Object[] fila = new Object[]{
            submenuId, // Columna de ID del submenú
            nombreSubmenu, // Columna del nombre del submenú
            Boolean.valueOf(visualizar), // Asegúrate de que el valor sea Boolean
            Boolean.valueOf(agregar), // Asegúrate de que el valor sea Boolean
            Boolean.valueOf(editar), // Asegúrate de que el valor sea Boolean
            Boolean.valueOf(eliminar), // Asegúrate de que el valor sea Boolean
            Boolean.FALSE // Columna "Todo" inicialmente en false
        };
        model.addRow(fila);
    }

    public void cargarPermisos(int rolId) {
        // Limpiar todas las tablas antes de cargar nuevos datos
        limpiarTablas();

        this.rolId = obtenerRolIdActual(); // Forzar la actualización del rolId.
        if (this.rolId == -1) {
            System.out.println("Error: No se pudo obtener un rol válido.");
            return;
        }

        System.out.println("Rol ID obtenido: " + rolId);

        // Cargar permisos de menús principales
        cargarPermisosMenuPrincipal(rolId);

        // Cargar permisos de submenús
        cargarPermisosSubmenus(rolId);
    }

    public void cargarTablasPermisos() {
        // Cargar los submenús y permisos en cada tabla según el ID del menú
        llenarTablaConSubmenu(tbAdministracion, 1);  // Menú Administración
        llenarTablaConSubmenu(tbAdmision, 2);        // Menú Admisión
        llenarTablaConSubmenu(tbRegistros, 3);       // Menú Registros
        llenarTablaConSubmenu(tbReportes, 4);        // Menú Reportes
    }

    public int getRolId() {
        try {
            String textoRolId = txtIdTPU.getText();
            System.out.println("Texto obtenido de txtIdTPU: " + textoRolId);
            return Integer.parseInt(textoRolId);
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir txtIdTPU a número: " + e.getMessage());
            return -1;  // Valor por defecto si hay un error
        }
    }

    // Método getter para acceder a txtIdTPU
    public JTextField getTxtIdTPU() { // Si es JLabel, cámbialo a JLabel
        return txtIdTPU;
    }

    public void cargarPermisosDesdePrincipal() {
        // Obtener el Rol ID del campo txtIdTPU antes de cargar cualquier permiso
        int rolId = getRolId();

        if (rolId != -1) {
            cargarPermisos(rolId);  // Cargar los permisos asociados a ese Rol ID
        } else {
            JOptionPane.showMessageDialog(null, "Rol ID no válido.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cbxTPU = new javax.swing.JComboBox<>();
        btnGrabar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtIdTPU = new javax.swing.JTextField();
        btnModificar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnAdministracion = new javax.swing.JButton();
        btnAdmision = new javax.swing.JButton();
        btnRegistros = new javax.swing.JButton();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel3 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        btnReportes = new javax.swing.JButton();
        chAdministracion = new javax.swing.JCheckBox();
        chAdmision = new javax.swing.JCheckBox();
        chRegistros = new javax.swing.JCheckBox();
        chReportes = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbAdministracion = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbAdmision = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbRegistros = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbReportes = new javax.swing.JTable();

        setTitle("Asignación de Permisos");

        jPanel1.setBackground(new java.awt.Color(0, 51, 153));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tipo de Usuario");

        cbxTPU.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTPUItemStateChanged(evt);
            }
        });

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        btnGrabar.setText("Grabar");
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel1)
                .addGap(38, 38, 38)
                .addComponent(cbxTPU, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnGrabar)
                .addGap(71, 71, 71)
                .addComponent(btnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addGap(58, 58, 58)
                .addComponent(btnSalir)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtIdTPU, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(171, 171, 171)
                .addComponent(btnNuevo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbxTPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGrabar)
                    .addComponent(btnSalir)
                    .addComponent(btnCancelar)
                    .addComponent(btnModificar))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtIdTPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(btnNuevo)))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        btnAdministracion.setBackground(new java.awt.Color(204, 204, 255));
        btnAdministracion.setText("1.- Administración");
        btnAdministracion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAdministracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdministracionActionPerformed(evt);
            }
        });

        btnAdmision.setBackground(new java.awt.Color(204, 204, 255));
        btnAdmision.setText("2.- Admisión");
        btnAdmision.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAdmision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmisionActionPerformed(evt);
            }
        });

        btnRegistros.setBackground(new java.awt.Color(204, 204, 255));
        btnRegistros.setText("3.- Registros");
        btnRegistros.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRegistros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrosActionPerformed(evt);
            }
        });

        jInternalFrame1.setTitle("Asignación de Permisos");

        jPanel3.setBackground(new java.awt.Color(0, 51, 153));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion.png"))); // NOI18N
        jButton4.setText("Salir");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tipo de Usuario");

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/guardar.png"))); // NOI18N
        jButton8.setText("Grabar");

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        jButton9.setText("Cancelar");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel2)
                .addGap(38, 38, 38)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jButton8)
                .addGap(26, 26, 26)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8)
                    .addComponent(jButton4)
                    .addComponent(jButton9))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jButton10.setBackground(new java.awt.Color(204, 204, 255));
        jButton10.setText("1.- Administración");
        jButton10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(204, 204, 255));
        jButton11.setText("1.- Administración");
        jButton11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(204, 204, 255));
        jButton12.setText("1.- Administración");
        jButton12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        btnReportes.setBackground(new java.awt.Color(204, 204, 255));
        btnReportes.setText("4.- Reportes");
        btnReportes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportesActionPerformed(evt);
            }
        });

        chAdministracion.setText("Administración");
        chAdministracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chAdministracionActionPerformed(evt);
            }
        });

        chAdmision.setText("Admisión");
        chAdmision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chAdmisionActionPerformed(evt);
            }
        });

        chRegistros.setText("Registros");
        chRegistros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chRegistrosActionPerformed(evt);
            }
        });

        chReportes.setText("Reportes");
        chReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chReportesActionPerformed(evt);
            }
        });

        tbAdministracion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N°", "Programa", "Visualizar", "Agregar", "Editar", "Eliminar", "Todo", "id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbAdministracion);

        tbAdmision.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N°", "Programa", "Visualizar", "Agregar", "Editar", "Eliminar", "Todo", "id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbAdmision);

        tbRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N°", "Programa", "Visualizar", "Agregar", "Editar", "Eliminar", "Todo", "id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbRegistros);

        tbReportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N°", "Programa", "Visualizar", "Agregar", "Editar", "Eliminar", "Todo", "id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tbReportes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(chAdministracion)
                .addGap(112, 112, 112)
                .addComponent(chAdmision)
                .addGap(118, 118, 118)
                .addComponent(chRegistros)
                .addGap(101, 101, 101)
                .addComponent(chReportes)
                .addContainerGap(429, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1067, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1075, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnAdministracion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3)
                    .addComponent(btnReportes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRegistros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAdmision, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chAdministracion)
                    .addComponent(chAdmision)
                    .addComponent(chRegistros)
                    .addComponent(chReportes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdministracion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdmision)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReportes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 607, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 607, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnAdministracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdministracionActionPerformed
        // Mostrar la tabla de administración y ocultar las demás
//        jScrollPane1.setVisible(true); // Mostrar tabla de Administración
        jScrollPane2.setVisible(false); // Ocultar tabla de Admisión
        jScrollPane3.setVisible(false); // Ocultar tabla de Registros
        jScrollPane5.setVisible(false); // Ocultar tabla de Reportes
        boolean isVisible = jScrollPane1.isVisible();
        jScrollPane1.setVisible(!isVisible);  // Alterna la visibilidad de la tabla
        // Cargar los datos en la tabla de administración si no se ha hecho antes
//        if (!isVisible) {
//            int menuId = obtenerMenuId("Administracion"); // Reemplaza "Administracion" con el nombre del menú deseado
//            if (menuId != -1) {
//                llenarTablaConSubmenu(tbAdministracion, menuId);
//            } else {
//                JOptionPane.showMessageDialog(null, "No se pudo encontrar el menú especificado.");
//            }
//        }

        // Redibujar el panel para asegurar que se vea correctamente
        updateLayout();
    }//GEN-LAST:event_btnAdministracionActionPerformed

    private void btnAdmisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmisionActionPerformed
        // Mostrar la tabla de admisión y ocultar las demás
        jScrollPane1.setVisible(false); // Ocultar tabla de Administración
//        jScrollPane2.setVisible(true); // Mostrar tabla de Admisión
        jScrollPane3.setVisible(false); // Ocultar tabla de Registros
        jScrollPane5.setVisible(false); // Ocultar tabla de Reportes
        boolean isVisible = jScrollPane2.isVisible();
        jScrollPane2.setVisible(!isVisible); // Alterna la visibilidad de la tabla
        // Cargar los datos en la tabla de Admisión si no se ha hecho antes
//        if (!isVisible) {
//            int menuId = obtenerMenuId("Admision"); // Reemplaza "Administracion" con el nombre del menú deseado
//            if (menuId != -1) {
//                llenarTablaConSubmenu(tbAdmision, menuId);
//            } else {
//                JOptionPane.showMessageDialog(null, "No se pudo encontrar el menú especificado.");
//            }
//        }
        // Redibujar el panel para asegurar que se vea correctamente
        updateLayout();
    }//GEN-LAST:event_btnAdmisionActionPerformed

    private void btnRegistrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrosActionPerformed
        // Mostrar la tabla de registros y ocultar las demás
        jScrollPane1.setVisible(false); // Ocultar tabla de Administración
        jScrollPane2.setVisible(false); // Ocultar tabla de Admisión
//        jScrollPane3.setVisible(true); // Mostrar tabla de Registros
        jScrollPane5.setVisible(false); // Ocultar tabla de Reportes
        boolean isVisible = jScrollPane3.isVisible();
        jScrollPane3.setVisible(!isVisible); // Alterna la visisbilidad de la tabla
//        if (!isVisible) {
//            int menuId = obtenerMenuId("Registros"); // Reemplaza "Administracion" con el nombre del menú deseado
//            if (menuId != -1) {
//                llenarTablaConSubmenu(tbRegistros, menuId);
//            } else {
//                JOptionPane.showMessageDialog(null, "No se pudo encontrar el menú especificado.");
//            }
//        }

        updateLayout();
    }//GEN-LAST:event_btnRegistrosActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void btnReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportesActionPerformed
        // Mostrar la tabla de reportes y ocultar las demás
        jScrollPane1.setVisible(false); // Ocultar tabla de Administración
        jScrollPane2.setVisible(false); // Ocultar tabla de Admisión
        jScrollPane3.setVisible(false); // Ocultar tabla de Registros
//        jScrollPane5.setVisible(true); // Mostrar tabla de Reportes
        boolean isVisible = jScrollPane5.isVisible();
        jScrollPane5.setVisible(!isVisible); // Alterna la visisbilidad de la tabla
//        if (!isVisible) {
//            int menuId = obtenerMenuId("Reportes"); // Reemplaza "Administracion" con el nombre del menú deseado
//            if (menuId != -1) {
//                llenarTablaConSubmenu(tbReportes, menuId);
//            } else {
//                JOptionPane.showMessageDialog(null, "No se pudo encontrar el menú especificado.");
//            }
//        }

        updateLayout();
    }//GEN-LAST:event_btnReportesActionPerformed

    private void cbxTPUItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTPUItemStateChanged
        MostrarCodigoPorTPU(cbxTPU, txtIdTPU);
        // Ahora cargamos los permisos del usuario seleccionado
//        int rolId = Integer.parseInt(txtIdTPU.getText());
//        cargarPermisosDelUsuario(rolId);

    }//GEN-LAST:event_cbxTPUItemStateChanged

    private void chAdministracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chAdministracionActionPerformed
//        
    }//GEN-LAST:event_chAdministracionActionPerformed

    private void chAdmisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chAdmisionActionPerformed
//       
    }//GEN-LAST:event_chAdmisionActionPerformed

    private void chRegistrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chRegistrosActionPerformed
//        
    }//GEN-LAST:event_chRegistrosActionPerformed

    private void chReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chReportesActionPerformed
//        
    }//GEN-LAST:event_chReportesActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        try {
            // Obtener el ID del rol desde el campo de texto
            String rolIdText = txtIdTPU.getText().trim();
            if (!rolIdText.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Error: El ID del rol debe ser un número válido.");
                return; // Sale del método si el ID del rol no es válido
            }
            int rolId = Integer.parseInt(rolIdText);

            // Imprimir el rol ID para depurar
            System.out.println("Rol ID: " + rolId);

            if (rolId > 0) {
                // Recolectar los permisos de cada tabla (Administración, Admisión, Registros, Reportes)
                System.out.println("Guardando permisos para Administracion...");
                guardarPermisosDeTabla(tbAdministracion, rolId);
                System.out.println("Permisos de Administracion guardados.");

                System.out.println("Guardando permisos para Admision...");
                guardarPermisosDeTabla(tbAdmision, rolId);
                System.out.println("Permisos de Admision guardados.");

                System.out.println("Guardando permisos para Registros...");
                guardarPermisosDeTabla(tbRegistros, rolId);
                System.out.println("Permisos de Registros guardados.");

                System.out.println("Guardando permisos para Reportes...");
                guardarPermisosDeTabla(tbReportes, rolId);
                System.out.println("Permisos de Reportes guardados.");

                // Guardar el permiso para ver los menús principales (checkboxes)
                System.out.println("Guardando permisos para menús principales...");
                permisoManager.guardarPermisoMenuPrincipal(rolId, "Administracion", chAdministracion.isSelected(), true);
                permisoManager.guardarPermisoMenuPrincipal(rolId, "Admision", chAdmision.isSelected(), true);
                permisoManager.guardarPermisoMenuPrincipal(rolId, "Registros", chRegistros.isSelected(), true);
                permisoManager.guardarPermisoMenuPrincipal(rolId, "Reportes", chReportes.isSelected(), true);

                System.out.println("Permisos de menús principales guardados.");

                JOptionPane.showMessageDialog(null, "Permisos guardados correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione un tipo de usuario válido.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los permisos: ID de rol no válido.");
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el stack trace en la consola
            JOptionPane.showMessageDialog(null, "Error al guardar los permisos: " + e.getMessage());
        }

    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        try {
            // Obtener el ID del rol desde el campo de texto
            int rolId = Integer.parseInt(txtIdTPU.getText());

            // Actualizar los permisos seleccionados en las tablas
            guardarPermisosDeTabla(tbAdministracion, rolId);
            guardarPermisosDeTabla(tbAdmision, rolId);
            guardarPermisosDeTabla(tbRegistros, rolId);
            guardarPermisosDeTabla(tbReportes, rolId);

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(null, "Permisos actualizados correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los permisos: " + e.getMessage());
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // Limpiar el campo del ID del rol
        txtIdTPU.setText("");

        // Limpiar las tablas de permisos (desmarcar todos los checkboxes)
        limpiarTablaPermisos(tbAdministracion);
        limpiarTablaPermisos(tbAdmision);
        limpiarTablaPermisos(tbRegistros);
        limpiarTablaPermisos(tbReportes);

        // Mostrar mensaje de cancelación
        JOptionPane.showMessageDialog(null, "Cambios cancelados.");


    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        limpiarCheckboxesEnTablas();

        // Asegúrate de que los botones permanezcan habilitados
        btnGrabar.setVisible(true);
        btnModificar.setVisible(true);
        btnCancelar.setVisible(true);
        btnSalir.setVisible(true);

        // Si el estado de los botones depende de alguna otra lógica, asegúrate de actualizar ese estado aquí.
        // Por ejemplo, si los botones dependen de una selección en las tablas o algún otro campo, asegúrate de que no se deshabiliten.
        // Refresca la interfaz si es necesario
        updateLayout();

    }//GEN-LAST:event_btnNuevoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdministracion;
    private javax.swing.JButton btnAdmision;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnRegistros;
    private javax.swing.JButton btnReportes;
    public javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxTPU;
    private javax.swing.JCheckBox chAdministracion;
    private javax.swing.JCheckBox chAdmision;
    private javax.swing.JCheckBox chRegistros;
    private javax.swing.JCheckBox chReportes;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable tbAdministracion;
    private javax.swing.JTable tbAdmision;
    private javax.swing.JTable tbRegistros;
    private javax.swing.JTable tbReportes;
    public javax.swing.JTextField txtIdTPU;
    // End of variables declaration//GEN-END:variables

    public void MostrarTipodeUsuarioCombo(JComboBox cbxTPU) {
        Connection connection = null;

        String sql = "select * from roles"; // Asegúrate de que la tabla se llama 'roles'
        Statement st;

        try {
            connection = Conectar.getInstancia().obtenerConexion();
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxTPU.removeAllItems();

            while (rs.next()) {
                cbxTPU.addItem(rs.getString("nombre")); // Asegúrate de que la columna se llama 'nombre'
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    public void MostrarCodigoPorTPU(JComboBox cbxTPU, JTextField idTPU) {
        Connection connection = null;

        String consuta = "select id_rol from roles where nombre=?";

        try {
            connection = Conectar.getInstancia().obtenerConexion();
            CallableStatement cs = connection.prepareCall(consuta);
            cs.setString(1, cbxTPU.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                idTPU.setText(rs.getString("id_rol"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }
    }

    private int obtenerMenuIdPorSubmenu(int submenuId) {
        Connection connection = null;
        int menuId = -1;

        try {
            // Obtener la conexión
            connection = Conectar.getInstancia().obtenerConexion();
            if (connection == null) {
                throw new RuntimeException("Error: La conexión a la base de datos es nula.");
            }

            // Consulta para obtener el menu_id a partir del submenu_id
            String sql = "SELECT menu_id FROM submenus WHERE id_submenu = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, submenuId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        menuId = rs.getInt("menu_id");
                    } else {
                        System.out.println("Error: No se encontró un menú para el submenú ID: " + submenuId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Devolver la conexión al pool
            Conectar.getInstancia().devolverConexion(connection);
        }

        return menuId;  // Devuelve -1 si no se encontró el menuId
    }

//setupMenuCheckboxListeners() Configura los listeners de los checkboxes de los menús principales.
//handleCheckboxChange(): Maneja el evento cuando un checkbox es seleccionado o deseleccionado, mostrando 
//        u ocultando componentes y cargando los submenús en la tabla correspondiente.
//llenarTablaConSubmenu(): Llena la tabla con los submenús asociados a un menú cuando el checkbox de ese menú es seleccionado.
//limpiarTabla(): Limpia la tabla cuando el checkbox de un menú es deseleccionado.
//obtenerMenuId(): Obtiene el ID del menú a partir de su nombre para usarlo en las consultas de submenús.
//updateLayout(): Actualiza la interfaz gráfica para reflejar los cambios en los componentes.
//    Flujo del código:
//cargarPermisos(int rolId): Llama a los métodos de cargar permisos de menús principales y submenús.
//cargarPermisosMenuPrincipal(int rolId): Marca los checkboxes de los menús principales basándose en los permisos almacenados. 
//actualizarCheckboxMenu() que llama internamente a handleCheckboxChange() para gestionar la lógica de activación de componentes.
//cargarPermisosSubmenus(int rolId): Llena las tablas de submenús con los permisos almacenados, utilizando el método llenarFilaSubmenu().
//llenarFilaSubmenu(): Agrega las filas correspondientes a los submenús con los permisos de visualización, agregar, editar, y eliminar.
//limpiarTabla(): Asegura que las tablas estén vacías antes de volver a cargar los datos.
//Ventajas:
//Consistencia: El uso de handleCheckboxChange() permite mantener la consistencia en cómo se actualiza la interfaz cuando se cargan los permisos.
//Simplicidad: Los métodos centralizan la lógica de cargar y manejar permisos, lo que facilita su mantenimiento.
//Flexibilidad: Puedes añadir nuevos menús y submenús de manera sencilla al sistema sin necesidad de cambiar grandes partes del código.
}
