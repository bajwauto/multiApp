package com.goibibo;

import static utilities.Log.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.goibibo.pages.FlightResultsPage;
import com.goibibo.pages.FlightsPage;
import com.goibibo.pages.HomePage;

import utilities.Generic;
import utilities.customAnnotations.Retry;

@Test(groups = { "flights", "full-blown" })
public class Flights extends Base {

	@Retry
	@Test(dataProvider = "excel", priority = 1, enabled = true)
	public void oneWayFlightSearch(Map<String, Object> testData) throws Exception {
		try {
			int currentIteration = ((Double) testData.get("sNo")).intValue();
			datasetRunning.set(currentIteration);
			String sourceCity = (String) testData.get("sourceCity");
			String destinationCity = (String) testData.get("destinationCity");
			String departureDate = (String) testData.get("departureDate");
			int adults = ((Double) testData.get("adultCount")).intValue();
			int children = ((Double) testData.get("childCount")).intValue();
			int infants = ((Double) testData.get("infantCount")).intValue();
			String travelClass = (String) testData.get("class");
			HomePage homePage = new HomePage();
			FlightsPage flightsPage = homePage.topMenu.openFlightsMenu();
			flightsPage.selectTripType("One way");
			flightsPage.selectSourceCity(sourceCity);
			flightsPage.selectDestinationCity(destinationCity);
			flightsPage.setDepartureDate(departureDate, "d-M-yy");
			flightsPage.setAdultTravellers(adults);
			flightsPage.setChildTravellers(children);
			flightsPage.setInfantTravellers(infants);
			flightsPage.selectTravelClass(travelClass);
			takeScreenshot();
			FlightResultsPage flights = flightsPage.searchFlights();

			// Checkpoints and Validations
			String actualSourceCity = flights.getSourceCity();
			asert().assertTrue(actualSourceCity.equalsIgnoreCase(sourceCity),
					"Source City validation failed. Expected Source City - " + sourceCity + "; Actual Source City - "
							+ actualSourceCity);

			String actualDestinationCity = flights.getDestinationCity();
			asert().assertTrue(actualDestinationCity.equalsIgnoreCase(destinationCity),
					"Destination City validation failed. Expected Destination City - " + destinationCity
							+ "; Actual Destination City - " + actualDestinationCity);

			String expectedDepartureDate = Generic.changeDateFormat(departureDate, "d-M-yy", "EEE, d MMM");
			String actualDepartureDate = flights.getDepartureDate();
			asert().assertTrue(actualDepartureDate.equalsIgnoreCase(expectedDepartureDate),
					"Departure Date validation failed. Expected Departure date - " + departureDate
							+ "; Actual Departure date - " + actualDepartureDate);

			String actualTotalPax = flights.getTotalTravellers();
			actualTotalPax = Generic.getRegexMatchesAndGroups(actualTotalPax, "^\\d+").get(0).get(0);
			int actualTotalPassengers = Integer.parseInt(actualTotalPax);
			asert().assertEquals(actualTotalPassengers, infants + children + adults,
					"Total Passengers validation Failed");

			String actualTripClass = flights.getTravelClass();
			asert().assertTrue(actualTripClass.equalsIgnoreCase(travelClass),
					"Travel Class validation failed. Expected Travel Class - " + travelClass
							+ "; Actual Travel Class - " + actualTripClass);

//			actualDepartureDate = flights.getFareTrendHighlightedDate();
//			expectedDepartureDate = Generic.changeDateFormat(departureDate, "d-M-yy", "MMM dd");
//			asert().assertTrue(actualDepartureDate.equalsIgnoreCase(expectedDepartureDate),
//					"Fare Trend highlighted date validation failed. Expected highlighted date - " + departureDate
//							+ "; Actual highlighted date - " + actualDepartureDate);

			takeFullPageScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			Assert.fail(e.getMessage(), e);
		}
	}

