package ryxis.core;

import java.util.Arrays;
import java.util.Properties;

@SuppressWarnings("serial")
public class AdvancedProperties extends Properties {
	
	public AdvancedProperties() {
		super();
	}
	
	public AdvancedProperties(Properties prop) {
		super(prop);
	}
	
	public String[] getStringArray(String val) {
		String asString = getProperty(val);
		if (asString == null)
			return null;
		String[] arr = asString.split("(?<!\\\\),\\s");
		for (int i = 0; i < arr.length; i++)
			arr[i] = arr[i].replace("\\,", ",").replace("\\\\", "\\");
		return arr;
	}
	
	public void setStringArray(String key, String[] val) {
		if (val == null)
			return;
		for (int i = 0; i < val.length; i++)
			val[i] = val[i].replace("\\", "\\\\").replace(",", "\\,");
		String asString = Arrays.toString(val);
		asString = asString.substring(1, asString.length() - 1);
		System.out.println(asString);
		setProperty(key, asString);
	}
}
