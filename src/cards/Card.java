package cards;

import java.io.Serializable;

import main.*;

public abstract class Card implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String description;
	
	public Card(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public abstract void act(Player player, Board board);
	
}
