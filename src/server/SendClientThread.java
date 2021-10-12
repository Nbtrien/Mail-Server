package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import core.DataPackage;
import core.Email;
import core.User;
import core.UserAuth;

public class SendClientThread extends Thread {
	public Socket client;
	public MailServer server;
	private String nickName;
	private ObjectOutputStream dos;
	private ObjectInputStream dis;
	private boolean run;
	
	public SendClientThread(MailServer server, Socket client){
		try {
			this.server = server;
			this.client = client;
			dos = new ObjectOutputStream(client.getOutputStream());
			dis = new ObjectInputStream (client.getInputStream());
			this.server.listUser.put(this.client, this);
			this.server.updaListUser();
			this.server.jTLog.append(client.getPort()+"\n");
			this.setName("thread "+client.getPort());
			run = true;
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
	// TODO Auto-generated method stub
	this.server.jTLog.append(this.getName()+"connected"+client.getPort()+"\n");
	
		while(run){
			DataPackage data = getData();
			int key = data.getKeyNum();
			switch (key) {
				case 0: {
		//				Register
					System.out.println("Register");
					UserAuth userRegister = data.getUserAuth();
					DataPackage dataPackage = new DataPackage(0);
					dataPackage.setStatus(this.server.register(userRegister));
					System.out.println(dataPackage.getKeyNum()+" > "+dataPackage.getStatus());
		//				server.sendData(dataPackage, cliAddress, cliPort);
					break;
				}
				case 1: {
		//				Login
					System.out.println("Login");
					UserAuth userLogin = data.getUserAuth();
					DataPackage dataPackage = new DataPackage(1);
					
					if(this.server.login(userLogin)) {
						dataPackage.setStatus(true);
						User user = server.database.getUser(userLogin.getEmail());
						dataPackage.setUser(user);
						this.server.jTLog.append(userLogin.getEmail()+" Login \n");
		//					this.server.listUser.put(this.client, this);
						this.server.updaListUser();
					} else {
						dataPackage.setStatus(false);
					}
					
					System.out.println(dataPackage.getKeyNum()+" > "+dataPackage.getStatus());
					this.server.sendData(this.client, dataPackage);
					break;
				}
				case 2: {
		//				User get List inbox
					DataPackage dataPackage = new DataPackage(2);
					User user = data.getUser();
					List<Email> eList = new ArrayList<>();
					Email newemail = new Email();
					for	(Email email : this.server.database.getListInbox(user.getId())) {
						eList.add(email);
						newemail = email;
					}
					dataPackage.seteList(eList);
		//				sendData(dataPackage);
					this.server.sendData(this.client, dataPackage);
					break;
				}
				case 3: {
		//				get Id Email
					Email email = this.server.database.getEmailbyId(data.getEmailId());
					DataPackage dataPackage = new DataPackage(3);
					dataPackage.setEmail(email);
					
		//				sendData(dataPackage);
					this.server.sendData(this.client, dataPackage);
					break;
				}
				case 4: {
		//				User send Email
					Email email = data.getEmail();
					System.out.println(email.toString());
					DataPackage dataPackage = new DataPackage(4);
					if (!this.server.database.checkUser(email.getUserTo().getEmail())) {
						dataPackage.setStatus(false);
					} else {
						User userFrom = email.getUserFrom();
						User userTo = this.server.database.getUser(email.getUserTo().getEmail());
						String userToPath = "f:/lập trình mạng/Mail Server/"+userTo.getEmail()+"/Inbox";
						File emailTo = new File(userToPath+"/"+userFrom.getUserName()+"-"+email.getTitle()+".txt");
					    try {
							if (emailTo.createNewFile()) {
							    System.out.println("File created: " + emailTo.getName());
							    FileWriter myWriter = new FileWriter(emailTo.getAbsoluteFile());
							    myWriter.write(email.getContent());
							    myWriter.close();
							    if (this.server.database.insertNewEmail(email.getTitle(), emailTo.getAbsolutePath().replace('\\', '/'), userFrom.getId() , userTo.getId())) {
							    	this.server.jTLog.append(userFrom.getEmail()+" send email to "+userTo.getEmail());
							    	dataPackage.setStatus(true);
								} else {
									dataPackage.setStatus(false);
								}
						    }
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
		//				sendData(dataPackage);
					this.server.sendData(this.client, dataPackage);
					break;
				}
				case 5: {
		//				User get list email sent
					DataPackage dataPackage = new DataPackage(5);
					User user = data.getUser();
					List<Email> eList = new ArrayList<>();
					for	(Email email : this.server.database.getListSend(user.getId())) {
						eList.add(email);
					}
					System.out.println("list size: "+eList.size());
					dataPackage.seteList(eList);
		//				sendData(dataPackage);
					this.server.sendData(this.client, dataPackage);
					break;
				}
				case 10: {
					run = false;
//					exit();
					break;
				}
			}
		}
	}
	
	private DataPackage getData(){
		DataPackage data = null;
		try {
			data = (DataPackage) this.dis.readObject();
			this.server.jTLog.append(this.getName()+"get "+data.getKeyNum()+" From: "+this.client.getPort()+"\n"+" data Local Port:"+ data.getLocalPort()+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public void sendData(DataPackage data){
		try {
			this.dos.writeObject(data);
			this.dos.flush();
			this.server.jTLog.append(this.getName()+"send "+data.getKeyNum()+" To: "+this.client.getPort()+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void exit(){
		try {
			this.dos.close();
			this.dis.close();
			this.client.close();
			System.out.println("EXIT");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}