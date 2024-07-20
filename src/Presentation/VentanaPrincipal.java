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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class VentanaPrincipal extends javax.swing.JFrame {
    private int rolId;
    private Conectar conectar;

     public VentanaPrincipal(int rolId) {
        this.rolId = rolId;
        this.conectar = new Conectar();
        initComponents();
        
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        cargarPermisos();
    }

    public VentanaPrincipal() {
        
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
        FormasDePago = new javax.swing.JMenuItem();
        menuAdmission = new javax.swing.JMenu();
        menuClientes = new javax.swing.JMenuItem();
        Marcas = new javax.swing.JMenuItem();
        Unidades = new javax.swing.JMenuItem();
        tipo_pagosgenerales = new javax.swing.JMenuItem();
        TipoProMat = new javax.swing.JMenuItem();
        TipoMaqVe = new javax.swing.JMenuItem();
        Localizacion = new javax.swing.JMenuItem();
        menuConfiguracion = new javax.swing.JMenuItem();
        menuRegisters = new javax.swing.JMenu();
        menuOrdenes = new javax.swing.JMenuItem();
        menuVerOrdenes = new javax.swing.JMenuItem();
        menuIngreso = new javax.swing.JMenuItem();
        menuGastos = new javax.swing.JMenuItem();
        equipos = new javax.swing.JMenuItem();
        Gastos_Generales = new javax.swing.JMenuItem();
        menuKardex = new javax.swing.JMenuItem();
        menuCargosPendientes = new javax.swing.JMenuItem();
        menuCancelaciones = new javax.swing.JMenuItem();
        menuReports = new javax.swing.JMenu();
        menuTrabajosRealizados = new javax.swing.JMenuItem();
        menuRCompras = new javax.swing.JMenuItem();
        menuDeudasPorPagar = new javax.swing.JMenuItem();
        menuDeudasPorCobrar = new javax.swing.JMenuItem();
        menuHorasTrabajadas = new javax.swing.JMenuItem();
        menuPayroll = new javax.swing.JMenuItem();
        menuStock = new javax.swing.JMenuItem();
        menuTrabajos = new javax.swing.JMenuItem();

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

        FormasDePago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        FormasDePago.setText("Formas de Pago");
        FormasDePago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FormasDePagoActionPerformed(evt);
            }
        });
        menuAdministration.add(FormasDePago);

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

        Marcas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/box.png"))); // NOI18N
        Marcas.setText("Marcas");
        Marcas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MarcasActionPerformed(evt);
            }
        });
        menuAdmission.add(Marcas);

        Unidades.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/medida.png"))); // NOI18N
        Unidades.setText("Unidades");
        Unidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnidadesActionPerformed(evt);
            }
        });
        menuAdmission.add(Unidades);

        tipo_pagosgenerales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/generate bill & print.png"))); // NOI18N
        tipo_pagosgenerales.setText("Tipo de Pago");
        tipo_pagosgenerales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipo_pagosgeneralesActionPerformed(evt);
            }
        });
        menuAdmission.add(tipo_pagosgenerales);

        TipoProMat.setText("Tipo de Productos y Materiales");
        TipoProMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipoProMatActionPerformed(evt);
            }
        });
        menuAdmission.add(TipoProMat);

        TipoMaqVe.setText("Tipo de Maquinarias y Vehiculos");
        TipoMaqVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipoMaqVeActionPerformed(evt);
            }
        });
        menuAdmission.add(TipoMaqVe);

        Localizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/edificio.png"))); // NOI18N
        Localizacion.setText("Localizacion");
        Localizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LocalizacionActionPerformed(evt);
            }
        });
        menuAdmission.add(Localizacion);

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

        menuIngreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        menuIngreso.setText("Ventas");
        menuIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuIngresoActionPerformed(evt);
            }
        });
        menuRegisters.add(menuIngreso);

        menuGastos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/compras.png"))); // NOI18N
        menuGastos.setText("Compras Productos y Materiales");
        menuGastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuGastosActionPerformed(evt);
            }
        });
        menuRegisters.add(menuGastos);

        equipos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/config.png"))); // NOI18N
        equipos.setText("Compra Equipos, Vehiculos");
        equipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                equiposActionPerformed(evt);
            }
        });
        menuRegisters.add(equipos);

        Gastos_Generales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/place order.png"))); // NOI18N
        Gastos_Generales.setText("Gastos Generales");
        Gastos_Generales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Gastos_GeneralesActionPerformed(evt);
            }
        });
        menuRegisters.add(Gastos_Generales);

        menuKardex.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/historial1.png"))); // NOI18N
        menuKardex.setText("Kardex");
        menuKardex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuKardexActionPerformed(evt);
            }
        });
        menuRegisters.add(menuKardex);

        menuCargosPendientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        menuCargosPendientes.setText("Cotizaciones");
        menuCargosPendientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCargosPendientesActionPerformed(evt);
            }
        });
        menuRegisters.add(menuCargosPendientes);

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

        menuRCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/historial1.png"))); // NOI18N
        menuRCompras.setText("Estadisticas");
        menuRCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRComprasActionPerformed(evt);
            }
        });
        menuReports.add(menuRCompras);

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

        menuPayroll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/money.png"))); // NOI18N
        menuPayroll.setLabel("Sueldos");
        menuPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPayrollActionPerformed(evt);
            }
        });
        menuReports.add(menuPayroll);

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

    private void menuPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPayrollActionPerformed

        Sueldos objPagosPersonal = new Sueldos();
        jpEscritorio.add(objPagosPersonal);
        objPagosPersonal.show();

    }//GEN-LAST:event_menuPayrollActionPerformed

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

    private void menuCargosPendientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCargosPendientesActionPerformed
        Cotizaciones objCargosPendientes = new Cotizaciones();
        jpEscritorio.add(objCargosPendientes);
        objCargosPendientes.show();

    }//GEN-LAST:event_menuCargosPendientesActionPerformed

    private void menuIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIngresoActionPerformed
        Ventas objVentas = new Ventas();
        jpEscritorio.add(objVentas);
        objVentas.show();

    }//GEN-LAST:event_menuIngresoActionPerformed

    private void menuGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuGastosActionPerformed
        ComprasProductosMateriales objGastos = new ComprasProductosMateriales();
        jpEscritorio.add(objGastos);
        objGastos.show();

    }//GEN-LAST:event_menuGastosActionPerformed

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

    private void menuRComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRComprasActionPerformed
        Estadisticas objCompras = new Estadisticas();
        jpEscritorio.add(objCompras);
        objCompras.show();

    }//GEN-LAST:event_menuRComprasActionPerformed

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

        if (JOptionPane.showConfirmDialog(null, "¡Desea salir del Sistema?", "Acceso", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnSalirActionPerformed

    private void menuTipoUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTipoUsuariosActionPerformed
        TipoDeUsuario objTipoDeUsuario = new TipoDeUsuario();
        jpEscritorio.add(objTipoDeUsuario);
        objTipoDeUsuario.show();
    }//GEN-LAST:event_menuTipoUsuariosActionPerformed

    private void menuBusquedaConveniosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBusquedaConveniosActionPerformed
        BusquedaDeConvenios objBusquedaDeConvenios = new BusquedaDeConvenios();
        jpEscritorio.add(objBusquedaDeConvenios);
        objBusquedaDeConvenios.show();
    }//GEN-LAST:event_menuBusquedaConveniosActionPerformed

    private void menuAsignacionPermisosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAsignacionPermisosActionPerformed
        AsignacionPermisos objAsignacionPermisos = new AsignacionPermisos();
        jpEscritorio.add(objAsignacionPermisos);
        objAsignacionPermisos.show();
    }//GEN-LAST:event_menuAsignacionPermisosActionPerformed

    private void menuTrabajosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTrabajosActionPerformed
        Trabajos objTrabajos = new Trabajos();
        jpEscritorio.add(objTrabajos);
        objTrabajos.show();
    }//GEN-LAST:event_menuTrabajosActionPerformed

    private void equiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_equiposActionPerformed
        CompraEquiposVehiculos objEquipos = new CompraEquiposVehiculos();
        jpEscritorio.add(objEquipos);
        objEquipos.show();
    }//GEN-LAST:event_equiposActionPerformed

    private void MarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MarcasActionPerformed
        Marcas objMarcas = new Marcas();
        jpEscritorio.add(objMarcas);
        objMarcas.show();
    }//GEN-LAST:event_MarcasActionPerformed

    private void Gastos_GeneralesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Gastos_GeneralesActionPerformed
        Gastos_Generales objPagosRecibos = new Gastos_Generales();
        jpEscritorio.add(objPagosRecibos);
        objPagosRecibos.show();
    }//GEN-LAST:event_Gastos_GeneralesActionPerformed

    private void tipo_pagosgeneralesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipo_pagosgeneralesActionPerformed
        tipos_pagosgenerales objConceptoPagos = new tipos_pagosgenerales();
        jpEscritorio.add(objConceptoPagos);
        objConceptoPagos.show();
    }//GEN-LAST:event_tipo_pagosgeneralesActionPerformed

    private void UnidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnidadesActionPerformed
        Unidades objUnidades = new Unidades();
        jpEscritorio.add(objUnidades);
        objUnidades.show();
    }//GEN-LAST:event_UnidadesActionPerformed

    private void LocalizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LocalizacionActionPerformed
        Localizacion objLocalizacion = new Localizacion();
        jpEscritorio.add(objLocalizacion);
        objLocalizacion.show();
    }//GEN-LAST:event_LocalizacionActionPerformed

    private void FormasDePagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FormasDePagoActionPerformed
        FormaDePago objFormasDePago = new FormaDePago();
        jpEscritorio.add(objFormasDePago);
        objFormasDePago.show();
    }//GEN-LAST:event_FormasDePagoActionPerformed

    private void TipoMaqVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TipoMaqVeActionPerformed
        TipoMaquinariasYVehiculos objTipoMaqVe = new TipoMaquinariasYVehiculos();
        jpEscritorio.add(objTipoMaqVe);
        objTipoMaqVe.show();
    }//GEN-LAST:event_TipoMaqVeActionPerformed

    private void TipoProMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TipoProMatActionPerformed
        TipoProductosMateriales objTipoProMat = new TipoProductosMateriales();
        jpEscritorio.add(objTipoProMat);
        objTipoProMat.show();
    }//GEN-LAST:event_TipoProMatActionPerformed

    private void menuConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConfiguracionActionPerformed
        Configuracion objConfig = new Configuracion();
        jpEscritorio.add(objConfig);
        objConfig.show();
    }//GEN-LAST:event_menuConfiguracionActionPerformed

    
    
    
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
                new VentanaPrincipal(1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem FormasDePago;
    private javax.swing.JMenuItem Gastos_Generales;
    private javax.swing.JMenuItem Localizacion;
    private javax.swing.JMenuItem Marcas;
    private javax.swing.JMenuItem TipoMaqVe;
    private javax.swing.JMenuItem TipoProMat;
    private javax.swing.JMenuItem Unidades;
    private javax.swing.JButton btnSalir;
    private javax.swing.JMenuItem equipos;
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
    public javax.swing.JMenuItem menuCargosPendientes;
    public javax.swing.JMenuItem menuClientes;
    private javax.swing.JMenuItem menuConfiguracion;
    public javax.swing.JMenuItem menuConvenios;
    public javax.swing.JMenuItem menuDeudasPorCobrar;
    public javax.swing.JMenuItem menuDeudasPorPagar;
    public javax.swing.JMenuItem menuEmpresas;
    public javax.swing.JMenuItem menuFormularios;
    public javax.swing.JMenuItem menuGastos;
    public javax.swing.JMenuItem menuHorasTrabajadas;
    public javax.swing.JMenuItem menuIngreso;
    public javax.swing.JMenuItem menuKardex;
    public javax.swing.JMenuItem menuOrdenes;
    public javax.swing.JMenuItem menuPayroll;
    public javax.swing.JMenuItem menuProductos;
    public javax.swing.JMenuItem menuProveedor;
    public javax.swing.JMenuItem menuPuestoDeTrabajo;
    public javax.swing.JMenuItem menuRCompras;
    public javax.swing.JMenu menuRegisters;
    public javax.swing.JMenu menuReports;
    public javax.swing.JMenuItem menuStock;
    public javax.swing.JMenuItem menuTarifario;
    public javax.swing.JMenuItem menuTipoUsuarios;
    public javax.swing.JMenuItem menuTrabajadores;
    private javax.swing.JMenuItem menuTrabajos;
    public javax.swing.JMenuItem menuTrabajosRealizados;
    public javax.swing.JMenuItem menuUsuarios;
    public javax.swing.JMenuItem menuVerOrdenes;
    private javax.swing.JMenuItem tipo_pagosgenerales;
    // End of variables declaration//GEN-END:variables

    private void cargarPermisos() {
        String sql = "SELECT m.nombre_menu, s.nombre_submenu, p.nombre_permiso " +
                     "FROM permisos_menus_submenus pms " +
                     "JOIN permisos p ON pms.permiso_id = p.id " +
                     "LEFT JOIN menus m ON pms.menu_id = m.id " +
                     "LEFT JOIN submenus s ON pms.submenu_id = s.id " +
                     "JOIN roles_permisos rp ON rp.permiso_id = p.id " +
                     "WHERE rp.rol_id = ?";

        try {
            PreparedStatement pst = conectar.getConexion().prepareStatement(sql);
            pst.setInt(1, rolId);
            ResultSet rs = pst.executeQuery();

            Map<String, JMenu> menuMap = new HashMap<>();
            while (rs.next()) {
                String menuName = rs.getString("nombre_menu");
                String submenuName = rs.getString("nombre_submenu");
                String permisoName = rs.getString("nombre_permiso");

                JMenu menu = menuMap.computeIfAbsent(menuName, k -> {
                    JMenu newMenu = new JMenu(menuName);
                    jMenuBar1.add(newMenu); // Asegúrate de que jMenuBar1 esté correctamente inicializado
                    return newMenu;
                });

                if (submenuName != null) {
                    JMenuItem submenu = new JMenuItem(submenuName);
                    menu.add(submenu);
                }

                // Aquí puedes habilitar o deshabilitar los permisos específicos si es necesario
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
