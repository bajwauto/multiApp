package com.goibibo.pages;

import java.util.ArrayList;
import java.util.HashMap;
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
		debugLogger(
				"Flight's " + by + " values extracted after sorting in the " + order + " order: " + valuesAfterSort);
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

	/**
	 * This method is used to apply a filter to the flight search results
	 * 
	 * @param filterName  - types of filters that can be applied on the flights
	 *                    results page. Possible values are Onward Price/Return
	 *                    Price/Onward Duration/Return Duration
	 * @param filterLimit - whether the user wants to adjust the upper or ower limit
	 *                    of the filter
	 * @param percentage  - adjust lower/upper slider by some percentage
	 * @return - list of flights with the specified filter
	 * @throws Exception
	 */
	public List<String> applyFilter(String filterName, String filterLimit, double percentage) throws Exception {
		List<String> flightsDetail = new ArrayList<String>();
		try {
			String slideXPath, sliderXPath, strTemp, key;
			switch (filterName.toLowerCase().trim()) {
			case "onward price":
				strTemp = "Onward Price";
				key = "finalPrice";
				break;
			case "return price":
				strTemp = "Return Price";
				key = "finalPrice";
				break;
			case "onward duration":
				strTemp = "Onward Duration";
				key = "duration";
				break;
			case "return duration":
				strTemp = "Return Duration";
				key = "duration";
				break;
			default:
				throw new Exception("Invalid filter name - " + filterName);
			}
			slideXPath = "//div[div/span/text()='" + strTemp + "']//div[@class='fltSld-connect fltSld-connect-1']";
			switch (filterLimit.toLowerCase().trim()) {
			case "upper":
				sliderXPath = slideXPath + "/following-sibling::div";
				break;
			case "lower":
			default:
				sliderXPath = slideXPath + "/preceding-sibling::div";
				break;
			}

			int slideWidth = browser.getObjectBounds(browser.findElement(By.xpath(slideXPath))).width;
			browser.dragAndDropBy(browser.findElement(By.xpath(sliderXPath)), (int) (slideWidth * percentage / 100), 0);
			browser.scrollToPageBottom(200);
			browser.scrollToPageTop();
			List<Map<String, String>> flightsDetails = getOnwardFlightsDetails();
			for (Map<String, String> flightDetails : flightsDetails)
				flightsDetail.add(flightDetails.get(key));
		} catch (Exception e) {
			throw new Exception("Could adjust the \"" + filterLimit + "\" limit of the filter \"" + filterName
					+ "\" by " + percentage + "%");
		}
		infoLogger("Filter applied successfully - Adjusted the \"" + filterLimit + "\" limit of the filter \""
				+ filterName + "\" by " + percentage + "%. Flight Details fetched: " + flightsDetail);
		return flightsDetail;
	}

	/**
	 * This method is used to get the current filter range values
	 * 
	 * @param filterName - types of filters that can be applied on the flights
	 *                   results page. Possible values are Onward Price/Return
	 *                   Price/Onward Duration/Return Duration
	 * @return - lower and upper limit values of the given filter
	 * @throws Exception
	 */
	public Map<String, String> getFilterRange(String filterName) throws Exception {
		Map<String, String> range = new HashMap<String, String>();
		try {
			switch (filterName.toLowerCase().trim()) {
			case "onward price":
				range.put("low", browser.getText(browser.findElement("min_Onward_Price_Filter_Value")).trim());
				range.put("high", browser.getText(browser.findElement("max_Onward_Price_Filter_Value")).trim());
				break;
			case "onward duration":
				range.put("low", browser.getText(browser.findElement("min_Onward_Duration_Filter_Value")).trim());
				range.put("high", browser.getText(browser.findElement("max_Onward_Duration_Filter_Value")).trim());
				break;
			case "return price":
				range.put("low", browser.getText(browser.findElement("min_Return_Price_Filter_Value")).trim());
				range.put("high", browser.getText(browser.findElement("max_Return_Price_Filter_Value")).trim());
				break;
			case "return duration":
				range.put("low", browser.getText(browser.findElement("min_Return_Duration_Filter_Value")).trim());
				range.put("high", browser.getText(browser.findElement("max_Return_Duration_Filter_Value")).trim());
				break;
			default:
				throw new Exception("Invalid filter name - " + filterName);
			}
		} catch (Exception e) {
			throw new Exception("Could not fetch the \"" + filterName + "\" filter range values", e);
		}
		infoLogger("Current filter(" + filterName + ") range fetched: " + range.get("low") + " - " + range.get("high"));
		return range;
	}

	/**
	 * This method is used to reset the specified filter
	 * 
	 * @param filterName - name of the filter to be reset
	 * @throws Exception
	 */
	public void resetFilter(String filterName) throws Exception {
		try {
			switch (filterName.toLowerCase().trim()) {
			case "onward price":
				browser.click("onward_Price_Filter");
				break;
			case "return price":
				browser.click("return_Price_Filter");
				break;
			case "onward duration":
				browser.click("onward_Duration_Filter");
				break;
			case "return duration":
				browser.click("return_Duration_Filter");
				break;
			case "preferred airlines":
				browser.click("preferred_Airlines_Filter");
				break;
			default:
				throw new Exception("Invalid filter name - " + filterName);
			}
		} catch (Exception e) {
			throw new Exception("Could not reset the \"" + filterName + "\" filter", e);
		}
		infoLogger("The filter \"" + filterName + "\" has been reset successfully");
	}
}
