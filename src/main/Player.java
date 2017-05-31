package main;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

public class Player extends Rectangle implements Serializable 
{
	private static final long serialVersionUID = 1L;
		
	public String playerName;
	public int money;
	public boolean inJail;
	public int doubles;
	public boolean lastRollWasDoubles;
	public boolean getOutOfJail;
	public int numRailroad;
	public int jailTime;
	public int currLocation;
	public Color playerColor;
	public int railroads;
	public boolean electrics;
	public boolean waters;
	public int objectCode;
	public ArrayList<Player> otherPlayers;
	public ArrayList<Integer> propertyList;
	public Dice playerDice;
	
	public Player(int startIndex, Color playerColor, String playerName) 
	{
		this.currLocation = startIndex;
		this.playerColor = playerColor;
		this.playerName = playerName;
		this.money = 1500;
		this.inJail = false;
		this.getOutOfJail = false;
		this.doubles = 0;
		this.numRailroad = 0;
		this.playerDice = new Dice();
		this.lastRollWasDoubles = false;
		this.jailTime = 0;
		this.otherPlayers = null;
		this.railroads = 0;
		this.electrics = false;
		this.waters = false;
		this.propertyList = new ArrayList<Integer>();
	}

	public void move() 
	{
		if (this.currLocation < 39)
			this.currLocation++;
		else
			this.currLocation = 0;
	}

	public int roll() 
	{
		int r1 = playerDice.roll();
		int r2 = playerDice.roll();

		if (r1 == r2) {
			this.doubles++;
			lastRollWasDoubles = true;
		}
		else{
			lastRollWasDoubles = false;
		}

		return r1 + r2;
	}
	public String getName(){
		return playerName;
	}
	
	public int[] rollJail() 
	{
		int r1 = playerDice.roll();
		int r2 = playerDice.roll();

		if (r1 == r2) 
		{
			this.doubles++;
			lastRollWasDoubles = true;
		}
		else
		{
			lastRollWasDoubles = false;
		}

		int[] temp = {r1, r2};
		return temp;
	}
}