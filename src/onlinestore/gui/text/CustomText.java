package onlinestore.gui.text;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CustomText extends Text {
	
	@SuppressWarnings("unused")
	private double fontSize;
	
	public CustomText() {
		fontSize = 12;
	}
	
	public CustomText(double fontSize) {
		this.fontSize = fontSize;
		this.setFont(Font.font("Tahoma", FontWeight.NORMAL, fontSize));
	}

	public CustomText(String msg, double fontSize) {
		this(fontSize);
		this.setText(msg);
	}

}
