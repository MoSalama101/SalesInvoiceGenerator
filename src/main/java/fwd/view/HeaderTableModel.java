package fwd.view;

import fwd.model.InvoiceHeader;
import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
/**
 *
 * @author m7md_
 */

public class HeaderTableModel extends AbstractTableModel {

    private List<InvoiceHeader> headerArray;
    private DateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");

    public HeaderTableModel(List<InvoiceHeader> headerArray) {
        this.headerArray = headerArray;
    }

    // Handle rows
    @Override
    public int getRowCount() {
        return headerArray.size();
    }

    public List<InvoiceHeader> getHeaderArray() {
        return headerArray;
    }

    // Handle Columns
    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int index) {

        switch (index) {
            case 0:
                return "invoice Num";
            case 1:
                return "Customer Name";
            case 2:
                return "Invoice Date";
            case 3:
                return "Invoice Total";
            default:
                return "";
        }
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader row = headerArray.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return row.getInvoiceNum();
            case 1:
                return row.getCustomerName();
            case 2:
                return dataFormat.format(row.getInvoiceDate());
            case 3:
                return row.invoiceTotal();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int index) {
        switch (index) {
            case 0:
                return Integer.class;
            case 1:
            case 2:
                return String.class;
            case 3:
                return Double.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }


}
