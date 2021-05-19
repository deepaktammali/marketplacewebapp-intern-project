package com.deepak.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends Item {
    int quantity=1;

    public void setItem(Item item){
        this.id = item.id;
        this.category = item.category;
        this.description = item.description;
        this.name = item.name;
        this.price = item.price;
        this.imageURL = item.imageURL;
    }

    public void incrementQuantity(){
        this.quantity += 1;
    }

    public void decrementQuantity(){
        this.quantity -= 1;
    }
}
