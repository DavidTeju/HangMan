package src;

import javax.swing.*;

public class Player extends Story.GameCharacter {
    private int score = 0;
    private final String NAME;
    private static int playerNumber = 1;
    private final String PLAYER = "Player " + playerNumber;

    public Player(){//Multiplayer constructor
        NAME = askName();
        setTitle(NAME);
        playerNumber++;
    }

    public Player(String newIcon, String newTitle){//Single-player constructor
        super(newIcon, newTitle);
        NAME = askName();
    }

    private String askName () {
        String nameHolder = null;
        while (nameHolder == null)
            nameHolder = JOptionPane.showInputDialog(null, "Please enter your name", PLAYER, JOptionPane.QUESTION_MESSAGE);
        return nameHolder;
    }

    public String getNAME() {
        return NAME;
    }

    public int getScore(){
        return score;
    }

    public void awardPoint(){
        score++;
    }
}
