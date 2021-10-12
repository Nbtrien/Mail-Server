package core;
import java.io.Serializable;
import java.util.List;

public class DataPackage implements Serializable{
	private int keyNum;
	private int emailId;
	private UserAuth userAuth;
	private boolean status;
	private User user;
	private List<Email> eList;
	private Email email;
	private int localPort;
	
	public DataPackage(int keyNum) {
		super();
		this.keyNum = keyNum;
	}
	
	public int getKeyNum() {
		return keyNum;
	}

	public int getEmailId() {
		return emailId;
	}

	public void setEmailId(int emailId) {
		this.emailId = emailId;
	}

	public UserAuth getUserAuth() {
		return userAuth;
	}
	public void setUserAuth(UserAuth userAuth) {
		this.userAuth = userAuth;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public List<Email> geteList() {
		return eList;
	}

	public void seteList(List<Email> eList) {
		this.eList = eList;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
	
	

	
}
