package fwd.controller;

import fwd.model.InvoiceHeader;
import fwd.model.InvoiceLines;
import fwd.model.LineTableModel;
import fwd.view.FWD_InvoiceGenerator;
import fwd.model.HeaderTableModel;
import fwd.view.CreateNewInvoiceDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Controller implements ActionListener, ListSelectionListener {

    private FWD_InvoiceGenerator mainframe;
    private JTextArea taArea;

    private DateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");


    public Controller(FWD_InvoiceGenerator mainframe) {
        this.mainframe = mainframe;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "Create New Invoice":
                createInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "cancelInvoice":
                cancelInvoice();
                break;
            case "submitInvoice":
                submitInvoice();
                break;
            case "load file":
                loadFile();
                break;
            case "save changes":
                saveChanges();
                break;
        }
    }


    private void createInvoice() {
        mainframe.setHeaderDialog(new CreateNewInvoiceDialog(mainframe));
        mainframe.getHeaderDialog().setVisible(true);
    }


    private void deleteInvoice() {
        int invIndex = mainframe.getHeaderTbl().getSelectedRow();
        InvoiceHeader header = mainframe.getInvHeaderTableModel().getHeaderArray().get(invIndex);
        mainframe.getInvHeaderTableModel().getHeaderArray().remove(invIndex);
        mainframe.getInvHeaderTableModel().fireTableDataChanged();
        mainframe.setInvLineTableModel(new LineTableModel(new ArrayList<InvoiceLines>()));
        mainframe.getItemsTbl().setModel(mainframe.getInvLineTableModel());
        mainframe.getInvLineTableModel().fireTableDataChanged();
        mainframe.getcustLbl().setText("");
        mainframe.getInvDateTF().setText("");
        mainframe.getNumLbl().setText("");
        mainframe.getTotalLbl().setText("");
        displayInvoices();
        JOptionPane.showMessageDialog(null, "Invoice Deleted ");

    }

    private void cancelInvoice() {
        mainframe.getHeaderDialog().setVisible(false);
        mainframe.getHeaderDialog().dispose();
        mainframe.setHeaderDialog(null);
    }

    private void submitInvoice() {
        String customerName = mainframe.getHeaderDialog().getCustomerNameTxtField().getText();
        String invDateString = mainframe.getHeaderDialog().getHeaderDateTxtField().getText();
        mainframe.getHeaderDialog().setVisible(false);
        mainframe.getHeaderDialog().dispose();
        mainframe.setHeaderDialog(null);
        try {
            Date invDate = dataFormat.parse(invDateString);
            int invNumber = getInvoiceNumber();
            InvoiceHeader headerLine = new InvoiceHeader(invNumber, invDate, customerName);

            mainframe.getInvHeaderArray().add(headerLine);

            mainframe.getInvHeaderTableModel().fireTableDataChanged();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(mainframe, "Invalid date Format", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            displayInvoices();
        }
    }

    private int getInvoiceNumber() {
        long max = 0;
        for (InvoiceHeader header : mainframe.getInvHeaderArray()) {
            if (header.getInvoiceNum() > max) {
                max = header.getInvoiceNum();
            }
        }
        return (int) (max + 1);
    }
    private InvoiceHeader getInvByNum(int invNum) {
        InvoiceHeader hd = null;
        for (InvoiceHeader inv : mainframe.getInvHeaderArray()) {
            if (invNum == inv.getInvoiceNum()) {
                hd = inv;
                break;
            }
        }
        return hd;
    }

    public void loadFile() {
        JFileChooser fc = new JFileChooser();
        int result = fc.showOpenDialog(mainframe);
        if (result == JFileChooser.APPROVE_OPTION) {
            File hdFile = fc.getSelectedFile();
            try {
                FileReader hdReader = new FileReader(hdFile);
                BufferedReader hdBuffReader = new BufferedReader(hdReader);
                String hdLine = null;

                while ((hdLine = hdBuffReader.readLine()) != null) {
                    String[] hdInv = hdLine.split(",");
                    String num = hdInv[0];
                    String date = hdInv[1];
                    String custName = hdInv[2];
                    // ---------- parsing step ------------- 
                    int invoiceNum = Integer.parseInt(num);
                    Date invoiceDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);

                    InvoiceHeader invHeader = new InvoiceHeader(invoiceNum, invoiceDate, custName);
                    mainframe.getInvHeaderArray().add(invHeader);
                }

                result = fc.showOpenDialog(mainframe);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lnFile = fc.getSelectedFile();
                    BufferedReader lnBuffReader = new BufferedReader(new FileReader(lnFile));
                    String itemLine = null;
                    while ((itemLine = lnBuffReader.readLine()) != null) {
                        String[] lnParts = itemLine.split(",");
                        String invNo = lnParts[0];
                        String itemName = lnParts[1];
                        String itemPrice = lnParts[2];
                        String itemCount = lnParts[3];
                        // ---------- parsing step ------------- 
                        int number = Integer.parseInt(invNo);
                        double price = Double.parseDouble(itemPrice);
                        int count = Integer.parseInt(itemCount);
                        // --------- linking between invoice and item tables------
                        InvoiceHeader invHeader = getInvByNum(number);
                        InvoiceLines invLine = new InvoiceLines(invHeader, itemName, price, count);
                        invHeader.getLineItems().add(invLine);
                    }
                    mainframe.setInvHeaderTableModel(new HeaderTableModel(mainframe.getInvHeaderArray()));
                    mainframe.getHeaderTbl().setModel(mainframe.getInvHeaderTableModel());
                    mainframe.getHeaderTbl().validate();
                }
            } catch (FileNotFoundException e) {
                System.out.println("File is not exist");
            } catch (IOException e) {
                System.out.println("File has problem while reading");
            } catch (ParseException e) {
                System.out.println("there is an error in data format");
            }
        }
        displayInvoices();
    }

    public void saveChanges() {
        JFileChooser fc = new JFileChooser();
        int result = fc.showSaveDialog(taArea);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getPath();
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(path);
                byte[] b = taArea.getText().getBytes();
                output.write(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public void valueChanged(ListSelectionEvent e) {
        invoicesTableRowSelected();
        System.out.println("Invoice is Selected");

    }

    private void invoicesTableRowSelected() {
        int selectedRowIndex = mainframe.getHeaderTbl().getSelectedRow();
        if (selectedRowIndex >= 0) {
            InvoiceHeader row = mainframe.getInvHeaderTableModel().getHeaderArray().get(selectedRowIndex);
            mainframe.getcustLbl().setText(row.getCustomerName());
            mainframe.getInvDateTF().setText(dataFormat.format(row.getInvoiceDate()));
            mainframe.getNumLbl().setText("" + row.getInvoiceNum());
            mainframe.getTotalLbl().setText("" + row.invoiceTotal());
            ArrayList<InvoiceLines> lines = row.getLineItems();
            mainframe.setInvLineTableModel(new LineTableModel(lines));
            mainframe.getItemsTbl().setModel(mainframe.getInvLineTableModel());
            mainframe.getInvLineTableModel().fireTableDataChanged();

        }
    }
    private void displayInvoices() {
        for (InvoiceHeader header : mainframe.getInvHeaderArray()) {
            System.out.println(header);
        }
    }

}
