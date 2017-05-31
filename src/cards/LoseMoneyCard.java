package cards;

import java.io.Serializable;

import main.Board;
import main.Player;

public class LoseMoneyCard extends Card implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int amount;

	public LoseMoneyCard(String description, int amount) {
		super(description);
		this.amount = amount;
	}

	@Override
	public void act(Player player, Board board) {
		player.money-=amount;
		board.boardPanel.updateScores();
	}

}