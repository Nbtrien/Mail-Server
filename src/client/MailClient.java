package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import core.DataPackage;
import core.DataProcessing;
import core.Email;
import core.MyScrollBarUI;
import core.User;

public class MailClient extends JFrame implements DataProcessing{
	
	private JLabel jLAccount;
	private Box boxLeft = Box.createVerticalBox();
	private Box boxRight = Box.createVerticalBox();
	private JPanel jPInbox, jPSend;
	private JTextField jTEmailTo, jTSubject;
	private JTextArea jTContent;
	private JFrame jFComposeEmail = null; 
	
	private User user;
	private static Socket client;
	private ObjectInputStream ois;
	private static ObjectOutputStream oos;
	private DataStream dataStream;
	
	public MailClient(User user, Socket client, ObjectInputStream ois, ObjectOutputStream oos) throws HeadlessException{
		super();
		this.user = user;
		this.client = client;
		this.oos = oos;
		this.ois = ois;
		dataStream = new DataStream(this, ois);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				exit();
			}
		});
		setLocation(50, 50);
		setTitle("Mail Client");
		setResizable(false);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS ));
		init();
		setVisible(true);
		run();
	}
	
	private void init() {
		JLabel jLTitle = new JLabel("Mail Client");
		jLTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		jLTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		jLTitle.setForeground(Color.blue);
		jLTitle.setBorder(new EmptyBorder(new Insets(10, 0, 10, 0))); 
		
		jLAccount = new JLabel();
		jLAccount.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 13));
		jLAccount.setForeground(Color.blue);
		jLAccount.setText("Account: "+user.getEmail());
		jLAccount.setBorder(new EmptyBorder(new Insets(0, 0, 10, 5))); 
		
		JPanel wrapper = new JPanel( new BorderLayout() );
		
		JPanel jP = new JPanel();
		jP.setLayout(new BoxLayout(jP, BoxLayout.Y_AXIS ));
		jP.setBorder(new EmptyBorder(new Insets(0, 15, 0, 10))); 
		
		JButton button = new JButton("Compose an email");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(jFComposeEmail == null) {
					jFComposeEmail = frameComposeEmail();
					jFComposeEmail.setVisible(true);
				}

			}
		});
		
		JButton jBRefresh = new JButton("Refresh");
		
		jBRefresh.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
		jBRefresh.setForeground(Color.BLUE);
		jBRefresh.setBorder(null);
		jBRefresh.setOpaque(false);
		jBRefresh.setContentAreaFilled(false);
		jBRefresh.setBorderPainted(false);
		jBRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				getInbox();
			}
		});
		
		jP.add(jLAccount);
		jP.add(button);
		jP.add(jBRefresh);
		
		wrapper.add(jP, BorderLayout.PAGE_START);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
		jPanel.setBorder(new EmptyBorder(new Insets(15, 10, 15, 10))); 

		JPanel jPLeft = new JPanel();
		jPLeft.setPreferredSize(new Dimension(500, 350));
		jPLeft.setMinimumSize(new Dimension(500, 350));
		jPLeft.setMaximumSize(new Dimension(500, 350));
		jPLeft.setLayout(new BoxLayout(jPLeft, BoxLayout.Y_AXIS));
		jPLeft.setBorder(BorderFactory.createTitledBorder("INBOX"));
		
		jPInbox = new JPanel();
		jPInbox.setLayout(new BorderLayout());
		jPInbox.setAutoscrolls(true);
		jPInbox.setEnabled(false);
		
		jPLeft.add(jPInbox);
		
		JPanel jPRight = new JPanel();
		jPRight.setPreferredSize(new Dimension(300, 350));
		jPRight.setMinimumSize(new Dimension(300, 350));
		jPRight.setMaximumSize(new Dimension(300, 350));
		jPRight.setLayout(new BoxLayout(jPRight, BoxLayout.Y_AXIS));
		jPRight.setBorder(BorderFactory.createTitledBorder("SEND"));
		
		jPSend = new JPanel();
		jPSend.setLayout(new BorderLayout());
		jPSend.setAutoscrolls(true);
		jPSend.setEnabled(false);
		
		jPRight.add(jPSend);
		
		jPanel.add(jPLeft);
		jPanel.add(Box.createHorizontalStrut(7));
		jPanel.add(jPRight);
		
		add(jLTitle);
		add(wrapper);
		add(jPanel);
		pack();
	}
	
	public JPanel formatLabelSend(String from, String title) {
		JPanel jP3 = new JPanel();
		jP3.setLayout(new BoxLayout(jP3, BoxLayout.X_AXIS));
		
		JLabel jLFrom = new JLabel(from);
		jLFrom.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		jLFrom.setOpaque(true);
		jLFrom.setBorder(new EmptyBorder(new Insets(5, 5, 5, 0)));
		jLFrom.setMinimumSize(new Dimension(150, 30));
		jLFrom.setPreferredSize(new Dimension(150, 30));
		jLFrom.setMaximumSize(new Dimension(150, 30));
		jLFrom.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel jLTitle = new JLabel(title);
		jLTitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		jLTitle.setOpaque(true);
		
		jP3.add(jLFrom);
		jP3.add(Box.createHorizontalStrut(26));
		jP3.add(jLTitle);
//		jP3.setBorder(BorderFactory.createLineBorder(new Color(4, 200, 151 )));
		jP3.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(4, 200, 151 )));
		return jP3;
	}
	
	private void run() {
		System.out.println("run");
		try {
    		getInbox();
    		getSend();
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		} 
	}
	
	private static void sendData(DataPackage data) {
		try {
			data.setLocalPort(client.getLocalPort());
			oos.writeObject(data);
			oos.flush();
			System.out.println("send: "+data.toString()+" "+client.getLocalPort());
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		} 
	}
	
	private void getInbox() {
		System.out.println("getInbox");
		DataPackage dataSend = new DataPackage(2);
		dataSend.setUser(user);
		sendData(dataSend);
	}
	
	private void getSend() {
		DataPackage dataSend = new DataPackage(5);
		dataSend.setUser(user);
		sendData(dataSend);
	}
	
	private static void getMail(int id) {
		DataPackage dataSend = new DataPackage(3);
		dataSend.setEmailId(id);
		sendData(dataSend);
	}
	
	public static JFrame frameReadEmail(String title, String userNameFrom, String userNameTo, String content) {
		JFrame jFrame = new JFrame();
		jFrame.setSize(450, 300);
		jFrame.setLocation(50, 50);
		jFrame.setResizable(false);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());
		
		JPanel jPHeader = new JPanel();
		jPHeader.setLayout(new BoxLayout(jPHeader,  BoxLayout.X_AXIS));
		jPHeader.setBorder(new EmptyBorder(5, 10, 5 , 10));
		
		JLabel jLTitle = new JLabel("Subject:");
		jLTitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLTitle.setMaximumSize(new Dimension(100, 30));
		jLTitle.setPreferredSize(new Dimension(50, 30));
		jLTitle.setMinimumSize(new Dimension(50, 30));
		
		JLabel jLSubject = new JLabel(title);
		jLSubject.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		
		jPHeader.add(jLTitle);
		jPHeader.add(jLSubject);
		
		JPanel jPFrom = new JPanel();
		jPFrom.setLayout(new BoxLayout(jPFrom,  BoxLayout.X_AXIS));
		jPFrom.setBorder(new EmptyBorder(0, 10, 0 , 10));
		
		JLabel jLTitleFrom = new JLabel("From: ");
		jLTitleFrom.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLTitleFrom.setMaximumSize(new Dimension(100, 30));
		jLTitleFrom.setPreferredSize(new Dimension(50, 30));
		jLTitleFrom.setMinimumSize(new Dimension(50, 30));
		
		JLabel jLFrom = new JLabel(userNameFrom);
		jLFrom.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		
		jPFrom.add(jLTitleFrom);
		jPFrom.add(jLFrom);
		
		JPanel jPTo = new JPanel();
		jPTo.setLayout(new BoxLayout(jPTo,  BoxLayout.X_AXIS));
		jPTo.setBorder(new EmptyBorder(0, 10, 0 , 10));
		
		JLabel jLTitleTo = new JLabel("To: ");
		jLTitleTo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLTitleTo.setMaximumSize(new Dimension(100, 30));
		jLTitleTo.setPreferredSize(new Dimension(50, 30));
		jLTitleTo.setMinimumSize(new Dimension(50, 30));
		
		JLabel jLTo = new JLabel(userNameTo);
		jLTo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		
		jPTo.add(jLTitleTo);
		jPTo.add(jLTo);
		
		JPanel jPContent= new JPanel();
		jPContent.setLayout(new GridLayout(1,0));

		jPContent.setBorder(new CompoundBorder(new EmptyBorder(new Insets(5, 5, 5, 10)), BorderFactory.createTitledBorder("Content")));
		
		JTextArea jTContent = new JTextArea(content);
		jTContent.setBorder(new EmptyBorder(new Insets(5, 5, 5, 10)));
		jTContent.setLineWrap(true);
		jTContent.setWrapStyleWord(true);
		
		JScrollPane jScrollPane = new JScrollPane(jTContent);
		jScrollPane.setMaximumSize(new Dimension(300, 200));
		jScrollPane.setMinimumSize(new Dimension(300, 200));
		jScrollPane.setPreferredSize(new Dimension(300, 200));
