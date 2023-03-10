package client.frm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import data.DAOUser;
import data.Message;
import data.Portrait;
import data.RegUser;
import tools.GetParameter;
import tools.FillWidth;

public class RegisterPane extends JFrame implements ActionListener{
	//标签
	private JLabel nickNamelaLabel = new JLabel("用户昵称:");
	private JLabel emailLabel = new JLabel("E-mail:");
	private JLabel passwordLabel = new JLabel("登录密码:");
	private JLabel rePassLabel= new JLabel("重复输入:");
	private JLabel sexLabel = 	new JLabel("性    别:");
	private JLabel agelaLabel = new JLabel("年龄:");
	private JLabel signaturelaLabel = new JLabel("个性签名:");
	
	//输入框
	private JTextField nickNameField = new JTextField("");
	private JTextField emailField= new JTextField("");
	private JPasswordField passwordField = new JPasswordField("");
	private JPasswordField rePassField = new JPasswordField("");
	private JComboBox  sexBox = new JComboBox();
	private JTextField ageField= new JTextField("0");
	private JTextArea signatureArea = new JTextArea();
	
	//头像
	private JLabel photoJLabel = new JLabel();
	private JButton changeButton = new JButton("更改头像");
	
	//按钮
	private JButton okButton = new JButton("注册");
	private JButton cancleButton = new JButton("取消");
	
	private ChooseProtrait chooseProtrait = null;
	
	private Socket client=null;
	private ObjectOutputStream oos=null;
	private ObjectInputStream ois=null;
	

