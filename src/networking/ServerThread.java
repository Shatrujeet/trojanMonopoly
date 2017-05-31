package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import gui.JoinHostGUI;
import main.Player;

public class ServerThread extends Thread {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Server cs;
	public Socket s;
	private Player teamData;
	JoinHostGUI loadFrame;
	
	public ServerThread(Socket s, Server cs, JoinHostGUI loadFrame) 
	{
		this.loadFrame = loadFrame;
		
		try 
		{
			this.s = s;
			this.cs = cs;
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			this.start();
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
	
	public void sendMessage(InformationPacket infoPacket)
	{
		try
		{
			oos.writeObject(infoPacket);
			oos.flush();
		} 
		catch (IOException ioe) 
		{
			ioe.printStackTrace();
			System.out.println("ioe: " + ioe.getMessage());
		}
	}

	public void run() 
	{
		try 
		{
			teamData = (Player)ois.readObject();
		} 
		catch (ClassNotFoundException e1) 
		{
			e1.printStackTrace();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		try 
		{
			while(true) 
			{
				InformationPacket infoPacket = (InformationPacket)ois.readObject();
				
				if(infoPacket != null)
				{
					cs.sendMessageToAllClients(infoPacket);
				}
			}
		} 
		catch (ClassNotFoundException cnfe) 
		{
			System.out.println("cnfe in run: " + cnfe.getMessage());
		} 
		catch (IOException ioe) 
		{
			if(loadFrame.gamePlay == null)
			{
				cs.serverThreads.remove(this);
							
				InformationPacket newPacket = new InformationPacket(cs.infoPacket);
				
				for(int i = 0; i < newPacket.playerList.size(); i++)
				{
					if(newPacket.playerList.get(i).objectCode == teamData.objectCode)
					{
						newPacket.playerList.remove(i);
					}
				}
				
				newPacket.playerCount-=1;
				
				cs.sendMessageToAllClients(newPacket);
				
				try 
				{
					s.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{				
				for(ServerThread st : cs.serverThreads)
				{
					try 
					{
						st.s.close();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
	}

