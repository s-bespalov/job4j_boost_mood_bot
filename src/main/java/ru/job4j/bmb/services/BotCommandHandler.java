package ru.job4j.bmb.services;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.bmb.components.TgUI;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class BotCommandHandler {
    private final UserRepository userRepository;
    private final MoodService moodService;
    private final TgUI tgUI;

    public BotCommandHandler(UserRepository userRepository,
                             MoodService moodService,
                             TgUI tgUI) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
    }

    Optional<Content> commands(Message message) {
        var chatId = message.getChatId();
        var clientId = message.getFrom().getId();
        var text = message.getText();
        System.out.println(text);
        return switch (text) {
            case "/start" -> handleStartCommand(chatId, clientId);
            case "/week_mood_log" -> moodService.weekMoodLogCommand(chatId, clientId);
            case "/month_mood_log" -> moodService.monthMoodLogCommand(chatId, clientId);
            case "/award" -> moodService.awards(chatId, clientId);
            default -> Optional.empty();
        };
    }

    Optional<Content> handleCallback(CallbackQuery callback) {
        var moodId = Long.valueOf(callback.getData());
        var client = callback.getFrom().getId();
        return StreamSupport.stream(userRepository.findAll().spliterator(), true)
                .filter(u -> Objects.equals(u.getClientId(), client))
                .map(user -> moodService.chooseMood(user, moodId))
                .findFirst();
    }

    private Optional<Content> handleStartCommand(long chatId, Long clientId) {
        var user = new User();
        user.setClientId(clientId);
        user.setChatId(chatId);
        userRepository.save(user);
        System.out.println(userRepository.findById(user.getId()));
        var content = new Content(user.getChatId());
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }
}
