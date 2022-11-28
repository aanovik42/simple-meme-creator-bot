package com.aanovik42.smartmemecreatorbot.bot;

import com.aanovik42.smartmemecreatorbot.handler.response.HandlerResponse;
import com.aanovik42.smartmemecreatorbot.processor.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramBot extends TelegramLongPollingBot {

    private final String username;
    private final String token;
    private final Processor processor;

    public TelegramBot(@Value("${bot.username}") String username,
                       @Value("${bot.token}") String token,
                       Processor processor) {
        this.username = username;
        this.token = token;
        this.processor = processor;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {

        HandlerResponse handlerResponse = processor.process(update);

        if (handlerResponse.hasSendPhoto())
            sendImage(handlerResponse.getSendPhoto());
        if (handlerResponse.hasSendMessage())
            sendMessage(handlerResponse.getSendMessage());
        if (handlerResponse.hasAnswerCallbackQuery())
            sendCallbackQueryAnswer(handlerResponse.getAnswerCallbackQuery());
    }

    private void sendMessage(SendMessage message) {

        message.enableHtml(true);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendImage(SendPhoto imageRequest) {

        imageRequest.setParseMode("html");
        try {
            execute(imageRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendCallbackQueryAnswer(AnswerCallbackQuery callbackQueryAnswer) {
        try {
            execute(callbackQueryAnswer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
