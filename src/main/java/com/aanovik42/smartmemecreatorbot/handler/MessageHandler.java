package com.aanovik42.smartmemecreatorbot.handler;

import com.aanovik42.smartmemecreatorbot.entity.Chat;
import com.aanovik42.smartmemecreatorbot.entity.MemeTemplate;
import com.aanovik42.smartmemecreatorbot.entity.enums.ChatState;
import com.aanovik42.smartmemecreatorbot.handler.response.HandlerResponse;
import com.aanovik42.smartmemecreatorbot.handler.keyboard.InlineKeyboardCreator;
import com.aanovik42.smartmemecreatorbot.repository.ChatRepo;
import com.aanovik42.smartmemecreatorbot.restclient.RestClient;
import com.aanovik42.smartmemecreatorbot.restclient.exception.TextLineTooLongException;
import com.aanovik42.smartmemecreatorbot.restclient.model.RequestModel;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MessageHandler implements Handler<Message> {

    private final String COMMAND_PREFIX = "/";
    private final InlineKeyboardCreator inlineKeyboardCreator;
    private final ChatRepo chatRepo;
    private final RestClient restClient;
    private final MessageCreator messageCreator;

    public MessageHandler(InlineKeyboardCreator inlineKeyboardCreator, ChatRepo chatRepo, RestClient restClient, MessageCreator messageCreator) {
        this.inlineKeyboardCreator = inlineKeyboardCreator;
        this.chatRepo = chatRepo;
        this.restClient = restClient;
        this.messageCreator = messageCreator;
    }

    @Override
    public HandlerResponse choose(Message message) {

        String chatId = message.getChatId().toString();
        HandlerResponse handlerResponse;

        if (!message.hasText()) {
            String reply = "Please send me a text";
            SendMessage sendMessage = messageCreator.createSendMessage(chatId, reply);
            handlerResponse = new HandlerResponse();
            handlerResponse.setSendMessage(sendMessage);
            return handlerResponse;
        }

        String messageText = message.getText().trim();
        if (messageText.startsWith(COMMAND_PREFIX)) {
            handlerResponse = handleCommand(chatId, messageText);
        } else {
            handlerResponse = handleText(chatId, messageText);
        }

        return handlerResponse;
    }

    private HandlerResponse handleCommand(String chatId, String messageText) {

        String commandIdentifier = messageText.split(" ")[0].toLowerCase();

        HandlerResponse handlerResponse = new HandlerResponse();
        switch (commandIdentifier) {
            case "/start":
            case "/create":
                handlerResponse = createStartMessage(chatId);
                break;
            default:
                String reply = "Please enter a valid command";
                SendMessage sendMessage = messageCreator.createSendMessage(chatId, reply);
                handlerResponse.setSendMessage(sendMessage);
        }

        return handlerResponse;
    }

    private HandlerResponse handleText(String chatId, String messageText) {

        HandlerResponse handlerResponse;
        Optional<Chat> optionalChat = chatRepo.findChatByChatId(chatId);
        if (optionalChat.isPresent() &&
                optionalChat.get().getChatState() == ChatState.AWAITING_TEXT) {
            handlerResponse = createMeme(optionalChat.get(), messageText);
        } else {
            handlerResponse = createStartMessage(chatId);
        }

        return handlerResponse;
    }

    private HandlerResponse createStartMessage(String chatId) {

        HandlerResponse handlerResponse = new HandlerResponse();

        String reply = "✨<b>Choose your meme:</b>";
        SendMessage sendMessage;
        try {
            InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeyboardCreator.createMemeKeyboard();
            sendMessage = messageCreator.createSendMessage(chatId, reply, inlineKeyboardMarkup);
        } catch (Exception e) {
            reply = "Oops, something went wrong. We're working on it, please try again later.";
            sendMessage = messageCreator.createSendMessage(chatId, reply);
        }
        handlerResponse.setSendMessage(sendMessage);
        Chat chat = new Chat(chatId, ChatState.AWAITING_MEME_CHOICE);
        chatRepo.save(chat);
        return handlerResponse;
    }

    private HandlerResponse createMeme(Chat chat, String messageText) {

        HandlerResponse handlerResponse = new HandlerResponse();

        String chatId = chat.getChatId();
        List<String> memeText = Arrays.asList(messageText.split("\\r?\\n"));
        MemeTemplate memeTemplate = chat.getMemeTemplate();
        if (memeTemplate.getBoxCount() != memeText.size()) {
            String reply = "Please send me <b>" + memeTemplate.getBoxCount() + "</b> lines of text.";
            SendMessage sendMessage = messageCreator.createSendMessage(chatId, reply);
            handlerResponse.setSendMessage(sendMessage);
            return handlerResponse;
        }

        memeText = memeText.stream()
                .map(textLine -> textLine.equals("&empty") ? "" : textLine)
                .collect(Collectors.toList());


        RequestModel requestModel = new RequestModel((long) memeTemplate.getId(), memeText);
        try {
            String imageUri = restClient.createMemeAndGetImageUri(requestModel);

            SendPhoto sendPhoto = messageCreator.createSendPhoto(chatId, imageUri);
            handlerResponse.setSendPhoto(sendPhoto);

            String reply = "Let's make another one? /create";
            SendMessage sendMessage = messageCreator.createSendMessage(chatId, reply);
            handlerResponse.setSendMessage(sendMessage);

            chat.setChatState(ChatState.NO_STATE);
            chatRepo.save(chat);
        } catch (TextLineTooLongException e) {
            String reply = e.getFormattedMessage();
            SendMessage sendMessage = messageCreator.createSendMessage(chatId, reply);
            handlerResponse.setSendMessage(sendMessage);
        } catch (RuntimeException e) {
            String reply = "Oops, can't create this meme. Maybe try another one?";
            SendMessage sendMessage = messageCreator.createSendMessage(chatId, reply);
            handlerResponse.setSendMessage(sendMessage);
        }

        return handlerResponse;
    }
}
