package networking;

import java.awt.CardLayout;
import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JOptionPane;

import gui.Account;
import gui.JoinHostGUI;
import main.BoardPanel;
import main.Monopoly;
import main.Player;
import main.Tile;

public class Client extends Thread {

	private ObjectInputStream ois;
	public ObjectOutputStream oos;
	public Socket s;
	JoinHostGUI loadFrame;
	String username;
	public Player playerData;
	public Monopoly gamePlay;
	public boolean serverAlso;
	
	public void connect(String hostname, int port, Account username, JoinHostGUI loadFrame) 
	{
		this.loadFrame = loadFrame;
		this.username = username.getName();

		try 
		{
			s = new Socket(hostname, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());

			InformationPacket infoPacket = null;

			while (infoPacket == null) 
			{
				try 
				{
					infoPacket = (InformationPacket) ois.readObject();
				} 
				catch (ClassNotFoundException e) 
				{
					e.printStackTrace();
				}
			}

			infoPacket.playerCount+=1;

			Random random = new Random();
			final float hue = random.nextFloat();
			final float saturation = 0.9f;
			final float luminance = 1.0f; 
			Color color = Color.getHSBColor(hue, saturation, luminance);
			
			playerData = new Player(0, color, username.getName());
			infoPacket.playerList.add(playerData);
			infoPacket.imageList.add(username.getImage());
			playerData.objectCode = infoPacket.codeGen;
			infoPacket.codeGen++;

			if (serverAlso) 
			{
				infoPacket.serverCode = infoPacket.codeGen;
			}


			oos.writeObject(playerData);
			oos.flush();

			oos.writeObject(infoPacket);
			oos.flush();

			this.start();
		}
		catch (IOException ioe) 
		{
			if (loadFrame.hostGameButton.isSelected() && !loadFrame.serverFailed)
			{
				connect(hostname, port, username, loadFrame);
			} 
			else 
			{
				System.out.println("Connect Failed");
			}
		}
	}

	public Client(String hostname, int port, Account username, JoinHostGUI loadFrame, boolean serverAlso) 
	{
		this.serverAlso = serverAlso;
		connect(hostname, port, username, loadFrame);
	}

