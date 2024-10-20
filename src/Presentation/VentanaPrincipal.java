package Presentation;

import Administration.*;
import Admission.Clientes;
import Admission.Configuracion;
import Admission.tipos_pagosgenerales;
import Register.CompraEquiposVehiculos;
import Admission.Localizacion;
import Admission.Marcas;
import Admission.TipoMaquinariasYVehiculos;
import Admission.TipoProductosMateriales;
import Register.Gastos_Generales;
import Admission.Unidades;
import Register.*;
import Reports.*;
import conectar.Conectar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class VentanaPrincipal extends javax.swing.JFrame {
    
    public JMenu getMenuAdministration() {
        return menuAdministracion;
    }
    
    public JMenu getMenuAdmission() {
        return menuAdmision;
    }
    
    public JMenuItem getMenuAsignacionPermisos() {
        return menuAsignacionPermisos;
    }
    
    public JMenuItem getMenuAsignaciondeTrabajos() {
        return menuAsignaciondeTrabajos;
    }
    
    public JMenuItem getMenuBusquedaConvenios() {
        return menuBusquedaConvenios;
    }
    
    public JMenuItem getMenuCancelaciones() {
        return menuCancelaciones;
    }
    
    public JMenuItem getMenuClientes() {
        return menuClientes;
    }
    
    public JMenuItem getMenuCompraEquyVehi() {
        return menuCompraEquyVehi;
    }
    
    public JMenuItem getMenuCompraProMat() {
        return menuCompraProMat;
    }
    
    public JMenuItem getMenuConfiguracion() {
        return menuConfiguracion;
    }
    
    public JMenuItem getMenuConvenios() {
        return menuConvenios;
    }
    
    public JMenuItem getMenuCotizaciones() {
        return menuCotizaciones;
    }
    
    public JMenuItem getMenuDeudasPorCobrar() {
        return menuDeudasPorCobrar;
    }
    
    public JMenuItem getMenuDeudasPorPagar() {
        return menuDeudasPorPagar;
    }
    
    public JMenuItem getMenuEmpresas() {
        return menuEmpresas;
    }
    
    public JMenuItem getMenuEstadisticas() {
        return menuEstadisticas;
    }
    
    public JMenuItem getMenuFormaDePago() {
        return menuFormaDePago;
    }
    
    public JMenuItem getMenuFormularios() {
        return menuFormularios;
    }
    
    public JMenuItem getMenuGastosGenerales() {
        return menuGastosGenerales;
    }
    
    public JMenuItem getMenuHorasTrabajadas() {
        return menuHorasTrabajadas;
    }
    
    public JMenuItem getMenuKardex() {
        return menuKardex;
    }
    
    public JMenuItem getMenuLocalizacion() {
        return menuLocalizacion;
    }
    
    public JMenuItem getMenuMarcas() {
        return menuMarcas;
    }
    
    public JMenuItem getMenuMenusSubmenus() {
        return menuMenusSubmenus;
    }
    
    public JMenuItem getMenuOrdenes() {
        return menuOrdenes;
    }
    
    public JMenuItem getMenuPresupuesto() {
        return menuPresupuesto;
    }
    
    public JMenuItem getMenuProductos() {
        return menuProductos;
    }
    
    public JMenuItem getMenuProveedor() {
        return menuProveedor;
    }
    
    public JMenuItem getMenuPuestoDeTrabajo() {
        return menuPuestoDeTrabajo;
    }
    
    public JMenu getMenuRegisters() {
        return menuRegistros;
    }
    
    public JMenu getMenuReports() {
        return menuReportes;
    }
    
    public JMenuItem getMenuStock() {
        return menuStock;
    }
    
    public JMenuItem getMenuSueldos() {
        return menuSueldos;
    }
    
    public JMenuItem getMenuTarifario() {
        return menuTarifario;
    }
    
    public JMenuItem getMenuTipoMaqVe() {
        return menuTipoMaqVe;
    }
    
    public JMenuItem getMenuTipoProMat() {
        return menuTipoProductosYMateriales;
    }
    
    public JMenuItem getMenuTipoUsuarios() {
        return menuRoles;
    }
    
    public JMenuItem getMenuTrabajadores() {
        return menuTrabajadores;
    }
    
    public JMenuItem getMenuTrabajos() {
        return menuTrabajos;
    }
    
    public JMenuItem getMenuTrabajosRealizados() {
        return menuTrabajosRealizados;
    }
    
    public JMenuItem getMenuUnidades() {
        return menuUnidades;
    }
    
    public JMenuItem getMenuUsuarios() {
        return menuUsuarios;
    }
    
    public JMenuItem getMenuVentas() {
        return menuVentas;
    }
    
    public JMenuItem getMenuVerOrdenes() {
        return menuVerOrdenes;
    }
    
    public JMenuItem getMenutipo_pagosgenerales() {
        return menutipo_pagosgenerales;
    }
    
    private int tipUsu;
    
    private String usuario; // Variable para almacenar el usuario
    private HorasTrabajadas horasTrabajadas; // Asegúrate de que esta instancia esté correctamente inicializada
//    private PuestoDeTrabajo puestoDeTrabajo;
    private Connection connection;
    
    private Map<String, JMenuItem> menuMap = new HashMap<>();
    
    Loggin lg = new Loggin();
    
    private Usuarios objUsuarios; // Mantener una única instancia
    private Roles objRoles;
////    private AsignacionPermisos objAsignacionPermisos;
    private Empresas objEmpresas;
    private Trabajadores objTrabajadores;
    private Tarifario objTarifario;
    private Productos objProductos;
////    private Formularios objFormularios;
    private Proveedor objProveedor;
////    private Convenios objConvenios;
////    private BusquedaDeConvenios objBusquedaDeConvenios;
    private AsignacionTrabajos objAsignacionTrabajos;
    private PuestoDeTrabajo objPuestoDeTrabajo;
    private FormaDePago objFormaDePago;
    private Menus objMenus;
    private Clientes objClientes;
    private Marcas objMarcas;
    private Unidades objUnidades;
    private tipos_pagosgenerales objtipos_pagosgenerales;
    private TipoProductosMateriales objTipoProductosMateriales;
    private TipoMaquinariasYVehiculos objTipoMaquinariasYVehiculos;
    private Localizacion objLocalizacion;
    private Configuracion objConfiguracion;
    private Orden objOrden;
    private VerOrdenes objVerOrdenes;
////    private Ventas objVentas;
    private ComprasProductosMateriales objComprasProductosMateriales;
    private CompraEquiposVehiculos objCompraEquiposVehiculos;
    private Gastos_Generales objGastos_Generales;
////    private Kardex objKardex;
    private Cotizaciones objCotizaciones;
////    private Cancelaciones objCancelaciones;
    private TrabajosRealizados objTrabajosRealizados;
    private Estadisticas objEstadisticas;
    private DeudasPorPagar objDeudasPorPagar;
    private DeudasPorCobrar objDeudasPorCobrar;
////    private HorasTrabajadas horasTrabajadas1;
    private Sueldos objSueldos;
    private Stock objStock;
    private Trabajos objTrabajos;
    private Presupuesto objPresupuesto;
    
    public VentanaPrincipal(int tipUsu, String usuario) {
        initComponents();
        
        objUsuarios = new Usuarios();
        objRoles = new Roles();
        objAsignacionTrabajos = new AsignacionTrabajos();
//        objBusquedaDeConvenios = new BusquedaDeConvenios();
//        objCancelaciones = new Cancelaciones();
        objClientes = new Clientes();
        objCompraEquiposVehiculos = new CompraEquiposVehiculos();
        objComprasProductosMateriales = new ComprasProductosMateriales();
        objConfiguracion = new Configuracion();
//        objConvenios = new Convenios();
        objCotizaciones = new Cotizaciones();
        objDeudasPorCobrar = new DeudasPorCobrar();
////        objDeudasPorPagar = new DeudasPorPagar();
        objEmpresas = new Empresas();
        objEstadisticas = new Estadisticas();
        objFormaDePago = new FormaDePago();
//        objFormularios = new Formularios();
        objGastos_Generales = new Gastos_Generales();
//        objKardex = new Kardex();
        objLocalizacion = new Localizacion();
        objTipoMaquinariasYVehiculos = new TipoMaquinariasYVehiculos();
        objMarcas = new Marcas();
        objMenus = new Menus();
        objOrden = new Orden();
        objPresupuesto = new Presupuesto();
        objProductos = new Productos();
        objProveedor = new Proveedor();
//        objPuestoDeTrabajo = new objPuestoDeTrabajo();
//        objRTrabajosRealizados = new RTrabajosRealizados();
        objRoles = new Roles();
        objStock = new Stock();
        objSueldos = new Sueldos();
        objTarifario = new Tarifario();
        objTrabajadores = new Trabajadores();
        objtipos_pagosgenerales = new tipos_pagosgenerales();
        objTipoProductosMateriales = new TipoProductosMateriales();
        objTrabajos = new Trabajos();
        objVerOrdenes = new VerOrdenes();
        
        objUnidades = new Unidades();
        
        this.tipUsu = tipUsu;
        this.usuario = usuario;
        
        this.objPuestoDeTrabajo = new PuestoDeTrabajo(); // Inicializamos puestoDeTrabajo
        this.horasTrabajadas = new HorasTrabajadas(); // Constructor sin parámetros
        this.horasTrabajadas.setPuestoDeTrabajo(this.objPuestoDeTrabajo); // Establecer el puesto después

        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
//        cargarPermisos();
        lbUsuario.setText(usuario); // Mostrar el nombre de usuario en la interfaz

        // Inhabilitar la "X" de cierre del JFrame
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Agregar WindowListener para manejar el cierre manualmente
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Aquí puedes agregar la lógica para manejar el cierre de la ventana
                if (JOptionPane.showConfirmDialog(null, "¿Desea salir del Sistema?", "Confirmar salida", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    int trabajadorId = obtenerTrabajadorId(usuario);
                    if (trabajadorId != 0) {
                        horasTrabajadas.registrarFinSesion(trabajadorId);
                        if (horasTrabajadas.puestoDeTrabajo != null) {
                            int idPDT = horasTrabajadas.puestoDeTrabajo.obtenerIdPuestoActivo(trabajadorId);
                            if (idPDT != -1) {
//                            ht.calcularPagos(trabajadorId);
                            } else {
                                System.out.println("No hay puesto activo para el trabajador ID: " + trabajadorId);
                            }
                        } else {
                            System.out.println("Error: puestoDeTrabajo no está inicializado.");
                        }
                    } else {
                        System.out.println("No se encontró el ID del trabajador.");
                    }
                    System.exit(0);
                }
            }
        });
        
        initializeMenuMap(); // Inicializamos el mapa con los menús
    }

    // Constructor vacío para evitar errores en la inicialización por defecto
    public VentanaPrincipal() {
        initComponents();

        // Inhabilitar la "X" de cierre del JFrame
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Agregar WindowListener para manejar el cierre manualmente
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Desea salir del Sistema?", "Confirmar salida", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    // Lógica para cerrar la aplicación aquí
                    System.exit(0);
                }
            }
        });
    }

    // Método reutilizado para agregar el WindowListener
    private void agregarWindowListener() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                manejarCierreSistema();
            }
        });
    }

    // Método para centralizar la lógica de cierre del sistema
    private void manejarCierreSistema() {
        if (JOptionPane.showConfirmDialog(null, "¿Desea salir del Sistema?", "Confirmar salida", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (registrarSalidaDelSistema()) {
                System.exit(0);
            }
        }
    }
    
    private boolean registrarSalidaDelSistema() {
        boolean ocurrioError = false;
        
        try {
            if (this.usuario == null) {
                System.out.println("Error: usuario no está inicializado.");
                return false;
            }
            
            int trabajadorId = obtenerTrabajadorId(this.usuario);
            
            if (trabajadorId != 0) {
                this.horasTrabajadas.registrarFinSesion(trabajadorId); // Registro de fin de sesión

                if (this.horasTrabajadas.puestoDeTrabajo != null) {
                    int idPDT = this.horasTrabajadas.puestoDeTrabajo.obtenerIdPuestoActivo(trabajadorId);
                    
                    if (idPDT != -1) {
                        // Calcular y guardar horas trabajadas y período de pago
                        double horasTrabajadasPorDia = this.horasTrabajadas.calcularHorasTrabajadasPorDia(trabajadorId);
                        LocalDate fecha = LocalDate.now();
                        LocalDate fechaInicioActividades = this.horasTrabajadas.obtenerFechaInicioActividadesDesdeBD();

                        // Obtener el tipo de periodo (Semanal, Quincenal, Mensual)
                        String tipoPeriodo = this.horasTrabajadas.puestoDeTrabajo.obtenerPeriodoPago(idPDT);
                        if (tipoPeriodo != null) {
                            LocalDate[] fechasPeriodo = this.horasTrabajadas.calcularInicioYFinPeriodo(tipoPeriodo, fechaInicioActividades, 1);
                            LocalDate fechaInicio = fechasPeriodo[0];
                            LocalDate fechaFin = fechasPeriodo[1];
                            
                            this.horasTrabajadas.guardarHorasTrabajadas(trabajadorId, horasTrabajadasPorDia, fecha, tipoPeriodo);
                            this.horasTrabajadas.calcularSueldos(trabajadorId, fechaInicio, fechaFin);
                        } else {
                            System.out.println("Error: no se pudo determinar el tipo de periodo.");
                            ocurrioError = true;
                        }
                    } else {
                        System.out.println("No hay puesto activo para el trabajador ID: " + trabajadorId);
                        ocurrioError = true;
                    }
                } else {
                    System.out.println("Error: puestoDeTrabajo no está inicializado.");
                    ocurrioError = true;
                }
            } else {
                System.out.println("No se encontró el ID del trabajador.");
                ocurrioError = true;
            }
        } catch (SQLException ex) {
            System.out.println("Ocurrió un error al registrar fin de sesión o calcular pagos: " + ex.getMessage());
            ex.printStackTrace();
            ocurrioError = true;  // Indicar que hubo un error
        }
        
        return !ocurrioError; // Retornar verdadero si no hubo errores
    }
    
    private void initializeMenuMap() {
        menuMap.put("menuAdministration", menuAdministracion);
        menuMap.put("menuAdmission", menuAdmision);
        menuMap.put("menuRegisters", menuRegistros);
        menuMap.put("menuReports", menuReportes);
        menuMap.put("menuUsuarios", menuUsuarios);
        menuMap.put("menuTipoUsuarios", menuRoles);
        // Añadir los demás menús y submenús aquí...
    }
    
    public void setMenuVisibility(String menuName, boolean visibility) {
        JMenuItem menu = menuMap.get(menuName);
        if (menu != null) {
            menu.setVisible(visibility);
        }
    }
    
    private int obtenerTrabajadorId(String usuario) {
        Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida aquí
        if (connection == null) {
            throw new RuntimeException("Error: La conexión a la base de datos es nula.");
        }
        
        String sql = "SELECT ut.id_trabajador FROM usuario_trabajador ut INNER JOIN usuarios u ON ut.id_usuario = u.idUsuarios WHERE u.usuario = ?";
        
        try {
            
            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.setString(1, usuario);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    int idTrabajador = rs.getInt("id_trabajador");
                    System.out.println("ID del trabajador obtenido: " + idTrabajador);
                    return idTrabajador;
                } else {
                    System.out.println("No se encontró el trabajador para el usuario: " + usuario);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Conectar.getInstancia().devolverConexion(connection);
        }
        return 0;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpEscritorio = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuAdministracion = new javax.swing.JMenu();
        menuUsuarios = new javax.swing.JMenuItem();
        menuRoles = new javax.swing.JMenuItem();
        menuAsignacionPermisos = new javax.swing.JMenuItem();
        menuEmpresas = new javax.swing.JMenuItem();
        menuTrabajadores = new javax.swing.JMenuItem();
        menuTarifario = new javax.swing.JMenuItem();
        menuProductos = new javax.swing.JMenuItem();
        menuFormularios = new javax.swing.JMenuItem();
        menuProveedor = new javax.swing.JMenuItem();
        menuConvenios = new javax.swing.JMenuItem();
        menuBusquedaConvenios = new javax.swing.JMenuItem();
        menuAsignaciondeTrabajos = new javax.swing.JMenuItem();
        menuPuestoDeTrabajo = new javax.swing.JMenuItem();
        menuFormaDePago = new javax.swing.JMenuItem();
        menuMenusSubmenus = new javax.swing.JMenuItem();
        menuAdmision = new javax.swing.JMenu();
        menuClientes = new javax.swing.JMenuItem();
        menuMarcas = new javax.swing.JMenuItem();
        menuUnidades = new javax.swing.JMenuItem();
        menutipo_pagosgenerales = new javax.swing.JMenuItem();
        menuTipoProductosYMateriales = new javax.swing.JMenuItem();
        menuTipoMaqVe = new javax.swing.JMenuItem();
        menuLocalizacion = new javax.swing.JMenuItem();
        menuConfiguracion = new javax.swing.JMenuItem();
        menuRegistros = new javax.swing.JMenu();
        menuOrdenes = new javax.swing.JMenuItem();
        menuVerOrdenes = new javax.swing.JMenuItem();
        menuVentas = new javax.swing.JMenuItem();
        menuCompraProMat = new javax.swing.JMenuItem();
        menuCompraEquyVehi = new javax.swing.JMenuItem();
        menuGastosGenerales = new javax.swing.JMenuItem();
        menuKardex = new javax.swing.JMenuItem();
        menuCotizaciones = new javax.swing.JMenuItem();
        menuCancelaciones = new javax.swing.JMenuItem();
        menuReportes = new javax.swing.JMenu();
        menuTrabajosRealizados = new javax.swing.JMenuItem();
        menuEstadisticas = new javax.swing.JMenuItem();
        menuDeudasPorPagar = new javax.swing.JMenuItem();
        menuDeudasPorCobrar = new javax.swing.JMenuItem();
        menuHorasTrabajadas = new javax.swing.JMenuItem();
        menuSueldos = new javax.swing.JMenuItem();
        menuStock = new javax.swing.JMenuItem();
        menuTrabajos = new javax.swing.JMenuItem();
        menuPresupuesto = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(6);

        jpEscritorio.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jpEscritorioLayout = new javax.swing.GroupLayout(jpEscritorio);
        jpEscritorio.setLayout(jpEscritorioLayout);
        jpEscritorioLayout.setHorizontalGroup(
            jpEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1188, Short.MAX_VALUE)
        );
        jpEscritorioLayout.setVerticalGroup(
            jpEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 512, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));

        jLabel1.setText("User Register as/Usuario Registrado como:");

        lbUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnSalir.setText("Exit/Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(lbUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(btnSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lbUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSalir))
                .addGap(27, 27, 27))
        );

        jMenuBar1.setBackground(new java.awt.Color(102, 102, 255));
        jMenuBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        menuAdministracion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 3));
        menuAdministracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/config.png"))); // NOI18N
        menuAdministracion.setText("Administración          ");

        menuUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/users.png"))); // NOI18N
        menuUsuarios.setLabel("Usuarios");
        menuUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUsuariosActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuUsuarios);

        menuRoles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-cliente.png"))); // NOI18N
        menuRoles.setText("Roles");
        menuRoles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRolesActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuRoles);

        menuAsignacionPermisos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/configuraciones.png"))); // NOI18N
        menuAsignacionPermisos.setText("Asignacion de Permisos");
        menuAsignacionPermisos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAsignacionPermisosActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuAsignacionPermisos);

        menuEmpresas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/box.png"))); // NOI18N
        menuEmpresas.setLabel("Empresas");
        menuEmpresas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEmpresasActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuEmpresas);

        menuTrabajadores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/usuario.png"))); // NOI18N
        menuTrabajadores.setLabel("Trabajadores");
        menuTrabajadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTrabajadoresActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuTrabajadores);

        menuTarifario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        menuTarifario.setText("Tarifario");
        menuTarifario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTarifarioActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuTarifario);

        menuProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/producto.png"))); // NOI18N
        menuProductos.setLabel("Productos");
        menuProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProductosActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuProductos);

        menuFormularios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/place order.png"))); // NOI18N
        menuFormularios.setLabel("Formularios");
        menuFormularios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFormulariosActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuFormularios);

        menuProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/proveedor.png"))); // NOI18N
        menuProveedor.setLabel("Proveedor");
        menuProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProveedorActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuProveedor);

        menuConvenios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        menuConvenios.setText("Convenios");
        menuConvenios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConveniosActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuConvenios);

        menuBusquedaConvenios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        menuBusquedaConvenios.setText("Busqueda de Convenios");
        menuBusquedaConvenios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBusquedaConveniosActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuBusquedaConvenios);

        menuAsignaciondeTrabajos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cliente.png"))); // NOI18N
        menuAsignaciondeTrabajos.setText("Asignación de Trabajos");
        menuAsignaciondeTrabajos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAsignaciondeTrabajosActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuAsignaciondeTrabajos);

        menuPuestoDeTrabajo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/detallista.png"))); // NOI18N
        menuPuestoDeTrabajo.setLabel("Puesto de Trabajo");
        menuPuestoDeTrabajo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPuestoDeTrabajoActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuPuestoDeTrabajo);

        menuFormaDePago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        menuFormaDePago.setText("Formas de Pago");
        menuFormaDePago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFormaDePagoActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuFormaDePago);

        menuMenusSubmenus.setText("Menus - Submenus");
        menuMenusSubmenus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMenusSubmenusActionPerformed(evt);
            }
        });
        menuAdministracion.add(menuMenusSubmenus);

        jMenuBar1.add(menuAdministracion);

        menuAdmision.setBackground(new java.awt.Color(0, 0, 153));
        menuAdmision.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 3));
        menuAdmision.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        menuAdmision.setText("Admisión                    ");

        menuClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cliente.png"))); // NOI18N
        menuClientes.setLabel("Clientes");
        menuClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuClientesActionPerformed(evt);
            }
        });
        menuAdmision.add(menuClientes);

        menuMarcas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/box.png"))); // NOI18N
        menuMarcas.setText("Marcas");
        menuMarcas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMarcasActionPerformed(evt);
            }
        });
        menuAdmision.add(menuMarcas);

        menuUnidades.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/medida.png"))); // NOI18N
        menuUnidades.setText("Unidades");
        menuUnidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUnidadesActionPerformed(evt);
            }
        });
        menuAdmision.add(menuUnidades);

        menutipo_pagosgenerales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/generate bill & print.png"))); // NOI18N
        menutipo_pagosgenerales.setText("Tipo de Pagos Generales");
        menutipo_pagosgenerales.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                menutipo_pagosgeneralesStateChanged(evt);
            }
        });
        menutipo_pagosgenerales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menutipo_pagosgeneralesActionPerformed(evt);
            }
        });
        menuAdmision.add(menutipo_pagosgenerales);

        menuTipoProductosYMateriales.setText("Tipo de Productos y Materiales");
        menuTipoProductosYMateriales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTipoProductosYMaterialesActionPerformed(evt);
            }
        });
        menuAdmision.add(menuTipoProductosYMateriales);

        menuTipoMaqVe.setText("Tipo de Maquinarias y Vehiculos");
        menuTipoMaqVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTipoMaqVeActionPerformed(evt);
            }
        });
        menuAdmision.add(menuTipoMaqVe);

        menuLocalizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/edificio.png"))); // NOI18N
        menuLocalizacion.setText("Localizacion");
        menuLocalizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLocalizacionActionPerformed(evt);
            }
        });
        menuAdmision.add(menuLocalizacion);

        menuConfiguracion.setText("Configuracion");
        menuConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConfiguracionActionPerformed(evt);
            }
        });
        menuAdmision.add(menuConfiguracion);

        jMenuBar1.add(menuAdmision);

        menuRegistros.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 3));
        menuRegistros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/view edit delete product.png"))); // NOI18N
        menuRegistros.setText("Registros                     ");

        menuOrdenes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/view edit delete product.png"))); // NOI18N
        menuOrdenes.setText("Orden de Servicio");
        menuOrdenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOrdenesActionPerformed(evt);
            }
        });
        menuRegistros.add(menuOrdenes);

        menuVerOrdenes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/edit.png"))); // NOI18N
        menuVerOrdenes.setLabel("Ver Ordenes");
        menuVerOrdenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuVerOrdenesActionPerformed(evt);
            }
        });
        menuRegistros.add(menuVerOrdenes);

        menuVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        menuVentas.setText("Ventas");
        menuVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuVentasActionPerformed(evt);
            }
        });
        menuRegistros.add(menuVentas);

        menuCompraProMat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/compras.png"))); // NOI18N
        menuCompraProMat.setText("Compra de Productos y Materiales");
        menuCompraProMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCompraProMatActionPerformed(evt);
            }
        });
        menuRegistros.add(menuCompraProMat);

        menuCompraEquyVehi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/config.png"))); // NOI18N
        menuCompraEquyVehi.setText("Compra Equipos, Vehiculos");
        menuCompraEquyVehi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCompraEquyVehiActionPerformed(evt);
            }
        });
        menuRegistros.add(menuCompraEquyVehi);

        menuGastosGenerales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/place order.png"))); // NOI18N
        menuGastosGenerales.setText("Gastos Generales");
        menuGastosGenerales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuGastosGeneralesActionPerformed(evt);
            }
        });
        menuRegistros.add(menuGastosGenerales);

        menuKardex.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/historial1.png"))); // NOI18N
        menuKardex.setText("Kardex");
        menuKardex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuKardexActionPerformed(evt);
            }
        });
        menuRegistros.add(menuKardex);

        menuCotizaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        menuCotizaciones.setText("Cotizaciones");
        menuCotizaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCotizacionesActionPerformed(evt);
            }
        });
        menuRegistros.add(menuCotizaciones);

        menuCancelaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        menuCancelaciones.setLabel("Cancelaciones");
        menuCancelaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCancelacionesActionPerformed(evt);
            }
        });
        menuRegistros.add(menuCancelaciones);

        jMenuBar1.add(menuRegistros);

        menuReportes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 3));
        menuReportes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf.png"))); // NOI18N
        menuReportes.setText("Reportes                     ");

        menuTrabajosRealizados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/producto_1.png"))); // NOI18N
        menuTrabajosRealizados.setText("Trabajos Realizados");
        menuTrabajosRealizados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTrabajosRealizadosActionPerformed(evt);
            }
        });
        menuReportes.add(menuTrabajosRealizados);

        menuEstadisticas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/historial1.png"))); // NOI18N
        menuEstadisticas.setText("Estadisticas");
        menuEstadisticas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEstadisticasActionPerformed(evt);
            }
        });
        menuReportes.add(menuEstadisticas);

        menuDeudasPorPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/View Bills & Order Placed Details.png"))); // NOI18N
        menuDeudasPorPagar.setLabel("Deudas por Pagar");
        menuDeudasPorPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDeudasPorPagarActionPerformed(evt);
            }
        });
        menuReportes.add(menuDeudasPorPagar);

        menuDeudasPorCobrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/generate bill & print.png"))); // NOI18N
        menuDeudasPorCobrar.setLabel("Deudas por Cobrar");
        menuDeudasPorCobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDeudasPorCobrarActionPerformed(evt);
            }
        });
        menuReportes.add(menuDeudasPorCobrar);

        menuHorasTrabajadas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ayuda.png"))); // NOI18N
        menuHorasTrabajadas.setLabel("Horas Trabajadas");
        menuHorasTrabajadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHorasTrabajadasActionPerformed(evt);
            }
        });
        menuReportes.add(menuHorasTrabajadas);

        menuSueldos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        menuSueldos.setLabel("Sueldos");
        menuSueldos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSueldosActionPerformed(evt);
            }
        });
        menuReportes.add(menuSueldos);

        menuStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/categorias.png"))); // NOI18N
        menuStock.setText("Stock");
        menuStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStockActionPerformed(evt);
            }
        });
        menuReportes.add(menuStock);

        menuTrabajos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cajero.png"))); // NOI18N
        menuTrabajos.setText("Trabajos");
        menuTrabajos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTrabajosActionPerformed(evt);
            }
        });
        menuReportes.add(menuTrabajos);

        menuPresupuesto.setText("Presupuesto");
        menuPresupuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPresupuestoActionPerformed(evt);
            }
        });
        menuReportes.add(menuPresupuesto);

        jMenuBar1.add(menuReportes);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpEscritorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpEscritorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuSueldosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSueldosActionPerformed
        if (!objSueldos.isShowing()) {
            jpEscritorio.add(objSueldos);
            objSueldos.show();
        }
        actualizarPermisosAccion("Sueldos", true, true, true);
    }//GEN-LAST:event_menuSueldosActionPerformed

    private void menuUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUsuariosActionPerformed
        if (!objUsuarios.isShowing()) {
            jpEscritorio.add(objUsuarios);
            objUsuarios.show();
        }
        //Aquí aplicar los permisos al abrir la ventana
        actualizarPermisosAccion("Usuarios", true, true, true); // Cambia según los permisos correctos
    }//GEN-LAST:event_menuUsuariosActionPerformed
    

    private void menuEmpresasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEmpresasActionPerformed
        if (!objEmpresas.isShowing()) {
            jpEscritorio.add(objEmpresas);
            objEmpresas.show();
        }
        actualizarPermisosAccion("Empresas", true, true, true);
    }//GEN-LAST:event_menuEmpresasActionPerformed

    private void menuTrabajadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTrabajadoresActionPerformed
        if (!objTrabajadores.isShowing()) {
            jpEscritorio.add(objTrabajadores);
            objTrabajadores.show();
        }
        actualizarPermisosAccion("Trabajadores", true, true, true);
    }//GEN-LAST:event_menuTrabajadoresActionPerformed

    private void menuTarifarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTarifarioActionPerformed
        if (!objTarifario.isShowing()) {
            jpEscritorio.add(objTarifario);
            objTarifario.show();
        }
        actualizarPermisosAccion("Tarifario", true, true, true);
    }//GEN-LAST:event_menuTarifarioActionPerformed

    private void menuProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProductosActionPerformed
        if (!objProductos.isShowing()) {
            jpEscritorio.add(objProductos);
            objProductos.show();
        }
        actualizarPermisosAccion("Productos", true, true, true);
    }//GEN-LAST:event_menuProductosActionPerformed

    private void menuFormulariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFormulariosActionPerformed
        
        Formularios objFormularios = new Formularios();
        jpEscritorio.add(objFormularios);
        objFormularios.show();

    }//GEN-LAST:event_menuFormulariosActionPerformed

    private void menuProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProveedorActionPerformed
        if (!objProveedor.isShowing()) {
            jpEscritorio.add(objProveedor);
            objProveedor.show();
        }
        actualizarPermisosAccion("Proveedor", true, true, true);
    }//GEN-LAST:event_menuProveedorActionPerformed

    private void menuConveniosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConveniosActionPerformed
        Convenios objConvenios = new Convenios();
        jpEscritorio.add(objConvenios);
        objConvenios.show();

    }//GEN-LAST:event_menuConveniosActionPerformed

    private void menuAsignaciondeTrabajosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAsignaciondeTrabajosActionPerformed
        if(objAsignacionTrabajos.isShowing()) {
        jpEscritorio.add(objAsignacionTrabajos);
        objAsignacionTrabajos.show();
        }
        actualizarPermisosAccion("Asignacion de Trabajos", true, true, true);
    }//GEN-LAST:event_menuAsignaciondeTrabajosActionPerformed

    private void menuPuestoDeTrabajoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPuestoDeTrabajoActionPerformed
        if (!objPuestoDeTrabajo.isShowing()) {
            jpEscritorio.add(objPuestoDeTrabajo);
            objPuestoDeTrabajo.show();
        }
    }//GEN-LAST:event_menuPuestoDeTrabajoActionPerformed

    private void menuClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuClientesActionPerformed
        if (!objClientes.isShowing()) {
            jpEscritorio.add(objClientes);
            objClientes.show();
        }
        actualizarPermisosAccion("Clientes", true, true, true);
    }//GEN-LAST:event_menuClientesActionPerformed

    private void menuOrdenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOrdenesActionPerformed
        if (!objOrden.isShowing()) {
            jpEscritorio.add(objOrden);
            objOrden.show();
        }
        actualizarPermisosAccion("Orden", true, true, true);
    }//GEN-LAST:event_menuOrdenesActionPerformed

    private void menuCotizacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCotizacionesActionPerformed
        if (!objCotizaciones.isShowing()) {
            jpEscritorio.add(objCotizaciones);
            objCotizaciones.show();
        }
        actualizarPermisosAccion("Cotizaciones", true, true, true);
    }//GEN-LAST:event_menuCotizacionesActionPerformed

    private void menuVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVentasActionPerformed
        Ventas objVentas = new Ventas();
        jpEscritorio.add(objVentas);
        objVentas.show();

    }//GEN-LAST:event_menuVentasActionPerformed

    private void menuCompraProMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCompraProMatActionPerformed
        if (!objComprasProductosMateriales.isShowing()) {
            jpEscritorio.add(objComprasProductosMateriales);
            objComprasProductosMateriales.show();
        }
        actualizarPermisosAccion("Compra de Productos y Materiales", true, true, true);
    }//GEN-LAST:event_menuCompraProMatActionPerformed

    private void menuKardexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuKardexActionPerformed
        Kardex objKardex = new Kardex();
        jpEscritorio.add(objKardex);
        objKardex.show();

    }//GEN-LAST:event_menuKardexActionPerformed

    private void menuVerOrdenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVerOrdenesActionPerformed
        if (!objVerOrdenes.isShowing()) {
            jpEscritorio.add(objVerOrdenes);
            objVerOrdenes.show();
        }
        actualizarPermisosAccion("Ver Ordenes", true, true, true);
    }//GEN-LAST:event_menuVerOrdenesActionPerformed

    private void menuCancelacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCancelacionesActionPerformed
        Cancelaciones objCancelaciones = new Cancelaciones();
        jpEscritorio.add(objCancelaciones);
        objCancelaciones.show();

    }//GEN-LAST:event_menuCancelacionesActionPerformed

    private void menuTrabajosRealizadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTrabajosRealizadosActionPerformed
        if (!objTrabajosRealizados.isShowing()) {
            jpEscritorio.add(objTrabajosRealizados);
            objTrabajosRealizados.show();
        }
        actualizarPermisosAccion("Trabajos Realizados", true, true, true);
    }//GEN-LAST:event_menuTrabajosRealizadosActionPerformed

    private void menuEstadisticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEstadisticasActionPerformed
        if (!objEstadisticas.isShowing()) {
            jpEscritorio.add(objEstadisticas);
            objEstadisticas.show();
        }
        actualizarPermisosAccion("Estadisticas", true, true, true);
    }//GEN-LAST:event_menuEstadisticasActionPerformed

    private void menuDeudasPorPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDeudasPorPagarActionPerformed
        if (!objDeudasPorPagar.isShowing()) {
            jpEscritorio.add(objDeudasPorPagar);
            objDeudasPorPagar.show();
        }
        actualizarPermisosAccion("Deudas por Pagar", true, true, true);
    }//GEN-LAST:event_menuDeudasPorPagarActionPerformed

    private void menuDeudasPorCobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDeudasPorCobrarActionPerformed
        if (!objDeudasPorCobrar.isShowing()) {
            jpEscritorio.add(objDeudasPorCobrar);
            objDeudasPorCobrar.show();
        }
        actualizarPermisosAccion("Deudas Por Cobrar", true, true, true);
    }//GEN-LAST:event_menuDeudasPorCobrarActionPerformed

    private void menuHorasTrabajadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHorasTrabajadasActionPerformed
        HorasTrabajadas objHorasTrabajadas = new HorasTrabajadas();
        jpEscritorio.add(objHorasTrabajadas);
        objHorasTrabajadas.show();

    }//GEN-LAST:event_menuHorasTrabajadasActionPerformed

    private void menuStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuStockActionPerformed
        if (!objStock.isShowing()) {
            jpEscritorio.add(objStock);
            objStock.show();
        }
        actualizarPermisosAccion("Stock", true, true, true);
    }//GEN-LAST:event_menuStockActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        System.out.println("Botón de salida presionado");
        
        if (JOptionPane.showConfirmDialog(null, "¿Desea salir del Sistema?", "Acceso", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            boolean ocurrioError = false;
            Connection connection = Conectar.getInstancia().obtenerConexion(); // Obtener la conexión válida aquí
            if (connection == null) {
                throw new RuntimeException("Error: La conexión a la base de datos es nula.");
            }
            
            try {
                if (this.usuario == null) {
                    System.out.println("Error: usuario no está inicializado.");
                    return;
                }
                
                int trabajadorId = obtenerTrabajadorId(this.usuario);
                
                if (trabajadorId != 0) {
                    this.horasTrabajadas.registrarFinSesion(trabajadorId); // Registro de fin de sesión

                    if (this.horasTrabajadas.puestoDeTrabajo != null) {
                        int idPDT = this.horasTrabajadas.puestoDeTrabajo.obtenerIdPuestoActivo(trabajadorId);
                        
                        if (idPDT != -1) {
                            // Calcular y guardar horas trabajadas y período de pago
                            double horasTrabajadasPorDia = this.horasTrabajadas.calcularHorasTrabajadasPorDia(trabajadorId);
                            LocalDate fecha = LocalDate.now();
                            LocalDate fechaInicioActividades = this.horasTrabajadas.obtenerFechaInicioActividadesDesdeBD();

                            // Obtener el tipo de periodo (Semanal, Quincenal, Mensual)
                            String tipoPeriodo = this.horasTrabajadas.puestoDeTrabajo.obtenerPeriodoPago(idPDT);
                            if (tipoPeriodo != null) {
                                LocalDate[] fechasPeriodo = this.horasTrabajadas.calcularInicioYFinPeriodo(tipoPeriodo, fechaInicioActividades, 1);
                                LocalDate fechaInicio = fechasPeriodo[0];
                                LocalDate fechaFin = fechasPeriodo[1];
                                
                                this.horasTrabajadas.guardarHorasTrabajadas(trabajadorId, horasTrabajadasPorDia, fecha, tipoPeriodo);
                                this.horasTrabajadas.calcularSueldos(trabajadorId, fechaInicio, fechaFin);
                                
                                try {
                                    Conectar.getInstancia().cerrarTodasLasConexiones();
                                } catch (Exception ex) {
                                    System.out.println("Error al cerrar la aplicación: " + ex.getMessage());
                                    ex.printStackTrace();
                                    ocurrioError = true;
                                }
                                
                            } else {
                                System.out.println("Error: no se pudo determinar el tipo de periodo.");
                                ocurrioError = true;
                            }
                        } else {
                            System.out.println("No hay puesto activo para el trabajador ID: " + trabajadorId);
                            ocurrioError = true;
                        }
                    } else {
                        System.out.println("Error: puestoDeTrabajo no está inicializado.");
                        ocurrioError = true;
                    }
                } else {
                    System.out.println("No se encontró el ID del trabajador.");
                    ocurrioError = true;
                }
            } catch (Exception ex) {
                System.out.println("Ocurrió un error al registrar fin de sesión o calcular pagos: " + ex.getMessage());
                ex.printStackTrace();
                ocurrioError = true;  // Indicar que hubo un error
            }
            
            if (!ocurrioError) {
                System.exit(0); // Cerrar el sistema si no ocurrió ningún error
            }
        }
    }//GEN-LAST:event_btnSalirActionPerformed

    private void menuRolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRolesActionPerformed
        if (!objRoles.isShowing()) {
            jpEscritorio.add(objRoles);
            objRoles.show();
        }
        actualizarPermisosAccion("Roles", true, true, true);
    }//GEN-LAST:event_menuRolesActionPerformed

    private void menuBusquedaConveniosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBusquedaConveniosActionPerformed
        BusquedaDeConvenios objBusquedaDeConvenios = new BusquedaDeConvenios();
        jpEscritorio.add(objBusquedaDeConvenios);
        objBusquedaDeConvenios.show();
    }//GEN-LAST:event_menuBusquedaConveniosActionPerformed

    private void menuAsignacionPermisosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAsignacionPermisosActionPerformed
        try {
            // Crear la instancia de la ventana de Asignación de Permisos
            AsignacionPermisos objAsignacionPermisos = new AsignacionPermisos(this);

            // Cargar permisos para el Rol actual después de mostrar la ventana
            int rolId = objAsignacionPermisos.getRolId();  // Obtener el Rol Id actual
            if (rolId != -1) {
                
                System.out.println("Rol ID obtenido: " + rolId);

                // Cargar las tablas de permisos con el rolId correcto
                objAsignacionPermisos.cargarPermisosDesdePrincipal();

                // Agregar y mostrar la ventana
                jpEscritorio.add(objAsignacionPermisos);
                objAsignacionPermisos.show();
            }
        } catch (SQLException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuAsignacionPermisosActionPerformed

    private void menuTrabajosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTrabajosActionPerformed
        if (!objTrabajos.isShowing()) {
            jpEscritorio.add(objTrabajos);
            objTrabajos.show();
        }
        actualizarPermisosAccion("Trabajos", true, true, true);
    }//GEN-LAST:event_menuTrabajosActionPerformed

    private void menuCompraEquyVehiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCompraEquyVehiActionPerformed
        if (!objCompraEquiposVehiculos.isShowing()) {
            jpEscritorio.add(objCompraEquiposVehiculos);
            objCompraEquiposVehiculos.show();
        }
        actualizarPermisosAccion("Compra de Equipos y Vehiculos", true, true, true);
    }//GEN-LAST:event_menuCompraEquyVehiActionPerformed

    private void menuMarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMarcasActionPerformed
        if (!objMarcas.isShowing()) {
            jpEscritorio.add(objMarcas);
            objMarcas.show();
        }
        actualizarPermisosAccion("Marcas", true, true, true);
    }//GEN-LAST:event_menuMarcasActionPerformed

    private void menuGastosGeneralesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuGastosGeneralesActionPerformed
        if (!objGastos_Generales.isShowing()) {
            jpEscritorio.add(objGastos_Generales);
            objGastos_Generales.show();
        }
        actualizarPermisosAccion("Gastos Generales", true, true, true);
    }//GEN-LAST:event_menuGastosGeneralesActionPerformed

    private void menutipo_pagosgeneralesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menutipo_pagosgeneralesActionPerformed
        if (!objtipos_pagosgenerales.isShowing()) {
            jpEscritorio.add(objtipos_pagosgenerales);
            objtipos_pagosgenerales.show();
        }
        actualizarPermisosAccion("Tipo de Pagos Generales", true, true, true);
    }//GEN-LAST:event_menutipo_pagosgeneralesActionPerformed

    private void menuUnidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUnidadesActionPerformed
        if (!objUnidades.isShowing()) {
            jpEscritorio.add(objUnidades);
            objUnidades.show();
        }
        actualizarPermisosAccion("Unidades", true, true, true);
    }//GEN-LAST:event_menuUnidadesActionPerformed

    private void menuLocalizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLocalizacionActionPerformed
        if (!objLocalizacion.isShowing()) {
            jpEscritorio.add(objLocalizacion);
            objLocalizacion.show();
        }
        actualizarPermisosAccion("Localizacion", true, true, true);
    }//GEN-LAST:event_menuLocalizacionActionPerformed

    private void menuFormaDePagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFormaDePagoActionPerformed
        if (!objFormaDePago.isShowing()) {
            jpEscritorio.add(objFormaDePago);
            objFormaDePago.show();
        }
        actualizarPermisosAccion("Forma de Pago", true, true, true);
    }//GEN-LAST:event_menuFormaDePagoActionPerformed

    private void menuTipoMaqVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTipoMaqVeActionPerformed
        if (!objTipoMaquinariasYVehiculos.isShowing()) {
            jpEscritorio.add(objTipoMaquinariasYVehiculos);
            objTipoMaquinariasYVehiculos.show();
        }
        actualizarPermisosAccion("Tipo de Maquinarias y Vehiculos", true, true, true);
    }//GEN-LAST:event_menuTipoMaqVeActionPerformed

    private void menuTipoProductosYMaterialesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTipoProductosYMaterialesActionPerformed
        if (!objTipoProductosMateriales.isShowing()) {
            jpEscritorio.add(objTipoProductosMateriales);
            objTipoProductosMateriales.show();
        }
        actualizarPermisosAccion("Tipo de Productos y Materiales", true, true, true);
    }//GEN-LAST:event_menuTipoProductosYMaterialesActionPerformed

    private void menuConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConfiguracionActionPerformed
        if (!objConfiguracion.isShowing()) {
            jpEscritorio.add(objConfiguracion);
            objConfiguracion.show();
        }
        actualizarPermisosAccion("Configuracion", true, true, true);
    }//GEN-LAST:event_menuConfiguracionActionPerformed

    private void menuPresupuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPresupuestoActionPerformed
        if (!objPresupuesto.isShowing()) {
            jpEscritorio.add(objPresupuesto);
            objPresupuesto.show();
        }
        //Aquí aplicar los permisos al abrir la ventana
        actualizarPermisosAccion("Usuarios", true, true, true); // Cambia según los permisos correctos
    }//GEN-LAST:event_menuPresupuestoActionPerformed

    private void menuMenusSubmenusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMenusSubmenusActionPerformed
        Menus objMenus = new Menus();
        jpEscritorio.add(objMenus);
        objMenus.show();
    }//GEN-LAST:event_menuMenusSubmenusActionPerformed

    private void menutipo_pagosgeneralesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_menutipo_pagosgeneralesStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_menutipo_pagosgeneralesStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    public javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jpEscritorio;
    public static final javax.swing.JLabel lbUsuario = new javax.swing.JLabel();
    public javax.swing.JMenu menuAdministracion;
    public javax.swing.JMenu menuAdmision;
    public javax.swing.JMenuItem menuAsignacionPermisos;
    public javax.swing.JMenuItem menuAsignaciondeTrabajos;
    public javax.swing.JMenuItem menuBusquedaConvenios;
    public javax.swing.JMenuItem menuCancelaciones;
    public javax.swing.JMenuItem menuClientes;
    public javax.swing.JMenuItem menuCompraEquyVehi;
    public javax.swing.JMenuItem menuCompraProMat;
    public javax.swing.JMenuItem menuConfiguracion;
    public javax.swing.JMenuItem menuConvenios;
    public javax.swing.JMenuItem menuCotizaciones;
    public javax.swing.JMenuItem menuDeudasPorCobrar;
    public javax.swing.JMenuItem menuDeudasPorPagar;
    public javax.swing.JMenuItem menuEmpresas;
    public javax.swing.JMenuItem menuEstadisticas;
    public javax.swing.JMenuItem menuFormaDePago;
    public javax.swing.JMenuItem menuFormularios;
    public javax.swing.JMenuItem menuGastosGenerales;
    public javax.swing.JMenuItem menuHorasTrabajadas;
    public javax.swing.JMenuItem menuKardex;
    public javax.swing.JMenuItem menuLocalizacion;
    public javax.swing.JMenuItem menuMarcas;
    public javax.swing.JMenuItem menuMenusSubmenus;
    public javax.swing.JMenuItem menuOrdenes;
    public javax.swing.JMenuItem menuPresupuesto;
    public javax.swing.JMenuItem menuProductos;
    public javax.swing.JMenuItem menuProveedor;
    public javax.swing.JMenuItem menuPuestoDeTrabajo;
    public javax.swing.JMenu menuRegistros;
    public javax.swing.JMenu menuReportes;
    public javax.swing.JMenuItem menuRoles;
    public javax.swing.JMenuItem menuStock;
    public javax.swing.JMenuItem menuSueldos;
    public javax.swing.JMenuItem menuTarifario;
    public javax.swing.JMenuItem menuTipoMaqVe;
    public javax.swing.JMenuItem menuTipoProductosYMateriales;
    public javax.swing.JMenuItem menuTrabajadores;
    public javax.swing.JMenuItem menuTrabajos;
    public javax.swing.JMenuItem menuTrabajosRealizados;
    public javax.swing.JMenuItem menuUnidades;
    public javax.swing.JMenuItem menuUsuarios;
    public javax.swing.JMenuItem menuVentas;
    public javax.swing.JMenuItem menuVerOrdenes;
    public javax.swing.JMenuItem menutipo_pagosgenerales;
    // End of variables declaration//GEN-END:variables

    public void actualizarMenuPrincipal(String menuName, boolean visualizar) {
        switch (menuName) {
            case "Administracion":
                menuAdministracion.setVisible(visualizar);
                break;
            case "Admision":
                menuAdmision.setVisible(visualizar);
                break;
            case "Registros":
                menuRegistros.setVisible(visualizar);
                break;
            case "Reportes":
                menuReportes.setVisible(visualizar);
                break;
            default:
                System.out.println("Menú principal no encontrado: " + menuName);
                break;
        }
    }
    
    public void actualizarSubmenu(String menuName, String submenuName, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
        // Localizar el submenú correspondiente en la interfaz
        JMenuItem submenu = getSubmenuFromMenu(menuName, submenuName);
        
        if (submenu != null) {
            submenu.setVisible(visualizar);  // Mostrar u ocultar el submenú

            // Si es necesario, puedes agregar más lógica para los permisos de agregar, editar, y eliminar
            if (submenuName.equals("Usuarios")) {
                actualizarPermisosAccion(submenuName, agregar, editar, eliminar);
            }
        } else {
            System.out.println("Submenú no encontrado: " + submenuName);
        }
    }

