
package Bases;

import javax.swing.JCheckBox;
import javax.swing.JTextField;


public class Permiso {
    private JCheckBox checkBox;
    private JTextField[] textFields;
    private String menuName;
    private String submenuName;

    public Permiso(JCheckBox checkBox, JTextField... textFields) {
        this.checkBox = checkBox;
        this.textFields = textFields;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public JTextField[] getTextFields() {
        return textFields;
    }
    
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getSubmenuName() {
        return submenuName;
    }

    public void setSubmenuName(String submenuName) {
        this.submenuName = submenuName;
    }
}
