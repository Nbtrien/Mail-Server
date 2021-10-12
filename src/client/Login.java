package client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import core.User;
import core.UserAuth;

public class Login extends JFrame implements ActionListener, DataProcessing{
	private JButton jBLogin, jBSignUp;
	private JTextField jTEmail;
	private JPasswordField jPassword;
	
	private JTextField jTNameRegis, jTEmailRegis, jTEmailDomain;
	private JPasswordField jPasswordRegis;
	private JButton jBRegister;
	
	private static final int PORT = 8002;
	private Socket client;
	private ObjectOutputStream  dos;
	private ObjectInputStream dis;
	private DataStream dataStream;
	
	public Login() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				exit();
			}
		});
		setLocation(50, 50);
		setTitle("Login");
		setResizable(false);
		init();
		setVisible(true);
	}
	
	private void init() {
		JPanel panel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxlayout);
        panel.setBorder(new EmptyBorder(new Insets(20, 50, 20, 50)));
		
		JPanel jPSignForm = new JPanel();
		jPSignForm.setLayout(new BoxLayout(jPSignForm, BoxLayout.Y_AXIS));
		jPSignForm.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPSignForm.setBorder(new EmptyBorder(new Insets(25, 20, 25, 20)));
		
		JLabel jLSignIn= new JLabel("Login");
		jLSignIn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		jLSignIn.setAlignmentX(Component.CENTER_ALIGNMENT);
		jLSignIn.setBorder(new EmptyBorder(new Insets(0, 0, 10, 0)));
		
		JPanel jPEmail = new JPanel();
		jPEmail.setLayout(new BoxLayout(jPEmail, BoxLayout.X_AXIS));
		jPEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPEmail.setBorder(new EmptyBorder(new Insets(15, 0, 15, 0)));
		
		JLabel jLEmail = new JLabel("Email: ");
		jLEmail.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLEmail.setAlignmentY(Component.CENTER_ALIGNMENT);
		jLEmail.setPreferredSize(new Dimension(80,20));
		
		jTEmail = new JTextField();
		jTEmail.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		jTEmail.setAlignmentY(Component.CENTER_ALIGNMENT);
		jTEmail.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		jTEmail.setForeground(new Color(112,112,112));
		jTEmail.setPreferredSize(new Dimension(200,20));
		
		jPEmail.add(jLEmail);
		jPEmail.add(jTEmail);
		
		JPanel jPPassWord = new JPanel();
		jPPassWord.setLayout(new BoxLayout(jPPassWord, BoxLayout.X_AXIS));
		jPPassWord.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPPassWord.setBorder(new EmptyBorder(new Insets(15, 0, 15, 0)));
		
		JLabel jLPassWord = new JLabel("PassWord: ");
		jLPassWord.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLPassWord.setAlignmentY(Component.CENTER_ALIGNMENT);
		jLPassWord.setPreferredSize(new Dimension(80,20));
		
		jPassword = new JPasswordField();
		jPassword.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		jPassword.setAlignmentY(Component.CENTER_ALIGNMENT);
		jPassword.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		
		jPPassWord.add(jLPassWord);
		jPPassWord.add(jPassword);
		
		jPSignForm.add(jLSignIn);
		jPSignForm.add(jPEmail);
		jPSignForm.add(jPPassWord);
		
		jBLogin = new JButton("Login");
		jBLogin.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		jBLogin.setAlignmentX(CENTER_ALIGNMENT);
		jBLogin.addActionListener(this);
		jPSignForm.add(jBLogin);		
		
		jBSignUp = new JButton("Create new account");
		jBSignUp.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 13));
		jBSignUp.setForeground(Color.BLUE);
		jBSignUp.setOpaque(false);
		jBSignUp.setContentAreaFilled(false);
		jBSignUp.setBorderPainted(false);
		jBSignUp.addActionListener(this);
		
		JPanel jPSignUp = new JPanel(new BorderLayout());
		jPSignUp.add(jBSignUp, BorderLayout.LINE_END);
		
		panel.add(jPSignForm);

		panel.add(jPSignUp);
		
		add(panel);
		pack();
	}
	
	private JPanel createFrameRegister() {
		
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
		
		jTNameRegis = new JTextField();
		jTNameRegis.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jTNameRegis.setAlignmentY(Component.CENTER_ALIGNMENT);
		jTNameRegis.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		jTNameRegis.setForeground(new Color(112,112,112));
		jTNameRegis.setPreferredSize(new Dimension(250,20));
		
		jPUserName.add(jLName);
		jPUserName.add(jTNameRegis);
		
		JPanel jPEmail = new JPanel();
		jPEmail.setLayout(new BoxLayout(jPEmail, BoxLayout.X_AXIS));
		jPEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPEmail.setBorder(new EmptyBorder(new Insets(10, 0, 10, 0)));
		
		JLabel jLEmail = new JLabel("Email: ");
		jLEmail.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLEmail.setAlignmentY(Component.CENTER_ALIGNMENT);
		jLEmail.setPreferredSize(new Dimension(80,20));
		
		jTEmailRegis = new JTextField();
		jTEmailRegis.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jTEmailRegis.setAlignmentY(Component.CENTER_ALIGNMENT);
		jTEmailRegis.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		jTEmailRegis.setForeground(new Color(112,112,112));
		jTEmailRegis.setPreferredSize(new Dimension(150,20));
		
		jTEmailRegis.setMaximumSize(new Dimension(150, 20));
		
		jTEmailDomain = new JTextField();
		jTEmailDomain.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jTEmailDomain.setAlignmentY(Component.CENTER_ALIGNMENT);
		jTEmailDomain.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		jTEmailDomain.setAlignmentX(RIGHT_ALIGNMENT);
		jTEmailDomain.setText("@gmail.com");
		jTEmailDomain.setDisabledTextColor(new Color(112,112,112));;
		jTEmailDomain.setEnabled(false);
		
		jPEmail.add(jLEmail);
		jPEmail.add(jTEmailRegis);
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
		
		jPasswordRegis = new JPasswordField();
		jPasswordRegis.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jPasswordRegis.setAlignmentY(Component.CENTER_ALIGNMENT);
		jPasswordRegis.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		
		jPPassWord.add(jLPassWord);
		jPPassWord.add(jPasswordRegis);
		
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
		
		return panel;
	}
	
	private void run() {
		try {
			client = new Socket("localhost", 8002);
			dos = new ObjectOutputStream (client.getOutputStream());
			dis = new ObjectInputStream(client.getInputStream());
			//client.close();
			dataStream = new DataStream(this, dis);
        } catch (Exception e) {
			JOptionPane.showMessageDialog(this,"Fail","Message Dialog",JOptionPane.WARNING_MESSAGE);
			System.exit(0);
	}
	}
	
	private void sendData(DataPackage data) {
		try {
			data.setLocalPort(client.getLocalPort());
			dos.writeObject(data);
			dos.flush();
			System.out.println(data.toString());
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		} 
	}

	private void exit(){
		try {
			DataPackage dataPackage = new DataPackage(10);
			sendData(dataPackage);
			dos.close();
			dis.close();
			client.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jBSignUp) {
			setVisible(false);
			JPanel jPRegis = createFrameRegister();
			getContentPane().removeAll();
			add(jPRegis);
			repaint();
			pack();
			setVisible(true);
		} else if (e.getSource() == jBLogin) {
			if ( jTEmail.getText().equals("") || jPassword.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Please Enter Your Infor!");
			} else {
				String email = jTEmail.getText();
				String passWord = jPassword.getText();
				UserAuth user = new UserAuth("", email, passWord);
				DataPackage dataSend = new DataPackage(1);
				dataSend.setUserAuth(user);
				sendData(dataSend);
			}
		} else if (e.getSource() == jBRegister) {
			if (!(jTNameRegis.getText().equals("")) && !(jTEmailRegis.getText().equals("")) && !(jPasswordRegis.getText().equals(""))) {
				UserAuth user = new UserAuth(jTNameRegis.getText(), jTEmailRegis.getText()+jTEmailDomain.getText(), jPasswordRegis.getText());
				DataPackage dataSend = new DataPackage(0);
				dataSend.setUserAuth(user);
				System.out.println(jTNameRegis.getText());
				System.out.println(jTEmailRegis.getText());
				System.out.println(jPasswordRegis.getText());
				
				sendData(dataSend);
				
			} else {
				JOptionPane.showMessageDialog(this, "Please Enter Your Infor!"+jTNameRegis.getText()+jTEmailRegis.getText()+jPasswordRegis.getText());
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Login().run();
	}
	
	@Override
	public void getData(DataPackage data) {
		// TODO Auto-generated method stub
		if (data.getKeyNum() == 0) {
//			Register
			if(!data.getStatus()) {
				JOptionPane.showMessageDialog(this, "Registration failed");
			} else {
				getContentPane().removeAll();
				repaint();
				init();
				pack();
				setVisible(true);
			}
		} else
		if (data.getKeyNum() == 1) {
//			Login
			System.out.println("login: "+data.getStatus());
			if(data.getStatus()) {
				User user = data.getUser();
				System.out.println(user.toString());
				dataStream.stopThread();
				this.dispose();
				new MailClient(user, this.client, this.dis, this.dos);
			} else {
				JOptionPane.showMessageDialog(this, "Email or Password is incorrect");
			}
		}
	}

}

