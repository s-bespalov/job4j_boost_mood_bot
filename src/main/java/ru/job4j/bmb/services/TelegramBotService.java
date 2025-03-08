package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.conditions.TelegramRealCondition;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.exceptions.SendContentException;
import ru.job4j.bmb.interfaces.SendContent;

import java.util.Objects;

@Service
@Conditional(TelegramRealCondition.class)
public class TelegramBotService extends TelegramLongPollingBot implements SendContent {
    private final BotCommandHandler handler;
    private final String botName;

    public TelegramBotService(@Value("${telegram.bot.name}") String botName,
                              @Value("${telegram.bot.token}") String botToken,
                              BotCommandHandler handler) {
        super(botToken);
        this.handler = handler;
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handler.handleCallback(update.getCallbackQuery())
                    .ifPresent(this::send);
        } else if (update.hasMessage() && update.getMessage().getText() != null) {
            handler.commands(update.getMessage())
                    .ifPresent(this::send);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void send(Content content) {
        try {
            if (Objects.nonNull(content.getAudio())) {
                sendAudio(content);
            } else if (Objects.nonNull(content.getPhoto())) {
                sendPhoto(content);
            } else if (Objects.nonNull(content.getMarkup())) {
                sendTextWithMarkup(content);
            } else if (Objects.nonNull(content.getText())) {
                sendPlainText(content);
            }
        } catch (TelegramApiException e) {
           throw new SendContentException(e);
        }
    }

    private void sendAudio(Content content) throws TelegramApiException {
        var sendAudio = new SendAudio();
        sendAudio.setAudio(content.getAudio());
        sendAudio.setChatId(content.getChatId());
        if (Objects.nonNull(content.getText())) {
            sendAudio.setCaption(content.getText());
        }
        execute(sendAudio);
    }

    private void sendTextWithMarkup(Content content) throws TelegramApiException {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(content.getChatId());
        sendMessage.setReplyMarkup(content.getMarkup());
        if (Objects.nonNull(content.getText())) {
            sendMessage.setText(content.getText());
        }
        execute(sendMessage);
    }

    private void sendPlainText(Content content) throws TelegramApiException {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(content.getChatId());
        sendMessage.setText(content.getText());
        execute(sendMessage);
    }

    private void sendPhoto(Content content) throws TelegramApiException {
        var sendPhoto = new SendPhoto();
        sendPhoto.setChatId(content.getChatId());
        sendPhoto.setPhoto(content.getPhoto());
        if (Objects.nonNull(content.getText())) {
            sendPhoto.setCaption(content.getText());
        }
        execute(sendPhoto);
    }
}