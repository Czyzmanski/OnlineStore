package onlinestore.gui.panels;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ActionPanel extends HBox {
	
	private Button actionButton;
	private Button backButton;

	public ActionPanel(double spacing, String actionBtnText,  String backBtnText) {
		super(spacing);
		
		actionButton = new Button(actionBtnText);
		backButton = new Button(backBtnText);
		
		this.setAlignment(Pos.BOTTOM_RIGHT);
		this.getChildren().addAll(actionButton, backButton);
	}

	public Button getActionButton() {
		return actionButton;
	}

	public Button getBackButton() {
		return backButton;
	}
	

}
