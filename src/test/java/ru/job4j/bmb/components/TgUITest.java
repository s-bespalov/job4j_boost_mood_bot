package ru.job4j.bmb.components;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.repository.MoodFakeRepository;
import ru.job4j.bmb.repository.MoodRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {TgUI.class, MoodFakeRepository.class})
class TgUITest {
    @Autowired
    @Qualifier("moodFakeRepository")
    private MoodRepository moodRepository;

    @Autowired
    private TgUI ui;

    @Test
    public void whenBtnGood() {
        assertThat(moodRepository).isNotNull();
        var expected = "Good Test";
        moodRepository.save(new Mood("Good Test", true));
        var markup = ui.buildButtons();
        var actual = markup.getKeyboard().iterator().next().iterator().next().getText();
        assertThat(actual).isEqualTo(expected);
    }
}
