package com.deepak.marketplace.controller;

import java.util.List;
import java.util.Vector;

import com.deepak.marketplace.model.Item;
import com.deepak.marketplace.model.User;
import com.deepak.marketplace.repository.ItemHibernateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@RequestMapping("/")
@SessionAttributes({"user","isLoggedIn"})
public class ItemsController {
    
    private ItemHibernateRepository itemRepository;

    @Autowired
    ItemsController(ItemHibernateRepository itemRepository){
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

    @GetMapping("/")
    public String getHomePage(Model model,@ModelAttribute("isLoggedIn") boolean isLoggedIn){
        List<Item> allItems = new Vector<Item>();
        itemRepository.findAll().forEach(allItems::add);
        model.addAttribute("products",allItems);
        model.addAttribute("pageTitle", "Fresco");
        model.addAttribute("path", "index");
        return "products";
    } 
    
    @GetMapping("/vegetables")
    public String getVegetables(Model model,@ModelAttribute("isLoggedIn") boolean isLoggedIn){
        List<Item> vegetables = itemRepository.findAllByCategory("vegetable");
        model.addAttribute("products", vegetables);
        model.addAttribute("pageTitle", "Fresco Vegetables");
        model.addAttribute("path", "/vegetables");
        return "products";
    }

    @GetMapping("/fruits")
    public String getFruits(Model model,@ModelAttribute("isLoggedIn") boolean isLoggedIn) {
        List<Item> fruits = itemRepository.findAllByCategory("fruit");
        model.addAttribute("products", fruits);
        model.addAttribute("pageTitle", "Fresco Fruits");
        model.addAttribute("path", "/fruits");
        return "products";
    }

    @GetMapping("/pantry")
    public String getPantryItems(Model model,@ModelAttribute("isLoggedIn") boolean isLoggedIn){
        List<Item> pantryItems = itemRepository.findAllByCategory("pantry");
        model.addAttribute("products", pantryItems);
        model.addAttribute("pageTitle", "Fresco Pantry");
        model.addAttribute("path", "/pantry");
        return "products";
    }
    
}
