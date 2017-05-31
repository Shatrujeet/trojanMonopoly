package cards;

import java.io.Serializable;

import main.Board;
import main.Player;

public class GetOutOfJail extends Card implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public GetOutOfJail(String description) 
	{
		super(description);
	}

	@Override
	public void act(Player player, Board board) 
	{
		player.getOutOfJail = true;
	}
}