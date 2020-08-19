package com.goibibo.pages;

import utilities.Browser;

public class TopMenu {
	Browser browser;

	public TopMenu() {
		browser = Browser.getInstance();
	}

	/**
	 * This method is used to perform click operation on a menu item(like Flights,
	 * Casb, Visa etc) from the top menu
	 * 
	 * @param menuItem - menu item to be clicked
	 */
	private void gotoMenu(String menuItem) {
		try {
			switch (menuItem.toLowerCase()) {
			case "flights":
				browser.click("flights_Link");
				break;
			case "hotels":
				browser.click("hotels_Link");
				break;
			case "bus":
				browser.click("bus_Link");
				break;
			case "irctc trains":
			case "irctctrains":
				browser.click("irctcTrains_Link");
				break;
			case "cabs":
				browser.click("cabs_Link");
				break;
			case "visa":
				browser.click("visa_Link");
				break;
			case "go stays hotels":
			case "gostayshotels":
				browser.click("goStaysHotels_Link");
				break;
			}
		} catch (Exception e) {
			System.err.println("Unable to click on Top menu item - " + menuItem);
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to click on the Flights Menu Item
	 * 
	 * @return - reference to the FlightsPage Instance
	 */
	public FlightsPage openFlightsMenu() {
		gotoMenu("Flights");
		return new FlightsPage();
	}

	/**
	 * This method is used to click on the Hotels Menu Item
	 * 
	 * @return - reference to the HotelsPage Instance
	 */
	public HotelsPage openHotelsMenu() {
		gotoMenu("Hotels");
		return new HotelsPage();
	}

	/**
	 * This method is used to click on the Go Stays Hotels Menu Item
	 * 
	 * @return - reference to the GoStaysHotelsPage Instance
	 */
	public GoStaysHotelsPage openGoStaysHotelsMenu() {
		gotoMenu("Go Stays Hotels");
		return new GoStaysHotelsPage();
	}

	/**
	 * This method is used to click on the Bus Menu Item
	 * 
	 * @return - reference to the BusPage Instance
	 */
	public BusPage openBusMenu() {
		gotoMenu("Bus");
		return new BusPage();
	}

	/**
	 * This method is used to click on the IRCTC Trains Menu Item
	 * 
	 * @return - reference to the IRCTCTrainsPage Instance
	 */
	public IRCTCTrainsPage openIRCTCTrainsMenu() {
		gotoMenu("IRCTC Trains");
		return new IRCTCTrainsPage();
	}

	/**
	 * This method is used to click on the Cabs Menu Item
	 * 
	 * @return - reference to the CabsPage Instance
	 */
	public CabsPage openCabsMenu() {
		gotoMenu("Cabs");
		return new CabsPage();
	}

	/**
	 * This method is used to click on the Visa Menu Item
	 * 
	 * @return - reference to the VisaPage Instance
	 */
	public VisaPage openVisaMenu() {
		gotoMenu("Visa");
		return new VisaPage();
	}
}
