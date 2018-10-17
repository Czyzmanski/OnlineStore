package onlinestore.gui.tableviews;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import onlinestore.datamodel.person.Customer;
import onlinestore.datamodel.person.Sex;

public class CustomersTable extends TableView<Customer> {

	private TableColumn<Customer, String> firstNameCol = new TableColumn<>("First name");
	private TableColumn<Customer, String> lastNameCol = new TableColumn<>("Last name");
	private TableColumn<Customer, Sex> sexCol = new TableColumn<>("Sex");
	private TableColumn<Customer, LocalDate> dateOfBirthCol = new TableColumn<>("Date of birth");
	private TableColumn<Customer, String> countryCol = new TableColumn<>("Country");
	private TableColumn<Customer, String> cityCol = new TableColumn<>("City");
	private TableColumn<Customer, String> addressCol = new TableColumn<>("Address");
	private TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
	private TableColumn<Customer, LocalDate> joiningDateCol = new TableColumn<>("Joined");
	private TableColumn<Customer, String> usernameCol = new TableColumn<>("Username");

	@SuppressWarnings("unchecked")
	public CustomersTable(ObservableList<Customer> dataList) {
		adjustColumns();

		getColumns().addAll(firstNameCol, lastNameCol, sexCol, dateOfBirthCol, countryCol, cityCol, addressCol,
				emailCol, joiningDateCol, usernameCol);
		setItems(dataList);
		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	private void adjustColumns() {
		firstNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName().getFirstName()));
		lastNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName().getLastName()));
		sexCol.setCellValueFactory(new PropertyValueFactory<>("sex"));
		dateOfBirthCol.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		countryCol.setCellValueFactory(
				param -> new SimpleStringProperty(param.getValue().getCity().getCountry().getName()));
		cityCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCity().getName()));
		addressCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAddress().toString()));
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		joiningDateCol.setCellValueFactory(new PropertyValueFactory<>("joiningDate"));
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

//		firstNameCol.setMinWidth(110);
//		lastNameCol.setMinWidth(110);
//		sexCol.setMinWidth(110);
//		dateOfBirthCol.setMinWidth(110);
//		addressCol.setMinWidth(290);

	}
}
