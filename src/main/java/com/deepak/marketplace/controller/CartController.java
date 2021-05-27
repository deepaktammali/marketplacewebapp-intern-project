package com.deepak.marketplace.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import com.deepak.marketplace.model.Cart;
import com.deepak.marketplace.model.CartItem;
import com.deepak.marketplace.model.User;
import com.deepak.marketplace.repository.ItemHibernateRepository;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@SessionAttributes({"cart","isLoggedIn","isAdmin"})
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

    @ModelAttribute("isAdmin")
    public boolean isAdmin(){
        return false;
    }

    @ModelAttribute("cart")
    public Cart getCart(){
        return new Cart();
    }

    // request mapping

    @GetMapping(value="/cart")
    public String getCart(@ModelAttribute("cart") Cart cart,Model model,@ModelAttribute("isLoggedIn") boolean isLoggedIn) {
        boolean isCartEmpty = cart.getCartItems().isEmpty();
        model.addAttribute("isCartEmpty", isCartEmpty);
        return "cart";
    }


    @GetMapping(value="/removefromcart")
    @ResponseBody
    public ResponseEntity removeFromCart(@ModelAttribute("cart") Cart cart,Model model,@PathParam("id")Long id) {

        boolean itemExists = cart.getCartItems()
                                .stream()    
                                .anyMatch(cartItem->{
                                    return cartItem.getId().equals(id);
                                });

        if(itemExists){
            List<CartItem> cartItems = cart.getCartItems()
                .stream()
                .filter(cartItem->!cartItem.getId().equals(id))
                .collect(Collectors.toList());

            cart.setCartItems(cartItems);
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }




    @GetMapping(value="/{category}/addtocart")
    public String addToCart(@ModelAttribute("cart") Cart cart,@PathParam("id")Long id,@PathVariable("category") String category ) {

        boolean itemExists = cart.getCartItems()
                                .stream()    
                                .anyMatch(cartItem->{
                                    return cartItem.getId().equals(id);
                                });

        if(itemExists){
            cart.getCartItems()
                .stream()
                .map(cartItem->{
                    if(cartItem.getId().equals(id)){
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
    if(category.equals("index")){
        return ("redirect:/");
    }

    return ("redirect:/"+category);
    }

    @GetMapping(value="/cart/{id}/setquantity/{qty}")
    @ResponseBody
    public ResponseEntity addQuantityToCartItem(@ModelAttribute("cart") Cart cart,Model model,@PathVariable("id")Long id,@PathVariable("qty") Integer qty ) {

        boolean itemExists = cart.getCartItems()
                                .stream()    
                                .anyMatch(cartItem->{
                                    return cartItem.getId().equals(id);
                                });

        if(itemExists){

            cart.getCartItems()
                .stream()
                .map(cartItem->{
                    if(cartItem.getId().equals(id)){
                        cartItem.setQuantity(qty);
                    }
                    return cartItem;
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok().build();
        
        }
            return ResponseEntity.badRequest().build();
        
    }
    

}
