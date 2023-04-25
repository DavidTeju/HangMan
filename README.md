# Hangman Game

This is a simple Hangman game implemented in Java. It supports single-player and multiplayer modes, with options for playing with two or more players. You have a limited number of attempts to guess the secret word by suggesting letters one at a time.

This project was my first ever programming project, which I developed during my freshman year. It holds a special place in my heart. This game helped me explore my passion for programming and served as the foundation for my programming journey.

## Features

- Single-player and multiplayer modes
- Support for 2 or more players in multiplayer mode
- Round-based gameplay
- Score tracking for each player
- Simple user interface with JOptionPane
- Unique story and dialogue elements

## Installation

To install and run the game, simply clone the repository and compile the Java files in your preferred development environment.

## How to Play

### Single-player Mode

1. Run the `Main.java` file.
2. Select 'Single-player' mode.
3. Follow the on-screen instructions and story prompts.
4. You will be given a secret word, and you have to guess the word one letter at a time.
5. You have a limited number of attempts to guess the word correctly.
6. If you successfully guess the word before running out of attempts, you will progress to the next round.
7. Complete all rounds to win the game.

### Multiplayer Mode

1. Run the `Main.java` file.
2. Select 'Multiplayer' mode.
3. Enter the number of players and the number of rounds you wish to play.
4. Players will take turns as the "word maker" and the "guesser."
5. The word maker provides a secret word, and the guesser has to guess the word one letter at a time.
6. Players have a limited number of attempts to guess the word correctly.
7. After each round, scores are updated, and players switch roles.
8. The player with the highest score at the end of all rounds wins the game.

## Files

- `Main.java`: The main file that handles game logic and user interface.
- `Player.java`: Represents a player in the game and handles scoring and player names.
- `Story.java`: Provides the story and dialogue elements for the single-player mode.
- `Word.java`: Handles the secret word generation, guessing, and attempts tracking.

## Final Thoughts

This hangman game project remains a cherished memory from my early programming days. It not only represents the beginning of my programming journey but also embodies the joy of storytelling and game development. I am excited to share it with you and hope you enjoy playing it as much as I enjoyed creating it.
