public class ATM {

    public static void main(String[] args) {

        BankService bank = new BankService();

        bank.addAccount(101, new Account(1234, 10000));
        bank.addAccount(102, new Account(4321, 5000));

        ATMSystem atm = new ATMSystem(bank);
        atm.start();
    }
}