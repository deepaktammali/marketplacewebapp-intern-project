package com.deepak.marketplace.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="orders")
public class Order {
    @Id
	Long id;
	@Column(name = "createdAt")
	String createdAt = new Date(System.currentTimeMillis()).toString() ;
	@ManyToOne
	Address billingAddress;
	@ManyToOne
	Address shippingAddress;
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
	List<OrderItem> orderItems;

	public void addOrderItem(OrderItem orderItem){
		orderItem.setOrder(this);
		orderItems.add(orderItem);
	}

}
