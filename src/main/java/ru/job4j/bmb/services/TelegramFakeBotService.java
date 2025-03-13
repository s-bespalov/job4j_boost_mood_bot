package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.conditions.TelegramFakeCondition;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.interfaces.SendContent;

@Service
@Conditional(TelegramFakeCondition.class)
public class TelegramFakeBotService extends TelegramLongPollingBot implements SendContent {
    private final String botName;

    public TelegramFakeBotService(@Value("${telegram.bot.name}") String botName,
                                 @Value("${telegram.bot.token}") String botToken) {
        super(botToken);
        this.botName = botName;
        System.out.println("Im Fake Telegram Service");
    }

    @Override
    public void send(Content content) {

    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().getText() != null) {
            System.out.println(update.getMessage().getText());
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}
