package client.frm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteOrder;
import java.util.Queue;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import data.FriendUser;
import data.Message;
import data.Portrait;
import data.Record;
import tools.DateDeal;
import tools.FillWidth;


public class ChatPane extends JFrame implements ActionListener{

private JPanel friendinfoJPanel=new JPanel();//朋友信息
private JLabel photoLabel=new JLabel();//朋友头像
private JTextPane msgJTextPane=new JTextPane();//消息文本框
private JTextPane writetJTextPane=new JTextPane();//编辑文本框

private JButton sendButton=new JButton("发送");
private JButton closeButton=new JButton("关闭");
private JPanel buttonPanel=new JPanel();

private Color bgColor=new Color(169,213,244);

private ObjectOutputStream oos=null;//用于发送消息
private FriendUser friendUser=null;//对方
private FriendUser selfUser=null;//自己
//public Vector<Record>historylogQueue=null;

public ChatPane(ObjectOutputStream oos,FriendUser friendUser,FriendUser selfUser,boolean show) {
	//主面板传入输出流，对方和自己的账号信息
	this.friendUser=friendUser;
	this.selfUser=selfUser;
	this.oos=oos;
	setTitle("与"+friendUser.getNickName()+"聊天中");
	setSize(494,500);
   Toolkit tk=Toolkit.getDefaultToolkit();
   setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
	init();
   closeButton.addActionListener(this);
   sendButton.addActionListener(this);
   msgJTextPane.setContentType("text/html");
 
   
   setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   setVisible(show);
}
//初始化
private void init() {
		closeButton.setFocusPainted(false);
		closeButton.setPreferredSize(new Dimension(60,20));
		closeButton.setMargin(new Insets(0, 5, 0, 5));
		sendButton.setFocusPainted(false);
		sendButton.setPreferredSize(new Dimension(60,20));
		sendButton.setMargin(new Insets(0, 5, 0, 5));
		
		//好友头像
		photoLabel.setPreferredSize(new Dimension(20,20));
		photoLabel.setIcon(new Portrait(friendUser.getPhoto(),friendUser.getState(),false));
		JLabel infoJLabel=new JLabel(friendUser.getNickName()+"("+friendUser.getAcnum()+") "+friendUser.getSignature());
		infoJLabel.setPreferredSize(new Dimension(400,20));
		infoJLabel.setForeground(Color.BLUE);
		friendinfoJPanel.setOpaque(true);
		friendinfoJPanel.setBackground(new Color(205,237,255));
		friendinfoJPanel.setPreferredSize(new Dimension(484,25));
		friendinfoJPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,2));
		friendinfoJPanel.add(photoLabel);
		friendinfoJPanel.add(infoJLabel);
		
		
		buttonPanel.setPreferredSize(new Dimension(484,30));
		buttonPanel.setOpaque(true);
		buttonPanel.setBackground(bgColor);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,0,5));
		buttonPanel.add(closeButton);
		buttonPanel.add(new FillWidth(5, 20,bgColor));
		buttonPanel.add(sendButton);
		buttonPanel.add(new FillWidth(5, 20,bgColor));
		
		//带滚动条的文本消息框
		msgJTextPane.setEditable(false);
		JScrollPane msgJScrollPane=new JScrollPane(msgJTextPane);
		msgJScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		msgJScrollPane.setBorder(new EmptyBorder(0,0,0,0));
		
		//带滚动条的文本输入框
		JScrollPane  writeJScrollPane=new JScrollPane(writetJTextPane);
		writeJScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		writeJScrollPane.setBorder(new EmptyBorder(0,0,0,0));
		
		//聊天消息面板
		JPanel msgJPanel=new JPanel();
		msgJPanel.setLayout(new BorderLayout());
		msgJPanel.add(friendinfoJPanel,BorderLayout.NORTH);
		msgJPanel.add(msgJScrollPane,BorderLayout.CENTER);
		
		//输入面板
		JPanel wrtJPanel =new JPanel();
		wrtJPanel.setPreferredSize(new Dimension(484,96));
		wrtJPanel.setLayout(new BorderLayout());
		wrtJPanel.add(new FillWidth(484,25,bgColor),BorderLayout.NORTH);
		wrtJPanel.add(writeJScrollPane,BorderLayout.CENTER);
		
		//中间面板：输入面板和消息面板
		JPanel centerJPanel=new JPanel();
		centerJPanel.setLayout(new BorderLayout());
		centerJPanel.setBorder(new LineBorder(new Color(118,171,211)));
		centerJPanel.setOpaque(true);
		centerJPanel.setBackground(bgColor);
		centerJPanel.add(msgJPanel,BorderLayout.CENTER);
		centerJPanel.add(wrtJPanel,BorderLayout.SOUTH);
		
		//总面板
		JPanel parentJPanel=new JPanel();
		parentJPanel.setLayout(new BorderLayout());
		parentJPanel.setOpaque(true);
		parentJPanel.setBackground(bgColor);
		parentJPanel.add(centerJPanel,BorderLayout.CENTER);
		parentJPanel.add(buttonPanel,BorderLayout.SOUTH);
		
		//整个聊天面板
		setLayout(new BorderLayout());
		add(friendinfoJPanel,BorderLayout.NORTH);
		add(parentJPanel,BorderLayout.CENTER);
		add(new FillWidth(494,5,bgColor),BorderLayout.SOUTH);
}
//关闭、发送按钮
@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	if(e.getSource()==closeButton) {
		this.dispose();
		return;
} 
	if(e.getSource()==sendButton) {
		String content=writetJTextPane.getText();//输入框内容
		if(content.equals(""))return;
		//发送到服务器
		Message message=new Message();
		Record record=new Record();
		record.setFromid(selfUser.getAcnum());
		record.setToid(friendUser.getAcnum());
		record.setId(1);
		record.setContent(content);
		showRecord(selfUser.getNickName(),record,Color.GREEN);
		message.settype(30);
		message.setobject(record);
		new WriteThread(message).start();
		writetJTextPane.setText("");
		return;
	}
	
}
//更新好友的图标
public void updateFriendPhoto(Icon icon){
	photoLabel.setIcon(icon);
}
//聊天面板显示用户昵称和聊天消息
public void showRecord(String nickName, Record record, Color color) {
	// TODO Auto-generated method stub
	insertString(nickName+" "+DateDeal.getDate2(record.getSendTime()),color);
	insertString(record.getContent(), null);
} 
private void insertString(String msg, Color color) {
	// TODO Auto-generated method stub
	StyledDocument document=msgJTextPane.getStyledDocument();
	SimpleAttributeSet set=new SimpleAttributeSet();
	if(color!=null)StyleConstants.setForeground(set, color);
	try {
		document.insertString(document.getLength(), msg+"\n", set);
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println("ChatPane.insertString()错误"+e);
	}
}
//向服务器发送聊天消息的线程
private class WriteThread extends Thread{
	private Message message=null;
	public WriteThread(Message message) {
	   this.message=message;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(oos!=null) {
			try {
				oos.writeObject(message);
				oos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "和服务端连接发生错误:"+e.getMessage()+",请重新登录!");
				System.exit(0);
			}
			
		}
	}
	
}
}
