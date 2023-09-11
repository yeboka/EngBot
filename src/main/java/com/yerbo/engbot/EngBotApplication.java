package com.yerbo.engbot;

import com.yerbo.engbot.services.BotService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class EngBotApplication {

    public static void main(String[] args) throws TelegramApiException {
        ApplicationContext applicationContext = SpringApplication.run(EngBotApplication.class, args);
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        BotService bot = applicationContext.getBean(BotService.class);
        telegramBotsApi.registerBot(bot);


    }
}
