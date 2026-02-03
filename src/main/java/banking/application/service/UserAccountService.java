package banking.application.service;

import banking.application.AccountProperties;
import banking.application.model.Account;
import banking.application.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class UserAccountService {
    private final UserService userService;
    private final AccountService accountService;
    private final AccountProperties properties;

    @Autowired
    public UserAccountService(UserService userService,
                              AccountService accountService,
                              AccountProperties properties) {
        this.userService = userService;
        this.accountService = accountService;
        this.properties = properties;
    }

    public User createUserWithAccount(String login) {
        User user = userService.createUser(login);
        Account account = accountService.createAccount(user.getId());
        return user;
    }

    public String createAdditionalAccount(int userId) {
        User user = userService.getUserById(userId);
        Account account = accountService.createAccount(userId);
        return String.format(
                "New account created with ID: %d for user: %s",
                account.getId(),
                user.getLogin()
        );
    }

    public String getUserDisplayInfo(int userId) {
        User user = userService.getUserById(userId);
        List<Account> accounts = accountService.getUserAccounts(userId);
        return formatUserWithAccounts(user, accounts);
    }

    public String getAllUsersDisplayInfo() {
        StringBuilder result = new StringBuilder("List of all users:\n");
        List<User> users = userService.showAllUsers();
        for (User user : users) {
            List<Account> accounts = accountService.getUserAccounts(user.getId());
            result.append(formatUserWithAccounts(user, accounts))
                    .append("\n");
        }
        return result.toString();
    }

    public String closeAccount(int accountId) {
        accountService.closeAccount(accountId);
        return String.format("Account with ID %d has been closed.", accountId);
    }

    public String transfer(int fromAccountId, int toAccountId, BigDecimal amount) {
        accountService.transfer(fromAccountId, toAccountId, amount);

        Account fromAccount = accountService.getAccountById(fromAccountId);
        Account toAccount = accountService.getAccountById(toAccountId);

        String commissionInfo = "";
        if (fromAccount.getUserId() != toAccount.getUserId()) {
            double commissionRate = properties.getTransferCommission() / 100.0;
            BigDecimal commission = amount.multiply(BigDecimal.valueOf(commissionRate))
                    .setScale(2, RoundingMode.HALF_UP);
            commissionInfo = String.format(" (commission: %.2f applied)", commission);
        }

        return String.format(
                "Amount %.2f transferred from account ID %d to account ID %d.%s",
                amount, fromAccountId, toAccountId, commissionInfo
        );
    }

    public String deposit(int accountId, BigDecimal amount) {
        accountService.deposit(accountId, amount);
        return String.format(
                "Amount %.2f deposited to account ID: %d",
                amount, accountId
        );
    }

    public String withdraw(int accountId, BigDecimal amount) {
        accountService.withdraw(accountId, amount);

        return String.format(
                "Amount %.2f withdrawn from account ID: %d",
                amount, accountId
        );
    }

    public boolean userExists(int userId) {
        return userService.userExists(userId);
    }

    public boolean accountExists(int accountId) {
        try {
            accountService.getAccountById(accountId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public BigDecimal getUserTotalBalance(int userId) {
        List<Account> accounts = accountService.getUserAccounts(userId);
        return accounts.stream()
                .map(Account::getMoneyAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String formatUserWithAccounts(User user, List<Account> accounts) {
        StringBuilder accountsStr = new StringBuilder("[");
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            accountsStr.append(formatAccount(acc));
            if (i < accounts.size() - 1) {
                accountsStr.append(", ");
            }
        }
        accountsStr.append("]");

        return String.format(
                "User{id=%d, login='%s', accountList=%s}",
                user.getId(),
                user.getLogin(),
                accountsStr.toString()
        );
    }

    private String formatAccount(Account account) {
        String amountStr;
        BigDecimal amount = account.getMoneyAmount();

        if (amount.scale() <= 0 || amount.stripTrailingZeros().scale() <= 0) {
            amountStr = String.valueOf(amount.intValue());
        } else {
            amountStr = String.format("%.2f", amount);
        }

        return String.format(
                "Account{id=%d, userId=%d, moneyAmount=%s}",
                account.getId(),
                account.getUserId(),
                amountStr
        );
    }
}
