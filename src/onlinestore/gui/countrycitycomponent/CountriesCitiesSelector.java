package onlinestore.gui.countrycitycomponent;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import onlinestore.datamodel.person.City;
import onlinestore.datamodel.person.Country;
import onlinestore.network.Hibernator;

public class CountriesCitiesSelector {

	// connecting to database in order to retrieve data to handle
	private Hibernator hib;

	// components to handle
	private ComboBox<String> ctryComboBox;
	private ComboBox<String> cityComboBox;

	// sorting with respect to locale
	private Locale locale;
	private Collator collator;

	public CountriesCitiesSelector(Hibernator hib, ComboBox<String> ctryComboBox, ComboBox<String> cityComboBox) {
		this.hib = hib;

		this.ctryComboBox = ctryComboBox;
		this.cityComboBox = cityComboBox;

		this.locale = Locale.getDefault();
		this.collator = Collator.getInstance();

		this.ctryComboBox.valueProperty().addListener((val, oldVal, newVal) -> mapCitiesToCountry(newVal));
		
		mapCitiesToCountry(this.ctryComboBox.getValue());
	}

	public void process(String country, String city) {
		if (hib.save(new City(city, new Country(country)))) {
			ObservableList<String> citiesNames = cityComboBox.getItems();
			citiesNames.add(city);
			updateCityComboBox(citiesNames);
		}
	}

	private void mapCitiesToCountry(String countryName) {
		List<String> citiesNames = hib.getCitiesByCountryName(countryName).stream().map(city -> city.getName())
				.collect(Collectors.toList());

		updateCityComboBox(FXCollections.observableArrayList(citiesNames));
	}

	private void updateCityComboBox(ObservableList<String> citiesNames) {
		FXCollections.sort(citiesNames, collator);
		cityComboBox.setItems(citiesNames);
	}

	public ComboBox<String> getCtryComboBox() {
		return ctryComboBox;
	}

	public void setCtryComboBox(ComboBox<String> ctryComboBox) {
		this.ctryComboBox = ctryComboBox;
	}

	public ComboBox<String> getCityComboBox() {
		return cityComboBox;
	}

	public void setCityComboBox(ComboBox<String> cityComboBox) {
		this.cityComboBox = cityComboBox;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		this.collator = Collator.getInstance(locale);
	}

	public Collator getCollator() {
		return collator;
	}

	public void setCollator(Collator collator) {
		this.collator = collator;
	}

}
