package cards;

import java.io.Serializable;

import main.Board;
import main.Player;

public class CollectFromAllCard extends Card implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int amount;

	public CollectFromAllCard(String description, int amount) {
		super(description);
		this.amount = amount;
	}

	@Override
	public void act(Player player, Board board) {
		for (Player currPlayer : board.gamePlay.infoPacket.playerList) {
			currPlayer.money-=amount;
			player.money+=amount;
			board.boardPanel.updateScores();
		}
	}
}