package banking.application.service;

import banking.application.AccountProperties;
import banking.application.generator.AccountIdGenerator;
import banking.application.model.Account;
import banking.application.storage.AccountsStorage;
import banking.application.storage.UsersStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final AccountsStorage accountsStorage;
    private final UsersStorage usersStorage;
    private final AccountIdGenerator accountIdGenerator;
    private final AccountProperties accountProperties;

    @Autowired
    public AccountService(AccountsStorage accountsStorage,
                          UsersStorage usersStorage,
                          AccountIdGenerator accountIdGenerator,
                          AccountProperties accountProperties) {
        this.accountsStorage = accountsStorage;
        this.usersStorage = usersStorage;
        this.accountIdGenerator = accountIdGenerator;
        this.accountProperties = accountProperties;
    }

    public Account createAccount(int userId) {
        if (!usersStorage.existsById(userId)) {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
        Account account = new Account(
                accountIdGenerator.getNextId(),
                userId,
                accountProperties.getDefaultAmount()
        );
        return accountsStorage.save(account);
    }

    public void deposit(int accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        Account account = getAccountById(accountId);
        account.deposit(amount);
        accountsStorage.save(account);
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
        accountsStorage.save(account);
    }

    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        if (!accountsStorage.existsById(fromAccountId)) {
            throw new IllegalArgumentException("Account with id: " + fromAccountId
            + " doesn't exists");
        }

        if (!accountsStorage.existsById(toAccountId)) {
            throw new IllegalArgumentException("Account with id: " + toAccountId
                    + " doesn't exists");
        }

        Account fromAccount = accountsStorage.getAccountById(fromAccountId);
        Account toAccount = accountsStorage.getAccountById(toAccountId);

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

        accountsStorage.save(fromAccount);
        accountsStorage.save(toAccount);
        
    }

    public void closeAccount(int accountId) {
        if (!accountsStorage.existsById(accountId)) {
            throw new IllegalArgumentException("Not found account with id: " + accountId);
        }

        Account accountToClose = accountsStorage.getAccountById(accountId);
        int userId = accountToClose.getUserId();

        var userAccounts = accountsStorage.findByUserId(userId);

        if (userAccounts.size() <= 1) {
            throw new IllegalArgumentException("Cannot close the only account of user. User ID: "
                    + userId);
        }

        Account firstOtherAccount = userAccounts.stream()
                .filter(acc -> acc.getId() != accountId)
                .findFirst()
                .orElseThrow( () ->
                        new IllegalArgumentException("No other accounts found")
                );

        BigDecimal remainingBalance = accountToClose.getMoneyAmount();
        if (remainingBalance.compareTo(BigDecimal.ZERO) > 0) {
            firstOtherAccount.deposit(remainingBalance);
            accountsStorage.save(firstOtherAccount);
        }

        accountsStorage.delete(accountId);
    }

    public Account getAccountById(int accountId) {
        return accountsStorage.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account with id " + accountId + " not found"
                ));
    }

    public java.util.List<Account> getUserAccounts(int userId) {
        return accountsStorage.findByUserId(userId);
    }

}
