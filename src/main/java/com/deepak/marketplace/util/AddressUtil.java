package com.deepak.marketplace.util;

import java.util.HashMap;

import com.deepak.marketplace.model.Address;

public class AddressUtil {

    public static Address[] parseShippingAndBillingAddress(HashMap<String,String> addressMap){
        String shippingContactName = addressMap.get("shipping_name");
        String shippingCompanyName = addressMap.get("shipping_company_name");
        String shippingAddress = addressMap.get("shipping_address");
        String shippingPhone = addressMap.get("shipping_phone");
        String shippingEmail= addressMap.get("shipping_email");
        
        String billingContactName = addressMap.get("billing_name");
        String billingCompanyName = addressMap.get("billing_company_name");
        String billingAddress= addressMap.get("billing_address");
        String billingPhone= addressMap.get("billing_phone");
        String billingEmail = addressMap.get("billing_email");
        
        Address billingAddressObj = new Address(billingContactName,billingCompanyName,billingAddress,billingPhone,billingEmail);
        Address shippingAddressObj = new Address(shippingContactName,shippingCompanyName,shippingAddress,shippingPhone,shippingEmail);
        
        Address[] addressList = {billingAddressObj,shippingAddressObj};
        return addressList;
    }
    
}
