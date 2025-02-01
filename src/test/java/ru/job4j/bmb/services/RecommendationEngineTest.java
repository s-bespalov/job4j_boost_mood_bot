package ru.job4j.bmb.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.job4j.bmb.content.ContentProviderFake;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@ContextConfiguration(classes = {RecommendationEngine.class, ContentProviderFake.class})
class RecommendationEngineTest {

    @Autowired
    private RecommendationEngine recommendationEngine;

    @Test
    public void whenRecommendGood() {
        var content = recommendationEngine.recommendFor(100L, 100L);
        var actual = content.getText();
        var expected = "Test";
        assertThat(actual).isEqualTo(expected);
    }
}