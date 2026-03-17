import java.util.ArrayList;
import java.util.List;

class Account implements BankOperations {

    private int pin;
    private int balance;
    private List<String> transactions = new ArrayList<>();

    public Account(int pin, int balance) {
        this.pin = pin;
        this.balance = balance;
    }

    public boolean validatePin(int inputPin) {
        return this.pin == inputPin;
    }

    public void changePin(int oldPin, int newPin) {
        if (this.pin == oldPin) {
            this.pin = newPin;
            System.out.println("PIN changed successfully");
        } else {
            System.out.println("Wrong old PIN");
        }
    }

    @Override
    public void deposit(int amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount");
            return;
        }
        balance += amount;
        transactions.add("Deposited: " + amount);
        System.out.println("Deposit successful. Balance: " + balance);
    }

    @Override
    public void withdraw(int amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount");
        } else if (amount > balance) {
            System.out.println("Insufficient balance");
        } else {
            balance -= amount;
            transactions.add("Withdrawn: " + amount);
            System.out.println("Withdraw successful. Balance: " + balance);
        }
    }

    @Override
    public int getBalance() {
        return balance;
    }

    public void showMiniStatement() {
        System.out.println("Mini Statement:");
        for (String t : transactions) {
            System.out.println(t);
        }
    }

    public void transfer(Account target, int amount) {
        if (amount <= 0 || amount > balance) {
            System.out.println("Transfer failed");
        } else {
            this.balance -= amount;
            target.balance += amount;
            transactions.add("Transferred: " + amount);
            System.out.println("Transfer successful");
        }
    }
}