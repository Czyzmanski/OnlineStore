package onlinestore.gui.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import onlinestore.datamodel.person.Customer;
import onlinestore.gui.MenuClockTask;
import onlinestore.gui.ShowModalWindow;
import onlinestore.gui.text.CustomText;
import onlinestore.network.Hibernator;

public class UserMenuScene extends Scene {

	// database communication
	private Hibernator hib;

	// logged user
	private Customer customer;

	// GUI
	private MainMenuScene mainMenu;
	private final Stage windowMenu;
	private final VBox layoutMenu;
	private final CustomText timeText = new CustomText(15);
	private final CustomText greetingText = new CustomText(15);
	private final Button goShoppingButton = new Button("Go shopping");
	private final Button detailsButton = new Button("My details");
	private final Button historyButton = new Button("History");
	private final Button logoutButton = new Button("Log out");

	// thread updating digital clock
	private Thread menuClock;
	private MenuClockTask menuClockTask;

	public UserMenuScene(MainMenuScene menu, Stage window, VBox layout, Customer cust) {
		super(layout, menu.getWidth(), menu.getHeight());

		mainMenu = menu;
		windowMenu = window;
		layoutMenu = layout;
		customer = cust;
		hib = mainMenu.getHibernator();

		launchDigitalClock();
		initMenuScene();
	}

	public void launchDigitalClock() {
		menuClockTask = new MenuClockTask();

		timeText.textProperty().unbind();
		timeText.textProperty().bind(menuClockTask.messageProperty());

		menuClock = new Thread(menuClockTask);
		menuClock.setDaemon(true);
		menuClock.setPriority(Thread.MAX_PRIORITY);
		menuClock.start();
	}

	private void initMenuScene() {
		goShoppingButton.setPrefSize(100, 20);
		detailsButton.setPrefSize(100, 20);
		historyButton.setPrefSize(100, 20);
		logoutButton.setPrefSize(100, 20);

		greetingText.setText(String.format("Hello, %s! Ready for some shopping?", customer.getName().getFirstName()));

		addListeners();

		layoutMenu.setAlignment(Pos.CENTER);
		layoutMenu.getChildren().addAll(timeText, greetingText, goShoppingButton, detailsButton, historyButton,
				logoutButton);

		windowMenu.setScene(this);
		windowMenu.setTitle(String.format("Logged in as: %s", customer.getName()));
	}

	private void addListeners() {
		logoutButton.setOnAction(e -> {
			ShowModalWindow.showConfirmationAlert("Log out", "Are you sure you want to log out?")
					.filter(response -> response == ButtonType.YES).ifPresent(response -> {
						mainMenu.launchDigitalClock();
						windowMenu.setScene(mainMenu);
						windowMenu.setTitle("Main Menu");
					});
		});
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
