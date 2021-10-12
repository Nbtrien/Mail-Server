package core;

import java.io.Serializable;

public class User implements Serializable{
	private int id;
	private String userName;
	private String email;
	
	public int getId() {
		return id;
	}
	public void setId(int i) {
		this.id = i;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", email=" + email + "]";
	}
}
