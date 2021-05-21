package com.deepak.marketplace.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.Map.Entry;

import com.itextpdf.html2pdf.HtmlConverter;

import org.springframework.stereotype.Service;

import com.deepak.marketplace.util.AddressUtil;
import com.deepak.marketplace.util.InvoiceHTMLGenerator;
import com.deepak.marketplace.model.Address;
import com.deepak.marketplace.model.CartItem;

@Service
public class InvoiceService {
    private static double taxRate = 0.05;
    private static double shipping = 50.00;

    public static Long generateInvoiceId(){
        Random rd = new Random();
        return Math.abs(rd.nextLong());
    }

    public static Map<String,Double> calculateTotals(Vector<CartItem> cartItems){
        Map<String,Double> totalsMap = new HashMap<>();

        double subTotal = 0.0;
        for (CartItem cartItem:cartItems){
            subTotal += cartItem.getPrice()*cartItem.getQuantity();
        }
        double tax = subTotal*taxRate;
        double grandTotal = subTotal + tax + shipping;

        totalsMap.put("subTotal", subTotal);
        totalsMap.put("tax", tax);
        totalsMap.put("shipping",shipping);
        totalsMap.put("grandTotal",grandTotal);
        return totalsMap;
    }


    public static void generateInvoice(Vector<CartItem> cartItems,Long invoiceId, HashMap<String,String> formData) throws IOException{
        

        // {billingAddress,shippingAddress} importing address
        Address[] addressList = AddressUtil.parseShippingAndBillingAddress(formData);
        Address billingAddress = addressList[0];
        Address shippingAddress = addressList[1];


        Map<String,Double> totalsMap = calculateTotals(cartItems);
        
        String cssPath = Paths.get("src/main/resources/static/invoice.css").toAbsolutePath().toString();
        String invoiceHTML = InvoiceHTMLGenerator.generateInvoiceHTML(cartItems,cssPath,totalsMap,billingAddress,shippingAddress);
        String invoicePDFPath = String.format("src/main/resources/invoices/invoice_%d.pdf", invoiceId);
        Path newInvoicePDFPath = Paths.get(invoicePDFPath);
        Files.createFile(newInvoicePDFPath);
        HtmlConverter.convertToPdf(invoiceHTML, new FileOutputStream(invoicePDFPath));
    }

    public static void sendMessage(){

    }

    public static void sendEmail(){

    }
}
