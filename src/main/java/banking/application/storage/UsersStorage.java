package banking.application.storage;

import banking.application.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class UsersStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private final HashMap<String, User> usersByLogin = new HashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        usersByLogin.put(user.getLogin(), user);
        return user;
    }

    @Override
    public List<User> showAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(usersByLogin.get(login));
    }

    @Override
    public Boolean existsByLogin(String login) {
        return usersByLogin.get(login) != null;
    }

    @Override
    public Boolean existsById(int userId) {
        return users.get(userId) != null;
    }


}
