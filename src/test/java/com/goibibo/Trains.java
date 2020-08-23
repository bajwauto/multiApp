package com.goibibo;

import static utilities.Log.error;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.goibibo.pages.HomePage;
import com.goibibo.pages.IRCTCTrainsPage;

import utilities.customAnnotations.Retry;

@Test(groups = { "trains", "full-blown" })
public class Trains extends Base {
	@Retry
	@Test(dataProvider = "excel", priority = 3, enabled = true)
	public void searchTrains(Map<String, Object> testData) {
		try {
			int currentIteration = ((Double) testData.get("sNo")).intValue();
			datasetRunning.set(currentIteration);
			String sourceStation = (String) testData.get("sourceStation");
			String destinationStation = (String) testData.get("destinationStation");
			String departureDate = (String) testData.get("departureDate");
			HomePage homePage = new HomePage();
			IRCTCTrainsPage trainsPage = homePage.topMenu.openIRCTCTrainsMenu();
			trainsPage.enterSourceStation(sourceStation);
			trainsPage.enterDestinationStation(destinationStation);
			trainsPage.setJourneyDate(departureDate, "d-M-yy");
			takeScreenshot();
			trainsPage.searchTrains();
			takeFullPageScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			Assert.fail(e.getMessage(), e);
		}
	}
}
