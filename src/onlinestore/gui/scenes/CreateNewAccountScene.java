package onlinestore.gui.scenes;

import java.time.LocalDate;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import onlinestore.datamodel.person.Address;
import onlinestore.datamodel.person.City;
import onlinestore.datamodel.person.Country;
import onlinestore.datamodel.person.Customer;
import onlinestore.datamodel.person.Name;
import onlinestore.datamodel.person.Sex;
import onlinestore.gui.birthdaycomponent.BirthdayInput;
import onlinestore.gui.countrycitycomponent.CityComboBox;
import onlinestore.gui.countrycitycomponent.CountriesCitiesSelector;
import onlinestore.gui.countrycitycomponent.CountryComboBox;
import onlinestore.gui.interfaces.CustomScene;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.InputValidator;
import onlinestore.gui.panels.ActionPanel;
import onlinestore.gui.passwordcomponent.PasswordInput;
import onlinestore.gui.sexcomponent.SexInput;
import onlinestore.gui.textfields.CustomIntegerTextField;
import onlinestore.gui.textfields.CustomTextField;
import onlinestore.gui.textfields.EmailTextField;
import onlinestore.gui.textfields.StreetNumberTextField;
import onlinestore.gui.textfields.UsernameTextField;
import onlinestore.network.EmailSender;

public class CreateNewAccountScene extends Scene implements InputValidator, InputPreparator<Customer>, CustomScene {

	// interaction with caller, in this case Main Menu
	private final MainMenuScene mainMenu;
	private final Stage window;

	// GUI
	private final GridPane addPersonLayout;

	private final Text createNewAccount = new Text("Create new account");
	private final Font headerFont = Font.font("Tahoma", FontWeight.NORMAL, 20);

	private final Label nickLabel = new Label("Username: ");
	private final Label passwordLabel = new Label("Password: ");
	private final Label repeatedPasswordLabel = new Label("Repeated password: ");
	private final Label firstNameLabel = new Label("First name: ");
	private final Label lastNameLabel = new Label("Last name: ");
	private final Label emailLabel = new Label("Email address: ");
	private final Label dateOfBirthLabel = new Label("Date of birth: ");
	private final Label sexLabel = new Label("Sex: ");
	private final Label countryLabel = new Label("Country: ");
	private final Label cityLabel = new Label("City: ");
	private final Label streetLabel = new Label("Street: ");
	private final Label streetNumberLabel = new Label("Street number: ");
	private final Label flatNumberLabel = new Label("Flat number: ");
	private final Text errorNotificationText = new Text();

	private final UsernameTextField usernameInput;
	private final PasswordInput passwordInput = new PasswordInput(10, this);
	private final CustomTextField firstNameInput = new CustomTextField("Name");
	private final CustomTextField lastNameInput = new CustomTextField("Last name");
	private final BirthdayInput dateOfBirthInput = new BirthdayInput(10);
	private final SexInput sexInput = new SexInput(10);
	private final CountryComboBox countryInput = new CountryComboBox();
	private final CityComboBox cityInput = new CityComboBox();
	private final CustomTextField streetInput = new CustomTextField("Street");
	private final StreetNumberTextField streetNumberInput = new StreetNumberTextField("Street number");
	private final CustomIntegerTextField flatNumberInput = new CustomIntegerTextField("Flat number");
	private final EmailTextField emailInput = new EmailTextField("Email address");
	private final ActionPanel buttonsPanel = new ActionPanel(10, "Submit", "Back");

	// interaction with the user
	private final ChangeListener<String> inputChangeListener;
	private final ChangeListener<Boolean> inputSelectionListener;
	private final CountriesCitiesSelector selector;
	private EmailSender sender;

	// further scenes
	private AuthenticationScene authScene = null;

	public CreateNewAccountScene(MainMenuScene mainMenu, GridPane layout, double width, double height) {
		super(layout, width, height);

		this.mainMenu = mainMenu;
		window = mainMenu.getWindowMenu();
		addPersonLayout = layout;
		selector = new CountriesCitiesSelector(mainMenu.getHibernator(), countryInput, cityInput);

		usernameInput = new UsernameTextField(mainMenu.getHibernator(), "Username");

		inputChangeListener = (val, oldVal, newVal) -> {
			if (isAnyInput())
				buttonsPanel.getActionButton().setDisable(false);
			else
				buttonsPanel.getActionButton().setDisable(true);
		};

		inputSelectionListener = (val, oldVal, newVal) -> {
			if (isAnyInput())
				buttonsPanel.getActionButton().setDisable(false);
			else
				buttonsPanel.getActionButton().setDisable(true);
		};

		adjustComponents();
	}

