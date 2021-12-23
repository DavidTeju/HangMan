package src;

import javax.swing.*;
import java.util.Random;

public class Story {
    static Player player = new Player("images\\victim.png", "You");
    static GameCharacter priest = new GameCharacter("images\\puritan.png", "Puritan Priest");
    static GameCharacter accuser = new GameCharacter("images\\accuser.png", "Accuser");

    public static void start(){
        priest.speak("You have been accused of witchcraft and heresy!");
        player.speak("I am innocent!");
        priest.speak("Stop lying, witch!\nTo prove your innocence, you have to guess what word I'm thinking of through God's help\n(If you think this is crazy, google \"Salem Swimming test\")");
    }

    public static void tutorial(){
        boolean questionNotAnswered = true;
        while (questionNotAnswered)
            switch (JOptionPane.showOptionDialog(null, "Hey, " + player.getName() + ". Do you want to learn how to play Hang Man?", "Tutorial", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"YES", "NO"}, -1)){
                case -1:
                    JOptionPane.showMessageDialog(null, "Please select an option", "Error!", JOptionPane.WARNING_MESSAGE);
                    break;
                case 1:
                    return;
                case 0:
                    questionNotAnswered = false;
            }
        JOptionPane.showMessageDialog(null, "You are given 8 tries and a secret word between 4 and 8 letters long.\nThe objective of the game is to guess the word, one character at a time, before you run out of tries.", "Tutorial", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, "Goodluck!!! You'll need it", "Tutorial", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void correct(){
        String congratWord;
        switch (new Random().nextInt(5)) {
            case 1 -> congratWord = "Lucky You!";
            case 2 -> congratWord = "You got lucky this time!";
            case 3 -> congratWord = "Maybe God is on your side";
            case 4 -> congratWord = "Not bad";
            case 0 -> congratWord = "Pray you get the next one too";
            default -> throw new IllegalStateException("Unexpected value: " + new Random().nextInt(6));
        }
        priest.speak(congratWord, "Good guess");
    }

    public static void wrong(){
        String insult;
        switch (new Random().nextInt(6)) {
            case 1 -> insult = "You witch!";
            case 2 -> insult = "You will burn";
            case 3 -> insult = "One guess closer to the stake";
            case 4 -> insult = "Wrong guess!!";
            case 5 -> insult = "Pray to God that you get the next one";
            case 0 -> insult = "Nope!";
            default -> throw new IllegalStateException("Unexpected value: " + new Random().nextInt(6));
        }
        priest.speak(insult, "Wrong guess");
    }

    public static void progress(int part) throws Exception {
        switch (part) {
            case 0 -> {
                priest.speak("What?! How did you guess my word? Are you consulting your devils?");
                player.speak("I've played your stupid game. Now let me go!");
                priest.speak("How dare you call such a holy procession a stupid game?\nFor your contempt, you shall have to do it over!");
                player.speak("You can't do that!");
                priest.speak("I already have.");
            }
            case 1 -> {
                priest.speak("You did it again. Maybe you are innocent. Untie her!");
                accuser.speak("NOOOOOOOOO!!!\nYOU CAN'T DO THAT!!!\nSHE'S A WITCH!!!");
                accuser.speak("ONE MORE TIME!!! TEST HER ONE MORE TIME!!!");
                priest.speak("We will try one more time and that's it!");
            }
            case 2 -> {//Victory. Plays when you have won the game
                accuser.speak("YOU CAN'T LET HER GOOO!!");
                priest.speak("I'm a man of my words. I said one more and I have to keep to that!");
                JOptionPane.showMessageDialog(null, "YOU WIN!!!");
            }
            default -> throw new Exception("Error with Story progression");
        }
    }

    public static void failed(String word){
        priest.speak("You're all out of attempts, witch");
        priest.speak("The word was " + word);
        priest.speak("TO THE STAKE!! WE SHALL BURN THIS WITCH ALIVE");
        JOptionPane.showMessageDialog(null, "You lose");
    }


    static class GameCharacter {
        private ImageIcon icon = null;
        private String title = null;

        public GameCharacter(String newIcon, String newTitle) {
            icon = new ImageIcon(newIcon);
            title = newTitle;
        }

        public GameCharacter(){
        }

        public void setIcon(String newIcon){
            if (icon == null)
                this.icon = new ImageIcon(newIcon);
        }

        public void setTitle(String newTitle){
            if (icon == null)
                this.title = newTitle;
        }

        public void speak(String wordsToSay){
            JOptionPane.showMessageDialog(null, wordsToSay, title, JOptionPane.PLAIN_MESSAGE, icon);
        }

        public void speak(String wordsToSay, String replacementTitle){
            JOptionPane.showMessageDialog(null, wordsToSay, replacementTitle, JOptionPane.PLAIN_MESSAGE, icon);
        }

    }
}
