package com.yerbo.engbot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardMarkupBuilder {
    private Long chatId;

    private List<InlineKeyboardButton> row = new ArrayList<>();
    private final List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

    public static InlineKeyboardMarkupBuilder builder() {
        return new InlineKeyboardMarkupBuilder();
    }

    public InlineKeyboardMarkupBuilder setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    public InlineKeyboardMarkupBuilder addButton(String text, String callback) {
        this.row.add(InlineKeyboardButton.builder().text(text).callbackData(callback).build());
        return this;
    }

    public InlineKeyboardMarkupBuilder abbNewRow() {
        this.keyboard.add(this.row);
        this.row = new ArrayList<>();
        return this;
    }

    public InlineKeyboardMarkup build() {
        this.keyboard.add(this.row);
        this.row = null;
        return InlineKeyboardMarkup.builder()
                .keyboard(keyboard).build();
    }
}
