package client.frm;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.PrimitiveIterator.OfDouble;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import data.Asking;
import data.FriendUser;
import data.LoginUser;
import data.Message;
import data.Portrait;
import data.Record;
import tools.FillWidth;

//主功能界面
public class MainPane extends JFrame implements ActionListener{
private JLabel photoJLabel = new JLabel();//头像
private JLabel nicknameJLabel=new JLabel();//昵称
private JTextArea signitureTextArea=new JTextArea();//个性签名

private JList friendJList=null;
private DefaultListModel listModel=null;

private JButton quitButton=new JButton("退出");
private JButton findButton=new JButton("查找");

private JButton btnCancleLogin = new JButton("取消登录");
private JPanel fillWidth = new FillWidth(118, 120, Color.WHITE);

public Socket client=null;
private ObjectOutputStream oos=null;
private ObjectInputStream ois=null;

private HashMap<Integer, ChatPane>chatHashMap=null;//聊天窗口用hashmap装
private  FriendUser selfUser=null;//用户自己（的信息）

private String serverIP;
private Integer serverPort;
private Integer acnum;
private String password;
private Integer state;

private FindWindow findWindow=null;

private Thread thread=null;

	public MainPane(String  serverIP, int serverPort, Integer acnum, String password, int state) {
	// TODO Auto-generated constructor stub
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.acnum = acnum;
		this.password = password;
		this.state = state;
		
		setTitle("Chatting");
		setSize(200, 550);
		setResizable(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
		setLocation((tk.getScreenSize().width - getSize().width) - 10,
				(tk.getScreenSize().height - getSize().height) / 2 - 30);
		
		getContentPane().setBackground(Color.WHITE);
		initLoginPane();

		setVisible(true);		
		thread = new LoginThread();//登录线程
		thread.start();
}
	//登录等待缓冲界面
	private void initLoginPane() {
		// TODO Auto-generated method stub
		btnCancleLogin.setPreferredSize(new Dimension(60, 20));
		btnCancleLogin.setMargin(new Insets(0, 0, 0, 0));
		btnCancleLogin.setFocusPainted(false);
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		add(fillWidth);
		add(btnCancleLogin);
		
		btnCancleLogin.addActionListener(this);
	}
	
	//主页面
	private void initMain(Vector<FriendUser> v) {
		selfUser = v.get(0);
		v.remove(0);
		setTitle("Chatting " + selfUser.getAcnum());
		
		//按钮
		quitButton.setMargin(new Insets(0, 5, 0, 5));
		findButton.setMargin(new Insets(0, 5, 0, 5));
	   quitButton.addActionListener(this);
	   findButton.addActionListener(this);
	    //按钮面板（底部）
			JPanel buttonJPanel=new JPanel();
			buttonJPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
			buttonJPanel.add(findButton);
			buttonJPanel.add(quitButton);
		
	   //头像
	   photoJLabel.setSize(50,50);
	   photoJLabel.setLocation(5,5);
	   photoJLabel.setOpaque(true);
	   photoJLabel.setBackground(new Color(116, 220, 253, 150));
	   photoJLabel.setHorizontalAlignment(SwingConstants.CENTER);
	   photoJLabel.setIcon(new Portrait(selfUser.getPhoto(), selfUser.getState()));
	   photoJLabel.setBorder(new LineBorder(new Color(60, 168, 206), 1, true));
	   
	   //昵称
	   nicknameJLabel.setSize(130,20);
	   nicknameJLabel.setLocation(60,5);
	   nicknameJLabel.setText(selfUser.getNickName() );
	   
	   //个性签名
	   signitureTextArea.setText(selfUser.getSignature());
	   signitureTextArea.setEditable(false);
	   signitureTextArea.setLineWrap(true);
	   signitureTextArea.setBackground(getBackground());
	   JScrollPane signJScrollPane=new JScrollPane(signitureTextArea);
	   signJScrollPane.setSize(125, 25);
		signJScrollPane.setLocation(60, 30);
		signJScrollPane.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		
		//顶部面板
		JPanel topJPanel = new JPanel();
		topJPanel .setLayout(null);
		topJPanel .setPreferredSize(new Dimension(200, 60));
		topJPanel .add(photoJLabel);
		topJPanel .add(nicknameJLabel);
		topJPanel .add(signJScrollPane);
		
		//好友列表
		listModel=new DefaultListModel<>();
		for(FriendUser fu:v) {
			if(fu!=null) {
				System.out.println(fu.getAcnum()+":"+fu.getNickName());
			listModel.addElement(fu);
			}
		}
		friendJList=new JList<>();
		friendJList.setCellRenderer(new CompanyLogoListCellRenderer());
		friendJList.setModel(listModel);
		friendJList.setFixedCellHeight(50);
		friendJList.addMouseListener(new ListMouseAdapter());
		friendJList.addMouseMotionListener(new ListMouseAdapter());//鼠标移动监听
		JScrollPane frdJScrollPane=new JScrollPane(friendJList);//滚动条
		
		//聊天窗口hashmap初始化
		chatHashMap=new HashMap<>();
		for (int i = 0; i < v.size(); i++)
			chatHashMap.put(v.get(i).getAcnum(), null);
		setVisible(false);
		btnCancleLogin.removeActionListener(this);
		remove(fillWidth);
		remove(btnCancleLogin);
		//validate();
		
		setLayout(new BorderLayout());
	
		add(topJPanel,BorderLayout.NORTH);
		add(frdJScrollPane,BorderLayout.CENTER);
		add(new FillWidth(5, 5), BorderLayout.EAST);
		add(new FillWidth(5, 5), BorderLayout.WEST);
		add(buttonJPanel, BorderLayout.SOUTH);
		setVisible(true);
		
		MainPane.this.addWindowListener(new MyWindowAdapter());
		findWindow=new FindWindow(this, true);
		//findWindow.setVisible(false);
	}
	
	private FriendUser getFriendUser(Integer acnum) {
	// TODO Auto-generated method stub
		for(int i=0;i<listModel.getSize();i++) {
			FriendUser user=(FriendUser)listModel.getElementAt(i);
			if(user.getAcnum().equals(acnum))return user;
		}
	return null;
}
	//打开好友聊天窗口
	private void chatWithFriend() {
		// TODO Auto-generated method stub
		Object object=friendJList.getSelectedValue();
		if(object instanceof FriendUser) {
			FriendUser friendUser=(FriendUser)object;
			int acnum=friendUser.getAcnum();
			ChatPane chatPane=chatHashMap.get(acnum);
			if(chatPane==null) {
				chatPane=new ChatPane(oos, friendUser, selfUser, true);
				chatHashMap.put(acnum, chatPane);
			}else {
				chatPane.setVisible(true);
			}			
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnCancleLogin) {
			closeClient();
			System.exit(0);
			return;
		}
		if(e.getSource()==quitButton) {
			quit();
			closeClient();
			System.exit(0);
		}
		if(e.getSource()==findButton) {
			if (findWindow == null)
				findWindow = new FindWindow(this, false);
			else {
				findWindow.setVisible(true);//查找好友窗口可见
			}
			return;			
		}
	}
	
	private void quit() {
	closeClient();
		thread.interrupt();//中断进程
		System.exit(0);
	}
	private void closeClient() {
		// TODO Auto-generated method stub
		try {
			if (oos != null)
				oos.close();
			oos = null;
			if (ois != null)
				ois.close();
			ois = null;
			if (client != null)
				client.close();
			client = null;
		} catch (IOException e) {
	    System.out.println("MainPane.WriteThread.closeClient()");
	      e.printStackTrace();
		}
	}
	
	//关闭窗体时，向服务器发送离线状态
	private class MyWindowAdapter extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			quit();
		}	
	}
	
	//登录线程，同时处理服务器发来的消息
