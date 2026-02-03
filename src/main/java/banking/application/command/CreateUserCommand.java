package banking.application.command;

import banking.application.model.User;
import banking.application.operation.ConsoleOperationType;
import banking.application.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CreateUserCommand implements OperationCommand {
    private final UserAccountService userAccountService;

    @Autowired
    public CreateUserCommand(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public void execute(Scanner scanner) {
        try {
            System.out.println("Enter login for new user:");
            String login = scanner.nextLine().trim();

            if (login.isEmpty()) {
                System.out.println("Error: Login cannot be empty");
                return;
            }
            User user = userAccountService.createUserWithAccount(login);
            String userInfo = userAccountService.getUserDisplayInfo(user.getId());
            System.out.println("User created: " + userInfo);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }
}
