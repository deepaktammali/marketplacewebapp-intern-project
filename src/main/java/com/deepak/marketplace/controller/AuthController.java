package com.deepak.marketplace.controller;

import javax.servlet.http.HttpSession;

import com.deepak.marketplace.model.User;
import com.deepak.marketplace.service.AuthService;

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
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/")
@SessionAttributes({"user","isLoggedIn"})
public class AuthController {

    private AuthService authService;

    @Autowired
    AuthController(AuthService authService){
        this.authService = authService;
    }

    @ModelAttribute("user")
    public User user(){
        return null;
    }

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(){
        return false;
    }

    @GetMapping("/signin")
    public String getSignIn(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(){
        return "register";
    }

    @PostMapping(value="/signin",consumes={MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String signIn(@RequestParam("username")String username,@RequestParam("password") String password,Model model){
        if(authService.isExists(username)){
            User user = authService.authenticateUser(username, password);
            if(user!=null){
                model.addAttribute("user",user);
                model.addAttribute("isLoggedIn", true);
                return "redirect:/";
            }
        }
        return "login";
    }


    @GetMapping(value="/signout")
    public String signOut(SessionStatus sessionStatus,HttpSession httpSession){
        sessionStatus.setComplete();
        httpSession.invalidate();
        return "redirect:/";
    }


    @PostMapping(value="/register",consumes={MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String register(@RequestParam String username,@RequestParam String password){
        
        if(!authService.isExists(username)){
            User newUser = authService.registerUser(username, password);
            if(newUser!=null){
                return "redirect:/";
            }
            return "register";
        }
        return "login";
    }


}
