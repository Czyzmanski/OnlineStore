package onlinestore.gui.scenes;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import onlinestore.datamodel.person.Customer;
import onlinestore.gui.interfaces.InputValidator;
import onlinestore.gui.panels.ActionPanel;
import onlinestore.gui.textfields.CustomTextField;

public class LoginScene extends Scene implements InputValidator {

	// interaction with caller, in this case Main Menu
	private final MainMenuScene mainMenu;
	private final Stage window;

	// GUI
	private final GridPane loginLayout;

	private final Text header = new Text("Log in or register");
	private final Font headerFont = Font.font("Tahoma", FontWeight.NORMAL, 20);

	private final Label usernameLabel = new Label("Username: ");
	private final Label passwordLabel = new Label("Password: ");

	private final CustomTextField usernameInput = new CustomTextField("Username");
	private final PasswordField passwordInput = new PasswordField();
	private final ActionPanel buttonsPanel = new ActionPanel(10, "Log in", "Back");
	private final Hyperlink createAccount = new Hyperlink("Don't have an account yet? Click here to register!");

	// further scenes
	private CreateNewAccountScene newAccountScene;
	private UserMenuScene userMenuScene;

	// interaction with the user
	private final ChangeListener<String> inputListener;

	public LoginScene(final MainMenuScene mainMenu, final GridPane layout, double width, double height) {
		super(layout, width, height);

		this.mainMenu = mainMenu;
		this.window = mainMenu.getWindowMenu();
		this.loginLayout = layout;

		this.inputListener = (observable, oldVal, newVal) -> {
			if (isAnyInput())
				buttonsPanel.getActionButton().setDisable(false);
			else
				buttonsPanel.getActionButton().setDisable(true);
		};

		adjustComponents();
		addLabels();
		addForms();
		addListeners();
	}

	private void adjustComponents() {
		loginLayout.setPadding(new Insets(10, 10, 10, 10));
		loginLayout.setVgap(8);
		loginLayout.setHgap(10);
		loginLayout.setAlignment(Pos.CENTER);

		passwordInput.setPromptText("Password");

		header.setFont(headerFont);

		buttonsPanel.getActionButton().setDisable(true);
		buttonsPanel.getActionButton().setOnAction(e -> {
			if (isValidInput()) {
				window.setScene(userMenuScene);
				window.setTitle(String.format("Logged in as: %s", userMenuScene.getCustomer().getName()));
			}
		});

		buttonsPanel.getBackButton().setOnAction(e -> {
			mainMenu.launchDigitalClock();
			window.setTitle("Menu");
			window.setScene(mainMenu);
		});

		createAccount.setOnAction(e -> {
			if (newAccountScene == null) 
				newAccountScene = new CreateNewAccountScene(mainMenu, new GridPane(), 900, 600);
			
			newAccountScene.setNeutralLook();
			window.setScene(newAccountScene);
			window.setTitle("Create new account");
		});
	}

	private void addLabels() {
		loginLayout.add(header, 0, 0, 2, 1);
		loginLayout.add(usernameLabel, 0, 1);
		loginLayout.add(passwordLabel, 0, 2);
	}

	private void addForms() {
		loginLayout.add(usernameInput, 1, 1);
		loginLayout.add(passwordInput, 1, 2);
		loginLayout.add(buttonsPanel, 1, 3);
		loginLayout.add(createAccount, 0, 4, 2, 1);
	}

	private void addListeners() {
		usernameInput.textProperty().addListener(inputListener);
		passwordInput.textProperty().addListener(inputListener);
	}

	@Override
	public boolean isAnyInput() {
		return usernameInput.isAnyInput() && passwordInput.getText() != null
				&& passwordInput.getText().trim().length() >= 7;
	}

	@Override
	public boolean isValidInput() {
		if (isAnyInput()) {
			Customer cust = mainMenu.getHibernator().getCustomerByUsername(usernameInput.getText());

			if (cust == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("No such user");
				alert.setHeaderText("NO SUCH USER");
				alert.setContentText("There is no user with provided username in the database.");
				alert.showAndWait();
				return false;
			}

			if (cust.getPassword().equals(passwordInput.getText()) == false) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Wrong password");
				alert.setHeaderText("WRONG PASSWORD");
				alert.setContentText("You have typed wrong password. Regard for Caps Lock.");
				alert.showAndWait();
				return false;
			}

			if (userMenuScene == null)
				userMenuScene = new UserMenuScene(mainMenu, window, new VBox(10), cust);
			else
				userMenuScene.setCustomer(cust);

			return true;
		}

		return false;
	}

	@Override
	public void setNeutralLook() {
		usernameInput.setNeutralLook();
		passwordInput.setStyle("");
		passwordInput.clear();
		createAccount.setVisited(false);
	}

	public CreateNewAccountScene getNewAccountScene() {
		return newAccountScene;
	}

}
