package server.frm.pane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import dao.UserDAObyMySQL;
import data.DAOUser;
import tools.DateDeal;
import tools.FillWidth;


//用户管理面板
public class UserPane extends JPanel implements ActionListener,Runnable{
	private JLabel queryJLabel=new JLabel("查询用户");
	private JTextField queryField=new JTextField("");
	private JButton queryButton=new JButton("查询");
	private JButton flashButton=new JButton("刷新");
	private JTable table=null;
	private DefaultTableModel model=null;
    public UserPane() {
		// TODO Auto-generated constructor stub
    	queryField.setPreferredSize(new Dimension(100,25));
    	model=new DefaultTableModel()
    	{{addColumn("ID");
    		addColumn("账号");
    		addColumn("昵称");
    		addColumn("性别");
    		addColumn("年龄");
    		addColumn("E-mail");
    		addColumn("状态");
    		addColumn("注册时间");  		
    		} };
    		table=new JTable(model);
    		TableRowSorter sorter=new TableRowSorter(model);
    		table.setRowSorter(sorter);
    		table.addMouseListener(new TableMouseAdapter());
    		table.addMouseMotionListener(new TableMouseAdapter());
    		//第一列 id 
    		TableColumn tcID=table.getColumn(model.getColumnName(0));
    		tcID.setPreferredWidth(25);
    		tcID.setMaxWidth(25);
    		tcID.setMinWidth(25);
    		//第二列账号
    		TableColumn tcac=table.getColumn(model.getColumnName(1));
    		tcac.setPreferredWidth(70);
    		tcac.setMaxWidth(80);
    		tcac.setMinWidth(60);
    		
    		TableColumn tcNick = table.getColumn(model.getColumnName(2));
    		tcNick.setPreferredWidth(70);
    		tcNick.setMaxWidth(80);
    		tcNick.setMinWidth(60);
    
    		TableColumn tcSex = table.getColumn(model.getColumnName(3));
    		tcSex.setPreferredWidth(30);
    		tcSex.setMaxWidth(30);
    		tcSex.setMinWidth(30);
    		
    		TableColumn tcAge = table.getColumn(model.getColumnName(4));
    		tcAge.setPreferredWidth(30);
    		tcAge.setMaxWidth(30);
    		tcAge.setMinWidth(30);

    		TableColumn tcState = table.getColumn(model.getColumnName(6));
    		tcState.setPreferredWidth(30);
    		tcState.setMaxWidth(30);
    		tcState.setMinWidth(30);
    	
    		
    		JPanel paneNorth=new JPanel();
    		paneNorth.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
    		paneNorth.add(queryJLabel);
    		paneNorth.add(queryField);
    		paneNorth.add(queryButton);
    		paneNorth.add(flashButton);
    		
    		setLayout(new BorderLayout());
    		add(paneNorth,BorderLayout.NORTH);
    		add(new JScrollPane(table),BorderLayout.CENTER);
    		add(new FillWidth(5,5),BorderLayout.WEST);
    		add(new FillWidth(5,5),BorderLayout.EAST);
    		
    		queryButton.addActionListener(this);
    		flashButton.addActionListener(this);
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		flashButton.setEnabled(false);
		table.setAutoCreateRowSorter(false);
		model.setRowCount(0);
		UserDAObyMySQL userdao=new UserDAObyMySQL();
		Vector<DAOUser>users=userdao.findAll();
		for(DAOUser u:users) addUserToTable(u);		
		table.setAutoCreateRowSorter(true);
		flashButton.setEnabled(true);
		}
				
 //查询，刷新等按钮事件
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//刷新
		if(e.getSource()==flashButton)flushpane();
		//查询
		if(e.getSource()==queryButton){
			Queryuser(queryField.getText());
		}
	}
public void flushpane() {
	// TODO Auto-generated method stub
	new Thread(this).start();
}
private void Queryuser(String query) {
	// TODO Auto-generated method stub
	if(query.equals(""))return;
   	UserDAObyMySQL userdao=new UserDAObyMySQL();
   	model.setRowCount(0);
   	try {
		int acnum=Integer.parseInt(query);
		DAOUser user=userdao.findById(acnum);
		if(user!=null)addUserToTable(user);			
	} catch (Exception e) {
		// TODO: handle exception
		JOptionPane.showMessageDialog(null, "请确保输入的正确的账号!");
		return;
	}
   	return;
}
private class TableMouseAdapter extends MouseAdapter{
	public void mouseMoved(MouseEvent e) {
	if(e.getSource()==table) {
		int row=table.rowAtPoint(e.getPoint());
		table.changeSelection(row, 0, false, true);//选中一行
	}
	}
	
}
private void addUserToTable(DAOUser user) {
	if(user!=null){
		Vector<Object> v = new Vector<Object>();
		v.add(user.getId());
		v.add(user.getAcnum());
		v.add(user.getNickname());
		v.add(user.getSex());
		v.add(user.getAge());
		v.add(user.getEmail());
		v.add(user.getState()==1?"在线":"离线");
		v.add(DateDeal.getDate(user.getRegisterTime()));
		model.addRow(v);
	}
}
}
