package banking.application.command;

import banking.application.operation.ConsoleOperationType;
import banking.application.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CloseAccountCommand implements OperationCommand {
    private final AccountService accountService;
    private final Scanner scanner;

    @Autowired
    public CloseAccountCommand(AccountService accountService,
                               Scanner scanner) {
        this.accountService = accountService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Enter account ID to close:");
            String accountIdInput = scanner.nextLine().trim();

            int accountId;
            try {
                accountId = Integer.parseInt(accountIdInput);
            } catch (NumberFormatException e) {
                System.out.println("Exception: Account ID must be a number");
                return;
            }

            if (!accountService.accountExists(accountId)) {
                System.out.println("Exception: Account with ID " + accountId + " not found");
                return;
            }

            String result = accountService.closeAccount(accountId);

            System.out.println(result);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
