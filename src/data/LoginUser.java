package data;

import java.io.Serializable;

public class LoginUser implements Serializable {
private Integer acnum;
private String password;
private  Integer state;
public LoginUser() {}
public LoginUser(Integer acnum,String password) {
	this.acnum=acnum;
	this.password=password;
}
public Integer getAcnum() {
	return acnum;
}
public void setAcnum(Integer acnum) {
	this.acnum = acnum;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public Integer getState() {
	return state;
}
public void setState(Integer state) {
	this.state = state;
}
}
