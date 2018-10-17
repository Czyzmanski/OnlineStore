package onlinestore.gui.textfields;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import onlinestore.datamodel.person.Customer;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.InputValidator;
import onlinestore.network.Hibernator;

public class UsernameTextField extends TextField implements InputPreparator<String>, InputValidator {

	private final Hibernator hib;

	private Thread checkUsername;
	private UsernameVerificator usernameVerificator = new UsernameVerificator();

	private boolean isValidUsername = false;

	volatile private int usernameInputChanged = 0;
	volatile private long usernameKeyTypedTimeStamp = 0;

	public UsernameTextField(Hibernator hib, String prompt) {
		this.hib = hib;

		this.setPromptText(prompt);

		this.addEventFilter(KeyEvent.KEY_TYPED, e -> {
			String str = e.getCharacter();
			if (str.length() == 1 && str.charAt(0) < '0')
				e.consume();
		});

		this.addEventHandler(KeyEvent.KEY_TYPED, e -> {
			usernameKeyTypedTimeStamp = System.currentTimeMillis();
		});

		this.textProperty().addListener((observable, oldVal, newVal) -> {
			if (usernameInputChanged == 0 && this.getText() != null && this.getText().length() > 0) {
				usernameInputChanged++;
				isValidUsername = false;

				checkUsername = new Thread(usernameVerificator, "CheckUsername");
				checkUsername.setDaemon(true);
				checkUsername.setPriority(Thread.MAX_PRIORITY);
				checkUsername.start();
			}
		});
	}

	@Override
	public boolean isAnyInput() {
		return isValidUsername;
	}

	@Override
	public boolean isValidInput() {
		return isAnyInput();
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

	private class UsernameVerificator implements Runnable {

		@Override
		public void run() {
			while (Thread.currentThread().isInterrupted() == false) {

				if (System.currentTimeMillis() - usernameKeyTypedTimeStamp > 2000) {
					Customer cust = hib.getCustomerByUsername(UsernameTextField.this.getText());

					if (cust == null) {
						isValidUsername = true;
						UsernameTextField.this.setStyle("");
					} else {
						isValidUsername = false;
						UsernameTextField.this.setStyle("-fx-background-color: FFE5E5;");
						
						Platform.runLater(() -> {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Occupied username");
							alert.setHeaderText("USERNAME ALREADY OCCUPIED");
							alert.setContentText(
									"Somebody has already used this username. \nPlease come up with another one.");
							alert.showAndWait();
						});
					}

					usernameInputChanged = 0;
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

}
