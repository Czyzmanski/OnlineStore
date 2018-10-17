package onlinestore;

import java.util.List;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import onlinestore.gui.scenes.MainMenuScene;
import onlinestore.network.LoginDetails;

public class Main extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		List<String> listArgs = this.getParameters().getRaw();

		int size = listArgs.size();

		if (size != 4) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Inappropriate number of arguments");
			alert.setHeaderText("INAPPROPRIATE NUMBER OF ARGUMENTS");
			alert.setContentText("You have to provide 4 arguments for the program:\n1. Hibernate connection username;"
					+ "\n2. Hibernate connection password;\n3. Online store mail;\n4. Online store mail password.");
			alert.showAndWait();
			System.exit(-1);
		}

		String[] args = new String[size];

		for (int i = 0; i < size; i++)
			args[i] = listArgs.get(i);
		
		LoginDetails details = new LoginDetails(args);

		// returns Menu singleton, which initializes all GUI
		MainMenuScene.getInstance(details, primaryStage, new VBox(10), 900, 600);
	}

}
