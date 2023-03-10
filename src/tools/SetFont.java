
package tools;


import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;

/**
 * 设置字体类。 （来自网络资源）
 */
public final class SetFont
{
	/**
	 * 使用UIManager设置某些组件的缺省字体。
	 * @param font 字体。
	 */
    public static void setFont(Font font)
    {
        UIManager.put("Label.font", font);
        UIManager.put("Label.foreground", Color.black);
        UIManager.put("Button.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("CheckBoxMenuItem.font", font);
        UIManager.put("RadioMenuItem.font", font);
        UIManager.put("List.font", font);
        UIManager.put("CheckBox.font", font);
        UIManager.put("RadioButton.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font);
        UIManager.put("TabbedPane.font", font);
        UIManager.put("Pane.font", font);
        UIManager.put("Frame.font", font);
        UIManager.put("EditorPane.font", font);
        UIManager.put("OptionPane.font", font);
        UIManager.put("TextPane.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("PasswordField.font", font);
        UIManager.put("Dialog.font", font);
        UIManager.put("Toolbar.font", font);
        UIManager.put("ToolTip.font", font);
        UIManager.put("Slider.font", font);
        UIManager.put("Progress.font", font);
        UIManager.put("FileChooser.font", font);
        UIManager.put("ColorChooser.font", font);
        UIManager.put("TitledBorder.font", font);
    }
}
