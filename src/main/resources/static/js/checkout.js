// Init Modal

MicroModal.init();
// MicroModal.show('modal-1'); 


document.getElementById("sameaddresscheckbox").addEventListener("change",function (evt){
    let isAddressSame = evt.target.checked;
    if(isAddressSame){
        fillShippingAddressWithBillingAddress()
    }
    else{
        clearShippingAddress();
    }
})


function setShippingAddress(billingName,billingCompanyName,billingAddress,billingPhone,billingEmail){
    setInputElementValueById("shipping_name",billingName);
    setInputElementValueById("shipping_company_name",billingCompanyName);
    setInputElementValueById("shipping_address",billingAddress);
    setInputElementValueById("shipping_phone",billingPhone);
    setInputElementValueById("shipping_email",billingEmail);
}

function getInputElementValueById(id){
    return document.getElementById(id).value;
}

function setInputElementValueById(id,value){
   document.getElementById(id).value = value;
}

function clearShippingAddress(){
    setShippingAddress("","","","","");
}


function fillShippingAddressWithBillingAddress(){
    let billingName = getInputElementValueById("billing_name");
    let billingCompanyName = getInputElementValueById("billing_company_name");
    let billingAddress= getInputElementValueById("billing_address");
    let billingPhone= getInputElementValueById("billing_phone");
    let billingEmail = getInputElementValueById("billing_email");
    setShippingAddress(billingName,billingCompanyName,billingAddress,billingPhone,billingEmail);
}





