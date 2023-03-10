package data;

import java.io.Serializable;
import java.sql.Date;
import java.util.Vector;

public class DAOUser implements Serializable {
	private Integer id;
	private Integer acnum;
	private String nickname;
	private String sex="女";
	private Integer age=0;
	private String password;
	private String signature = "hello world";
	private String email = "";
	private Integer photo = 1;
	private Integer state = 2;//离线
	private Vector<Integer> listFriend = new Vector<Integer>();
	private Date registerTime ;//注册时间
	public DAOUser(){}
	public DAOUser(String nickname,String email,String password){
		this.nickname=nickname;
		this.email=email;
		this.password=password;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAcnum() {
		return acnum;
	}
	public void setAcnum(Integer acnum) {
		this.acnum = acnum;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Vector<Integer> getListFriend() {
		return listFriend;
	}
	public void setListFriend(Vector<Integer> listFriend) {
		for(Integer i:listFriend)
		this.listFriend.add(i);
	}
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getPhoto() {
		return photo;
	}
	public void setPhoto(Integer photo) {
		this.photo = photo;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public void addfriends(Integer acnum) {
		this.listFriend.add(acnum);
	}
	public String toString() {
		return (acnum+"-"+nickname);
	}
}
