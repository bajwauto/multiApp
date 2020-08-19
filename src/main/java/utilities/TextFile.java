package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFile {
	/**
	 * This method is used to read the contents of a text file
	 * 
	 * @param filePath - absolute path to the text file to be read
	 * @return - data present in the text file in form of a list. Each element in a
	 *         list represents the data contained in each line of the file
	 * @throws IOException
	 */
	public static List<String> read(String filePath) throws IOException {
		File file = new File(filePath);
		List<String> data = new ArrayList<String>();
		String currentLine;
		FileReader fileReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fileReader);
		while ((currentLine = reader.readLine()) != null)
			data.add(currentLine);
		reader.close();
		fileReader.close();
		return data;
	}

	/**
	 * This method is used to read the contents of a line in a text file
	 * 
	 * @param filePath   - absolute path to the text file to be read
	 * @param lineNumber - the desired line whose data is to be read
	 * @return - text present in the desired line
	 * @throws IOException
	 */
	public static String read(String filePath, int lineNumber) throws IOException {
		List<String> data = read(filePath);
		String lineText = null;
		if (lineNumber <= data.size())
			lineText = data.get(lineNumber - 1);
		return lineText;
	}

	/**
	 * This method is used to write the provided data to the provided file
	 * 
	 * @param filePath    - absolute path to the file in which the data is to be
	 *                    written
	 * @param textToWrite - data to be written to the text file
	 * @param append      - true to write the data at the end of the file; false to
	 *                    overwrite the existing data with the new data being
	 *                    written
	 * @throws IOException
	 */
	public static void write(String filePath, String textToWrite, boolean append) throws IOException {
		File file = new File(filePath);
		FileWriter fileWriter = new FileWriter(file, append);
		BufferedWriter writer = new BufferedWriter(fileWriter);
		writer.write(textToWrite);
		writer.close();
		fileWriter.close();
	}
}
