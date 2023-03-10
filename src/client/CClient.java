package client;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.IOException;

import client.frm.LoginPane;
import tools.SetFont;

//¼ÓÔØ×ÖÌå£¬Æô¶¯µÇÂ¼´°¿Ú
public class CClient {
public static void main(String[] args) {
	try {
		Font font = Font.createFont(Font.TRUETYPE_FONT, new BufferedInputStream(CClient.class.getResourceAsStream("/tools/simsun.ttc")));
		font = font.deriveFont(Font.PLAIN, 12);
		SetFont.setFont(font);
	} catch (FontFormatException e) {
		System.out.println("´íÎó:"+e.getMessage());
	} catch (IOException e) {
		System.out.println("´íÎó:"+e.getMessage());
	}
	new LoginPane();
}
}
