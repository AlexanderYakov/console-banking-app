package banking.application.generator;

import org.springframework.stereotype.Component;

@Component
public class UserIdGenerator {
    private int currentId = 1;

    public int getNextId() {
        return currentId++;
    }
}
