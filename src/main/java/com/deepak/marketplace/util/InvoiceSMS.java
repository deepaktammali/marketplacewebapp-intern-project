package com.deepak.marketplace.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Properties;

import com.deepak.marketplace.model.Address;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class InvoiceSMS {
    public static void sendMessage(Long invoiceId, Address billingAddress, Double grandTotal){

        Path mailEnvFilePath = Paths.get("src\\main\\resources\\properties\\smsenv.properties");

        try(
				FileInputStream smsEnvFileInputStream = new FileInputStream(mailEnvFilePath.toAbsolutePath().toString());
				)
		{
			Properties smsEnvProperties = new Properties();
			
			smsEnvProperties.load(smsEnvFileInputStream);
			
			final String ACCOUNT_SID = smsEnvProperties.getProperty("ACCOUNT_SID");
		    final String AUTH_TOKEN = smsEnvProperties.getProperty("AUTH_TOKEN");
		    final String FROM_NUMBER = smsEnvProperties.getProperty("FROM_NUMBER");
		    
		        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

				String dueDate = new Date(System.currentTimeMillis()+(int)6.048e+8).toString();
				String createdAt = new Date(System.currentTimeMillis()).toString();
				String messageContent = String.format("Hi %s, Thanks for shopping with Fresco. Invoice %d, dated %s, is sent to your email %s. The amount due is %f if paid by %s.", billingAddress.getContactName(),invoiceId,createdAt,billingAddress.getEmailAddress(),grandTotal,dueDate);

		        Message message = Message
		                .creator(new PhoneNumber(billingAddress.getPhoneNumber()), // to
		                        new PhoneNumber(FROM_NUMBER), // from
		                        messageContent)
		                .create();

		        System.out.println(message.getSid());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
