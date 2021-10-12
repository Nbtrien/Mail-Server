package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import core.DataPackage;

public class ClientConnect extends Thread {
	public Socket client;
	public MailServer server;
	private String nickName;
	private ObjectOutputStream dos;
	private ObjectInputStream dis;
	private boolean run;
	ObjectOutputStream oos;
    ObjectInputStream ois;
    int i=1;
	public ClientConnect(MailServer server, Socket client){
		try {
			this.server=server;
			this.client=client;
			dos = new ObjectOutputStream(client.getOutputStream());
			dis = new ObjectInputStream(client.getInputStream());
			this.setName("thread "+client.getPort());
			run=true;
			this.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run(){
		while(run) {
			getMSG();
		}
		
	}
//	private void logout() {
//		try {
//			dos.close();
//			dis.close();
//			client.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	private boolean checkNick(String nick){
//		return server.listUser.containsKey(nick);
//	}
//	
//	private void sendMSG(String data){
//		try {
//			dos.writeUTF(data);
//			dos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void serverSendMSG(String msg1,String msg2){
//		sendMSG(msg1);
//		sendMSG(msg2);
//	}
	private DataPackage getMSG(){
		DataPackage data = null;
		try {
			try {
				data = (DataPackage) dis.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.server.jTLog.append(this.getName()+"get "+data.getKeyNum()+" From: "+this.client.getPort()+"\n"+" data Local Port:"+ data.getLocalPort()+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
}