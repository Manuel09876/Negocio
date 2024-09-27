package Administration;

import Admission.*;
import Register.*;
import Reports.*;
import Bases.Permiso;
import Bases.PermisoManager;
import Presentation.VentanaPrincipal;
//import com.toedter.calendar.JDateChooser;
import java.sql.Statement;
import conectar.Conectar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
//import java.util.ArrayList;
import java.util.List;
//import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
//import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.JTextField;

public class AsignacionPermisos extends javax.swing.JInternalFrame {

    Conectar conexion = new Conectar();
    Connection conect = conexion.getConexion();

    private VentanaPrincipal vp;
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

    private PermisoManager permisoManager;
    private List<Permiso> permisos;

    JTable tbUsuTrab = null;

    public AsignacionPermisos(VentanaPrincipal vp) throws SQLException {
        this.vp = vp;
        this.u = new Usuarios();
        this.r = new Roles();
        this.em = new Empresas();
        this.tb = new Trabajadores();
        this.tf = new Tarifario();
        this.p = new Productos();
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
        this.permisoManager = new PermisoManager(conect); // Instancia para manejar permisos

        initComponents();
        conexion = new Conectar();
        conect = conexion.getConexion();
        AutoCompleteDecorator.decorate(cbxTPU);
        MostrarTipodeUsuarioCombo(cbxTPU);

        jScrollPane1.setVisible(false);
        jScrollPane2.setVisible(false);
        jScrollPane3.setVisible(false);
        jScrollPane5.setVisible(false);

        // Configurar checkboxes en las tablas
        setupTableCheckBoxes(tbAdministracion);
        setupTableCheckBoxes(tbAdmision);
        setupTableCheckBoxes(tbRegistros);
        setupTableCheckBoxes(tbReportes);

        // Configurar listeners para los botones y checkboxes
        setupListeners();

        // Configurar oyentes del modelo de tabla para cada tabla
        setupTableModelListener(tbAdministracion, "Administracion");
        setupTableModelListener(tbAdmision, "Admision");
        setupTableModelListener(tbRegistros, "Registros");
        setupTableModelListener(tbReportes, "Reportes");
    }

    private void setupTableCheckBoxes(JTable table) {
        for (int i = 2; i <= 6; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            tc.setCellEditor(table.getDefaultEditor(Boolean.class));
            tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
        }
    }

    private void setupListeners() {
        btnAdministracion.addActionListener(evt -> showTable(jScrollPane1, jScrollPane2, jScrollPane3, jScrollPane5));
        btnAdmision.addActionListener(evt -> showTable(jScrollPane2, jScrollPane1, jScrollPane3, jScrollPane5));
        btnRegistros.addActionListener(evt -> showTable(jScrollPane3, jScrollPane1, jScrollPane2, jScrollPane5));
        btnReportes.addActionListener(evt -> showTable(jScrollPane5, jScrollPane1, jScrollPane2, jScrollPane3));

        chAdministracion.addItemListener(e -> vp.menuAdministration.setVisible(e.getStateChange() == ItemEvent.SELECTED));
        chAdmision.addItemListener(e -> vp.menuAdmission.setVisible(e.getStateChange() == ItemEvent.SELECTED));
        chRegistros.addItemListener(e -> vp.menuRegisters.setVisible(e.getStateChange() == ItemEvent.SELECTED));
        chReportes.addItemListener(e -> vp.menuReports.setVisible(e.getStateChange() == ItemEvent.SELECTED));
    }

    private void showTable(JScrollPane... scrollPanes) {
        for (JScrollPane pane : scrollPanes) {
            pane.setVisible(false);  // Ocultar todas las tablas primero
        }
        scrollPanes[0].setVisible(true);  // Mostrar la tabla relevante
        updateLayout();
    }

