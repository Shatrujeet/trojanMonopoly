package networking;

import gui.Constants;
import gui.JoinHostGUI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;


public class Server implements Runnable
{
	public Vector<ServerThread> serverThreads;
	public ServerSocket ss;
	private int port;
	public InformationPacket infoPacket;
	public int maxPlayers;
	public JoinHostGUI loadFrame;
	public boolean updated;
	
	public Server(int port, int maxPlayers, JoinHostGUI loadFrame) 
	{
		this.loadFrame = loadFrame;
		this.maxPlayers = maxPlayers;
		this.port = port;
		updated = false;
		serverThreads = new Vector<ServerThread>();
		infoPacket = new InformationPacket();
		infoPacket.totalPlayers = maxPlayers;
		
		new Thread(this).start();
	}
	
	public void sendMessageToAllClients(InformationPacket infoPacket) 
	{
		this.infoPacket = infoPacket;
		
		for (ServerThread st : serverThreads) 
		{
			st.sendMessage(infoPacket);
		}
	}
	
	@Override
	public void run() 
	{
		try 
		{
			ss = new ServerSocket(port);

			while (serverThreads.size() != maxPlayers) 
			{
				Socket s = ss.accept();
				
				ServerThread st = new ServerThread(s, this, loadFrame);
				st.sendMessage(infoPacket);
				
				serverThreads.add(st);					
			}
			Constants.sound.endSound();
		} 
		catch (IOException ioe) 
		{
			loadFrame.serverFailed = true;
		} 
		finally 
		{
			if (ss != null) 
			{
				try 
				{
					ss.close();
				} 
				catch (IOException ioe)
				{
					System.out.println("ioe closing ss: " + ioe.getMessage());
				}
			}
		}
	}
}









