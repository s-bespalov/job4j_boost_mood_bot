package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.events.UserEvent;
import ru.job4j.bmb.interfaces.SendContent;

@Service
public class AchievementService implements BeanNameAware, ApplicationListener<UserEvent> {

    private final SendContent sendContent;

    public AchievementService(SendContent sendContent) {
        this.sendContent = sendContent;
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean 'AchievementService' is going through @PostConstruct init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean 'AchievementService' will be destroyed via @PreDestroy.");
    }

    @Override
    public void setBeanName(@NonNull String name) {
        System.out.println(name);
    }

    @Override
    public void onApplicationEvent(UserEvent event) {
        var user = event.getUser();
        var content = new Content(user.getChatId());
        content.setText("Сервис AchievementService получил событие UserEvent от вас");
        sendContent.send(content);
    }
}
