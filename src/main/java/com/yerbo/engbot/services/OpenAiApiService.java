package com.yerbo.engbot.services;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAiApiService {
    @Value("${openai.token}")
    String openaiToken;

    public String getDefinitionOfWord (String word) {
        String prompt = String.format("Give me" +
                " simple definition of this word - %s." +
                " For example my word is 'Hello' and you should answer like this" +
                " used to express a greeting, answer a telephone," +
                " or attract attention' ", word);
        OpenAiService service = new OpenAiService(openaiToken);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(List.of(new ChatMessage("system", prompt)))
                .model("gpt-3.5-turbo")
                .build();

        List<ChatCompletionChoice> choices = service.createChatCompletion(completionRequest).getChoices();
        return choices.get(0).getMessage().getContent();

    }
}
