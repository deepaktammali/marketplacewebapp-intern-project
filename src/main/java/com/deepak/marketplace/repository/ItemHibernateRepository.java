package com.deepak.marketplace.repository;

import com.deepak.marketplace.model.Item;
import com.deepak.marketplace.repository.custom.ItemRepositoryCustom;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface ItemHibernateRepository extends CrudRepository<Item,Long>,ItemRepositoryCustom {

}
