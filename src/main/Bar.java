package main;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;


public class Bar extends JPanel
{
	private Color barColor;
	private JPanel[] houses;
	/*
	public void paint(Graphics g)
	{
		super.paintComponent(g);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		g.drawRect(0, 0, 100, 20);
		g.setColor(barColor);
		g.fillRect(0,0,100,20);
	}*/
	
	Bar(Color color)
	{
		this.setBackground(color);
		this.setPreferredSize(new Dimension(100, 20));
		this.setLayout(new GridLayout(1,5));
		
		
		this.houses = new JPanel[5];
		
		for (int i=0; i<5; i++) {
			JPanel tempPanel = new JPanel();
			tempPanel.setBorder(new LineBorder(Color.BLACK));
			tempPanel.setBackground(Color.WHITE);
			tempPanel.setVisible(false);
			houses[i] = tempPanel;
			add(tempPanel);
		}
		
	}
	
	public void updateHouses(int numHouses) {
		for (int i=0; i<numHouses; i++) {
			houses[i].setVisible(true);
		}
		for (int i=4; i>=numHouses; i--) {
			houses[i].setVisible(false);
		}
		repaint();
		revalidate();
		
	}
	
	
	
	
}
