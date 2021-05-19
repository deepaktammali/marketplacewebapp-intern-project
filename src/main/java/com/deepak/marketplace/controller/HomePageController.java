package com.deepak.marketplace.controller;

import java.util.List;
import java.util.Vector;

import com.deepak.marketplace.model.Item;
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
public class HomePageController {

    private ItemHibernateRepository itemRepository;

    @Autowired
    HomePageController(ItemHibernateRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public String getHomePage(Model model,@ModelAttribute boolean isLoggedIn){
        List<Item> allItems = new Vector<Item>();
        itemRepository.findAll().forEach(allItems::add);
        model.addAttribute("products",allItems);
        model.addAttribute("pageTitle", "Freso");
        model.addAttribute("path", "");
        return "products";
    }   
}
