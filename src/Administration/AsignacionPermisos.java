package Administration;

import Presentation.VentanaPrincipal;
import java.sql.Statement;
import conectar.Conectar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

public class AsignacionPermisos extends javax.swing.JInternalFrame {

    Conectar con = new Conectar();
    VentanaPrincipal vp = new VentanaPrincipal();

    public AsignacionPermisos() {
        initComponents();
        MostrarTipodeUsuarioCombo(cbxTPU);

        jScrollPane1.setVisible(false);
        jScrollPane2.setVisible(false);
        jScrollPane3.setVisible(false);
        jScrollPane5.setVisible(false);

        // Configurar listeners para los botones y checkboxes
        setupListeners();
        setupTableModelListener(tbAdministracion);
        setupTableModelListener(tbAdmision);
        setupTableModelListener(tbRegistros);
        setupTableModelListener(tbReportes);
    }
    
    private void setupListeners() {
        btnAdministracion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                toggleTableVisibility(jScrollPane1);
            }
        });

        btnAdmision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                toggleTableVisibility(jScrollPane2);
            }
        });

        btnRegistros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                toggleTableVisibility(jScrollPane3);
            }
        });

        btnReportes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                toggleTableVisibility(jScrollPane5);
            }
        });

        chAdministracion.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                vp.menuAdministration.setVisible(e.getStateChange() == ItemEvent.SELECTED);
                updateLayout();
            }
        });

        chAdmision.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                vp.menuAdmission.setVisible(e.getStateChange() == ItemEvent.SELECTED);
                updateLayout();
            }
        });

        chRegistros.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                vp.menuRegisters.setVisible(e.getStateChange() == ItemEvent.SELECTED);
                updateLayout();
            }
        });

        chReportes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                vp.menuReports.setVisible(e.getStateChange() == ItemEvent.SELECTED);
                updateLayout();
            }
        });
        
        
    }
    
    private void setupTableModelListener(JTable table) {
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (column == 6) { // Si se cambia la columna 6 (índice 5, ya que comienza en 0)
                        Boolean selected = (Boolean) table.getValueAt(row, column);
                        for (int i = 2; i <= 5; i++) {
                            table.setValueAt(selected, row, i);
                        }
                    }
                }
            }
        });
    }

    private void toggleTableVisibility(JScrollPane scrollPane) {
        scrollPane.setVisible(!scrollPane.isVisible());
        updateLayout();
    }

    private void updateLayout() {
        revalidate();
        repaint();
    }
    
       private void initTable(javax.swing.JTable table, javax.swing.JButton btn) {
        addCheckBox(2, table);
        addCheckBox(3, table);
        addCheckBox(4, table);
        addCheckBox(5, table);
        addCheckBox(6, table);
        btn.setVisible(false); // Botón oculto al inicio
    }

    private void addTableModelListener(javax.swing.JTable table, javax.swing.JButton btn) {
    table.getModel().addTableModelListener(e -> {
        if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
            int column = e.getColumn();
            int row = e.getFirstRow();

            if (column < 2 || column > 5) {
                return; // No hacemos nada si se modifica una columna distinta a las primeras 4 columnas
            }

            boolean allSelected = true;

            for (int i = 2; i <= 5; i++) {
                Object value = table.getValueAt(row, i);
                if (!(value instanceof Boolean) || !(Boolean) value) {
                    allSelected = false;
                    break;
                }
            }

            // Si todas las primeras 4 casillas están marcadas, marcamos la casilla 5, si no, la desmarcamos
            table.setValueAt(allSelected, row, 6);

            // Mostramos u ocultamos el botón dependiendo del estado de la casilla 5
            btn.setVisible(allSelected);
            updateLayout();
        }
    });
    }

    private void addCheckBox(int column, javax.swing.JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
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

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close.png"))); // NOI18N
        btnCancelar.setText("Cancelar");

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnModificar.setText("Modificar");

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
                {"6", "Servicios", null, null, null, null, null},
                {"7", "Productos", null, null, null, null, null},
                {"8", "Formularios", null, null, null, null, null},
                {"9", "Proveedor", null, null, null, null, null},
                {"10", "Convenios", null, null, null, null, null},
                {"11", "Busqueda de Convenios", null, null, null, null, null},
                {"12", "Asignación de Trabajos", null, null, null, null, null},
                {"13", "Puesto de Trabajos", null, null, null, null, null}
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
                {"1", "Clientes", null, null, null, null, null}
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
                {"1", "Abrir y Cerrar Caja", null, null, null, null, null},
                {"2", "Orden", null, null, null, null, null},
                {"3", "Cargos Pendientes", null, null, null, null, null},
                {"4", "Ventas", null, null, null, null, null},
                {"5", "Compras", null, null, null, null, null},
                {"6", "Kardex", null, null, null, null, null},
                {"7", "Ver Ordenes", null, null, null, null, null},
                {"8", "Cancelaciones", null, null, null, null, null}
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
                {"1", "Ventas", null, null, null, null, null},
                {"2", "Compras", null, null, null, null, null},
                {"3", "Deudas por Pagar", null, null, null, null, null},
                {"4", "Deudas por Cobrar", null, null, null, null, null},
                {"5", "Horas Trabajadas", null, null, null, null, null},
                {"6", "Servicios", null, null, null, null, null},
                {"7", "Stock", null, null, null, null, null}
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
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5))
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
                                .addComponent(chReportes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnRegistros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnReportes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnAdmision, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3)))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdmision)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReportes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 505, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 505, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnAdministracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdministracionActionPerformed
        
    }//GEN-LAST:event_btnAdministracionActionPerformed

    private void btnAdmisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmisionActionPerformed
        
    }//GEN-LAST:event_btnAdmisionActionPerformed

    private void btnRegistrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrosActionPerformed
        
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
        
    }//GEN-LAST:event_btnReportesActionPerformed

    private void cbxTPUItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTPUItemStateChanged
        MostrarCodigoPorTPU(cbxTPU, txtIdTPU);

    }//GEN-LAST:event_cbxTPUItemStateChanged

    private void cbxTPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTPUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTPUActionPerformed

    private void chAdministracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chAdministracionActionPerformed
