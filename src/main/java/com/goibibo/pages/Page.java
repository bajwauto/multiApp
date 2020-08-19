package com.goibibo.pages;

import utilities.Browser;

public class Page {
	public Browser browser;
	public TopMenu topMenu;

	public Page() {
		browser = Browser.getInstance();
		topMenu = new TopMenu();
	}
}
