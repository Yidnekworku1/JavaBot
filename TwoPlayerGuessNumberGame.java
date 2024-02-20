package com.example;

import java.util.Random;

import org.telegram.telegrambots.meta.api.objects.User;

public class TwoPlayerGuessNumberGame {
    private int targetNumber;
    private User player1;
    private User player2;
    private boolean player1Turn;
    private boolean gameInProgress;

    public TwoPlayerGuessNumberGame(User player1) {
        this.player1 = player1;
        targetNumber = new Random().nextInt(100) + 1;
        player1Turn = true;
        gameInProgress = true;
    }
    public void makeGuess(int guessedNumber) {
        if (gameInProgress) {
            // Implement your logic to check the guessed number
            // Update game state accordingly
            // For simplicity, let's just print a message here
            if (guessedNumber == targetNumber) {
                System.out.println("Correct guess! " + player1.getFirstName() + " wins!");
                gameInProgress = false;
            } else {
                System.out.println("Incorrect guess. " + player2.getFirstName() +
                        ", guess a number using /guess <number>.");
                player1Turn = !player1Turn;
            }
        } else {
            System.out.println("Game is already finished.");
        }
    }
    public User getCurrentPlayer() {
        return player1Turn ? player1 : player2;
    }

    public boolean isPlayer1Turn() {
        return player1Turn;
    }
}
