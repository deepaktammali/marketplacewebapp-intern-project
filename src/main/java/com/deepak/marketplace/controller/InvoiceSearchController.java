package com.deepak.marketplace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/invoicesearch")
public class InvoiceSearchController {

        @GetMapping
        public String getInvoiceSearchPage(Model model){
            model.addAttribute("pageTitle", "Search For Invoices");
            return "invoicesearch";
        }

        @PostMapping
        public void postSearchInvoice(){

        }

}
