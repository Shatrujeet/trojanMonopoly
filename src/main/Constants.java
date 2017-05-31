package main;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;

import main.Sound;


public class Constants {
	
	public static final ImageIcon[] game_pieces = {
		new ImageIcon("src/images/cote.jpeg"),
		new ImageIcon("src/images/miller.jpeg"),
		new ImageIcon("src/images/shindler.jpeg"),
		new ImageIcon("src/images/redekopp.jpeg"),
		new ImageIcon("src/images/rosenbloom.jpeg"),
		new ImageIcon("src/images/saty.jpeg"), 
		new ImageIcon("src/images/guest_user.png")
	};
	
	public static final HashMap<ImageIcon, String> captcha_images = new HashMap<ImageIcon, String>();
	public static final String one = captcha_images.put(new ImageIcon("src/images/captcha.jpeg"), "morning overlooks");
	public static final String two = captcha_images.put(new ImageIcon("src/images/captcha2.png"), "prens entsTo");
	public static final String three = captcha_images.put(new ImageIcon("src/images/captcha3.png"), "contend because");
	
	public static final ImageIcon[] captchas = {
		new ImageIcon("src/images/captcha.jpeg"),
		new ImageIcon("src/images/captcha2.png"),
		new ImageIcon("src/images/captcha3.png")
	};
	
	public static final Sound sound = new Sound();
	
	public static final String sound_filename = "src/images/music.wav";
	
	
	
	
	
	//public static final ImageIcon captcha = new ImageIcon("src/images/captcha.jpeg");
	
	public static final String[] game_pieces_names = { 
		"Cote",
		"Miller",
		"Shindler", 
		"Redekopp", 
		"Rosenbloom",
		"Saty"
	};
	
	public static final Font XSmallFont = new Font("Verdana", Font.BOLD, 5);
	public static final Font smallFont = new Font("Verdana", Font.BOLD, 10);
	public static final Font mediumFont = new Font("Verdana", Font.BOLD, 20);
	public static final Font largeFont = new Font("Verdana", Font.BOLD, 30);
	public static final Font XLFont = new Font("Verdana", Font.BOLD, 40);
	

}
