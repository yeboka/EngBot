package com.yerbo.engbot.services;

import com.yerbo.engbot.enums.Status;
import com.yerbo.engbot.models.User;
import com.yerbo.engbot.models.Vocabulary;
import com.yerbo.engbot.repository.UserRepository;
import com.yerbo.engbot.repository.VocabularyRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class BotService extends TelegramLongPollingBot {

    String botUsername;
    OpenAiApiService openAiApiService;
    GoogleTranslateService translateService;
    UserRepository userRepository;
    VocabularyRepository vocabularyRepository;
    User user;

    @Autowired
    public BotService(@Value("${bot.token}") String botToken,
                      @Value("${bot.name}") String botUsername,
                      OpenAiApiService openAiApiService,
                      GoogleTranslateService translateService,
                      UserRepository userRepository,
                      VocabularyRepository vocabularyRepository) {
        super(botToken);
        this.botUsername = botUsername;
        this.openAiApiService = openAiApiService;
        this.translateService = translateService;
        this.userRepository = userRepository;
        this.vocabularyRepository = vocabularyRepository;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getChat().getUserName();

        saveUser(chatId, username);

        String[] translate = translateService.translateText(text);
        Vocabulary vocabularyItem = Vocabulary.builder()
                .user(user)
                .engWord(translate[0].toLowerCase())
                .rusWord(translate[1].toLowerCase())
                .status(Status.NEW)
                .build();

        String answer = vocabularyItem.getEngWord() + " - " + vocabularyItem.getRusWord();

        if (text.split(" ").length == 1 && !vocabularyRepository.existsByUserAndEngWord(user, vocabularyItem.getEngWord())) {
            vocabularyRepository.save(vocabularyItem);

//            String definitionOfWord = openAiApiService.getDefinitionOfWord(text);
//            answer += "\n" + definitionOfWord;
        }



        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(answer).build();

        execute(message);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void saveUser(Long chatId, String username) {
        if (userRepository.existsById(chatId)) {
            user = userRepository.findById(chatId).orElse(null);
        } else {
            user = User.builder()
                    .id(chatId)
                    .username(username)
                    .build();

            userRepository.save(user);
        }
    }
}
