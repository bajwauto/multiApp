package com.goibibo;

import static utilities.Log.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.goibibo.pages.BusPage;
import com.goibibo.pages.BusResultsPage;
import com.goibibo.pages.HomePage;

import utilities.Generic;
import utilities.customAnnotations.Retry;

@Test(groups = { "full-blown", "buses" })
public class Buses extends Base {
	@Retry
	@Test(dataProvider = "excel", enabled = true, priority = 8)
	public void searchBuses(Map<String, Object> testData) throws Exception {
		try {
			int currentIteration = ((Double) testData.get("sNo")).intValue();
			datasetRunning.set(currentIteration);
			String sourceCity = (String) testData.get("sourceCity");
			String destinationCity = (String) testData.get("destinationCity");
			String travelDate = (String) testData.get("travelDate");
			HomePage home = new HomePage();
			BusPage bus = home.topMenu.openBusMenu();
			bus.enterSourceCity(sourceCity);
			bus.enterDestinationCity(destinationCity);
			bus.enterTravelDate(travelDate, "d-M-yy");
			takeScreenshot();
			BusResultsPage busResults = bus.searchBuses();

			String actualSourceCity = busResults.getSourceCity();
			Assert.assertTrue(actualSourceCity.contains(sourceCity),
					"Incorrect Source City fetched from the Search Results. Actual Source city - " + actualSourceCity
							+ "\r\n Expected Source City: " + sourceCity);

			String actualdestinationCity = busResults.getDestinationCity();
			Assert.assertTrue(actualdestinationCity.contains(destinationCity),
					"Incorrect Destination City fetched from the Search Results. Actual Destination city - "
							+ actualdestinationCity + "\r\n Expected Destination City: " + destinationCity);

			String actualTravelDate = busResults.getTravelDate();
			travelDate = Generic.changeDateFormat(travelDate, "d-M-yy", "MMM d, yyyy");
			Assert.assertEquals(actualTravelDate, travelDate, "Incorrect travel date fetched from the search results");
			takeFullPageScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			Assert.fail(e.getMessage(), e);
		}
	}

	@Test(dataProvider = "excel", priority = 9, enabled = true, groups = "run")
	public void sortBuses(Map<String, Object> testData) throws Exception {
		try {
			searchBuses(testData);
			String sortBy = (String) testData.get("sortBy");
			String sortOrder = (String) testData.get("sortOrder");
			BusResultsPage busResults = new BusResultsPage();
			browser.scrollToPageTop();
			List<String> sortedBuses = busResults.sortBusesBy(sortBy, sortOrder);
			List<Long> actualValues = new ArrayList<Long>();
			List<Long> expectedValues = new ArrayList<Long>();
			switch (sortBy.toLowerCase().trim()) {
			case "duration":
			case "fastest":
				for (int i = 0; i < sortedBuses.size(); i++) {
					List<String> currentMatch = Generic
							.getRegexMatchesAndGroups(sortedBuses.get(i), "(?i)(\\d+)\\s*h\\s*(\\d+)\\s*m").get(0);
					actualValues.add(Long.parseLong(currentMatch.get(1)) * 60 + Long.parseLong(currentMatch.get(2)));
					expectedValues.add(actualValues.get(i));
				}
				break;
			case "arrival":
			case "departure":
			case "arrivalTime":
			case "departureTime":
				for (int i = 0; i < sortedBuses.size(); i++) {
					List<String> currentMatch = Generic
							.getRegexMatchesAndGroups(sortedBuses.get(i), "(?i)(\\d+)\\s*:\\s*(\\d+)").get(0);
					actualValues.add(Long.parseLong(currentMatch.get(1)) * 60 + Long.parseLong(currentMatch.get(2)));
					expectedValues.add(actualValues.get(i));
				}
				break;
			case "price":
			case "cheapest":
			default:
				for (String flight : sortedBuses) {
					actualValues.add(Long.parseLong(flight.replaceAll("\\D+", "")));
					expectedValues.add(Long.parseLong(flight.replaceAll("\\D+", "")));
				}
			}
			Collections.sort(expectedValues);
			if (sortOrder.toLowerCase().contains("desc"))
				Collections.reverse(expectedValues);
			Assert.assertEquals(actualValues, expectedValues,
					"The Buses are not sorted by " + sortBy + " in the " + sortOrder + " order");

			takeFullPageScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			Assert.fail(e.getMessage(), e);
		}
	}
}
