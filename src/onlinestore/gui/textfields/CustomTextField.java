package onlinestore.gui.textfields;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.InputValidator;

public class CustomTextField extends TextField implements InputValidator, InputPreparator<String> {
	
	private final EventHandler<KeyEvent> eventFilter = e -> {
		String character = e.getCharacter();
		if(character.length() == 1) {
			char c = character.charAt(0);
			if(c <= '9' && c != ' ')
				e.consume();
		}
	};

	public CustomTextField(String prompt) {
		this.setPromptText(prompt);
		this.setOnMouseClicked(e -> this.setStyle(""));
		this.addEventFilter(KeyEvent.KEY_TYPED, eventFilter);
	}

	@Override
	public boolean isAnyInput() {
		String input = this.getText();
		return input != null && input.trim().isEmpty() == false;
	}

	@Override
	public boolean isValidInput() {
		if(isAnyInput() && this.getText().length() >= 2)
			return true;
		this.setStyle("-fx-background-color: FFE5E5;");
		return false;
	}

	@Override
	public void setNeutralLook() {
		this.clear();
		this.setStyle("");
	}

	@Override
	public String getPreparedInput() {
		return Character.toUpperCase(this.getText().trim().charAt(0))
				+ this.getText().trim().toLowerCase().substring(1);
	}

	public EventHandler<KeyEvent> getEventFilter() {
		return eventFilter;
	}

}
