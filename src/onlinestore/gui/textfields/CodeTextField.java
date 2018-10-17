package onlinestore.gui.textfields;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.InputValidator;

public class CodeTextField extends TextField implements InputPreparator<String>, InputValidator {

	public CodeTextField(String prompt) {
		this.setPromptText(prompt);
		this.setOnMouseClicked(e -> this.setStyle(""));
		
		this.addEventFilter(KeyEvent.KEY_TYPED, e -> {
			String input = e.getCharacter();

			if (input.length() != 1)
				e.consume();
			if (isAnyInput())
				e.consume();

			char c = input.charAt(0);

			if (c < '0' || c > '9')
				e.consume();
		});
	}

	@Override
	public boolean isAnyInput() {
		String input = this.getText();
		return input != null && input.length() == 6;
	}

	@Override
	public boolean isValidInput() {
		return isAnyInput();
	}

	@Override
	public void setNeutralLook() {
		this.setStyle("");
		this.clear();
	}

	@Override
	public String getPreparedInput() {
		return this.getText();
	}

}
