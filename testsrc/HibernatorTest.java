import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import javax.mail.PasswordAuthentication;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import onlinestore.datamodel.person.Address;
import onlinestore.datamodel.person.City;
import onlinestore.datamodel.person.Country;
import onlinestore.datamodel.person.Customer;
import onlinestore.datamodel.person.Name;
import onlinestore.datamodel.person.Person;
import onlinestore.datamodel.person.Sex;
import onlinestore.network.Hibernator;

class HibernatorTest {

	private static Hibernator hib;
	private static Customer customer;

	@BeforeAll
	static void setUpBeforeClass() {
		try {
			hib = new Hibernator(new PasswordAuthentication("s16772", "oracle12"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Name name = new Name("Jan", "Dziki");
		Country country = new Country("Polska");
		City city = new City("Warszawa", country);
		
		customer = new Customer();
		customer.setName(name);
		customer.setBirthDate(LocalDate.of(1995, 10, 10));
		customer.setSex(Sex.MALE);
		customer.setCity(city);
		customer.setAddress(new Address("Krótka", "23C", null));
		customer.setEmail("dzik@wp.pl");
		customer.setUsername("dzik");
		customer.setPassword("rowerek12");
		customer.setJoiningDate(LocalDate.now());
	}

	@AfterAll
	static void tearDownAfterClass() {
		hib.close();
	}
	
	@Test
	void testPersonShouldReturnNull() {
		assertNull(hib.getById(Person.class, (long) -1));
	}
	
	@Test
	void testPersonShouldReturnNotNull() {
		 assertNotNull(hib.getById(Person.class, (long) 1));
	}
	
	@Test
	void testCustomerShouldReturnNull() {
		assertNull(hib.getById(Customer.class, (long) -1));
	}
	
	@Test
	void testCustomerShouldReturnNotNull() {
		assertNotNull(hib.getById(Customer.class, (long) 1));
	}
	
	@Test
	void testCustomerShouldSaveSuccessfully() {
		assertTrue(hib.save(customer));
	}
	
	@Test
	void testPersonAndCustomerShouldReturnTrue() {
		assertTrue(hib.getAll(Person.class).size() != hib.getAll(Customer.class).size());
	}
	
	@Test
	void testCityShouldReturnSomeCities() {
		assertTrue(hib.getCitiesByCountryName("Polska").size() > 0);
	}

	@Test
	void testCity() {
		assertAll(() -> assertNull(hib.getCityByName("Abcdefg")), 
				  () -> assertNull(hib.getById(City.class, (long) -1)),
				  () -> assertNotNull(hib.getById(City.class, (long) 1)),
				  () -> assertTrue(hib.getAll(City.class).size() > 0),
				  () -> assertEquals(new City("Warszawa", new Country("Polska")), hib.getCityByName("Warszawa")));
	}


	@Test
	void testCountry() {
		assertAll(() -> assertNull(hib.getCountryByName("Abcdefg")), 
				  () -> assertNull(hib.getById(Country.class, (long) -1)),
				  () -> assertNotNull(hib.getById(Country.class, (long) 1)),
				  () -> assertTrue(hib.getAll(Country.class).size() > 0),
				  () -> assertEquals(new Country("Kanada"), hib.getCountryByName("Kanada")));
	}

}
