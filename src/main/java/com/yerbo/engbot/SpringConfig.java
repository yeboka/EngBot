package com.yerbo.engbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:telegrambot.properties")
public class SpringConfig {

    @Value("${bot.token}")
    String botToken;
    @Value("${bot.name}")
    String botUsername;

    @Bean
    public String getBotToken(){
        return botToken;
    }
    @Bean
    public String getBotUsername() {
        return botUsername;
    }
}