	@Override
	public Customer getPreparedInput() {
		Name name = new Name(firstNameInput.getPreparedInput(), lastNameInput.getPreparedInput());
		LocalDate dateOfBirth = dateOfBirthInput.getPreparedInput();
		Sex sex = sexInput.getPreparedInput();
		Country country = new Country(countryInput.getPreparedInput());
		City city = new City(cityInput.getPreparedInput(), country);
		Address address = new Address(streetInput.getPreparedInput(), streetNumberInput.getPreparedInput(),
				flatNumberInput.getPreparedInput());
		String email = emailInput.getPreparedInput();
		String username = usernameInput.getPreparedInput();
		String password = passwordInput.getPreparedInput();

		selector.process(country.getName(), city.getName());

		Customer customer = new Customer();
		customer.setName(name);
		customer.setBirthDate(dateOfBirth);
		customer.setSex(sex);
		customer.setCity(city);
		customer.setAddress(address);
		customer.setEmail(email);
		customer.setUsername(username);
		customer.setPassword(password);
		customer.setJoiningDate(LocalDate.now());

		return customer;
	}

	@Override
	public void setNeutralLook() {
		usernameInput.setNeutralLook();
		passwordInput.setNeutralLook();
		firstNameInput.setNeutralLook();
		lastNameInput.setNeutralLook();
		emailInput.setNeutralLook();
		dateOfBirthInput.setNeutralLook();
		sexInput.setNeutralLook();
		cityInput.setNeutralLook();
		streetInput.setNeutralLook();
		streetNumberInput.setNeutralLook();
		flatNumberInput.setNeutralLook();
		errorNotificationText.setText("");
	}

	@Override
	public boolean isAnyInput() {
		return usernameInput.isAnyInput() && passwordInput.isAnyInput() && firstNameInput.isAnyInput()
				&& lastNameInput.isAnyInput() && dateOfBirthInput.isAnyInput() && sexInput.isAnyInput()
				&& countryInput.isAnyInput() && cityInput.isAnyInput() && streetInput.isAnyInput()
				&& streetNumberInput.isAnyInput() && emailInput.isAnyInput();
	}

	@Override
	public boolean isValidInput() {
		return usernameInput.isValidInput() && passwordInput.isValidInput() && firstNameInput.isValidInput()
				&& lastNameInput.isValidInput() && dateOfBirthInput.isValidInput() && sexInput.isValidInput()
				&& countryInput.isValidInput() && cityInput.isValidInput() && streetInput.isValidInput()
				&& streetNumberInput.isValidInput()
				&& (flatNumberInput.isAnyInput() == false || flatNumberInput.isValidInput())
				&& emailInput.isValidInput();
	}

	@Override
	public void adjustComponents() {
		addPersonLayout.setPadding(new Insets(10, 10, 10, 10));
		addPersonLayout.setVgap(8);
		addPersonLayout.setHgap(10);
		addPersonLayout.setAlignment(Pos.CENTER);

		createNewAccount.setFont(headerFont);

		errorNotificationText.setFill(Color.FIREBRICK);

		addLabels();
		addListeners();
		addForms();
	}

	@Override
	public void addLabels() {
		addPersonLayout.add(createNewAccount, 0, 0, 2, 1);
		addPersonLayout.add(nickLabel, 0, 1);
		addPersonLayout.add(passwordLabel, 0, 2);
		addPersonLayout.add(repeatedPasswordLabel, 0, 3);
		addPersonLayout.add(firstNameLabel, 0, 4);
		addPersonLayout.add(lastNameLabel, 0, 5);
		addPersonLayout.add(emailLabel, 0, 6);
		addPersonLayout.add(dateOfBirthLabel, 0, 7);
		addPersonLayout.add(sexLabel, 0, 8);
		addPersonLayout.add(countryLabel, 0, 9);
		addPersonLayout.add(cityLabel, 0, 10);
		addPersonLayout.add(streetLabel, 0, 11);
		addPersonLayout.add(streetNumberLabel, 0, 12);
		addPersonLayout.add(flatNumberLabel, 0, 13);
		addPersonLayout.add(errorNotificationText, 0, 15, 2, 1);
	}

