package onlinestore.gui.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import onlinestore.gui.interfaces.CustomScene;
import onlinestore.gui.panels.ActionPanel;
import onlinestore.gui.textfields.CodeTextField;
import onlinestore.network.Hibernator;

public class AuthenticationScene extends Scene implements CustomScene {
	
	// interaction with database
	private Hibernator hib;

	// interaction with caller, in this case Main Menu
	private MainMenuScene mainMenu;
	private Stage window;

	// GUI
	private GridPane authLayout;

	private Text errorText = new Text();
	private Text thanks = new Text("Thank you for your registration!");
	private Text info = new Text("The email with 6-figure confirmation code has just been sent to you!");
	private Font font = Font.font("Tahoma", FontWeight.NORMAL, 16);
	private Font errorFont = Font.font("Tahoma", FontWeight.NORMAL, 16);

	private Label codeLabel = new Label("6-figure code: ");
	private CodeTextField codeInput = new CodeTextField("6-figure code");

	private Hyperlink resend = new Hyperlink("Haven't received the email? Click here to resend!");
	private ActionPanel action = new ActionPanel(10, "Submit", "Back");

	// interaction with the calling scene
	private CreateNewAccountScene previousScene;

	public AuthenticationScene(MainMenuScene mainMenu, GridPane layout, double width, double height) {
		super(layout, width, height);

		this.mainMenu = mainMenu;
		window = mainMenu.getWindowMenu();
		authLayout = layout;
		previousScene = mainMenu.getLoginScene().getNewAccountScene();
		
		hib = mainMenu.getHibernator();

		adjustComponents();
	}

	@Override
	public void adjustComponents() {
		authLayout.setPadding(new Insets(10, 10, 10, 10));
		authLayout.setVgap(8);
		authLayout.setHgap(10);
		authLayout.setAlignment(Pos.CENTER);

		thanks.setFont(font);
		info.setFont(font);

		errorText.setFont(errorFont);
		errorText.setFill(Color.FIREBRICK);

		addLabels();
		addForms();
		addListeners();
	}

	@Override
	public void addLabels() {
		authLayout.add(thanks, 0, 0, 2, 1);
		authLayout.add(info, 0, 1, 2, 1);
		authLayout.add(codeLabel, 0, 2);

		authLayout.add(errorText, 0, 4, 2, 1);
	}

	@Override
	public void addForms() {
		authLayout.add(codeInput, 1, 2);

		authLayout.add(resend, 0, 7, 2, 1);
		authLayout.add(action, 2, 8);

	}

	@Override
	public void addListeners() {
		resend.setOnAction(e -> previousScene.sendMail(previousScene.getEmailInput().getPreparedInput(),
				previousScene.getFirstNameInput().getPreparedInput()));

		action.getActionButton().textProperty().addListener((observable, oldCode, newCode) -> {
			if (codeInput.isAnyInput())
				action.getActionButton().setDisable(false);
			else
				action.getActionButton().setDisable(true);
		});

		action.getActionButton().setOnAction(e -> {
			if (codeInput.getText().equals(previousScene.getSender().getLastCode())) {
				hib.save(previousScene.getPreparedInput());
				
				window.setTitle("Log in");
				window.setScene(mainMenu.getLoginScene());
			} else {
				codeInput.setStyle("-fx-background-color: FFE5E5;");
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Incorrect code");
				alert.setHeaderText("INCORRECT CODE");
				alert.setContentText("You have typed incorrect code. Please try again.");
				alert.showAndWait();
			}
		});

		action.getBackButton().setOnAction(e -> {
			window.setTitle("Create new account");
			window.setScene(mainMenu.getLoginScene().getNewAccountScene());
		});
	}

	@Override
	public void setNeutralLook() {
		codeInput.setStyle("");
		errorText.setText("");
		errorText.setFill(Color.FIREBRICK);
		resend.setVisited(false);
	}

	public Text getErrorText() {
		return errorText;
	}

}