    // Método actualizado para configurar los oyentes del modelo de tabla
    private void setupTableModelListener(JTable table, String tableName) {
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();

                    if (column == 6) { // Columna "Todo"
                        Boolean selected = (Boolean) table.getValueAt(row, 6);
                        for (int i = 2; i <= 5; i++) {
                            table.setValueAt(selected, row, i);
                        }
                    } else if (column >= 2 && column <= 5) {
                        Boolean selected = (Boolean) table.getValueAt(row, column);
                        String menuName = getMenuNameByRow(tableName, row);
                        String submenuName = getSubmenuNameByRow(tableName, row);

                        if (!menuName.isEmpty() && !submenuName.isEmpty()) {
                            int menuId = obtenerMenuId(menuName);
                            int permisoId = obtenerPermisoIdPorColumna(column);
                            permisoManager.guardarPermiso(menuId, permisoId, selected, true, true, true);
                        } else {
                            System.out.println("Error: Nombres de menú o submenú no válidos.");
                        }
                    }
                }
            }
        });
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
                return ""; // Devuelve un valor vacío si el nombre de la tabla no coincide
        }
        return submenuNames.length > row ? submenuNames[row] : "";
    }

    private void manejarAccionPorMenuYSubmenu(String menuName, String submenuName, int column, Boolean selected) {
        if (menuName.equals("menuAdministration")) {
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
                default:
                    System.out.println("Submenú no reconocido en Administración.");
            }
        } else if (menuName.equals("menuAdmission")) {
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
                default:
                    System.out.println("Submenú no reconocido en Admisión.");
            }
        } else if (menuName.equals("menuRegisters")) {
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
                default:
                    System.out.println("Submenú no reconocido en Registros.");
            }
        } else if (menuName.equals("menuReports")) {
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
                default:
                    System.out.println("Submenú no reconocido en Reportes.");
            }
        }
    }

    private void handleUsuarios(int column, Boolean selected) {
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

        // Cargar los datos necesarios en la tabla
        u.CargarDatosTable("");  // Cargar datos generales
        u.CargarDatosTablaUsuariosTrabajadores(tbUsuTrab);  // Cargar datos específicos de usuarios-trabajadores

        // Lógica según la columna y el estado seleccionado
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    u.btnMostrarDatos.setVisible(true);
                } else {
                    u.btnMostrarDatos.setVisible(false);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    u.btnGrabar.setEnabled(true);  // Permitir grabar nuevos datos
                    u.btnNuevo.setVisible(true);  // Mostrar el botón Nuevo
                    u.btnCancelar.setVisible(true);  // Mostrar el botón Cancelar
                } else {
                    u.btnGrabar.setEnabled(false);  // Deshabilitar si no está seleccionado
                }
                break;
            case 4: // Editar
                if (selected) {
                    u.btnModificar.setVisible(true);  // Mostrar el botón de Modificar
                    u.btnActivar.setVisible(true);  // Mostrar el botón de Activar
                    u.btnInactivar.setVisible(true);  // Mostrar el botón de Inactivar
                } else {
                    u.btnModificar.setVisible(false);
                    u.btnActivar.setVisible(false);
                    u.btnInactivar.setVisible(false);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    u.btnEliminar.setVisible(true);  // Mostrar el botón de Eliminar
                } else {
                    u.btnEliminar.setVisible(false);  // Ocultar si no está seleccionado
                }
                break;
            default:
                // Puedes añadir lógica para otras columnas si es necesario
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
        em.btnActivar.setVisible(false);
        em.btnAgregar.setVisible(false);
        em.btnCancelar.setVisible(false);
        em.btnEliminar.setVisible(false);
        em.btnInactivar.setVisible(false);
        em.btnLimpiar.setVisible(false);
        em.btnModificar.setVisible(false);
        em.btnNuevo.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        tb.btnActivar.setVisible(false);
        tb.btnCancelar.setVisible(false);
        tb.btnEliminar.setVisible(false);
        tb.btnGrabar.setVisible(false);
        tb.btnInactivar.setVisible(false);
        tb.btnModificar.setVisible(false);
        tb.btnNuevo.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        tf.btnCancelar.setVisible(false);
        tf.btnEliminar.setVisible(false);
        tf.btnGuardar.setVisible(false);
        tf.btnModificar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        p.btnActivar.setVisible(false);
        p.btnAdd.setVisible(false);
        p.btnCancel.setVisible(false);
        p.btnDelete.setVisible(false);
        p.btnInactivar.setVisible(false);
        p.btnNew.setVisible(false);
        p.btnUpdate.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        pr.btnCancel.setVisible(false);
        pr.btnEliminar.setVisible(false);
        pr.btnGrabar.setVisible(false);
        pr.btnInactivar.setVisible(false);
        pr.btnModificar.setVisible(false);
        pr.btnNew.setVisible(false);
        pr.btnReingresar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        pt.btnActivar.setVisible(false);
        pt.btnCancelar.setVisible(false);
        pt.btnDesactivar.setVisible(false);
        pt.btnEliminar.setVisible(false);
        pt.btnGuardar.setVisible(false);
        pt.btnModificar.setVisible(false);
        pt.btnNuevoTipoDeTrabajo.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        fp.btnActivar.setVisible(false);
        fp.btnCancelar.setVisible(false);
        fp.btnEliminar.setVisible(false);
        fp.btnGuardar.setVisible(false);
        fp.btnInactivar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        ms.btnAddMenu.setVisible(false);
        ms.btnAddSubmenu.setVisible(false);
        ms.btnModificar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        c.btnActivar.setVisible(false);
        c.btnActualizar.setVisible(false);
        c.btnBorrar.setVisible(false);
        c.btnCancelar.setVisible(false);
        c.btnGuardar.setVisible(false);
        c.btnInactivar.setVisible(false);
        c.btnLimpiar.setVisible(false);
        c.btnNuevo.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        mc.btnActivar.setVisible(false);
        mc.btnCancelar.setVisible(false);
        mc.btnEliminar.setVisible(false);
        mc.btnGuardar.setVisible(false);
        mc.btnInactivar.setVisible(false);
        mc.btnModificar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        un.btnActivar.setVisible(false);
        un.btnCancelar.setVisible(false);
        un.btnEliminar.setVisible(false);
        un.btnGuardar.setVisible(false);
        un.btnInactivar.setVisible(false);
        un.btnModificar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        tp.btnActivar.setVisible(false);
        tp.btnCancelar.setVisible(false);
        tp.btnEliminar.setVisible(false);
        tp.btnGuardar.setVisible(false);
        tp.btnInactivar.setVisible(false);
        tp.btnModificar.setVisible(false);
        tp.btnLimpiar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        tpm.btnActivar.setVisible(false);
        tpm.btnCancelar.setVisible(false);
        tpm.btnEliminar.setVisible(false);
        tpm.btnGuardar.setVisible(false);
        tpm.btnInactivar.setVisible(false);
        tpm.btnModificar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        tm.btnActivar.setVisible(false);
        tm.btnCancelar.setVisible(false);
        tm.btnEliminar.setVisible(false);
        tm.btnGuardar.setVisible(false);
        tm.btnInactivar.setVisible(false);
        tm.btnModificar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        l.btnActivar.setVisible(false);
        l.btnCancelar.setVisible(false);
        l.btnEliminar.setVisible(false);
        l.btnGuardar.setVisible(false);
        l.btnInactivar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        cf.btnActualizar.setVisible(false);
        cf.btnCargarLogo.setVisible(false);
        cf.btnGuardar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        os.btnAdicionar.setVisible(false);
        os.btnEliminar.setVisible(false);
        os.btnGenerarCompra.setVisible(false);
        os.btnLista.setVisible(false);
        os.btnMTP.setVisible(false);
        os.btnServicios.setVisible(false);
        os.btnTotal.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        vo.btnBuscarFechaEmpresa.setVisible(false);
        vo.btnMostrarTodo.setVisible(false);
        vo.btnSoloEmpresa.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        cpm.btnEliminar.setVisible(false);
        cpm.btnGuardar.setVisible(false);
        cpm.btnMostrarStock.setVisible(false);
        cpm.btnRegistrarCredito.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        cev.btnBorrar.setVisible(false);
        cev.btnGuardar.setVisible(false);
        cev.btnLimpiar.setVisible(false);
        cev.btnModificar.setVisible(false);
        cev.btnNuevo.setVisible(false);
        cev.btnRegistrarCredito.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        gg.btnBorrar.setVisible(false);
        gg.btnCancel.setVisible(false);
        gg.btnGuardar.setVisible(false);
        gg.btnLimpiar.setVisible(false);
        gg.btnModificar.setVisible(false);
        gg.btnNuevo.setVisible(false);
        gg.btnRegistrarCredito.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        ct.btnAdicionar.setVisible(false);
        ct.btnEliminar.setVisible(false);
        ct.btnGenerar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

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
        tr.btnBusquedaEmpresa.setVisible(false);
        tr.btnBusquedaFechaEmpresa.setVisible(false);
        tr.btnMostrarTodo.setVisible(false);
        tr.btnPagar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

                }
                break;
            case 3: // Agregar
                if (selected) {
                    tr.btnBusquedaEmpresa.setVisible(true);
                    tr.btnBusquedaFechaEmpresa.setVisible(true);
                    tr.btnMostrarTodo.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    tr.btnPagar.setVisible(true);
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
        es.btnGenerarReporte.setVisible(false);
        es.btnImprimirGrafica.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

                }
                break;
            case 3: // Agregar
                if (selected) {
                    es.btnGenerarReporte.setVisible(true);
                    es.btnImprimirGrafica.setVisible(true);
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
        dpp.btnEliminar.setVisible(false);
        dpp.btnPagadas.setVisible(false);
        dpp.btnCanceladas.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {

                }
                break;
            case 3: // Agregar
                if (selected) {
                    dpp.btnEliminar.setVisible(true);
                    dpp.btnCanceladas.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    dpp.btnPagadas.setVisible(true);
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
                    // Implementar lógica de Visualizar
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
        ht.btnCierre.setVisible(false);
        ht.btnEliminar.setVisible(false);
        ht.btnGenerar.setVisible(true);
        ht.btnModificar.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    ht.btnMostrarDatos.setVisible(true);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    ht.btnCierre.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    ht.btnModificar.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    ht.btnEliminar.setVisible(true);
                }
                break;
        }
    }

    private void handleSueldos(int column, Boolean selected) {
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    sd.btnCargarPeriodos.setVisible(true);
                    sd.btnMostrar.setVisible(true);
                    sd.btnLimpiar.setVisible(true);
                    sd.btnReporte.setVisible(true);
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
        st.btnActualizarStock.setVisible(false);
        st.btnSolicitantes.setVisible(false);

        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    st.btnMostrarTodo.setVisible(true);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    // Implementar lógica de agregar
                }
                break;
            case 4: // Editar
                if (selected) {
                    st.btnActualizarStock.setVisible(true);
                    st.btnSolicitantes.setVisible(true);
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
        tbs.btnReasignar.setVisible(false);
        tbs.btnReprogramar.setVisible(false);
        tbs.btnTrabajoRealizado.setVisible(false);
        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    tbs.btnFiltrar.setVisible(true);
                    tbs.btnMostrarTodo.setVisible(true);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    tbs.btnReasignar.setVisible(true);
                    tbs.btnReprogramar.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    tbs.btnTrabajoRealizado.setVisible(true);
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
        b.addButton.setVisible(false);
        b.deleteButton.setVisible(false);
        b.editButton.setVisible(false);

        switch (column) {
            case 2: // Visualizar
                if (selected) {
                    b.exportButton.setVisible(true);
                }
                break;
            case 3: // Agregar
                if (selected) {
                    b.addButton.setVisible(true);
                }
                break;
            case 4: // Editar
                if (selected) {
                    b.editButton.setVisible(true);
                }
                break;
            case 5: // Eliminar
                if (selected) {
                    b.deleteButton.setVisible(true);
                }
                break;
        }
    }

    // Métodos auxiliares para obtener nombres de menú y submenú basado en la tabla y la fila
    private String getMenuNameByRow(String tableName, int row) {
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

    // Método para obtener el ID del menú
    private int obtenerMenuId(String menuName) {
        String sql = "SELECT id_menu FROM menus WHERE nombre_menu = ?";
        try (PreparedStatement ps = conect.prepareStatement(sql)) {
            ps.setString(1, menuName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_menu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Método para obtener el ID del submenú
//    private int obtenerSubmenuId(String submenuName) {
//        String sql = "SELECT id_submenu FROM submenus WHERE nombre_submenu = ?";
//        try (PreparedStatement ps = conect.prepareStatement(sql)) {
//            ps.setString(1, submenuName);  // Asegúrate de que el nombre de columna sea correcto
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                System.out.println("Submenu ID encontrado: " + rs.getInt("id_submenu"));
//                return rs.getInt("id_submenu");  // Verifica que el nombre de la columna es 'id_submenu'
//            } else {
//                System.out.println("No se encontró submenu con el nombre: " + submenuName);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;  // Devuelve -1 si no se encuentra un resultado válido
//    }
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

    // Método para guardar el permiso en la base de datos
//    private void guardarPermisoEnBaseDeDatos(int rolId, int menuId, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
//        // Aquí suponemos que `submenuId` no es necesario si estás trabajando solo con `menuId`.
//        // Si también necesitas `submenuId`, lo puedes agregar en la consulta SQL.
//
//        String sql = "INSERT INTO roles_menus_submenus (id_rol, id_menu, visualizar, agregar, editar, eliminar) "
//                + "VALUES (?, ?, ?, ?, ?, ?) "
//                + "ON DUPLICATE KEY UPDATE visualizar = ?, agregar = ?, editar = ?, eliminar = ?";
//
//        try (PreparedStatement ps = conect.prepareStatement(sql)) {
//            ps.setInt(1, rolId);     // ID del rol
//            ps.setInt(2, menuId);    // ID del menú
//            ps.setBoolean(3, visualizar);  // Visualizar permiso
//            ps.setBoolean(4, agregar);     // Agregar permiso
//            ps.setBoolean(5, editar);      // Editar permiso
//            ps.setBoolean(6, eliminar);    // Eliminar permiso
//
//            // Actualizar los permisos si ya existen
//            ps.setBoolean(7, visualizar);
//            ps.setBoolean(8, agregar);
//            ps.setBoolean(9, editar);
//            ps.setBoolean(10, eliminar);
//
//            // Ejecutar la actualización/insert
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    private void actualizarTablaConPermisos(int menuId, int submenuId, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
//        // Determinar qué tabla corresponde según el menuId
//        JTable tablaCorrespondiente = null;
//
//        switch (menuId) {
//            case 1: // Administración
//                tablaCorrespondiente = tbAdministracion;
//                break;
//            case 2: // Admisión
//                tablaCorrespondiente = tbAdmision;
//                break;
//            case 3: // Registros
//                tablaCorrespondiente = tbRegistros;
//                break;
//            case 4: // Reportes
//                tablaCorrespondiente = tbReportes;
//                break;
//            default:
//                System.out.println("Error: ID de menú no válido.");
//                return;
//        }
//        // Recorrer las filas de la tabla para encontrar el submenú correspondiente
//        for (int row = 0; row < tablaCorrespondiente.getRowCount(); row++) {
//            int idSubmenu = Integer.parseInt(tablaCorrespondiente.getValueAt(row, 0).toString()); // Asegúrate de que el ID del submenú está en la columna 0
//
//            if (idSubmenu == submenuId) {
//                // Actualiza las columnas de permisos en la fila correspondiente
//                tablaCorrespondiente.setValueAt(visualizar, row, 2); // Columna de "Visualizar"
//                tablaCorrespondiente.setValueAt(agregar, row, 3);    // Columna de "Agregar"
//                tablaCorrespondiente.setValueAt(editar, row, 4);     // Columna de "Editar"
//                tablaCorrespondiente.setValueAt(eliminar, row, 5);   // Columna de "Eliminar"
//                break; // Salir del bucle una vez que se haya encontrado el submenú
//            }
//        }
//    }
//    private void toggleTableVisibility(JScrollPane scrollPane) {
//        scrollPane.setVisible(!scrollPane.isVisible());
//        updateLayout();
//    }
//
    private void updateLayout() {
        revalidate(); // Vuelve a validar el diseño
        repaint(); // Repinta la interfaz para mostrar los cambios
    }

//    private void cargarPermisos(int rolId) {
//        String sql = "SELECT * FROM roles_menus_submenus WHERE id_rol = ?";
//        try (PreparedStatement ps = conect.prepareStatement(sql)) {
//            ps.setInt(1, rolId);
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                int menuId = rs.getInt("id_menu");
//                int submenuId = rs.getInt("id_submenu");
//                boolean visualizar = rs.getBoolean("visualizar");
//                boolean agregar = rs.getBoolean("agregar");
//                boolean editar = rs.getBoolean("editar");
//                boolean eliminar = rs.getBoolean("eliminar");
//
//                // Actualizar las tablas con estos valores
//                // Lógica para buscar la fila correcta en la tabla
//                actualizarTablaConPermisos(menuId, submenuId, visualizar, agregar, editar, eliminar);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
// Método para guardar permisos desde una tabla específica
    private void guardarPermisosDeTabla(JTable tabla, int rolId) {
        for (int i = 0; i < tabla.getRowCount(); i++) {
            int menuId = obtenerMenuId(tabla.getValueAt(i, 1).toString());
            Boolean visualizar = (Boolean) tabla.getValueAt(i, 2);
            Boolean agregar = (Boolean) tabla.getValueAt(i, 3);
            Boolean editar = (Boolean) tabla.getValueAt(i, 4);
            Boolean eliminar = (Boolean) tabla.getValueAt(i, 5);

            visualizar = (visualizar != null) ? visualizar : false;
            agregar = (agregar != null) ? agregar : false;
            editar = (editar != null) ? editar : false;
            eliminar = (eliminar != null) ? eliminar : false;

            permisoManager.guardarPermiso(rolId, menuId, visualizar, agregar, editar, eliminar);
        }
    }

    private void limpiarTablas(JTable... tablas) {
        for (JTable tabla : tablas) {
            limpiarTablaPermisos(tabla);
        }
    }

//    public void cargarPermisosDelUsuario(int rolId) {
//        // Supongamos que los permisos están almacenados en la base de datos
//        // Hacer una consulta para obtener los permisos asociados al rol
//        String sql = "SELECT * FROM roles_menus_submenus WHERE id_rol = ?";
//
//        try (PreparedStatement ps = conect.prepareStatement(sql)) {
//            ps.setInt(1, rolId);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    int menuId = rs.getInt("id_menu");
//                    int submenuId = rs.getInt("id_submenu");
//                    boolean visualizar = rs.getBoolean("visualizar");
//                    boolean agregar = rs.getBoolean("agregar");
//                    boolean editar = rs.getBoolean("editar");
//                    boolean eliminar = rs.getBoolean("eliminar");
//
//                    // Basado en el ID del menú y submenú, puedes habilitar o deshabilitar los elementos correspondientes en la interfaz
//                    aplicarPermisos(menuId, submenuId, visualizar, agregar, editar, eliminar);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error al cargar los permisos del usuario.");
//        }
//    }
//// Método para aplicar los permisos a la interfaz
//    private void aplicarPermisos(int menuId, int submenuId, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
//        // Basado en el menuId, identificamos cuál es el menú
//        switch (menuId) {
//            case 1: // Administración
//                aplicarPermisosAdministracion(submenuId, visualizar, agregar, editar, eliminar);
//                break;
//            case 2: // Admisión
//                aplicarPermisosAdmision(submenuId, visualizar, agregar, editar, eliminar);
//                break;
//            case 3: // Registros
//                aplicarPermisosRegistros(submenuId, visualizar, agregar, editar, eliminar);
//                break;
//            case 4: // Reportes
//                aplicarPermisosReportes(submenuId, visualizar, agregar, editar, eliminar);
//                break;
//            default:
//                System.out.println("Menú no encontrado con id: " + menuId);
//        }
//    }
//// Ejemplo para aplicar permisos en el menú de Administración
//    private void aplicarPermisosAdministracion(int submenuId, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
//        switch (submenuId) {
//            case 1: // Usuarios
//                vp.menuAdministration.getMenuComponent(0).setVisible(visualizar); // Por ejemplo, el submenú "Usuarios"
//                vp.btnAgregarUsuario.setEnabled(agregar);
//                vp.btnEditarUsuario.setEnabled(editar);
//                vp.btnEliminarUsuario.setEnabled(eliminar);
//                break;
//            case 2: // Roles
//                vp.menuAdministracion.getMenuComponent(1).setVisible(visualizar); // Submenú "Roles"
//                vp.btnAgregarRol.setEnabled(agregar);
//                vp.btnEditarRol.setEnabled(editar);
//                vp.btnEliminarRol.setEnabled(eliminar);
//                break;
//            // Otros submenús del menú de Administración
//            default:
//                System.out.println("Submenú de Administración no encontrado con id: " + submenuId);
//        }
//    }
//// Ejemplo para el menú de Admisión
//    private void aplicarPermisosAdmision(int submenuId, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
//        switch (submenuId) {
//            case 1: // Clientes
//                vp.menuAdmision.getMenuComponent(0).setVisible(visualizar); // Submenú "Clientes"
//                vp.btnAgregarCliente.setEnabled(agregar);
//                vp.btnEditarCliente.setEnabled(editar);
//                vp.btnEliminarCliente.setEnabled(eliminar);
//                break;
//            case 2: // Marcas
//                vp.menuAdmision.getMenuComponent(1).setVisible(visualizar); // Submenú "Marcas"
//                vp.btnAgregarMarca.setEnabled(agregar);
//                vp.btnEditarMarca.setEnabled(editar);
//                vp.btnEliminarMarca.setEnabled(eliminar);
//                break;
//            // Otros submenús del menú de Admisión
//            default:
//                System.out.println("Submenú de Admisión no encontrado con id: " + submenuId);
//        }
//    }
//// Ejemplo para el menú de Registros
//    private void aplicarPermisosRegistros(int submenuId, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
//        switch (submenuId) {
//            case 1: // Ordenes de Servicio
//                vp.menuRegistros.getMenuComponent(0).setVisible(visualizar); // Submenú "Ordenes de Servicio"
//                vp.btnAgregarOrden.setEnabled(agregar);
//                vp.btnEditarOrden.setEnabled(editar);
//                vp.btnEliminarOrden.setEnabled(eliminar);
//                break;
//            // Otros submenús del menú de Registros
//            default:
//                System.out.println("Submenú de Registros no encontrado con id: " + submenuId);
//        }
//    }
//// Ejemplo para el menú de Reportes
//    private void aplicarPermisosReportes(int submenuId, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
//        switch (submenuId) {
//            case 1: // Trabajos Realizados
//                vp.menuReportes.getMenuComponent(0).setVisible(visualizar); // Submenú "Trabajos Realizados"
//                vp.btnAgregarTrabajo.setEnabled(agregar);
//                vp.btnEditarTrabajo.setEnabled(editar);
//                vp.btnEliminarTrabajo.setEnabled(eliminar);
//                break;
//            // Otros submenús del menú de Reportes
//            default:
//                System.out.println("Submenú de Reportes no encontrado con id: " + submenuId);
//        }
//    }
//// Método para ocultar los menús y submenús
//    private void ocultarMenuYSubmenu(int menuId, int submenuId) {
//        switch (menuId) {
//            case 1: // Menú de Administración
//                vp.menuAdministration.setVisible(false);
//                break;
//            case 2: // Menú de Admisión
//                vp.menuAdmission.setVisible(false);
//                break;
//            case 3: // Menú de Registros
//                vp.menuRegisters.setVisible(false);
//                break;
//            case 4: // Menú de Reportes
//                vp.menuReports.setVisible(false);
//                break;
//            // Similar lógica para ocultar submenús específicos si es necesario
//            default:
//                System.out.println("Menú o submenú no reconocido.");
//                break;
//        }
//    }
    // Método para limpiar los checkboxes en una tabla específica
    private void limpiarTablaPermisos(JTable tabla) {
        for (int i = 0; i < tabla.getRowCount(); i++) {
            tabla.setValueAt(false, i, 2); // Limpiar columna "Visualizar"
            tabla.setValueAt(false, i, 3); // Limpiar columna "Agregar"
            tabla.setValueAt(false, i, 4); // Limpiar columna "Editar"
            tabla.setValueAt(false, i, 5); // Limpiar columna "Eliminar"
        }
    }

    void cargarMenusPorRol(int rolId) {
        String sql = """
        SELECT m.id_menu, m.nombre_menu, s.id_submenu, s.nombre_submenu
        FROM menus m
        LEFT JOIN submenus s ON m.id_menu = s.menu_id
        INNER JOIN roles_menus_submenus r ON r.id_menu = m.id_menu
        WHERE r.id_rol = ? AND r.visualizar = 1
    """;
        try (PreparedStatement ps = conect.prepareStatement(sql)) {
            ps.setInt(1, rolId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Lógica para cargar los menús y submenús permitidos en la interfaz
                int menuId = rs.getInt("id_menu");
                String nombreMenu = rs.getString("nombre_menu");
                int submenuId = rs.getInt("id_submenu");
                String nombreSubmenu = rs.getString("nombre_submenu");
                // Mostrar estos menús y submenús en la interfaz
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ejemplo de cómo manejar la actualización de permisos
    void actualizarPermisos(int rolId, int menuId, int submenuId, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
        String sql = """
        INSERT INTO roles_menus_submenus (id_rol, id_menu, id_submenu, visualizar, agregar, editar, eliminar)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE visualizar = VALUES(visualizar), agregar = VALUES(agregar), editar = VALUES(editar), eliminar = VALUES(eliminar);
    """;
        try (PreparedStatement ps = conect.prepareStatement(sql)) {
            ps.setInt(1, rolId);
            ps.setInt(2, menuId);
            ps.setInt(3, submenuId);
            ps.setBoolean(4, visualizar);
            ps.setBoolean(5, agregar);
            ps.setBoolean(6, editar);
            ps.setBoolean(7, eliminar);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
        cbxTPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTPUActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel1)
                        .addGap(38, 38, 38)
                        .addComponent(cbxTPU, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtIdTPU, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addComponent(btnGrabar)
                .addGap(71, 71, 71)
                .addComponent(btnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addGap(58, 58, 58)
                .addComponent(btnSalir)
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtIdTPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
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
                {"1", "Usuarios", null, null, null, null, null},
                {"2", "Tipo de Usuarios", null, null, null, null, null},
                {"3", "Asisnación de Permisos", null, null, null, null, null},
                {"4", "Empresas", null, null, null, null, null},
                {"5", "Trabajadores", null, null, null, null, null},
                {"6", "Tarifario", null, null, null, null, null},
                {"7", "Productos", null, null, null, null, null},
                {"8", "Formularios", null, null, null, null, null},
                {"9", "Proveedor", null, null, null, null, null},
                {"10", "Convenios", null, null, null, null, null},
                {"11", "Busqueda de Convenios", null, null, null, null, null},
                {"12", "Asignación de Trabajos", null, null, null, null, null},
                {"13", "Puesto de Trabajos", null, null, null, null, null},
                {"14", "Formas de Pago", null, null, null, null, null}
            },
            new String [] {
                "N°", "Programa", "Visualizar", "Agregar", "Editar", "Eliminar", "Todo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbAdministracion);

        tbAdmision.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Clientes", null, null, null, null, null},
                {"2", "Marcas", null, null, null, null, null},
                {"3", "Unidades", null, null, null, null, null},
                {"4", "Tipo de Pagos", null, null, null, null, null},
                {"5", "Tipos de Productos y Materiales", null, null, null, null, null},
                {"6", "Tipo de Maquinarias y Vehiculos", null, null, null, null, null},
                {"7", "Localización", null, null, null, null, null},
                {"8", "Configuración", null, null, null, null, null}
            },
            new String [] {
                "N°", "Programa", "Visualizar", "Agregar", "Editar", "Eliminar", "Todo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbAdmision);

        tbRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Orden de Servicio", null, null, null, null, null},
                {"2", "Ver Ordenes", null, null, null, null, null},
                {"3", "Ventas", null, null, null, null, null},
                {"4", "Compras de Productos y Materiales", null, null, null, null, null},
                {"5", "Compra Equipos y Vehiculos", null, null, null, null, null},
                {"6", "Gastos Generales", null, null, null, null, null},
                {"7", "Kardex", null, null, null, null, null},
                {"8", "Cotizaciones", null, null, null, null, null},
                {"9", "Cancelaciones", null, null, null, null, null}
            },
            new String [] {
                "N°", "Programa", "Visualizar", "Agregar", "Editar", "Eliminar", "Todo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbRegistros);

        tbReportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Trabajos Realizados", null, null, null, null, null},
                {"2", "Estadisticas", null, null, null, null, null},
                {"3", "Deudas por Pagar", null, null, null, null, null},
                {"4", "Deudas por Cobrar", null, null, null, null, null},
                {"5", "Horas Trabajadas", null, null, null, null, null},
                {"6", "Sueldos", null, null, null, null, null},
                {"7", "Stock", null, null, null, null, null},
                {"8", "Trabajos", null, null, null, null, null},
                {"9", "Presupuestos", null, null, null, null, null}
            },
            new String [] {
                "N°", "Programa", "Visualizar", "Agregar", "Editar", "Eliminar", "Todo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAdministracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(chAdministracion)
                                .addGap(112, 112, 112)
                                .addComponent(chAdmision)
                                .addGap(118, 118, 118)
                                .addComponent(chRegistros)
                                .addGap(101, 101, 101)
                                .addComponent(chReportes))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnRegistros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnAdmision, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnReportes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        jScrollPane1.setVisible(true); // Mostrar tabla de Administración
        jScrollPane2.setVisible(false); // Ocultar tabla de Admisión
        jScrollPane3.setVisible(false); // Ocultar tabla de Registros
        jScrollPane5.setVisible(false); // Ocultar tabla de Reportes
        updateLayout();
    }//GEN-LAST:event_btnAdministracionActionPerformed

    private void btnAdmisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmisionActionPerformed
        // Mostrar la tabla de admisión y ocultar las demás
        jScrollPane1.setVisible(false); // Ocultar tabla de Administración
        jScrollPane2.setVisible(true); // Mostrar tabla de Admisión
        jScrollPane3.setVisible(false); // Ocultar tabla de Registros
        jScrollPane5.setVisible(false); // Ocultar tabla de Reportes
        updateLayout();
    }//GEN-LAST:event_btnAdmisionActionPerformed

    private void btnRegistrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrosActionPerformed
        // Mostrar la tabla de registros y ocultar las demás
        jScrollPane1.setVisible(false); // Ocultar tabla de Administración
        jScrollPane2.setVisible(false); // Ocultar tabla de Admisión
        jScrollPane3.setVisible(true); // Mostrar tabla de Registros
        jScrollPane5.setVisible(false); // Ocultar tabla de Reportes
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
        jScrollPane5.setVisible(true); // Mostrar tabla de Reportes
        updateLayout();
    }//GEN-LAST:event_btnReportesActionPerformed

    private void cbxTPUItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTPUItemStateChanged
        MostrarCodigoPorTPU(cbxTPU, txtIdTPU);

    }//GEN-LAST:event_cbxTPUItemStateChanged

    private void cbxTPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTPUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTPUActionPerformed

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
            int rolId = Integer.parseInt(txtIdTPU.getText());

            // Recolectar los permisos de cada tabla (Administración, Admisión, Registros, Reportes)
            guardarPermisosDeTabla(tbAdministracion, rolId);
            guardarPermisosDeTabla(tbAdmision, rolId);
            guardarPermisosDeTabla(tbRegistros, rolId);
            guardarPermisosDeTabla(tbReportes, rolId);

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(null, "Permisos guardados correctamente.");
        } catch (Exception e) {
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdministracion;
    private javax.swing.JButton btnAdmision;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnModificar;
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
    private javax.swing.JTextField txtIdTPU;
    // End of variables declaration//GEN-END:variables

    public void MostrarTipodeUsuarioCombo(JComboBox cbxTPU) {
        String sql = "select * from roles"; // Asegúrate de que la tabla se llama 'roles'
        Statement st;

        try {
            st = conect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxTPU.removeAllItems();

            while (rs.next()) {
                cbxTPU.addItem(rs.getString("nombre")); // Asegúrate de que la columna se llama 'nombre'
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoPorTPU(JComboBox cbxTPU, JTextField idTPU) {

        String consuta = "select id from roles where nombre=?";

        try {
            CallableStatement cs = conect.prepareCall(consuta);
            cs.setString(1, cbxTPU.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                idTPU.setText(rs.getString("id"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

}
