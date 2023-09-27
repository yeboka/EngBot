package com.yerbo.engbot.utils;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Setter
@NoArgsConstructor
public class ReplyKeyboardMarkupBuilder {
    private Long chatId;

    private List<KeyboardRow> keyboard = new ArrayList<>();
    private KeyboardRow keyboardRow;

    public static ReplyKeyboardMarkupBuilder builder() {
        return new ReplyKeyboardMarkupBuilder();
    }

    public ReplyKeyboardMarkupBuilder row() {
        this.keyboardRow = new KeyboardRow();
        return this;
    }

    public ReplyKeyboardMarkupBuilder setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    public ReplyKeyboardMarkupBuilder addButton(String text) {
        this.keyboardRow.add(text);
        return this;
    }

    public ReplyKeyboardMarkup build() {
        this.keyboard.add(keyboardRow);
        this.keyboardRow = null;
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }

}
