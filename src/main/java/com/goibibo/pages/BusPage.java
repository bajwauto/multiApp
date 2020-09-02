package com.goibibo.pages;

import org.openqa.selenium.By;

import utilities.Generic;

public class BusPage extends Page {
	/**
	 * This method is used to enter the source Bus station/city
	 * 
	 * @param sourceCity - city to be set as the source city/stand
	 * @throws Exception
	 */
	public void enterSourceCity(String sourceCity) throws Exception {
		try {
			browser.sendkeys("from_Bus_Stand", sourceCity);
			browser.click(By.xpath("//div[contains(li/span/text(),'" + sourceCity + "')]"));
		} catch (Exception e) {
			throw new Exception("Unable to set \"" + sourceCity + "\" as the source city", e);
		}
		infoLogger("Selected the source City - " + sourceCity);
	}

	/**
	 * This method is used to enter the destination Bus station/city
	 * 
	 * @param destinationCity - city to be set as the destination city/stand
	 * @throws Exception
	 */
	public void enterDestinationCity(String destinationCity) throws Exception {
		try {
			browser.sendkeys("to_Bus_Stand", destinationCity);
			browser.click(By.xpath("//div[contains(li/span/text(),'" + destinationCity + "')]"));
		} catch (Exception e) {
			throw new Exception("Unable to set \"" + destinationCity + "\" as the destination city", e);
		}
		infoLogger("Selected the destination City - " + destinationCity);
	}

	/**
	 * This method is used to enter the travel date
	 * 
	 * @param date   - date to be set as the journey date
	 * @param format - format of the date provided
	 * @throws Exception
	 */
	public void enterTravelDate(String date, String format) throws Exception {
		try {
			browser.click("travel_Date_Element");
			String expectedDate = Generic.changeDateFormat(date, format, "d-MMMM yyyy");
			String expectedD = expectedDate.split("-")[0];
			String expectedMY = expectedDate.split("-")[1];
			String actualMY;
			while (!(actualMY = browser.getText(browser.findElement(By.xpath("//p[contains(@class,'MonthNamePara')]"))))
					.equalsIgnoreCase(expectedMY))
				browser.click("travel_Date_toNextMonth_Element");
			if (!(actualMY.equalsIgnoreCase(expectedMY)))
				throw new Exception("Could not navigate to the desired Month.");
			else {
				browser.click(By.xpath(
						"//ul[contains(@class,'dcalendarstyles__DateWrapDiv')]//span[text()='" + expectedD + "']"));
			}
		} catch (Exception e) {
			throw new Exception("Unable to set \"" + date + "\" as the journey date", e);
		}
		infoLogger("Set the journey date to " + date);
	}

	/**
	 * This method is used to perform the bus search by clicking on the Search Bus
	 * button and wait for the search results to load
	 * 
	 * @return - an instance of the BusResultsPage
	 * @throws Exception
	 */
	public BusResultsPage searchBuses() throws Exception {
		try {
			browser.click("search_Bus_Button");
			browser.findElement(By.cssSelector("div.dAUADy"));
			browser.scrollToPageBottom(200);
			browser.scrollToPageTop();
		} catch (Exception e) {
			throw new Exception("Could not search for the Buses", e);
		}
		infoLogger("Search Bus button was clicked successfully");
		return new BusResultsPage();
	}
}
