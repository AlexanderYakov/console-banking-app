package banking.application.command;

import banking.application.operation.ConsoleOperationType;

public interface OperationCommand {
    void execute();
    ConsoleOperationType getOperationType();
}
