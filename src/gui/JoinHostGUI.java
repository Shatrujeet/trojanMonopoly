package gui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

import networking.Client;
import networking.Server;
import main.Monopoly;


public class JoinHostGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JLabel welcomeLabel;
	private JLabel descriptionLabel;
	
	private JPanel buttonPanel;
	private ButtonGroup buttonGroup;
	public JRadioButton joinGameButton;
	public JRadioButton hostGameButton;
	
	private JPanel lowerPanel;
	
	private JPanel hostPanel;
	private JLabel hostLabel;
	public JTextField hostTextField;
	
	private JPanel portPanel;
	private JLabel portLabel;
	public JTextField portTextField;
	
	public JButton joinButton;
	public JButton hostButton;
	
	private JPanel gamePiecePanel;
	private JLabel gamePieceLabel;
	private JComboBox<String> gamePieceChooser;
	private JLabel gamePieceImage;
	

	private JLabel playerSliderLabel;
	private JSlider playerSlider;
	
	private JLabel salarySliderLabel;
	private JSlider salarySlider;
	
	private JPanel optionsPanel;
	
	private JCheckBox freeParking;
	private Boolean freeParkingAllowed;
	
	private JPanel landOnGoPanel;
	private JLabel landOnGoLabel;
	private JComboBox<String> landOnGoChooser;
	private Boolean landOnGoCashDoubled;
	
	public JLabel messageLabel;
	
	private Account user;
	
	public boolean serverFailed = false;
	public Monopoly gamePlay = null;
	
	public JoinHostGUI(Account user)
	{
		super("Monopoly");
		this.user = user;
		initalizeComponents();
		createGUI();
		addEvents();
		
	}
	
	private void initalizeComponents() {
		landOnGoCashDoubled = false;
		
		getContentPane().setBackground(Color.decode("#CEE6D1"));
		
		welcomeLabel = new JLabel("Welcome " + user.getName() + "!");
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomeLabel.setFont(Constants.largeFont);
		
		descriptionLabel = new JLabel("Please choose whether you would like to host or join a game.");
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		descriptionLabel.setFont(Constants.mediumFont);
		
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.decode("#CEE6D1"));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonGroup = new ButtonGroup();
		
		joinGameButton = new JRadioButton("Join Game");
		joinGameButton.setFont(Constants.mediumFont);
		
		hostGameButton = new JRadioButton("Host Game");
		hostGameButton.setFont(Constants.mediumFont);
		if(user.isGuest())
		{
			hostGameButton.setEnabled(false);
		}
		
		lowerPanel = new JPanel();
		lowerPanel.setBackground(Color.decode("#CEE6D1"));
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
		
		hostPanel = new JPanel();
		hostPanel.setBackground(Color.decode("#CEE6D1"));
		hostLabel = new JLabel("Host: ");
		hostLabel.setFont(Constants.mediumFont);
		hostTextField = new JTextField(20);
		
		portPanel = new JPanel();
		portPanel.setBackground(Color.decode("#CEE6D1"));
		portLabel = new JLabel("Port: ");
		portLabel.setFont(Constants.mediumFont);
		
		portTextField = new JTextField(20);	
		
		playerSliderLabel = new JLabel("Please choose the number of teams on the slider below.");
		playerSliderLabel.setFont(Constants.mediumFont);
		playerSliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		playerSlider = new JSlider(JSlider.HORIZONTAL, 2, 4, 2);
		playerSlider.setAlignmentX(CENTER_ALIGNMENT);
		playerSlider.setMajorTickSpacing(1);
		playerSlider.setPaintLabels(true);
		playerSlider.setPaintTrack(true);
		playerSlider.setPaintTicks(true);
		playerSlider.setSnapToTicks(true);
		playerSlider.setFont(Constants.smallFont);
		
		salarySliderLabel = new JLabel("Please choose the starting salary on the slider below.");
		salarySliderLabel.setFont(Constants.mediumFont);
		salarySliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		salarySlider = new JSlider(JSlider.HORIZONTAL, 1000, 2000, 1500);
		salarySlider.setAlignmentX(CENTER_ALIGNMENT);
		salarySlider.setMajorTickSpacing(500);
		salarySlider.setPaintLabels(true);
		salarySlider.setPaintTrack(true);
		salarySlider.setPaintTicks(true);
		salarySlider.setSnapToTicks(true);
		salarySlider.setFont(Constants.smallFont);
		
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayout(1, 2));
		optionsPanel.setBackground(Color.decode("#CEE6D1"));
		
		freeParking = new JCheckBox("Enable Free Parking");
		freeParking.setFont(Constants.mediumFont);
		freeParking.setAlignmentX(Component.CENTER_ALIGNMENT);
		freeParking.setBackground(Color.decode("#CEE6D1"));
		
		landOnGoPanel = new JPanel();
		landOnGoPanel.setBackground(Color.decode("#CEE6D1"));
		landOnGoPanel.setLayout(new BoxLayout(landOnGoPanel, BoxLayout.Y_AXIS));
		
		landOnGoLabel = new JLabel("Please choose land on Go salary:");
		landOnGoLabel.setFont(Constants.mediumFont);
		landOnGoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		landOnGoChooser = new JComboBox<String>();
		landOnGoChooser.setFont(Constants.smallFont);
		
		gamePiecePanel = new JPanel();
		gamePiecePanel.setBackground(Color.decode("#CEE6D1"));
		gamePiecePanel.setLayout(new BoxLayout(gamePiecePanel, BoxLayout.X_AXIS));
		
		gamePieceLabel = new JLabel("Please choose a profile picture:");
		gamePieceLabel.setFont(Constants.mediumFont);
		
		gamePieceChooser = new JComboBox<String>();
		gamePieceChooser.setFont(Constants.smallFont);
		
		gamePieceImage = new JLabel(Constants.game_pieces[0]);
		
		joinButton = new JButton("Join Game!");
		joinButton.setFont(Constants.smallFont);
		joinButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		hostButton = new JButton("Host Game!");
		hostButton.setFont(Constants.smallFont);
		hostButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		messageLabel = new JLabel("");
		messageLabel.setFont(Constants.mediumFont);
		messageLabel.setForeground(Color.RED);
		messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
	}
	
	private void createGUI() {
		setSize(800, 500);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setMaximumSize(new Dimension(800, 600));
		setMinimumSize(new Dimension(800, 600));
		
		add(welcomeLabel);
		add(Box.createGlue());
		
		add(descriptionLabel);
		add(Box.createGlue());
		
		buttonGroup.add(joinGameButton);
		buttonGroup.add(hostGameButton);
		
		buttonPanel.add(joinGameButton);
		buttonPanel.add(hostGameButton);
		add(buttonPanel);
		add(Box.createGlue());
		
		add(lowerPanel);
		
	
		for(int i = 0; i < Constants.game_pieces.length - 1; i++)
		{
			gamePieceChooser.addItem(Constants.game_pieces_names[i]);
		}
		
		
		gamePiecePanel.add(gamePieceLabel);
		gamePiecePanel.add(gamePieceChooser);
		gamePiecePanel.add(gamePieceImage);
		
		landOnGoChooser.addItem("Traditional");
		landOnGoChooser.addItem("Double the Amount");
		
		landOnGoPanel.add(landOnGoLabel);
		landOnGoPanel.add(landOnGoChooser);
		
		optionsPanel.add(freeParking);
		optionsPanel.add(landOnGoPanel);
		
		hostPanel.add(hostLabel);
		hostPanel.add(hostTextField);
		
		portPanel.add(portLabel);
		portPanel.add(portTextField);
		
		if(!user.isGuest())
		{
			gamePieceImage.setIcon(Constants.game_pieces[gamePieceChooser.getSelectedIndex()]);
			user.setImage(Constants.game_pieces[gamePieceChooser.getSelectedIndex()]);
		}
	}
	
	private void addEvents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		joinGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(joinGameButton.isSelected())
				{
					lowerPanel.removeAll();
					lowerPanel.add(hostPanel);
					lowerPanel.add(portPanel);
					
					if(!user.isGuest())
						lowerPanel.add(gamePiecePanel);
					
					lowerPanel.add(joinButton);
					lowerPanel.add(messageLabel);
					repaintt();
					
				}
				
			}
			
		});
		
		hostGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(hostGameButton.isSelected())
				{
					
					lowerPanel.removeAll();
					lowerPanel.add(portPanel);
					
					lowerPanel.add(gamePiecePanel);
					
					lowerPanel.add(playerSliderLabel);
					lowerPanel.add(playerSlider);
					lowerPanel.add(Box.createGlue());
					
					lowerPanel.add(salarySliderLabel);
					lowerPanel.add(salarySlider);
					lowerPanel.add(Box.createGlue());
					
					lowerPanel.add(optionsPanel);
					lowerPanel.add(Box.createGlue());
					
					lowerPanel.add(hostButton);
					lowerPanel.add(messageLabel);
					repaintt();
				}
				
			}
			
		});
		
		gamePieceChooser.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent e) {
				
				gamePieceImage.setIcon(Constants.game_pieces[gamePieceChooser.getSelectedIndex()]);
				user.setImage(Constants.game_pieces[gamePieceChooser.getSelectedIndex()]);
				
			}
			
		});
		
		joinButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//create Client and try to connect to the server
				joinButton.setEnabled(false);
				Client c = new Client(hostTextField.getText(), Integer.parseInt(portTextField.getText()), user, JoinHostGUI.this, false);
				//StatisticsGUI stat = new StatisticsGUI(user, false);
				//stat.setVisible(true);
				//setVisible(false);
			}
		
		});
		
		hostButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hostButton.setEnabled(false);
				Constants.sound.playSound(Constants.sound_filename);
				Server s = new Server(Integer.parseInt(portTextField.getText()), playerSlider.getValue(), JoinHostGUI.this);
				Client c = new Client("localhost", Integer.parseInt(portTextField.getText()), user, JoinHostGUI.this, false);
				//StatisticsGUI stat = new StatisticsGUI(user, false);
				//stat.setVisible(true);
				//setVisible(false);
			}
			
		});
		
		landOnGoChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(landOnGoChooser.getSelectedItem().equals("Double the Amount"))
				{
					landOnGoCashDoubled = true;
					System.out.println("landOnGoCashDoubled = " + landOnGoCashDoubled);
				} else if(landOnGoChooser.getSelectedItem().equals("Traditional"))
				{
					landOnGoCashDoubled = false;
					System.out.println("landOnGoCashDoubled = " + landOnGoCashDoubled);
				}
				
			}

		});
		
		freeParking.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(freeParking.isSelected()) {
					freeParkingAllowed = true;
					System.out.println("freeParkingAllowed = " + freeParkingAllowed);
				} else if(!freeParking.isSelected())
				{
					freeParkingAllowed = false;
					System.out.println("freeParkingAllowed = " + freeParkingAllowed);
				}
				
			}
			
		});
	
	}
	
	private void repaintt()
	{
		this.revalidate();
		this.repaint();
	}

	public void updateNetworkMessage(int numTeamsNeeded) {
		messageLabel.setText("We are waiting for " + numTeamsNeeded+ " to join.");
	}
	


	
	
	
	

}
