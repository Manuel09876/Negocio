
package Bases;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class Tables extends DefaultTableCellRenderer{
    

    
   @Override
    public Component getTableCellRendererComponent(
            JTable jtable, Object o,boolean bln, boolean bln1, int row, int col) {
        super.getTableCellRendererComponent(jtable, o, bln, bln1, row, col);
        if (jtable.getValueAt(row, col).toString().equals("Inactivo")) {
            setBackground(Color.red);
            setForeground(Color.white);
        } else {
            setBackground(Color.white);
            setForeground(Color.BLACK);
        }
        return this;
    }
    
}
