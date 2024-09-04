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
import javax.swing.WindowConstants;

public class VentanaPrincipal extends javax.swing.JFrame {

    private int tipUsu;
    private Conectar conectar;
    private String usuario; // Variable para almacenar el usuario
    private HorasTrabajadas ht; // Asegúrate de que esta instancia esté correctamente inicializada
    private PuestoDeTrabajo puestoDeTrabajo;

    Loggin lg = new Loggin();

    public VentanaPrincipal(int tipUsu, String usuario) {
        initComponents();
        this.conectar = new Conectar();
        this.puestoDeTrabajo = new PuestoDeTrabajo(); // Inicializamos puestoDeTrabajo
        this.ht = new HorasTrabajadas(this.puestoDeTrabajo); // Pasamos puestoDeTrabajo a HorasTrabajadas
        this.tipUsu = tipUsu;
        this.usuario = usuario; // Almacena el usuario

        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        cargarPermisos();

        System.out.println("Usuario en VentanaPrincipal: " + this.usuario); // Mensaje de depuración
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
                        ht.registrarFinSesion(trabajadorId);
                        if (ht.puestoDeTrabajo != null) {
                            int idPDT = ht.puestoDeTrabajo.obtenerIdPuestoActivo(trabajadorId);
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
    }

    // Constructor vacío para evitar errores en la inicialización por defecto
    public VentanaPrincipal() {
        initComponents();
        this.conectar = new Conectar();
        this.puestoDeTrabajo = new PuestoDeTrabajo(); // Inicializamos puestoDeTrabajo
        this.ht = new HorasTrabajadas(this.puestoDeTrabajo); // Pasamos puestoDeTrabajo a HorasTrabajadas
        ht = new HorasTrabajadas();  // Inicializar ht aquí

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpEscritorio = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuAdministration = new javax.swing.JMenu();
        menuUsuarios = new javax.swing.JMenuItem();
        menuTipoUsuarios = new javax.swing.JMenuItem();
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
        menuAdmission = new javax.swing.JMenu();
        menuClientes = new javax.swing.JMenuItem();
        menuMarcas = new javax.swing.JMenuItem();
        menuUnidades = new javax.swing.JMenuItem();
        menutipo_pagosgenerales = new javax.swing.JMenuItem();
        menuTipoProMat = new javax.swing.JMenuItem();
        menuTipoMaqVe = new javax.swing.JMenuItem();
        menuLocalizacion = new javax.swing.JMenuItem();
        menuConfiguracion = new javax.swing.JMenuItem();
        menuRegisters = new javax.swing.JMenu();
        menuOrdenes = new javax.swing.JMenuItem();
        menuVerOrdenes = new javax.swing.JMenuItem();
        menuVentas = new javax.swing.JMenuItem();
        menuCompraProMat = new javax.swing.JMenuItem();
        menuCompraEquyVehi = new javax.swing.JMenuItem();
        menuGastosGenerales = new javax.swing.JMenuItem();
        menuKardex = new javax.swing.JMenuItem();
        menuCotizaciones = new javax.swing.JMenuItem();
        menuCancelaciones = new javax.swing.JMenuItem();
        menuReports = new javax.swing.JMenu();
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jpEscritorioLayout.setVerticalGroup(
            jpEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );

        jLabel1.setText("User Register as/Usuario Registrado como:");

        lbUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnSalir.setText("Exit/Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jMenuBar1.setBackground(new java.awt.Color(102, 102, 255));
        jMenuBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        menuAdministration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 3));
        menuAdministration.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/config.png"))); // NOI18N
        menuAdministration.setText("Administración          ");

        menuUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/users.png"))); // NOI18N
        menuUsuarios.setLabel("Usuarios");
        menuUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUsuariosActionPerformed(evt);
            }
        });
        menuAdministration.add(menuUsuarios);

        menuTipoUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-cliente.png"))); // NOI18N
        menuTipoUsuarios.setText("Tipo de Usuario");
        menuTipoUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTipoUsuariosActionPerformed(evt);
            }
        });
        menuAdministration.add(menuTipoUsuarios);

        menuAsignacionPermisos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/configuraciones.png"))); // NOI18N
        menuAsignacionPermisos.setText("Asignación de Permisos");
        menuAsignacionPermisos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAsignacionPermisosActionPerformed(evt);
            }
        });
        menuAdministration.add(menuAsignacionPermisos);

        menuEmpresas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/box.png"))); // NOI18N
        menuEmpresas.setLabel("Empresas");
        menuEmpresas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEmpresasActionPerformed(evt);
            }
        });
        menuAdministration.add(menuEmpresas);

        menuTrabajadores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/usuario.png"))); // NOI18N
        menuTrabajadores.setLabel("Trabajadores");
        menuTrabajadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTrabajadoresActionPerformed(evt);
            }
        });
        menuAdministration.add(menuTrabajadores);

        menuTarifario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        menuTarifario.setText("Tarifario");
        menuTarifario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTarifarioActionPerformed(evt);
            }
        });
        menuAdministration.add(menuTarifario);

        menuProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/producto.png"))); // NOI18N
        menuProductos.setLabel("Productos");
        menuProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProductosActionPerformed(evt);
            }
        });
        menuAdministration.add(menuProductos);

        menuFormularios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/place order.png"))); // NOI18N
        menuFormularios.setLabel("Formularios");
        menuFormularios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFormulariosActionPerformed(evt);
            }
        });
        menuAdministration.add(menuFormularios);

        menuProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/proveedor.png"))); // NOI18N
        menuProveedor.setLabel("Proveedor");
        menuProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProveedorActionPerformed(evt);
            }
        });
        menuAdministration.add(menuProveedor);

        menuConvenios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        menuConvenios.setText("Convenios");
        menuConvenios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConveniosActionPerformed(evt);
            }
        });
        menuAdministration.add(menuConvenios);

        menuBusquedaConvenios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        menuBusquedaConvenios.setText("Busqueda de Convenios");
        menuBusquedaConvenios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBusquedaConveniosActionPerformed(evt);
            }
        });
        menuAdministration.add(menuBusquedaConvenios);

        menuAsignaciondeTrabajos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cliente.png"))); // NOI18N
        menuAsignaciondeTrabajos.setText("Asignación de Trabajos");
        menuAsignaciondeTrabajos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAsignaciondeTrabajosActionPerformed(evt);
            }
        });
        menuAdministration.add(menuAsignaciondeTrabajos);

        menuPuestoDeTrabajo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/detallista.png"))); // NOI18N
        menuPuestoDeTrabajo.setLabel("Puesto de Trabajo");
        menuPuestoDeTrabajo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPuestoDeTrabajoActionPerformed(evt);
            }
        });
        menuAdministration.add(menuPuestoDeTrabajo);

        menuFormaDePago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        menuFormaDePago.setText("Formas de Pago");
        menuFormaDePago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFormaDePagoActionPerformed(evt);
            }
        });
        menuAdministration.add(menuFormaDePago);

        menuMenusSubmenus.setText("Menus - Submenus");
        menuMenusSubmenus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMenusSubmenusActionPerformed(evt);
            }
        });
        menuAdministration.add(menuMenusSubmenus);

        jMenuBar1.add(menuAdministration);

        menuAdmission.setBackground(new java.awt.Color(0, 0, 153));
        menuAdmission.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 3));
        menuAdmission.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        menuAdmission.setText("Admisión                    ");

        menuClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cliente.png"))); // NOI18N
        menuClientes.setLabel("Clientes");
        menuClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuClientesActionPerformed(evt);
            }
        });
        menuAdmission.add(menuClientes);

        menuMarcas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/box.png"))); // NOI18N
        menuMarcas.setText("Marcas");
        menuMarcas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMarcasActionPerformed(evt);
            }
        });
        menuAdmission.add(menuMarcas);

        menuUnidades.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/medida.png"))); // NOI18N
        menuUnidades.setText("Unidades");
        menuUnidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUnidadesActionPerformed(evt);
            }
        });
        menuAdmission.add(menuUnidades);

        menutipo_pagosgenerales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/generate bill & print.png"))); // NOI18N
        menutipo_pagosgenerales.setText("Tipo de Pagos Generales");
        menutipo_pagosgenerales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menutipo_pagosgeneralesActionPerformed(evt);
            }
        });
        menuAdmission.add(menutipo_pagosgenerales);

        menuTipoProMat.setText("Tipo de Productos y Materiales");
        menuTipoProMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTipoProMatActionPerformed(evt);
            }
        });
        menuAdmission.add(menuTipoProMat);

        menuTipoMaqVe.setText("Tipo de Maquinarias y Vehiculos");
        menuTipoMaqVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTipoMaqVeActionPerformed(evt);
            }
        });
        menuAdmission.add(menuTipoMaqVe);

        menuLocalizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/edificio.png"))); // NOI18N
        menuLocalizacion.setText("Localizacion");
        menuLocalizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLocalizacionActionPerformed(evt);
            }
        });
        menuAdmission.add(menuLocalizacion);

        menuConfiguracion.setText("Configuracion");
        menuConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConfiguracionActionPerformed(evt);
            }
        });
        menuAdmission.add(menuConfiguracion);

        jMenuBar1.add(menuAdmission);

        menuRegisters.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 3));
        menuRegisters.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/view edit delete product.png"))); // NOI18N
        menuRegisters.setText("Registros                     ");

        menuOrdenes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/view edit delete product.png"))); // NOI18N
        menuOrdenes.setText("Orden de Servicio");
        menuOrdenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOrdenesActionPerformed(evt);
            }
        });
        menuRegisters.add(menuOrdenes);

        menuVerOrdenes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/edit.png"))); // NOI18N
        menuVerOrdenes.setLabel("Ver Ordenes");
        menuVerOrdenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuVerOrdenesActionPerformed(evt);
            }
        });
        menuRegisters.add(menuVerOrdenes);

        menuVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        menuVentas.setText("Ventas");
        menuVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuVentasActionPerformed(evt);
            }
        });
        menuRegisters.add(menuVentas);

        menuCompraProMat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/compras.png"))); // NOI18N
        menuCompraProMat.setText("Compras Productos y Materiales");
        menuCompraProMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCompraProMatActionPerformed(evt);
            }
        });
        menuRegisters.add(menuCompraProMat);

        menuCompraEquyVehi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/config.png"))); // NOI18N
        menuCompraEquyVehi.setText("Compra Equipos, Vehiculos");
        menuCompraEquyVehi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCompraEquyVehiActionPerformed(evt);
            }
        });
        menuRegisters.add(menuCompraEquyVehi);

        menuGastosGenerales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/place order.png"))); // NOI18N
        menuGastosGenerales.setText("Gastos Generales");
        menuGastosGenerales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuGastosGeneralesActionPerformed(evt);
            }
        });
        menuRegisters.add(menuGastosGenerales);

        menuKardex.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/historial1.png"))); // NOI18N
        menuKardex.setText("Kardex");
        menuKardex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuKardexActionPerformed(evt);
            }
        });
        menuRegisters.add(menuKardex);

        menuCotizaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        menuCotizaciones.setText("Cotizaciones");
        menuCotizaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCotizacionesActionPerformed(evt);
            }
        });
        menuRegisters.add(menuCotizaciones);

        menuCancelaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        menuCancelaciones.setLabel("Cancelaciones");
        menuCancelaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCancelacionesActionPerformed(evt);
            }
        });
        menuRegisters.add(menuCancelaciones);

        jMenuBar1.add(menuRegisters);

        menuReports.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 3));
        menuReports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf.png"))); // NOI18N
        menuReports.setText("Reportes                     ");

        menuTrabajosRealizados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/producto_1.png"))); // NOI18N
        menuTrabajosRealizados.setText("Trabajos Realizados");
        menuTrabajosRealizados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTrabajosRealizadosActionPerformed(evt);
            }
        });
        menuReports.add(menuTrabajosRealizados);

        menuEstadisticas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/historial1.png"))); // NOI18N
        menuEstadisticas.setText("Estadisticas");
        menuEstadisticas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEstadisticasActionPerformed(evt);
            }
        });
        menuReports.add(menuEstadisticas);

        menuDeudasPorPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/View Bills & Order Placed Details.png"))); // NOI18N
        menuDeudasPorPagar.setLabel("Deudas por Pagar");
        menuDeudasPorPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDeudasPorPagarActionPerformed(evt);
            }
        });
        menuReports.add(menuDeudasPorPagar);

        menuDeudasPorCobrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/generate bill & print.png"))); // NOI18N
        menuDeudasPorCobrar.setLabel("Deudas por Cobrar");
        menuDeudasPorCobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDeudasPorCobrarActionPerformed(evt);
            }
        });
        menuReports.add(menuDeudasPorCobrar);

        menuHorasTrabajadas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ayuda.png"))); // NOI18N
        menuHorasTrabajadas.setLabel("Horas Trabajadas");
        menuHorasTrabajadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHorasTrabajadasActionPerformed(evt);
            }
        });
        menuReports.add(menuHorasTrabajadas);

        menuSueldos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        menuSueldos.setLabel("Sueldos");
        menuSueldos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSueldosActionPerformed(evt);
            }
        });
        menuReports.add(menuSueldos);

        menuStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/categorias.png"))); // NOI18N
        menuStock.setText("Stock");
        menuStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStockActionPerformed(evt);
            }
        });
        menuReports.add(menuStock);

        menuTrabajos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cajero.png"))); // NOI18N
        menuTrabajos.setText("Trabajos");
        menuTrabajos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTrabajosActionPerformed(evt);
            }
        });
        menuReports.add(menuTrabajos);

        menuPresupuesto.setText("Presupuesto");
        menuPresupuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPresupuestoActionPerformed(evt);
            }
        });
        menuReports.add(menuPresupuesto);

        jMenuBar1.add(menuReports);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(lbUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(btnSalir)
                .addContainerGap(624, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpEscritorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpEscritorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(21, 21, 21))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lbUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap()))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSalir)
                        .addGap(21, 21, 21))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuSueldosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSueldosActionPerformed

        Sueldos objPagosPersonal = new Sueldos();
        jpEscritorio.add(objPagosPersonal);
        objPagosPersonal.show();

    }//GEN-LAST:event_menuSueldosActionPerformed

    private void menuUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUsuariosActionPerformed
        Usuarios objUsuarios = new Usuarios();
        jpEscritorio.add(objUsuarios);
        objUsuarios.show();

    }//GEN-LAST:event_menuUsuariosActionPerformed


    private void menuEmpresasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEmpresasActionPerformed
        Empresas objEmpresas = new Empresas();
        jpEscritorio.add(objEmpresas);
        objEmpresas.show();

    }//GEN-LAST:event_menuEmpresasActionPerformed

    private void menuTrabajadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTrabajadoresActionPerformed
        Trabajadores objTrabajadores = new Trabajadores();
        jpEscritorio.add(objTrabajadores);
        objTrabajadores.show();

    }//GEN-LAST:event_menuTrabajadoresActionPerformed

    private void menuTarifarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTarifarioActionPerformed
        Tarifario objServicios = new Tarifario();
        jpEscritorio.add(objServicios);
        objServicios.show();

    }//GEN-LAST:event_menuTarifarioActionPerformed

    private void menuProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProductosActionPerformed
        Productos objProductos = new Productos();
        jpEscritorio.add(objProductos);
        objProductos.show();

    }//GEN-LAST:event_menuProductosActionPerformed

    private void menuFormulariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFormulariosActionPerformed

        Formularios objForms = new Formularios();
        jpEscritorio.add(objForms);
        objForms.show();

    }//GEN-LAST:event_menuFormulariosActionPerformed

    private void menuProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProveedorActionPerformed
        Proveedor objProveedor = new Proveedor();
        jpEscritorio.add(objProveedor);
        objProveedor.show();

    }//GEN-LAST:event_menuProveedorActionPerformed

    private void menuConveniosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConveniosActionPerformed
        Convenios objConvenios = new Convenios();
        jpEscritorio.add(objConvenios);
        objConvenios.show();

    }//GEN-LAST:event_menuConveniosActionPerformed

    private void menuAsignaciondeTrabajosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAsignaciondeTrabajosActionPerformed
        AsignacionTrabajos objAsignacionTrabajos = new AsignacionTrabajos();
        jpEscritorio.add(objAsignacionTrabajos);
        objAsignacionTrabajos.show();

    }//GEN-LAST:event_menuAsignaciondeTrabajosActionPerformed

    private void menuPuestoDeTrabajoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPuestoDeTrabajoActionPerformed
        PuestoDeTrabajo objPuestosdeTrabajo = new PuestoDeTrabajo();
        jpEscritorio.add(objPuestosdeTrabajo);
        objPuestosdeTrabajo.show();

    }//GEN-LAST:event_menuPuestoDeTrabajoActionPerformed

    private void menuClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuClientesActionPerformed
        Clientes objClientes = new Clientes();
        jpEscritorio.add(objClientes);
        objClientes.show();

    }//GEN-LAST:event_menuClientesActionPerformed

    private void menuOrdenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOrdenesActionPerformed
        Orden objOrdenes = new Orden();
        jpEscritorio.add(objOrdenes);
        objOrdenes.show();

    }//GEN-LAST:event_menuOrdenesActionPerformed

    private void menuCotizacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCotizacionesActionPerformed
        Cotizaciones objCargosPendientes = new Cotizaciones();
        jpEscritorio.add(objCargosPendientes);
        objCargosPendientes.show();

    }//GEN-LAST:event_menuCotizacionesActionPerformed

    private void menuVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVentasActionPerformed
        Ventas objVentas = new Ventas();
        jpEscritorio.add(objVentas);
        objVentas.show();

    }//GEN-LAST:event_menuVentasActionPerformed

    private void menuCompraProMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCompraProMatActionPerformed
        ComprasProductosMateriales objGastos = new ComprasProductosMateriales();
        jpEscritorio.add(objGastos);
        objGastos.show();

    }//GEN-LAST:event_menuCompraProMatActionPerformed

    private void menuKardexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuKardexActionPerformed
        Kardex objKardex = new Kardex();
        jpEscritorio.add(objKardex);
        objKardex.show();

    }//GEN-LAST:event_menuKardexActionPerformed

    private void menuVerOrdenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVerOrdenesActionPerformed
        VerOrdenes objVerOrdenes = new VerOrdenes();
        jpEscritorio.add(objVerOrdenes);
        objVerOrdenes.show();

    }//GEN-LAST:event_menuVerOrdenesActionPerformed

    private void menuCancelacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCancelacionesActionPerformed
        Cancelaciones objCancelaciones = new Cancelaciones();
        jpEscritorio.add(objCancelaciones);
        objCancelaciones.show();

    }//GEN-LAST:event_menuCancelacionesActionPerformed

    private void menuTrabajosRealizadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTrabajosRealizadosActionPerformed
        RTrabajosRealizados objRVentas = new RTrabajosRealizados();
        jpEscritorio.add(objRVentas);
        objRVentas.show();

    }//GEN-LAST:event_menuTrabajosRealizadosActionPerformed

    private void menuEstadisticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEstadisticasActionPerformed
        Estadisticas objCompras = new Estadisticas();
        jpEscritorio.add(objCompras);
        objCompras.show();

    }//GEN-LAST:event_menuEstadisticasActionPerformed

    private void menuDeudasPorPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDeudasPorPagarActionPerformed
        DeudasPorPagar objDeudasPorPagar = null;
        try {
            objDeudasPorPagar = new DeudasPorPagar();
        } catch (SQLException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        jpEscritorio.add(objDeudasPorPagar);
        objDeudasPorPagar.show();

    }//GEN-LAST:event_menuDeudasPorPagarActionPerformed

    private void menuDeudasPorCobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDeudasPorCobrarActionPerformed
        DeudasPorCobrar objDeudasPorCobrar = new DeudasPorCobrar();
        jpEscritorio.add(objDeudasPorCobrar);
        objDeudasPorCobrar.show();

    }//GEN-LAST:event_menuDeudasPorCobrarActionPerformed

    private void menuHorasTrabajadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHorasTrabajadasActionPerformed
        HorasTrabajadas objHorasTrabajadas = new HorasTrabajadas();
        jpEscritorio.add(objHorasTrabajadas);
        objHorasTrabajadas.show();

    }//GEN-LAST:event_menuHorasTrabajadasActionPerformed

    private void menuStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuStockActionPerformed
        Stock objStock = new Stock();
        jpEscritorio.add(objStock);
        objStock.show();

    }//GEN-LAST:event_menuStockActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        System.out.println("Botón de salida presionado");
        if (JOptionPane.showConfirmDialog(null, "¿Desea salir del Sistema?", "Acceso", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int trabajadorId = obtenerTrabajadorId(this.usuario);
            if (trabajadorId != 0) {
                ht.registrarFinSesion(trabajadorId); // Registro de fin de sesión

                if (ht.puestoDeTrabajo != null) {
                    int idPDT = ht.puestoDeTrabajo.obtenerIdPuestoActivo(trabajadorId);

                    if (idPDT != -1) {
                        // Calcular y guardar horas trabajadas y período de pago
                        double horasTrabajadas = ht.calcularHorasTrabajadasPorDia(trabajadorId);
                        LocalDate fecha = LocalDate.now();
                        String periodoPago = ht.determinarPeriodoPago(fecha);

                        ht.guardarHorasTrabajadas(trabajadorId, horasTrabajadas, fecha, periodoPago);

                        // Calcular y guardar pagos
                        ht.calcularPagos(trabajadorId);
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
    }//GEN-LAST:event_btnSalirActionPerformed

    private void menuTipoUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTipoUsuariosActionPerformed
        Roles objTipoDeUsuario = new Roles();
        jpEscritorio.add(objTipoDeUsuario);
        objTipoDeUsuario.show();
    }//GEN-LAST:event_menuTipoUsuariosActionPerformed

    private void menuBusquedaConveniosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBusquedaConveniosActionPerformed
        BusquedaDeConvenios objBusquedaDeConvenios = new BusquedaDeConvenios();
        jpEscritorio.add(objBusquedaDeConvenios);
        objBusquedaDeConvenios.show();
    }//GEN-LAST:event_menuBusquedaConveniosActionPerformed

    private void menuAsignacionPermisosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAsignacionPermisosActionPerformed
        AsignacionPermisos objAsignacionPermisos = null;
        try {
            objAsignacionPermisos = new AsignacionPermisos(this);
        } catch (SQLException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        jpEscritorio.add(objAsignacionPermisos);
        objAsignacionPermisos.show();
    }//GEN-LAST:event_menuAsignacionPermisosActionPerformed

    private void menuTrabajosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTrabajosActionPerformed
        Trabajos objTrabajos = new Trabajos();
        jpEscritorio.add(objTrabajos);
        objTrabajos.show();
    }//GEN-LAST:event_menuTrabajosActionPerformed

    private void menuCompraEquyVehiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCompraEquyVehiActionPerformed
        CompraEquiposVehiculos objEquipos = new CompraEquiposVehiculos();
        jpEscritorio.add(objEquipos);
        objEquipos.show();
    }//GEN-LAST:event_menuCompraEquyVehiActionPerformed

    private void menuMarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMarcasActionPerformed
        Marcas objMarcas = new Marcas();
        jpEscritorio.add(objMarcas);
        objMarcas.show();
    }//GEN-LAST:event_menuMarcasActionPerformed

    private void menuGastosGeneralesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuGastosGeneralesActionPerformed
        Gastos_Generales objPagosRecibos = new Gastos_Generales();
        jpEscritorio.add(objPagosRecibos);
        objPagosRecibos.show();
    }//GEN-LAST:event_menuGastosGeneralesActionPerformed

    private void menutipo_pagosgeneralesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menutipo_pagosgeneralesActionPerformed
        tipos_pagosgenerales objConceptoPagos = new tipos_pagosgenerales();
        jpEscritorio.add(objConceptoPagos);
        objConceptoPagos.show();
    }//GEN-LAST:event_menutipo_pagosgeneralesActionPerformed

    private void menuUnidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUnidadesActionPerformed
        Unidades objUnidades = new Unidades();
        jpEscritorio.add(objUnidades);
        objUnidades.show();
    }//GEN-LAST:event_menuUnidadesActionPerformed

    private void menuLocalizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLocalizacionActionPerformed
        Localizacion objLocalizacion = new Localizacion();
        jpEscritorio.add(objLocalizacion);
        objLocalizacion.show();
    }//GEN-LAST:event_menuLocalizacionActionPerformed

    private void menuFormaDePagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFormaDePagoActionPerformed
        FormaDePago objFormasDePago = new FormaDePago();
        jpEscritorio.add(objFormasDePago);
        objFormasDePago.show();
    }//GEN-LAST:event_menuFormaDePagoActionPerformed

    private void menuTipoMaqVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTipoMaqVeActionPerformed
        TipoMaquinariasYVehiculos objTipoMaqVe = new TipoMaquinariasYVehiculos();
        jpEscritorio.add(objTipoMaqVe);
        objTipoMaqVe.show();
    }//GEN-LAST:event_menuTipoMaqVeActionPerformed

    private void menuTipoProMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTipoProMatActionPerformed
        TipoProductosMateriales objTipoProMat = new TipoProductosMateriales();
        jpEscritorio.add(objTipoProMat);
        objTipoProMat.show();
    }//GEN-LAST:event_menuTipoProMatActionPerformed

    private void menuConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConfiguracionActionPerformed
        Configuracion objConfig = new Configuracion();
        jpEscritorio.add(objConfig);
        objConfig.show();
    }//GEN-LAST:event_menuConfiguracionActionPerformed

    private void menuPresupuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPresupuestoActionPerformed
        BudgetManager objBudget = new BudgetManager();
        jpEscritorio.add(objBudget);
        objBudget.show();
    }//GEN-LAST:event_menuPresupuestoActionPerformed

    private void menuMenusSubmenusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMenusSubmenusActionPerformed
        Menus objMenus = new Menus();
        jpEscritorio.add(objMenus);
        objMenus.show();
    }//GEN-LAST:event_menuMenusSubmenusActionPerformed

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
    public javax.swing.JPanel jpEscritorio;
    public static final javax.swing.JLabel lbUsuario = new javax.swing.JLabel();
    public javax.swing.JMenu menuAdministration;
    public javax.swing.JMenu menuAdmission;
    public javax.swing.JMenuItem menuAsignacionPermisos;
    public javax.swing.JMenuItem menuAsignaciondeTrabajos;
    public javax.swing.JMenuItem menuBusquedaConvenios;
    public javax.swing.JMenuItem menuCancelaciones;
    public javax.swing.JMenuItem menuClientes;
    private javax.swing.JMenuItem menuCompraEquyVehi;
    public javax.swing.JMenuItem menuCompraProMat;
    private javax.swing.JMenuItem menuConfiguracion;
    public javax.swing.JMenuItem menuConvenios;
    public javax.swing.JMenuItem menuCotizaciones;
    public javax.swing.JMenuItem menuDeudasPorCobrar;
    public javax.swing.JMenuItem menuDeudasPorPagar;
    public javax.swing.JMenuItem menuEmpresas;
    public javax.swing.JMenuItem menuEstadisticas;
    private javax.swing.JMenuItem menuFormaDePago;
    public javax.swing.JMenuItem menuFormularios;
    private javax.swing.JMenuItem menuGastosGenerales;
    public javax.swing.JMenuItem menuHorasTrabajadas;
    public javax.swing.JMenuItem menuKardex;
    private javax.swing.JMenuItem menuLocalizacion;
    private javax.swing.JMenuItem menuMarcas;
    private javax.swing.JMenuItem menuMenusSubmenus;
    public javax.swing.JMenuItem menuOrdenes;
    private javax.swing.JMenuItem menuPresupuesto;
    public javax.swing.JMenuItem menuProductos;
    public javax.swing.JMenuItem menuProveedor;
    public javax.swing.JMenuItem menuPuestoDeTrabajo;
    public javax.swing.JMenu menuRegisters;
    public javax.swing.JMenu menuReports;
    public javax.swing.JMenuItem menuStock;
    public javax.swing.JMenuItem menuSueldos;
    public javax.swing.JMenuItem menuTarifario;
    private javax.swing.JMenuItem menuTipoMaqVe;
    private javax.swing.JMenuItem menuTipoProMat;
    public javax.swing.JMenuItem menuTipoUsuarios;
    public javax.swing.JMenuItem menuTrabajadores;
    private javax.swing.JMenuItem menuTrabajos;
    public javax.swing.JMenuItem menuTrabajosRealizados;
    private javax.swing.JMenuItem menuUnidades;
    public javax.swing.JMenuItem menuUsuarios;
    public javax.swing.JMenuItem menuVentas;
    public javax.swing.JMenuItem menuVerOrdenes;
    private javax.swing.JMenuItem menutipo_pagosgenerales;
    // End of variables declaration//GEN-END:variables

    private void cargarPermisos() {
        String sql = "SELECT nombre_menu AS menu_name, nombre_submenu AS submenu_name "
                + "FROM roles_permisos rp "
                + "JOIN menus m ON rp.menu_id = m.id "
                + "LEFT JOIN submenus s ON rp.submenu_id = s.id "
                + "WHERE rp.rol_id = ?";
        try (PreparedStatement pst = conectar.getConexion().prepareStatement(sql)) {
            pst.setInt(1, tipUsu);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String menuName = rs.getString("menu_name");
                String submenuName = rs.getString("submenu_name");
                setMenuVisibility(menuName, true);
                if (submenuName != null) {
                    setMenuVisibility(submenuName, true);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar permisos: " + e.getMessage());
        }
    }

    public void setMenuVisibility(String menuName, boolean visibility) {
        switch (menuName) {
            case "menuAdministration":
                menuAdministration.setVisible(visibility);
                break;
            case "menuAdmission":
                menuAdmission.setVisible(visibility);
                break;
            case "menuRegisters":
                menuRegisters.setVisible(visibility);
                break;
            case "menuReports":
                menuReports.setVisible(visibility);
                break;
            case "menuSubAdministration1":
                menuUsuarios.setVisible(visibility);
                break;
            case "menuSubAdministration2":
                menuTipoUsuarios.setVisible(visibility);
                break;
            case "menuSubAdministration3":
                menuAsignacionPermisos.setVisible(visibility);
                break;
            case "menuSubAdministration4":
                menuEmpresas.setVisible(visibility);
                break;
            case "menuSubAdministration5":
                menuTrabajadores.setVisible(visibility);
                break;
            case "menuSubAdministration6":
                menuTarifario.setVisible(visibility);
                break;
            case "menuSubAdministration7":
                menuProductos.setVisible(visibility);
                break;
            case "menuSubAdministration8":
                menuFormularios.setVisible(visibility);
                break;
            case "menuSubAdministration9":
                menuProveedor.setVisible(visibility);
                break;
            case "menuSubAdministration10":
                menuConvenios.setVisible(visibility);
                break;
            case "menuSubAdministration11":
                menuBusquedaConvenios.setVisible(visibility);
                break;
            case "menuSubAdministration12":
                menuAsignaciondeTrabajos.setVisible(visibility);
                break;
            case "menuSubAdministration13":
                menuPuestoDeTrabajo.setVisible(visibility);
                break;
            case "menuSubAdministration14":
                menuFormaDePago.setVisible(visibility);
                break;

            case "menuSubAdmission1":
                menuClientes.setVisible(visibility);
                break;
            case "menuSubAdmission2":
                menuMarcas.setVisible(visibility);
                break;
            case "menuSubAdmission3":
                menuUnidades.setVisible(visibility);
                break;
            case "menuSubAdmission4":
                menutipo_pagosgenerales.setVisible(visibility);
                break;
            case "menuSubAdmission5":
                menuTipoProMat.setVisible(visibility);
                break;
            case "menuSubAdmission6":
                menuTipoMaqVe.setVisible(visibility);
                break;
            case "menuSubAdmission7":
                menuLocalizacion.setVisible(visibility);
                break;
            case "menuSubAdmission8":
                menuConfiguracion.setVisible(visibility);
                break;

            case "menuSubRegister1":
                menuOrdenes.setVisible(visibility);
                break;
            case "menuSubRegister2":
                menuVerOrdenes.setVisible(visibility);
                break;
            case "menuSubRegister3":
                menuVentas.setVisible(visibility);
                break;
            case "menuSubRegister4":
                menuCompraProMat.setVisible(visibility);
                break;
            case "menuSubRegister5":
                menuCompraEquyVehi.setVisible(visibility);
                break;
            case "menuSubRegister6":
                menuGastosGenerales.setVisible(visibility);
                break;
            case "menuSubRegister7":
                menuKardex.setVisible(visibility);
                break;
            case "menuSubRegister8":
                menuCotizaciones.setVisible(visibility);
                break;
            case "menuSubRegister9":
                menuCancelaciones.setVisible(visibility);
                break;

            case "menuSubReports1":
                menuTrabajosRealizados.setVisible(visibility);
                break;
            case "menuSubReports2":
                menuEstadisticas.setVisible(visibility);
                break;
            case "menuSubReports3":
                menuDeudasPorPagar.setVisible(visibility);
                break;
            case "menuSubReports4":
                menuDeudasPorCobrar.setVisible(visibility);
                break;
            case "menuSubReports5":
                menuHorasTrabajadas.setVisible(visibility);
                break;
            case "menuSubReports6":
                menuSueldos.setVisible(visibility);
                break;
            case "menuSubReports7":
                menuStock.setVisible(visibility);
                break;
            case "menuSubReports8":
                menuTrabajos.setVisible(visibility);
                break;
            case "menuSubReports9":
                menuPresupuesto.setVisible(visibility);
                break;

        }
    }

    private int obtenerTrabajadorId(String usuario) {
        String sql = "SELECT ut.id_trabajador FROM usuario_trabajador ut INNER JOIN usuarios u ON ut.id_usuario = u.idUsuarios WHERE u.usuario = ?";
        try {
            PreparedStatement pst = conectar.getConexion().prepareStatement(sql);
            pst.setString(1, usuario);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int idTrabajador = rs.getInt("id_trabajador");
                System.out.println("ID del trabajador obtenido: " + idTrabajador); // Mensaje de depuración
                return idTrabajador;
            } else {
                System.out.println("No se encontró el trabajador para el usuario: " + usuario); // Mensaje de depuración
            }
        } catch (SQLException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