	@Override
	public void addForms() {
		addPersonLayout.add(usernameInput, 1, 1);
		addPersonLayout.add(passwordInput, 1, 2, 1, 2);
		addPersonLayout.add(firstNameInput, 1, 4);
		addPersonLayout.add(lastNameInput, 1, 5);
		addPersonLayout.add(emailInput, 1, 6);
		addPersonLayout.add(dateOfBirthInput, 1, 7);
		addPersonLayout.add(sexInput, 1, 8);
		addPersonLayout.add(countryInput, 1, 9);
		addPersonLayout.add(cityInput, 1, 10);
		addPersonLayout.add(streetInput, 1, 11);
		addPersonLayout.add(streetNumberInput, 1, 12);
		addPersonLayout.add(flatNumberInput, 1, 13);
		addPersonLayout.add(buttonsPanel, 1, 14);
	}

	@Override
	public void addListeners() {
		usernameInput.textProperty().addListener(inputChangeListener);
		passwordInput.setInputListener(inputChangeListener);
		firstNameInput.textProperty().addListener(inputChangeListener);
		lastNameInput.textProperty().addListener(inputChangeListener);
		emailInput.textProperty().addListener(inputChangeListener);
		sexInput.setSelectionListener(inputSelectionListener);
		countryInput.valueProperty().addListener(inputChangeListener);
		cityInput.valueProperty().addListener(inputChangeListener);
		streetInput.textProperty().addListener(inputChangeListener);
		streetNumberInput.textProperty().addListener(inputChangeListener);

		streetInput.removeEventFilter(KeyEvent.KEY_TYPED, streetInput.getEventFilter());
		streetInput.addEventFilter(KeyEvent.KEY_TYPED, e -> {
			String character = e.getCharacter();
			if (character.length() == 1) {
				char c = character.charAt(0);
				if (c < '0' && c != ' ')
					e.consume();
			}
		});

		buttonsPanel.getActionButton().setDisable(true);
		buttonsPanel.getActionButton().setOnAction(e -> {
			if (isValidInput()) {
				changeScene();
				sendMail(emailInput.getPreparedInput(), firstNameInput.getPreparedInput());
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid format of the input");
				alert.setHeaderText("INVALID FORMAT OF THE INPUT ");
				alert.setContentText("There has occured a problem with the format of data you have typed.");
				alert.showAndWait();
			}
		});

		buttonsPanel.getBackButton().setOnAction(e -> {
			window.setTitle("Login");
			window.setScene(mainMenu.getLoginScene());
			mainMenu.getLoginScene().setNeutralLook();
		});
	}

	private void changeScene() {
		if (authScene == null)
			authScene = new AuthenticationScene(mainMenu, new GridPane(), 900, 600);
		window.setTitle("Authentication");
		authScene.setNeutralLook();
		window.setScene(authScene);
	}

	public void sendMail(String recipient, String name) {
		PasswordAuthentication mailLogin = mainMenu.getLoginDetails().getMailLogin();

		if (sender == null)
			sender = new EmailSender();

		sender.setProgressOutput(authScene.getErrorText());

		Thread senderThread = new Thread(() -> {
			try {
				sender.send("online_store@o2.pl", recipient, name, mailLogin.getUserName(), mailLogin.getPassword());
			} catch (RuntimeException e) {
				e.printStackTrace();
				authScene.getErrorText()
						.setText("Make sure you have provided login and password as command-line arguments!");
			} catch (MessagingException e) {
				e.printStackTrace();
				authScene.getErrorText().setText("There has occured some problem while sending confirmation mail.");
			}
		});

		senderThread.setDaemon(true);
		senderThread.start();

	}

	public MainMenuScene getMainMenu() {
		return mainMenu;
	}

	public UsernameTextField getNickInput() {
		return usernameInput;
	}

	public PasswordInput getPasswordInput() {
		return passwordInput;
	}

	public CustomTextField getFirstNameInput() {
		return firstNameInput;
	}

	public CustomTextField getLastNameInput() {
		return lastNameInput;
	}

	public BirthdayInput getDateOfBirthInput() {
		return dateOfBirthInput;
	}

	public SexInput getSexInput() {
		return sexInput;
	}

	public CountryComboBox getCountryInput() {
		return countryInput;
	}

	public CityComboBox getCityInput() {
		return cityInput;
	}

	public CustomTextField getStreetInput() {
		return streetInput;
	}

	public StreetNumberTextField getStreetNumberInput() {
		return streetNumberInput;
	}

	public CustomIntegerTextField getFlatNumberInput() {
		return flatNumberInput;
	}

	public EmailTextField getEmailInput() {
		return emailInput;
	}

	public Text getErrorNotificationText() {
		return errorNotificationText;
	}

	public EmailSender getSender() {
		return sender;
	}

}
