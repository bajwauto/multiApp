package com.goibibo;

import static utilities.Log.error;

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
}
