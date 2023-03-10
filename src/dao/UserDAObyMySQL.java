package dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import data.DAOUser;
import tools.GetParameter;

public class UserDAObyMySQL implements DAO<DAOUser, Integer> {
	private static String tb;
	private static Connection connection;

static public void getconn(){
	String url;
	String user;
	String  pwd;
	try {
		HashMap<String, String>paramap=new GetParameter().paramap;
		url=paramap.get( "LinkParameter")+"qq?useUnicode=true&characterEncoding=utf8";
		user=paramap.get("UserName");
		 pwd=paramap.get("Password");
		 tb=paramap.get("TableName");
		Class.forName(paramap.get("Driver"));
		connection=(Connection) DriverManager.getConnection(url,user,pwd);
		 System.out.println("connect to mysql:succeed");		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		 System.out.println("connect to mysql:failed");
		e.printStackTrace();
	}
			}
	
	
	@Override
	public Vector<DAOUser> findAll()   {
		// TODO Auto-generated method stub
		Vector<DAOUser> v=new Vector<>();
		try {
			PreparedStatement stm=(PreparedStatement) connection.prepareStatement("select * from "+tb);
			stm.executeQuery();
			ResultSet rs = stm.getResultSet();
			while (rs.next()) {
				DAOUser u=new DAOUser();
				accessresult(rs, u);
				v.add(u);
			}
			System.out.println("findAll succeed");
			return v;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("findAll failed");
			System.out.println(e);
			return null;
		}
	}

	@Override
	public boolean add(DAOUser user)  {
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstm = (PreparedStatement) connection.prepareStatement("insert into " + tb + " values(?,?,?,?,?,?,?,?,?,?,?,?)");
			pstm.setInt(1, user.getId());
			pstm.setInt(2, user.getAcnum());
			pstm.setString(3, user.getNickname());
			pstm.setString(4, user.getSex());
			pstm.setInt(5, user.getAge());
			pstm.setString(6, user.getPassword());
			pstm.setString(7, user.getSignature());
			pstm.setString(8, user.getEmail());
			pstm.setInt(9, user.getPhoto());
			pstm.setInt(10, user.getState());
			pstm.setString(11, user.getListFriend().toString());
			pstm.setDate(12, user.getRegisterTime());
		//System.out.println(	pstm.toString());
			int i = pstm.executeUpdate();
			if (i != 1) {
				System.out.println("add failed");
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			return false;
		}
		System.out.println("add succeed");
		return true;
	}

	@Override
	public boolean delete(DAOUser user)  {
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstm=(PreparedStatement) connection.prepareStatement("delete from "+tb+" where acnum=?;");
			pstm.setInt(1, user.getAcnum());
			int i=pstm.executeUpdate();
			if(i!=1) {
				System.out.println("delete failed");
				return false;
			}
		} catch (Exception e) {
			System.out.println("delete failed");
			System.out.println(e);
			return false;
			// TODO: handle exception
		}
		return true;
	}

	@Override
	public boolean update(DAOUser user) throws Exception {
		// TODO Auto-generated method stub
		if (delete(user))
			return add(user);
		else
			return false;
	}
