package banking.application.command;

import banking.application.operation.ConsoleOperationType;
import banking.application.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class TransferAccountCommand implements OperationCommand {
    private final AccountService accountService;
    private final Scanner scanner;

    @Autowired
    public TransferAccountCommand(AccountService accountService,
                                  Scanner scanner) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Enter source account ID:");
            String fromAccountIdInput = scanner.nextLine().trim();

            int fromAccountId;
            try {
                fromAccountId = Integer.parseInt(fromAccountIdInput);
            } catch (NumberFormatException e) {
                System.out.println("Exception: Source account ID must be a number");
                return;
            }

            System.out.println("Enter target account ID:");
            String toAccountIdInput = scanner.nextLine().trim();

            int toAccountId;
            try {
                toAccountId = Integer.parseInt(toAccountIdInput);
            } catch (NumberFormatException e) {
                System.out.println("Exception: Target account ID must be a number");
                return;
            }

            if (fromAccountId == toAccountId) {
                System.out.println("Exception: Cannot transfer to the same account");
                return;
            }

            System.out.println("Enter amount to transfer:");
            String amountInput = scanner.nextLine().trim();

            BigDecimal amount;
            try {
                amount = new BigDecimal(amountInput);
            } catch (NumberFormatException e) {
                System.out.println("Exception: Amount must be a valid number");
                return;
            }

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Exception: Transfer amount must be positive");
                return;
            }

            if (!accountService.bothAccountsExist(fromAccountId, toAccountId)) {
                if (!accountService.accountExists(fromAccountId)) {
                    System.out.println("Exception: Source account with ID " + fromAccountId
                            + " not found");
                } else {
                    System.out.println("Exception: Target account with ID " + toAccountId
                            + " not found");
                }
                return;
            }

            accountService.transfer(fromAccountId, toAccountId, amount);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
