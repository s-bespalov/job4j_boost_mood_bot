package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    @PostConstruct
    public void init() {
        System.out.println("Bean 'RecommendationService' is going through @PostConstruct init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean 'RecommendationService' will be destroyed via @PreDestroy.");
    }
}
