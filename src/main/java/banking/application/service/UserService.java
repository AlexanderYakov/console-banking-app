package banking.application.service;

import banking.application.generator.UserIdGenerator;
import banking.application.model.User;
import banking.application.storage.UsersStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UsersStorage usersStorage;
    private final UserIdGenerator idGenerator;

    @Autowired
    public UserService(UsersStorage usersStorage, UserIdGenerator idGenerator) {
        this.usersStorage = usersStorage;
        this.idGenerator = idGenerator;
    }

    public User createUser(String login) {
        if (usersStorage.existsByLogin(login)) {
            throw new IllegalArgumentException("User with login " + login
                    + " already exists");
        }
        int userId = idGenerator.getNextId();
        User user = new User(userId, login);
        usersStorage.save(user);
        return user;
    }

    public User getUserById(int id) {
        return usersStorage.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User with ID " + id
                        + " not found")
        );
    }

    public List<User> showAllUsers() {
        return usersStorage.showAll();
    }

    public boolean userExists(int id) {
       return usersStorage.findById(id).isPresent();
    }

    public boolean isLoginAvailable(String login) {
        return usersStorage.existsByLogin(login);
    }

    public Optional<User> findUserByLogin(String login) {
        return usersStorage.findByLogin(login);
    }
}
