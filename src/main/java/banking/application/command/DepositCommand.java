package banking.application.command;

import banking.application.operation.ConsoleOperationType;
import banking.application.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class DepositCommand implements OperationCommand {
    private final UserAccountService userAccountService;

    @Autowired
    public DepositCommand(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public void execute(Scanner scanner) {
        try {
            System.out.println("Enter account ID:");
            String accountIdInput = scanner.nextLine().trim();

            int accountId;
            try {
                accountId = Integer.parseInt(accountIdInput);
            } catch (NumberFormatException e) {
                System.out.println("Error: Account ID must be a number");
                return;
            }

            if (!userAccountService.accountExists(accountId)) {
                System.out.println("Error: Account with ID " + accountId + " not found");
                return;
            }

            System.out.println("Enter amount to deposit:");
            String amountInput = scanner.nextLine().trim();

            BigDecimal amount;
            try {
                amount = new BigDecimal(amountInput);
            } catch (NumberFormatException e) {
                System.out.println("Error: Amount must be a valid number");
                return;
            }

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Error: Deposit amount must be positive");
                return;
            }

            String result = userAccountService.deposit(accountId, amount);

            System.out.println(result);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
