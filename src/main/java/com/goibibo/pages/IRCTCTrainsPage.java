package com.goibibo.pages;

import static utilities.Log.warn;

import org.openqa.selenium.By;

import utilities.Generic;

public class IRCTCTrainsPage extends Page {
	/**
	 * This method is used to set the source station
	 * 
	 * @param source - String containing the source station
	 * @throws Exception
	 */
	public void enterSourceStation(String source) throws Exception {
		try {
			browser.click("train_From_Textbox");
			browser.sendkeys("train_From_Textbox", source);
			browser.click(By.xpath("//li[@role='option']//span[contains(text(),'" + source + "')]"));
		} catch (Exception e) {
			throw new Exception("Could not set \"" + source + "\" as the source station", e);
		}
	}

	/**
	 * This method is used to set the destination station
	 * 
	 * @param source - String containing the destination station
	 * @throws Exception
	 */
	public void enterDestinationStation(String destination) throws Exception {
		try {
			browser.sendkeys("train_To_Textbox", destination);
			browser.click(By.xpath("//li[@role='option']//span[contains(text(),'" + destination + "')]"));
		} catch (Exception e) {
			throw new Exception("Could not set \"" + destination + "\" as the destination station", e);
		}
	}

	/**
	 * This method is used to set the journey date
	 * 
	 * @param date   - date of journey
	 * @param format - format of the date provided
	 * @throws Exception
	 */
	public void setJourneyDate(String date, String format) throws Exception {
		try {
			browser.click("train_Journey_Date");
			date = Generic.changeDateFormat(date, format, "d-MMMM yyyy");
			String expectedD = date.split("-")[0];
			String expectedMY = date.split("-")[1];
			String actualMY;
			while (!(actualMY = browser.getText(browser.findElement(By.className("DayPicker-Caption"))))
					.equalsIgnoreCase(expectedMY)) {
				browser.click(By.cssSelector("span[aria-label='Next Month']"));
			}
			if (actualMY.equalsIgnoreCase(expectedMY))
				browser.click(By.xpath("//div[contains(@class,'DayPicker-Day') and text()='" + expectedD + "']"));
			else
				throw new Exception("Could not reach the desired travel month");
		} catch (Exception e) {
			throw new Exception("Could not select \"" + date + "\" as the journey date");
		}
	}

	/**
	 * This method is used to search for the trains by clicking on the Search Trains
	 * Button
	 * 
	 * @throws Exception
	 */
	public void searchTrains() throws Exception {
		try {
			browser.click("search_Button");
			browser.findElement(By.className("srpCardWrap"));
		} catch (Exception e) {
			try {
				browser.findElement(By.className("noTrainsFoundCard"));
			} catch (Exception e2) {
				e2.printStackTrace();
				warn("No trains are available for the given trip");
			}
		}
	}

	/**
	 * This method is used to open the train running status page
	 * 
	 * @return - in instance of the checkTrainRunningStatus class
	 * @throws Exception
	 */
	public TrainRunningStatusPage checkTrainRunningStatus() throws Exception {
		try {
			browser.click("train_Running_Status_Link");
			browser.findElement(By.xpath("//h1[text()='Train Running Status']"));
			return new TrainRunningStatusPage();
		} catch (Exception e) {
			throw new Exception("Unable to open the page 'Train Running Status'");
		}
	}
}
