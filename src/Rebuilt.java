import javax.swing.*;

public class Rebuilt {
    /*TODO Implement score system dependent on percentage correct guesses and difficulty of word (using scrabble points)
        TODO Add Timed mode with Time limitations
        TODO Add Leaderboard
        TODO Add Settings and difficulty modes
        TODO Add Homescreen using JFrame
        TODO Add three player mode with two people guessing one player's word and the player with the highest score winning
         */
    public static void main(String[] args) {
        boolean modeNotPicked = true;
        while (modeNotPicked) {
            int mode = JOptionPane.showOptionDialog(null, "Single Player or Multiplayer?", "Welcome!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Multiplayer", "Singleplayer"}, 0);
            switch (mode) {
                case 0 -> {
                    runMultiplayer();
                    modeNotPicked = false;
                }case 1 -> {
                    runSinglePlayer();
                    modeNotPicked = false;
                }default -> JOptionPane.showMessageDialog(null, "Please choose a mode", "error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static void runMultiplayer(){
        Player[] players = {new Player(), new Player()};
        int numOfRounds = 0;
        Player wordMaker;
        Player guesser;
        Word secretWord;

        while (numOfRounds == 0)
            numOfRounds = Integer.parseInt(JOptionPane.showInputDialog("How many rounds do you wish to play?"));

        for (int i = 0; i<numOfRounds*2; i++){
            JOptionPane.showMessageDialog(null, "Round " + 1);
            wordMaker = players[i%2];
            guesser = players[(i+1)%2];
            secretWord = Word.getPlayerWord(wordMaker);
            secretWord.guess();
        }

    }

    public static void runSinglePlayer(){
        final int NUM_OF_ROUNDS = 3;
        boolean hasLost = false;
        Story.start();
        Story.tutorial();

        //This loop runs for seperate words
        for (int j = 0; j<NUM_OF_ROUNDS && !hasLost; j++) {
            Word secretWord = Word.generateWord();

            //This loops runs for each guess until the word has been successfully guessed or the player runs out
            while(secretWord.getAttemptsLeft() > 0 && !secretWord.isGuessed())
                if (secretWord.guess())
                    Story.correct();
                else
                    Story.wrong();

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