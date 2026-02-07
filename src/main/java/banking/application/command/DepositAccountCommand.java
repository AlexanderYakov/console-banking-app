package banking.application.command;

import banking.application.operation.ConsoleOperationType;
import banking.application.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class DepositAccountCommand implements OperationCommand {
    private final AccountService accountService;
    private final Scanner scanner;

    @Autowired
    public DepositAccountCommand(AccountService accountService,
                                 Scanner scanner) {
        this.accountService = accountService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Enter account ID:");
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

            System.out.println("Enter amount to deposit:");
            String amountInput = scanner.nextLine().trim();

            BigDecimal amount;
            try {
                amount = new BigDecimal(amountInput);
            } catch (NumberFormatException e) {
                System.out.println("Exception: Amount must be a valid number");
                return;
            }

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Exception: Deposit amount must be positive");
                return;
            }
            accountService.deposit(accountId, amount);
            String result = String.format(
                    "Amount %.2f deposited to account ID: %d",
                    amount, accountId);

            System.out.println(result);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
