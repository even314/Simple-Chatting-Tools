
package tools;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

//Ìî³äBorderLayoutµÄ¿í¶È

public class FillWidth extends JPanel {

	public FillWidth(int width,int height) {
		setPreferredSize(new Dimension(width,height));
	}

	public FillWidth(int width,int height, Color color) {
		// TODO Auto-generated constructor stub
		setPreferredSize(new Dimension(width,height));
		setBackground(color);
		
	}
}
