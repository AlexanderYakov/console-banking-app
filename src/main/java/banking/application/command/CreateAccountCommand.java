package banking.application.command;

import banking.application.operation.ConsoleOperationType;
import banking.application.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CreateAccountCommand implements OperationCommand {
    private final UserAccountService userAccountService;

    @Autowired
    public CreateAccountCommand(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public void execute(Scanner scanner) {
        try {
            System.out.println("Enter the user id for which to create an account:");
            String input = scanner.nextLine().trim();

            int userId;
            try {
                userId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: User ID must be a number");
                return;
            }

            if (!userAccountService.userExists(userId)) {
                System.out.println("Error: User with ID " + userId + " not found");
                return;
            }

            String result = userAccountService.createAdditionalAccount(userId);

            System.out.println(result);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}
