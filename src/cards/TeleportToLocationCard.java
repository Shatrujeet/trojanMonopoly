package cards;

import java.io.Serializable;

import main.Board;
import main.Player;

public class TeleportToLocationCard extends Card implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int location;

	public TeleportToLocationCard(String description, int location) {
		super(description);
		this.location = location;
	}

	@Override
	public void act(Player player, Board board) {
		player.currLocation = location;
		player.inJail = true;
		player.jailTime = 3;
		
		board.updatePlayerLocations();
	}
}
