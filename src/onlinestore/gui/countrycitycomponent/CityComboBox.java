package onlinestore.gui.countrycitycomponent;

import javafx.scene.control.ComboBox;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.TextualInputValidator;

public class CityComboBox extends ComboBox<String> implements InputPreparator<String>, TextualInputValidator {

	public CityComboBox() {
		this.setEditable(true);
		this.setPromptText("City");
		this.setOnMouseClicked(e -> this.setStyle(""));
	}

	@Override
	public boolean isAnyInput() {
		String value = this.getValue();
		if(value == null || value.trim().isEmpty())
			return false;
		return true;
	}

	@Override
	public boolean isValidInput() {
		return isValidInput(this.getValue());
	}

	@Override
	public void setNeutralLook() {
		this.setStyle("");
		this.setValue(null);
	}

	@Override
	public String getPreparedInput() {
		if(this.getValue() == null)
			return null;
		return Character.toUpperCase(this.getValue().trim().charAt(0))
				+ this.getValue().trim().toLowerCase().substring(1);
	}

}
