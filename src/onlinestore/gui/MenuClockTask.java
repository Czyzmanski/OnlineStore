package onlinestore.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.Locale;

import javafx.concurrent.Task;

public class MenuClockTask extends Task<Void> {

	public MenuClockTask() {
	}

	@Override
	protected Void call() {
		while (isCancelled() == false) {
			DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
			LocalDateTime localTime = LocalDateTime.now();

			String day = localTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
			String date = localTime.format(formatter);

			updateMessage(day + " " + date);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				if (isCancelled()) {
					break;
				}
			}
		}

		return null;
	}

}
