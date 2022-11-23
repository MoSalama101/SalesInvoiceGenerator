package fwd.view;

import javax.swing.*;
import java.awt.*;


public class CreateNewInvoiceDialog extends JDialog {
    private JTextField customerNameTxtField;
    private JTextField headerDateTxtField;

    private JLabel customerNameLabel;
    private JLabel headerDateLabel;

    private JButton okBtn;
    private JButton cancelBtn;

    public CreateNewInvoiceDialog(FWD_InvoiceGenerator frame) {

        customerNameLabel = new JLabel("Customer Name:");
        customerNameTxtField = new JTextField(20);
        headerDateLabel = new JLabel("Invoice Date:");
        headerDateTxtField = new JTextField(20);
        okBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");

        okBtn.setActionCommand("submitInvoice");
        cancelBtn.setActionCommand("cancelInvoice");

        okBtn.addActionListener(frame.getController());
        cancelBtn.addActionListener(frame.getController());
        setLayout(new GridLayout(3, 2));

        add(headerDateLabel);
        add(headerDateTxtField);
        add(customerNameLabel);
        add(customerNameTxtField);
        add(okBtn);
        add(cancelBtn);

        pack();

    }

    public JTextField getCustomerNameTxtField() {
        return customerNameTxtField;
    }

    public JTextField getHeaderDateTxtField() {
        return headerDateTxtField;
    }
}