	public void run() 
	{
		try 
		{
			while (true) 
			{
				InformationPacket infoPacket = (InformationPacket) ois.readObject();

				if (infoPacket != null)
				{
					loadFrame.messageLabel.setText("Waiting For: "
							+ (infoPacket.totalPlayers - infoPacket.playerCount) + " More Players!");
					loadFrame.messageLabel.setVisible(true);

					if (infoPacket.playerCount == infoPacket.totalPlayers) 
					{
						loadFrame.setVisible(false);
						gamePlay = new Monopoly(loadFrame, username, this, infoPacket);
						
						loadFrame.gamePlay = gamePlay;
						break;
					}
				}
			}

			while (true) 
			{
				InformationPacket infoPacket = (InformationPacket) ois.readObject();
				
				if (infoPacket != null) 
				{
					gamePlay.infoPacket = infoPacket;
					gamePlay.gameBoard.playerList = infoPacket.playerList;
					
					if(infoPacket.isMessage)
					{
						gamePlay.gameBoard.boardPanel.sidebar.chatScreen.append(infoPacket.message);
						infoPacket.message = null;
						infoPacket.isMessage = false;
					}
					
					else if(infoPacket.isRoll)
					{
						Player currPlayer = infoPacket.playerList.get(infoPacket.whoseTurn);
						gamePlay.gameBoard.boardPanel.logArea.append("\n" + infoPacket.playerList.get(infoPacket.whoseTurn).getName() + " rolled a " + infoPacket.roll);

						int turn = infoPacket.whoseTurn;
						gamePlay.gameBoard.boardPanel.setMiddle(infoPacket.imageList.get(turn), infoPacket.playerList.get(turn).getName());
						
						if (currPlayer.lastRollWasDoubles)
						{
							if (currPlayer.doubles == 3)
							{
								if(currPlayer.objectCode == (playerData.objectCode)){
								JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
										"You Rolled 3 Doubles in a Row!" + "\nGo to Jail!", "Go to Jail",
										JOptionPane.INFORMATION_MESSAGE);
								
									gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(true);
								}
								else{
									gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
								}
								
								gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Rolled Doubles Thrice! He's Going to SAL!");

								currPlayer.inJail = true;
								currPlayer.jailTime = 3;
								currPlayer.currLocation = 10;
								gamePlay.gameBoard.playerList = infoPacket.playerList;
								gamePlay.gameBoard.updatePlayerLocations();
							} 
							else 
							{
								gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(false);
								
								gamePlay.gameBoard.boardPanel.moveDistance(currPlayer, infoPacket.roll);
								if(gamePlay.gameBoard.tileArray[currPlayer.currLocation].isProperty && gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner != null
										&& currPlayer.objectCode != gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.objectCode && !gamePlay.gameBoard.tileArray[currPlayer.currLocation].isMortgaged)
								{
									if (currPlayer.currLocation == 5 || currPlayer.currLocation == 12 || currPlayer.currLocation == 15
											|| currPlayer.currLocation == 25 || currPlayer.currLocation == 28
											|| currPlayer.currLocation == 35) {
										if (currPlayer.currLocation == 5 || currPlayer.currLocation == 15 || currPlayer.currLocation == 25
												|| currPlayer.currLocation == 35) {
											int money = gamePlay.gameBoard.tileArray[currPlayer.currLocation].rentPrices[currPlayer.railroads];

											JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
													"Someone Else Owns This, You Lose $" + money, "Monopoly", JOptionPane.PLAIN_MESSAGE);

											currPlayer.money -= money;
											
											
											int ownerObjectCode = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.objectCode;
											Player tileOwner = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner;
											for(int i = 0; i < infoPacket.playerList.size(); i++){
												if(infoPacket.playerList.get(i).objectCode == ownerObjectCode){
													tileOwner = infoPacket.playerList.get(i);
												}
											}
											
											

											gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + tileOwner.getName());

											gamePlay.gameBoard.boardPanel.updateScores();
											gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.money += money;
											gamePlay.gameBoard.boardPanel.updateScores();
										} else if (currPlayer.currLocation == 28) {
											Random r = new Random();
											int roll = r.nextInt(12);

											roll += 1;

											Player tileOwner = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner;

											if (!tileOwner.electrics) {
												int money = roll * 4;

												JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
														+roll + "\n" + tileOwner.playerName + " Owns This, You Lose $" + money, "Monopoly",
														JOptionPane.PLAIN_MESSAGE);

												currPlayer.money -= money;
												gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + tileOwner.getName());
												
												tileOwner.money += money;
											} else {
												int money = roll * 10;

												JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
														"You Rolled a " + roll + "\n" + tileOwner.playerName
																+ " Owns This And Electric You Lose $" + money,
														"Monopoly", JOptionPane.PLAIN_MESSAGE);

												currPlayer.money -= money;
												gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.getName());
												tileOwner.money += money;
												
											}

										} else if (currPlayer.currLocation == 12) {
											Random r = new Random();
											int roll = r.nextInt(12);

											roll += 1;

											Player tileOwner = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner;

											if (!tileOwner.waters) {
												int money = roll * 4;

												JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
														"You Rolled a " + roll + "\n" + tileOwner.playerName + " Owns This, You Lose $" + money,
														"Monopoly", JOptionPane.PLAIN_MESSAGE);

												currPlayer.money -= money;
												gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + tileOwner.getName());

												tileOwner.money += money;
											} else {
												int money = roll * 10;

												JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
														"You Rolled a " + roll + "\n" + tileOwner.playerName
																+ " Owns This And Waters You Lose $" + money,
														"Monopoly", JOptionPane.PLAIN_MESSAGE);

												currPlayer.money -= money;
												gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + tileOwner.getName());

