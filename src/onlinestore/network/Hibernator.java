package onlinestore.network;

import java.util.List;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javafx.application.Platform;
import onlinestore.datamodel.person.City;
import onlinestore.datamodel.person.Country;
import onlinestore.datamodel.person.Customer;
import onlinestore.datamodel.person.Name;
import onlinestore.datamodel.person.Person;
import onlinestore.gui.ShowModalWindow;

public class Hibernator {

	private SessionFactory fact;
	private PasswordAuthentication databaseLogin;
	private Configuration con;

	public Hibernator(PasswordAuthentication databaseLogin) throws Exception {
		this.databaseLogin = databaseLogin;

		con = new Configuration();
		con.addAnnotatedClass(Person.class);
		con.addAnnotatedClass(Customer.class);
		con.addAnnotatedClass(Country.class);
		con.addAnnotatedClass(City.class);
		con.addAnnotatedClass(Name.class);

		Properties prop = con.getProperties();
		prop.setProperty("hibernate.connection.username", databaseLogin.getUserName());
		prop.setProperty("hibernate.connection.password", databaseLogin.getPassword());

		fact = con.configure().buildSessionFactory();
	}

	public boolean save(Person person) {
		Transaction tx = null;

		try (Session ses = fact.openSession()) {
			tx = ses.beginTransaction();

			City city = person.getCity();

			if (!save(city))
				return false;

			Country country = city.getCountry();

			if (!save(country))
				return false;

			if (person instanceof Customer && getCustomerByUsername(((Customer) person).getUsername()) != null)
				return false;

			ses.save(person);

			tx.commit();
		} catch (NoResultException e) {

		} catch (Exception e) {
			handleException(e, tx);
			return false;
		}

		return true;
	}

	public boolean save(City city) {
		Transaction tx = null;

		try (Session ses = fact.openSession()) {
			tx = ses.beginTransaction();

			Query<City> query = ses.createQuery("FROM City WHERE upper(name) = :cityName", City.class);
			query.setParameter("cityName", city.getName().toUpperCase());

			City res = query.getSingleResult();

			if (res == null)
				ses.save(city);
			else
				city.setId(res.getId());

			tx.commit();
		} catch (NoResultException e) {

		} catch (Exception e) {
			handleException(e, tx);
			return false;
		}

		return true;
	}

	public boolean save(Country country) {
		Transaction tx = null;

		try (Session ses = fact.openSession()) {
			tx = ses.beginTransaction();

			Query<Country> query = ses.createQuery("FROM Country WHERE upper(name) = :countryName", Country.class);
			query.setParameter("countryName", country.getName().toUpperCase());

			Country res = query.getSingleResult();

			if (res == null)
				ses.save(country);
			else
				country.setId(res.getId());

			tx.commit();
		} catch (NoResultException e) {

		} catch (Exception e) {
			handleException(e, tx);
			return false;
		}

		return true;
	}

	/*
	 * @return list of object of specified type or null if error occurs
	 */

	@SuppressWarnings("rawtypes")
	public List getAll(Class<?> cl) {
		Transaction tx = null;
		List res = null;

		try (Session ses = fact.openSession()) {
			tx = ses.beginTransaction();

			Query query = ses.createQuery(String.format("FROM %s", cl.getSimpleName()), cl);

			res = query.getResultList();

			tx.commit();
		} catch (NoResultException e) {

		} catch (Exception e) {
			handleException(e, tx);
		}

		return res;
	}

	/*
	 * @return the object of designated type by the specified id or null if there is
	 * no such city in the database or error occurs
	 */

	@SuppressWarnings("rawtypes")
	public Object getById(Class<?> cl, Long id) {
		Transaction tx = null;
		Object res = null;

		try (Session ses = fact.openSession()) {
			tx = ses.beginTransaction();

			Query query = ses.createQuery(String.format("FROM %s WHERE id = :id", cl.getSimpleName()), cl);
			query.setParameter("id", id);

			res = query.getSingleResult();

			tx.commit();
		} catch (NoResultException e) {

		} catch (Exception e) {
			handleException(e, tx);
		}

		return res;
	}

	/*
	 * @return customer of specified username
	 */

	public Customer getCustomerByUsername(String username) {
		Transaction tx = null;
		Customer res = null;

		try (Session ses = fact.openSession()) {
			tx = ses.beginTransaction();

			Query<Customer> query = ses.createQuery("FROM Customer WHERE username = :username", Customer.class);
			query.setParameter("username", username);

			res = query.getSingleResult();

			tx.commit();
		} catch (NoResultException e) {
			System.err.println("getCustomerByUsername : NoResultException");
		} catch (Exception e) {
			handleException(e, tx);
		}

		return res;
	}

	/*
	 * @return city of the specified name or null if there is no such city in the
	 * database or error occurs
	 */

	public City getCityByName(String name) {
		Transaction tx = null;
		City res = null;

		try (Session ses = fact.openSession()) {
			tx = ses.beginTransaction();

			Query<City> query = ses.createQuery("FROM City WHERE upper(name) = :cityName", City.class);
			query.setParameter("cityName", name.toUpperCase());

			res = query.getSingleResult();

			tx.commit();
		} catch (NoResultException e) {

		} catch (Exception e) {
			handleException(e, tx);
		}

		return res;
	}

	/*
	 * @return cities that are located in the specified country
	 */

	public List<City> getCitiesByCountryName(String name) {
		Transaction tx = null;
		List<City> res = null;

		try (Session ses = fact.openSession()) {
			tx = ses.beginTransaction();

			Query<City> query = ses.createQuery(
					"SELECT city FROM City city JOIN city.country WHERE upper(city.country.name) = :countryName",
					City.class);
			query.setParameter("countryName", name.toUpperCase());

			res = query.getResultList();

			tx.commit();
		} catch (NoResultException e) {

		} catch (Exception e) {
			handleException(e, tx);
		}

		return res;
	}

	/*
	 * @return country with the specified name or null if there is no such country
	 * in the database or error occurs
	 */

	public Country getCountryByName(String name) {
		Transaction tx = null;
		Country res = null;

		try (Session ses = fact.openSession()) {
			tx = ses.beginTransaction();

			Query<Country> query = ses.createQuery("FROM Country WHERE upper(name) = :countryName", Country.class);
			query.setParameter("countryName", name.toUpperCase());

			res = query.getSingleResult();

			tx.commit();
		} catch (NoResultException e) {

		} catch (Exception e) {
			handleException(e, tx);
		}

		return res;
	}

	/*
	 * closes SessionFactory
	 */

	public void close() {
		if (fact != null)
			fact.close();
	}

	/*
	 * shows alert with exception stack trace
	 */

	private void handleException(Exception e, Transaction tx) {
		if (tx != null) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}
		}
		Platform.runLater(() -> ShowModalWindow.showProblemWithConnectionAlert(e));
		e.printStackTrace();
	}

}
