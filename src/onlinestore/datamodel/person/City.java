package onlinestore.datamodel.person;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "miasto")
public class City implements Serializable {

	private static final long serialVersionUID = 3813380701652892572L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "miasto_seq")
	@SequenceGenerator(name = "miasto_seq", allocationSize = 1)
	@Column(name = "miastoid")
	private long id;

	@NaturalId
	@Column(name = "miastonazwa", unique = true)
	private String name;

	@ManyToOne
	@JoinColumn(name = "panstwoid", foreignKey = @ForeignKey(name = "PANSTWO_MIASTO_FK1"))
	private Country country;

	City() {
	}

	public City(String name, Country country) {
		this.name = name;
		this.country = country;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + "]";
	}

}