//		jScrollPane.setBorder(BorderFactory.createLineBorder(Color.black));

		jScrollPane.setComponentZOrder(jScrollPane.getVerticalScrollBar(), 0);
		jScrollPane.setComponentZOrder(jScrollPane.getViewport(), 1);
		jScrollPane.getVerticalScrollBar().setOpaque(false);
		
		jScrollPane.setLayout(new ScrollPaneLayout() {
		      @Override
		      public void layoutContainer(Container parent) {
		        JScrollPane jScrollPane = (JScrollPane) parent;

		        Rectangle availR = jScrollPane.getBounds();
		        availR.x = availR.y = 0;

		        Insets parentInsets = parent.getInsets();
		        availR.x = parentInsets.left;
		        availR.y = parentInsets.top;
		        availR.width -= parentInsets.left + parentInsets.right;
		        availR.height -= parentInsets.top + parentInsets.bottom;

		        Rectangle vsbR = new Rectangle();
		        vsbR.width = 9;
		        vsbR.height = availR.height;
		        vsbR.x = availR.x + availR.width - vsbR.width;
		        vsbR.y = availR.y;

		        if (viewport != null) {
		          viewport.setBounds(availR);
		        }
		        if (vsb != null) {
		          vsb.setVisible(true);
		          vsb.setBounds(vsbR);
		        }
		      }
		    });
		jScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
		
		jPContent.add(jScrollPane);
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel.add(jPHeader, gbc);
        gbc.gridy++;
		jPanel.add(jPFrom, gbc);
	      gbc.gridy++;
		jPanel.add(jPTo, gbc);
		gbc.gridy++;
		gbc.weighty = 1;
		jPanel.add(jPContent, gbc);
		
		jFrame.add(jPanel);

		return jFrame;
		
	}
	
	public JFrame frameComposeEmail() {
		JFrame jFrame = new JFrame();
		jFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				jFrame.dispose();
				jFComposeEmail = null;
			}
		});
		jFrame.setSize(450, 400);
		jFrame.setLocation(50, 50);
		jFrame.setResizable(false);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());
		
		JLabel jLTitle = new JLabel("New Message", SwingConstants.CENTER);
		jLTitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
		
		JPanel jPTo = new JPanel();
		jPTo.setLayout(new BoxLayout(jPTo,  BoxLayout.X_AXIS));
		jPTo.setBorder(new EmptyBorder(5, 10, 5 , 10));
		
		JLabel jLTo = new JLabel("To:");
		jLTo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLTo.setMaximumSize(new Dimension(80, 20));
		jLTo.setPreferredSize(new Dimension(80, 20));
		jLTo.setMinimumSize(new Dimension(80, 20));
		
		jTEmailTo = new JTextField();
		jTEmailTo.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		jTEmailTo.setBorder(BorderFactory.createLineBorder(Color.black));
		
		jPTo.add(jLTo);
		jPTo.add(jTEmailTo);
		
		JPanel jPSubject = new JPanel();
		jPSubject.setLayout(new BoxLayout(jPSubject,  BoxLayout.X_AXIS));
		jPSubject.setBorder(new EmptyBorder(5, 10, 5 , 10));
		
		JLabel jLSubject = new JLabel("Subject:");
		jLSubject.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		jLSubject.setMaximumSize(new Dimension(80, 20));
		jLSubject.setPreferredSize(new Dimension(80, 20));
		jLSubject.setMinimumSize(new Dimension(80, 20));
		
		jTSubject = new JTextField();
		jTSubject.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		jTSubject.setBorder(BorderFactory.createLineBorder(Color.black));
		
		jPSubject.add(jLSubject);
		jPSubject.add(jTSubject);
		
		JPanel jPContent= new JPanel();
		jPContent.setLayout(new GridLayout(1,0));

		jPContent.setBorder(new CompoundBorder(new EmptyBorder(new Insets(5, 5, 5, 10)), BorderFactory.createTitledBorder("Content")));
		
		jTContent = new JTextArea();
		jTContent.setBorder(new EmptyBorder(new Insets(5, 5, 5, 10)));
		jTContent.setLineWrap(true);
		jTContent.setWrapStyleWord(true);
		
		JScrollPane jScrollPane = new JScrollPane(jTContent);
		jScrollPane.setMaximumSize(new Dimension(300, 175));
		jScrollPane.setMinimumSize(new Dimension(300, 175));
		jScrollPane.setPreferredSize(new Dimension(300, 175));
