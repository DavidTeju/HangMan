import javax.swing.*;
import java.net.URL;

public class Player extends Story.GameCharacter {
	private int score = 0;
	private final String NAME;
	private String PLAYER_ID;
	
	public Player(URL newIcon, String newTitle) {//Single-player constructor
		super(newIcon, newTitle);
		NAME = askName();
	}
	
	public Player(int i) {//Multiplayer constructor
		PLAYER_ID = "Player " + (i + 1);
		NAME = askName();
		setTitle(NAME);
	}
	
	private String askName() {
		String nameHolder = null;
		while (nameHolder == null) {
			nameHolder = JOptionPane.showInputDialog(null, "Please enter your name", PLAYER_ID, JOptionPane.QUESTION_MESSAGE);
			if (nameHolder.length() > 10) {
				JOptionPane.showMessageDialog(null
						, "Your name must be 10 letters or less\nPlease shorten your name or use a nickname"
						, PLAYER_ID
						, JOptionPane.QUESTION_MESSAGE);
				nameHolder = null;
			}
		}
		return nameHolder;
	}
	
	public String getNAME() {
		return NAME;
	}
	
	public int getScore() {
		return score;
	}
	
	public void awardPoint() {
		score++;
	}
	
	public void awardPoint(int pointsToADD) {
		score += pointsToADD;
	}
}