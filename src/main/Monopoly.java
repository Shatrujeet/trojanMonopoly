package main;

import gui.JoinHostGUI;

import java.io.IOException;

import javax.swing.SwingUtilities;

import networking.Client;
import networking.InformationPacket;

public class Monopoly 
{
	public Board gameBoard;
	public InformationPacket infoPacket;
	
	public Monopoly(JoinHostGUI loadFrame, String userName, Client client, InformationPacket infoPacket) 
	{
		this.infoPacket = infoPacket;
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				gameBoard = new Board(userName, Monopoly.this, loadFrame, client);				
			}
		});
	}
}
