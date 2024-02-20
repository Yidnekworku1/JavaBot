package com.example;

import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class TwoPlayerGuessNumberBot extends TelegramLongPollingBot{

        private final Map<Long, TwoPlayerGuessNumberGame> games = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            User user = update.getMessage().getFrom();
          

            // Check if the message starts a new game
            if ("/startgame".equalsIgnoreCase(messageText)) {
                startNewGame(chatId, user);
            } else if ("/joingame".equalsIgnoreCase(messageText)) {
                invitePlayer(chatId, user);
            } else {
                // Otherwise, process the guess
                processGuess(chatId, messageText);
            }
        }
    }

    private void startNewGame(long chatId, User player1) {
        // Check if there's an existing game in the chat
        if (games.containsKey(chatId)) {
            sendTextMessage(chatId, "A game is already in progress. Wait for it to finish.");
        } else {
            TwoPlayerGuessNumberGame game = new TwoPlayerGuessNumberGame(player1);
            games.put(chatId, game);

            sendTextMessage(chatId, "New two-player guess the number game started! " + player1.getFirstName() +
                    " guesses a number using /guess <number>." +
                    "\nPlayer 2, join the game using /joingame.");
        }
    }
    private void invitePlayer(long chatId, User inviter) {
        TwoPlayerGuessNumberGame game = games.get(chatId);

        if (game != null) {
            String inviteLink = generateInviteLink(chatId);
            sendTextMessage(chatId, inviter.getFirstName() + ", use the following invite link to join the game:\n" + inviteLink);
        } else {
            sendTextMessage(chatId, "No active game to join. Start a new game using /startgame.");
        }
    }
    /**
     * @param chatId
     * @return
     */
    private String generateInviteLink(long chatId) {
        CreateChatInviteLink createChatInviteLink = new CreateChatInviteLink();
        createChatInviteLink.setChatId(getBaseUrl());
        .setExpireDate(60); // Set the expiration time for the invite link in seconds

        try {
            return execute(createChatInviteLink).getInviteLink();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating invite link.";
        }
    }
    private void processGuess(long chatId, String guess) {
        TwoPlayerGuessNumberGame game = games.get(chatId);

        if (game != null && game.isPlayer1Turn()) {
            int guessedNumber;
            try {
                guessedNumber = Integer.parseInt(guess);
            } catch (NumberFormatException e) {
                sendTextMessage(chatId, "Invalid guess. Please enter a valid number.");
                return;
            }

            game.makeGuess(guessedNumber);
            sendTextMessage(chatId, game.getCurrentPlayer().getFirstName() +
                    ", guess a number using /guess <number>.");
        } else {
            sendTextMessage(chatId, "It's not your turn to guess. Wait for the other player's turn.");
        }
    }
    private void sendTextMessage(long chatId, String text) {
    SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "GuessNumber";
    }

    @Override
    public String getBotToken() {
        return "6772580095:AAFItz7c4IiEPUNKYhsH4I6X1sj-NdI50ow";
    }

    
}
