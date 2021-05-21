package com.deepak.marketplace.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;

public class InvoiceEmail {
    public static void sendEmail(){

        Path mailEnvFilePath = Paths.get("src\\main\\resources\\properties\\mailenv.properties");

        try(
				FileInputStream mailEnvFileInputStream = new FileInputStream(mailEnvFilePath.toAbsolutePath().toString());
				)
		{
			String host = "smtp.gmail.com";
			
			Properties mailEnvProperties = new Properties();
			mailEnvProperties.load(mailEnvFileInputStream);
			
			Properties mailConfigProperties = new Properties();
			mailConfigProperties.setProperty("mail.smtp.host",host);
			mailConfigProperties.setProperty("mail.smtp.ssl.enable","true");
			Session session = Session.getInstance(mailConfigProperties);
			
			MimeMessage message = new MimeMessage(session);
			message.setText("Message-2 sent using jakarta mail");
			message.setRecipient(RecipientType.TO,new InternetAddress("121004258@sastra.ac.in"));
			Transport.send(message,mailEnvProperties.getProperty("username"),mailEnvProperties.getProperty("password"));
			
			System.out.println("Mail Sent");
		} catch (IOException | MessagingException e) {
			e.printStackTrace();
		}
    }
}
