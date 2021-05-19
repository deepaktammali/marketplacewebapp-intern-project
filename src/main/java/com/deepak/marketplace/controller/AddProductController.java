package com.deepak.marketplace.controller;

import com.deepak.marketplace.model.Item;
import com.deepak.marketplace.model.User;
import com.deepak.marketplace.repository.ItemHibernateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@RequestMapping("/addproduct")
@SessionAttributes({"user","isLoggedIn"})
public class AddProductController {
    
    private ItemHibernateRepository itemRepository;

    @Autowired
    AddProductController(ItemHibernateRepository itemRepository){
        this.itemRepository = itemRepository;
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
    public String handleGet(){
        return "addproduct";
    }

    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String addNewProduct(Item item,@ModelAttribute("isLoggedIn") boolean isLoggedIn){
        itemRepository.save(item);
        System.out.println(item);
        return "redirect:/";
    }
}
