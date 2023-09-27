package com.yerbo.engbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@RestController
public class EngBotApplication {

    public static void main(String[] args) throws TelegramApiException {
        ApplicationContext applicationContext = SpringApplication.run(EngBotApplication.class, args);
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = applicationContext.getBean(Bot.class);
        telegramBotsApi.registerBot(bot);
    }

}
