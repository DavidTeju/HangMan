package src;

import javax.swing.*;

public class Main {
    /*TODO Implement score system dependent on percentage correct guesses and difficulty of word (using scrabble points)
        TODO Add Timed mode with Time limitations
        TODO Add Leaderboard
        TODO Add Settings and difficulty modes
        TODO Add Home-screen using JFrame
        TODO Add three player mode with two people guessing one player's word and the player with the highest score winning
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
                }case 1 -> {
                    mode = "single";
                    runSinglePlayer();
                    modeNotPicked = false;
                }default -> JOptionPane.showMessageDialog(null, "Please choose a mode", "error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static String getMode(){
        return mode;
    }

   private static void runMultiplayer(){
        Player[] players = {new Player(), new Player()};
        int numOfRounds = 0;
        Player wordMaker;
        Player guesser;
        Word secretWord;

        while (numOfRounds == 0)
            try{
                numOfRounds = Integer.parseInt(JOptionPane.showInputDialog("How many rounds do you wish to play?"));
            }catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Please input a valid whole number");
            }

        for (int i = 0; i<numOfRounds*2; i++){
            wordMaker = players[i%2];//Switch places
            guesser = players[(i+1)%2];
            JOptionPane.showMessageDialog(null, String.format("Round %d%nGuesser: %s", (i+2)/2, guesser.getNAME()));
            secretWord = Word.getPlayerWord(wordMaker);
            while(secretWord.getAttemptsLeft() > 0 && !secretWord.isGuessed())
                if (secretWord.guess())
                    JOptionPane.showMessageDialog(null, "Correct!");
                else
                    JOptionPane.showMessageDialog(null, "Wrong!");

            if (secretWord.isGuessed()) {
                JOptionPane.showMessageDialog(null, "Great job guessing the word!\n\t\"" + secretWord.getSecretWord() + "\"");
                guesser.awardPoint();
            }
            else
                JOptionPane.showMessageDialog(null, "Oh No!\nYou didn't guess the word. It was " + secretWord.getSecretWord());
        }

        Player winner;
        if (players[0].getScore()==players[1].getScore()) {
            JOptionPane.showMessageDialog(null, "Draw!!!");
        }
        else {
            winner = players[0].getScore()>players[1].getScore()
                    ? players[0]
                    : players[1];
            JOptionPane.showMessageDialog(null, winner.getNAME()+ " wins!!!");
        }

        playAgainPrompt();
    }

    private static void runSinglePlayer(){
        final int NUM_OF_ROUNDS = 3;
        boolean hasLost = false;
        Story.start();
        Story.tutorial();

        //This loop runs for separate words
        for (int j = 0; j<NUM_OF_ROUNDS && !hasLost; j++) {
            Word secretWord = Word.generateWord();

            //This loops runs for each guess until the word has been successfully guessed or the player runs out
            while(secretWord.getAttemptsLeft() > 0 && !secretWord.isGuessed())
                if (secretWord.guess())
                    Story.correct();//Runs when you guess correctly
                else
                    Story.wrong();//Runs when you guess wrong

            if (secretWord.isGuessed()) { //Runs when you guess the word correctly
                try {
                    Story.progress(j);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else { //Runs when you run out of lives
                hasLost = true;
                Story.failed(secretWord.getSecretWord());
            }
        }

        playAgainPrompt();
    }

    private static void playAgainPrompt(){
        int playAgain = JOptionPane.showOptionDialog(null, "Play Again?", "Dead End", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Yes", "No"}, -1);
        if (playAgain == 0)
            main(new String[1]);
        else
            JOptionPane.showMessageDialog(null, "Thanks for playing!");
    }
}