package banking.application.model;

import java.math.BigDecimal;

public class Account {
    private final int id;
    private final int userId;
    private BigDecimal moneyAmount;

    public Account(int id, int userId, BigDecimal initialBalance) {
        this.id = validatePositive(id, "Account ID");
        this.userId = validatePositive(userId, "User ID");
        moneyAmount = validateBalance(initialBalance);
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

    private static int validatePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
        return value;
    }

    private static BigDecimal validateBalance(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        return balance;
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
