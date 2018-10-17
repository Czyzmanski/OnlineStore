package onlinestore.gui.textfields;

import java.util.Locale;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.InputValidator;

public class StreetNumberTextField extends TextField implements InputValidator, InputPreparator<String> {
	
	private final EventHandler<KeyEvent> eventFilter = e -> {
		String character = e.getCharacter();
		if(character.length() == 1) {
			char c = character.charAt(0);
			if(c < '0')
				e.consume();
		}
	};

	public StreetNumberTextField(String prompt) {
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
		if (isAnyInput()) {
			String str = this.getText();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (c >= '0' && c <= '9')
					return true;
			}
		}
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
		return this.getText().trim().toUpperCase(Locale.getDefault());
	}

}
