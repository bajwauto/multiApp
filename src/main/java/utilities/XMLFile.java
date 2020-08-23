package utilities;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLFile {
	/**
	 * This method is used to parse the XML data present in a String and return a
	 * new DOM Document
	 * 
	 * @param xml - String containing the XML data
	 * @return - DOM document corresponding to the parsed XML data present in the
	 *         String
	 */
	private static Document getDocument(String xml) {
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * This method is used to parse the XML data present in a file and return a new
	 * DOM Document
	 * 
	 * @param file - reference to the XML file containing the data
	 * @return - DOM document corresponding to the parsed XML data present in a file
	 */
	private static Document getDocument(File file) {
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * This method is used to extract the desired data from the XML data stored in a
	 * String using the given XPath Query
	 * 
	 * @param xml        - String containing the XML data
	 * @param xPathQuery - XPath query for fetching the desired data
	 * @return - List of Strings containing the desired data
	 */
	public static List<String> read(String xml, String xPathQuery) {
		Document document = getDocument(xml);
		List<String> matches = new ArrayList<String>();
		try {
			NodeList nodes = (NodeList) (XPathFactory.newInstance().newXPath().compile(xPathQuery).evaluate(document,
					XPathConstants.NODESET));
			for (int i = 0; i < nodes.getLength(); i++)
				matches.add(nodes.item(i).getTextContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matches;
	}

	/**
	 * This method is used to extract the desired data from the XML data stored in
	 * the given XML file
	 * 
	 * @param xmlFile    - reference to the file containing the XML data
	 * @param xPathQuery - XPath query for fetching the desired data
	 * @return - List of Strings containing the desired data
	 */
	public static List<String> read(File xmlFile, String xPathQuery) {
		Document document = getDocument(xmlFile);
		List<String> matches = new ArrayList<String>();
		try {
			NodeList nodes = (NodeList) (XPathFactory.newInstance().newXPath().compile(xPathQuery).evaluate(document,
					XPathConstants.NODESET));
			for (int i = 0; i < nodes.getLength(); i++)
				matches.add(nodes.item(i).getTextContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matches;
	}
}
