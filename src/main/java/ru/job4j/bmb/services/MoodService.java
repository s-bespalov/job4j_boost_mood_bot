package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.*;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.MoodContentRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class MoodService {
    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final MoodContentRepository moodContentRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       MoodContentRepository moodContentRepository) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.moodContentRepository = moodContentRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        var content = recommendationEngine.recommendFor(user.getChatId(), moodId);
        var moodContent = moodContentRepository.findById(moodId);
        moodContent.ifPresent(value -> moodLogRepository
                .save(new MoodLog(user, value.getMood(), Instant.now().getEpochSecond())));
        return content;
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        long oneWeekAgo = Instant.now().minus(1, ChronoUnit.WEEKS).getEpochSecond();
        var logs = getMoodLogFromDate(clientId, oneWeekAgo);
        var logsFormatted = formatMoodLogs(logs, "Weekly Moods");
        var content = new Content(chatId);
        content.setText(logsFormatted);
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        long oneMonthAgo = Instant.now().minus(1, ChronoUnit.MONTHS).getEpochSecond();
        var logs = getMoodLogFromDate(clientId, oneMonthAgo);
        var logsFormatted = formatMoodLogs(logs, "Monthly Moods");
        var content = new Content(chatId);
        content.setText(logsFormatted);
        return Optional.of(content);
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        var achievementsText = StreamSupport.stream(achievementRepository.findAll().spliterator(), true)
                .filter(achievement -> Objects.equals(achievement.getUser().getId(), clientId))
                .map(Achievement::getAward)
                .map(award -> String.format("%s : %s", award.getTitle(), award.getDescription()))
                .toList();
        var content = new Content(chatId);
        content.setText(String.join("\n", achievementsText));
        return Optional.of(content);
    }

    private List<MoodLog> getMoodLogFromDate(Long clientId, long fromDate) {
        return StreamSupport.stream(moodLogRepository.findAll().spliterator(), true)
                .filter(log -> Objects.equals(log.getUser().getId(), clientId))
                .filter(log -> log.getCreatedAt() > fromDate)
                .toList();
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\nNo mood logs found.";
        }
        var sb = new StringBuilder(title + ":\n");
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            sb.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
        });
        return sb.toString();
    }
}
