import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Main {
	/*
		TODO Add Timed mode with Time limitations
		TODO Add Settings and difficulty modes
		TODO Add Home-screen using JFrame
		 */
	private static String mode;
	
	public static void main(String[] args) {
		boolean modeNotPicked = true;
		while (modeNotPicked) {
			int modeCode = JOptionPane.showOptionDialog(null, "Single Player or Multiplayer?", "Welcome!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Multiplayer", "Single-player"}, 0);
			switch (modeCode) {
				case 0 -> {
					mode = "multi";
					runMultiplayer();
					modeNotPicked = false;
				}
				case 1 -> {
					mode = "single";
					runSinglePlayer();
					modeNotPicked = false;
				}
				default -> JOptionPane.showMessageDialog(null, "Please choose a mode", "error", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public static String getMode() {
		return mode;
	}
	
	private static void runMultiplayer() {
		int numOfRounds = 0;
		int numOfPlayers = 0;
		Player wordMaker, guesser;
		Player[] players;
		Word secretWord;
		
		while (numOfRounds < 1 || numOfPlayers < 2)
			try {
				numOfPlayers = Integer.parseInt(JOptionPane.showInputDialog("How many players"));
				numOfRounds = Integer.parseInt(JOptionPane.showInputDialog("How many rounds do you wish to play?"));
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Please input a valid whole number");
			} catch (NullPointerException e) {
				System.exit(1);
			}
		
		players = new Player[numOfPlayers];
		Arrays.setAll(players, Player::new);
		
		for (int i = 1; i <= numOfRounds; i++) {
			JOptionPane.showMessageDialog(null, String.format("Round %d!", i));
			
			for (int j = 0; j < numOfPlayers; j++) {
				wordMaker = players[j];//Switch places
				secretWord = Word.getPlayerWord(wordMaker);
				
				if (numOfPlayers > 2)
					runForMoreThanTwoPlayers(numOfPlayers, players, secretWord, i, j);
				else {
					guesser = players[(j + 1) % 2];
					runForTwoPlayers(secretWord, guesser);
				}
			}
		}
		
		boolean isDraw = Arrays.stream(players).map(Player::getScore).collect(Collectors.toSet()).size() <= 1;
		
		if (isDraw)
			JOptionPane.showMessageDialog(null, "Draw!!!");
		else {
			Player winner = Arrays.stream(players).max(Comparator.comparing(Player::getScore)).orElse(null);
			JOptionPane.showMessageDialog(null, winner.getNAME() + " wins!!!");
		}
		playAgainPrompt();
	}
	
	private static void runForTwoPlayers(Word secretWord, Player guesser) {
		JOptionPane.showMessageDialog(null, String.format("Alright %s. Time to guess!", guesser.getNAME()), "Time to guess!", JOptionPane.PLAIN_MESSAGE);
		while (secretWord.getAttemptsLeft() > 0 && !secretWord.isGuessed())
			if (secretWord.guess()) JOptionPane.showMessageDialog(null, "Good guess!!");
			else JOptionPane.showMessageDialog(null, "Wrong guess!!");
			
		if (secretWord.isGuessed()) {
			JOptionPane.showMessageDialog(null, "Great job guessing the word!\n\t\"" + secretWord + "\"");
			guesser.awardPoint();
		} else
			JOptionPane.showMessageDialog(null, "Oh No!\nYou didn't guess the word. It was " + secretWord);
		
	}
	
	private static void runForMoreThanTwoPlayers(int numOfPlayers, Player[] players, Word secretWord, int i, int j) {
		Player guesser;
		forEachGuesser:
		for (int k = 0; true; k = (++k % numOfPlayers)/*increase the k but keep it within the bounds of the array*/) {
			guesser = players[k];
			if (k == j) guesser = players[++k]; //to ensure we don't have the wordMaker as a guesser
			JOptionPane.showMessageDialog(null, String.format("Round %d%nGuesser: %s", i, guesser.getNAME()));
//		} else JOptionPane.showMessageDialog(null, String.format("Round %d", i));
//				while ((secretWord.getAttemptsLeft() > 0 || players.length > 2) && !secretWord.isGuessed())
			//No attempts limit for more than 2 players
			while (true)
				if (secretWord.guess(guesser)) {
					if (secretWord.isGuessed())
						break forEachGuesser;//Breaks if that guess was the last guess needed to guess word
					else
						JOptionPane.showMessageDialog(null, "Great guess! Keep going");
				} else {
					JOptionPane.showMessageDialog(null, "Wrong! Next guesser!");
					continue forEachGuesser;
				}
		}
		//only reaches here when the word is guessed
		JOptionPane.showMessageDialog(null, "Great job guessing the word!\nSecret word: " + secretWord);
		var stringOfScores = new StringBuilder(String.format("%-20s  Score", "Player"));
		Arrays.stream(players)
				.sorted(Comparator.comparing(Player::getScore))
				.map(obj -> String.format("%-20s %d%n", obj.getNAME() + ":", obj.getScore()))
				.forEach(stringOfScores::append);
		JOptionPane.showMessageDialog(null, stringOfScores);
	}
	
	private static void runSinglePlayer() {
		final int NUM_OF_ROUNDS = 3;
		boolean hasLost = false;
		int score = 0;
		Story.start();
		Story.tutorial();
		
		//This loop runs for separate words
		for (int j = 0; j < NUM_OF_ROUNDS && !hasLost; j++) {
			Word secretWord = Word.generateWord();
			
			//This loops runs for each guess until the word has been successfully guessed or the player runs out
			while (secretWord.getAttemptsLeft() > 0 && !secretWord.isGuessed())
				if (secretWord.guess())
					Story.correct();//Runs when you guess correctly
				else
					Story.wrong();//Runs when you guess wrong
			
			if (secretWord.isGuessed()) { //Runs when you guess the word correctly
				score += (secretWord.getAttemptsLeft() + 16) * secretWord.getPoints(); //formula for score
				try {
					Story.progress(j);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else { //Runs when you run out of lives
				hasLost = true;
				Story.failed(secretWord.toString());
			}
		}
		if (!hasLost)
			try {
				Scorer.checkHighScore(score, Story.player);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		playAgainPrompt();
	}
	
	private static void playAgainPrompt() {
		int playAgain = JOptionPane.showOptionDialog(null, "Play Again?", "Dead End", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Yes", "No"}, -1);
		if (playAgain == 0)
			main(new String[0]);
		else
			JOptionPane.showMessageDialog(null, "Thanks for playing!");
	}
}