package onlinestore;

public class StringPreparator {

	private StringPreparator() {
	}
	
	public static String getPreparedString(String str) {
		if(str == null) 
			throw new IllegalArgumentException("String to prepare cannot be null.");
		return Character.toUpperCase(str.trim().charAt(0)) + str.trim().toLowerCase().substring(1);
	}

}
