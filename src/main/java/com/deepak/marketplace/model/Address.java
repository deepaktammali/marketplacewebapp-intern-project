package com.deepak.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    String contactName;
	String companyName;
	String companyAddress;
	String phoneNumber;
    String emailAddress;
}
