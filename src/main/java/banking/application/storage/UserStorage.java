package banking.application.storage;

import banking.application.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User save(User user);
    List<User> showAll();
    Optional<User> findById(int id);
    Optional <User> findByLogin(String login);
    Boolean existsByLogin(String login);
    Boolean existsById(int userId);
}
