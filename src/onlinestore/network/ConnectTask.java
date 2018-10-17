package onlinestore.network;

import javax.mail.PasswordAuthentication;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import onlinestore.gui.ShowModalWindow;

public class ConnectTask extends Task<Hibernator> {

	private boolean showWindow = true;

	private SimpleBooleanProperty connectedProperty;
	private SimpleObjectProperty<Paint> colorProperty;

	private PasswordAuthentication databaseLogin;

	public ConnectTask(PasswordAuthentication databaseLogin) {
		this.databaseLogin = databaseLogin;
		this.connectedProperty = new SimpleBooleanProperty(true);
		this.colorProperty = new SimpleObjectProperty<Paint>(Color.BLACK);
	}

	@Override
	public Hibernator call() {
		Thread.currentThread().setName("EstablishConnection");

		updateProgress(-1, -1);
		updateMessage("Connecting to database...");

		Hibernator hibernator = null;

		try {
			hibernator = new Hibernator(databaseLogin);
			updateProgress(1, 1);
			updateMessage("Connected");
			colorProperty.set(Color.LIMEGREEN);
		} catch (Exception e) {
			updateMessage("Not connected");
			connectedProperty.set(false);
			colorProperty.set(Color.FIREBRICK);

			if (showWindow) {
				Platform.runLater(() -> {
					ShowModalWindow.showProblemWithConnectionAlert(e);
					System.exit(-1);
				});
			}
		}

		return hibernator;
	}

	public SimpleObjectProperty<Paint> getColorProperty() {
		return colorProperty;
	}

	public SimpleBooleanProperty getConnectedProperty() {
		return connectedProperty;
	}

	public boolean isShowWindow() {
		return showWindow;
	}

	public void setShowWindow(boolean showWindow) {
		this.showWindow = showWindow;
	}

}
