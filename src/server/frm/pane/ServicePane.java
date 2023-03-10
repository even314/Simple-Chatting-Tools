package server.frm.pane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dao.AskingDAObyFile;
import dao.LogDAObyFile;
import dao.RecordDAOByFile;
import dao.UserDAObyMySQL;
import data.Asking;
import data.DAOUser;
import data.FriendUser;
import data.Log;
import data.LoginUser;
import data.Message;
import data.Record;
import data.RegUser;
import tools.DateDeal;
import tools.GetParameter;
import tools.JQCreater;
import tools.FillWidth;

public class ServicePane extends JPanel implements ActionListener,Runnable{
		private JButton startButton=new JButton("启动服务器");
		 private JButton stopButton=new JButton("关闭服务器");
		 private JTextArea logArea=new JTextArea();
		 private ServerSocket server=null;
		 //哈希表存客户端连接（对应在线的用户）<账号，客户端连接类>
		 public static Hashtable<Integer, ClientLink>clientHashtable=null;
		 
		 private static boolean isServiceRun=false;//程序是否运行中
		 
		 private String pathString="log.txt";//操作日志路径
		 private PrintWriter rafPrintWriter=null;
		 
		 //初始化面板，放置组件
		 public ServicePane() {
		try {
			//绑定写入的日志文件
			rafPrintWriter=new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(pathString),true)));
		} catch (FileNotFoundException e) {
			logArea.append("发生异常错误，请确保"+pathString+"文件可写!原因如下:"+e.getMessage());
			startButton.setEnabled(false);//启动服务器的按钮无法按下
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		 //界面
		 setLayout(new FlowLayout(FlowLayout.CENTER));
		 logArea.setEditable(false);
		 logArea.setLineWrap(true);
		 startButton.addActionListener(this);
		 stopButton.addActionListener(this);
		 stopButton.setEnabled(false);
		 
		 JPanel pane=new JPanel();
		 pane.setPreferredSize(new Dimension(600,70));
		 pane.setLayout(new FlowLayout(FlowLayout.CENTER));
		 pane.add(startButton);
		 pane.add(stopButton);
		 
		 setLayout(new BorderLayout());
		 add(pane,BorderLayout.NORTH);
		 add(new JScrollPane(logArea),BorderLayout.CENTER);
		add(new FillWidth(4, 4),BorderLayout.WEST);//fillwidth：自定义空白占位组件类
		add(new FillWidth(4,4),BorderLayout.EAST);
		 }
		 		 	 
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource()==startButton) {
				try {
					startButton.setEnabled(false);
					stopButton.setEnabled(true);
					startServer();
				} catch (Exception e1) {
					// TODO: handle exception
					writeSysLog(DateDeal.getCurrentTime()+",服务器启动服务时发生错误，原因如下:"+e1.getMessage());
				}
				}
				if(e.getSource()==stopButton) {
					startButton.setEnabled(true);
					stopButton.setEnabled(false);
					try {
						stopServer();
					} catch (Exception e1) {
						// TODO: handle exception
						writeSysLog(DateDeal.getCurrentTime()+",服务器停止服务时发生错误，原因如下:"+e1.getMessage());
					}
				}
			}

