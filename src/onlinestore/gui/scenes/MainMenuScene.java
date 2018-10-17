package onlinestore.gui.scenes;

import java.util.concurrent.ExecutionException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import onlinestore.gui.MenuClockTask;
import onlinestore.gui.ShowModalWindow;
import onlinestore.gui.text.CustomText;
import onlinestore.network.ConnectTask;
import onlinestore.network.Hibernator;
import onlinestore.network.LoginDetails;

public class MainMenuScene extends Scene {

	public static MainMenuScene MENU = null;

	private LoginDetails loginDetails;

	// database connection
	private volatile Hibernator hibernator;
	private ConnectTask connect;
	private Thread makeConnection;

	// main menu scene
	private Stage windowMenu;
	private VBox layoutMenu;
	private CustomText timeText = new CustomText(15);
	private Button showPersonsButton = new Button("Show Persons");
	private Button addPersonButton = new Button("Log in");
	private CustomText connectionInfo = new CustomText(13);
	private ProgressIndicator connectionState = new ProgressIndicator();

	// thread updating digital clock
	private Thread menuClock;
	private MenuClockTask menuClockTask;

	// further scenes and stages
	private LoginScene loginScene;
	private ShowCustomersScene customersScene;

	public static MainMenuScene getInstance(LoginDetails loginDetails, Stage window, VBox layout, double width,
			double height) {

		if (MENU != null)
			return MENU;

		MENU = new MainMenuScene(loginDetails, window, layout, width, height);

		return MENU;
	}

	private MainMenuScene(LoginDetails login, Stage window, VBox layout, double width, double height) {
		super(layout, width, height);

		windowMenu = window;
		layoutMenu = layout;
		loginDetails = login;

		connect = new ConnectTask(loginDetails.getDatabaseLogin());
		makeConnection = new Thread(connect);
		makeConnection.setDaemon(true);
		makeConnection.start();

		launchDigitalClock();
		initMenuScene();

		new Thread(() -> {
			Thread.currentThread().setName("Initialize Hibernator Reference");
			try {
				hibernator = connect.get();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
		}).start();
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

	public void close() {
		if (hibernator != null) {
			hibernator.close();
		} else {
			new Thread(() -> {
				try {
					connect.setShowWindow(false);
					hibernator = connect.get();
					if (hibernator != null) {
						hibernator.close();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				}
			}).start();
		}

		menuClockTask.cancel();
	}

	private void initMenuScene() {
		adjustButtons();
		bindProperties();

		layoutMenu.setAlignment(Pos.CENTER);
		layoutMenu.getChildren().addAll(timeText, showPersonsButton, addPersonButton, connectionInfo, connectionState);

		windowMenu.setScene(this);
		windowMenu.setTitle("Menu");
		windowMenu.setOnCloseRequest(e -> {
			ShowModalWindow.showConfirmationAlert("Exit", "Are you sure you want to exit?").ifPresent(response -> {
				if (response == ButtonType.YES) {
					close();
					windowMenu.close();
				} else {
					e.consume();
				}
			});
		});
		windowMenu.show();
	}

	private void adjustButtons() {
		showPersonsButton.setPrefSize(100, 20);
		addPersonButton.setPrefSize(100, 20);

		showPersonsButton.setOnAction(e -> {
			if (hibernator != null) {
				if(customersScene == null) {
					customersScene = new ShowCustomersScene(this, new VBox(10), 900, 600);
				} else {
					customersScene.setContent();
				}
				
				windowMenu.setScene(customersScene);
				windowMenu.setTitle("Customers");
				
				connect.getConnectedProperty().set(false);
				menuClockTask.cancel();
			}
		});

		addPersonButton.setOnAction(e -> {
			if (hibernator != null) {
				if (loginScene == null)
					loginScene = new LoginScene(this, new GridPane(), 900, 600);

				loginScene.setNeutralLook();
				windowMenu.setScene(loginScene);
				windowMenu.setTitle("Login");

				connect.getConnectedProperty().set(false);
				menuClockTask.cancel();
			}
		});
	}

	private void bindProperties() {
		timeText.textProperty().bind(menuClockTask.messageProperty());

		connectionState.visibleProperty().bind(connect.getConnectedProperty());
		connectionState.progressProperty().bind(connect.progressProperty());

		connectionInfo.textProperty().bind(connect.messageProperty());
		connectionInfo.fillProperty().bind(connect.getColorProperty());
		connectionInfo.visibleProperty().bind(connect.getConnectedProperty());
	}

	public LoginDetails getLoginDetails() {
		return loginDetails;
	}

	public Stage getWindowMenu() {
		return windowMenu;
	}

	public Button getAddPersonButton() {
		return addPersonButton;
	}

	public Text getTimeAndHour() {
		return timeText;
	}

	public void setHibernator(Hibernator hibernator) {
		this.hibernator = hibernator;
	}

	public Hibernator getHibernator() {
		return hibernator;
	}

	public VBox getLayoutMenu() {
		return layoutMenu;
	}

	public Button getShowPersonsButton() {
		return showPersonsButton;
	}

	public MenuClockTask getMenuClockService() {
		return menuClockTask;
	}

	public void setMenuClockService(MenuClockTask menuClock) {
		this.menuClockTask = menuClock;
	}

	public LoginScene getLoginScene() {
		return loginScene;
	}

}
