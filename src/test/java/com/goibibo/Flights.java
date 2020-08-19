package com.goibibo;

import org.testng.annotations.Test;

import com.goibibo.pages.FlightsPage;
import com.goibibo.pages.HomePage;

public class Flights extends Base {
	@Test(enabled = true)
	public void roundTripFlightSearch() throws Exception {
		HomePage homePage = new HomePage();
		FlightsPage flightsPage = homePage.topMenu.openFlightsMenu();
		flightsPage.selectTripType("Round Trip");
		flightsPage.selectSourceCity("Chennai");
		flightsPage.selectDestinationCity("Mumbai");
		flightsPage.setDepartureDate("26-12-20", "dd-MM-yy");
		flightsPage.setReturnDate("1-1-21", "d-M-yy");
		flightsPage.setAdultTravellers(2);
		flightsPage.setChildTravellers(2);
		flightsPage.setInfantTravellers(1);
		flightsPage.selectTravelClass("Premium Economy");
		flightsPage.searchFlights();
	}

	@Test(enabled = true)
	public void oneWayFlightSearch() throws Exception {
		HomePage homePage = new HomePage();
		FlightsPage flightsPage = homePage.topMenu.openFlightsMenu();
		flightsPage.selectTripType("One way");
		flightsPage.selectSourceCity("Kolkata");
		flightsPage.selectDestinationCity("Mundra");
		flightsPage.setDepartureDate("02-02-21", "dd-MM-yy");
		flightsPage.setAdultTravellers(1);
		flightsPage.setChildTravellers(2);
		flightsPage.selectTravelClass("Economy");
		flightsPage.searchFlights();
	}
}
