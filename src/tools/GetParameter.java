package tools;

import java.util.HashMap;

public class GetParameter {


	/**
	 * Port:服务器启动服务开启的端口
	 * MinDIgit:随机生成账号的最小长度
	 * MaxDigit:..最大长度
	 * ShieldAc:屏蔽的账号
	 * IsBak：自动备份日志
	 * Driver：数据库驱动
	 * LinkParameter：数据库连接参数
	 * UserName、password、dbname、charset:连接数据库
	 */
	public String[] keys ;
	/**
	 * 属性文件的缺省值。
	 */
	public  String[] values;
	
	public HashMap<String,String> paramap;

	
	public GetParameter() {
		keys =new String[]  { "Port", "MinDigit", "MaxDigit", "ShieldAc",
				"IsBak", "Driver", "LinkParameter",
				"UserName", "Password", "DBName", "Charset" ,"TableName"};
		values =new String[] { "3608", "5", "9", "10000;88888", "1",
				 "com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/",
					"12345user", "12345", "chatting", "gb2312","chatting.user" };
		paramap=new HashMap<>()
		{
			{
				for(int i=0;i<keys.length;i++)
				put(keys[i],values[i]);
			}
		};
	}
}
