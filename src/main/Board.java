package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;

import decks.Chance;
import decks.CommunityChest;
import gui.JoinHostGUI;
import networking.Client;
import networking.InformationPacket;

public class Board extends JFrame
{
	private static final long serialVersionUID = 1L;
	Color purple = new Color(133,6,191);
	Color lightBlue = new Color(91,152,255);
	Color navy  = new Color(39,6,184);
	
	public BoardPanel boardPanel;
	public Tile[] tileArray = new Tile[40];
	public int whoseTurn;
	public ArrayList<Player> playerList;
	public Chance chanceDeck;
	public CommunityChest communityChestDeck;
	public Monopoly gamePlay;
	public Client client;
	public String username;
	public Dice playerDice;
	
	public Board(String username, Monopoly gamePlay, JoinHostGUI loadFrame, Client client)
	{
		this.gamePlay = gamePlay;
		this.client = client;
		this.username = username;
		
		playerList = gamePlay.infoPacket.playerList;
		
		chanceDeck = gamePlay.infoPacket.chanceDeck;
		communityChestDeck = gamePlay.infoPacket.communityChestDeck;
		
		for(Player tempPlayer : playerList)
		{
			tempPlayer.otherPlayers = playerList;
		}
		
		whoseTurn = gamePlay.infoPacket.whoseTurn;
		initializeTiles();
		
		Sidebar sidebar = new Sidebar(playerList);
		boardPanel = new BoardPanel(tileArray, this, sidebar);
		boardPanel.setPreferredSize(new Dimension(1100, 990));
		add(boardPanel);
		add(sidebar, BorderLayout.EAST);
				
		setTitle("Monopoly");
		pack();
		setResizable(false);
		setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		updatePlayerLocations();

		setVisible(true);
	}
	
