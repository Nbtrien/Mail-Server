package core;
import java.io.Serializable;

public class UserAuth implements Serializable{
	private String name;
	private String email;
	private String password;
	public UserAuth(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	@Override
	public String toString() {
		return "UserAuth [name=" + name + ", email=" + email + ", password=" + password + "]";
	}
	
	
}
