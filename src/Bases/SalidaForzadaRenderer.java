package Bases;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class SalidaForzadaRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Obtener el valor de la columna "Forzado" (la última columna)
        Boolean esForzado = (Boolean) table.getModel().getValueAt(row, table.getColumnCount() - 1);

        // Si es forzado, cambiar el color de la fila
        if (esForzado) {
            cell.setBackground(java.awt.Color.RED);  // Color para salidas forzadas
        } else {
            cell.setBackground(java.awt.Color.WHITE);  // Color normal
        }

        return cell;
    }
}
