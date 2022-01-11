package src;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Word {
    private final String secretWord;
    private final static int MAXIMUM_ATTEMPTS = 8;
    private int attemptsLeft = MAXIMUM_ATTEMPTS;
    private final HashSet<Integer> ndxFound;
    private final ArrayList<String> attempts = new ArrayList<>();
    private String displayWord = "_ _ _ _ _ _ _ _";

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public String getSecretWord(){
        return secretWord;
    }

    public Word(String secretWord) {
        this.secretWord = secretWord.toUpperCase();
        ndxFound = new HashSet<>();
        displayWord = displayWord.substring(0, secretWord.length()*2-1);
    }

    public static Word getPlayerWord(Player player) {
        String wordToPass = JOptionPane.showInputDialog(player.getNAME() + ", please input your secret word");
        while (wordToPass.length() > 8 || wordToPass.length() < 4) {
            JOptionPane.showMessageDialog(null, "Please input a word between 4 and 8", "Error", JOptionPane.WARNING_MESSAGE);
            wordToPass = JOptionPane.showInputDialog("Please input your secret word");
        }
        return new Word(wordToPass);
    }

    public static Word generateWord(){
        String secretWord;
        Scanner wordBank;
        final String wordBankPath = "hangmanWords.txt";
        ArrayList<String> words = new ArrayList<>();

        try {
            wordBank = new Scanner (new File(wordBankPath));
            while (wordBank.hasNext())
                words.add(wordBank.next());
        }
        catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Game System Error. " + wordBankPath + " not found.");
        }

        secretWord = words.get((new Random()).nextInt(words.size()));
        Story.priest.speak("I have thought of a word. Better get guessing!");
        return new Word(secretWord);
    }

    private boolean confirmGuess(char guess) {
        int previousSize = ndxFound.size();

        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == guess) {
                ndxFound.add(i);
                int index = (i+1)*2-2;
                displayWord = displayWord.substring(0, index) + guess + displayWord.substring(index+1);
            }
        }

        boolean isCorrect = previousSize != ndxFound.size();
        if (isCorrect)
            attempts.add(guess + " ✓");
        else {
            attempts.add(guess + " X");
            attemptsLeft--;
        }

        return isCorrect;
    }

    public boolean guess() {
        boolean repeated = false;
        char guess = '\u0000';
        do {
            try {
                guess = JOptionPane.showInputDialog(String.format("Word: %s │ Guesses left: %d%nYour guesses: %s%n%s", displayWord, attemptsLeft, attempts, "What's your next guess?!")).charAt(0);
                guess = Character.toUpperCase(guess);
            }
            catch (StringIndexOutOfBoundsException e){
                if (Main.getMode().equals("single"))
                    Story.priest.speak("Speak up!");
                else //if multiplayer mode then bland prompt
                    JOptionPane.showMessageDialog(null, "Please input a guess");
            }

            boolean hasBeenGuessed = attempts.toString().contains(guess + "");
            if (Main.getMode().equals("single")) {
                if (!Character.isLetter(guess) && guess != '\u0000') {//If the guess is not a letter
                    if (repeated) return punish();
                    Story.priest.speak("I don't spell words with numbers!\nMake that mistake again and you'll lose a guess attempt");
                    repeated = true;
                } else if (hasBeenGuessed) {
                    if (repeated) return punish();
                    Story.priest.speak("You've already guessed that letter!\nMake that mistake again and you'll lose a guess attempt");
                    repeated = true;
                }
                else repeated = false;
            }
            else {
                if (!Character.isLetter(guess)) {//If the guess is not a letter [ && guess != '\u0000']
                    JOptionPane.showMessageDialog(null, "Please guess a letter");
                    repeated = true;
                } else if (hasBeenGuessed) {
                    JOptionPane.showMessageDialog(null, "You've guessed that letter already");
                    repeated = true;
                } else repeated = false;
            }
        } while (repeated || guess=='\u0000');

        return confirmGuess(guess);
    }

    private boolean punish () {
        Story.priest.speak("Are you mocking me?");
        JOptionPane.showMessageDialog(null, "You have lost a guess attempt for angering the priest");
        attemptsLeft--;
        return false;
    }

    public boolean isGuessed(){
        return ndxFound.size() == secretWord.length();
    }
}