// Método auxiliar para obtener un submenú a partir del menú principal
    private JMenuItem getSubmenuFromMenu(String menuName, String submenuName) {
        JMenu menu = null;
        
        switch (menuName) {
            case "Administracion":
                menu = menuAdministracion;
                break;
            case "Admision":
                menu = menuAdmision;
                break;
            case "Registros":
                menu = menuRegistros;
                break;
            case "Reportes":
                menu = menuReportes;
                break;
            default:
                System.out.println("Menú no reconocido: " + menuName);
                return null;
        }
        
        if (menu != null) {
            for (int i = 0; i < menu.getItemCount(); i++) {
                JMenuItem submenu = menu.getItem(i);
                if (submenu.getText().equals(submenuName)) {
                    return submenu;
                }
            }
            System.out.println("Submenú no encontrado: " + submenuName);
        }
        return null;
    }
    
    public void aplicarPermisos(int menuId, String submenuName, boolean visualizar, boolean agregar, boolean editar, boolean eliminar) {
        // Primero, obtenemos el submenú correspondiente
        JMenuItem submenu = getSubmenuFromMenuId(menuId, submenuName);

        // Verificar si se encontró el submenú
        if (submenu != null) {
            // Aplicar permisos de visualización
            submenu.setVisible(visualizar);

            // Si se necesita controlar permisos de agregar, editar o eliminar, puedes aplicar la lógica adicional
            // Aquí podrías habilitar o deshabilitar botones de "Agregar", "Editar", "Eliminar" en las interfaces correspondientes.
            if (submenuName.equals("Usuarios")) {
                actualizarPermisosAccion(submenuName, agregar, editar, eliminar);
            }
        } else {
            System.out.println("Submenú no encontrado: " + submenuName);
        }
    }

    // Método auxiliar para obtener el submenú a partir del id del menú
    private JMenuItem getSubmenuFromMenuId(int menuId, String submenuName) {
        JMenu menu = null;
        
        switch (menuId) {
            case 1:  // Administracion
                menu = menuAdministracion;
                break;
            case 2:  // Admision
                menu = menuAdmision;
                break;
            case 3:  // Registros
                menu = menuRegistros;
                break;
            case 4:  // Reportes
                menu = menuReportes;
                break;
            default:
                System.out.println("Menú no reconocido: " + menuId);
                return null;
        }

        // Recorrer todos los submenús dentro del menú
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem submenu = menu.getItem(i);
            if (submenu.getText().equals(submenuName)) {
                return submenu;
            }
        }
        
        return null;  // Submenú no encontrado
    }
    
    public void actualizarPermisosAccion(String submenuName, boolean agregar, boolean editar, boolean eliminar) {
        switch (submenuName) {
            case "Usuarios":
                // Asegúrate de que los botones no son nulos antes de manipularlos
                if (objUsuarios.btnMostrarDatos != null) {
                    objUsuarios.btnMostrarDatos.setEnabled(true);
                }
                if (objUsuarios.btnGuia != null) {
                    objUsuarios.btnGuia.setEnabled(true);
                }
                if (objUsuarios.btnNuevo != null) {
                    objUsuarios.btnNuevo.setEnabled(true);
                }
                if (objUsuarios.btnGrabar != null) {
                    objUsuarios.btnGrabar.setEnabled(agregar);
                }
                if (objUsuarios.btnModificar != null) {
                    objUsuarios.btnModificar.setEnabled(editar);
                }
                if (objUsuarios.btnActivar != null) {
                    objUsuarios.btnActivar.setEnabled(editar);
                }
                if (objUsuarios.btnInactivar != null) {
                    objUsuarios.btnInactivar.setEnabled(editar);
                }
                if (objUsuarios.rdRelacionar != null) {
                    objUsuarios.rdRelacionar.setEnabled(editar);
                }
                if (objUsuarios.btnEliminar != null) {
                    objUsuarios.btnEliminar.setEnabled(eliminar);
                }
                break;
            
            case "Roles":
                if (objRoles.btnGrabarTU != null) {
                    objRoles.btnGrabarTU.setEnabled(agregar);
                }
                if (objRoles.btnModificar != null) {
                    objRoles.btnModificar.setEnabled(editar);
                }
                if (objRoles.btnActivar != null) {
                    objRoles.btnActivar.setEnabled(editar);
                }
                if (objRoles.btnInactivar != null) {
                    objRoles.btnInactivar.setEnabled(editar);
                }
                if (objRoles.btnEliminar != null) {
                    objRoles.btnInactivar.setEnabled(eliminar);
                }
                break;
            
            case "Asignacion de Permisos":
                break;
            
            case "Empresas":
                if (objEmpresas.btnAgregar != null) {
                    objEmpresas.setEnabled(agregar);
                }
                if (objEmpresas.btnCancelar != null) {
                    objEmpresas.setEnabled(agregar);
                }
                if (objEmpresas.btnNuevo != null) {
                    objEmpresas.setEnabled(agregar);
                }
                if (objEmpresas.btnModificar != null) {
                    objEmpresas.setEnabled(editar);
                }
                if (objEmpresas.btnActivar != null) {
                    objEmpresas.setEnabled(editar);
                }
                if (objEmpresas.btnInactivar != null) {
                    objEmpresas.setEnabled(editar);
                }
                if (objEmpresas.btnEliminar != null) {
                    objEmpresas.setEnabled(eliminar);
                }
                break;
            
            case "Trabajadores":
                if (objTrabajadores.btnGrabar != null) {
                    objTrabajadores.setEnabled(agregar);
                }
                if (objTrabajadores.btnNuevo != null) {
                    objTrabajadores.setEnabled(agregar);
                }
                if (objTrabajadores.btnCancelar != null) {
                    objTrabajadores.setEnabled(agregar);
                }
                if (objTrabajadores.btnModificar != null) {
                    objTrabajadores.setEnabled(editar);
                }
                if (objTrabajadores.btnActivar != null) {
                    objTrabajadores.setEnabled(editar);
                }
                if (objTrabajadores.btnInactivar != null) {
                    objTrabajadores.setEnabled(editar);
                }
                if (objTrabajadores.btnEliminar != null) {
                    objTrabajadores.setEnabled(eliminar);
                }
                break;
            
            case "Tarifario":
                if (objTarifario.btnCancelar != null) {
                    objTarifario.setEnabled(agregar);
                }
                if (objTarifario.btnGuardar != null) {
                    objTarifario.setEnabled(agregar);
                }
                if (objTarifario.btnModificar != null) {
                    objTarifario.setEnabled(editar);
                }
                if (objTarifario.btnEliminar != null) {
                    objTarifario.setEnabled(eliminar);
                }
            
            case "Productos":
                if (objProductos.btnAdd != null) {
                    objProductos.setEnabled(agregar);
                }
                if (objProductos.btnNew != null) {
                    objProductos.setEnabled(agregar);
                }
                if (objProductos.btnCancel != null) {
                    objProductos.setEnabled(agregar);
                }
                if (objProductos.btnUpdate != null) {
                    objProductos.setEnabled(editar);
                }
                if (objProductos.btnActivar != null) {
                    objProductos.setEnabled(editar);
                }
                if (objProductos.btnInactivar != null) {
                    objProductos.setEnabled(editar);
                }
                if (objProductos.btnDelete != null) {
                    objProductos.setEnabled(eliminar);
                }
                break;
            
            case "Formulario":
                break;
            
            case "Proveedor":
                if (objProveedor.btnGrabar != null) {
                    objProveedor.setEnabled(agregar);
                }
                if (objProveedor.btnCancel != null) {
                    objProveedor.setEnabled(agregar);
                }
                if (objProveedor.btnNew != null) {
                    objProveedor.setEnabled(agregar);
                }
                if (objProveedor.btnModificar != null) {
                    objProveedor.setEnabled(editar);
                }
                if (objProveedor.btnReingresar != null) {
                    objProveedor.setEnabled(editar);
                }
                if (objProveedor.btnInactivar != null) {
                    objProveedor.setEnabled(editar);
                }
                if (objProveedor.btnEliminar != null) {
                    objProveedor.setEnabled(eliminar);
                }
                break;
            
            case "Convenios":
                
                break;
            
            case "Busqueda de Convenios":
                break;
            
            case "Asignacion de Trabajos":
                if(objAsignacionTrabajos.btnMostrarTodo !=null){
                    objAsignacionTrabajos.setEnabled(agregar);
                }
                if(objAsignacionTrabajos.btnFiltrar !=null) {
                    objAsignacionTrabajos.setEnabled(agregar);
                }
                if(objAsignacionTrabajos.btnSoloEmpresa !=null) {
                    objAsignacionTrabajos.setEnabled(agregar);
                }
                if(objAsignacionTrabajos.btnProgramar !=null){
                    objAsignacionTrabajos.setEnabled(editar);
                }
                break;
            
            case "Puesto de Trabajo":
                if (objPuestoDeTrabajo.btnGuardar != null) {
                    objPuestoDeTrabajo.setEnabled(agregar);
                }
                if (objPuestoDeTrabajo.btnNuevoTipoDeTrabajo != null) {
                    objPuestoDeTrabajo.setEnabled(agregar);
                }
                if (objPuestoDeTrabajo.btnModificar != null) {
                    objPuestoDeTrabajo.setEnabled(editar);
                }
                if (objPuestoDeTrabajo.btnActivar != null) {
                    objPuestoDeTrabajo.setEnabled(editar);
                }
                if (objPuestoDeTrabajo.btnDesactivar != null) {
                    objPuestoDeTrabajo.setEnabled(editar);
                }
                if (objPuestoDeTrabajo.btnEliminar != null) {
                    objPuestoDeTrabajo.setEnabled(eliminar);
                }
                break;
            
            case "Forma de Pago":
                if (objFormaDePago.btnGuardar != null) {
                    objFormaDePago.setEnabled(agregar);
                }
                if (objFormaDePago.btnCancelar != null) {
                    objFormaDePago.setEnabled(agregar);
                }
                if (objFormaDePago.btnActivar != null) {
                    objFormaDePago.setEnabled(editar);
                }
                if (objFormaDePago.btnInactivar != null) {
                    objFormaDePago.setEnabled(editar);
                }
                if (objFormaDePago.btnEliminar != null) {
                    objFormaDePago.setEnabled(eliminar);
                }
                break;
            
            case "Menu - Submenus":
                if (objMenus.btnAddMenu != null) {
                    objMenus.setEnabled(agregar);
                }
                if (objMenus.btnAddSubmenu != null) {
                    objMenus.setEnabled(agregar);
                }
                if (objMenus.btnModificar != null) {
                    objMenus.setEnabled(editar);
                }
                break;
            
                //ADMISION
            case "Cientes":
                if (objClientes.btnGuardar != null) {
                    objClientes.setEnabled(agregar);
                }
                if (objClientes.btnCancelar != null) {
                    objClientes.setEnabled(agregar);
                }
                if (objClientes.btnNuevo != null) {
                    objClientes.setEnabled(agregar);
                }
                if (objClientes.btnActualizar != null) {
                    objClientes.setEnabled(editar);
                }
                if (objClientes.btnActivar != null) {
                    objClientes.setEnabled(editar);
                }
                if (objClientes.btnInactivar != null) {
                    objClientes.setEnabled(editar);
                }
                if (objClientes.btnEliminar != null) {
                    objClientes.setEnabled(eliminar);
                }
                break;
            
            case "Marcas":
                if (objMarcas.btnGuardar != null) {
                    objMarcas.setEnabled(agregar);
                }
                if (objMarcas.btnCancelar != null) {
                    objMarcas.setEnabled(agregar);
                }
                if (objMarcas.btnModificar != null) {
                    objMarcas.setEnabled(editar);
                }
                if (objMarcas.btnActivar != null) {
                    objMarcas.setEnabled(editar);
                }
                if (objMarcas.btnInactivar != null) {
                    objMarcas.setEnabled(editar);
                }
                if (objMarcas.btnEliminar != null) {
                    objMarcas.setEnabled(eliminar);
                }
                break;
            
            case "Unidades":
                if (objUnidades.btnCancelar != null) {
                    objUnidades.setEnabled(agregar);
                }
                if (objUnidades.btnGuardar != null) {
                    objUnidades.setEnabled(agregar);
                }
                if (objUnidades.btnModificar != null) {
                    objUnidades.setEnabled(editar);
                }
                if (objUnidades.btnActivar != null) {
                    objUnidades.setEnabled(editar);
                }
                if (objUnidades.btnInactivar != null) {
                    objUnidades.setEnabled(editar);
                }
                if (objUnidades.btnEliminar != null) {
                    objUnidades.setEnabled(eliminar);
                }
                break;
            
            case "Tipo de Pagos Generales":
                if (objtipos_pagosgenerales.btnCancelar != null) {
                    objtipos_pagosgenerales.setEnabled(agregar);
                }
                if (objtipos_pagosgenerales.btnGuardar != null) {
                    objtipos_pagosgenerales.setEnabled(agregar);
                }
                if (objtipos_pagosgenerales.btnModificar != null) {
                    objtipos_pagosgenerales.setEnabled(editar);
                }
                if (objtipos_pagosgenerales.btnActivar != null) {
                    objtipos_pagosgenerales.setEnabled(editar);
                }
                if (objtipos_pagosgenerales.btnInactivar != null) {
                    objtipos_pagosgenerales.setEnabled(editar);
                }
                if (objtipos_pagosgenerales.btnEliminar != null) {
                    objtipos_pagosgenerales.setEnabled(eliminar);
                }
                break;
            
            case "Tipo de Productos y Materiales":
                if (objTipoProductosMateriales.btnCancelar != null) {
                    objTipoProductosMateriales.setEnabled(agregar);
                }
                if (objTipoProductosMateriales.btnGuardar != null) {
                    objTipoProductosMateriales.setEnabled(agregar);
                }
                if (objTipoProductosMateriales.btnActivar != null) {
                    objTipoProductosMateriales.setEnabled(editar);
                }
                if (objTipoProductosMateriales.btnInactivar != null) {
                    objTipoProductosMateriales.setEnabled(editar);
                }
                if (objTipoProductosMateriales.btnModificar != null) {
                    objTipoProductosMateriales.setEnabled(editar);
                }
                if (objTipoProductosMateriales.btnEliminar != null) {
                    objTipoProductosMateriales.setEnabled(eliminar);
                }
                break;
            
            case "Tipo de Maquinarias y Vehiculos":
                if (objTipoMaquinariasYVehiculos.btnCancelar != null) {
                    objTipoMaquinariasYVehiculos.setEnabled(agregar);
                }
                if (objTipoMaquinariasYVehiculos.btnGuardar != null) {
                    objTipoMaquinariasYVehiculos.setEnabled(agregar);
                }
                if (objTipoMaquinariasYVehiculos.btnActivar != null) {
                    objTipoMaquinariasYVehiculos.setEnabled(editar);
                }
                if (objTipoMaquinariasYVehiculos.btnInactivar != null) {
                    objTipoMaquinariasYVehiculos.setEnabled(editar);
                }
                if (objTipoMaquinariasYVehiculos.btnModificar != null) {
                    objTipoMaquinariasYVehiculos.setEnabled(editar);
                }
                if (objTipoMaquinariasYVehiculos.btnEliminar != null) {
                    objTipoMaquinariasYVehiculos.setEnabled(eliminar);
                }
                break;
            
            case "Localizacion":
                if (objLocalizacion.btnCancelar != null) {
                    objLocalizacion.setEnabled(agregar);
                }
                if (objLocalizacion.btnGuardar != null) {
                    objLocalizacion.setEnabled(agregar);
                }
                if (objLocalizacion.btnActivar != null) {
                    objLocalizacion.setEnabled(editar);
                }
                if (objLocalizacion.btnInactivar != null) {
                    objLocalizacion.setEnabled(editar);
                }
                if (objLocalizacion.btnEliminar != null) {
                    objLocalizacion.setEnabled(eliminar);
                }
                break;
            
            case "Configuracion":
                if (objConfiguracion.btnGuardar != null) {
                    objConfiguracion.setEnabled(agregar);
                }
                if (objConfiguracion.btnCargarLogo != null) {
                    objConfiguracion.setEnabled(agregar);
                }
                if (objConfiguracion.btnActualizar != null) {
                    objConfiguracion.setEnabled(editar);
                }
                break;
            
                //REGISTROS
            case "Orden de Servicio":
                if (objOrden.btnAdicionar != null) {
                    objOrden.setEnabled(agregar);
                }
                if (objOrden.btnGenerarCompra != null) {
                    objOrden.setEnabled(agregar);
                }
                if (objOrden.btnGenerarCompra != null) {
                    objOrden.setEnabled(agregar);
                }
                if (objOrden.btnMTP != null) {
                    objOrden.setEnabled(agregar);
                }
                if (objOrden.btnServicios != null) {
                    objOrden.setEnabled(agregar);
                }
                if (objOrden.btnTotal != null) {
                    objOrden.setEnabled(agregar);
                }
                if (objOrden.btnEliminar != null) {
                    objOrden.setEnabled(agregar);
                }
                break;
            
            case "Ver Ordenes":
                if (objVerOrdenes.btnBuscarFechaEmpresa != null) {
                    objVerOrdenes.setEnabled(agregar);
                }
                if (objVerOrdenes.btnSoloEmpresa != null) {
                    objVerOrdenes.setEnabled(agregar);
                }
                if (objVerOrdenes.btnMostrarTodo != null) {
                    objVerOrdenes.setEnabled(agregar);
                }
                break;
            
            case "Ventas":
                break;
            
            case "Compra de Productos y Materiales":
                if (objComprasProductosMateriales.btnGuardar != null) {
                    objComprasProductosMateriales.setEnabled(agregar);
                }
                if (objComprasProductosMateriales.btnMostrarStock != null) {
                    objComprasProductosMateriales.setEnabled(agregar);
                }
                if (objComprasProductosMateriales.btnRegistrarCredito != null) {
                    objComprasProductosMateriales.setEnabled(agregar);
                }
                if (objComprasProductosMateriales.btnEliminar != null) {
                    objComprasProductosMateriales.setEnabled(agregar);
                }
                break;
            
            case "Compra de Equipos, Vehiculos":
                if (objCompraEquiposVehiculos.btnGuardar != null) {
                    objCompraEquiposVehiculos.setEnabled(agregar);
                }
                if (objCompraEquiposVehiculos.btnNuevo != null) {
                    objCompraEquiposVehiculos.setEnabled(agregar);
                }
                if (objCompraEquiposVehiculos.btnLimpiar != null) {
                    objCompraEquiposVehiculos.setEnabled(agregar);
                }
                if (objCompraEquiposVehiculos.btnModificar != null) {
                    objCompraEquiposVehiculos.setEnabled(editar);
                }
                if (objCompraEquiposVehiculos.btnBorrar != null) {
                    objCompraEquiposVehiculos.setEnabled(eliminar);
                }
                break;
            
            case "Gastos generales":
                if (objGastos_Generales.btnNuevo != null) {
                    objGastos_Generales.setEnabled(agregar);
                }
                if (objGastos_Generales.btnGuardar != null) {
                    objGastos_Generales.setEnabled(agregar);
                }
                if (objGastos_Generales.btnCancel != null) {
                    objGastos_Generales.setEnabled(agregar);
                }
                if (objGastos_Generales.btnModificar != null) {
                    objGastos_Generales.setEnabled(editar);
                }
                if (objGastos_Generales.btnModificar != null) {
                    objGastos_Generales.setEnabled(editar);
                }
                if (objGastos_Generales.btnBorrar != null) {
                    objGastos_Generales.setEnabled(eliminar);
                }
                break;
            
            case "Kardex":
                break;
            
            case "Cotizaciones":
                if (objCotizaciones.btnAdicionar != null) {
                    objCotizaciones.setEnabled(agregar);
                }
                if (objCotizaciones.btnEliminar != null) {
                    objCotizaciones.setEnabled(agregar);
                }
                if (objCotizaciones.btnGenerar != null) {
                    objCotizaciones.setEnabled(agregar);
                }
                break;
            
            case "Cancelaciones":                
                break;
            
                //REPORTES
            case "Trabajos Realizados":
                if (objTrabajosRealizados.btnBusquedaEmpresa != null) {
                    objTrabajosRealizados.setEnabled(agregar);
                }
                if (objTrabajosRealizados.btnBusquedaFechaEmpresa != null) {
                    objTrabajosRealizados.setEnabled(agregar);
                }
                if (objTrabajosRealizados.btnMostrarTodo != null) {
                    objTrabajosRealizados.setEnabled(agregar);
                }
                if (objTrabajosRealizados.btnPagar != null) {
                    objTrabajosRealizados.setEnabled(editar);
                }
                break;
            
            case "Estadisticas":
                if (objEstadisticas.btnGenerarReporte != null) {
                    objEstadisticas.setEnabled(agregar);
                }
                if (objEstadisticas.btnImprimirGrafica != null) {
                    objEstadisticas.setEnabled(agregar);
                }
                break;
            
            case "Deudas por Pagar":
                if (objDeudasPorPagar.btnCanceladas != null) {
                    objDeudasPorPagar.setEnabled(agregar);
                }
                break;
            
            case "Deudas Por Cobrar":
                if (objDeudasPorCobrar.btnMostrarDeudas != null) {
                    objDeudasPorCobrar.btnMostrarDeudas.setEnabled(true);
                }
                if (objDeudasPorCobrar.btnBusquedaEmpresa != null) {
                    objDeudasPorCobrar.btnBusquedaEmpresa.setEnabled(true);
                }
                if (objDeudasPorCobrar.btnMostrarCanceladas != null) {
                    objDeudasPorCobrar.btnMostrarCanceladas.setEnabled(true);
                }
                if (objDeudasPorCobrar.btnPagar != null) {
                    objDeudasPorCobrar.btnPagar.setEnabled(editar);
                }
                if (objDeudasPorCobrar.btnAbrirPDF != null) {
                    objDeudasPorCobrar.btnAbrirPDF.setEnabled(editar);
                }
                if (objDeudasPorCobrar.btnBusquedaFechaEmpresa != null) {
                    objDeudasPorCobrar.btnBusquedaFechaEmpresa.setEnabled(true);
                }
                break;
            
            case "Horas Trabajadas":   //REVISAR                
                break;
            
            case "Sueldos":
                if (objSueldos.btnCargarPeriodos != null) {
                    objSueldos.setEnabled(agregar);
                }
                if (objSueldos.btnLimpiar != null) {
                    objSueldos.setEnabled(agregar);
                }
                if (objSueldos.btnMostrar != null) {
                    objSueldos.setEnabled(agregar);
                }
                if (objSueldos.btnReporte != null) {
                    objSueldos.setEnabled(agregar);
                }
                break;
            
            case "Stock":
                if (objStock.btnActualizarStock != null) {
                    objStock.setEnabled(agregar);
                }
                if (objStock.btnMostrarTodo != null) {
                    objStock.setEnabled(agregar);
                }
                if (objStock.btnSolicitantes != null) {
                    objStock.setEnabled(agregar);
                }
                break;
            
            case "Trabajos":
                if (objTrabajos.btnFiltrar != null) {
                    objTrabajos.setEnabled(agregar);
                }
                if (objTrabajos.btnMostrarTodo != null) {
                    objTrabajos.setEnabled(agregar);
                }
                if (objTrabajos.btnTrabajoRealizado != null) {
                    objTrabajos.setEnabled(agregar);
                }
                if (objTrabajos.btnReasignar != null) {
                    objTrabajos.setEnabled(editar);
                }
                if (objTrabajos.btnReprogramar != null) {
                    objTrabajos.setEnabled(editar);
                }
                break;
            
            case "Presupuesto":
                break;
            // Asegúrate de que los botones no son nulos antes de manipularlos

            // Repetir para otros submenús...
        }
    }
    
}
