package banking.application.command;

import banking.application.operation.ConsoleOperationType;

public interface OperationCommand {
    void execute(java.util.Scanner scanner);
    ConsoleOperationType getOperationType();
}
