package com.deepak.marketplace.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String username;
    String password;
    String flag;

    public User(String username,String password){
        this(username,password,"member");
    }


    public User(String username,String password,String flag){
        this.username = username;
        this.password = password;
        this.flag = flag;
    }
}
