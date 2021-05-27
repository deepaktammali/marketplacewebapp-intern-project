package com.deepak.marketplace.repository.custom;

import com.deepak.marketplace.model.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private SessionFactory sessionFactory;

    @Autowired
    UserRepositoryCustomImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User findByUsername(String username) {
        // get current session
        Session session = sessionFactory.getCurrentSession();
        // get current transaction and begin transaction
        session.getTransaction().begin();

        Query<User> query = session.createQuery("FROM User WHERE username=:username", User.class);
        query.setParameter("username", username);
        User user = query.uniqueResult();
        // get transaction and commit the transaction
        session.getTransaction().commit();
        return user;
    }

    @Override
    public User makeUserAdmin(Long id) {
        // get current session
        Session session = sessionFactory.getCurrentSession();
        // get current transaction and begin transaction
        session.getTransaction().begin();

        Query<User> query = session.createQuery("FROM User WHERE id=:id", User.class);
        query.setParameter("id", id);
        User user = query.uniqueResult();
        user.setFlag("admin");
        session.getTransaction().commit();
        return user;
    }

}
