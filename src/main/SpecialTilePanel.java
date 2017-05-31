package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SpecialTilePanel extends TilePanel
{
	ImageIcon specialIcon;
	String description = "";
	private PlayerBar playerBar;
	Board gameBoard;
	
	SpecialTilePanel(int price, String name, Color tileColor, Board gameBoard)
	{
		setPreferredSize(new Dimension(100, 90));
		JTextPane namePane = new JTextPane();
		setSize(new Dimension(100, 90));
			namePane.setMargin(new Insets(0,0,0,0));
			namePane.setEditable(false);
			namePane.setFont(new Font("Verdana", Font.BOLD, 10));
			namePane.setText(name);
		StyledDocument doc = namePane.getStyledDocument();
		SimpleAttributeSet centerText = new SimpleAttributeSet();
		StyleConstants.setAlignment(centerText, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), centerText, false);
		
		this.gameBoard = gameBoard;
		
		if (name.contains("Chance"))
		{
			specialIcon = new ImageIcon(getClass().getResource("chance_blue.png"));
			description = "";
		}
		else if (name.contains("Chest"))
		{
			specialIcon = new ImageIcon(getClass().getResource("chest.png"));
		}
		else if (name.contains("Luxury"))
		{	
			specialIcon = new ImageIcon(getClass().getResource("luxury.png"));
			description = "PAY $75.00";
		}
		else if (name.contains("Income"))
		{
			specialIcon = new ImageIcon(getClass().getResource("income.png"));
		}
		
		
		JPanel specialPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				((FlowLayout)specialPanel.getLayout()).setVgap(0);
				((FlowLayout)specialPanel.getLayout()).setHgap(0);
		JLabel specialLabel = new JLabel(specialIcon);
			specialPanel.add(specialLabel);
		
		JTextPane descriptionPane = new JTextPane();
			descriptionPane.setPreferredSize(new Dimension(100,15));
			descriptionPane.setMargin(new Insets(0,0,0,0));
			descriptionPane.setText(description);
			descriptionPane.setFont(new Font("Verdana", Font.BOLD, 12));
			descriptionPane.setEditable(false);
		doc = descriptionPane.getStyledDocument();
		centerText = new SimpleAttributeSet();
		StyleConstants.setAlignment(centerText, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), centerText, false);
			
		add(namePane);
		//add(specialPanel);
		add(descriptionPane);
		
		playerBar = new PlayerBar(gameBoard);
		add(playerBar);
	}
	
	public void clearPlayerBar()
	{
		for(JPanel playerPanel : playerBar.playerPanels)
		{
			playerPanel.setVisible(false);
		}
	}
	
	public void updatePlayerBar(Player tempPlayer)
	{
		int i = 0;
		
		for(Player tempP : gameBoard.playerList)
		{			
			if(tempP.playerColor == tempPlayer.playerColor)
			{
				playerBar.playerPanels.get(i).setVisible(true);
				playerBar.paintPanel();
			}
			
			i++;
		}
	}
}
