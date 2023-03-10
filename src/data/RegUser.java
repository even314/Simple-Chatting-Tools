
package data;

import java.io.Serializable;

 /**
 * ◊¢≤·”√ªß¿‡°£
 */
public class RegUser implements Serializable {
	private Integer acnum;
	private String nickname;
	private String password;
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getAcnum() {
		return acnum;
	}
	public void setAcnum(Integer acnum) {
		this.acnum = acnum;
	}
	
	
}
