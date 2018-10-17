package onlinestore.gui.textfields;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.InputValidator;

public class CustomIntegerTextField extends TextField implements InputPreparator<Integer>, InputValidator {

	private Integer value;
	
	private final EventHandler<KeyEvent> eventFilter = e -> {
		String character = e.getCharacter();
		if(character.length() != 1) {
			e.consume();
		} else {
			char c = character.charAt(0);
			if(c < '0' || c > '9')
				e.consume();
		}
	};

	public CustomIntegerTextField(String prompt) {
		this.setPromptText(prompt);
		this.setOnMouseClicked(e -> this.setStyle(""));
		this.addEventFilter(KeyEvent.KEY_TYPED, eventFilter);
	}

	@Override
	public boolean isAnyInput() {
		String input = this.getText();
		return input != null && input.isEmpty() == false;
	}

	@Override
	public boolean isValidInput() {
		if (isAnyInput()) {
			try {
				value = Integer.parseInt(this.getText());
				return true;
			} catch (NumberFormatException e) {
				this.setStyle("-fx-background-color: FFE5E5;");
				return false;
			}
		}
		return false;
	}

	@Override
	public void setNeutralLook() {
		this.clear();
		this.setStyle("");
	}

	@Override
	public Integer getPreparedInput() {
		return value;
	}

	public EventHandler<KeyEvent> getEventFilter() {
		return eventFilter;
	}

}