												tileOwner.money += money;
												gamePlay.gameBoard.boardPanel.updateScores();
											}
										}
									} else {
										int ownerObjectCode = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.objectCode;
										Player tileOwner = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner;
										for(int i = 0; i < infoPacket.playerList.size(); i++){
											if(infoPacket.playerList.get(i).objectCode == ownerObjectCode){
												tileOwner = infoPacket.playerList.get(i);
											}
										}

										int houses = gamePlay.gameBoard.tileArray[currPlayer.currLocation].houses;
										int money = gamePlay.gameBoard.tileArray[currPlayer.currLocation].rentPrices[houses];

										JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel, "Someone Else Owns This, You Lose $" + money,
												"Monopoly", JOptionPane.PLAIN_MESSAGE);

										currPlayer.money -= money;
										gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + tileOwner.getName());

										tileOwner.money += money;
										gamePlay.gameBoard.boardPanel.updateScores();
									}
								
								}
								if(currPlayer.currLocation == 10 && currPlayer.inJail)
								{
									gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(false);
									
									if(infoPacket.playerList.get(infoPacket.whoseTurn).objectCode == playerData.objectCode)
									{
										gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
										gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
										gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(true);
										gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(false);
										gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(false);
										gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(true);
									}
								}
								else if(currPlayer.objectCode == playerData.objectCode)
								{
									gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(true);
									if(gamePlay.gameBoard.tileArray[currPlayer.currLocation].isProperty && (gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner == null))
										gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(true);
									
									gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(true);
									gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(true);
									gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(true);
								}
							}
						} 
						else //if not doubles
						{
							
							gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(false);
							
							gamePlay.gameBoard.boardPanel.moveDistance(currPlayer, infoPacket.roll);
							if(gamePlay.gameBoard.tileArray[currPlayer.currLocation].isProperty && gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner != null
									&& currPlayer.objectCode != gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.objectCode && !gamePlay.gameBoard.tileArray[currPlayer.currLocation].isMortgaged)
							{
								if (currPlayer.currLocation == 5 || currPlayer.currLocation == 12 || currPlayer.currLocation == 15
										|| currPlayer.currLocation == 25 || currPlayer.currLocation == 28
										|| currPlayer.currLocation == 35) {
									if (currPlayer.currLocation == 5 || currPlayer.currLocation == 15 || currPlayer.currLocation == 25
											|| currPlayer.currLocation == 35) {
										int money = gamePlay.gameBoard.tileArray[currPlayer.currLocation].rentPrices[currPlayer.railroads];

										JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
												"Someone Else Owns This, You Lose $" + money, "Monopoly", JOptionPane.PLAIN_MESSAGE);

										currPlayer.money -= money;
										gamePlay.gameBoard.boardPanel.updateScores();
										gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.money += money;
										gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.getName());
										gamePlay.gameBoard.boardPanel.updateScores();
									} else if (currPlayer.currLocation == 28) {
										Random r = new Random();
										int roll = r.nextInt(12);

										roll += 1;

										int ownerObjectCode = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.objectCode;
										Player tileOwner = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner;
										for(int i = 0; i < infoPacket.playerList.size(); i++){
											if(infoPacket.playerList.get(i).objectCode == ownerObjectCode){
												tileOwner = infoPacket.playerList.get(i);
											}
										}

										if (!tileOwner.electrics) {
											int money = roll * 4;

											JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
													+roll + "\n" + tileOwner.playerName + " Owns This, You Lose $" + money, "Monopoly",
													JOptionPane.PLAIN_MESSAGE);

											currPlayer.money -= money;
											gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.getName());
											tileOwner.money += money;
											gamePlay.gameBoard.boardPanel.updateScores();
										} else {
											int money = roll * 10;

											JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
													"You Rolled a " + roll + "\n" + tileOwner.playerName
															+ " Owns This And Electric You Lose $" + money,
													"Monopoly", JOptionPane.PLAIN_MESSAGE);

											currPlayer.money -= money;
											gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.getName());
											tileOwner.money += money;
											gamePlay.gameBoard.boardPanel.updateScores();
										}

									} else if (currPlayer.currLocation == 12) {
										Random r = new Random();
										int roll = r.nextInt(12);

										roll += 1;

										int ownerObjectCode = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.objectCode;
										Player tileOwner = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner;
										for(int i = 0; i < infoPacket.playerList.size(); i++){
											if(infoPacket.playerList.get(i).objectCode == ownerObjectCode){
												tileOwner = infoPacket.playerList.get(i);
											}
										}

										if (!tileOwner.waters) {
											int money = roll * 4;

											JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
													"You Rolled a " + roll + "\n" + tileOwner.playerName + " Owns This, You Lose $" + money,
													"Monopoly", JOptionPane.PLAIN_MESSAGE);

											currPlayer.money -= money;
											gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.getName());
											tileOwner.money += money;
											gamePlay.gameBoard.boardPanel.updateScores();
										} else {
											int money = roll * 10;

											JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
													"You Rolled a " + roll + "\n" + tileOwner.playerName
															+ " Owns This And Waters You Lose $" + money,
													"Monopoly", JOptionPane.PLAIN_MESSAGE);

											currPlayer.money -= money;
											gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.getName());
											tileOwner.money += money;
											gamePlay.gameBoard.boardPanel.updateScores();
										}
										
									}
								} else {
									int houses = gamePlay.gameBoard.tileArray[currPlayer.currLocation].houses;
									int money = gamePlay.gameBoard.tileArray[currPlayer.currLocation].rentPrices[houses];
									int ownerObjectCode = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.objectCode;
									Player tileOwner = gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner;
									for(int i = 0; i < infoPacket.playerList.size(); i++){
										if(infoPacket.playerList.get(i).objectCode == ownerObjectCode){
											tileOwner = infoPacket.playerList.get(i);
										}
									}

									JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel, "Someone Else Owns This, You Lose $" + money,
											"Monopoly", JOptionPane.PLAIN_MESSAGE);

									currPlayer.money -= money;
									gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.getName() + " Lost $" + money + " to " + gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.getName());
									
									
									tileOwner.money += money;
									//infoPacket.playerList.get(0).money += money;
									gamePlay.gameBoard.boardPanel.updateScores();
									//System.out.println(gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner.money);
									
								}
							
							}
							if(currPlayer.inJail){
								gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(false);
								
								if(infoPacket.playerList.get(infoPacket.whoseTurn).objectCode == playerData.objectCode)
								{
									gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(true);
									gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(true);
									gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(false);
								}
							}
							else if(currPlayer.objectCode == (playerData.objectCode)){
								gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(true);
								if(gamePlay.gameBoard.tileArray[currPlayer.currLocation].isProperty && (gamePlay.gameBoard.tileArray[currPlayer.currLocation].tileOwner == null))
									gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(true);
									
							}
							else{
								gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(false);
							}
						}
						
						gamePlay.gameBoard.boardPanel.updateScores();
					}
					else if(infoPacket.isJailRoll){
						
						Player currPlayer = gamePlay.infoPacket.playerList.get(gamePlay.infoPacket.whoseTurn);
						
						if(currPlayer.lastRollWasDoubles)
						{
							gamePlay.gameBoard.boardPanel.logArea.append("\n" + infoPacket.playerList.get(infoPacket.whoseTurn).getName() + "Rolled a " + infoPacket.rollJailVal[0] + " & " +infoPacket. rollJailVal[1] + "! They are Free!");
						}
						else
						{
							gamePlay.gameBoard.boardPanel.logArea.append("\n" + gamePlay.infoPacket.playerList.get(gamePlay.infoPacket.whoseTurn).getName() + "Rolled a " + infoPacket.rollJailVal[0] + " & " + infoPacket.rollJailVal[1] + "! They are Not Free!");
						}
						
						if(currPlayer.lastRollWasDoubles){
							if (gamePlay.infoPacket.playerList.get(gamePlay.infoPacket.whoseTurn).objectCode == playerData.objectCode) {
								JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
										"You Rolled a " + infoPacket.rollJailVal[0] + " & " + infoPacket.rollJailVal[1] + "\nCongratulations! You Are Free!",
										"Escape Jail?", JOptionPane.INFORMATION_MESSAGE);
								
							}
							currPlayer.inJail = false;
							currPlayer.jailTime = 0;

							gamePlay.gameBoard.tileArray[10].getTilePanel().updatePlayerBar(currPlayer);
							} else {
								if(gamePlay.infoPacket.playerList.get(gamePlay.infoPacket.whoseTurn).objectCode == playerData.objectCode){
									JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel,
											"You Rolled a " + infoPacket.rollJailVal[0] + " & " + infoPacket.rollJailVal[1] + "\nFailure! You Are Still In Jail!",
											"Escape Jail?", JOptionPane.INFORMATION_MESSAGE);
							}
						}
						
						gamePlay.gameBoard.boardPanel.sidebar.pay50.setEnabled(false);
						gamePlay.gameBoard.boardPanel.sidebar.useCard.setEnabled(false);
						gamePlay.gameBoard.boardPanel.sidebar.rollInJail.setEnabled(false);
						if(gamePlay.infoPacket.playerList.get(gamePlay.infoPacket.whoseTurn).objectCode == playerData.objectCode){
							gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(true);
						}else
							gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(false);
					
					}
					else if(infoPacket.isPay50){
						Player currPlayer = gamePlay.infoPacket.playerList.get(gamePlay.infoPacket.whoseTurn);
						gamePlay.gameBoard.boardPanel.logArea.append("\n" + infoPacket.playerList.get(infoPacket.whoseTurn).getName() + "Paid $50 to Get Out of SAL!");

						currPlayer.money-=50;
						gamePlay.gameBoard.boardPanel.updateScores();
						
						currPlayer.inJail = false;
						currPlayer.jailTime = 0;
						gamePlay.gameBoard.tileArray[10].getTilePanel().updatePlayerBar(currPlayer);
						
						gamePlay.gameBoard.boardPanel.sidebar.pay50.setEnabled(false);
						gamePlay.gameBoard.boardPanel.sidebar.useCard.setEnabled(false);
						gamePlay.gameBoard.boardPanel.sidebar.rollInJail.setEnabled(false);
						
						if(gamePlay.infoPacket.playerList.get(gamePlay.infoPacket.whoseTurn).objectCode == playerData.objectCode)
							gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(true);
						else
							gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(false);
					}
					else if(infoPacket.isUseCard){
						Player currPlayer = gamePlay.infoPacket.playerList.get(gamePlay.infoPacket.whoseTurn);
						gamePlay.gameBoard.boardPanel.logArea.append("\n" + infoPacket.playerList.get(infoPacket.whoseTurn).getName() + "Used a Get Out of SAL Free Card!");

						currPlayer.inJail = false;
						currPlayer.getOutOfJail = false;
						currPlayer.jailTime = 0;
						gamePlay.gameBoard.tileArray[10].getTilePanel().updatePlayerBar(currPlayer);

						gamePlay.gameBoard.boardPanel.sidebar.pay50.setEnabled(false);
						gamePlay.gameBoard.boardPanel.sidebar.useCard.setEnabled(false);
						gamePlay.gameBoard.boardPanel.sidebar.rollInJail.setEnabled(false);
						
						if(gamePlay.infoPacket.playerList.get(gamePlay.infoPacket.whoseTurn).objectCode == playerData.objectCode)
							gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(true);
						else
							gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(false);
					}
					else if(infoPacket.isJailEnd){

						int turn = infoPacket.whoseTurn;
						gamePlay.gameBoard.boardPanel.logArea.append("\nIt is " + infoPacket.playerList.get(turn).getName() + "'s Turn");

						Player oldPlayer = gamePlay.infoPacket.playerList.get(gamePlay.infoPacket.jailPlayerEnded);
						oldPlayer.jailTime--;
						
						gamePlay.gameBoard.whoseTurn = turn;
						
						Player newPlayer = gamePlay.infoPacket.playerList.get(gamePlay.infoPacket.whoseTurn);

						
						for(Player p: infoPacket.playerList){
							if(p.objectCode == playerData.objectCode){
								playerData = p;
							}
						}
						
						
						if(!playerData.inJail)
						{
							CardLayout c1 = (CardLayout)(gamePlay.gameBoard.boardPanel.sidebar.cardPanel.getLayout());
							c1.show(gamePlay.gameBoard.boardPanel.sidebar.cardPanel, "normal");
							gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(false);
							if(infoPacket.playerList.get(turn).objectCode == (playerData.objectCode)){
								gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(true);
								if(gamePlay.gameBoard.tileArray[infoPacket.playerList.get(turn).currLocation].isProperty && (gamePlay.gameBoard.tileArray[infoPacket.playerList.get(turn).currLocation].tileOwner == null))
									gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(true);
							}
						}
						
						else 
						{
							if(oldPlayer.jailTime == 0)
							{
								oldPlayer.inJail = false;
								oldPlayer.jailTime = 0;
								oldPlayer.money-=50;
								gamePlay.gameBoard.playerList = infoPacket.playerList;
								
								gamePlay.gameBoard.tileArray[10].getTilePanel().updatePlayerBar(oldPlayer);
								
								CardLayout c1 = (CardLayout)(gamePlay.gameBoard.boardPanel.sidebar.cardPanel.getLayout());
								c1.show(gamePlay.gameBoard.boardPanel.sidebar.cardPanel, "normal");
								
								gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(false);
								
								if(oldPlayer.objectCode == playerData.objectCode)
								{
									gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(true);
									if(gamePlay.gameBoard.tileArray[infoPacket.playerList.get(turn).currLocation].isProperty && (gamePlay.gameBoard.tileArray[infoPacket.playerList.get(turn).currLocation].tileOwner == null))
										gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(true);
									gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(true);
									gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(true);
									gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(true);
								
								
									JOptionPane.showMessageDialog(gamePlay.gameBoard.boardPanel, "You Have Been In Jail for 3 Turns! Pay 50 and Leave!" + "\nYou are Free!", "You are Free",
											JOptionPane.INFORMATION_MESSAGE);									
								}
								
								gamePlay.gameBoard.boardPanel.updateScores();
							}
							else
							{
								CardLayout c1 = (CardLayout)(gamePlay.gameBoard.boardPanel.sidebar.cardPanel.getLayout());
								c1.show(gamePlay.gameBoard.boardPanel.sidebar.cardPanel, "jail");
								gamePlay.gameBoard.boardPanel.sidebar.rollInJail.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.useCard.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.pay50.setEnabled(false);
								
								if(infoPacket.playerList.get(turn).objectCode == (playerData.objectCode))
								{
								
									gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(false);
									gamePlay.gameBoard.boardPanel.sidebar.useCard.setEnabled(true);
									gamePlay.gameBoard.boardPanel.sidebar.pay50.setEnabled(true);
									gamePlay.gameBoard.boardPanel.sidebar.rollInJail.setEnabled(true);
								}
							}
						}
						
					}
					else if(infoPacket.isIncomeTax)
					{
						if(!infoPacket.incomeChoice)
						{
							int percentage = infoPacket.playerList.get(infoPacket.whoseTurn).money / 10;
							infoPacket.playerList.get(infoPacket.whoseTurn).money -= percentage;
							gamePlay.gameBoard.boardPanel.updateScores();
							
							gamePlay.gameBoard.boardPanel.logArea.append("\n" + infoPacket.playerList.get(infoPacket.whoseTurn).getName() + " Lost $" + percentage + " to Income Tax!");
						}
						else
						{
							infoPacket.playerList.get(infoPacket.whoseTurn).money -= 200;
							gamePlay.gameBoard.boardPanel.updateScores();
							
							gamePlay.gameBoard.boardPanel.logArea.append("\n" + infoPacket.playerList.get(infoPacket.whoseTurn).getName() + " Lost $" + 200 + " to Income Tax!");
						}
					}
					
					else if(infoPacket.isEndTurn)
					{
						int turn = infoPacket.whoseTurn;
						
						gamePlay.gameBoard.whoseTurn = turn;
						gamePlay.gameBoard.boardPanel.logArea.append("\nIt is " + infoPacket.playerList.get(turn).getName() + "'s Turn");

						gamePlay.gameBoard.boardPanel.setMiddle(infoPacket.imageList.get(turn), infoPacket.playerList.get(turn).getName());
						for(Player p: infoPacket.playerList){
							if(p.objectCode == playerData.objectCode){
								playerData = p;
							}
						}
						
						
						if(!playerData.inJail)
						{
							CardLayout c1 = (CardLayout)(gamePlay.gameBoard.boardPanel.sidebar.cardPanel.getLayout());
							gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(false);
							if(infoPacket.playerList.get(turn).objectCode == (playerData.objectCode)){
								gamePlay.gameBoard.boardPanel.sidebar.rollDice.setEnabled(true);
								if(gamePlay.gameBoard.tileArray[infoPacket.playerList.get(turn).currLocation].isProperty && (gamePlay.gameBoard.tileArray[infoPacket.playerList.get(turn).currLocation].tileOwner == null))
									gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.endTurn.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.addHouseHotel.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.mortgageProperty.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.declareBankrupt.setEnabled(true);
								
								c1.show(gamePlay.gameBoard.boardPanel.sidebar.cardPanel, "normal");
							}
						}
						
						else 
						{
							CardLayout c1 = (CardLayout)(gamePlay.gameBoard.boardPanel.sidebar.cardPanel.getLayout());
							c1.show(gamePlay.gameBoard.boardPanel.sidebar.cardPanel, "jail");
							gamePlay.gameBoard.boardPanel.sidebar.rollInJail.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.useCard.setEnabled(false);
							gamePlay.gameBoard.boardPanel.sidebar.pay50.setEnabled(false);
							
							if(infoPacket.playerList.get(turn).objectCode == (playerData.objectCode)){
							
								gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.useCard.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.pay50.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.rollInJail.setEnabled(true);
							}
							/*if(infoPacket.playerList.get(turn).objectCode == playerData.objectCode){
								gamePlay.gameBoard.boardPanel.sidebar.rollInJail.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.pay50.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(false);
								gamePlay.gameBoard.boardPanel.sidebar.useCard.setEnabled(false);
							}else{
								gamePlay.gameBoard.boardPanel.sidebar.rollInJail.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.pay50.setEnabled(true);
								gamePlay.gameBoard.boardPanel.sidebar.jailEndButton.setEnabled(false);
								
								
								
								if(gamePlay.gameBoard.playerList.get(turn).getOutOfJail)
								{
									gamePlay.gameBoard.boardPanel.sidebar.useCard.setEnabled(true);
								}
								else
								{
									gamePlay.gameBoard.boardPanel.sidebar.useCard.setEnabled(false);
								}
								
								gamePlay.gameBoard.boardPanel.jailEndButton.setEnabled(false);
							}*/
						}
					}
					else if(infoPacket.isBuyProperty){
						int location = gamePlay.gameBoard.playerList.get(infoPacket.whoseTurn).currLocation;
						Player currPlayer = gamePlay.gameBoard.playerList.get(infoPacket.whoseTurn);
						gamePlay.gameBoard.boardPanel.logArea.append("\n" + infoPacket.playerList.get(infoPacket.whoseTurn).getName() + " Bought " + gamePlay.gameBoard.tileArray[gamePlay.gameBoard.gamePlay.infoPacket.playerList.get(infoPacket.whoseTurn).currLocation].name.replace("\n", " "));

						
						currPlayer.money -= gamePlay.gameBoard.tileArray[location].price;
						gamePlay.gameBoard.boardPanel.updateScores();

						gamePlay.gameBoard.tileArray[location].tileOwner = currPlayer;
						currPlayer.propertyList.add(location);

						if (location == 5 || location == 12 || location == 15
								|| location == 25 || location == 28
								|| location == 35) {
							currPlayer.railroads++;
						} else if (location == 28) {
							currPlayer.waters = true;
						} else if (location == 12) {
							currPlayer.electrics = true;
						}

						gamePlay.gameBoard.tileArray[location].getTilePanel().updateOwnerPane();
						gamePlay.gameBoard.boardPanel.sidebar.buyProperty.setEnabled(false);
						
					}
					else if(infoPacket.isMortgage){
						Tile property = gamePlay.gameBoard.tileArray[infoPacket.propertyVal];
						property.isMortgaged = true;
						int mortgageCash = property.price / 2;
						Player currPlayer = infoPacket.playerList.get(infoPacket.whoseTurn);
						currPlayer.money += (mortgageCash);
						JOptionPane
								.showMessageDialog(
										gamePlay.gameBoard.getBoardPanel(), currPlayer.playerName + " has been reimbursed $"
												+ mortgageCash + " for mortgaging " + property.name,
										"Monopoly", JOptionPane.PLAIN_MESSAGE);
						
						gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.playerName + " has been reimbursed $"
								+ mortgageCash + " for mortgaging " + property.name.replace("\n", " "));

						gamePlay.gameBoard.boardPanel.updateScores();
					}
					else if(infoPacket.isUnMortgage){
						Tile property = gamePlay.gameBoard.tileArray[infoPacket.propertyVal];
						property.isMortgaged = false;
						int mortgageCash = property.price / 2 + property.price / 10;
						Player currPlayer = infoPacket.playerList.get(infoPacket.whoseTurn);
						currPlayer.money -= (mortgageCash);
						JOptionPane
								.showMessageDialog(
										gamePlay.gameBoard.getBoardPanel(), currPlayer.playerName + " has been charged $"
												+ mortgageCash + " for unmortgaging " + property.name,
										"Monopoly", JOptionPane.PLAIN_MESSAGE);
						
						gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.playerName + " has been charged $"
								+ mortgageCash + " for unmortgaging " + property.name.replace("\n", " "));
						gamePlay.gameBoard.boardPanel.updateScores();
					}
					else if(infoPacket.isHouse){
						Player currPlayer = gamePlay.gameBoard.playerList.get(infoPacket.whoseTurn);
						Tile property = gamePlay.gameBoard.tileArray[infoPacket.propertyVal];
						property.houses += infoPacket.numHouses;
						currPlayer.money -= (infoPacket.moneyOwed);
						
						if (infoPacket.moneyOwed > 0) {
							JOptionPane.showMessageDialog(
									gamePlay.gameBoard.getBoardPanel(), currPlayer.playerName + " bought " + infoPacket.numHouses + " house(s) on "
											+ property.name + " for " + infoPacket.moneyOwed,
									"House Market", JOptionPane.PLAIN_MESSAGE);
							
							gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.playerName + " bought " + infoPacket.numHouses + " house(s) on "
									+ property.name.replace("\n", " ") + " for " + infoPacket.moneyOwed);
						}
						if (infoPacket.moneyOwed < 0) {
							JOptionPane.showMessageDialog(
									gamePlay.gameBoard.getBoardPanel(), currPlayer.playerName + " sold " + Math.abs(infoPacket.numHouses) 
									+ " house(s) on " + property.name + " for " + Math.abs(infoPacket.moneyOwed),
									"House Market", JOptionPane.PLAIN_MESSAGE);
							
							gamePlay.gameBoard.boardPanel.logArea.append("\n" + currPlayer.playerName + " sold " + Math.abs(infoPacket.numHouses) 
							+ " house(s) on " + property.name.replace("\n", " ") + " for " + Math.abs(infoPacket.moneyOwed));
						}
						if (infoPacket.moneyOwed == 0) {
							if(infoPacket.playerList.get(infoPacket.whoseTurn).objectCode == playerData.objectCode)
							{
								JOptionPane.showMessageDialog(
										gamePlay.gameBoard.getBoardPanel(), "No houses were bought or sold",
										"House Market", JOptionPane.PLAIN_MESSAGE);
							}
						}
						gamePlay.gameBoard.boardPanel.updateScores();
						property.getTilePanel().updateHouses(property.houses);
					}
				}
			}
		}
		catch (ClassNotFoundException cnfe) 
		{
			System.out.println("cnfe: " + cnfe.getMessage());
		} 
		catch (IOException ioe) 
		{
			if (s != null) 
			{
				try 
				{
					if (gamePlay == null) 
					{
						s.close();
						loadFrame.messageLabel.setText("Host Cancelled. Choose Another Game to Join!");

						loadFrame.hostGameButton.setEnabled(true);
						loadFrame.joinGameButton.setEnabled(true);
						loadFrame.portTextField.setEnabled(true);
						loadFrame.hostTextField.setEnabled(true);
						loadFrame.hostButton.setEnabled(true);
						loadFrame.joinButton.setEnabled(true);
						//loadFrame.clearChoices.setEnabled(true);
					} 
					else 
					{
						gamePlay.gameBoard.setVisible(false);
						//JoinHostGUI loadFrame = new JoinHostGUI(username);
						//loadFrame.setVisible(true);
						gamePlay = null;
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		} 
		finally 
		{
			try 
			{
				if (s != null) 
				{
					s.close();
				}
			} 
			catch (IOException ioe) 
			{
				System.out.println("ioe: " + ioe.getMessage());
			}
		}
	}
}
