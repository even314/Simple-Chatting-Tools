package dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Vector;

import data.Asking;
import data.FriendUser;
import data.Record;

public class AskingDAObyFile  implements DAO<Asking, Integer>{
	private String suffixName = ".dat";
	private String pathAdmin = "Askings";
	@Override
	public Vector<Asking> findAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
//添加管理员记录 (请求记录，新记录）
	public boolean add(Asking asking) throws FileNotFoundException, IOException {
		File category=new File(pathAdmin);//askings
		if(!category.exists())category.mkdir();
		File file=new File(pathAdmin+File.separator+asking.getTouser().getAcnum()+suffixName);
		ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		oos.writeObject(asking);
		oos.flush();
		oos.close();
		oos = null;		
		return true;
	}
	
	@Override
	public boolean update(Asking obj) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Asking findById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
//查找申请记录,返回一个记录数组
	public Vector<Asking>findLeaveAsking(int acnum) throws FileNotFoundException, IOException{
		File file=new File(pathAdmin+File.separator+acnum+suffixName);
		if(!file.exists())return null;
		Vector<Asking> v=new Vector<>();
		ObjectInputStream ois=new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
		while(true) {//输入流读文件
			Object object=null;
			try {
				object=ois.readObject();
			} catch (Exception e) {
				// TODO: handle exception
				break;
			}
			if(object==null)break;
			if(object instanceof Asking) {
				Asking asking=(Asking)object;
				v.add(asking);
			}
		}
		ois.close();
		ois=null;
		return v;
	}

	@Override
	public boolean delete(Asking ask) throws Exception {
		// TODO Auto-generated method stub
		
		return false;
	}
	//删除管理端信息（已读新信息删除）
	public boolean deleteAskingForAdmin(int jqnum){
		File file = new File(pathAdmin+File.separator+jqnum+suffixName);
		if(file.exists())
			return file.delete();
		else
			return false;
	}
	public static void main(String[] args) {
		Asking asking=new Asking(new FriendUser(1,"1","1",1,1), new FriendUser(2,"2","2",2,2), false);
		AskingDAObyFile aDaObyFile=new AskingDAObyFile();
		try {
			aDaObyFile.add(asking);
			System.out.println("AskingDAObyFile.main()");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
