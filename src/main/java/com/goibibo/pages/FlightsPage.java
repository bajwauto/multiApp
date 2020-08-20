package com.goibibo.pages;

import static utilities.Log.error;
import static utilities.Log.info;

import org.openqa.selenium.By;

import utilities.Generic;

public class FlightsPage extends Page {
	/**
	 * This method is used to select the type of trip i.e One way/Round
	 * trip/Multicity
	 * 
	 * @param tripType - type of trip(One way/Round trip/Multicity)
	 */
	public void selectTripType(String tripType) {
		try {
			switch (tripType.toLowerCase()) {
			case "oneway":
			case "one way":
				browser.click("oneway_Button");
				break;
			case "round trip":
			case "roundtrip":
				browser.click("roundTrip_Button");
				break;
			case "multicity":
			case "multi city":
				browser.click("multiCity_Button");
				break;
			default:
				error("Invalid trip type - " + tripType);
				throw new Exception("Invalid trip type - " + tripType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			error("Unable to select the trip type \"" + tripType + "\"");
		}
		info("Selected the trip type - " + tripType);
	}

	/**
	 * This method is used to select the source city of the trip
	 * 
	 * @param sourceCity - the source city
	 */
	public void selectSourceCity(String sourceCity) {
		try {
			browser.sendkeys("from_Textbox", sourceCity);
			browser.click(By.xpath("//li[@role='option']//span[contains(text(),'" + sourceCity + "')]"));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not set \"" + sourceCity + "\" as the source city of the trip");
		}
		info("Selected the Source city - " + sourceCity);
	}

	/**
	 * This method is used to select the destination city of the trip
	 * 
	 * @param destinationCity - the destination city
	 */
	public void selectDestinationCity(String destinationCity) {
		try {
			browser.sendkeys("to_Textbox", destinationCity);
			browser.click(By.xpath("//li[@role='option']//span[contains(text(),'" + destinationCity + "')]"));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not set \"" + destinationCity + "\" as the destination city of the trip");
		}
		info("Selected the Destination city - " + destinationCity);
	}

	private void setCalendarDate(String date, String format) throws Exception {
		String dateToSet = Generic.changeDateFormat(date, format, "d-MMMM yyyy");
		String actualMY;
		String expectedD = dateToSet.split("-")[0];
		String expectedMY = dateToSet.split("-")[1];
		while (!(actualMY = browser.getText(browser.findElement(By.cssSelector("div.DayPicker-Caption"))))
				.equalsIgnoreCase(expectedMY)) {
			browser.click("nextMonth_Button");
		}
		if (!actualMY.equalsIgnoreCase(expectedMY)) {
			error("Could not navigate to the desired Month in the calendar");
			throw new Exception("Could not navigate to the desired Month in the calendar");
		} else {
			browser.click(By.xpath("//div[@class='DayPicker-Day']/div[text()='" + expectedD + "']"));
		}
	}

	/**
	 * This method is used to set the departure date of the trip
	 * 
	 * @param date   - date to be set as the trip's departure date
	 * @param format - provided departure date format
	 */
	public void setDepartureDate(String date, String format) {
		try {
			browser.click("departure_Calendar");
			setCalendarDate(date, format);
		} catch (Exception e) {
			e.printStackTrace();
			error("Could not set \"" + date + "\" as the departure date");
		}
		info("Selected the Deprature Date - " + date);
	}

	/**
	 * This method is used to set the return date of the trip
	 * 
	 * @param date   - date to be set as the trip's return date
	 * @param format - provided return date format
	 */
	public void setReturnDate(String date, String format) {
		try {
			browser.click("return_Calendar");
			setCalendarDate(date, format);
		} catch (Exception e) {
			e.printStackTrace();
			error("Could not set \"" + date + "\" as the return date");
		}
		info("Selected the Return Date - " + date);
	}

	/**
	 * This method is used to set the number of adult travelers in the trip
	 * 
	 * @param noOfAdults - no. of adults traveling
	 */
	public void setAdultTravellers(int noOfAdults) {
		try {
			String currentAdultCount;
			browser.click("travellers_Element");
			while (!(currentAdultCount = browser.getAttribute(browser.FindElement("adult_Textbox"), "value"))
					.equalsIgnoreCase(noOfAdults + "")) {
				browser.click("addAdult_Element");
			}
		} catch (Exception e) {
			e.printStackTrace();
			error("Unable to set the number of adult travellers to " + noOfAdults);
		}
		info("No. of adult travellers set to " + noOfAdults);
	}

	/**
	 * This method is used to set the number of adult travelers in the trip
	 * 
	 * @param noOfAdults - no. of adults traveling
	 */
	public void setChildTravellers(int noOfChildren) {
		try {
			String currentChildrenCount;
			browser.click("travellers_Element");
			while (!(currentChildrenCount = browser.getAttribute(browser.FindElement("child_Textbox"), "value"))
					.equalsIgnoreCase(noOfChildren + "")) {
				browser.click("addChild_Element");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Unable to set the number of child travellers to " + noOfChildren);
		}
		info("No. of children set to " + noOfChildren);
	}

	/**
	 * This method is used to set the number of infant travelers in the trip
	 * 
	 * @param noOfAdults - no. of infants traveling
	 */
	public void setInfantTravellers(int noOfInfants) {
		try {
			String currentInfantCount;
			browser.click("travellers_Element");
			while (!(currentInfantCount = browser.getAttribute(browser.FindElement("infant_Textbox"), "value"))
					.equalsIgnoreCase(noOfInfants + "")) {
				browser.click("addInfant_Element");
			}
		} catch (Exception e) {
			e.printStackTrace();
			error("Unable to set the number of Infant travellers to " + noOfInfants);
		}
		info("No. of Infants set to " + noOfInfants);
	}

	/**
	 * This method is used to select the travel Class
	 * 
	 * @param travelClass - travel class like Economy/Business/First class/Premium
	 *                    Economy
	 */
	public void selectTravelClass(String travelClass) {
		try {
			browser.selectOptionFromList(browser.FindElement("travelClass_List"), travelClass);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not select \"" + travelClass + "\" as the travel class");
		}
		info("Selected the traveller class - " + travelClass);
	}

	/**
	 * This method is used to click on the Search Button
	 */
	public void searchFlights() {
		try {
			browser.click("search_Button");
			browser.findElement(By.cssSelector("div[data-cy^='flightItem_']"));
		} catch (Exception e) {
			e.printStackTrace();
			error("Could not search flights");
		}
		info("Flight search was successful");
	}
}
