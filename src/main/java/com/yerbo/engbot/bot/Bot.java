package com.yerbo.engbot.bot;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {

    String botUsername;

    public Bot(@Qualifier("getBotToken") String botToken, @Qualifier("getBotUsername") String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        String s = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String prompt = String.format("Give me translation to Russian language and simple definition of this word - {%s}, and highlight this word. For example my word is 'Hello' and you should answer like this '{Hello} : {Привет} - used to express a greeting, answer a telephone, or attract attention' ", s);

        OpenAiService service = new OpenAiService("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");
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(List.of(new ChatMessage("system", prompt)))
                .model("gpt-3.5-turbo")
                .build();
        List<ChatCompletionChoice> choices = service.createChatCompletion(completionRequest).getChoices();
        SendMessage message = SendMessage.builder().chatId(chatId).text(choices.get(0).getMessage().getContent()).build();
        execute(message);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
