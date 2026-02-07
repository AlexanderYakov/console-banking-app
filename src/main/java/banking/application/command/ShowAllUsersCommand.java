package banking.application.command;

import banking.application.model.Account;
import banking.application.model.User;
import banking.application.operation.ConsoleOperationType;
import banking.application.service.AccountService;
import banking.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowAllUsersCommand implements OperationCommand {
    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public ShowAllUsersCommand(UserService userService,
                               AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }


    @Override
    public void execute() {
        try {
            StringBuilder result = new StringBuilder("List of all users:\n");
            List<User> users = userService.showAllUsers();
            for (User user : users) {
                List<Account> accounts = accountService.getUserAccounts(user.getId());
                result.append(userService.formatUserWithAccounts(user, accounts))
                        .append("\n");
            }
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }
}
