package com.yerbo.engbot.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotService extends TelegramLongPollingBot {

    String botUsername;
    OpenAiApiService openAiApiService;
    GoogleTranslateService translateService;

    @Autowired
    public BotService(@Value("${bot.token}") String botToken,
                      @Value("${bot.name}") String botUsername,
                      OpenAiApiService openAiApiService,
                      GoogleTranslateService translateService) {
        super(botToken);
        this.botUsername = botUsername;
        this.openAiApiService = openAiApiService;
        this.translateService = translateService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        StringBuilder answer = new StringBuilder();


        String translateText = translateService.translateText(text);
        answer.append(text).append(" - ").append(translateText);
        answer.append("\n");

        if (text.split(" ").length == 1) {
            String definitionOfWord = openAiApiService.getDefinitionOfWord(text);
            answer.append(definitionOfWord);
            answer.append("\n");
            String translateOfDefinition = translateService.translateText(definitionOfWord);
            answer.append(translateOfDefinition);
        }
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(answer.toString()).build();

        execute(message);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
