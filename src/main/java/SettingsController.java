package main.java;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.w3c.dom.ls.LSOutput;

import java.util.prefs.Preferences;

public class SettingsController {
    @FXML
    private CheckBox capitalizedCB;
    @FXML
    private CheckBox exceptFirstCB;
    @FXML
    private TextField delimiterTF;

    private boolean capitalized = true;
    private boolean exceptFirst = true;
    private CharSequence delimiter = "";

    private Preferences prefs;

    public void init() {
        prefs = Preferences.userNodeForPackage(SettingsController.class);
        capitalized = prefs.getBoolean("capitalized", true);
        exceptFirst = prefs.getBoolean("exceptFirst", true);
        delimiter = prefs.get("delimiter","");

        capitalizedCB.setSelected(capitalized);
        exceptFirstCB.setSelected(exceptFirst);
        delimiterTF.setText(delimiter.toString());
    }

    public boolean getCapitalized() {
        return capitalized;
    }

    public boolean getExceptFirst() {
        return exceptFirst;
    }

    public CharSequence getDelimiter() {
        return delimiter;
    }

    public void setCapitalized() {
        capitalized = capitalizedCB.isSelected();
        prefs.putBoolean("capitalized", capitalized);
    }

    public void setExceptFirst() {
        exceptFirst = exceptFirstCB.isSelected();
        prefs.putBoolean("exceptFirst", exceptFirst);
    }

    public void setDelimiter() {
        delimiter = delimiterTF.getText();
        prefs.put("delimiter", delimiter.toString());
    }
}
