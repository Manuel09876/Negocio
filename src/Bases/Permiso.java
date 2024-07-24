
package Bases;

import javax.swing.JCheckBox;
import javax.swing.JTextField;


public class Permiso {
    private JCheckBox checkBox;
    private JTextField[] textFields;

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
    
}
