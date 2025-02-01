package ru.job4j.bmb.content;

public class ContentProviderFake implements ContentProvider {
    @Override
    public Content byMood(Long chatId, Long moodId) {
        var content = new Content(chatId);
        content.setText("Test");
        return content;
    }
}