private class LoginThread extends Thread{
	public LoginThread(){
	try {
		client=new Socket(serverIP,serverPort);
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println("MainPane.LoginThread.LoginThread()"+e);
	}

	MainPane.this.addWindowListener(new MyWindowAdapter());

	//发送到服务器
	try {
		oos=new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
		LoginUser user=new LoginUser(acnum,password);
		user.setState(1);
		Message message=new Message(20,user);
		//System.out.println("MainPane.LoginThread.LoginThread()298");
		new WriteThread(message).start();
		ois=new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		closeClient();
		JOptionPane.showMessageDialog(null,
				"请确保输入的服务器IP和端口正确!" + e.getMessage());
		dispose();
		new LoginPane();
		System.out.println("MainPane.LoginThread.LoginThread()");
		e.printStackTrace();
	}	
	}
	//线程接受服务器消息
@Override
public void run() {
	// TODO Auto-generated method stub
	try {
	//	System.out.println("MainPane.LoginThread.run()345");
		Message message=null;
		while(ois!=null) {
			Object object=ois.readObject();
			if(object instanceof Message) {
			//	System.out.println("MainPane.LoginThread.run()350");
				message=(Message)object;
				int type=message.gettype();
				System.out.println("type="+type);
				switch (type) {
				case 21://登录成功，加载好友列表
					//System.out.println("MainPane.LoginThread.run()登陆成功");
					if(message.getobject() instanceof Vector) {
					Vector<FriendUser> friendlist=(Vector)message.getobject();
					//System.out.println("MainPane.LoginThread.run()362getvec"+friendlist.size());
					initMain(friendlist);
					}					
					break;
				case 22://登陆失败
					closeClient();
					JOptionPane.showMessageDialog(null, message.getobject().toString());
					dispose();
					new LoginPane();
					break;
				case 23://账号在别处登录
					causeLetClientQuit(message);
					break;
				case 31:// 接收到消息
				//	System.out.println("MainPane.LoginThread.run() 31 381");
					dealRecord(message.getobject(), type);
					break;
				case 90:// 服务端退出
					causeLetClientQuit(message);
					break;
				case 25:// 好友状态改变，好友对象由控制台传,头像会改变颜色,list要重新渲染
					dealFriendUserLogin(message);
					break;
				case 41:// 接到好友申请
					dealAsking(message);
					break;
				case 46:case 48://添加好友失败
					System.out.println("MainPane.LoginThread.run()");
					if(message.getobject() instanceof String)
						JOptionPane.showMessageDialog(null,(String)message.getobject());
					break;
				case 49://添加好友成功
					RefreshFriendlist(message);
					break;
				}
			}
		}
	} catch (Exception e) {
		// TODO: handle exception
		closeClient();
		JOptionPane.showMessageDialog(null,
				"和服务端连接发生错误:" + e.getMessage() + ",请重新登录!");
		e.printStackTrace();
		System.exit(0);
	}
}

}
private void dealRecord(Object object, int type) {
	// TODO Auto-generated method stub
	//System.out.println("MainPane.dealRecord()413");
	if(object instanceof Record) {
		Record record=(Record) object;
		FriendUser friendUser=getFriendUser(record.getFromid());
		if(friendUser!=null) {
		ChatPane chatPane=chatHashMap.get(record.getFromid());
		if(chatPane==null) {
			chatPane=new ChatPane(oos, friendUser, selfUser, true);
			chatHashMap.put(record.getFromid(), chatPane);
		}
		else if (chatPane.isDisplayable()) {
			chatPane.setFocusable(true);
		}else {
			chatPane.setVisible(true);
		}
		chatPane.showRecord(friendUser.getNickName(), record, 
				Color.BLUE);
		//chatPane.historylogQueue.add(record);
		}
	}
}
public void RefreshFriendlist(Message message) {
	// TODO Auto-generated method stub
	Object object=message.getobject();
	if(object instanceof FriendUser) {
		FriendUser friend=(FriendUser)object;
		if(getFriendUser(friend.getAcnum())!=null)return;
		listModel.addElement(friend);
		friendJList.repaint();
	}
}
//接收好友请求消息
private void dealAsking(Message message) {
	// TODO Auto-generated method stub
	Object object=message.getobject();
	if(object instanceof Asking) {
		Asking asking=(Asking) object;
		asking.setOos(oos);
		asking.setVisible(true);
	}
}
//好友上线，重新渲染
public void dealFriendUserLogin(Message message) {
	// TODO Auto-generated method stub
	if(message.getobject() instanceof FriendUser) {
		FriendUser friendUser=(FriendUser)message.getobject();
		if(listModel.contains(friendUser)) {
			//找到Listmodel中的frienduser
			for(int i=0;i<listModel.getSize();i++) {
			try {
				FriendUser friendinlist=(FriendUser)listModel.get(i);
				if(friendinlist.equals(friendUser)) {
					friendinlist.setState(friendUser.getState());
					friendinlist.setPhoto(friendUser.getPhoto());
					friendinlist.setNickName(friendUser.getNickName());
					friendinlist.setSignature(friendUser.getSignature());
					friendJList.repaint();//重新绘制jlist
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.print("MainPane.dealFriendUserLogin()");
				e.printStackTrace();
				return;
			} 
			}
		}
	}
}
//服务端退出
private void causeLetClientQuit(Message message){
	closeClient();
	JOptionPane.showMessageDialog(null, message.getobject().toString());
	System.exit(0);
}

private class WriteThread extends Thread{
	Message message;
	public WriteThread(Message message) {
		this.message=message;
	//	System.out.println("MainPane.WriteThread.WriteThread()472");
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
			try {
				if(oos!=null)
				{
//				System.out.println("MainPane.WriteThread.run()479");	
				oos.writeObject(message);
				oos.flush();				
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("MainPane.WriteThread.run()");
				e.printStackTrace();
				//无法登录，需要关闭socket和流(closeclient完成)
				closeClient();
				JOptionPane.showMessageDialog(null,
						"和服务端连接发生错误:" + e.getMessage() + ",请重新登录!");
				System.exit(0);
			}
	}

	
}

private class FindWindow extends JDialog implements ActionListener {
	private JButton okButton=new JButton("确定");
	private JButton quitButton=new JButton("退出");
	private JLabel searchJLabel=new JLabel("请输入对方账号");
	private  JTextArea searchJTextArea=new JTextArea();	
	
	
	public FindWindow(MainPane owner, boolean modal) {
		// TODO Auto-generated constructor stub
		//母窗口，是否为模态
		super(owner,modal);
		setTitle("查找好友");
		setSize(400, 200);
		setResizable(false);
		Toolkit tk = Toolkit.getDefaultToolkit();
		setLocation((tk.getScreenSize().width - getSize().width) / 2,
				(tk.getScreenSize().height - getSize().height) / 2);
		init();
		okButton.addActionListener(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(false);
	}

	private void init() {
		// TODO Auto-generated method stub
		okButton.setPreferredSize(new Dimension(100,20));
		quitButton.setPreferredSize(new Dimension(100,20));
		searchJLabel.setPreferredSize(new Dimension(100,20));
		searchJTextArea.setPreferredSize(new Dimension(250,20));
		setLayout(new BorderLayout());
		JPanel centerJPanel=new JPanel();
		centerJPanel.setPreferredSize(new Dimension(400,20));
		//centerJPanel.setLayout(new FlowLayout());
		centerJPanel.add(searchJLabel);
		centerJPanel.add(searchJTextArea);
		JPanel btnJPanel=new JPanel(new FlowLayout());
		btnJPanel.setPreferredSize(new Dimension(400,50));
	btnJPanel.add(okButton);
	btnJPanel.add(quitButton);
	//	add(new FillWidth(400, 10),BorderLayout.SOUTH);
	add(new FillWidth(400, 50),BorderLayout.NORTH);
	add(centerJPanel,BorderLayout.CENTER);
	add(btnJPanel,BorderLayout.SOUTH);
		quitButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==okButton) {
		if(	searchJTextArea.getText().toString().equals(""))return;
			try {			
				Integer friacnum=Integer.valueOf(searchJTextArea.getText());
				Message message=new Message(40,friacnum);
				new WriteThread(message).start();
			   searchJTextArea.setText("");
			} catch (Exception e2) {
				// TODO: handle exception
				//e2.printStackTrace();
				System.out.println(searchJTextArea.getText());
				return;
			}					
		}
		if(e.getSource()==quitButton) {
			searchJTextArea.setText("");
			setVisible(false);
		}
	}
	
}

//鼠标事件，双击进入，adapter改写
private class ListMouseAdapter extends MouseAdapter{
	public void mouseMoved(MouseEvent e) {//鼠标移动到列表，该位置item为选中状态
		if (e.getSource() == friendJList) {
			friendJList.clearSelection();
			int index = friendJList.locationToIndex(e.getPoint());//获得鼠标光标坐标所在的item位置
			friendJList.setSelectedIndex(index);//更新选中状态
		}
	}
	public void  mouseClicked(MouseEvent e) {//双击与朋友聊天
		if (e.getSource() == friendJList) {
			if (e.getClickCount() == 2) {
				chatWithFriend();
			}
	}
}
}

/**
 * 自己定制的好友类表(来自网络资源)
 */
private class CompanyLogoListCellRenderer extends DefaultListCellRenderer {
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Component retValue = super.getListCellRendererComponent(list,
				value, index, isSelected, cellHasFocus);
		if (value instanceof FriendUser) {
			FriendUser user = (FriendUser) value;
			setIcon(new Portrait(user.getPhoto(), user.getState()));
			setText("<html>" + user.getNickName() + "["
					+ user.getAcnum() + "]" + "<br><font color='red'>"
					+ user.getSignature() + "</font></html>");		
		}
		return retValue;
	}
}
}
