package data;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tools.FillWidth;

public class Asking extends JDialog implements Serializable,ActionListener{
 private FriendUser touser;
 private FriendUser frmuser;

private JLabel msgJLabel=new JLabel();
 private JButton accButton=new JButton("接受");
 private JButton refButton=new JButton("拒绝");
 private ObjectOutputStream oos=null;
 
public  Asking(FriendUser touser,FriendUser frmUser,Boolean visible){
	 this.setTouser(touser);
	 this.frmuser=frmUser;
	 setSize(400,200);
	 setTitle("好友申请");
	 init();
	 setModal(true);
	 accButton.addActionListener(this);
	 refButton.addActionListener(this);
	 setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	setVisible(visible);
 }

	private void init() {
	// TODO Auto-generated method stub
	msgJLabel.setText("用户 "+frmuser.getNickName()+"["+frmuser.getAcnum()
	+"]"+'\n'+" 向你发送好友申请");
	msgJLabel.setSize(new Dimension(200, 20));
	JPanel msgJPanel=new JPanel(new FlowLayout());
	msgJPanel.setSize(new Dimension(400,30));
	msgJPanel.add(msgJLabel);
	accButton.setSize(new Dimension(60,20));
	refButton.setSize(new Dimension(60,20));
	msgJLabel.setSize(new Dimension(100,50));
	JPanel btnPanel=new JPanel(new FlowLayout());
	btnPanel.add(accButton);
	btnPanel.add(refButton);
	add(new FillWidth(400, 50),BorderLayout.NORTH);
	add(msgJPanel,BorderLayout.CENTER);
	add(btnPanel,BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==accButton) {
		Vector<Integer> eachaddFriends=new Vector<>();
		eachaddFriends.add(frmuser.getAcnum());
		eachaddFriends.add(touser.getAcnum());
			Message msg=new Message(45,eachaddFriends);
		if(oos!=null)
		{
			try {
				oos.writeObject(msg);
				oos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		dispose();
		}
		if(e.getSource()==refButton) {
			Message msg=new Message(47,frmuser);
			if(oos!=null)
			{
				try {
					oos.writeObject(msg);
					oos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			dispose();
		}
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public FriendUser getTouser() {
		return touser;
	}

	public void setTouser(FriendUser touser) {
		this.touser = touser;
	}
	 public FriendUser getFrmuser() {
			return frmuser;
		}

		public void setFrmuser(FriendUser frmuser) {
			this.frmuser = frmuser;
		}

public static void main(String[] args) {
FriendUser f=	new FriendUser();
	f.setAcnum(1);
	f.setNickName("O");
	f.setPhoto(1);
  f.setState(2);
  new Asking(f, f,true);
}

}