//        if (vp != null && vp.menuAdministration != null) {
//            vp.menuAdministration.setVisible(chAdministracion.isSelected());
//            jScrollPane1.setVisible(chAdministracion.isSelected());
//            updateLayout();
//        } else {
//            System.out.println("vp.menuAdministracion es null");
//        }
    }//GEN-LAST:event_chAdministracionActionPerformed

    private void chAdmisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chAdmisionActionPerformed
//        if (vp != null && vp.menuAdmission != null) {
//            vp.menuAdmission.setVisible(chAdmision.isSelected());
//            jScrollPane1.setVisible(chAdmision.isSelected());
//            updateLayout();
//        } else {
//            System.out.println("vp.menuAdmission es null");
//        }
    }//GEN-LAST:event_chAdmisionActionPerformed

    private void chRegistrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chRegistrosActionPerformed
//        if (vp != null && vp.menuRegisters != null) {
//            vp.menuRegisters.setVisible(chRegistros.isSelected());
//            jScrollPane1.setVisible(chRegistros.isSelected());
//            updateLayout();
//        } else {
//            System.out.println("vp.menuRegister es null");
//        }
    }//GEN-LAST:event_chRegistrosActionPerformed

    private void chReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chReportesActionPerformed
//        if (vp != null && vp.menuReports != null) {
//            vp.menuReports.setVisible(chReportes.isSelected());
//            jScrollPane1.setVisible(chReportes.isSelected());
//            updateLayout();
//        } else {
//            System.out.println("vp.menuReports es null");
//        }
    }//GEN-LAST:event_chReportesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdministracion;
    private javax.swing.JButton btnAdmision;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRegistros;
    private javax.swing.JButton btnReportes;
    private javax.swing.JButton btnSalir;
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
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable tbAdministracion;
    private javax.swing.JTable tbAdmision;
    private javax.swing.JTable tbRegistros;
    private javax.swing.JTable tbReportes;
    private javax.swing.JTextField txtIdTPU;
    // End of variables declaration//GEN-END:variables

    public void MostrarTipodeUsuarioCombo(JComboBox cbxTPU) {

        String sql = "";
        sql = "select * from tipodeusuario";
        Statement st;

        try {

            st = (Statement) con.getConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            cbxTPU.removeAllItems();

            while (rs.next()) {

                cbxTPU.addItem(rs.getString("tipoDeUsuario"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Mostrar Tabla " + e.toString());
        }
    }

    public void MostrarCodigoPorTPU(JComboBox cbxTPU, JTextField idTPU) {

        String consuta = "select tipodeusuario.idTipoDeUsuario from tipodeusuario where tipodeusuario.tipoDeUsuario=?";

        try {
            CallableStatement cs = con.getConexion().prepareCall(consuta);
            cs.setString(1, cbxTPU.getSelectedItem().toString());
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                idTPU.setText(rs.getString("idTipoDeUsuario"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar " + e.toString());
        }
    }

}
