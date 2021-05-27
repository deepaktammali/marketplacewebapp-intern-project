package com.deepak.marketplace.controller;

import java.util.List;
import java.util.Vector;

import com.deepak.marketplace.model.User;
import com.deepak.marketplace.repository.UserHibernateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/addadmin")
@SessionAttributes({"user","isLoggedIn","isAdmin"})
public class AddAdminController {

    private UserHibernateRepository userRepository;

    @Autowired
    AddAdminController(UserHibernateRepository userRepository){
        this.userRepository = userRepository;
    }

    @ModelAttribute("user")
    public User user(){
        return null;
    }

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(){
        return false;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(){
        return false;
    }
    

    @GetMapping
    public String getAddAdminPage(Model model){
        List<User> users = new Vector<User>();
        userRepository.findAll().forEach(users::add);
        model.addAttribute("users",users);
        return "addadmin";
    }


    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String addAdmin(@RequestParam("id") Long id){
        User user = userRepository.makeUserAdmin(id);
        return "redirect:/addadmin";
    }

}
