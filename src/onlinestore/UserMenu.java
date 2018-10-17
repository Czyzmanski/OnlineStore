package onlinestore;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sun.mail.imap.Utility;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import onlinestore.datamodel.person.Person;

public class UserMenu {

	static enum Options {
		HIRING, SHOWING_VEHICLES
	}

	public static void displayUserMenu(final ObservableList<Person> allPersons, final ObservableList<Room> allRooms,
			final Stage callerWindow, final Scene callerScene, final String callerWindowTitle, final Person user,
			final Map<String, LinkedList<String>> typedCountriesCities) {

		Stage windowUserMenu = callerWindow;
		VBox layoutMenu = new VBox(10);
		Scene sceneMenu = new Scene(layoutMenu, 640, 480);
		Text greetingText = new Text(String.format("What do you wish to do, %s?", user.name));
		Button showRooms = new Button("Show my rooms");
		Button hireRooms = new Button("Hire new room");
		Button showDetails = new Button("Show my profile's details");
		Button logout = new Button("Logout");
		Button deleteAccount = new Button("Delete my account");
		String title = String.format("Loged in as: %s %s", user.name, user.lastName);

		greetingText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));

		showRooms.setPrefSize(170, 20);
		hireRooms.setPrefSize(170, 20);
		showDetails.setPrefSize(170, 20);
		logout.setPrefSize(170, 20);
		deleteAccount.setPrefSize(170, 20);

		showRooms.setOnAction(e -> displayRooms(FXCollections.observableArrayList(user.rooms), windowUserMenu,
				sceneMenu, title, user, Options.SHOWING_VEHICLES));
		hireRooms.setOnAction(e -> displayRooms(
				FXCollections.observableArrayList(
						allRooms.stream().filter(room -> room.occupier == null).collect(Collectors.toList())),
				windowUserMenu, sceneMenu, title, user, Options.HIRING));
		showDetails.setOnAction(e -> displayUserDetails(windowUserMenu, sceneMenu, title, user, typedCountriesCities));
		logout.setOnAction(e -> {
			windowUserMenu.setScene(callerScene);
			windowUserMenu.setTitle(callerWindowTitle);
		});
		deleteAccount.setOnAction(e -> deleteAccount(callerWindow, callerScene, user, allPersons));
		
		if(user.dateOfBirth.equals(LocalDate.now())) {
			Text happyBirthday = new Text("Happy Birthday, " + user.name + "!");
			happyBirthday.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
			layoutMenu.getChildren().add(happyBirthday);
		}

		layoutMenu.setAlignment(Pos.CENTER);
		layoutMenu.getChildren().addAll(greetingText, showRooms, hireRooms, showDetails, logout, deleteAccount);

		windowUserMenu.setScene(sceneMenu);
		windowUserMenu.setTitle(title);
	}

	private static void displayUserDetails(final Stage callerWindow, final Scene callerScene, final String callerTitle,
			final Person user, final HashMap<String, LinkedList<String>> typedCountriesCities) {

		GridPane showDetailsLayout = new GridPane();
		Scene showDetailsScene = new Scene(showDetailsLayout);
		Label nameInfoLabel = new Label("Name: ");
		Label lastNameInfoLabel = new Label("Last name: ");
		Label dateOfBirthInfoLabel = new Label("Date of birth: ");
		Label addressInfoLabel = new Label("Address: ");
		Label dateOfJoiningInfoLabel = new Label("Joined: ");
		Label numberOfHiredRoomsInfoLabel = new Label("Number of hired rooms: ");
		Label numberOfVehiclesInfoLabel = new Label("Number of your vehicles: ");
		Label availableSpaceInfoLabel = new Label("Available space: ");
		Label occupiedSpaceInfoLabel = new Label("Occupied space: ");
		Label nameLabel = new Label(user.name);
		Label lastNameLabel = new Label(user.lastName);
		Label dateOfBirthLabel = new Label(user.dateOfBirth.toString());
		Label addressLabel = new Label(user.getAddress());
		Label dateOfJoiningLabel = new Label(
				user.dateOfJoining != null ? user.dateOfJoining.toString() : "No available");
		Label numberOfHiredRoomsLabel = new Label(String.valueOf(user.rooms.size()));
		Label numberOfVehiclesLabel = new Label();
		Label availableSpaceLabel = new Label();
		Label occupiedSpaceLabel = new Label();
		Button backButton = new Button("Back");
		Button editAddress = new Button("Edit my address");
		HBox buttonsLayout = new HBox(10, editAddress, backButton);

		Integer numberOfVehicles = user.rooms.stream().map(room -> room.vehicles.size()).reduce(0,
				(acc, elem) -> acc + elem);
		numberOfVehiclesLabel.setText(numberOfVehicles.toString());

		Double availableSpace = user.rooms.stream().map(room -> room.space - room.getOccupiedSpace()).reduce(0.,
				(acc, elem) -> acc + elem);
		availableSpaceLabel.setText(String.format("%.2f", availableSpace));

		Double occupiedSpace = user.rooms.stream().map(room -> room.getOccupiedSpace()).reduce(0.,
				(acc, elem) -> acc + elem);
		occupiedSpaceLabel.setText(String.format("%.2f", occupiedSpace));

		showDetailsLayout.setPadding(new Insets(10, 10, 10, 10));
		showDetailsLayout.setVgap(8);
		showDetailsLayout.setHgap(10);
		showDetailsLayout.setAlignment(Pos.CENTER);

		showDetailsLayout.add(nameInfoLabel, 0, 0);
		showDetailsLayout.add(lastNameInfoLabel, 0, 1);
		showDetailsLayout.add(dateOfBirthInfoLabel, 0, 2);
		showDetailsLayout.add(addressInfoLabel, 0, 3);
		showDetailsLayout.add(dateOfJoiningInfoLabel, 0, 4);
		showDetailsLayout.add(numberOfHiredRoomsInfoLabel, 0, 5);
		showDetailsLayout.add(numberOfVehiclesInfoLabel, 0, 6);
		showDetailsLayout.add(availableSpaceInfoLabel, 0, 7);
		showDetailsLayout.add(occupiedSpaceInfoLabel, 0, 8);
		showDetailsLayout.add(nameLabel, 1, 0);
		showDetailsLayout.add(lastNameLabel, 1, 1);
		showDetailsLayout.add(dateOfBirthLabel, 1, 2);
		showDetailsLayout.add(addressLabel, 1, 3);
		showDetailsLayout.add(dateOfJoiningLabel, 1, 4);
		showDetailsLayout.add(numberOfHiredRoomsLabel, 1, 5);
		showDetailsLayout.add(numberOfVehiclesLabel, 1, 6);
		showDetailsLayout.add(availableSpaceLabel, 1, 7);
		showDetailsLayout.add(occupiedSpaceLabel, 1, 8);
		showDetailsLayout.add(buttonsLayout, 1, 9);

		backButton.setMinWidth(50);
		backButton.setOnAction(e -> callerWindow.setScene(callerScene));

		buttonsLayout.setAlignment(Pos.BOTTOM_RIGHT);

		editAddress.setOnAction(e -> displayAddressEditor(callerScene, callerWindow, showDetailsScene, callerTitle,
				user, typedCountriesCities));

		callerWindow.setScene(showDetailsScene);
	}

	private static void displayAddressEditor(final Scene preCallerScene, final Stage callerWindow,
			final Scene callerScene, final String callerTitle, final Person user,
			final HashMap<String, LinkedList<String>> typedCountriesCities) {

		GridPane editAddressLayout = new GridPane();
		Scene editAddressScene = new Scene(editAddressLayout, 640, 480);
		Label countryLabel = new Label("New country: ");
		Label cityLabel = new Label("New city: ");
		Label streetLabel = new Label("New street: ");
		Label streetNumberLabel = new Label("New street number: ");
		Label flatNumberLabel = new Label("New flat number: ");
		ComboBox<String> countryInput = new ComboBox<>();
		ComboBox<String> cityInput = new ComboBox<>();
		TextField streetInput = new TextField();
		TextField streetNumberInput = new TextField();
		TextField flatNumberInput = new TextField();
		Button submit = new Button("Submit");
		Button back = new Button("Back");
		HBox buttonsPanel = new HBox(10);

		countryInput.setEditable(false);
		countryInput.setPromptText("Choose new country");
		Utility.initCountries(countryInput);
		cityInput.setEditable(true);
		cityInput.setPromptText("New city");
		streetInput.setPromptText("Typed in new street");
		streetNumberInput.setPromptText("Typed in new street number");
		flatNumberInput.setPromptText("Typed in new flat number");

		countryInput.setOnMouseClicked(e -> countryInput.setStyle(""));
		cityInput.setOnMouseClicked(e -> cityInput.setStyle(""));
		streetInput.setOnMouseClicked(e -> streetInput.setStyle(""));
		streetNumberInput.setOnMouseClicked(e -> streetNumberInput.setStyle(""));
		flatNumberInput.setOnMouseClicked(e -> flatNumberInput.setStyle(""));

		ChangeListener<String> inputListener = (val, oldVal, newVal) -> {
			if (Utility.isNotEnoughChangingDetailsInput(countryInput, cityInput, streetInput, streetNumberInput,
					flatNumberInput) == false)
				submit.setDisable(false);
			else
				submit.setDisable(true);
		};

		countryInput.valueProperty().addListener(inputListener);
		cityInput.valueProperty().addListener(inputListener);
		streetInput.textProperty().addListener(inputListener);
		streetNumberInput.textProperty().addListener(inputListener);
		flatNumberInput.textProperty().addListener(inputListener);

		LinkedList<String> defaultCountryCities = typedCountriesCities
				.get(Locale.getDefault().getDisplayCountry().toUpperCase());
		if (defaultCountryCities != null) {
			cityInput.setItems(FXCollections.observableArrayList(defaultCountryCities));
		}

		countryInput.valueProperty().addListener((val, oldVal, newVal) -> {
			newVal = newVal.toUpperCase();
			if (typedCountriesCities.containsKey(newVal)) {
				cityInput.setItems(FXCollections.observableArrayList(typedCountriesCities.get(newVal)));
			} else {
				typedCountriesCities.put(newVal, new LinkedList<>());
				cityInput.setItems(FXCollections.observableArrayList());
			}
		});

		submit.setDisable(true);
		submit.setOnAction(e -> {
			String res;
			boolean isError = false;

			res = countryInput.getValue();
			if (res != null && res.trim().isEmpty() == false)
				user.country = res.toUpperCase();

			res = cityInput.getValue();
			if (res != null && res.trim().isEmpty() == false)
				user.city = res.toUpperCase();

			res = streetInput.getText();
			if (res != null && res.trim().isEmpty() == false)
				user.street = res.toUpperCase();

			res = streetNumberInput.getText();
			if (res != null && res.trim().isEmpty() == false && Utility.isErrorStreetNumber(res) == false)
				user.streetNumber = res;
			else if (res != null && res.trim().isEmpty() == false) {
				Utility.isError(streetNumberInput, res);
				isError = true;
			}

			res = flatNumberInput.getText();
			if (res != null && res.trim().isEmpty() == false) {
				try {
					Integer.parseInt(res);
					if (isError == false) {
						String[] tocens = user.streetNumber.split("/");
						user.streetNumber = tocens[0] + "/" + res;
					}
				} catch (NumberFormatException ex) {
					isError = true;
					flatNumberInput.setStyle("-fx-background-color: FFE5E5;");
				}
			}

			if (isError == true)
				Utility.showErrorDialog();

			Utility.selectAllInput(streetInput, streetNumberInput, flatNumberInput);
			countryInput.setStyle("");
			cityInput.setStyle("");
			streetInput.setStyle("");
			streetNumberInput.setStyle("");
			flatNumberInput.setStyle("");
		});

		back.setOnAction(
				e -> displayUserDetails(callerWindow, preCallerScene, callerTitle, user, typedCountriesCities));
		back.setMinWidth(50);

		buttonsPanel.setAlignment(Pos.BOTTOM_RIGHT);
		buttonsPanel.getChildren().addAll(submit, back);

		editAddressLayout.add(countryLabel, 0, 0);
		editAddressLayout.add(cityLabel, 0, 1);
		editAddressLayout.add(streetLabel, 0, 2);
		editAddressLayout.add(streetNumberLabel, 0, 3);
		editAddressLayout.add(flatNumberLabel, 0, 4);
		editAddressLayout.add(countryInput, 1, 0);
		editAddressLayout.add(cityInput, 1, 1);
		editAddressLayout.add(streetInput, 1, 2);
		editAddressLayout.add(streetNumberInput, 1, 3);
		editAddressLayout.add(flatNumberInput, 1, 4);
		editAddressLayout.add(buttonsPanel, 1, 5);

		editAddressLayout.setPadding(new Insets(10, 10, 10, 10));
		editAddressLayout.setVgap(8);
		editAddressLayout.setHgap(10);
		editAddressLayout.setAlignment(Pos.CENTER);

		callerWindow.setScene(editAddressScene);
	}

	private static void displayRooms(final ObservableList<Room> rooms, final Stage callerWindow,
			final Scene callerScene, final String callerTitle, final Person user, final Options option) {

		VBox showRoomsLayout = new VBox(10);
		Scene showRoomsScene = new Scene(showRoomsLayout);
		Text occupierText = new Text(option == Options.SHOWING_VEHICLES ? "Your rooms" : "Rooms you can hire");
		TableView<Room> roomsTable = new TableView<>();
		TableColumn<Room, Integer> ordinalCol = new TableColumn<>("Ordinal");
		TableColumn<Room, Double> spaceCol = new TableColumn<>("Space");
		TableColumn<Room, Double> occupiedSpaceCol = new TableColumn<>("Occupied space");
		TableColumn<Room, LocalDate> hiredCol = new TableColumn<>("Hired");
		Button back = new Button("Back");
		Button showVehiclesOrHireRoom = new Button(option == Options.SHOWING_VEHICLES ? "Show vehicles" : "Hire");
		Button deleteRoom = new Button("Resign from hiring selected room");
		HBox upLayout = new HBox(back);

		ordinalCol.setCellValueFactory(
				col -> new ReadOnlyObjectWrapper<>(roomsTable.getItems().indexOf(col.getValue()) + 1));
		spaceCol.setCellValueFactory(new PropertyValueFactory<>("space"));
		occupiedSpaceCol.setCellValueFactory(new PropertyValueFactory<>("occupiedSpace"));
		hiredCol.setCellValueFactory(new PropertyValueFactory<>("dateOfHirng"));

		ordinalCol.setMinWidth(110);
		spaceCol.setMinWidth(110);
		occupiedSpaceCol.setMinWidth(170);
		hiredCol.setMinWidth(170);

		roomsTable.getColumns().addAll(ordinalCol, spaceCol, occupiedSpaceCol, hiredCol);
		roomsTable.setItems(rooms);
		roomsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		roomsTable.getSelectionModel().selectedItemProperty().addListener(e -> {
			Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
			if (selectedRoom != null) {
				showVehiclesOrHireRoom.setDisable(false);
				deleteRoom.setDisable(false);
			} else {
				showVehiclesOrHireRoom.setDisable(true);
				deleteRoom.setDisable(false);
			}
		});
		roomsTable.requestFocus();

		back.setOnAction(e -> callerWindow.setScene(callerScene));
		back.setMinWidth(50);
		showVehiclesOrHireRoom.setDisable(true);
		showVehiclesOrHireRoom.setOnAction(e -> {
			Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
			if (option == Options.SHOWING_VEHICLES) {
				displayVehicles(callerWindow, callerScene, callerTitle, selectedRoom);
			} else {
				selectedRoom.occupier = user;
				user.rooms.add(selectedRoom);
				selectedRoom.dateOfHiring = LocalDate.now();
				rooms.remove(selectedRoom);
				Utility.showAlert(AlertType.INFORMATION, "Congratulations!",
						"Congratulations " + user.name + " " + user.lastName + "!",
						"You have successfuly hired designated room!");
			}
		});
		showVehiclesOrHireRoom.setMinWidth(130);

		deleteRoom.setMinWidth(250);
		deleteRoom.setDisable(true);
		deleteRoom.setOnAction(e -> {
			Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
			user.rooms.remove(selectedRoom);
			rooms.remove(selectedRoom);
			selectedRoom.occupier = null;
			selectedRoom.removeAllVehicles();
			selectedRoom.dateOfHiring = null;
		});

		upLayout.setAlignment(Pos.BOTTOM_LEFT);

		occupierText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));

		showRoomsLayout.setAlignment(Pos.CENTER);
		showRoomsLayout.setPadding(new Insets(20, 20, 20, 20));
		showRoomsLayout.getChildren().addAll(upLayout, occupierText, roomsTable, showVehiclesOrHireRoom);

		if (option == Options.SHOWING_VEHICLES)
			showRoomsLayout.getChildren().add(deleteRoom);

		callerWindow.setScene(showRoomsScene);
	}

	private static void displayVehicles(final Stage callerWindow, final Scene callerScene, final String callerTitle,
			final Room room) {

		ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(room.vehicles);
		VBox showVehiclesLayout = new VBox(10);
		Scene showVehiclesScene = new Scene(showVehiclesLayout);
		Text roomText = new Text(room.toString());
		TableView<Vehicle> vehiclesTable = new TableView<>();
		TableColumn<Vehicle, Integer> ordinalCol = new TableColumn<>("Ordinal");
		TableColumn<Vehicle, String> nameCol = new TableColumn<>("Name");
		TableColumn<Vehicle, Double> spaceCol = new TableColumn<>("Space");
		Button back = new Button("Back");
		Button addNewVehicle = new Button("Add new vehicle");
		Button removeVehicle = new Button("Remove selected vehicle");
		HBox upLayout = new HBox(10);
		HBox downLayout = new HBox(15);
		Label nameLabel = new Label("Name: ");
		Label spaceLabel = new Label("Space: ");
		TextField nameInput = new TextField();
		TextField spaceInput = new TextField();

		ordinalCol.setCellValueFactory(
				col -> new ReadOnlyObjectWrapper<>(vehiclesTable.getItems().indexOf(col.getValue()) + 1));
		spaceCol.setCellValueFactory(new PropertyValueFactory<>("space"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		ordinalCol.setMinWidth(110);
		spaceCol.setMinWidth(110);
		nameCol.setMinWidth(170);

		vehiclesTable.getColumns().addAll(ordinalCol, nameCol, spaceCol);
		vehiclesTable.setItems(vehicles);
		vehiclesTable.getSelectionModel().selectedItemProperty().addListener(e -> {
			Vehicle selectedVehicle = vehiclesTable.getSelectionModel().getSelectedItem();
			if (selectedVehicle != null)
				removeVehicle.setDisable(false);
			else
				removeVehicle.setDisable(true);
		});
		vehiclesTable.requestFocus();

		back.setOnAction(e -> callerWindow.setScene(callerScene));
		back.setMinWidth(50);
		upLayout.getChildren().addAll(back);

		addNewVehicle.setMinWidth(110);
		addNewVehicle.setOnAction(e -> {
			boolean isError = false;

			if (Utility.isError(nameInput, nameInput.getText()))
				isError = true;
			if (Utility.isMeasurementError(spaceInput, spaceInput.getText()))
				isError = true;

			if (isError == true) {
				Utility.showErrorDialog();
				return;
			}

			Vehicle newVehicle = new Vehicle(nameInput.getText().trim().toUpperCase(),
					new Double(spaceInput.getText()));
			try {
				room.addVehicle(newVehicle);
				vehiclesTable.getItems().add(newVehicle);
				roomText.setText(room.toString());
			} catch (OverflowException e1) {
				Utility.showAlert(AlertType.ERROR, "No room in the room!", "No room in the room!",
						"You cannot add that vehicle because there is no space for it! Make some room!");
			} finally {
				Utility.selectAllInput(nameInput, spaceInput);
			}
		});

		removeVehicle.setMinWidth(250);
		removeVehicle.setDisable(true);
		removeVehicle.setOnAction(e -> {
			Vehicle selectedVehicle = vehiclesTable.getSelectionModel().getSelectedItem();
			room.removeVehicle(selectedVehicle);
			vehicles.remove(selectedVehicle);
		});

		nameInput.setPromptText("Name of the vehicle");
		spaceInput.setPromptText("Space of the vehicle");

		nameInput.setOnMouseClicked(e -> nameInput.setStyle(""));
		spaceInput.setOnMouseClicked(e -> spaceInput.setStyle(""));

		downLayout.getChildren().addAll(nameLabel, nameInput, spaceLabel, spaceInput, addNewVehicle);

		roomText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));

		showVehiclesLayout.setAlignment(Pos.CENTER);
		showVehiclesLayout.setPadding(new Insets(20, 20, 20, 20));
		showVehiclesLayout.getChildren().addAll(upLayout, roomText, vehiclesTable, downLayout, removeVehicle);

		callerWindow.setScene(showVehiclesScene);
	}

	private static void deleteAccount(final Stage callerWindow, final Scene callerScene, final Person user,
			final ObservableList<Person> allPersons) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Important decision");
		alert.setHeaderText("Are you sure you want to do this?");
		alert.setContentText(
				"This decision is irreversible. You will not be able to restore your account if you click left button.");
		ButtonType yes = new ButtonType("Yes");
		ButtonType no = new ButtonType("No");
		ButtonType back = new ButtonType("Back", ButtonBar.ButtonData.BACK_PREVIOUS);
		alert.getButtonTypes().setAll(yes, no, back);

		Optional<ButtonType> optional = alert.showAndWait();
		if (optional.isPresent() && optional.get() == yes) {
			user.rooms.stream().forEach(room -> {
				room.occupier = null;
				room.removeAllVehicles();
			});
			user.rooms.clear();
			allPersons.remove(user);
			callerWindow.setScene(callerScene);
		} else if (optional.isPresent() && optional.get() == no) {
			alert.close();
		}
	}

}