	@Retry
	@Test(dataProvider = "excel", priority = 2, enabled = true)
	public void roundTripFlightSearch(Map<String, Object> testData) throws Exception {
		try {
			int currentIteration = ((Double) testData.get("sNo")).intValue();
			datasetRunning.set(currentIteration);
			String sourceCity = (String) testData.get("sourceCity");
			String destinationCity = (String) testData.get("destinationCity");
			String departureDate = (String) testData.get("departureDate");
			String returnDate = (String) testData.get("returnDate");
			int adults = ((Double) testData.get("adultCount")).intValue();
			int children = ((Double) testData.get("childCount")).intValue();
			int infants = ((Double) testData.get("infantCount")).intValue();
			String travelClass = (String) testData.get("class");
			HomePage homePage = new HomePage();
			FlightsPage flightsPage = homePage.topMenu.openFlightsMenu();
			flightsPage.selectTripType("Round Trip");
			flightsPage.selectSourceCity(sourceCity);
			flightsPage.selectDestinationCity(destinationCity);
			flightsPage.setDepartureDate(departureDate, "d-M-yy");
			flightsPage.setReturnDate(returnDate, "d-M-yy");
			flightsPage.setAdultTravellers(adults);
			flightsPage.setChildTravellers(children);
			flightsPage.setInfantTravellers(infants);
			flightsPage.selectTravelClass(travelClass);
			takeScreenshot();
			FlightResultsPage flights = flightsPage.searchFlights();
			// Checkpoints and Validations
			String actualSourceCity = flights.getSourceCity();
			asert().assertTrue(actualSourceCity.equalsIgnoreCase(sourceCity),
					"Source City validation failed. Expected Source City - " + sourceCity + "; Actual Source City - "
							+ actualSourceCity);

			String actualDestinationCity = flights.getDestinationCity();
			asert().assertTrue(actualDestinationCity.equalsIgnoreCase(destinationCity),
					"Destination City validation failed. Expected Destination City - " + destinationCity
							+ "; Actual Destination City - " + actualDestinationCity);

			String expectedDepartureDate = Generic.changeDateFormat(departureDate, "d-M-yy", "EEE, d MMM");
			String actualDepartureDate = flights.getDepartureDate();
			asert().assertTrue(actualDepartureDate.equalsIgnoreCase(expectedDepartureDate),
					"Departure Date validation failed. Expected Departure date - " + departureDate
							+ "; Actual Departure date - " + actualDepartureDate);

			String expectedReturnDate = Generic.changeDateFormat(returnDate, "d-M-yy", "EEE, d MMM");
			String actualReturnDate = flights.getReturnDate();
			asert().assertTrue(actualReturnDate.equalsIgnoreCase(expectedReturnDate),
					"Return Date validation failed. Expected Return date - " + expectedReturnDate
							+ "; Actual Return date - " + actualReturnDate);

			String actualTotalPax = flights.getTotalTravellers();
			actualTotalPax = Generic.getRegexMatchesAndGroups(actualTotalPax, "^\\d+").get(0).get(0);
			int actualTotalPassengers = Integer.parseInt(actualTotalPax);
			asert().assertEquals(actualTotalPassengers, infants + children + adults,
					"Total Passengers validation Failed");

			String actualTripClass = flights.getTravelClass();
			asert().assertTrue(actualTripClass.equalsIgnoreCase(travelClass),
					"Travel Class validation failed. Expected Travel Class - " + travelClass
							+ "; Actual Travel Class - " + actualTripClass);
			takeFullPageScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			Assert.fail(e.getMessage(), e);
		}
	}

	@Test(dataProvider = "excel", priority = 5, enabled = true)
	public void oneWayFlightSort(Map<String, Object> testData) throws Exception {
		try {
			oneWayFlightSearch(testData);
			String sortBy = (String) testData.get("sortBy");
			String sortOrder = (String) testData.get("sortOrder");
			FlightResultsPage flights = new FlightResultsPage();
			List<String> sortedFlights = flights.sortOnwardFlights(sortBy, sortOrder);
			List<Long> actualValues = new ArrayList<Long>();
			List<Long> expectedValues = new ArrayList<Long>();
			switch (sortBy.toLowerCase().trim()) {
			case "duration":
				for (int i = 0; i < sortedFlights.size(); i++) {
					List<String> currentMatch = Generic
							.getRegexMatchesAndGroups(sortedFlights.get(i), "(?i)(\\d+)\\s*h\\s*(\\d+)\\s*m").get(0);
					actualValues.add(Long.parseLong(currentMatch.get(1)) * 60 + Long.parseLong(currentMatch.get(2)));
					expectedValues.add(actualValues.get(i));
				}
				break;
			case "arrival":
			case "departure":
				for (int i = 0; i < sortedFlights.size(); i++) {
					List<String> currentMatch = Generic
							.getRegexMatchesAndGroups(sortedFlights.get(i), "(?i)(\\d+)\\s*:\\s*(\\d+)").get(0);
					actualValues.add(Long.parseLong(currentMatch.get(1)) * 60 + Long.parseLong(currentMatch.get(2)));
					expectedValues.add(actualValues.get(i));
				}
				break;
			case "price":
			default:
				for (String flight : sortedFlights) {
					actualValues.add(Long.parseLong(flight.replaceAll("\\D+", "")));
					expectedValues.add(Long.parseLong(flight.replaceAll("\\D+", "")));
				}
			}
			Collections.sort(expectedValues);
			if (sortOrder.toLowerCase().contains("desc"))
				Collections.reverse(expectedValues);
			asert().assertEquals(actualValues, expectedValues,
					"The flights are not sorted by " + sortBy + " in the " + sortOrder + " order");

			takeFullPageScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			Assert.fail(e.getMessage(), e);
		}
	}

