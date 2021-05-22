package com.deepak.marketplace.repository;

import com.deepak.marketplace.model.Order;

import org.springframework.data.repository.CrudRepository;
import com.deepak.marketplace.repository.custom.OrderRepositoryCustom;

public interface OrderHibernateRepository extends CrudRepository<Order,Long>,OrderRepositoryCustom {
    
}
