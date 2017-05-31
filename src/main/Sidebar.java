package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Sidebar extends JPanel{
	public ArrayList<String> names = new ArrayList<>();
	public int numPlayers;
	//GamePanel gamePanel;
	public JTextArea chatScreen, playerCash;
	public JTextField chatField;
	public JButton sendMessageButton, buyProperty, rollDice, addHouseHotel, mortgageProperty, declareBankrupt, 
		endTurn, rollInJail, pay50, useCard, jailEndButton;
	public JLabel playerOption, chatWindow;
	public JLabel [] currentPlayerCash;
	public Font large, medium, normal, small;
	public Color pastelBlue, pastelGreen, pastelRed;
	public JPanel cardPanel;
	
	
	public Sidebar(ArrayList<Player> playerList){
		for(Player player : playerList){
			names.add(player.getName());
		}
		numPlayers = names.size();
		initializePanel();
		setVisible(true);
		createGUI();
		this.setPreferredSize(new Dimension(350,900));
	}
	
	public void initializePanel(){
		//fonts and colors
		large = new Font("Verdana", Font.BOLD, 30);
		medium = new Font("Verdana", Font.BOLD, 25);
		normal = new Font("Verdana", Font.BOLD, 20);
		small = new Font("Verdana", Font.BOLD, 15);
		pastelBlue = Color.decode("#75aaff");
		pastelGreen = Color.decode("#cee6d1");
		pastelRed = Color.decode("#e0b9b1");

		
		chatScreen = new JTextArea();
		chatScreen.setSize(new Dimension(250,250));
		chatScreen.setEditable(false);
		chatField = new JTextField(20);
		sendMessageButton = new JButton("Send");
		
		playerOption = new JLabel("Player Option Menu");
		playerOption.setBackground(pastelBlue);
		playerOption.setBorder(new EmptyBorder(10,10,10,10));
		
		chatWindow = new JLabel("Chat Window");
		chatWindow.setBackground(pastelBlue);
		chatWindow.setBorder(new EmptyBorder(10,10,10,10));
		
		playerOption.setFont(medium);
		chatWindow.setFont(medium);
		buyProperty = new JButton("Buy Property");
		rollDice = new JButton("Roll Dice");
		declareBankrupt = new JButton("<html><center>"+"Declare"+"<br>"+"Bankruptcy"+"</center></html>");
		addHouseHotel = new JButton("<html><center>"+"Add House"+"<br>"+"or Hotel"+"</center></html>");
		mortgageProperty = new JButton("<html><center>"+"Mortgage"+"<br>"+"Property"+"</center></html>");
		endTurn = new JButton("End Turn");
		pay50 = new JButton("Pay $50");
		rollInJail = new JButton("<html><center>"+"Roll to get"+"<br>"+"out of jail"+"</center></html>");
		jailEndButton = new JButton("End Turn");
		useCard = new JButton("Use Card");
		createButton(buyProperty, rollDice, addHouseHotel, mortgageProperty, sendMessageButton, declareBankrupt, endTurn, pay50, rollInJail, useCard, jailEndButton);
		currentPlayerCash = new JLabel[numPlayers];
		
		for(int i = 0; i < numPlayers; i++){
			currentPlayerCash[i] = new JLabel(names.get(i) + ": " + "$1500");
			currentPlayerCash[i].setFont(small);
		}
	}
	
	public void createGUI(){	
		JScrollPane chatScrollPane = new JScrollPane(chatScreen, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatScrollPane.setPreferredSize(new Dimension(350,350));
		chatScrollPane.setMaximumSize(new Dimension(350,350));
		chatScrollPane.setMinimumSize(new Dimension(350,350));
		chatScrollPane.setBorder(null);
		
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
		eastPanel.setPreferredSize(new Dimension(350,990));
		eastPanel.setMaximumSize(new Dimension(350,990));
		eastPanel.setMinimumSize(new Dimension(350,990));
		eastPanel.setBackground(pastelGreen);
		
		JPanel topEast = new JPanel();
		topEast.setBackground(pastelGreen);
		topEast.setSize(new Dimension(100,300));
		topEast.setLayout(new BoxLayout(topEast, BoxLayout.Y_AXIS));
		JPanel jp = new JPanel();
		jp.setPreferredSize(new Dimension(350,65));
		jp.setMinimumSize(new Dimension(350,65));
		jp.setMaximumSize(new Dimension(350,65));
		jp.setBackground(pastelRed);
		jp.setBorder(new LineBorder(Color.black, 1));
		jp.add(playerOption);
		eastPanel.add(jp);
		//topEast.add(playerCash);
		JPanel cashPanel = new JPanel();
		cashPanel.setPreferredSize(new Dimension(350,100));
		cashPanel.setMinimumSize(new Dimension(350,100));
		cashPanel.setMaximumSize(new Dimension(350,100));
		cashPanel.setLayout(new BoxLayout(cashPanel, BoxLayout.Y_AXIS));
		for(JLabel label : currentPlayerCash){
			topEast.add(label);			
		}
		JPanel buttonPanel = new JPanel();
		JPanel jailPanel = new JPanel();
		cardPanel = new JPanel();
		cardPanel.setLayout(new CardLayout());
		buttonPanel.setBackground(pastelGreen);
		jailPanel.setBackground(pastelGreen);
		jailPanel.setLayout(new GridLayout(2,2,2,2));
		buttonPanel.setLayout(new GridLayout(3,2,2,2));
		buttonPanel.add(rollDice);
		buttonPanel.add(buyProperty);
		buttonPanel.add(addHouseHotel);
		buttonPanel.add(mortgageProperty);
		buttonPanel.add(declareBankrupt);
		buttonPanel.add(endTurn);
		
		jailPanel.add(rollInJail);
		jailPanel.add(pay50);
		jailPanel.add(useCard);
		jailPanel.add(jailEndButton);
		
		cardPanel.add(buttonPanel, "normal");
		cardPanel.add(jailPanel, "jail");
		((CardLayout)cardPanel.getLayout()).show(cardPanel, "normal");
		
		topEast.add(cardPanel);
		eastPanel.add(topEast);
		eastPanel.add(Box.createRigidArea(new Dimension(0,5)));
		
		JPanel chatPanel = new JPanel();
		chatPanel.setBackground(pastelGreen);
		JPanel jp2 = new JPanel();
		jp2.setPreferredSize(new Dimension(350,65));
		jp2.setMinimumSize(new Dimension(350,65));
		jp2.setMaximumSize(new Dimension(350,65));
		jp2.setBackground(pastelRed);
		jp2.setBorder(new LineBorder(Color.black, 1));
		jp2.add(chatWindow);
		eastPanel.add(jp2);
		chatPanel.add(chatField);
		eastPanel.add(chatScrollPane);
		chatPanel.add(sendMessageButton);
		eastPanel.add(chatPanel);
		add(eastPanel, BorderLayout.EAST);
		
	}
	
	public void createButton(JButton ...buttons){
		for(JButton button: buttons){
			button.setBackground(pastelBlue);
			button.setForeground(Color.black);
			//button.setPreferredSize(new Dimension(100,40));
			button.setBorder(new EmptyBorder(10,10,10,10));
			button.setFont(small);
			button.setHorizontalAlignment(JButton.CENTER);
			//button.setBorder(new LineBorder(Color.BLACK, 1));
			button.setOpaque(true);
		}	
	}
}//end class
