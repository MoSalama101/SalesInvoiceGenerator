package fwd.view;

import javax.swing.*;
import java.awt.*;

public class CreateItemDialog extends JDialog {
    private JTextField itemNameTxtField;
    private JTextField itemCountTxtField;
    private JTextField itemPriceTxtField;

    private JLabel itemNameLabel;
    private JLabel itemCountLabel;
    private JLabel itemPriceLabel;

    private JButton okButton;
    private JButton cancelButton;

    public CreateItemDialog(FWD_InvoiceGenerator frame) {
        itemNameTxtField = new JTextField(20);
        itemNameLabel = new JLabel("Item Name");

        itemCountTxtField = new JTextField(20);
        itemCountLabel = new JLabel("Item Count");

        itemPriceTxtField = new JTextField(20);
        itemPriceLabel = new JLabel("Item Price");

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        okButton.setActionCommand("submit Item");
        cancelButton.setActionCommand("cancel Item");

        okButton.addActionListener(frame.getController());
        cancelButton.addActionListener(frame.getController());
        setLayout(new GridLayout(4, 2));

        add(itemNameLabel);
        add(itemNameTxtField);
        add(itemCountLabel);
        add(itemCountTxtField);
        add(itemPriceLabel);
        add(itemPriceTxtField);
        add(okButton);
        add(cancelButton);

        pack();
    }

    public JTextField getItemNameTxtField() {
        return itemNameTxtField;
    }

    public JTextField getItemCountTxtField() {
        return itemCountTxtField;
    }

    public JTextField getItemPriceTxtField() {
        return itemPriceTxtField;
    }

}

