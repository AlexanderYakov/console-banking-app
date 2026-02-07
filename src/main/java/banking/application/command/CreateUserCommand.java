package banking.application.command;

import banking.application.model.User;
import banking.application.operation.ConsoleOperationType;
import banking.application.service.AccountService;
import banking.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CreateUserCommand implements OperationCommand {
    private final UserService userService;
    private final AccountService accountService;
    private final Scanner scanner;

    @Autowired
    public CreateUserCommand(Scanner scanner,
                             UserService userService,
                             AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Enter login for new user:");
            String login = scanner.nextLine().trim();

            if (login.isEmpty()) {
                System.out.println("Exception: Login cannot be empty");
                return;
            }
            User user = userService.createUser(login);
            accountService.createAccount(user.getId());
            String userInfo = userService.getUserDisplayInfo(user.getId());
            System.out.println("User created: " + userInfo);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }
}
