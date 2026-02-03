package banking.application.model;

import java.math.BigDecimal;

public class Account {
    private final int id;
    private final int userId;
    private BigDecimal moneyAmount;

    public Account(int id, int userId, BigDecimal initialBalance) {
        if (id <= 0) {
            throw new IllegalArgumentException("Account ID must be positive");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        if (initialBalance == null) {
            throw new IllegalArgumentException("Initial balance cannot be null");
        }
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        this.id = id;
        this.userId = userId;
        this.moneyAmount = initialBalance;
    }


    public Account(int id, int userId, double initialBalance) {
        this(id, userId, BigDecimal.valueOf(initialBalance));
    }

    public void deposit(BigDecimal amount) {
        this.moneyAmount = this.moneyAmount.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        this.moneyAmount = this.moneyAmount.subtract(amount);
    }
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", moneyAmount=" + moneyAmount.toString() +
                '}';
    }
}
