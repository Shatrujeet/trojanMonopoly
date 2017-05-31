package main;
import gui.Account;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class PlayerBar extends JPanel
{
	public ArrayList<JPanel> playerPanels;	
	public Board gameBoard;
	
	public Account user;
	
	public PlayerBar(Board gameBoard)
	{
		this.gameBoard = gameBoard;
		setBackground(Color.WHITE);
		
		setLayout(new GridLayout(1, gameBoard.playerList.size()));

		this.playerPanels = new ArrayList<JPanel>();
		
		for(Player tempPlayer : gameBoard.playerList)
		{
			JPanel tempPanel = new JPanel();
			tempPanel.setBorder(new LineBorder(Color.BLACK));
			tempPanel.setBackground(tempPlayer.playerColor);
			
			playerPanels.add(tempPanel);
			tempPanel.setVisible(false);
			add(tempPanel);
		}
	}
	
	public void paintPanel()
	{
		for(int i = 0; i < gameBoard.playerList.size(); i++)
		{
			if(gameBoard.playerList.get(i).inJail)
			{
				playerPanels.get(i).setBackground(Color.WHITE);
			}
			else
			{
				playerPanels.get(i).setBackground(gameBoard.playerList.get(i).playerColor);
			}
		}
	}
}
