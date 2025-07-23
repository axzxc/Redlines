import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    public static void applyDarkTheme() {
        UIManager.put("Panel.background", Constants.mainBG);
        UIManager.put("ScrollPane.background", Constants.mainBG);
        UIManager.put("Viewport.background", Constants.mainBG);
        UIManager.put("OptionPane.background", Constants.mainBG);
        //UIManager.put("ToolTip.background", Constants.annoBG);
        //UIManager.put("ToolTip.foreground", Constants.textColorDM);

        UIManager.put("Label.foreground", Constants.textColorDM);
        UIManager.put("Button.background", Constants.buttonColor);
        UIManager.put("Button.foreground", Constants.textColorDM);
        UIManager.put("CheckBox.foreground", Constants.textColorDM);
        UIManager.put("RadioButton.foreground", Constants.textColorDM);
        UIManager.put("ToggleButton.foreground", Constants.textColorDM);
        UIManager.put("ComboBox.foreground", Constants.textColorDM);
        UIManager.put("ComboBox.background", Constants.mainFG);

        UIManager.put("TextField.foreground", Constants.textColorDM);
        UIManager.put("TextField.background", Constants.mainFG);
        UIManager.put("PasswordField.foreground", Constants.textColorDM);
        UIManager.put("PasswordField.background", Constants.mainFG);
        UIManager.put("TextArea.foreground", Constants.textColorDM);
        UIManager.put("TextArea.background", Constants.mainFG);
        UIManager.put("TextPane.foreground", Constants.textColorDM);
        UIManager.put("TextPane.background", Constants.mainFG);
        UIManager.put("EditorPane.foreground", Constants.textColorDM);
        UIManager.put("EditorPane.background", Constants.mainFG);
        UIManager.put("OptionPane.messageForeground", Constants.textColorDM);
        UIManager.put("List.foreground", Constants.textColorDM);
        UIManager.put("List.background", Constants.mainFG);
        UIManager.put("Table.foreground", Constants.textColorDM);
        UIManager.put("Table.background", Constants.mainFG);
        UIManager.put("Tree.foreground", Constants.textColorDM);
        UIManager.put("Tree.background", Constants.mainFG);
        UIManager.put("CheckBox.foreground", Constants.textColorDM);
        UIManager.put("CheckBox.background", Constants.mainBG);
        UIManager.put("RadioBuutton.background", Constants.mainBG);
    }
}
