package onlinestore.datamodel.person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "panstwo")
public class Country {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "panstwo_seq")
	@SequenceGenerator(name = "panstwo_seq", allocationSize = 1)
	@Column(name = "panstwoid")
	private long id;
	
	@NaturalId
	@Column(name = "panstwonazwa", unique = true)
	private String name;

	Country() {
	}

	public Country(String country) {
		super();
		this.name = country;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Country other = (Country) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", country=" + name + "]";
	}

}
