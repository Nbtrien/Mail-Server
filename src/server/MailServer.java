package server;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import core.ConnectSQL;
import core.DataPackage;
import core.Database;
import core.PasswordHash;
import core.User;
import core.UserAuth;  

public class MailServer extends JFrame{
	
	public static JTextArea jTLog, jTListClient;
	
	private static ConnectSQL conn;
	public static Database database;
	
	private static final int PORT = 8002;
	private ServerSocket serverSocket;
	public Hashtable<Socket, SendClientThread> listUser;
	 
	public MailServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(50, 75);
		setTitle("Mail Client");
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS ));
		init();
		setVisible(true);
		conn = new ConnectSQL();
		database = new Database(conn);
	}
	
	public void init() {
		JLabel jLTitle = new JLabel("Server");
		jLTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		jLTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		jLTitle.setForeground(Color.blue);
		jLTitle.setBorder(new EmptyBorder(new Insets(15, 10, 15, 10)));
		
		JPanel panel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(boxlayout);
        panel.setBorder(new EmptyBorder(new Insets(20, 50, 20, 50)));
		
		JPanel jPLog = new JPanel();
		jPLog.setLayout(new BoxLayout(jPLog, BoxLayout.Y_AXIS));
		jPLog.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPLog.setBorder(new EmptyBorder(new Insets(15, 0, 15, 0)));
		
		JLabel jLLog= new JLabel("Server Log");
		jLLog.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLLog.setAlignmentX(Component.CENTER_ALIGNMENT);
		jLLog.setForeground(Color.black);
		
		jTLog = new JTextArea();
		jTLog.setBorder(BorderFactory.createLineBorder(Color.black));
		jTLog.setAlignmentY(Component.CENTER_ALIGNMENT);
		jTLog.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		jTLog.setForeground(new Color(112,112,112));
		
		JScrollPane scroll = new JScrollPane (jTLog, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(250,300));
		
		jPLog.add(jLLog);
		jPLog.add(scroll);
		
		JPanel jPListClient = new JPanel();
		jPListClient.setLayout(new BoxLayout(jPListClient, BoxLayout.Y_AXIS));
		jPListClient.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPListClient.setBorder(new EmptyBorder(new Insets(15, 0, 15, 0)));
		
		JLabel jLClient= new JLabel("List Client Online");
		jLClient.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLClient.setAlignmentX(Component.CENTER_ALIGNMENT);
		jLClient.setForeground(Color.black);
		
		jTListClient = new JTextArea();
		jTListClient.setBorder(BorderFactory.createLineBorder(Color.black));
		jTListClient.setAlignmentY(Component.CENTER_ALIGNMENT);
		jTListClient.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		jTListClient.setForeground(new Color(112,112,112));
		jTListClient.setPreferredSize(new Dimension(120,300));
		
		jPListClient.add(jLClient);
		jPListClient.add(jTListClient);
		
		panel.add(jPLog);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(jPListClient);
		
		add(jLTitle);
		add(panel);
		pack();
	}

	
	public boolean register(UserAuth userRegister){
		boolean result = false;
		try {
			if (database.checkEmailRegister(userRegister.getEmail())) {
				PasswordHash passwordHash = new PasswordHash(userRegister.getPassword());
				String newPasString = passwordHash.toHexString();
				if (database.insertNewUser(userRegister.getName(), userRegister.getEmail(), newPasString)) {
					result = true;
					File theDir = new File("f:/lập trình mạng/Mail Server/"+userRegister.getEmail());
					if (!theDir.exists()){
					   if (theDir.mkdirs()) {
						   String path = theDir.getAbsolutePath();
						   File sentFolder = new File(path+"/Sent");
						   File inboxFolder = new File(path+"/Inbox");
						   if (sentFolder.mkdirs() && inboxFolder.mkdirs()) {
							   String pathInboxFolder = inboxFolder.getAbsolutePath();
							   System.out.println("suscess");
							   try {
								      File myObj = new File(pathInboxFolder+"/new_email.txt");
								      if (myObj.createNewFile()) {
								        System.out.println("File created: " + myObj.getName());
								        FileWriter myWriter = new FileWriter(myObj.getAbsoluteFile());
								        myWriter.write("Thank "+userRegister.getEmail()+" for using this service. We hope that you will feel comfortabl........");
								        myWriter.close();
								        System.out.println("Successfully wrote to the file.");
								        User serverUser = database.getUser("server@gmail.com");
								        User clientUser = database.getUser(userRegister.getEmail());
								        System.out.println(database.insertNewEmail("Welcom"+userRegister.getName(), myObj.getAbsolutePath().replace('\\', '/'), serverUser.getId() , clientUser.getId()));
								      } else {
								        System.out.println("File already exists.");
								      }
								    } catch (IOException e) {
								      System.out.println("An error occurred.");
								      e.printStackTrace();
								    }
						   }
					   } else {
						System.out.println("fail");
					}
					} else {
						System.out.println("file existed");
					}
					jTLog.append(userRegister.getEmail()+" Register Successfully \n");
				}
			} else {
				System.out.println("register fail");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	}
	
	public boolean login(UserAuth userLogin) {
		boolean result = false;
		String email = userLogin.getEmail();
		PasswordHash passwordHash = new PasswordHash(userLogin.getPassword());
		try {
			String password = passwordHash.toHexString();
			ResultSet rst = conn.gettable("SELECT * FROM users");
			ResultSetMetaData rsm = rst.getMetaData();
			int count_column = rsm.getColumnCount();
			while (rst.next())
			{
				if( (email.equals(rst.getObject("email").toString())) && password.equals(rst.getObject("password"))) {
					result = true;
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void sendData(Socket socket, DataPackage dataPackage){
		listUser.get(socket).sendData(dataPackage);
		
//		while(e. hasMoreElements()){
//			userTo = (User) e.nextElement();
//			if(name.compareTo(from)!=0) {
//				listUser.get(name).serverSendMSG("2", msg);
//			}
//		}
		
	}
	
	public void updaListUser() {
//		this.jTListClient.selectAll();
//		this.jTListClient.replaceSelection("");
//		Enumeration e = listUser.keys();
//		this.jTListClient.append(listUser.size()+"\n");
//		while(e. hasMoreElements()){
//			User user = (User) e.nextElement();
//			this.jTListClient.append(user.getEmail()+"\n");
//			System.out.println(user.getEmail());
//		}
		
		this.jTListClient.selectAll();
		this.jTListClient.replaceSelection("");
		Enumeration e = listUser.keys();
		this.jTListClient.append(listUser.size()+"\n");
		while(e. hasMoreElements()){
			Socket socket = (Socket) e.nextElement();
			this.jTListClient.append(socket.getPort()+"\n");
		}
	}
	
	private void go() {		
		try {
			listUser = new Hashtable<Socket, SendClientThread>();
			serverSocket = new ServerSocket(8002);
			String p = String.valueOf(serverSocket.getLocalPort());
			jTLog.append("Server is Running \n");
			jTLog.append("PORT: "+p+"\n");
			while(true){
				Socket client = serverSocket.accept();
				jTLog.append("accept "+client.getPort()+" \n");
				new SendClientThread(this, client);
//				new ClientConnect(this, client);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		try {
//            Thread.sleep(300);
//        } catch (InterruptedException ie) {
//        	ie.printStackTrace();
//        }
	}
	
	
	public static void main(String[] args)
	{
		new MailServer().go();	
	}
}