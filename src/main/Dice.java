package main;

import java.io.Serializable;

public class Dice implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int sides;

	public Dice()
	{
		sides = 6;
    }

    public Dice(int numSides)
    {
    	sides = numSides;
    }


    public int roll()
    {
    	return  (int)(Math.random() * sides) + 1;
    }
}