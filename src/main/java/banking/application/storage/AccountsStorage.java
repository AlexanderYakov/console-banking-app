package banking.application.storage;

import banking.application.AccountStorage;
import banking.application.model.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountsStorage implements AccountStorage {
    HashMap<Integer, Account> accounts = new HashMap<>();


    @Override
    public Account save(Account account) {
        accounts.put(account.getId(), account);
        return account;
    }

    @Override
    public Optional<Account> findById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public Account getAccountById(int id) {
        return accounts.get(id);
    }

    @Override
    public List<Account> findByUserId(int id) {
        return accounts.values().stream()
                .filter(account -> account.getUserId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(int accountId) {
        accounts.remove(accountId);
    }

    public Boolean existsById(int id) {
        return accounts.get(id) != null;
    }
}
