package client;

import java.io.IOException;
import java.io.ObjectInputStream;

import core.DataPackage;
import core.DataProcessing;

public class DataStream extends Thread {
	private boolean run;
	private ObjectInputStream dis;
	private DataProcessing dataProcessing;

	public DataStream( DataProcessing dataProcessing, ObjectInputStream dis){
		run = true;
		this.dataProcessing = dataProcessing;
		this.dis = dis;
		this.start();
	}
	public void run(){
		DataPackage dataPackage = null;
		while(run){
			try {
				dataPackage = (DataPackage) dis.readObject();
				dataProcessing.getData(dataPackage);
			}catch (IOException e) {
				//e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void stopThread(){
		this.run = false;
	}
}