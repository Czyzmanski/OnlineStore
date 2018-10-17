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

public class DayChooserListener implements ChangeListener<Integer> {

	private final ComboBox<String> monthChooser;
	private final ComboBox<Integer> yearChooser;
	private final ObservableList<String> allMonths;

	public DayChooserListener(final ComboBox<String> monthChooser, final ComboBox<Integer> yearChooser,
			final ObservableList<String> allMonths) {
		this.monthChooser = monthChooser;
		this.yearChooser = yearChooser;
		this.allMonths = allMonths;
	}

	@Override
	public void changed(ObservableValue<? extends Integer> val, Integer oldDay, Integer newDay) {
		LocalDate today = LocalDate.now();

		adjustMonthChooser(today, BirthdayInput.FEBRUARY_29, val, oldDay, newDay);
		adjustYearChooser(today, BirthdayInput.FEBRUARY_29, val, oldDay, newDay);
	}

	private void adjustMonthChooser(LocalDate today, MonthDay February29, ObservableValue<? extends Integer> val,
			Integer oldDay, Integer newDay) {

		/*
		 * Disable certain months with regard to selected day
		 * 
		 */

		monthChooser.setCellFactory(p -> {
			return new ListCell<String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (item != null) {
						this.setText(item);
						int numberOfMonth = allMonths.lastIndexOf(item) + 1;
						boolean enable = true;

						/*
						 * Disable current month if selected day is one of the days following current
						 * day and selected year is current year
						 * 
						 */

						if (yearChooser.getValue() != null && yearChooser.getValue() == today.getYear()
								&& numberOfMonth == today.getMonthValue() && newDay > today.getDayOfMonth()) {
							this.setDisable(true);
							this.setTextFill(Color.LIGHTGRAY);
							enable = false;
						}

						/*
						 * Disable months that follow current month if selected year is current year,
						 * regardless of selected day
						 * 
						 */

						if (yearChooser.getValue() != null && yearChooser.getValue() == today.getYear()
								&& numberOfMonth > today.getMonthValue()) {
							this.setDisable(true);
							this.setTextFill(Color.LIGHTGRAY);
							enable = false;
						}

						/*
						 * Disable months for whom selected day is invalid
						 * 
						 */

						try {
							MonthDay.of(numberOfMonth, newDay);
						} catch (DateTimeException e) {
							this.setDisable(true);
							this.setTextFill(Color.LIGHTGRAY);
							enable = false;
						}

						/*
						 * Disable February if selected day is 29 and selected year is not a leap year
						 * 
						 */

						if (newDay == 29 && yearChooser.getValue() != null
								&& February29.isValidYear(yearChooser.getValue()) == false) {
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

			};
		});

	}

	private void adjustYearChooser(LocalDate today, MonthDay February29, ObservableValue<? extends Integer> val,
			Integer oldDay, Integer newDay) {

		/*
		 * Disable particular years with regard to selected day and selected month
		 * 
		 */

		yearChooser.setCellFactory(p -> {
			return new ListCell<Integer>() {

				@Override
				public void updateItem(Integer item, boolean empty) {
					super.updateItem(item, empty);

					if (item != null) {
						this.setText(item.toString());

						/*
						 * If and only if month is selected, it is possible to disable certain years
						 * 
						 */

						if (monthChooser.getValue() != null) {
							int numberOfMonth = allMonths.lastIndexOf(monthChooser.getValue()) + 1;
							boolean enable = true;

							/*
							 * Disable all but leap years if newly selected day is 29 and selected month is
							 * February
							 * 
							 */

							if (newDay == 29 && numberOfMonth == 2 && February29.isValidYear(item) == false) {
								this.setDisable(true);
								this.setTextFill(Color.LIGHTGRAY);
								enable = false;
							}

							/*
							 * Disable current year if selected month is current month and newly selected
							 * day is greater than current day
							 * 
							 */

							if (item == today.getYear() && numberOfMonth == today.getMonthValue()
									&& newDay > today.getDayOfMonth()) {
								this.setDisable(true);
								this.setTextFill(Color.LIGHTGRAY);
								enable = false;
							}

							/*
							 * Disable current year if selected month follows current month, value of newly
							 * selected day doesn't matter
							 * 
							 */

							if (item == today.getYear() && numberOfMonth > today.getMonthValue()) {
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

}
