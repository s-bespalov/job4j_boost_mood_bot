package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;

@Service
public class BotCommandHandler implements BeanNameAware {

    @PostConstruct
    public void init() {
        System.out.println("Bean 'BotCommandHandler' is going through @PostConstruct init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean 'BotCommandHandler' will be destroyed via @PreDestroy.");
    }

    void receive(Content content) {
        System.out.println(content);
    }

    @Override
    public void setBeanName(@NonNull String name) {
        System.out.println(name);
    }
}