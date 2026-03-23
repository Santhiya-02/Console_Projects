import java.util.ArrayList;

// OOP Concept: Single Responsibility - Library manages all data and core logic
public class Library {

    ArrayList<User> users           = new ArrayList<>();
    ArrayList<Book> books           = new ArrayList<>();
    ArrayList<Transaction> borrows  = new ArrayList<>();
    ArrayList<Transaction> fines    = new ArrayList<>();

    // OOP Concept: Constructor - initializes with sample data
    public Library() {
        // Default users
        users.add(new User("Admin",  "admin@lib.com",  "admin123", "ADMIN"));
        users.add(new User("Alice",  "alice@mail.com", "alice123", "BORROWER"));
        users.add(new User("Bob",    "bob@mail.com",   "bob123",   "BORROWER"));

        // Default books
        books.add(new Book("B001", "Clean Code",              "Robert Martin", 450, 5));
        books.add(new Book("B002", "The Pragmatic Programmer", "Andrew Hunt",  500, 3));
        books.add(new Book("B003", "Design Patterns",          "Gang of Four", 600, 4));
        books.add(new Book("B004", "Refactoring",              "Martin Fowler",400, 2));
        books.add(new Book("B005", "Intro to Algorithms",      "CLRS",         800, 6));
    }

    // ── Find helpers ──────────────────────────────────────────────
    User findUser(String email) {
        for (User u : users)
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        return null;
    }

    Book findBook(String isbnOrTitle) {
        for (Book b : books)
            if (b.getIsbn().equalsIgnoreCase(isbnOrTitle) ||
                b.getTitle().equalsIgnoreCase(isbnOrTitle)) return b;
        return null;
    }

    Transaction findActiveBorrow(String email, String isbnOrTitle) {
        Book book = findBook(isbnOrTitle);
        if (book == null) return null;
        for (Transaction t : borrows)
            if (t.getBorrowerEmail().equalsIgnoreCase(email) &&
                t.getBookIsbn().equalsIgnoreCase(book.getIsbn()) &&
                t.getStatus().equals("BORROWED")) return t;
        return null;
    }

    int countActiveBorrows(String email) {
        int count = 0;
        for (Transaction t : borrows)
            if (t.getBorrowerEmail().equalsIgnoreCase(email) && t.getStatus().equals("BORROWED"))
                count++;
        return count;
    }

    // ── Borrow ────────────────────────────────────────────────────
    String borrowBook(String email, String isbnOrTitle, String today, String dueDate) {
        User user = findUser(email);
        if (user.getDeposit() < 500) return "Need minimum Rs.500 deposit to borrow.";
        Book book = findBook(isbnOrTitle);
        if (book == null)              return "Book not found.";
        if (book.getAvailableQty() <= 0) return "Book not available.";
        if (countActiveBorrows(email) >= 3) return "Cannot borrow more than 3 books.";
        if (findActiveBorrow(email, isbnOrTitle) != null) return "You already borrowed this book.";

        Transaction t = new Transaction(email, book.getIsbn(), today, dueDate);
        borrows.add(t);
        book.setAvailableQty(book.getAvailableQty() - 1);
        book.addBorrowCount();
        return "Borrowed: " + book.getTitle() + " | Due: " + dueDate;
    }

    // ── Return ────────────────────────────────────────────────────
    String returnBook(String email, String isbnOrTitle, String returnDate) {
        Transaction t = findActiveBorrow(email, isbnOrTitle);
        if (t == null) return "No active borrow found.";
        Book book = findBook(isbnOrTitle);

        double fine = calcLateFine(t.getDueDate(), returnDate, book.getCost());
        t.setReturnDate(returnDate);
        t.setStatus("RETURNED");
        book.setAvailableQty(book.getAvailableQty() + 1);

        if (fine > 0) {
            Transaction f = new Transaction(email, book.getIsbn(), returnDate, returnDate);
            f.setFineType("LATE");
            f.setFineAmount(fine);
            f.setFineReason("Late return");
            f.setStatus("FINE");
            fines.add(f);
            t.setFineAmount(fine);
            return "Returned. Late fine: Rs." + fine + ". Please pay.";
        }
        return "Book returned. No fine.";
    }

    // ── Extend ────────────────────────────────────────────────────
    String extendBorrow(String email, String isbnOrTitle, String newDueDate) {
        Transaction t = findActiveBorrow(email, isbnOrTitle);
        if (t == null)                    return "No active borrow found.";
        if (t.getExtensionCount() >= 2)   return "Max 2 extensions already used.";
        t.setDueDate(newDueDate);
        t.setExtensionCount(t.getExtensionCount() + 1);
        return "Extended. New due date: " + newDueDate + " (Extension " + t.getExtensionCount() + "/2)";
    }

