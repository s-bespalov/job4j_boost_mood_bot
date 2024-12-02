package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodContent;

@Repository
public interface MoodContentRepository extends CrudRepository<MoodContent, Long> {
}
