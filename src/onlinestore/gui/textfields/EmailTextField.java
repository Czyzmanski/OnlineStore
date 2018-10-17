package onlinestore.gui.textfields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextField;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.InputValidator;

public class EmailTextField extends TextField implements InputPreparator<String>, InputValidator {

	private final Pattern mailPattern;

	public EmailTextField(String prompt) {
		this.mailPattern = Pattern.compile(
				"^[A-Za-z0-9\\!\\\"#\\$%&'\\(\\)\\*\\+,\\-\\./:;\\<\\=\\>\\?\\[\\]\\^_`\\{\\|\\}~]{2,}@([A-Za-z0-9]{2,}[\\.\\-]{1}){1,3}[A-Za-z]{2,4}$");

		this.setPromptText(prompt);
		this.setOnMouseClicked(e -> this.setStyle(""));
		this.textProperty().addListener((a, b, c) -> {
			isValidInput();
		});
	}

	@Override
	public boolean isAnyInput() {
		String input = this.getText();
		return input != null && input.trim().isEmpty() == false;
	}

	@Override
	public boolean isValidInput() {
		boolean result = false;
		
		if (isAnyInput()) {
			Matcher mailMatcher = mailPattern.matcher(this.getText());

			if (mailMatcher.matches()) {
				this.setStyle("");
				result = true;
			} else {
				this.setStyle("-fx-background-color: FFE5E5;");
			}
		}
		// this.setStyle("-fx-background-color: FFE5E5;");
		return result;
	}

	@Override
	public void setNeutralLook() {
		this.clear();
		this.setStyle("");
	}

	@Override
	public String getPreparedInput() {
		return this.getText();
	}

}
