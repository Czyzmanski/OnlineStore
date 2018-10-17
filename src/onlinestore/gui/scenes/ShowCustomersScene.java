package onlinestore.gui.scenes;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import onlinestore.datamodel.person.Customer;
import onlinestore.gui.panels.ActionPanel;
import onlinestore.gui.tableviews.CustomersTable;
import onlinestore.network.Hibernator;

public class ShowCustomersScene extends Scene {

	private MainMenuScene mainMenu;
	private Stage window;
	private VBox layout;
	
	// GUI
	private CustomersTable table;
	private ActionPanel actionPanel;
	
	private Hibernator hib;

	public ShowCustomersScene(MainMenuScene mainMenu, VBox layout, double width, double height) {
		super(layout, width, height);

		this.mainMenu = mainMenu;
		this.window = mainMenu.getWindowMenu();
		this.layout = layout;
		this.hib = mainMenu.getHibernator();
		
		actionPanel = new ActionPanel(10, "Show history", "Back");
		
		setContent();
		adjustComponents();
	}

	@SuppressWarnings("unchecked")
	public void setContent() {
		List<Customer> customers = hib.getAll(Customer.class);
		if(table != null) {
			table.setItems(FXCollections.observableArrayList(customers));
		} else {
			table = new CustomersTable(FXCollections.observableArrayList(customers));
		}
	}
	
	private void adjustComponents() {
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.getChildren().addAll(table, actionPanel);
		
		actionPanel.getActionButton().setDisable(true);
		
		addListeners();
	}
	
	private void addListeners() {
		table.getSelectionModel().selectedItemProperty().addListener((observ, oldSel, newSel) -> {
			Button showHistoryBtn = actionPanel.getActionButton();
			if(table.getSelectionModel().getSelectedItem() == null) {
				showHistoryBtn.setDisable(true);
			} else {
				showHistoryBtn.setDisable(false);
			}
		});
		
		actionPanel.getActionButton().setOnAction(e -> {
			// scene change, query to database retrieving all purchases of selected customer
		});
		
		actionPanel.getBackButton().setOnAction(e -> {
			mainMenu.launchDigitalClock();
			window.setTitle("Menu");
			window.setScene(mainMenu);
		});
	}

}
