package onlinestore.network;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class EmailSender {
	
	private String lastCode;
	private Text progressOutput;

	public EmailSender() {
	}

	public void send(String sender, String recipient, String name, String login, String password)
			throws AddressException, MessagingException {

		Properties properties = System.getProperties();

		properties.put("mail.smtp.host", "poczta.o2.pl");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(login, password);
			}
		});
		
		lastCode = generateCode();

		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(sender));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
		message.setSubject("Szymon Czyzmanski's Online Store: confirmation mail");
		message.setText(String.format(
				"Hello %s!%nThank you for joining to my online store! Your code is: %s %nEnjoy,%nSzymon Czyzmanski",
				name, lastCode));
		
		if(progressOutput != null)
			progressOutput.setText("Sending email...");

		Transport.send(message);

		if(progressOutput != null) {
			progressOutput.setText("Email sent succesfully!");
			progressOutput.setFill(Color.GREENYELLOW);
		}
		
	}

	public String generateCode() {
		StringBuilder sb = new StringBuilder(6);
		for (int i = 0; i < 6; i++)
			sb.append((int) (Math.random() * 10));
		return sb.toString();
	}

	public String getLastCode() {
		return lastCode;
	}

	public void setProgressOutput(Text progressOutput) {
		this.progressOutput = progressOutput;
	}
	
	

}
