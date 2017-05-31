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
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class UtilityTilePanel extends TilePanel {
	ImageIcon utilityIcon;
	private JTextPane ownerPane;
	private PlayerBar playerBar;
	private Tile tileInstance;
	Board gameBoard;

	UtilityTilePanel(int price, String name, Color tileColor, Tile tileInstance, Board gameBoard) {
		this.tileInstance = tileInstance;
		this.gameBoard = gameBoard;

		setLayout(new BoxLayout(UtilityTilePanel.this, BoxLayout.Y_AXIS));
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

		if (name.contains("Railroad") || name.contains("Short"))
			utilityIcon = new ImageIcon(getClass().getResource("railroad.png"));
		else if (name.contains("Water"))
			utilityIcon = new ImageIcon(getClass().getResource("water.png"));
		else if (name.contains("Electric"))
			utilityIcon = new ImageIcon(getClass().getResource("electric.png"));

		JPanel utilityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		((FlowLayout) utilityPanel.getLayout()).setVgap(0);
		((FlowLayout) utilityPanel.getLayout()).setHgap(0);
		JLabel utilityLabel = new JLabel(utilityIcon);
		utilityPanel.add(utilityLabel);

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
		String ownerName = (tileInstance.tileOwner == null ? "None" : tileInstance.tileOwner.playerName);
		String ownerText = "Owner: " + ownerName;

		ownerPane.setText(ownerText);
	}

}
