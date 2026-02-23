package banking.application.command;

import banking.application.operation.ConsoleOperationType;
import banking.application.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.InputMismatchException;
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
            int fromAccountId = scanner.nextInt();

            System.out.println("Enter target account ID:");
            int toAccountId = scanner.nextInt();

            System.out.println("Enter amount to transfer:");
            BigDecimal amount = scanner.nextBigDecimal();

            scanner.nextLine();

            accountService.transfer(fromAccountId, toAccountId, amount);

        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Exception: All inputs must be valid numbers");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
