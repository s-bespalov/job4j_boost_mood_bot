package ru.job4j.bmb.repository;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.Mood;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MoodFakeRepository extends CrudRepositoryFake<Mood, Long> implements MoodRepository {

    public Mood save(Mood entity) {
        var lastId = memory.keySet().stream()
                .max(Comparator.naturalOrder())
                .orElse(0L);
        return memory.put(lastId + 1, entity);
    }

    public List<Mood> findAll() {
        return new ArrayList<>(memory.values());
    }

}