package main;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;

import java.awt.*;
import java.util.ArrayList;

public class TilePanel extends JPanel {
	private PlayerBar playerBar;
	private JTextPane ownerPane;
	private Tile tileInstance;
	private Board gameBoard;
	public Bar bar;
	
	TilePanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		setSize(new Dimension(100, 90));
		setBackground(Color.WHITE);
	}

	TilePanel(int price, String name, Color tileColor, Tile tileInstance, Board gameBoard) {
		this.tileInstance = tileInstance;
		this.gameBoard = gameBoard;

		setLayout(new BoxLayout(TilePanel.this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		setSize(new Dimension(100, 90));
		setBackground(Color.WHITE);

		JTextPane namePane = new JTextPane();
		namePane.setPreferredSize(new Dimension(100, 30));
		namePane.setMargin(new Insets(0, 0, 0, 0));
		namePane.setEditable(false);
		namePane.setFont(new Font("Verdana", Font.BOLD, 10));
		namePane.setText(name);

		StyledDocument doc = namePane.getStyledDocument();
		SimpleAttributeSet centerText = new SimpleAttributeSet();
		StyleConstants.setAlignment(centerText, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), centerText, false);

		String priceText = "PRICE: $" + String.valueOf(price);
		JTextPane pricePane = new JTextPane();
		pricePane.setPreferredSize(new Dimension(100, 20));
		pricePane.setMargin(new Insets(0, 0, 0, 0));
		pricePane.setText(priceText);
		pricePane.setFont(new Font("Verdana", Font.BOLD, 10));
		pricePane.setEditable(false);

		doc = pricePane.getStyledDocument();
		centerText = new SimpleAttributeSet();
		StyleConstants.setAlignment(centerText, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), centerText, false);

		String ownerName = (tileInstance.tileOwner == null ? "None" : tileInstance.tileOwner.playerName);
		String ownerText = "Owner: " + ownerName;
		ownerPane = new JTextPane();
		ownerPane.setPreferredSize(new Dimension(100, 20));
		ownerPane.setMargin(new Insets(0, 0, 0, 0));
		ownerPane.setText(ownerText);
		ownerPane.setFont(new Font("Verdana", Font.BOLD, 10));
		ownerPane.setEditable(false);

		doc = ownerPane.getStyledDocument();
		centerText = new SimpleAttributeSet();
		StyleConstants.setAlignment(centerText, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), centerText, false);
		bar = new Bar(tileColor);
		add(bar);
		add(namePane);
		add(pricePane);
		add(ownerPane);

		playerBar = new PlayerBar(gameBoard);
		add(playerBar);

	}

	public void clearPlayerBar() {
		for (JPanel playerPanel : playerBar.playerPanels) {

			playerPanel.setVisible(false);
		}

	}

	public void updatePlayerBar(Player tempPlayer) {

		int i = 0;

		for (Player tempP : gameBoard.playerList) {
			if (tempP.playerColor == tempPlayer.playerColor) {
				playerBar.playerPanels.get(i).setVisible(true);
				playerBar.paintPanel();
			}

			i++;
		}

	}

	public void updateOwnerPane() {
		System.out.println(tileInstance);
		String ownerName = (tileInstance.tileOwner == null ? "None" : tileInstance.tileOwner.playerName);
		String ownerText = "Owner: " + ownerName;

		ownerPane.setText(ownerText);

	}
	public void updateHouses(int numHouses){
		bar.updateHouses(numHouses);
	}
}
