package onlinestore.gui.passwordcomponent;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.InputValidator;
import onlinestore.gui.scenes.CreateNewAccountScene;

public class PasswordInput extends VBox implements InputPreparator<String>, InputValidator {

	private final PasswordField passwordInput = new PasswordField();
	private final PasswordField repeatedPasswordInput = new PasswordField();

	private final PasswordVerificator passwordVerificator = new PasswordVerificator();
	private final RepeatedPasswordVerificator repeatedPasswordVerificator = new RepeatedPasswordVerificator();

	private final Text errorText;

	volatile long passwordKeyTypedTimeStamp = 0;
	volatile long repeatedPasswordKeyTypedTimeStamp = 0;

	volatile int passwordInputChanged = 0;
	volatile int repeatedPasswordInputChanged = 0;

	private boolean areEqual = false;

	public PasswordInput(double spacing, CreateNewAccountScene parent) {
		super(spacing);

		errorText = parent.getErrorNotificationText();

		passwordInput.setPromptText("Password");
		repeatedPasswordInput.setPromptText("Repeated password");

		passwordInput.addEventHandler(KeyEvent.KEY_TYPED, e -> {
			passwordKeyTypedTimeStamp = System.currentTimeMillis();
		});
		
		repeatedPasswordInput.addEventHandler(KeyEvent.KEY_TYPED, e -> {
			repeatedPasswordKeyTypedTimeStamp = System.currentTimeMillis();
		});

		passwordInput.textProperty().addListener((observable, oldPassword, newPassword) -> {
			if (passwordInputChanged == 0) {
				passwordInputChanged++;
				Thread t = new Thread(passwordVerificator);
				t.setDaemon(true);
				t.start();
			}

			if (areEqual) {
				Thread t = new Thread(repeatedPasswordVerificator);
				t.setDaemon(true);
				t.start();
			}
		});
		
		repeatedPasswordInput.textProperty().addListener((observable, oldPassword, newPassword) -> {
			if (repeatedPasswordInputChanged == 0) {
				repeatedPasswordInputChanged++;
				
				Thread t = new Thread(repeatedPasswordVerificator);
				t.setDaemon(true);
				t.start();
			}
		});

		this.getChildren().addAll(passwordInput, repeatedPasswordInput);
	}

	public void setInputListener(ChangeListener<String> inputListener) {
		passwordInput.textProperty().addListener(inputListener);
		repeatedPasswordInput.textProperty().addListener(inputListener);
	}

	@Override
	public boolean isAnyInput() {
		return areEqual;
	}

	@Override
	public boolean isValidInput() {
		return isAnyInput();
	}

	@Override
	public void setNeutralLook() {
		passwordInput.clear();
		passwordInput.setStyle("");
		repeatedPasswordInput.clear();
		repeatedPasswordInput.setStyle("");
	}

	@Override
	public String getPreparedInput() {
		return repeatedPasswordInput.getText();
	}

	private boolean isStrongPassword(String password) {
		if (password.length() <= 7)
			return false;

		return true;
	}

	private class PasswordVerificator implements Runnable {

		@Override
		public void run() {
			while (Thread.currentThread().isInterrupted() == false) {

				if (System.currentTimeMillis() - passwordKeyTypedTimeStamp > 2000
						&& isStrongPassword(passwordInput.getText()) == false) {

					passwordInput.setStyle("-fx-background-color: FFE5E5;");
					errorText.setText(
							"Password should contain min 7 characters");

					passwordInputChanged = 0;
					Thread.currentThread().interrupt();

				} else if (System.currentTimeMillis() - passwordKeyTypedTimeStamp > 2000
						&& isStrongPassword(passwordInput.getText())) {

					passwordInputChanged = 0;
					
					passwordInput.setStyle("");
					errorText.setText("");
					
					Thread.currentThread().interrupt();

				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

			}

		}

	}

	private class RepeatedPasswordVerificator implements Runnable {

		@Override
		public void run() {
			while (Thread.currentThread().isInterrupted() == false) {

				if (System.currentTimeMillis() - repeatedPasswordKeyTypedTimeStamp > 2000) {
					String password = passwordInput.getText();

					if (isStrongPassword(password) && password.equals(repeatedPasswordInput.getText())) {
						areEqual = true;
						
						repeatedPasswordInput.setStyle("");
						errorText.setText("");
						
						repeatedPasswordInputChanged = 0;
						
						Thread.currentThread().interrupt();
					} else if (isStrongPassword(password)) {

						errorText.setText("Password and repeated password differ.");
						repeatedPasswordInput.setStyle("-fx-background-color: FFE5E5;");

						areEqual = false;
						
						repeatedPasswordInputChanged = 0;

						Thread.currentThread().interrupt();
					}
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

			}

		}

	}

}
