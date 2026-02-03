package banking.application;

import banking.application.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountStorage {
    Account save(Account account);
    Optional<Account> findById(int id);
    List<Account> findByUserId(int id);
    void delete(int accountId);
}
