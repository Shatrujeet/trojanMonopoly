package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import networking.InformationPacket;

public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public JButton rollButton, endTurnButton, jailRollButton, pay50Button, jailEndButton, getOutOfJail, buyButton,
			buyHouseButton;
	
	//public JPanel log;
	public JTextArea logArea;
	//public ArrayList<JLabel> scoreLabels;
	//public JPanel scorePanel;
	//public JTextArea chatArea;
	//public JTextField chatField;
	//public JButton sendMessageButton;
	//public JPanel chatPanel;
	public Board gameBoard;
	public Sidebar sidebar;

	public boolean buyHouseDialogExists;
	public boolean mortgageDialogExists;
	public JLabel middle = new JLabel();
	public JLabel currTurn = new JLabel();
	
	
	BoardPanel(Tile[] tileArray, Board gameBoard, Sidebar sidebar) {
		setLayout(null);
		//scoreLabels = new ArrayList<JLabel>();
		this.gameBoard = gameBoard;
		this.sidebar = sidebar;
		
		//log = new JPanel();
		logArea = new JTextArea();
		logArea.setBackground(new Color(0xc0e2ca));
		//log.add(logArea);
		logArea.setLineWrap(true);
		logArea.setWrapStyleWord(true);
		logArea.append("The Game Has Started! It is " + gameBoard.playerList.get(0).getName() + "'s Turn!");

		//chatPanel = new JPanel();
		//chatPanel.setPreferredSize(new Dimension(500, 500));
		//chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

		JPanel sendPanel = new JPanel();

		//chatField = new JTextField();
		//chatField.setPreferredSize(new Dimension(350, 50));
		//sendMessageButton = new JButton("Send");
		
		sidebar.sendMessageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempMessage = gameBoard.gamePlay.infoPacket.message = gameBoard.username + ": "
						+ sidebar.chatField.getText() + "\n";
				sidebar.chatField.setText("");

				gameBoard.gamePlay.infoPacket.isMessage = true;
				gameBoard.gamePlay.infoPacket.message = tempMessage;

				gameBoard.sendInfoPacket(gameBoard.gamePlay.infoPacket);
			}
		});

		//sendMessageButton.setPreferredSize(new Dimension(50, 50));
		//chatArea = new JTextArea();
		//chatArea.setPreferredSize(new Dimension(500, 450));

		//sendPanel.add(chatField);
		//sendPanel.add(sendMessageButton);

		//chatPanel.add(chatArea);
		//chatPanel.add(sendPanel);

		// CENTER
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new CardLayout());
		JPanel notInJailButtonPanel = new JPanel();
		JPanel inJailButtonPanel = new JPanel();

		
		sidebar.jailEndButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int turn = gameBoard.whoseTurn;
				InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
				
				tPacket.jailPlayerEnded = turn;
				
				if(turn + 1 == gameBoard.playerList.size())
				{
					turn = 0;
				}
				else
				{
					turn++;
				}
				
				tPacket.setFalse();
				
				
				tPacket.isJailEnd = true;
				tPacket.whoseTurn = turn;
				gameBoard.sendInfoPacket(tPacket);
				
			}
		});
		sidebar.useCard.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
				tPacket.setFalse();
				tPacket.isUseCard = true;
				tPacket.whoseTurn = gameBoard.whoseTurn;
				gameBoard.sendInfoPacket(tPacket);				
			}
		});
		sidebar.pay50.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
				tPacket.setFalse();
				tPacket.isPay50 = true;
				tPacket.whoseTurn = gameBoard.whoseTurn;
				gameBoard.sendInfoPacket(tPacket);
			}
		});
		sidebar.rollInJail.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				Player currPlayer = gameBoard.playerList.get(gameBoard.whoseTurn);
				int [] rollVal = currPlayer.rollJail();
				
				InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
				tPacket.setFalse();
				tPacket.rollJailVal = rollVal;
				tPacket.isJailRoll = true;
				tPacket.whoseTurn = gameBoard.whoseTurn;
				gameBoard.sendInfoPacket(tPacket);
			}
		});


		
		rollButton = new JButton("Roll");
		sidebar.rollDice.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Player currPlayer = gameBoard.playerList.get(gameBoard.whoseTurn);
				int rollVal = currPlayer.roll();

				InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
				tPacket.setFalse();
				tPacket.isRoll = true;
				tPacket.roll = rollVal;
				tPacket.whoseTurn = gameBoard.whoseTurn;
				
				gameBoard.sendInfoPacket(tPacket);	
			}	
		});

		jailRollButton = new JButton("Roll");
		jailRollButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Player currPlayer = gameBoard.playerList.get(gameBoard.whoseTurn);
				int[] rollVal = currPlayer.rollJail();

				if (currPlayer.lastRollWasDoubles) {
					JOptionPane.showMessageDialog(BoardPanel.this,
							"You Rolled a " + rollVal[0] + " & " + rollVal[1] + "\nCongratulations! You Are Free!",
							"Escape Jail?", JOptionPane.INFORMATION_MESSAGE);

					currPlayer.inJail = false;
					currPlayer.jailTime = 0;

					gameBoard.tileArray[10].getTilePanel().updatePlayerBar(currPlayer);
				} else {
					JOptionPane.showMessageDialog(BoardPanel.this,
							"You Rolled a " + rollVal[0] + " & " + rollVal[1] + "\nFailure! You Are Still In Jail!",
							"Escape Jail?", JOptionPane.INFORMATION_MESSAGE);
				}

				jailRollButton.setEnabled(false);
				pay50Button.setEnabled(false);
				jailEndButton.setEnabled(true);
			}
		});

		pay50Button = new JButton("Pay 50");
		pay50Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Player currPlayer = gameBoard.playerList.get(gameBoard.whoseTurn);
				currPlayer.money -= 50;
				updateScores();

				JOptionPane.showMessageDialog(BoardPanel.this, "You Payed $50 " + "\nCongratulations! You Are Free!",
						"Escape Jail?", JOptionPane.INFORMATION_MESSAGE);

				currPlayer.inJail = false;
				currPlayer.jailTime = 0;

				gameBoard.tileArray[10].getTilePanel().updatePlayerBar(currPlayer);

				jailRollButton.setEnabled(false);
				pay50Button.setEnabled(false);
				jailEndButton.setEnabled(true);
			}
		});

		jailEndButton = new JButton("End Turn");
		jailEndButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int turn = gameBoard.whoseTurn;

				if (turn + 1 == gameBoard.playerList.size()) {
					turn = 0;
				} else {
					turn++;
				}

				gameBoard.whoseTurn = turn;

				if (!gameBoard.playerList.get(turn).inJail) {
					((CardLayout) buttonPanel.getLayout()).show(buttonPanel, "NJail");

					rollButton.setEnabled(true);
					buyButton.setEnabled(false);
					endTurnButton.setEnabled(false);
				} else {
					((CardLayout) buttonPanel.getLayout()).show(buttonPanel, "YJail");

					jailRollButton.setEnabled(true);
					pay50Button.setEnabled(true);

					if (gameBoard.playerList.get(turn).getOutOfJail) {
						getOutOfJail.setEnabled(true);
					} else {
						getOutOfJail.setEnabled(false);
					}

					jailEndButton.setEnabled(false);
				}
			}
		});

		getOutOfJail = new JButton("Use Card");
		getOutOfJail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Player currPlayer = gameBoard.playerList.get(gameBoard.whoseTurn);

				currPlayer.inJail = false;
				currPlayer.jailTime = 0;

				gameBoard.tileArray[10].getTilePanel().updatePlayerBar(currPlayer);

				jailRollButton.setEnabled(false);
				getOutOfJail.setEnabled(false);
				pay50Button.setEnabled(false);
				jailEndButton.setEnabled(true);
			}
		});

		inJailButtonPanel.add(jailRollButton);
		inJailButtonPanel.add(pay50Button);
		inJailButtonPanel.add(getOutOfJail);
		inJailButtonPanel.add(jailEndButton);
		
		sidebar.buyProperty.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				
				InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
				
				tPacket.setFalse();
				tPacket.isBuyProperty = true;
				tPacket.whoseTurn = gameBoard.whoseTurn;
				
				gameBoard.sendInfoPacket(tPacket);
			}
		});

		buyButton = new JButton("Buy");
		buyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Player currPlayer = gameBoard.playerList.get(gameBoard.whoseTurn);

				currPlayer.money -= tileArray[currPlayer.currLocation].price;
				updateScores();

				tileArray[currPlayer.currLocation].tileOwner = currPlayer;
				currPlayer.propertyList.add(currPlayer.currLocation);

				if (currPlayer.currLocation == 5 || currPlayer.currLocation == 12 || currPlayer.currLocation == 15
						|| currPlayer.currLocation == 25 || currPlayer.currLocation == 28
						|| currPlayer.currLocation == 35) {
					currPlayer.railroads++;
				} else if (currPlayer.currLocation == 28) {
					currPlayer.waters = true;
				} else if (currPlayer.currLocation == 12) {
					currPlayer.electrics = true;
				}

				tileArray[currPlayer.currLocation].getTilePanel().updateOwnerPane();
				buyButton.setEnabled(false);
			}
		});

		

		

		endTurnButton = new JButton("End Turn");
		sidebar.endTurn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int turn = gameBoard.whoseTurn;
				
				if(turn + 1 == gameBoard.playerList.size())
				{
					turn = 0;
				}
				else
				{
					turn++;
				}
				InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
				tPacket.setFalse();
				
				tPacket.isEndTurn = true;
				
				tPacket.whoseTurn = turn;
				
				gameBoard.sendInfoPacket(tPacket);
			}

		});
		sidebar.mortgageProperty.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if (!mortgageDialogExists) {
					new MortgageDialog(gameBoard.gamePlay.infoPacket.playerList.get(gameBoard.gamePlay.infoPacket.whoseTurn).propertyList, gameBoard.tileArray);
					mortgageDialogExists = true;
				}

			}
		});
		sidebar.addHouseHotel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(!buyHouseDialogExists){
					new BuyHouseDialog(gameBoard.gamePlay.infoPacket.playerList.get(gameBoard.gamePlay.infoPacket.whoseTurn).propertyList, gameBoard.tileArray);
					buyHouseDialogExists = true;
				}
			}
		});
		buyHouseDialogExists = false;
		
		endTurnButton.setEnabled(false);
		buyButton.setEnabled(false);

		notInJailButtonPanel.add(endTurnButton);

		buttonPanel.add(notInJailButtonPanel, "NJail");
		buttonPanel.add(inJailButtonPanel, "YJail");

		((CardLayout) buttonPanel.getLayout()).show(buttonPanel, "NJail");

		//centerPanel.add(buttonPanel);
		centerPanel.setBackground(new Color(0xc0e2ca));
		centerPanel.setBounds(100, 90, 900, 810);

		//scorePanel = new JPanel();
		//scorePanel.setLayout(new GridLayout(gameBoard.playerList.size(), 1));

		for (Player tempPlayer : gameBoard.playerList) {
			JLabel tempLabel = new JLabel(tempPlayer.playerName + ": $" + tempPlayer.money);
			//scoreLabels.add(tempLabel);
			//scorePanel.add(tempLabel);
		}

		//centerPanel.add(scorePanel);
		//centerPanel.add(chatPanel);
		centerPanel.add(middle);
		middle.setAlignmentX(CENTER_ALIGNMENT);
		centerPanel.add(currTurn);
		currTurn.setAlignmentX(CENTER_ALIGNMENT);
		JScrollPane jsp = new JScrollPane(logArea);
		jsp.setAlignmentX(CENTER_ALIGNMENT);
		
		//log.add();
		centerPanel.add(jsp);
		add(centerPanel);
		
		//Disable Buttons for nonTurn
		
		
		if(gameBoard.gamePlay.infoPacket.playerList.get(gameBoard.gamePlay.infoPacket.whoseTurn).objectCode == gameBoard.client.playerData.objectCode)
		{
			sidebar.buyProperty.setEnabled(true);
			sidebar.endTurn.setEnabled(true);
			sidebar.rollDice.setEnabled(true);
			sidebar.addHouseHotel.setEnabled(true);
			sidebar.declareBankrupt.setEnabled(true);
			sidebar.mortgageProperty.setEnabled(true);
		}
		else
		{
			sidebar.buyProperty.setEnabled(false);
			sidebar.endTurn.setEnabled(false);
			sidebar.rollDice.setEnabled(false);
			sidebar.addHouseHotel.setEnabled(false);
			sidebar.declareBankrupt.setEnabled(false);
			sidebar.mortgageProperty.setEnabled(false);
		}

		// TOP
		TilePanel tempTile = tileArray[20].getTilePanel();
		add(tempTile);
		tempTile.setLocation(0, 0);

		tempTile = tileArray[21].getTilePanel();
		add(tempTile);
		tempTile.setLocation(100, 0);

		tempTile = tileArray[22].getTilePanel();
		add(tempTile);
		tempTile.setLocation(200, 0);

		tempTile = tileArray[23].getTilePanel();
		add(tempTile);
		tempTile.setLocation(300, 0);

		tempTile = tileArray[24].getTilePanel();
		add(tempTile);
		tempTile.setLocation(400, 0);

		tempTile = tileArray[25].getTilePanel();
		add(tempTile);
		tempTile.setLocation(500, 0);

		tempTile = tileArray[26].getTilePanel();
		add(tempTile);
		tempTile.setLocation(600, 0);

		tempTile = tileArray[27].getTilePanel();
		add(tempTile);
		tempTile.setLocation(700, 0);

		tempTile = tileArray[28].getTilePanel();
		add(tempTile);
		tempTile.setLocation(800, 0);

		tempTile = tileArray[29].getTilePanel();
		add(tempTile);
		tempTile.setLocation(900, 0);

		tempTile = tileArray[30].getTilePanel();
		add(tempTile);
		tempTile.setLocation(1000, 0);

		// LEFT
		tempTile = tileArray[19].getTilePanel();
		add(tempTile);
		tempTile.setLocation(0, 90);

		tempTile = tileArray[18].getTilePanel();
		add(tempTile);
		tempTile.setLocation(0, 180);

		tempTile = tileArray[17].getTilePanel();
		add(tempTile);
		tempTile.setLocation(0, 270);

		tempTile = tileArray[16].getTilePanel();
		add(tempTile);
		tempTile.setLocation(0, 360);

		tempTile = tileArray[15].getTilePanel();
		add(tempTile);
		tempTile.setLocation(0, 450);

		tempTile = tileArray[14].getTilePanel();
		add(tempTile);
		tempTile.setLocation(0, 540);

		tempTile = tileArray[13].getTilePanel();
		add(tempTile);
		tempTile.setLocation(0, 630);

		tempTile = tileArray[12].getTilePanel();
		add(tempTile);
		tempTile.setLocation(0, 720);

		tempTile = tileArray[11].getTilePanel();
		add(tempTile);
		tempTile.setLocation(0, 810);

		// RIGHT
		tempTile = tileArray[31].getTilePanel();
		add(tempTile);
		tempTile.setLocation(1000, 90);

		tempTile = tileArray[32].getTilePanel();
		add(tempTile);
		tempTile.setLocation(1000, 180);

		tempTile = tileArray[33].getTilePanel();
		add(tempTile);
		tempTile.setLocation(1000, 270);

		tempTile = tileArray[34].getTilePanel();
		add(tempTile);
		tempTile.setLocation(1000, 360);

		tempTile = tileArray[35].getTilePanel();
		add(tempTile);
		tempTile.setLocation(1000, 450);

		tempTile = tileArray[36].getTilePanel();
		add(tempTile);
		tempTile.setLocation(1000, 540);

		tempTile = tileArray[37].getTilePanel();
		add(tempTile);
		tempTile.setLocation(1000, 630);

		tempTile = tileArray[38].getTilePanel();
		add(tempTile);
		tempTile.setLocation(1000, 720);

		tempTile = tileArray[39].getTilePanel();
		add(tempTile);
		tempTile.setLocation(1000, 810);

		// BOTTOM

		tempTile = tileArray[10].getTilePanel();
		add(tempTile);
		tempTile.setLocation(0, 900);

		tempTile = tileArray[9].getTilePanel();
		add(tempTile);
		tempTile.setLocation(100, 900);

		tempTile = tileArray[8].getTilePanel();
		add(tempTile);
		tempTile.setLocation(200, 900);

		tempTile = tileArray[7].getTilePanel();
		add(tempTile);
		tempTile.setLocation(300, 900);

		tempTile = tileArray[6].getTilePanel();
		add(tempTile);
		tempTile.setLocation(400, 900);

		tempTile = tileArray[5].getTilePanel();
		add(tempTile);
		tempTile.setLocation(500, 900);

		tempTile = tileArray[4].getTilePanel();
		add(tempTile);
		tempTile.setLocation(600, 900);

		tempTile = tileArray[3].getTilePanel();
		add(tempTile);
		tempTile.setLocation(700, 900);

		tempTile = tileArray[2].getTilePanel();
		add(tempTile);
		tempTile.setLocation(800, 900);

		tempTile = tileArray[1].getTilePanel();
		add(tempTile);
		tempTile.setLocation(900, 900);

		tempTile = tileArray[0].getTilePanel();
		add(tempTile);
		tempTile.setLocation(1000, 900);
		
		setMiddle(gameBoard.gamePlay.infoPacket.imageList.get(gameBoard.gamePlay.infoPacket.whoseTurn), gameBoard.gamePlay.infoPacket.playerList.get(gameBoard.gamePlay.infoPacket.whoseTurn).getName());
	}
		

	public void updateScores() {
		for (int i = 0; i < sidebar.currentPlayerCash.length; i++) {
			sidebar.currentPlayerCash[i].setText(gameBoard.playerList.get(i).playerName + ": $" + gameBoard.playerList.get(i).money);
		}
	}

	public void operateOnSpecialTile(Player currPlayer) {
		// GO TO JAIL
		if (currPlayer.currLocation == 30) {
			if(gameBoard.client.playerData.objectCode == gameBoard.gamePlay.infoPacket.playerList.get(gameBoard.gamePlay.infoPacket.whoseTurn).objectCode)
				JOptionPane.showMessageDialog(null, "You Landed in Jail!", "Monopoly", JOptionPane.PLAIN_MESSAGE);
			
			logArea.append("\n" + gameBoard.gamePlay.infoPacket.playerList.get(gameBoard.gamePlay.infoPacket.whoseTurn) + " landed in SAL");
			
			
			currPlayer.inJail = true;
			currPlayer.jailTime = 3;
			currPlayer.currLocation = 10;
			gameBoard.updatePlayerLocations();
		}
		
		
		// INCOME TAX
		else if (currPlayer.currLocation == 4) {
			if(gameBoard.client.playerData.objectCode == gameBoard.gamePlay.infoPacket.playerList.get(gameBoard.gamePlay.infoPacket.whoseTurn).objectCode)
			{
				String[] options = new String[] { "10%", "$200" };
	
				int response = JOptionPane.showOptionDialog(this, "Choose to Pay Either 10% of Your Total Money or $200!",
						"Income Tax", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
	
				if (response == 0) {
					InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
					tPacket.setFalse();
					tPacket.isIncomeTax = true;
					tPacket.incomeChoice = false;
					
					gameBoard.sendInfoPacket(tPacket);
				} else {
					InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
					tPacket.setFalse();
					tPacket.isIncomeTax = true;
					tPacket.incomeChoice = true;
					
					gameBoard.sendInfoPacket(tPacket);
				}
			}
		}
		// Luxury Tax
		else if (currPlayer.currLocation == 38) {
			if(gameBoard.gamePlay.infoPacket.playerList.get(gameBoard.gamePlay.infoPacket.whoseTurn).objectCode == gameBoard.client.playerData.objectCode)
				JOptionPane.showMessageDialog(this, "You Lose $75 to Luxury Tax!", "Luxury Tax", JOptionPane.PLAIN_MESSAGE);

			currPlayer.money -= 75;
			
			logArea.append("\n" + gameBoard.gamePlay.infoPacket.playerList.get(gameBoard.gamePlay.infoPacket.whoseTurn).getName() + " lost $75 to Income Tax");
			updateScores();
		}
		// Community Chest
		else if (currPlayer.currLocation == 2 || currPlayer.currLocation == 17 || currPlayer.currLocation == 33) {
			gameBoard.communityChestDeck.draw(currPlayer, gameBoard);
		}
		// Chance
		else if (currPlayer.currLocation == 7 || currPlayer.currLocation == 22 || currPlayer.currLocation == 36) {
			gameBoard.chanceDeck.draw(currPlayer, gameBoard);
		}
	}

//	public void rent(Player currPlayer) {
//		if (gameBoard.tileArray[currPlayer.currLocation].tileOwner == null) {
//			return;
//		}
//
//		if (gameBoard.tileArray[currPlayer.currLocation].tileOwner.playerColor != currPlayer.playerColor) {
//			if (currPlayer.currLocation == 5 || currPlayer.currLocation == 12 || currPlayer.currLocation == 15
//					|| currPlayer.currLocation == 25 || currPlayer.currLocation == 28
//					|| currPlayer.currLocation == 35) {
//				if (currPlayer.currLocation == 5 || currPlayer.currLocation == 15 || currPlayer.currLocation == 25
//						|| currPlayer.currLocation == 35) {
//					int money = gameBoard.tileArray[currPlayer.currLocation].rentPrices[currPlayer.railroads];
//
//					JOptionPane.showMessageDialog(gameBoard.boardPanel,
//							"Someone Else Owns This, You Lose $" + money, "Monopoly", JOptionPane.PLAIN_MESSAGE);
//
//					currPlayer.money -= money;
//					updateScores();
//					gameBoard.tileArray[currPlayer.currLocation].tileOwner.money += money;
//					updateScores();
//				} else if (currPlayer.currLocation == 28) {
//					Random r = new Random();
//					int roll = r.nextInt(12);
//
//					roll += 1;
//
//					Player tileOwner = gameBoard.tileArray[currPlayer.currLocation].tileOwner;
//
//					if (!tileOwner.electrics) {
//						int money = roll * 4;
//
//						JOptionPane.showMessageDialog(gameBoard.boardPanel,
//								+roll + "\n" + tileOwner.playerName + " Owns This, You Lose $" + money, "Monopoly",
//								JOptionPane.PLAIN_MESSAGE);
//
//						currPlayer.money -= money;
//						tileOwner.money += money;
//					} else {
//						int money = roll * 10;
//
//						JOptionPane.showMessageDialog(gameBoard.boardPanel,
//								"You Rolled a " + roll + "\n" + tileOwner.playerName
//										+ " Owns This And Electric You Lose $" + money,
//								"Monopoly", JOptionPane.PLAIN_MESSAGE);
//
//						currPlayer.money -= money;
//						tileOwner.money += money;
//					}
//
//				} else if (currPlayer.currLocation == 12) {
//					Random r = new Random();
//					int roll = r.nextInt(12);
//
//					roll += 1;
//
//					Player tileOwner = gameBoard.tileArray[currPlayer.currLocation].tileOwner;
//
//					if (!tileOwner.waters) {
//						int money = roll * 4;
//
//						JOptionPane.showMessageDialog(gameBoard.boardPanel,
//								"You Rolled a " + roll + "\n" + tileOwner.playerName + " Owns This, You Lose $" + money,
//								"Monopoly", JOptionPane.PLAIN_MESSAGE);
//
//						currPlayer.money -= money;
//						tileOwner.money += money;
//					} else {
//						int money = roll * 10;
//
//						JOptionPane.showMessageDialog(gameBoard.boardPanel,
//								"You Rolled a " + roll + "\n" + tileOwner.playerName
//										+ " Owns This And Waters You Lose $" + money,
//								"Monopoly", JOptionPane.PLAIN_MESSAGE);
//
//						currPlayer.money -= money;
//						tileOwner.money += money;
//					}
//				}
//			} else {
//				int houses = gameBoard.tileArray[currPlayer.currLocation].houses;
//				int money = gameBoard.tileArray[currPlayer.currLocation].rentPrices[houses];
//
//				JOptionPane.showMessageDialog(gameBoard.boardPanel, "Someone Else Owns This, You Lose $" + money,
//						"Monopoly", JOptionPane.PLAIN_MESSAGE);
//
//				currPlayer.money -= money;
//				gameBoard.tileArray[currPlayer.currLocation].tileOwner.money += money;
//			}
//		}
//	}

	public void moveDistance(Player currPlayer, int distance) {
				// MOVING INCREMENTALLY
			boolean go = false;
			
				for (int i = 0; i < distance; i++) {

					currPlayer.move();

					if (currPlayer.currLocation == 0) {
						go = true;
					}
				}
				
				gameBoard.updatePlayerLocations();

				if(go)
				{
					if(currPlayer.objectCode == (gameBoard.client.playerData.objectCode)){
					JOptionPane.showMessageDialog(gameBoard.boardPanel, "You passed GO! You Received $200",
							"Monopoly", JOptionPane.PLAIN_MESSAGE);
					}
					
					logArea.append("\n" + currPlayer.getName() + " passed Go & gained $200!");

					currPlayer.money += 200;
					updateScores();
					
				}
				


				// AT THIS POINT THE PLAYER HAS STOPPED MOVING

				if (!currPlayer.lastRollWasDoubles) {
					gameBoard.boardPanel.endTurnButton.setEnabled(true);

					if (gameBoard.tileArray[currPlayer.currLocation].isProperty
							&& gameBoard.tileArray[currPlayer.currLocation].tileOwner == null)
						gameBoard.boardPanel.buyButton.setEnabled(true);
					else
						gameBoard.boardPanel.buyButton.setEnabled(false);

					currPlayer.lastRollWasDoubles = false;
					currPlayer.doubles = 0;
				} else {
					if (!currPlayer.inJail) {
						if(currPlayer.objectCode == (gameBoard.client.playerData.objectCode)){
						JOptionPane.showMessageDialog(gameBoard.boardPanel,
								"You Rolled Doubles!" + "\nRoll Again!", "Rolled Doubles",
								JOptionPane.INFORMATION_MESSAGE);
						}

						logArea.append("\n" + currPlayer.getName() + " rolled Doubles! They May Roll Again!!");

						gameBoard.boardPanel.rollButton.setEnabled(true);

						if (gameBoard.tileArray[currPlayer.currLocation].isProperty
								&& gameBoard.tileArray[currPlayer.currLocation].tileOwner == null) {
							gameBoard.boardPanel.buyButton.setEnabled(true);
						} else
							gameBoard.boardPanel.buyButton.setEnabled(false);
					}
				}

				//rent(currPlayer);
				operateOnSpecialTile(currPlayer);
	}

	

		
	
	public class MortgageDialog extends JDialog {
		public JLabel titleLabel;
		public JList propertyList;
		public JButton mortgageButton, unmortgageButton, cancelButton;
		public String[] tileNames;
		public JScrollPane scrollPane;
		Tile[] tileArray;

		public MortgageDialog(ArrayList<Integer> propertyList, Tile[] tileArray) {
			super();

			this.tileArray = tileArray;
			tileNames = new String[tileArray.length];

			for (int i = 0; i < propertyList.size(); i++) {
				tileNames[i] = tileArray[propertyList.get(i)].name;
			}

			setSize(600, 600);
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			setTitle("Mortgage options");
			setLocationRelativeTo(BoardPanel.this);
			setResizable(true);
			setLayout(new GridBagLayout());
			initComponents();
			setVisible(true);
		}

		public void initComponents() {
			titleLabel = new JLabel("Mortgage or unmortgage property");
			titleLabel.setHorizontalAlignment(JLabel.CENTER);
			titleLabel.setFont(new Font("Helvetica", Font.BOLD, 25));

			GridBagConstraints gridBagConstraint = new GridBagConstraints();
			gridBagConstraint.anchor = GridBagConstraints.CENTER;
			gridBagConstraint.gridwidth = 1;
			gridBagConstraint.gridheight = 1;
			gridBagConstraint.fill = GridBagConstraints.BOTH;
			gridBagConstraint.gridx = 0;
			gridBagConstraint.gridy = 0;
			gridBagConstraint.weightx = 1.0;
			gridBagConstraint.weighty = 0.1;

			add(titleLabel, gridBagConstraint);

			gridBagConstraint.gridy = 1;
			gridBagConstraint.weighty = 1.0;

			propertyList = new JList(tileNames);
			propertyList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					int index = ((JList) e.getSource()).getSelectedIndex();
					int zNumOfHouses = tileArray[index].houses;
				}
			});

			scrollPane = new JScrollPane(propertyList);
			add(scrollPane, gridBagConstraint);

			gridBagConstraint.gridy = 3;
			gridBagConstraint.weighty = 0.2;

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(1, 2));

			mortgageButton = new JButton("Mortgage");
			mortgageButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String propertyName = (String) propertyList.getSelectedValue();
					Tile property = null;
					for (Tile currTile : tileArray) {
						if (propertyName == currTile.name) {
							property = currTile;
						}
					}
					
					if (property.houses != 0) {
						JOptionPane.showMessageDialog(
								gameBoard.getBoardPanel(), "All occupying houses must be sold before mortgaging property",
								"Error", JOptionPane.PLAIN_MESSAGE);
					}

					if (property.isMortgaged)
						return; // this means the property was already mortgaged
					property.isMortgaged = true;

					dispose();
					InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
					tPacket.setFalse();
					tPacket.isMortgage = true;
					//tPacket.propertyVal = 
					int propertyIndex = -1;
					for(int i = 0; i < gameBoard.tileArray.length; i++){
						if(property.name.equals(gameBoard.tileArray[i].name)){
							propertyIndex = i;
						}
					}
					tPacket.propertyVal = propertyIndex;
					
					gameBoard.sendInfoPacket(tPacket);
					mortgageDialogExists = false;
				}
			});

			unmortgageButton = new JButton("Unmortgage");
			unmortgageButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String propertyName = (String) propertyList.getSelectedValue();
					Tile property = null;
					for (Tile currTile : tileArray) {
						if (propertyName == currTile.name) {
							property = currTile;
						}
					}
					Boolean isMortgaged = property.isMortgaged;

					if (!isMortgaged)
						return;
					property.isMortgaged = false;

					int mortgageCash = property.price / 2 + property.price / 10;
					Player currPlayer = gameBoard.gamePlay.infoPacket.playerList.get(gameBoard.gamePlay.infoPacket.whoseTurn);
					
					if (currPlayer.money < mortgageCash) {
						JOptionPane.showMessageDialog(
								gameBoard.getBoardPanel(), currPlayer.playerName + " does not have adequate cash to"
								+ " unmortgage " + property.name,
								"Error", JOptionPane.PLAIN_MESSAGE);
						dispose();
						mortgageDialogExists = false;
						return;
						
					}
					
					
					
					InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
					tPacket.setFalse();
					tPacket.isUnMortgage = true;
					//tPacket.propertyVal = 
					int propertyIndex = -1;
					for(int i = 0; i < gameBoard.tileArray.length; i++){
						if(property.name.equals(gameBoard.tileArray[i].name)){
							propertyIndex = i;
						}
					}
					tPacket.propertyVal = propertyIndex;
					
					gameBoard.sendInfoPacket(tPacket);
					
					currPlayer.money -= (mortgageCash);
					
					dispose();
					mortgageDialogExists = false;
				}
			});

			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					mortgageDialogExists = false;
				}
			});

			buttonPanel.add(mortgageButton);
			buttonPanel.add(unmortgageButton);
			buttonPanel.add(cancelButton);
			add(buttonPanel, gridBagConstraint);
		}
	}
	public class BuyHouseDialog extends JDialog {
		public JLabel titleLabel, subtitleLabel;
		public JList propertyList;
		public JSlider numOfHouses;
		public JButton okButton, cancelButton;
		public String[] tileNames;
		public JScrollPane scrollPane;
		Tile[] tileArray;

		public BuyHouseDialog(ArrayList<Integer> propertyList, Tile[] tileArray) {
			super();

			this.tileArray = tileArray;
			tileNames = new String[tileArray.length];
			for (int i = 0; i < propertyList.size(); i++) {
				Tile currTile = tileArray[propertyList.get(i)];
				
				if (currTile.isEstate) {
					tileNames[i] = currTile.name;
				}
			}

			setSize(600, 600);
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			setTitle("Buy/Sell Houses");
			setLocationRelativeTo(BoardPanel.this);
			setResizable(true);
			setLayout(new GridBagLayout());
			initComponents();
			setVisible(true);
		}

		public void initComponents() {
			titleLabel = new JLabel("Set Desired Number of Houses");
			titleLabel.setHorizontalAlignment(JLabel.CENTER);
			titleLabel.setFont(new Font("Helvetica", Font.BOLD, 25));

			GridBagConstraints gridBagConstraint = new GridBagConstraints();
			gridBagConstraint.anchor = GridBagConstraints.CENTER;
			gridBagConstraint.gridwidth = 1;
			gridBagConstraint.gridheight = 1;
			gridBagConstraint.fill = GridBagConstraints.BOTH;
			gridBagConstraint.gridx = 0;
			gridBagConstraint.gridy = 0;
			gridBagConstraint.weightx = 1.0;
			gridBagConstraint.weighty = 0.1;

			add(titleLabel, gridBagConstraint);
			

			gridBagConstraint.gridy = 1; //1
			gridBagConstraint.weighty = 1.0;
			

			numOfHouses = new JSlider();
			numOfHouses.setMinimum(0);
			numOfHouses.setMaximum(5);
			numOfHouses.setMajorTickSpacing(1);
			numOfHouses.setPaintTicks(true);
			numOfHouses.setPaintLabels(true);
			numOfHouses.setValue(0);
			numOfHouses.setSnapToTicks(true);

			propertyList = new JList(tileNames);
			propertyList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					int index = ((JList) e.getSource()).getSelectedIndex();
					int zNumOfHouses = tileArray[index].houses;
					

				}
			});

			scrollPane = new JScrollPane(propertyList);
			add(scrollPane, gridBagConstraint);
			
			gridBagConstraint.gridy = 2; //2
			gridBagConstraint.weighty = 0.1;
			
			add(numOfHouses, gridBagConstraint);

			gridBagConstraint.gridy = 3; //3
			gridBagConstraint.weighty = 0.2;

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(1, 2));

			
			okButton = new JButton("Ok");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					

					String propertyName = (String) propertyList.getSelectedValue();
					if (propertyName == null) return;
					Tile property = null;
					
					
					for (Tile currTile : tileArray) {
						if (propertyName.equals(currTile.name)) {
							property = currTile;
						}
					}
					
					if (property.isMortgaged) {
						dispose();
						buyHouseDialogExists = false;
						return;
					}
					
					int preHouses = property.houses;
					//property.houses = numOfHouses.getValue();
					int postHouses = numOfHouses.getValue();
					int houseDiff = postHouses-preHouses;
					int moneyOwed = 0;
					
					if (property.price < 130) {
						moneyOwed = 50 * houseDiff;
					}
					if (property.price > 130 && property.price < 210) {
						moneyOwed = 100 * houseDiff;
					}
					if (property.price > 210 && property.price < 290) {
						moneyOwed = 150 * houseDiff;
					}
					if (property.price > 210) {
						moneyOwed = 200 * houseDiff;
					}
					
					//property.getTilePanel().updateHouses(numOfHouses.getValue());
					Player currPlayer = gameBoard.gamePlay.infoPacket.playerList.get(gameBoard.gamePlay.infoPacket.whoseTurn);
					
					if (currPlayer.money < moneyOwed) {
						JOptionPane.showMessageDialog(
								gameBoard.getBoardPanel(), "Sorry! " + currPlayer.getName() + " does not have the required $"
								+ moneyOwed + " to pay for the requested purchase",
								"House Market", JOptionPane.PLAIN_MESSAGE);
						dispose();
						buyHouseDialogExists = false;
						return;
					}
					
					InformationPacket tPacket = new InformationPacket(gameBoard.gamePlay.infoPacket);
					tPacket.setFalse();
					tPacket.isHouse = true;
					tPacket.numHouses = houseDiff;
					tPacket.moneyOwed = moneyOwed;
					int propertyIndex = -1;
					for(int i = 0; i < gameBoard.tileArray.length; i++){
						if(property.name.equals(gameBoard.tileArray[i].name)){
							propertyIndex = i;
						}
					}
					tPacket.propertyVal = propertyIndex;
					
					gameBoard.sendInfoPacket(tPacket);
					/*currPlayer.money -= (moneyOwed);
					
					if (moneyOwed > 0) {
						JOptionPane.showMessageDialog(
								gameBoard.getBoardPanel(), currPlayer.playerName + " bought " + houseDiff + " house(s) on "
										+ property.name + " for " + moneyOwed,
								"House Market", JOptionPane.PLAIN_MESSAGE);
					}
					if (moneyOwed < 0) {
						JOptionPane.showMessageDialog(
								gameBoard.getBoardPanel(), currPlayer.playerName + " sold " + Math.abs(houseDiff) 
								+ " house(s) on " + property.name + " for " + Math.abs(moneyOwed),
								"House Market", JOptionPane.PLAIN_MESSAGE);
					}
					if (moneyOwed == 0) {
						JOptionPane.showMessageDialog(
								gameBoard.getBoardPanel(), "No houses were bought or sold",
								"House Market", JOptionPane.PLAIN_MESSAGE);
					}*/
					
					dispose();
					buyHouseDialogExists = false;
				}
			});

			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					buyHouseDialogExists = false;
				}
			});

			buttonPanel.add(okButton);
			buttonPanel.add(cancelButton);
			add(buttonPanel, gridBagConstraint);
		}
	}
	public void setMiddle(ImageIcon i, String name) {
		middle.setIcon(i);
		currTurn.setText(name + "'s turn!");
		revalidate();
		repaint();	
	}
	
}

