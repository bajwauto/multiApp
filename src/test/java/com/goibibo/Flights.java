package com.goibibo;

import static utilities.Log.error;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.goibibo.pages.FlightsPage;
import com.goibibo.pages.HomePage;

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
			flightsPage.searchFlights();
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
			flightsPage.searchFlights();
			takeFullPageScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			Assert.fail(e.getMessage(), e);
		}
	}
}
