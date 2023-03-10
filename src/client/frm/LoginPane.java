package client.frm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalBorders.OptionDialogBorder;

import data.LoginUser;
import data.Message;
import tools.GetParameter;
import tools.FillWidth;


public class LoginPane extends JFrame implements ActionListener {
private JLabel acnumJLabel=new JLabel("账号");
private JLabel pwdJLabel=new JLabel("密码");
private JPasswordField pwdField=new JPasswordField();
private JTextField acnumField=new JTextField();

private JButton registerButton=new JButton("申请账号");
private JButton loginButton=new JButton("登录");

//布置框架面板
public LoginPane() {
	setTitle("登录");
	setSize(324,170);
	setResizable(false);
	Toolkit tk=Toolkit.getDefaultToolkit();
	setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
	setBackground(new Color(224,244,251));
  init();
  
  loginButton.addActionListener(this);
  registerButton.addActionListener(this);
  
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setVisible(true);
}
//初始化各部件
private void init() {
	// TODO Auto-generated method stub
	acnumField.setPreferredSize(new Dimension(140,20));
	pwdField.setPreferredSize(new Dimension(140,20));
	
	//父面板
	JPanel pane = new JPanel();
	pane.setBackground(new Color(240,250,255));
	pane.setBorder(new LineBorder(new Color(144,185,215)));//外边框
	pane.setLayout(new FlowLayout(FlowLayout.CENTER,15,12));//流式布局，居中对齐
	pane.setPreferredSize(new Dimension(300,110));
	
	pane.add(new FillWidth(20,20,new Color(240,250,255)));
	pane.add(acnumJLabel);
	pane.add(acnumField);
	pane.add(new FillWidth(20,20,new Color(240,250,255)));
	pane.add(new FillWidth(20,20,new Color(240,250,255)));
	pane.add(pwdJLabel);
	pane.add(pwdField);
	pane.add(new FillWidth(20,20,new Color(240,250,255)));
	pane. add(registerButton);
    pane. add(loginButton);
	
	
	//主框架
	setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
	add(new FillWidth(100, 10));
	add(pane);
	add(new FillWidth(300,8));
}
@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	if(e.getSource()==registerButton)
	{
		dispose();
		new RegisterPane();
	}
	if(e.getSource()==loginButton) {
		if(acnumField.getText().toString().equals("")
				||new String(pwdField.getPassword()).equals("") )return;
		try {
			Integer acnum=Integer.valueOf(acnumField.getText());
			dispose();
			new MainPane("127.0.0.1",Integer.parseInt(new GetParameter().paramap.get("Port")),
					acnum,new String(pwdField.getPassword()),1);		
		} catch (Exception e2) {
			// TODO: handle exception
			System.out.println("LoginPane.actionPerformed()"+e);
			JOptionPane.showMessageDialog(null, "请在账号框输入数字！");
			return;
		}

	}
	
}
public static void main(String[] args) {
	LoginPane login=new LoginPane();
}
}
