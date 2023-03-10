package data;

import java.io.Serializable;

public class FriendUser implements Serializable{
private Integer acnum;
private String nickName;
private String signature;
private Integer photo;
private Integer state;
public FriendUser(Integer acnum,String nickName,String signature,Integer photo, Integer state) {
	this.acnum=acnum;
	this.nickName=nickName;
	this.signature=signature;
	this.photo=photo;
	this.state=state;
}
public  FriendUser() {
	
}
public Integer getAcnum() {
	return acnum;
}
public void setAcnum(Integer acnum) {
	this.acnum = acnum;
}
public String getNickName() {
	return nickName;
}
public void setNickName(String nickName) {
	this.nickName = nickName;
}
public String getSignature() {
	return signature;
}
public void setSignature(String signature) {
	this.signature = signature;
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
public boolean equals(Object object) {
	if(object instanceof FriendUser) {
	FriendUser user=(FriendUser)object;
	return user.getAcnum().equals(acnum);
	}else {
		return false;
	}
}
public FriendUser(DAOUser u) {
	this.acnum=u.getAcnum();
	this.nickName=u.getNickname();
	this.signature=u.getSignature();
	this.photo=u.getPhoto();
	this.state=u.getState();
}
}
