package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
