package com.example;


import java.util.Random;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class RandomGame extends TelegramLongPollingBot{

    private int secretNumber;
    private String player1ChatId;
    private String player2ChatId;
    private boolean isPlayer1Turn = true;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            var msg = update.getMessage();
            var user = msg.getFrom();
            String chatId = msg.getChatId().toString();
            String text = msg.getText();

            CreateChatInviteLink createChatInviteLink = new CreateChatInviteLink();
            createChatInviteLink.setChatId(chatId);
            
            sendTextMessage(chatId, createChatInviteLink.getMethod());

            if (text.equals("/startgame")) {
                startGame(chatId);
            } else if (text.matches("\\d+")) {
                int guess = Integer.parseInt(text);
                handleGuess(chatId, guess);
            } else {
                sendTextMessage(chatId, "Invalid input. Please enter a number.");
            }
            
        }

    }

    private void startGame(String chatId) {
        player1ChatId = chatId;
        sendTextMessage(chatId, "Game started! Player 1's turn to guess a number between 1 and 10.");
        secretNumber = new Random().nextInt(10) + 1;
    }

    private void handleGuess(String chatId, int guess) {
        if ((isPlayer1Turn && chatId == player1ChatId) || (!isPlayer1Turn && chatId == player2ChatId)) {
            if (guess == secretNumber) {
                sendTextMessage(chatId, "Correct! The number was " + secretNumber + ". Player " + (isPlayer1Turn ? "1" : "2") + " wins!");
                resetGame();
            } else {
                sendTextMessage(chatId, "Incorrect guess. Try again, Player " + (isPlayer1Turn ? "1" : "2") + ".");
                isPlayer1Turn = !isPlayer1Turn;
            }
        } else {
            sendTextMessage(chatId, "It's not your turn. Wait for the other player to guess.");
        }
    }

    private void sendTextMessage(String chatId, String text) {
        SendMessage sm = SendMessage.builder()
        .chatId(chatId) //Who are we sending a message to
        .text(text).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } 
        catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }  
    }
    private void resetGame(){
        player1ChatId = "";
        player2ChatId = "";
        isPlayer1Turn = true;
        secretNumber = 0;
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
