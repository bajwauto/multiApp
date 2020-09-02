package com.goibibo.pages;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utilities.Generic;

public class BusResultsPage extends Page {
	/**
	 * This method is used to fetch the source city selected
	 * 
	 * @return - String containing the source city fetched
	 * @throws Exception
	 */
	public String getSourceCity() throws Exception {
		String sourceCity;
		try {
			sourceCity = browser.getAttribute(browser.findElement("results_From_Bus_Stand"), "value");
		} catch (Exception e) {
			throw new Exception("Unable to get the source city selected", e);
		}
		infoLogger("Source City fetched - " + sourceCity);
		return sourceCity;
	}

	/**
	 * This method is used to fetch the destination city selected
	 * 
	 * @return - String containing the destination city fetched
	 * @throws Exception
	 */
	public String getDestinationCity() throws Exception {
		String destinationCity;
		try {
			destinationCity = browser.getAttribute(browser.findElement("results_To_Bus_Stand"), "value");
		} catch (Exception e) {
			throw new Exception("Unable to get the destination city selected", e);
		}
		infoLogger("Destination City fetched - " + destinationCity);
		return destinationCity;
	}

	/**
	 * This method is used to fetch the travel date selected
	 * 
	 * @return - String containing the travel date
	 * @throws Exception
	 */
	public String getTravelDate() throws Exception {
		String travelDate;
		try {
			travelDate = browser.getAttribute(browser.findElement("travel_Date_Element"), "value");
		} catch (Exception e) {
			throw new Exception("Unable to fetch the travel date", e);
		}
		infoLogger("Travel Date fetched - " + travelDate);
		return travelDate;
	}

	/**
	 * This method is used to fetch the high level details of all the buses searched
	 * 
	 * @return - high level details of all the buses searched like operator,
	 *         busType, rating, departureTime, duration, travelDate, arrivalTime,
	 *         pickupPoint, dropPoint, price etc.
	 * @throws Exception
	 */
	public List<Map<String, String>> getBusesDetails() throws Exception {
		List<Map<String, String>> busesDetails = new ArrayList<Map<String, String>>();
		try {
			String busDetailsRegex = "^(?<operator>.+)\\s+(?<busType>.+)\\s+(?:(?<rating>\\d+(?:\\.\\d+)?)\\s+\\/5\\s+)?(?:(?<ratingCount>\\d+ Ratings)\\s+)?(?<departureTime>\\d{2}:\\d{2})\\s+(?<pickupPoint>.+)\\s+(?<duration>\\d+h\\s+\\d+m)\\s+(?:\\d+ rest stops?\\s+)?(?<travelDate>\\d+.+)\\s+(?<arrivalTime>\\d{2}:\\d{2})\\s+(?<dropPoint>.+)\\s+.+\\s+(?<price>\\d+)";
			browser.scrollToPageBottom(200);
			browser.scrollToPageTop();
			List<WebElement> buses = browser
					.findElements(By.cssSelector("div.SrpActiveCardstyles__ActivepcardInnerLayoutDiv-yk1110-3"));
			for (WebElement bus : buses) {
				Map<String, String> currentBusDetails = new LinkedHashMap<String, String>();
				String busDetails = browser.getText(bus);
				Map<String, String> capturedGroups = Generic.getRegexNamedGroupsValues(busDetails, busDetailsRegex)
						.get(0);
				for (Map.Entry<String, String> groupNameValue : capturedGroups.entrySet())
					currentBusDetails.put(groupNameValue.getKey(), groupNameValue.getValue());
				busesDetails.add(currentBusDetails);
			}
		} catch (Exception e) {
			throw new Exception("Unable to fetch the Buses details", e);
		}
		infoLogger("Buses Details fetched: " + busesDetails);
		return busesDetails;
	}

	/**
	 * This method is used to sort the buses by the given parameter in the given
	 * order
	 * 
	 * @param sortBy - sort buses by Rating/Departure/Arrival/fastest/cheapest
	 * @param order  - sort order - ascending/descending
	 * @return - sorted values of the parameter used for sorting
	 * @throws Exception
	 */
	public List<String> sortBusesBy(String sortBy, String order) throws Exception {
		List<String> sortedParamValues = new ArrayList<String>();
		try {
			String paramXPath, arrowDirection, keyName;
			switch (sortBy.toLowerCase().trim()) {
			case "fastest":
			case "duration":
				paramXPath = "//span[contains(text(),'FASTEST')]";
				keyName = "duration";
				break;
			case "arrival":
			case "arrivalTime":
				paramXPath = "//span[contains(text(),'ARRIVAL')]";
				keyName = "arrivalTime";
				break;
			case "departure":
			case "departureTime":
				paramXPath = "//span[contains(text(),'DEPARTURE')]";
				keyName = "departureTime";
				break;
			case "rating":
				paramXPath = "//span[contains(text(),'RATING')]";
				keyName = "rating";
				break;
			case "cheapest":
			case "price":
			default:
				paramXPath = "//span[contains(text(),'CHEAPEST')]";
				keyName = "price";
			}

			switch (order.toLowerCase().trim()) {
			case "descending":
			case "desc":
				arrowDirection = "ArrowDown";
				break;
			case "ascending":
			case "asc":
			default:
				arrowDirection = "ArrowUp";
			}

			browser.click(By.xpath(paramXPath));
			while (!(browser.getAttribute(browser.findElement(By.xpath(paramXPath + "/*[local-name()='svg']")), "class")
					.contains(arrowDirection)))
				browser.click(By.xpath(paramXPath));
			List<Map<String, String>> sortedBusesDetails = getBusesDetails();
			for (Map<String, String> busDetails : sortedBusesDetails) {
				if (busDetails.containsKey(keyName))
					sortedParamValues.add(busDetails.get(keyName));
			}
		} catch (Exception e) {
			throw new Exception("Could not sort buses by parameter \"" + sortBy + "\" in the \"" + order + "\" order");
		}
		infoLogger("Buses sorted by parameter \"" + sortBy + "\" in \"" + order + "\" order. Sorted values are: "
				+ sortedParamValues);
		return sortedParamValues;
	}
}
