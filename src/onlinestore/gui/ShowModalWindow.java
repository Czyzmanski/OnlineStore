package onlinestore.gui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ShowModalWindow {

	private ShowModalWindow() {
	}
	
	public static Optional<ButtonType> showConfirmationAlert(String title, String msg) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(msg);
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		
		return alert.showAndWait();
	}

	public static Optional<ButtonType> showProblemWithConnectionAlert(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		e.printStackTrace(pw);

		TextArea textArea = new TextArea(sw.toString());
		textArea.setEditable(false);
		textArea.setWrapText(false);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);

		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane content = new GridPane();
		content.setMaxWidth(Double.MAX_VALUE);
		content.add(textArea, 0, 0);

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Problem with connecting to database");
		alert.setHeaderText("PROBLEM WITH CONNECTING TO DATABASE. \nCHECK YOUR NETWORK CONNECTION (REGARD FOR VPN)");
		alert.setContentText("There has occured a problem when connecting to database: \n");
		alert.getDialogPane().setExpandableContent(content);
		
		return alert.showAndWait();
	}

}