	public RegisterPane() {
		setTitle("新用户注册");
		setSize(330,343);
		setResizable(false);
		Toolkit tk=Toolkit.getDefaultToolkit();
		setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
		init();
		changeButton.addActionListener(this);
		cancleButton.addActionListener(this);
		okButton.addActionListener(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	//初始化面板
	private void init() {
		// TODO Auto-generated method stub
		setLayout(null);
		//标签大小格式
		nickNamelaLabel.setPreferredSize(new Dimension(60,20));
		emailLabel.setPreferredSize(new Dimension(60,20));
		passwordLabel.setPreferredSize(new Dimension(60,20));
		rePassLabel.setPreferredSize(new Dimension(60,20));
		sexLabel.setPreferredSize(new Dimension(60,20));
		agelaLabel.setPreferredSize(new Dimension(30,20));
		signaturelaLabel.setPreferredSize(new Dimension(60,20));
		
		nickNameField.setPreferredSize(new Dimension(120,20));
		emailField.setPreferredSize(new Dimension(120,20));
		passwordField.setPreferredSize(new Dimension(120,20));
		rePassField.setPreferredSize(new Dimension(120,20));
		sexBox.setPreferredSize(new Dimension(40,20));
		ageField.setPreferredSize(new Dimension(40,20));
		JScrollPane sp = new JScrollPane(signatureArea);
		sp.setPreferredSize(new Dimension(220,60));
		
		photoJLabel.setOpaque(true);
		photoJLabel.setBackground(Color.WHITE);
		photoJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		photoJLabel.setPreferredSize(new Dimension(50,50));
		photoJLabel.setBorder(new LineBorder(Color.DARK_GRAY));
		photoJLabel.setIcon(new Portrait());
		changeButton.setPreferredSize(new Dimension(60,20));
		changeButton.setMargin(new Insets(0,0,0,0));
		
		sexBox.addItem("男");
		sexBox.addItem("女");
		
		//必填框
		JPanel requrePanel = new JPanel();
		requrePanel.setBorder(new TitledBorder(new LineBorder(Color.GRAY),"必填选项"));
		requrePanel.setSize(210,135);
		requrePanel.setLocation(10, 10);
		requrePanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,6));
		requrePanel.add(nickNamelaLabel);
		requrePanel.add(nickNameField);
		requrePanel.add(emailLabel);
		requrePanel.add(emailField);
		requrePanel.add(passwordLabel);
		requrePanel.add(passwordField);
		requrePanel.add(rePassLabel);
		requrePanel.add(rePassField);
		
		//选填框
		JPanel unrequireJPanel = new JPanel();
		unrequireJPanel.setBorder(new TitledBorder(new LineBorder(Color.GRAY),"选填选项"));
		unrequireJPanel.setSize(305,125);
		unrequireJPanel.setLocation(10, 150);
		unrequireJPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,6));
		unrequireJPanel.add(sexLabel);
		unrequireJPanel.add(sexBox);
		unrequireJPanel.add(agelaLabel);
		unrequireJPanel.add(ageField);
		unrequireJPanel.add(new FillWidth(80,20));
		unrequireJPanel.add(signaturelaLabel);
		unrequireJPanel.add(sp);
		
		
		//头像选择区
		JPanel panePhoto = new JPanel();
		panePhoto.setBorder(new TitledBorder(new LineBorder(Color.GRAY),"头像"));
		panePhoto.setSize(85,135);
		panePhoto.setLocation(230, 10);
		panePhoto.setLayout(new FlowLayout(FlowLayout.CENTER,5,8));
		panePhoto.add(new FillWidth(50,4));
		panePhoto.add(photoJLabel);
		panePhoto.add(changeButton);
		
		//按钮选项区
		JPanel buttonPanel = new JPanel();
		buttonPanel.setSize(305,30);
		buttonPanel.setLocation(10, 275);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT,2,5));
		buttonPanel.add(new FillWidth(100,20));
		buttonPanel.add(okButton);
		buttonPanel.add(new FillWidth(8,20));
		buttonPanel.add(cancleButton);
		
		add(requrePanel);
		add(unrequireJPanel);
		add(panePhoto);
		add(buttonPanel);
	}
	
	//按钮事件 注册、取消
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==cancleButton) {
			dispose();
			new LoginPane();
		}
		if(e.getSource()==changeButton) {//换头像(来自网络资源)
			if(chooseProtrait==null)
				new ChooseProtrait();
			else
				chooseProtrait.setVisible(true);//显示换头像对话框			
		}
		if(e.getSource()==okButton) {
			if(nickNameField.getText().toString().equals("")||emailField.getText().toString().equals(""))return;
			//注册按钮 验证格式（两次密码）发送注册消息到服务器
			try {
				//两次密码是否一样
				String pwdString= new String(passwordField.getPassword());
				String repwdsString=new String(rePassField.getPassword());
				if(pwdString.equals("")||pwdString.equals(""))return;
				if(pwdString.equals(repwdsString)) 
				{	
					okButton.setEnabled(false);
					//System.out.println("RegisterPane.actionPerformed()207");
				new RegNewUser().start();
				}
				else {
					JOptionPane.showMessageDialog(null,"两次密码不一致！");
				}
			} catch (Exception e2) {
				// TODO: handle exception
				System.out.println("RegisterPane.actionPerformed()"+e2);
			}
		}
	}
	//注册新用户的线程 向服务器提交信息
	private class RegNewUser extends Thread {
		   public RegNewUser() throws IOException {			  
			try {
				client=new Socket("127.0.0.1",Integer.parseInt(new GetParameter().paramap.get("Port")));
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			oos=new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
			DAOUser user=new DAOUser(nickNameField.getText(),emailField.getText(),new String(passwordField.getPassword()) );
			user.setSex(sexBox.getSelectedItem().toString());
			try {
				user.setAge(Integer.parseInt(ageField.getText()));
			} catch (Exception e) {
				// TODO: handle exception
				user.setAge(0);
			}
			user.setSignature(signatureArea.getText());
			user.setPhoto(((Portrait)photoJLabel.getIcon()).getNum());
			Message message=new Message(10,user);
			//发给服务器
			oos.writeObject(message);
			oos.flush();
			//接收服务器响应
			ois=new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
		}
		   //接收服务器响应并处理的线程
		   @Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(ois!=null) {
					Object object=ois.readObject();
					if(object instanceof Message) {
						Message message=(Message)object;
						int type=message.gettype();
						switch (type) {
						case 11://注册成功
							//服务器返回reguser对象，有随机生成的账号和用户密码
							RegUser xuser=(RegUser)message.getobject();		
							new RegSuccess(xuser,RegisterPane.this,true);							
							closeClient();
							break;
						case 12:
							JOptionPane.showMessageDialog(null, "注册失败!请重新注册!");
							okButton.setEnabled(true);
							closeClient();
							break;
						case 90:
							JOptionPane.showMessageDialog(null, message.getobject().toString());
							closeClient();
							System.exit(0);
							break;
						}						
					}	break;				
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("RegisterPane.RegNewUser.run()"+e);
				JOptionPane.showMessageDialog(null, "发生错误,原因:"+e.getMessage());
				closeClient();
			}
		}
		private void closeClient() {
			// TODO Auto-generated method stub
			//关闭输入输出流
		try {
			if(oos!=null)oos.close();oos=null;
			if(ois!=null)ois.close();ois=null;
			if(client!=null)client.close();client=null;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("RegisterPane.RegNewUser.closeClient()"+e);
		}
		}
	}
	
	//注册成功窗口
	private class RegSuccess extends JDialog implements ActionListener{
		private JTextArea infoArea=new JTextArea();
		private JButton loginButton=new JButton("直接登录");
		private JButton returnButton=new JButton("返回登录界面");
		private RegUser user;
		
		//owner:父窗口，modal:是否模态
		public RegSuccess(RegUser user,Frame owner,boolean modal) {
		super(owner,modal);
		this.user=user;
		setSize(250,200);
		setResizable(false);
		Toolkit tk=Toolkit.getDefaultToolkit();
		setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
		setTitle("注册信息");
		
		infoArea.setText("恭喜!,注册成功！\n"+
				"用户昵称:"+user.getNickname()+"\n"+
				"登陆号码:"+user.getAcnum()+"\n"+
				"登陆密码:"+user.getPassword()+"\n"+
				"请妥善保管您的号码和密码!");
		infoArea.setEditable(false);
		infoArea.setOpaque(true);
		infoArea.setBackground(this.getBackground());
		infoArea.setPreferredSize(new Dimension(200,100));
		infoArea.setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY),"注册信息"));
		setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		
		loginButton.addActionListener(this);
		returnButton.addActionListener(this);
		add(infoArea);
		add(loginButton);
		add(returnButton);
		
		setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==loginButton) {
				dispose();
				RegisterPane.this.dispose();//关闭注册页 registerpane的this（对象）
			try {
				new MainPane("127.0.0.1",Integer.parseInt(new GetParameter().paramap.get("Port")),
						user.getAcnum(),user.getPassword(),1);
			} catch (Exception e2) {
				System.out.println("RegisterPane.RegSuccess.actionPerformed()"+e);
				// TODO: handle exception
			}
			}
			if(e.getSource()==returnButton) {
				dispose();
				RegisterPane.this.dispose();
				new LoginPane();
			}
		}
	}
	//头像选择窗口(来自网络资源)
	private class ChooseProtrait extends JDialog implements ActionListener{
		private JButton[] btnPortrait = new JButton[158];
		private Portrait[] portraits = new Portrait[158];
		
		private JLabel lblBoys = new JLabel("男士头像(共30个)");
		private JLabel lblGirls = new JLabel("女士头像(共29个)");
		private JLabel lblAnimals = new JLabel("动物头像(共36个)");
		private JLabel lblOthers = new JLabel("其他头像(共63个)");
		
		private JLabel lblViewInfo = new JLabel("预览:");
		private JLabel lblPhotoView = new JLabel();
		
		private JButton btnP_Ok = new JButton("确定");
		private JButton btnP_Cancle = new JButton("取消");
		
		public ChooseProtrait() {
			setTitle("选择头像");
			setSize(500,440);
			setResizable(false);
			Toolkit tk=Toolkit.getDefaultToolkit();
			setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
			
			btnP_Ok.setSize(80,20);
			btnP_Ok.setLocation(300, 375);
			btnP_Ok.addActionListener(this);
			btnP_Cancle.setSize(80,20);
			btnP_Cancle.setLocation(400, 375);
			btnP_Cancle.addActionListener(this);
			
			//初始化按钮，并将头像显示的按钮上
			for(int i=0;i<btnPortrait.length;i++){
				btnPortrait[i] = new JButton();
				btnPortrait[i].setMargin(new Insets(0,0,0,0));
				btnPortrait[i].setPreferredSize(new Dimension(50,50));
				btnPortrait[i].addActionListener(this);
				btnPortrait[i].setOpaque(true);
				btnPortrait[i].setBackground(Color.WHITE);
			}
			//初始化预览头像
			lblPhotoView.setOpaque(true);
			lblPhotoView.setBackground(Color.WHITE);
			lblPhotoView.setHorizontalAlignment(SwingConstants.CENTER);
			lblPhotoView.setPreferredSize(new Dimension(50,50));
			lblPhotoView.setBorder(new LineBorder(Color.DARK_GRAY));
			lblPhotoView.setIcon(photoJLabel.getIcon());
			lblPhotoView.setSize(50,50);
			lblPhotoView.setLocation(420, 40);
			lblViewInfo.setSize(60,20);
			lblViewInfo.setLocation(425, 10);
			
			initJLabel(lblBoys);
			initJLabel(lblGirls);
			initJLabel(lblAnimals);
			initJLabel(lblOthers);
			
			JPanel paneBoys = getPane(0, 30);
			JPanel paneGirls = getPane(30, 60);
			JPanel paneAnimals = getPane(60, 96);
			JPanel paneOthers = getPane(96, 158);
			
			JPanel panePortrait = new JPanel();
			panePortrait.setPreferredSize(new Dimension(380,1500));
			panePortrait.setOpaque(true);
			panePortrait.setBackground(Color.WHITE);
			panePortrait.add(lblBoys);
			panePortrait.add(paneBoys);
			panePortrait.add(lblGirls);
			panePortrait.add(paneGirls);
			panePortrait.add(lblAnimals);
			panePortrait.add(paneAnimals);
			panePortrait.add(lblOthers);
			panePortrait.add(paneOthers);
			
			JScrollPane sp = new JScrollPane(panePortrait);
			sp.setSize(400,350);
			sp.setLocation(10,5);
			setLayout(null);
			
			JPanel paneAll = new JPanel();
			paneAll.setSize(480,365);
			paneAll.setLocation(5, 0);
			paneAll.setOpaque(true);
			paneAll.setBackground(Color.WHITE);
			paneAll.setBorder(new LineBorder(Color.BLACK));
			paneAll.setLayout(null);
			paneAll.add(sp);
			paneAll.add(lblViewInfo);
			paneAll.add(lblPhotoView);
			
			add(paneAll);
			add(btnP_Ok);
			add(btnP_Cancle);
			
			//启动多线程加载头像到按钮上，加快对话框的显示时间
			new Thread(){
				public void run() {
					for(int i=0;i<btnPortrait.length;i++){
						portraits[i] = new Portrait(i+1);
						btnPortrait[i].setIcon(portraits[i]);
					}
				}
			}.start();
			
			
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(true);
		}
		
		/**
		 * 初始化一些面板，仅仅为简便方法。
		 * @param pane
		 */
		private void initJLabel(JLabel pane){
			pane.setOpaque(true);
			pane.setBackground(new Color(226,247,254));
			pane.setPreferredSize(new Dimension(380,25));
			pane.setBorder(new LineBorder(Color.BLACK));
		}
		
		/**
		 * 根据开始和结束获得面板。
		 * @param begin 开始的头像位置。
		 * @param end 结束的头像位置。
		 * @return 添加好头像的面板。
		 */
		private JPanel getPane(int begin,int end){
			JPanel pane = new JPanel();
			pane.setOpaque(true);
			pane.setBackground(Color.WHITE);
			pane.setLayout(new GridLayout(0,7,5,5));
			for(int i = begin;i<end;i++)
				pane.add(btnPortrait[i]);
			
			return pane;
		}
		
		public void actionPerformed(ActionEvent e) {
			//点击确定时更改选择的图像
			if(e.getSource()==btnP_Ok){
				photoJLabel.setIcon(lblPhotoView.getIcon());
				dispose();
				return;
			}
			//点击确定时关闭选择图像的窗口
			if(e.getSource()==btnP_Cancle){
				dispose();
				return;
			}
			//如果是图像的按钮时，显示图想到浏览头像面板上
			int i = -1;
			for(i=0;i<btnPortrait.length;i++){
				if(e.getSource()==btnPortrait[i])
					break;
			}
			if(i<btnPortrait.length){
				lblPhotoView.setIcon(portraits[i]);
			}
				
		}
	}
	public static void main(String[] args) {
		RegisterPane r=new RegisterPane();		
	}
}
