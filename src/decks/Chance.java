package decks;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import cards.AdvanceToLocationCard;
import cards.Card;
import cards.CollectFromAllCard;
import cards.GainMoneyCard;
import cards.GetOutOfJail;
import cards.GiveToAllCard;
import cards.LoseMoneyCard;
import cards.TeleportToLocationCard;
import main.Board;
import main.Player;
import queue.MonopolyQueue;

public class Chance implements Serializable
{
	private static final long serialVersionUID = 1L;
	private MonopolyQueue<Card> deck;

	public Chance() 
	{
		deck = new MonopolyQueue<Card>();
		
		deck.enqueue(new TeleportToLocationCard("Go Directly to SAL", 10));
		deck.enqueue(new AdvanceToLocationCard("Advance to GO", 0));
		deck.enqueue(new AdvanceToLocationCard("Advance to Parkside APTS", 11));
		deck.enqueue(new CollectFromAllCard("Transfer student football ticket becasue of CS Assignment. Collect $50 from each player.", 50));
		deck.enqueue(new GiveToAllCard("You have to pay to get into a party. Give $30 to Each Player", 30));
		deck.enqueue(new LoseMoneyCard("Your USC Uber canceled on you. You Lose $100! Oh No!", 100));
		deck.enqueue(new GainMoneyCard("Your friend swiped you in the dinning hall. Save $40 on food.", 40));
		deck.enqueue(new GetOutOfJail("You Now Have a Get Out of SAL Free Card"));

		shuffle();
	}

	public void draw(Player player, Board board) 
	{
		Card c = deck.dequeue();

		if(board.client.playerData.objectCode == board.gamePlay.infoPacket.playerList.get(board.whoseTurn).objectCode)
		{
			JOptionPane.showMessageDialog(null, c.getDescription(), "Monopoly",
					JOptionPane.PLAIN_MESSAGE);
		}
		
		board.boardPanel.logArea.append("\n" + player.getName() + " Drew: " + c.getDescription());

		if(c instanceof AdvanceToLocationCard)
		{
			board.boardPanel.buyButton.setEnabled(false);
			board.boardPanel.endTurnButton.setEnabled(false);
		}
		
		c.act(player, board);

		deck.enqueue(c);
	}

	public void shuffle() 
	{
		ArrayList<Card> shuffler = new ArrayList<Card>();
		while (deck.getSize() != 0) {
			shuffler.add(deck.dequeue());
		}

		while (shuffler.size() != 0) {
			int n = shuffler.size();
			int randIndex = (int) (n * Math.random());
			deck.enqueue(shuffler.remove(randIndex));
		}
	}
}
