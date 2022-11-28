package com.aanovik42.smartmemecreatorbot.handler.response;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

@Getter
@Setter
public class HandlerResponse {

    private SendMessage sendMessage;
    private AnswerCallbackQuery answerCallbackQuery;
    private SendPhoto sendPhoto;

    public boolean hasSendMessage() {
        return sendMessage != null;
    }

    public boolean hasAnswerCallbackQuery() {
        return answerCallbackQuery != null;
    }

    public boolean hasSendPhoto() {
        return sendPhoto != null;
    }
}
