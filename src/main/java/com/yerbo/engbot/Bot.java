package com.yerbo.engbot;

import com.yerbo.engbot.enums.Status;
import com.yerbo.engbot.models.User;
import com.yerbo.engbot.models.Vocabulary;
import com.yerbo.engbot.services.GoogleTranslateService;
import com.yerbo.engbot.services.OpenAiApiService;
import com.yerbo.engbot.services.UserService;
import com.yerbo.engbot.services.VocabularyService;
import com.yerbo.engbot.utils.ReplyKeyboardMarkupBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class Bot extends TelegramLongPollingBot {

    String botUsername;
    OpenAiApiService openAiApiService;
    GoogleTranslateService translateService;
    VocabularyService vocabularyService;
    UserService userService;
    User user;

    @Autowired
    public Bot(@Value("${bot.token}") String botToken,
               @Value("${bot.name}") String botUsername,
               OpenAiApiService openAiApiService,
               GoogleTranslateService translateService,
               UserService userRepository,
               VocabularyService vocabularyService) {
        super(botToken);
        this.botUsername = botUsername;
        this.openAiApiService = openAiApiService;
        this.translateService = translateService;
        this.vocabularyService = vocabularyService;
        this.userService = userRepository;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()){
            if (update.getCallbackQuery().getData().startsWith("#"))
                updateVocabularyPage(update.getCallbackQuery());
            return;
        }

        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getChat().getUserName();

        saveUser(chatId, username);

        if (text.equals("/start")) {
            start(chatId);
        } else if (text.equals("Vocabulary")) {
            showVocabulary(chatId);
        } else {
            translate(text, chatId);
        }


    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void saveUser(Long chatId, String username) {
        user = userService.saveUser(chatId, username);
    }

    public void translate(String text, Long chatId) throws TelegramApiException, IOException {
        String[] translate = translateService.translateText(text);
        Vocabulary vocabularyItem = Vocabulary.builder()
                .user(user)
                .engWord(translate[0].toLowerCase())
                .rusWord(translate[1].toLowerCase())
                .status(Status.NEW)
                .build();

        String answer = vocabularyItem.getEngWord() + " - " + vocabularyItem.getRusWord();

        if (text.split(" ").length == 1) {
            vocabularyService.saveWord(user, vocabularyItem);
        }
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(answer)
                .build();

        execute(message);
    }

    public void start(Long chatId) throws TelegramApiException {
        String greeting = "It is vocabulary bot!";

        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkupBuilder.builder()
                .row()
                .addButton("Vocabulary")
                .setChatId(chatId)
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(greeting)
                .replyMarkup(keyboardMarkup)
                .build();

        execute(message);
    }

    public void showVocabulary(Long chatId) throws TelegramApiException {
        Page<Vocabulary> words = vocabularyService.getPageOfWordsOfUser(user, 0);

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(getPageText(words))
                .replyMarkup(getVocabularyKeyboard(-1, 2))
                .build();

        if (words.getTotalPages() == 1) {
            message.setReplyMarkup(getVocabularyKeyboard(-1, -1));
        }

        execute(message);
    }

    public void updateVocabularyPage(CallbackQuery callbackQuery) throws TelegramApiException {

        String callbackData = callbackQuery.getData();

        if (callbackData.startsWith("#")) {
            int pageNum = Integer.parseInt(callbackData.substring(1)) - 1;
            Page<Vocabulary> words = vocabularyService.getPageOfWordsOfUser(user, pageNum);

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .text(getPageText(words))
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .replyMarkup(
                            getVocabularyKeyboard(pageNum, (words.getTotalPages() == pageNum+1) ? -1 : pageNum + 2)
                    )
                    .build();
            execute(editMessage);
        }
    }

    public String getPageText(Page<Vocabulary> words) {
        StringBuilder messageText = new StringBuilder();
        messageText.append("YOU'RE VOCABULARY\n");
        words.getContent().forEach(vocab -> messageText.append(vocab.getEngWord()).append(" - ").append(vocab.getRusWord()).append("\n"));

        return messageText.toString();
    }

    public InlineKeyboardMarkup getVocabularyKeyboard(int previusPageId, int nextPageId) {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        if (previusPageId > 0) row.add(InlineKeyboardButton.builder()
                        .text("<< " + previusPageId)
                        .callbackData("#" + previusPageId)
                .build());

        if (nextPageId > 0) row.add(InlineKeyboardButton.builder()
                    .text(nextPageId + " >>")
                    .callbackData("#" + nextPageId)
                .build());

        keyboard.add(row);

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
    }
}
