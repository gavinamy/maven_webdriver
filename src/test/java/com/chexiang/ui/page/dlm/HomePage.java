package com.chexiang.ui.page.dlm;

import com.chexiang.ui.page.dlm.scene.Operator;

public class HomePage extends DlmPage {

	public void logIn(Operator role) {
		openBrowser(uiCase.GlobalConst.get("browser"));
		enterSystem(role);

	}

	public void logOutAndIn(Operator role) {
		close();
		enterSystem(role);
	}

	private void enterSystem(Operator operator) {
		webElement("_account").setValue(operator.getId());
		webElement("_password").setDriverData();
		webElement("submit").click();
		webElement("loginSelectBtn").click();
	}

	public void switchAccount(String role) {
		webElement("userName").mouseMoveOn();
		webElement("功能菜单", "切换账号").click();
		webElement("登录角色", role).click();
	}

	public void proxyLogIn(String user, String role) {
		webElement("userName").mouseMoveOn();
		webElement("功能菜单", "代理登录").click();
		select("proxyLoginTable_length").setValue("20");
		webElement("代理按钮", user, role).click();
	}

	@Override
	protected void initMenu() {
	}
}