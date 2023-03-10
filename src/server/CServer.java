package server;

import java.awt.Font;
import java.io.BufferedInputStream;

import dao.UserDAObyMySQL;
import server.frm.Server;
import tools.SetFont;

public class CServer {
	
	public static void main(String[] args) {
		try {
			//加载字体
			Font font = Font.createFont(Font.TRUETYPE_FONT,new BufferedInputStream(CServer.class.getResourceAsStream("/tools/simsun.ttc")));
			font = font.deriveFont(Font.PLAIN, 12);
			SetFont.setFont(font);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("字体加载错误:"+e);
		}
		try {
			UserDAObyMySQL.getconn();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("connsql failed "+e);
		}
		new Server();
		}
}