private void accessresult(ResultSet rs,DAOUser u) {
	try {		 
			u.setId(rs.getInt(1));
			u.setAcnum(rs.getInt(2));
			u.setNickname(rs.getString(3));
			u.setSex(rs.getString(4));
			u.setAge(rs.getInt(5));
			u.setPassword(rs.getString(6));
			u.setSignature(rs.getString(7));
			u.setEmail(rs.getString(8));
			u.setPhoto(rs.getInt(9));
			u.setState(rs.getInt(10));
			String listFriends = rs.getString(11);
			u.setRegisterTime(rs.getDate(12));
			Vector<Integer> vec = new Vector<>();
			try {
				if (listFriends != null && !"".equals(listFriends)) {
					listFriends = listFriends.replaceAll("[/S\\[\\]]", "");
					String[] numbers = listFriends.split(",");
					for (String nu : numbers) {
						vec.add(Integer.parseInt(nu));
					}
				}
			//	System.out.println("UserDAObyMySQL.accessresult() get vec:"+vec);
			}catch (Exception e) {
				// TODO: handle exception
			}
			u.setListFriend(vec);
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	//通过账号查找用户
	@Override
	public DAOUser findById(Integer acnum) {
		// TODO Auto-generated method stub
		try {
		PreparedStatement pstm=(PreparedStatement) connection.prepareStatement("select * from "+tb+" where acnum=?;");
		pstm.setInt(1, acnum);
		pstm.executeQuery();
		ResultSet rs = pstm.getResultSet();
		DAOUser u=new DAOUser();
		if (rs.next())
		accessresult(rs, u);
		if(!(u.getAcnum()).equals(acnum))return null;
		System.out.println("findById succeed");
		return u;
		} 
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("findById failed");
			System.out.println(e);
			return null;
		}
	}
	public Vector<DAOUser> findUserByName(String NickName){
		Vector<DAOUser> v = new Vector<DAOUser>();
		try {
			PreparedStatement stm = (PreparedStatement) connection.prepareStatement("select * from "+tb+" where nickname like ?");
			String pkgnickname="%"+NickName+"%";
			System.out.println(pkgnickname.toString());
			stm.setString(1, pkgnickname);
			System.out.println(stm.toString());
			stm.executeQuery();
			ResultSet rs = stm.getResultSet();
			while(rs.next()){
			DAOUser u=new DAOUser();
			accessresult(rs, u);
			v.add(u);
			}System.out.println("find by nickname succeed");
			return v;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("find by nickname failed");
			return null;
		}

	}
	
	//寻找所有在线用户
	public Vector<DAOUser> findOnlineAll() {
		Vector<DAOUser> v = new Vector<DAOUser>();
		try {
			PreparedStatement stm = (PreparedStatement) connection.prepareStatement("select * from "+tb+" where state=1;");
			stm.executeQuery();
			ResultSet rs = stm.getResultSet();
			while (rs.next()) {
				DAOUser u = new DAOUser();
				accessresult(rs, u);
				v.add(u);
			}
			System.out.println("findOnlineAll succeed");
			return v;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("findOnlineAll() failed");
			e.printStackTrace();
			return null;
		}
	}
	
	//按昵称寻找当前在线用户
	public Vector<DAOUser> findOnlineUserByName(String NickName){
		Vector<DAOUser> v = new Vector<DAOUser>();
		try {
			v=findUserByName(NickName);
			//删去离线账号
			for(int i=0;i<v.size();) {
				if(v.get(i).getState()!=1)v.remove(i);
				else i++;
			}
			System.out.println("findOnlineUserByName succeed");
			return v;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("findOnlineUserByName failed");
			e.printStackTrace();
			return null;
		}
		
		}
	public static void main(String[] args) {
		UserDAObyMySQL test=new UserDAObyMySQL();
		UserDAObyMySQL.getconn();
		test.findAll();
		DAOUser user=new DAOUser() {
			{
				setAcnum(1);
				setAge(1);
				setEmail("123");
				setId(1);
				setNickname("uu");
				setPassword("123");
				setPhoto(1);
				setState(2);
				addfriends(2);
			}
		};
		//test.add(user);
		//test.delete(user);
		test.add(user);
		DAOUser user1=new DAOUser() {
			{
				setAcnum(2);
				setAge(1);
				setEmail("122");
				setId(2);
				setNickname("uu");
				setPassword("123");
				setPhoto(1);
				//setState(2);
				addfriends(1);
			}		
		};
		test.add(user1);
         System.out.println(test.findById(1).toString());
         System.out.println(test.findAll().toString());
         System.out.println(test.findUserByName("u").toString());
}

}