//启动服务器
		private void startServer() throws ClassNotFoundException, IOException {
				// TODO Auto-generated method stub
				isServiceRun=true;//服务器正在运行
				clientHashtable=new Hashtable<Integer,ClientLink>();
				//Class.forName("tools.GetParameter");
				int port= Integer.parseInt(new GetParameter().paramap.get("Port")) ;
				server=new ServerSocket(port);
				new Thread(this).start();//启动线程，执行run（可以理解为调用run）
				writeSysLog(DateDeal.getCurrentTime()+",服务器服务启动成功!等待用户上线...");
			}
		private void stopServer() {
			// TODO Auto-generated method stub
			isServiceRun=false;
			Enumeration<ClientLink>en=clientHashtable.elements();//枚举类，相当于迭代器
		while(en.hasMoreElements()) {
			//逐一退出客户账号
			ClientLink client=en.nextElement();
			client.updateUserState(client.acnum, 2);
			client.letClientQuit();
		}
		clientHashtable.clear();
		clientHashtable=null;
		if(server!=null)
			try {
				server.close();
				server = null;
				writeSysLog(DateDeal.getCurrentTime()+",服务器服务停止成功!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
		//父线程：serversoket监听，建立连接
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(isServiceRun) {
				try {
					Socket client=server.accept();
					System.out.println("come accept");
					new Thread(new ClientLink(client)).start();//子线程
				} catch (Exception e) {
					// TODO: handle exception
					writeSysLog(DateDeal.getCurrentTime()+",服务器接受客户端时发生异常:"+e.getMessage());
				}
				
			}
		}

		//写系统服务界面日志
		 public void writeSysLog(String log) {
				// TODO Auto-generated method stub
			    logArea.append(log+"\n");
				//滚动条自动下滚
				logArea.setCaretPosition(logArea.getDocument().getLength());
				rafPrintWriter.write(log+"\n");
				rafPrintWriter.flush();
			}
		 
		 //写入文件
			private  void writelog(Log log) {
				// TODO Auto-generated method stub
				try {
					LogDAObyFile logDAO=new LogDAObyFile();
					logDAO.add(log);				
				} catch (Exception e) {
					// TODO: handle exception
					writeSysLog(DateDeal.getCurrentTime()+",写入操作日志["+log.toString()+"]时发生错误:"+e.getMessage());
				}
			}
		
		 //客户端连接类，接收处理客户端信息和发出。
		 private class ClientLink implements Runnable,Serializable  {
					public Socket client=null;
					public ObjectInputStream ois=null;
					public ObjectOutputStream oos=null;
					public int acnum=-1;//账号
					public boolean equals(ClientLink cl) {
						return (this.client.equals(cl.client));
					}
					public ClientLink(Socket client) {
						this.client=client;//连接到某个客户端的socket对象
						writeSysLog(DateDeal.getCurrentTime()+",客户端"+getClientIP()+"]连接到服务端");
						try {
							ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
							oos = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
						} catch (IOException e) {
							writeSysLog(DateDeal.getCurrentTime()+",获取到客户端"+getClientIP()+"的连接发生错误:"+e.getMessage());
						}
					}

					/*处理和发送message
					 * type类型：
					 * 以1开头的：注册相关消息
					10:客户端发送注册信息到服务端
					11:服务端回复注册成功到客户端
					12:服务端回复注册失败到客户端
					
					以2开头的：登陆相关消息
					20:客户端发送登陆信息到服务端（判断此用户是否已登录）
					21:登陆成功服务端发送好友信息到客户端
					22:登录失败服务端发送错误信息到客户端
					23:服务端发送账号在别处登陆
					24:客户端发送退出到服务端
					25:服务端发送好友上线功能
					
					以3开头的：发送记录相关消息
					30:客户端发送消息到服务端
					31:服务端根据消息发送到客户端
					
					以4开头的：搜索在线用户加好友相关消息
					40:客户端发送好友申请
					41:服务器向客户端发送好友申请
					44:向客户端发送添加好友的qq号
					45:客户端发送互相添加好友请求
					47:客户端拒绝添加好友，添加好友失败
					49:服务向客户端发送添加好友成功
					
					以9开头的：系统相关消息
					90:服务端发送下线功能到客户端
					*/
			@Override
					public void run() {
				// TODO Auto-generated method stub
				try {
					while(isServiceRun&&ois!=null&&oos!=null) {
						//接受客户端发来的信息
						Object object=ois.readObject();
						if(object instanceof Message) {
							Message message=(Message) object;
							int type=message.gettype();
							if(type==10) {//客户端注册
								dealRegiter(message);
								break;
							}
						switch (type) {
						case 20://客户端登录
							{System.out.println("ServicePane.ClientLink.run()20,267");
							DealLogin(message);
							break;}
						case 24://客户端退出
							dealQuit(message);
							break;
						case 30://发聊天消息
							dealMessage(message);
							break;
						case 40://好友请求
						   dealAskAddFri(message);
							break;
						case 45://互相添加好友
							Addeach(message);
							break;
						case 47://添加好友失败
						dealFailAdd(message);
							break;
						}
								}
						else
							writeSysLog("客户端"+getClientIP()+"发送错误的数据信息到服务端");
					}
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("server recieve msg failed");
					removeClientForException(this);
					closeClient();
				}
			}
			

			//得到客户端的ip
			private String getClientIP() {
				// TODO Auto-generated method stub
				return client==null?"[客户端已关闭,不能获取信息]":"["+client.getInetAddress().toString()+":"+client.getPort()+"]";						
			}
			
			//给客户端发送消息
			public boolean writeToClient(Message message) {
				// TODO Auto-generated method stub
				System.out.println("ServicePane.ClientLink.writeToClient()304");
				new ClientWrite(this,message).start();
				return true;
			}
				
			//10：处理注册信息
		    private void dealRegiter(Message message) throws FileNotFoundException, IOException {
				if(message.getobject()instanceof DAOUser) {
								DAOUser user=(DAOUser)message.getobject();
								JQCreater creater = new JQCreater();
								int id = creater.createID();
								int num = creater.createJQ();
								creater.saveIDJQ(id, num);
								user.setId(id);
								user.setAcnum(num);
								user.setRegisterTime(new java.sql.Date(System.currentTimeMillis()));
								UserDAObyMySQL udbm=new UserDAObyMySQL();
								boolean b=udbm.add(user);
								Message regresultMessage=new Message();
								if(b) {
									regresultMessage.settype(11);				
									RegUser regUser=new RegUser();
									regUser.setAcnum(user.getAcnum());
									regUser.setNickname(user.getNickname());
									regUser.setPassword(user.getPassword());
									regresultMessage.setobject(regUser);
									writelog(getLog(user,"新用户注册成功!"));
								}
								else{
									regresultMessage.settype(12);
									regresultMessage.setobject(null);
									writelog(getLog(user,"用户注册失败!"));
								}
								writeToClient(regresultMessage);
								}
							else {
								writeSysLog("客户端"+getClientIP()+"发送错误的数据信息到服务端");					
							}
						}
			
			//20：用户登录
			private void DealLogin(Message message) {
				if(message.getobject() instanceof LoginUser) {
					LoginUser loginUser=(LoginUser)message.getobject();
					String inputpassword=loginUser.getPassword();
					int acnum=loginUser.getAcnum();
					UserDAObyMySQL userDAO=new UserDAObyMySQL();
					try {
						DAOUser user=userDAO.findById(acnum);//与数据库信息交流的对象
						//是数据库中信息取出的载体
						Message loginresultMessage=new Message();
						if(user!=null)//账号存在
						{
							if(user.getPassword().equals(inputpassword)) {
								//密码正确，成功登录
								this.acnum=acnum;//绑定socket
								//检测用户是否登录，若已经登录，之前登录的用户下线
								if(clientHashtable.containsKey(acnum))
									letClientLogout(acnum,client.getInetAddress().toString());
								user.setState(loginUser.getState());
								userDAO.update(user);
								
								//获取user好友信息，转为好友对象，发送给用户
								//用户在客户端要用frienduser类进行交流（隐去daouser中密码的信息
								//因此还要创建一个表示自己的frienduser
								Vector<FriendUser>friends=new Vector<FriendUser>() ;
									FriendUser selfUser=new FriendUser(user);
										friends.add(selfUser);								
								Vector<Integer> friendlist=user.getListFriend();
								if(friendlist!=null)
								for(Integer i:friendlist) {
									DAOUser tempDaoUser=userDAO.findById(i);
								if(tempDaoUser!=null)
						{
									FriendUser fu=new FriendUser(tempDaoUser);
								 friends.add(fu);
									}
								}	
								loginresultMessage.settype(21);
								loginresultMessage.setobject(friends);
							//	System.out.println("ServicePane.ClientLink.DealLogin()548");
								writeToClient(loginresultMessage);
									clientHashtable.put(user.getAcnum(), this);
								//通知好友，我上线了
								writelog(getLog(user, "用户登录"));
								telfriendState(user);
								//检测是否有好友留言
								RecordDAOByFile recordDAO=new RecordDAOByFile();
								Vector<Record> v=recordDAO.findLeaveRecord(acnum);
								try {
									if(v!=null)for(Record r:v) 
									sendRecordToClient(this, r);
									recordDAO.deleteRecordForAdmin(acnum);									
								} catch (Exception e) {
									// TODO: handle exception
									writelog(getLog(user, "发送留言给用户时发生错误:"+e.getMessage()));
									System.out.println("send record failed "+e);
								}
								//检测是否有申请
								AskingDAObyFile askingDAO=new AskingDAObyFile();
								Vector<Asking> va=askingDAO.findLeaveAsking(acnum);
								try {
									if(va!=null)for(Asking a:va) 
									sendAskingToClient(this, a);
									askingDAO.deleteAskingForAdmin(acnum);
									
								} catch (Exception e) {
									// TODO: handle exception
									writelog(getLog(user, "发送请求给用户时发生错误:"+e.getMessage()));
									System.out.println("send asking failed "+e);
								}
							}
							else {
								loginresultMessage.settype(22);
								loginresultMessage.setobject("登陆密码错误！"+"["+loginUser.getAcnum()+"]");
								writeToClient(loginresultMessage);
								writelog(getLoginLog(loginUser, "错误的用户["+loginUser.getAcnum()+"]登录密码"));
								closeClient();
							}
						}
						else {//用户不存在
							loginresultMessage.settype(22);
							loginresultMessage.setobject("不存在的用户["+loginUser.getAcnum()+"]");
							writeToClient(loginresultMessage);
							writelog (getLoginLog(loginUser, "不存在的用户["+loginUser.getAcnum()+"]登录"));
							closeClient();						
						}
					} catch (Exception e) {
						// TODO: handle exception
						writeSysLog("错误："+e.getMessage());
					}

				}	else{
					writeSysLog("客户端"+getClientIP()+"发送错误的数据信息到服务端");
					closeClient();
					}
			}	
			
			//客户端下线
			private void letClientQuit() {
				Message message=new Message(90,"和服务端断开连接");
				writeToClient(message);
				closeClient();
			}
			//关闭客户端
			private void closeClient() {
				// TODO Auto-generated method stub
				String ip=getClientIP();
				writeSysLog(DateDeal.getCurrentTime()+",客户端"+getClientIP()+"下线.");
				try {
					//关闭stream&socket
					if(oos!=null)oos.close();oos = null;
					if(ois!=null)ois.close();ois = null;
					if(client!=null)client.close();client=null;
				} catch (IOException e) {
					writeSysLog(DateDeal.getCurrentTime()+",关闭到客户端"+ip+"的连接时时发生错误:"+e.getMessage());
				}
			}			
			//24：客户端退出
			private void dealQuit(Message message) {
				Object object=message.getobject();
				if(object instanceof FriendUser) {
					FriendUser user=(FriendUser)object;
					writelog(getLog(user, "用户退出"));
				}
				updateUserState(acnum,2);
				UserDAObyMySQL userDAO=new UserDAObyMySQL();
				try {
					telfriendState(userDAO.findById(acnum));
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}
				clientHashtable.remove(acnum);
				closeClient();
			}
		
			//30：服务器接受处理聊天消息
			private void dealMessage(Message message) throws FileNotFoundException, IOException {
				if(message.getobject()instanceof Record) {
					Record record=(Record) message.getobject();
					record.setSendTime(new Date(System.currentTimeMillis()));
					int acnum=record.getToid();//接收方
					if(clientHashtable.containsKey(acnum)) {
						//在线
						ClientLink client=clientHashtable.get(acnum);
						sendRecordToClient(client,record);
					}
					else {
						record.setRead(false);
						record.setReadTime(new Date(System.currentTimeMillis()));
						record.setSendTime(new Date(System.currentTimeMillis()));
						RecordDAOByFile recordDAO = new RecordDAOByFile();
						recordDAO.add(record);
					}
				}else
					writeSysLog("客户端"+getClientIP()+"发送错误的数据信息["+message.getobject()+"]到服务端");
			}
			//服务器发送聊天消息给接收方
			private void sendRecordToClient(ClientLink client, Record record) throws FileNotFoundException, IOException {
				// TODO Auto-generated method stub
				Message message=new Message(31,record);
				if(client.writeToClient(message)) {
					//成功发送
					record.setRead(true);
					record.setReadTime(new Date(System.currentTimeMillis()));
				}
				else {
					record.setRead(false);//未读
					//写入文件储存
					RecordDAOByFile rdf=new RecordDAOByFile();
					rdf.add(record);
				}
			}
			
			//40：搜索好友，加好友请求
			private void dealAskAddFri(Message message) {
				// TODO Auto-generated method stub
				Object object=message.getobject();
				if(object instanceof Integer) {
					Integer toacnum=(Integer)object;
					UserDAObyMySQL udao=new UserDAObyMySQL();
				   DAOUser u= udao.findById(toacnum);
				   Message remsg=new Message();
				 if(u==null) {
					 System.out.println("ServicePane.ClientLink.dealAskAddFri()");
					remsg.settype(46);
					remsg.setobject("用户不存在，添加失败！");
					writeToClient(remsg);
					return;
				 }
				 else {
					 //this:发出者 frmuser
					FriendUser frmUser=new FriendUser(udao.findById(acnum));
					FriendUser toUser=new FriendUser(u);
					Asking asking=new Asking(toUser,frmUser,false);
					if(clientHashtable.containsKey(toacnum)) {
						//对方在线
						ClientLink client=clientHashtable.get(toacnum);
						sendAskingToClient(client, asking);
					}
					else {
						//对方不在线，写入文件
						AskingDAObyFile adao=new AskingDAObyFile();
						try {
							adao.add(asking);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				}
			}
			private void sendAskingToClient(ClientLink client, Asking asking) {
				Message remsg=new Message();
				remsg.settype(41);
				remsg.setobject(asking);
				client.writeToClient(remsg);
			}
			
			//45：互相添加好友
			private void Addeach(Message message) {
				// TODO Auto-generated method stub
				//this:接收方 touser
				Object object=message.getobject();
				if(object instanceof Vector) {
					Vector<Integer> eachothernum=(Vector<Integer>) object;
				//0:from,1:to
				Message msg1=new Message();
				Message msg2=new Message();
				msg1.settype(49);
				msg2.settype(49);
				Integer frmnum=eachothernum.get(0);
				UserDAObyMySQL udao=new UserDAObyMySQL();
				DAOUser user1=udao.findById(frmnum);
				user1.getListFriend().add(acnum);
				DAOUser user2=udao.findById(acnum);
				user2.getListFriend().add(frmnum);
				try {
					udao.update(user1);
					udao.update(user2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				FriendUser fuser1=new FriendUser(user1);
				FriendUser fuser2=new FriendUser(user2);
				msg2.setobject(fuser1);
				msg1.setobject(fuser2);
				if(clientHashtable.containsKey(frmnum)) {
				ClientLink clientLink=	clientHashtable.get(frmnum);
				clientLink.writeToClient(msg1);
				}
				writeToClient(msg2);
				}
			}
			
			//加好友失败，有个问题：只有在线用户能收到信息
		    private void dealFailAdd(Message message) {
				// TODO Auto-generated method stub
				if(message.getobject() instanceof FriendUser) {
					FriendUser frmUser=(FriendUser) message.getobject();
					if(clientHashtable.containsKey(frmUser.getAcnum())) {
						ClientLink clientLink=clientHashtable.get(frmUser.getAcnum());
						clientLink.writeToClient(new Message(48,"对方 "+acnum+" 拒绝好友请求！"));
					}
				}
			}
		
		  //更新用户状态
			private void updateUserState(int acnum, int state) {
				// TODO Auto-generated method stub
				UserDAObyMySQL userdao=new UserDAObyMySQL();
				DAOUser user=userdao.findById(acnum);
				if(user!=null) {
					try {
						user.setState(state);
						userdao.update(user);
					} catch (Exception e) {
						// TODO: handle exception
						writelog(getLog(user, "更改用户状态时发生错误:"+e.getMessage()));
						System.out.println("update state failed"+e);
					}
					
				}
			}
			//告诉好友我(user)的状态（上线或下线）
			private void telfriendState(DAOUser user) {
				FriendUser selfUser=new FriendUser(user);
				Message message=new Message(25,selfUser);
				Vector<Integer> friendlist=user.getListFriend();
				for(Integer i:friendlist) {
					if(clientHashtable.containsKey(i)) {
						ClientLink client=clientHashtable.get(i);
						client.writeToClient(message);
					}
				}
			}
			
			//生成日志对象
			private Log getLog(DAOUser user, String what) {
				// TODO Auto-generated method stub
				Log log = new Log();
				log.setUserid(user.getAcnum());
				log.setIp(client.getLocalAddress().toString());
				log.setNickname(user.getNickname());
				log.setTime(new Date(System.currentTimeMillis()));
				log.setWhat(what);
				return log;
			}
			private Log getLog(FriendUser user, String what) {
				// TODO Auto-generated method stub
				Log log=new Log();
				log.setUserid(user.getAcnum());
				log.setIp(client.getLocalAddress().toString());
				log.setNickname(user.getNickName());
				log.setTime(new Date(System.currentTimeMillis()));
				log.setWhat(what);
				return log;
			}	
          private Log getLoginLog(LoginUser loginUser, String what) {
				// TODO Auto-generated method stub
    	  Log log=new Log();
    		log.setNickname("未知用户");
			log.setUserid(loginUser.getAcnum());
			log.setIp(client.getLocalAddress().toString());
			log.setTime(new Date(System.currentTimeMillis()));
			log.setWhat(what);
			return log;
			}
		 }
	 
	   //服务器向某个客户端传递消息的类
		 private class ClientWrite extends Thread{
				private ClientLink clientLink;
				private Message message;
				public ClientWrite(ClientLink clientLink,Message message) {
				this.clientLink=clientLink;
				this.message=message;
				}
				public void run() {
					if(clientLink.oos!=null) {
						try {
							clientLink.oos.writeObject(message);
							clientLink.oos.flush();
							System.out.println("ServicePane.ClientWrite.run()621");
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("ServicePane.ClientWrite.run()"+e);
							e.getStackTrace();
							writeSysLog("向客户端"+clientLink.getClientIP()+"发送数据失败!");
						}
						
					}
				}
				}
		//某ip的账号断开与服务器的连接
			private void letClientLogout(int acnum, String ip) {
				// TODO Auto-generated method stub
				if(clientHashtable.containsKey(acnum)) {
					ClientLink clientLink=clientHashtable.get(acnum);
					Message msg=new Message(23,"您的账号在别处[IP:"+ip+"]登录,程序将退出!");
					clientLink.writeToClient(msg);
					clientHashtable.remove(acnum);
					clientLink.closeClient();
				}
			}			 
			
			//客户端发生异常和服务器断开连接，清空clienthashtable中断开的客户端信息
			private void removeClientForException(ClientLink client) {
				if(client!=null&&clientHashtable!=null&&clientHashtable.contains(client)) {
					Enumeration<Integer> enumeration=clientHashtable.keys();
					while (enumeration.hasMoreElements()) {
						Integer acnum = (Integer) enumeration.nextElement();
						if(clientHashtable.get(acnum).equals(client)) {
							clientHashtable.remove(acnum);
							client.updateUserState(acnum, 2);//2:离线
							break;
						}
					}										
				}
			}
}
		 

		 
