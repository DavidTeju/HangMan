package src;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Scorer {
    private static final Object[][] SCORE_ARRAY = {
            {1, 'A', 'E', 'I', 'L', 'N', 'O', 'R', 'S', 'T', 'U'},
            {2, 'D', 'G'},
            {3, 'B', 'C', 'M', 'P'},
            {4, 'F', 'H', 'V', 'W', 'Y'},
            {5, 'K'},
            {8, 'J', 'X'},
            {10, 'Q', 'Z'}
            };

    static int getWordScore(String word){
        int totalScore = 0;
        for (char letter: word.toCharArray()) {
            try {
                totalScore += getLetterScore(letter);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        return totalScore;
    }

    static int getLetterScore(char letter) throws Exception {
        assert Character.isLetter(letter);
        letter = Character.toUpperCase(letter);
        for (Object[] score: SCORE_ARRAY)
            for (int i = 1; i<score.length; i++)
                if ((char) score[i] == letter)
                    return (int) score[0];
        throw new Exception("This isn't supposed to happen. Score array may be corrupted");
    }

    public static void checkHighScore(int score, Player player) throws FileNotFoundException {
        Scanner leaderboard = new Scanner(new File("data-files\\leaderboard.txt"));
        List<List<Object>> leaderboardList = new ArrayList<>();

        //Load the array
        for (int i = 0; i<10; i++){
            leaderboardList.add(new ArrayList<>());
            leaderboardList.get(i).add(leaderboard.next());
            leaderboardList.get(i).add(Integer.parseInt(leaderboard.next()));
        }

        for (int i = 9; i>=0; i--)
            if ((int) leaderboardList.get(i).get(1)>score) {
                //Magic fuckery that checks for the highest score that is lower than our score and inserts our score at
                //that index by streaming an array of our name and score to a list and adding that using the List.add(int, T) method
                //Will probably need to revise this to improve readability.
                leaderboardList.add(i + 1, Arrays.stream(new Object[]{player.getNAME(), score}).toList());
                if (i<9) newHighScore(leaderboardList, i+1);
                break;
            }

        if (leaderboardList.size() == 10) {
            //if true, this would mean our score is higher than all the scores on the leaderboard, so we would need to put it
            //at the top using the same magic fuckery from before
            leaderboardList.add(0, Arrays.stream(new Object[]{player.getNAME(), score}).toList());
            newHighScore(leaderboardList, 1);
        }
    }

    private static void newHighScore(List<List<Object>> leaderboard, int pos) throws FileNotFoundException {
        var file = new PrintWriter("data-files\\leaderboard.txt");

        for (int i = 0; i<10; i++)
            file.printf("%s\t%d%n", leaderboard.get(i).get(0), (int)leaderboard.get(i).get(1));
        file.close();

        StringBuilder boardToShow = new StringBuilder(String.format("%-10s%-15s%s%n", "", "Name", "Score"));

        switch (pos) {
            case 1, 2 ->{
                boardToShow.append(String.format("%-10s%-15s%d%n", "1st", leaderboard.get(0).get(0), (int) leaderboard.get(0).get(1)));
                boardToShow.append(String.format("%-10s%-15s%d%n", "2nd", leaderboard.get(1).get(0), (int) leaderboard.get(1).get(1)));
                boardToShow.append(String.format("%-10s%-15s%d%n...", "3rd", leaderboard.get(2).get(0), (int) leaderboard.get(2).get(1)));
            }
            case 9, 10 ->{
                boardToShow.append(String.format("...%n%-10s%-15s%d%n", "8th", leaderboard.get(7).get(0), (int) leaderboard.get(7).get(1)));
                boardToShow.append(String.format("%-10s%-15s%d%n", "9th", leaderboard.get(8).get(0), (int) leaderboard.get(8).get(1)));
                boardToShow.append(String.format("%-10s%-15s%d%n", "10th", leaderboard.get(9).get(0), (int) leaderboard.get(9).get(1)));
            }
            default -> {
                boardToShow.append(String.format("...%n%-10s%-15s%d%n", getOrdinal(pos - 1), leaderboard.get(pos - 2).get(0), (int) leaderboard.get(pos - 2).get(1)));
                boardToShow.append(String.format("%-10s%-15s%d%n", getOrdinal(pos), leaderboard.get(pos - 1).get(0), (int) leaderboard.get(pos - 1).get(1)));
                boardToShow.append(String.format("%-10s%-15s%d%n...", getOrdinal(pos + 1), leaderboard.get(pos).get(0), (int) leaderboard.get(pos).get(1)));
            }
        }

        JOptionPane.showMessageDialog(null, String.format(
                "You are now %s on the leaderboard:%n%s",
                getOrdinal(pos),
                boardToShow
        ));
    }

    private static String getOrdinal(int num){
        assert num<=10 && num>0;
        return switch (num) {
            case 1 -> 1 + "st";
            case 2 -> 2 + "nd";
            case 3 -> 3 + "rd";
            default -> num + "th";
        };
    }


}
