package ru.job4j.bmb.services;

import org.junit.jupiter.api.Test;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.events.UserEvent;
import ru.job4j.bmb.interfaces.SendContent;
import ru.job4j.bmb.model.User;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class AchievementServiceTest {

    @Test
    public void whenReceiveAchievement() {
        var result = new ArrayList<Content>();
        var sendContent = new SendContent() {
            @Override
            public void send(Content content) {
                result.add(content);
            }
        };
        var achievementService = new AchievementService(sendContent);
        var user = new User();
        user.setClientId(100);
        var userEvent = new UserEvent(this, user);
        var expected = "Сервис AchievementService получил событие UserEvent от вас";
        achievementService.onApplicationEvent(userEvent);
        var actual = result.iterator().next().getText();
        assertThat(actual).isEqualTo(expected);
    }
}