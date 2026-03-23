import java.util.ArrayList;
import java.util.Scanner;

// OOP Concept: Encapsulation - Borrower handles all borrower-specific actions
public class Borrower {

    private Library lib;
    private Scanner sc;

    public Borrower(Library lib, Scanner sc) {
        this.lib = lib;
        this.sc  = sc;
    }

    public void showMenu(User user) {
        while (true) {
            System.out.println("\n===== Borrower Menu [" + user.getName() + "] =====");
            System.out.println("Deposit: Rs." + user.getDeposit());
            System.out.println("1. View Available Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. Extend Borrow");
            System.out.println("5. Exchange Book");
            System.out.println("6. Mark Book as Lost");
            System.out.println("7. Report Card Lost");
            System.out.println("8. Pay Fine");
            System.out.println("9. My Borrows");
            System.out.println("10. My Fines");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            String ch = sc.nextLine();
            if      (ch.equals("1"))  viewBooks();
            else if (ch.equals("2"))  borrowBook(user);
            else if (ch.equals("3"))  returnBook(user);
            else if (ch.equals("4"))  extendBorrow(user);
            else if (ch.equals("5"))  exchangeBook(user);
            else if (ch.equals("6"))  markLost(user);
            else if (ch.equals("7"))  { System.out.println(lib.cardLost(user.getEmail())); }
            else if (ch.equals("8"))  payFine(user);
            else if (ch.equals("9"))  myBorrows(user);
            else if (ch.equals("10")) myFines(user);
            else if (ch.equals("0"))  break;
            else System.out.println("Invalid choice.");
        }
    }

    void viewBooks() {
        System.out.println("\n--- Available Books ---");
        boolean any = false;
        for (Book b : lib.books)
            if (b.getAvailableQty() > 0) { b.display(); any = true; }
        if (!any) System.out.println("No books available.");
    }

    void borrowBook(User user) {
        System.out.print("Enter ISBN or Title: "); String q = sc.nextLine();
        System.out.print("Today's date (DD/MM/YYYY): "); String today = sc.nextLine();
        String due = lib.addDays(today, 15);
        System.out.println(lib.borrowBook(user.getEmail(), q, today, due));
    }

    void returnBook(User user) {
        System.out.print("Enter ISBN or Title: "); String q = sc.nextLine();
        System.out.print("Return date (DD/MM/YYYY): "); String date = sc.nextLine();
        System.out.println(lib.returnBook(user.getEmail(), q, date));
    }

    void extendBorrow(User user) {
        System.out.print("Enter ISBN or Title: "); String q = sc.nextLine();
        Transaction t = lib.findActiveBorrow(user.getEmail(), q);
        if (t == null) { System.out.println("No active borrow found."); return; }
        String newDue = lib.addDays(t.getDueDate(), 15);
        System.out.println(lib.extendBorrow(user.getEmail(), q, newDue));
    }

    void exchangeBook(User user) {
        System.out.print("Current book (ISBN or Title): "); String cur = sc.nextLine();
        System.out.print("Return date (DD/MM/YYYY): ");     String date = sc.nextLine();
        System.out.println(lib.returnBook(user.getEmail(), cur, date));

        System.out.print("New book (ISBN or Title): "); String nw = sc.nextLine();
        System.out.print("Today's date (DD/MM/YYYY): "); String today = sc.nextLine();
        String due = lib.addDays(today, 15);
        System.out.println(lib.borrowBook(user.getEmail(), nw, today, due));
    }

    void markLost(User user) {
        System.out.print("Enter ISBN or Title: ");
        System.out.println(lib.markLost(user.getEmail(), sc.nextLine()));
    }

    void payFine(User user) {
        ArrayList<Transaction> unpaid = lib.getUnpaidFines(user.getEmail());
        if (unpaid.isEmpty()) { System.out.println("No pending fines."); return; }
        System.out.println("Pending Fines:");
        for (int i = 0; i < unpaid.size(); i++) {
            System.out.print((i + 1) + ". ");
            unpaid.get(i).displayFine();
        }
        System.out.print("Select fine number: "); int idx = Integer.parseInt(sc.nextLine());
        System.out.println("Pay via: 1.Cash  2.Deposit Deduction");
        System.out.print("Choice: "); String mode = sc.nextLine();
        System.out.println(lib.payFine(user.getEmail(), idx, mode));
    }

    void myBorrows(User user) {
        System.out.println("\n--- My Borrows ---");
        boolean any = false;
        for (Transaction t : lib.borrows)
            if (t.getBorrowerEmail().equalsIgnoreCase(user.getEmail())) { t.displayBorrow(); any = true; }
        if (!any) System.out.println("No borrow history.");
    }

    void myFines(User user) {
        System.out.println("\n--- My Fines ---");
        boolean any = false;
        for (Transaction f : lib.fines)
            if (f.getBorrowerEmail().equalsIgnoreCase(user.getEmail())) { f.displayFine(); any = true; }
        if (!any) System.out.println("No fines on record.");
    }
}
