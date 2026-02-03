package banking.application.command;

import banking.application.operation.ConsoleOperationType;
import banking.application.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class TransferCommand implements OperationCommand{
    private final UserAccountService userAccountService;

    @Autowired
    public TransferCommand(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public void execute(Scanner scanner) {
        try {
            System.out.println("Enter source account ID:");
            String fromAccountIdInput = scanner.nextLine().trim();

            int fromAccountId;
            try {
                fromAccountId = Integer.parseInt(fromAccountIdInput);
            } catch (NumberFormatException e) {
                System.out.println("Error: Source account ID must be a number");
                return;
            }

            if (!userAccountService.accountExists(fromAccountId)) {
                System.out.println("Error: Source account with ID " + fromAccountId + " not found");
                return;
            }

            System.out.println("Enter target account ID:");
            String toAccountIdInput = scanner.nextLine().trim();

            int toAccountId;
            try {
                toAccountId = Integer.parseInt(toAccountIdInput);
            } catch (NumberFormatException e) {
                System.out.println("Error: Target account ID must be a number");
                return;
            }

            if (!userAccountService.accountExists(toAccountId)) {
                System.out.println("Error: Target account with ID " + toAccountId + " not found");
                return;
            }

            if (fromAccountId == toAccountId) {
                System.out.println("Error: Cannot transfer to the same account");
                return;
            }

            System.out.println("Enter amount to transfer:");
            String amountInput = scanner.nextLine().trim();

            BigDecimal amount;
            try {
                amount = new BigDecimal(amountInput);
            } catch (NumberFormatException e) {
                System.out.println("Error: Amount must be a valid number");
                return;
            }

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Error: Transfer amount must be positive");
                return;
            }

            String result = userAccountService.transfer(fromAccountId, toAccountId, amount);

            System.out.println(result);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
