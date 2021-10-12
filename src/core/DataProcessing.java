package core;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public interface DataProcessing {
	
	public void getData(DataPackage dataPackage);
	
	default public void sendData(DatagramSocket socket, byte[] data, DatagramPacket outPkt, InetAddress host, int PORT) {
		try {
	    	outPkt = new DatagramPacket(data,data.length, host, PORT);
			socket.send(outPkt);
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		} 
	}
}
