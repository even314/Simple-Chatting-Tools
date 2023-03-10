package server.frm;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import server.frm.pane.ServicePane;
import server.frm.pane.UserPane;
//服务器类
public class Server extends JFrame implements ChangeListener{
	private JTabbedPane panes=null;
	private ServicePane servicePane=new ServicePane();
	private UserPane userPane=new UserPane();
	
	//初始化界面
    public Server() {
    	setTitle("Chatting服务端");
    	setSize(600,520);
    	setResizable(false);
    	Toolkit tk=Toolkit.getDefaultToolkit();
    	setLocation((tk.getScreenSize().width-getSize().width)/2,
    			(tk.getScreenSize().height-getSize().height)/2);
    	panes=new JTabbedPane();
    	panes.add("系统服务",servicePane);
    	panes.add("用户管理",userPane);
    	add(panes);
    	panes.addChangeListener(this);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
    }
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if (panes.getSelectedComponent() == userPane)
			userPane.flushpane();
	}

}
