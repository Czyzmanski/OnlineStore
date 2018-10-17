package onlinestore.gui.birthdaycomponent;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.MonthDay;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;

public class MonthChooserListener implements ChangeListener<String> {

	private final ComboBox<Integer> dayChooser;
	private final ComboBox<Integer> yearChooser;
	private final ObservableList<String> allMonths;

	public MonthChooserListener(final ComboBox<Integer> dayChooser, final ComboBox<Integer> yearChooser,
			final ObservableList<String> allMonths) {
		this.dayChooser = dayChooser;
		this.yearChooser = yearChooser;
		this.allMonths = allMonths;
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldMonth, String newMonth) {
		int numberOfMonth = allMonths.lastIndexOf(newMonth) + 1;
		LocalDate today = LocalDate.now();

		adjustDayChooser(today, BirthdayInput.FEBRUARY_29, numberOfMonth, observable, oldMonth, newMonth);
		adjustYearChooser(today, BirthdayInput.FEBRUARY_29, numberOfMonth, observable, oldMonth, newMonth);
	}

	private void adjustDayChooser(LocalDate today, MonthDay February29, int numberOfMonth,
			ObservableValue<? extends String> observable, String oldMonth, String newMonth) {

		/*
		 * Disable certain days with regard to newly selected month
		 * 
		 */

		dayChooser.setCellFactory(p -> {
			return new ListCell<Integer>() {

				@Override
				public void updateItem(Integer item, boolean empty) {
					super.updateItem(item, empty);

					if (item != null) {
						this.setText(item.toString());
						boolean enable = true;

						/*
						 * Disable days invalid for selected month
						 * 
						 */

						try {
							MonthDay.of(numberOfMonth, item);
						} catch (DateTimeException e) {
							this.setDisable(true);
							this.setTextFill(Color.LIGHTGRAY);
							enable = false;
						}

						/*
						 * Disable certain days when year is selected
						 * 
						 */

						if (yearChooser.getValue() != null) {
							int selectedYear = yearChooser.getValue();

							/*
							 * Disable the 29th of February when selected year is not a leap year
							 * 
							 */

							if (numberOfMonth == 2 && item == 29 && February29.isValidYear(selectedYear) == false) {
								this.setDisable(true);
								this.setTextFill(Color.LIGHTGRAY);
								enable = false;
							}

							/*
							 * Disable days following current day when selected year is current year and
							 * selected month is current month
							 * 
							 */

							if (selectedYear == today.getYear() && numberOfMonth == today.getMonthValue()
									&& item > today.getDayOfMonth()) {
								this.setDisable(true);
								this.setTextFill(Color.LIGHTGRAY);
								enable = false;
							}

						}

						/*
						 * If we are here, it means that particular day is valid one with regard to
						 * selected month
						 * 
						 */

						if (enable) {
							this.setDisable(false);
							this.setTextFill(Color.BLACK);
						}

					}
				}
			};
		});

	}

	private void adjustYearChooser(LocalDate today, MonthDay February29, int numberOfMonth,
			ObservableValue<? extends String> observable, String oldMonth, String newMonth) {

		/*
		 * Disable certain years with regard to newly selected month
		 * 
		 */

		yearChooser.setCellFactory(p -> {
			return new ListCell<Integer>() {

				@Override
				public void updateItem(Integer item, boolean empty) {
					super.updateItem(item, empty);

					if (item != null) {
						this.setText(item.toString());
						boolean enable = true;

						/*
						 * Disable all but leap years if selected month is February and selected day is
						 * 29
						 * 
						 */

						if (numberOfMonth == 2 && dayChooser.getValue() != null && dayChooser.getValue() == 29
								&& February29.isValidYear(item) == false) {
							this.setDisable(true);
							this.setTextFill(Color.LIGHTGREY);
							enable = false;
						}

						/*
						 * Disable current year if selected month is one of the months following current
						 * year
						 * 
						 */

						if (item == today.getYear() && numberOfMonth > today.getMonthValue()) {
							this.setDisable(true);
							this.setTextFill(Color.LIGHTGREY);
							enable = false;
						}

						/*
						 * Disable current year if selected month is current month and selected day is
						 * one of the days following current day
						 * 
						 */

						if (item == today.getYear() && numberOfMonth == today.getMonthValue()
								&& dayChooser.getValue() != null && dayChooser.getValue() > today.getDayOfMonth()) {
							this.setDisable(true);
							this.setTextFill(Color.LIGHTGREY);
							enable = false;
						}

						/*
						 * If we are here, it means that particular year is valid one with respect to
						 * selected month
						 * 
						 */

						if (enable) {
							this.setDisable(false);
							this.setTextFill(Color.BLACK);
						}

					}

				}

			};
		});

	}

}
