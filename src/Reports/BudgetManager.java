package Reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import conectar.Conectar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class BudgetManager extends javax.swing.JInternalFrame {

    Conectar con = new Conectar();
    Connection connect = con.getConexion();

    private JTable budgetTable;
    private DefaultTableModel budgetTableModel;
    private JTextField nameField, amountField;
    private JComboBox<String> typeComboBox;
    private JButton addButton, editButton, deleteButton, exportButton;

    public BudgetManager() {
        initComponents();

      setTitle("Gestión de Presupuestos");
        setSize(800, 600);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        setLayout(new BorderLayout());

        // Panel de entrada de datos
        JPanel inputPanel = new JPanel(new GridLayout(2, 5));
        inputPanel.add(new JLabel("Nombre:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Monto:"));
        amountField = new JTextField();
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Tipo:"));
        typeComboBox = new JComboBox<>(new String[]{"Ingreso", "Egreso"});
        inputPanel.add(typeComboBox);
        addButton = new JButton("Agregar");
        inputPanel.add(addButton);
        editButton = new JButton("Editar");
        inputPanel.add(editButton);
        deleteButton = new JButton("Eliminar");
        inputPanel.add(deleteButton);
        exportButton = new JButton("Exportar a PDF");
        inputPanel.add(exportButton);
        add(inputPanel, BorderLayout.NORTH);

        // Tabla de presupuestos
        budgetTableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Monto", "Tipo"}, 0);
        budgetTable = new JTable(budgetTableModel);
        add(new JScrollPane(budgetTable), BorderLayout.CENTER);

        // Eventos de los botones
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBudget();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editBudget();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBudget();
            }
        });
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToPDF();
            }
        });

        
        // Inicializar la tabla de presupuestos
        refreshBudgetTable();
    }

    private void addBudget() {
        String name = nameField.getText();
        String amount = amountField.getText();
        String type = (String) typeComboBox.getSelectedItem();

        try (PreparedStatement stmt = connect.prepareStatement("INSERT INTO budgets (name, amount, type) VALUES (?, ?, ?)")) {

            stmt.setString(1, name);
            stmt.setBigDecimal(2, new BigDecimal(amount));
            stmt.setString(3, type);
            stmt.executeUpdate();

            refreshBudgetTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editBudget() {
        int selectedRow = budgetTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un presupuesto para editar.");
            return;
        }

        int id = (int) budgetTableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String amount = amountField.getText();
        String type = (String) typeComboBox.getSelectedItem();

        try (PreparedStatement stmt = connect.prepareStatement("UPDATE budgets SET name = ?, amount = ?, type = ? WHERE id = ?")) {

            stmt.setString(1, name);
            stmt.setBigDecimal(2, new BigDecimal(amount));
            stmt.setString(3, type);
            stmt.setInt(4, id);
            stmt.executeUpdate();

            refreshBudgetTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteBudget() {
        int selectedRow = budgetTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un presupuesto para eliminar.");
            return;
        }

        int id = (int) budgetTableModel.getValueAt(selectedRow, 0);

        try (PreparedStatement stmt = connect.prepareStatement("DELETE FROM budgets WHERE id = ?")) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            refreshBudgetTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshBudgetTable() {
        try (Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM budgets")) {

            budgetTableModel.setRowCount(0); // Limpiar la tabla
            while (rs.next()) {
                budgetTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("amount"),
                        rs.getString("type")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exportToPDF() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("presupuestos.pdf"));
            document.open();

            PdfPTable table = new PdfPTable(4);
            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Monto");
            table.addCell("Tipo");

            for (int i = 0; i < budgetTableModel.getRowCount(); i++) {
                table.addCell(budgetTableModel.getValueAt(i, 0).toString());
                table.addCell(budgetTableModel.getValueAt(i, 1).toString());
                table.addCell(budgetTableModel.getValueAt(i, 2).toString());
                table.addCell(budgetTableModel.getValueAt(i, 3).toString());
            }

            document.add(table);
            document.close();

            JOptionPane.showMessageDialog(this, "Presupuestos exportados a PDF con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 572, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
