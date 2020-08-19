package utilities;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesFile {
	/**
	 * This method is used to read the value of a property from a properties file
	 * 
	 * @param filePath - absolute path to the properties file
	 * @param property - the name of the property whose value is desired
	 * @return - value of the desired property
	 * @throws IOException
	 */
	public static String read(String filePath, String property) throws IOException {
		String propertyValue = null;
		FileReader reader = new FileReader(filePath);
		Properties properties = new Properties();
		properties.load(reader);
		if (properties.containsKey(property))
			propertyValue = properties.getProperty(property);
		reader.close();
		return propertyValue;
	}

	/**
	 * This method is used to fetch all the entries present in a properties file
	 * 
	 * @param filePath - absolute path to the properties file
	 * @return - a set of all entries present in the properties file
	 * @throws IOException
	 */
	public static Set<Entry<Object, Object>> readAll(String filePath) throws IOException {
		FileReader reader = new FileReader(filePath);
		Properties properties = new Properties();
		properties.load(reader);
		reader.close();
		return properties.entrySet();
	}
}
