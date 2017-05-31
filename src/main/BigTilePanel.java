package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class BigTilePanel extends TilePanel
{
	ImageIcon bigIcon;
	String description = "";
	private PlayerBar playerBar;
	Board gameBoard;
	
	BigTilePanel(int price, String name, Color tileColor, Board gameBoard)
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		setSize(new Dimension(100, 90));
		setBackground(Color.WHITE);
		
		this.gameBoard = gameBoard;

		JTextPane namePane = new JTextPane();
			namePane.setPreferredSize(new Dimension(100,40));
			namePane.setMargin(new Insets(0,0,0,0));
			namePane.setEditable(false);
			namePane.setFont(new Font("Verdana", Font.BOLD, 10));
			namePane.setText(name);
			
		StyledDocument doc = namePane.getStyledDocument();
		SimpleAttributeSet centerText = new SimpleAttributeSet();
		StyleConstants.setAlignment(centerText, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), centerText, false);
		
		
		if (name.contains("GO"))
		{
			bigIcon = new ImageIcon(getClass().getResource("go.png"));
			description = "Collect\n$300 Salary\nas you Pass";
		}
		else if (name.contains("In SAL"))
		{
			bigIcon = new ImageIcon(getClass().getResource("inJail.png"));
		}
		else if (name.contains("Go To SAL"))
		{	
			bigIcon = new ImageIcon(getClass().getResource("goJail.png"));
		}
		else if (name.contains("Credit"))
		{
			bigIcon = new ImageIcon(getClass().getResource("parking.png"));
		}
		
		
		JPanel bigPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				((FlowLayout)bigPanel.getLayout()).setVgap(0);
				((FlowLayout)bigPanel.getLayout()).setHgap(0);
				
		JLabel bigLabel = new JLabel(bigIcon);
			bigPanel.add(bigLabel);

		add(namePane);
		add(bigPanel);
		
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
