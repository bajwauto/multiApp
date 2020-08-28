package com.goibibo.pages;

import java.util.ArrayList;
import static utilities.Log.debug;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class FlightResultsPage extends Page {
	/**
	 * This method is used to fetch the source City from the flights results page
	 * 
	 * @return - the source city
	 * @throws Exception
	 */
	public String getSourceCity() throws Exception {
		try {
			String sourceCity = browser.getAttribute(browser.findElement("source_City_Textbox"), "value");
			infoLogger("Source city fetched: " + sourceCity);
			return sourceCity;
		} catch (Exception e) {
			throw new Exception("Could not fetch the value of Source City selected", e);
		}
	}

	/**
	 * This method is used to fetch the source City from the flights results page
	 * 
	 * @return - the destination city
	 * @throws Exception
	 */
	public String getDestinationCity() throws Exception {
		try {
			String destinationCity = browser.getAttribute(browser.findElement("destination_City_Textbox"), "value");
			infoLogger("Destination city fetched: " + destinationCity);
			return destinationCity;
		} catch (Exception e) {
			throw new Exception("Could not fetch the value of Destination City selected", e);
		}
	}

	/**
	 * This method is used to fetch the departure date from the flights results page
	 * 
	 * @return - the departure date
	 * @throws Exception
	 */
	public String getDepartureDate() throws Exception {
		try {
			String departureDate = browser.getAttribute(browser.findElement("departure_Date_Textbox"), "value");
			infoLogger("Departure Date fetched: " + departureDate);
			return departureDate;
		} catch (Exception e) {
			throw new Exception("Could not fetch the value of Departure Date selected", e);
		}
	}

	/**
	 * This method is used to fetch the return date from the flights results page
	 * 
	 * @return - the return date
	 * @throws Exception
	 */
	public String getReturnDate() throws Exception {
		try {
			String returnDate = browser.getAttribute(browser.findElement("return_Date_Textbox"), "value");
			infoLogger("Return Date fetched: " + returnDate);
			return returnDate;
		} catch (Exception e) {
			throw new Exception("Could not fetch the value of Return Date selected", e);
		}
	}

	/**
	 * This method is used to fetch the no. of travellers from the flights results
	 * page
	 * 
	 * @return - the no. of travellers
	 * @throws Exception
	 */
	public String getTotalTravellers() throws Exception {
		try {
			String totalTravellers = browser.getText(browser.findElement("total_Travellers_Element"));
			infoLogger("Total Travellers fetched: " + totalTravellers);
			return totalTravellers;
		} catch (Exception e) {
			throw new Exception("Could not fetch the total number of travellers", e);
		}
	}

	/**
	 * This method is used to fetch the Travel Class from the flights results page
	 * 
	 * @return - the travel class
	 * @throws Exception
	 */
	public String getTravelClass() throws Exception {
		try {
			String travelClass = browser.getText(browser.findElement("travel_Class_Element"));
			infoLogger("Travel Class fetched: " + travelClass);
			return travelClass;
		} catch (Exception e) {
			throw new Exception("Could not fetch the value of Travel Class selected", e);
		}
	}

	/**
	 * This method is used to get the date from the highlighted item from the Fare
	 * Trend section Flights search results page
	 * 
	 * @return - date from the highlighted item in the the Fare Trend section
	 * @throws Exception
	 */
	public String getFareTrendHighlightedDate() throws Exception {
		try {
			browser.disableVisibilityCheck(true);
			return browser.getText(browser.findElement("highlighted_Date_Element"));
		} catch (Exception e) {
			throw new Exception("Could not fetch the date of the highlighted item in the Fare Trend Section", e);
		}
	}

	/**
	 * This method is used to get the current price filter range in the flights
	 * search results page
	 * 
	 * @param direction - possible values - onward/return
	 * @return - a hashmap containing the minimum and maximum price of the price
	 *         filter
	 * @throws Exception
	 */
	public Map<String, String> getCurrentPriceFilterRange(String direction) throws Exception {
		Map<String, String> priceFilterRange = new LinkedHashMap<String, String>();
		try {
			switch (direction.toLowerCase().trim()) {
			case "return":
				priceFilterRange.put("min", browser.getText(browser.findElement("min_Return_Price_Filter_Value")));
				priceFilterRange.put("max", browser.getText(browser.findElement("max_Return_Price_Filter_Value")));
				break;
			case "onward":
			default:
				priceFilterRange.put("min", browser.getText(browser.findElement("min_Onward_Price_Filter_Value")));
				priceFilterRange.put("max", browser.getText(browser.findElement("max_Onward_Price_Filter_Value")));
			}
			return priceFilterRange;
		} catch (Exception e) {
			throw new Exception("Could not fetch the current price range from the price filter", e);
		}
	}

	/**
	 * This method is used to get the current duration filter range in the flights
	 * search results page
	 * 
	 * @param direction - possible values - onward/return
	 * @return - a hashmap containing the minimum and maximum price of the duration
	 *         filter
	 * @throws Exception
	 */
	public Map<String, String> getCurrentDurationFilterRange(String direction) throws Exception {
		Map<String, String> durationFilterRange = new LinkedHashMap<String, String>();
		try {
			switch (direction.toLowerCase().trim()) {
			case "return":
				durationFilterRange.put("min",
						browser.getText(browser.findElement("min_Return_Duration_Filter_Value")));
				durationFilterRange.put("max",
						browser.getText(browser.findElement("max_Reward_Duration_Filter_Value")));
				break;
			case "onward":
			default:
				durationFilterRange.put("min",
						browser.getText(browser.findElement("min_Onward_Duration_Filter_Value")));
				durationFilterRange.put("max",
						browser.getText(browser.findElement("max_Onward_Duration_Filter_Value")));
			}
			return durationFilterRange;
		} catch (Exception e) {
			throw new Exception("Could not fetch the current duration range from the duration filter", e);
		}
	}

	/**
	 * This method is used to sort the flights by the specified parameter in the
	 * specified order
	 * 
	 * @param by    - possible parameter values - departure/arrival/duration/price
	 * @param order - the sorting order
	 * @return - List of desired parameter values in the desired order
	 * @throws Exception
	 */
	public List<String> sortOnwardFlights(String by, String order) throws Exception {
		List<String> valuesAfterSort = new ArrayList<String>();
		String locatorXPath, key;
		switch (by.toLowerCase().trim()) {
		case "departure":
			locatorXPath = "//span[contains(@class,'fb ico11') and text()='DEPARTURE']";
			key = "departureTime";
			break;
		case "duration":
			locatorXPath = "//span[contains(@class,'fb ico11') and text()='DURATION']";
			key = "duration";
			break;
		case "arrival":
			locatorXPath = "//span[contains(@class,'fb ico11') and text()='ARRIVAL']";
			key = "arrivalTime";
			break;
		case "price":
		default:
			locatorXPath = "//span[contains(@class,'fb ico11') and text()='PRICE']";
			key = "finalPrice";
		}

		switch (order.toLowerCase().trim()) {
		case "descending":
		case "desc":
			order = "down";
			break;
		case "ascending":
		case "asc":
		default:
			order = "up";
		}

		try {
			browser.click(By.xpath(locatorXPath));
			while (!browser
					.getAttribute(browser.findElement(By.xpath(locatorXPath + "//following-sibling::i")), "class")
					.contains(order))
				browser.click(By.xpath(locatorXPath));
			if (!browser.getAttribute(browser.findElement(By.xpath(locatorXPath + "//following-sibling::i")), "class")
					.contains(order))
				throw new Exception("Could not sort flights in the specified order");
			else {
				browser.scrollToPageBottom(200);
				browser.scrollToPageTop();
				List<Map<String, String>> flightsDetails = getOnwardFlightsDetails();
				for (Map<String, String> flightDetails : flightsDetails)
					valuesAfterSort.add(flightDetails.get(key));
			}
		} catch (Exception e) {
			throw new Exception("Unable to sort the flights by " + by + " in " + order + " order", e);
		}
		debugLogger("Flight's " + by + " values extracted after sorting in the " + order + " order: " + valuesAfterSort);
		return valuesAfterSort;
	}

	/**
	 * This method is used to fetch the high level details of all the onward flights
	 * 
	 * @return - flights details like airline, departure Time, duration, arrival
	 *         time and final price
	 * @throws Exception
	 */
	public List<Map<String, String>> getOnwardFlightsDetails() throws Exception {
		try {
			List<Map<String, String>> flightsDetails = new ArrayList<Map<String, String>>();
			List<WebElement> airlines = browser.findElements("airLines_Element");
			List<WebElement> depTimes = browser.findElements("departure_Time_Element");
			List<WebElement> durations = browser.findElements("duration_Element");
			List<WebElement> arrTimes = browser.findElements("arrival_Time_Element");
			List<WebElement> prices = browser.findElements("final_Price_Element");
			for (int i = 0; i < prices.size(); i++) {
				Map<String, String> currentFlightDetails = new LinkedHashMap<String, String>();
				currentFlightDetails.put("airline", browser.getText(airlines.get(i)));
				currentFlightDetails.put("departureTime", browser.getText(depTimes.get(i)));
				currentFlightDetails.put("duration", browser.getText(durations.get(i)));
				currentFlightDetails.put("arrivalTime", browser.getText(arrTimes.get(i)));
				currentFlightDetails.put("finalPrice", browser.getText(prices.get(i)));
				flightsDetails.add(currentFlightDetails);
			}
			return flightsDetails;
		} catch (Exception e) {
			throw new Exception("Unable to fetch the flights details", e);
		}
	}
}
