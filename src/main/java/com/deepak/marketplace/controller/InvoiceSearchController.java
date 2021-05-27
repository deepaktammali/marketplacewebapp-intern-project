package com.deepak.marketplace.controller;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.websocket.server.PathParam;

import com.deepak.marketplace.model.Order;
import com.deepak.marketplace.model.User;
import com.deepak.marketplace.repository.OrderHibernateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/invoicesearch")
@SessionAttributes({"cart","isLoggedIn","isAdmin"})
public class InvoiceSearchController {

    private OrderHibernateRepository orderRepository;

    @Autowired
    InvoiceSearchController(OrderHibernateRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @ModelAttribute("user")
    public User user(){
        return null;
    }

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(){
        return false;
    }


    @GetMapping
    public String getInvoiceSearchPage(Model model) {
        model.addAttribute("pageTitle", "Search For Invoice");
        return "invoicesearch";
    }

    @GetMapping("/{invoiceId}")
    @ResponseBody
    public ResponseEntity<byte[]> getInvoice(Model model, @PathVariable("invoiceId") Long invoiceId) {
        
        
        
        String invoicePDFPathString = String.format("src/main/resources/invoices/invoice_%d.pdf", invoiceId);
        Path invoicePDFPath = Paths.get(invoicePDFPathString);

        try(FileInputStream invoiceFileStream = new FileInputStream(invoicePDFPath.toAbsolutePath().toString());){
            ResponseEntity<byte[]> response = ResponseEntity
                                                    .ok()
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

    

    @PostMapping(consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public String postSearchInvoice(Model model,@RequestParam("invoiceid") Long invoiceId) {
        orderRepository.findById(invoiceId).ifPresentOrElse(order -> {
            model.addAttribute("showSearchResults", true);
            model.addAttribute("order", order);
        }, new Runnable() {
            @Override
            public void run() {
                String errorMessage = "Cannot find an invoice with details specified.";
                model.addAttribute("hasErrors", true);
                model.addAttribute("errorMessage", errorMessage);
            }
        });

        return "invoicesearch";
    }

    @ModelAttribute("showSearchResults")
    public boolean showSearchResults(){
        return false;
    }


    @ModelAttribute("hasErrors")
    public boolean hasErrors(){
        return false;
    }

    @ModelAttribute("errorMessage")
    public String errorMessage(){
        return null;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(){
        return false;
    }
}
