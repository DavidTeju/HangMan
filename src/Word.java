import org.apache.commons.lang3.mutable.MutableBoolean;

import javax.swing.*;
import java.util.*;

public class Word {
	private final String wordValue;
	private final static int MAXIMUM_ATTEMPTS = 8;
	private int attemptsLeft = MAXIMUM_ATTEMPTS;
	private final HashSet<Integer> ndxFound;
	private final ArrayList<Guess> attempts = new ArrayList<>();
	private String displayWord;
	private final int points;
	
	public int getAttemptsLeft() {
		return attemptsLeft;
	}
	
	@Override
	public String toString() {
		return wordValue;
	}
	
	public Word(String wordValue) {
		this.wordValue = wordValue.toUpperCase();
		ndxFound = new HashSet<>();
		displayWord = "_" + " _".repeat(wordValue.length() - 1);
		points = Scorer.getWordScore(wordValue);
	}
	
	public static Word getPlayerWord(Player player) {
		String wordToPass = JOptionPane.showInputDialog(player.getNAME() + ", please input your secret word");
		if (wordToPass == null) System.exit(1);
		while (wordToPass.length() > 8 || wordToPass.length() < 4) {
			JOptionPane.showMessageDialog(null, "Please input a word between 4 and 8", "Error", JOptionPane.WARNING_MESSAGE);
			wordToPass = JOptionPane.showInputDialog("Please input your secret word");
		}
		return new Word(wordToPass);
	}
	
	public static Word generateWord() {
		try (var streamOfWords = new Scanner(Objects.requireNonNull(Word.class.getResourceAsStream("hangmanWords.txt"))).tokens()) {
			List<String> words = streamOfWords.toList();
			String secretWord = words.get((new Random()).nextInt(words.size()));
			Story.priest.speak("I have thought of a word. Better get guessing!");
			return new Word(secretWord);
		}
	}
	
	private boolean confirmGuess(Guess guess) {
		int previousSize = ndxFound.size();
		
		for (int i = 0; i < wordValue.length(); i++)
			if (wordValue.charAt(i) == guess.charValue) {
				ndxFound.add(i);
				int index = 2 * i;
				displayWord = displayWord.substring(0, index) + guess.charValue + displayWord.substring(index + 1);
			}
		
		guess.isCorrect = previousSize != ndxFound.size();
		attempts.add(guess);
		if (!guess.isCorrect) attemptsLeft--;
		
		return guess.isCorrect;
	}
	
	public boolean guess() {
		Guess guess = getPlayerGuess();
		
		return confirmGuess(guess);
	}
	
	public boolean guess(Player guesser){
		Guess guess = getPlayerGuess();
		
		if (confirmGuess(guess)) {
			guesser.awardPoint(Scorer.getLetterScore(guess.charValue));
			return true;
		} else return false;
	}
	
	private Guess getPlayerGuess() {
		Guess guess = null;
		MutableBoolean repeated = new MutableBoolean(false);
		boolean guessIsInvalid = true;
		do {
			try {
				guess = new Guess(JOptionPane.showInputDialog(String.format("Word: %s │ Guesses left: %d%nYour guesses: %s%n%s", displayWord, attemptsLeft, attempts, "What's your next guess?!")));
				guessIsInvalid = !guess.isValid(repeated);
			} catch (StringIndexOutOfBoundsException e) {
				if (Main.getMode().equals("single"))
					Story.priest.speak("Speak up!");
				else //if multiplayer mode then bland prompt
					JOptionPane.showMessageDialog(null, "Please input a guess");
			} catch (NullPointerException e) {
				System.exit(1);
			}
		} while (repeated.booleanValue() || guessIsInvalid);
		return guess;
	}
	
	private boolean punish() {
		Story.priest.speak("Are you mocking me?");
		JOptionPane.showMessageDialog(null, "You have lost a guess attempt for angering the priest");
		attemptsLeft--;
		return false;
	}
	
	public boolean isGuessed() {
		return ndxFound.size() == wordValue.length();
	}
	
	public int getPoints() {
		return points;
	}
	
	class Guess {
		private final char charValue;
		private boolean isCorrect;
		
		Guess(String guess) {
			charValue = Character.toUpperCase(guess.charAt(0));
		}
		
		private boolean isValid(MutableBoolean repeated) {
			boolean hasBeenGuessed = attempts.contains(this);
			if (Main.getMode().equals("single")) {
				if (!Character.isLetter(this.charValue) && this.charValue != '\u0000') {//If the guess is not a letter
					if (repeated.booleanValue()) return punish();
					Story.priest.speak("I don't spell words with numbers!\nMake that mistake again and you'll lose a guess attempt");
					repeated.setTrue();
					return false;
				}
				if (hasBeenGuessed) {
					if (repeated.booleanValue()) return punish();
					Story.priest.speak("You've already guessed that letter!\nMake that mistake again and you'll lose a guess attempt");
					repeated.setTrue();
					return false;
				}
			} else {
				if (!Character.isLetter(this.charValue)) {//If the guess is not a letter [ && guess != '\u0000']
					JOptionPane.showMessageDialog(null, "Please guess a letter");
					repeated.setTrue();
					return false;
				}
				if (hasBeenGuessed) {
					JOptionPane.showMessageDialog(null, "You've guessed that letter already");
					repeated.setTrue();
					return false;
				}
			}
			
			repeated.setFalse();
			return true;
		}
		
		@Override
		public boolean equals(Object toEqual) {
			if (this == toEqual) return true;
			if (toEqual == null || getClass() != toEqual.getClass()) return false;
			Guess guessToCheck = (Guess) toEqual;
			return charValue == guessToCheck.charValue && isCorrect == guessToCheck.isCorrect;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(charValue, isCorrect);
		}
		
		@Override
		public String toString() {
			return charValue + (isCorrect ? " ✓" : " X");
		}
	}
}