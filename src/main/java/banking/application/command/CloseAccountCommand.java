package banking.application.command;

import banking.application.model.Account;
import banking.application.model.User;
import banking.application.operation.ConsoleOperationType;
import banking.application.service.AccountService;
import banking.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class CloseAccountCommand implements OperationCommand {
    private final UserService userService;
    private final AccountService accountService;
    private final Scanner scanner;

    @Autowired
    public CloseAccountCommand(UserService userService,
                               AccountService accountService,
                               Scanner scanner) {
        this.userService = userService;
        this.accountService = accountService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Enter account ID to close:");

            int accountId = scanner.nextInt();
            scanner.nextLine();

            Account account = accountService.closeAccount(accountId);
            User user = userService.getUserById(account.getUserId());
            user.getAccountList().removeIf(acc -> acc.getId() == accountId);

            System.out.printf("Account with ID %d has been closed.%n", accountId);

        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Exception: Account ID must be a number");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
