package client;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import core.DataPackage;
import core.DataProcessing;
import core.UserAuth;

public class Register extends JFrame implements ActionListener, DataProcessing{

	private JButton jBRegister;
	private JTextField jTName, jTEmail, jTEmailDomain;
	private JPasswordField jPassword;
	
//	private static DatagramSocket socket;
//	private static DatagramPacket inPkt, outPkt;
//	private static byte[] buff;
//	private static String msg = "", msgIn = "";
//	private static final int PORT = 4321;
//	private static InetAddress host;
	private Socket client;
	private ObjectOutputStream  oos;
	private ObjectInputStream ois;
	private DataStream dataStream2;
	
	public Register(Socket client) {
		this.client = client;
		System.out.println(client.getPort());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(50, 50);
		setTitle("Register Form");
		setResizable(false);
		init();
		setVisible(true);
		try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void init() {
		System.out.println("init");
		JPanel panel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxlayout);
        panel.setBorder(new EmptyBorder(new Insets(20, 50, 20, 50)));
		
		JPanel jPSignForm = new JPanel();
		jPSignForm.setLayout(new BoxLayout(jPSignForm, BoxLayout.Y_AXIS));
		jPSignForm.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPSignForm.setBorder(new EmptyBorder(new Insets(25, 20, 25, 20)));
		
		JLabel jLRegister = new JLabel("Register");
		jLRegister .setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		jLRegister .setAlignmentX(Component.CENTER_ALIGNMENT);
		jLRegister .setBorder(new EmptyBorder(new Insets(0, 0, 10, 0)));
		
		JPanel jPUserName = new JPanel();
		jPUserName.setLayout(new BoxLayout(jPUserName, BoxLayout.X_AXIS));
		jPUserName.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPUserName.setBorder(new EmptyBorder(new Insets(10, 0, 10, 0)));
		
		JLabel jLName = new JLabel("Name: ");
		jLName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLName.setAlignmentY(Component.CENTER_ALIGNMENT);
		jLName.setPreferredSize(new Dimension(80,20));
		
		jTName = new JTextField();
		jTName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jTName.setAlignmentY(Component.CENTER_ALIGNMENT);
		jTName.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		jTName.setForeground(new Color(112,112,112));
		jTName.setPreferredSize(new Dimension(250,20));
		
		jPUserName.add(jLName);
		jPUserName.add(jTName);
		
		JPanel jPEmail = new JPanel();
		jPEmail.setLayout(new BoxLayout(jPEmail, BoxLayout.X_AXIS));
		jPEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPEmail.setBorder(new EmptyBorder(new Insets(10, 0, 10, 0)));
		
		JLabel jLEmail = new JLabel("Email: ");
		jLEmail.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLEmail.setAlignmentY(Component.CENTER_ALIGNMENT);
		jLEmail.setPreferredSize(new Dimension(80,20));
		
		jTEmail = new JTextField();
		jTEmail.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jTEmail.setAlignmentY(Component.CENTER_ALIGNMENT);
		jTEmail.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		jTEmail.setForeground(new Color(112,112,112));
		jTEmail.setPreferredSize(new Dimension(150,20));
		
		jTEmail.setMaximumSize(new Dimension(150, 20));
		
		jTEmailDomain = new JTextField();
		jTEmailDomain.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jTEmailDomain.setAlignmentY(Component.CENTER_ALIGNMENT);
		jTEmailDomain.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		jTEmailDomain.setAlignmentX(RIGHT_ALIGNMENT);
		jTEmailDomain.setText("@gmail.com");
		jTEmailDomain.setDisabledTextColor(new Color(112,112,112));;
		jTEmailDomain.setEnabled(false);
		
		jPEmail.add(jLEmail);
		jPEmail.add(jTEmail);
		jPEmail.add(Box.createHorizontalStrut(5));
		jPEmail.add(jTEmailDomain);
		
		JPanel jPPassWord = new JPanel();
		jPPassWord.setLayout(new BoxLayout(jPPassWord, BoxLayout.X_AXIS));
		jPPassWord.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPPassWord.setBorder(new EmptyBorder(new Insets(10, 0, 15, 0)));
		
		JLabel jLPassWord = new JLabel("PassWord: ");
		jLPassWord.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLPassWord.setAlignmentY(Component.CENTER_ALIGNMENT);
		jLPassWord.setPreferredSize(new Dimension(80,20));
		
		jPassword = new JPasswordField();
		jPassword.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jPassword.setAlignmentY(Component.CENTER_ALIGNMENT);
		jPassword.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		
		jPPassWord.add(jLPassWord);
		jPPassWord.add(jPassword);
		
		jPSignForm.add(jLRegister );
		jPSignForm.add(jPUserName);
		jPSignForm.add(jPEmail);
		jPSignForm.add(jPPassWord);
		
		jBRegister = new JButton("Register");
		jBRegister.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		jBRegister.setAlignmentX(CENTER_ALIGNMENT);
		jBRegister.addActionListener(this);
		
		jPSignForm.add(jBRegister);		
		
		panel.add(jPSignForm);
		
		add(panel);
		pack();
	}
	
    public void run() throws IOException {
    	System.out.println("run");
    			ois = new ObjectInputStream(client.getInputStream());
    			dataStream2 = new DataStream(this, ois);
    		oos = new ObjectOutputStream (client.getOutputStream());
    }
	
	private void sendData(DataPackage data) {
		try {
			oos.writeObject(data);
			oos.flush();
			System.out.println(data.toString());
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		} 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jBRegister) {
			if (!(jTName.getText().equals("")) && !(jTEmail.getText().equals("")) && !(jPassword.getText().equals(""))) {
				UserAuth user = new UserAuth(jTName.getText(), jTEmail.getText()+jTEmailDomain.getText(), jPassword.getText());
				DataPackage dataSend = new DataPackage(0);
				dataSend.setUserAuth(user);
				System.out.println(jTName.getText());
				System.out.println(jTEmail.getText());
				System.out.println(jPassword.getText());
				
				sendData(dataSend);
				
			} else {
				JOptionPane.showMessageDialog(this, "Please Enter Your Infor!");
			}
		}
		
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		new Register();
//	}

	@Override
	public void getData(DataPackage data) {
		// TODO Auto-generated method stub
		if (data.getKeyNum() == 0) {
			if(!data.getStatus()) {
				JOptionPane.showMessageDialog(this, "Registration failed");
//				JOptionPane.showMessageDialog(this, data.getStatus());
			} else {
				this.dispose();
				new Login();
			}
		}
		
	}

}
