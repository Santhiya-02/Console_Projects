
public class Transaction {
    
    private String borrowerEmail;
    private String bookIsbn;
      private String borrowDate;
     private String dueDate;
    private String returnDate;
     private String status;   
    private int extensionCount;
    private double fineAmount;

    private String fineType;   
     private String fineReason;
    private boolean finePaid;
    private String paymentMode;

     public Transaction(String borrowerEmail, String bookIsbn, String borrowDate, String dueDate) {
        this.borrowerEmail = borrowerEmail;
        this.bookIsbn = bookIsbn;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = "BORROWED";
        this.extensionCount = 0;
        this.fineAmount = 0;
        this.finePaid = false;
    }

    public String getBorrowerEmail()  { return borrowerEmail; }
    public String getBookIsbn()       { return bookIsbn; }
    public String getBorrowDate()     { return borrowDate; }
    public String getDueDate()        { return dueDate; }
    public String getReturnDate()     { return returnDate; }
    public String getStatus()         { return status; }
    public int getExtensionCount()    { return extensionCount; }
    public double getFineAmount()     { return fineAmount; }
    public String getFineType()       { return fineType; }
    public String getFineReason()     { return fineReason; }
    public boolean isFinePaid()       { return finePaid; }
    public String getPaymentMode()    { return paymentMode; }

    public void setDueDate(String d)      { this.dueDate = d; }
    public void setReturnDate(String d)   { this.returnDate = d; }
    public void setStatus(String s)       { this.status = s; }
    public void setExtensionCount(int c)  { this.extensionCount = c; }
    public void setFineAmount(double f)   { this.fineAmount = f; }
    public void setFineType(String t)     { this.fineType = t; }
    public void setFineReason(String r)   { this.fineReason = r; }
    public void setFinePaid(boolean p)    { this.finePaid = p; }
    public void setPaymentMode(String m)  { this.paymentMode = m; }

    public void displayBorrow() {
        System.out.println("Book: " + bookIsbn + " | Borrowed: " + borrowDate +
                " | Due: " + dueDate + " | Status: " + status +
                " | Extensions: " + extensionCount + "/2");
    }

    public void displayFine() {
        System.out.println("Type: " + fineType + " | Amount: Rs." + fineAmount +
                " | Reason: " + fineReason + " | Paid: " + (finePaid ? paymentMode : "NO"));
    }
}
