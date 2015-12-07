package com.chexiang.ui.page.dlm;

//意向管理
public class SearchOppor extends DlmPage {
	public void failApply() {
		initMenu();
		webElement("custName").setParam("潜客名称");
		webElement("searchOppor").click();
		webElement("战败复选框", caseParam.get("潜客名称")).select();
		webElement("addOpporFail").click();
		failMsg();
	}

	@Override
	protected void initMenu() {
		menu("客户管理", "意向管理");
	}
}