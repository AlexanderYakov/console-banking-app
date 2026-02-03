package banking.application.console;

import banking.application.command.OperationCommand;
import banking.application.operation.ConsoleOperationType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OperationsConsoleListener implements Runnable {
    private final List<OperationCommand> commands;
    private final Map<ConsoleOperationType, OperationCommand> commandMap;
    private final Scanner scanner;

    @Autowired
    public OperationsConsoleListener(List<OperationCommand> commands) {
        this.commands = commands;
        this.commandMap = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    @PostConstruct
    public void init() {
        for (OperationCommand command : commands) {
            commandMap.put(command.getOperationType(), command);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                printAvailableOperations();

                String input = scanner.nextLine().trim();

                if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                    System.out.println("Exiting application...");
                    break;
                }

                ConsoleOperationType operationType;
                try {
                    operationType = ConsoleOperationType.valueOf(input);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: Unknown operation type: " + input);
                    System.out.println("Please enter a valid operation type from the list above.");
                    continue;
                }

                OperationCommand command = commandMap.get(operationType);
                if (command == null) {
                    System.out.println("Error: No command handler for operation: " + operationType);
                    continue;
                }

                command.execute(scanner);

            } catch (Exception e) {
                System.out.println("Unexpected error in main loop: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private void printAvailableOperations() {
        System.out.println("\nPlease enter one of operation type:");

        Arrays.stream(ConsoleOperationType.values())
                .sorted(Comparator.comparing(Enum::name))
                .forEach(opType -> System.out.println("-" + opType));

        System.out.print("> ");
    }

}
