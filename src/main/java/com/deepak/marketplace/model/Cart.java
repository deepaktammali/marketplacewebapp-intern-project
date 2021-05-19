package com.deepak.marketplace.model;

import java.util.List;
import java.util.Vector;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    User user;
    List<CartItem> cartItems = new Vector<CartItem>();
}
