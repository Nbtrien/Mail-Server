package core;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectSQL
{
	Connection conn;
	Statement sttm;
	public void ConnectSQL()
	{
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mail","root","");  
			System.out.println("ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int change(String sql) 
	{
		int n=0;
		try {
			ConnectSQL();
			sttm = conn.createStatement();
			n=sttm.executeUpdate(sql);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return n;
	}
	
	public ResultSet gettable(String sql) 
	{
		ResultSet n = null;
		try {
			ConnectSQL();
			sttm = conn.createStatement();
			n = sttm.executeQuery(sql);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return n;
	}
}
