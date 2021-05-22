package com.deepak.marketplace.repository.custom;

import com.deepak.marketplace.model.Order;

import java.util.List;


public interface OrderRepositoryCustom {
    public List<Order> findOrdersByCustomerName(String name);
}
