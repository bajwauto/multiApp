package utilities;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Generic {
	/**
	 * This method is used to create a folder along with its non-existent parent
	 * folders.
	 * 
	 * @param dirPath - path to the folder to be created
	 * @return - true, iff directory is created, else false
	 */
	public static boolean createDirectory(String dirPath) {
		File file = new File(dirPath);
		return file.mkdirs();
	}

	/**
	 * This method is ued to get the specified date in the desired format
	 * 
	 * @param date   - date to be formatted
	 * @param format - the desired format in which the date is required
	 * @return - string containing the formatted date
	 */
	public static String getFormattedDate(Date date, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}

	/**
	 * This method is used to change the provided date format
	 * 
	 * @param date           - string containing the date as per the format
	 *                       mentioned in the variable originalFormat
	 * @param originalFormat - the original format of the date provided
	 * @param desiredFormat  - the desired format of the date provided
	 * @return - string containing the date in the desired format
	 */
	public static String changeDateFormat(String date, String originalFormat, String desiredFormat) {
		SimpleDateFormat originalDateFormat = new SimpleDateFormat(originalFormat);
		SimpleDateFormat desiredDateFormat = new SimpleDateFormat(desiredFormat);
		String finalDate;
		try {
			finalDate = desiredDateFormat.format(originalDateFormat.parse(date));
		} catch (ParseException e) {
			finalDate = null;
			System.err
					.println("The provided date \"" + date + "\" does not have the fomrat \"" + originalFormat + "\"");
			e.printStackTrace();
		}
		return finalDate;
	}

	/**
	 * This method is used to get the substrings(along with their captured groups,
	 * if any) from the given text matched by the given regular expression pattern
	 * 
	 * @param text  - String to be tested against the provided regular expression
	 * @param regex - regular expression pattern
	 * @return - List of all the matches matched by the given regular expression.
	 *         Each match is represented by a list wherein the first item in the
	 *         list is the actual match and groups captured in the current match are
	 *         stored from 2nd item onwards. For example - [ [M1, M1G1, M1G2...],
	 *         [M2, M2G1, M2G2...], [M3, M3G1, M3G2...], ... ]
	 */
	public static List<List<String>> getRegexMatchesAndGroups(String text, String regex) {
		List<List<String>> matchesAndGroups = new ArrayList<List<String>>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			List<String> currentMatchAndGroups = new ArrayList<String>();
			for (int i = 0; i <= matcher.groupCount(); i++)
				currentMatchAndGroups.add(matcher.group(i));
			matchesAndGroups.add(currentMatchAndGroups);
		}
		return matchesAndGroups;
	}

	/**
	 * This method is used to get the values of named groups in each substring
	 * matched by the given regex
	 * 
	 * @param text  - String to be tested against the provided regular expression
	 * @param regex - regular expression pattern containing named capture groups
	 * @return - List of groupName-Value pairs.
	 */
	public static List<Map<String, String>> getRegexNamedGroupsValues(String text, String regex) {
		List<List<String>> groupNames = getRegexMatchesAndGroups(regex, "<([^>]+)>");
		List<Map<String, String>> matchesAndGroups = new ArrayList<Map<String, String>>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			Map<String, String> groupNameAndValues = new LinkedHashMap<String, String>();
			for (int i = 0; i < groupNames.size(); i++) {
				String currentGroupName = groupNames.get(i).get(1);
				String currentGroupValue;
				try {
					currentGroupValue = matcher.group(currentGroupName);
				} catch (IllegalArgumentException e) {
					currentGroupValue = null;
				}
				if (currentGroupValue != null)
					groupNameAndValues.put(currentGroupName, currentGroupValue);
			}
			matchesAndGroups.add(groupNameAndValues);
		}
		return matchesAndGroups;
	}

	/**
	 * This method is used to get the relative path of file or folders present in
	 * this project
	 * 
	 * @param folderOrFileName - name of folder or file whose relative path w.r.t.
	 *                         project's base path is desired
	 * @return relative path of the file or folder w.r.t. project's base path
	 */
	private static String getPath(String folderOrFileName) {
		String subPath;
		switch (folderOrFileName.toLowerCase().trim()) {
		case "src":
		case "logs":
		case "screenshots":
		case "reports":
			subPath = folderOrFileName;
			break;
		case "main":
		case "test":
			subPath = getPath("src") + "/" + folderOrFileName;
			break;
		case "mainjava":
			subPath = getPath("main") + "/java";
			break;
		case "testjava":
			subPath = getPath("test") + "/java";
			break;
		case "mainresources":
			subPath = getPath("main") + "/resources";
			break;
		case "testresources":
			subPath = getPath("test") + "/resources";
			break;
		case "or":
		case "objectrepository":
		case "objectrepositories":
			subPath = getPath("testresources") + "/objectRepositories";
			break;
		case "datasheets":
			subPath = getPath("testresources") + "/datasheets";
			break;
		case "extentconfig":
			subPath = getPath("mainresources") + "/extentConfiguration.xml";
			break;
		default:
			subPath = "";
		}
		return subPath;
	}

	/**
	 * This method is used to get the absolute path to files or folders present in
	 * this project
	 * 
	 * @param fileOrFolderName - name of file or folder whose absolute path is
	 *                         desired
	 * @return - absolute path to the given file or folder
	 */
	public static String getAbsolutePath(String fileOrFolderName) {
		String path = getPath(fileOrFolderName);
		File file = new File(path);
		return file.getAbsolutePath();
	}
}
