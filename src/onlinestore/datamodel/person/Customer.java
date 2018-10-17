package onlinestore.datamodel.person;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "klient")
@PrimaryKeyJoinColumn(name = "klientid")
public class Customer extends Person {

	private static final long serialVersionUID = -3135294118982560722L;

	@Column(name = "username", unique = true)
	private String username;

	@Column(name = "haslo")
	private String password;

	public Customer() {
	}

	public Customer(Name name, LocalDate birthDate, Sex sex, City city, Address address, String email,
			LocalDate joiningDate, String username, String password) {
		super(name, birthDate, sex, city, address, email, joiningDate);
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Customer [username=" + username + "]\n" + super.toString();
	}

}
