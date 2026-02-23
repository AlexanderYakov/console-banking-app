package banking.application.command;

import banking.application.operation.ConsoleOperationType;
import banking.application.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class WithdrawAccountCommand implements OperationCommand {
    private final AccountService accountService;
    private final Scanner scanner;

    @Autowired
    public WithdrawAccountCommand(AccountService accountService,
                                  Scanner scanner) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Enter account ID to withdraw from:");
            int accountId = scanner.nextInt();

            System.out.println("Enter amount to withdraw:");
            BigDecimal amount = scanner.nextBigDecimal();
            scanner.nextLine();

            accountService.withdraw(accountId, amount);

            String result = String.format(
                    "Amount %.2f withdrawn from account ID: %d",
                    amount, accountId
            );
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
        return ConsoleOperationType.ACCOUNT_WITHDRAW;
    }
}
