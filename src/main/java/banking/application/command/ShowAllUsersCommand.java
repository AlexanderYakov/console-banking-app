package banking.application.command;

import banking.application.model.Account;
import banking.application.model.User;
import banking.application.operation.ConsoleOperationType;
import banking.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowAllUsersCommand implements OperationCommand {
    private final UserService userService;

    @Autowired
    public ShowAllUsersCommand(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void execute() {
        try {
            StringBuilder result = new StringBuilder("List of all users:\n");
            List<User> users = userService.showAllUsers();
            for (User user : users) {
                List<Account> accounts = user.getAccountList();
                result.append(userService.formatUserWithAccounts(user, accounts))
                        .append("\n");
            }
            System.out.print(result);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }
}
