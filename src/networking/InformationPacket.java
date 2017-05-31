package networking;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import decks.Chance;
import decks.CommunityChest;
import main.Player;

public class InformationPacket implements Serializable
{
	private static final long serialVersionUID = 1L;
	public ArrayList<Player> playerList;
	public ArrayList<ImageIcon> imageList;
	public int playerCount;
	public int totalPlayers;
	public int whoseTurn;
	public int codeGen;
	public int serverCode;
	public boolean exit;
	public int exitCode;
	public boolean isMessage;
	public String message;
	public boolean isRoll;
	public boolean isEndTurn;
	public int roll;
	public boolean isBuyProperty;
	public boolean isCard;
	public Chance chanceDeck;
	public CommunityChest communityChestDeck;
	public boolean isIncomeTax;
	public boolean incomeChoice;
	public boolean isJailEnd;
	public int jailPlayerEnded;
	public boolean isPay50;
	public boolean isUseCard;
	public boolean isJailRoll;
	public int[] rollJailVal;
	public boolean isMortgage;
	public boolean isUnMortgage;
	public int propertyVal;
	public boolean isHouse;
	public int numHouses;
	public int moneyOwed;
	
	public InformationPacket()
	{
		this.playerList = new ArrayList<Player>();
		this.imageList = new ArrayList<ImageIcon>();
		this.playerCount = 0;
		this.totalPlayers = -1;
		this.whoseTurn = 0;
		this.codeGen = 0;
		this.jailPlayerEnded = -1;
		this.serverCode = -1;
		this.exit = false;
		this.exitCode = -1;
		this.isMessage = false;
		this.message = null;
		this.isRoll = false;
		this.isEndTurn = false;
		this.roll = -1;
		this.isBuyProperty = false;
		this.isCard = false;
		this.chanceDeck = new Chance();
		this.communityChestDeck = new CommunityChest();
		this.isIncomeTax = false;
		this.incomeChoice = false;
		this.isJailEnd = false;
		this.isPay50 = false;
		this.isUseCard = false;
		this.isJailRoll = false;
		this.rollJailVal = null;
		this.isMortgage = false;
		this.isUnMortgage = false;
		this.propertyVal = -1;
		this.isHouse = false;
		this.numHouses = -1;
		this.moneyOwed = -1;
	}
	
	public InformationPacket(InformationPacket infoPacket)
	{
		this.imageList = infoPacket.imageList;
		this.playerList = infoPacket.playerList;
		this.playerCount = infoPacket.playerCount;
		this.totalPlayers = infoPacket.totalPlayers;
		this.whoseTurn = infoPacket.whoseTurn;
		this.codeGen = infoPacket.codeGen;
		this.serverCode = infoPacket.serverCode;
		this.exit = infoPacket.exit;
		this.exitCode = infoPacket.exitCode;
		this.isMessage = infoPacket.isMessage;
		this.message = infoPacket.message;
		this.roll = infoPacket.roll;
		this.isEndTurn = infoPacket.isEndTurn;
		this.isRoll = infoPacket.isRoll;
		this.isBuyProperty = infoPacket.isBuyProperty;
		this.isCard = infoPacket.isCard;
		this.chanceDeck = infoPacket.chanceDeck;
		this.communityChestDeck = infoPacket.communityChestDeck;
		this.incomeChoice = infoPacket.incomeChoice;
		this.isIncomeTax = infoPacket.isIncomeTax;
		this.isJailEnd = infoPacket.isJailEnd;
		this.isPay50 = infoPacket.isPay50;
		this.isUseCard = infoPacket.isUseCard;
		this.isJailRoll = infoPacket.isJailRoll;
		this.rollJailVal = infoPacket.rollJailVal;
		this.jailPlayerEnded = infoPacket.jailPlayerEnded;
		this.isMortgage = infoPacket.isMortgage;
		this.isUnMortgage = infoPacket.isUnMortgage;
		this.propertyVal = infoPacket.propertyVal;
		this.isHouse = infoPacket.isHouse;
		this.numHouses = infoPacket.numHouses;
		this.moneyOwed = infoPacket.moneyOwed;
	}
	
	public void setFalse(){
		exit = false;
		isMessage = false;
		message = null;
		isRoll = false;
		isEndTurn = false;
		roll = -1;
		isBuyProperty = false;
		isCard = false;
		this.incomeChoice = false;
		this.isIncomeTax = false;
		isJailEnd = false;
		isPay50 = false;
		isUseCard = false;
		isJailRoll = false;
		this.rollJailVal = null;
		this.isMortgage = false;
		this.isUnMortgage = false;
		this.propertyVal = -1;
		this.isHouse = false;
		this.numHouses = -1;
		this.moneyOwed = -1;
	}
}