	@Test(dataProvider = "excel", priority = 6, enabled = true, groups = "run")
	public void oneWayFlightFilters(Map<String, Object> testData) {
		try {
			oneWayFlightSearch(testData);
			String filter = (String) testData.get("filter");
			FlightResultsPage flights = new FlightResultsPage();
			List<String> filteredFlights = new ArrayList<String>();
			Map<String, String> filterRange = new HashMap<String, String>();
			switch (filter.toLowerCase().trim()) {
			case "onward price":
			case "return price":
				String[] priceSliders = new String[] { "lower", "upper" };
				double[] priceSliderPercentages = new double[] { 30, -30 };
				for (int i = 0; i < priceSliders.length; i++) {
					filteredFlights = flights.applyFilter(filter, priceSliders[i], priceSliderPercentages[i]);
					filterRange = flights.getFilterRange(filter);
					int lowPriceLimit = Integer.parseInt(filterRange.get("low").replaceAll(",", ""));
					int highPriceLimit = Integer.parseInt(filterRange.get("high").replaceAll(",", ""));
					for (String filteredFlight : filteredFlights) {
						int currentPrice = Integer.parseInt(filteredFlight.replaceAll(",", ""));
						if (currentPrice < lowPriceLimit || currentPrice > highPriceLimit)
							throw new Exception(
									"The flight price \"" + currentPrice + "\" doesn't fall in the price Range - ["
											+ lowPriceLimit + "," + highPriceLimit + "].");
					}
					flights.resetFilter(filter);
				}
				break;
			case "onward duration":
			case "return duration":
				String durationParserRegex = "^\\s*(\\d+)\\s*h\\s*(\\d+)\\s*m\\s*$";
				String[] durationSliders = new String[] { "lower", "upper" };
				double[] durationSliderPercentages = new double[] { 30, -30 };
				for (int i = 0; i < durationSliders.length; i++) {
					filteredFlights = flights.applyFilter(filter, durationSliders[i], durationSliderPercentages[i]);
					filterRange = flights.getFilterRange(filter);
					List<String> lowerLimitMatches = Generic
							.getRegexMatchesAndGroups(filterRange.get("low"), durationParserRegex).get(0);
					List<String> upperLimitMatches = Generic
							.getRegexMatchesAndGroups(filterRange.get("high"), durationParserRegex).get(0);
					int lowDurationLimit = Integer.parseInt(lowerLimitMatches.get(1)) * 60
							+ Integer.parseInt(lowerLimitMatches.get(2));
					int highDurationLimit = Integer.parseInt(upperLimitMatches.get(1)) * 60
							+ Integer.parseInt(upperLimitMatches.get(2));
					for (String filteredFlight : filteredFlights) {
						List<String> matches = Generic.getRegexMatchesAndGroups(filteredFlight, durationParserRegex)
								.get(0);
						int currentDuration = Integer.parseInt(matches.get(1)) * 60 + Integer.parseInt(matches.get(2));
						if (currentDuration < lowDurationLimit || currentDuration > highDurationLimit)
							throw new Exception("The flight duration \"" + currentDuration
									+ "\" doesn't fall in the duration Range - [" + lowDurationLimit + ","
									+ highDurationLimit + "].");
					}
					flights.resetFilter(filter);
				}
				break;
			default:
				throw new Exception("Invalid filter name - " + filter);
			}
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			Assert.fail(e.getMessage(), e);
		}
	}
}
