package onlinestore.gui.sexcomponent;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import onlinestore.datamodel.person.Sex;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.InputValidator;

public class SexInput extends HBox implements InputValidator, InputPreparator<Sex> {
	
	private final ToggleGroup sexGroup = new ToggleGroup();
	private final RadioButton femaleRadioButton = new RadioButton("female");
	private final RadioButton maleRadioButton = new RadioButton("male");
	
	public SexInput(double spacing) {
		super(spacing);
		femaleRadioButton.setToggleGroup(sexGroup);
		maleRadioButton.setToggleGroup(sexGroup);
		
		this.getChildren().addAll(femaleRadioButton, maleRadioButton);
	}
	
	public void setSelectionListener(final ChangeListener<Boolean> listener) {
		femaleRadioButton.selectedProperty().addListener(listener);
		maleRadioButton.selectedProperty().addListener(listener);
	}

	@Override
	public boolean isAnyInput() {
		return sexGroup.getSelectedToggle() != null;
	}

	@Override
	public boolean isValidInput() {
		return isAnyInput();
	}

	@Override
	public void setNeutralLook() {
		femaleRadioButton.setSelected(false);
		maleRadioButton.setSelected(false);
	}

	@Override
	public Sex getPreparedInput() {
		if(sexGroup.getSelectedToggle() == femaleRadioButton)
			return Sex.FEMALE;
		if(sexGroup.getSelectedToggle() == maleRadioButton)
			return Sex.MALE;
		return null;
	}
	
}
