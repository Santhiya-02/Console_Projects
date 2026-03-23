
public class User {
    private String name;
    private String email;
    private String password;
    private String role; 
    private double deposit;
    private boolean active;

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.deposit = role.equals("BORROWER") ? 1500.0 : 0.0;
        this.active = true;
    }

    public String getName()     { return name; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public String getRole()     { return role; }
    public double getDeposit()  { return deposit; }
    public boolean isActive()   { return active; }

    public void setDeposit(double d) { this.deposit = d; }
    public void setActive(boolean a) { this.active = a; }

    public void display() {
        System.out.println("Name: " + name + " | Email: " + email + " | Role: " + role + " | Deposit: Rs." + deposit);
    }
}
