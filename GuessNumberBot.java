package com.example;

import java.util.ArrayList;
import java.util.List;

import org.jvnet.hk2.internal.Creator;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder;
import org.telegram.telegrambots.meta.api.objects.ChatInviteLink;
import org.telegram.telegrambots.meta.api.objects.ChatJoinRequest;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.passport.PassportData;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class GuessNumberBot extends TelegramLongPollingBot {
    private int secretNumber;
    private long player1ChatId;
    private long player2ChatId;
    private boolean isPlayer1Turn = true;


    @Override
    public void onUpdateReceived(Update update) {
        
        var msg = update.getMessage();
        var user = msg.getFrom();

 
        String chatId = msg.getChatId().toString();
        ChatJoinRequest chatJoinRequest = new ChatJoinRequest();
        

        chatJoinRequest.getInviteLink();

        System.out.println(chatJoinRequest.getChat());
    
        System.out.println(user.getFirstName() + " wrote " + msg.getText());

        if (msg.getText().equalsIgnoreCase("hey")) {
            sendText(user.getId(), "ሰላም" + user.getFirstName());
        }
        if (msg.getText().equalsIgnoreCase("link")) {
            String inviteLink = exportChatInviteLink(chatId);
            sendText(user.getId(), "Here is your link:\n" + inviteLink);
        }
        if (msg.getText().equalsIgnoreCase("/start")) {

          ChatInviteLink chatInviteLink = new ChatInviteLink();
          //chatInviteLink.setInviteLink("https://t.me/EskiGemet_bot/?start=");
          chatInviteLink.setInviteLink(getBaseUrl());
          String link = chatInviteLink.getInviteLink();

        
          chatInviteLink.setCreator(user);
          chatInviteLink.setCreatesJoinRequest(true);
          
          
          sendText(user.getId(), "link is : "+ link );
          sendText(user.getId(), "requested by : " + chatInviteLink.getCreator().getFirstName());
          


        }
         

 
        
    }

    private String exportChatInviteLink(String chatId) {
 
        try{
           String lin = execute(exportChatInviteLink);
           System.out.println(lin);
            return lin;
            
            
        }
        catch(TelegramApiException e){
            return "Error Exporting";

        }
        
    }

    @Override
    public String getBotToken() {

        return "6772580095:AAFItz7c4IiEPUNKYhsH4I6X1sj-NdI50ow";
    }

    @Override
    public String getBotUsername() {
        
        return "GuessNumber";
    }
    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
        .chatId(who.toString()) //Who are we sending a message to
        .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } 
        catch (TelegramApiException e) {
        throw new RuntimeException(e);      //Any error will be printed here
   }



}
    
}
