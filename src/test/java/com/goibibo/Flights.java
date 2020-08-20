package com.goibibo;

import java.util.Map;

import org.testng.annotations.Test;

import com.goibibo.pages.FlightsPage;
import com.goibibo.pages.HomePage;

public class Flights extends Base {
	@Test(dataProvider = "excel", priority = 1, enabled = true)
	public void oneWayFlightSearch(Map<String, Object> testData) throws Exception {
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
		flightsPage.searchFlights();
	}

	@Test(dataProvider = "excel", priority = 2, enabled = true)
	public void roundTripFlightSearch(Map<String, Object> testData) throws Exception {
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
		flightsPage.searchFlights();
	}
}
