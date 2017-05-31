package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import main.Board;
import networking.InformationPacket;

public class CloseListener implements WindowListener
{

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		String ObjButtons[] = {"Yes","No"};
		
	    int PromptResult = JOptionPane.showOptionDialog(null, 
	        "Are You Sure You Want to Exit?", "Exit", 
	        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, 
	        ObjButtons,ObjButtons[1]);
	    
	    if(PromptResult == 0)
	    {
	    	if(e.getSource() instanceof Board)
	    	{
		    	Board boardFrame = (Board)e.getSource();     
		    	
		    	boardFrame.gamePlay.infoPacket.exit = true;
				boardFrame.gamePlay.infoPacket.exitCode = boardFrame.client.playerData.objectCode;
				
				InformationPacket iPacket = new InformationPacket(boardFrame.gamePlay.infoPacket);
				
				try 
				{
					boardFrame.client.oos.writeObject(iPacket);
					boardFrame.client.oos.flush();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				
				if(boardFrame.gamePlay.infoPacket.exitCode != boardFrame.gamePlay.infoPacket.serverCode)
				{
					System.exit(0);
				}
	    	}
	    	else
	    	{
	    		System.exit(0);
	    	}
	    }
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
