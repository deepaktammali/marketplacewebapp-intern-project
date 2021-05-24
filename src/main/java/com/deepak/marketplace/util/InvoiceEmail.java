package com.deepak.marketplace.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Properties;

import com.deepak.marketplace.model.Address;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeMessage.RecipientType;

public class InvoiceEmail {
	public static void sendEmail(Long invoiceId, Address billingAddress) {

		Path mailEnvFilePath = Paths.get("src\\main\\resources\\properties\\mailenv.properties");

		try (FileInputStream mailEnvFileInputStream = new FileInputStream(
				mailEnvFilePath.toAbsolutePath().toString());) {
			String host = "smtp.gmail.com";

			Properties mailEnvProperties = new Properties();
			mailEnvProperties.load(mailEnvFileInputStream);

			Properties mailConfigProperties = new Properties();
			mailConfigProperties.setProperty("mail.smtp.host", host);
			mailConfigProperties.setProperty("mail.smtp.ssl.enable", "true");
			Session session = Session.getInstance(mailConfigProperties);

			String username = mailEnvProperties.getProperty("username");
			String password = mailEnvProperties.getProperty("password");

			MimeMessage message = new MimeMessage(session);
			String dueDate = new Date(System.currentTimeMillis()+(int)6.048e+8).toString();
			String mailSubject = String.format("Invoice for Products from Fresco due %s",dueDate);
			message.setSubject(mailSubject);
			message.setRecipient(RecipientType.TO, new InternetAddress(billingAddress.getEmailAddress()));

		// setting attachment
			// create the message body part
			BodyPart messageBodyPart = new MimeBodyPart();

			// set the actual message
			
			String emailBodyMessage = String.format("Hi %s, \nI hope you’re well! \nPlease see attached invoice number %d for Fresco products, due on %s. Don’t hesitate to reach out if you have any questions.",billingAddress.getContactName(),invoiceId,dueDate);
			messageBodyPart.setText(emailBodyMessage);

			// create an instance of multipart object
			Multipart multipart = new MimeMultipart();

			// set the first text message part
			multipart.addBodyPart(messageBodyPart);

			// set the second part, which is the attachment
			messageBodyPart = new MimeBodyPart();
			String invoicePathString = String.format("src\\main\\resources\\invoices\\invoice_%d.pdf", invoiceId);
			Path invoicePath = Paths.get(invoicePathString).toAbsolutePath();
			DataSource source = new FileDataSource(invoicePath.toString());
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(String.format("invoice_%d.pdf", invoiceId));
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			Transport.send(message, username, password);

			System.out.println("Mail Sent");
		} catch (IOException | MessagingException e) {
			e.printStackTrace();
		}
	}
}
