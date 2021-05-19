package com.deepak.marketplace.repository.custom;

import com.deepak.marketplace.model.User;

public interface UserRepositoryCustom{
    public User findByUsername(String username);
}
