package fwd.model;

public class InvoiceLines {
   
    private InvoiceHeader inv;
    private String itemName;
    private double itemPrice;
    private int count;

    public InvoiceLines(InvoiceHeader inv, String itemName, double itemPrice, int count) {
        this.inv = inv;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public InvoiceHeader getInv() {
        return inv;
    }

    public void setInv(InvoiceHeader inv) {
        this.inv = inv;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return "InvoiceLines{" + "itemName=" + itemName + ", itemPrice=" + itemPrice + ", count=" + count + '}';
    }
    
   public double itemSubTotal(){
        return itemPrice * count;
    }
}
