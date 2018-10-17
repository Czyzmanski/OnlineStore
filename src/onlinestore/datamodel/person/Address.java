package onlinestore.datamodel.person;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

// value object
@Embeddable
public class Address implements Serializable {

	private static final long serialVersionUID = -941641319273574907L;

	@Column(name = "ulica")
	private String street;

	@Column(name = "budynek")
	private String streetNumber;

	@Column(name = "nrmieszkania")
	private Integer flatNumber;

	Address() {
	}

	public Address(String street, String streetNumber, Integer flatNumber) {
		super();
		this.street = street;
		this.streetNumber = streetNumber;
		this.flatNumber = flatNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public Integer getFlatNumber() {
		return flatNumber;
	}

	public void setFlatNumber(Integer flatNumber) {
		this.flatNumber = flatNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + flatNumber;
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((streetNumber == null) ? 0 : streetNumber.hashCode());
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
		Address other = (Address) obj;
		if (flatNumber != other.flatNumber)
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (streetNumber == null) {
			if (other.streetNumber != null)
				return false;
		} else if (!streetNumber.equals(other.streetNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return street + " " + streetNumber + (flatNumber != null ? "/" + flatNumber : "");
	}

}
