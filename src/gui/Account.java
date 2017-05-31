package gui;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Account {
	private Connection conn = null;
	private Statement st = null;
	private ResultSet rs = null;
	private String user;
	private String password;
	private boolean guest;
	private ImageIcon gamePiece;
	private int totalGames;
	private int gamesWon;
	private String username;
	
	//used for guest
 	public Account() {
		guest = true;
		this.user = "Guest";
		this.username = "Guest";
		this.password = "g";
		gamePiece = Constants.game_pieces[6];
		try {
		Class.forName("com.mysql.jdbc.Driver"); //allows us to write java code that is database independent
		conn =  DriverManager.getConnection("jdbc:mysql://localhost/Users?user=root&password=root&useSSL=false");
		st = conn.createStatement();
		getGames();
		}  catch (SQLException sqle)
		{
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("cnfe: " + e.getMessage());
		}
	
	}
	
	//used for an actual account
	public Account(String usern, String password) {
		guest = false;
		this.user = usern;
		this.password = password;
		
		String[] parsed = user.split("@");
		username = parsed[0];
		
		
		
		try {
		Class.forName("com.mysql.jdbc.Driver"); //allows us to write java code that is database independent
		conn =  DriverManager.getConnection("jdbc:mysql://localhost/Users?user=root&password=root&useSSL=false");
		st = conn.createStatement();
		}  catch (SQLException sqle)
		{
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("cnfe: " + e.getMessage());
		}
		
		
		
	}
	
	//checks to see if it is in the account
	public boolean inAccount() {
			try {
				rs = st.executeQuery("SELECT username, passw FROM Users WHERE username='" + user + "' AND passw='"+ password + "';");
				//if these is nothing in rs, return false
				if(!rs.next())
				{
					System.out.println("inAccount(): returning false");
					return false;
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return true;
			} 
				
		return true;
		
	}
	
	//creates the account
	public boolean createAccount() {
		if(inAccount())
		{
			return false;
			
		} else {
			try {
				st.executeUpdate("INSERT INTO Users (username, passw, totalGames, gamesWon) VALUES ('" + user + "', '" + password + "', " + 0 + ", " + 0 + ");");
				return true;
			} catch (SQLException sqle)
			{
				System.out.println("sqle: " + sqle.getMessage());
			} 
		}
		return false;
		
	}
	
	//this sets the values of totalGames and gamesWon
	private void getGames() {
		try {
			rs = st.executeQuery("SELECT totalGames FROM Users WHERE username='" + user + "' AND passw='"+ password + "';");
			if(rs.next())
			{
				totalGames = rs.getInt(1);
				System.out.println("totalGames = " + totalGames);
			}
			
			
			rs = st.executeQuery("SELECT gamesWon FROM Users WHERE username='" + user + "' AND passw='"+ password + "';");
			if(rs.next())
			{
				gamesWon = rs.getInt(1);
				System.out.println("gamesWon = " + gamesWon);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	//this closes the SQL
	public void close() {
		try {
			if(rs != null)
				rs.close();
			if(st != null)
				st.close();
			if(conn != null)
				st.close();
		} catch (SQLException s)
		{
			System.out.println(s.getMessage());
		}
		
	}
	
	//this sets the image of the gamePiece
	public void setImage(ImageIcon gamePieceImage) {
		gamePiece = gamePieceImage;
	}
	
	//this gets the name
	public String getName() {
		return username;
	}
		
	//is the boolean on if it is Guest
	public boolean isGuest() {
		return guest;
	}
	
	//this gets the iage icon
	public ImageIcon getImage() {
		return gamePiece;
	}
	
	public int getGamesWon() {
		return gamesWon;
	}
	
	public int getTotalGames() {
		return totalGames;
	}

	public void updateDatabase(Boolean won) {
		totalGames++;
		try {
			st.executeUpdate("UPDATE Users SET totalGames=" + totalGames + " WHERE username='" + user +"'");
			if(won) {
				gamesWon++;
				st.executeUpdate("UPDATE Users SET gamesWon=" + gamesWon + " WHERE username='" + user +"'");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public HashMap<String, String> getDatabaseSet()
	{
		ResultSet rs = null;
		Statement st = null;

		HashMap<String, String> returnMap = new HashMap<String, String>();
		
		try 
		{
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM User");
			
			while(rs.next())
			{
				String username = rs.getString("username");
				String password = rs.getString("password");
				
				returnMap.put(username, password);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(rs != null)
				{
					rs.close();
				}
				
				if(st != null)
				{
					st.close();
				}
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		
		return returnMap;
	}

	

}
