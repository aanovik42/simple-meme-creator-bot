package com.aanovik42.smartmemecreatorbot.handler;

import com.aanovik42.smartmemecreatorbot.entity.Chat;
import com.aanovik42.smartmemecreatorbot.entity.MemeTemplate;
import com.aanovik42.smartmemecreatorbot.entity.enums.ChatState;
import com.aanovik42.smartmemecreatorbot.handler.response.HandlerResponse;
import com.aanovik42.smartmemecreatorbot.repository.ChatRepo;
import com.aanovik42.smartmemecreatorbot.restclient.RestClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery> {

    private final ChatRepo chatRepo;
    private final RestClient restClient;
    private final MessageCreator messageCreator;

    public CallbackQueryHandler(ChatRepo chatRepo, RestClient restClient, MessageCreator messageCreator) {
        this.chatRepo = chatRepo;
        this.restClient = restClient;
        this.messageCreator = messageCreator;
    }

    @Override
    public HandlerResponse choose(CallbackQuery callbackQuery) {

        String chatId = callbackQuery.getMessage().getChat().getId().toString();
        Long memeId = Long.parseLong(callbackQuery.getData());

        HandlerResponse handlerResponse = new HandlerResponse();

        try {
            MemeTemplate memeTemplate = restClient.getMemeTemplate(memeId);
            Chat chat = new Chat(chatId, ChatState.AWAITING_TEXT, memeTemplate);
            chatRepo.save(chat);

            String sampleImageUrl = memeTemplate.getSampleImageUrl();

            StringBuilder reply = new StringBuilder("Send me " + memeTemplate.getBoxCount() + " lines of text, like this:");
            for(int i = 1; i <= memeTemplate.getBoxCount(); i++)
                reply.append("\n<i>Sample Text ").append(i).append("</i>");
            reply.append("\nFor empty line type <i>&empty</i>");
            SendPhoto sendPhoto = messageCreator.createSendPhoto(chatId, sampleImageUrl, reply.toString());
            handlerResponse.setSendPhoto(sendPhoto);
        } catch (HttpClientErrorException e) {
            String reply = "Oops, can't create this meme. Maybe try another one?";
            SendMessage sendMessage = messageCreator.createSendMessage(chatId, reply);
            handlerResponse.setSendMessage(sendMessage);
        } catch (Exception e) {
            String reply = "Oops, something went wrong. We're working on it, please try again later.";
            SendMessage sendMessage = messageCreator.createSendMessage(chatId, reply);
            handlerResponse.setSendMessage(sendMessage);
        }

        AnswerCallbackQuery callbackQueryAnswer = messageCreator.createAnswerCallbackQuery(callbackQuery.getId());
        handlerResponse.setAnswerCallbackQuery(callbackQueryAnswer);

        return handlerResponse;
    }
}
