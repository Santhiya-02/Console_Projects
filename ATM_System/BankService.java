import java.util.HashMap;
import java.util.Map;

class BankService {

    private Map<Integer, Account> accounts = new HashMap<>();

    public void addAccount(int accNo, Account account) {
        accounts.put(accNo, account);
    }

    public Account getAccount(int accNo) {
        return accounts.get(accNo);
    }

    public Account authenticate(int accNo, int pin) {
        Account acc = accounts.get(accNo);
        if (acc != null && acc.validatePin(pin)) {
            return acc;
        }
        return null;
    }
}