	public void sendInfoPacket(InformationPacket iPacket)
	{
		InformationPacket tempPacket = new InformationPacket(iPacket);
		
		try 
		{
			client.oos.writeObject(tempPacket);
			client.oos.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	// Creates all tile objects.
	// code on the end indicates the tile type.
	// 0 = standard property tiles
	// 1 = big tiles
	// 2 = utility tiles
	// 3 = special event tiles
	public void initializeTiles()
	{
		int[] railRoadPrices = {25, 50, 100, 200};

		tileArray[0] = new Tile(0, "GO!", Color.white, 1, this, false, null, false);
		
		int[] rentPrices = {2, 10, 30, 90, 160, 250};
		tileArray[1] = new Tile(60, "Fluor", navy, 0, this, true, rentPrices, true);
		
		tileArray[2] = new Tile(0, "Community\nChest", Color.white, 3, this, false, null, false);
		
		int[] rentPrices2 = {4, 20, 60, 180, 320, 450};
		tileArray[3] = new Tile(60, "Web", navy, 0, this, true, rentPrices2, true);
		
		tileArray[4] = new Tile(0, "Income\nTax", Color.white, 3, this, false, null, false);
		
		tileArray[5] = new Tile(200, "Everybody's\nKitchen", Color.white, 2, this, true, railRoadPrices, false);
		
		int[] rentPrices3 = {6, 30, 90, 270, 400, 550};
		tileArray[6] = new Tile(100, "SOS", lightBlue, 0, this, true, rentPrices3, true);
		
		tileArray[7] = new Tile(0, "Chance", Color.white, 3, this, false, null, false);
		
		int[] rentPrices4 = {6, 30, 90, 270, 400, 550};
		tileArray[8] = new Tile(100, "THH", lightBlue, 0, this, true, rentPrices4, true);
		
		int[] rentPrices5 = {8, 40, 100, 300, 450, 600};
		tileArray[9] = new Tile(120, "VKC", lightBlue, 0, this, true, rentPrices5, true);
		
		tileArray[10] = new Tile(0, "In SAL", Color.white, 1, this, false, null, false);
		
		int[] rentPrices6 = {10, 50, 150, 450, 625, 750};
		tileArray[11] = new Tile(140, "Parkside\nAPTS", purple, 0, this, true, rentPrices6, true);
		
		
		tileArray[12] = new Tile(150, "USC\nWIFI", Color.white, 2, this, true, null, false);
		
		int[] rentPrices7 = {10, 50, 150, 450, 625, 750};
		tileArray[13] = new Tile(140, "Parkside\nA&H", purple, 0, this, true, rentPrices7, true);
		
		int[] rentPrices8 = {12, 60, 180, 500, 700, 900};
		tileArray[14] = new Tile(160, "Parkside\nIRC", purple, 0, this, true, rentPrices8, true);
		
		tileArray[15] = new Tile(200, "Cafe\n84", Color.white, 2, this, true, railRoadPrices, false);
		
		int[] rentPrices9 = {14, 70, 200, 550, 750, 950};
		tileArray[16] = new Tile(180, "Trojan\nHall", Color.orange, 0, this, true, rentPrices9, true);
		
		tileArray[17] = new Tile(0, "Community\nChest", Color.white, 3, this, false, null, false);
		
		int[] rentPrices10 = {14, 70, 200, 550, 750, 950};
		tileArray[18] = new Tile(180, "Marks\nTower", Color.orange, 0, this, true, rentPrices10, true);
		
		int[] rentPrices11 = {16, 80, 220, 600, 800, 1000};
		tileArray[19] = new Tile(200, "Pardee", Color.orange, 0, this, true, rentPrices11, true);
		
		
		tileArray[20] = new Tile(0, "Extra Credit", Color.white, 1, this, false, null, false);
		
		int[] rentPrices12 = {18, 90, 250, 700, 875, 1050};
		tileArray[21] = new Tile(220, "Leavey", Color.red, 0, this, true, rentPrices12, true);
		
		
		tileArray[22] = new Tile(0, "Chance", Color.white, 3, this, false, null, false);
		
		int[] rentPrices13 = {18, 90, 250, 700, 875, 1050};
		tileArray[23] = new Tile(220, "Doheny", Color.red, 0, this, true, rentPrices13, true);
		
		int[] rentPrices14 = {20, 100, 300, 750, 925, 1100};
		tileArray[24] = new Tile(240, "Mudd", Color.red, 0, this, true, rentPrices14, true);
		
		
		tileArray[25] = new Tile(200, "Parkside\nResturant", Color.white, 2, this, true, railRoadPrices, false);
		
		int[] rentPrices15 = {22, 110, 330, 800, 975, 1150};
		tileArray[26] = new Tile(260, "New", Color.yellow, 0, this, true, rentPrices15, true);
		
		int[] rentPrices16 = {22, 110, 330, 800, 975, 1150};
		tileArray[27] = new Tile(260, "North", Color.yellow, 0, this, true, rentPrices16, true);
		
		tileArray[28] = new Tile(150, "Drought\nTax", Color.white, 2, this, true, null, false);
		
		int[] rentPrices17 = {24, 120, 360, 850, 1025, 1200};
		tileArray[29] = new Tile(280, "Birkrant", Color.yellow, 0, this, true, rentPrices17, true);
		
		tileArray[30] = new Tile(0, "Go To SAL", Color.white, 1, this, false, null, false);
		
		int[] rentPrices18 = {26, 130, 390, 900, 1100, 1275};
		tileArray[31] = new Tile(300, "GFS", Color.green, 0, this, true, rentPrices18, true);
		
		int[] rentPrices19 = {26, 130, 390, 900, 1100, 1275};
		tileArray[32] = new Tile(300, "SGM", Color.green, 0, this, true, rentPrices19, true);
		
		
		tileArray[33] = new Tile(0, "Community\nChest", Color.white, 3, this, false, null, false);
		
		int[] rentPrices20 = {28, 150, 450, 1000, 1200, 1400};
		tileArray[34] = new Tile(320, "KAP", Color.green, 0, this, true, rentPrices20, true);
		
		tileArray[35] = new Tile(200, "Campus\nCenter", Color.white, 2, this, true, railRoadPrices, false);
		
		
		tileArray[36] = new Tile(0, "Chance", Color.white, 3, this, false, null, false);
		
		int[] rentPrices21 = {35, 175, 500, 1100, 1300, 1500};
		tileArray[37] = new Tile(350, "USC\nBookstore", Color.blue, 0, this, true, rentPrices21, true);
		
		tileArray[38] = new Tile(0, "Tuition\nIncrease", Color.white, 3, this, true, null, false);
		
		int[] rentPrices22 = {50, 200, 600, 1400, 1700, 2000};
		tileArray[39] = new Tile(400,  "USC\nHealth Center", Color.blue, 0, this, true, rentPrices22, true);		
	}
	
	public void updatePlayerLocations()
	{
		for(Tile tilePanel : tileArray)
		{
			tilePanel.getTilePanel().clearPlayerBar();
		}
		
		for(Player tempPlayer : gamePlay.infoPacket.playerList)
		{
			tileArray[tempPlayer.currLocation].getTilePanel().updatePlayerBar(tempPlayer);
		}
	}
	
	public BoardPanel getBoardPanel()
	{
		return boardPanel;
	}
}
