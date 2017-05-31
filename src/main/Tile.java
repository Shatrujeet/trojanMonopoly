package main;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class Tile implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int price;
	public String name;
	public Color tileColor;
	public TilePanel gameTile;
	public Player tileOwner;
	public boolean isProperty;
	public int houses;
	public int[] rentPrices;
	public boolean isMortgaged;
	public boolean isEstate;
	
	Tile(int priceIn, String nameIn, Color colorIn, int type, Board gameBoard, boolean isProperty, int[] rentPrices, boolean isEstate)
	{
		tileOwner = null;
		this.isProperty = isProperty;
		price = priceIn;
		name = nameIn;
		tileColor = colorIn;
		houses = 0;
		this.isEstate = isEstate;
		
		this.rentPrices = rentPrices;
		
		// Add a specific tilePanel based on the Tile's type.
		// 0 = normal
		// 1 = big tile
		// 2 = utility tile
		// 3 = special tile
		if (type == 1)
			gameTile = new BigTilePanel(price, name, tileColor, gameBoard);
		else if (type == 2)
			gameTile = new UtilityTilePanel(price, name, tileColor, this, gameBoard);
		else if(type == 3)
			gameTile = new SpecialTilePanel(price, name, tileColor, gameBoard);
		else
			gameTile = new TilePanel(price, name, tileColor, this, gameBoard); 
	}
	
	public TilePanel getTilePanel()
	{
		return gameTile;
	}
}
