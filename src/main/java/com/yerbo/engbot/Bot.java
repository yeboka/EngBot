package com.yerbo.engbot;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {

    String botUsername;

    public Bot (@Qualifier("getBotToken") String botToken, @Qualifier("getBotUsername") String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
        System.out.println(botUsername);
    }

    @Override
    public void onUpdateReceived(Update update) {
        String s = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        SendMessage message = SendMessage.builder().chatId(chatId).text(s).build();
        System.out.println(s);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
