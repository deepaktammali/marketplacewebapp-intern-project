package com.deepak.marketplace.repository.custom;

import java.util.List;
import com.deepak.marketplace.model.Item;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private SessionFactory sessionFactory;

    @Autowired
    ItemRepositoryCustomImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Item findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Query<Item> query = session.createQuery("FROM Item WHERE name=:name", Item.class);
        query.setParameter("name", name);
        Item item = query.uniqueResult();
        tx.commit();
        return item;
    }

    @Override
    public List<Item> findAllByCategory(String category) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Query<Item> query = session.createQuery("FROM Item WHERE category=:category", Item.class);
        query.setParameter("category", category);
        List<Item> items = query.list();
        tx.commit();
        return items;
    }
}
