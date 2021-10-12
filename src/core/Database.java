package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Database {
	private ConnectSQL conn;
	
	public Database(ConnectSQL conn) {
		super();
		this.conn = conn;
	}
	
	public boolean checkUser(String email) {
		boolean resutl = false;
		ResultSet rst = conn.gettable("SELECT * FROM users WHERE email = "+"'"+email+"'");
		System.out.println("SELECT * FROM users WHERE email = "+"'"+email+"'");
		try {
			if (rst.isBeforeFirst()) {
				resutl = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resutl;
	}
	
	public boolean checkEmailRegister(String email) throws SQLException {
		boolean check = true;
		ResultSet rst = conn.gettable("SELECT * FROM users");
		ResultSetMetaData rsm = rst.getMetaData();
		int count_column = rsm.getColumnCount();
		while (rst.next())
		{
			for(int i=1; i<=count_column; i++) {
				if(email.equals(rst.getObject("email").toString())) {
					check = false;
				}
			}
		}
		return check;
	}

	public User getUser(String email) {
		User user = new User();
		ResultSet rst = conn.gettable("SELECT * FROM users WHERE email = "+"'"+email+"'");
		System.out.println("SELECT * FROM users WHERE email = "+"'"+email+"'");
		try {
		while (rst.next())
		{
//				System.out.println(rst.getObject(i).toString());
				user.setId(rst.getInt("id"));
				user.setEmail(rst.getObject("email").toString());
				user.setUserName(rst.getObject("user_name").toString());
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	public User getUserbyId(int id) {
		User user = new User();
		ResultSet rst = conn.gettable("SELECT * FROM users WHERE id = "+"'"+id+"'");
		System.out.println("SELECT * FROM users WHERE id = "+"'"+id+"'");
		try {
		while (rst.next())
		{
//				System.out.println(rst.getObject(i).toString());
				user.setId(rst.getInt("id"));
				user.setEmail(rst.getObject("email").toString());
				user.setUserName(rst.getObject("user_name").toString());
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	private byte[] getFileContent(String fileLocation) {
		Path path = Paths.get(fileLocation);
		byte[] file = null;
		try {
			file = Files.readAllBytes(path);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return file;
	}
	
	public Email getEmailbyId(int id) {
		Email email = new Email();
		ResultSet rst = conn.gettable("SELECT * FROM mails WHERE id = "+"'"+id+"'");
		System.out.println("SELECT * FROM mails WHERE id = "+"'"+id+"'");
		try {
		while (rst.next())
		{
				User userFrom = this.getUserbyId(rst.getInt("user_id_from"));
				User userTo = this.getUserbyId(rst.getInt("user_id_to"));
				email.setId(id);
				email.setTitle(rst.getObject("title").toString());
				email.setUserFrom(userFrom);
				email.setUserTo(userTo);
//				email.setContent(rst.getObject("file_location").toString());
				String content  = new String(getFileContent(rst.getObject("file_location").toString()));
				email.setContent(content);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return email;
	}
	
	public List<Email> getListInbox(int id) {
		List<Email> eList = new ArrayList<>();
		ResultSet rst = conn.gettable("SELECT * FROM mails WHERE user_id_to = "+"'"+id+"'");
		System.out.println("SELECT * FROM mails WHERE user_id_to = "+"'"+id+"'");
		try {
			while (rst.next())
			{
				User user = this.getUserbyId(rst.getInt("user_id_from"));
				
				Email email = new Email();
				email.setId(rst.getInt("id"));
				email.setTitle(rst.getObject("title").toString());
				email.setUserFrom(user);
				eList.add(email);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return eList;
	}
	
	public List<Email> getListSend(int id) {
		List<Email> eList = new ArrayList<>();
		ResultSet rst = conn.gettable("SELECT * FROM mails WHERE user_id_from = "+"'"+id+"'");
		System.out.println("SELECT * FROM mails WHERE user_id_from = "+"'"+id+"'");
		try {
			while (rst.next())
			{
				User user = this.getUserbyId(rst.getInt("user_id_to"));
				Email email = new Email();
				email.setId(rst.getInt("id"));
				email.setTitle(rst.getObject("title").toString());
				email.setUserFrom(user);
				eList.add(email);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return eList;
	}
	
	public boolean insertNewUser(String userName, String email, String password) {
		System.out.println("INSERT INTO `users`(`user_name`, `email`, `password`) VALUES('"+userName+"','"+email+"','"+password+"')");
		int n = conn.change("INSERT INTO `users`(`user_name`, `email`, `password`) VALUES('"+userName+"','"+email+"','"+password+"')");
		if (n == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean insertNewEmail(String title, String fileLocation, int userIdFrom, int userIdTo) {
	   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
	   LocalDateTime now = LocalDateTime.now();  
		System.out.println("INSERT INTO `mails`(`title`, `file_location`, `user_id_from`, `user_id_to`, `date`) VALUES('"+title+"','"+fileLocation+"','"+userIdFrom+"','"+userIdTo+"','"+now+"')");
		int n = conn.change("INSERT INTO `mails`(`title`, `file_location`, `user_id_from`, `user_id_to`, `date`) VALUES('"+title+"','"+fileLocation+"','"+userIdFrom+"','"+userIdTo+"','"+now+"')");
		if (n == 1) {
			return true;
		} else {
			return false;
		}
	}
}
