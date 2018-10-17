package onlinestore.gui.countrycitycomponent;

import java.text.Collator;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.InputValidator;

public class CountryComboBox extends ComboBox<String> implements InputValidator, InputPreparator<String> {
	
	public CountryComboBox() {
		setNeutralLook();
		initCountries();
	}

	@Override
	public String getPreparedInput() {
		if(this.getValue() == null)
			return null;
		return Character.toUpperCase(this.getValue().trim().charAt(0))
				+ this.getValue().trim().toLowerCase().substring(1);
	}

	@Override
	public boolean isAnyInput() {
		return this.getValue() != null;
	}

	@Override
	public boolean isValidInput() {
		return isAnyInput();
	}

	@Override
	public void setNeutralLook() {
		this.setValue(Locale.getDefault().getDisplayCountry());
	}
	
	private void initCountries() {
		String[] countryCodes = Locale.getISOCountries();
		for (String countryCode : countryCodes) {
			this.getItems().add(new Locale("", countryCode).getDisplayCountry());
		}
		FXCollections.sort(this.getItems(), Collator.getInstance(Locale.getDefault()));
	}
	
}
