import java.util.Scanner;

class ATMSystem {

    private BankService bankService;

    public ATMSystem(BankService bankService) {
        this.bankService = bankService;
    }

    public void start() {

        Scanner sc = new Scanner(System.in);

        System.out.println("1. User Login");
        System.out.println("2. Admin Login");

        int type = sc.nextInt();

        if (type == 1) {
            userMenu(sc);
        } else {
            adminMenu(sc);
        }
    }

    private void userMenu(Scanner sc) {

        System.out.print("Enter Account No: ");
        int accNo = sc.nextInt();

        System.out.print("Enter PIN: ");
        int pin = sc.nextInt();

        Account acc = bankService.authenticate(accNo, pin);

        if (acc == null) {
            System.out.println("Invalid login");
            return;
        }

        while (true) {
            System.out.println("\n1.Withdraw 2.Deposit 3.Balance 4.Change PIN 5.Transfer 6.Mini Statement 7.Exit");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter amount: ");
                    acc.withdraw(sc.nextInt());
                    break;

                case 2:
                    System.out.print("Enter amount: ");
                    acc.deposit(sc.nextInt());
                    break;

                case 3:
                    System.out.println("Balance: " + acc.getBalance());
                    break;

                case 4:
                    System.out.print("Enter old PIN: ");
                    int oldPin = sc.nextInt();
                    System.out.print("Enter new PIN: ");
                    int newPin = sc.nextInt();
                    acc.changePin(oldPin, newPin);
                    break;

                case 5:
                    System.out.print("Enter target account: ");
                    int targetAcc = sc.nextInt();
                    System.out.print("Enter amount: ");
                    int amt = sc.nextInt();

                    Account target = bankService.getAccount(targetAcc);
                    if (target != null) {
                        acc.transfer(target, amt);
                    } else {
                        System.out.println("Target not found");
                    }
                    break;

                case 6:
                    acc.showMiniStatement();
                    break;

                case 7:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void adminMenu(Scanner sc) {

        System.out.print("Enter Admin PIN: ");
        int pin = sc.nextInt();

        if (pin != 9999) {
            System.out.println("Wrong Admin PIN");
            return;
        }

        while (true) {
            System.out.println("\n1.Deposit to User 2.Check Balance 3.View Transactions 4.Exit");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter account: ");
                    int accNo = sc.nextInt();
                    Account acc = bankService.getAccount(accNo);
                    if (acc != null) {
                        System.out.print("Enter amount: ");
                        acc.deposit(sc.nextInt());
                    }
                    break;

                case 2:
                    System.out.print("Enter account: ");
                    accNo = sc.nextInt();
                    acc = bankService.getAccount(accNo);
                    if (acc != null) {
                        System.out.println("Balance: " + acc.getBalance());
                    }
                    break;

                case 3:
                    System.out.print("Enter account: ");
                    accNo = sc.nextInt();
                    acc = bankService.getAccount(accNo);
                    if (acc != null) {
                        acc.showMiniStatement();
                    }
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}