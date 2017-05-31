package cards;

import java.io.Serializable;

import main.Board;
import main.Player;

public class AdvanceToLocationCard extends Card implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int location;

	public AdvanceToLocationCard(String description, int location) 
	{
		super(description);
		this.location = location;
	}

	@Override
	public void act(Player player, Board board) 
	{
		int distance = location - player.currLocation;
		if (distance < 0) distance += 40;
		board.boardPanel.moveDistance(player, distance);		
	}
}
