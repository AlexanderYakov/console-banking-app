package banking.application.service;

import banking.application.model.Account;
import banking.application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final AccountService accountService;
    private int userId = 0;
    private final HashMap<Integer, User> users = new HashMap<>();
    private final HashMap<String, User> usersByLogin = new HashMap<>();

    @Autowired
    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    public User createUser(String login) {
        if (findUserByLogin(login).isPresent()) {
            throw new IllegalArgumentException("User with login " + login
                    + " already exists");
        }
        ++userId;
        User user = new User(userId, login, new ArrayList<>());
        Account account = accountService.createAccount(user.getId());
        user.getAccountList().add(account);
        users.put(user.getId(), user);
        usersByLogin.put(user.getLogin(), user);
        return user;
    }

    public User getUserById(int id) {
        return Optional.ofNullable(users.get(id)).orElseThrow(
                () -> new IllegalArgumentException("User with ID " + id
                        + " not found")
        );
    }

    public List<User> showAllUsers() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findUserByLogin(String login) {
        return Optional.ofNullable(usersByLogin.get(login));
    }

    public String getUserDisplayInfo(int userId) {
        User user = getUserById(userId);
        List<Account> accounts = accountService.getUserAccounts(userId);
        return formatUserWithAccounts(user, accounts);
    }

    public String formatUserWithAccounts(User user, List<Account> accounts) {
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
                "User{id=%d, login='%s', accounts=%s}",
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
