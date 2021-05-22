package com.deepak.marketplace.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
    String contactName;
	String companyName;
	String companyAddress;
	String phoneNumber;
    String emailAddress;

	public Address(String contactName,String companyName,String companyAddress,String phoneNumber,String emailAddress){
		this(null,contactName,companyName,companyAddress,phoneNumber,emailAddress);
	}

}
