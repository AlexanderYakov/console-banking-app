package banking.application.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final int id;

    private final String login;

    public User(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login;
    }
}
