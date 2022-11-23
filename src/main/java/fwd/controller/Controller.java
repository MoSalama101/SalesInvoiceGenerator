package fwd.controller;

import fwd.model.InvoiceHeader;
import fwd.model.InvoiceLines;
import fwd.view.CreateNewInvoice;
import fwd.view.FWD_InvoiceGenerator;
import fwd.view.HeaderTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;

public class Controller implements ActionListener {

    private FWD_InvoiceGenerator mainframe;
    private CreateNewInvoice newInvFrame;
    private JTextArea taArea;

    public Controller(FWD_InvoiceGenerator mainframe) {
        this.mainframe = mainframe;
    }

    public Controller(CreateNewInvoice newInvFrame) {
        this.newInvFrame = newInvFrame;
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
            case "Add New Item":
                addItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
            case "load file":
                loadFile();
                break;
            case "save changes":
                //saveChanges();
                break;
        }
    }

    public void createInvoice() {
    }

    public void deleteInvoice() {
    }

    public void addItem() {
    }

    public void deleteItem() {
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

    private InvoiceHeader getInvByName(int invNum) {
        InvoiceHeader hd = null;
        for (InvoiceHeader inv : mainframe.getInvHeaderArray()) {
            hd = inv;
            break;
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
                    FileReader lnReader = new FileReader(lnFile);
                    BufferedReader lnBuffReader = new BufferedReader(lnReader);
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
                        InvoiceHeader invHeader = getInvByName(number);
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
    private void displayInvoices() {
        for (InvoiceHeader header : mainframe.getInvHeaderArray()) {
            System.out.println(header);
        }
    }

}