    // ── Mark Lost ─────────────────────────────────────────────────
    String markLost(String email, String isbnOrTitle) {
        Transaction t = findActiveBorrow(email, isbnOrTitle);
        if (t == null) return "No active borrow found.";
        Book book = findBook(isbnOrTitle);
        double fine = book.getCost() * 0.5;
        t.setStatus("LOST");
        t.setFineAmount(fine);

        Transaction f = new Transaction(email, book.getIsbn(), "", "");
        f.setFineType("LOST_BOOK");
        f.setFineAmount(fine);
        f.setFineReason("Book lost: " + book.getTitle());
        f.setStatus("FINE");
        fines.add(f);
        return "Marked lost. Fine: Rs." + fine + ". Please pay.";
    }

    // ── Card Lost ─────────────────────────────────────────────────
    String cardLost(String email) {
        Transaction f = new Transaction(email, "-", "", "");
        f.setFineType("LOST_CARD");
        f.setFineAmount(10);
        f.setFineReason("Membership card lost");
        f.setStatus("FINE");
        fines.add(f);
        return "Card loss noted. Fine: Rs.10. Please pay.";
    }

    // ── Pay Fine ──────────────────────────────────────────────────
    String payFine(String email, int index, String mode) {
        ArrayList<Transaction> myFines = getUnpaidFines(email);
        if (index < 1 || index > myFines.size()) return "Invalid selection.";
        Transaction f = myFines.get(index - 1);
        User user = findUser(email);
        if (mode.equals("2")) {
            if (user.getDeposit() < f.getFineAmount()) return "Insufficient deposit. Pay by cash.";
            user.setDeposit(user.getDeposit() - f.getFineAmount());
            f.setPaymentMode("DEPOSIT");
        } else {
            f.setPaymentMode("CASH");
        }
        f.setFinePaid(true);
        return "Fine of Rs." + f.getFineAmount() + " paid via " + f.getPaymentMode() + ".";
    }

    ArrayList<Transaction> getUnpaidFines(String email) {
        ArrayList<Transaction> list = new ArrayList<>();
        for (Transaction f : fines)
            if (f.getBorrowerEmail().equalsIgnoreCase(email) && !f.isFinePaid())
                list.add(f);
        return list;
    }

    // ── Fine Calculation ──────────────────────────────────────────
    // 2 Rs/day, doubles every 10 days, capped at 80% of book cost
    double calcLateFine(String dueDateStr, String returnDateStr, double bookCost) {
        int daysLate = daysBetween(dueDateStr, returnDateStr);
        if (daysLate <= 0) return 0;
        double fine = 0;
        int remaining = daysLate, period = 0;
        while (remaining > 0) {
            int days = Math.min(remaining, 10);
            fine += days * 2.0 * Math.pow(2, period);
            remaining -= days;
            period++;
        }
        return Math.min(fine, bookCost * 0.8);
    }

    // Simple date difference: DD/MM/YYYY
    int daysBetween(String d1, String d2) {
        try {
            String[] p1 = d1.split("/"), p2 = d2.split("/");
            int day1 = Integer.parseInt(p1[0]), mon1 = Integer.parseInt(p1[1]), yr1 = Integer.parseInt(p1[2]);
            int day2 = Integer.parseInt(p2[0]), mon2 = Integer.parseInt(p2[1]), yr2 = Integer.parseInt(p2[2]);
            long days1 = yr1 * 365L + mon1 * 30 + day1;
            long days2 = yr2 * 365L + mon2 * 30 + day2;
            return (int)(days2 - days1);
        } catch (Exception e) { return 0; }
    }

    String addDays(String dateStr, int days) {
        try {
            String[] p = dateStr.split("/");
            int day = Integer.parseInt(p[0]), mon = Integer.parseInt(p[1]), yr = Integer.parseInt(p[2]);
            day += days;
            int[] daysInMonth = {0,31,28,31,30,31,30,31,31,30,31,30,31};
            while (day > daysInMonth[mon]) { day -= daysInMonth[mon]; mon++; if (mon > 12) { mon = 1; yr++; } }
            return String.format("%02d/%02d/%04d", day, mon, yr);
        } catch (Exception e) { return dateStr; }
    }
}
