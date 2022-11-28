package com.aanovik42.smartmemecreatorbot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class MessageCreator {

    public SendMessage createSendMessage(String chatId, String reply) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(reply);
        return sendMessage;
    }

    public SendMessage createSendMessage(String chatId, String reply, InlineKeyboardMarkup keyboard) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(reply);
        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }

    public SendPhoto createSendPhoto(String chatId, String imageUri) {

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(imageUri));
        return sendPhoto;
    }

    public SendPhoto createSendPhoto(String chatId, String imageUri, String caption) {

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(imageUri));
        sendPhoto.setCaption(caption);
        return sendPhoto;
    }

    public AnswerCallbackQuery createAnswerCallbackQuery (String callbackQueryId) {

        AnswerCallbackQuery callbackQueryAnswer = new AnswerCallbackQuery();
        callbackQueryAnswer.setCallbackQueryId(callbackQueryId);
        return callbackQueryAnswer;
    }
}
