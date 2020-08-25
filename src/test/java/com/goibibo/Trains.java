package com.goibibo;

import static utilities.Log.error;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.goibibo.pages.HomePage;
import com.goibibo.pages.IRCTCTrainsPage;
import com.goibibo.pages.TrainRunningStatusPage;

import utilities.customAnnotations.Retry;

@Test(groups = { "trains", "full-blown" })
public class Trains extends Base {
	@Retry
	@Test(dataProvider = "excel", priority = 3, enabled = true, description = "This test is used to validate that the Train's search functionality is working as expected")
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

	@Retry
	@Test(dataProvider = "excel", priority = 4, enabled = true, description = "This test is used to validate that the Train's Live running functionality is working as expected")
	public void getTrainStatus(Map<String, Object> testData) {
		try {
			int currentIteration = ((Double) testData.get("sNo")).intValue();
			datasetRunning.set(currentIteration);
			String trainNameOrNo = (String) testData.get("trainNameOrNo");
			String station = (String) testData.get("station");
			String departureDay = (String) testData.get("departureDay");
			HomePage homePage = new HomePage();
			IRCTCTrainsPage trainsPage = homePage.topMenu.openIRCTCTrainsMenu();
			TrainRunningStatusPage trainStatusPage = trainsPage.checkTrainRunningStatus();
			trainStatusPage.enterTrainName(trainNameOrNo);
			if (!station.trim().equalsIgnoreCase("-")) {
				Thread.sleep(1000); // to be removed
				trainStatusPage.selectStation(station);
			}
			trainStatusPage.selectDepartDay(departureDay);
			takeScreenshot();
			trainStatusPage.getTrainStatus();
			takeFullPageScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			Assert.fail(e.getMessage(), e);
		}
	}
}
