package com.deepak.marketplace.controller;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Vector;

import com.deepak.marketplace.model.Address;
import com.deepak.marketplace.model.Cart;
import com.deepak.marketplace.model.CartItem;
import com.deepak.marketplace.service.InvoiceService;
import com.deepak.marketplace.util.AddressUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"cart","isLoggedIn"})
public class CheckoutController {

    private SessionFactory sessionFactory;

    @Autowired
    CheckoutController(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @PostMapping(value="/getcompleteinvoice",
    produces = {MediaType.APPLICATION_PDF_VALUE},
    consumes={MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> getInvoice(@ModelAttribute("cart") Cart cart,@RequestParam HashMap<String,String> formData){
        
        
        Long invoiceId = InvoiceService.generateInvoiceId();
        try{
            Vector<CartItem> cartItems = (Vector<CartItem>)cart.getCartItems() ;
            InvoiceService.generateInvoice(cartItems, invoiceId,formData);

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
            return response;
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
