package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    default List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay) {
        List<User> voted = StreamSupport.stream(findAll().spliterator(), true)
                .filter(moodLog -> moodLog.getCreatedAt() >= startOfDay)
                .filter(moodLog -> moodLog.getCreatedAt() <= endOfDay)
                .map(MoodLog::getUser)
                .distinct()
                .toList();
        return StreamSupport.stream(findAll().spliterator(), true)
                .map(MoodLog::getUser)
                .filter(user -> !voted.contains(user))
                .distinct()
                .toList();
    }
}
