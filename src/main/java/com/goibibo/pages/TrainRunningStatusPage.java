package com.goibibo.pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class TrainRunningStatusPage extends Page {
	/**
	 * This method is used to enter the train name or number to get the live running
	 * status
	 * 
	 * @param nameOrNumber - name or number of the train whose live status is
	 *                     desired
	 * @throws Exception
	 */
	public void enterTrainName(String nameOrNumber) throws Exception {
		String tClass;
		try {
			long number = Long.parseLong(nameOrNumber);
			tClass = "trainNumber";
		} catch (NumberFormatException nfe) {
			tClass = "trainNameNumber";
		}
		try {
			browser.sendkeys("train_Number_Textbox", nameOrNumber);
			while (true) {
				try {
					browser.sendkeys("train_Number_Textbox", Keys.chord(Keys.ARROW_DOWN));
					browser.driver().findElement(By.cssSelector("span." + tClass));
					break;
				} catch (Exception infinite) {
					// do nothing
				}
			}
			WebElement element = browser.findElement(By.cssSelector("span." + tClass));
			if (browser.getText(element).contains(nameOrNumber))
				browser.click(element);
			else
				throw new Exception("No option available corresponding to train " + nameOrNumber);
		} catch (Exception e) {
			throw new Exception("Could not set \"" + nameOrNumber + "\" as the train name or number");
		}
		infoLogger("The train name/number(" + nameOrNumber + ") was entered");
	}

	/**
	 * This method is used to select the station
	 * 
	 * @param value - text or value of the station
	 * @throws Exception
	 */
	public void selectStation(String value) throws Exception {
		try {
			browser.selectOptionFromList(browser.findElement("station_Dropdown"), value);
		} catch (Exception e) {
			throw new Exception("Could not set \"" + value + "\" as the Station");
		}
		infoLogger("Selected the station - " + value);
	}

	/**
	 * This method is used to select the depart day
	 * 
	 * @param day - possible values are yesterday/today/tomorrow
	 * @throws IOException
	 */
	public void selectDepartDay(String day) throws Exception {
		try {
			switch (day.toLowerCase().trim()) {
			case "yesterday":
				browser.click("yesterday_Status_Link");
				break;
			case "tomorrow":
				browser.click("tomorrow_Status_Link");
				break;
			case "today":
			default:
				browser.click("today_Status_Link");
			}
		} catch (Exception e) {
			throw new Exception("Could not set \"" + day + "\" as the Depart Day");
		}
		infoLogger("Day selected: " + day);
	}

	/**
	 * This method is used to fetch the live running status of the given train by
	 * clicking on the get status button
	 * 
	 * @throws Exception
	 */
	public void getTrainStatus() throws Exception {
		try {
			browser.click("get_Status_Button");
			infoLogger("The Get train status button was clicked");
			browser.waitForPageToGetLoaded();
			browser.findElement(By.id("train_details"));
		} catch (Exception e) {
			throw new Exception("Could not fetch the train's live status");
		}
	}
}
