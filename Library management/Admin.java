import java.util.ArrayList;
import java.util.Scanner;

// OOP Concept: Inheritance - Admin extends User behaviour for admin-specific actions
public class Admin {

    private Library lib;
    private Scanner sc;

    public Admin(Library lib, Scanner sc) {
        this.lib = lib;
        this.sc  = sc;
    }

    public void showMenu(User admin) {
        while (true) {
            System.out.println("\n===== Admin Menu [" + admin.getName() + "] =====");
            System.out.println("1. Book Management");
            System.out.println("2. User Management");
            System.out.println("3. Reports");
            System.out.println("4. Manage Borrower Fine Limit");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            String ch = sc.nextLine();
            if (ch.equals("1"))      bookMenu();
            else if (ch.equals("2")) userMenu();
            else if (ch.equals("3")) reportsMenu();
            else if (ch.equals("4")) manageFineLimit();
            else if (ch.equals("0")) break;
            else System.out.println("Invalid choice.");
        }
    }

    // ── Book Management ───────────────────────────────────────────
    void bookMenu() {
        System.out.println("\n-- Book Management --");
        System.out.println("1.Add  2.Modify  3.Delete  4.View All  5.Search");
        System.out.print("Choice: ");
        String ch = sc.nextLine();
        if (ch.equals("1"))      addBook();
        else if (ch.equals("2")) modifyBook();
        else if (ch.equals("3")) deleteBook();
        else if (ch.equals("4")) viewAllBooks();
        else if (ch.equals("5")) searchBook();
        else System.out.println("Invalid.");
    }

    void addBook() {
        System.out.print("ISBN: ");    String isbn   = sc.nextLine();
        if (lib.findBook(isbn) != null) { System.out.println("ISBN already exists."); return; }
        System.out.print("Title: ");   String title  = sc.nextLine();
        System.out.print("Author: ");  String author = sc.nextLine();
        System.out.print("Cost: ");    double cost   = Double.parseDouble(sc.nextLine());
        System.out.print("Quantity: ");int qty       = Integer.parseInt(sc.nextLine());
        lib.books.add(new Book(isbn, title, author, cost, qty));
        System.out.println("Book added.");
    }

    void modifyBook() {
        System.out.print("Enter ISBN: ");
        Book book = lib.findBook(sc.nextLine());
        if (book == null) { System.out.println("Book not found."); return; }
        book.display();
        System.out.println("1.Title  2.Author  3.Cost  4.Available Qty  5.Total Qty");
        System.out.print("Field: ");
        String ch = sc.nextLine();
        if (ch.equals("1"))      { System.out.print("New Title: ");  book.setTitle(sc.nextLine()); }
        else if (ch.equals("2")) { System.out.print("New Author: "); book.setAuthor(sc.nextLine()); }
        else if (ch.equals("3")) { System.out.print("New Cost: ");   book.setCost(Double.parseDouble(sc.nextLine())); }
        else if (ch.equals("4")) { System.out.print("New Available Qty: "); book.setAvailableQty(Integer.parseInt(sc.nextLine())); }
        else if (ch.equals("5")) { System.out.print("New Total Qty: ");     book.setTotalQty(Integer.parseInt(sc.nextLine())); }
        else { System.out.println("Invalid."); return; }
        System.out.println("Updated.");
    }

    void deleteBook() {
        System.out.print("Enter ISBN: ");
        Book book = lib.findBook(sc.nextLine());
        if (book == null) { System.out.println("Book not found."); return; }
        lib.books.remove(book);
        System.out.println("Book deleted.");
    }

    void viewAllBooks() {
        System.out.println("Sort: 1.By Name  2.By Available Qty");
        System.out.print("Choice: ");
        String ch = sc.nextLine();
        ArrayList<Book> sorted = new ArrayList<>(lib.books);
        if (ch.equals("1")) {
            // Bubble sort by title
            for (int i = 0; i < sorted.size() - 1; i++)
                for (int j = 0; j < sorted.size() - 1 - i; j++)
                    if (sorted.get(j).getTitle().compareTo(sorted.get(j+1).getTitle()) > 0) {
                        Book tmp = sorted.get(j); sorted.set(j, sorted.get(j+1)); sorted.set(j+1, tmp);
                    }
        } else {
            // Bubble sort by available qty descending
            for (int i = 0; i < sorted.size() - 1; i++)
                for (int j = 0; j < sorted.size() - 1 - i; j++)
                    if (sorted.get(j).getAvailableQty() < sorted.get(j+1).getAvailableQty()) {
                        Book tmp = sorted.get(j); sorted.set(j, sorted.get(j+1)); sorted.set(j+1, tmp);
                    }
        }
        System.out.println("\n--- All Books ---");
        for (Book b : sorted) b.display();
    }

    void searchBook() {
        System.out.print("Enter ISBN or Title: "); String q = sc.nextLine();
        Book b = lib.findBook(q);
        if (b != null) { b.display(); return; }
        boolean found = false;
        for (Book bk : lib.books)
            if (bk.getTitle().toLowerCase().contains(q.toLowerCase())) { bk.display(); found = true; }
        if (!found) System.out.println("No books found.");
    }

