package com.deepak.marketplace.controller;

import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import com.deepak.marketplace.model.Cart;
import com.deepak.marketplace.model.CartItem;
import com.deepak.marketplace.model.Item;
import com.deepak.marketplace.model.User;
import com.deepak.marketplace.repository.ItemHibernateRepository;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@SessionAttributes({"cart","isLoggedIn"})
public class CartController {

    private ItemHibernateRepository itemRepository;

    @Autowired
    CartController(ItemHibernateRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    // model attributes

    @ModelAttribute("user")
    public User user(){
        return null;
    }

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(){
        return false;
    }

    @ModelAttribute("cart")
    public Cart getCart(){
        return new Cart();
    }

    // request mapping

    @GetMapping(value="/cart")
    public String getCart(@ModelAttribute("cart") Cart cart,@ModelAttribute("isLoggedIn") boolean isLoggedIn) {
        return "cart";
    }

    @GetMapping(value="/{category}/addtocart")
    public String addToCart(@ModelAttribute("cart") Cart cart,@PathParam("id")Long id,@PathVariable("category") String category ) {

        boolean itemExists = cart.getCartItems()
                                .stream()    
                                .anyMatch(cartItem->{
                                    return cartItem.getId().equals(id);
                                });

        if(itemExists){
            System.out.println("Item exists");
    
            cart.getCartItems()
                .stream()
                .map(cartItem->{
                    if(cartItem.getId().equals(id)){
                        System.out.println("Incrementing Quantity"+cartItem.getQuantity());
                        cartItem.incrementQuantity();
                    }
                    return cartItem;
                })
                .collect(Collectors.toList());
        }
        else{
        itemRepository.findById(id).ifPresent(item->{
            CartItem cartItem = new CartItem();
            cartItem.setItem(item);
            cartItem.setQuantity(1);
            cart.getCartItems().add(cartItem);
        });

    }

    return ("redirect:/"+category);
    }

    @GetMapping(value="/cart/{id}/setquantity/{qty}")
    public void addToCart(@ModelAttribute("cart") Cart cart,@PathParam("id")Long id,@PathVariable("qty") Integer qty ) {

        cart.getCartItems()
            .stream()
            .map(cartItem->{
                if(cartItem.getId().equals(id)){
                    cartItem.setQuantity(qty);
                }
                return cartItem;
            });
    }
    

}
