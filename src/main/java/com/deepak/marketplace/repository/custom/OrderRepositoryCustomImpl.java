package com.deepak.marketplace.repository.custom;

import java.util.List;

import com.deepak.marketplace.model.Order;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private SessionFactory sessionFactory;

    @Autowired
    OrderRepositoryCustomImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Order> findOrdersByCustomerName(String contactName) {
        Session session = sessionFactory.getCurrentSession();

        Transaction transaction = session.getTransaction();

        Query<Order> query = session.createQuery("FROM Order WHERE billingAddress.contactName=:contactName",Order.class);
        query.setParameter("contactName", contactName);
        List<Order> matchedOrders = query.list();
        transaction.commit();
        return matchedOrders;
    }    
}
