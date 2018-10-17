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

public class YearChooserListener implements ChangeListener<Integer> {

	private final ComboBox<Integer> dayChooser;
	private final ComboBox<String> monthChooser;
	private final ObservableList<String> allMonths;

	public YearChooserListener(final ComboBox<Integer> dayChooser, final ComboBox<String> monthChooser,
			final ObservableList<String> allMonths) {
		this.dayChooser = dayChooser;
		this.monthChooser = monthChooser;
		this.allMonths = allMonths;
	}

	@Override
	public void changed(ObservableValue<? extends Integer> observable, Integer oldYear, Integer newYear) {
		LocalDate today = LocalDate.now();

		adjustDayChooser(today, BirthdayInput.FEBRUARY_29, observable, oldYear, newYear);
		adjustMonthChooser(today, BirthdayInput.FEBRUARY_29, observable, oldYear, newYear);
	}

	private void adjustDayChooser(LocalDate today, MonthDay February29, ObservableValue<? extends Integer> observable,
			Integer oldYear, Integer newYear) {

		/*
		 * Disable certain days with regard to newly selected year
		 * 
		 */

		dayChooser.setCellFactory(p -> {
			return new ListCell<Integer>() {

				@Override
				public void updateItem(Integer item, boolean empty) {
					super.updateItem(item, empty);

					if (item != null) {
						this.setText(item.toString());

						/*
						 * Disable certain days when month is selected
						 * 
						 */

						if (monthChooser.getValue() != null) {
							int numberOfMonth = allMonths.lastIndexOf(monthChooser.getValue()) + 1;
							boolean enable = true;

							/*
							 * Disable 29th of February if selected year is not leap year
							 * 
							 */

							if (February29.isValidYear(newYear) == false && numberOfMonth == 2 && item == 29) {
								this.setDisable(true);
								this.setTextFill(Color.LIGHTGRAY);
								enable = false;
							}

							/*
							 * Disable days following current day if selected month is current month and
							 * selected year is current year
							 * 
							 */

							if (newYear == today.getYear() && numberOfMonth == today.getMonthValue()
									&& item > today.getDayOfMonth()) {
								this.setDisable(true);
								this.setTextFill(Color.LIGHTGRAY);
								enable = false;
							}

							/*
							 * Disable months that are invalid for selected day, regardless of year
							 * 
							 */

							try {
								MonthDay.of(numberOfMonth, item);
							} catch (DateTimeException e) {
								this.setDisable(true);
								this.setTextFill(Color.LIGHTGRAY);
								enable = false;
							}

							if (enable) {
								this.setDisable(false);
								this.setTextFill(Color.BLACK);
							}

						}

					}
				}

			};
		});

	}

	private void adjustMonthChooser(LocalDate today, MonthDay February29, ObservableValue<? extends Integer> observable,
			Integer oldYear, Integer newYear) {

		/*
		 * Disable certain months with regard to newly selected year
		 * 
		 */

		monthChooser.setCellFactory(p -> {
			return new ListCell<String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (item != null) {
						this.setText(item);

						/*
						 * Disable certain months when day is selected
						 * 
						 */

						int numberOfMonth = allMonths.lastIndexOf(item) + 1;

						if (dayChooser.getValue() != null) {
							int numberOfDay = dayChooser.getValue();
							boolean enable = true;

							/*
							 * Disable February when selected day is 29 and selected year is not leap year
							 * 
							 */

							if (numberOfDay == 29 && February29.isValidYear(newYear) == false && numberOfMonth == 2) {
								this.setDisable(true);
								this.setTextFill(Color.LIGHTGRAY);
								enable = false;
							}

							/*
							 * Disable current month when selected year is current year and selected day is
							 * after today
							 * 
							 */

							if (newYear == today.getYear() && numberOfDay > today.getDayOfMonth()
									&& numberOfMonth == today.getMonthValue()) {
								this.setDisable(true);
								this.setTextFill(Color.LIGHTGRAY);
								enable = false;
							}

							/*
							 * Disable months that are invalid for selected day, regardless of year
							 * 
							 */

							try {
								MonthDay.of(numberOfMonth, numberOfDay);
							} catch (DateTimeException e) {
								this.setDisable(true);
								this.setTextFill(Color.LIGHTGRAY);
								enable = false;
							}

							/*
							 * If we are here, it means that month is valid one with respect to selected day
							 * 
							 */

							if (enable) {
								this.setDisable(false);
								this.setTextFill(Color.BLACK);
							}

						}

						/*
						 * Disable months following current month when selected year is current year,
						 * regardless of day selection
						 * 
						 */

						if (newYear == today.getYear() && numberOfMonth > today.getMonthValue()) {
							this.setDisable(true);
							this.setTextFill(Color.LIGHTGRAY);
						}

					}

				}
			};
		});

	}

}
