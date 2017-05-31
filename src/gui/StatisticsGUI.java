package gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class StatisticsGUI extends JFrame {
	
	private Account user;
	
	private JLabel titleLabel;
	private JLabel gamesWonLabel;
	private JLabel totalGamesLabel;
	private JLabel winningPercentageLabel;
	private JLabel profPicLabel;
	
	public StatisticsGUI(Account user, Boolean won) {
		super("Monopoly");
		this.user = user;
		
		user.updateDatabase(won);
		user.close();
		initalizeComponents();
		createGUI();
		addEvents();
	}



	private void initalizeComponents() {
		titleLabel = new JLabel(user.getName() + "'s Stats");
		titleLabel.setFont(Constants.largeFont);
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		profPicLabel = new JLabel();
		profPicLabel.setIcon(user.getImage());
		profPicLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		gamesWonLabel = new JLabel("Games Won: " + user.getGamesWon());
		gamesWonLabel.setFont(Constants.mediumFont);
		gamesWonLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		totalGamesLabel = new JLabel("Total Games Played: " + user.getTotalGames());
		totalGamesLabel.setFont(Constants.mediumFont);
		totalGamesLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		winningPercentageLabel = new JLabel((int)(((double)user.getGamesWon()/(double)user.getTotalGames()) * 100) + "%");
		winningPercentageLabel.setFont(Constants.largeFont);
		winningPercentageLabel.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	private void createGUI() {
		setSize(400, 300);
		this.getContentPane().setBackground(Color.decode("#CEE6D1"));
		
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(titleLabel);
		add(profPicLabel);
		add(gamesWonLabel);
		add(totalGamesLabel);
		add(winningPercentageLabel);
		
		
		
	}
	
	private void addEvents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	

}
