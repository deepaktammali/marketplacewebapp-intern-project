package com.deepak.marketplace.controller;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Vector;

import com.deepak.marketplace.model.Cart;
import com.deepak.marketplace.model.CartItem;
import com.deepak.marketplace.model.User;
import com.deepak.marketplace.service.OrderService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({"cart","isLoggedIn"})
public class CheckoutController {

    private SessionFactory sessionFactory;
    private OrderService orderService;

    @Autowired
    CheckoutController(SessionFactory sessionFactory,OrderService orderService){
        this.sessionFactory = sessionFactory;
        this.orderService = orderService;
    }

    @ModelAttribute("user")
    public User user(){
        return null;
    }

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(){
        return false;
    }

    @PostMapping(value="/getcompleteinvoice",
    produces = {MediaType.APPLICATION_PDF_VALUE},
    consumes={MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> getInvoice(@ModelAttribute("cart") Cart cart,@RequestParam HashMap<String,String> formData,SessionStatus sessionStatus){
        
        
        Long invoiceId = orderService.generateInvoiceId();
        System.out.println("Invoice Id "+invoiceId);
        try{
            Vector<CartItem> cartItems = (Vector<CartItem>)cart.getCartItems() ;
            orderService.processOrder(cartItems, invoiceId,formData);

            Session session  = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            
            session.getTransaction().commit();

            String invoicePDFPathString = String.format("src/main/resources/invoices/invoice_%d.pdf", invoiceId);
            Path invoicePDFPath = Paths.get(invoicePDFPathString);

            FileInputStream invoiceFileStream = new FileInputStream(invoicePDFPath.toAbsolutePath().toString());
            
            var headers = new HttpHeaders();
            ResponseEntity<byte[]> response = ResponseEntity
                                                    .ok()
                                                    .headers(headers)
                                                    .contentType(MediaType.APPLICATION_PDF)
                                                    .body(invoiceFileStream.readAllBytes());
            invoiceFileStream.close();
            sessionStatus.setComplete();
            return response;
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