//		jScrollPane.setBorder(BorderFactory.createLineBorder(Color.black));

		jScrollPane.setComponentZOrder(jScrollPane.getVerticalScrollBar(), 0);
		jScrollPane.setComponentZOrder(jScrollPane.getViewport(), 1);
		jScrollPane.getVerticalScrollBar().setOpaque(false);
		
		jScrollPane.setLayout(new ScrollPaneLayout() {
		      @Override
		      public void layoutContainer(Container parent) {
		        JScrollPane jScrollPane = (JScrollPane) parent;

		        Rectangle availR = jScrollPane.getBounds();
		        availR.x = availR.y = 0;

		        Insets parentInsets = parent.getInsets();
		        availR.x = parentInsets.left;
		        availR.y = parentInsets.top;
		        availR.width -= parentInsets.left + parentInsets.right;
		        availR.height -= parentInsets.top + parentInsets.bottom;

		        Rectangle vsbR = new Rectangle();
		        vsbR.width = 9;
		        vsbR.height = availR.height;
		        vsbR.x = availR.x + availR.width - vsbR.width;
		        vsbR.y = availR.y;

		        if (viewport != null) {
		          viewport.setBounds(availR);
		        }
		        if (vsb != null) {
		          vsb.setVisible(true);
		          vsb.setBounds(vsbR);
		        }
		      }
		    });
		jScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
		
		jPContent.add(jScrollPane);
		
		JPanel jpButton = new JPanel();
		jpButton.setLayout(new BoxLayout(jpButton, BoxLayout.Y_AXIS));
		
		JButton jBSend = new JButton("Send");
		jBSend.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		jBSend.setMaximumSize(new Dimension(75, 30));
		jBSend.setPreferredSize(new Dimension(75, 30));
		jBSend.setMinimumSize(new Dimension(75, 30));
		jBSend.setAlignmentX(Component.CENTER_ALIGNMENT);
		jBSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendEmail();
			}
		});
		jpButton.add(jBSend);
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel.add(jLTitle, gbc);
        gbc.gridy++;
        jPanel.add(jPTo, gbc);
        gbc.gridy++;
        jPanel.add(jPSubject, gbc);
        gbc.gridy++;
        jPanel.add(jPContent, gbc);
        gbc.gridy++;
        jPanel.add(jpButton, gbc);
        
		jFrame.add(jPanel);
		
		return jFrame;
	}
	
	private void sendEmail() {
		String emailTo = jTEmailTo.getText();
		String emailSubject = jTSubject.getText();
		String emailContent = jTContent.getText();
		if (emailTo.equals("") || emailSubject.equals("") || emailContent.equals("")) {
			JOptionPane.showMessageDialog(this, "Please Enter Somethings");
		} else {
			DataPackage dataPackage = new DataPackage(4);
			Email email = new Email();
			email.setTitle(emailSubject);
			email.setUserFrom(user);
			email.setContent(emailContent);
			User userTo = new User();
			userTo.setEmail(emailTo);
			email.setUserTo(userTo);
			dataPackage.setEmail(email);
			sendData(dataPackage);
		}
		
	}

	private void exit(){
		try {
			DataPackage dataPackage = new DataPackage(10);
			sendData(dataPackage);
			oos.close();
			ois.close();
			client.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	public void getData(DataPackage data) {
		int key = data.getKeyNum();
		// TODO Auto-generated method stub
		switch (key) {
			case 2: {
	//			receive list inbox
				this.boxLeft.removeAll();
				this.boxLeft.revalidate();;
				this.boxLeft.repaint();
				this.jPInbox.removeAll();
				this.jPInbox.revalidate();
				this.jPInbox.repaint();
				System.out.println("get Inbox");
				List<Email> eList = data.geteList();
				for (Email email : eList) {
					System.out.println(email.toString());
					JPanel jP2 = formatLabelSend(email.getUserFrom().getUserName(), email.getTitle());
					JPanel rightJPanel = new JPanel(new BorderLayout());
					rightJPanel.add(jP2);
					rightJPanel.setName(email.getId()+"");
					rightJPanel.addMouseListener(getMyMouseListener());
					boxLeft.add(rightJPanel);
					this.jPInbox.add(boxLeft, BorderLayout.PAGE_START);
					this.jPInbox.revalidate();
				}
				break;
			}
			case 3: {
//				receive email content
				Email email = data.getEmail();
				JFrame jFPreviewFrame = frameReadEmail(email.getTitle(), email.getUserFrom().getEmail(), email.getUserTo().getEmail(), email.getContent());
				jFPreviewFrame.setVisible(true);
				break;
			}
			case 4:{
//				receive send email status
//				JOptionPane.showMessageDialog(this, data.getStatus());
				if (data.getStatus()) {
					if(jFComposeEmail.isVisible()) {
						jFComposeEmail.setVisible(false);
						jFComposeEmail = null;
					}
					getSend();
				} else {
					JOptionPane.showMessageDialog(this, "Failed!!!");
				}
				break;
			}
			case 5: {
				this.boxRight.removeAll();
				this.boxRight.revalidate();;
				this.boxRight.repaint();
				this.jPSend.removeAll();
				this.jPSend.revalidate();
				this.jPSend.repaint();
//				receive list send
				System.out.println("get Send");
				List<Email> eList = data.geteList();
				System.out.println("list size: "+eList.size());
				for (Email email : eList) {
					System.out.println(email.toString());
					JPanel jP2 = formatLabelSend(email.getUserFrom().getUserName(), email.getTitle());
					JPanel rightJPanel = new JPanel(new BorderLayout());
					rightJPanel.add(jP2);
					rightJPanel.setName(email.getId()+"");
					rightJPanel.addMouseListener(getMyMouseListener());
					boxRight.add(rightJPanel);
					this.jPSend.add(boxRight, BorderLayout.PAGE_START);
					this.jPSend.revalidate();
				}
				break;
			}
			default: break;
		}

//		if (key == 2) {
////			receive list inbox
//			this.boxLeft.removeAll();
//			this.boxLeft.revalidate();;
//			this.boxLeft.repaint();
//			this.jPInbox.removeAll();
//			this.jPInbox.revalidate();
//			this.jPInbox.repaint();
//			System.out.println("get Inbox");
//			List<Email> eList = data.geteList();
//			for (Email email : eList) {
//				System.out.println(email.toString());
//				JPanel jP2 = formatLabelSend(email.getUserFrom().getUserName(), email.getTitle());
//				JPanel rightJPanel = new JPanel(new BorderLayout());
//				rightJPanel.add(jP2);
//				rightJPanel.setName(email.getId()+"");
//				rightJPanel.addMouseListener(getMyMouseListener());
//				boxLeft.add(rightJPanel);
//				this.jPInbox.add(boxLeft, BorderLayout.PAGE_START);
//				this.jPInbox.revalidate();
//			}
//		} else if (key == 3) {
////			receive email content
//			Email email = data.getEmail();
//			JFrame jFPreviewFrame = frameReadEmail(email.getTitle(), email.getUserFrom().getEmail(), email.getUserTo().getEmail(), email.getContent());
//			jFPreviewFrame.setVisible(true);
//
//		} else if (key == 4) {
////			receive send email status
////			JOptionPane.showMessageDialog(this, data.getStatus());
//			if (data.getStatus()) {
//				if(jFComposeEmail.isVisible()) {
//					jFComposeEmail.setVisible(false);
//					jFComposeEmail = null;
//				}
//				getSend();
//			} else {
//				JOptionPane.showMessageDialog(this, "Failed!!!");
//			}
//		} else if (key == 5) {
//			this.boxRight.removeAll();
//			this.boxRight.revalidate();;
//			this.boxRight.repaint();
//			this.jPSend.removeAll();
//			this.jPSend.revalidate();
//			this.jPSend.repaint();
////			receive list send
//			System.out.println("get Send");
//			List<Email> eList = data.geteList();
//			System.out.println("list size: "+eList.size());
//			for (Email email : eList) {
//				System.out.println(email.toString());
//				JPanel jP2 = formatLabelSend(email.getUserFrom().getUserName(), email.getTitle());
//				JPanel rightJPanel = new JPanel(new BorderLayout());
//				rightJPanel.add(jP2);
//				rightJPanel.setName(email.getId()+"");
//				rightJPanel.addMouseListener(getMyMouseListener());
//				boxRight.add(rightJPanel);
//				this.jPSend.add(boxRight, BorderLayout.PAGE_START);
//				this.jPSend.revalidate();
//			}
//		}
	}
	
	public static MouseListener getMyMouseListener() {
		return new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				JPanel panel = (JPanel) e.getSource();
				
				int emailId = Integer.parseInt(panel.getName());
				getMail(emailId);
//				System.out.println(emailId);
//				JFrame jFPreviewFrame = createFrame(fileId, fileId, fileId);
			}
		};
	}

}
