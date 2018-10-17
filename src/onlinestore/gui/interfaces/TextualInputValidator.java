package onlinestore.gui.interfaces;

public interface TextualInputValidator extends InputValidator {
	
	default boolean isValidInput(String input){
		if(this.isAnyInput() == false)
			return false;
		input = input.trim();
		for(int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if(c <= '9' && c != ' ')
				return false;
		}
		return true;
	}
	
}