    // ── User Management ───────────────────────────────────────────
    void userMenu() {
        System.out.println("\n-- User Management --");
        System.out.println("1.Add Admin  2.Add Borrower  3.View All  4.Deactivate");
        System.out.print("Choice: ");
        String ch = sc.nextLine();
        if (ch.equals("1"))      addUser("ADMIN");
        else if (ch.equals("2")) addUser("BORROWER");
        else if (ch.equals("3")) { for (User u : lib.users) u.display(); }
        else if (ch.equals("4")) deactivateUser();
        else System.out.println("Invalid.");
    }

    void addUser(String role) {
        System.out.print("Name: ");     String name  = sc.nextLine();
        System.out.print("Email: ");    String email = sc.nextLine();
        if (lib.findUser(email) != null) { System.out.println("Email already exists."); return; }
        System.out.print("Password: "); String pwd   = sc.nextLine();
        lib.users.add(new User(name, email, pwd, role));
        System.out.println(role + " added.");
    }

    void deactivateUser() {
        System.out.print("Enter email: ");
        User u = lib.findUser(sc.nextLine());
        if (u == null) { System.out.println("User not found."); return; }
        u.setActive(false);
        System.out.println("User deactivated.");
    }

    void manageFineLimit() {
        System.out.print("Enter borrower email: ");
        User u = lib.findUser(sc.nextLine());
        if (u == null || !u.getRole().equals("BORROWER")) { System.out.println("Borrower not found."); return; }
        System.out.println("Current deposit: Rs." + u.getDeposit());
        System.out.print("Set new deposit amount: ");
        u.setDeposit(Double.parseDouble(sc.nextLine()));
        System.out.println("Deposit updated.");
    }

    // ── Reports ───────────────────────────────────────────────────
    void reportsMenu() {
        System.out.println("\n-- Reports --");
        System.out.println("1.Low Qty Books  2.Never Borrowed  3.Heavily Borrowed");
        System.out.println("4.Outstanding Borrows  5.Book Status by ISBN");
        System.out.print("Choice: ");
        String ch = sc.nextLine();
        if (ch.equals("1"))      reportLowQty();
        else if (ch.equals("2")) reportNeverBorrowed();
        else if (ch.equals("3")) reportHeavilyBorrowed();
        else if (ch.equals("4")) reportOutstanding();
        else if (ch.equals("5")) reportBookStatus();
        else System.out.println("Invalid.");
    }

    void reportLowQty() {
        System.out.println("\n--- Low Quantity Books (Available <= 2) ---");
        boolean any = false;
        for (Book b : lib.books) if (b.getAvailableQty() <= 2) { b.display(); any = true; }
        if (!any) System.out.println("All books have enough stock.");
    }

    void reportNeverBorrowed() {
        System.out.println("\n--- Never Borrowed Books ---");
        boolean any = false;
        for (Book b : lib.books) {
            boolean borrowed = false;
            for (Transaction t : lib.borrows)
                if (t.getBookIsbn().equals(b.getIsbn())) { borrowed = true; break; }
            if (!borrowed) { b.display(); any = true; }
        }
        if (!any) System.out.println("All books have been borrowed.");
    }

    void reportHeavilyBorrowed() {
        System.out.println("\n--- Heavily Borrowed Books ---");
        // Find max borrow count and print top books
        for (Book b : lib.books)
            if (b.getBorrowCount() > 0)
                System.out.println(b.getTitle() + " | Borrowed " + b.getBorrowCount() + " time(s)");
    }

    void reportOutstanding() {
        System.out.print("As of date (DD/MM/YYYY): "); String asOf = sc.nextLine();
        System.out.println("\n--- Outstanding Borrows as of " + asOf + " ---");
        boolean any = false;
        for (Transaction t : lib.borrows) {
            if (t.getStatus().equals("BORROWED") && lib.daysBetween(t.getDueDate(), asOf) >= 0) {
                User u = lib.findUser(t.getBorrowerEmail());
                Book b = lib.findBook(t.getBookIsbn());
                System.out.println("Borrower: " + (u != null ? u.getName() : "?") +
                        " | Book: " + (b != null ? b.getTitle() : "?") +
                        " | Due: " + t.getDueDate());
                any = true;
            }
        }
        if (!any) System.out.println("No outstanding borrows.");
    }

    void reportBookStatus() {
        System.out.print("Enter ISBN: "); String isbn = sc.nextLine();
        Book book = lib.findBook(isbn);
        if (book == null) { System.out.println("Book not found."); return; }
        book.display();
        boolean any = false;
        for (Transaction t : lib.borrows) {
            if (t.getBookIsbn().equals(book.getIsbn()) && t.getStatus().equals("BORROWED")) {
                User u = lib.findUser(t.getBorrowerEmail());
                System.out.println("Borrowed by: " + (u != null ? u.getName() : "?") +
                        " | Expected return: " + t.getDueDate());
                any = true;
            }
        }
        if (!any) System.out.println("Book is available in rack.");
    }
}
