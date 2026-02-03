package banking.application.command;

import banking.application.operation.ConsoleOperationType;
import banking.application.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ShowAllUsersCommand implements OperationCommand {
    private final UserAccountService userAccountService;

    @Autowired
    public ShowAllUsersCommand(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }


    @Override
    public void execute(Scanner scanner) {
        try {
            String allUsersInfo = userAccountService.getAllUsersDisplayInfo();
            System.out.println(allUsersInfo);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }
}
