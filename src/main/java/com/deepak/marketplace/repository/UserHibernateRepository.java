package com.deepak.marketplace.repository;

import com.deepak.marketplace.model.User;
import com.deepak.marketplace.repository.custom.UserRepositoryCustom;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHibernateRepository extends CrudRepository<User,Long>,UserRepositoryCustom {
    
}
