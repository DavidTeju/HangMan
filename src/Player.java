import javax.swing.*;
import java.util.Random;

public class Player extends Story.NPC {
    private int score = 0;
    private final String name;
    private static int playerNumber = 1;
    private final String PLAYER = "Player " + playerNumber;
    private static int iconNumber = 0;

    public Player(){
        this.name = JOptionPane.showInputDialog(null, "Please enter your name", PLAYER, JOptionPane.QUESTION_MESSAGE);
        iconNumber+=(new Random().nextInt(0, 2)); //Use this method to ensure that there is never a duplicate icon for two players
        iconNumber%=3;
        setIcon("smile"+iconNumber);
        setTitle(name);
        playerNumber++;
    }
    public Player(String newIcon, String newTitle){
        super(newIcon, newTitle);
        this.name = JOptionPane.showInputDialog(null, "Please enter your name", PLAYER, JOptionPane.QUESTION_MESSAGE);
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

    public String getPlayer() {
        return PLAYER;
    }
}
