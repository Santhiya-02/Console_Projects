
public class Book {
    private String isbn;
    private String title;
     private String author;
     private double cost;
      private int totalQty;
      private int availableQty;
    private int borrowCount;

    public Book(String isbn, String title, String author, double cost, int qty) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.cost = cost;
        this.totalQty = qty;
        this.availableQty = qty;
        this.borrowCount = 0;
    }

     public String getIsbn()        { return isbn; }
    public String getTitle()       { return title; }
      public String getAuthor()      { return author; }
      public double getCost()        { return cost; }
     public int getTotalQty()       { return totalQty; }
      public int getAvailableQty()   { return availableQty; }
    public int getBorrowCount()    { return borrowCount; }

    public void setTitle(String t)      { this.title = t; }
     public void setAuthor(String a)     { this.author = a; }
      public void setCost(double c)       { this.cost = c; }
      public void setTotalQty(int q)      { this.totalQty = q; }
    public void setAvailableQty(int q)  { this.availableQty = q; }
    public void addBorrowCount()        { this.borrowCount++; }

    public void display() {
        System.out.println("[" + isbn + "] " + title + " by " + author +
                " | Available: " + availableQty + "/" + totalQty + " | Cost: Rs." + cost);
    }
}
