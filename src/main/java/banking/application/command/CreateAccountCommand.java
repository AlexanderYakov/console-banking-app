package banking.application.command;

import banking.application.model.Account;
import banking.application.model.User;
import banking.application.operation.ConsoleOperationType;
import banking.application.service.AccountService;
import banking.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CreateAccountCommand implements OperationCommand {
    private final UserService userService;
    private final AccountService accountService;
    private final Scanner scanner;

    @Autowired
    public CreateAccountCommand(UserService userService,
                                AccountService accountService,
                                Scanner scanner) {
        this.userService = userService;
        this.accountService = accountService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Enter the user id for which to create an account:");
            String input = scanner.nextLine().trim();

            int userId;
            try {
                userId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Exception: User ID must be a number");
                return;
            }

            if (!userService.userExists(userId)) {
                System.out.println("Exception: User with ID " + userId + " not found");
                return;
            }

            User user = userService.getUserById(userId);
            Account account = accountService.createAccount(userId);
            String result = String.format(
                    "New account created with ID: %d for user: %s",
                    account.getId(),
                    user.getLogin()
            );

            System.out.println(result);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}
