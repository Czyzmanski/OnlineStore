package onlinestore.datamodel.person;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "osoba")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person implements Serializable {

	private static final long serialVersionUID = -8143578542158518966L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "osoba_seq")
	@SequenceGenerator(name = "osoba_seq", allocationSize = 1)
	@Column(name = "osobaid")
	private long id;
	
	@Embedded
	private Name name;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "plec")
	private Sex sex;
	
	@ManyToOne
	@JoinColumn(name = "miastoid", foreignKey = @ForeignKey(name = "MIASTO_OSOBA_FK1"))
	private City city;
	
	@Embedded
	private Address address;
	
	@Column(name = "email")
	protected String email;
	
	@Column(name = "dataurodzenia")
	protected LocalDate birthDate;
	
	@Column(name = "datadolaczenia")
	protected LocalDate joiningDate;

	public Person() {
	}

	public Person(Name name, LocalDate birthDate, Sex sex, City city, Address address, String email, LocalDate joiningDate) {
		this.name = name;
		this.birthDate = birthDate;
		this.sex = sex;
		this.city = city;
		this.address = address;
		this.email = email;
		this.joiningDate = joiningDate;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", sex=" + sex + ", address=" + address + ", email=" + email
				+ ", birthDate=" + birthDate + ", joiningDate=" + joiningDate + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

}
