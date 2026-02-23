package banking.application.service;

import banking.application.config.AccountProperties;
import banking.application.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountProperties accountProperties;
    private int accountId = 0;
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    @Autowired
    public AccountService(AccountProperties accountProperties) {
        this.accountProperties = accountProperties;
    }

    public Account createAccount(int userId) {
        Account account = new Account(
                ++accountId,
                userId,
                accountProperties.getDefaultAmount()
        );
        accounts.put(account.getId(), account);
        return account;
    }

    public void deposit(int accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        Account account = getAccountById(accountId);
        account.deposit(amount);
        accounts.put(account.getId(), account);
    }

    public void withdraw(int accountId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Account account = getAccountById(accountId);

        if (account.getMoneyAmount().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough money in account"
                    + accountId + ". Available: " + account.getMoneyAmount()
                    + ", requested: " + amount);
        }

        account.withdraw(amount);
        accounts.put(account.getId(), account);
    }

    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        Account fromAccount = getAccountById(fromAccountId);
        Account toAccount = getAccountById(toAccountId);

        BigDecimal commission = BigDecimal.ZERO;
        if (fromAccount.getUserId() != toAccount.getUserId()) {
            commission = BigDecimal.valueOf(accountProperties.getTransferCommission() / 100);
        }

        BigDecimal totalWithdraw = amount.add(commission.multiply(amount));
        if (fromAccount.getMoneyAmount().compareTo(totalWithdraw) < 0) {
            throw new IllegalArgumentException("Not enough money for transfer. Available: "
                    + fromAccount.getMoneyAmount() + ", required: " + totalWithdraw
                    + " (amount: " + amount + ", commission: " + commission + ")");
        }

        fromAccount.withdraw(totalWithdraw);
        toAccount.deposit(amount);

        accounts.put(fromAccount.getId(), fromAccount);
        accounts.put(toAccount.getId(), toAccount);

        String commissionInfo = String.format(" (commission: %.2f applied)", commission);
        String result = String.format(
                "Amount %.2f transferred from account ID %d to account ID %d.%s",
                amount, fromAccountId, toAccountId, commissionInfo
        );
        System.out.println(result);

    }

    public Account closeAccount(int accountId) {

        Account accountToClose = getAccountById(accountId);
        int userId = accountToClose.getUserId();

        var userAccounts = getUserAccounts(userId);

        if (userAccounts.size() <= 1) {
            throw new IllegalArgumentException("Cannot close the only account of user. User ID: "
                    + userId);
        }

        Account firstOtherAccount = userAccounts.stream()
                .filter(acc -> acc.getId() != accountId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("No other accounts found")
                );

        BigDecimal remainingBalance = accountToClose.getMoneyAmount();
        if (remainingBalance.compareTo(BigDecimal.ZERO) > 0) {
            firstOtherAccount.deposit(remainingBalance);
            accounts.put(firstOtherAccount.getId(), firstOtherAccount);
        }

        accounts.remove(accountId);
        return accountToClose;
    }

    public Account getAccountById(int accountId) {
        return Optional.ofNullable(accounts.get(accountId))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account with id " + accountId + " not found"
                ));
    }

    public List<Account> getUserAccounts(int userId) {
        return accounts.values().stream()
                .filter(account -> account.getUserId() == userId)
                .collect(Collectors.toList());
    }
}
