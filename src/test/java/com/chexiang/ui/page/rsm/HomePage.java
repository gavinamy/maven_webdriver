package com.chexiang.ui.page.rsm;

public class HomePage extends RsmPage {
	public void logIn(String username) {
		openBrowser(uiCase.GlobalConst.get("browser"));
		webElement("username").setValue(username);
		webElement("password").setDriverData();
		webElement("submit").click();
	}
}