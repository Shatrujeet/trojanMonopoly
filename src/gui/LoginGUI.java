package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class LoginGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel welcomeLabel;
	private JLabel descriptionLabel;
	
	private JLabel errorLabel;
	
	private JPanel usernamePanel;
	private JLabel usernameLabel;
	private JTextField usernameTextField;
	private String username;
	
	private JPanel passwordPanel;
	private JLabel passwordLabel;
	private JTextField passwordTextField;
	private String password;
	
	private JPanel captchaPanel;
	private JTextField captchaTextField;
	
	private JPanel buttonPanel;
	private JButton guestLoginButton;
	private JButton loginButton;
	private JButton createAccountButton;
	int randomNum = -1;
	
	
	public LoginGUI()
	{
		super("Monopoly");
		initalizeComponents();
		createGUI();
		addEvents();
	}
	
	private void initalizeComponents() {
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		getContentPane().setBackground(Color.decode("#CEE6D1"));
		
		//initalizes the welcome label
		welcomeLabel = new JLabel("Welcome to Monopoly!");
		welcomeLabel.setFont(Constants.largeFont);
		welcomeLabel.setForeground(Color.BLACK);
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//initalizes the description label
		descriptionLabel = new JLabel("Please login, create account, or play as a guest.");
		descriptionLabel.setForeground(Color.BLACK);
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		descriptionLabel.setFont(Constants.mediumFont);
		
		errorLabel = new JLabel(" ");
		errorLabel.setForeground(Color.RED);
		errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		errorLabel.setFont(Constants.mediumFont);
		
		//initalizes the username info
		usernamePanel = new JPanel();
		
		usernamePanel.setBackground(Color.decode("#CEE6D1"));
		
		usernameLabel = new JLabel("Email:");
		usernameLabel.setFont(Constants.mediumFont);
		usernameLabel.setForeground(Color.BLACK);
		usernameTextField = new JTextField(20);
		username = "";
		
		//initalizes the password info
		passwordPanel = new JPanel();
		
		passwordPanel.setBackground(Color.decode("#CEE6D1"));
		
		passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(Constants.mediumFont);
		passwordLabel.setForeground(Color.BLACK);
		passwordTextField = new JTextField(20);
		password = "";
		
		captchaPanel = new JPanel();
		captchaPanel.setBackground(Color.decode("#CEE6D1"));
		captchaTextField = new JTextField(20);
		
		
		buttonPanel = new JPanel();	
		buttonPanel.setBackground(Color.decode("#CEE6D1"));
		
		guestLoginButton = new JButton("Login as Guest");
		guestLoginButton.setFont(Constants.smallFont);
		guestLoginButton.setForeground(Color.BLACK);
		
		loginButton = new JButton("Login");
		loginButton.setFont(Constants.smallFont);
		loginButton.setEnabled(false);
		loginButton.setForeground(Color.BLACK);
		
		createAccountButton = new JButton("Create Account");
		createAccountButton.setFont(Constants.smallFont);
		createAccountButton.setEnabled(false);
		createAccountButton.setForeground(Color.BLACK);
		
	}
	
	private void createGUI() {
		setSize(600, 400);
		setMaximumSize(new Dimension(600, 400));
		setMinimumSize(new Dimension(600, 400));
		
		
		add(welcomeLabel);
		add(Box.createGlue());
		add(descriptionLabel);
		add(Box.createGlue());
		add(errorLabel);
		
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameTextField);
		add(usernamePanel);
		
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordTextField);
		add(passwordPanel);
		
		JLabel temp = new JLabel();
		Random num = new Random();
		randomNum = num.nextInt(3);
		System.out.println("randomNum = " + randomNum);
		ImageIcon i = Constants.captchas[randomNum];
		temp.setIcon(i);
		captchaPanel.add(temp);
		captchaPanel.add(captchaTextField);
		add(captchaPanel);
		
		
		buttonPanel.add(guestLoginButton);
		buttonPanel.add(loginButton);
		buttonPanel.add(createAccountButton);
		add(buttonPanel);
		
		
	}
	
	private void addEvents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		usernameTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				username = usernameTextField.getText();
				if(!username.equals("") && !username.contains(" ") 
						&& !password.equals("") && !password.contains(" "))
				{
					loginButton.setEnabled(true);
					createAccountButton.setEnabled(true);
				} else {
					loginButton.setEnabled(false);
					createAccountButton.setEnabled(false);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		passwordTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				password = passwordTextField.getText();
				if(!username.equals("") && !username.contains(" ") 
						&& !password.equals("") && !password.contains(" "))
				{
					loginButton.setEnabled(true);
					createAccountButton.setEnabled(true);
				} else {
					loginButton.setEnabled(false);
					createAccountButton.setEnabled(false);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		guestLoginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Account a = new Account();
				JoinHostGUI jh = new JoinHostGUI(a);
				setVisible(false);
				jh.setVisible(true);
				//a.close();
			}
			
		});
	
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Account a = new Account(usernameTextField.getText(), passwordTextField.getText());
				//System.out.println();
				if(!a.inAccount() )
				{
					errorLabel.setText("This username/password combo does not exist.");
					a.close();
				} else if (captchaTextField.getText().equals("morning overlooks") || captchaTextField.getText().equals("prens entsTo") ||
						captchaTextField.getText().equals("contend because")) {
					JoinHostGUI jh = new JoinHostGUI(a);
					setVisible(false);
					jh.setVisible(true);
					
				} else {
					
					errorLabel.setText("Incorrect captcha");
					a.close();
				}				

			}
			
		});
		
		createAccountButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EmailValidator validator = new EmailValidator();
				if(!validator.validate(usernameTextField.getText()))
				{
					errorLabel.setText("Please enter an email address.");
				} else if (captchaTextField.getText().equals("morning overlooks") || captchaTextField.getText().equals("prens entsTo") ||
						captchaTextField.getText().equals("contend because")) {
					Account a = new Account(usernameTextField.getText(), passwordTextField.getText());
					if(!a.createAccount())
					{
						errorLabel.setText("This username/password combo already exists.");
						a.close();
					} else {
						JoinHostGUI jh = new JoinHostGUI(a);
						setVisible(false);
						jh.setVisible(true);
					}
					
				}else {
					errorLabel.setText("Incorrect captcha");
					
				}
				
				
			}
			
		});
	}
	
	
	
	
	public static void main(String[] args) {
		LoginGUI l = new LoginGUI();
		l.setVisible(true);
	}
	

	

	
	
	

}
