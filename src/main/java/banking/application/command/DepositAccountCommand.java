package banking.application.command;

import banking.application.operation.ConsoleOperationType;
import banking.application.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.InputMismatchException;
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
            int accountId = scanner.nextInt();

            System.out.println("Enter amount to deposit:");
            BigDecimal amount = scanner.nextBigDecimal();
            scanner.nextLine();

            accountService.deposit(accountId, amount);
            String result = String.format(
                    "Amount %.2f deposited to account ID: %d",
                    amount, accountId);

            System.out.println(result);

        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Exception: All inputs must be valid numbers");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
