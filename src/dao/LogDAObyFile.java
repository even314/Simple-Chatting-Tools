package dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import data.Log;
import tools.DateDeal;

public class LogDAObyFile implements DAO<Log, Integer>{
	private String path = "logs";
	private String suffixName = ".txt";
	@Override
	public Vector<Log> findAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	//添加新日志，返回成功与否
	@Override
	public boolean add(Log log) throws IOException {
		// TODO Auto-generated method stub
		File category=new File(path);//logs目录
		if(!category.exists())category.mkdir();//如果没有该目录就创建
		File file=new File(path+File.separator+DateDeal.getCurrentDate()+suffixName);
		PrintWriter pw=new PrintWriter(new BufferedOutputStream(new FileOutputStream(file,true)));//append:true
		String xlog = "时间:"+DateDeal.getCurrentTime()+",用户:"+log.getNickname()+"["+log.getUserid()+"],IP:"+log.getIp()+",操作:"+log.getWhat()+"\n";
		pw.write(xlog);
		pw.flush();
		pw.close();
		return true;
	}

	@Override
	public boolean delete(Log obj) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Log obj) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Log findById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
