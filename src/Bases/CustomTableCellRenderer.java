package Bases;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    private static final int STOCK_COLUMN_INDEX = 2; // Índice de la columna del stock
    private static final int STOCK_MINIMO_COLUMN_INDEX = 4; // Índice de la columna del stock mínimo

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (STOCK_COLUMN_INDEX < table.getColumnCount() && STOCK_MINIMO_COLUMN_INDEX < table.getColumnCount()) {
            try {
                Object stockValue = table.getValueAt(row, STOCK_COLUMN_INDEX);
                Object stockMinimoValue = table.getValueAt(row, STOCK_MINIMO_COLUMN_INDEX);

                if (stockValue != null && stockMinimoValue != null) {
                    int stock = Integer.parseInt(stockValue.toString());
                    int stockMinimo = Integer.parseInt(stockMinimoValue.toString());

                    if (stock <= stockMinimo) {
                        c.setBackground(Color.RED);
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            } catch (NumberFormatException e) {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        } else {
            c.setBackground(Color.WHITE);
            c.setForeground(Color.BLACK);
        }

        return c;
    }
}