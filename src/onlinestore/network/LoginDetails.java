package onlinestore.network;

import javax.mail.PasswordAuthentication;

public class LoginDetails {
	
	private PasswordAuthentication databaseLogin;
	private PasswordAuthentication mailLogin;

	public LoginDetails(String[] args) {
		databaseLogin = new PasswordAuthentication(args[0], args[1]);
		mailLogin = new PasswordAuthentication(args[2], args[3]);
	}

	public PasswordAuthentication getDatabaseLogin() {
		return databaseLogin;
	}

	public PasswordAuthentication getMailLogin() {
		return mailLogin;
	}

}
