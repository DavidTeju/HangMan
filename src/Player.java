package src;

import javax.swing.*;
import java.util.Random;

public class Player extends Story.GameCharacter {
    private int score = 0;
    private final String name;
    private static int playerNumber = 1;
    private final String PLAYER = "Player " + playerNumber;

    public Player(){//Multiplayer constructor
        name = askName();
        setTitle(name);
        playerNumber++;
    }

    public Player(String newIcon, String newTitle){//Single-player constructor
        super(newIcon, newTitle);
        name = askName();
    }

    private String askName () {
        String nameHolder = null;
        while (nameHolder == null)
            nameHolder = JOptionPane.showInputDialog(null, "Please enter your name", PLAYER, JOptionPane.QUESTION_MESSAGE);
        return nameHolder;
    }

    public String getName() {
        return name;
    }

    public int getScore(){
        return score;
    }

    public void awardPoint(){
        score++;
    }
}
