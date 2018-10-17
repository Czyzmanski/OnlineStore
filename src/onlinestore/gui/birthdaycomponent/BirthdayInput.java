package onlinestore.gui.birthdaycomponent;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.format.TextStyle;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import onlinestore.gui.interfaces.InputPreparator;
import onlinestore.gui.interfaces.InputValidator;

public class BirthdayInput extends HBox implements InputValidator, InputPreparator<LocalDate> {
	
	public static final MonthDay FEBRUARY_29 = MonthDay.of(Month.FEBRUARY, 29);

	private final ComboBox<Integer> dayChooser;
	private final ComboBox<String> monthChooser;
	private final ComboBox<Integer> yearChooser;
	private final ObservableList<Integer> allDays;
	private final ObservableList<String> allMonths;
	private final ObservableList<Integer> allYears;

	public BirthdayInput(double spacing) {
		super(spacing);
		dayChooser = new ComboBox<>();
		monthChooser = new ComboBox<>();
		yearChooser = new ComboBox<>();

		allDays = FXCollections.observableArrayList();
		allMonths = FXCollections.observableArrayList();
		allYears = FXCollections.observableArrayList();

		adjustDayChooser();
		adjustMonthChooser();
		adjustYearChooser();
		populateLists();
		populateComboBoxes();

		getChildren().addAll(dayChooser, monthChooser, yearChooser);
	}

	@Override
	public boolean isAnyInput() {
		return dayChooser.getValue() != null && monthChooser.getValue() != null && yearChooser.getValue() != null;
	}

	@Override
	public boolean isValidInput() {
		return isAnyInput();
	}

	@Override
	public void setNeutralLook() {
		dayChooser.setValue(null);
		monthChooser.setValue(null);
		yearChooser.setValue(null);

		dayChooser.setStyle("");
		monthChooser.setStyle("");
		yearChooser.setStyle("");

		populateComboBoxes();
	}

	@Override
	public LocalDate getPreparedInput() {
		if (isValidInput())
			return LocalDate.of(yearChooser.getValue(), allMonths.lastIndexOf(monthChooser.getValue()) + 1,
					dayChooser.getValue());
		return null;
	}

	private void populateComboBoxes() {
		dayChooser.setItems(allDays);
		monthChooser.setItems(allMonths);
		yearChooser.setItems(allYears);
	}

	private void populateLists() {
		for (int i = 1; i <= 31; i++)
			allDays.add(i);

		Locale defaultLocale = Locale.getDefault();
		for (Month m : Month.values())
			allMonths.add(m.getDisplayName(TextStyle.FULL, defaultLocale));

		LocalDate localDate = LocalDate.now();
		for (int i = localDate.getYear(); i >= localDate.minusYears(120).getYear(); i--)
			allYears.add(i);
	}

	private void adjustDayChooser() {
		dayChooser.setPromptText("Day");
		dayChooser.valueProperty().addListener(new DayChooserListener(monthChooser, yearChooser, allMonths));
	}

	private void adjustMonthChooser() {
		monthChooser.setPromptText("Month");
		monthChooser.valueProperty().addListener(new MonthChooserListener(dayChooser, yearChooser, allMonths));
	}

	private void adjustYearChooser() {
		yearChooser.setPromptText("Year");
		yearChooser.valueProperty().addListener(new YearChooserListener(dayChooser, monthChooser, allMonths));
	}

}
