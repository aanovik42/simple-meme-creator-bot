package com.aanovik42.smartmemecreatorbot.handler.keyboard;

import com.aanovik42.smartmemecreatorbot.entity.MemeTemplate;
import com.aanovik42.smartmemecreatorbot.restclient.RestClient;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardCreator {

    private final RestClient restClient;

    public InlineKeyboardCreator(RestClient restClient) {
        this.restClient = restClient;
    }

    public InlineKeyboardMarkup createMemeKeyboard() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<MemeTemplate> memeTemplates = restClient.getAllMemeTemplates();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (MemeTemplate memeTemplate : memeTemplates) {
            String memeTemplateName = memeTemplate.getName();
            String memeTemplateId = memeTemplate.getId().toString();
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(memeTemplateName)
                    .callbackData(memeTemplateId)
                    .build();
            buttons.add(button);
        }

        List<List<InlineKeyboardButton>> keyboard = Lists.partition(buttons, 2);

        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }
}
