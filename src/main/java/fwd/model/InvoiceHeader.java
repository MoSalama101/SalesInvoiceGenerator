package fwd.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader {
  
    private int invoiceNum;
    private Date invoiceDate;
    private String customerName;
    private ArrayList<InvoiceLines> items; 

    public InvoiceHeader(int invoiceNum, Date invoiceDate, String customerName) {
        this.invoiceNum = invoiceNum;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public ArrayList<InvoiceLines> getLineItems() {
        if(items == null){
        items = new ArrayList();
        }
        return items;
    }

    @Override
    public String toString() {
        String headerInfo = "InvoiceHeader{" + "invoiceNum=" + invoiceNum + ", invoiceDate=" + invoiceDate + ", customerName=" + customerName + ", items=" + items + '}';
        for(InvoiceLines lineItems: getLineItems()){
            headerInfo += "\n\t" + lineItems;
        }
        return headerInfo;
    }

    public double invoiceTotal(){
        double sum = 0.0;
        for (InvoiceLines item : getLineItems()) {
             sum += item.itemSubTotal();
        }
        return sum;
    }

    public String getDataAsCSV() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return "" + getInvoiceNum() + "," + dateFormat.format(getInvoiceDate()) + "," + getCustomerName();

    }

    public void addInvLine(InvoiceLines itemLine) {
        getLineItems().add(itemLine);

    }
}
