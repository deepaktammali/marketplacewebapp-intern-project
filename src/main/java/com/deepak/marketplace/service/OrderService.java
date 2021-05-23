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
import java.util.stream.Collectors;

import com.itextpdf.html2pdf.HtmlConverter;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepak.marketplace.util.AddressUtil;
import com.deepak.marketplace.util.InvoiceEmail;
import com.deepak.marketplace.util.InvoiceHTMLGenerator;
import com.deepak.marketplace.util.InvoiceSMS;
import com.deepak.marketplace.model.Address;
import com.deepak.marketplace.model.CartItem;
import com.deepak.marketplace.model.Order;
import com.deepak.marketplace.model.OrderItem;

@Service
public class OrderService {
    private static double taxRate = 0.05;
    private static double shipping = 50.00;
    private SessionFactory sessionFactory;


    @Autowired
    OrderService(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }



    public long generateInvoiceId(){
        Random rd = new Random();
        return Math.abs(rd.nextInt(2147483647));
    }

    public Map<String,Double> calculateTotals(Vector<CartItem> cartItems){
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

    public void persistOrderToDB(Vector<CartItem> cartItems,Long invoiceId, Address billingAddress,Address shippingAddress,boolean sameShipAndBillAddr){

        System.out.println("OrderId "+invoiceId);

        // get session and start transaction
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();

        Order newOrder = new Order();
        List<OrderItem> orderItems = cartItems
                .stream()
                .map(cartItem->{
                    return new OrderItem(newOrder,cartItem.getItem(),cartItem.getQuantity());
                })
                .collect(Collectors.toList());
        newOrder.setOrderItems(orderItems);
        newOrder.setId(invoiceId);

        System.out.println("SAVED ID : "+newOrder.getId());

        if(sameShipAndBillAddr){
            session.save(billingAddress);
            newOrder.setBillingAddress(billingAddress);
            newOrder.setShippingAddress(billingAddress);
        }
        else{
            session.save(billingAddress);
            session.save(shippingAddress);
            newOrder.setBillingAddress(billingAddress);
            newOrder.setShippingAddress(shippingAddress);
        }

        session.save(newOrder);

        // end session
        transaction.commit();
    }


    public void generateInvoice(Vector<CartItem> cartItems,Long invoiceId, Address billingAddress,Address shippingAddress) throws IOException{
        Map<String,Double> totalsMap = calculateTotals(cartItems);
        
        String cssPath = Paths.get("src/main/resources/static/invoice.css").toAbsolutePath().toString();
        String invoiceHTML = InvoiceHTMLGenerator.generateInvoiceHTML(cartItems,invoiceId,cssPath,totalsMap,billingAddress,shippingAddress);
        String invoicePDFPath = String.format("src/main/resources/invoices/invoice_%d.pdf", invoiceId);
        Path newInvoicePDFPath = Paths.get(invoicePDFPath);
        Files.createFile(newInvoicePDFPath);
        HtmlConverter.convertToPdf(invoiceHTML, new FileOutputStream(invoicePDFPath));
    }

    public void sendMessage(Address billingAddress){
        InvoiceSMS.sendMessage();
    }


    public void processOrder(Vector<CartItem> cartItems,Long invoiceId, HashMap<String,String> formData) throws IOException{

        // {billingAddress,shippingAddress} importing address
        Address[] addressList = AddressUtil.parseShippingAndBillingAddress(formData);
        Address billingAddress = addressList[0];
        Address shippingAddress = addressList[1];

        boolean sameShipAndBillAddr = formData.containsKey("isBillingAndShippingAddrSame");
        persistOrderToDB(cartItems, invoiceId, billingAddress, shippingAddress, sameShipAndBillAddr);
        generateInvoice(cartItems, invoiceId, billingAddress, shippingAddress);

        // email and message
        InvoiceEmail.sendEmail(invoiceId,billingAddress);
    }
}
