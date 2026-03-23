import java.util.Scanner;

// OOP Concept: Object creation and interaction between all classes
public class Main {
    public static void main(String[] args) {
        Scanner sc      = new Scanner(System.in);
        Library lib     = new Library();       // data store
        Admin admin     = new Admin(lib, sc);  // admin operations
        Borrower borrower = new Borrower(lib, sc); // borrower operations

        System.out.println("*************************************");
        System.out.println("*   LIBRARY MANAGEMENT SYSTEM      *");
        System.out.println("*************************************");
        System.out.println("  Admin   : admin@lib.com  / admin123");
        System.out.println("  Borrower: alice@mail.com / alice123");
        System.out.println("  Borrower: bob@mail.com   / bob123");

        while (true) {
            System.out.println("\n1. Login   0. Exit");
            System.out.print("Choice: ");
            String ch = sc.nextLine();

            if (ch.equals("0")) {
                System.out.println("Thank you. Goodbye!");
                break;
            }

            if (!ch.equals("1")) {
                System.out.println("Invalid choice.");
                continue;
            }

            System.out.print("Email: ");    String email = sc.nextLine();
            System.out.print("Password: "); String pwd   = sc.nextLine();

            User user = lib.findUser(email);

            if (user == null || !user.getPassword().equals(pwd)) {
                System.out.println("Invalid email or password.");
                continue;
            }

            if (!user.isActive()) {
                System.out.println("Account is deactivated. Contact admin.");
                continue;
            }

            System.out.println("Welcome, " + user.getName() + "! [" + user.getRole() + "]");

            if (user.getRole().equals("ADMIN")) {
                admin.showMenu(user);
            } else {
                borrower.showMenu(user);
            }
        }

        sc.close();
    }
}
